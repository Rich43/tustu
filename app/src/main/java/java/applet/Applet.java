package java.applet;

import java.awt.AWTPermission;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Panel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import sun.applet.AppletAudioClip;

/* loaded from: rt.jar:java/applet/Applet.class */
public class Applet extends Panel {
    private transient AppletStub stub;
    private static final long serialVersionUID = -5836846270535785031L;
    AccessibleContext accessibleContext = null;

    public Applet() throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        objectInputStream.defaultReadObject();
    }

    public final void setStub(AppletStub appletStub) {
        SecurityManager securityManager;
        if (this.stub != null && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(new AWTPermission("setAppletStub"));
        }
        this.stub = appletStub;
    }

    public boolean isActive() {
        if (this.stub != null) {
            return this.stub.isActive();
        }
        return false;
    }

    public URL getDocumentBase() {
        return this.stub.getDocumentBase();
    }

    public URL getCodeBase() {
        return this.stub.getCodeBase();
    }

    public String getParameter(String str) {
        return this.stub.getParameter(str);
    }

    public AppletContext getAppletContext() {
        return this.stub.getAppletContext();
    }

    @Override // java.awt.Component
    public void resize(int i2, int i3) {
        Dimension size = size();
        if (size.width != i2 || size.height != i3) {
            super.resize(i2, i3);
            if (this.stub != null) {
                this.stub.appletResize(i2, i3);
            }
        }
    }

    @Override // java.awt.Component
    public void resize(Dimension dimension) {
        resize(dimension.width, dimension.height);
    }

    @Override // java.awt.Container
    public boolean isValidateRoot() {
        return true;
    }

    public void showStatus(String str) {
        getAppletContext().showStatus(str);
    }

    public Image getImage(URL url) {
        return getAppletContext().getImage(url);
    }

    public Image getImage(URL url, String str) {
        try {
            return getImage(new URL(url, str));
        } catch (MalformedURLException e2) {
            return null;
        }
    }

    public static final AudioClip newAudioClip(URL url) {
        return new AppletAudioClip(url);
    }

    public AudioClip getAudioClip(URL url) {
        return getAppletContext().getAudioClip(url);
    }

    public AudioClip getAudioClip(URL url, String str) {
        try {
            return getAudioClip(new URL(url, str));
        } catch (MalformedURLException e2) {
            return null;
        }
    }

    public String getAppletInfo() {
        return null;
    }

    @Override // java.awt.Component
    public Locale getLocale() {
        Locale locale = super.getLocale();
        if (locale == null) {
            return Locale.getDefault();
        }
        return locale;
    }

    public String[][] getParameterInfo() {
        return (String[][]) null;
    }

    public void play(URL url) {
        AudioClip audioClip = getAudioClip(url);
        if (audioClip != null) {
            audioClip.play();
        }
    }

    public void play(URL url, String str) {
        AudioClip audioClip = getAudioClip(url, str);
        if (audioClip != null) {
            audioClip.play();
        }
    }

    public void init() {
    }

    public void start() {
    }

    public void stop() {
    }

    public void destroy() {
    }

    @Override // java.awt.Panel, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleApplet();
        }
        return this.accessibleContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:java/applet/Applet$AccessibleApplet.class */
    public class AccessibleApplet extends Panel.AccessibleAWTPanel {
        private static final long serialVersionUID = 8127374778187708896L;

        protected AccessibleApplet() {
            super();
        }

        @Override // java.awt.Panel.AccessibleAWTPanel, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.FRAME;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            accessibleStateSet.add(AccessibleState.ACTIVE);
            return accessibleStateSet;
        }
    }
}
