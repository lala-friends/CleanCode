package main.java.appendix.single;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private volatile boolean keepProcessing = true;

    public Server(final int port, final int millisecondsTime) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setSoTimeout(millisecondsTime);
    }

    @Override
    public void run() {
        System.out.println("Server started");

        while (keepProcessing) {
            try {
                System.out.println("accepting client");
                final var socket = serverSocket.accept();
                System.out.println("get client");
                process(socket);
            } catch (IOException e) {
                if (!keepProcessing) {
                    System.out.println("Server stopped gracefully.");
                    break; // 정상 종료
                } else {
                    throw new RuntimeException(e); // 예외 처리
                }
            }
        }
    }

    private void process(final Socket socket) {
        if (socket == null) {
            return;
        }

        try {
            System.out.println("Server: getting message");
            final var message = MessageUtils.getMessage(socket);
            System.out.printf("Server: got message: $s\n", message);
            Thread.sleep(1000);
            System.out.printf("Server: sending reploy: %s\n", message);
            MessageUtils.sendMessage(socket, "Processed: " + message);
            System.out.println("Server: sent");
            closeIgnoringException(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProcessing() {
        keepProcessing = false;
        closeIgnoringException(serverSocket);
    }

    private void closeIgnoringException(final Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    private void closeIgnoringException(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {}
        }
    }
}
