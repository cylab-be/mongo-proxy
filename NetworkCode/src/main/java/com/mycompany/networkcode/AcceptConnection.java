/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.networkcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author sonoflight
 */
public class AcceptConnection implements Runnable {
    private final ServerSocket server;
    private Socket socket;
    private BufferedReader in;
    private PrintStream out;
    private Scanner sc;
    /**
     * @param s
     */
    public AcceptConnection(final ServerSocket s) {
        server = s;
    }

    @Override
    public final void run() {
        try {
            while (true) {
                socket = server.accept();
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintStream(socket.getOutputStream());
                Thread t = new Thread(new Reception(in));
                t.start();
                out.println("well connect!");
                while (true) {
                    sc = new Scanner(System.in);
                    System.out.println("Votre msg : ");
                    String str = sc.nextLine();
                    out.println(str);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
