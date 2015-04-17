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
     * Gibt den "anerkannten" Webroot des Servers zurück; dh. einen validen und absoluten Pfad zum Ordner
     * @return Pfad zum Webroot
     */
    public static String getCanonicalWebRoot(File webRoot) {
        String canonicalWebRoot = "";
        try {
            canonicalWebRoot = webRoot.getCanonicalPath();
        } catch (IOException ignored) {
        }
        return canonicalWebRoot;
    }
}