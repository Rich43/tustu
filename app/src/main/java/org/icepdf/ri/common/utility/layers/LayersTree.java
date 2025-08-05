package org.icepdf.ri.common.utility.layers;

import java.awt.Font;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/layers/LayersTree.class */
public class LayersTree extends JTree {
    public LayersTree(TreeNode root) {
        super(root);
        setCellRenderer(new CheckBoxRenderer());
        getSelectionModel().setSelectionMode(1);
        setRootVisible(true);
        setScrollsOnExpand(true);
        setFont(new Font("SansSerif", 0, 13));
        setRowHeight(18);
    }
}
