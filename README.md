# Mongo Proxy

[![Maven](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/be/cylab/mongo-proxy/maven-metadata.xml.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22be.cylab%22%20AND%20a%3A%22mongo-proxy%22) [![Build Status](https://travis-ci.org/cylab-be/mongo-proxy.svg?branch=master)](https://travis-ci.org/cylab-be/mongo-proxy)

A proxy for MongoDB written in Java. Using this proxy allows to easily trigger additional actions when data is inserted in the database, for example:

```
// port 
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
