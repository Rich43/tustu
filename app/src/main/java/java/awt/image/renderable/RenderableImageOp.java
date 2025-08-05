package java.awt.image.renderable;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.util.Vector;

/* loaded from: rt.jar:java/awt/image/renderable/RenderableImageOp.class */
public class RenderableImageOp implements RenderableImage {
    ParameterBlock paramBlock;
    ContextualRenderedImageFactory myCRIF;
    Rectangle2D boundingBox;

    public RenderableImageOp(ContextualRenderedImageFactory contextualRenderedImageFactory, ParameterBlock parameterBlock) {
        this.myCRIF = contextualRenderedImageFactory;
        this.paramBlock = (ParameterBlock) parameterBlock.clone();
    }

    @Override // java.awt.image.renderable.RenderableImage
    public Vector<RenderableImage> getSources() {
        return getRenderableSources();
    }

    private Vector getRenderableSources() {
        Vector vector = null;
        if (this.paramBlock.getNumSources() > 0) {
            vector = new Vector();
            for (int i2 = 0; i2 < this.paramBlock.getNumSources(); i2++) {
                Object source = this.paramBlock.getSource(i2);
                if (!(source instanceof RenderableImage)) {
                    break;
                }
                vector.add((RenderableImage) source);
            }
        }
        return vector;
    }

    @Override // java.awt.image.renderable.RenderableImage
    public Object getProperty(String str) {
        return this.myCRIF.getProperty(this.paramBlock, str);
    }

    @Override // java.awt.image.renderable.RenderableImage
    public String[] getPropertyNames() {
        return this.myCRIF.getPropertyNames();
    }

    @Override // java.awt.image.renderable.RenderableImage
    public boolean isDynamic() {
        return this.myCRIF.isDynamic();
    }

    @Override // java.awt.image.renderable.RenderableImage
    public float getWidth() {
        if (this.boundingBox == null) {
            this.boundingBox = this.myCRIF.getBounds2D(this.paramBlock);
        }
        return (float) this.boundingBox.getWidth();
    }

    @Override // java.awt.image.renderable.RenderableImage
    public float getHeight() {
        if (this.boundingBox == null) {
            this.boundingBox = this.myCRIF.getBounds2D(this.paramBlock);
        }
        return (float) this.boundingBox.getHeight();
    }

    @Override // java.awt.image.renderable.RenderableImage
    public float getMinX() {
        if (this.boundingBox == null) {
            this.boundingBox = this.myCRIF.getBounds2D(this.paramBlock);
        }
        return (float) this.boundingBox.getMinX();
    }

    @Override // java.awt.image.renderable.RenderableImage
    public float getMinY() {
        if (this.boundingBox == null) {
            this.boundingBox = this.myCRIF.getBounds2D(this.paramBlock);
        }
        return (float) this.boundingBox.getMinY();
    }

    public ParameterBlock setParameterBlock(ParameterBlock parameterBlock) {
        ParameterBlock parameterBlock2 = this.paramBlock;
        this.paramBlock = (ParameterBlock) parameterBlock.clone();
        return parameterBlock2;
    }

    public ParameterBlock getParameterBlock() {
        return this.paramBlock;
    }

    @Override // java.awt.image.renderable.RenderableImage
    public RenderedImage createScaledRendering(int i2, int i3, RenderingHints renderingHints) {
        double width = i2 / getWidth();
        double height = i3 / getHeight();
        if (Math.abs((width / height) - 1.0d) < 0.01d) {
            width = height;
        }
        return createRendering(new RenderContext(AffineTransform.getScaleInstance(width, height), renderingHints));
    }

    @Override // java.awt.image.renderable.RenderableImage
    public RenderedImage createDefaultRendering() {
        return createRendering(new RenderContext(new AffineTransform()));
    }

    @Override // java.awt.image.renderable.RenderableImage
    public RenderedImage createRendering(RenderContext renderContext) {
        ParameterBlock parameterBlock = (ParameterBlock) this.paramBlock.clone();
        Vector renderableSources = getRenderableSources();
        if (renderableSources != null) {
            try {
                Vector<Object> vector = new Vector<>();
                for (int i2 = 0; i2 < renderableSources.size(); i2++) {
                    RenderedImage renderedImageCreateRendering = ((RenderableImage) renderableSources.elementAt(i2)).createRendering(this.myCRIF.mapRenderContext(i2, renderContext, this.paramBlock, this));
                    if (renderedImageCreateRendering == null) {
                        return null;
                    }
                    vector.addElement(renderedImageCreateRendering);
                }
                if (vector.size() > 0) {
                    parameterBlock.setSources(vector);
                }
            } catch (ArrayIndexOutOfBoundsException e2) {
                return null;
            }
        }
        return this.myCRIF.create(renderContext, parameterBlock);
    }
}
