package com.mycompany.hello_word;

import com.mongodb.MongoClient;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import com.mongodb.client.FindIterable;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.Iterator;

public class TestClass {

    public static void main(String args[]) {
        
        // Creating a Mongo client
        MongoClient mongo = new MongoClient("localhost");

        // Creating Credentials
        //MongoCredential credential;
        //credential = MongoCredential.createCredential("sampleUser", "myDb",
        //        "password".toCharArray());
        //System.out.println("Connected to the database successfully");
        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");
        //System.out.println("Credentials ::" + credential);

        // Creating a Collection
        //database.createCollection("testCollection");
        //System.out.println("collection created successfully\n");
        // select to manipulate a collection
        MongoCollection<Document> collection = database.getCollection(
                "myCollection");

        // Creating a document
        String names[] = {
            "Kue GUY", "Kolawole Abdoulaye"};
        String periode[] = {
            "03-04-2018", "25-05-2018"};

        /*Document document = new Document("Title", "dbStage")
                .append("departement", "CISS")
                .append("Responsable", "Thibault");
              //.append("stagiaire", names)
              //.append("periode", periode);

        Document document2 = new Document("Title", "Projet")
                .append("Reseau", "mongodb")
                .append("Web", "siteWeb");
        collection.insertOne(document);
        collection.insertOne(document2);
         */
        //System.out.println("Documents inserted successfully\n");
        collection.updateOne(Filters.eq("Title", "Projet"), Updates.set(
                "Reseau", "mongodb-proxy"));
        collection.updateOne(Filters.eq("Title", "dbStage"), Updates.set(
                "Responsable", "Debatty"));

        //when using the _id generate by the pc no changes updated
        collection.updateOne(Filters.eq(
                "_id", "5ac5ef702df9939b4359c1bc"), Updates.set(
                "Web", "laravel"));

        //wecan choise document by using any data like reference in the document
        collection.updateOne(Filters.eq(
                "Responsable", "Thibault"), Updates.set("Title", "Cylab"));
        System.out.println("Document update successfully...\n");

        //Delete a document
        collection.deleteOne(Filters.eq("Title", "dbStage"));
        System.out.println("Document deleted successfully...\n");

        //count document in collection
        int number = (int) collection.count();
        System.out.println(number + "\n");

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
            i++;}

        //list all collection of the db
        for (String name : database.listCollectionNames()) {
            System.out.println(name);}

        //and other type of displaying all document
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());}
        }
            finally {
            cursor.close();}

    }
}
