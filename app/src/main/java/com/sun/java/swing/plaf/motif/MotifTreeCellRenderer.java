package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultTreeCellRenderer;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTreeCellRenderer.class */
public class MotifTreeCellRenderer extends DefaultTreeCellRenderer {
    static final int LEAF_SIZE = 13;
    static final Icon LEAF_ICON = new IconUIResource(new TreeLeafIcon());

    public static Icon loadLeafIcon() {
        return LEAF_ICON;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTreeCellRenderer$TreeLeafIcon.class */
    public static class TreeLeafIcon implements Icon, Serializable {

        /* renamed from: bg, reason: collision with root package name */
        Color f11840bg = UIManager.getColor("Tree.iconBackground");
        Color shadow = UIManager.getColor("Tree.iconShadow");
        Color highlight = UIManager.getColor("Tree.iconHighlight");

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.setColor(this.f11840bg);
            int i4 = i3 - 3;
            graphics.fillRect(i2 + 4, i4 + 7, 5, 5);
            graphics.drawLine(i2 + 6, i4 + 6, i2 + 6, i4 + 6);
            graphics.drawLine(i2 + 3, i4 + 9, i2 + 3, i4 + 9);
            graphics.drawLine(i2 + 6, i4 + 12, i2 + 6, i4 + 12);
            graphics.drawLine(i2 + 9, i4 + 9, i2 + 9, i4 + 9);
            graphics.setColor(this.highlight);
            graphics.drawLine(i2 + 2, i4 + 9, i2 + 5, i4 + 6);
            graphics.drawLine(i2 + 3, i4 + 10, i2 + 5, i4 + 12);
            graphics.setColor(this.shadow);
            graphics.drawLine(i2 + 6, i4 + 13, i2 + 10, i4 + 9);
            graphics.drawLine(i2 + 9, i4 + 8, i2 + 7, i4 + 6);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 13;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 13;
        }
    }
}
