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

    private Main() { }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        try {

            // Wait for client connection...
            ServerSocket socket =  new ServerSocket(PORT);

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


class ConnectionHandler implements Runnable {

    private static final int PORT_DB = 27017;

    private final Socket client;

    public ConnectionHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            InputStream client_in = client.getInputStream();
            OutputStream client_out = client.getOutputStream();

            // Connect to server
            Socket srv_socket = srv_socket = new Socket("127.0.0.1", PORT_DB);
            OutputStream srv_out = srv_out = srv_socket.getOutputStream();
            InputStream srv_in = srv_socket.getInputStream();

            while (true) {
                System.out.println("Read from client...");
                byte[] msg = readMessage(client_in);

                System.out.println("Write same message to server");
                srv_out.write(msg);

                System.out.println("Read from server");
                byte[] response = readMessage(srv_in);

                client_out.write(response);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public byte[] readMessage(InputStream stream) throws IOException {
        // 1. length of message
        int lentgh_1 = stream.read();
        int lentgh_2 = stream.read();
        int lentgh_3 = stream.read();
        int lentgh_4 = stream.read();
        int msg_length = lentgh_1 + lentgh_2 * 256 + lentgh_3 * 256 * 256 + lentgh_4 * 256 * 256 * 256;
        System.out.println("Message length: " + msg_length);

        // 2. content of message
        byte[] msg = new byte[msg_length];
        int offset = 4;
        while (offset < msg_length) {
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
