package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/CacheFilter.class */
public class CacheFilter {
    private static final Rectangle TEMP_RECT = new Rectangle();
    private static final DirtyRegionContainer TEMP_CONTAINER = new DirtyRegionContainer(1);
    private static final Affine3D TEMP_CACHEFILTER_TRANSFORM = new Affine3D();
    private static final RectBounds TEMP_BOUNDS = new RectBounds();
    private static final double EPSILON = 1.0E-7d;
    private RTTexture tempTexture;
    private double lastXDelta;
    private double lastYDelta;
    private ScrollCacheState scrollCacheState;
    private ImageData cachedImageData;
    private double cachedScaleX;
    private double cachedScaleY;
    private double cachedRotate;
    private double cachedX;
    private double cachedY;
    private NGNode node;
    private boolean scaleHint;
    private boolean rotateHint;
    private CacheHint cacheHint;
    private Rectangle cacheBounds = new Rectangle();
    private final Affine2D cachedXform = new Affine2D();
    private final Affine2D screenXform = new Affine2D();
    private boolean wasUnsupported = false;

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/CacheFilter$ScrollCacheState.class */
    private enum ScrollCacheState {
        CHECKING_PRECONDITIONS,
        ENABLED,
        DISABLED
    }

    private Rectangle computeDirtyRegionForTranslate() {
        if (this.lastXDelta != 0.0d) {
            if (this.lastXDelta > 0.0d) {
                TEMP_RECT.setBounds(0, 0, (int) this.lastXDelta, this.cacheBounds.height);
            } else {
                TEMP_RECT.setBounds(this.cacheBounds.width + ((int) this.lastXDelta), 0, -((int) this.lastXDelta), this.cacheBounds.height);
            }
        } else if (this.lastYDelta > 0.0d) {
            TEMP_RECT.setBounds(0, 0, this.cacheBounds.width, (int) this.lastYDelta);
        } else {
            TEMP_RECT.setBounds(0, this.cacheBounds.height + ((int) this.lastYDelta), this.cacheBounds.width, -((int) this.lastYDelta));
        }
        return TEMP_RECT;
    }

    protected CacheFilter(NGNode node, CacheHint cacheHint) {
        this.scrollCacheState = ScrollCacheState.CHECKING_PRECONDITIONS;
        this.node = node;
        this.scrollCacheState = ScrollCacheState.CHECKING_PRECONDITIONS;
        setHint(cacheHint);
    }

    public void setHint(CacheHint cacheHint) {
        this.cacheHint = cacheHint;
        this.scaleHint = cacheHint == CacheHint.SPEED || cacheHint == CacheHint.SCALE || cacheHint == CacheHint.SCALE_AND_ROTATE;
        this.rotateHint = cacheHint == CacheHint.SPEED || cacheHint == CacheHint.ROTATE || cacheHint == CacheHint.SCALE_AND_ROTATE;
    }

    final boolean isScaleHint() {
        return this.scaleHint;
    }

    final boolean isRotateHint() {
        return this.rotateHint;
    }

    boolean matchesHint(CacheHint cacheHint) {
        return this.cacheHint == cacheHint;
    }

    boolean unsupported(double[] xformInfo) {
        double scaleX = xformInfo[0];
        double scaleY = xformInfo[1];
        double rotate = xformInfo[2];
        if (rotate <= EPSILON && rotate >= -1.0E-7d) {
            return false;
        }
        if (scaleX > scaleY + EPSILON || scaleY > scaleX + EPSILON || scaleX < scaleY - EPSILON || scaleY < scaleX - EPSILON || this.cachedScaleX > this.cachedScaleY + EPSILON || this.cachedScaleY > this.cachedScaleX + EPSILON || this.cachedScaleX < this.cachedScaleY - EPSILON || this.cachedScaleY < this.cachedScaleX - EPSILON) {
            return true;
        }
        return false;
    }

    private boolean isXformScrollCacheCapable(double[] xformInfo) {
        if (unsupported(xformInfo)) {
            return false;
        }
        double rotate = xformInfo[2];
        return this.rotateHint || rotate == 0.0d;
    }

    private boolean needToRenderCache(BaseTransform renderXform, double[] xformInfo, float pixelScale) {
        if (this.cachedImageData == null) {
            return true;
        }
        if (this.lastXDelta != 0.0d || this.lastYDelta != 0.0d) {
            if (Math.abs(this.lastXDelta) >= this.cacheBounds.width || Math.abs(this.lastYDelta) >= this.cacheBounds.height || Math.rint(this.lastXDelta) != this.lastXDelta || Math.rint(this.lastYDelta) != this.lastYDelta) {
                this.node.clearDirtyTree();
                this.lastYDelta = 0.0d;
                this.lastXDelta = 0.0d;
                return true;
            }
            if (this.scrollCacheState == ScrollCacheState.CHECKING_PRECONDITIONS) {
                if (impl_scrollCacheCapable() && isXformScrollCacheCapable(xformInfo)) {
                    this.scrollCacheState = ScrollCacheState.ENABLED;
                } else {
                    this.scrollCacheState = ScrollCacheState.DISABLED;
                    return true;
                }
            }
        }
        if (this.cachedXform.getMxx() == renderXform.getMxx() && this.cachedXform.getMyy() == renderXform.getMyy() && this.cachedXform.getMxy() == renderXform.getMxy() && this.cachedXform.getMyx() == renderXform.getMyx()) {
            return false;
        }
        if (this.wasUnsupported || unsupported(xformInfo)) {
            return true;
        }
        double scaleX = xformInfo[0];
        double scaleY = xformInfo[1];
        double rotate = xformInfo[2];
        if (!this.scaleHint) {
            if (this.rotateHint && this.cachedScaleX - EPSILON < scaleX && scaleX < this.cachedScaleX + EPSILON && this.cachedScaleY - EPSILON < scaleY && scaleY < this.cachedScaleY + EPSILON) {
                return false;
            }
            return true;
        }
        if (this.cachedScaleX < pixelScale || this.cachedScaleY < pixelScale) {
            return true;
        }
        if (this.rotateHint) {
            return false;
        }
        if (this.cachedRotate - EPSILON < rotate && rotate < this.cachedRotate + EPSILON) {
            return false;
        }
        return true;
    }

    void updateScreenXform(double[] xformInfo) {
        if (this.scaleHint) {
            if (this.rotateHint) {
                double screenScaleX = xformInfo[0] / this.cachedScaleX;
                double screenScaleY = xformInfo[1] / this.cachedScaleY;
                double screenRotate = xformInfo[2] - this.cachedRotate;
                this.screenXform.setToScale(screenScaleX, screenScaleY);
                this.screenXform.rotate(screenRotate);
                return;
            }
            double screenScaleX2 = xformInfo[0] / this.cachedScaleX;
            double screenScaleY2 = xformInfo[1] / this.cachedScaleY;
            this.screenXform.setToScale(screenScaleX2, screenScaleY2);
            return;
        }
        if (this.rotateHint) {
            double screenRotate2 = xformInfo[2] - this.cachedRotate;
            this.screenXform.setToRotation(screenRotate2, 0.0d, 0.0d);
        } else {
            this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        }
    }

    public void invalidate() {
        if (this.scrollCacheState == ScrollCacheState.ENABLED) {
            this.scrollCacheState = ScrollCacheState.CHECKING_PRECONDITIONS;
        }
        imageDataUnref();
        this.lastYDelta = 0.0d;
        this.lastXDelta = 0.0d;
    }

    void imageDataUnref() {
        if (this.tempTexture != null) {
            this.tempTexture.dispose();
            this.tempTexture = null;
        }
        if (this.cachedImageData != null) {
            Filterable implImage = this.cachedImageData.getUntransformedImage();
            if (implImage != null) {
                implImage.lock();
            }
            this.cachedImageData.unref();
            this.cachedImageData = null;
        }
    }

    void invalidateByTranslation(double translateXDelta, double translateYDelta) {
        if (this.cachedImageData == null) {
            return;
        }
        if (this.scrollCacheState == ScrollCacheState.DISABLED) {
            imageDataUnref();
        } else if (translateXDelta != 0.0d && translateYDelta != 0.0d) {
            imageDataUnref();
        } else {
            this.lastYDelta = translateYDelta;
            this.lastXDelta = translateXDelta;
        }
    }

    public void dispose() {
        invalidate();
        this.node = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    double[] unmatrix(BaseTransform xform) {
        double angleRad;
        double[] retVal = new double[3];
        double[] dArr = {new double[]{xform.getMxx(), xform.getMxy()}, new double[]{xform.getMyx(), xform.getMyy()}};
        double xSignum = Math.signum((double) dArr[0][0]);
        double ySignum = Math.signum((double) dArr[1][1]);
        double scaleX = xSignum * v2length(dArr[0]);
        v2scale(dArr[0], xSignum);
        double shearXY = v2dot(dArr[0], dArr[1]);
        v2combine(dArr[1], dArr[0], dArr[1], 1.0d, -shearXY);
        double scaleY = ySignum * v2length(dArr[1]);
        v2scale(dArr[1], ySignum);
        long j2 = dArr[1][0];
        long j3 = dArr[0][0];
        if (j2 >= 0.0d) {
            angleRad = Math.acos(j3);
        } else if (j3 > 0.0d) {
            angleRad = 6.283185307179586d + Math.asin(j2);
        } else {
            angleRad = 3.141592653589793d + Math.acos(-j3);
        }
        retVal[0] = scaleX;
        retVal[1] = scaleY;
        retVal[2] = angleRad;
        return retVal;
    }

    void v2combine(double[] v0, double[] v1, double[] result, double scalarA, double scalarB) {
        result[0] = (scalarA * v0[0]) + (scalarB * v1[0]);
        result[1] = (scalarA * v0[1]) + (scalarB * v1[1]);
    }

    double v2dot(double[] v0, double[] v1) {
        return (v0[0] * v1[0]) + (v0[1] * v1[1]);
    }

    void v2scale(double[] v2, double newLen) {
        double len = v2length(v2);
        if (len != 0.0d) {
            v2[0] = v2[0] * (newLen / len);
            v2[1] = v2[1] * (newLen / len);
        }
    }

    double v2length(double[] v2) {
        return Math.sqrt((v2[0] * v2[0]) + (v2[1] * v2[1]));
    }

    void render(Graphics g2) {
        Filterable implImage;
        BaseTransform xform = g2.getTransformNoClone();
        FilterContext fctx = PrFilterContext.getInstance(g2.getAssociatedScreen());
        double[] xformInfo = unmatrix(xform);
        boolean isUnsupported = unsupported(xformInfo);
        this.lastXDelta *= xformInfo[0];
        this.lastYDelta *= xformInfo[1];
        if (this.cachedImageData != null && (implImage = this.cachedImageData.getUntransformedImage()) != null) {
            implImage.lock();
            if (!this.cachedImageData.validate(fctx)) {
                implImage.unlock();
                invalidate();
            }
        }
        float pixelScale = g2.getAssociatedScreen().getRenderScale();
        if (needToRenderCache(xform, xformInfo, pixelScale)) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.incrementCounter("CacheFilter rebuilding");
            }
            if (this.cachedImageData != null) {
                Filterable implImage2 = this.cachedImageData.getUntransformedImage();
                if (implImage2 != null) {
                    implImage2.unlock();
                }
                invalidate();
            }
            if (this.scaleHint) {
                this.cachedScaleX = Math.max(pixelScale, xformInfo[0]);
                this.cachedScaleY = Math.max(pixelScale, xformInfo[1]);
                this.cachedRotate = 0.0d;
                this.cachedXform.setTransform(this.cachedScaleX, 0.0d, 0.0d, this.cachedScaleX, 0.0d, 0.0d);
                updateScreenXform(xformInfo);
            } else {
                this.cachedScaleX = xformInfo[0];
                this.cachedScaleY = xformInfo[1];
                this.cachedRotate = xformInfo[2];
                this.cachedXform.setTransform(xform.getMxx(), xform.getMyx(), xform.getMxy(), xform.getMyy(), 0.0d, 0.0d);
                this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
            }
            this.cacheBounds = impl_getCacheBounds(this.cacheBounds, this.cachedXform);
            this.cachedImageData = impl_createImageData(fctx, this.cacheBounds);
            impl_renderNodeToCache(this.cachedImageData, this.cacheBounds, this.cachedXform, null);
            Rectangle cachedBounds = this.cachedImageData.getUntransformedBounds();
            this.cachedX = cachedBounds.f11913x;
            this.cachedY = cachedBounds.f11914y;
        } else {
            if (this.scrollCacheState == ScrollCacheState.ENABLED && (this.lastXDelta != 0.0d || this.lastYDelta != 0.0d)) {
                impl_moveCacheBy(this.cachedImageData, this.lastXDelta, this.lastYDelta);
                impl_renderNodeToCache(this.cachedImageData, this.cacheBounds, this.cachedXform, computeDirtyRegionForTranslate());
                this.lastYDelta = 0.0d;
                this.lastXDelta = 0.0d;
            }
            if (isUnsupported) {
                this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
            } else {
                updateScreenXform(xformInfo);
            }
        }
        this.wasUnsupported = isUnsupported;
        Filterable implImage3 = this.cachedImageData.getUntransformedImage();
        if (implImage3 == null) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.incrementCounter("CacheFilter not used");
            }
            impl_renderNodeToScreen(g2);
        } else {
            double mxt = xform.getMxt();
            double myt = xform.getMyt();
            impl_renderCacheToScreen(g2, implImage3, mxt, myt);
            implImage3.unlock();
        }
    }

    ImageData impl_createImageData(FilterContext fctx, Rectangle bounds) {
        Filterable ret;
        try {
            ret = Effect.getCompatibleImage(fctx, bounds.width, bounds.height);
            Texture cachedTex = ((PrDrawable) ret).getTextureObject();
            cachedTex.contentsUseful();
        } catch (Throwable th) {
            ret = null;
        }
        return new ImageData(fctx, ret, bounds);
    }

    void impl_renderNodeToCache(ImageData cacheData, Rectangle cacheBounds, BaseTransform xform, Rectangle dirtyBounds) {
        PrDrawable image = (PrDrawable) cacheData.getUntransformedImage();
        if (image != null) {
            Graphics g2 = image.createGraphics();
            TEMP_CACHEFILTER_TRANSFORM.setToIdentity();
            TEMP_CACHEFILTER_TRANSFORM.translate(-cacheBounds.f11913x, -cacheBounds.f11914y);
            if (xform != null) {
                TEMP_CACHEFILTER_TRANSFORM.concatenate(xform);
            }
            if (dirtyBounds != null) {
                TEMP_CONTAINER.deriveWithNewRegion((RectBounds) TEMP_BOUNDS.deriveWithNewBounds(dirtyBounds));
                this.node.doPreCulling(TEMP_CONTAINER, TEMP_CACHEFILTER_TRANSFORM, new GeneralTransform3D());
                g2.setHasPreCullingBits(true);
                g2.setClipRectIndex(0);
                g2.setClipRect(dirtyBounds);
            }
            g2.transform(TEMP_CACHEFILTER_TRANSFORM);
            if (this.node.getClipNode() != null) {
                this.node.renderClip(g2);
            } else if (this.node.getEffectFilter() != null) {
                this.node.renderEffect(g2);
            } else {
                this.node.renderContent(g2);
            }
        }
    }

    void impl_renderNodeToScreen(Object implGraphics) {
        Graphics g2 = (Graphics) implGraphics;
        if (this.node.getEffectFilter() != null) {
            this.node.renderEffect(g2);
        } else {
            this.node.renderContent(g2);
        }
    }

    void impl_renderCacheToScreen(Object implGraphics, Filterable implImage, double mxt, double myt) {
        Graphics g2 = (Graphics) implGraphics;
        g2.setTransform(this.screenXform.getMxx(), this.screenXform.getMyx(), this.screenXform.getMxy(), this.screenXform.getMyy(), mxt, myt);
        g2.translate((float) this.cachedX, (float) this.cachedY);
        Texture cachedTex = ((PrDrawable) implImage).getTextureObject();
        Rectangle cachedBounds = this.cachedImageData.getUntransformedBounds();
        g2.drawTexture(cachedTex, 0.0f, 0.0f, cachedBounds.width, cachedBounds.height);
    }

    boolean impl_scrollCacheCapable() {
        NGNode clip;
        if (!(this.node instanceof NGGroup)) {
            return false;
        }
        List<NGNode> children = ((NGGroup) this.node).getChildren();
        if (children.size() != 1) {
            return false;
        }
        NGNode child = children.get(0);
        if (!child.getTransform().is2D() || (clip = this.node.getClipNode()) == null || !clip.isRectClip(BaseTransform.IDENTITY_TRANSFORM, false)) {
            return false;
        }
        if (this.node instanceof NGRegion) {
            NGRegion region = (NGRegion) this.node;
            if (!region.getBorder().isEmpty()) {
                return false;
            }
            Background background = region.getBackground();
            if (!background.isEmpty()) {
                if (!background.getImages().isEmpty() || background.getFills().size() != 1) {
                    return false;
                }
                BackgroundFill fill = background.getFills().get(0);
                Paint fillPaint = fill.getFill();
                BaseBounds clipBounds = clip.getCompleteBounds(TEMP_BOUNDS, BaseTransform.IDENTITY_TRANSFORM);
                return fillPaint.isOpaque() && (fillPaint instanceof Color) && fill.getInsets().equals(Insets.EMPTY) && clipBounds.getMinX() == 0.0f && clipBounds.getMinY() == 0.0f && clipBounds.getMaxX() == region.getWidth() && clipBounds.getMaxY() == region.getHeight();
            }
            return true;
        }
        return true;
    }

    void impl_moveCacheBy(ImageData cachedImageData, double xDelta, double yDelta) {
        PrDrawable drawable = (PrDrawable) cachedImageData.getUntransformedImage();
        Rectangle r2 = cachedImageData.getUntransformedBounds();
        int x2 = (int) Math.max(0.0d, -xDelta);
        int y2 = (int) Math.max(0.0d, -yDelta);
        int destX = (int) Math.max(0.0d, xDelta);
        int destY = (int) Math.max(0.0d, yDelta);
        int w2 = r2.width - ((int) Math.abs(xDelta));
        int h2 = r2.height - ((int) Math.abs(yDelta));
        Graphics g2 = drawable.createGraphics();
        if (this.tempTexture != null) {
            this.tempTexture.lock();
            if (this.tempTexture.isSurfaceLost()) {
                this.tempTexture = null;
            }
        }
        if (this.tempTexture == null) {
            this.tempTexture = g2.getResourceFactory().createRTTexture(drawable.getPhysicalWidth(), drawable.getPhysicalHeight(), Texture.WrapMode.CLAMP_NOT_NEEDED);
        }
        Graphics tempG = this.tempTexture.createGraphics();
        tempG.clear();
        tempG.drawTexture(drawable.getTextureObject(), 0.0f, 0.0f, w2, h2, x2, y2, x2 + w2, y2 + h2);
        tempG.sync();
        g2.clear();
        g2.drawTexture(this.tempTexture, destX, destY, destX + w2, destY + h2, 0.0f, 0.0f, w2, h2);
        this.tempTexture.unlock();
    }

    Rectangle impl_getCacheBounds(Rectangle bounds, BaseTransform xform) {
        BaseBounds b2 = this.node.getClippedBounds(TEMP_BOUNDS, xform);
        bounds.setBounds(b2);
        return bounds;
    }

    BaseBounds computeDirtyBounds(BaseBounds region, BaseTransform tx, GeneralTransform3D pvTx) {
        BaseBounds region2;
        if (!this.node.dirtyBounds.isEmpty()) {
            region2 = region.deriveWithNewBounds(this.node.dirtyBounds);
        } else {
            region2 = region.deriveWithNewBounds(this.node.transformedBounds);
        }
        if (!region2.isEmpty()) {
            region2.roundOut();
            BaseBounds region3 = this.node.computePadding(region2);
            BaseBounds region4 = tx.transform(region3, region3);
            region2 = pvTx.transform(region4, region4);
        }
        return region2;
    }
}
