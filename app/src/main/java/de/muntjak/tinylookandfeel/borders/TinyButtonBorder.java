package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;
import de.muntjak.tinylookandfeel.TinySpinnerButtonUI;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyButtonBorder.class */
public class TinyButtonBorder extends AbstractBorder implements UIResource {
    protected final Insets borderInsets = new Insets(2, 2, 2, 2);

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyButtonBorder$CompoundBorderUIResource.class */
    public static class CompoundBorderUIResource extends CompoundBorder implements UIResource {
        public CompoundBorderUIResource(Border border, Border border2) {
            super(border, border2);
        }
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (TinyLookAndFeel.controlPanelInstantiated) {
            AbstractButton abstractButton = (AbstractButton) component;
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
            boolean zEquals = Boolean.TRUE.equals(abstractButton.getClientProperty("isSpinnerButton"));
            boolean z2 = (zEquals && Theme.spinnerRollover.getValue()) || (!zEquals && Theme.buttonRolloverBorder.getValue());
            if (!zEquals) {
                boolean z3 = (component instanceof JButton) && ((JButton) component).isDefaultButton();
                if (!abstractButton.isEnabled()) {
                    DrawRoutines.drawRoundedBorder(graphics, Theme.buttonBorderDisabledColor.getColor(), i2, i3, i4, i5);
                    return;
                }
                DrawRoutines.drawRoundedBorder(graphics, Theme.buttonBorderColor.getColor(), i2, i3, i4, i5);
                if (abstractButton.getModel().isPressed()) {
                    return;
                }
                if (abstractButton.getModel().isRollover() && z2) {
                    DrawRoutines.drawRolloverBorder(graphics, Theme.buttonRolloverColor.getColor(), i2, i3, i4, i5);
                    return;
                } else {
                    if (z3 || (Theme.buttonFocusBorder.getValue() && abstractButton.isFocusOwner() && abstractButton.isFocusPainted())) {
                        DrawRoutines.drawRolloverBorder(graphics, Theme.buttonDefaultColor.getColor(), i2, i3, i4, i5);
                        return;
                    }
                    return;
                }
            }
            graphics.setColor(TinySpinnerButtonUI.getSpinnerParent(abstractButton).getBackground());
            graphics.drawRect(0, 0, i4 - 1, i5 - 1);
            graphics.setColor(TinySpinnerButtonUI.getSpinner(abstractButton).getBackground());
            if (Boolean.TRUE.equals(abstractButton.getClientProperty("isNextButton"))) {
                graphics.drawLine(0, i5 - 1, 0, i5 - 1);
            } else {
                graphics.drawLine(0, 0, 0, 0);
            }
            if (abstractButton.isEnabled()) {
                graphics.setColor(Theme.spinnerBorderColor.getColor());
            } else {
                graphics.setColor(Theme.spinnerBorderDisabledColor.getColor());
            }
            graphics.drawLine(i2 + 1, i3, (i2 + i4) - 2, i3);
            graphics.drawLine(i2 + 1, (i3 + i5) - 1, (i2 + i4) - 2, (i3 + i5) - 1);
            graphics.drawLine(i2, i3 + 1, i2, (i3 + i5) - 2);
            graphics.drawLine((i2 + i4) - 1, i3 + 1, (i2 + i4) - 1, (i3 + i5) - 2);
            if (!abstractButton.getModel().isPressed() && abstractButton.getModel().isRollover() && z2) {
                DrawRoutines.drawRolloverBorder(graphics, Theme.buttonRolloverColor.getColor(), i2, i3, i4, i5);
            }
        }
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return this.borderInsets;
    }
}
