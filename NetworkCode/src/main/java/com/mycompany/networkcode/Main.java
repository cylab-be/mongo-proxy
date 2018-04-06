package com.mycompany.networkcode;

import java.net.*;
import java.io.*;


/**
 *
 * @author sonoflight
 */
public class Main {

   final static int port = 9632;
   
    public static void main(String[] args) {
       
    try{
        
        InetAddress localAdr = InetAddress.getLocalHost();
        System.out.println("local address : "+ localAdr.getHostAddress());
        
        System.out.println(" host name : "+ localAdr.getHostName());
        
        InetAddress serverAdr = InetAddress.getByName("www.google.be");
        System.out.println("name  = "+serverAdr.getHostAddress());
        
        InetAddress serverAdr2 = InetAddress.getByName("www.ecam.be");
        System.out.println("name  = "+serverAdr2.getHostAddress());
        
        InetAddress[] adrServeurs = InetAddress.getAllByName("www.microsoft.com");

        System.out.println("Adresses Microsoft : ");

        for (int i = 0; i > adrServeurs.length; i++) {

        System.out.println("     "+adrServeurs[i].getHostAddress());

       }
        
        ServerSocket socketServeur = new ServerSocket(port);
        System.out.println("Lancement du serveur");

        while (true) {
        Socket socketClient = socketServeur.accept();
        String message = "";

        System.out.println("Connexion avec : "+socketClient.getInetAddress());

        // InputStream in = socketClient.getInputStream();
        // OutputStream out = socketClient.getOutputStream();

        BufferedReader in = new BufferedReader(
        new InputStreamReader(socketClient.getInputStream()));
        PrintStream out = new PrintStream(socketClient.getOutputStream());
        message = in.readLine();
        out.println(message);

        socketClient.close();
        
     
        }
    }
    catch(Exception e){
       e.printStackTrace();
        
    }
       
    }
    
}
