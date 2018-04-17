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
            ServerSocket socket =  new ServerSocket(PORT);
            Socket client = socket.accept();
            System.out.println("client connection reussie!");
            BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
            PrintStream db = new PrintStream(client.getOutputStream());
            String msg = in.readLine();
            System.out.println("Msg client :" + msg);
            Socket com = new Socket("127.0.0.1", PORT_DB);
            System.out.println("connection reussie!");
            PrintWriter out = new PrintWriter(com.getOutputStream());
            out.println(msg);
            System.out.println("msg envoyé");
            BufferedReader in_com = new BufferedReader(
                        new InputStreamReader(com.getInputStream()));
            String db_msg = in_com.readLine();
            db.println(db_msg);
            System.out.println("DB msg :" + db_msg);
            db.println(db_msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
