package java.net;

import java.io.IOException;

/* compiled from: URLConnection.java */
/* loaded from: rt.jar:java/net/UnknownContentHandler.class */
class UnknownContentHandler extends ContentHandler {
    static final ContentHandler INSTANCE = new UnknownContentHandler();

    UnknownContentHandler() {
    }

    @Override // java.net.ContentHandler
    public Object getContent(URLConnection uRLConnection) throws IOException {
        return uRLConnection.getInputStream();
    }
}
