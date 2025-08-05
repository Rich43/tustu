package java.awt.dnd;

/* loaded from: rt.jar:java/awt/dnd/DragSourceDropEvent.class */
public class DragSourceDropEvent extends DragSourceEvent {
    private static final long serialVersionUID = -5571321229470821891L;
    private boolean dropSuccess;
    private int dropAction;

    public DragSourceDropEvent(DragSourceContext dragSourceContext, int i2, boolean z2) {
        super(dragSourceContext);
        this.dropAction = 0;
        this.dropSuccess = z2;
        this.dropAction = i2;
    }

    public DragSourceDropEvent(DragSourceContext dragSourceContext, int i2, boolean z2, int i3, int i4) {
        super(dragSourceContext, i3, i4);
        this.dropAction = 0;
        this.dropSuccess = z2;
        this.dropAction = i2;
    }

    public DragSourceDropEvent(DragSourceContext dragSourceContext) {
        super(dragSourceContext);
        this.dropAction = 0;
        this.dropSuccess = false;
    }

    public boolean getDropSuccess() {
        return this.dropSuccess;
    }

    public int getDropAction() {
        return this.dropAction;
    }
}
