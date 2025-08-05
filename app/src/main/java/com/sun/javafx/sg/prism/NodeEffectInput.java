package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrRenderInfo;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NodeEffectInput.class */
public final class NodeEffectInput extends Effect {
    private NGNode node;
    private RenderType renderType;
    private BaseBounds tempBounds;
    private ImageData cachedIdentityImageData;
    private ImageData cachedTransformedImageData;
    private BaseTransform cachedTransform;

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NodeEffectInput$RenderType.class */
    public enum RenderType {
        EFFECT_CONTENT,
        CLIPPED_CONTENT,
        FULL_CONTENT
    }

    public NodeEffectInput(NGNode node) {
        this(node, RenderType.EFFECT_CONTENT);
    }

    public NodeEffectInput(NGNode node, RenderType renderType) {
        this.tempBounds = new RectBounds();
        this.node = node;
        this.renderType = renderType;
    }

    public NGNode getNode() {
        return this.node;
    }

    public void setNode(NGNode node) {
        if (this.node != node) {
            this.node = node;
            flush();
        }
    }

    static boolean contains(ImageData cachedImage, Rectangle imgbounds) {
        Rectangle cachedBounds = cachedImage.getUntransformedBounds();
        return cachedBounds.contains(imgbounds);
    }

    @Override // com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        BaseTransform t2 = transform == null ? BaseTransform.IDENTITY_TRANSFORM : transform;
        this.tempBounds = this.node.getContentBounds(this.tempBounds, t2);
        return this.tempBounds.copy();
    }

    @Override // com.sun.scenario.effect.Effect
    public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        Graphics g2;
        if ((renderHelper instanceof PrRenderInfo) && (g2 = ((PrRenderInfo) renderHelper).getGraphics()) != null) {
            render(g2, transform);
            return null;
        }
        Rectangle bounds = getImageBoundsForNode(this.node, this.renderType, transform, outputClip);
        if (transform.isIdentity()) {
            if (this.cachedIdentityImageData != null && contains(this.cachedIdentityImageData, bounds) && this.cachedIdentityImageData.validate(fctx)) {
                this.cachedIdentityImageData.addref();
                return this.cachedIdentityImageData;
            }
        } else if (this.cachedTransformedImageData != null && contains(this.cachedTransformedImageData, bounds) && this.cachedTransformedImageData.validate(fctx) && this.cachedTransform.equals(transform)) {
            this.cachedTransformedImageData.addref();
            return this.cachedTransformedImageData;
        }
        ImageData retData = getImageDataForBoundedNode(fctx, this.node, this.renderType, transform, bounds);
        if (transform.isIdentity()) {
            flushIdentityImage();
            this.cachedIdentityImageData = retData;
            this.cachedIdentityImageData.addref();
        } else {
            flushTransformedImage();
            this.cachedTransform = transform.copy();
            this.cachedTransformedImageData = retData;
            this.cachedTransformedImageData.addref();
        }
        return retData;
    }

    @Override // com.sun.scenario.effect.Effect
    public Effect.AccelType getAccelType(FilterContext fctx) {
        return Effect.AccelType.INTRINSIC;
    }

    public void flushIdentityImage() {
        if (this.cachedIdentityImageData != null) {
            this.cachedIdentityImageData.unref();
            this.cachedIdentityImageData = null;
        }
    }

    public void flushTransformedImage() {
        if (this.cachedTransformedImageData != null) {
            this.cachedTransformedImageData.unref();
            this.cachedTransformedImageData = null;
        }
        this.cachedTransform = null;
    }

    public void flush() {
        flushIdentityImage();
        flushTransformedImage();
    }

    public void render(Graphics g2, BaseTransform transform) {
        BaseTransform savetx = null;
        if (!transform.isIdentity()) {
            savetx = g2.getTransformNoClone().copy();
            g2.transform(transform);
        }
        this.node.renderContent(g2);
        if (savetx != null) {
            g2.setTransform(savetx);
        }
    }

    static ImageData getImageDataForNode(FilterContext fctx, NGNode node, boolean contentOnly, BaseTransform transform, Rectangle clip) {
        RenderType rendertype = contentOnly ? RenderType.EFFECT_CONTENT : RenderType.FULL_CONTENT;
        Rectangle r2 = getImageBoundsForNode(node, rendertype, transform, clip);
        return getImageDataForBoundedNode(fctx, node, rendertype, transform, r2);
    }

    static Rectangle getImageBoundsForNode(NGNode node, RenderType type, BaseTransform transform, Rectangle clip) {
        BaseBounds bounds = new RectBounds();
        switch (type) {
            case EFFECT_CONTENT:
                bounds = node.getContentBounds(bounds, transform);
                break;
            case FULL_CONTENT:
                bounds = node.getCompleteBounds(bounds, transform);
                break;
            case CLIPPED_CONTENT:
                bounds = node.getClippedBounds(bounds, transform);
                break;
        }
        Rectangle r2 = new Rectangle(bounds);
        if (clip != null) {
            r2.intersectWith(clip);
        }
        return r2;
    }

    private static ImageData getImageDataForBoundedNode(FilterContext fctx, NGNode node, RenderType renderType, BaseTransform transform, Rectangle bounds) {
        PrDrawable ret = (PrDrawable) Effect.getCompatibleImage(fctx, bounds.width, bounds.height);
        if (ret != null) {
            Graphics g2 = ret.createGraphics();
            g2.translate(-bounds.f11913x, -bounds.f11914y);
            if (transform != null) {
                g2.transform(transform);
            }
            switch (renderType) {
                case EFFECT_CONTENT:
                    node.renderContent(g2);
                    break;
                case FULL_CONTENT:
                    node.render(g2);
                    break;
                case CLIPPED_CONTENT:
                    node.renderForClip(g2);
                    break;
            }
        }
        return new ImageData(fctx, ret, bounds);
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return false;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        return null;
    }
}
