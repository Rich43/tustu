package com.sun.javafx.webkit;

import com.sun.javafx.webkit.theme.RenderThemeImpl;
import com.sun.javafx.webkit.theme.ScrollBarThemeImpl;
import com.sun.webkit.ThemeClient;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/ThemeClientImpl.class */
public final class ThemeClientImpl extends ThemeClient {
    private final Accessor accessor;

    public ThemeClientImpl(Accessor accessor) {
        this.accessor = accessor;
    }

    @Override // com.sun.webkit.ThemeClient
    protected RenderTheme createRenderTheme() {
        return new RenderThemeImpl(this.accessor);
    }

    @Override // com.sun.webkit.ThemeClient
    protected ScrollBarTheme createScrollBarTheme() {
        return new ScrollBarThemeImpl(this.accessor);
    }
}
