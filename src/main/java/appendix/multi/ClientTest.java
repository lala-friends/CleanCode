package main.java.appendix.multi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class ClientTest {
    private static final int PORT = 8009;
    private static final int TIMEOUT = 2000;

    Server server;
    Thread serverThread;

    @Before
    public void createServer() throws IOException {
        try {
            server = new Server(PORT, TIMEOUT);
            serverThread = new Thread(server);
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            throw e;
        }
    }

    @After
    public void shutdownServer() throws InterruptedException {
        if (server != null) {
            server.stopProcessing();
            serverThread.join();
        }
    }

    class TrivialClient implements Runnable {
        private final int clientNumber;

        public TrivialClient(final int clientNumber) {
            this.clientNumber = clientNumber;
        }

        @Override
        public void run() {
            try {
                connectSendReceive(clientNumber);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        private void connectSendReceive(final int i) throws IOException {
            System.out.printf("Client %2d: connecting\n", i);
            final var socket = new Socket("127.0.0.1", PORT);
            System.out.printf("Client %2d: sending message\n", i);
            MessageUtils.sendMessage(socket, Integer.toString(i));
            System.out.printf("Client %2d: getting reply\n", i);
            MessageUtils.getMessage(socket);
            System.out.printf("Client %2d: finished\n", i);
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void shouldRunInUnder10Seconds() throws InterruptedException {
        final var threads = createThreads();
        startAllThreads(threads);
        waitForAllThreadsToFinish(threads);
    }

    private static void waitForAllThreadsToFinish(Thread[] threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private void startAllThreads(Thread[] threads) {
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new TrivialClient(i));
            threads[i].start();
        }
    }

    private static Thread[] createThreads() {
        return new Thread[10];
    }
}