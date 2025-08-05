package sun.net.www.content.audio;

import java.io.IOException;
import java.net.ContentHandler;
import java.net.URLConnection;
import sun.applet.AppletAudioClip;

/* loaded from: rt.jar:sun/net/www/content/audio/x_wav.class */
public class x_wav extends ContentHandler {
    @Override // java.net.ContentHandler
    public Object getContent(URLConnection uRLConnection) throws IOException {
        return new AppletAudioClip(uRLConnection);
    }
}
