package org.example.server;

import org.example.server.exception.UnsupportedProtocolException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public final int PORT = 10001;
    private ServerSocket server;

    private final Application application;

    public Server(Application application) {
        this.application = application;
    }

    /**
     * Starts the server.
     */
    public void start() throws IOException {
        System.out.println("Start server...");
        server = new ServerSocket(PORT, 5);
        System.out.println("Server running at: http://localhost:" + PORT);

        run();
    }

    /**
     * Main server loop.
     */
    private void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        while (true) {
            try {
                Socket socket = server.accept();
                executorService.submit(new RequestHandler(socket, application));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
