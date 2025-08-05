package sun.net.www.content.text;

import java.io.IOException;
import java.net.ContentHandler;
import java.net.URLConnection;

/* loaded from: rt.jar:sun/net/www/content/text/plain.class */
public class plain extends ContentHandler {
    @Override // java.net.ContentHandler
    public Object getContent(URLConnection uRLConnection) {
        try {
            uRLConnection.getInputStream();
            return new PlainTextInputStream(uRLConnection.getInputStream());
        } catch (IOException e2) {
            return "Error reading document:\n" + e2.toString();
        }
    }
}
