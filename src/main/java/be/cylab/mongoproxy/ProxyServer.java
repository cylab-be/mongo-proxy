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

/**
 *
 * @author tibo
 */
public class ProxyServer {

    private final int port;

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
                new Thread(new ConnectionHandler(client)).start();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}


/**
 * @author sonoflight
 */
class ConnectionHandler implements Runnable {

    private static final int PORT_DB = 27017;

    private final Socket client;

    ConnectionHandler(final Socket client) {
        this.client = client;
        opcodes.put(1, "OP_REPLY");
        /**
         * ...
         */
    }

    private final HashMap<Integer, String> opcodes = new HashMap<>();

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

    public String getOpcodeName(final int opcode) {
        // String op = opcodes.get(opcode);
        // System.out.println(op);
        //view of type of request
        switch (opcode) {
            case 1:
                return "OP_REPLY";

            case 2001:
                return "OP_UPDATE";

            case 2002:
                return "OP_INSERT";

            case 2003:
                return "RESERVED";

            case 2004:
                return "OP_QUERY";

            case 2005:
                return "OP_GET_MORE";

            case 2006:
                return "OP_DELETE";

            case 2007:
                return "OP_KILL_CURSORS";

            case 2010:
                return "OP_COMMAND";

            case 2011:
                return "OP_COMMANDREPLY";

            case 2013:
                return "OP_MSG";

            default:
                return "Unknown";
        }

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

    public static String readString(final byte[] msg, final int start) {
        return readCString(msg, start + 4);
    }

    public void processQuery(final byte[] msg) {

        String collection = readCString(msg, 20);
        System.out.println("Collection: " + collection);

        Document doc = new Document(msg, 29 + collection.length());
        System.out.println("Document: " + doc);
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

    protected void processInsert(final byte[] msg) {

    }
}