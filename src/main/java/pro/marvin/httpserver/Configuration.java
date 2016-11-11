package pro.marvin.httpserver;

import org.kohsuke.args4j.Option;
import pro.marvin.util.logger.Logger;

import java.io.File;

public class Configuration {
	@Option(name = "--help", aliases = "-h", usage = "print usage help")
	private boolean printHelp = false;

	@Option(name = "--port", aliases = {"-p"}, usage = "port to use")
	private int port = 8080;

	@Option(name = "--directory", aliases = {"-d"}, usage = "web-root directory")
	private File webRoot = new File("./");

	@Option(name = "--directory-listing", aliases = {"-l"}, usage = "allow directory-listing")
	private boolean directoryListingAllowed = false;

	@Option(name = "--logfile", aliases = {"-f"}, usage = "path and name of log-file (if wanted)")
	private File loggerFile = null;

	private Logger logger = new Logger(this.loggerFile);

	public boolean getPrintHelp() {
		return printHelp;
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

	public File getLoggerFile() {
		return loggerFile;
	}

	public Logger getLogger() {
		return logger;
	}
}