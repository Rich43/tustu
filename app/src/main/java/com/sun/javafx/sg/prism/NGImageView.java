package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.RectBounds;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CachingCompoundImage;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.Coords;
import com.sun.prism.image.ViewPort;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGImageView.class */
public class NGImageView extends NGNode {
    private Image image;
    private CachingCompoundImage compoundImage;
    private CompoundCoords compoundCoords;

    /* renamed from: x, reason: collision with root package name */
    private float f11954x;

    /* renamed from: y, reason: collision with root package name */
    private float f11955y;

    /* renamed from: w, reason: collision with root package name */
    private float f11956w;

    /* renamed from: h, reason: collision with root package name */
    private float f11957h;
    private Coords coords;
    private ViewPort reqviewport;
    private ViewPort imgviewport;
    private boolean renderable = false;
    private boolean coordsOK = false;
    static final int MAX_SIZE_OVERRIDE = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NGImageView.class.desiredAssertionStatus();
    }

    private void invalidate() {
        this.coordsOK = false;
        this.coords = null;
        this.compoundCoords = null;
        this.imgviewport = null;
        geometryChanged();
    }

    public void setViewport(float vx, float vy, float vw, float vh, float cw, float ch) {
        if (vw > 0.0f && vh > 0.0f) {
            this.reqviewport = new ViewPort(vx, vy, vw, vh);
        } else {
            this.reqviewport = null;
        }
        this.f11956w = cw;
        this.f11957h = ch;
        invalidate();
    }

    private void calculatePositionAndClipping() {
        this.renderable = false;
        this.coordsOK = true;
        if (this.reqviewport == null || this.image == null) {
            this.renderable = this.image != null;
            return;
        }
        float iw = this.image.getWidth();
        float ih = this.image.getHeight();
        if (iw == 0.0f || ih == 0.0f) {
            return;
        }
        this.imgviewport = this.reqviewport.getScaledVersion(this.image.getPixelScale());
        this.coords = this.imgviewport.getClippedCoords(iw, ih, this.f11956w, this.f11957h);
        this.renderable = this.coords != null;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void doRender(Graphics g2) {
        if (!this.coordsOK) {
            calculatePositionAndClipping();
        }
        if (this.renderable) {
            super.doRender(g2);
        }
    }

    private int maxSizeWrapper(ResourceFactory factory) {
        return factory.getMaximumTextureSize();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        int imgW = this.image.getWidth();
        int imgH = this.image.getHeight();
        ResourceFactory factory = g2.getResourceFactory();
        int maxSize = maxSizeWrapper(factory);
        if (imgW <= maxSize && imgH <= maxSize) {
            Texture texture = factory.getCachedTexture(this.image, Texture.WrapMode.CLAMP_TO_EDGE);
            if (this.coords == null) {
                g2.drawTexture(texture, this.f11954x, this.f11955y, this.f11954x + this.f11956w, this.f11955y + this.f11957h, 0.0f, 0.0f, imgW, imgH);
            } else {
                this.coords.draw(texture, g2, this.f11954x, this.f11955y);
            }
            texture.unlock();
            return;
        }
        if (this.compoundImage == null) {
            this.compoundImage = new CachingCompoundImage(this.image, maxSize);
        }
        if (this.coords == null) {
            this.coords = new Coords(this.f11956w, this.f11957h, new ViewPort(0.0f, 0.0f, imgW, imgH));
        }
        if (this.compoundCoords == null) {
            this.compoundCoords = new CompoundCoords(this.compoundImage, this.coords);
        }
        this.compoundCoords.draw(g2, this.compoundImage, this.f11954x, this.f11955y);
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return false;
    }

    public void setImage(Object img) {
        Image newImage = (Image) img;
        if (this.image == newImage) {
            return;
        }
        boolean needsInvalidate = (newImage != null && this.image != null && this.image.getPixelScale() == newImage.getPixelScale() && this.image.getHeight() == newImage.getHeight() && this.image.getWidth() == newImage.getWidth()) ? false : true;
        this.image = newImage;
        this.compoundImage = null;
        if (needsInvalidate) {
            invalidate();
        }
    }

    public void setX(float x2) {
        if (this.f11954x != x2) {
            this.f11954x = x2;
            geometryChanged();
        }
    }

    public void setY(float y2) {
        if (this.f11955y != y2) {
            this.f11955y = y2;
            geometryChanged();
        }
    }

    public void setSmooth(boolean s2) {
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOpaqueRegion() {
        if ($assertionsDisabled || this.image == null || (this.image.getWidth() >= 1 && this.image.getHeight() >= 1)) {
            return super.hasOpaqueRegion() && this.f11956w >= 1.0f && this.f11957h >= 1.0f && this.image != null && this.image.isOpaque();
        }
        throw new AssertionError();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected RectBounds computeOpaqueRegion(RectBounds opaqueRegion) {
        return (RectBounds) opaqueRegion.deriveWithNewBounds(this.f11954x, this.f11955y, 0.0f, this.f11954x + this.f11956w, this.f11955y + this.f11957h, 0.0f);
    }
}
