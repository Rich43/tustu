package com.sun.javafx.webkit.prism.theme;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.prism.Graphics;
import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.scene.Scene;
import javafx.scene.control.Control;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/theme/PrismRenderer.class */
public final class PrismRenderer extends Renderer {
    @Override // com.sun.javafx.webkit.theme.Renderer
    protected void render(Control control, WCGraphicsContext g2) {
        Scene.impl_setAllowPGAccess(true);
        NGNode peer = control.impl_getPeer();
        Scene.impl_setAllowPGAccess(false);
        peer.render((Graphics) g2.getPlatformGraphics());
    }
}
