package org.icepdf.ri.common.utility.layers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

/* compiled from: LayersTree.java */
/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/layers/CheckBoxRenderer.class */
class CheckBoxRenderer extends JPanel implements TreeCellRenderer {
    protected JCheckBox checkBox;
    protected TreeLabel treeLabel;

    public CheckBoxRenderer() {
        setLayout(null);
        JCheckBox jCheckBox = new JCheckBox();
        this.checkBox = jCheckBox;
        add(jCheckBox);
        TreeLabel treeLabel = new TreeLabel();
        this.treeLabel = treeLabel;
        add(treeLabel);
        this.checkBox.setBackground(UIManager.getColor("Tree.textBackground"));
        this.treeLabel.setForeground(UIManager.getColor("Tree.textForeground"));
    }

    @Override // javax.swing.tree.TreeCellRenderer
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        if (value instanceof LayersTreeNode) {
            this.checkBox.setSelected(((LayersTreeNode) value).isSelected());
        }
        this.treeLabel.setFont(tree.getFont());
        this.treeLabel.setText(stringValue);
        this.treeLabel.setSelected(isSelected);
        this.treeLabel.setFocus(hasFocus);
        return this;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension d_check = this.checkBox.getPreferredSize();
        Dimension d_label = this.treeLabel.getPreferredSize();
        return new Dimension(d_check.width + d_label.width, d_check.height < d_label.height ? d_label.height : d_check.height);
    }

    @Override // java.awt.Container, java.awt.Component
    public void doLayout() {
        Dimension dCheck = this.checkBox.getPreferredSize();
        Dimension dLabel = this.treeLabel.getPreferredSize();
        int yCheck = 0;
        int yLabel = 0;
        if (dCheck.height < dLabel.height) {
            yCheck = (dLabel.height - dCheck.height) / 2;
        } else {
            yLabel = (dCheck.height - dLabel.height) / 2;
        }
        this.checkBox.setLocation(0, yCheck);
        this.checkBox.setBounds(0, yCheck, dCheck.width, dCheck.height);
        this.treeLabel.setLocation(dCheck.width, yLabel);
        this.treeLabel.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource) {
            color = null;
        }
        super.setBackground(color);
    }

    /* compiled from: LayersTree.java */
    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/layers/CheckBoxRenderer$TreeLabel.class */
    public class TreeLabel extends JLabel {
        boolean isSelected;
        boolean hasFocus;

        public TreeLabel() {
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public void setBackground(Color color) {
            if (color instanceof ColorUIResource) {
                color = null;
            }
            super.setBackground(color);
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics g2) {
            String str = getText();
            if (str != null && 0 < str.length()) {
                if (this.isSelected) {
                    g2.setColor(UIManager.getColor("Tree.selectionBackground"));
                } else {
                    g2.setColor(UIManager.getColor("Tree.textBackground"));
                }
                Dimension d2 = getPreferredSize();
                int imageOffset = 0;
                Icon currentI = getIcon();
                if (currentI != null) {
                    imageOffset = currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
                }
                g2.fillRect(imageOffset, 0, (d2.width - 1) - imageOffset, d2.height);
                if (this.hasFocus) {
                    g2.setColor(UIManager.getColor("Tree.selectionBorderColor"));
                    g2.drawRect(imageOffset, 0, (d2.width - 1) - imageOffset, d2.height - 1);
                }
            }
            super.paint(g2);
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            Dimension retDimension = super.getPreferredSize();
            if (retDimension != null) {
                retDimension = new Dimension(retDimension.width + 3, retDimension.height);
            }
            return retDimension;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public void setFocus(boolean hasFocus) {
            this.hasFocus = hasFocus;
        }
    }
}
