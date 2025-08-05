package com.sun.webkit.plugin;

import com.sun.prism.paint.Color;
import com.sun.webkit.graphics.WCGraphicsContext;
import java.io.IOError;
import java.net.URL;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/plugin/DefaultPlugin.class */
final class DefaultPlugin implements Plugin {
    private static final Logger log = Logger.getLogger("com.sun.browser.plugin.DefaultPlugin");

    /* renamed from: x, reason: collision with root package name */
    private int f12063x = 0;

    /* renamed from: y, reason: collision with root package name */
    private int f12064y = 0;

    /* renamed from: w, reason: collision with root package name */
    private int f12065w = 0;

    /* renamed from: h, reason: collision with root package name */
    private int f12066h = 0;

    private void init(String pluginDetails) {
    }

    DefaultPlugin(URL url, String type, String[] pNames, String[] pValues) {
        init("Default Plugin for: " + (null == url ? "(null)" : url.toExternalForm()));
    }

    @Override // com.sun.webkit.plugin.Plugin
    public void paint(WCGraphicsContext g2, int intX, int intY, int intWidth, int intHeight) {
        g2.fillRect(this.f12063x, this.f12064y, this.f12065w, this.f12066h, new Color(0.6666667f, 1.0f, 1.0f, 0.06666667f));
    }

    @Override // com.sun.webkit.plugin.Plugin
    public void activate(Object nativeContainer, PluginListener pl) {
    }

    @Override // com.sun.webkit.plugin.Plugin
    public void destroy() {
    }

    @Override // com.sun.webkit.plugin.Plugin
    public void setVisible(boolean isVisible) {
    }

    @Override // com.sun.webkit.plugin.Plugin
    public void setEnabled(boolean enabled) {
    }

    @Override // com.sun.webkit.plugin.Plugin
    public void setBounds(int x2, int y2, int width, int height) {
        this.f12063x = x2;
        this.f12064y = y2;
        this.f12065w = width;
        this.f12066h = height;
    }

    @Override // com.sun.webkit.plugin.Plugin
    public Object invoke(String subObjectId, String methodName, Object[] args) throws IOError {
        return null;
    }

    @Override // com.sun.webkit.plugin.Plugin
    public boolean handleMouseEvent(String type, int offsetX, int offsetY, int screenX, int screenY, int button, boolean buttonDown, boolean altKey, boolean metaKey, boolean ctrlKey, boolean shiftKey, long timeStamp) {
        return false;
    }

    @Override // com.sun.webkit.plugin.Plugin
    public void requestFocus() {
    }

    @Override // com.sun.webkit.plugin.Plugin
    public void setNativeContainerBounds(int x2, int y2, int width, int height) {
    }
}
