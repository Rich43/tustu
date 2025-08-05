package com.sun.prism;

import com.sun.glass.ui.Screen;

/* loaded from: jfxrt.jar:com/sun/prism/RenderTarget.class */
public interface RenderTarget extends Surface {
    Screen getAssociatedScreen();

    Graphics createGraphics();

    boolean isOpaque();

    void setOpaque(boolean z2);

    boolean isMSAA();
}
