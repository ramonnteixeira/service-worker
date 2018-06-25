# Service Worker

[![CircleCI](https://circleci.com/gh/ramonnteixeira/service-worker.svg?style=svg)](https://circleci.com/gh/ramonnteixeira/service-worker)

This lib borned to manipulate a thread pool with concurrently queue tasks.

To use add maven dependency

```
<dependency>
    <groupId>com.github.ramonnteixeira</groupId>
    <artifactId>service-worker</artifactId>
    <version>1.0.0</version>
</dependency>
```

On your application, start the Service worker and stop only when closing the app.


```
public static void main(String[] args) {
    ServiceWorker.start(4);

    // Queue MyService.exec() now and every 5 seconds after the last execution
    ServiceWorker.get().run(MyService::exec, LocalDateTime.now(), 5000);

    // Search for "other_pool" and queue MyService.exec() now 
    ServiceWorker.get("other_pool").run(MyService::exec);

    // Queue MyService.finish() to run after 30 seconds
    ServiceWorker.get().run(MyService::shutdown, 30, TimeUnit.SECONDS);
}

public static void exec() {
    System.out.println("Success");
}

public static void shutdown() {
    ServiceWorker.shutDown();
}
```

