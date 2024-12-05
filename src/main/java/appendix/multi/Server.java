package main.java.appendix.multi;

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
        Socket socket = null;

        while (keepProcessing) {
            try {
                socket = serverSocket.accept();
                if (socket == null) {
                    return;
                }

                final var clientSocket = new ThreadPerRequestScheduler();
                clientSocket.schedule(new ClientRequestProcessor(socket));

            } catch (IOException e) {
                if (!keepProcessing) {
                    System.out.println("Server stopped gracefully.");
                    break; // 정상 종료
                } else {
                    throw new RuntimeException(e); // 예외 처리
                }
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public void stopProcessing() {
        keepProcessing = false;
        closeIgnoringException(serverSocket);
    }

    private void closeIgnoringException(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {}
        }
    }
}
