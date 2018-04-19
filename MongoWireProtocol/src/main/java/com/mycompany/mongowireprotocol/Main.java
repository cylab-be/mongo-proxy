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

    //declaration of numbers used for spacifie byte position in msg
    private static final int BYTEPOSITION_12 = 12;
    private static final int BYTEPOSITION_13 = 13;
    private static final int BYTEPOSITION_14 = 14;
    private static final int BYTEPOSITION_15 = 15;
    private static final int BYTEPOSITION_0 = 0;
    private static final int BYTEPOSITION_1 = 1;
    private static final int BYTEPOSITION_2 = 2;
    private static final int BYTEPOSITION_3 = 3;
    private static final int BYTEPOSITION_4 = 4;
    private static final int BYTEPOSITION_5 = 5;
    private static final int BYTEPOSITION_6 = 6;
    private static final int BYTEPOSITION_7 = 7;
    private static final int BYTEPOSITION_8 = 8;
    private static final int BYTEPOSITION_16 = 16;
    private static final int BYTEPOSITION_24 = 24;

    private static final int BYTE_1 = 0x000000ff;
    private static final int BYTE_2 = 0x0000ff00;
    private static final int BYTE_3 = 0x00ff0000;
    private static final int BYTE_4 = 0xff000000;

    private final Socket client;

    ConnectionHandler(final Socket client) {
        this.client = client;
    }

    @Override
    public final void run() {
        if (new ConnectionHandler(client) == null) {
        } else {

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
                    final int opcode = (msg[BYTEPOSITION_15] << BYTEPOSITION_24) & BYTE_4
                            | (msg[BYTEPOSITION_14] << BYTEPOSITION_16) & BYTE_3
                            | (msg[BYTEPOSITION_13] << BYTEPOSITION_8) & BYTE_2
                            | (msg[BYTEPOSITION_12] << 0) & BYTE_1;

                    System.out.println("Byte 12 : " + msg[BYTEPOSITION_12]);
                    System.out.println("Opcode: " + opcode);

                    //System.out.println("Write same message to server");
                    srv_out.write(msg);

                    //System.out.println("Read from server");
                    byte[] response = readMessage(srv_in);

                    client_out.write(response);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public byte[] readMessage(final InputStream stream) throws IOException {

        if (stream == null) {
            System.out.println("no stream detect!");
        } else {
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
            int offset = BYTEPOSITION_4;
            while (offset < msg_length) {
                int tmp = stream.read(msg, offset, (msg_length - offset));
                offset += tmp;
            }

            // 3. Fill 4 first Bytes
            msg[BYTEPOSITION_0] = (byte) lentgh_1;
            msg[BYTEPOSITION_1] = (byte) lentgh_2;
            msg[BYTEPOSITION_2] = (byte) lentgh_3;
            msg[BYTEPOSITION_3] = (byte) lentgh_4;

            return msg;
        }
        return null;
    }
}
