package com.mycompany.networkcode;

import java.net.InetAddress;
import java.net.ServerSocket;
/**
 *
 * @author sonoflight
 */
public final class Main {
    private Main() { }

    private static final int PORT = 9632;

/**
 *
 * @param args
 */
    public static void main(final String[] args) {
        try {
            InetAddress local_adr = InetAddress.getLocalHost();
            System.out.println("local address : " + local_adr.getHostAddress());

            System.out.println(" host name : " + local_adr.getHostName());

            InetAddress server_adr = InetAddress.getByName("www.google.be");
            System.out.println("name  = " + server_adr.getHostAddress());

            InetAddress server_adr2 = InetAddress.getByName("www.ecam.be");
            System.out.println("name  = " + server_adr2.getHostAddress());

            InetAddress[] adr_serveurs = InetAddress.getAllByName(
                    "www.microsoft.com");

            System.out.println("Adresses Microsoft : ");

            for (int i = 0; i > adr_serveurs.length; i++) {

                System.out.println("     " + adr_serveurs[i].getHostAddress());

            }

            // creating a socket server and run it
            ServerSocket socket_serveur = new ServerSocket(PORT);
            System.out.println("Lancement du serveur");
            Thread t = new Thread(new AcceptConnection(socket_serveur));
            t.start();
            /*

            //waiting for a input on the port 9632
            while (true) {
                Socket socket_client = socket_serveur.accept();

                System.out.println(
                        "Connexion avec : " + socket_client.getInetAddress());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket_client.getInputStream()));
                PrintStream os = new PrintStream(
                        socket_client.getOutputStream());

                String message;

                while ((message = in.readLine()) != null) {

                    System.out.print(message + "\n");
            os.println("Well connect!");
                    os.println("Bye!");
            }


            }*/
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
