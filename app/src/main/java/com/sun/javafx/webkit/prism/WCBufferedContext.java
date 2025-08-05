package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NodeEffectInput;
import com.sun.javafx.webkit.prism.WCGraphicsPrismContext;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.DropShadow;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCBufferedContext.class */
final class WCBufferedContext extends WCGraphicsPrismContext {
    private final PrismImage img;
    private boolean isInitialized;
    private final RectBounds TEMP_BOUNDS = new RectBounds();
    private final NGRectangle TEMP_NGRECT = new NGRectangle();
    private final RoundRectangle2D TEMP_RECT = new RoundRectangle2D();
    private final float[] TEMP_COORDS = new float[6];
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WCBufferedContext.class.desiredAssertionStatus();
    }

    WCBufferedContext(PrismImage img) {
        this.img = img;
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext
    public WCGraphicsPrismContext.Type type() {
        return WCGraphicsPrismContext.Type.DEDICATED;
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext, com.sun.webkit.graphics.WCGraphicsContext
    public WCImage getImage() {
        return this.img;
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext
    Graphics getGraphics(boolean checkClip) {
        init();
        if (this.baseGraphics == null) {
            this.baseGraphics = this.img.getGraphics();
        }
        return super.getGraphics(checkClip);
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext
    protected boolean shouldCalculateIntersection() {
        return this.baseGraphics == null;
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext
    protected boolean shouldRenderRect(float x2, float y2, float w2, float h2, DropShadow shadow, BasicStroke stroke) {
        if (!shouldCalculateIntersection()) {
            return true;
        }
        if (shadow != null) {
            this.TEMP_RECT.setFrame(x2, y2, w2, h2);
            return shouldRenderShape(this.TEMP_RECT, shadow, stroke);
        }
        if (stroke != null) {
            float s2 = 0.0f;
            float sx2 = 0.0f;
            switch (stroke.getType()) {
                case 0:
                    sx2 = stroke.getLineWidth();
                    s2 = sx2 / 2.0f;
                    break;
                case 2:
                    s2 = stroke.getLineWidth();
                    sx2 = s2 * 2.0f;
                    break;
            }
            x2 -= s2;
            y2 -= s2;
            w2 += sx2;
            h2 += sx2;
        }
        this.TEMP_BOUNDS.setBounds(x2, y2, x2 + w2, y2 + h2);
        return trIntersectsClip(this.TEMP_BOUNDS, getTransformNoClone());
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext
    protected boolean shouldRenderShape(Shape shape, DropShadow shadow, BasicStroke stroke) {
        if (!shouldCalculateIntersection()) {
            return true;
        }
        BaseTransform accumTX = shadow != null ? BaseTransform.IDENTITY_TRANSFORM : getTransformNoClone();
        float[] fArr = this.TEMP_COORDS;
        this.TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
        fArr[0] = Float.POSITIVE_INFINITY;
        float[] fArr2 = this.TEMP_COORDS;
        this.TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
        fArr2[2] = Float.NEGATIVE_INFINITY;
        if (stroke == null) {
            Shape.accumulate(this.TEMP_COORDS, shape, accumTX);
        } else {
            stroke.accumulateShapeBounds(this.TEMP_COORDS, shape, accumTX);
        }
        this.TEMP_BOUNDS.setBounds(this.TEMP_COORDS[0], this.TEMP_COORDS[1], this.TEMP_COORDS[2], this.TEMP_COORDS[3]);
        BaseTransform tx = null;
        if (shadow != null) {
            this.TEMP_NGRECT.updateRectangle(this.TEMP_BOUNDS.getMinX(), this.TEMP_BOUNDS.getMinY(), this.TEMP_BOUNDS.getWidth(), this.TEMP_BOUNDS.getHeight(), 0.0f, 0.0f);
            this.TEMP_NGRECT.setContentBounds(this.TEMP_BOUNDS);
            BaseBounds bb2 = shadow.getBounds(BaseTransform.IDENTITY_TRANSFORM, new NodeEffectInput(this.TEMP_NGRECT));
            if (!$assertionsDisabled && bb2.getBoundsType() != BaseBounds.BoundsType.RECTANGLE) {
                throw new AssertionError();
            }
            this.TEMP_BOUNDS.setBounds((RectBounds) bb2);
            tx = getTransformNoClone();
        }
        return trIntersectsClip(this.TEMP_BOUNDS, tx);
    }

    private boolean trIntersectsClip(RectBounds bounds, BaseTransform tx) {
        if (tx != null && !tx.isIdentity()) {
            tx.transform(bounds, bounds);
        }
        Rectangle clip = getClipRectNoClone();
        if (clip != null) {
            return bounds.intersects(clip.f11913x, clip.f11914y, clip.f11913x + clip.width, clip.f11914y + clip.height);
        }
        if (this.img != null) {
            return bounds.intersects(0.0f, 0.0f, this.img.getWidth() * this.img.getPixelScale(), this.img.getHeight() * this.img.getPixelScale());
        }
        return false;
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext, com.sun.webkit.graphics.WCGraphicsContext
    public void saveState() {
        init();
        super.saveState();
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext, com.sun.webkit.graphics.WCGraphicsContext
    public void scale(float sx, float sy) {
        init();
        super.scale(sx, sy);
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext, com.sun.webkit.graphics.WCGraphicsContext
    public void setTransform(WCTransform tm) {
        init();
        super.setTransform(tm);
    }

    private void init() {
        if (!this.isInitialized) {
            BaseTransform t2 = PrismGraphicsManager.getPixelScaleTransform();
            initBaseTransform(t2);
            setClip(0, 0, this.img.getWidth(), this.img.getHeight());
            this.isInitialized = true;
        }
    }

    @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext, com.sun.webkit.graphics.WCGraphicsContext
    public void dispose() {
    }
}
