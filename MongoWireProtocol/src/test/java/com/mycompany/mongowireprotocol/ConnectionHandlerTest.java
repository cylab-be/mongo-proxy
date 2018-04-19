/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongowireprotocol;

import com.mongodb.MongoClient;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sonoflight
 */
public class ConnectionHandlerTest {

    private static final int PORT = 9632;

    public ConnectionHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class ConnectionHandler.
     */
    @org.junit.Test
    public void testRun() {
        System.out.println("run");
        ConnectionHandler instance = null;
        instance.run();
    }

    /**
     * Test of readMessage method, of class ConnectionHandler.
     */
    @org.junit.Test
    public void testReadMessage() throws Exception {
        MongoClient mongo = new MongoClient("localhost", PORT);
        ServerSocket socket = new ServerSocket(PORT);
        Socket client = socket.accept();
        ConnectionHandler connectionHandler = new ConnectionHandler(client);
        InputStream stream = null;
        System.out.println("readMessage");

        byte[] result = connectionHandler.readMessage(stream);
        assertNull("The result most be null ", result);
        stream = client.getInputStream();

        result = connectionHandler.readMessage(stream);
        assertNotNull("The result most not be null ", result);

    }
}
