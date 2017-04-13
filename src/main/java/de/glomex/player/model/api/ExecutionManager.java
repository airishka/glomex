package de.glomex.player.model.api;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class ExecutionManager implements Executor {

    private final Logger log = Logging.getLogger(ExecutionManager.class);

    private final static int MAX_THREADS = 5;

    private final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

    void shutdown() {
        log.entering("ExecutionManager", "Shutdown");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(1, TimeUnit.SECONDS))
                    log.severe("Error terminating thread pool");
            }
        } catch (InterruptedException interrupted) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public @NotNull <T> Future<T> submit(@NotNull Callable<T> task) {
        return executor.submit(task);
    }

    @Override
    public void execute(@NotNull Runnable task) {
        executor.execute(task);
    }

}

