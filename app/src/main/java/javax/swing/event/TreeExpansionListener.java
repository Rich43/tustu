package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/TreeExpansionListener.class */
public interface TreeExpansionListener extends EventListener {
    void treeExpanded(TreeExpansionEvent treeExpansionEvent);

    void treeCollapsed(TreeExpansionEvent treeExpansionEvent);
}
