package java.awt.dnd.peer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;

/* loaded from: rt.jar:java/awt/dnd/peer/DropTargetContextPeer.class */
public interface DropTargetContextPeer {
    void setTargetActions(int i2);

    int getTargetActions();

    DropTarget getDropTarget();

    DataFlavor[] getTransferDataFlavors();

    Transferable getTransferable() throws InvalidDnDOperationException;

    boolean isTransferableJVMLocal();

    void acceptDrag(int i2);

    void rejectDrag();

    void acceptDrop(int i2);

    void rejectDrop();

    void dropComplete(boolean z2);
}
