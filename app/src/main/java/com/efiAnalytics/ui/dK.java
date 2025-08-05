package com.efiAnalytics.ui;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.PropertiesManager;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dK.class */
public class dK {

    /* renamed from: a, reason: collision with root package name */
    SwingController f11335a;

    /* renamed from: b, reason: collision with root package name */
    JDialog f11336b;

    /* renamed from: c, reason: collision with root package name */
    InterfaceC1662et f11337c = null;

    public dK(Window window) {
        this.f11335a = null;
        this.f11336b = null;
        this.f11335a = new SwingController();
        Properties properties = new Properties();
        properties.setProperty("application.showLocalStorageDialogs", Boolean.FALSE.toString());
        PropertiesManager propertiesManager = new PropertiesManager(System.getProperties(), properties, ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));
        SwingViewBuilder swingViewBuilder = new SwingViewBuilder(this.f11335a, propertiesManager);
        propertiesManager.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION, Boolean.FALSE.booleanValue());
        propertiesManager.setBoolean(PropertiesManager.PROPERTY_SHOW_UTILITY_SAVE, Boolean.FALSE.booleanValue());
        propertiesManager.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ROTATE, Boolean.FALSE.booleanValue());
        propertiesManager.setInt(PropertiesManager.PROPERTY_DEFAULT_PAGEFIT, 4);
        JPanel jPanelBuildViewerPanel = swingViewBuilder.buildViewerPanel();
        ComponentKeyBinding.install(this.f11335a, jPanelBuildViewerPanel);
        this.f11335a.getDocumentViewController().setAnnotationCallback(new MyAnnotationCallback(this.f11335a.getDocumentViewController()));
        this.f11336b = new JDialog(window);
        this.f11336b.setDefaultCloseOperation(1);
        this.f11336b.setLayout(new BorderLayout());
        this.f11336b.add(BorderLayout.CENTER, jPanelBuildViewerPanel);
        this.f11336b.setSize(900, 600);
        bV.a((Component) window, (Component) this.f11336b);
        this.f11336b.setVisible(true);
    }

    public void a(URL url) {
        if (url.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
            String str = "";
            if (url.getHost() != null && url.getHost().length() > 0) {
                str = str + url.getHost() + CallSiteDescriptor.TOKEN_DELIMITER;
            }
            try {
                str = str + URLDecoder.decode(url.getFile(), "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                Logger.getLogger(dK.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (new File(str).exists()) {
                this.f11335a.openDocument(str);
            } else {
                JOptionPane.showMessageDialog(this.f11336b, "File not found at:\n" + str, "PDF file not found!", 0);
            }
        } else {
            this.f11335a.openDocument(url.getFile());
        }
        if (url.getRef() != null && url.getRef().contains("page=")) {
            String strSubstring = url.getRef().substring("page=".length());
            if (strSubstring.contains("&")) {
                strSubstring = strSubstring.substring(0, strSubstring.indexOf("&"));
            }
            try {
                int i2 = Integer.parseInt(strSubstring) - 1;
                this.f11335a.showPage(i2);
                bH.C.c("Set pdf to page: " + i2);
            } catch (NumberFormatException e3) {
                bH.C.c("Bad Page: " + strSubstring);
            }
        } else if (url.getRef() != null && url.getRef().length() > 0) {
            String ref = url.getRef();
            if (ref.contains("&")) {
                ref = ref.substring(0, ref.indexOf("&"));
            }
            Destination destinationA = a(ref);
            if (destinationA != null) {
                this.f11335a.getDocumentViewController().setDestinationTarget(destinationA);
            }
        }
        this.f11335a.setPageViewMode(2, false);
    }

    private Destination a(String str) {
        Dictionary destinations;
        ArrayList arrayList;
        Destination destination = null;
        if (this.f11335a.getDocument() != null && this.f11335a.getDocument().getCatalog() != null && (destinations = this.f11335a.getDocument().getCatalog().getDestinations()) != null) {
            HashMap<Object, Object> entries = destinations.getEntries();
            Iterator<Object> it = entries.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Name name = (Name) it.next();
                if (name.equals(str) && (arrayList = (ArrayList) entries.get(name)) != null) {
                    destination = new Destination(destinations.getLibrary(), arrayList);
                    break;
                }
            }
        }
        return destination;
    }

    public void a(boolean z2) {
        this.f11336b.setVisible(z2);
    }
}
