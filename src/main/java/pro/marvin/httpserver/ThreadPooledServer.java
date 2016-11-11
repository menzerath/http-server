package pro.marvin.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPooledServer implements Runnable {
    protected HTTPServer server;
    protected ExecutorService threadPool;

    /**
     * This ThreadPooledServer makes sure that only 10 Threads are running at the same time to server requests
     *
     * @param server Calling HTTPServer having all the required parameters
     */
    public ThreadPooledServer(HTTPServer server) {
        this.server = server;
    }

    /**
     * Start this Thread and open the Pool for 10 Threads
     */
    public void run() {
        threadPool = Executors.newFixedThreadPool(10);

        ServerSocket socket = null;
        try {
            socket = new ServerSocket(server.getPort());
        } catch (IOException | IllegalArgumentException e) {
            server.getLogger().exception(e.getMessage());
            server.getLogger().exception("Exiting...");
            System.exit(1);
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                threadPool.execute(new HTTPThread(server, socket.accept()));
            } catch (IOException e) {
                server.getLogger().exception(e.getMessage());
            }
        }
    }
}