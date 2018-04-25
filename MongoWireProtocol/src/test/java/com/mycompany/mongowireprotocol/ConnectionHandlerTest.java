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
import junit.framework.TestCase;
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
public class ConnectionHandlerTest extends TestCase{
    private ConnectionHandler connectionhandler;
    private MongoClient mongo;
    private ServerSocket socket;
    private Socket client;
    private static final int PORT = 9632;

    public ConnectionHandlerTest() {
    }

    @Before
    public void setUp() throws IOException {
        //initialize variables
        mongo = new MongoClient("localhost", PORT);
        socket = new ServerSocket(PORT);
        client = socket.accept();
        connectionhandler = new ConnectionHandler(client);
   }

    @After
    public void tearDown() throws IOException {

      connectionhandler = null;
      client.close();
      socket.close();
      mongo.close();
    }

    /**
     * Test of run method, of class ConnectionHandler.
     */
    @org.junit.Test
    public void testRun() {
        System.out.println("run");
        //connectionhandler.run();
    }

    /**
     * Test of readMessage method, of class ConnectionHandler.
     */
    @org.junit.Test
    public void testReadMessage() throws Exception {
        /*
        InputStream stream = null;
        System.out.println("readMessage");

        //run test
        byte[] result = connectionhandler.readMessage(stream);
        assertNull("The result most be null ", result);

        stream = client.getInputStream();
        result = connectionhandler.readMessage(stream);
        assertNotNull("The result most not be null ", result);
        */
    }
}
