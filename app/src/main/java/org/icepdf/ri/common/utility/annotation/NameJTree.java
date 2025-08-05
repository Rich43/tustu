package org.icepdf.ri.common.utility.annotation;

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.icepdf.ri.images.Images;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/NameJTree.class */
public class NameJTree extends JTree {
    public NameJTree() {
        getSelectionModel().setSelectionMode(1);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(new ImageIcon(Images.get("page.gif")));
        renderer.setClosedIcon(new ImageIcon(Images.get("page.gif")));
        renderer.setLeafIcon(new ImageIcon(Images.get("page.gif")));
        setCellRenderer(renderer);
        setModel(null);
        setRootVisible(true);
        setScrollsOnExpand(true);
        setFont(new Font("SansSerif", 0, 13));
        setRowHeight(18);
    }
}
