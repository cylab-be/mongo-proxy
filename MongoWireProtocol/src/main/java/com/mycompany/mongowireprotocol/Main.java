/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongowireprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author sonoflight
 */
public final class Main {

    private static final int PORT = 9632;

    private Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        try {

            // Wait for client connection...
            ServerSocket socket = new ServerSocket(PORT);

            while (true) {
                Socket client = socket.accept();
                System.out.println("Connected");
                new Thread(new ConnectionHandler(client)).start();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Bye!");
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
        /** ... */
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
                System.out.println("Read from client...");
                byte[] msg = readMessage(client_in);

                //Get opcode
                //consersion of 4bytes to a single int
                final int opcode = (msg[15] << 24) & 0xff000000
                        | (msg[14] << 16) & 0x00ff0000
                        | (msg[13] << 8) & 0x0000ff00
                        | (msg[12]) & 0x000000ff;


                System.out.println("Opcode: " + opcode);

                //System.out.println("Write same message to server");
                srv_out.write(msg);

                //System.out.println("Read from server");
                byte[] response = readMessage(srv_in);

                // String op = opcodes.get(opcode);
                // System.out.println(op);

                // QUERY
                if (opcode == 2004) {
                    extractMsg(msg);
                }


                //view of type of request
                switch (opcode) {
                    case 1:
                        System.out.println("OP_REPLY");
                        break;
                    case 2001:
                        System.out.println("OP_UPDATE");
                        break;
                    case 2002:
                        System.out.println("OP_INSERT");
                        break;
                    case 2003:
                        System.out.println("RESERVED");
                        break;
                    case 2004:
                        System.out.println("OP_QUERY");

                        break;
                    case 2005:
                        System.out.println("OP_GET_MORE");
                        break;
                    case 2006:
                        System.out.println("OP_DELETE");
                        break;
                    case 2007:
                        System.out.println("OP_KILL_CURSORS");
                        break;
                    case 2010:
                        System.out.println("OP_COMMAND");
                        break;
                    case 2011:
                        System.out.println("OP_COMMANDREPLY");
                        break;
                    case 2013:
                        System.out.println("OP_MSG");
                        break;
                    default:
                        System.out.println("request unknown!");

                }

                client_out.write(response);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void extractMsg(final byte[] msg) {

        //Determination of the length of the collection
        //name in the OP_QUERY msg
        int i = 20; //start position of cstring
        char t = 0x00;
        while (msg[i] != t) {
            System.out.println((char) msg[i]);

            i++;
        }

        int name_collection_length = i - 20;

        int l = 0;

        char[] name_collection = new char[name_collection_length];

        System.out.println("Create msg");

        while (l < name_collection_length) {
            name_collection[l] = (char) msg[20 + l];
            l++;
        }

        System.out.println("collection name : "
                + Arrays.toString(name_collection));
        System.out.println("End of name collection extract.");

        //Get documcument in msg
        System.out.println("extract document...");
        int j = 20 + name_collection_length + 8; //start position of Document

        final int document_lenght = (msg[j + 3] << 24) & 0xff000000
                | (msg[j + 2] << 16) & 0x00ff0000
                | (msg[j + 1] << 8) & 0x0000ff00
                | (msg[j]) & 0x000000ff;
        System.out.println("lenght document : " + document_lenght);
        byte[] document = new byte[document_lenght];
        int k;
        for (k = 0; k < document_lenght; k++) {
            document[0] = msg[j + k];
        }
        String doc = Arrays.toString(document);
        System.out.append("document : " + doc);

    }

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
        System.out.println("Message length: " + msg_length);

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
}
