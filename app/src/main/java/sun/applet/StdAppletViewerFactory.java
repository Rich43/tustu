package sun.applet;

import java.awt.MenuBar;
import java.net.URL;
import java.util.Hashtable;

/* compiled from: AppletViewer.java */
/* loaded from: rt.jar:sun/applet/StdAppletViewerFactory.class */
final class StdAppletViewerFactory implements AppletViewerFactory {
    StdAppletViewerFactory() {
    }

    @Override // sun.applet.AppletViewerFactory
    public AppletViewer createAppletViewer(int i2, int i3, URL url, Hashtable hashtable) {
        return new AppletViewer(i2, i3, url, hashtable, System.out, this);
    }

    @Override // sun.applet.AppletViewerFactory
    public MenuBar getBaseMenuBar() {
        return new MenuBar();
    }

    @Override // sun.applet.AppletViewerFactory
    public boolean isStandalone() {
        return true;
    }
}
