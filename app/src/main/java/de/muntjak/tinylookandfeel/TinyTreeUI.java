package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTreeUI.class */
public class TinyTreeUI extends MetalTreeUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyTreeUI();
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void installDefaults() {
        super.installDefaults();
        if (this.tree.getCellRenderer() instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer defaultTreeCellRenderer = (DefaultTreeCellRenderer) this.tree.getCellRenderer();
            defaultTreeCellRenderer.setBackgroundNonSelectionColor(Theme.treeTextBgColor.getColor());
            defaultTreeCellRenderer.setBackgroundSelectionColor(Theme.treeSelectedBgColor.getColor());
            defaultTreeCellRenderer.setTextNonSelectionColor(Theme.treeTextColor.getColor());
            defaultTreeCellRenderer.setTextSelectionColor(Theme.treeSelectedTextColor.getColor());
            UIDefaults defaults = UIManager.getDefaults();
            defaultTreeCellRenderer.setClosedIcon(defaults.getIcon("Tree.closedIcon"));
            defaultTreeCellRenderer.setOpenIcon(defaults.getIcon("Tree.openIcon"));
            defaultTreeCellRenderer.setLeafIcon(defaults.getIcon("Tree.leafIcon"));
        }
    }
}
