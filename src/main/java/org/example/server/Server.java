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

        run(); // call run() method
    }

    /**
     * Main server loop.
     */
    private void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //这里用到了线程池，可以了解一下。线程池里的线程会去执行
        //RequestHandler
        //这个类的run方法。
        while (true) {
            try {
                Socket socket = server.accept();
                executorService.submit(new RequestHandler(socket, application));//execute run method in RequestHandler
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
