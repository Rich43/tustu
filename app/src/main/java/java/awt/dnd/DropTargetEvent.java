package java.awt.dnd;

import java.util.EventObject;

/* loaded from: rt.jar:java/awt/dnd/DropTargetEvent.class */
public class DropTargetEvent extends EventObject {
    private static final long serialVersionUID = 2821229066521922993L;
    protected DropTargetContext context;

    public DropTargetEvent(DropTargetContext dropTargetContext) {
        super(dropTargetContext.getDropTarget());
        this.context = dropTargetContext;
    }

    public DropTargetContext getDropTargetContext() {
        return this.context;
    }
}
