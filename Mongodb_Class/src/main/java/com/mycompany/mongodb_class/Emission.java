/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongodb_class;

import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 * @author sonoflight
 */
public class Emission implements Runnable {
    private PrintStream out;
    private Scanner sc;
    /**
     *
     * @param o
     */
    public Emission(final PrintStream o) {
        out = o;
    }
    @Override
    public final void run() {
        sc = new Scanner(System.in);
        while (true) {
           System.out.println("votre message : ");
           String msg = sc.nextLine();
           out.println(msg);
        }
    }
}
