package java.awt.image.renderable;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.util.Vector;

/* loaded from: rt.jar:java/awt/image/renderable/RenderableImage.class */
public interface RenderableImage {
    public static final String HINTS_OBSERVED = "HINTS_OBSERVED";

    Vector<RenderableImage> getSources();

    Object getProperty(String str);

    String[] getPropertyNames();

    boolean isDynamic();

    float getWidth();

    float getHeight();

    float getMinX();

    float getMinY();

    RenderedImage createScaledRendering(int i2, int i3, RenderingHints renderingHints);

    RenderedImage createDefaultRendering();

    RenderedImage createRendering(RenderContext renderContext);
}
