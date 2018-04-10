package com.mycompany.mongodb_class;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    final static int port = 9632;

    public static void main(String[] args) {

        /*MongoClient mongo = new MongoClient("localhost", 27017);
    MongoDatabase database = mongo.getDatabase("myDb");
    MongoCollection<Document> collection = database.getCollection("stageCollection");
         */
        // creating document db
        Document doc = new Document("id", 1).append("Title", "stageAcademique")
                .append("personInCharge", new Document("Name", "Debatty").append("lastName", "Thibault"))
                .append("students", Arrays.asList(new Document("Name", "Kue").append("lastName", "Guy").append("Project", "Mongo-Proxy"),
                        new Document("Name", "Kolawole").append("lastName", "Abdoulaye").append("Project", "Laravel")))
                .append("Info", new Document("Duration", Arrays.asList("03/04/18", "18/05/18")).append("Hours", "8h/day"));

        /* This is done once to document the database
    //adding the document to the collection in db
    collection.insertOne(doc);
         */
 /*
     mongodbConnect mongodb = new mongodbConnect("myDb");
     //List<Document> documents = new ArrayList<Document>();
     mongodb.viewCollection("stageCollection");
     mongodb.SearchDoc("Title","Projet");
     mongodb.countDocument("stageCollection");
     mongodb.countDocument("myCollection");

     mongodb.SearchDoc("Title","stageAcademique");
         */
        Socket socket;
        DataInputStream in;
        DataOutputStream out;

        try {

            //get server addresse
            InetAddress serveur = InetAddress.getByName("Guy-RolandMacBook-Pro.local");
            //creatiing the socket for a connection to the port 9632
            socket = new Socket(serveur, port);
            //opening I/O stream

            out = new DataOutputStream(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.writeBytes("Hello word\n");
            out.writeBytes("connection success!\n");

            /*
        String responseLine;
                while ((responseLine = is.readLine()) != null) {
                    System.out.println("Server: " + responseLine);
                    if (responseLine.contains("OK\n")) {
                      break;
                    }
                }*/
            System.out.println(is.readLine());

            is.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
