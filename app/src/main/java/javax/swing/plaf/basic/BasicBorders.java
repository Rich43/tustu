package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders.class */
public class BasicBorders {
    public static Border getButtonBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new BorderUIResource.CompoundBorderUIResource(new ButtonBorder(lookAndFeelDefaults.getColor("Button.shadow"), lookAndFeelDefaults.getColor("Button.darkShadow"), lookAndFeelDefaults.getColor("Button.light"), lookAndFeelDefaults.getColor("Button.highlight")), new MarginBorder());
    }

    public static Border getRadioButtonBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new BorderUIResource.CompoundBorderUIResource(new RadioButtonBorder(lookAndFeelDefaults.getColor("RadioButton.shadow"), lookAndFeelDefaults.getColor("RadioButton.darkShadow"), lookAndFeelDefaults.getColor("RadioButton.light"), lookAndFeelDefaults.getColor("RadioButton.highlight")), new MarginBorder());
    }

    public static Border getToggleButtonBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new BorderUIResource.CompoundBorderUIResource(new ToggleButtonBorder(lookAndFeelDefaults.getColor("ToggleButton.shadow"), lookAndFeelDefaults.getColor("ToggleButton.darkShadow"), lookAndFeelDefaults.getColor("ToggleButton.light"), lookAndFeelDefaults.getColor("ToggleButton.highlight")), new MarginBorder());
    }

    public static Border getMenuBarBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new MenuBarBorder(lookAndFeelDefaults.getColor("MenuBar.shadow"), lookAndFeelDefaults.getColor("MenuBar.highlight"));
    }

    public static Border getSplitPaneBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new SplitPaneBorder(lookAndFeelDefaults.getColor("SplitPane.highlight"), lookAndFeelDefaults.getColor("SplitPane.darkShadow"));
    }

    public static Border getSplitPaneDividerBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new SplitPaneDividerBorder(lookAndFeelDefaults.getColor("SplitPane.highlight"), lookAndFeelDefaults.getColor("SplitPane.darkShadow"));
    }

    public static Border getTextFieldBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new FieldBorder(lookAndFeelDefaults.getColor("TextField.shadow"), lookAndFeelDefaults.getColor("TextField.darkShadow"), lookAndFeelDefaults.getColor("TextField.light"), lookAndFeelDefaults.getColor("TextField.highlight"));
    }

    public static Border getProgressBarBorder() {
        UIManager.getLookAndFeelDefaults();
        return new BorderUIResource.LineBorderUIResource(Color.green, 2);
    }

    public static Border getInternalFrameBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new BorderUIResource.CompoundBorderUIResource(new BevelBorder(0, lookAndFeelDefaults.getColor("InternalFrame.borderLight"), lookAndFeelDefaults.getColor("InternalFrame.borderHighlight"), lookAndFeelDefaults.getColor("InternalFrame.borderDarkShadow"), lookAndFeelDefaults.getColor("InternalFrame.borderShadow")), BorderFactory.createLineBorder(lookAndFeelDefaults.getColor("InternalFrame.borderColor"), 1));
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$RolloverButtonBorder.class */
    public static class RolloverButtonBorder extends ButtonBorder {
        public RolloverButtonBorder(Color color, Color color2, Color color3, Color color4) {
            super(color, color2, color3, color4);
        }

        @Override // javax.swing.plaf.basic.BasicBorders.ButtonBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            AbstractButton abstractButton = (AbstractButton) component;
            ButtonModel model = abstractButton.getModel();
            Color color = this.shadow;
            Container parent = abstractButton.getParent();
            if (parent != null && parent.getBackground().equals(this.shadow)) {
                color = this.darkShadow;
            }
            if ((model.isRollover() && (!model.isPressed() || model.isArmed())) || model.isSelected()) {
                Color color2 = graphics.getColor();
                graphics.translate(i2, i3);
                if ((model.isPressed() && model.isArmed()) || model.isSelected()) {
                    graphics.setColor(color);
                    graphics.drawRect(0, 0, i4 - 1, i5 - 1);
                    graphics.setColor(this.lightHighlight);
                    graphics.drawLine(i4 - 1, 0, i4 - 1, i5 - 1);
                    graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
                } else {
                    graphics.setColor(this.lightHighlight);
                    graphics.drawRect(0, 0, i4 - 1, i5 - 1);
                    graphics.setColor(color);
                    graphics.drawLine(i4 - 1, 0, i4 - 1, i5 - 1);
                    graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
                }
                graphics.translate(-i2, -i3);
                graphics.setColor(color2);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$RolloverMarginBorder.class */
    static class RolloverMarginBorder extends EmptyBorder {
        public RolloverMarginBorder() {
            super(3, 3, 3, 3);
        }

        @Override // javax.swing.border.EmptyBorder, javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            Insets margin = null;
            if (component instanceof AbstractButton) {
                margin = ((AbstractButton) component).getMargin();
            }
            if (margin == null || (margin instanceof UIResource)) {
                insets.left = this.left;
                insets.top = this.top;
                insets.right = this.right;
                insets.bottom = this.bottom;
            } else {
                insets.left = margin.left;
                insets.top = margin.top;
                insets.right = margin.right;
                insets.bottom = margin.bottom;
            }
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$ButtonBorder.class */
    public static class ButtonBorder extends AbstractBorder implements UIResource {
        protected Color shadow;
        protected Color darkShadow;
        protected Color highlight;
        protected Color lightHighlight;

        public ButtonBorder(Color color, Color color2, Color color3, Color color4) {
            this.shadow = color;
            this.darkShadow = color2;
            this.highlight = color3;
            this.lightHighlight = color4;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            boolean z2 = false;
            boolean zIsDefaultButton = false;
            if (component instanceof AbstractButton) {
                ButtonModel model = ((AbstractButton) component).getModel();
                z2 = model.isPressed() && model.isArmed();
                if (component instanceof JButton) {
                    zIsDefaultButton = ((JButton) component).isDefaultButton();
                }
            }
            BasicGraphicsUtils.drawBezel(graphics, i2, i3, i4, i5, z2, zIsDefaultButton, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 3, 3, 3);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$ToggleButtonBorder.class */
    public static class ToggleButtonBorder extends ButtonBorder {
        public ToggleButtonBorder(Color color, Color color2, Color color3, Color color4) {
            super(color, color2, color3, color4);
        }

        @Override // javax.swing.plaf.basic.BasicBorders.ButtonBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            BasicGraphicsUtils.drawBezel(graphics, i2, i3, i4, i5, false, false, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
        }

        @Override // javax.swing.plaf.basic.BasicBorders.ButtonBorder, javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 2, 2, 2);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$RadioButtonBorder.class */
    public static class RadioButtonBorder extends ButtonBorder {
        public RadioButtonBorder(Color color, Color color2, Color color3, Color color4) {
            super(color, color2, color3, color4);
        }

        @Override // javax.swing.plaf.basic.BasicBorders.ButtonBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (component instanceof AbstractButton) {
                AbstractButton abstractButton = (AbstractButton) component;
                ButtonModel model = abstractButton.getModel();
                if ((model.isArmed() && model.isPressed()) || model.isSelected()) {
                    BasicGraphicsUtils.drawLoweredBezel(graphics, i2, i3, i4, i5, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
                    return;
                } else {
                    BasicGraphicsUtils.drawBezel(graphics, i2, i3, i4, i5, false, abstractButton.isFocusPainted() && abstractButton.hasFocus(), this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
                    return;
                }
            }
            BasicGraphicsUtils.drawBezel(graphics, i2, i3, i4, i5, false, false, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
        }

        @Override // javax.swing.plaf.basic.BasicBorders.ButtonBorder, javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 2, 2, 2);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$MenuBarBorder.class */
    public static class MenuBarBorder extends AbstractBorder implements UIResource {
        private Color shadow;
        private Color highlight;

        public MenuBarBorder(Color color, Color color2) {
            this.shadow = color;
            this.highlight = color2;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            Color color = graphics.getColor();
            graphics.translate(i2, i3);
            graphics.setColor(this.shadow);
            SwingUtilities2.drawHLine(graphics, 0, i4 - 1, i5 - 2);
            graphics.setColor(this.highlight);
            SwingUtilities2.drawHLine(graphics, 0, i4 - 1, i5 - 1);
            graphics.translate(-i2, -i3);
            graphics.setColor(color);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(0, 0, 2, 0);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$MarginBorder.class */
    public static class MarginBorder extends AbstractBorder implements UIResource {
        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            Insets margin = null;
            if (component instanceof AbstractButton) {
                margin = ((AbstractButton) component).getMargin();
            } else if (component instanceof JToolBar) {
                margin = ((JToolBar) component).getMargin();
            } else if (component instanceof JTextComponent) {
                margin = ((JTextComponent) component).getMargin();
            }
            insets.top = margin != null ? margin.top : 0;
            insets.left = margin != null ? margin.left : 0;
            insets.bottom = margin != null ? margin.bottom : 0;
            insets.right = margin != null ? margin.right : 0;
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$FieldBorder.class */
    public static class FieldBorder extends AbstractBorder implements UIResource {
        protected Color shadow;
        protected Color darkShadow;
        protected Color highlight;
        protected Color lightHighlight;

        public FieldBorder(Color color, Color color2, Color color3, Color color4) {
            this.shadow = color;
            this.highlight = color3;
            this.darkShadow = color2;
            this.lightHighlight = color4;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            BasicGraphicsUtils.drawEtchedRect(graphics, i2, i3, i4, i5, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            Insets margin = null;
            if (component instanceof JTextComponent) {
                margin = ((JTextComponent) component).getMargin();
            }
            insets.top = margin != null ? 2 + margin.top : 2;
            insets.left = margin != null ? 2 + margin.left : 2;
            insets.bottom = margin != null ? 2 + margin.bottom : 2;
            insets.right = margin != null ? 2 + margin.right : 2;
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$SplitPaneDividerBorder.class */
    static class SplitPaneDividerBorder implements Border, UIResource {
        Color highlight;
        Color shadow;

        SplitPaneDividerBorder(Color color, Color color2) {
            this.highlight = color;
            this.shadow = color2;
        }

        @Override // javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof BasicSplitPaneDivider)) {
                return;
            }
            JSplitPane splitPane = ((BasicSplitPaneDivider) component).getBasicSplitPaneUI().getSplitPane();
            Dimension size = component.getSize();
            Component leftComponent = splitPane.getLeftComponent();
            graphics.setColor(component.getBackground());
            graphics.drawRect(i2, i3, i4 - 1, i5 - 1);
            if (splitPane.getOrientation() == 1) {
                if (leftComponent != null) {
                    graphics.setColor(this.highlight);
                    graphics.drawLine(0, 0, 0, size.height);
                }
                if (splitPane.getRightComponent() != null) {
                    graphics.setColor(this.shadow);
                    graphics.drawLine(size.width - 1, 0, size.width - 1, size.height);
                    return;
                }
                return;
            }
            if (leftComponent != null) {
                graphics.setColor(this.highlight);
                graphics.drawLine(0, 0, size.width, 0);
            }
            if (splitPane.getRightComponent() != null) {
                graphics.setColor(this.shadow);
                graphics.drawLine(0, size.height - 1, size.width, size.height - 1);
            }
        }

        @Override // javax.swing.border.Border
        public Insets getBorderInsets(Component component) {
            BasicSplitPaneUI basicSplitPaneUI;
            JSplitPane splitPane;
            Insets insets = new Insets(0, 0, 0, 0);
            if ((component instanceof BasicSplitPaneDivider) && (basicSplitPaneUI = ((BasicSplitPaneDivider) component).getBasicSplitPaneUI()) != null && (splitPane = basicSplitPaneUI.getSplitPane()) != null) {
                if (splitPane.getOrientation() == 1) {
                    insets.bottom = 0;
                    insets.top = 0;
                    insets.right = 1;
                    insets.left = 1;
                    return insets;
                }
                insets.bottom = 1;
                insets.top = 1;
                insets.right = 0;
                insets.left = 0;
                return insets;
            }
            insets.right = 1;
            insets.left = 1;
            insets.bottom = 1;
            insets.top = 1;
            return insets;
        }

        @Override // javax.swing.border.Border
        public boolean isBorderOpaque() {
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicBorders$SplitPaneBorder.class */
    public static class SplitPaneBorder implements Border, UIResource {
        protected Color highlight;
        protected Color shadow;

        public SplitPaneBorder(Color color, Color color2) {
            this.highlight = color;
            this.shadow = color2;
        }

        @Override // javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof JSplitPane)) {
                return;
            }
            JSplitPane jSplitPane = (JSplitPane) component;
            Component leftComponent = jSplitPane.getLeftComponent();
            graphics.setColor(component.getBackground());
            graphics.drawRect(i2, i3, i4 - 1, i5 - 1);
            if (jSplitPane.getOrientation() == 1) {
                if (leftComponent != null) {
                    Rectangle bounds = leftComponent.getBounds();
                    graphics.setColor(this.shadow);
                    graphics.drawLine(0, 0, bounds.width + 1, 0);
                    graphics.drawLine(0, 1, 0, bounds.height + 1);
                    graphics.setColor(this.highlight);
                    graphics.drawLine(0, bounds.height + 1, bounds.width + 1, bounds.height + 1);
                }
                Component rightComponent = jSplitPane.getRightComponent();
                if (rightComponent != null) {
                    Rectangle bounds2 = rightComponent.getBounds();
                    int i6 = bounds2.f12372x + bounds2.width;
                    int i7 = bounds2.f12373y + bounds2.height;
                    graphics.setColor(this.shadow);
                    graphics.drawLine(bounds2.f12372x - 1, 0, i6, 0);
                    graphics.setColor(this.highlight);
                    graphics.drawLine(bounds2.f12372x - 1, i7, i6, i7);
                    graphics.drawLine(i6, 0, i6, i7 + 1);
                    return;
                }
                return;
            }
            if (leftComponent != null) {
                Rectangle bounds3 = leftComponent.getBounds();
                graphics.setColor(this.shadow);
                graphics.drawLine(0, 0, bounds3.width + 1, 0);
                graphics.drawLine(0, 1, 0, bounds3.height);
                graphics.setColor(this.highlight);
                graphics.drawLine(1 + bounds3.width, 0, 1 + bounds3.width, bounds3.height + 1);
                graphics.drawLine(0, bounds3.height + 1, 0, bounds3.height + 1);
            }
            Component rightComponent2 = jSplitPane.getRightComponent();
            if (rightComponent2 != null) {
                Rectangle bounds4 = rightComponent2.getBounds();
                int i8 = bounds4.f12372x + bounds4.width;
                int i9 = bounds4.f12373y + bounds4.height;
                graphics.setColor(this.shadow);
                graphics.drawLine(0, bounds4.f12373y - 1, 0, i9);
                graphics.drawLine(i8, bounds4.f12373y - 1, i8, bounds4.f12373y - 1);
                graphics.setColor(this.highlight);
                graphics.drawLine(0, i9, bounds4.width + 1, i9);
                graphics.drawLine(i8, bounds4.f12373y, i8, i9);
            }
        }

        @Override // javax.swing.border.Border
        public Insets getBorderInsets(Component component) {
            return new Insets(1, 1, 1, 1);
        }

        @Override // javax.swing.border.Border
        public boolean isBorderOpaque() {
            return true;
        }
    }
}
