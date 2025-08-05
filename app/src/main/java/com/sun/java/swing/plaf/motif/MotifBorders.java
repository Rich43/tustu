package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders.class */
public class MotifBorders {

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders$BevelBorder.class */
    public static class BevelBorder extends AbstractBorder implements UIResource {
        private Color darkShadow;
        private Color lightShadow;
        private boolean isRaised;

        public BevelBorder(boolean z2, Color color, Color color2) {
            this.darkShadow = UIManager.getColor("controlShadow");
            this.lightShadow = UIManager.getColor("controlLtHighlight");
            this.isRaised = z2;
            this.darkShadow = color;
            this.lightShadow = color2;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.setColor(this.isRaised ? this.lightShadow : this.darkShadow);
            graphics.drawLine(i2, i3, (i2 + i4) - 1, i3);
            graphics.drawLine(i2, (i3 + i5) - 1, i2, i3 + 1);
            graphics.setColor(this.isRaised ? this.darkShadow : this.lightShadow);
            graphics.drawLine(i2 + 1, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
            graphics.drawLine((i2 + i4) - 1, (i3 + i5) - 1, (i2 + i4) - 1, i3 + 1);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(1, 1, 1, 1);
            return insets;
        }

        public boolean isOpaque(Component component) {
            return true;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders$FocusBorder.class */
    public static class FocusBorder extends AbstractBorder implements UIResource {
        private Color focus;
        private Color control;

        public FocusBorder(Color color, Color color2) {
            this.control = color;
            this.focus = color2;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (component.hasFocus()) {
                graphics.setColor(this.focus);
                graphics.drawRect(i2, i3, i4 - 1, i5 - 1);
            } else {
                graphics.setColor(this.control);
                graphics.drawRect(i2, i3, i4 - 1, i5 - 1);
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(1, 1, 1, 1);
            return insets;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders$ButtonBorder.class */
    public static class ButtonBorder extends AbstractBorder implements UIResource {
        protected Color focus;
        protected Color shadow;
        protected Color highlight;
        protected Color darkShadow;

        public ButtonBorder(Color color, Color color2, Color color3, Color color4) {
            this.focus = UIManager.getColor("activeCaptionBorder");
            this.shadow = UIManager.getColor("Button.shadow");
            this.highlight = UIManager.getColor("Button.light");
            this.shadow = color;
            this.highlight = color2;
            this.darkShadow = color3;
            this.focus = color4;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            boolean z2 = false;
            boolean z3 = false;
            boolean zIsDefaultCapable = false;
            boolean zIsDefaultButton = false;
            if (component instanceof AbstractButton) {
                AbstractButton abstractButton = (AbstractButton) component;
                ButtonModel model = abstractButton.getModel();
                z2 = model.isArmed() && model.isPressed();
                z3 = (model.isArmed() && z2) || (abstractButton.isFocusPainted() && abstractButton.hasFocus());
                if (abstractButton instanceof JButton) {
                    zIsDefaultCapable = ((JButton) abstractButton).isDefaultCapable();
                    zIsDefaultButton = ((JButton) abstractButton).isDefaultButton();
                }
            }
            int i6 = i2 + 1;
            int i7 = i3 + 1;
            int i8 = (i2 + i4) - 2;
            int i9 = (i3 + i5) - 2;
            if (zIsDefaultCapable) {
                if (zIsDefaultButton) {
                    graphics.setColor(this.shadow);
                    graphics.drawLine(i2 + 3, i3 + 3, i2 + 3, (i3 + i5) - 4);
                    graphics.drawLine(i2 + 3, i3 + 3, (i2 + i4) - 4, i3 + 3);
                    graphics.setColor(this.highlight);
                    graphics.drawLine(i2 + 4, (i3 + i5) - 4, (i2 + i4) - 4, (i3 + i5) - 4);
                    graphics.drawLine((i2 + i4) - 4, i3 + 3, (i2 + i4) - 4, (i3 + i5) - 4);
                }
                i6 += 6;
                i7 += 6;
                i8 -= 6;
                i9 -= 6;
            }
            if (z3) {
                graphics.setColor(this.focus);
                if (zIsDefaultButton) {
                    graphics.drawRect(i2, i3, i4 - 1, i5 - 1);
                } else {
                    graphics.drawRect(i6 - 1, i7 - 1, (i8 - i6) + 2, (i9 - i7) + 2);
                }
            }
            graphics.setColor(z2 ? this.shadow : this.highlight);
            graphics.drawLine(i6, i7, i8, i7);
            graphics.drawLine(i6, i7, i6, i9);
            graphics.setColor(z2 ? this.highlight : this.shadow);
            graphics.drawLine(i8, i7 + 1, i8, i9);
            graphics.drawLine(i6 + 1, i9, i8, i9);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            int i2 = ((component instanceof JButton) && ((JButton) component).isDefaultCapable()) ? 8 : 2;
            insets.set(i2, i2, i2, i2);
            return insets;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders$ToggleButtonBorder.class */
    public static class ToggleButtonBorder extends ButtonBorder {
        public ToggleButtonBorder(Color color, Color color2, Color color3, Color color4) {
            super(color, color2, color3, color4);
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.ButtonBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (component instanceof AbstractButton) {
                AbstractButton abstractButton = (AbstractButton) component;
                ButtonModel model = abstractButton.getModel();
                if ((model.isArmed() && model.isPressed()) || model.isSelected()) {
                    MotifBorders.drawBezel(graphics, i2, i3, i4, i5, model.isPressed() || model.isSelected(), abstractButton.isFocusPainted() && abstractButton.hasFocus(), this.shadow, this.highlight, this.darkShadow, this.focus);
                    return;
                } else {
                    MotifBorders.drawBezel(graphics, i2, i3, i4, i5, false, abstractButton.isFocusPainted() && abstractButton.hasFocus(), this.shadow, this.highlight, this.darkShadow, this.focus);
                    return;
                }
            }
            MotifBorders.drawBezel(graphics, i2, i3, i4, i5, false, false, this.shadow, this.highlight, this.darkShadow, this.focus);
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.ButtonBorder, javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 2, 3, 3);
            return insets;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders$MenuBarBorder.class */
    public static class MenuBarBorder extends ButtonBorder {
        public MenuBarBorder(Color color, Color color2, Color color3, Color color4) {
            super(color, color2, color3, color4);
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.ButtonBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof JMenuBar)) {
                return;
            }
            JMenuBar jMenuBar = (JMenuBar) component;
            if (jMenuBar.isBorderPainted()) {
                Dimension size = jMenuBar.getSize();
                MotifBorders.drawBezel(graphics, i2, i3, size.width, size.height, false, false, this.shadow, this.highlight, this.darkShadow, this.focus);
            }
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.ButtonBorder, javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(6, 6, 6, 6);
            return insets;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders$FrameBorder.class */
    public static class FrameBorder extends AbstractBorder implements UIResource {
        JComponent jcomp;
        Color frameHighlight;
        Color frameColor;
        Color frameShadow;
        public static final int BORDER_SIZE = 5;

        public FrameBorder(JComponent jComponent) {
            this.jcomp = jComponent;
        }

        public void setComponent(JComponent jComponent) {
            this.jcomp = jComponent;
        }

        public JComponent component() {
            return this.jcomp;
        }

        protected Color getFrameHighlight() {
            return this.frameHighlight;
        }

        protected Color getFrameColor() {
            return this.frameColor;
        }

        protected Color getFrameShadow() {
            return this.frameShadow;
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(5, 5, 5, 5);
            return insets;
        }

        protected boolean drawTopBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!graphics.getClipBounds().intersects(new Rectangle(i2, i3, i4, 5))) {
                return false;
            }
            int i6 = i4 - 1;
            graphics.setColor(this.frameColor);
            graphics.drawLine(i2, i3 + 2, i6 - 2, i3 + 2);
            graphics.drawLine(i2, i3 + 3, i6 - 2, i3 + 3);
            graphics.drawLine(i2, i3 + 4, i6 - 2, i3 + 4);
            graphics.setColor(this.frameHighlight);
            graphics.drawLine(i2, i3, i6, i3);
            graphics.drawLine(i2, i3 + 1, i6, i3 + 1);
            graphics.drawLine(i2, i3 + 2, i2, i3 + 4);
            graphics.drawLine(i2 + 1, i3 + 2, i2 + 1, i3 + 4);
            graphics.setColor(this.frameShadow);
            graphics.drawLine(i2 + 4, i3 + 4, i6 - 4, i3 + 4);
            graphics.drawLine(i6, i3 + 1, i6, 4);
            graphics.drawLine(i6 - 1, i3 + 2, i6 - 1, 4);
            return true;
        }

        protected boolean drawLeftBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!graphics.getClipBounds().intersects(new Rectangle(0, 0, getBorderInsets(component).left, i5))) {
                return false;
            }
            graphics.setColor(this.frameHighlight);
            graphics.drawLine(i2, 5, i2, i5 - 1);
            graphics.drawLine(i2 + 1, 5, i2 + 1, i5 - 2);
            graphics.setColor(this.frameColor);
            graphics.fillRect(i2 + 2, 5, i2 + 2, i5 - 3);
            graphics.setColor(this.frameShadow);
            graphics.drawLine(i2 + 4, 5, i2 + 4, i5 - 5);
            return true;
        }

        protected boolean drawRightBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!graphics.getClipBounds().intersects(new Rectangle(i4 - getBorderInsets(component).right, 0, getBorderInsets(component).right, i5))) {
                return false;
            }
            int i6 = i4 - getBorderInsets(component).right;
            graphics.setColor(this.frameColor);
            graphics.fillRect(i6 + 1, 5, 2, i5 - 1);
            graphics.setColor(this.frameShadow);
            graphics.fillRect(i6 + 3, 5, 2, i5 - 1);
            graphics.setColor(this.frameHighlight);
            graphics.drawLine(i6, 5, i6, i5 - 1);
            return true;
        }

        protected boolean drawBottomBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!graphics.getClipBounds().intersects(new Rectangle(0, i5 - getBorderInsets(component).bottom, i4, getBorderInsets(component).bottom))) {
                return false;
            }
            int i6 = i5 - getBorderInsets(component).bottom;
            graphics.setColor(this.frameShadow);
            graphics.drawLine(i2 + 1, i5 - 1, i4 - 1, i5 - 1);
            graphics.drawLine(i2 + 2, i5 - 2, i4 - 2, i5 - 2);
            graphics.setColor(this.frameColor);
            graphics.fillRect(i2 + 2, i6 + 1, i4 - 4, 2);
            graphics.setColor(this.frameHighlight);
            graphics.drawLine(i2 + 5, i6, i4 - 5, i6);
            return true;
        }

        protected boolean isActiveFrame() {
            return this.jcomp.hasFocus();
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (isActiveFrame()) {
                this.frameColor = UIManager.getColor("activeCaptionBorder");
            } else {
                this.frameColor = UIManager.getColor("inactiveCaptionBorder");
            }
            this.frameHighlight = this.frameColor.brighter();
            this.frameShadow = this.frameColor.darker().darker();
            drawTopBorder(component, graphics, i2, i3, i4, i5);
            drawLeftBorder(component, graphics, i2, i3, i4, i5);
            drawRightBorder(component, graphics, i2, i3, i4, i5);
            drawBottomBorder(component, graphics, i2, i3, i4, i5);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders$InternalFrameBorder.class */
    public static class InternalFrameBorder extends FrameBorder {
        JInternalFrame frame;
        public static final int CORNER_SIZE = 24;

        public InternalFrameBorder(JInternalFrame jInternalFrame) {
            super(jInternalFrame);
            this.frame = jInternalFrame;
        }

        public void setFrame(JInternalFrame jInternalFrame) {
            this.frame = jInternalFrame;
        }

        public JInternalFrame frame() {
            return this.frame;
        }

        public int resizePartWidth() {
            if (!this.frame.isResizable()) {
                return 0;
            }
            return 5;
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.FrameBorder
        protected boolean drawTopBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (super.drawTopBorder(component, graphics, i2, i3, i4, i5) && this.frame.isResizable()) {
                graphics.setColor(getFrameShadow());
                graphics.drawLine(23, i3 + 1, 23, i3 + 4);
                graphics.drawLine((i4 - 24) - 1, i3 + 1, (i4 - 24) - 1, i3 + 4);
                graphics.setColor(getFrameHighlight());
                graphics.drawLine(24, i3, 24, i3 + 4);
                graphics.drawLine(i4 - 24, i3, i4 - 24, i3 + 4);
                return true;
            }
            return false;
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.FrameBorder
        protected boolean drawLeftBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (super.drawLeftBorder(component, graphics, i2, i3, i4, i5) && this.frame.isResizable()) {
                graphics.setColor(getFrameHighlight());
                int i6 = i3 + 24;
                graphics.drawLine(i2, i6, i2 + 4, i6);
                int i7 = i5 - 24;
                graphics.drawLine(i2 + 1, i7, i2 + 5, i7);
                graphics.setColor(getFrameShadow());
                graphics.drawLine(i2 + 1, i6 - 1, i2 + 5, i6 - 1);
                graphics.drawLine(i2 + 1, i7 - 1, i2 + 5, i7 - 1);
                return true;
            }
            return false;
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.FrameBorder
        protected boolean drawRightBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (super.drawRightBorder(component, graphics, i2, i3, i4, i5) && this.frame.isResizable()) {
                int i6 = i4 - getBorderInsets(component).right;
                graphics.setColor(getFrameHighlight());
                int i7 = i3 + 24;
                graphics.drawLine(i6, i7, i4 - 2, i7);
                int i8 = i5 - 24;
                graphics.drawLine(i6 + 1, i8, i6 + 3, i8);
                graphics.setColor(getFrameShadow());
                graphics.drawLine(i6 + 1, i7 - 1, i4 - 2, i7 - 1);
                graphics.drawLine(i6 + 1, i8 - 1, i6 + 3, i8 - 1);
                return true;
            }
            return false;
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.FrameBorder
        protected boolean drawBottomBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (super.drawBottomBorder(component, graphics, i2, i3, i4, i5) && this.frame.isResizable()) {
                int i6 = i5 - getBorderInsets(component).bottom;
                graphics.setColor(getFrameShadow());
                graphics.drawLine(23, i6 + 1, 23, i5 - 1);
                graphics.drawLine(i4 - 24, i6 + 1, i4 - 24, i5 - 1);
                graphics.setColor(getFrameHighlight());
                graphics.drawLine(24, i6, 24, i5 - 2);
                graphics.drawLine((i4 - 24) + 1, i6, (i4 - 24) + 1, i5 - 2);
                return true;
            }
            return false;
        }

        @Override // com.sun.java.swing.plaf.motif.MotifBorders.FrameBorder
        protected boolean isActiveFrame() {
            return this.frame.isSelected();
        }
    }

    public static void drawBezel(Graphics graphics, int i2, int i3, int i4, int i5, boolean z2, boolean z3, Color color, Color color2, Color color3, Color color4) {
        Color color5 = graphics.getColor();
        graphics.translate(i2, i3);
        if (z2) {
            if (z3) {
                graphics.setColor(color4);
                graphics.drawRect(0, 0, i4 - 1, i5 - 1);
            }
            graphics.setColor(color);
            graphics.drawRect(1, 1, i4 - 3, i5 - 3);
            graphics.setColor(color2);
            graphics.drawLine(2, i5 - 3, i4 - 3, i5 - 3);
            graphics.drawLine(i4 - 3, 2, i4 - 3, i5 - 4);
        } else {
            if (z3) {
                graphics.setColor(color4);
                graphics.drawRect(0, 0, i4 - 1, i5 - 1);
                graphics.setColor(color2);
                graphics.drawLine(1, 1, 1, i5 - 3);
                graphics.drawLine(2, 1, i4 - 4, 1);
                graphics.setColor(color);
                graphics.drawLine(2, i5 - 3, i4 - 3, i5 - 3);
                graphics.drawLine(i4 - 3, 1, i4 - 3, i5 - 4);
                graphics.setColor(color3);
                graphics.drawLine(1, i5 - 2, i4 - 2, i5 - 2);
                graphics.drawLine(i4 - 2, i5 - 2, i4 - 2, 1);
            } else {
                graphics.setColor(color2);
                graphics.drawLine(1, 1, 1, i5 - 3);
                graphics.drawLine(2, 1, i4 - 4, 1);
                graphics.setColor(color);
                graphics.drawLine(2, i5 - 3, i4 - 3, i5 - 3);
                graphics.drawLine(i4 - 3, 1, i4 - 3, i5 - 4);
                graphics.setColor(color3);
                graphics.drawLine(1, i5 - 2, i4 - 2, i5 - 2);
                graphics.drawLine(i4 - 2, i5 - 2, i4 - 2, 0);
            }
            graphics.translate(-i2, -i3);
        }
        graphics.setColor(color5);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifBorders$MotifPopupMenuBorder.class */
    public static class MotifPopupMenuBorder extends AbstractBorder implements UIResource {
        protected Font font;
        protected Color background;
        protected Color foreground;
        protected Color shadowColor;
        protected Color highlightColor;
        protected static final int TEXT_SPACING = 2;
        protected static final int GROOVE_HEIGHT = 2;

        public MotifPopupMenuBorder(Font font, Color color, Color color2, Color color3, Color color4) {
            this.font = font;
            this.background = color;
            this.foreground = color2;
            this.shadowColor = color3;
            this.highlightColor = color4;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof JPopupMenu)) {
                return;
            }
            Font font = graphics.getFont();
            Color color = graphics.getColor();
            JPopupMenu jPopupMenu = (JPopupMenu) component;
            String label = jPopupMenu.getLabel();
            if (label == null) {
                return;
            }
            graphics.setFont(this.font);
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jPopupMenu, graphics, this.font);
            int height = fontMetrics.getHeight();
            int descent = fontMetrics.getDescent();
            int ascent = fontMetrics.getAscent();
            Point point = new Point();
            int iStringWidth = SwingUtilities2.stringWidth(jPopupMenu, fontMetrics, label);
            point.f12371y = i3 + ascent + 2;
            point.f12370x = i2 + ((i4 - iStringWidth) / 2);
            graphics.setColor(this.background);
            graphics.fillRect(point.f12370x - 2, point.f12371y - (height - descent), iStringWidth + 4, height - descent);
            graphics.setColor(this.foreground);
            SwingUtilities2.drawString(jPopupMenu, graphics, label, point.f12370x, point.f12371y);
            MotifGraphicsUtils.drawGroove(graphics, i2, point.f12371y + 2, i4, 2, this.shadowColor, this.highlightColor);
            graphics.setFont(font);
            graphics.setColor(color);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            if (!(component instanceof JPopupMenu)) {
                return insets;
            }
            int descent = 0;
            int ascent = 16;
            if (((JPopupMenu) component).getLabel() == null) {
                insets.bottom = 0;
                insets.right = 0;
                insets.top = 0;
                insets.left = 0;
                return insets;
            }
            FontMetrics fontMetrics = component.getFontMetrics(this.font);
            if (fontMetrics != null) {
                descent = fontMetrics.getDescent();
                ascent = fontMetrics.getAscent();
            }
            insets.top += ascent + descent + 2 + 2;
            return insets;
        }
    }
}
