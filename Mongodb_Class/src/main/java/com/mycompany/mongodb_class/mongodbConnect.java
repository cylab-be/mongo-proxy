package com.mycompany.mongodb_class;



import org.bson.Document;
import com.mongodb.client.FindIterable;
import java.util.Iterator;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

// this class is use to connect to a database for a search of a document
public class mongodbConnect {
    private MongoDatabase database;
    
    public mongodbConnect (String dbName){
    
    //connection to the DB
    MongoClient mongo = new MongoClient("localhost", 27017);
    this.database = mongo.getDatabase(dbName);
    
    }
    
    //Search and print the specified document 
    public void  SearchDoc(String reference, String value)
    {
        
        for( String name : database.listCollectionNames())
        {
            MongoCollection<Document> collection = database.getCollection(name);
            MongoCursor<Document> cursor = collection.find(Filters.eq(reference, value)).iterator();
            try 
            {
                while (cursor.hasNext()) 
                {
                    System.out.println("Document found in <"+name+"> view: \n\t"+cursor.next().toJson()+"\n");
                }
            } 
            finally 
            {
                cursor.close();
            }
            
                
        }
        
    }
    
    //View a collection
    public void viewCollection (String collectionName)
    {
        MongoCollection<Document> collection = database.getCollection(collectionName);
         MongoCursor<Document> cursor = collection.find().iterator();
        try 
        {
            while (cursor.hasNext()) 
            {
                System.out.println("Collection <"+ collectionName+"> view: \n\t" +cursor.next().toJson()+"\n");
            }
        } 
        finally 
        {
            cursor.close();
        }
                
    }
    
    //number of document in collection
    public void countDocument(String collectionName)
    {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        System.out.println("nombre de document dans <"+collectionName+"> : "+ collection.count()+"\n");
    }
    
}
