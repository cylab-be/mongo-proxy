/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.cylab.mongoproxy;

/**
 *
 * @author sonoflight
 */
public final class Main {

    private static final int PORT = 9632;

    private Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        System.out.println("Starting...");
        ProxyServer server = new ProxyServer(PORT);
        server.run();

        System.out.println("Bye!");
    }
}


