package javax.swing.tree;

import javax.swing.event.TreeExpansionEvent;

/* loaded from: rt.jar:javax/swing/tree/ExpandVetoException.class */
public class ExpandVetoException extends Exception {
    protected TreeExpansionEvent event;

    public ExpandVetoException(TreeExpansionEvent treeExpansionEvent) {
        this(treeExpansionEvent, null);
    }

    public ExpandVetoException(TreeExpansionEvent treeExpansionEvent, String str) {
        super(str);
        this.event = treeExpansionEvent;
    }
}
