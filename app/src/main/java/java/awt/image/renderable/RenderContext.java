package java.awt.image.renderable;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/* loaded from: rt.jar:java/awt/image/renderable/RenderContext.class */
public class RenderContext implements Cloneable {
    RenderingHints hints;
    AffineTransform usr2dev;
    Shape aoi;

    public RenderContext(AffineTransform affineTransform, Shape shape, RenderingHints renderingHints) {
        this.hints = renderingHints;
        this.aoi = shape;
        this.usr2dev = (AffineTransform) affineTransform.clone();
    }

    public RenderContext(AffineTransform affineTransform) {
        this(affineTransform, null, null);
    }

    public RenderContext(AffineTransform affineTransform, RenderingHints renderingHints) {
        this(affineTransform, null, renderingHints);
    }

    public RenderContext(AffineTransform affineTransform, Shape shape) {
        this(affineTransform, shape, null);
    }

    public RenderingHints getRenderingHints() {
        return this.hints;
    }

    public void setRenderingHints(RenderingHints renderingHints) {
        this.hints = renderingHints;
    }

    public void setTransform(AffineTransform affineTransform) {
        this.usr2dev = (AffineTransform) affineTransform.clone();
    }

    public void preConcatenateTransform(AffineTransform affineTransform) {
        preConcetenateTransform(affineTransform);
    }

    @Deprecated
    public void preConcetenateTransform(AffineTransform affineTransform) {
        this.usr2dev.preConcatenate(affineTransform);
    }

    public void concatenateTransform(AffineTransform affineTransform) {
        concetenateTransform(affineTransform);
    }

    @Deprecated
    public void concetenateTransform(AffineTransform affineTransform) {
        this.usr2dev.concatenate(affineTransform);
    }

    public AffineTransform getTransform() {
        return (AffineTransform) this.usr2dev.clone();
    }

    public void setAreaOfInterest(Shape shape) {
        this.aoi = shape;
    }

    public Shape getAreaOfInterest() {
        return this.aoi;
    }

    public Object clone() {
        return new RenderContext(this.usr2dev, this.aoi, this.hints);
    }
}
