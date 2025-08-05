package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTreeUI.class */
public class WindowsTreeUI extends BasicTreeUI {
    protected static final int HALF_SIZE = 4;
    protected static final int SIZE = 9;

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsTreeUI();
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void ensureRowsAreVisible(int i2, int i3) {
        if (this.tree != null && i2 >= 0 && i3 < getRowCount(this.tree)) {
            Rectangle visibleRect = this.tree.getVisibleRect();
            if (i2 == i3) {
                Rectangle pathBounds = getPathBounds(this.tree, getPathForRow(this.tree, i2));
                if (pathBounds != null) {
                    pathBounds.f12372x = visibleRect.f12372x;
                    pathBounds.width = visibleRect.width;
                    this.tree.scrollRectToVisible(pathBounds);
                    return;
                }
                return;
            }
            Rectangle pathBounds2 = getPathBounds(this.tree, getPathForRow(this.tree, i2));
            if (pathBounds2 != null) {
                Rectangle pathBounds3 = pathBounds2;
                int i4 = pathBounds2.f12373y;
                int i5 = i4 + visibleRect.height;
                int i6 = i2 + 1;
                while (i6 <= i3) {
                    pathBounds3 = getPathBounds(this.tree, getPathForRow(this.tree, i6));
                    if (pathBounds3 != null && pathBounds3.f12373y + pathBounds3.height > i5) {
                        i6 = i3;
                    }
                    i6++;
                }
                if (pathBounds3 == null) {
                    return;
                }
                this.tree.scrollRectToVisible(new Rectangle(visibleRect.f12372x, i4, 1, (pathBounds3.f12373y + pathBounds3.height) - i4));
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected TreeCellRenderer createDefaultCellRenderer() {
        return new WindowsTreeCellRenderer();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTreeUI$ExpandedIcon.class */
    public static class ExpandedIcon implements Icon, Serializable {
        public static Icon createExpandedIcon() {
            return new ExpandedIcon();
        }

        XPStyle.Skin getSkin(Component component) {
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                return xp.getSkin(component, TMSchema.Part.TVP_GLYPH);
            }
            return null;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            XPStyle.Skin skin = getSkin(component);
            if (skin != null) {
                skin.paintSkin(graphics, i2, i3, TMSchema.State.OPENED);
                return;
            }
            Color background = component.getBackground();
            if (background != null) {
                graphics.setColor(background);
            } else {
                graphics.setColor(Color.white);
            }
            graphics.fillRect(i2, i3, 8, 8);
            graphics.setColor(Color.gray);
            graphics.drawRect(i2, i3, 8, 8);
            graphics.setColor(Color.black);
            graphics.drawLine(i2 + 2, i3 + 4, i2 + 6, i3 + 4);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            XPStyle.Skin skin = getSkin(null);
            if (skin != null) {
                return skin.getWidth();
            }
            return 9;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            XPStyle.Skin skin = getSkin(null);
            if (skin != null) {
                return skin.getHeight();
            }
            return 9;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTreeUI$CollapsedIcon.class */
    public static class CollapsedIcon extends ExpandedIcon {
        public static Icon createCollapsedIcon() {
            return new CollapsedIcon();
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsTreeUI.ExpandedIcon, javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            XPStyle.Skin skin = getSkin(component);
            if (skin != null) {
                skin.paintSkin(graphics, i2, i3, TMSchema.State.CLOSED);
            } else {
                super.paintIcon(component, graphics, i2, i3);
                graphics.drawLine(i2 + 4, i3 + 2, i2 + 4, i3 + 6);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTreeUI$WindowsTreeCellRenderer.class */
    public class WindowsTreeCellRenderer extends DefaultTreeCellRenderer {
        public WindowsTreeCellRenderer() {
        }

        @Override // javax.swing.tree.DefaultTreeCellRenderer, javax.swing.tree.TreeCellRenderer
        public Component getTreeCellRendererComponent(JTree jTree, Object obj, boolean z2, boolean z3, boolean z4, int i2, boolean z5) {
            super.getTreeCellRendererComponent(jTree, obj, z2, z3, z4, i2, z5);
            if (!jTree.isEnabled()) {
                setEnabled(false);
                if (z4) {
                    setDisabledIcon(getLeafIcon());
                } else if (z2) {
                    setDisabledIcon(getOpenIcon());
                } else {
                    setDisabledIcon(getClosedIcon());
                }
            } else {
                setEnabled(true);
                if (z4) {
                    setIcon(getLeafIcon());
                } else if (z2) {
                    setIcon(getOpenIcon());
                } else {
                    setIcon(getClosedIcon());
                }
            }
            return this;
        }
    }
}
