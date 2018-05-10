# Test files

### msg_1525942174707

byte[] corresponding to

```java
MongoDatabase database = mongo.getDatabase("myDb");
MongoCollection<Document> collection = database.getCollection(
        "myCollection");

Document doc = new Document("key", "value");
collection.insertOne(doc);
```

with java mongodb-driver version 3.5.0

### msg_1525942174710

byte[] corresponding to

```java
MongoDatabase database = mongo.getDatabase("myDb");
MongoCollection<Document> collection = database.getCollection(
        "myCollection");

Document doc = new Document("name", "MongoDB")
        .append("type", "database")
        .append("count", 1)
        .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
        .append("info", new Document("x", 203).append("y", 102));
collection.insertOne(doc);
```

with java mongodb-driver version 3.5.0
