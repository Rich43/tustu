package javafx.embed.swing;

import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.tk.Toolkit;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Set;
import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:javafx/embed/swing/SwingDragSource.class */
final class SwingDragSource extends CachingTransferable implements EmbeddedSceneDSInterface {
    private int sourceActions;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SwingDragSource.class.desiredAssertionStatus();
    }

    SwingDragSource() {
    }

    void updateContents(DropTargetDragEvent e2, boolean fetchData) {
        this.sourceActions = e2.getSourceActions();
        updateData(e2.getTransferable(), fetchData);
    }

    void updateContents(DropTargetDropEvent e2, boolean fetchData) {
        this.sourceActions = e2.getSourceActions();
        updateData(e2.getTransferable(), fetchData);
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
    public Set<TransferMode> getSupportedActions() {
        if ($assertionsDisabled || Toolkit.getToolkit().isFxUserThread()) {
            return SwingDnD.dropActionsToTransferModes(this.sourceActions);
        }
        throw new AssertionError();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
    public void dragDropEnd(TransferMode performedAction) {
        throw new UnsupportedOperationException();
    }
}
