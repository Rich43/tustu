package com.sun.javafx.sg.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Offset;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGRegion.class */
public class NGRegion extends NGGroup {
    private static final Affine2D SCRATCH_AFFINE;
    private static final Rectangle TEMP_RECT;
    private static WeakHashMap<Screen, RegionImageCache> imageCacheMap;
    private static final int CACHE_SLICE_V = 1;
    private static final int CACHE_SLICE_H = 2;
    private List<CornerRadii> normalizedFillCorners;
    private List<CornerRadii> normalizedStrokeCorners;
    private Shape shape;
    private NGShape ngShape;
    private float width;
    private float height;
    private int cacheMode;
    private Integer cacheKey;
    private static final Offset nopEffect;
    private EffectFilter nopEffectFilter;
    static final /* synthetic */ boolean $assertionsDisabled;
    private Background background = Background.EMPTY;
    private Insets backgroundInsets = Insets.EMPTY;
    private Border border = Border.EMPTY;
    private boolean scaleShape = true;
    private boolean centerShape = true;
    private boolean cacheShape = false;
    private float opaqueTop = Float.NaN;
    private float opaqueRight = Float.NaN;
    private float opaqueBottom = Float.NaN;
    private float opaqueLeft = Float.NaN;

    static {
        $assertionsDisabled = !NGRegion.class.desiredAssertionStatus();
        SCRATCH_AFFINE = new Affine2D();
        TEMP_RECT = new Rectangle();
        imageCacheMap = new WeakHashMap<>();
        nopEffect = new Offset(0, 0, null);
    }

    static Paint getPlatformPaint(javafx.scene.paint.Paint paint) {
        return (Paint) Toolkit.getPaintAccessor().getPlatformPaint(paint);
    }

    public void updateShape(Object shape, boolean scaleShape, boolean positionShape, boolean cacheShape) {
        this.ngShape = shape == null ? null : (NGShape) ((javafx.scene.shape.Shape) shape).impl_getPeer();
        this.shape = shape == null ? null : this.ngShape.getShape();
        this.scaleShape = scaleShape;
        this.centerShape = positionShape;
        this.cacheShape = cacheShape;
        invalidateOpaqueRegion();
        this.cacheKey = null;
        visualsChanged();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        invalidateOpaqueRegion();
        this.cacheKey = null;
        visualsChanged();
        if (this.background != null && this.background.isFillPercentageBased()) {
            this.backgroundInsets = null;
        }
    }

    public void imagesUpdated() {
        visualsChanged();
    }

    public void updateBorder(Border b2) {
        Border old = this.border;
        this.border = b2 == null ? Border.EMPTY : b2;
        if (!this.border.getOutsets().equals(old.getOutsets())) {
            geometryChanged();
        } else {
            visualsChanged();
        }
    }

    public void updateStrokeCorners(List<CornerRadii> normalizedStrokeCorners) {
        this.normalizedStrokeCorners = normalizedStrokeCorners;
    }

    private CornerRadii getNormalizedStrokeRadii(int index) {
        if (this.normalizedStrokeCorners == null) {
            return this.border.getStrokes().get(index).getRadii();
        }
        return this.normalizedStrokeCorners.get(index);
    }

    public void updateBackground(Background b2) {
        Background old = this.background;
        this.background = b2 == null ? Background.EMPTY : b2;
        List<BackgroundFill> fills = this.background.getFills();
        this.cacheMode = 0;
        if (!PrismSettings.disableRegionCaching && !fills.isEmpty() && (this.shape == null || this.cacheShape)) {
            this.cacheMode = 3;
            int max = fills.size();
            for (int i2 = 0; i2 < max && this.cacheMode != 0; i2++) {
                BackgroundFill fill = fills.get(i2);
                javafx.scene.paint.Paint paint = fill.getFill();
                if (this.shape == null) {
                    if (paint instanceof LinearGradient) {
                        LinearGradient linear = (LinearGradient) paint;
                        if (linear.getStartX() != linear.getEndX()) {
                            this.cacheMode &= -3;
                        }
                        if (linear.getStartY() != linear.getEndY()) {
                            this.cacheMode &= -2;
                        }
                    } else if (!(paint instanceof Color)) {
                        this.cacheMode = 0;
                    }
                } else if (paint instanceof ImagePattern) {
                    this.cacheMode = 0;
                }
            }
        }
        this.backgroundInsets = null;
        this.cacheKey = null;
        if (!this.background.getOutsets().equals(old.getOutsets())) {
            geometryChanged();
        } else {
            visualsChanged();
        }
    }

    public void updateFillCorners(List<CornerRadii> normalizedFillCorners) {
        this.normalizedFillCorners = normalizedFillCorners;
    }

    private CornerRadii getNormalizedFillRadii(int index) {
        if (this.normalizedFillCorners == null) {
            return this.background.getFills().get(index).getRadii();
        }
        return this.normalizedFillCorners.get(index);
    }

    public void setOpaqueInsets(float top, float right, float bottom, float left) {
        this.opaqueTop = top;
        this.opaqueRight = right;
        this.opaqueBottom = bottom;
        this.opaqueLeft = left;
        invalidateOpaqueRegion();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void clearDirtyTree() {
        super.clearDirtyTree();
        if (this.ngShape != null) {
            this.ngShape.clearDirtyTree();
        }
    }

    private RegionImageCache getImageCache(Graphics g2) {
        Screen screen = g2.getAssociatedScreen();
        RegionImageCache cache = imageCacheMap.get(screen);
        if (cache != null) {
            RTTexture tex = cache.getBackingStore();
            if (tex.isSurfaceLost()) {
                imageCacheMap.remove(screen);
                cache = null;
            }
        }
        if (cache == null) {
            cache = new RegionImageCache(g2.getResourceFactory());
            imageCacheMap.put(screen, cache);
        }
        return cache;
    }

    private Integer getCacheKey(int w2, int h2) {
        if (this.cacheKey == null) {
            int key = 31 * w2;
            int key2 = (((key * 37) + h2) * 47) + this.background.hashCode();
            if (this.shape != null) {
                key2 = (key2 * 73) + this.shape.hashCode();
            }
            this.cacheKey = Integer.valueOf(key2);
        }
        return this.cacheKey;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOpaqueRegion() {
        return (!super.hasOpaqueRegion() || Float.isNaN(this.opaqueTop) || Float.isNaN(this.opaqueRight) || Float.isNaN(this.opaqueBottom) || Float.isNaN(this.opaqueLeft)) ? false : true;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected RectBounds computeOpaqueRegion(RectBounds opaqueRegion) {
        return (RectBounds) opaqueRegion.deriveWithNewBounds(this.opaqueLeft, this.opaqueTop, 0.0f, this.width - this.opaqueRight, this.height - this.opaqueBottom, 0.0f);
    }

    @Override // com.sun.javafx.sg.prism.NGGroup, com.sun.javafx.sg.prism.NGNode
    protected NGNode.RenderRootResult computeRenderRoot(NodePath path, RectBounds dirtyRegion, int cullingIndex, BaseTransform tx, GeneralTransform3D pvTx) {
        NGNode.RenderRootResult result = super.computeRenderRoot(path, dirtyRegion, cullingIndex, tx, pvTx);
        if (result == NGNode.RenderRootResult.NO_RENDER_ROOT) {
            result = computeNodeRenderRoot(path, dirtyRegion, cullingIndex, tx, pvTx);
        }
        return result;
    }

    @Override // com.sun.javafx.sg.prism.NGGroup, com.sun.javafx.sg.prism.NGNode
    protected boolean hasVisuals() {
        return (this.border.isEmpty() && this.background.isEmpty()) ? false : true;
    }

    @Override // com.sun.javafx.sg.prism.NGGroup, com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGGroup, com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        if (!g2.getTransformNoClone().is2D() && isContentBounds2D()) {
            if (!$assertionsDisabled && getEffectFilter() != null) {
                throw new AssertionError();
            }
            if (this.nopEffectFilter == null) {
                this.nopEffectFilter = new EffectFilter(nopEffect, this);
            }
            this.nopEffectFilter.render(g2);
            return;
        }
        if (this.shape != null) {
            renderAsShape(g2);
        } else if (this.width > 0.0f && this.height > 0.0f) {
            renderAsRectangle(g2);
        }
        super.renderContent(g2);
    }

    private void renderAsShape(Graphics g2) {
        if (!this.background.isEmpty()) {
            Insets outsets = this.background.getOutsets();
            Shape outsetShape = resizeShape((float) (-outsets.getTop()), (float) (-outsets.getRight()), (float) (-outsets.getBottom()), (float) (-outsets.getLeft()));
            RectBounds outsetShapeBounds = outsetShape.getBounds();
            int textureWidth = Math.round(outsetShapeBounds.getWidth());
            int textureHeight = Math.round(outsetShapeBounds.getHeight());
            RTTexture cached = null;
            Rectangle rect = null;
            if (this.cacheMode != 0 && g2.getTransformNoClone().isTranslateOrIdentity()) {
                RegionImageCache imageCache = getImageCache(g2);
                if (imageCache.isImageCachable(textureWidth, textureHeight)) {
                    Integer key = getCacheKey(textureWidth, textureHeight);
                    rect = TEMP_RECT;
                    rect.setBounds(0, 0, textureWidth + 1, textureHeight + 1);
                    boolean render = imageCache.getImageLocation(key, rect, this.background, this.shape, g2);
                    if (!rect.isEmpty()) {
                        cached = imageCache.getBackingStore();
                    }
                    if (cached != null && render) {
                        Graphics cachedGraphics = cached.createGraphics();
                        cachedGraphics.translate(rect.f11913x - outsetShapeBounds.getMinX(), rect.f11914y - outsetShapeBounds.getMinY());
                        renderBackgroundShape(cachedGraphics);
                        if (PulseLogger.PULSE_LOGGING_ENABLED) {
                            PulseLogger.incrementCounter("Rendering region shape image to cache");
                        }
                    }
                }
            }
            if (cached != null) {
                float dstX1 = outsetShapeBounds.getMinX();
                float dstY1 = outsetShapeBounds.getMinY();
                float dstX2 = outsetShapeBounds.getMaxX();
                float dstY2 = outsetShapeBounds.getMaxY();
                float srcX1 = rect.f11913x;
                float srcY1 = rect.f11914y;
                float srcX2 = srcX1 + textureWidth;
                float srcY2 = srcY1 + textureHeight;
                g2.drawTexture(cached, dstX1, dstY1, dstX2, dstY2, srcX1, srcY1, srcX2, srcY2);
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.incrementCounter("Cached region shape image used");
                }
            } else {
                renderBackgroundShape(g2);
            }
        }
        if (!this.border.isEmpty()) {
            List<BorderStroke> strokes = this.border.getStrokes();
            int max = strokes.size();
            for (int i2 = 0; i2 < max; i2++) {
                BorderStroke stroke = strokes.get(i2);
                setBorderStyle(g2, stroke, -1.0d, false);
                Insets insets = stroke.getInsets();
                g2.draw(resizeShape((float) insets.getTop(), (float) insets.getRight(), (float) insets.getBottom(), (float) insets.getLeft()));
            }
        }
    }

    private void renderBackgroundShape(Graphics g2) {
        com.sun.prism.paint.ImagePattern imagePattern;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter("NGRegion renderBackgroundShape slow path");
            PulseLogger.addMessage("Slow shape path for " + getName());
        }
        List<BackgroundFill> fills = this.background.getFills();
        int max = fills.size();
        for (int i2 = 0; i2 < max; i2++) {
            BackgroundFill fill = fills.get(i2);
            Paint paint = getPlatformPaint(fill.getFill());
            if (!$assertionsDisabled && paint == null) {
                throw new AssertionError();
            }
            g2.setPaint(paint);
            Insets insets = fill.getInsets();
            g2.fill(resizeShape((float) insets.getTop(), (float) insets.getRight(), (float) insets.getBottom(), (float) insets.getLeft()));
        }
        List<BackgroundImage> images = this.background.getImages();
        int max2 = images.size();
        for (int i3 = 0; i3 < max2; i3++) {
            BackgroundImage image = images.get(i3);
            Image prismImage = (Image) image.getImage().impl_getPlatformImage();
            if (prismImage != null) {
                Shape translatedShape = resizeShape(0.0f, 0.0f, 0.0f, 0.0f);
                RectBounds bounds = translatedShape.getBounds();
                if (image.getSize().isCover()) {
                    imagePattern = new com.sun.prism.paint.ImagePattern(prismImage, bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight(), false, false);
                } else {
                    imagePattern = new com.sun.prism.paint.ImagePattern(prismImage, bounds.getMinX(), bounds.getMinY(), prismImage.getWidth(), prismImage.getHeight(), false, false);
                }
                com.sun.prism.paint.ImagePattern pattern = imagePattern;
                g2.setPaint(pattern);
                g2.fill(translatedShape);
            }
        }
    }

    private void renderAsRectangle(Graphics g2) {
        if (!this.background.isEmpty()) {
            renderBackgroundRectangle(g2);
        }
        if (!this.border.isEmpty()) {
            renderBorderRectangle(g2);
        }
    }

    private void renderBackgroundRectangle(Graphics g2) {
        double tileWidth;
        double tileHeight;
        double tileX;
        double tileY;
        if (this.backgroundInsets == null) {
            updateBackgroundInsets();
        }
        double leftInset = this.backgroundInsets.getLeft() + 1.0d;
        double rightInset = this.backgroundInsets.getRight() + 1.0d;
        double topInset = this.backgroundInsets.getTop() + 1.0d;
        double bottomInset = this.backgroundInsets.getBottom() + 1.0d;
        int cacheWidth = roundUp(this.width);
        if ((this.cacheMode & 2) != 0) {
            cacheWidth = Math.min(cacheWidth, (int) (leftInset + rightInset));
        }
        int cacheHeight = roundUp(this.height);
        if ((this.cacheMode & 1) != 0) {
            cacheHeight = Math.min(cacheHeight, (int) (topInset + bottomInset));
        }
        Insets outsets = this.background.getOutsets();
        int outsetsTop = roundUp(outsets.getTop());
        int outsetsRight = roundUp(outsets.getRight());
        int outsetsBottom = roundUp(outsets.getBottom());
        int outsetsLeft = roundUp(outsets.getLeft());
        int textureWidth = outsetsLeft + cacheWidth + outsetsRight;
        int textureHeight = outsetsTop + cacheHeight + outsetsBottom;
        boolean cache = this.background.getFills().size() > 1 && this.cacheMode != 0 && g2.getTransformNoClone().isTranslateOrIdentity();
        RTTexture cached = null;
        Rectangle rect = null;
        if (cache) {
            RegionImageCache imageCache = getImageCache(g2);
            if (imageCache.isImageCachable(textureWidth, textureHeight)) {
                Integer key = getCacheKey(textureWidth, textureHeight);
                rect = TEMP_RECT;
                rect.setBounds(0, 0, textureWidth + 1, textureHeight + 1);
                boolean render = imageCache.getImageLocation(key, rect, this.background, this.shape, g2);
                if (!rect.isEmpty()) {
                    cached = imageCache.getBackingStore();
                }
                if (cached != null && render) {
                    Graphics cacheGraphics = cached.createGraphics();
                    cacheGraphics.translate(rect.f11913x + outsetsLeft, rect.f11914y + outsetsTop);
                    renderBackgroundRectanglesDirectly(cacheGraphics, cacheWidth, cacheHeight);
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.incrementCounter("Rendering region background image to cache");
                    }
                }
            }
        }
        if (cached != null) {
            renderBackgroundRectangleFromCache(g2, cached, rect, textureWidth, textureHeight, topInset, rightInset, bottomInset, leftInset, outsetsTop, outsetsRight, outsetsBottom, outsetsLeft);
        } else {
            renderBackgroundRectanglesDirectly(g2, this.width, this.height);
        }
        List<BackgroundImage> images = this.background.getImages();
        int max = images.size();
        for (int i2 = 0; i2 < max; i2++) {
            BackgroundImage image = images.get(i2);
            Image prismImage = (Image) image.getImage().impl_getPlatformImage();
            if (prismImage != null) {
                int imgUnscaledWidth = (int) image.getImage().getWidth();
                int imgUnscaledHeight = (int) image.getImage().getHeight();
                int imgWidth = prismImage.getWidth();
                int imgHeight = prismImage.getHeight();
                if (imgWidth != 0 && imgHeight != 0) {
                    BackgroundSize size = image.getSize();
                    if (size.isCover()) {
                        float scale = Math.max(this.width / imgWidth, this.height / imgHeight);
                        Texture texture = g2.getResourceFactory().getCachedTexture(prismImage, Texture.WrapMode.CLAMP_TO_EDGE);
                        g2.drawTexture(texture, 0.0f, 0.0f, this.width, this.height, 0.0f, 0.0f, this.width / scale, this.height / scale);
                        texture.unlock();
                    } else {
                        double w2 = size.isWidthAsPercentage() ? size.getWidth() * this.width : size.getWidth();
                        double h2 = size.isHeightAsPercentage() ? size.getHeight() * this.height : size.getHeight();
                        if (size.isContain()) {
                            float scaleX = this.width / imgUnscaledWidth;
                            float scaleY = this.height / imgUnscaledHeight;
                            float scale2 = Math.min(scaleX, scaleY);
                            tileWidth = Math.ceil(scale2 * imgUnscaledWidth);
                            tileHeight = Math.ceil(scale2 * imgUnscaledHeight);
                        } else if (size.getWidth() >= 0.0d && size.getHeight() >= 0.0d) {
                            tileWidth = w2;
                            tileHeight = h2;
                        } else if (w2 >= 0.0d) {
                            tileWidth = w2;
                            double scale3 = tileWidth / imgUnscaledWidth;
                            tileHeight = imgUnscaledHeight * scale3;
                        } else if (h2 >= 0.0d) {
                            tileHeight = h2;
                            double scale4 = tileHeight / imgUnscaledHeight;
                            tileWidth = imgUnscaledWidth * scale4;
                        } else {
                            tileWidth = imgUnscaledWidth;
                            tileHeight = imgUnscaledHeight;
                        }
                        BackgroundPosition pos = image.getPosition();
                        if (pos.getHorizontalSide() == Side.LEFT) {
                            double position = pos.getHorizontalPosition();
                            if (pos.isHorizontalAsPercentage()) {
                                tileX = (position * this.width) - (position * tileWidth);
                            } else {
                                tileX = position;
                            }
                        } else if (pos.isHorizontalAsPercentage()) {
                            double position2 = 1.0d - pos.getHorizontalPosition();
                            tileX = (position2 * this.width) - (position2 * tileWidth);
                        } else {
                            tileX = (this.width - tileWidth) - pos.getHorizontalPosition();
                        }
                        if (pos.getVerticalSide() == Side.TOP) {
                            double position3 = pos.getVerticalPosition();
                            if (pos.isVerticalAsPercentage()) {
                                tileY = (position3 * this.height) - (position3 * tileHeight);
                            } else {
                                tileY = position3;
                            }
                        } else if (pos.isVerticalAsPercentage()) {
                            double position4 = 1.0d - pos.getVerticalPosition();
                            tileY = (position4 * this.height) - (position4 * tileHeight);
                        } else {
                            tileY = (this.height - tileHeight) - pos.getVerticalPosition();
                        }
                        paintTiles(g2, prismImage, image.getRepeatX(), image.getRepeatY(), pos.getHorizontalSide(), pos.getVerticalSide(), 0.0f, 0.0f, this.width, this.height, 0, 0, imgWidth, imgHeight, (float) tileX, (float) tileY, (float) tileWidth, (float) tileHeight);
                    }
                }
            }
        }
    }

    private void renderBackgroundRectangleFromCache(Graphics g2, RTTexture cached, Rectangle rect, int textureWidth, int textureHeight, double topInset, double rightInset, double bottomInset, double leftInset, int outsetsTop, int outsetsRight, int outsetsBottom, int outsetsLeft) {
        float dstWidth = outsetsLeft + this.width + outsetsRight;
        float dstHeight = outsetsTop + this.height + outsetsBottom;
        boolean sameWidth = ((float) textureWidth) == dstWidth;
        boolean sameHeight = ((float) textureHeight) == dstHeight;
        float dstX1 = (-outsetsLeft) - 0.49609375f;
        float dstY1 = (-outsetsTop) - 0.49609375f;
        float dstX2 = this.width + outsetsRight + 0.49609375f;
        float dstY2 = this.height + outsetsBottom + 0.49609375f;
        float srcX1 = rect.f11913x - 0.49609375f;
        float srcY1 = rect.f11914y - 0.49609375f;
        float srcX2 = rect.f11913x + textureWidth + 0.49609375f;
        float srcY2 = rect.f11914y + textureHeight + 0.49609375f;
        double adjustedLeftInset = leftInset;
        double adjustedRightInset = rightInset;
        double adjustedTopInset = topInset;
        double adjustedBottomInset = bottomInset;
        if (leftInset + rightInset > this.width) {
            double fraction = this.width / (leftInset + rightInset);
            adjustedLeftInset *= fraction;
            adjustedRightInset *= fraction;
        }
        if (topInset + bottomInset > this.height) {
            double fraction2 = this.height / (topInset + bottomInset);
            adjustedTopInset *= fraction2;
            adjustedBottomInset *= fraction2;
        }
        if (sameWidth && sameHeight) {
            g2.drawTexture(cached, dstX1, dstY1, dstX2, dstY2, srcX1, srcY1, srcX2, srcY2);
        } else if (sameHeight) {
            float left = 0.49609375f + ((float) (adjustedLeftInset + outsetsLeft));
            float right = 0.49609375f + ((float) (adjustedRightInset + outsetsRight));
            float dstLeftX = dstX1 + left;
            float dstRightX = dstX2 - right;
            float srcLeftX = srcX1 + left;
            float srcRightX = srcX2 - right;
            g2.drawTexture3SliceH(cached, dstX1, dstY1, dstX2, dstY2, srcX1, srcY1, srcX2, srcY2, dstLeftX, dstRightX, srcLeftX, srcRightX);
        } else if (sameWidth) {
            float top = 0.49609375f + ((float) (adjustedTopInset + outsetsTop));
            float bottom = 0.49609375f + ((float) (adjustedBottomInset + outsetsBottom));
            float dstTopY = dstY1 + top;
            float dstBottomY = dstY2 - bottom;
            float srcTopY = srcY1 + top;
            float srcBottomY = srcY2 - bottom;
            g2.drawTexture3SliceV(cached, dstX1, dstY1, dstX2, dstY2, srcX1, srcY1, srcX2, srcY2, dstTopY, dstBottomY, srcTopY, srcBottomY);
        } else {
            float left2 = 0.49609375f + ((float) (adjustedLeftInset + outsetsLeft));
            float top2 = 0.49609375f + ((float) (adjustedTopInset + outsetsTop));
            float right2 = 0.49609375f + ((float) (adjustedRightInset + outsetsRight));
            float bottom2 = 0.49609375f + ((float) (adjustedBottomInset + outsetsBottom));
            float dstLeftX2 = dstX1 + left2;
            float dstRightX2 = dstX2 - right2;
            float srcLeftX2 = srcX1 + left2;
            float srcRightX2 = srcX2 - right2;
            float dstTopY2 = dstY1 + top2;
            float dstBottomY2 = dstY2 - bottom2;
            float srcTopY2 = srcY1 + top2;
            float srcBottomY2 = srcY2 - bottom2;
            g2.drawTexture9Slice(cached, dstX1, dstY1, dstX2, dstY2, srcX1, srcY1, srcX2, srcY2, dstLeftX2, dstTopY2, dstRightX2, dstBottomY2, srcLeftX2, srcTopY2, srcRightX2, srcBottomY2);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter("Cached region background image used");
        }
    }

    private void renderBackgroundRectanglesDirectly(Graphics g2, float width, float height) {
        List<BackgroundFill> fills = this.background.getFills();
        int max = fills.size();
        for (int i2 = 0; i2 < max; i2++) {
            BackgroundFill fill = fills.get(i2);
            Insets insets = fill.getInsets();
            float t2 = (float) insets.getTop();
            float l2 = (float) insets.getLeft();
            float b2 = (float) insets.getBottom();
            float r2 = (float) insets.getRight();
            float w2 = (width - l2) - r2;
            float h2 = (height - t2) - b2;
            if (w2 > 0.0f && h2 > 0.0f) {
                Paint paint = getPlatformPaint(fill.getFill());
                g2.setPaint(paint);
                CornerRadii radii = getNormalizedFillRadii(i2);
                if (radii.isUniform() && (PlatformImpl.isCaspian() || PlatformUtil.isEmbedded() || PlatformUtil.isIOS() || radii.getTopLeftHorizontalRadius() <= 0.0d || radii.getTopLeftHorizontalRadius() > 4.0d)) {
                    float tlhr = (float) radii.getTopLeftHorizontalRadius();
                    float tlvr = (float) radii.getTopLeftVerticalRadius();
                    if (tlhr == 0.0f && tlvr == 0.0f) {
                        g2.fillRect(l2, t2, w2, h2);
                    } else {
                        float arcWidth = tlhr + tlhr;
                        float arcHeight = tlvr + tlvr;
                        if (arcWidth > w2) {
                            arcWidth = w2;
                        }
                        if (arcHeight > h2) {
                            arcHeight = h2;
                        }
                        g2.fillRoundRect(l2, t2, w2, h2, arcWidth, arcHeight);
                    }
                } else {
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.incrementCounter("NGRegion renderBackgrounds slow path");
                        PulseLogger.addMessage("Slow background path for " + getName());
                    }
                    g2.fill(createPath(width, height, t2, l2, b2, r2, radii));
                }
            }
        }
    }

    private void renderBorderRectangle(Graphics g2) {
        List<BorderImage> images = this.border.getImages();
        List<BorderStroke> strokes = images.isEmpty() ? this.border.getStrokes() : Collections.emptyList();
        int max = strokes.size();
        for (int i2 = 0; i2 < max; i2++) {
            BorderStroke stroke = strokes.get(i2);
            BorderWidths widths = stroke.getWidths();
            CornerRadii radii = getNormalizedStrokeRadii(i2);
            Insets insets = stroke.getInsets();
            javafx.scene.paint.Paint topStroke = stroke.getTopStroke();
            javafx.scene.paint.Paint rightStroke = stroke.getRightStroke();
            javafx.scene.paint.Paint bottomStroke = stroke.getBottomStroke();
            javafx.scene.paint.Paint leftStroke = stroke.getLeftStroke();
            float topInset = (float) insets.getTop();
            float rightInset = (float) insets.getRight();
            float bottomInset = (float) insets.getBottom();
            float leftInset = (float) insets.getLeft();
            float topWidth = (float) (widths.isTopAsPercentage() ? this.height * widths.getTop() : widths.getTop());
            float rightWidth = (float) (widths.isRightAsPercentage() ? this.width * widths.getRight() : widths.getRight());
            float bottomWidth = (float) (widths.isBottomAsPercentage() ? this.height * widths.getBottom() : widths.getBottom());
            float leftWidth = (float) (widths.isLeftAsPercentage() ? this.width * widths.getLeft() : widths.getLeft());
            BorderStrokeStyle topStyle = stroke.getTopStyle();
            BorderStrokeStyle rightStyle = stroke.getRightStyle();
            BorderStrokeStyle bottomStyle = stroke.getBottomStyle();
            BorderStrokeStyle leftStyle = stroke.getLeftStyle();
            StrokeType topType = topStyle.getType();
            StrokeType rightType = rightStyle.getType();
            StrokeType bottomType = bottomStyle.getType();
            StrokeType leftType = leftStyle.getType();
            float t2 = topInset + (topType == StrokeType.OUTSIDE ? (-topWidth) / 2.0f : topType == StrokeType.INSIDE ? topWidth / 2.0f : 0.0f);
            float l2 = leftInset + (leftType == StrokeType.OUTSIDE ? (-leftWidth) / 2.0f : leftType == StrokeType.INSIDE ? leftWidth / 2.0f : 0.0f);
            float b2 = bottomInset + (bottomType == StrokeType.OUTSIDE ? (-bottomWidth) / 2.0f : bottomType == StrokeType.INSIDE ? bottomWidth / 2.0f : 0.0f);
            float r2 = rightInset + (rightType == StrokeType.OUTSIDE ? (-rightWidth) / 2.0f : rightType == StrokeType.INSIDE ? rightWidth / 2.0f : 0.0f);
            float radius = (float) radii.getTopLeftHorizontalRadius();
            if (stroke.isStrokeUniform()) {
                if ((!(topStroke instanceof Color) || ((Color) topStroke).getOpacity() != 0.0d) && topStyle != BorderStrokeStyle.NONE) {
                    float w2 = (this.width - l2) - r2;
                    float h2 = (this.height - t2) - b2;
                    double di = 2.0d * radii.getTopLeftHorizontalRadius();
                    double circle = di * 3.141592653589793d;
                    double totalLineLength = circle + (2.0d * (w2 - di)) + (2.0d * (h2 - di));
                    if (w2 >= 0.0f && h2 >= 0.0f) {
                        setBorderStyle(g2, stroke, totalLineLength, true);
                        if (radii.isUniform() && radius == 0.0f) {
                            g2.drawRect(l2, t2, w2, h2);
                        } else if (radii.isUniform()) {
                            float ar2 = radius + radius;
                            if (ar2 > w2) {
                                ar2 = w2;
                            }
                            if (ar2 > h2) {
                                ar2 = h2;
                            }
                            g2.drawRoundRect(l2, t2, w2, h2, ar2, ar2);
                        } else {
                            g2.draw(createPath(this.width, this.height, t2, l2, b2, r2, radii));
                        }
                    }
                }
            } else if (radii.isUniform() && radius == 0.0f) {
                if ((!(topStroke instanceof Color) || ((Color) topStroke).getOpacity() != 0.0d) && topStyle != BorderStrokeStyle.NONE) {
                    g2.setPaint(getPlatformPaint(topStroke));
                    if (BorderStrokeStyle.SOLID == topStyle) {
                        g2.fillRect(leftInset, topInset, (this.width - leftInset) - rightInset, topWidth);
                    } else {
                        g2.setStroke(createStroke(topStyle, topWidth, this.width, true));
                        g2.drawLine(l2, t2, this.width - r2, t2);
                    }
                }
                if ((!(rightStroke instanceof Color) || ((Color) rightStroke).getOpacity() != 0.0d) && rightStyle != BorderStrokeStyle.NONE) {
                    g2.setPaint(getPlatformPaint(rightStroke));
                    if (BorderStrokeStyle.SOLID == rightStyle) {
                        g2.fillRect((this.width - rightInset) - rightWidth, topInset, rightWidth, (this.height - topInset) - bottomInset);
                    } else {
                        g2.setStroke(createStroke(rightStyle, rightWidth, this.height, true));
                        g2.drawLine(this.width - r2, t2, this.width - r2, this.height - b2);
                    }
                }
                if ((!(bottomStroke instanceof Color) || ((Color) bottomStroke).getOpacity() != 0.0d) && bottomStyle != BorderStrokeStyle.NONE) {
                    g2.setPaint(getPlatformPaint(bottomStroke));
                    if (BorderStrokeStyle.SOLID == bottomStyle) {
                        g2.fillRect(leftInset, (this.height - bottomInset) - bottomWidth, (this.width - leftInset) - rightInset, bottomWidth);
                    } else {
                        g2.setStroke(createStroke(bottomStyle, bottomWidth, this.width, true));
                        g2.drawLine(l2, this.height - b2, this.width - r2, this.height - b2);
                    }
                }
                if ((!(leftStroke instanceof Color) || ((Color) leftStroke).getOpacity() != 0.0d) && leftStyle != BorderStrokeStyle.NONE) {
                    g2.setPaint(getPlatformPaint(leftStroke));
                    if (BorderStrokeStyle.SOLID == leftStyle) {
                        g2.fillRect(leftInset, topInset, leftWidth, (this.height - topInset) - bottomInset);
                    } else {
                        g2.setStroke(createStroke(leftStyle, leftWidth, this.height, true));
                        g2.drawLine(l2, t2, l2, this.height - b2);
                    }
                }
            } else {
                Shape[] paths = createPaths(t2, l2, b2, r2, radii);
                if (topStyle != BorderStrokeStyle.NONE) {
                    double rsum = radii.getTopLeftHorizontalRadius() + radii.getTopRightHorizontalRadius();
                    double topLineLength = this.width + (rsum * (-0.21460183660255172d));
                    g2.setStroke(createStroke(topStyle, topWidth, topLineLength, true));
                    g2.setPaint(getPlatformPaint(topStroke));
                    g2.draw(paths[0]);
                }
                if (rightStyle != BorderStrokeStyle.NONE) {
                    double rsum2 = radii.getTopRightVerticalRadius() + radii.getBottomRightVerticalRadius();
                    double rightLineLength = this.height + (rsum2 * (-0.21460183660255172d));
                    g2.setStroke(createStroke(rightStyle, rightWidth, rightLineLength, true));
                    g2.setPaint(getPlatformPaint(rightStroke));
                    g2.draw(paths[1]);
                }
                if (bottomStyle != BorderStrokeStyle.NONE) {
                    double rsum3 = radii.getBottomLeftHorizontalRadius() + radii.getBottomRightHorizontalRadius();
                    double bottomLineLength = this.width + (rsum3 * (-0.21460183660255172d));
                    g2.setStroke(createStroke(bottomStyle, bottomWidth, bottomLineLength, true));
                    g2.setPaint(getPlatformPaint(bottomStroke));
                    g2.draw(paths[2]);
                }
                if (leftStyle != BorderStrokeStyle.NONE) {
                    double rsum4 = radii.getTopLeftVerticalRadius() + radii.getBottomLeftVerticalRadius();
                    double leftLineLength = this.height + (rsum4 * (-0.21460183660255172d));
                    g2.setStroke(createStroke(leftStyle, leftWidth, leftLineLength, true));
                    g2.setPaint(getPlatformPaint(leftStroke));
                    g2.draw(paths[3]);
                }
            }
        }
        int max2 = images.size();
        for (int i3 = 0; i3 < max2; i3++) {
            BorderImage ib = images.get(i3);
            Image prismImage = (Image) ib.getImage().impl_getPlatformImage();
            if (prismImage != null) {
                int imgWidth = prismImage.getWidth();
                int imgHeight = prismImage.getHeight();
                float imgScale = prismImage.getPixelScale();
                BorderWidths widths2 = ib.getWidths();
                Insets insets2 = ib.getInsets();
                BorderWidths slices = ib.getSlices();
                int topInset2 = (int) Math.round(insets2.getTop());
                int rightInset2 = (int) Math.round(insets2.getRight());
                int bottomInset2 = (int) Math.round(insets2.getBottom());
                int leftInset2 = (int) Math.round(insets2.getLeft());
                int topWidth2 = widthSize(widths2.isTopAsPercentage(), widths2.getTop(), this.height);
                int rightWidth2 = widthSize(widths2.isRightAsPercentage(), widths2.getRight(), this.width);
                int bottomWidth2 = widthSize(widths2.isBottomAsPercentage(), widths2.getBottom(), this.height);
                int leftWidth2 = widthSize(widths2.isLeftAsPercentage(), widths2.getLeft(), this.width);
                int topSlice = sliceSize(slices.isTopAsPercentage(), slices.getTop(), imgHeight, imgScale);
                int rightSlice = sliceSize(slices.isRightAsPercentage(), slices.getRight(), imgWidth, imgScale);
                int bottomSlice = sliceSize(slices.isBottomAsPercentage(), slices.getBottom(), imgHeight, imgScale);
                int leftSlice = sliceSize(slices.isLeftAsPercentage(), slices.getLeft(), imgWidth, imgScale);
                if (leftInset2 + leftWidth2 + rightInset2 + rightWidth2 <= this.width && topInset2 + topWidth2 + bottomInset2 + bottomWidth2 <= this.height) {
                    int centerMinX = leftInset2 + leftWidth2;
                    int centerMinY = topInset2 + topWidth2;
                    int centerW = ((Math.round(this.width) - rightInset2) - rightWidth2) - centerMinX;
                    int centerH = ((Math.round(this.height) - bottomInset2) - bottomWidth2) - centerMinY;
                    int centerMaxX = centerW + centerMinX;
                    int centerMaxY = centerH + centerMinY;
                    int centerSliceWidth = (imgWidth - leftSlice) - rightSlice;
                    int centerSliceHeight = (imgHeight - topSlice) - bottomSlice;
                    paintTiles(g2, prismImage, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, leftInset2, topInset2, leftWidth2, topWidth2, 0, 0, leftSlice, topSlice, 0.0f, 0.0f, leftWidth2, topWidth2);
                    float tileWidth = ib.getRepeatX() == BorderRepeat.STRETCH ? centerW : topSlice > 0 ? (centerSliceWidth * topWidth2) / topSlice : 0;
                    paintTiles(g2, prismImage, ib.getRepeatX(), BorderRepeat.STRETCH, Side.LEFT, Side.TOP, centerMinX, topInset2, centerW, topWidth2, leftSlice, 0, centerSliceWidth, topSlice, (centerW - tileWidth) / 2.0f, 0.0f, tileWidth, topWidth2);
                    paintTiles(g2, prismImage, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, centerMaxX, topInset2, rightWidth2, topWidth2, imgWidth - rightSlice, 0, rightSlice, topSlice, 0.0f, 0.0f, rightWidth2, topWidth2);
                    float tileWidth2 = leftWidth2;
                    float tileHeight = ib.getRepeatY() == BorderRepeat.STRETCH ? centerH : leftSlice > 0 ? (leftWidth2 * centerSliceHeight) / leftSlice : 0;
                    paintTiles(g2, prismImage, BorderRepeat.STRETCH, ib.getRepeatY(), Side.LEFT, Side.TOP, leftInset2, centerMinY, leftWidth2, centerH, 0, topSlice, leftSlice, centerSliceHeight, 0.0f, (centerH - tileHeight) / 2.0f, tileWidth2, tileHeight);
                    float tileWidth3 = rightWidth2;
                    float tileHeight2 = ib.getRepeatY() == BorderRepeat.STRETCH ? centerH : rightSlice > 0 ? (rightWidth2 * centerSliceHeight) / rightSlice : 0;
                    paintTiles(g2, prismImage, BorderRepeat.STRETCH, ib.getRepeatY(), Side.LEFT, Side.TOP, centerMaxX, centerMinY, rightWidth2, centerH, imgWidth - rightSlice, topSlice, rightSlice, centerSliceHeight, 0.0f, (centerH - tileHeight2) / 2.0f, tileWidth3, tileHeight2);
                    paintTiles(g2, prismImage, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, leftInset2, centerMaxY, leftWidth2, bottomWidth2, 0, imgHeight - bottomSlice, leftSlice, bottomSlice, 0.0f, 0.0f, leftWidth2, bottomWidth2);
                    float tileWidth4 = ib.getRepeatX() == BorderRepeat.STRETCH ? centerW : bottomSlice > 0 ? (centerSliceWidth * bottomWidth2) / bottomSlice : 0;
                    paintTiles(g2, prismImage, ib.getRepeatX(), BorderRepeat.STRETCH, Side.LEFT, Side.TOP, centerMinX, centerMaxY, centerW, bottomWidth2, leftSlice, imgHeight - bottomSlice, centerSliceWidth, bottomSlice, (centerW - tileWidth4) / 2.0f, 0.0f, tileWidth4, bottomWidth2);
                    paintTiles(g2, prismImage, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, centerMaxX, centerMaxY, rightWidth2, bottomWidth2, imgWidth - rightSlice, imgHeight - bottomSlice, rightSlice, bottomSlice, 0.0f, 0.0f, rightWidth2, bottomWidth2);
                    if (ib.isFilled()) {
                        float imgW = ib.getRepeatX() == BorderRepeat.STRETCH ? centerW : centerSliceWidth;
                        float imgH = ib.getRepeatY() == BorderRepeat.STRETCH ? centerH : centerSliceHeight;
                        paintTiles(g2, prismImage, ib.getRepeatX(), ib.getRepeatY(), Side.LEFT, Side.TOP, centerMinX, centerMinY, centerW, centerH, leftSlice, topSlice, centerSliceWidth, centerSliceHeight, 0.0f, 0.0f, imgW, imgH);
                    }
                }
            }
        }
    }

    private void updateBackgroundInsets() {
        float top = 0.0f;
        float right = 0.0f;
        float bottom = 0.0f;
        float left = 0.0f;
        List<BackgroundFill> fills = this.background.getFills();
        int max = fills.size();
        for (int i2 = 0; i2 < max; i2++) {
            BackgroundFill fill = fills.get(i2);
            Insets insets = fill.getInsets();
            CornerRadii radii = getNormalizedFillRadii(i2);
            top = (float) Math.max(top, insets.getTop() + Math.max(radii.getTopLeftVerticalRadius(), radii.getTopRightVerticalRadius()));
            right = (float) Math.max(right, insets.getRight() + Math.max(radii.getTopRightHorizontalRadius(), radii.getBottomRightHorizontalRadius()));
            bottom = (float) Math.max(bottom, insets.getBottom() + Math.max(radii.getBottomRightVerticalRadius(), radii.getBottomLeftVerticalRadius()));
            left = (float) Math.max(left, insets.getLeft() + Math.max(radii.getTopLeftHorizontalRadius(), radii.getBottomLeftHorizontalRadius()));
        }
        this.backgroundInsets = new Insets(roundUp(top), roundUp(right), roundUp(bottom), roundUp(left));
    }

    private int widthSize(boolean isPercent, double sliceSize, float objSize) {
        return (int) Math.round(isPercent ? sliceSize * objSize : sliceSize);
    }

    private int sliceSize(boolean isPercent, double sliceSize, float objSize, float scale) {
        if (isPercent) {
            sliceSize *= objSize;
        }
        if (sliceSize > objSize) {
            sliceSize = objSize;
        }
        return (int) Math.round(sliceSize * scale);
    }

    private int roundUp(double d2) {
        return d2 - ((double) ((int) d2)) == 0.0d ? (int) d2 : (int) (d2 + 1.0d);
    }

    private BasicStroke createStroke(BorderStrokeStyle sb, double strokeWidth, double lineLength, boolean forceCentered) {
        int cap;
        int join;
        int type;
        BasicStroke bs2;
        double[] array;
        float dashOffset;
        if (sb.getLineCap() == StrokeLineCap.BUTT) {
            cap = 0;
        } else if (sb.getLineCap() == StrokeLineCap.SQUARE) {
            cap = 2;
        } else {
            cap = 1;
        }
        if (sb.getLineJoin() == StrokeLineJoin.BEVEL) {
            join = 2;
        } else if (sb.getLineJoin() == StrokeLineJoin.MITER) {
            join = 0;
        } else {
            join = 1;
        }
        if (forceCentered) {
            type = 0;
        } else if (this.scaleShape) {
            type = 1;
        } else {
            switch (sb.getType()) {
                case INSIDE:
                    type = 1;
                    break;
                case OUTSIDE:
                    type = 2;
                    break;
                case CENTERED:
                default:
                    type = 0;
                    break;
            }
        }
        if (sb == BorderStrokeStyle.NONE) {
            throw new AssertionError((Object) "Should never have been asked to draw a border with NONE");
        }
        if (strokeWidth <= 0.0d) {
            bs2 = new BasicStroke((float) strokeWidth, cap, join, (float) sb.getMiterLimit());
        } else if (sb.getDashArray().size() > 0) {
            List<Double> dashArray = sb.getDashArray();
            if (dashArray == BorderStrokeStyle.DOTTED.getDashArray()) {
                if (lineLength > 0.0d) {
                    double remainder = lineLength % (strokeWidth * 2.0d);
                    double numSpaces = lineLength / (strokeWidth * 2.0d);
                    double spaceWidth = (strokeWidth * 2.0d) + (remainder / numSpaces);
                    array = new double[]{0.0d, spaceWidth};
                    dashOffset = 0.0f;
                } else {
                    array = new double[]{0.0d, strokeWidth * 2.0d};
                    dashOffset = 0.0f;
                }
            } else if (dashArray != BorderStrokeStyle.DASHED.getDashArray()) {
                array = new double[dashArray.size()];
                for (int i2 = 0; i2 < array.length; i2++) {
                    array[i2] = dashArray.get(i2).doubleValue();
                }
                dashOffset = (float) sb.getDashOffset();
            } else if (lineLength > 0.0d) {
                double dashLength = strokeWidth * 2.0d;
                double gapLength = strokeWidth * 1.4d;
                double segmentLength = dashLength + gapLength;
                double divided = lineLength / segmentLength;
                double numSegments = (int) divided;
                if (numSegments > 0.0d) {
                    double dashCumulative = numSegments * dashLength;
                    gapLength = (lineLength - dashCumulative) / numSegments;
                }
                array = new double[]{dashLength, gapLength};
                dashOffset = (float) (dashLength * 0.6d);
            } else {
                array = new double[]{2.0d * strokeWidth, 1.4d * strokeWidth};
                dashOffset = 0.0f;
            }
            bs2 = new BasicStroke(type, (float) strokeWidth, cap, join, (float) sb.getMiterLimit(), array, dashOffset);
        } else {
            bs2 = new BasicStroke(type, (float) strokeWidth, cap, join, (float) sb.getMiterLimit());
        }
        return bs2;
    }

    private void setBorderStyle(Graphics g2, BorderStroke sb, double length, boolean forceCentered) {
        BorderWidths widths = sb.getWidths();
        BorderStrokeStyle bs2 = sb.getTopStyle();
        double sbWidth = widths.isTopAsPercentage() ? this.height * widths.getTop() : widths.getTop();
        Paint sbFill = getPlatformPaint(sb.getTopStroke());
        if (bs2 == null) {
            bs2 = sb.getLeftStyle();
            sbWidth = widths.isLeftAsPercentage() ? this.width * widths.getLeft() : widths.getLeft();
            sbFill = getPlatformPaint(sb.getLeftStroke());
            if (bs2 == null) {
                bs2 = sb.getBottomStyle();
                sbWidth = widths.isBottomAsPercentage() ? this.height * widths.getBottom() : widths.getBottom();
                sbFill = getPlatformPaint(sb.getBottomStroke());
                if (bs2 == null) {
                    bs2 = sb.getRightStyle();
                    sbWidth = widths.isRightAsPercentage() ? this.width * widths.getRight() : widths.getRight();
                    sbFill = getPlatformPaint(sb.getRightStroke());
                }
            }
        }
        if (bs2 == null || bs2 == BorderStrokeStyle.NONE) {
            return;
        }
        g2.setStroke(createStroke(bs2, sbWidth, length, forceCentered));
        g2.setPaint(sbFill);
    }

    private void doCorner(Path2D path, CornerRadii radii, float x2, float y2, int quadrant, float tstart, float tend, boolean newPath) {
        float hr;
        float vr;
        float dx0;
        float dy0;
        float dx1;
        float dy1;
        switch (quadrant & 3) {
            case 0:
                hr = (float) radii.getTopLeftHorizontalRadius();
                vr = (float) radii.getTopLeftVerticalRadius();
                dx0 = 0.0f;
                dy0 = vr;
                dx1 = hr;
                dy1 = 0.0f;
                break;
            case 1:
                hr = (float) radii.getTopRightHorizontalRadius();
                vr = (float) radii.getTopRightVerticalRadius();
                dx0 = -hr;
                dy0 = 0.0f;
                dx1 = 0.0f;
                dy1 = vr;
                break;
            case 2:
                hr = (float) radii.getBottomRightHorizontalRadius();
                vr = (float) radii.getBottomRightVerticalRadius();
                dx0 = 0.0f;
                dy0 = -vr;
                dx1 = -hr;
                dy1 = 0.0f;
                break;
            case 3:
                hr = (float) radii.getBottomLeftHorizontalRadius();
                vr = (float) radii.getBottomLeftVerticalRadius();
                dx0 = hr;
                dy0 = 0.0f;
                dx1 = 0.0f;
                dy1 = -vr;
                break;
            default:
                return;
        }
        if (hr > 0.0f && vr > 0.0f) {
            path.appendOvalQuadrant(x2 + dx0, y2 + dy0, x2, y2, x2 + dx1, y2 + dy1, tstart, tend, newPath ? Path2D.CornerPrefix.MOVE_THEN_CORNER : Path2D.CornerPrefix.LINE_THEN_CORNER);
        } else if (newPath) {
            path.moveTo(x2, y2);
        } else {
            path.lineTo(x2, y2);
        }
    }

    private Path2D createPath(float width, float height, float t2, float l2, float bo2, float ro, CornerRadii radii) {
        float r2 = width - ro;
        float b2 = height - bo2;
        Path2D path = new Path2D();
        doCorner(path, radii, l2, t2, 0, 0.0f, 1.0f, true);
        doCorner(path, radii, r2, t2, 1, 0.0f, 1.0f, false);
        doCorner(path, radii, r2, b2, 2, 0.0f, 1.0f, false);
        doCorner(path, radii, l2, b2, 3, 0.0f, 1.0f, false);
        path.closePath();
        return path;
    }

    private Path2D makeRoundedEdge(CornerRadii radii, float x0, float y0, float x1, float y1, int quadrant) {
        Path2D path = new Path2D();
        doCorner(path, radii, x0, y0, quadrant, 0.5f, 1.0f, true);
        doCorner(path, radii, x1, y1, quadrant + 1, 0.0f, 0.5f, false);
        return path;
    }

    private Path2D[] createPaths(float t2, float l2, float bo2, float ro, CornerRadii radii) {
        float r2 = this.width - ro;
        float b2 = this.height - bo2;
        return new Path2D[]{makeRoundedEdge(radii, l2, t2, r2, t2, 0), makeRoundedEdge(radii, r2, t2, r2, b2, 1), makeRoundedEdge(radii, r2, b2, l2, b2, 2), makeRoundedEdge(radii, l2, b2, l2, t2, 3)};
    }

    private Shape resizeShape(float topOffset, float rightOffset, float bottomOffset, float leftOffset) {
        RectBounds bounds = this.shape.getBounds();
        if (this.scaleShape) {
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate(leftOffset, topOffset);
            float w2 = (this.width - leftOffset) - rightOffset;
            float h2 = (this.height - topOffset) - bottomOffset;
            SCRATCH_AFFINE.scale(w2 / bounds.getWidth(), h2 / bounds.getHeight());
            if (this.centerShape) {
                SCRATCH_AFFINE.translate(-bounds.getMinX(), -bounds.getMinY());
            }
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        if (this.centerShape) {
            float boundsWidth = bounds.getWidth();
            float boundsHeight = bounds.getHeight();
            float newW = (boundsWidth - leftOffset) - rightOffset;
            float newH = (boundsHeight - topOffset) - bottomOffset;
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate((leftOffset + ((this.width - boundsWidth) / 2.0f)) - bounds.getMinX(), (topOffset + ((this.height - boundsHeight) / 2.0f)) - bounds.getMinY());
            if (newH != boundsHeight || newW != boundsWidth) {
                SCRATCH_AFFINE.translate(bounds.getMinX(), bounds.getMinY());
                SCRATCH_AFFINE.scale(newW / boundsWidth, newH / boundsHeight);
                SCRATCH_AFFINE.translate(-bounds.getMinX(), -bounds.getMinY());
            }
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        if (topOffset != 0.0f || rightOffset != 0.0f || bottomOffset != 0.0f || leftOffset != 0.0f) {
            float newW2 = (bounds.getWidth() - leftOffset) - rightOffset;
            float newH2 = (bounds.getHeight() - topOffset) - bottomOffset;
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate(leftOffset, topOffset);
            SCRATCH_AFFINE.translate(bounds.getMinX(), bounds.getMinY());
            SCRATCH_AFFINE.scale(newW2 / bounds.getWidth(), newH2 / bounds.getHeight());
            SCRATCH_AFFINE.translate(-bounds.getMinX(), -bounds.getMinY());
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        return this.shape;
    }

    private void paintTiles(Graphics g2, Image img, BorderRepeat repeatX, BorderRepeat repeatY, Side horizontalSide, Side verticalSide, float regionX, float regionY, float regionWidth, float regionHeight, int srcX, int srcY, int srcW, int srcH, float tileX, float tileY, float tileWidth, float tileHeight) {
        BackgroundRepeat rx = null;
        BackgroundRepeat ry = null;
        switch (repeatX) {
            case REPEAT:
                rx = BackgroundRepeat.REPEAT;
                break;
            case STRETCH:
                rx = BackgroundRepeat.NO_REPEAT;
                break;
            case ROUND:
                rx = BackgroundRepeat.ROUND;
                break;
            case SPACE:
                rx = BackgroundRepeat.SPACE;
                break;
        }
        switch (repeatY) {
            case REPEAT:
                ry = BackgroundRepeat.REPEAT;
                break;
            case STRETCH:
                ry = BackgroundRepeat.NO_REPEAT;
                break;
            case ROUND:
                ry = BackgroundRepeat.ROUND;
                break;
            case SPACE:
                ry = BackgroundRepeat.SPACE;
                break;
        }
        paintTiles(g2, img, rx, ry, horizontalSide, verticalSide, regionX, regionY, regionWidth, regionHeight, srcX, srcY, srcW, srcH, tileX, tileY, tileWidth, tileHeight);
    }

    private void paintTiles(Graphics g2, Image img, BackgroundRepeat repeatX, BackgroundRepeat repeatY, Side horizontalSide, Side verticalSide, float regionX, float regionY, float regionWidth, float regionHeight, int srcX, int srcY, int srcW, int srcH, float tileX, float tileY, float tileWidth, float tileHeight) {
        int countX;
        float xIncrement;
        int countY;
        float yIncrement;
        if (regionWidth <= 0.0f || regionHeight <= 0.0f || srcW <= 0 || srcH <= 0) {
            return;
        }
        if (!$assertionsDisabled && (srcX < 0 || srcY < 0 || srcW <= 0 || srcH <= 0)) {
            throw new AssertionError();
        }
        if (tileX == 0.0f && tileY == 0.0f && repeatX == BackgroundRepeat.REPEAT && repeatY == BackgroundRepeat.REPEAT) {
            if (srcX != 0 || srcY != 0 || srcW != img.getWidth() || srcH != img.getHeight()) {
                img = img.createSubImage(srcX, srcY, srcW, srcH);
            }
            g2.setPaint(new com.sun.prism.paint.ImagePattern(img, 0.0f, 0.0f, tileWidth, tileHeight, false, false));
            g2.fillRect(regionX, regionY, regionWidth, regionHeight);
            return;
        }
        if (repeatX == BackgroundRepeat.SPACE && regionWidth < tileWidth * 2.0f) {
            repeatX = BackgroundRepeat.NO_REPEAT;
        }
        if (repeatY == BackgroundRepeat.SPACE && regionHeight < tileHeight * 2.0f) {
            repeatY = BackgroundRepeat.NO_REPEAT;
        }
        if (repeatX == BackgroundRepeat.REPEAT) {
            float offsetX = 0.0f;
            if (tileX != 0.0f) {
                float mod = tileX % tileWidth;
                tileX = mod == 0.0f ? 0.0f : tileX < 0.0f ? mod : mod - tileWidth;
                offsetX = tileX;
            }
            countX = (int) Math.max(1.0d, Math.ceil((regionWidth - offsetX) / tileWidth));
            xIncrement = horizontalSide == Side.RIGHT ? -tileWidth : tileWidth;
        } else if (repeatX == BackgroundRepeat.SPACE) {
            tileX = 0.0f;
            countX = (int) (regionWidth / tileWidth);
            float remainder = regionWidth % tileWidth;
            xIncrement = tileWidth + (remainder / (countX - 1));
        } else if (repeatX == BackgroundRepeat.ROUND) {
            tileX = 0.0f;
            countX = (int) (regionWidth / tileWidth);
            tileWidth = regionWidth / ((int) (regionWidth / tileWidth));
            xIncrement = tileWidth;
        } else {
            countX = 1;
            xIncrement = horizontalSide == Side.RIGHT ? -tileWidth : tileWidth;
        }
        if (repeatY == BackgroundRepeat.REPEAT) {
            float offsetY = 0.0f;
            if (tileY != 0.0f) {
                float mod2 = tileY % tileHeight;
                tileY = mod2 == 0.0f ? 0.0f : tileY < 0.0f ? mod2 : mod2 - tileHeight;
                offsetY = tileY;
            }
            countY = (int) Math.max(1.0d, Math.ceil((regionHeight - offsetY) / tileHeight));
            yIncrement = verticalSide == Side.BOTTOM ? -tileHeight : tileHeight;
        } else if (repeatY == BackgroundRepeat.SPACE) {
            tileY = 0.0f;
            countY = (int) (regionHeight / tileHeight);
            float remainder2 = regionHeight % tileHeight;
            yIncrement = tileHeight + (remainder2 / (countY - 1));
        } else if (repeatY == BackgroundRepeat.ROUND) {
            tileY = 0.0f;
            countY = (int) (regionHeight / tileHeight);
            tileHeight = regionHeight / ((int) (regionHeight / tileHeight));
            yIncrement = tileHeight;
        } else {
            countY = 1;
            yIncrement = verticalSide == Side.BOTTOM ? -tileHeight : tileHeight;
        }
        Texture texture = g2.getResourceFactory().getCachedTexture(img, Texture.WrapMode.CLAMP_TO_EDGE);
        int srcX2 = srcX + srcW;
        int srcY2 = srcY + srcH;
        float regionX2 = regionX + regionWidth;
        float regionY2 = regionY + regionHeight;
        float dstY = regionY + tileY;
        for (int y2 = 0; y2 < countY; y2++) {
            float dstY2 = dstY + tileHeight;
            float dstX = regionX + tileX;
            for (int x2 = 0; x2 < countX; x2++) {
                float dstX2 = dstX + tileWidth;
                float dx1 = dstX < regionX ? regionX : dstX;
                float dy1 = dstY < regionY ? regionY : dstY;
                boolean skipRender = dx1 > regionX2 || dy1 > regionY2;
                float dx2 = dstX2 > regionX2 ? regionX2 : dstX2;
                float dy2 = dstY2 > regionY2 ? regionY2 : dstY2;
                if (dx2 < regionX || dy2 < regionY) {
                    skipRender = true;
                }
                if (!skipRender) {
                    float sx1 = dstX < regionX ? srcX + (srcW * ((-tileX) / tileWidth)) : srcX;
                    float sy1 = dstY < regionY ? srcY + (srcH * ((-tileY) / tileHeight)) : srcY;
                    float sx2 = dstX2 > regionX2 ? srcX2 - (srcW * ((dstX2 - regionX2) / tileWidth)) : srcX2;
                    float sy2 = dstY2 > regionY2 ? srcY2 - (srcH * ((dstY2 - regionY2) / tileHeight)) : srcY2;
                    g2.drawTexture(texture, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
                }
                dstX += xIncrement;
            }
            dstY += yIncrement;
        }
        texture.unlock();
    }

    final Border getBorder() {
        return this.border;
    }

    final Background getBackground() {
        return this.background;
    }

    final float getWidth() {
        return this.width;
    }

    final float getHeight() {
        return this.height;
    }
}
