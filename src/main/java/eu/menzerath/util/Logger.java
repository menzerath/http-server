package eu.menzerath.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Logger {
    private boolean writeToFile;
    private BlockingQueue<String> queue;

    /**
     * Konstruktor; Übergibt die Datei, in der alle Log-Meldungen gespeichert werden, bei Bedarf an die LogfileWriter-Klasse
     *
     * @param logfile Neue Log-Datei
     */
    public Logger(File logfile) {
        if (logfile != null) {
            queue = new ArrayBlockingQueue<>(1024);
            new Thread(new LogfileWriter(queue, logfile)).start();
            writeToFile = true;
        } else {
            writeToFile = false;
        }
    }

    /**
     * Zugriff auf eine Datei / ein Verzeichnis
     *
     * @param file Datei auf die zugegriffen wurde
     * @param ip   IP-Adresse des Clients
     */
    public void access(String file, String ip) {
        write("[200] [" + ip.replace("/", "") + "] " + file);
    }

    /**
     * Fehler beim Zugriff (403, 404, ...)
     *
     * @param code HTTP-Status-Code
     * @param file Datei auf die zugegriffen werden sollte
     * @param ip   IP-Adresse des Clients
     */
    public void error(int code, String file, String ip) {
        write("[" + code + "] [" + ip.replace("/", "") + "] " + file);
    }

    /**
     * Interner Fehler (Abbruch eines Streams, ...)
     *
     * @param message Inhalt / Grund der Exception
     */
    public void exception(String message) {
        write("[EXC] " + message);
    }

    /**
     * Gibt die Log-Meldung auf der Konsole aus und speichert sie in der Log-Datei
     *
     * @param message Inhalt der Meldung
     */
    private void write(String message) {
        // Zusammensetzung der Meldung
        String out = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "] " + message;

        // Ausgabe auf der Konsole
        System.out.println(out);

        // Schreibe in Datei
        if (writeToFile) queue.add(out);
    }
}