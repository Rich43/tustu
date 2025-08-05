package sun.net.www.protocol.http.logging;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: rt.jar:sun/net/www/protocol/http/logging/HttpLogFormatter.class */
public class HttpLogFormatter extends SimpleFormatter {
    private static volatile Pattern pattern = null;
    private static volatile Pattern cpattern = null;

    public HttpLogFormatter() {
        if (pattern == null) {
            pattern = Pattern.compile("\\{[^\\}]*\\}");
            cpattern = Pattern.compile("[^,\\] ]{2,}");
        }
    }

    @Override // java.util.logging.SimpleFormatter, java.util.logging.Formatter
    public String format(LogRecord logRecord) {
        String sourceClassName = logRecord.getSourceClassName();
        if (sourceClassName == null || (!sourceClassName.startsWith("sun.net.www.protocol.http") && !sourceClassName.startsWith("sun.net.www.http"))) {
            return super.format(logRecord);
        }
        String message = logRecord.getMessage();
        StringBuilder sb = new StringBuilder("HTTP: ");
        if (message.startsWith("sun.net.www.MessageHeader@")) {
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String strSubstring = message.substring(matcher.start() + 1, matcher.end() - 1);
                if (strSubstring.startsWith("null: ")) {
                    strSubstring = strSubstring.substring(6);
                }
                if (strSubstring.endsWith(": null")) {
                    strSubstring = strSubstring.substring(0, strSubstring.length() - 6);
                }
                sb.append("\t").append(strSubstring).append("\n");
            }
        } else if (message.startsWith("Cookies retrieved: {")) {
            String strSubstring2 = message.substring(20);
            sb.append("Cookies from handler:\n");
            while (strSubstring2.length() >= 7) {
                if (strSubstring2.startsWith("Cookie=[")) {
                    String strSubstring3 = strSubstring2.substring(8);
                    int iIndexOf = strSubstring3.indexOf("Cookie2=[");
                    if (iIndexOf > 0) {
                        strSubstring3 = strSubstring3.substring(0, iIndexOf - 1);
                        strSubstring2 = strSubstring3.substring(iIndexOf);
                    } else {
                        strSubstring2 = "";
                    }
                    if (strSubstring3.length() >= 4) {
                        Matcher matcher2 = cpattern.matcher(strSubstring3);
                        while (matcher2.find()) {
                            int iStart = matcher2.start();
                            int iEnd = matcher2.end();
                            if (iStart >= 0) {
                                sb.append("\t").append(strSubstring3.substring(iStart + 1, (iEnd > 0 ? iEnd : strSubstring3.length()) - 1)).append("\n");
                            }
                        }
                    }
                }
                if (strSubstring2.startsWith("Cookie2=[")) {
                    String strSubstring4 = strSubstring2.substring(9);
                    int iIndexOf2 = strSubstring4.indexOf("Cookie=[");
                    if (iIndexOf2 > 0) {
                        strSubstring4 = strSubstring4.substring(0, iIndexOf2 - 1);
                        strSubstring2 = strSubstring4.substring(iIndexOf2);
                    } else {
                        strSubstring2 = "";
                    }
                    Matcher matcher3 = cpattern.matcher(strSubstring4);
                    while (matcher3.find()) {
                        int iStart2 = matcher3.start();
                        int iEnd2 = matcher3.end();
                        if (iStart2 >= 0) {
                            sb.append("\t").append(strSubstring4.substring(iStart2 + 1, (iEnd2 > 0 ? iEnd2 : strSubstring4.length()) - 1)).append("\n");
                        }
                    }
                }
            }
        } else {
            sb.append(message).append("\n");
        }
        return sb.toString();
    }
}
