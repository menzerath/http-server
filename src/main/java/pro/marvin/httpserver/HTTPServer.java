package pro.marvin.httpserver;

import pro.marvin.util.ServerHelper;
import pro.marvin.util.console.ConsoleWindow;
import pro.marvin.util.logger.Logger;

import java.awt.*;
import java.io.File;

public class HTTPServer {
	private int port;
	private File webRoot;
	private boolean directoryListingAllowed;
	private Logger logger;

	/**
	 * Entry-Point of this application; creates a HTTP-Server-Object
	 *
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
	 * Constructor; creates (if needed) some directories and starts a ThreadPooledServer
	 */
	public HTTPServer(int port, final File webRoot, final boolean allowDirectoryListing, File logfile) {
		this.port = port;
		this.webRoot = webRoot;
		this.directoryListingAllowed = allowDirectoryListing;
		this.logger = new Logger(logfile);

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

		// a new ThreadPooledServer will handle all the requests
		new Thread(new ThreadPooledServer(this)).start();
	}

	public int getPort() {
		return port;
	}

	public File getWebRoot() {
		return webRoot;
	}

	public boolean isDirectoryListingAllowed() {
		return directoryListingAllowed;
	}

	public Logger getLogger() {
		return logger;
	}
}