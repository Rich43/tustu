package sun.net.www.protocol.mailto;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/* loaded from: rt.jar:sun/net/www/protocol/mailto/Handler.class */
public class Handler extends URLStreamHandler {
    @Override // java.net.URLStreamHandler
    public synchronized URLConnection openConnection(URL url) {
        return new MailToURLConnection(url);
    }

    @Override // java.net.URLStreamHandler
    public void parseURL(URL url, String str, int i2, int i3) {
        String protocol = url.getProtocol();
        int port = url.getPort();
        String strSubstring = "";
        if (i2 < i3) {
            strSubstring = str.substring(i2, i3);
        }
        boolean z2 = false;
        if (strSubstring == null || strSubstring.equals("")) {
            z2 = true;
        } else {
            boolean z3 = true;
            for (int i4 = 0; i4 < strSubstring.length(); i4++) {
                if (!Character.isWhitespace(strSubstring.charAt(i4))) {
                    z3 = false;
                }
            }
            if (z3) {
                z2 = true;
            }
        }
        if (z2) {
            throw new RuntimeException("No email address");
        }
        setURLHandler(url, protocol, "", port, strSubstring, null);
    }

    private void setURLHandler(URL url, String str, String str2, int i2, String str3, String str4) {
        setURL(url, str, str2, i2, str3, null);
    }
}
