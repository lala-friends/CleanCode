package main.java.appendix.multi;

import java.io.IOException;
import java.net.Socket;

public class ClientRequestProcessor {
    private final Socket socket;
    public ClientRequestProcessor(final Socket socket) {
        this.socket = socket;
    }

    public void process() throws IOException, InterruptedException {
        final var message = MessageUtils.getMessage(socket);
        Thread.sleep(1000);
        MessageUtils.sendMessage(socket, "Processed: " + message);

    }
}
