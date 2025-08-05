package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.GraphicsPipeline;
import java.nio.ByteOrder;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/ViewScene.class */
class ViewScene extends GlassScene {
    private static final String UNSUPPORTED_FORMAT = "Transparent windows only supported for BYTE_BGRA_PRE format on LITTLE_ENDIAN machines";
    private View platformView;
    private ViewPainter painter;
    private PaintRenderJob paintRenderJob;

    public ViewScene(boolean depthBuffer, boolean msaa) {
        super(depthBuffer, msaa);
        this.platformView = Application.GetApplication().createView();
        this.platformView.setEventHandler(new GlassViewEventHandler(this));
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    protected boolean isSynchronous() {
        return this.painter != null && (this.painter instanceof PresentingPainter);
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    protected View getPlatformView() {
        return this.platformView;
    }

    ViewPainter getPainter() {
        return this.painter;
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    public void setStage(GlassStage stage) {
        super.setStage(stage);
        if (stage != null) {
            WindowStage wstage = (WindowStage) stage;
            if (wstage.needsUpdateWindow() || GraphicsPipeline.getPipeline().isUploading()) {
                if (Pixels.getNativeFormat() != 1 || ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
                    throw new UnsupportedOperationException(UNSUPPORTED_FORMAT);
                }
                this.painter = new UploadingPainter(this);
            } else {
                this.painter = new PresentingPainter(this);
            }
            this.painter.setRoot(getRoot());
            this.paintRenderJob = new PaintRenderJob(this, PaintCollector.getInstance().getRendered(), this.painter);
        }
    }

    WindowStage getWindowStage() {
        return (WindowStage) getStage();
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene, com.sun.javafx.tk.TKScene
    public void dispose() {
        if (this.platformView != null) {
            QuantumToolkit.runWithRenderLock(() -> {
                this.platformView.close();
                this.platformView = null;
                updateSceneState();
                this.painter = null;
                this.paintRenderJob = null;
                return null;
            });
        }
        super.dispose();
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene, com.sun.javafx.tk.TKScene
    public void setRoot(NGNode root) {
        super.setRoot(root);
        if (this.painter != null) {
            this.painter.setRoot(root);
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene, com.sun.javafx.tk.TKScene
    public void setCursor(Object cursor) {
        super.setCursor(cursor);
        Application.invokeLater(() -> {
            Window window;
            CursorFrame cursorFrame = (CursorFrame) cursor;
            Cursor platformCursor = CursorUtils.getPlatformCursor(cursorFrame);
            if (this.platformView != null && (window = this.platformView.getWindow()) != null) {
                window.setCursor(platformCursor);
            }
        });
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    void repaint() {
        if (this.platformView != null && !setPainting(true)) {
            Toolkit tk = Toolkit.getToolkit();
            tk.addRenderJob(this.paintRenderJob);
        }
    }

    @Override // com.sun.javafx.tk.TKScene
    public void enableInputMethodEvents(boolean enable) {
        this.platformView.enableInputMethodEvents(enable);
    }

    @Override // com.sun.javafx.tk.TKScene
    public void finishInputMethodComposition() {
        this.platformView.finishInputMethodComposition();
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    public String toString() {
        View view = getPlatformView();
        return " scene: " + hashCode() + " @ (" + view.getWidth() + "," + view.getHeight() + ")";
    }

    void synchroniseOverlayWarning() {
        try {
            waitForSynchronization();
            OverlayWarning warning = getWindowStage().getWarning();
            if (warning == null) {
                this.painter.setOverlayRoot(null);
            } else {
                this.painter.setOverlayRoot(warning.impl_getPeer());
                warning.updateBounds();
                warning.impl_updatePeer();
            }
        } finally {
            releaseSynchronization(true);
            entireSceneNeedsRepaint();
        }
    }
}
