package com.github.ramonnteixeira.worker;

import java.time.LocalDateTime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServiceWorkerTest {

    public static void main(String[] args) {
        ServiceWorker.start();

        ServiceWorker.run(ServiceWorkerTest::exec, LocalDateTime.now(), 5000);

        ServiceWorker.stop();
    }

    public static void exec() {
        // nothing
    }

    @BeforeClass
    public static void up() {
        ServiceWorker.start();
    }

    @AfterClass
    public static void down() {
        ServiceWorker.stop();
    }

    @Test
    public void scheduleWithFixedDelay() throws InterruptedException {
        FunctionMock mock = new FunctionMock();
        ServiceWorker.run(mock::executeWithDelay, LocalDateTime.now(), 500);
        Thread.sleep(1350L);
        Assert.assertTrue("Expected count = 2, but count = " + mock.getCount(), mock.getCount() == 2);
    }

    @Test
    public void runNow() throws InterruptedException {
        FunctionMock mock = new FunctionMock();
        ServiceWorker.run(mock::count);
        ServiceWorker.run(mock::count, LocalDateTime.now());
        Thread.sleep(50L);
        Assert.assertTrue("Expected count = 2, but count = " + mock.getCount(), mock.getCount() == 2);
    }

}
