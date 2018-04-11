package com.github.ramonnteixeira.worker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceWorker {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceWorker.class);
    private static final Map<String, ServiceWorker> INSTANCES = new HashMap<>();
    private static final String SERVICE_DEFAULT = "service_worker_default";

    private ScheduledExecutorService executor;
    private final String poolName;

    private ServiceWorker(final String poolName) {
        this.poolName = poolName;
    }

    /**
     * Get a default service worker
     *
     * @return instance of ServiceWorker
     */
    public static final ServiceWorker get() {
        return get(SERVICE_DEFAULT);
    }

    /**
     * Get a specific service worker
     *
     * @param poolName
     * @return instance of ServiceWorker with specific poolName
     */
    public static final ServiceWorker get(final String poolName) {
        if (INSTANCES.containsKey(poolName)) {
            return INSTANCES.get(poolName);
        }
        
        return INSTANCES.values().stream().findAny().get();
    }
    
    private static ServiceWorker getOrCreate(final String poolName) {
        final ServiceWorker instance = INSTANCES.getOrDefault(poolName, new ServiceWorker(poolName));
        INSTANCES.put(poolName, instance);
        return instance;
    }

    /**
     * Start an instance of serviceWorker with poolName default
     *
     * @param corePoolSize number of threads in pool
     */
    public static final void start(final int corePoolSize) {
        start(SERVICE_DEFAULT, corePoolSize);
    }

    /**
     * Start an instance of serviceWorker
     *
     * @param corePoolSize number of threads in pool
     * @param poolName Name of threads
     */
    public static final void start(final String poolName, final int corePoolSize) {
        final ServiceWorker instance = getOrCreate(poolName);
        instance.executor = Executors.newScheduledThreadPool(corePoolSize,
                (Runnable r) -> new Thread(r, poolName));
    }

    /**
     * Shutdown all instances
     */
    public static final void shutDown() {
        INSTANCES.values().stream().forEach(ServiceWorker::stop);
    }

    /**
     * Stop all process
     */
    private void stop() {
        if (executor != null) {
            executor.shutdown();
            try {
                executor.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
            executor.shutdownNow();
            executor = null;
        }
    }

    /**
     * Queue a background process to run in 5 milliseconds
     *
     * @param runnable
     */
    public final void run(final Runnable runnable) {
        run(runnable, 5l);
    }

    /**
     * Queue a background process to run after X milliseconds
     *
     * @param runnable
     * @param delay
     */
    public final void run(final Runnable runnable, final long delay) {
        run(runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Queue a background process to run after X timeUnit
     *
     * @param runnable
     * @param delay
     * @param timeunit
     */
    public final void run(final Runnable runnable, final long delay, final TimeUnit timeunit) {
        executor.schedule(runnable, delay, timeunit);
    }

    /**
     * Queue a background process to run on fixed LocalDateTime
     *
     * @param runnable
     * @param time
     */
    public final void run(final Runnable runnable, final LocalDateTime time) {
        run(runnable, Duration.between(LocalDateTime.now(), time).toMillis());
    }

    /**
     * Queuing a background process to run after X milliseconds and on finally rescheduller to X milliseconds
     *
     * @param runnable
     * @param delay
     * @param period
     */
    public final void run(final Runnable runnable, final long delay, final long period) {
        executor.scheduleWithFixedDelay(runnable, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Queuing a background process to run on fixed LocalDateTime and on finally rescheduller to X milliseconds .
     *
     * @param runnable
     * @param firstTime
     * @param period
     */
    public final void run(final Runnable runnable, final LocalDateTime firstTime, final long period) {
        run(runnable, Duration.between(LocalDateTime.now(), firstTime).toMillis(), period);
    }

}
