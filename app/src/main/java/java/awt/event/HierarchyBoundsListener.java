package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/HierarchyBoundsListener.class */
public interface HierarchyBoundsListener extends EventListener {
    void ancestorMoved(HierarchyEvent hierarchyEvent);

    void ancestorResized(HierarchyEvent hierarchyEvent);
}
