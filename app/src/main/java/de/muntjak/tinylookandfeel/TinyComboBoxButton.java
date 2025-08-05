package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyButtonBorder;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.CellRendererPane;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyComboBoxButton.class */
public class TinyComboBoxButton extends JButton {
    private static final HashMap cache = new HashMap();
    protected JComboBox comboBox;
    protected JList listBox;
    protected CellRendererPane rendererPane;
    protected Icon comboIcon;
    protected boolean iconOnly;
    private static BufferedImage focusImg;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyComboBoxButton$ButtonKey.class */
    private static class ButtonKey {
        private Color panelBackground;
        private Dimension size;
        private boolean enabled;
        private boolean editable;
        private boolean pressed;
        private boolean rollover;

        ButtonKey(Color color, Dimension dimension, boolean z2, boolean z3, boolean z4, boolean z5) {
            this.panelBackground = color;
            this.size = dimension;
            this.enabled = z2;
            this.editable = z3;
            this.pressed = z4;
            this.rollover = z5;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ButtonKey)) {
                return false;
            }
            ButtonKey buttonKey = (ButtonKey) obj;
            return this.enabled == buttonKey.enabled && this.editable == buttonKey.editable && this.pressed == buttonKey.pressed && this.rollover == buttonKey.rollover && this.panelBackground.equals(buttonKey.panelBackground) && this.size.equals(buttonKey.size);
        }

        public int hashCode() {
            return this.panelBackground.hashCode() * this.size.hashCode() * (this.enabled ? 2 : 1) * (this.editable ? 8 : 4) * (this.pressed ? 32 : 16) * (this.rollover ? 128 : 64);
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    public final JComboBox getComboBox() {
        return this.comboBox;
    }

    public final void setComboBox(JComboBox jComboBox) {
        this.comboBox = jComboBox;
    }

    public final Icon getComboIcon() {
        return this.comboIcon;
    }

    public final void setComboIcon(Icon icon) {
        this.comboIcon = icon;
    }

    public final boolean isIconOnly() {
        return this.iconOnly;
    }

    public final void setIconOnly(boolean z2) {
        this.iconOnly = z2;
    }

    TinyComboBoxButton() {
        ImageIcon imageIconLoadIcon;
        super("");
        this.iconOnly = false;
        setModel(new DefaultButtonModel(this) { // from class: de.muntjak.tinylookandfeel.TinyComboBoxButton.1
            private final TinyComboBoxButton this$0;

            {
                this.this$0 = this;
            }

            @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
            public void setArmed(boolean z2) {
                super.setArmed(isPressed() ? true : z2);
            }
        });
        setBackground(UIManager.getColor("ComboBox.background"));
        setForeground(UIManager.getColor("ComboBox.foreground"));
        if (focusImg != null || (imageIconLoadIcon = TinyLookAndFeel.loadIcon("ComboBoxFocus.png")) == null) {
            return;
        }
        focusImg = new BufferedImage(2, 2, 1);
        imageIconLoadIcon.paintIcon(this, focusImg.getGraphics(), 0, 0);
    }

    public TinyComboBoxButton(JComboBox jComboBox, Icon icon, boolean z2, CellRendererPane cellRendererPane, JList jList) {
        this();
        this.comboBox = jComboBox;
        this.comboIcon = icon;
        this.rendererPane = cellRendererPane;
        this.listBox = jList;
        setEnabled(this.comboBox.isEnabled());
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        Color background = getParent().getParent().getBackground();
        int height = getHeight();
        int width = getWidth();
        ButtonKey buttonKey = null;
        BufferedImage bufferedImage = null;
        Graphics graphics2 = graphics;
        boolean z2 = false;
        if (!TinyLookAndFeel.controlPanelInstantiated) {
            buttonKey = new ButtonKey(background, getSize(), this.comboBox.isEnabled(), this.comboBox.isEditable(), this.model.isPressed(), this.model.isRollover());
            Object obj = cache.get(buttonKey);
            if (obj != null) {
                graphics.drawImage((Image) obj, 0, 0, this);
                if (this.comboBox.isEditable()) {
                    return;
                } else {
                    z2 = true;
                }
            } else {
                bufferedImage = new BufferedImage(width, height, 2);
                graphics2 = bufferedImage.getGraphics();
            }
        }
        boolean zIsLeftToRight = getComponentOrientation().isLeftToRight();
        if (!z2) {
            if (!this.comboBox.isEnabled()) {
                graphics2.setColor(Theme.textDisabledBgColor.getColor());
            } else if (this.comboBox.isEditable()) {
                graphics2.setColor(Theme.textBgColor.getColor());
            } else {
                graphics2.setColor(this.comboBox.getBackground());
            }
            graphics2.fillRect(1, 1, width - 2, height - 2);
            graphics2.setColor(background);
            graphics2.drawRect(0, 0, width - 1, height - 1);
            ColorUIResource color = !isEnabled() ? Theme.comboButtDisabledColor.getColor() : this.model.isPressed() ? Theme.comboButtPressedColor.getColor() : this.model.isRollover() ? Theme.comboButtRolloverColor.getColor() : Theme.comboButtColor.getColor();
            graphics2.setColor(color);
            drawXpButton(graphics2, new Rectangle(width - 18, 1, 18, height - 2), color);
            Border border = getBorder();
            if (border != null && (border instanceof TinyButtonBorder.CompoundBorderUIResource)) {
                if (isEnabled()) {
                    DrawRoutines.drawRoundedBorder(graphics2, Theme.comboBorderColor.getColor(), 0, 0, width, height);
                    if (!getModel().isPressed() && getModel().isRollover() && Theme.comboRollover.getValue()) {
                        DrawRoutines.drawRolloverBorder(graphics2, Theme.buttonRolloverColor.getColor(), 0, 0, width, height);
                    }
                } else {
                    DrawRoutines.drawRoundedBorder(graphics2, Theme.comboBorderDisabledColor.getColor(), 0, 0, width, height);
                }
            }
            if (buttonKey != null) {
                graphics2.dispose();
                graphics.drawImage(bufferedImage, 0, 0, this);
                cache.put(buttonKey, bufferedImage);
            }
        }
        Insets insets = new Insets(Theme.comboInsets.top, Theme.comboInsets.left, Theme.comboInsets.bottom, 0);
        int i2 = width - (insets.left + insets.right);
        int i3 = height - (insets.top + insets.bottom);
        if (i3 <= 0 || i2 <= 0) {
            return;
        }
        int i4 = insets.left;
        int i5 = insets.top;
        int i6 = i4 + (i2 - 1);
        int i7 = i5 + (i3 - 1);
        int i8 = zIsLeftToRight ? i6 : i4;
        Component listCellRendererComponent = null;
        boolean z3 = false;
        boolean zIsOpaque = false;
        boolean z4 = false;
        if (!this.iconOnly && this.comboBox != null) {
            listCellRendererComponent = this.comboBox.getRenderer().getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, getModel().isPressed(), false);
            listCellRendererComponent.setFont(this.rendererPane.getFont());
            if (this.model.isArmed() && this.model.isPressed()) {
                if (isOpaque()) {
                    listCellRendererComponent.setBackground(UIManager.getColor("Button.select"));
                }
                listCellRendererComponent.setForeground(this.comboBox.getForeground());
            } else if (!this.comboBox.isEnabled()) {
                if (isOpaque()) {
                    listCellRendererComponent.setBackground(Theme.textDisabledBgColor.getColor());
                } else {
                    this.comboBox.setBackground(Theme.textDisabledBgColor.getColor());
                }
                listCellRendererComponent.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
            } else if (!this.comboBox.hasFocus() || this.comboBox.isPopupVisible()) {
                listCellRendererComponent.setForeground(this.comboBox.getForeground());
                listCellRendererComponent.setBackground(this.comboBox.getBackground());
            } else {
                if (this.comboBox.isEditable()) {
                    listCellRendererComponent.setForeground(Theme.mainColor.getColor());
                } else {
                    listCellRendererComponent.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
                }
                listCellRendererComponent.setBackground(UIManager.getColor("ComboBox.focusBackground"));
                if (listCellRendererComponent instanceof JComponent) {
                    z3 = true;
                    JComponent jComponent = (JComponent) listCellRendererComponent;
                    zIsOpaque = jComponent.isOpaque();
                    jComponent.setOpaque(true);
                    z4 = true;
                }
            }
            int i9 = i2 - (insets.right + 18);
            boolean z5 = listCellRendererComponent instanceof JPanel;
            if (zIsLeftToRight) {
                this.rendererPane.paintComponent(graphics, listCellRendererComponent, this, i4, i5, i9, i3, z5);
            } else {
                this.rendererPane.paintComponent(graphics, listCellRendererComponent, this, i4 + 18, i5, i9, i3, z5);
            }
            if (z4 && Theme.comboFocus.getValue()) {
                graphics.setColor(Color.black);
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.setPaint(new TexturePaint(focusImg, new Rectangle(i4, i5, 2, 2)));
                graphics2D.draw(new Rectangle(i4, i5, i9, i3));
            }
        }
        if (z3) {
            ((JComponent) listCellRendererComponent).setOpaque(zIsOpaque);
        }
    }

    private void drawXpButton(Graphics graphics, Rectangle rectangle, Color color) {
        int i2 = rectangle.f12372x + rectangle.width;
        int i3 = rectangle.f12373y + rectangle.height;
        int value = Theme.comboSpreadLight.getValue();
        int value2 = Theme.comboSpreadDark.getValue();
        if (!isEnabled()) {
            value = Theme.comboSpreadLightDisabled.getValue();
            value2 = Theme.comboSpreadDarkDisabled.getValue();
        }
        int i4 = rectangle.height - 2;
        float f2 = (10.0f * value) / (i4 - 3);
        float f3 = (10.0f * value2) / (i4 - 3);
        int i5 = i4 / 2;
        for (int i6 = 1; i6 < i4 - 1; i6++) {
            if (i6 < i5) {
                graphics.setColor(ColorRoutines.lighten(color, (int) ((i5 - i6) * f2)));
            } else if (i6 == i5) {
                graphics.setColor(color);
            } else {
                graphics.setColor(ColorRoutines.darken(color, (int) ((i6 - i5) * f3)));
            }
            graphics.drawLine(rectangle.f12372x + 1, rectangle.f12373y + i6 + 1, (rectangle.f12372x + rectangle.width) - 3, rectangle.f12373y + i6 + 1);
        }
        ColorUIResource color2 = !isEnabled() ? Theme.comboButtBorderDisabledColor.getColor() : Theme.comboButtBorderColor.getColor();
        graphics.setColor(color2);
        graphics.drawLine(rectangle.f12372x + 2, rectangle.f12373y + 1, i2 - 4, rectangle.f12373y + 1);
        graphics.drawLine(rectangle.f12372x + 1, rectangle.f12373y + 2, rectangle.f12372x + 1, i3 - 3);
        graphics.drawLine(i2 - 3, rectangle.f12373y + 2, i2 - 3, i3 - 3);
        graphics.drawLine(rectangle.f12372x + 2, i3 - 2, i2 - 4, i3 - 2);
        graphics.setColor(new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 128));
        graphics.drawLine(rectangle.f12372x + 1, rectangle.f12373y + 1, rectangle.f12372x + 1, rectangle.f12373y + 1);
        graphics.drawLine(i2 - 3, rectangle.f12373y + 1, i2 - 3, rectangle.f12373y + 1);
        graphics.drawLine(rectangle.f12372x + 1, i3 - 2, rectangle.f12372x + 1, i3 - 2);
        graphics.drawLine(i2 - 3, i3 - 2, i2 - 3, i3 - 2);
        if (isEnabled()) {
            graphics.setColor(Theme.comboArrowColor.getColor());
        } else {
            graphics.setColor(Theme.comboArrowDisabledColor.getColor());
        }
        drawXpArrow(graphics, rectangle);
    }

    private void drawXpArrow(Graphics graphics, Rectangle rectangle) {
        int i2 = (rectangle.f12372x + ((rectangle.width - 8) / 2)) - 1;
        int i3 = rectangle.f12373y + ((rectangle.height - 6) / 2) + 1;
        graphics.drawLine(i2 + 1, i3, i2 + 1, i3);
        graphics.drawLine(i2 + 7, i3, i2 + 7, i3);
        graphics.drawLine(i2, i3 + 1, i2 + 2, i3 + 1);
        graphics.drawLine(i2 + 6, i3 + 1, i2 + 8, i3 + 1);
        graphics.drawLine(i2 + 1, i3 + 2, i2 + 3, i3 + 2);
        graphics.drawLine(i2 + 5, i3 + 2, i2 + 7, i3 + 2);
        graphics.drawLine(i2 + 2, i3 + 3, i2 + 6, i3 + 3);
        graphics.drawLine(i2 + 3, i3 + 4, i2 + 5, i3 + 4);
        graphics.drawLine(i2 + 4, i3 + 5, i2 + 4, i3 + 5);
    }
}
