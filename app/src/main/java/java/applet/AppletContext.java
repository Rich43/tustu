package java.applet;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

/* loaded from: rt.jar:java/applet/AppletContext.class */
public interface AppletContext {
    AudioClip getAudioClip(URL url);

    Image getImage(URL url);

    Applet getApplet(String str);

    Enumeration<Applet> getApplets();

    void showDocument(URL url);

    void showDocument(URL url, String str);

    void showStatus(String str);

    void setStream(String str, InputStream inputStream) throws IOException;

    InputStream getStream(String str);

    Iterator<String> getStreamKeys();
}
