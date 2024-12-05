package main.java.appendix.multi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageUtils {
    public static String getMessage(final Socket socket) throws IOException {
        final var stream = socket.getInputStream();
        final var ois = new ObjectInputStream(stream);
        return ois.readUTF();
    }

    public static void sendMessage(final Socket socket, final String message) throws IOException {
        final var stream = socket.getOutputStream();
        final var oos = new ObjectOutputStream(stream);
        oos.writeUTF(message);
        oos.flush();
    }
}
