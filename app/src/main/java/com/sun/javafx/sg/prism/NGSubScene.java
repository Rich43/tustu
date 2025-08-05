package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGSubScene.class */
public class NGSubScene extends NGNode {
    private float slWidth;
    private float slHeight;
    private double lastScaledW;
    private double lastScaledH;
    private RTTexture rtt;
    private RTTexture resolveRTT;
    private NGNode root;
    private boolean renderSG;
    private final boolean depthBuffer;
    private final boolean msaa;
    private Paint fillPaint;
    private NGCamera camera;
    private NGLightBase[] lights;
    private boolean isOpaque;
    static final double THRESHOLD = 0.00390625d;

    public NGSubScene(boolean depthBuffer, boolean msaa) {
        this.resolveRTT = null;
        this.root = null;
        this.renderSG = true;
        this.isOpaque = false;
        this.depthBuffer = depthBuffer;
        this.msaa = msaa;
    }

    private NGSubScene() {
        this(false, false);
    }

    public void setRoot(NGNode root) {
        this.root = root;
    }

    public void setFillPaint(Object paint) {
        this.fillPaint = (Paint) paint;
    }

    public void setCamera(NGCamera camera) {
        this.camera = camera == null ? NGCamera.INSTANCE : camera;
    }

    public void setWidth(float width) {
        if (this.slWidth != width) {
            this.slWidth = width;
            geometryChanged();
            invalidateRTT();
        }
    }

    public void setHeight(float height) {
        if (this.slHeight != height) {
            this.slHeight = height;
            geometryChanged();
            invalidateRTT();
        }
    }

    public NGLightBase[] getLights() {
        return this.lights;
    }

    public void setLights(NGLightBase[] lights) {
        this.lights = lights;
    }

    public void markContentDirty() {
        visualsChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void visualsChanged() {
        this.renderSG = true;
        super.visualsChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void geometryChanged() {
        this.renderSG = true;
        super.geometryChanged();
    }

    private void invalidateRTT() {
        if (this.rtt != null) {
            this.rtt.dispose();
            this.rtt = null;
        }
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return false;
    }

    private void applyBackgroundFillPaint(Graphics g2) {
        this.isOpaque = true;
        if (this.fillPaint != null) {
            if (this.fillPaint instanceof Color) {
                Color fillColor = (Color) this.fillPaint;
                this.isOpaque = ((double) fillColor.getAlpha()) >= 1.0d;
                g2.clear(fillColor);
                return;
            } else {
                if (!this.fillPaint.isOpaque()) {
                    g2.clear();
                    this.isOpaque = false;
                }
                g2.setPaint(this.fillPaint);
                g2.fillRect(0.0f, 0.0f, this.rtt.getContentWidth(), this.rtt.getContentHeight());
                return;
            }
        }
        this.isOpaque = false;
        g2.clear();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void renderForcedContent(Graphics gOptional) {
        this.root.renderForcedContent(gOptional);
    }

    private static double hypot(double x2, double y2, double z2) {
        return Math.sqrt((x2 * x2) + (y2 * y2) + (z2 * z2));
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        if (this.slWidth <= 0.0d || this.slHeight <= 0.0d) {
            return;
        }
        BaseTransform txform = g2.getTransformNoClone();
        double scaleX = hypot(txform.getMxx(), txform.getMyx(), txform.getMzx());
        double scaleY = hypot(txform.getMxy(), txform.getMyy(), txform.getMzy());
        double scaledW = this.slWidth * scaleX;
        double scaledH = this.slHeight * scaleY;
        int rtWidth = (int) Math.ceil(scaledW - THRESHOLD);
        int rtHeight = (int) Math.ceil(scaledH - THRESHOLD);
        if (Math.max(Math.abs(scaledW - this.lastScaledW), Math.abs(scaledH - this.lastScaledH)) > THRESHOLD) {
            if (this.rtt != null && (rtWidth != this.rtt.getContentWidth() || rtHeight != this.rtt.getContentHeight())) {
                invalidateRTT();
            }
            this.renderSG = true;
            this.lastScaledW = scaledW;
            this.lastScaledH = scaledH;
        }
        if (this.rtt != null) {
            this.rtt.lock();
            if (this.rtt.isSurfaceLost()) {
                this.renderSG = true;
                this.rtt = null;
            }
        }
        if (this.renderSG || !this.root.isClean()) {
            if (this.rtt == null) {
                ResourceFactory factory = g2.getResourceFactory();
                this.rtt = factory.createRTTexture(rtWidth, rtHeight, Texture.WrapMode.CLAMP_TO_ZERO, this.msaa);
            }
            Graphics rttGraphics = this.rtt.createGraphics();
            rttGraphics.scale((float) scaleX, (float) scaleY);
            rttGraphics.setLights(this.lights);
            rttGraphics.setDepthBuffer(this.depthBuffer);
            if (this.camera != null) {
                rttGraphics.setCamera(this.camera);
            }
            applyBackgroundFillPaint(rttGraphics);
            this.root.render(rttGraphics);
            this.root.clearDirtyTree();
            this.renderSG = false;
        }
        if (this.msaa) {
            int x0 = this.rtt.getContentX();
            int y0 = this.rtt.getContentY();
            int x1 = x0 + rtWidth;
            int y1 = y0 + rtHeight;
            if ((this.isOpaque || g2.getCompositeMode() == CompositeMode.SRC) && isDirectBlitTransform(txform, scaleX, scaleY) && !g2.isDepthTest()) {
                int tx = (int) (txform.getMxt() + 0.5d);
                int ty = (int) (txform.getMyt() + 0.5d);
                RenderTarget target = g2.getRenderTarget();
                int dstX0 = target.getContentX() + tx;
                int dstY0 = target.getContentY() + ty;
                int dstX1 = dstX0 + rtWidth;
                int dstY1 = dstY0 + rtHeight;
                int dstW = target.getContentWidth();
                int dstH = target.getContentHeight();
                int dX = dstX1 > dstW ? dstW - dstX1 : 0;
                int dY = dstY1 > dstH ? dstH - dstY1 : 0;
                g2.blit(this.rtt, null, x0, y0, x1 + dX, y1 + dY, dstX0, dstY0, dstX1 + dX, dstY1 + dY);
            } else {
                if (this.resolveRTT != null && (this.resolveRTT.getContentWidth() < rtWidth || this.resolveRTT.getContentHeight() < rtHeight)) {
                    this.resolveRTT.dispose();
                    this.resolveRTT = null;
                }
                if (this.resolveRTT != null) {
                    this.resolveRTT.lock();
                    if (this.resolveRTT.isSurfaceLost()) {
                        this.resolveRTT = null;
                    }
                }
                if (this.resolveRTT == null) {
                    this.resolveRTT = g2.getResourceFactory().createRTTexture(rtWidth, rtHeight, Texture.WrapMode.CLAMP_TO_ZERO, false);
                }
                this.resolveRTT.createGraphics().blit(this.rtt, this.resolveRTT, x0, y0, x1, y1, x0, y0, x1, y1);
                g2.drawTexture(this.resolveRTT, 0.0f, 0.0f, (float) (rtWidth / scaleX), (float) (rtHeight / scaleY), 0.0f, 0.0f, rtWidth, rtHeight);
                this.resolveRTT.unlock();
            }
        } else {
            g2.drawTexture(this.rtt, 0.0f, 0.0f, (float) (rtWidth / scaleX), (float) (rtHeight / scaleY), 0.0f, 0.0f, rtWidth, rtHeight);
        }
        this.rtt.unlock();
    }

    private static boolean isDirectBlitTransform(BaseTransform tx, double sx, double sy) {
        return (sx == 1.0d && sy == 1.0d) ? tx.isTranslateOrIdentity() : tx.is2D() && tx.getMxx() == sx && tx.getMxy() == 0.0d && tx.getMyx() == 0.0d && tx.getMyy() == sy;
    }

    public NGCamera getCamera() {
        return this.camera;
    }
}
