package eu.menzerath.httpserver;

import eu.menzerath.util.ServerHelper;
import eu.menzerath.util.logger.ConsoleWindow;
import eu.menzerath.util.logger.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

public class HTTPServer {

    /**
     * Entry-Point of this application; creates a HTTP-Server-Object
     * @param args Passed arguments
     */
    public static void main(String[] args) {
        // Always show the ConsoleWindow if possible
        if (!GraphicsEnvironment.isHeadless()) {
            ConsoleWindow.show();
        }

        if (args.length == 4) {
            // Log-File wanted
            new HTTPServer(Integer.valueOf(args[0]), new File(args[1]), Boolean.valueOf(args[2]), new File(args[3]));
        } else if (args.length == 3) {
            // No Log-File wanted
            new HTTPServer(Integer.valueOf(args[0]), new File(args[1]), Boolean.valueOf(args[2]), null);
        } else {
            // Use Default-Parameters
            new HTTPServer(80, new File("./"), true, new File("../log.txt"));
        }
    }

    /**
     * Constructor; creates (if needed) some directories and starts a ConnectionListener
     */
    public HTTPServer(int port, final File webRoot, final boolean allowDirectoryListing, File logfile) {
        final Logger logger = new Logger(logfile);

        // Prints out some information
        System.out.println("##############################################\n### a simple Java HTTP-Server              ###\n" +
                "### github.com/MarvinMenzerath/HTTP-Server ###\n##############################################");
        System.out.println("Current version: " + HTTPServer.class.getPackage().getImplementationVersion());
        System.out.println("Serving at:      " + "http://" + ServerHelper.getServerIp() + ":" + port);
        System.out.println("Directory:       " + ServerHelper.getCanonicalPath(webRoot));

        // Creates a directory for the content to serve (if needed)
        if (!webRoot.exists() && !webRoot.mkdir()) {
            logger.exception("Unable to create webRoot-directory.");
            logger.exception("Exiting...");
            System.exit(1);
        }

        // Open a new ServerSocket
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException | IllegalArgumentException e) {
            // Port in use
            logger.exception(e.getMessage());
            logger.exception("Exiting...");
            System.exit(1);
        }

        // New Thread: waits for incoming connections and hands them over to a new HTTPThread, which handles the request
        final ServerSocket finalSocket = socket;
        Thread connectionListener = new Thread() {
            public void run(){
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        HTTPThread thread = new HTTPThread(finalSocket.accept(), webRoot, allowDirectoryListing, logger);
                        thread.start();
                    } catch (IOException e) {
                        logger.exception(e.getMessage());
                    }
                }
            }
        };
        connectionListener.start();
    }
}