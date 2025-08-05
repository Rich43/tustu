package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.bV;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import javafx.fxml.FXMLLoader;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/HtmlDisplay.class */
public class HtmlDisplay extends AbstractC1420s implements InterfaceC1421t, Serializable {

    /* renamed from: a, reason: collision with root package name */
    JEditorPane f9350a = new JEditorPane(new HTMLEditorKit().getContentType(), "<html><body></body></html>");

    /* renamed from: b, reason: collision with root package name */
    String f9351b = "";

    /* renamed from: c, reason: collision with root package name */
    String f9352c = "body { font-size: 100% }";

    /* renamed from: d, reason: collision with root package name */
    private boolean f9353d = true;

    /* renamed from: f, reason: collision with root package name */
    private boolean f9354f = false;

    public HtmlDisplay() {
        setLayout(new BorderLayout());
        this.f9350a.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f9350a.setEditable(false);
        this.f9350a.setContentType("text/html; charset=UTF-8");
        this.f9350a.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        this.f9350a.setText("<html><body></body></html>");
        ((HTMLDocument) this.f9350a.getDocument()).getStyleSheet().addRule(this.f9352c);
        this.f9350a.addHyperlinkListener(new aI(this));
        add(BorderLayout.CENTER, new JScrollPane(this.f9350a));
    }

    public void setDocumentUrl(String str) throws V.a {
        try {
            String fullLocalUrlFromRelative = str;
            if (!fullLocalUrlFromRelative.startsWith("http") && !fullLocalUrlFromRelative.startsWith(DeploymentDescriptorParser.ATTR_FILE)) {
                fullLocalUrlFromRelative = getFullLocalUrlFromRelative(fullLocalUrlFromRelative);
            }
            ((AbstractDocument) this.f9350a.getDocument()).setAsynchronousLoadPriority(2);
            this.f9350a.getDocument().putProperty(Document.StreamDescriptionProperty, null);
            if (fullLocalUrlFromRelative.toLowerCase().endsWith("html") || fullLocalUrlFromRelative.toLowerCase().endsWith("htm")) {
                this.f9350a.setContentType("text/html; charset=utf-8");
            } else {
                this.f9350a.setContentType("text/plain; charset=utf-8");
            }
            this.f9350a.setPage(fullLocalUrlFromRelative);
            this.f9351b = str;
        } catch (FileNotFoundException e2) {
            bV.d("File Not Found:\n" + e2.getMessage(), this);
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.a("Unable to read file:\n" + str);
        }
    }

    public void setDocumentUrlLazy(String str) {
        new aJ(this, str).start();
    }

    public String getDocumentUrl() {
        return this.f9351b;
    }

    public void setText(String str) {
        this.f9350a.setText(str);
        ((HTMLDocument) this.f9350a.getDocument()).getStyleSheet().addRule(this.f9352c);
    }

    private String getFullLocalUrlFromRelative(String str) {
        try {
            return "file:///" + bH.W.b((str.startsWith("/") || str.startsWith(FXMLLoader.ESCAPE_PREFIX) || str.contains(CallSiteDescriptor.TOKEN_DELIMITER)) ? str : new File(new File(".").getCanonicalPath(), str).getAbsolutePath(), FXMLLoader.ESCAPE_PREFIX, "/");
        } catch (IOException e2) {
            e2.printStackTrace();
            return "file:///." + str;
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void setRunDemo(boolean z2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isRunDemo() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void goDead() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void invalidatePainter() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isMustPaint() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isComponentPaintedAt(int i2, int i3) {
        return i2 >= 0 && i2 < getWidth() && i3 >= 0 && i3 < getHeight();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void subscribeToOutput() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void unsubscribeToOutput() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void paintBackground(Graphics graphics) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean requiresBackgroundRepaint() {
        return true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void updateGauge(Graphics graphics) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public Area areaPainted() {
        return new Area(new Rectangle2D.Double(0.0d, 0.0d, getWidth(), getHeight()));
    }

    public boolean isUseExternalBrowser() {
        return this.f9353d;
    }

    public void setUseExternalBrowser(boolean z2) {
        this.f9353d = z2;
    }

    public boolean isUseExternalBrowserOnlyForHttp() {
        return this.f9354f;
    }

    public void setUseExternalBrowserOnlyForHttp(boolean z2) {
        this.f9354f = z2;
    }
}
