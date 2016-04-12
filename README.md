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

## License
The MIT License (MIT)

Copyright (c) 2013-2015 [Marvin Menzerath](https://menzerath.eu)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
