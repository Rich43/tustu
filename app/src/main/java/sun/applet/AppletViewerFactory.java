package sun.applet;

import java.awt.MenuBar;
import java.net.URL;
import java.util.Hashtable;

/* loaded from: rt.jar:sun/applet/AppletViewerFactory.class */
public interface AppletViewerFactory {
    AppletViewer createAppletViewer(int i2, int i3, URL url, Hashtable hashtable);

    MenuBar getBaseMenuBar();

    boolean isStandalone();
}
