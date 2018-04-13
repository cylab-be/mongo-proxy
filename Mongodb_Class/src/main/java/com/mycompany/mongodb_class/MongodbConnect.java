package com.mycompany.mongodb_class;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

/**
 *
 * @author sonoflight
 */
public class MongodbConnect {

    private MongoDatabase database;

    /**
     * @param dbname the db name
     */
    public MongodbConnect(final String dbname) {

        MongoClient mongo = new MongoClient("localhost");
        this.database = mongo.getDatabase(dbname);

    }
/**
 *
 * @author sonoflight
 */
    public final void searchDoc(final String reference, final String value) {

        for (String name : database.listCollectionNames()) {
            MongoCollection<Document> collection = database.getCollection(name);
            MongoCursor<Document> cursor = collection.find(Filters.eq(
                    reference, value)).iterator();
            try {
                while (cursor.hasNext()) {
                    System.out.println("Document found in <" + name
                            + "> view: \n\t" + cursor.next().toJson() + "\n");
                }
            } finally {
                cursor.close();
            }

        }

    }

/**
 *
 * @author sonoflight
 */
    public final void viewCollection(final String collection_name) {
        MongoCollection<Document> collection = database.getCollection(
                collection_name);
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println("Collection <" + collection_name
                        + "> view: \n\t" + cursor.next().toJson() + "\n");
            }
        } finally {
            cursor.close();
        }

    }
/**
 *
 * @author sonoflight
 */
    public final void countDocument(final String collection_name) {
        MongoCollection<Document> collection = database.getCollection(
                collection_name);
        System.out.println("nombre de document dans <" + collection_name
                + "> : " + collection.count() + "\n");
    }

}
