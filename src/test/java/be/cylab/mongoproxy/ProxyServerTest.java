/*
 * The MIT License
 *
 * Copyright 2018 tibo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package be.cylab.mongoproxy;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import org.bson.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author tibo
 */
public class ProxyServerTest {

    /**
     * The port on which the proxy server will be listening.
     */
    public static final int PORT = 9632;

    /**
     * Test of run method, of class ProxyServer.
     * @throws java.lang.InterruptedException
     */
    @Test
    public final void testRun() throws InterruptedException, Exception {

        System.out.println("Start the proxy server...");
        System.setProperty(
                org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
<<<<<<< Updated upstream

        Thread srv_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProxyServer srv = new ProxyServer(PORT);
                srv.addListener("myDb","myCollection", new Listener() {
                    @Override
                    public void run(
                            final be.cylab.mongoproxy.Document doc) {
                        System.out.println("Notified: " + doc);
                    }
                });
                srv.run();
=======
        Thread srv_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ProxyServer srv = new ProxyServer(PORT);
                    srv.addListener("myDb","myCollection", new Listener() {
                        @Override
                        public void run(
                                final be.cylab.mongoproxy.Document doc) {
                            System.out.println("Notified: " + doc);
                        }
                    });
                    srv.run();
                } catch (Exception ex) {
                    srv_thread_exception = ex;
                }
>>>>>>> Stashed changes
            }
        });

        srv_thread.setUncaughtExceptionHandler(
                (final Thread t, final Throwable e) -> {
            throw new RuntimeException("Server thread exception", e);
        });
        srv_thread.start();

        System.out.println("Run some tests...");
        ClientTest client_test = new ClientTest();
        Thread client_thread = new Thread(client_test);
        client_thread.start();

        // Wait for the client to finish, and kill if it is stuck
        client_thread.join(10000);
        if (client_thread.isAlive()) {
            client_thread.interrupt();
        }

        System.out.println(client_test.getException());

        if (client_test.getException() != null) {
            throw client_test.getException();
        }

        srv_thread.interrupt();
    }

}

class ClientTest implements Runnable {

    private AssertionError er;

    @Override
    public void run() {
<<<<<<< Updated upstream
        try {
            MongoClient mongo = new MongoClient(
                    "localhost", ProxyServerTest.PORT);
            MongoDatabase database = mongo.getDatabase("myDb");
            MongoCollection<Document> collection = database.getCollection(
                    "myCollection");

            long initial_count = collection.count();
            Document doc = new Document("key", "value");
            collection.insertOne(doc);

            Document doc2 = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));
            collection.insertOne(doc2);
            //collection.deleteMany(Filters.eq("name", "MongoDB"));

            long final_count = collection.count();
            assertEquals(initial_count + 2, final_count);
        } catch (AssertionError error) {
            this.er = error;
        }
    }

    public AssertionError getException() {
        return this.er;
=======
        MongoClient mongo = new MongoClient("localhost", ProxyServerTest.PORT);
        MongoDatabase database = mongo.getDatabase("myDb");
        MongoCollection<Document> collection = database.getCollection(
                "myCollection");
 
        long initial_count = collection.count();
        Document doc = new Document("key", "value");
        collection.insertOne(doc);

        System.out.println("Insert...");
        Document doc2 = new Document("name", "MongoDB")
            .append("type", "database")
            .append("count", 1)
            .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
            .append("info", new Document("x", 203).append("y", 102));
        collection.insertOne(doc2);

        long final_count = collection.count();
        assertEquals(initial_count + 2, final_count);
>>>>>>> Stashed changes
    }
}