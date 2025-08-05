package javax.swing;

import java.awt.Graphics;
import java.awt.Rectangle;

/* loaded from: rt.jar:javax/swing/GraphicsWrapper.class */
interface GraphicsWrapper {
    Graphics subGraphics();

    boolean isClipIntersecting(Rectangle rectangle);

    int getClipX();

    int getClipY();

    int getClipWidth();

    int getClipHeight();
}
