package com.mycompany.mongodb_class;



import org.bson.Document;
import com.mongodb.client.FindIterable;
import java.util.Iterator;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

// this class is use to connect to a database for a search of a document
public class SearchFileClass {
    private MongoDatabase database;
    
    public SearchFileClass (String dbName){
    
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
            FindIterable<Document> iterDoc = collection.find(Filters.eq(reference, value));
            Iterator it = iterDoc.iterator();
            int i = 1;
            while (it.hasNext()) {
            System.out.println("Document view:" + it.next() +"\n");
            i++;
            }
            
                
        }
        
    }
    
}
