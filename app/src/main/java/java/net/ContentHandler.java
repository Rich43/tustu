package java.net;

import java.io.IOException;

/* loaded from: rt.jar:java/net/ContentHandler.class */
public abstract class ContentHandler {
    public abstract Object getContent(URLConnection uRLConnection) throws IOException;

    public Object getContent(URLConnection uRLConnection, Class[] clsArr) throws IOException {
        Object content = getContent(uRLConnection);
        for (Class cls : clsArr) {
            if (cls.isInstance(content)) {
                return content;
            }
        }
        return null;
    }
}
