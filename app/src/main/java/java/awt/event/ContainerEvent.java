package java.awt.event;

import java.awt.Component;
import java.awt.Container;

/* loaded from: rt.jar:java/awt/event/ContainerEvent.class */
public class ContainerEvent extends ComponentEvent {
    public static final int CONTAINER_FIRST = 300;
    public static final int CONTAINER_LAST = 301;
    public static final int COMPONENT_ADDED = 300;
    public static final int COMPONENT_REMOVED = 301;
    Component child;
    private static final long serialVersionUID = -4114942250539772041L;

    public ContainerEvent(Component component, int i2, Component component2) {
        super(component, i2);
        this.child = component2;
    }

    public Container getContainer() {
        if (this.source instanceof Container) {
            return (Container) this.source;
        }
        return null;
    }

    public Component getChild() {
        return this.child;
    }

    @Override // java.awt.event.ComponentEvent, java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 300:
                str = "COMPONENT_ADDED";
                break;
            case 301:
                str = "COMPONENT_REMOVED";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str + ",child=" + this.child.getName();
    }
}
