package java.awt.image.renderable;

import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;

/* loaded from: rt.jar:java/awt/image/renderable/ContextualRenderedImageFactory.class */
public interface ContextualRenderedImageFactory extends RenderedImageFactory {
    RenderContext mapRenderContext(int i2, RenderContext renderContext, ParameterBlock parameterBlock, RenderableImage renderableImage);

    RenderedImage create(RenderContext renderContext, ParameterBlock parameterBlock);

    Rectangle2D getBounds2D(ParameterBlock parameterBlock);

    Object getProperty(ParameterBlock parameterBlock, String str);

    String[] getPropertyNames();

    boolean isDynamic();
}
