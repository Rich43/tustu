package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import java.util.Arrays;
import java.util.Set;
import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/EmbeddedSceneDS.class */
final class EmbeddedSceneDS implements EmbeddedSceneDSInterface {
    private final EmbeddedSceneDnD dnd;
    private final ClipboardAssistance assistant;
    private final GlassSceneDnDEventHandler dndHandler;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EmbeddedSceneDS.class.desiredAssertionStatus();
    }

    public EmbeddedSceneDS(EmbeddedSceneDnD dnd, ClipboardAssistance assistant, GlassSceneDnDEventHandler dndHandler) {
        this.dnd = dnd;
        this.assistant = assistant;
        this.dndHandler = dndHandler;
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
    public Set<TransferMode> getSupportedActions() {
        if ($assertionsDisabled || this.dnd.isHostThread()) {
            return (Set) this.dnd.executeOnFXThread(() -> {
                return QuantumClipboard.clipboardActionsToTransferModes(this.assistant.getSupportedSourceActions());
            });
        }
        throw new AssertionError();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
    public Object getData(String mimeType) {
        if ($assertionsDisabled || this.dnd.isHostThread()) {
            return this.dnd.executeOnFXThread(() -> {
                return this.assistant.getData(mimeType);
            });
        }
        throw new AssertionError();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
    public String[] getMimeTypes() {
        if ($assertionsDisabled || this.dnd.isHostThread()) {
            return (String[]) this.dnd.executeOnFXThread(() -> {
                return this.assistant.getMimeTypes();
            });
        }
        throw new AssertionError();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
    public boolean isMimeTypeAvailable(String mimeType) {
        if ($assertionsDisabled || this.dnd.isHostThread()) {
            return ((Boolean) this.dnd.executeOnFXThread(() -> {
                return Boolean.valueOf(Arrays.asList(this.assistant.getMimeTypes()).contains(mimeType));
            })).booleanValue();
        }
        throw new AssertionError();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
    public void dragDropEnd(TransferMode performedAction) {
        if (!$assertionsDisabled && !this.dnd.isHostThread()) {
            throw new AssertionError();
        }
        this.dnd.executeOnFXThread(() -> {
            try {
                this.dndHandler.handleDragEnd(performedAction, this.assistant);
                return null;
            } finally {
                this.dnd.onDragSourceReleased(this);
            }
        });
    }
}
