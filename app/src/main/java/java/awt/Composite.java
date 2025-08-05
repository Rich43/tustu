package java.awt;

import java.awt.image.ColorModel;

/* loaded from: rt.jar:java/awt/Composite.class */
public interface Composite {
    CompositeContext createContext(ColorModel colorModel, ColorModel colorModel2, RenderingHints renderingHints);
}
