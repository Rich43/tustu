package java.awt.image.renderable;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;

/* loaded from: rt.jar:java/awt/image/renderable/RenderedImageFactory.class */
public interface RenderedImageFactory {
    RenderedImage create(ParameterBlock parameterBlock, RenderingHints renderingHints);
}
