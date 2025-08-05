package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/TreeModelListener.class */
public interface TreeModelListener extends EventListener {
    void treeNodesChanged(TreeModelEvent treeModelEvent);

    void treeNodesInserted(TreeModelEvent treeModelEvent);

    void treeNodesRemoved(TreeModelEvent treeModelEvent);

    void treeStructureChanged(TreeModelEvent treeModelEvent);
}
