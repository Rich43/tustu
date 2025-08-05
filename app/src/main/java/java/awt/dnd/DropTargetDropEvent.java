package java.awt.dnd;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;

/* loaded from: rt.jar:java/awt/dnd/DropTargetDropEvent.class */
public class DropTargetDropEvent extends DropTargetEvent {
    private static final long serialVersionUID = -1721911170440459322L;
    private static final Point zero = new Point(0, 0);
    private Point location;
    private int actions;
    private int dropAction;
    private boolean isLocalTx;

    public DropTargetDropEvent(DropTargetContext dropTargetContext, Point point, int i2, int i3) {
        super(dropTargetContext);
        this.location = zero;
        this.actions = 0;
        this.dropAction = 0;
        this.isLocalTx = false;
        if (point == null) {
            throw new NullPointerException("cursorLocn");
        }
        if (i2 != 0 && i2 != 1 && i2 != 2 && i2 != 1073741824) {
            throw new IllegalArgumentException("dropAction = " + i2);
        }
        if ((i3 & (-1073741828)) != 0) {
            throw new IllegalArgumentException("srcActions");
        }
        this.location = point;
        this.actions = i3;
        this.dropAction = i2;
    }

    public DropTargetDropEvent(DropTargetContext dropTargetContext, Point point, int i2, int i3, boolean z2) {
        this(dropTargetContext, point, i2, i3);
        this.isLocalTx = z2;
    }

    public Point getLocation() {
        return this.location;
    }

    public DataFlavor[] getCurrentDataFlavors() {
        return getDropTargetContext().getCurrentDataFlavors();
    }

    public List<DataFlavor> getCurrentDataFlavorsAsList() {
        return getDropTargetContext().getCurrentDataFlavorsAsList();
    }

    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return getDropTargetContext().isDataFlavorSupported(dataFlavor);
    }

    public int getSourceActions() {
        return this.actions;
    }

    public int getDropAction() {
        return this.dropAction;
    }

    public Transferable getTransferable() {
        return getDropTargetContext().getTransferable();
    }

    public void acceptDrop(int i2) {
        getDropTargetContext().acceptDrop(i2);
    }

    public void rejectDrop() {
        getDropTargetContext().rejectDrop();
    }

    public void dropComplete(boolean z2) throws InvalidDnDOperationException {
        getDropTargetContext().dropComplete(z2);
    }

    public boolean isLocalTransfer() {
        return this.isLocalTx;
    }
}
