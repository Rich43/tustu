package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/EmbeddedSceneDT.class */
final class EmbeddedSceneDT implements EmbeddedSceneDTInterface {
    private final EmbeddedSceneDnD dnd;
    private final GlassSceneDnDEventHandler dndHandler;
    private EmbeddedSceneDSInterface dragSource;
    private ClipboardAssistance assistant;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EmbeddedSceneDT.class.desiredAssertionStatus();
    }

    public EmbeddedSceneDT(EmbeddedSceneDnD dnd, GlassSceneDnDEventHandler dndHandler) {
        this.dnd = dnd;
        this.dndHandler = dndHandler;
    }

    private void close() {
        this.dnd.onDropTargetReleased(this);
        this.assistant = null;
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDTInterface
    public TransferMode handleDragEnter(int x2, int y2, int xAbs, int yAbs, TransferMode recommendedDropAction, EmbeddedSceneDSInterface ds) {
        if ($assertionsDisabled || this.dnd.isHostThread()) {
            return (TransferMode) this.dnd.executeOnFXThread(() -> {
                if (!$assertionsDisabled && this.dragSource != null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this.assistant != null) {
                    throw new AssertionError();
                }
                this.dragSource = ds;
                this.assistant = new EmbeddedDTAssistant(this.dragSource);
                return this.dndHandler.handleDragEnter(x2, y2, xAbs, yAbs, recommendedDropAction, this.assistant);
            });
        }
        throw new AssertionError();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDTInterface
    public void handleDragLeave() {
        if (!$assertionsDisabled && !this.dnd.isHostThread()) {
            throw new AssertionError();
        }
        this.dnd.executeOnFXThread(() -> {
            if (!$assertionsDisabled && this.assistant == null) {
                throw new AssertionError();
            }
            try {
                this.dndHandler.handleDragLeave(this.assistant);
                return null;
            } finally {
                close();
            }
        });
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDTInterface
    public TransferMode handleDragDrop(int x2, int y2, int xAbs, int yAbs, TransferMode recommendedDropAction) {
        if ($assertionsDisabled || this.dnd.isHostThread()) {
            return (TransferMode) this.dnd.executeOnFXThread(() -> {
                if (!$assertionsDisabled && this.assistant == null) {
                    throw new AssertionError();
                }
                try {
                    TransferMode transferModeHandleDragDrop = this.dndHandler.handleDragDrop(x2, y2, xAbs, yAbs, recommendedDropAction, this.assistant);
                    close();
                    return transferModeHandleDragDrop;
                } catch (Throwable th) {
                    close();
                    throw th;
                }
            });
        }
        throw new AssertionError();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDTInterface
    public TransferMode handleDragOver(int x2, int y2, int xAbs, int yAbs, TransferMode recommendedDropAction) {
        if ($assertionsDisabled || this.dnd.isHostThread()) {
            return (TransferMode) this.dnd.executeOnFXThread(() -> {
                if ($assertionsDisabled || this.assistant != null) {
                    return this.dndHandler.handleDragOver(x2, y2, xAbs, yAbs, recommendedDropAction, this.assistant);
                }
                throw new AssertionError();
            });
        }
        throw new AssertionError();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/EmbeddedSceneDT$EmbeddedDTAssistant.class */
    private static class EmbeddedDTAssistant extends ClipboardAssistance {
        private EmbeddedSceneDSInterface dragSource;

        EmbeddedDTAssistant(EmbeddedSceneDSInterface source) {
            super("DND-Embedded");
            this.dragSource = source;
        }

        @Override // com.sun.glass.ui.ClipboardAssistance
        public void flush() {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.glass.ui.ClipboardAssistance
        public Object getData(String mimeType) {
            return this.dragSource.getData(mimeType);
        }

        @Override // com.sun.glass.ui.ClipboardAssistance
        public int getSupportedSourceActions() {
            return QuantumClipboard.transferModesToClipboardActions(this.dragSource.getSupportedActions());
        }

        @Override // com.sun.glass.ui.ClipboardAssistance
        public void setTargetAction(int actionDone) {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.glass.ui.ClipboardAssistance
        public String[] getMimeTypes() {
            return this.dragSource.getMimeTypes();
        }
    }
}
