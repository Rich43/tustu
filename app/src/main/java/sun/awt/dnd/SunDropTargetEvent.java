package sun.awt.dnd;

import java.awt.Component;
import java.awt.event.MouseEvent;
import sun.awt.dnd.SunDropTargetContextPeer;

/* loaded from: rt.jar:sun/awt/dnd/SunDropTargetEvent.class */
public class SunDropTargetEvent extends MouseEvent {
    public static final int MOUSE_DROPPED = 502;
    private final SunDropTargetContextPeer.EventDispatcher dispatcher;

    public SunDropTargetEvent(Component component, int i2, int i3, int i4, SunDropTargetContextPeer.EventDispatcher eventDispatcher) {
        super(component, i2, System.currentTimeMillis(), 0, i3, i4, 0, 0, 0, false, 0);
        this.dispatcher = eventDispatcher;
        this.dispatcher.registerEvent(this);
    }

    public void dispatch() {
        try {
            this.dispatcher.dispatchEvent(this);
        } finally {
            this.dispatcher.unregisterEvent(this);
        }
    }

    @Override // java.awt.event.InputEvent, java.awt.AWTEvent
    public void consume() {
        boolean zIsConsumed = isConsumed();
        super.consume();
        if (!zIsConsumed && isConsumed()) {
            this.dispatcher.unregisterEvent(this);
        }
    }

    public SunDropTargetContextPeer.EventDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override // java.awt.event.MouseEvent, java.awt.event.ComponentEvent, java.awt.AWTEvent
    public String paramString() {
        switch (this.id) {
            case 502:
                return "MOUSE_DROPPED,(" + getX() + "," + getY() + ")";
            default:
                return super.paramString();
        }
    }
}
