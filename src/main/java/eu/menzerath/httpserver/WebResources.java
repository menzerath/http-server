package eu.menzerath.httpserver;

/**
 * Diese Klasse beinhaltet Ressourcen, die bei der Dateiauflistung und
 * bei Fehlerseien angezeigt werden:
 * CSS, Header, Footer
 *
 * Icons (base64-encodiert) in der Auflistung der Dateien: erstellt von Yannick Lung (yanlu.de)
 */
public class WebResources {
    private static final String STYLE =
            "html * { font-family: sans-serif;!important }" +
                    "body { margin: 1em }" +
                    "h1 { font-size: 2em; margin: 0 0 .2em 0 }" +
                    "table { border-collapse: collapse; margin-bottom: 1em }" +
                    "th { background: #70a0b2; color: #fff }" +
                    "td, tbody th { border: 1px solid #e1e1e1; font-size: 15px; padding: .5em .3em }" +
                    "tr:hover td { background: #e9edf1 }" +
                    "td.center { text-align: center }" +
                    "div.folder { background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABmJLR0QA/wD/AP+gvaeTAAAAeUlEQVQ4je3UMQqDUAyA4e8VBw/RS/QKHbxPr+HiJUrv0Cu5OBREEKyLg/g6iD6hgz8EkhB+Qobw74RFfUex6LUo0a0RZrP8igfyH3MfVOt2PIAXenx3Ro9nwCC+5VaGMNmTcUkpO4WncIewSeirA27il7WVdyLPgYxpHR5nWoKFpAAAAABJRU5ErkJggg==); background-position: center center; background-repeat:no-repeat; height:20px; width:20px }" +
                    "div.file { background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABmJLR0QA/wD/AP+gvaeTAAAA3klEQVQ4je3TvUoDQRSG4cdgIekEQfACbC0s05pC8Dr2EqxTWlta5AZs7BMk9qb0BmxNEdjGxp/CWZgMM7tZSJHCDw4cds687MzLsOMcRH0VqisPeMYaq3TxMOrPcBw2lFKFuVPc4wbfJSDUWLYA66i/xi3uSsMT/GxRE4xC/4VxDBkk0IW/ey3VIpkfYIqT5kN65Eu8lo6Ac7zgLfmzYQn4gccWYIUrHGXW3nPAbaSMQsX5xDwHvMCsBdiZvlI6RfWVkksjKgvskpLLxnPtKyWX+PXsTMpT0/xL2UMpv3j7OZHPExvvAAAAAElFTkSuQmCC); background-position: center center; background-repeat:no-repeat; height:20px; width:20px }" +
                    "div.back { background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABmJLR0QA/wD/AP+gvaeTAAAA5ElEQVQ4je3UsUpDQRCF4Y+YQAqDIIgQIYVdCFZaikVAiI29teQJItha+gB2eQMLwUZEfAJLWyEWViGNWAQCYlJchWW5JnchnR4YWPbM/Mzsssu/IlVTC0pzvHOcLQNYQR+XKKcC44IN3GA/FfSjlWC9jQfsBnufqGMPDaxiiK9F4AOMMC0QH7jG4W+wU0wKwuK4w1YIK2EtGj1FR3hCK894X9DNGAO8yc429IbYjKFNvESJ9+jIbj9UBTvo4fk79zZvhHU8BsCLAmPDMV7RzjPLuEoEQg3deQknsue3VCV/Dn9QMzw6REIVdJIPAAAAAElFTkSuQmCC); background-position: center center; background-repeat:no-repeat; height:20px; width:20px }";

    public static String getDirectoryTemplate(String title, String content) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                "<link rel=\"icon\" type=\"image/png\" href=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABmJLR0QA/wD/AP+gvaeTAAAAeUlEQVQ4je3UMQqDUAyA4e8VBw/RS/QKHbxPr+HiJUrv0Cu5OBREEKyLg/g6iD6hgz8EkhB+Qobw74RFfUex6LUo0a0RZrP8igfyH3MfVOt2PIAXenx3Ro9nwCC+5VaGMNmTcUkpO4WncIewSeirA27il7WVdyLPgYxpHR5nWoKFpAAAAABJRU5ErkJggg==\" />" +
                "<style>" + STYLE + "</style>" +
                "<title>" + title + "</title>" +
                "</head>" +
                "<body>" +
                "<h1>" + title + "</h1>" +
                content +
                "<hr>" +
                "<p>powered by a simple Java <a href=\"https://github.com/MarvinMenzerath/HTTP-Server\" target=\"_blank\">HTTP-Server</a> created by <a href=\"https://github.com/MarvinMenzerath\" target=\"_blank\">Marvin Menzerath</a></p>" +
                "</body>" +
                "</html>";
    }

    public static String getErrorTemplate(String error) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>" + error + "</title>" +
                "</head>" +
                "<body>" +
                "<h1>" + error + "</h1>" +
                "<hr>" +
                "<p>powered by a simple Java <a href=\"https://github.com/MarvinMenzerath/HTTP-Server\" target=\"_blank\">HTTP-Server</a> created by <a href=\"https://github.com/MarvinMenzerath\" target=\"_blank\">Marvin Menzerath</a></p>" +
                "</body>" +
                "</html>";
    }
}