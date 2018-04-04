/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hello_word;

import java.util.Scanner;

/**
 *
 * @author sonoflight
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("HELLO");
        Scanner sc = new Scanner(System.in);
        System.out.println("entrer un mot\n");
        String word = sc.nextLine();
        System.out.println("le mot entré est: "+ word);
        System.out.println("entrer un nombre\n");
        int number = sc.nextInt();
        System.out.println("le nombre entré est: "+ number);
       
    }
    
}
