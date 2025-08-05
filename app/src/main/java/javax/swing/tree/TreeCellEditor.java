package javax.swing.tree;

import java.awt.Component;
import javax.swing.CellEditor;
import javax.swing.JTree;

/* loaded from: rt.jar:javax/swing/tree/TreeCellEditor.class */
public interface TreeCellEditor extends CellEditor {
    Component getTreeCellEditorComponent(JTree jTree, Object obj, boolean z2, boolean z3, boolean z4, int i2);
}
