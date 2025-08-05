package com.sun.javafx.tk.quantum;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.Presentable;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/ViewPainter.class */
abstract class ViewPainter implements Runnable {
    private static NodePath[] ROOT_PATHS;
    protected static final ReentrantLock renderLock;
    protected int penWidth = -1;
    protected int penHeight = -1;
    protected int viewWidth;
    protected int viewHeight;
    protected final SceneState sceneState;
    protected Presentable presentable;
    protected ResourceFactory factory;
    protected boolean freshBackBuffer;
    private int width;
    private int height;
    private NGNode root;
    private NGNode overlayRoot;
    private Rectangle dirtyRect;
    private RectBounds clip;
    private RectBounds dirtyRegionTemp;
    private DirtyRegionPool dirtyRegionPool;
    private DirtyRegionContainer dirtyRegionContainer;
    private Affine3D tx;
    private Affine3D scaleTx;
    private GeneralTransform3D viewProjTx;
    private GeneralTransform3D projTx;
    private RTTexture sceneBuffer;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ViewPainter.class.desiredAssertionStatus();
        ROOT_PATHS = new NodePath[PrismSettings.dirtyRegionCount];
        renderLock = new ReentrantLock();
    }

    protected ViewPainter(GlassScene gs) {
        this.sceneState = gs.getSceneState();
        if (this.sceneState == null) {
            throw new NullPointerException("Scene state is null");
        }
        if (PrismSettings.dirtyOptsEnabled) {
            this.tx = new Affine3D();
            this.viewProjTx = new GeneralTransform3D();
            this.projTx = new GeneralTransform3D();
            this.scaleTx = new Affine3D();
            this.clip = new RectBounds();
            this.dirtyRect = new Rectangle();
            this.dirtyRegionTemp = new RectBounds();
            this.dirtyRegionPool = new DirtyRegionPool(PrismSettings.dirtyRegionCount);
            this.dirtyRegionContainer = this.dirtyRegionPool.checkOut();
        }
    }

    protected final void setRoot(NGNode node) {
        this.root = node;
    }

    protected final void setOverlayRoot(NGNode node) {
        this.overlayRoot = node;
    }

    private void adjustPerspective(NGCamera camera) {
        if (!$assertionsDisabled && !PrismSettings.dirtyOptsEnabled) {
            throw new AssertionError();
        }
        if (camera instanceof NGPerspectiveCamera) {
            this.scaleTx.setToScale(this.width / 2.0d, (-this.height) / 2.0d, 1.0d);
            this.scaleTx.translate(1.0d, -1.0d);
            this.projTx.mul(this.scaleTx);
            this.viewProjTx = camera.getProjViewTx(this.viewProjTx);
            this.projTx.mul(this.viewProjTx);
        }
    }

    protected void paintImpl(Graphics backBufferGraphics) {
        if (this.width <= 0 || this.height <= 0 || backBufferGraphics == null) {
            this.root.renderForcedContent(backBufferGraphics);
            return;
        }
        Graphics g2 = backBufferGraphics;
        float pixelScale = getPixelScaleFactor();
        g2.setPixelScaleFactor(pixelScale);
        boolean renderEverything = this.overlayRoot != null || this.freshBackBuffer || this.sceneState.getScene().isEntireSceneDirty() || this.sceneState.getScene().getDepthBuffer() || !PrismSettings.dirtyOptsEnabled;
        boolean showDirtyOpts = PrismSettings.showDirtyRegions || PrismSettings.showOverdraw;
        if (showDirtyOpts && !this.sceneState.getScene().getDepthBuffer()) {
            int bufferWidth = (int) Math.ceil(this.width * pixelScale);
            int bufferHeight = (int) Math.ceil(this.height * pixelScale);
            if (this.sceneBuffer != null) {
                this.sceneBuffer.lock();
                if (this.sceneBuffer.isSurfaceLost() || bufferWidth != this.sceneBuffer.getContentWidth() || bufferHeight != this.sceneBuffer.getContentHeight()) {
                    this.sceneBuffer.unlock();
                    this.sceneBuffer.dispose();
                    this.sceneBuffer = null;
                }
            }
            if (this.sceneBuffer == null) {
                this.sceneBuffer = g2.getResourceFactory().createRTTexture(bufferWidth, bufferHeight, Texture.WrapMode.CLAMP_TO_ZERO, false);
                renderEverything = true;
            }
            this.sceneBuffer.contentsUseful();
            g2 = this.sceneBuffer.createGraphics();
            g2.scale(pixelScale, pixelScale);
        } else if (this.sceneBuffer != null) {
            this.sceneBuffer.dispose();
            this.sceneBuffer = null;
        }
        int status = -1;
        if (!renderEverything) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newPhase("Dirty Opts Computed");
            }
            this.clip.setBounds(0.0f, 0.0f, this.width, this.height);
            this.dirtyRegionTemp.makeEmpty();
            this.dirtyRegionContainer.reset();
            this.tx.setToIdentity();
            this.projTx.setIdentity();
            adjustPerspective(this.sceneState.getCamera());
            status = this.root.accumulateDirtyRegions(this.clip, this.dirtyRegionTemp, this.dirtyRegionPool, this.dirtyRegionContainer, this.tx, this.projTx);
            this.dirtyRegionContainer.roundOut();
            if (status == 1) {
                this.root.doPreCulling(this.dirtyRegionContainer, this.tx, this.projTx);
            }
        }
        int dirtyRegionSize = status == 1 ? this.dirtyRegionContainer.size() : 0;
        if (dirtyRegionSize > 0) {
            g2.setHasPreCullingBits(true);
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newPhase("Render Roots Discovered");
            }
            for (int i2 = 0; i2 < dirtyRegionSize; i2++) {
                NodePath path = getRootPath(i2);
                path.clear();
                this.root.getRenderRoot(getRootPath(i2), this.dirtyRegionContainer.getDirtyRegion(i2), i2, this.tx, this.projTx);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.addMessage(dirtyRegionSize + " different dirty regions to render");
                for (int i3 = 0; i3 < dirtyRegionSize; i3++) {
                    PulseLogger.addMessage("Dirty Region " + i3 + ": " + ((Object) this.dirtyRegionContainer.getDirtyRegion(i3)));
                    PulseLogger.addMessage("Render Root Path " + i3 + ": " + ((Object) getRootPath(i3)));
                }
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED && PrismSettings.printRenderGraph) {
                StringBuilder s2 = new StringBuilder();
                List<NGNode> roots = new ArrayList<>();
                for (int i4 = 0; i4 < dirtyRegionSize; i4++) {
                    RectBounds dirtyRegion = this.dirtyRegionContainer.getDirtyRegion(i4);
                    if (dirtyRegion.getWidth() > 0.0f && dirtyRegion.getHeight() > 0.0f) {
                        NodePath nodePath = getRootPath(i4);
                        if (!nodePath.isEmpty()) {
                            roots.add(nodePath.last());
                        }
                    }
                }
                this.root.printDirtyOpts(s2, roots);
                PulseLogger.addMessage(s2.toString());
            }
            for (int i5 = 0; i5 < dirtyRegionSize; i5++) {
                RectBounds dirtyRegion2 = this.dirtyRegionContainer.getDirtyRegion(i5);
                if (dirtyRegion2.getWidth() > 0.0f && dirtyRegion2.getHeight() > 0.0f) {
                    this.dirtyRect.setBounds(dirtyRegion2);
                    if (pixelScale != 1.0f) {
                        this.dirtyRect.f11913x = (int) (r0.f11913x * pixelScale);
                        this.dirtyRect.f11914y = (int) (r0.f11914y * pixelScale);
                        this.dirtyRect.width = (int) (r0.width * pixelScale);
                        this.dirtyRect.height = (int) (r0.height * pixelScale);
                    }
                    g2.setClipRect(this.dirtyRect);
                    g2.setClipRectIndex(i5);
                    doPaint(g2, getRootPath(i5));
                    getRootPath(i5).clear();
                }
            }
        } else {
            g2.setHasPreCullingBits(false);
            g2.setClipRect(null);
            doPaint(g2, null);
        }
        this.root.renderForcedContent(g2);
        if (this.overlayRoot != null) {
            this.overlayRoot.render(g2);
        }
        if (showDirtyOpts) {
            if (this.sceneBuffer != null) {
                g2.sync();
                backBufferGraphics.clear();
                backBufferGraphics.drawTexture(this.sceneBuffer, 0.0f, 0.0f, this.width, this.height, this.sceneBuffer.getContentX(), this.sceneBuffer.getContentY(), this.sceneBuffer.getContentX() + this.sceneBuffer.getContentWidth(), this.sceneBuffer.getContentY() + this.sceneBuffer.getContentHeight());
                this.sceneBuffer.unlock();
            }
            if (PrismSettings.showOverdraw) {
                if (dirtyRegionSize > 0) {
                    for (int i6 = 0; i6 < dirtyRegionSize; i6++) {
                        Rectangle clip = new Rectangle(this.dirtyRegionContainer.getDirtyRegion(i6));
                        backBufferGraphics.setClipRectIndex(i6);
                        paintOverdraw(backBufferGraphics, clip);
                        backBufferGraphics.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.3f));
                        backBufferGraphics.drawRect(clip.f11913x, clip.f11914y, clip.width, clip.height);
                    }
                } else {
                    Rectangle clip2 = new Rectangle(0, 0, this.width, this.height);
                    if (!$assertionsDisabled && backBufferGraphics.getClipRectIndex() != 0) {
                        throw new AssertionError();
                    }
                    paintOverdraw(backBufferGraphics, clip2);
                    backBufferGraphics.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.3f));
                    backBufferGraphics.drawRect(clip2.f11913x, clip2.f11914y, clip2.width, clip2.height);
                }
            } else if (dirtyRegionSize > 0) {
                backBufferGraphics.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.3f));
                for (int i7 = 0; i7 < dirtyRegionSize; i7++) {
                    RectBounds reg = this.dirtyRegionContainer.getDirtyRegion(i7);
                    backBufferGraphics.fillRect(reg.getMinX(), reg.getMinY(), reg.getWidth(), reg.getHeight());
                }
            } else {
                backBufferGraphics.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.3f));
                backBufferGraphics.fillRect(0.0f, 0.0f, this.width, this.height);
            }
            this.root.clearPainted();
        }
    }

    private void paintOverdraw(Graphics g2, Rectangle clip) {
        int[] pixels = new int[clip.width * clip.height];
        this.root.drawDirtyOpts(BaseTransform.IDENTITY_TRANSFORM, this.projTx, clip, pixels, g2.getClipRectIndex());
        Image image = Image.fromIntArgbPreData(pixels, clip.width, clip.height);
        Texture texture = this.factory.getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
        g2.drawTexture(texture, clip.f11913x, clip.f11914y, clip.f11913x + clip.width, clip.f11914y + clip.height, 0.0f, 0.0f, clip.width, clip.height);
        texture.unlock();
    }

    private static NodePath getRootPath(int i2) {
        if (ROOT_PATHS[i2] == null) {
            ROOT_PATHS[i2] = new NodePath();
        }
        return ROOT_PATHS[i2];
    }

    protected void disposePresentable() {
        if (this.presentable instanceof GraphicsResource) {
            ((GraphicsResource) this.presentable).dispose();
        }
        this.presentable = null;
    }

    protected boolean validateStageGraphics() {
        if (!this.sceneState.isValid()) {
            return false;
        }
        int width = this.sceneState.getWidth();
        this.viewWidth = width;
        this.width = width;
        int height = this.sceneState.getHeight();
        this.viewHeight = height;
        this.height = height;
        return this.sceneState.isWindowVisible() && !this.sceneState.isWindowMinimized();
    }

    protected float getPixelScaleFactor() {
        if (this.presentable == null) {
            return 1.0f;
        }
        return this.presentable.getPixelScaleFactor();
    }

    private void doPaint(Graphics g2, NodePath renderRootPath) {
        if (renderRootPath != null) {
            if (renderRootPath.isEmpty()) {
                this.root.clearDirtyTree();
                return;
            } else if (!$assertionsDisabled && renderRootPath.getCurrentNode() != this.root) {
                throw new AssertionError();
            }
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newPhase("Painting");
        }
        GlassScene scene = this.sceneState.getScene();
        scene.clearEntireSceneDirty();
        g2.setLights(scene.getLights());
        g2.setDepthBuffer(scene.getDepthBuffer());
        Color clearColor = this.sceneState.getClearColor();
        if (clearColor != null) {
            g2.clear(clearColor);
        }
        Paint curPaint = this.sceneState.getCurrentPaint();
        if (curPaint != null) {
            if (curPaint.getType() != Paint.Type.COLOR) {
                g2.getRenderTarget().setOpaque(curPaint.isOpaque());
            }
            g2.setPaint(curPaint);
            g2.fillQuad(0.0f, 0.0f, this.width, this.height);
        }
        g2.setCamera(this.sceneState.getCamera());
        g2.setRenderRoot(renderRootPath);
        this.root.render(g2);
    }
}
