package main.java.appendix.multi;

import java.io.IOException;

public class ThreadPerRequestScheduler implements ClientScheduler{
    @Override
    public void schedule(final ClientRequestProcessor requestProcessor) {
        final var runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    requestProcessor.process();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        final var thread = new Thread(runnable);
        thread.start();
    }
}
