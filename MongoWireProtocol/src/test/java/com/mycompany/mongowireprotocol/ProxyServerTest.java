/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongowireprotocol;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author tibo
 */
public class ProxyServerTest {

    private static final int PORT = 9632;

    /**
     * Test of run method, of class ProxyServer.
     * @throws java.lang.InterruptedException
     */
    @Test
    public final void testRun() throws InterruptedException {
        System.out.println("run");

        System.out.println("Start the proxy server...");
        Thread srv_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProxyServer srv = new ProxyServer(PORT);
                srv.run();
            }
        });
        srv_thread.start();

        System.out.println("Run some tests...");
        Thread client_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MongoClient mongo = new MongoClient("localhost", PORT);
                MongoDatabase database = mongo.getDatabase("myDb");
                MongoCollection<Document> collection = database.getCollection(
                        "myCollection");

                long initial_count = collection.count();
                Document doc = new Document("key", "value");
                collection.insertOne(doc);
                long final_count = collection.count();
                assertEquals(initial_count + 1, final_count);
            }
        });
        client_thread.start();

        // Wait for the client to finish, and kill if it is stuck
        client_thread.join(10000);
        if (client_thread.isAlive()) {
            client_thread.interrupt();
        }

        srv_thread.interrupt();


    }

}
