/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.networkcode;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author sonoflight
 */
public class Reception implements Runnable {

    private final BufferedReader in;
/**
 *
 * @param i
 */
    public Reception(final BufferedReader i) {
        in = i;
    }
    @Override
    public final void run() {
        while (true) {

            try {
                System.out.println("Msg client :" + in.readLine());

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
