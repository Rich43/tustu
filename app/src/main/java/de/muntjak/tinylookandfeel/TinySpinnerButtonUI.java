package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.ColorRoutines;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.plaf.ComponentUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySpinnerButtonUI.class */
public class TinySpinnerButtonUI extends TinyButtonUI {
    private int orientation;
    private static final HashMap cache = new HashMap();
    protected static final Dimension xpSize = new Dimension(15, 8);

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySpinnerButtonUI$ButtonKey.class */
    private static class ButtonKey {
        private Color background;
        private Color parentBackground;
        private Dimension size;
        private boolean rollover;
        private int orientation;

        ButtonKey(Color color, Color color2, Dimension dimension, boolean z2, int i2) {
            this.background = color;
            this.parentBackground = color2;
            this.size = dimension;
            this.rollover = z2;
            this.orientation = i2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ButtonKey)) {
                return false;
            }
            ButtonKey buttonKey = (ButtonKey) obj;
            return this.orientation == buttonKey.orientation && this.rollover == buttonKey.rollover && this.background.equals(buttonKey.background) && this.parentBackground.equals(buttonKey.parentBackground) && this.size.equals(buttonKey.size);
        }

        public int hashCode() {
            return this.background.hashCode() * this.parentBackground.hashCode() * this.size.hashCode() * this.orientation * (this.rollover ? 2 : 1);
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    public static Container getSpinnerParent(Component component) {
        Container parent = component.getParent();
        boolean z2 = false;
        while (parent != null) {
            if (parent instanceof JSpinner) {
                z2 = true;
            }
            parent = parent.getParent();
            if (z2) {
                return parent;
            }
        }
        return null;
    }

    public static JSpinner getSpinner(Component component) {
        return (JSpinner) component.getParent();
    }

    public static ComponentUI createUI(JComponent jComponent) {
        throw new IllegalStateException("Must not be used this way.");
    }

    TinySpinnerButtonUI(int i2) {
        this.orientation = i2;
    }

    @Override // de.muntjak.tinylookandfeel.TinyButtonUI, javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        if (!abstractButton.isEnabled()) {
            graphics.setColor(Theme.spinnerButtDisabledColor.getColor());
        } else if (abstractButton.getModel().isPressed()) {
            if (abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed()) {
                graphics.setColor(Theme.spinnerButtPressedColor.getColor());
            } else {
                graphics.setColor(Theme.spinnerButtColor.getColor());
            }
        } else if (abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed()) {
            graphics.setColor(Theme.spinnerButtRolloverColor.getColor());
        } else {
            graphics.setColor(Theme.spinnerButtColor.getColor());
        }
        if (TinyLookAndFeel.controlPanelInstantiated) {
            drawXpButtonNoCache(graphics, abstractButton);
        } else {
            drawXpButton(graphics, abstractButton);
        }
    }

    private void drawXpButton(Graphics graphics, AbstractButton abstractButton) {
        boolean z2 = !abstractButton.getModel().isPressed() && abstractButton.getModel().isRollover() && Theme.spinnerRollover.getValue();
        Color background = getSpinnerParent(abstractButton).getBackground();
        ButtonKey buttonKey = new ButtonKey(graphics.getColor(), background, abstractButton.getSize(), z2, this.orientation);
        Object obj = cache.get(buttonKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, 0, 0, abstractButton);
            return;
        }
        int width = abstractButton.getWidth() - 1;
        int height = abstractButton.getHeight() - 1;
        int height2 = abstractButton.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width + 1, height2, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        int value = Theme.spinnerSpreadLight.getValue();
        int value2 = Theme.spinnerSpreadDark.getValue();
        if (!abstractButton.isEnabled()) {
            value = Theme.spinnerSpreadLightDisabled.getValue();
            value2 = Theme.spinnerSpreadDarkDisabled.getValue();
        }
        float f2 = (10.0f * value) / (height2 - 2);
        float f3 = (10.0f * value2) / (height2 - 2);
        int i2 = height2 / 2;
        Color color = graphics.getColor();
        for (int i3 = 1; i3 < height2 - 1; i3++) {
            if (i3 < i2) {
                graphics2.setColor(ColorRoutines.lighten(color, (int) ((i2 - i3) * f2)));
            } else if (i3 == i2) {
                graphics2.setColor(color);
            } else {
                graphics2.setColor(ColorRoutines.darken(color, (int) ((i3 - i2) * f3)));
            }
            graphics2.drawLine(1, i3, width, i3);
        }
        graphics2.setColor(background);
        graphics2.drawRect(0, 0, width, height);
        graphics2.setColor(getSpinner(abstractButton).getBackground());
        if (Boolean.TRUE.equals(abstractButton.getClientProperty("isNextButton"))) {
            graphics2.drawLine(0, height, 0, height);
        } else {
            graphics2.drawLine(0, 0, 0, 0);
        }
        if (abstractButton.isEnabled()) {
            graphics2.setColor(Theme.spinnerBorderColor.getColor());
        } else {
            graphics2.setColor(Theme.spinnerBorderDisabledColor.getColor());
        }
        graphics2.drawLine(1, 0, width - 1, 0);
        graphics2.drawLine(1, height, width - 1, height);
        graphics2.drawLine(0, 1, 0, height - 1);
        graphics2.drawLine(width, 1, width, height - 1);
        if (z2) {
            DrawRoutines.drawRolloverBorder(graphics2, Theme.buttonRolloverColor.getColor(), 0, 0, width + 1, height2);
        }
        if (abstractButton.isEnabled()) {
            graphics2.setColor(Theme.spinnerArrowColor.getColor());
        } else {
            graphics2.setColor(Theme.spinnerArrowDisabledColor.getColor());
        }
        drawXpArrow(graphics2, abstractButton);
        graphics2.dispose();
        graphics.drawImage(bufferedImage, 0, 0, abstractButton);
        cache.put(buttonKey, bufferedImage);
    }

    private void drawXpButtonNoCache(Graphics graphics, AbstractButton abstractButton) {
        boolean z2 = !abstractButton.getModel().isPressed() && abstractButton.getModel().isRollover() && Theme.spinnerRollover.getValue();
        int width = abstractButton.getWidth() - 1;
        int height = (abstractButton.getHeight() - 1) + 1;
        int value = Theme.spinnerSpreadLight.getValue();
        int value2 = Theme.spinnerSpreadDark.getValue();
        if (!abstractButton.isEnabled()) {
            value = Theme.spinnerSpreadLightDisabled.getValue();
            value2 = Theme.spinnerSpreadDarkDisabled.getValue();
        }
        float f2 = (10.0f * value) / (height - 2);
        float f3 = (10.0f * value2) / (height - 2);
        int i2 = height / 2;
        Color color = graphics.getColor();
        for (int i3 = 1; i3 < height - 1; i3++) {
            if (i3 < i2) {
                graphics.setColor(ColorRoutines.lighten(color, (int) ((i2 - i3) * f2)));
            } else if (i3 == i2) {
                graphics.setColor(color);
            } else {
                graphics.setColor(ColorRoutines.darken(color, (int) ((i3 - i2) * f3)));
            }
            graphics.drawLine(1, i3, width, i3);
        }
        if (abstractButton.isEnabled()) {
            graphics.setColor(Theme.spinnerArrowColor.getColor());
        } else {
            graphics.setColor(Theme.spinnerArrowDisabledColor.getColor());
        }
        drawXpArrow(graphics, abstractButton);
    }

    private void drawXpArrow(Graphics graphics, AbstractButton abstractButton) {
        int i2 = (abstractButton.getSize().height - 6) / 2;
        switch (this.orientation) {
            case 1:
                int i3 = i2 - 1;
                graphics.drawLine(7, i3 + 2, 7, i3 + 2);
                graphics.drawLine(6, i3 + 3, 8, i3 + 3);
                graphics.drawLine(5, i3 + 4, 9, i3 + 4);
                graphics.drawLine(4, i3 + 5, 6, i3 + 5);
                graphics.drawLine(8, i3 + 5, 10, i3 + 5);
                break;
            case 5:
                graphics.drawLine(4, i2 + 2, 6, i2 + 2);
                graphics.drawLine(8, i2 + 2, 10, i2 + 2);
                graphics.drawLine(5, i2 + 3, 9, i2 + 3);
                graphics.drawLine(6, i2 + 4, 8, i2 + 4);
                graphics.drawLine(7, i2 + 5, 7, i2 + 5);
                break;
        }
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return xpSize;
    }
}
