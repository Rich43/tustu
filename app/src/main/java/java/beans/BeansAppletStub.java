package java.beans;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;

/* compiled from: Beans.java */
/* loaded from: rt.jar:java/beans/BeansAppletStub.class */
class BeansAppletStub implements AppletStub {
    transient boolean active;
    transient Applet target;
    transient AppletContext context;
    transient URL codeBase;
    transient URL docBase;

    BeansAppletStub(Applet applet, AppletContext appletContext, URL url, URL url2) {
        this.target = applet;
        this.context = appletContext;
        this.codeBase = url;
        this.docBase = url2;
    }

    @Override // java.applet.AppletStub
    public boolean isActive() {
        return this.active;
    }

    @Override // java.applet.AppletStub
    public URL getDocumentBase() {
        return this.docBase;
    }

    @Override // java.applet.AppletStub
    public URL getCodeBase() {
        return this.codeBase;
    }

    @Override // java.applet.AppletStub
    public String getParameter(String str) {
        return null;
    }

    @Override // java.applet.AppletStub
    public AppletContext getAppletContext() {
        return this.context;
    }

    @Override // java.applet.AppletStub
    public void appletResize(int i2, int i3) {
    }
}
