package eu.menzerath.httpserver;

import eu.menzerath.util.ServerHelper;
import eu.menzerath.util.FileManager;
import eu.menzerath.util.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class HTTPThread extends Thread {
    private Socket socket;
    private File webRoot;
    private boolean allowDirectoryListing;
    private Logger logger;

    /**
     * Konstruktor; speichert die übergebenen Daten
     *
     * @param socket                verwendeter Socket
     * @param webRoot               Pfad zum Hauptverzeichnis
     * @param allowDirectoryListing Sollen Verzeichnisinhalte aufgelistet werden, falls keine Index-Datei vorliegt?
     */
    public HTTPThread(Socket socket, File webRoot, boolean allowDirectoryListing, Logger logger) {
        this.socket = socket;
        this.webRoot = webRoot;
        this.allowDirectoryListing = allowDirectoryListing;
        this.logger = logger;
    }

    /**
     * "Herz" des Servers: Verarbeitet den Request des Clients, und sendet schließlich die Response
     */
    public void run() {
        // Vorbereitung und Einrichtung des BufferedReader und BufferedOutputStream
        // zum Lesen des Requests und zur Ausgabe der Response
        final BufferedReader in;
        final BufferedOutputStream out;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
            out = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.exception(e.getMessage());
            return;
        }

        // Timeout für die Verbindung von 30 Sekunden
        // Verhindert zu viele offengehaltene Verbindungen, aber auch Übertragung von großen Dateien
        try {
            socket.setSoTimeout(30000);
        } catch (SocketException e) {
            logger.exception(e.getMessage());
        }

        // Lesen des Request
        String line;
        ArrayList<String> request = new ArrayList<>();
        try {
            while ((line = in.readLine()) != null && line.length() != 0 && !line.equals("")) {
                request.add(line);
            }
        } catch (IOException e) {
            // Request konnte nicht (korrekt) gelesen werden
            sendError(out, 400, "Bad Request");
            logger.exception(e.getMessage());
            return;
        }

        // Request war leer; sollte nicht auftreten
        if (request.isEmpty()) return;

        // Nur Requests mit dem HTTP 1.0 / 1.1 Protokoll erlaubt
        if (!request.get(0).endsWith(" HTTP/1.0") && !request.get(0).endsWith(" HTTP/1.1")) {
            sendError(out, 400, "Bad Request");
            logger.error(400, "Bad Request: " + request.get(0), socket.getInetAddress().toString());
            return;
        }

        // Es muss ein GET- oder POST-Request sein
        boolean isPostRequest = false;
        if (!request.get(0).startsWith("GET ")) {
            if (request.get(0).startsWith("POST ")) {
                // POST-Requests werden gesondert behandelt
                isPostRequest = true;
            } else {
                // Methode nicht implementiert oder unbekannt
                sendError(out, 501, "Not Implemented");
                logger.error(501, "Not Implemented: " + request.get(0), socket.getInetAddress().toString());
                return;
            }
        }

        // Auf welche Datei / welchen Pfad wird zugegriffen?
        String wantedFile;
        String path;
        File file;

        // GET-Request ist wahrscheinlicher, daher wird zuerst diese Methode angenommen
        wantedFile = request.get(0).substring(4, request.get(0).length() - 9);
        if (isPostRequest) wantedFile = request.get(0).substring(5, request.get(0).length() - 9);

        // GET-Request mit Argumenten: Entferne diese für die Pfad-Angabe
        if (!isPostRequest && request.get(0).contains("?")) {
            path = wantedFile.substring(0, wantedFile.indexOf("?"));
        } else {
            path = wantedFile;
        }

        // Bestimme nun die exakte Datei, bzw. das Verzeichnis, welche(s) angefordert wurde
        try {
            file = new File(webRoot, URLDecoder.decode(path, "UTF-8")).getCanonicalFile();
        } catch (IOException e) {
            logger.exception(e.getMessage());
            return;
        }

        // Falls ein Verzeichnis angezeigt werden soll, und eine Index-Datei vorhanden ist
        // soll letztere angezeigt werden
        if (file.isDirectory()) {
            File indexFile = new File(file, "index.html");
            if (indexFile.exists() && !indexFile.isDirectory()) {
                file = indexFile;

                // "/index.html" an Verzeichnispfad anhängen
                if (wantedFile.contains("?")) {
                    wantedFile = wantedFile.substring(0, wantedFile.indexOf("?")) + "/index.html" + wantedFile.substring(wantedFile.indexOf("?"));
                }
            }
        }

        if (!file.toString().startsWith(ServerHelper.getCanonicalWebRoot(webRoot))) {
            // Datei liegt nicht innerhalb des Web-Roots: Zugriff verhindern und
            // Fehlerseite senden
            sendError(out, 403, "Forbidden");
            logger.error(403, wantedFile, socket.getInetAddress().toString());
            return;
        } else if (!file.exists()) {
            // Datei existiert nicht: Fehlerseite senden
            sendError(out, 404, "Not Found");
            logger.error(404, wantedFile, socket.getInetAddress().toString());
            return;
        } else if (file.isDirectory()) {
            // Innerhalb eines Verzeichnis: Auflistung aller Dateien

            // Verzeichnisauflistung verboten?
            if (!allowDirectoryListing) {
                // Fehlermeldung senden
                sendError(out, 403, "Forbidden");
                logger.error(403, wantedFile, socket.getInetAddress().toString());
                return;
            }

            // Ersetze alle "%20"-Leerzeichen mit einem "echten" Leerzeichen
            path = path.replace("%20", " ");

            File[] files = file.listFiles();

            // Das Verzeichnis ist leer? Sende eine entsprechende Fehlermeldung
            if (files != null) {
                if (files.length == 0) {
                    sendError(out, 404, "Not Found");
                    logger.error(404, wantedFile, socket.getInetAddress().toString());
                    return;
                }
            } else {
                // Kann unter Umständen auf Windows-Systemen vorkommen
                // Beispiel: Aufruf von "Documents and Settings" anstelle von "Users"
                sendError(out, 403, "Forbidden");
                logger.error(403, wantedFile, socket.getInetAddress().toString());
                return;
            }

            // Alle Einträge alphabetisch sortieren: Zuerst Ordner, danach Dateien
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    if (f1.isDirectory() && !f2.isDirectory()) {
                        return -1;
                    } else if (!f1.isDirectory() && f2.isDirectory()) {
                        return 1;
                    } else {
                        return f1.toString().compareToIgnoreCase(f2.toString());
                    }
                }
            });

            // Ausgabe in einer Tabelle vorbereiten
            String content = "<table><tr><th></th><th>Name</th><th>Last modified</th><th>Size</th></tr>";

            // Einen "Ebene höher"-Eintrag anlegen, falls nicht im Web-Root gearbeitet wird
            if (!path.equals("/")) {
                String parentDirectory = path.substring(0, path.length() - 1);
                int lastSlash = parentDirectory.lastIndexOf("/");
                if (lastSlash > 1) {
                    parentDirectory = parentDirectory.substring(0, lastSlash);
                } else {
                    parentDirectory = "/";
                }

                content += "<tr><td class=\"center\"><div class=\"back\">&nbsp;</div></td>" +
                        "<td><a href=\"" + parentDirectory.replace(" ", "%20") + "\">Parent Directory</a></td>" +
                        "<td></td>" +
                        "<td></td></tr>";
            }

            if (path.equals("/")) path = ""; // Anpassung für Dateiauflistung

            // Jede Datei zur Ausgabe hinzufügen
            for (File myFile : files) {
                // Meta-Daten der Datei abrufen
                String filename = myFile.getName();
                String img;
                String fileSize = FileManager.getReadableFileSize(myFile.length());
                if (myFile.isDirectory()) {
                    img = "<div class=\"folder\">&nbsp;</div>";
                    fileSize = "";
                } else {
                    img = "<div class=\"file\">&nbsp;</div>";
                }

                // Datei in die Tabelle einfügen
                content += "<tr><td class=\"center\">" + img + "</td>" +
                        "<td><a href=\"" + path.replace(" ", "%20") + "/" + filename.replace(" ", "%20") + "\">" + filename + "</a></td>" +
                        "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(myFile.lastModified()) + "</td>" +
                        "<td class=\"center\">" + fileSize + "</td></tr>";
            }

            if (path.equals("")) path = "/"; // Rück-Anpassung für spätere Verwendung

            // Tabelle schließen und mit Template zusammenfügen
            content += "</table>";
            String output = WebResources.getDirectoryTemplate("Index of " + path, content);

            // Abschließenden Slash an Verzeichnis anhängen
            if (!wantedFile.endsWith("/")) wantedFile += "/";

            // Header und Inhalt senden
            sendHeader(out, 200, "OK", "text/html", -1, System.currentTimeMillis());
            logger.access(wantedFile, socket.getInetAddress().toString());
            try {
                out.write(output.getBytes());
            } catch (IOException e) {
                logger.exception(e.getMessage());
            }
        } else {
            // Eine einzelne Datei wurde angefordert: Ausgabe via InputStream

            // InputStream vorbereiten
            InputStream reader = null;
            try {
                reader = new BufferedInputStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                logger.exception(e.getMessage());
            }

            // Datei existiert (erstaunlicherweise) nicht (mehr)
            if (reader == null) {
                sendError(out, 404, "Not Found");
                logger.error(404, wantedFile, socket.getInetAddress().toString());
                return;
            }

            // Falls es keinen festgelegten ContentType zur Dateiendung gibt, wird der Download gestartet
            String contentType = FileManager.getContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Header senden, Zugriff loggen und Datei senden
            sendHeader(out, 200, "OK", contentType, file.length(), file.lastModified());
            logger.access(wantedFile, socket.getInetAddress().toString());
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                reader.close();
            } catch (NullPointerException | IOException e) {
                // Wirft eine "Broken Pipe" oder "Socket Write Error" Exception,
                // wenn der Download / Stream abgebrochen wird
                logger.exception(e.getMessage());
            }
        }

        // OutputStream leeren und schließen
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.exception(e.getMessage());
        }
    }

    /**
     * Sende den HTTP 1.1 Header zum Client
     *
     * @param out           Genutzter OutputStream
     * @param code          Status-Code, der gesendet werden soll
     * @param codeMessage   Zum Status-Code gehörende Nachricht
     * @param contentType   ContentType des Inhalts
     * @param contentLength Größe des Inhalts
     * @param lastModified  Wann die Datei zuletzt verändert wurde (zum Caching des Browsers)
     */
    private void sendHeader(BufferedOutputStream out, int code, String codeMessage, String contentType, long contentLength, long lastModified) {
        try {
            out.write(("HTTP/1.1 " + code + " " + codeMessage + "\r\n" +
                    "Date: " + new Date().toString() + "\r\n" +
                    "Server: Marvins HTTP-Server\r\n" +
                    "Content-Type: " + contentType + "; charset=utf-8\r\n" +
                    ((contentLength != -1) ? "Content-Length: " + contentLength + "\r\n" : "") +
                    "Last-modified: " + new Date(lastModified).toString() + "\r\n" +
                    "\r\n").getBytes());
        } catch (IOException e) {
            logger.exception(e.getMessage());
        }
    }

    /**
     * Sendet eine Fehlerseite zum Browser
     *
     * @param out     Genutzter OutputStream
     * @param code    Fehler-Code, der gesendet werden soll (403, 404, ...)
     * @param message Zusätzlicher Text ("Not Found", ...)
     */
    private void sendError(BufferedOutputStream out, int code, String message) {
        // Bereitet Daten der Response vor
        String output = WebResources.getErrorTemplate("Error " + code + ": " + message);

        // Sendet Header der Response
        sendHeader(out, code, message, "text/html", output.length(), System.currentTimeMillis());

        try {
            // Sendet Daten der Response
            out.write(output.getBytes());
            out.flush();
            out.close();

            // Schließt den Socket; "keep-alive" wird also ignoriert
            socket.close();
        } catch (IOException e) {
            logger.exception(e.getMessage());
        }
    }
}