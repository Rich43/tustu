package com.sun.webkit.plugin;

import com.sun.webkit.graphics.WCGraphicsContext;
import java.io.IOException;

/* loaded from: jfxrt.jar:com/sun/webkit/plugin/Plugin.class */
public interface Plugin {
    public static final int EVENT_BEFOREACTIVATE = -4;
    public static final int EVENT_FOCUSCHANGE = -1;

    void requestFocus();

    void setNativeContainerBounds(int i2, int i3, int i4, int i5);

    void activate(Object obj, PluginListener pluginListener);

    void destroy();

    void setVisible(boolean z2);

    void setEnabled(boolean z2);

    void setBounds(int i2, int i3, int i4, int i5);

    Object invoke(String str, String str2, Object[] objArr) throws IOException;

    void paint(WCGraphicsContext wCGraphicsContext, int i2, int i3, int i4, int i5);

    boolean handleMouseEvent(String str, int i2, int i3, int i4, int i5, int i6, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, long j2);
}
