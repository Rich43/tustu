package java.awt.dnd;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;

/* loaded from: rt.jar:java/awt/dnd/DropTargetDragEvent.class */
public class DropTargetDragEvent extends DropTargetEvent {
    private static final long serialVersionUID = -8422265619058953682L;
    private Point location;
    private int actions;
    private int dropAction;

    public DropTargetDragEvent(DropTargetContext dropTargetContext, Point point, int i2, int i3) {
        super(dropTargetContext);
        if (point == null) {
            throw new NullPointerException("cursorLocn");
        }
        if (i2 != 0 && i2 != 1 && i2 != 2 && i2 != 1073741824) {
            throw new IllegalArgumentException("dropAction" + i2);
        }
        if ((i3 & (-1073741828)) != 0) {
            throw new IllegalArgumentException("srcActions");
        }
        this.location = point;
        this.actions = i3;
        this.dropAction = i2;
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

    public void acceptDrag(int i2) {
        getDropTargetContext().acceptDrag(i2);
    }

    public void rejectDrag() {
        getDropTargetContext().rejectDrag();
    }
}
