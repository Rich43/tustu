package java.awt.dnd;

import java.awt.Point;
import java.util.EventObject;

/* loaded from: rt.jar:java/awt/dnd/DragSourceEvent.class */
public class DragSourceEvent extends EventObject {
    private static final long serialVersionUID = -763287114604032641L;
    private final boolean locationSpecified;

    /* renamed from: x, reason: collision with root package name */
    private final int f12374x;

    /* renamed from: y, reason: collision with root package name */
    private final int f12375y;

    public DragSourceEvent(DragSourceContext dragSourceContext) {
        super(dragSourceContext);
        this.locationSpecified = false;
        this.f12374x = 0;
        this.f12375y = 0;
    }

    public DragSourceEvent(DragSourceContext dragSourceContext, int i2, int i3) {
        super(dragSourceContext);
        this.locationSpecified = true;
        this.f12374x = i2;
        this.f12375y = i3;
    }

    public DragSourceContext getDragSourceContext() {
        return (DragSourceContext) getSource();
    }

    public Point getLocation() {
        if (this.locationSpecified) {
            return new Point(this.f12374x, this.f12375y);
        }
        return null;
    }

    public int getX() {
        return this.f12374x;
    }

    public int getY() {
        return this.f12375y;
    }
}
