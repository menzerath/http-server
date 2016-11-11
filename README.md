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
	* Directory-Listing
	* simple 403 and 404 error-pages

## How To

### Requirements
* Java (Version 7 oder höher)

### Download
Download a current `HTTP-Server.jar`-file from [GitHub Releases](https://github.com/MarvinMenzerath/HTTP-Server/releases) or compile the application on your own using Maven.

### Start
Double-click the application to serve the current directory you are in or use the commandline to customize a few parts of the configuration.  
Important: If a graphical environment is available, the application will open a new window and show every log-entry in there.

#### Custom WebRoot and Log-File
```
java -jar HTTP-Server.jar [PORT] [WEBROOT] [ALLOW_DIRECTORY_LISTING] [LOG_FILE]
java -jar HTTP-Server.jar 80 /var/www true /var/log/http-server.log
```

#### Custom WebRoot and no Log-File
```
java -jar HTTP-Server.jar [PORT] [WEBROOT] [ALLOW_DIRECTORY_LISTING]
java -jar HTTP-Server.jar 80 /var/www true
```

#### Default Start
```
java -jar HTTP-Server.jar
java -jar HTTP-Server.jar 80 ./ true ../log.txt
```
