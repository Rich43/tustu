package java.awt.dnd;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.peer.DropTargetContextPeer;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/* loaded from: rt.jar:java/awt/dnd/DropTargetContext.class */
public class DropTargetContext implements Serializable {
    private static final long serialVersionUID = -634158968993743371L;
    private DropTarget dropTarget;
    private transient DropTargetContextPeer dropTargetContextPeer;
    private transient Transferable transferable;

    DropTargetContext(DropTarget dropTarget) {
        this.dropTarget = dropTarget;
    }

    public DropTarget getDropTarget() {
        return this.dropTarget;
    }

    public Component getComponent() {
        return this.dropTarget.getComponent();
    }

    public void addNotify(DropTargetContextPeer dropTargetContextPeer) {
        this.dropTargetContextPeer = dropTargetContextPeer;
    }

    public void removeNotify() {
        this.dropTargetContextPeer = null;
        this.transferable = null;
    }

    protected void setTargetActions(int i2) {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        if (dropTargetContextPeer != null) {
            synchronized (dropTargetContextPeer) {
                dropTargetContextPeer.setTargetActions(i2);
                getDropTarget().doSetDefaultActions(i2);
            }
            return;
        }
        getDropTarget().doSetDefaultActions(i2);
    }

    protected int getTargetActions() {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        if (dropTargetContextPeer != null) {
            return dropTargetContextPeer.getTargetActions();
        }
        return this.dropTarget.getDefaultActions();
    }

    public void dropComplete(boolean z2) throws InvalidDnDOperationException {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        if (dropTargetContextPeer != null) {
            dropTargetContextPeer.dropComplete(z2);
        }
    }

    protected void acceptDrag(int i2) {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        if (dropTargetContextPeer != null) {
            dropTargetContextPeer.acceptDrag(i2);
        }
    }

    protected void rejectDrag() {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        if (dropTargetContextPeer != null) {
            dropTargetContextPeer.rejectDrag();
        }
    }

    protected void acceptDrop(int i2) {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        if (dropTargetContextPeer != null) {
            dropTargetContextPeer.acceptDrop(i2);
        }
    }

    protected void rejectDrop() {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        if (dropTargetContextPeer != null) {
            dropTargetContextPeer.rejectDrop();
        }
    }

    protected DataFlavor[] getCurrentDataFlavors() {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        return dropTargetContextPeer != null ? dropTargetContextPeer.getTransferDataFlavors() : new DataFlavor[0];
    }

    protected List<DataFlavor> getCurrentDataFlavorsAsList() {
        return Arrays.asList(getCurrentDataFlavors());
    }

    protected boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return getCurrentDataFlavorsAsList().contains(dataFlavor);
    }

    protected Transferable getTransferable() throws InvalidDnDOperationException {
        DropTargetContextPeer dropTargetContextPeer = getDropTargetContextPeer();
        if (dropTargetContextPeer == null) {
            throw new InvalidDnDOperationException();
        }
        if (this.transferable == null) {
            Transferable transferable = dropTargetContextPeer.getTransferable();
            boolean zIsTransferableJVMLocal = dropTargetContextPeer.isTransferableJVMLocal();
            synchronized (this) {
                if (this.transferable == null) {
                    this.transferable = createTransferableProxy(transferable, zIsTransferableJVMLocal);
                }
            }
        }
        return this.transferable;
    }

    DropTargetContextPeer getDropTargetContextPeer() {
        return this.dropTargetContextPeer;
    }

    protected Transferable createTransferableProxy(Transferable transferable, boolean z2) {
        return new TransferableProxy(transferable, z2);
    }

    /* loaded from: rt.jar:java/awt/dnd/DropTargetContext$TransferableProxy.class */
    protected class TransferableProxy implements Transferable {
        protected Transferable transferable;
        protected boolean isLocal;
        private sun.awt.datatransfer.TransferableProxy proxy;

        TransferableProxy(Transferable transferable, boolean z2) {
            this.proxy = new sun.awt.datatransfer.TransferableProxy(transferable, z2);
            this.transferable = transferable;
            this.isLocal = z2;
        }

        @Override // java.awt.datatransfer.Transferable
        public DataFlavor[] getTransferDataFlavors() {
            return this.proxy.getTransferDataFlavors();
        }

        @Override // java.awt.datatransfer.Transferable
        public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
            return this.proxy.isDataFlavorSupported(dataFlavor);
        }

        @Override // java.awt.datatransfer.Transferable
        public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
            return this.proxy.getTransferData(dataFlavor);
        }
    }
}
