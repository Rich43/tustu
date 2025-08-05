package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsBorders.class */
public class WindowsBorders {
    public static Border getProgressBarBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new BorderUIResource.CompoundBorderUIResource(new ProgressBarBorder(lookAndFeelDefaults.getColor("ProgressBar.shadow"), lookAndFeelDefaults.getColor("ProgressBar.highlight")), new EmptyBorder(1, 1, 1, 1));
    }

    public static Border getToolBarBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new ToolBarBorder(lookAndFeelDefaults.getColor("ToolBar.shadow"), lookAndFeelDefaults.getColor("ToolBar.highlight"));
    }

    public static Border getFocusCellHighlightBorder() {
        return new ComplementDashedBorder();
    }

    public static Border getTableHeaderBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new BorderUIResource.CompoundBorderUIResource(new BasicBorders.ButtonBorder(lookAndFeelDefaults.getColor("Table.shadow"), lookAndFeelDefaults.getColor("Table.darkShadow"), lookAndFeelDefaults.getColor("Table.light"), lookAndFeelDefaults.getColor("Table.highlight")), new BasicBorders.MarginBorder());
    }

    public static Border getInternalFrameBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new BorderUIResource.CompoundBorderUIResource(BorderFactory.createBevelBorder(0, lookAndFeelDefaults.getColor("InternalFrame.borderColor"), lookAndFeelDefaults.getColor("InternalFrame.borderHighlight"), lookAndFeelDefaults.getColor("InternalFrame.borderDarkShadow"), lookAndFeelDefaults.getColor("InternalFrame.borderShadow")), new InternalFrameLineBorder(lookAndFeelDefaults.getColor("InternalFrame.activeBorderColor"), lookAndFeelDefaults.getColor("InternalFrame.inactiveBorderColor"), lookAndFeelDefaults.getInt("InternalFrame.borderWidth")));
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsBorders$ProgressBarBorder.class */
    public static class ProgressBarBorder extends AbstractBorder implements UIResource {
        protected Color shadow;
        protected Color highlight;

        public ProgressBarBorder(Color color, Color color2) {
            this.highlight = color2;
            this.shadow = color;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.setColor(this.shadow);
            graphics.drawLine(i2, i3, i4 - 1, i3);
            graphics.drawLine(i2, i3, i2, i5 - 1);
            graphics.setColor(this.highlight);
            graphics.drawLine(i2, i5 - 1, i4 - 1, i5 - 1);
            graphics.drawLine(i4 - 1, i3, i4 - 1, i5 - 1);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(1, 1, 1, 1);
            return insets;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsBorders$ToolBarBorder.class */
    public static class ToolBarBorder extends AbstractBorder implements UIResource, SwingConstants {
        protected Color shadow;
        protected Color highlight;

        public ToolBarBorder(Color color, Color color2) {
            this.highlight = color2;
            this.shadow = color;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            int width;
            int height;
            int i6;
            int i7;
            Border border;
            if (!(component instanceof JToolBar)) {
                return;
            }
            graphics.translate(i2, i3);
            XPStyle xp = XPStyle.getXP();
            if (xp != null && (border = xp.getBorder(component, TMSchema.Part.TP_TOOLBAR)) != null) {
                border.paintBorder(component, graphics, 0, 0, i4, i5);
            }
            if (((JToolBar) component).isFloatable()) {
                boolean z2 = ((JToolBar) component).getOrientation() == 1;
                if (xp != null) {
                    XPStyle.Skin skin = xp.getSkin(component, z2 ? TMSchema.Part.RP_GRIPPERVERT : TMSchema.Part.RP_GRIPPER);
                    if (z2) {
                        i6 = 0;
                        i7 = 2;
                        width = i4 - 1;
                        height = skin.getHeight();
                    } else {
                        width = skin.getWidth();
                        height = i5 - 1;
                        i6 = component.getComponentOrientation().isLeftToRight() ? 2 : (i4 - width) - 2;
                        i7 = 0;
                    }
                    skin.paintSkin(graphics, i6, i7, width, height, TMSchema.State.NORMAL);
                } else if (!z2) {
                    if (component.getComponentOrientation().isLeftToRight()) {
                        graphics.setColor(this.shadow);
                        graphics.drawLine(4, 3, 4, i5 - 4);
                        graphics.drawLine(4, i5 - 4, 2, i5 - 4);
                        graphics.setColor(this.highlight);
                        graphics.drawLine(2, 3, 3, 3);
                        graphics.drawLine(2, 3, 2, i5 - 5);
                    } else {
                        graphics.setColor(this.shadow);
                        graphics.drawLine(i4 - 3, 3, i4 - 3, i5 - 4);
                        graphics.drawLine(i4 - 4, i5 - 4, i4 - 4, i5 - 4);
                        graphics.setColor(this.highlight);
                        graphics.drawLine(i4 - 5, 3, i4 - 4, 3);
                        graphics.drawLine(i4 - 5, 3, i4 - 5, i5 - 5);
                    }
                } else {
                    graphics.setColor(this.shadow);
                    graphics.drawLine(3, 4, i4 - 4, 4);
                    graphics.drawLine(i4 - 4, 2, i4 - 4, 4);
                    graphics.setColor(this.highlight);
                    graphics.drawLine(3, 2, i4 - 4, 2);
                    graphics.drawLine(3, 2, 3, 3);
                }
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(1, 1, 1, 1);
            if (!(component instanceof JToolBar)) {
                return insets;
            }
            if (((JToolBar) component).isFloatable()) {
                int i2 = XPStyle.getXP() != null ? 12 : 9;
                if (((JToolBar) component).getOrientation() == 0) {
                    if (component.getComponentOrientation().isLeftToRight()) {
                        insets.left = i2;
                    } else {
                        insets.right = i2;
                    }
                } else {
                    insets.top = i2;
                }
            }
            return insets;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsBorders$DashedBorder.class */
    public static class DashedBorder extends LineBorder implements UIResource {
        public DashedBorder(Color color) {
            super(color);
        }

        public DashedBorder(Color color, int i2) {
            super(color, i2);
        }

        @Override // javax.swing.border.LineBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            Color color = graphics.getColor();
            graphics.setColor(this.lineColor);
            for (int i6 = 0; i6 < this.thickness; i6++) {
                BasicGraphicsUtils.drawDashedRect(graphics, i2 + i6, i3 + i6, (i4 - i6) - i6, (i5 - i6) - i6);
            }
            graphics.setColor(color);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsBorders$ComplementDashedBorder.class */
    static class ComplementDashedBorder extends LineBorder implements UIResource {
        private Color origColor;
        private Color paintColor;

        public ComplementDashedBorder() {
            super(null);
        }

        @Override // javax.swing.border.LineBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            Color background = component.getBackground();
            if (this.origColor != background) {
                this.origColor = background;
                this.paintColor = new Color(this.origColor.getRGB() ^ (-1));
            }
            graphics.setColor(this.paintColor);
            BasicGraphicsUtils.drawDashedRect(graphics, i2, i3, i4, i5);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsBorders$InternalFrameLineBorder.class */
    public static class InternalFrameLineBorder extends LineBorder implements UIResource {
        protected Color activeColor;
        protected Color inactiveColor;

        public InternalFrameLineBorder(Color color, Color color2, int i2) {
            super(color, i2);
            this.activeColor = color;
            this.inactiveColor = color2;
        }

        @Override // javax.swing.border.LineBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            JInternalFrame internalFrame;
            if (component instanceof JInternalFrame) {
                internalFrame = (JInternalFrame) component;
            } else if (component instanceof JInternalFrame.JDesktopIcon) {
                internalFrame = ((JInternalFrame.JDesktopIcon) component).getInternalFrame();
            } else {
                return;
            }
            if (internalFrame.isSelected()) {
                this.lineColor = this.activeColor;
                super.paintBorder(component, graphics, i2, i3, i4, i5);
            } else {
                this.lineColor = this.inactiveColor;
                super.paintBorder(component, graphics, i2, i3, i4, i5);
            }
        }
    }
}
