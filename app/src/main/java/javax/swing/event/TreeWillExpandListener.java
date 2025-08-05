package javax.swing.event;

import java.util.EventListener;
import javax.swing.tree.ExpandVetoException;

/* loaded from: rt.jar:javax/swing/event/TreeWillExpandListener.class */
public interface TreeWillExpandListener extends EventListener {
    void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException;

    void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException;
}
