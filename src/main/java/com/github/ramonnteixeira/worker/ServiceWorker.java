package com.github.ramonnteixeira.worker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceWorker {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceWorker.class);
    private static final ServiceWorker INSTANCE = new ServiceWorker();

    private ScheduledExecutorService executor;

    private ServiceWorker() {
        //NOP
    }

    /**
     * Start ServiceWorker with default values (2 corePoolSize, "service_worker" as pollName);
     */
    public static void start() {
        start(2, "service_worker");
    }

    /**
     * Start serviceWorker
     *
     * @param corePoolSize number of threads in pool
     * @param poolName Name of threads
     */
    public static void start(final int corePoolSize, final String poolName) {
        stop();
        INSTANCE.executor = Executors.newScheduledThreadPool(corePoolSize,
                (Runnable r) -> new Thread(r, poolName));
    }

    /**
     * Stop all process
     */
    public static final void stop() {
        if (INSTANCE.executor != null) {
            INSTANCE.executor.shutdown();
            try {
                INSTANCE.executor.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
            INSTANCE.executor.shutdownNow();

            INSTANCE.executor = null;
        }
    }

    /**
     * Queue a background process to run in 5 milliseconds
     *
     * @param runnable
     */
    public static void run(Runnable runnable) {
        run(runnable, 5l);
    }

    /**
     * Queue a background process to run after X milliseconds
     *
     * @param runnable
     * @param delay
     */
    public static void run(Runnable runnable, long delay) {
        run(runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Queue a background process to run after X timeUnit
     *
     * @param runnable
     * @param delay
     * @param timeunit
     */
    public static void run(Runnable runnable, long delay, TimeUnit timeunit) {
        INSTANCE.executor.schedule(runnable, delay, timeunit);
    }

    /**
     * Queue a background process to run on fixed LocalDateTime
     *
     * @param runnable
     * @param time
     */
    public static void run(Runnable runnable, LocalDateTime time) {
        run(runnable, Duration.between(LocalDateTime.now(), time).toMillis());
    }

    /**
     * Queuing a background process to run after X milliseconds and on finally rescheduller to X milliseconds
     *
     * @param runnable
     * @param delay
     * @param period
     */
    public static void run(Runnable runnable, long delay, long period) {
        INSTANCE.executor.scheduleWithFixedDelay(runnable, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Queuing a background process to run on fixed LocalDateTime and on finally rescheduller to X milliseconds .
     *
     * @param runnable
     * @param firstTime
     * @param period
     */
    public static void run(Runnable runnable, LocalDateTime firstTime, long period) {
        run(runnable, Duration.between(LocalDateTime.now(), firstTime).toMillis(), period);
    }

}
