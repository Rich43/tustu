package javax.swing.event;

import java.awt.AWTEvent;
import java.awt.Container;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/event/AncestorEvent.class */
public class AncestorEvent extends AWTEvent {
    public static final int ANCESTOR_ADDED = 1;
    public static final int ANCESTOR_REMOVED = 2;
    public static final int ANCESTOR_MOVED = 3;
    Container ancestor;
    Container ancestorParent;

    public AncestorEvent(JComponent jComponent, int i2, Container container, Container container2) {
        super(jComponent, i2);
        this.ancestor = container;
        this.ancestorParent = container2;
    }

    public Container getAncestor() {
        return this.ancestor;
    }

    public Container getAncestorParent() {
        return this.ancestorParent;
    }

    public JComponent getComponent() {
        return (JComponent) getSource();
    }
}
