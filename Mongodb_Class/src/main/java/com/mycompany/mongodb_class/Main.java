package com.mycompany.mongodb_class;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import org.bson.Document;

/**
 *
 * @author sonoflight
 */
public final class Main {

    /**
     * @param constructor private constructor
     */
    private Main() {
    }
    private static final int PORT = 9632;
    private static final int PORT_DB = 27017;

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {



        MongoClient mongo = new MongoClient("localhost", PORT);
        MongoDatabase database = mongo.getDatabase("myDb");
        MongoCollection<Document> collection = database.getCollection("myCollection");
        System.out.println("Found " + collection.count() + " documents in collection");

        // creating document db
        Document doc = new Document("id", 1)
                .append(
                "Title", "stageAcademique")
                .append(
         "personInCharge", new Document("Name", "Debatty").append( "lastName",
         "Thibault")) .append("students", Arrays.asList(new Document("Name",
         "Kue").append( "lastName", "Guy").append("Project", "Mongo-Proxy"),
         new Document( "Name", "Kolawole").append("lastName",
         "Abdoulaye").append( "Project", "Laravel"))) .append("Info", new
         Document("Duration", Arrays.asList( "03/04/18",
         "18/05/18")).append("Hours", "8h/day"));

         //This is done once to document the database
        //adding the document tothe collection in db
        collection.insertOne(doc);



      /**
     MongodbConnect mongodb = new MongodbConnect("myDb");
     //List<Document> documents = new ArrayList<Document>();
     mongodb.viewCollection("stageCollection");
     mongodb.searchDoc("Title","Projet");
     mongodb.countDocument("stageCollection");
     mongodb.countDocument("myCollection");

     mongodb.searchDoc("Title","stageAcademique");
     */




        /**
         *Socket socket;

        try {
            //get server addresse
            InetAddress serveur = InetAddress.getLocalHost();
            //creatiing the socket for a connection to the port 9632
            socket = new Socket(serveur, PORT);
            //opening I/O stream

            PrintStream os = new PrintStream(socket.getOutputStream());
            BufferedReader is = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            Thread t = new Thread(new Emission(os));
            t.start();


        String responseLine;
                while ((responseLine = is.readLine()) != null) {
                    System.out.println("Server: " + responseLine);
                    if (responseLine.contains("OK\n")) {
                      break;
                    }
                }
            String msg;
            while ((msg = is.readLine()) != null) {
                System.out.println(msg);
            }
            is.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
         */


    }
}
