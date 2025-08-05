package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.View;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKSceneListener;
import com.sun.javafx.tk.TKScenePaintListener;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.input.InputMethodRequests;
import javafx.stage.StageStyle;
import sun.misc.JavaSecurityAccess;
import sun.misc.SharedSecrets;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassScene.class */
abstract class GlassScene implements TKScene {
    private static final JavaSecurityAccess javaSecurityAccess;
    private GlassStage stage;
    protected TKSceneListener sceneListener;
    protected TKDragGestureListener dragGestureListener;
    protected TKDragSourceListener dragSourceListener;
    protected TKDropTargetListener dropTargetListener;
    protected InputMethodRequests inputMethodRequests;
    private TKScenePaintListener scenePaintListener;
    private NGNode root;
    private NGCamera camera;
    protected Paint fillPaint;
    private final boolean depthBuffer;
    private final boolean msaa;
    private NGLightBase[] lights;
    static final /* synthetic */ boolean $assertionsDisabled;
    private volatile boolean entireSceneDirty = true;
    private boolean doPresent = true;
    private final AtomicBoolean painting = new AtomicBoolean(false);
    private AccessControlContext accessCtrlCtx = null;
    SceneState sceneState = new SceneState(this);

    protected abstract boolean isSynchronous();

    static {
        $assertionsDisabled = !GlassScene.class.desiredAssertionStatus();
        javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
    }

    protected GlassScene(boolean depthBuffer, boolean msaa) {
        this.msaa = msaa;
        this.depthBuffer = depthBuffer;
    }

    @Override // com.sun.javafx.tk.TKScene
    public void dispose() {
        if (!$assertionsDisabled && this.stage != null) {
            throw new AssertionError();
        }
        this.root = null;
        this.camera = null;
        this.fillPaint = null;
        this.sceneListener = null;
        this.dragGestureListener = null;
        this.dragSourceListener = null;
        this.dropTargetListener = null;
        this.inputMethodRequests = null;
        this.scenePaintListener = null;
        this.sceneState = null;
    }

    @Override // com.sun.javafx.tk.TKScene
    public final AccessControlContext getAccessControlContext() {
        if (this.accessCtrlCtx == null) {
            throw new RuntimeException("Scene security context has not been set!");
        }
        return this.accessCtrlCtx;
    }

    public final void setSecurityContext(AccessControlContext ctx) {
        if (this.accessCtrlCtx != null) {
            throw new RuntimeException("Scene security context has been already set!");
        }
        AccessControlContext acc = AccessController.getContext();
        this.accessCtrlCtx = (AccessControlContext) javaSecurityAccess.doIntersectionPrivilege(() -> {
            return AccessController.getContext();
        }, acc, ctx);
    }

    @Override // com.sun.javafx.tk.TKScene
    public void waitForRenderingToComplete() {
        PaintCollector.getInstance().waitForRenderingToComplete();
    }

    @Override // com.sun.javafx.tk.TKScene
    public void waitForSynchronization() {
        ViewPainter.renderLock.lock();
    }

    @Override // com.sun.javafx.tk.TKScene
    public void releaseSynchronization(boolean updateState) {
        if (updateState) {
            updateSceneState();
        }
        ViewPainter.renderLock.unlock();
    }

    boolean getDepthBuffer() {
        return this.depthBuffer;
    }

    boolean isMSAA() {
        return this.msaa;
    }

    @Override // com.sun.javafx.tk.TKScene
    public void setTKSceneListener(TKSceneListener listener) {
        this.sceneListener = listener;
    }

    @Override // com.sun.javafx.tk.TKScene
    public synchronized void setTKScenePaintListener(TKScenePaintListener listener) {
        this.scenePaintListener = listener;
    }

    public void setTKDropTargetListener(TKDropTargetListener listener) {
        this.dropTargetListener = listener;
    }

    public void setTKDragSourceListener(TKDragSourceListener listener) {
        this.dragSourceListener = listener;
    }

    public void setTKDragGestureListener(TKDragGestureListener listener) {
        this.dragGestureListener = listener;
    }

    public void setInputMethodRequests(InputMethodRequests requests) {
        this.inputMethodRequests = requests;
    }

    @Override // com.sun.javafx.tk.TKScene
    public void setRoot(NGNode root) {
        this.root = root;
        entireSceneNeedsRepaint();
    }

    protected NGNode getRoot() {
        return this.root;
    }

    NGCamera getCamera() {
        return this.camera;
    }

    @Override // com.sun.javafx.tk.TKScene
    public NGLightBase[] getLights() {
        return this.lights;
    }

    @Override // com.sun.javafx.tk.TKScene
    public void setLights(NGLightBase[] lights) {
        this.lights = lights;
    }

    @Override // com.sun.javafx.tk.TKScene
    public void setCamera(NGCamera camera) {
        this.camera = camera == null ? NGCamera.INSTANCE : camera;
        entireSceneNeedsRepaint();
    }

    @Override // com.sun.javafx.tk.TKScene
    public void setFillPaint(Object fillPaint) {
        this.fillPaint = (Paint) fillPaint;
        entireSceneNeedsRepaint();
    }

    @Override // com.sun.javafx.tk.TKScene
    public void setCursor(Object cursor) {
    }

    @Override // com.sun.javafx.tk.TKScene
    public final void markDirty() {
        sceneChanged();
    }

    @Override // com.sun.javafx.tk.TKScene
    public void entireSceneNeedsRepaint() {
        if (Platform.isFxApplicationThread()) {
            this.entireSceneDirty = true;
            sceneChanged();
        } else {
            Platform.runLater(() -> {
                this.entireSceneDirty = true;
                sceneChanged();
            });
        }
    }

    public boolean isEntireSceneDirty() {
        return this.entireSceneDirty;
    }

    public void clearEntireSceneDirty() {
        this.entireSceneDirty = false;
    }

    @Override // com.sun.javafx.tk.TKScene
    public TKClipboard createDragboard(boolean isDragSource) {
        ClipboardAssistance assistant = new ClipboardAssistance(Clipboard.DND) { // from class: com.sun.javafx.tk.quantum.GlassScene.1
            @Override // com.sun.glass.ui.ClipboardAssistance
            public void actionPerformed(int performedAction) {
                super.actionPerformed(performedAction);
                AccessController.doPrivileged(() -> {
                    try {
                        if (GlassScene.this.dragSourceListener != null) {
                            GlassScene.this.dragSourceListener.dragDropEnd(0.0d, 0.0d, 0.0d, 0.0d, QuantumToolkit.clipboardActionToTransferMode(performedAction));
                        }
                        return null;
                    } finally {
                        QuantumClipboard.releaseCurrentDragboard();
                    }
                }, GlassScene.this.getAccessControlContext());
            }
        };
        return QuantumClipboard.getDragboardInstance(assistant, isDragSource);
    }

    protected final GlassStage getStage() {
        return this.stage;
    }

    void setStage(GlassStage stage) {
        this.stage = stage;
        sceneChanged();
    }

    final SceneState getSceneState() {
        return this.sceneState;
    }

    final void updateSceneState() {
        this.sceneState.update();
    }

    protected View getPlatformView() {
        return null;
    }

    boolean setPainting(boolean value) {
        return this.painting.getAndSet(value);
    }

    void repaint() {
    }

    final void stageVisible(boolean visible) {
        if (!visible && PrismSettings.forceRepaint) {
            PaintCollector.getInstance().removeDirtyScene(this);
        }
        if (visible) {
            PaintCollector.getInstance().addDirtyScene(this);
        }
    }

    public void sceneChanged() {
        if (this.stage != null) {
            PaintCollector.getInstance().addDirtyScene(this);
        } else {
            PaintCollector.getInstance().removeDirtyScene(this);
        }
    }

    public final synchronized void frameRendered() {
        if (this.scenePaintListener != null) {
            this.scenePaintListener.frameRendered();
        }
    }

    public final synchronized void setDoPresent(boolean value) {
        this.doPresent = value;
    }

    public final synchronized boolean getDoPresent() {
        return this.doPresent;
    }

    protected Color getClearColor() {
        WindowStage windowStage = this.stage instanceof WindowStage ? (WindowStage) this.stage : null;
        if (windowStage != null && windowStage.getPlatformWindow().isTransparentWindow()) {
            return Color.TRANSPARENT;
        }
        if (this.fillPaint == null) {
            return Color.WHITE;
        }
        if (this.fillPaint.isOpaque() || (windowStage != null && windowStage.getPlatformWindow().isUnifiedWindow())) {
            if (this.fillPaint.getType() == Paint.Type.COLOR) {
                return (Color) this.fillPaint;
            }
            if (this.depthBuffer) {
                return Color.TRANSPARENT;
            }
            return null;
        }
        return Color.WHITE;
    }

    final Paint getCurrentPaint() {
        WindowStage windowStage = this.stage instanceof WindowStage ? (WindowStage) this.stage : null;
        if (windowStage != null && windowStage.getStyle() == StageStyle.TRANSPARENT) {
            if (Color.TRANSPARENT.equals(this.fillPaint)) {
                return null;
            }
            return this.fillPaint;
        }
        if (this.fillPaint != null && this.fillPaint.isOpaque() && this.fillPaint.getType() == Paint.Type.COLOR) {
            return null;
        }
        return this.fillPaint;
    }

    public String toString() {
        return " scene: " + hashCode() + ")";
    }
}
