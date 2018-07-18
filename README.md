# Mongo Proxy

[![Maven](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/be/cylab/mongo-proxy/maven-metadata.xml.svg)](https://mvnrepository.com/artifact/be.cylab/mongo-proxy)
[![Build Status](https://travis-ci.org/cylab-be/mongo-proxy.svg?branch=master)](https://travis-ci.org/cylab-be/mongo-proxy)
[![Coverage Status](https://coveralls.io/repos/github/cylab-be/mongo-proxy/badge.svg?branch=master)](https://coveralls.io/github/cylab-be/mongo-proxy?branch=master)

A proxy for MongoDB written in Java. Using this proxy allows to easily trigger additional actions when data is inserted in the database

Developement now takes place at https://gitlab.cylab.be/cylab/mongo-proxy

## Example

```java
// port on which the proxy will be listening 
int PORT = 9632;
ProxyServer srv = new ProxyServer(PORT);
srv.addListener("admin.$cmd", new Listener() {
    @Override
    public void run(Document doc) {
        System.out.println("Notified: " + doc);
    }
});
srv.run();
```
## MongoDb Wire Protocol Structure
this table shows the structure of the driver message for different version of driver and command done.

|Version of mongodb driver |  Command         |[Mongodb Wire protocol](https://docs.mongodb.com/manual/reference/mongodb-wire-protocol/)  |
|--------------------------|-------------------|-----------------------------|
|3.5						    | db/collection/insertOne(document)| opCode = Op_Query 
|.|.|fullCollectionName = dbName.$cmd
|.|.|Document = [insert:collectionName, ordered:true, documents:[...] ]





 








