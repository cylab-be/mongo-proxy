/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongowireprotocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author sonoflight
 */
public final class Main {
    private static final int PORT = 9632;
    private static final int PORT_DB = 27017;
    private Main() { }
    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        try {

            // Create sockets...
            ServerSocket socket =  new ServerSocket(PORT);
            Socket client = socket.accept();
            System.out.println("client connection reussie!");

            InputStreamReader client_in = new InputStreamReader(client.getInputStream());
            PrintStream client_out = new PrintStream(client.getOutputStream());

            // Read from client
            int lentgh_1 = client_in.read();
            int lentgh_2 = client_in.read();

            System.out.println("Lengths : " + lentgh_1 + " - " + lentgh_2);
            //System.out.println("Msg client :" + msg);

            /*Socket srv_socket = new Socket("127.0.0.1", PORT_DB);
            System.out.println("connection reussie!");
            PrintWriter srv_out = new PrintWriter(srv_socket.getOutputStream());
            //srv_out.println(msg);
            System.out.println("msg envoyé");
            BufferedReader srv_int = new BufferedReader(
                        new InputStreamReader(srv_socket.getInputStream()));
            String db_msg = srv_int.readLine();
            client_out.println(db_msg);
            System.out.println("DB msg :" + db_msg);
            client_out.println(db_msg);*/
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
