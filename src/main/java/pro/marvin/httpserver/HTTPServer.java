package pro.marvin.httpserver;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import pro.marvin.util.ServerHelper;
import pro.marvin.util.console.ConsoleWindow;

import java.awt.*;

public class HTTPServer {
	private static Configuration config;

	/**
	 * Entry-Point of this application; creates a HTTP-Server-Object
	 *
	 * @param args Passed arguments
	 */
	public static void main(String[] args) {
		// create default config-object
		config = new Configuration();

		// print welcome head
		System.out.println("##############################################\n" +
				"### a simple Java HTTP-Server              ###\n" +
				"### github.com/MarvinMenzerath/HTTP-Server ###\n" +
				"##############################################");

		// parse arguments and adjust default config-object
		CmdLineParser parser = new CmdLineParser(config);
		try {
			parser.parseArgument(args);
			if (config.getPrintHelp()) {
				parser.printUsage(System.out);
				System.exit(0);
			}
		} catch (CmdLineException e) {
			System.out.println("Error parsing arguments: " + e.getMessage());
			parser.printUsage(System.out);
			System.exit(1);
		}

		// show ConsoleWindow if possible and allowed
		if (!GraphicsEnvironment.isHeadless() && config.getShowGui()) {
			ConsoleWindow.show();
		}

		// start
		new HTTPServer(config);
	}

	/**
	 * Constructor; creates (if needed) some directories and starts a ThreadPooledServer
	 */
	public HTTPServer(Configuration config) {
		// Print out some information
		System.out.println("Current version:   " + HTTPServer.class.getPackage().getImplementationVersion());
		System.out.println("Serving at:        " + "http://" + ServerHelper.getServerIp() + ":" + config.getPort());
		System.out.println("Directory:         " + ServerHelper.getCanonicalPath(config.getWebRoot()));
		System.out.println("Directory Listing: " + (config.isDirectoryListingAllowed() ? "yes" : "no"));
		System.out.println("Logfile:           " + (config.getLoggerFile() != null ? config.getLoggerFile().getAbsoluteFile() : "no"));

		// Creates a directory for the content to serve (if needed)
		if (!config.getWebRoot().exists() && !config.getWebRoot().mkdir()) {
			config.getLogger().exception("Unable to create web-root directory.");
			config.getLogger().exception("Exiting...");
			System.exit(1);
		}

		// a new ThreadPooledServer will handle all the requests
		new Thread(new ThreadPooledServer()).start();
	}

	public static Configuration getConfig() {
		return config;
	}
}