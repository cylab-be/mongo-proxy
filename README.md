# mongo-proxy

A proxy for MongoDB written in Java. Using this proxy allows to easily trigger additional actions when data is inserted in the database, for example:

```java
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
