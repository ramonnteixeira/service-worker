# Service Worker

This lib borned to manipulate a thread pool with concurrently queue tasks.

To use add maven dependency

```
<dependency>
    <groupId>com.github.ramonnteixeira</groupId>
    <artifactId>service-worker</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

On your application, start the Service worker and stop only when go close the app.


```
public static void main(String[] args) {
    ServiceWorker.start();

    // Queue MyService.exec() now and every 5 seconds after the last execution
    ServiceWorker.run(MyService::exec, LocalDateTime.now(), 5000);

    // Queue MyService.exec() now
    ServiceWorker.run(MyService::exec);

    // Queue MyService.finish() to run after 30 seconds
    ServiceWorker.run(MyService::shutdown, 30, TimeUnit.SECONDS);
}

public static void exec() {
    System.out.println("Success");
}

public static void shutdown() {
    ServiceWorker.stop();    
}
```

