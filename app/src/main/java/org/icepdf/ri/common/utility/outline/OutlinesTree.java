package org.icepdf.ri.common.utility.outline;

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import org.icepdf.ri.images.Images;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/outline/OutlinesTree.class */
public class OutlinesTree extends JTree {
    public OutlinesTree() {
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

    @Override // javax.swing.JTree
    public TreePath getNextMatch(String prefix, int startingRow, Position.Bias bias) {
        int max = getRowCount();
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        if (startingRow < 0 || startingRow >= max) {
            throw new IllegalArgumentException();
        }
        String prefix2 = prefix.toUpperCase();
        int increment = bias == Position.Bias.Forward ? 1 : -1;
        int row = startingRow;
        do {
            TreePath path = getPathForRow(row);
            String text = convertValueToText(path.getLastPathComponent(), isRowSelected(row), isExpanded(row), true, row, false);
            if (text != null && text.toUpperCase().startsWith(prefix2)) {
                return path;
            }
            row = ((row + increment) + max) % max;
        } while (row != startingRow);
        return null;
    }
}
