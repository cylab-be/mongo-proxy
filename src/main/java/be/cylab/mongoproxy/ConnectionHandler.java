/*
 * The MIT License
 *
 * Copyright 2018 sonoflight.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package be.cylab.mongoproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sonoflight
 */
class ConnectionHandler implements Runnable {

    private final int port_db;
    private final String ip;
    private final Logger logger = LoggerFactory.getLogger(
            ConnectionHandler.class);
    private final Socket client;
    private final HashMap<String, LinkedList<Listener>> listeners;

    ConnectionHandler(
            final Socket client, final String ip, final int port_db,
            final HashMap<String, LinkedList<Listener>> listeners) {
        this.client = client;
        this.listeners = listeners;
        this.ip = ip;
        this.port_db = port_db;
    }

    @Override
    public final void run() {
        try {
            InputStream client_in = client.getInputStream();
            OutputStream client_out = client.getOutputStream();
            // Connect to server
            Socket srv_socket = new Socket(ip, port_db);
            OutputStream srv_out = srv_socket.getOutputStream();
            InputStream srv_in = srv_socket.getInputStream();
            while (true) {
                byte[] msg = readMessage(client_in);
                int opcode = Helper.readInt(msg, 12);
                logger.info("Opcode: {}", OpCode.getOpcodeName(opcode));
                if (opcode == 2004) {
                    processQuery(msg);
                }
                srv_out.write(msg);
                byte[] response = readMessage(srv_in);
                client_out.write(response);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * process the OP_QUERY request.
     *
     * in the documentation of the MONGO wire protocol, the reading of the
     * collection name manipulated begin at the 20th byte, but because of the
     * version of the MONGO driver we use (3.5.0) the series of bytes
     * corresponds rather to the name of the DB and the name of the collection
     * manipulated is contained in the document.
     *
     * @param msg byte array which contain the message.
     */
    public void processQuery(final byte[] msg) {
        //get collection name to run listner if find
        String collection_name = Helper.readCString(msg, 20);
        //get documment in msg
        Document doc = new Document(msg, 29 + collection_name.length());
        System.out.println("doc: " + doc.toString());
        //check if the first part of the document is ElementString
        Boolean is_string = doc.get(0).isString();
        if (is_string) {
            //create key of the listener
            String collection_request = collection_name + doc.get(0).value();
            LinkedList<Listener> collection_listeners = listeners.get(
                    collection_request);
            for (Listener listener : collection_listeners) {
                listener.run(doc);
                logger.info("listner running...");
            }
        }
        logger.debug("Document: {}", doc.toString());
        logger.debug("collection name: {}", collection_name);
    }

    /**
     * Read the complete message to a Byte array.
     *
     * @param stream stream were the message is extract to a byte array.
     * @return a Byte array.
     * @throws IOException
     * @throws Exception
     */
    public byte[] readMessage(
            final InputStream stream) throws IOException, Exception {
        if (stream == null) {
            throw new Exception("Stream is null!");
        }
        // https://docs.mongodb.com/manual/reference/mongodb-wire-protocol/
        // Header =
        // int32 = 4 Bytes = 32 bits
        // 1. length of message
        int lentgh_1 = stream.read();
        int lentgh_2 = stream.read();
        int lentgh_3 = stream.read();
        int lentgh_4 = stream.read();
        // Value is little endian:
        final int msg_length = lentgh_1
                + lentgh_2 * 256 + lentgh_3 * 256 * 256
                + lentgh_4 * 256 * 256 * 256;
        // 2. content of message
        byte[] msg = new byte[msg_length];
        int offset = 4;
        while (offset < msg_length) {
            //read the stream and skip the 4 first bytes
            int tmp = stream.read(msg, offset, msg_length - offset);
            offset += tmp;
        }
        // 3. Fill 4 first Bytes
        msg[0] = (byte) lentgh_1;
        msg[1] = (byte) lentgh_2;
        msg[2] = (byte) lentgh_3;
        msg[3] = (byte) lentgh_4;
        return msg;
    }

}
