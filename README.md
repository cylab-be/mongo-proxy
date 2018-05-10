# Mongo Proxy

[![Maven](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/be/cylab/mongo-proxy/maven-metadata.xml.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22be.cylab%22%20AND%20a%3A%22mongo-proxy%22) [![Build Status](https://travis-ci.org/cylab-be/mongo-proxy.svg?branch=master)](https://travis-ci.org/cylab-be/mongo-proxy) [![Coverage Status](https://coveralls.io/repos/github/cylab-be/mongo-proxy/badge.svg?branch=master)](https://coveralls.io/github/cylab-be/mongo-proxy?branch=master)

A proxy for MongoDB written in Java. Using this proxy allows to easily trigger additional actions when data is inserted in the database, for example:

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
