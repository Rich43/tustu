package javax.swing.event;

import java.util.EventObject;
import javax.swing.tree.TreePath;

/* loaded from: rt.jar:javax/swing/event/TreeExpansionEvent.class */
public class TreeExpansionEvent extends EventObject {
    protected TreePath path;

    public TreeExpansionEvent(Object obj, TreePath treePath) {
        super(obj);
        this.path = treePath;
    }

    public TreePath getPath() {
        return this.path;
    }
}
