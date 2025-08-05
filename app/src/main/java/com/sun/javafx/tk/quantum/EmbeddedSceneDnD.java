package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/EmbeddedSceneDnD.class */
final class EmbeddedSceneDnD {
    private final GlassSceneDnDEventHandler dndHandler;
    private HostDragStartListener dragStartListener;
    private EmbeddedSceneDSInterface fxDragSource;
    private EmbeddedSceneDTInterface fxDropTarget;
    private Thread hostThread;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EmbeddedSceneDnD.class.desiredAssertionStatus();
    }

    public EmbeddedSceneDnD(GlassScene scene) {
        this.dndHandler = new GlassSceneDnDEventHandler(scene);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDrag() {
        if (!$assertionsDisabled && !Platform.isFxApplicationThread()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.fxDragSource == null) {
            throw new AssertionError();
        }
        this.dragStartListener.dragStarted(this.fxDragSource, TransferMode.COPY);
    }

    private void setHostThread() {
        if (this.hostThread == null) {
            this.hostThread = Thread.currentThread();
        }
    }

    public boolean isHostThread() {
        return Thread.currentThread() == this.hostThread;
    }

    public void onDragSourceReleased(EmbeddedSceneDSInterface ds) {
        if (!$assertionsDisabled && this.fxDragSource != ds) {
            throw new AssertionError();
        }
        this.fxDragSource = null;
        Toolkit.getToolkit().exitNestedEventLoop(this, null);
    }

    public void onDropTargetReleased(EmbeddedSceneDTInterface dt) {
        if (!$assertionsDisabled && this.fxDropTarget != dt) {
            throw new AssertionError();
        }
        this.fxDropTarget = null;
    }

    <T> T executeOnFXThread(Callable<T> r2) {
        if (Platform.isFxApplicationThread()) {
            try {
                return r2.call();
            } catch (Exception e2) {
                return null;
            }
        }
        AtomicReference<T> result = new AtomicReference<>();
        CountDownLatch l2 = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                result.set(r2.call());
                l2.countDown();
            } catch (Exception e3) {
                l2.countDown();
            } catch (Throwable th) {
                l2.countDown();
                throw th;
            }
        });
        try {
            l2.await();
        } catch (Exception e3) {
        }
        return result.get();
    }

    public TKClipboard createDragboard(boolean isDragSource) {
        if (!$assertionsDisabled && !Platform.isFxApplicationThread()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.fxDragSource != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isDragSource) {
            throw new AssertionError();
        }
        ClipboardAssistance assistant = new ClipboardAssistance("DND-Embedded") { // from class: com.sun.javafx.tk.quantum.EmbeddedSceneDnD.1
            @Override // com.sun.glass.ui.ClipboardAssistance
            public void flush() {
                super.flush();
                EmbeddedSceneDnD.this.startDrag();
                Toolkit.getToolkit().enterNestedEventLoop(EmbeddedSceneDnD.this);
            }
        };
        this.fxDragSource = new EmbeddedSceneDS(this, assistant, this.dndHandler);
        return QuantumClipboard.getDragboardInstance(assistant, isDragSource);
    }

    public void setDragStartListener(HostDragStartListener l2) {
        setHostThread();
        this.dragStartListener = l2;
    }

    public EmbeddedSceneDTInterface createDropTarget() {
        setHostThread();
        return (EmbeddedSceneDTInterface) executeOnFXThread(() -> {
            if (!$assertionsDisabled && this.fxDropTarget != null) {
                throw new AssertionError();
            }
            this.fxDropTarget = new EmbeddedSceneDT(this, this.dndHandler);
            return this.fxDropTarget;
        });
    }
}
