package eu.menzerath.httpserver;

import eu.menzerath.util.Logger;
import eu.menzerath.util.ServerHelper;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

public class HTTPServer {

    /**
     * Einstiegspunkt der Anwendung; erstellt ein HTTPServer-Objekt
     * @param args Beim Aufruf übergebene Argumente
     */
    public static void main(String[] args) {
        if (args.length == 4) {
            new HTTPServer(Integer.valueOf(args[0]), new File(args[1]), Boolean.valueOf(args[2]), new File(args[3]));
        } else if (args.length == 3) {
            new HTTPServer(Integer.valueOf(args[0]), new File(args[1]), Boolean.valueOf(args[2]), null);
        } else {
            new HTTPServer(80, new File("./"), true, new File("../log.txt"));
        }
    }

    /**
     * Konstruktor; erstellt (wenn nötig) den Ordner für die Daten und startet schließlich den ConnectionListener
     */
    public HTTPServer(int port, final File webRoot, final boolean allowDirectoryListing, File logfile) {
        Logger.setLogfile(logfile);

        // Gib die IP-Adresse sowie den Port des Servers aus
        // Passe die Ausgabe an die Länge der IP-Adresse + Port an
        String lineOne = "";
        String lineUrl = "### http://" + ServerHelper.getServerIp() + ":" + port + " ###";
        String lineTitle = "### HTTP-Server";
        for (int i = 0; i < lineUrl.length(); i++) lineOne += "#";
        for (int i = 0; i < lineUrl.length() - 18; i++) lineTitle += " ";
        lineTitle += "###";

        // Ausgabe der Informationen
        System.out.println(lineOne);
        System.out.println(lineTitle);
        System.out.println(lineUrl);
        System.out.println(lineOne);

        // Erstellt einen Ordner für die Daten (falls nötig)
        if (!webRoot.exists() && !webRoot.mkdir()) {
            // Ordner existiert nicht & konnte nicht angelegt werden: Abbruch
            Logger.exception("Konnte Daten-Verzeichnis nicht erstellen.");
            Logger.exception("Beende...");
            System.exit(1);
        }

        // Erstelle einen ServerSocket mit dem angegebenen Port
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException | IllegalArgumentException e) {
            // Port bereits belegt, darf nicht genutzt werden, ...: Abbruch
            Logger.exception(e.getMessage());
            Logger.exception("Beende...");
            System.exit(1);
        }

        // Neuer Thread: wartet auf eingehende Verbindungen und "vermittelt" diese an einen neuen HTTPThread, der die Anfrage dann verarbeitet
        final ServerSocket finalSocket = socket;
        Thread connectionListener = new Thread(){
            public void run(){
                while (true) {
                    try {
                        HTTPThread thread = new HTTPThread(finalSocket.accept(), webRoot, allowDirectoryListing);
                        thread.start();
                    } catch (IOException e) {
                        Logger.exception(e.getMessage());
                        Logger.exception("Beende...");
                        System.exit(1);
                    }
                }
            }
        };
        connectionListener.start();
    }
}