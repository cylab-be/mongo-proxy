/*
 * The MIT License
 *
 * Copyright 2018 Thibault Debatty.
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
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT,TORT OR OTHERWISE, ARISIFNG FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package be.cylab.mongoproxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Thibault Debatty
 */
public class ProxyServer {

    private final Logger logger = LoggerFactory.getLogger(
            ProxyServer.class);

    private final int port;
    private final HashMap<String, LinkedList<Listener>> listeners
            = new HashMap<>();

    /**
     *
     * @param port port on which the proxy will listen.
     */
    public ProxyServer(final int port) {
        this.port = port;
    }

    /**
     * Run forever.
     */
    public final void run() {

        try {

            // Wait for client connection...
            ServerSocket socket = new ServerSocket(port);

            while (true) {
                Socket client = socket.accept();
                logger.info("Connected");
                new Thread(new ConnectionHandler(client, listeners)).start();
            }

        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     *
     * @param db name of the database
     * @param collection name of the collection
     * @param listener listener used for notification
     */
    public final void addListener(final String db, final String collection,
            final Listener listener) {

        String collection_request = db + ".$cmd" + collection;

        LinkedList<Listener> collection_listeners
                = listeners.getOrDefault(
                        collection_request, new LinkedList<>());

        collection_listeners.add(listener);
        listeners.put(collection_request, collection_listeners);
    }

}
