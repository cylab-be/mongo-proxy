package com.mycompany.hello_word;

import org.bson.Document;
import com.mongodb.client.FindIterable;
import java.util.Iterator;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

public class TestClass {

    public static void main(String args[]) {

        // Creating a Mongo client
        MongoClient mongo = new MongoClient("localhost", 27017);

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");
        System.out.println("Credentials ::" + credential);

        // Creating a Collection
        // database.createCollection("myCollection");
        System.out.println("collection created successfully");

        // select to manipulate a collection
        MongoCollection<Document> collection = database.getCollection("myCollection");

        // Creating a document
        String names[] = {"Kue GUY", "Kolawole Abdoulaye"};
        String periode[] = {"03-04-2018", "25-05-2018"};

        Document document = new Document("Title", "dbStage")
                .append("departement", "CISS")
                .append("Responsable", "Thibault");
//                .append("stagiaire", names)
//                .append("periode", periode);

        Document document2 = new Document("Title", "Projet")
                .append("Reseau", "mongodb")
                .append("Web", "siteWeb");
        collection.insertOne(document);
        collection.insertOne(document2);
        System.out.println("Documents inserted successfully");

        /*
     find() method of com.mongodb.client.MongoCollection class is used.
     This method returns a cursor, so you need to iterate this cursor.
         */
        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();
        int i = 1;

        // Getting the iterator
        Iterator it = iterDoc.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
            i++;
        }
    }
}
