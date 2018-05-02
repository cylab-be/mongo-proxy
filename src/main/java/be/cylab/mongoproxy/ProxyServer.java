/*
 * The MIT License
 *
 * Copyright 2018 tibo.
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author tibo
 */
public class ProxyServer {

    private final int port;
    private final HashMap<String, LinkedList<Listener>> listeners
            = new HashMap<>();

    /**
     *
     * @param port
     */
    public ProxyServer(final int port) {
        this.port = port;
    }

    /**
     * Run forever.
     */
    public final void run() {

        try {

            // Wait for client connection...
            ServerSocket socket = new ServerSocket(port);

            while (true) {
                Socket client = socket.accept();
                System.out.println("Connected");
                new Thread(new ConnectionHandler(client, listeners)).start();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     *
     * @param collection
     * @param listener
     */
    public final void addListener(final String collection,
            final Listener listener) {
        LinkedList<Listener> collection_listeners
                = listeners.getOrDefault(collection, new LinkedList<>());

        collection_listeners.add(listener);
        listeners.put(collection, collection_listeners);
    }

}

/**
 * @author sonoflight
 */
class ConnectionHandler implements Runnable {

    private static final int PORT_DB = 27017;

    private final Socket client;
    private final HashMap<String, LinkedList<Listener>> listeners;

    /**
     *
     * @param client
     * @param listeners
     */
    ConnectionHandler(final Socket client, final HashMap<String, LinkedList<
            Listener>> listeners) {
        this.client = client;
        this.listeners = listeners;
    }

    @Override
    public final void run() {

        try {
            InputStream client_in = client.getInputStream();
            OutputStream client_out = client.getOutputStream();

            // Connect to server
            Socket srv_socket = new Socket("127.0.0.1", PORT_DB);
            OutputStream srv_out = srv_socket.getOutputStream();
            InputStream srv_in = srv_socket.getInputStream();

            while (true) {
                // System.out.println("Read from client...");
                byte[] msg = readMessage(client_in);

                int opcode = readInt(msg, 12);
                System.out.println("Opcode: " + getOpcodeName(opcode));

                if (opcode == 2004) {
                    processQuery(msg);
                }

                if (opcode == 2002) {
                    processInsert(msg);
                }

                //System.out.println("Write same message to server");
                srv_out.write(msg);

                //System.out.println("Read from server");
                byte[] response = readMessage(srv_in);

                client_out.write(response);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Get the name of this opcode.
     *
     * @param opcode
     * @return
     */
    public String getOpcodeName(final int opcode) {
        return OpCode.findByValue(opcode).name();

    }

    public static final char STRING_TERMINATION = 0x00;

    /**
     * Read a string from the byte array.
     *
     * @param msg
     * @param start
     * @return
     */
    public static String readCString(final byte[] msg, final int start) {
        StringBuilder string = new StringBuilder();
        int i = start;
        while (msg[i] != STRING_TERMINATION) {
            string.append((char) msg[i]);
            i++;
        }

        return string.toString();
    }

    /**
     *
     * @param msg
     * @param start
     * @return
     */
    public static String readString(final byte[] msg, final int start) {
        return readCString(msg, start + 4);
    }

    /**
     *
     * @param msg
     */
    public void processQuery(final byte[] msg) {

        //get collection name to run listner if find
        String collection_name = readCString(msg, 20);
        System.out.println("Collection: " + collection_name);

        //get documment in msg 
        Document doc = new Document(msg, 29 + collection_name.length());
        System.out.println("Document: " + doc);

        //find collection in the liste of listners
        LinkedList<Listener> collection_listeners = listeners.get(
                collection_name);

        if (collection_listeners == null) {
            return;
        }

        for (Listener listener : collection_listeners) {
            listener.run(doc);
            System.out.println("listner running...");

        }
    }

    /**
     * Read the complete message to a Byte array.
     *
     * @param stream
     * @return
     * @throws IOException
     * @throws Exception
     */
    public byte[] readMessage(final InputStream stream)
            throws IOException, Exception {

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
        final int msg_length = lentgh_1 + lentgh_2 * 256
                + lentgh_3 * 256 * 256 + lentgh_4 * 256 * 256 * 256;
        //System.out.println("Message length: " + msg_length);

        // 2. content of message
        byte[] msg = new byte[msg_length];
        int offset = 4;
        while (offset < msg_length) {
            //read the stream and skip the 4 first bytes
            int tmp = stream.read(msg, offset, (msg_length - offset));
            offset += tmp;
        }

        // 3. Fill 4 first Bytes
        msg[0] = (byte) lentgh_1;
        msg[1] = (byte) lentgh_2;
        msg[2] = (byte) lentgh_3;
        msg[3] = (byte) lentgh_4;

        return msg;
    }

    /**
     * Consersion of 4bytes to a single int.
     *
     * @param bytes
     * @param start
     * @return
     */
    protected static int readInt(final byte[] bytes, final int start) {

        return (bytes[start + 3] << 24) & 0xff000000
                | (bytes[start + 2] << 16) & 0x00ff0000
                | (bytes[start + 1] << 8) & 0x0000ff00
                | (bytes[start]) & 0x000000ff;
    }

    /**
     *
     * @param msg
     */
    protected void processInsert(final byte[] msg) {

    }
}
