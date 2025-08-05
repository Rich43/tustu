package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeCellRenderer;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTreeUI.class */
public class MotifTreeUI extends BasicTreeUI {
    static final int HALF_SIZE = 7;
    static final int SIZE = 14;

    @Override // javax.swing.plaf.basic.BasicTreeUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintVerticalLine(Graphics graphics, JComponent jComponent, int i2, int i3, int i4) {
        if (this.tree.getComponentOrientation().isLeftToRight()) {
            graphics.fillRect(i2, i3, 2, (i4 - i3) + 2);
        } else {
            graphics.fillRect(i2 - 1, i3, 2, (i4 - i3) + 2);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintHorizontalLine(Graphics graphics, JComponent jComponent, int i2, int i3, int i4) {
        graphics.fillRect(i3, i2, (i4 - i3) + 1, 2);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTreeUI$MotifExpandedIcon.class */
    public static class MotifExpandedIcon implements Icon, Serializable {

        /* renamed from: bg, reason: collision with root package name */
        static Color f11841bg;
        static Color fg;
        static Color highlight;
        static Color shadow;

        public MotifExpandedIcon() {
            f11841bg = UIManager.getColor("Tree.iconBackground");
            fg = UIManager.getColor("Tree.iconForeground");
            highlight = UIManager.getColor("Tree.iconHighlight");
            shadow = UIManager.getColor("Tree.iconShadow");
        }

        public static Icon createExpandedIcon() {
            return new MotifExpandedIcon();
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.setColor(highlight);
            graphics.drawLine(i2, i3, (i2 + 14) - 1, i3);
            graphics.drawLine(i2, i3 + 1, i2, (i3 + 14) - 1);
            graphics.setColor(shadow);
            graphics.drawLine((i2 + 14) - 1, i3 + 1, (i2 + 14) - 1, (i3 + 14) - 1);
            graphics.drawLine(i2 + 1, (i3 + 14) - 1, (i2 + 14) - 1, (i3 + 14) - 1);
            graphics.setColor(f11841bg);
            graphics.fillRect(i2 + 1, i3 + 1, 12, 12);
            graphics.setColor(fg);
            graphics.drawLine(i2 + 3, (i3 + 7) - 1, (i2 + 14) - 4, (i3 + 7) - 1);
            graphics.drawLine(i2 + 3, i3 + 7, (i2 + 14) - 4, i3 + 7);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 14;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 14;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTreeUI$MotifCollapsedIcon.class */
    public static class MotifCollapsedIcon extends MotifExpandedIcon {
        public static Icon createCollapsedIcon() {
            return new MotifCollapsedIcon();
        }

        @Override // com.sun.java.swing.plaf.motif.MotifTreeUI.MotifExpandedIcon, javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            super.paintIcon(component, graphics, i2, i3);
            graphics.drawLine((i2 + 7) - 1, i3 + 3, (i2 + 7) - 1, i3 + 10);
            graphics.drawLine(i2 + 7, i3 + 3, i2 + 7, i3 + 10);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifTreeUI();
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    public TreeCellRenderer createDefaultCellRenderer() {
        return new MotifTreeCellRenderer();
    }
}
