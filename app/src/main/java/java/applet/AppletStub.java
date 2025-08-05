package java.applet;

import java.net.URL;

/* loaded from: rt.jar:java/applet/AppletStub.class */
public interface AppletStub {
    boolean isActive();

    URL getDocumentBase();

    URL getCodeBase();

    String getParameter(String str);

    AppletContext getAppletContext();

    void appletResize(int i2, int i3);
}
