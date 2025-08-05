package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyFileChooserUI;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyToolButtonBorder.class */
public class TinyToolButtonBorder extends AbstractBorder {
    protected static final Insets insets = new Insets(1, 1, 1, 1);

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        drawXpBorder(component, graphics, i2, i3, i4, i5);
    }

    private void drawXpBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        ColorUIResource color;
        AbstractButton abstractButton = (AbstractButton) component;
        boolean zEquals = Boolean.TRUE.equals(abstractButton.getClientProperty(TinyFileChooserUI.IS_FILE_CHOOSER_BUTTON_KEY));
        boolean z2 = abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed();
        if (abstractButton.getModel().isPressed()) {
            if (z2) {
                color = Theme.toolBorderPressedColor.getColor();
            } else if (abstractButton.isSelected()) {
                color = Theme.toolBorderSelectedColor.getColor();
            } else if (zEquals) {
                return;
            } else {
                color = Theme.toolBorderColor.getColor();
            }
        } else if (z2) {
            color = abstractButton.isSelected() ? Theme.toolBorderSelectedColor.getColor() : Theme.toolBorderRolloverColor.getColor();
        } else if (abstractButton.isSelected()) {
            color = Theme.toolBorderSelectedColor.getColor();
        } else if (zEquals) {
            return;
        } else {
            color = Theme.toolBorderColor.getColor();
        }
        DrawRoutines.drawRoundedBorder(graphics, color, i2, i3, i4, i5);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        if (!(component instanceof AbstractButton)) {
            return insets;
        }
        AbstractButton abstractButton = (AbstractButton) component;
        if (abstractButton.getMargin() == null || (abstractButton.getMargin() instanceof UIResource)) {
            return Theme.toolMargin;
        }
        Insets margin = abstractButton.getMargin();
        return new Insets(margin.top + 1, margin.left + 1, margin.bottom + 1, margin.right + 1);
    }
}
