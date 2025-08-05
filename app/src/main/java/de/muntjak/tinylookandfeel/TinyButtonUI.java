package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyButtonBorder;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyButtonUI.class */
public class TinyButtonUI extends MetalButtonUI {
    public static final int BG_CHANGE_AMOUNT = 10;
    private boolean graphicsTranslated;
    private boolean isToolBarButton;
    private boolean isFileChooserButton;
    private boolean isDefaultButton;
    private static final HashMap cache = new HashMap();
    private static final TinyButtonUI buttonUI = new TinyButtonUI();
    private static final BasicStroke focusStroke = new BasicStroke(1.0f, 0, 2, 1.0f, new float[]{1.0f, 1.0f}, 0.0f);

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyButtonUI$ButtonKey.class */
    private static class ButtonKey {
        private Color background;
        private Color parentBackground;
        private int height;
        private boolean rollover;
        private boolean isDefault;
        private boolean hasFocus;
        private boolean isBorderPainted;

        ButtonKey(Color color, Color color2, int i2, boolean z2, boolean z3, boolean z4, boolean z5) {
            this.background = color;
            this.parentBackground = color2;
            this.height = i2;
            this.rollover = z2;
            this.isDefault = z3;
            this.hasFocus = z4;
            this.isBorderPainted = z5;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ButtonKey)) {
                return false;
            }
            ButtonKey buttonKey = (ButtonKey) obj;
            return this.height == buttonKey.height && this.rollover == buttonKey.rollover && this.isDefault == buttonKey.isDefault && this.hasFocus == buttonKey.hasFocus && this.isBorderPainted == buttonKey.isBorderPainted && this.background.equals(buttonKey.background) && this.parentBackground.equals(buttonKey.parentBackground);
        }

        public int hashCode() {
            return this.background.hashCode() * this.parentBackground.hashCode() * this.height * (this.rollover ? 2 : 1) * (this.isDefault ? 8 : 4) * (this.hasFocus ? 16 : 8) * (this.isBorderPainted ? 32 : 16);
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        InputMap inputMap;
        super.installUI(jComponent);
        if (Theme.buttonEnter.getValue() && jComponent.isFocusable() && (inputMap = (InputMap) UIManager.get(new StringBuffer().append(getPropertyPrefix()).append("focusInputMap").toString())) != null) {
            inputMap.put(KeyStroke.getKeyStroke(10, 0, false), "pressed");
            inputMap.put(KeyStroke.getKeyStroke(10, 0, true), "released");
        }
    }

    @Override // javax.swing.plaf.metal.MetalButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        abstractButton.setRolloverEnabled(true);
    }

    @Override // javax.swing.plaf.metal.MetalButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected void paintFocus(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
        if (this.isFileChooserButton) {
            return;
        }
        if ((!this.isToolBarButton || Theme.toolFocus.getValue()) && Theme.buttonFocus.getValue()) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Rectangle bounds = abstractButton.getBounds();
            graphics.setColor(Color.black);
            graphics2D.setStroke(focusStroke);
            int i2 = 2;
            int i3 = 2;
            int i4 = (2 + bounds.width) - 5;
            int i5 = (2 + bounds.height) - 5;
            if (!this.isToolBarButton) {
                i2 = 2 + 1;
                i3 = 2 + 1;
                i4--;
                i5--;
            }
            if (this.graphicsTranslated) {
                graphics.translate(-1, -1);
            }
            graphics2D.drawLine(i2, i3, i4, i3);
            graphics2D.drawLine(i2, i3, i2, i5);
            graphics2D.drawLine(i2, i5, i4, i5);
            graphics2D.drawLine(i4, i3, i4, i5);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return buttonUI;
    }

    @Override // javax.swing.plaf.metal.MetalButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
        if (this.isToolBarButton || this.isFileChooserButton) {
            return;
        }
        Color colorDarken = !(abstractButton.getBackground() instanceof ColorUIResource) ? ColorRoutines.darken(abstractButton.getBackground(), 10) : abstractButton instanceof JToggleButton ? Theme.toggleSelectedBg.getColor() : Theme.buttonPressedColor.getColor();
        graphics.setColor(colorDarken);
        drawXpButton(graphics, abstractButton, colorDarken, false);
        if ((abstractButton instanceof JToggleButton) || !Theme.shiftButtonText.getValue() || abstractButton.getText() == null || "".equals(abstractButton.getText())) {
            return;
        }
        graphics.translate(1, 1);
        this.graphicsTranslated = true;
    }

    public void paintToolBarButton(Graphics graphics, AbstractButton abstractButton) {
        boolean z2 = abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed();
        Color background = this.isFileChooserButton ? abstractButton.getParent().getBackground() : Theme.toolButtColor.getColor();
        Color color = abstractButton.getModel().isPressed() ? z2 ? Theme.toolButtPressedColor.getColor() : abstractButton.isSelected() ? Theme.toolButtSelectedColor.getColor() : background : z2 ? abstractButton.isSelected() ? Theme.toolButtSelectedColor.getColor() : Theme.toolButtRolloverColor.getColor() : abstractButton.isSelected() ? Theme.toolButtSelectedColor.getColor() : background;
        graphics.setColor(color);
        drawXpToolBarButton(graphics, abstractButton, color, false);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        if (this.isToolBarButton || this.isFileChooserButton) {
            paintToolBarButton(graphics, abstractButton);
        } else if ((abstractButton instanceof JToggleButton) && abstractButton.isSelected()) {
            paintButtonPressed(graphics, abstractButton);
        } else {
            this.isDefaultButton = (jComponent instanceof JButton) && ((JButton) jComponent).isDefaultButton();
            boolean zIsRollover = abstractButton.getModel().isRollover();
            boolean z2 = jComponent.getBackground().equals(Theme.buttonNormalColor.getColor()) || (jComponent.getBackground() instanceof ColorUIResource);
            Color color = !abstractButton.isEnabled() ? Theme.buttonDisabledColor.getColor() : abstractButton.getModel().isPressed() ? zIsRollover ? z2 ? Theme.buttonPressedColor.getColor() : ColorRoutines.darken(jComponent.getBackground(), 10) : jComponent.getBackground() : zIsRollover ? z2 ? Theme.buttonRolloverBgColor.getColor() : ColorRoutines.lighten(jComponent.getBackground(), 10) : z2 ? Theme.buttonNormalColor.getColor() : jComponent.getBackground();
            graphics.setColor(color);
            if (TinyLookAndFeel.controlPanelInstantiated) {
                drawXpButtonNoCache(graphics, abstractButton, color, zIsRollover);
            } else {
                drawXpButton(graphics, abstractButton, color, zIsRollover);
            }
        }
        super.paint(graphics, jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintIcon(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        if (jComponent instanceof JToggleButton) {
            paintToggleButtonIcon(graphics, jComponent, rectangle);
        } else {
            super.paintIcon(graphics, jComponent, rectangle);
        }
    }

    protected void paintToggleButtonIcon(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        Icon rolloverIcon = null;
        if (!model.isEnabled()) {
            rolloverIcon = model.isSelected() ? abstractButton.getDisabledSelectedIcon() : abstractButton.getDisabledIcon();
        } else if (model.isPressed() && model.isArmed()) {
            rolloverIcon = abstractButton.getPressedIcon();
            if (rolloverIcon == null) {
                rolloverIcon = abstractButton.getSelectedIcon();
            }
        } else if (model.isSelected()) {
            if (abstractButton.isRolloverEnabled() && model.isRollover()) {
                rolloverIcon = abstractButton.getRolloverSelectedIcon();
                if (rolloverIcon == null) {
                    rolloverIcon = abstractButton.getSelectedIcon();
                }
            } else {
                rolloverIcon = abstractButton.getSelectedIcon();
            }
        } else if (model.isRollover()) {
            rolloverIcon = abstractButton.getRolloverIcon();
        }
        if (rolloverIcon == null) {
            rolloverIcon = abstractButton.getIcon();
        }
        rolloverIcon.paintIcon(abstractButton, graphics, rectangle.f12372x, rectangle.f12373y);
    }

    @Override // javax.swing.plaf.metal.MetalButtonUI, javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        this.isToolBarButton = Boolean.TRUE.equals(jComponent.getClientProperty(TinyToolBarUI.IS_TOOL_BAR_BUTTON_KEY));
        this.isFileChooserButton = Boolean.TRUE.equals(jComponent.getClientProperty(TinyFileChooserUI.IS_FILE_CHOOSER_BUTTON_KEY));
        paint(graphics, jComponent);
        this.graphicsTranslated = false;
    }

    private void drawXpButton(Graphics graphics, AbstractButton abstractButton, Color color, boolean z2) {
        Border border;
        if (abstractButton.isContentAreaFilled() && abstractButton.isOpaque()) {
            int width = abstractButton.getWidth();
            int height = abstractButton.getHeight();
            Color background = abstractButton.getParent().getBackground();
            ButtonKey buttonKey = new ButtonKey(color, background, height, z2 & Theme.buttonRolloverBorder.getValue(), this.isDefaultButton, abstractButton.hasFocus(), abstractButton.isBorderPainted());
            Object obj = cache.get(buttonKey);
            if (obj != null) {
                int width2 = ((Image) obj).getWidth(abstractButton);
                if (width2 == width) {
                    graphics.drawImage((Image) obj, 0, 0, abstractButton);
                    return;
                }
                graphics.drawImage((Image) obj, 0, 0, 3, height, 0, 0, 3, height, abstractButton);
                graphics.drawImage((Image) obj, width - 4, 0, width, height, width2 - 4, 0, width2, height, abstractButton);
                graphics.drawImage((Image) obj, 3, 0, width - 4, height, 3, 0, width2 - 4, height, abstractButton);
                return;
            }
            BufferedImage bufferedImage = new BufferedImage(width, height, 2);
            Graphics graphics2 = bufferedImage.getGraphics();
            graphics2.setColor(background);
            graphics2.drawRect(0, 0, width - 1, height - 1);
            int value = Theme.buttonSpreadLight.getValue();
            int value2 = Theme.buttonSpreadDark.getValue();
            if (!abstractButton.isEnabled()) {
                value = Theme.buttonSpreadLightDisabled.getValue();
                value2 = Theme.buttonSpreadDarkDisabled.getValue();
            }
            float f2 = (10.0f * value) / (height - 3);
            float f3 = (10.0f * value2) / (height - 3);
            int i2 = height / 2;
            for (int i3 = 1; i3 < height - 1; i3++) {
                if (i3 < i2) {
                    graphics2.setColor(ColorRoutines.lighten(color, (int) ((i2 - i3) * f2)));
                } else if (i3 == i2) {
                    graphics2.setColor(color);
                } else {
                    graphics2.setColor(ColorRoutines.darken(color, (int) ((i3 - i2) * f3)));
                }
                graphics2.drawLine(2, i3, width - 3, i3);
                if (i3 == 1) {
                    graphics2.drawLine(1, 1, 1, height - 2);
                    if (z2 || this.isDefaultButton) {
                        graphics2.drawLine(width - 2, 1, width - 2, height - 2);
                    }
                } else if (i3 == height - 2 && !z2 && !this.isDefaultButton) {
                    graphics2.drawLine(width - 2, 1, width - 2, height - 2);
                }
            }
            if (abstractButton.isBorderPainted() && (border = abstractButton.getBorder()) != null && (border instanceof TinyButtonBorder.CompoundBorderUIResource)) {
                if (z2 && Theme.buttonRolloverBorder.getValue()) {
                    graphics2.setColor(Theme.buttonRolloverColor.getColor());
                    graphics2.drawLine(1, height - 2, 1, height - 2);
                    graphics2.drawLine(width - 2, height - 2, width - 2, height - 2);
                } else if (this.isDefaultButton && abstractButton.isEnabled()) {
                    graphics2.setColor(Theme.buttonDefaultColor.getColor());
                    graphics2.drawLine(1, height - 2, 1, height - 2);
                    graphics2.drawLine(width - 2, height - 2, width - 2, height - 2);
                }
                drawXpBorder(abstractButton, graphics2, 0, 0, width, height);
            }
            graphics2.dispose();
            graphics.drawImage(bufferedImage, 0, 0, abstractButton);
            cache.put(buttonKey, bufferedImage);
        }
    }

    private void drawXpBorder(AbstractButton abstractButton, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (Boolean.TRUE.equals(abstractButton.getClientProperty("isComboBoxButton"))) {
            if (!abstractButton.isEnabled()) {
                DrawRoutines.drawRoundedBorder(graphics, Theme.comboBorderDisabledColor.getColor(), i2, i3, i4, i5);
                return;
            }
            DrawRoutines.drawRoundedBorder(graphics, Theme.comboBorderColor.getColor(), i2, i3, i4, i5);
            if (!abstractButton.getModel().isPressed() && abstractButton.getModel().isRollover() && Theme.comboRollover.getValue()) {
                DrawRoutines.drawRolloverBorder(graphics, Theme.buttonRolloverColor.getColor(), i2, i3, i4, i5);
                return;
            }
            return;
        }
        boolean z2 = (abstractButton instanceof JButton) && ((JButton) abstractButton).isDefaultButton();
        boolean zEquals = Boolean.TRUE.equals(abstractButton.getClientProperty("isSpinnerButton"));
        boolean z3 = (zEquals && Theme.spinnerRollover.getValue()) || (!zEquals && Theme.buttonRolloverBorder.getValue());
        boolean z4 = z2 || (zEquals && Theme.buttonFocusBorder.getValue() && abstractButton.isFocusOwner()) || (!zEquals && Theme.buttonFocusBorder.getValue() && abstractButton.isFocusOwner() && abstractButton.isFocusPainted());
        if (!abstractButton.isEnabled()) {
            DrawRoutines.drawRoundedBorder(graphics, Theme.buttonBorderDisabledColor.getColor(), i2, i3, i4, i5);
            return;
        }
        DrawRoutines.drawRoundedBorder(graphics, Theme.buttonBorderColor.getColor(), i2, i3, i4, i5);
        if (abstractButton.getModel().isPressed()) {
            return;
        }
        if (abstractButton.getModel().isRollover() && z3) {
            DrawRoutines.drawRolloverBorder(graphics, Theme.buttonRolloverColor.getColor(), i2, i3, i4, i5);
        } else if (z4) {
            DrawRoutines.drawRolloverBorder(graphics, Theme.buttonDefaultColor.getColor(), i2, i3, i4, i5);
        }
    }

    private void drawXpButtonNoCache(Graphics graphics, AbstractButton abstractButton, Color color, boolean z2) {
        if (abstractButton.isContentAreaFilled() && abstractButton.isOpaque()) {
            int width = abstractButton.getWidth();
            int height = abstractButton.getHeight();
            graphics.setColor(abstractButton.getParent().getBackground());
            graphics.drawRect(0, 0, width - 1, height - 1);
            int value = Theme.buttonSpreadLight.getValue();
            int value2 = Theme.buttonSpreadDark.getValue();
            if (!abstractButton.isEnabled()) {
                value = Theme.buttonSpreadLightDisabled.getValue();
                value2 = Theme.buttonSpreadDarkDisabled.getValue();
            }
            float f2 = (10.0f * value) / (height - 3);
            float f3 = (10.0f * value2) / (height - 3);
            int i2 = height / 2;
            for (int i3 = 1; i3 < height - 1; i3++) {
                if (i3 < i2) {
                    graphics.setColor(ColorRoutines.lighten(color, (int) ((i2 - i3) * f2)));
                } else if (i3 == i2) {
                    graphics.setColor(color);
                } else {
                    graphics.setColor(ColorRoutines.darken(color, (int) ((i3 - i2) * f3)));
                }
                graphics.drawLine(2, i3, width - 3, i3);
                if (i3 == 1) {
                    graphics.drawLine(1, 1, 1, height - 2);
                    if (z2 || this.isDefaultButton) {
                        graphics.drawLine(width - 2, 1, width - 2, height - 2);
                    }
                } else if (i3 == height - 2 && !z2 && !this.isDefaultButton) {
                    graphics.drawLine(width - 2, 1, width - 2, height - 2);
                }
            }
            if (abstractButton.isBorderPainted()) {
                if (z2 && Theme.buttonRolloverBorder.getValue()) {
                    graphics.setColor(Theme.buttonRolloverColor.getColor());
                    graphics.drawLine(1, height - 2, 1, height - 2);
                    graphics.drawLine(width - 2, height - 2, width - 2, height - 2);
                } else if (this.isDefaultButton && abstractButton.isEnabled()) {
                    graphics.setColor(Theme.buttonDefaultColor.getColor());
                    graphics.drawLine(1, height - 2, 1, height - 2);
                    graphics.drawLine(width - 2, height - 2, width - 2, height - 2);
                }
            }
        }
    }

    private void drawXpToolBarButton(Graphics graphics, AbstractButton abstractButton, Color color, boolean z2) {
        int width = abstractButton.getWidth();
        int height = abstractButton.getHeight();
        if (abstractButton.isContentAreaFilled()) {
            graphics.fillRect(1, 1, width - 2, height - 2);
        }
        Color background = abstractButton.getParent().getBackground();
        if (!(background instanceof ColorUIResource) || this.isFileChooserButton) {
            graphics.setColor(background);
        } else {
            graphics.setColor(Theme.toolBarColor.getColor());
        }
        graphics.drawRect(0, 0, width - 1, height - 1);
    }
}
