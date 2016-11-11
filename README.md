# HTTP-Server
This is a very simple Java HTTP-Server, which allows quick file-transfers and directory-listings.

Originally this small server was developed as a part of my **[Facharbeit zum HTTP-Protokoll](http://menzerath.eu/artikel/wie-funktioniert-das-http-protokoll/)** but now it contains some more features and is a bit more extensible.

## Support
This small HTTP-server currently supports those features:

* Requests
	* `GET`
	* `POST`
	* `HEAD`
* Header
	* `If-Modified-Since`
* Other features
	* directory-listing
	* simple 403 and 404 error-pages

## How To

### Requirements
* Java (Version 8 or higher)

### Download
Download a current `HTTP-Server.jar`-file from [GitHub Releases](https://github.com/MarvinMenzerath/HTTP-Server/releases) or compile the application on your own using Maven.

### Start
Double-click the application to serve the current directory you are in or use the commandline to customize a few parts of the configuration.  
Important: If a graphical environment is available and you did not deactivate the gui, the application will open a new window and show every log-entry in there.

```
$ java -jar HTTP-Server.jar --help
##############################################
### a simple Java HTTP-Server              ###
### github.com/MarvinMenzerath/HTTP-Server ###
##############################################
 --directory (-d) FILE    : web-root directory (default: .)
 --directory-listing (-l) : allow directory-listing (default: false)
 --help (-h)              : print usage help (default: true)
 --logfile (-f) FILE      : path and name of log-file (if wanted)
 --no-gui (-g)            : do not show gui (if possible) (default: false)
 --port (-p) N            : port to use (default: 8080)
 ```