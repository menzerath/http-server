package eu.menzerath.util;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class ServerHelper {

    /**
     * Gibt die IP-Adresse des Servers im lokalen Netzwerk zurück
     * @return IP-Adresse des Servers
     */
    public static String getServerIp() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
            return "127.0.0.1";
        }
    }

    /**
     * Gibt den "anerkannten" (validen und absoluten) Pfad zu einer Datei/einem Verzeichnis zurück
     * @return Pfad zur Datei / zum Verzeichnis
     */
    public static String getCanonicalPath(File file) {
        String canonicalPath = "";
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException ignored) {
        }
        return canonicalPath;
    }
}