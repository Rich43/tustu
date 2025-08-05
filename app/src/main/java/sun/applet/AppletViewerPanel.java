package sun.applet;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

/* loaded from: rt.jar:sun/applet/AppletViewerPanel.class */
class AppletViewerPanel extends AppletPanel {
    static boolean debug = false;
    URL documentURL;
    URL baseURL;
    Hashtable atts;
    private static final long serialVersionUID = 8890989370785545619L;

    AppletViewerPanel(URL url, Hashtable hashtable) {
        String file;
        int iLastIndexOf;
        this.documentURL = url;
        this.atts = hashtable;
        String parameter = getParameter(Constants.ATTRNAME_CODEBASE);
        if (parameter != null) {
            try {
                this.baseURL = new URL(url, parameter.endsWith("/") ? parameter : parameter + "/");
            } catch (MalformedURLException e2) {
            }
        }
        if (this.baseURL == null && (iLastIndexOf = (file = url.getFile()).lastIndexOf(47)) >= 0 && iLastIndexOf < file.length() - 1) {
            try {
                this.baseURL = new URL(url, file.substring(0, iLastIndexOf + 1));
            } catch (MalformedURLException e3) {
            }
        }
        if (this.baseURL == null) {
            this.baseURL = url;
        }
    }

    @Override // java.applet.AppletStub
    public String getParameter(String str) {
        return (String) this.atts.get(str.toLowerCase());
    }

    @Override // java.applet.AppletStub
    public URL getDocumentBase() {
        return this.documentURL;
    }

    @Override // java.applet.AppletStub
    public URL getCodeBase() {
        return this.baseURL;
    }

    @Override // sun.applet.AppletPanel, java.awt.Component
    public int getWidth() {
        String parameter = getParameter(MetadataParser.WIDTH_TAG_NAME);
        if (parameter != null) {
            return Integer.valueOf(parameter).intValue();
        }
        return 0;
    }

    @Override // sun.applet.AppletPanel, java.awt.Component
    public int getHeight() {
        String parameter = getParameter(MetadataParser.HEIGHT_TAG_NAME);
        if (parameter != null) {
            return Integer.valueOf(parameter).intValue();
        }
        return 0;
    }

    @Override // sun.applet.AppletPanel
    public boolean hasInitialFocus() {
        if (isJDK11Applet() || isJDK12Applet()) {
            return false;
        }
        String parameter = getParameter("initial_focus");
        if (parameter != null && parameter.toLowerCase().equals("false")) {
            return false;
        }
        return true;
    }

    @Override // sun.applet.AppletPanel
    public String getCode() {
        return getParameter("code");
    }

    @Override // sun.applet.AppletPanel
    public String getJarFiles() {
        return getParameter(Constants.ATTRNAME_ARCHIVE);
    }

    @Override // sun.applet.AppletPanel
    public String getSerializedObject() {
        return getParameter("object");
    }

    @Override // java.applet.AppletStub
    public AppletContext getAppletContext() {
        return (AppletContext) getParent();
    }

    static void debug(String str) {
        if (debug) {
            System.err.println("AppletViewerPanel:::" + str);
        }
    }

    static void debug(String str, Throwable th) {
        if (debug) {
            th.printStackTrace();
            debug(str);
        }
    }
}
