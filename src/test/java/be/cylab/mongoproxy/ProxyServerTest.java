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

                Document doc2 = new Document("name", "MongoDB")
                    .append("type", "database")
                    .append("count", 1)
                    .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                    .append("info", new Document("x", 203).append("y", 102));
                collection.insertOne(doc2);

                long final_count = collection.count();
                assertEquals(initial_count + 2, final_count);
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
