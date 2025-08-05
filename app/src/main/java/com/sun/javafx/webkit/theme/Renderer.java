package com.sun.javafx.webkit.theme;

import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.scene.control.Control;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/Renderer.class */
public abstract class Renderer {
    private static Renderer instance;

    protected abstract void render(Control control, WCGraphicsContext wCGraphicsContext);

    public static void setRenderer(Renderer renderer) {
        instance = renderer;
    }

    public static Renderer getRenderer() {
        return instance;
    }
}
