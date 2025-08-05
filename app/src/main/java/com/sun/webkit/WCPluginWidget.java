package com.sun.webkit;

import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.network.URLs;
import com.sun.webkit.plugin.Plugin;
import com.sun.webkit.plugin.PluginListener;
import com.sun.webkit.plugin.PluginManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/WCPluginWidget.class */
final class WCPluginWidget extends WCWidget implements PluginListener {
    private static final Logger log = Logger.getLogger(WCPluginWidget.class.getName());
    private final Plugin plugin;
    private long pData;

    private static native void initIDs();

    private native WCRectangle twkConvertToPage(WCRectangle wCRectangle);

    private native void twkInvalidateWindowlessPluginRect(int i2, int i3, int i4, int i5);

    private native void twkSetPlugunFocused(boolean z2);

    static {
        initIDs();
    }

    private WCPluginWidget(WebPage webPage, Plugin plugin, int width, int height) {
        super(webPage);
        this.pData = 0L;
        this.plugin = plugin;
        setBounds(0, 0, width, height);
        WebPageClient wpc = webPage.getPageClient();
        this.plugin.activate(null == wpc ? null : wpc.getContainer(), this);
    }

    @Override // com.sun.webkit.WCWidget
    protected void requestFocus() {
        this.plugin.requestFocus();
    }

    private static WCPluginWidget create(WebPage webPage, int width, int height, String urlString, String mimeType, String[] pNames, String[] pValues) {
        URL url = null;
        try {
            url = URLs.newURL(urlString);
        } catch (MalformedURLException ex) {
            log.log(Level.FINE, (String) null, (Throwable) ex);
        }
        return new WCPluginWidget(webPage, PluginManager.createPlugin(url, mimeType, pNames, pValues), width, height);
    }

    private void fwkSetNativeContainerBounds(int x2, int y2, int width, int height) {
        this.plugin.setNativeContainerBounds(x2, y2, width, height);
    }

    @Override // com.sun.webkit.WCWidget
    void setBounds(int x2, int y2, int width, int height) {
        super.setBounds(x2, y2, width, height);
        this.plugin.setBounds(x2, y2, width, height);
    }

    private void setEnabled(boolean enabled) {
        this.plugin.setEnabled(enabled);
    }

    @Override // com.sun.webkit.WCWidget
    protected void setVisible(boolean visible) {
        this.plugin.setVisible(visible);
    }

    @Override // com.sun.webkit.WCWidget
    protected void destroy() {
        this.pData = 0L;
        this.plugin.destroy();
    }

    private void paint(WCGraphicsContext g2, int x2, int y2, int width, int height) {
        WCRectangle bd2 = getBounds();
        WCRectangle clip = bd2.intersection(new WCRectangle(x2, y2, width, height));
        if (!clip.isEmpty()) {
            g2.translate(bd2.getX(), bd2.getY());
            clip.translate(-bd2.getX(), -bd2.getY());
            g2.setClip(clip.getIntX(), clip.getIntY(), clip.getIntWidth(), clip.getIntHeight());
            this.plugin.paint(g2, clip.getIntX(), clip.getIntY(), clip.getIntWidth(), clip.getIntHeight());
        }
    }

    private boolean fwkHandleMouseEvent(String type, int offsetX, int offsetY, int screenX, int screenY, int button, boolean buttonDown, boolean altKey, boolean metaKey, boolean ctrlKey, boolean shiftKey, long timeStamp) {
        return this.plugin.handleMouseEvent(type, offsetX, offsetY, screenX, screenY, button, buttonDown, altKey, metaKey, ctrlKey, shiftKey, timeStamp);
    }

    @Override // com.sun.webkit.plugin.PluginListener
    public void fwkRedraw(int x2, int y2, int width, int height, boolean eraseBackground) {
        twkInvalidateWindowlessPluginRect(x2, y2, width, height);
    }

    @Override // com.sun.webkit.plugin.PluginListener
    public String fwkEvent(int eventId, String name, String params) {
        if (-1 == eventId && Boolean.parseBoolean(params)) {
            twkSetPlugunFocused(Boolean.valueOf(params).booleanValue());
            return "";
        }
        return "";
    }
}
