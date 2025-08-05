package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.JTextComponent;
import sun.swing.StringUIClientPropertyKey;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders.class */
public class MetalBorders {
    static Object NO_BUTTON_ROLLOVER = new StringUIClientPropertyKey("NoButtonRollover");
    private static Border buttonBorder;
    private static Border textBorder;
    private static Border textFieldBorder;
    private static Border toggleButtonBorder;

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$Flush3DBorder.class */
    public static class Flush3DBorder extends AbstractBorder implements UIResource {
        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (component.isEnabled()) {
                MetalUtils.drawFlush3DBorder(graphics, i2, i3, i4, i5);
            } else {
                MetalUtils.drawDisabledBorder(graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 2, 2, 2);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$ButtonBorder.class */
    public static class ButtonBorder extends AbstractBorder implements UIResource {
        protected static Insets borderInsets = new Insets(3, 3, 3, 3);

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof AbstractButton)) {
                return;
            }
            if (MetalLookAndFeel.usingOcean()) {
                paintOceanBorder(component, graphics, i2, i3, i4, i5);
                return;
            }
            AbstractButton abstractButton = (AbstractButton) component;
            ButtonModel model = abstractButton.getModel();
            if (model.isEnabled()) {
                boolean z2 = model.isPressed() && model.isArmed();
                boolean z3 = (abstractButton instanceof JButton) && ((JButton) abstractButton).isDefaultButton();
                if (z2 && z3) {
                    MetalUtils.drawDefaultButtonPressedBorder(graphics, i2, i3, i4, i5);
                    return;
                }
                if (z2) {
                    MetalUtils.drawPressed3DBorder(graphics, i2, i3, i4, i5);
                    return;
                } else if (z3) {
                    MetalUtils.drawDefaultButtonBorder(graphics, i2, i3, i4, i5, false);
                    return;
                } else {
                    MetalUtils.drawButtonBorder(graphics, i2, i3, i4, i5, false);
                    return;
                }
            }
            MetalUtils.drawDisabledBorder(graphics, i2, i3, i4 - 1, i5 - 1);
        }

        private void paintOceanBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            AbstractButton abstractButton = (AbstractButton) component;
            ButtonModel model = ((AbstractButton) component).getModel();
            graphics.translate(i2, i3);
            if (MetalUtils.isToolBarButton(abstractButton)) {
                if (model.isEnabled()) {
                    if (model.isPressed()) {
                        graphics.setColor(MetalLookAndFeel.getWhite());
                        graphics.fillRect(1, i5 - 1, i4 - 1, 1);
                        graphics.fillRect(i4 - 1, 1, 1, i5 - 1);
                        graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                        graphics.drawRect(0, 0, i4 - 2, i5 - 2);
                        graphics.fillRect(1, 1, i4 - 3, 1);
                        return;
                    }
                    if (model.isSelected() || model.isRollover()) {
                        graphics.setColor(MetalLookAndFeel.getWhite());
                        graphics.fillRect(1, i5 - 1, i4 - 1, 1);
                        graphics.fillRect(i4 - 1, 1, 1, i5 - 1);
                        graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                        graphics.drawRect(0, 0, i4 - 2, i5 - 2);
                        return;
                    }
                    graphics.setColor(MetalLookAndFeel.getWhite());
                    graphics.drawRect(1, 1, i4 - 2, i5 - 2);
                    graphics.setColor(UIManager.getColor("Button.toolBarBorderBackground"));
                    graphics.drawRect(0, 0, i4 - 2, i5 - 2);
                    return;
                }
                graphics.setColor(UIManager.getColor("Button.disabledToolBarBorderBackground"));
                graphics.drawRect(0, 0, i4 - 2, i5 - 2);
                return;
            }
            if (model.isEnabled()) {
                boolean zIsPressed = model.isPressed();
                model.isArmed();
                if ((component instanceof JButton) && ((JButton) component).isDefaultButton()) {
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawRect(0, 0, i4 - 1, i5 - 1);
                    graphics.drawRect(1, 1, i4 - 3, i5 - 3);
                    return;
                }
                if (zIsPressed) {
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.fillRect(0, 0, i4, 2);
                    graphics.fillRect(0, 2, 2, i5 - 2);
                    graphics.fillRect(i4 - 1, 1, 1, i5 - 1);
                    graphics.fillRect(1, i5 - 1, i4 - 2, 1);
                    return;
                }
                if (model.isRollover() && abstractButton.getClientProperty(MetalBorders.NO_BUTTON_ROLLOVER) == null) {
                    graphics.setColor(MetalLookAndFeel.getPrimaryControl());
                    graphics.drawRect(0, 0, i4 - 1, i5 - 1);
                    graphics.drawRect(2, 2, i4 - 5, i5 - 5);
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawRect(1, 1, i4 - 3, i5 - 3);
                    return;
                }
                graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                graphics.drawRect(0, 0, i4 - 1, i5 - 1);
                return;
            }
            graphics.setColor(MetalLookAndFeel.getInactiveControlTextColor());
            graphics.drawRect(0, 0, i4 - 1, i5 - 1);
            if ((component instanceof JButton) && ((JButton) component).isDefaultButton()) {
                graphics.drawRect(1, 1, i4 - 3, i5 - 3);
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(3, 3, 3, 3);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$InternalFrameBorder.class */
    public static class InternalFrameBorder extends AbstractBorder implements UIResource {
        private static final int corner = 14;

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            ColorUIResource controlDarkShadow;
            ColorUIResource controlShadow;
            ColorUIResource controlInfo;
            if ((component instanceof JInternalFrame) && ((JInternalFrame) component).isSelected()) {
                controlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
                controlShadow = MetalLookAndFeel.getPrimaryControlShadow();
                controlInfo = MetalLookAndFeel.getPrimaryControlInfo();
            } else {
                controlDarkShadow = MetalLookAndFeel.getControlDarkShadow();
                controlShadow = MetalLookAndFeel.getControlShadow();
                controlInfo = MetalLookAndFeel.getControlInfo();
            }
            graphics.setColor(controlDarkShadow);
            graphics.drawLine(1, 0, i4 - 2, 0);
            graphics.drawLine(0, 1, 0, i5 - 2);
            graphics.drawLine(i4 - 1, 1, i4 - 1, i5 - 2);
            graphics.drawLine(1, i5 - 1, i4 - 2, i5 - 1);
            for (int i6 = 1; i6 < 5; i6++) {
                graphics.drawRect(i2 + i6, i3 + i6, (i4 - (i6 * 2)) - 1, (i5 - (i6 * 2)) - 1);
            }
            if ((component instanceof JInternalFrame) && ((JInternalFrame) component).isResizable()) {
                graphics.setColor(controlShadow);
                graphics.drawLine(15, 3, i4 - 14, 3);
                graphics.drawLine(3, 15, 3, i5 - 14);
                graphics.drawLine(i4 - 2, 15, i4 - 2, i5 - 14);
                graphics.drawLine(15, i5 - 2, i4 - 14, i5 - 2);
                graphics.setColor(controlInfo);
                graphics.drawLine(14, 2, (i4 - 14) - 1, 2);
                graphics.drawLine(2, 14, 2, (i5 - 14) - 1);
                graphics.drawLine(i4 - 3, 14, i4 - 3, (i5 - 14) - 1);
                graphics.drawLine(14, i5 - 3, (i4 - 14) - 1, i5 - 3);
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(5, 5, 5, 5);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$FrameBorder.class */
    static class FrameBorder extends AbstractBorder implements UIResource {
        private static final int corner = 14;

        FrameBorder() {
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            ColorUIResource controlDarkShadow;
            ColorUIResource controlShadow;
            ColorUIResource controlInfo;
            Window windowAncestor = SwingUtilities.getWindowAncestor(component);
            if (windowAncestor != null && windowAncestor.isActive()) {
                controlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
                controlShadow = MetalLookAndFeel.getPrimaryControlShadow();
                controlInfo = MetalLookAndFeel.getPrimaryControlInfo();
            } else {
                controlDarkShadow = MetalLookAndFeel.getControlDarkShadow();
                controlShadow = MetalLookAndFeel.getControlShadow();
                controlInfo = MetalLookAndFeel.getControlInfo();
            }
            graphics.setColor(controlDarkShadow);
            graphics.drawLine(i2 + 1, i3 + 0, (i2 + i4) - 2, i3 + 0);
            graphics.drawLine(i2 + 0, i3 + 1, i2 + 0, (i3 + i5) - 2);
            graphics.drawLine((i2 + i4) - 1, i3 + 1, (i2 + i4) - 1, (i3 + i5) - 2);
            graphics.drawLine(i2 + 1, (i3 + i5) - 1, (i2 + i4) - 2, (i3 + i5) - 1);
            for (int i6 = 1; i6 < 5; i6++) {
                graphics.drawRect(i2 + i6, i3 + i6, (i4 - (i6 * 2)) - 1, (i5 - (i6 * 2)) - 1);
            }
            if ((windowAncestor instanceof Frame) && ((Frame) windowAncestor).isResizable()) {
                graphics.setColor(controlShadow);
                graphics.drawLine(15, 3, i4 - 14, 3);
                graphics.drawLine(3, 15, 3, i5 - 14);
                graphics.drawLine(i4 - 2, 15, i4 - 2, i5 - 14);
                graphics.drawLine(15, i5 - 2, i4 - 14, i5 - 2);
                graphics.setColor(controlInfo);
                graphics.drawLine(14, 2, (i4 - 14) - 1, 2);
                graphics.drawLine(2, 14, 2, (i5 - 14) - 1);
                graphics.drawLine(i4 - 3, 14, i4 - 3, (i5 - 14) - 1);
                graphics.drawLine(14, i5 - 3, (i4 - 14) - 1, i5 - 3);
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(5, 5, 5, 5);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$DialogBorder.class */
    static class DialogBorder extends AbstractBorder implements UIResource {
        private static final int corner = 14;

        DialogBorder() {
        }

        protected Color getActiveBackground() {
            return MetalLookAndFeel.getPrimaryControlDarkShadow();
        }

        protected Color getActiveHighlight() {
            return MetalLookAndFeel.getPrimaryControlShadow();
        }

        protected Color getActiveShadow() {
            return MetalLookAndFeel.getPrimaryControlInfo();
        }

        protected Color getInactiveBackground() {
            return MetalLookAndFeel.getControlDarkShadow();
        }

        protected Color getInactiveHighlight() {
            return MetalLookAndFeel.getControlShadow();
        }

        protected Color getInactiveShadow() {
            return MetalLookAndFeel.getControlInfo();
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            Color inactiveBackground;
            Color inactiveHighlight;
            Color inactiveShadow;
            Window windowAncestor = SwingUtilities.getWindowAncestor(component);
            if (windowAncestor != null && windowAncestor.isActive()) {
                inactiveBackground = getActiveBackground();
                inactiveHighlight = getActiveHighlight();
                inactiveShadow = getActiveShadow();
            } else {
                inactiveBackground = getInactiveBackground();
                inactiveHighlight = getInactiveHighlight();
                inactiveShadow = getInactiveShadow();
            }
            graphics.setColor(inactiveBackground);
            graphics.drawLine(i2 + 1, i3 + 0, (i2 + i4) - 2, i3 + 0);
            graphics.drawLine(i2 + 0, i3 + 1, i2 + 0, (i3 + i5) - 2);
            graphics.drawLine((i2 + i4) - 1, i3 + 1, (i2 + i4) - 1, (i3 + i5) - 2);
            graphics.drawLine(i2 + 1, (i3 + i5) - 1, (i2 + i4) - 2, (i3 + i5) - 1);
            for (int i6 = 1; i6 < 5; i6++) {
                graphics.drawRect(i2 + i6, i3 + i6, (i4 - (i6 * 2)) - 1, (i5 - (i6 * 2)) - 1);
            }
            if ((windowAncestor instanceof Dialog) && ((Dialog) windowAncestor).isResizable()) {
                graphics.setColor(inactiveHighlight);
                graphics.drawLine(15, 3, i4 - 14, 3);
                graphics.drawLine(3, 15, 3, i5 - 14);
                graphics.drawLine(i4 - 2, 15, i4 - 2, i5 - 14);
                graphics.drawLine(15, i5 - 2, i4 - 14, i5 - 2);
                graphics.setColor(inactiveShadow);
                graphics.drawLine(14, 2, (i4 - 14) - 1, 2);
                graphics.drawLine(2, 14, 2, (i5 - 14) - 1);
                graphics.drawLine(i4 - 3, 14, i4 - 3, (i5 - 14) - 1);
                graphics.drawLine(14, i5 - 3, (i4 - 14) - 1, i5 - 3);
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(5, 5, 5, 5);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$ErrorDialogBorder.class */
    static class ErrorDialogBorder extends DialogBorder implements UIResource {
        ErrorDialogBorder() {
        }

        @Override // javax.swing.plaf.metal.MetalBorders.DialogBorder
        protected Color getActiveBackground() {
            return UIManager.getColor("OptionPane.errorDialog.border.background");
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$QuestionDialogBorder.class */
    static class QuestionDialogBorder extends DialogBorder implements UIResource {
        QuestionDialogBorder() {
        }

        @Override // javax.swing.plaf.metal.MetalBorders.DialogBorder
        protected Color getActiveBackground() {
            return UIManager.getColor("OptionPane.questionDialog.border.background");
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$WarningDialogBorder.class */
    static class WarningDialogBorder extends DialogBorder implements UIResource {
        WarningDialogBorder() {
        }

        @Override // javax.swing.plaf.metal.MetalBorders.DialogBorder
        protected Color getActiveBackground() {
            return UIManager.getColor("OptionPane.warningDialog.border.background");
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$PaletteBorder.class */
    public static class PaletteBorder extends AbstractBorder implements UIResource {
        int titleHeight = 0;

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            graphics.drawLine(0, 1, 0, i5 - 2);
            graphics.drawLine(1, i5 - 1, i4 - 2, i5 - 1);
            graphics.drawLine(i4 - 1, 1, i4 - 1, i5 - 2);
            graphics.drawLine(1, 0, i4 - 2, 0);
            graphics.drawRect(1, 1, i4 - 3, i5 - 3);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(1, 1, 1, 1);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$OptionDialogBorder.class */
    public static class OptionDialogBorder extends AbstractBorder implements UIResource {
        int titleHeight = 0;

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            Color primaryControlDarkShadow;
            graphics.translate(i2, i3);
            int iIntValue = -1;
            if (component instanceof JInternalFrame) {
                Object clientProperty = ((JInternalFrame) component).getClientProperty("JInternalFrame.messageType");
                if (clientProperty instanceof Integer) {
                    iIntValue = ((Integer) clientProperty).intValue();
                }
            }
            switch (iIntValue) {
                case -1:
                case 1:
                default:
                    primaryControlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
                    break;
                case 0:
                    primaryControlDarkShadow = UIManager.getColor("OptionPane.errorDialog.border.background");
                    break;
                case 2:
                    primaryControlDarkShadow = UIManager.getColor("OptionPane.warningDialog.border.background");
                    break;
                case 3:
                    primaryControlDarkShadow = UIManager.getColor("OptionPane.questionDialog.border.background");
                    break;
            }
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawLine(1, 0, i4 - 2, 0);
            graphics.drawLine(0, 1, 0, i5 - 2);
            graphics.drawLine(i4 - 1, 1, i4 - 1, i5 - 2);
            graphics.drawLine(1, i5 - 1, i4 - 2, i5 - 1);
            for (int i6 = 1; i6 < 3; i6++) {
                graphics.drawRect(i6, i6, (i4 - (i6 * 2)) - 1, (i5 - (i6 * 2)) - 1);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(3, 3, 3, 3);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$MenuBarBorder.class */
    public static class MenuBarBorder extends AbstractBorder implements UIResource {
        protected static Insets borderInsets = new Insets(1, 0, 1, 0);

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.translate(i2, i3);
            if (MetalLookAndFeel.usingOcean()) {
                if ((component instanceof JMenuBar) && !MetalToolBarUI.doesMenuBarBorderToolBar((JMenuBar) component)) {
                    graphics.setColor(MetalLookAndFeel.getControl());
                    SwingUtilities2.drawHLine(graphics, 0, i4 - 1, i5 - 2);
                    graphics.setColor(UIManager.getColor("MenuBar.borderColor"));
                    SwingUtilities2.drawHLine(graphics, 0, i4 - 1, i5 - 1);
                }
            } else {
                graphics.setColor(MetalLookAndFeel.getControlShadow());
                SwingUtilities2.drawHLine(graphics, 0, i4 - 1, i5 - 1);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            if (MetalLookAndFeel.usingOcean()) {
                insets.set(0, 0, 2, 0);
            } else {
                insets.set(1, 0, 1, 0);
            }
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$MenuItemBorder.class */
    public static class MenuItemBorder extends AbstractBorder implements UIResource {
        protected static Insets borderInsets = new Insets(2, 2, 2, 2);

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof JMenuItem)) {
                return;
            }
            ButtonModel model = ((JMenuItem) component).getModel();
            graphics.translate(i2, i3);
            if (component.getParent() instanceof JMenuBar) {
                if (model.isArmed() || model.isSelected()) {
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawLine(0, 0, i4 - 2, 0);
                    graphics.drawLine(0, 0, 0, i5 - 1);
                    graphics.drawLine(i4 - 2, 2, i4 - 2, i5 - 1);
                    graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
                    graphics.drawLine(i4 - 1, 1, i4 - 1, i5 - 1);
                    graphics.setColor(MetalLookAndFeel.getMenuBackground());
                    graphics.drawLine(i4 - 1, 0, i4 - 1, 0);
                }
            } else if (model.isArmed() || ((component instanceof JMenu) && model.isSelected())) {
                graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
                graphics.drawLine(0, 0, i4 - 1, 0);
                graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
                graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
            } else {
                graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
                graphics.drawLine(0, 0, 0, i5 - 1);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 2, 2, 2);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$PopupMenuBorder.class */
    public static class PopupMenuBorder extends AbstractBorder implements UIResource {
        protected static Insets borderInsets = new Insets(3, 1, 2, 1);

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            graphics.drawRect(0, 0, i4 - 1, i5 - 1);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
            graphics.drawLine(1, 1, i4 - 2, 1);
            graphics.drawLine(1, 2, 1, 2);
            graphics.drawLine(1, i5 - 2, 1, i5 - 2);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(3, 1, 2, 1);
            return insets;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$RolloverButtonBorder.class */
    public static class RolloverButtonBorder extends ButtonBorder {
        @Override // javax.swing.plaf.metal.MetalBorders.ButtonBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            ButtonModel model = ((AbstractButton) component).getModel();
            if (model.isRollover()) {
                if (!model.isPressed() || model.isArmed()) {
                    super.paintBorder(component, graphics, i2, i3, i4, i5);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$RolloverMarginBorder.class */
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

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$ToolBarBorder.class */
    public static class ToolBarBorder extends AbstractBorder implements UIResource, SwingConstants {
        protected MetalBumps bumps = new MetalBumps(10, 10, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), UIManager.getColor("ToolBar.background"));

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof JToolBar)) {
                return;
            }
            graphics.translate(i2, i3);
            if (((JToolBar) component).isFloatable()) {
                if (((JToolBar) component).getOrientation() == 0) {
                    int i6 = MetalLookAndFeel.usingOcean() ? -1 : 0;
                    this.bumps.setBumpArea(10, i5 - 4);
                    if (MetalUtils.isLeftToRight(component)) {
                        this.bumps.paintIcon(component, graphics, 2, 2 + i6);
                    } else {
                        this.bumps.paintIcon(component, graphics, i4 - 12, 2 + i6);
                    }
                } else {
                    this.bumps.setBumpArea(i4 - 4, 10);
                    this.bumps.paintIcon(component, graphics, 2, 2);
                }
            }
            if (((JToolBar) component).getOrientation() == 0 && MetalLookAndFeel.usingOcean()) {
                graphics.setColor(MetalLookAndFeel.getControl());
                graphics.drawLine(0, i5 - 2, i4, i5 - 2);
                graphics.setColor(UIManager.getColor("ToolBar.borderColor"));
                graphics.drawLine(0, i5 - 1, i4, i5 - 1);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            if (MetalLookAndFeel.usingOcean()) {
                insets.set(1, 2, 3, 2);
            } else {
                insets.right = 2;
                insets.bottom = 2;
                insets.left = 2;
                insets.top = 2;
            }
            if (!(component instanceof JToolBar)) {
                return insets;
            }
            if (((JToolBar) component).isFloatable()) {
                if (((JToolBar) component).getOrientation() == 0) {
                    if (component.getComponentOrientation().isLeftToRight()) {
                        insets.left = 16;
                    } else {
                        insets.right = 16;
                    }
                } else {
                    insets.top = 16;
                }
            }
            Insets margin = ((JToolBar) component).getMargin();
            if (margin != null) {
                insets.left += margin.left;
                insets.top += margin.top;
                insets.right += margin.right;
                insets.bottom += margin.bottom;
            }
            return insets;
        }
    }

    public static Border getButtonBorder() {
        if (buttonBorder == null) {
            buttonBorder = new BorderUIResource.CompoundBorderUIResource(new ButtonBorder(), new BasicBorders.MarginBorder());
        }
        return buttonBorder;
    }

    public static Border getTextBorder() {
        if (textBorder == null) {
            textBorder = new BorderUIResource.CompoundBorderUIResource(new Flush3DBorder(), new BasicBorders.MarginBorder());
        }
        return textBorder;
    }

    public static Border getTextFieldBorder() {
        if (textFieldBorder == null) {
            textFieldBorder = new BorderUIResource.CompoundBorderUIResource(new TextFieldBorder(), new BasicBorders.MarginBorder());
        }
        return textFieldBorder;
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$TextFieldBorder.class */
    public static class TextFieldBorder extends Flush3DBorder {
        @Override // javax.swing.plaf.metal.MetalBorders.Flush3DBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof JTextComponent)) {
                if (component.isEnabled()) {
                    MetalUtils.drawFlush3DBorder(graphics, i2, i3, i4, i5);
                    return;
                } else {
                    MetalUtils.drawDisabledBorder(graphics, i2, i3, i4, i5);
                    return;
                }
            }
            if (component.isEnabled() && ((JTextComponent) component).isEditable()) {
                MetalUtils.drawFlush3DBorder(graphics, i2, i3, i4, i5);
            } else {
                MetalUtils.drawDisabledBorder(graphics, i2, i3, i4, i5);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$ScrollPaneBorder.class */
    public static class ScrollPaneBorder extends AbstractBorder implements UIResource {
        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof JScrollPane)) {
                return;
            }
            JScrollPane jScrollPane = (JScrollPane) component;
            JViewport columnHeader = jScrollPane.getColumnHeader();
            int height = 0;
            if (columnHeader != null) {
                height = columnHeader.getHeight();
            }
            JViewport rowHeader = jScrollPane.getRowHeader();
            int width = 0;
            if (rowHeader != null) {
                width = rowHeader.getWidth();
            }
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
            graphics.drawRect(0, 0, i4 - 2, i5 - 2);
            graphics.setColor(MetalLookAndFeel.getControlHighlight());
            graphics.drawLine(i4 - 1, 1, i4 - 1, i5 - 1);
            graphics.drawLine(1, i5 - 1, i4 - 1, i5 - 1);
            graphics.setColor(MetalLookAndFeel.getControl());
            graphics.drawLine(i4 - 2, 2 + height, i4 - 2, 2 + height);
            graphics.drawLine(1 + width, i5 - 2, 1 + width, i5 - 2);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(1, 1, 2, 2);
            return insets;
        }
    }

    public static Border getToggleButtonBorder() {
        if (toggleButtonBorder == null) {
            toggleButtonBorder = new BorderUIResource.CompoundBorderUIResource(new ToggleButtonBorder(), new BasicBorders.MarginBorder());
        }
        return toggleButtonBorder;
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$ToggleButtonBorder.class */
    public static class ToggleButtonBorder extends ButtonBorder {
        @Override // javax.swing.plaf.metal.MetalBorders.ButtonBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            AbstractButton abstractButton = (AbstractButton) component;
            ButtonModel model = abstractButton.getModel();
            if (MetalLookAndFeel.usingOcean()) {
                if (model.isArmed() || !abstractButton.isEnabled()) {
                    super.paintBorder(component, graphics, i2, i3, i4, i5);
                    return;
                } else {
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawRect(0, 0, i4 - 1, i5 - 1);
                    return;
                }
            }
            if (!component.isEnabled()) {
                MetalUtils.drawDisabledBorder(graphics, i2, i3, i4 - 1, i5 - 1);
                return;
            }
            if (model.isPressed() && model.isArmed()) {
                MetalUtils.drawPressed3DBorder(graphics, i2, i3, i4, i5);
            } else if (model.isSelected()) {
                MetalUtils.drawDark3DBorder(graphics, i2, i3, i4, i5);
            } else {
                MetalUtils.drawFlush3DBorder(graphics, i2, i3, i4, i5);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalBorders$TableHeaderBorder.class */
    public static class TableHeaderBorder extends AbstractBorder {
        protected Insets editorBorderInsets = new Insets(2, 2, 2, 0);

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
            graphics.drawLine(i4 - 1, 0, i4 - 1, i5 - 1);
            graphics.drawLine(1, i5 - 1, i4 - 1, i5 - 1);
            graphics.setColor(MetalLookAndFeel.getControlHighlight());
            graphics.drawLine(0, 0, i4 - 2, 0);
            graphics.drawLine(0, 0, 0, i5 - 2);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 2, 2, 0);
            return insets;
        }
    }

    public static Border getDesktopIconBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new LineBorder(MetalLookAndFeel.getControlDarkShadow(), 1), new MatteBorder(2, 2, 1, 2, MetalLookAndFeel.getControl()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Border getToolBarRolloverBorder() {
        if (MetalLookAndFeel.usingOcean()) {
            return new CompoundBorder(new ButtonBorder(), new RolloverMarginBorder());
        }
        return new CompoundBorder(new RolloverButtonBorder(), new RolloverMarginBorder());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Border getToolBarNonrolloverBorder() {
        if (MetalLookAndFeel.usingOcean()) {
            new CompoundBorder(new ButtonBorder(), new RolloverMarginBorder());
        }
        return new CompoundBorder(new ButtonBorder(), new RolloverMarginBorder());
    }
}
