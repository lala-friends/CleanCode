package main.java.appendix.multi;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorClientScheduler implements ClientScheduler {
    private final Executor executor;

    public ExecutorClientScheduler(final int availableThreads) {
        executor = Executors.newFixedThreadPool(availableThreads);
    }

    @Override
    public void schedule(final ClientRequestProcessor requestProcessor) {
        final var runnable = new Runnable() {
            @Override
            public void run() {
                requestProcessor.process();
            }
        };
        executor.execute(runnable);
    }
}
