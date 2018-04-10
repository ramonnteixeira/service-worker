package com.github.ramonnteixeira.worker;

import java.time.LocalDateTime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServiceWorkerTest {

    @BeforeClass
    public static void up() {
        ServiceWorker.get().start(4);
    }

    @AfterClass
    public static void down() {
        ServiceWorker.shutDown();
    }

    @Test
    public void scheduleWithFixedDelay() throws InterruptedException {
        FunctionMock mock = new FunctionMock();
        ServiceWorker.get().run(mock::executeWithDelay, LocalDateTime.now(), 500);
        Thread.sleep(1350L);
        Assert.assertTrue("Expected count = 2, but count = " + mock.getCount(), mock.getCount() == 2);
    }

    @Test
    public void runNow() throws InterruptedException {
        FunctionMock mock = new FunctionMock();
        ServiceWorker.get().run(mock::count);
        ServiceWorker.get().run(mock::count, LocalDateTime.now());
        Thread.sleep(50L);
        Assert.assertTrue("Expected count = 2, but count = " + mock.getCount(), mock.getCount() == 2);
    }

}
