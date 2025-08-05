package com.sun.javafx.webkit.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.sg.prism.NGImageView;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPath;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NGText;
import com.sun.javafx.text.TextRun;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DropShadow;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCIcon;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCSize;
import com.sun.webkit.graphics.WCTransform;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCGraphicsPrismContext.class */
class WCGraphicsPrismContext extends WCGraphicsContext {
    private static final Logger log;
    private static final boolean DEBUG_DRAW_CLIP_SHAPE;
    Graphics baseGraphics;
    private BaseTransform baseTransform;
    private int fontSmoothingType;
    private static final BasicStroke focusRingStroke;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final List<ContextState> states = new ArrayList();
    private ContextState state = new ContextState();
    private Graphics cachedGraphics = null;
    private boolean isRootLayerValid = false;

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCGraphicsPrismContext$Type.class */
    public enum Type {
        PRIMARY,
        DEDICATED
    }

    static {
        $assertionsDisabled = !WCGraphicsPrismContext.class.desiredAssertionStatus();
        log = Logger.getLogger(WCGraphicsPrismContext.class.getName());
        DEBUG_DRAW_CLIP_SHAPE = Boolean.valueOf((String) AccessController.doPrivileged(() -> {
            return System.getProperty("com.sun.webkit.debugDrawClipShape", "false");
        })).booleanValue();
        focusRingStroke = new BasicStroke(1.1f, 0, 1, 0.0f, new float[]{1.0f}, 0.0f);
    }

    WCGraphicsPrismContext(Graphics g2) {
        this.state.setClip(g2.getClipRect());
        this.state.setAlpha(g2.getExtraAlpha());
        this.baseGraphics = g2;
        initBaseTransform(g2.getTransformNoClone());
    }

    WCGraphicsPrismContext() {
    }

    public Type type() {
        return Type.PRIMARY;
    }

    final void initBaseTransform(BaseTransform t2) {
        this.baseTransform = new Affine3D(t2);
        this.state.setTransform((Affine3D) this.baseTransform);
    }

    private void resetCachedGraphics() {
        this.cachedGraphics = null;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public Object getPlatformGraphics() {
        return getGraphics(false);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public boolean isValid() {
        Object platformGraphics = getPlatformGraphics();
        if (!(platformGraphics instanceof Graphics)) {
            return false;
        }
        Graphics g2 = (Graphics) platformGraphics;
        return !g2.getResourceFactory().isDisposed();
    }

    Graphics getGraphics(boolean checkClip) {
        if (this.cachedGraphics == null) {
            Layer l2 = this.state.getLayerNoClone();
            this.cachedGraphics = l2 != null ? l2.getGraphics() : this.baseGraphics;
            ResourceFactory rf = this.cachedGraphics.getResourceFactory();
            if (!rf.isDisposed()) {
                this.state.apply(this.cachedGraphics);
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("getPlatformGraphics for " + ((Object) this) + " : " + ((Object) this.cachedGraphics));
            }
        }
        Rectangle clip = this.cachedGraphics.getClipRectNoClone();
        if (checkClip && clip != null && clip.isEmpty()) {
            return null;
        }
        return this.cachedGraphics;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void saveState() {
        this.state.markAsRestorePoint();
        saveStateInternal();
    }

    private void saveStateInternal() {
        this.states.add(this.state);
        this.state = this.state.m1277clone();
    }

    private void startNewLayer(Layer layer) {
        saveStateInternal();
        Rectangle clip = this.state.getClipNoClone();
        Affine3D newTr = new Affine3D(BaseTransform.getTranslateInstance(-clip.f11913x, -clip.f11914y));
        newTr.concatenate(this.state.getTransformNoClone());
        clip.f11913x = 0;
        clip.f11914y = 0;
        Graphics g2 = getGraphics(true);
        if (g2 != null && g2 != this.baseGraphics) {
            layer.init(g2);
        }
        this.state.setTransform(newTr);
        this.state.setLayer(layer);
        resetCachedGraphics();
    }

    private void renderLayer(Layer layer) {
        WCTransform cur = getTransform();
        setTransform(new WCTransform(1.0d, 0.0d, 0.0d, 1.0d, layer.getX(), layer.getY()));
        Graphics g2 = getGraphics(true);
        if (g2 != null) {
            layer.render(g2);
        }
        setTransform(cur);
    }

    private void restoreStateInternal() {
        int size = this.states.size();
        if (size == 0) {
            if (!$assertionsDisabled) {
                throw new AssertionError((Object) "Unbalanced restoreState");
            }
            return;
        }
        Layer layer = this.state.getLayerNoClone();
        this.state = this.states.remove(size - 1);
        if (layer != this.state.getLayerNoClone()) {
            renderLayer(layer);
            layer.dispose();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Popped layer " + ((Object) layer));
                return;
            }
            return;
        }
        resetCachedGraphics();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void restoreState() {
        log.fine("restoring state");
        do {
            restoreStateInternal();
        } while (!this.state.isRestorePoint());
    }

    private void flushAllLayers() {
        if (this.state == null) {
            return;
        }
        if (this.isRootLayerValid) {
            log.fine("FlushAllLayers: root layer is valid, skipping");
            return;
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine("FlushAllLayers");
        }
        ContextState currentState = this.state;
        for (int i2 = this.states.size() - 1; i2 >= 0; i2--) {
            Layer layer = this.state.getLayerNoClone();
            this.state = this.states.get(i2);
            if (layer != this.state.getLayerNoClone()) {
                renderLayer(layer);
            } else {
                resetCachedGraphics();
            }
        }
        Layer layer2 = this.state.getLayerNoClone();
        if (layer2 != null) {
            renderLayer(layer2);
        }
        this.state = currentState;
        this.isRootLayerValid = true;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void dispose() {
        if (!this.states.isEmpty()) {
            log.fine("Unbalanced saveState/restoreState");
        }
        for (ContextState state : this.states) {
            if (state.getLayerNoClone() != null) {
                state.getLayerNoClone().dispose();
            }
        }
        this.states.clear();
        if (this.state != null && this.state.getLayerNoClone() != null) {
            this.state.getLayerNoClone().dispose();
        }
        this.state = null;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setClip(WCPath path, boolean isOut) {
        Affine3D tr = new Affine3D(this.state.getTransformNoClone());
        path.transform(tr.getMxx(), tr.getMyx(), tr.getMxy(), tr.getMyy(), tr.getMxt(), tr.getMyt());
        if (!isOut) {
            WCRectangle pathBounds = path.getBounds();
            int pixelX = (int) Math.floor(pathBounds.getX());
            int pixelY = (int) Math.floor(pathBounds.getY());
            int pixelW = ((int) Math.ceil(pathBounds.getMaxX())) - pixelX;
            int pixelH = ((int) Math.ceil(pathBounds.getMaxY())) - pixelY;
            this.state.clip(new Rectangle(pixelX, pixelY, pixelW, pixelH));
        }
        Rectangle clip = this.state.getClipNoClone();
        if (isOut) {
            path.addRect(clip.f11913x, clip.f11914y, clip.width, clip.height);
        }
        path.translate(-clip.f11913x, -clip.f11914y);
        Layer layer = new ClipLayer(getGraphics(false), clip, path, type() == Type.DEDICATED);
        startNewLayer(layer);
        if (log.isLoggable(Level.FINE)) {
            log.fine("setClip(WCPath " + path.getID() + ")");
            log.fine("Pushed layer " + ((Object) layer));
        }
    }

    private Rectangle transformClip(Rectangle localClip) {
        if (localClip == null) {
            return null;
        }
        float[] points = {localClip.f11913x, localClip.f11914y, localClip.f11913x + localClip.width, localClip.f11914y, localClip.f11913x, localClip.f11914y + localClip.height, localClip.f11913x + localClip.width, localClip.f11914y + localClip.height};
        this.state.getTransformNoClone().transform(points, 0, points, 0, 4);
        float minX = Math.min(points[0], Math.min(points[2], Math.min(points[4], points[6])));
        float maxX = Math.max(points[0], Math.max(points[2], Math.max(points[4], points[6])));
        float minY = Math.min(points[1], Math.min(points[3], Math.min(points[5], points[7])));
        float maxY = Math.max(points[1], Math.max(points[3], Math.max(points[5], points[7])));
        return new Rectangle(new RectBounds(minX, minY, maxX, maxY));
    }

    private void setClip(Rectangle shape) {
        Rectangle rc;
        Affine3D tr = this.state.getTransformNoClone();
        if (tr.getMxy() != 0.0d || tr.getMxz() != 0.0d || tr.getMyx() != 0.0d || tr.getMyz() != 0.0d || tr.getMzx() != 0.0d || tr.getMzy() != 0.0d) {
            WCPath path = new WCPathImpl();
            path.addRect(shape.f11913x, shape.f11914y, shape.width, shape.height);
            setClip(path, false);
            return;
        }
        this.state.clip(transformClip(shape));
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "setClip({0})", shape);
        }
        if (DEBUG_DRAW_CLIP_SHAPE && (rc = this.state.getClipNoClone()) != null && rc.width >= 2 && rc.height >= 2) {
            WCTransform cur = getTransform();
            setTransform(new WCTransform(1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d));
            Graphics g2d = getGraphics(true);
            if (g2d != null) {
                float fbase = (float) Math.random();
                g2d.setPaint(new Color(fbase, 1.0f - fbase, 0.5f, 0.1f));
                g2d.setStroke(new BasicStroke());
                g2d.fillRect(rc.f11913x, rc.f11914y, rc.width, rc.height);
                g2d.setPaint(new Color(1.0f - fbase, fbase, 0.5f, 1.0f));
                g2d.drawRect(rc.f11913x, rc.f11914y, rc.width, rc.height);
            }
            setTransform(cur);
            this.state.clip(new Rectangle(rc.f11913x + 1, rc.f11914y + 1, rc.width - 2, rc.height - 2));
        }
        if (this.cachedGraphics == null) {
            return;
        }
        this.cachedGraphics.setClipRect(this.state.getClipNoClone());
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setClip(int cx, int cy, int cw, int ch) {
        setClip(new Rectangle(cx, cy, cw, ch));
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setClip(WCRectangle c2) {
        setClip(new Rectangle((int) c2.getX(), (int) c2.getY(), (int) c2.getWidth(), (int) c2.getHeight()));
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCRectangle getClip() {
        Rectangle r2 = this.state.getClipNoClone();
        if (r2 == null) {
            return null;
        }
        return new WCRectangle(r2.f11913x, r2.f11914y, r2.width, r2.height);
    }

    protected Rectangle getClipRectNoClone() {
        return this.state.getClipNoClone();
    }

    protected Affine3D getTransformNoClone() {
        return this.state.getTransformNoClone();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void translate(float x2, float y2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "translate({0},{1})", new Object[]{Float.valueOf(x2), Float.valueOf(y2)});
        }
        this.state.translate(x2, y2);
        if (this.cachedGraphics != null) {
            this.cachedGraphics.translate(x2, y2);
        }
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void scale(float sx, float sy) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("scale(" + sx + " " + sy + ")");
        }
        this.state.scale(sx, sy);
        if (this.cachedGraphics != null) {
            this.cachedGraphics.scale(sx, sy);
        }
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void rotate(float radians) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("rotate(" + radians + ")");
        }
        this.state.rotate(radians);
        if (this.cachedGraphics == null) {
            return;
        }
        this.cachedGraphics.setTransform(this.state.getTransformNoClone());
    }

    protected boolean shouldRenderRect(float x2, float y2, float w2, float h2, DropShadow shadow, BasicStroke stroke) {
        return true;
    }

    protected boolean shouldRenderShape(Shape shape, DropShadow shadow, BasicStroke stroke) {
        return true;
    }

    protected boolean shouldCalculateIntersection() {
        return false;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void fillRect(final float x2, final float y2, final float w2, final float h2, final Color color) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("fillRect(%f, %f, %f, %f, %s)", Float.valueOf(x2), Float.valueOf(y2), Float.valueOf(w2), Float.valueOf(h2), color));
        }
        if (!shouldRenderRect(x2, y2, w2, h2, this.state.getShadowNoClone(), null)) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                Paint paint = color != null ? color : WCGraphicsPrismContext.this.state.getPaintNoClone();
                DropShadow shadow = WCGraphicsPrismContext.this.state.getShadowNoClone();
                if (shadow != null || !WCGraphicsPrismContext.this.state.getPerspectiveTransformNoClone().isIdentity()) {
                    NGRectangle node = new NGRectangle();
                    node.updateRectangle(x2, y2, w2, h2, 0.0f, 0.0f);
                    WCGraphicsPrismContext.this.render(g2, shadow, paint, null, node);
                } else {
                    g2.setPaint(paint);
                    g2.fillRect(x2, y2, w2, h2);
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void fillRoundedRect(final float x2, final float y2, final float w2, final float h2, final float topLeftW, final float topLeftH, final float topRightW, final float topRightH, final float bottomLeftW, final float bottomLeftH, final float bottomRightW, final float bottomRightH, final Color color) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("fillRoundedRect(%f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %s)", Float.valueOf(x2), Float.valueOf(y2), Float.valueOf(w2), Float.valueOf(h2), Float.valueOf(topLeftW), Float.valueOf(topLeftH), Float.valueOf(topRightW), Float.valueOf(topRightH), Float.valueOf(bottomLeftW), Float.valueOf(bottomLeftH), Float.valueOf(bottomRightW), Float.valueOf(bottomRightH), color));
        }
        if (!shouldRenderRect(x2, y2, w2, h2, this.state.getShadowNoClone(), null)) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                float arcW = (((topLeftW + topRightW) + bottomLeftW) + bottomRightW) / 2.0f;
                float arcH = (((topLeftH + topRightH) + bottomLeftH) + bottomRightH) / 2.0f;
                DropShadow shadow = WCGraphicsPrismContext.this.state.getShadowNoClone();
                if (shadow != null) {
                    NGRectangle node = new NGRectangle();
                    node.updateRectangle(x2, y2, w2, h2, arcW, arcH);
                    WCGraphicsPrismContext.this.render(g2, shadow, color, null, node);
                } else {
                    g2.setPaint(color);
                    g2.fillRoundRect(x2, y2, w2, h2, arcW, arcH);
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void clearRect(final float x2, final float y2, final float w2, final float h2) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("clearRect(%f, %f, %f, %f)", Float.valueOf(x2), Float.valueOf(y2), Float.valueOf(w2), Float.valueOf(h2)));
        }
        if (shouldCalculateIntersection()) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                g2.clearQuad(x2, y2, x2 + w2, y2 + h2);
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setFillColor(Color color) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, String.format("setFillColor(%s)", color));
        }
        this.state.setPaint(color);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setFillGradient(WCGradient gradient) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setFillGradient(" + ((Object) gradient) + ")");
        }
        this.state.setPaint((Gradient) gradient.getPlatformGradient());
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setTextMode(boolean fill, boolean stroke, boolean clip) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setTextMode(fill:" + fill + ",stroke:" + stroke + ",clip:" + clip + ")");
        }
        this.state.setTextMode(fill, stroke, clip);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setFontSmoothingType(int fontSmoothingType) {
        this.fontSmoothingType = fontSmoothingType;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public int getFontSmoothingType() {
        return this.fontSmoothingType;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setStrokeStyle(int style) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "setStrokeStyle({0})", Integer.valueOf(style));
        }
        this.state.getStrokeNoClone().setStyle(style);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setStrokeColor(Color color) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, String.format("setStrokeColor(%s)", color));
        }
        this.state.getStrokeNoClone().setPaint(color);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setStrokeWidth(float width) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "setStrokeWidth({0})", new Object[]{Float.valueOf(width)});
        }
        this.state.getStrokeNoClone().setThickness(width);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setStrokeGradient(WCGradient gradient) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setStrokeGradient(" + ((Object) gradient) + ")");
        }
        this.state.getStrokeNoClone().setPaint((Gradient) gradient.getPlatformGradient());
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setLineDash(float offset, float... sizes) {
        if (log.isLoggable(Level.FINE)) {
            StringBuilder s2 = new StringBuilder("[");
            for (float f2 : sizes) {
                s2.append(f2).append(',');
            }
            s2.append(']');
            log.log(Level.FINE, "setLineDash({0},{1}", new Object[]{Float.valueOf(offset), s2});
        }
        this.state.getStrokeNoClone().setDashOffset(offset);
        if (sizes != null) {
            boolean allZero = true;
            int i2 = 0;
            while (true) {
                if (i2 >= sizes.length) {
                    break;
                }
                if (sizes[i2] == 0.0f) {
                    i2++;
                } else {
                    allZero = false;
                    break;
                }
            }
            if (allZero) {
                sizes = null;
            }
        }
        this.state.getStrokeNoClone().setDashSizes(sizes);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setLineCap(int lineCap) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setLineCap(" + lineCap + ")");
        }
        this.state.getStrokeNoClone().setLineCap(lineCap);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setLineJoin(int lineJoin) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setLineJoin(" + lineJoin + ")");
        }
        this.state.getStrokeNoClone().setLineJoin(lineJoin);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setMiterLimit(float miterLimit) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setMiterLimit(" + miterLimit + ")");
        }
        this.state.getStrokeNoClone().setMiterLimit(miterLimit);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setShadow(float dx, float dy, float blur, Color color) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("setShadow(%f, %f, %f, %s)", Float.valueOf(dx), Float.valueOf(dy), Float.valueOf(blur), color));
        }
        this.state.setShadow(createShadow(dx, dy, blur, color));
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawPolygon(final WCPath path, boolean shouldAntialias) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawPolygon({0})", new Object[]{Boolean.valueOf(shouldAntialias)});
        }
        if (!shouldRenderShape(((WCPathImpl) path).getPlatformPath(), null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.4
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                Path2D p2d = (Path2D) path.getPlatformPath();
                g2.setPaint(WCGraphicsPrismContext.this.state.getPaintNoClone());
                g2.fill(p2d);
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(g2)) {
                    g2.draw(p2d);
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawLine(final int x0, final int y0, final int x1, final int y1) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawLine({0}, {1}, {2}, {3})", new Object[]{Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1)});
        }
        Line2D line = new Line2D(x0, y0, x1, y1);
        if (!shouldRenderShape(line, null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.5
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(g2)) {
                    g2.drawLine(x0, y0, x1, y1);
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawPattern(final WCImage texture, final WCRectangle srcRect, final WCTransform patternTransform, final WCPoint phase, final WCRectangle destRect) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawPattern({0}, {1}, {2}, {3})", new Object[]{Integer.valueOf(destRect.getIntX()), Integer.valueOf(destRect.getIntY()), Integer.valueOf(destRect.getIntWidth()), Integer.valueOf(destRect.getIntHeight())});
        }
        if (shouldRenderRect(destRect.getX(), destRect.getY(), destRect.getWidth(), destRect.getHeight(), null, null) && texture != null) {
            new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.6
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
                void doPaint(Graphics g2) {
                    Image img = ((PrismImage) texture).getImage();
                    if (!srcRect.contains(new WCRectangle(0.0f, 0.0f, texture.getWidth(), texture.getHeight()))) {
                        img = img.createSubImage(srcRect.getIntX(), srcRect.getIntY(), (int) Math.ceil(srcRect.getWidth()), (int) Math.ceil(srcRect.getHeight()));
                    }
                    double[] m2 = patternTransform.getMatrix();
                    Affine3D at2 = new Affine3D();
                    at2.translate(phase.getX(), phase.getY());
                    at2.concatenate(m2[0], m2[2], m2[4], m2[1], m2[3], m2[5]);
                    g2.setPaint(new ImagePattern(img, srcRect.getX(), srcRect.getY(), srcRect.getWidth(), srcRect.getHeight(), at2, false, false));
                    g2.fillRect(destRect.getX(), destRect.getY(), destRect.getWidth(), destRect.getHeight());
                }
            }.paint();
        }
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawImage(final WCImage img, final float dstx, final float dsty, final float dstw, final float dsth, final float srcx, final float srcy, final float srcw, final float srch) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawImage(img, dst({0},{1},{2},{3}), src({4},{5},{6},{7}))", new Object[]{Float.valueOf(dstx), Float.valueOf(dsty), Float.valueOf(dstw), Float.valueOf(dsth), Float.valueOf(srcx), Float.valueOf(srcy), Float.valueOf(srcw), Float.valueOf(srch)});
        }
        if (shouldRenderRect(dstx, dsty, dstw, dsth, this.state.getShadowNoClone(), null) && (img instanceof PrismImage)) {
            new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.7
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
                void doPaint(Graphics g2) {
                    PrismImage pi = (PrismImage) img;
                    DropShadow shadow = WCGraphicsPrismContext.this.state.getShadowNoClone();
                    if (shadow != null) {
                        NGImageView node = new NGImageView();
                        node.setImage(pi.getImage());
                        node.setX(dstx);
                        node.setY(dsty);
                        node.setViewport(srcx, srcy, srcw, srch, dstw, dsth);
                        node.setContentBounds(new RectBounds(dstx, dsty, dstx + dstw, dsty + dsth));
                        WCGraphicsPrismContext.this.render(g2, shadow, null, null, node);
                        return;
                    }
                    pi.draw(g2, (int) dstx, (int) dsty, (int) (dstx + dstw), (int) (dsty + dsth), (int) srcx, (int) srcy, (int) (srcx + srcw), (int) (srcy + srch));
                }
            }.paint();
        }
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawBitmapImage(final ByteBuffer image, final int x2, final int y2, final int w2, final int h2) {
        if (!shouldRenderRect(x2, y2, w2, h2, null, null)) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.8
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                ResourceFactory rf = g2.getResourceFactory();
                if (rf.isDisposed()) {
                    WCGraphicsPrismContext.log.fine("WCGraphicsPrismContext::doPaint skip because device has been disposed");
                    return;
                }
                image.order(ByteOrder.nativeOrder());
                Image img = Image.fromByteBgraPreData(image, w2, h2);
                Texture txt = rf.createTexture(img, Texture.Usage.STATIC, Texture.WrapMode.REPEAT);
                g2.drawTexture(txt, x2, y2, x2 + w2, y2 + h2, 0.0f, 0.0f, w2, h2);
                txt.dispose();
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawIcon(WCIcon icon, int x2, int y2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "UNIMPLEMENTED drawIcon ({0}, {1})", new Object[]{Integer.valueOf(x2), Integer.valueOf(y2)});
        }
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawRect(final int x2, final int y2, final int w2, final int h2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawRect({0}, {1}, {2}, {3})", new Object[]{Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(w2), Integer.valueOf(h2)});
        }
        if (!shouldRenderRect(x2, y2, w2, h2, null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.9
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                Paint c2 = WCGraphicsPrismContext.this.state.getPaintNoClone();
                if (c2 != null && c2.isOpaque()) {
                    g2.setPaint(c2);
                    g2.fillRect(x2, y2, w2, h2);
                }
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(g2)) {
                    g2.drawRect(x2, y2, w2, h2);
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawString(WCFont f2, int[] glyphs, float[] advances, final float x2, final float y2) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("Drawing %d glyphs @(%.1f, %.1f)", Integer.valueOf(glyphs.length), Float.valueOf(x2), Float.valueOf(y2)));
        }
        final PGFont font = (PGFont) f2.getPlatformFont();
        final TextRun gl = TextUtilities.createGlyphList(glyphs, advances, x2, y2);
        final DropShadow shadow = this.state.getShadowNoClone();
        final BasicStroke stroke = this.state.isTextStroke() ? this.state.getStrokeNoClone().getPlatformStroke() : null;
        final FontStrike strike = font.getStrike(getTransformNoClone(), getFontSmoothingType());
        if (shouldCalculateIntersection()) {
            Metrics m2 = strike.getMetrics();
            gl.setMetrics(m2.getAscent(), m2.getDescent(), m2.getLineGap());
            if (!shouldRenderRect(x2, y2, gl.getWidth(), gl.getHeight(), shadow, stroke)) {
                return;
            }
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.10
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                Paint paint;
                Paint paint2 = WCGraphicsPrismContext.this.state.isTextFill() ? WCGraphicsPrismContext.this.state.getPaintNoClone() : null;
                if (shadow != null) {
                    NGText span = new NGText();
                    span.setGlyphs(new GlyphList[]{gl});
                    span.setFont(font);
                    span.setFontSmoothingType(WCGraphicsPrismContext.this.fontSmoothingType);
                    WCGraphicsPrismContext.this.render(g2, shadow, paint2, stroke, span);
                    return;
                }
                if (paint2 != null) {
                    g2.setPaint(paint2);
                    g2.drawString(gl, strike, x2, y2, null, 0, 0);
                }
                if (stroke != null && (paint = WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint()) != null) {
                    g2.setPaint(paint);
                    g2.setStroke(stroke);
                    g2.draw(strike.getOutline(gl, BaseTransform.getTranslateInstance(x2, y2)));
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawString(WCFont f2, String str, boolean rtl, int from, int to, float x2, float y2) {
        float x3;
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("str='%s' (length=%d), from=%d, to=%d, rtl=%b, @(%.1f, %.1f)", str, Integer.valueOf(str.length()), Integer.valueOf(from), Integer.valueOf(to), Boolean.valueOf(rtl), Float.valueOf(x2), Float.valueOf(y2)));
        }
        TextLayout layout = TextUtilities.createLayout(str.substring(from, to), f2.getPlatformFont());
        int count = 0;
        GlyphList[] runs = layout.getRuns();
        for (GlyphList glyphList : runs) {
            count += glyphList.getGlyphCount();
        }
        int[] glyphs = new int[count];
        float[] adv = new float[count];
        int count2 = 0;
        for (GlyphList run : layout.getRuns()) {
            int gc = run.getGlyphCount();
            for (int i2 = 0; i2 < gc; i2++) {
                glyphs[count2] = run.getGlyphCode(i2);
                adv[count2] = run.getPosX(i2 + 1) - run.getPosX(i2);
                count2++;
            }
        }
        if (rtl) {
            x3 = x2 + (TextUtilities.getLayoutWidth(str.substring(from), f2.getPlatformFont()) - layout.getBounds().getWidth());
        } else {
            x3 = x2 + TextUtilities.getLayoutWidth(str.substring(0, from), f2.getPlatformFont());
        }
        drawString(f2, glyphs, adv, x3, y2);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setComposite(int composite) {
        log.log(Level.FINE, "setComposite({0})", Integer.valueOf(composite));
        this.state.setCompositeOperation(composite);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawEllipse(final int x2, final int y2, final int w2, final int h2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawEllipse({0}, {1}, {2}, {3})", new Object[]{Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(w2), Integer.valueOf(h2)});
        }
        if (!shouldRenderRect(x2, y2, w2, h2, null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.11
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                g2.setPaint(WCGraphicsPrismContext.this.state.getPaintNoClone());
                g2.fillEllipse(x2, y2, w2, h2);
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(g2)) {
                    g2.drawEllipse(x2, y2, w2, h2);
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawFocusRing(final int x2, final int y2, final int w2, final int h2, final Color color) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, String.format("drawFocusRing: %d, %d, %d, %d, %s", Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(w2), Integer.valueOf(h2), color));
        }
        if (!shouldRenderRect(x2, y2, w2, h2, null, focusRingStroke)) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.12
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                g2.setPaint(color);
                BasicStroke stroke = g2.getStroke();
                g2.setStroke(WCGraphicsPrismContext.focusRingStroke);
                g2.drawRoundRect(x2, y2, w2, h2, 4.0f, 4.0f);
                g2.setStroke(stroke);
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setAlpha(float alpha) {
        log.log(Level.FINE, "setAlpha({0})", Float.valueOf(alpha));
        this.state.setAlpha(alpha);
        if (null == this.cachedGraphics) {
            return;
        }
        this.cachedGraphics.setExtraAlpha(this.state.getAlpha());
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public float getAlpha() {
        return this.state.getAlpha();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void beginTransparencyLayer(float opacity) {
        TransparencyLayer layer = new TransparencyLayer(getGraphics(false), this.state.getClipNoClone(), opacity);
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("beginTransparencyLayer(%s)", layer));
        }
        this.state.markAsRestorePoint();
        startNewLayer(layer);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void endTransparencyLayer() {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("endTransparencyLayer(%s)", this.state.getLayerNoClone()));
        }
        restoreState();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawWidget(final RenderTheme theme, final Ref widget, final int x2, final int y2) {
        WCSize s2 = theme.getWidgetSize(widget);
        if (!shouldRenderRect(x2, y2, s2.getWidth(), s2.getHeight(), null, null)) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.13
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                theme.drawWidget(WCGraphicsPrismContext.this, widget, x2, y2);
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawScrollbar(final ScrollBarTheme theme, final Ref widget, final int x2, final int y2, final int pressedPart, final int hoveredPart) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("drawScrollbar(%s, %s, x = %d, y = %d)", theme, widget, Integer.valueOf(x2), Integer.valueOf(y2)));
        }
        WCSize s2 = theme.getWidgetSize(widget);
        if (!shouldRenderRect(x2, y2, s2.getWidth(), s2.getHeight(), null, null)) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.14
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                theme.paint(WCGraphicsPrismContext.this, widget, x2, y2, pressedPart, hoveredPart);
            }
        }.paint();
    }

    private static Rectangle intersect(Rectangle what, Rectangle with) {
        if (what == null) {
            return with;
        }
        RectBounds b2 = what.toRectBounds();
        b2.intersectWith(with);
        what.setBounds(b2);
        return what;
    }

    private static Color4f createColor4f(Color color) {
        return new Color4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    private DropShadow createShadow(float dx, float dy, float blur, Color color) {
        if (dx == 0.0f && dy == 0.0f && blur == 0.0f) {
            return null;
        }
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX((int) dx);
        shadow.setOffsetY((int) dy);
        shadow.setRadius(blur < 0.0f ? 0.0f : blur > 127.0f ? 127.0f : blur);
        shadow.setColor(createColor4f(color));
        return shadow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void render(Graphics g2, Effect effect, Paint paint, BasicStroke stroke, NGNode node) {
        if (node instanceof NGShape) {
            NGShape shape = (NGShape) node;
            Shape realShape = shape.getShape();
            Paint strokePaint = this.state.getStrokeNoClone().getPaint();
            if (stroke != null && strokePaint != null) {
                realShape = stroke.createStrokedShape(realShape);
                shape.setDrawStroke(stroke);
                shape.setDrawPaint(strokePaint);
                shape.setMode(paint == null ? NGShape.Mode.STROKE : NGShape.Mode.STROKE_FILL);
            } else {
                shape.setMode(paint == null ? NGShape.Mode.EMPTY : NGShape.Mode.FILL);
            }
            shape.setFillPaint(paint);
            shape.setContentBounds(realShape.getBounds());
        }
        boolean culling = g2.hasPreCullingBits();
        g2.setHasPreCullingBits(false);
        node.setEffect(effect);
        node.render(g2);
        g2.setHasPreCullingBits(culling);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCGraphicsPrismContext$ContextState.class */
    private static final class ContextState {
        private final WCStrokeImpl stroke;
        private Rectangle clip;
        private Paint paint;
        private float alpha;
        private boolean textFill;
        private boolean textStroke;
        private boolean textClip;
        private boolean restorePoint;
        private DropShadow shadow;
        private Affine3D xform;
        private GeneralTransform3D perspectiveTransform;
        private Layer layer;
        private int compositeOperation;

        private ContextState() {
            this.stroke = new WCStrokeImpl();
            this.textFill = true;
            this.textStroke = false;
            this.textClip = false;
            this.restorePoint = false;
            this.clip = null;
            this.paint = Color.BLACK;
            this.stroke.setPaint(Color.BLACK);
            this.alpha = 1.0f;
            this.xform = new Affine3D();
            this.perspectiveTransform = new GeneralTransform3D();
            this.compositeOperation = 2;
        }

        private ContextState(ContextState state) {
            this.stroke = new WCStrokeImpl();
            this.textFill = true;
            this.textStroke = false;
            this.textClip = false;
            this.restorePoint = false;
            this.stroke.copyFrom(state.getStrokeNoClone());
            setPaint(state.getPaintNoClone());
            this.clip = state.getClipNoClone();
            if (this.clip != null) {
                this.clip = new Rectangle(this.clip);
            }
            this.xform = new Affine3D(state.getTransformNoClone());
            this.perspectiveTransform = new GeneralTransform3D().set(state.getPerspectiveTransformNoClone());
            setShadow(state.getShadowNoClone());
            setLayer(state.getLayerNoClone());
            setAlpha(state.getAlpha());
            setTextMode(state.isTextFill(), state.isTextStroke(), state.isTextClip());
            setCompositeOperation(state.getCompositeOperation());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public ContextState m1277clone() {
            return new ContextState(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void apply(Graphics g2) {
            g2.setTransform(getTransformNoClone());
            g2.setPerspectiveTransform(getPerspectiveTransformNoClone());
            g2.setClipRect(getClipNoClone());
            g2.setExtraAlpha(getAlpha());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getCompositeOperation() {
            return this.compositeOperation;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCompositeOperation(int compositeOperation) {
            this.compositeOperation = compositeOperation;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public WCStrokeImpl getStrokeNoClone() {
            return this.stroke;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Paint getPaintNoClone() {
            return this.paint;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPaint(Paint paint) {
            this.paint = paint;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Rectangle getClipNoClone() {
            return this.clip;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Layer getLayerNoClone() {
            return this.layer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLayer(Layer layer) {
            this.layer = layer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setClip(Rectangle area) {
            this.clip = area;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clip(Rectangle area) {
            if (null == this.clip) {
                this.clip = area;
            } else {
                this.clip.intersectWith(area);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float getAlpha() {
            return this.alpha;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTextMode(boolean fill, boolean stroke, boolean clip) {
            this.textFill = fill;
            this.textStroke = stroke;
            this.textClip = clip;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isTextFill() {
            return this.textFill;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isTextStroke() {
            return this.textStroke;
        }

        private boolean isTextClip() {
            return this.textClip;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void markAsRestorePoint() {
            this.restorePoint = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRestorePoint() {
            return this.restorePoint;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setShadow(DropShadow shadow) {
            this.shadow = shadow;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public DropShadow getShadowNoClone() {
            return this.shadow;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Affine3D getTransformNoClone() {
            return this.xform;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public GeneralTransform3D getPerspectiveTransformNoClone() {
            return this.perspectiveTransform;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTransform(Affine3D at2) {
            this.xform.setTransform(at2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPerspectiveTransform(GeneralTransform3D gt) {
            this.perspectiveTransform.set(gt);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void concatTransform(Affine3D at2) {
            this.xform.concatenate(at2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void translate(double dx, double dy) {
            this.xform.translate(dx, dy);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void scale(double sx, double sy) {
            this.xform.scale(sx, sy);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void rotate(double radians) {
            this.xform.rotate(radians);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCGraphicsPrismContext$Layer.class */
    private static abstract class Layer {
        FilterContext fctx;
        PrDrawable buffer;
        Graphics graphics;
        final Rectangle bounds;
        boolean permanent;

        abstract void init(Graphics graphics);

        abstract void render(Graphics graphics);

        Layer(Graphics g2, Rectangle bounds, boolean permanent) {
            this.bounds = new Rectangle(bounds);
            this.permanent = permanent;
            int w2 = Math.max(bounds.width, 1);
            int h2 = Math.max(bounds.height, 1);
            this.fctx = WCGraphicsPrismContext.getFilterContext(g2);
            if (permanent) {
                ResourceFactory f2 = GraphicsPipeline.getDefaultResourceFactory();
                if (f2 == null || f2.isDisposed()) {
                    WCGraphicsPrismContext.log.fine("Layer :: cannot construct RTT because device disposed or not ready");
                    this.fctx = null;
                    this.buffer = null;
                    return;
                } else {
                    RTTexture rtt = f2.createRTTexture(w2, h2, Texture.WrapMode.CLAMP_NOT_NEEDED);
                    rtt.makePermanent();
                    this.buffer = ((PrRenderer) Renderer.getRenderer(this.fctx)).createDrawable(rtt);
                    return;
                }
            }
            this.buffer = (PrDrawable) Effect.getCompatibleImage(this.fctx, w2, h2);
        }

        Graphics getGraphics() {
            if (this.graphics == null && this.buffer != null) {
                this.graphics = this.buffer.createGraphics();
            }
            return this.graphics;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispose() {
            if (this.buffer != null) {
                if (this.permanent) {
                    this.buffer.flush();
                } else {
                    Effect.releaseCompatibleImage(this.fctx, this.buffer);
                }
                this.fctx = null;
                this.buffer = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getX() {
            return this.bounds.f11913x;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getY() {
            return this.bounds.f11914y;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCGraphicsPrismContext$TransparencyLayer.class */
    private final class TransparencyLayer extends Layer {
        private final float opacity;

        private TransparencyLayer(Graphics g2, Rectangle bounds, float opacity) {
            super(g2, bounds, false);
            this.opacity = opacity;
        }

        @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Layer
        void init(Graphics g2) {
            WCGraphicsPrismContext.this.state.setCompositeOperation(2);
        }

        @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Layer
        void render(Graphics g2) {
            new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.TransparencyLayer.1
                {
                    WCGraphicsPrismContext wCGraphicsPrismContext = WCGraphicsPrismContext.this;
                }

                @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
                void doPaint(Graphics g3) {
                    float op = g3.getExtraAlpha();
                    g3.setExtraAlpha(TransparencyLayer.this.opacity);
                    Affine3D tx = new Affine3D(g3.getTransformNoClone());
                    g3.setTransform(BaseTransform.IDENTITY_TRANSFORM);
                    g3.drawTexture(TransparencyLayer.this.buffer.getTextureObject(), TransparencyLayer.this.bounds.f11913x, TransparencyLayer.this.bounds.f11914y, TransparencyLayer.this.bounds.width, TransparencyLayer.this.bounds.height);
                    g3.setTransform(tx);
                    g3.setExtraAlpha(op);
                }
            }.paint(g2);
        }

        public String toString() {
            return String.format("TransparencyLayer[%d,%d + %dx%d, opacity %.2f]", Integer.valueOf(this.bounds.f11913x), Integer.valueOf(this.bounds.f11914y), Integer.valueOf(this.bounds.width), Integer.valueOf(this.bounds.height), Float.valueOf(this.opacity));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCGraphicsPrismContext$ClipLayer.class */
    private static final class ClipLayer extends Layer {
        private final WCPath normalizedToClipPath;
        private boolean srcover;

        private ClipLayer(Graphics g2, Rectangle bounds, WCPath normalizedToClipPath, boolean permanent) {
            super(g2, bounds, permanent);
            this.normalizedToClipPath = normalizedToClipPath;
            this.srcover = true;
        }

        @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Layer
        void init(Graphics g2) {
            RTTexture texture = null;
            ReadbackGraphics readbackGraphics = null;
            try {
                readbackGraphics = (ReadbackGraphics) g2;
                texture = readbackGraphics.readBack(this.bounds);
                getGraphics().drawTexture(texture, 0.0f, 0.0f, this.bounds.width, this.bounds.height);
                if (readbackGraphics != null && texture != null) {
                    readbackGraphics.releaseReadBackBuffer(texture);
                }
                this.srcover = false;
            } catch (Throwable th) {
                if (readbackGraphics != null && texture != null) {
                    readbackGraphics.releaseReadBackBuffer(texture);
                }
                throw th;
            }
        }

        @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Layer
        void render(Graphics g2) {
            Path2D p2d = ((WCPathImpl) this.normalizedToClipPath).getPlatformPath();
            PrDrawable bufferImg = (PrDrawable) Effect.getCompatibleImage(this.fctx, this.bounds.width, this.bounds.height);
            Graphics bufferGraphics = bufferImg.createGraphics();
            bufferGraphics.setPaint(Color.BLACK);
            bufferGraphics.fill(p2d);
            if ((g2 instanceof MaskTextureGraphics) && !(g2 instanceof PrinterGraphics)) {
                MaskTextureGraphics mg = (MaskTextureGraphics) g2;
                if (this.srcover) {
                    mg.drawPixelsMasked(this.buffer.getTextureObject(), bufferImg.getTextureObject(), this.bounds.f11913x, this.bounds.f11914y, this.bounds.width, this.bounds.height, 0, 0, 0, 0);
                } else {
                    mg.maskInterpolatePixels(this.buffer.getTextureObject(), bufferImg.getTextureObject(), this.bounds.f11913x, this.bounds.f11914y, this.bounds.width, this.bounds.height, 0, 0, 0, 0);
                }
            } else {
                Blend blend = new Blend(Blend.Mode.SRC_IN, new PassThrough(bufferImg, this.bounds.width, this.bounds.height), new PassThrough(this.buffer, this.bounds.width, this.bounds.height));
                Affine3D tx = new Affine3D(g2.getTransformNoClone());
                g2.setTransform(BaseTransform.IDENTITY_TRANSFORM);
                PrEffectHelper.render(blend, g2, this.bounds.f11913x, this.bounds.f11914y, null);
                g2.setTransform(tx);
            }
            Effect.releaseCompatibleImage(this.fctx, bufferImg);
        }

        public String toString() {
            return String.format("ClipLayer[%d,%d + %dx%d, path %s]", Integer.valueOf(this.bounds.f11913x), Integer.valueOf(this.bounds.f11914y), Integer.valueOf(this.bounds.width), Integer.valueOf(this.bounds.height), this.normalizedToClipPath);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCGraphicsPrismContext$Composite.class */
    private abstract class Composite {
        abstract void doPaint(Graphics graphics);

        private Composite() {
        }

        void paint() {
            paint(WCGraphicsPrismContext.this.getGraphics(true));
        }

        void paint(Graphics g2) {
            if (g2 != null) {
                CompositeMode oldCompositeMode = g2.getCompositeMode();
                switch (WCGraphicsPrismContext.this.state.getCompositeOperation()) {
                    case 1:
                        g2.setCompositeMode(CompositeMode.SRC);
                        doPaint(g2);
                        g2.setCompositeMode(oldCompositeMode);
                        break;
                    case 2:
                        g2.setCompositeMode(CompositeMode.SRC_OVER);
                        doPaint(g2);
                        g2.setCompositeMode(oldCompositeMode);
                        break;
                    default:
                        blend(g2);
                        break;
                }
                WCGraphicsPrismContext.this.isRootLayerValid = false;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x0083 A[Catch: all -> 0x0112, TryCatch #0 {all -> 0x0112, blocks: (B:5:0x002a, B:7:0x0032, B:9:0x009b, B:8:0x0083), top: B:35:0x002a }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void blend(com.sun.prism.Graphics r12) {
            /*
                Method dump skipped, instructions count: 322
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite.blend(com.sun.prism.Graphics):void");
        }

        private Effect createBlend(Blend.Mode mode, PrDrawable dstImg, PrDrawable srcImg, int width, int height) {
            return new Blend(mode, new PassThrough(dstImg, width, height), new PassThrough(srcImg, width, height));
        }

        private Effect createEffect(PrDrawable dstImg, PrDrawable srcImg, int width, int height) {
            switch (WCGraphicsPrismContext.this.state.getCompositeOperation()) {
                case 0:
                case 10:
                    return new Blend(Blend.Mode.SRC_OVER, createBlend(Blend.Mode.SRC_OUT, dstImg, srcImg, width, height), createBlend(Blend.Mode.SRC_OUT, srcImg, dstImg, width, height));
                case 1:
                case 2:
                case 11:
                default:
                    return createBlend(Blend.Mode.SRC_OVER, dstImg, srcImg, width, height);
                case 3:
                    return createBlend(Blend.Mode.SRC_IN, dstImg, srcImg, width, height);
                case 4:
                    return createBlend(Blend.Mode.SRC_OUT, dstImg, srcImg, width, height);
                case 5:
                    return createBlend(Blend.Mode.SRC_ATOP, dstImg, srcImg, width, height);
                case 6:
                    return createBlend(Blend.Mode.SRC_OVER, srcImg, dstImg, width, height);
                case 7:
                    return createBlend(Blend.Mode.SRC_IN, srcImg, dstImg, width, height);
                case 8:
                    return createBlend(Blend.Mode.SRC_OUT, srcImg, dstImg, width, height);
                case 9:
                    return createBlend(Blend.Mode.SRC_ATOP, srcImg, dstImg, width, height);
                case 12:
                    return createBlend(Blend.Mode.ADD, dstImg, srcImg, width, height);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCGraphicsPrismContext$PassThrough.class */
    private static final class PassThrough extends Effect {
        private final PrDrawable img;
        private final int width;
        private final int height;

        private PassThrough(PrDrawable img, int width, int height) {
            this.img = img;
            this.width = width;
            this.height = height;
        }

        @Override // com.sun.scenario.effect.Effect
        public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
            this.img.lock();
            ImageData imgData = new ImageData(fctx, this.img, new Rectangle((int) transform.getMxt(), (int) transform.getMyt(), this.width, this.height));
            imgData.setReusable(true);
            return imgData;
        }

        @Override // com.sun.scenario.effect.Effect
        public RectBounds getBounds(BaseTransform transform, Effect defaultInput) {
            return null;
        }

        @Override // com.sun.scenario.effect.Effect
        public Effect.AccelType getAccelType(FilterContext fctx) {
            return Effect.AccelType.INTRINSIC;
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

    /* JADX INFO: Access modifiers changed from: private */
    public static FilterContext getFilterContext(Graphics g2) {
        Screen screen = g2.getAssociatedScreen();
        if (screen == null) {
            ResourceFactory factory = g2.getResourceFactory();
            return PrFilterContext.getPrinterContext(factory);
        }
        return PrFilterContext.getInstance(screen);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void strokeArc(int x2, int y2, int w2, int h2, int startAngle, int angleSpan) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("strokeArc(%d, %d, %d, %d, %d, %d)", Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(w2), Integer.valueOf(h2), Integer.valueOf(startAngle), Integer.valueOf(angleSpan)));
        }
        final Arc2D arc = new Arc2D(x2, y2, w2, h2, startAngle, angleSpan, 0);
        if (this.state.getStrokeNoClone().isApplicable() && !shouldRenderShape(arc, null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.15
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(g2)) {
                    g2.draw(arc);
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCImage getImage() {
        return null;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void strokeRect(final float x2, final float y2, final float w2, final float h2, float lineWidth) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("strokeRect_FFFFF(%f, %f, %f, %f, %f)", Float.valueOf(x2), Float.valueOf(y2), Float.valueOf(w2), Float.valueOf(h2), Float.valueOf(lineWidth)));
        }
        final BasicStroke stroke = new BasicStroke(lineWidth, 0, 0, Math.max(1.0f, lineWidth), this.state.getStrokeNoClone().getDashSizes(), this.state.getStrokeNoClone().getDashOffset());
        if (!shouldRenderRect(x2, y2, w2, h2, null, stroke)) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.16
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                g2.setStroke(stroke);
                Paint paint = WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint();
                if (paint == null) {
                    paint = WCGraphicsPrismContext.this.state.getPaintNoClone();
                }
                g2.setPaint(paint);
                g2.drawRect(x2, y2, w2, h2);
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void strokePath(WCPath path) {
        log.fine("strokePath");
        if (path != null) {
            final BasicStroke stroke = this.state.getStrokeNoClone().getPlatformStroke();
            final DropShadow shadow = this.state.getShadowNoClone();
            final Path2D p2d = (Path2D) path.getPlatformPath();
            if ((stroke == null && shadow == null) || !shouldRenderShape(p2d, shadow, stroke)) {
                return;
            }
            new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.17
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
                void doPaint(Graphics g2) {
                    if (shadow != null) {
                        NGPath node = new NGPath();
                        node.updateWithPath2d(p2d);
                        WCGraphicsPrismContext.this.render(g2, shadow, null, stroke, node);
                    } else if (stroke != null) {
                        Paint paint = WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint();
                        if (paint == null) {
                            paint = WCGraphicsPrismContext.this.state.getPaintNoClone();
                        }
                        g2.setPaint(paint);
                        g2.setStroke(stroke);
                        g2.draw(p2d);
                    }
                }
            }.paint();
        }
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void fillPath(final WCPath path) {
        log.fine("fillPath");
        if (path == null || !shouldRenderShape(((WCPathImpl) path).getPlatformPath(), this.state.getShadowNoClone(), null)) {
            return;
        }
        new Composite() { // from class: com.sun.javafx.webkit.prism.WCGraphicsPrismContext.18
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.sun.javafx.webkit.prism.WCGraphicsPrismContext.Composite
            void doPaint(Graphics g2) {
                Path2D p2d = (Path2D) path.getPlatformPath();
                Paint paint = WCGraphicsPrismContext.this.state.getPaintNoClone();
                DropShadow shadow = WCGraphicsPrismContext.this.state.getShadowNoClone();
                if (shadow != null) {
                    NGPath node = new NGPath();
                    node.updateWithPath2d(p2d);
                    WCGraphicsPrismContext.this.render(g2, shadow, paint, null, node);
                } else {
                    g2.setPaint(paint);
                    g2.fill(p2d);
                }
            }
        }.paint();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setPerspectiveTransform(WCTransform tm) {
        GeneralTransform3D at2 = new GeneralTransform3D().set(tm.getMatrix());
        this.state.setPerspectiveTransform(at2);
        resetCachedGraphics();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setTransform(WCTransform tm) {
        double[] m2 = tm.getMatrix();
        Affine3D at2 = new Affine3D(new Affine2D(m2[0], m2[1], m2[2], m2[3], m2[4], m2[5]));
        if (this.state.getLayerNoClone() == null) {
            at2.preConcatenate(this.baseTransform);
        }
        this.state.setTransform(at2);
        resetCachedGraphics();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCTransform getTransform() {
        Affine3D xf = this.state.getTransformNoClone();
        return new WCTransform(xf.getMxx(), xf.getMyx(), xf.getMxy(), xf.getMyy(), xf.getMxt(), xf.getMyt());
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void concatTransform(WCTransform tm) {
        double[] m2 = tm.getMatrix();
        Affine3D at2 = new Affine3D(new Affine2D(m2[0], m2[1], m2[2], m2[3], m2[4], m2[5]));
        this.state.concatTransform(at2);
        resetCachedGraphics();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void flush() {
        if (!isValid()) {
            log.fine("WCGraphicsPrismContext::flush : GC is invalid");
        } else {
            flushAllLayers();
        }
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCGradient createLinearGradient(WCPoint p1, WCPoint p2) {
        return new WCLinearGradient(p1, p2);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCGradient createRadialGradient(WCPoint p1, float r1, WCPoint p2, float r2) {
        return new WCRadialGradient(p1, r1, p2, r2);
    }
}
