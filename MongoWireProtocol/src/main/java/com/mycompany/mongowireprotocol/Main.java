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
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author sonoflight
 */
public class Main {
private static final int PORT = 9632;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerSocket socket =  new ServerSocket(PORT);
            Socket client = socket.accept();
            
            BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
            PrintStream db = new PrintStream(client.getOutputStream());
            String msg;
            
            
            Socket com = new Socket ("127.0.0.1",27017);
            System.out.println("connection reussie!");
            
            PrintStream out = new PrintStream(com.getOutputStream());
            
            while ((msg = in.readLine()) != null) {
                System.out.println("Msg client :" + msg);
                out.println(msg);
            
            };
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
