package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import java.security.AccessController;
import javafx.application.Platform;
import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassSceneDnDEventHandler.class */
class GlassSceneDnDEventHandler {
    private final GlassScene scene;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GlassSceneDnDEventHandler.class.desiredAssertionStatus();
    }

    public GlassSceneDnDEventHandler(GlassScene scene) {
        this.scene = scene;
    }

    private double getPlatformScale() {
        Window w2;
        View view = this.scene.getPlatformView();
        if (view != null && (w2 = view.getWindow()) != null) {
            return w2.getPlatformScale();
        }
        return 1.0d;
    }

    public TransferMode handleDragEnter(int x2, int y2, int xAbs, int yAbs, TransferMode recommendedTransferMode, ClipboardAssistance dropTargetAssistant) {
        if ($assertionsDisabled || Platform.isFxApplicationThread()) {
            return (TransferMode) AccessController.doPrivileged(() -> {
                if (this.scene.dropTargetListener != null) {
                    double pScale = getPlatformScale();
                    QuantumClipboard dragboard = QuantumClipboard.getDragboardInstance(dropTargetAssistant, false);
                    return this.scene.dropTargetListener.dragEnter(x2 / pScale, y2 / pScale, xAbs / pScale, yAbs / pScale, recommendedTransferMode, dragboard);
                }
                return null;
            }, this.scene.getAccessControlContext());
        }
        throw new AssertionError();
    }

    public void handleDragLeave(ClipboardAssistance dropTargetAssistant) {
        if (!$assertionsDisabled && !Platform.isFxApplicationThread()) {
            throw new AssertionError();
        }
        AccessController.doPrivileged(() -> {
            if (this.scene.dropTargetListener != null) {
                this.scene.dropTargetListener.dragExit(0.0d, 0.0d, 0.0d, 0.0d);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public TransferMode handleDragDrop(int x2, int y2, int xAbs, int yAbs, TransferMode recommendedTransferMode, ClipboardAssistance dropTargetAssistant) {
        if ($assertionsDisabled || Platform.isFxApplicationThread()) {
            return (TransferMode) AccessController.doPrivileged(() -> {
                if (this.scene.dropTargetListener != null) {
                    double pScale = getPlatformScale();
                    return this.scene.dropTargetListener.drop(x2 / pScale, y2 / pScale, xAbs / pScale, yAbs / pScale, recommendedTransferMode);
                }
                return null;
            }, this.scene.getAccessControlContext());
        }
        throw new AssertionError();
    }

    public TransferMode handleDragOver(int x2, int y2, int xAbs, int yAbs, TransferMode recommendedTransferMode, ClipboardAssistance dropTargetAssistant) {
        if ($assertionsDisabled || Platform.isFxApplicationThread()) {
            return (TransferMode) AccessController.doPrivileged(() -> {
                if (this.scene.dropTargetListener != null) {
                    double pScale = getPlatformScale();
                    return this.scene.dropTargetListener.dragOver(x2 / pScale, y2 / pScale, xAbs / pScale, yAbs / pScale, recommendedTransferMode);
                }
                return null;
            }, this.scene.getAccessControlContext());
        }
        throw new AssertionError();
    }

    public void handleDragStart(int button, int x2, int y2, int xAbs, int yAbs, ClipboardAssistance dragSourceAssistant) {
        if (!$assertionsDisabled && !Platform.isFxApplicationThread()) {
            throw new AssertionError();
        }
        AccessController.doPrivileged(() -> {
            if (this.scene.dragGestureListener != null) {
                double pScale = getPlatformScale();
                QuantumClipboard dragboard = QuantumClipboard.getDragboardInstance(dragSourceAssistant, true);
                this.scene.dragGestureListener.dragGestureRecognized(x2 / pScale, y2 / pScale, xAbs / pScale, yAbs / pScale, button, dragboard);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public void handleDragEnd(TransferMode performedTransferMode, ClipboardAssistance dragSourceAssistant) {
        if (!$assertionsDisabled && !Platform.isFxApplicationThread()) {
            throw new AssertionError();
        }
        AccessController.doPrivileged(() -> {
            try {
                if (this.scene.dragSourceListener != null) {
                    this.scene.dragSourceListener.dragDropEnd(0.0d, 0.0d, 0.0d, 0.0d, performedTransferMode);
                }
                return null;
            } finally {
                QuantumClipboard.releaseCurrentDragboard();
            }
        }, this.scene.getAccessControlContext());
    }
}
