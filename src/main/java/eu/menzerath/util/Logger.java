package eu.menzerath.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class Logger {
    private static File logfile;

    /**
     * Ändert die Datei, in der alle Log-Meldungen gespeichert werden
     *
     * @param pLogfile Neue Log-Datei
     */
    public static void setLogfile(File pLogfile) {
        logfile = pLogfile;
    }

    /**
     * Zugriff auf eine Datei / ein Verzeichnis
     *
     * @param file Datei auf die zugegriffen wurde
     * @param ip   IP-Adresse des Clients
     */
    public static void access(String file, String ip) {
        write("[200] [" + ip.replace("/", "") + "] " + file);
    }

    /**
     * Fehler beim Zugriff (403, 404, ...)
     *
     * @param code HTTP-Status-Code
     * @param file Datei auf die zugegriffen werden sollte
     * @param ip   IP-Adresse des Clients
     */
    public static void error(int code, String file, String ip) {
        write("[" + code + "] [" + ip.replace("/", "") + "] " + file);
    }

    /**
     * Interner Fehler (Abbruch eines Streams, ...)
     *
     * @param message Inhalt / Grund der Exception
     */
    public static void exception(String message) {
        write("[EXC] " + message);
    }

    /**
     * Gibt die Log-Meldung auf der Konsole aus und speichert sie in der Log-Datei
     *
     * @param message Inhalt der Meldung
     */
    private static void write(String message) {
        // Zusammensetzung der Meldung
        String out = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "] " + message;

        // Ausgabe auf der Konsole
        System.out.println(out);

        // Schreibe in Datei
        if (logfile != null) {
            try {
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(logfile, true));
                printWriter.append(out).append("\r\n");
                printWriter.close();
            } catch (IOException ignored) {
            }
        }
    }
}