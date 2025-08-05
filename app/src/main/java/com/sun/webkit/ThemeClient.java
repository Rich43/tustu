package com.sun.webkit;

import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;

/* loaded from: jfxrt.jar:com/sun/webkit/ThemeClient.class */
public abstract class ThemeClient {
    private static RenderTheme defaultRenderTheme;

    protected abstract RenderTheme createRenderTheme();

    protected abstract ScrollBarTheme createScrollBarTheme();

    public static void setDefaultRenderTheme(RenderTheme theme) {
        defaultRenderTheme = theme;
    }

    public static RenderTheme getDefaultRenderTheme() {
        return defaultRenderTheme;
    }
}
