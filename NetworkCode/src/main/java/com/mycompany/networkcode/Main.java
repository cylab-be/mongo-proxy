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
        
        // creating a socket server and run it
        ServerSocket socketServeur = new ServerSocket(port);
        System.out.println("Lancement du serveur");
        
        //waiting for a input on the port 9632  
        while (true) {
        Socket socketClient = socketServeur.accept();
        

        System.out.println("Connexion avec : "+socketClient.getInetAddress());

       BufferedReader in = new BufferedReader(

        new InputStreamReader(socketClient.getInputStream()));

        String message;
        
        while ((message = in.readLine())!= null )
        {

            System.out.print(message+"\n");
        }
        
       PrintStream os = new PrintStream(socketClient.getOutputStream());
        os.println("connect to : \n");
        os.println("OK\n");
        
        }
    }
    catch(Exception e){
       e.printStackTrace();
        
    }
       
    }
    
}
