package eu.menzerath.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

public class FileManager {
    private static HashMap<String, String> mimeTypes = new HashMap<>();
    private static boolean mimeTypesInitCompleted = false;

    /**
     * Diese HashMap beinhaltet einige Dateiendungen, die vom Webserver nicht mit dem Standard-MimeType ausgeliefert werden sollen
     *
     * @return Alle konfigruierten Dateiendungen mit ihren MimeTypes
     */
    public static HashMap<String, String> getMimeTypes() {
        if (mimeTypesInitCompleted) return mimeTypes;

        // Bilder
        mimeTypes.put(".gif", "image/gif");
        mimeTypes.put(".jpg", "image/jpeg");
        mimeTypes.put(".jpeg", "image/jpeg");
        mimeTypes.put(".png", "image/png");
        mimeTypes.put(".ico", "image/x-ico");

        // Audio
        mimeTypes.put(".mp3", "audio/mpeg");
        mimeTypes.put(".wav", "audio/wav");
        mimeTypes.put(".flac", "audio/flac");
        mimeTypes.put(".ogg", "audio/x-ogg");
        mimeTypes.put(".mp4", "video/mp4");
        mimeTypes.put(".flv", "video/x-flv");

        // Webseiten
        mimeTypes.put(".html", "text/html");
        mimeTypes.put(".htm", "text/html");
        mimeTypes.put(".shtml", "text/html");
        mimeTypes.put(".xhtml", "text/html");
        mimeTypes.put(".css", "text/css");
        mimeTypes.put(".js", "text/js");

        // Linux
        mimeTypes.put(".deb", "application/x-debian-package");

        // Archive
        mimeTypes.put(".zip", "application/zip");
        mimeTypes.put(".tar", "application/x-tar");
        mimeTypes.put(".gtar", "application/x-gtar");
        mimeTypes.put(".tar.gz", "application/gzip");
        mimeTypes.put(".tgz", "application/gzip");
        mimeTypes.put(".gz", "application/gzip");

        // Anderes
        mimeTypes.put(".txt", "text/plain");
        mimeTypes.put(".log", "text/plain");
        mimeTypes.put(".md", "text/x-markdown");
        mimeTypes.put(".pdf", "application/pdf");
        mimeTypes.put(".xml", "application/xml");
        mimeTypes.put(".java", "text/plain");

        mimeTypesInitCompleted = true;
        return mimeTypes;
    }

    /**
     * Wandelt die Dateigröße in einen lesbaren Wert (mit entsprechender Einheit) um
     * Quelle: Mr Ed: Format file size as MB, GB etc, 08.04.2011
     *         http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc, 18.01.2014
     *
     * @param size Dateigröße (in Bits)
     * @return Lesbare Dateigröße
     */
    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Gibt den Content-Type (Mime-Type) für die entsprechende Dateiendung zurück
     *
     * @param file Zu prüfende Datei
     * @return Content-Type der Datei
     */
    public static String getContentType(File file) {
        return getMimeTypes().get(getFileExtension(file));
    }

    /**
     * Gibt die Dateiendung der gewählten Datei zurück (zB: "txt")
     *
     * @param file Zu prüfende Datei
     * @return Dateiendung
     */
    private static String getFileExtension(File file) {
        String filename = file.getName();
        int pos = filename.lastIndexOf(".");
        if (pos >= 0) return filename.substring(pos).toLowerCase();
        return "";
    }
}