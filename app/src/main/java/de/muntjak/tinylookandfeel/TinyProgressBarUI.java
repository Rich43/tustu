package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyProgressBarUI.class */
public class TinyProgressBarUI extends BasicProgressBarUI {
    private static final HashMap cache = new HashMap();
    private static final Dimension PREFERRED_YQ_HORIZONTAL = new Dimension(146, 7);
    private static final Dimension PREFERRED_YQ_VERTICAL = new Dimension(7, 146);

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyProgressBarUI$ProgressKey.class */
    private static class ProgressKey {

        /* renamed from: c, reason: collision with root package name */
        private Color f12130c;
        private boolean horizontal;
        private int size;

        ProgressKey(Color color, boolean z2, int i2) {
            this.f12130c = color;
            this.horizontal = z2;
            this.size = i2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ProgressKey)) {
                return false;
            }
            ProgressKey progressKey = (ProgressKey) obj;
            return this.size == progressKey.size && this.horizontal == progressKey.horizontal && this.f12130c.equals(progressKey.f12130c);
        }

        public int hashCode() {
            return this.f12130c.hashCode() * (this.horizontal ? 1 : 2) * this.size;
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected Dimension getPreferredInnerHorizontal() {
        return PREFERRED_YQ_HORIZONTAL;
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected Dimension getPreferredInnerVertical() {
        return PREFERRED_YQ_VERTICAL;
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyProgressBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void paintDeterminate(Graphics graphics, JComponent jComponent) {
        Insets insets = this.progressBar.getInsets();
        int width = this.progressBar.getWidth() - (insets.right + insets.left);
        int height = this.progressBar.getHeight() - (insets.top + insets.bottom);
        if (this.progressBar.getOrientation() == 0) {
            int amountFull = getAmountFull(insets, width, height);
            drawXpHorzProgress(graphics, insets.left, insets.top, width, height, amountFull);
            if (this.progressBar.isStringPainted()) {
                graphics.setFont(jComponent.getFont());
                paintString(graphics, insets.left, insets.top, width, height, amountFull, insets);
                return;
            }
            return;
        }
        int amountFull2 = getAmountFull(insets, width, height);
        drawXpVertProgress(graphics, insets.left, insets.top, width, height, amountFull2);
        if (this.progressBar.isStringPainted()) {
            graphics.setFont(jComponent.getFont());
            paintString(graphics, insets.left, insets.top, width, height, amountFull2, insets);
        }
    }

    private void drawXpHorzProgress(Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        graphics.translate(i2, i3);
        if (!this.progressBar.isOpaque()) {
            graphics.setColor(this.progressBar.getBackground());
            graphics.fillRect(0, 0, i4, i5);
        }
        ProgressKey progressKey = new ProgressKey(this.progressBar.getForeground(), true, i5);
        Object obj = cache.get(progressKey);
        if (obj == null) {
            BufferedImage bufferedImage = new BufferedImage(6, i5, 2);
            Graphics graphics2 = bufferedImage.getGraphics();
            Color foreground = this.progressBar.getForeground();
            Color colorLighten = ColorRoutines.lighten(foreground, 15);
            Color colorLighten2 = ColorRoutines.lighten(foreground, 35);
            graphics2.setColor(ColorRoutines.lighten(foreground, 60));
            graphics2.drawLine(0, 0, 5, 0);
            graphics2.drawLine(0, i5 - 1, 5, i5 - 1);
            graphics2.setColor(colorLighten2);
            graphics2.drawLine(0, 1, 5, 1);
            graphics2.drawLine(0, i5 - 2, 5, i5 - 2);
            graphics2.setColor(colorLighten);
            graphics2.drawLine(0, 2, 5, 2);
            graphics2.drawLine(0, i5 - 3, 5, i5 - 3);
            graphics2.setColor(foreground);
            graphics2.fillRect(0, 3, 6, i5 - 6);
            graphics2.dispose();
            cache.put(progressKey, bufferedImage);
            obj = bufferedImage;
        }
        for (int i7 = 0; i7 < i6; i7 += 8) {
            if (i7 + 6 > i4) {
                graphics.drawImage((Image) obj, i7, 0, i4 - i7, i5, this.progressBar);
            } else {
                graphics.drawImage((Image) obj, i7, 0, this.progressBar);
            }
        }
        graphics.translate(-i2, -i3);
    }

    private void drawXpVertProgress(Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        graphics.translate(i2, i3);
        if (!this.progressBar.isOpaque()) {
            graphics.setColor(this.progressBar.getBackground());
            graphics.fillRect(0, 0, i4, i5);
        }
        ProgressKey progressKey = new ProgressKey(this.progressBar.getForeground(), false, i4);
        Object obj = cache.get(progressKey);
        if (obj == null) {
            BufferedImage bufferedImage = new BufferedImage(i4, 6, 2);
            Graphics graphics2 = bufferedImage.getGraphics();
            Color foreground = this.progressBar.getForeground();
            Color colorLighten = ColorRoutines.lighten(foreground, 15);
            Color colorLighten2 = ColorRoutines.lighten(foreground, 35);
            graphics2.setColor(ColorRoutines.lighten(foreground, 60));
            graphics2.drawLine(0, 0, 0, 5);
            graphics2.drawLine(i4 - 1, 0, i4 - 1, 5);
            graphics2.setColor(colorLighten2);
            graphics2.drawLine(1, 0, 1, 5);
            graphics2.drawLine(i4 - 2, 0, i4 - 2, 5);
            graphics2.setColor(colorLighten);
            graphics2.drawLine(2, 0, 2, 5);
            graphics2.drawLine(i4 - 3, 0, i4 - 3, 5);
            graphics2.setColor(foreground);
            graphics2.fillRect(3, 0, i4 - 6, 6);
            graphics2.dispose();
            cache.put(progressKey, bufferedImage);
            obj = bufferedImage;
        }
        for (int i7 = 0; i7 < i6; i7 += 8) {
            if (i7 + 6 > i5) {
                graphics.drawImage((Image) obj, 0, 0, i4, i5 - i7, this.progressBar);
            } else {
                graphics.drawImage((Image) obj, 0, (i5 - i7) - 6, this.progressBar);
            }
        }
        graphics.translate(-i2, -i3);
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void paintIndeterminate(Graphics graphics, JComponent jComponent) {
        Insets insets = this.progressBar.getInsets();
        int width = this.progressBar.getWidth() - (insets.right + insets.left);
        int height = this.progressBar.getHeight() - (insets.top + insets.bottom);
        Rectangle rectangle = new Rectangle();
        try {
            rectangle = getBox(rectangle);
        } catch (NullPointerException e2) {
        }
        if (this.progressBar.getOrientation() == 0) {
            drawXpHorzProgress(graphics, insets.left, insets.top, width, height, rectangle);
        } else {
            drawXpVertProgress(graphics, insets.left, insets.top, width, height, rectangle);
        }
        if (this.progressBar.isStringPainted()) {
            if (this.progressBar.getOrientation() == 0) {
                paintString(graphics, insets.left, insets.top, width, height, rectangle.width, insets);
            } else {
                paintString(graphics, insets.left, insets.top, width, height, rectangle.height, insets);
            }
        }
    }

    private void drawXpHorzProgress(Graphics graphics, int i2, int i3, int i4, int i5, Rectangle rectangle) {
        if (!this.progressBar.isOpaque()) {
            graphics.setColor(this.progressBar.getBackground());
            graphics.fillRect(i2, i3, i4, i5);
        }
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        ProgressKey progressKey = new ProgressKey(this.progressBar.getForeground(), true, i5);
        Object obj = cache.get(progressKey);
        if (obj == null) {
            BufferedImage bufferedImage = new BufferedImage(6, i5, 2);
            Graphics graphics2 = bufferedImage.getGraphics();
            Color foreground = this.progressBar.getForeground();
            Color colorLighten = ColorRoutines.lighten(foreground, 15);
            Color colorLighten2 = ColorRoutines.lighten(foreground, 35);
            graphics2.setColor(ColorRoutines.lighten(foreground, 60));
            graphics2.drawLine(0, 0, 5, 0);
            graphics2.drawLine(0, i5 - 1, 5, i5 - 1);
            graphics2.setColor(colorLighten2);
            graphics2.drawLine(0, 1, 5, 1);
            graphics2.drawLine(0, i5 - 2, 5, i5 - 2);
            graphics2.setColor(colorLighten);
            graphics2.drawLine(0, 2, 5, 2);
            graphics2.drawLine(0, i5 - 3, 5, i5 - 3);
            graphics2.setColor(foreground);
            graphics2.fillRect(0, 3, 6, i5 - 6);
            graphics2.dispose();
            cache.put(progressKey, bufferedImage);
            obj = bufferedImage;
        }
        for (int i6 = 0; i6 + 6 < rectangle.width; i6 += 8) {
            graphics.drawImage((Image) obj, i6, 0, this.progressBar);
        }
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }

    private void drawXpVertProgress(Graphics graphics, int i2, int i3, int i4, int i5, Rectangle rectangle) {
        if (!this.progressBar.isOpaque()) {
            graphics.setColor(this.progressBar.getBackground());
            graphics.fillRect(i2, i3, i4, i5);
        }
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        ProgressKey progressKey = new ProgressKey(this.progressBar.getForeground(), false, i4);
        Object obj = cache.get(progressKey);
        if (obj == null) {
            BufferedImage bufferedImage = new BufferedImage(i4, 6, 2);
            Graphics graphics2 = bufferedImage.getGraphics();
            Color foreground = this.progressBar.getForeground();
            Color colorLighten = ColorRoutines.lighten(foreground, 15);
            Color colorLighten2 = ColorRoutines.lighten(foreground, 35);
            graphics2.setColor(ColorRoutines.lighten(foreground, 60));
            graphics2.drawLine(0, 0, 0, 5);
            graphics2.drawLine(i4 - 1, 0, i4 - 1, 5);
            graphics2.setColor(colorLighten2);
            graphics2.drawLine(1, 0, 1, 5);
            graphics2.drawLine(i4 - 2, 0, i4 - 2, 5);
            graphics2.setColor(colorLighten);
            graphics2.drawLine(2, 0, 2, 5);
            graphics2.drawLine(i4 - 3, 0, i4 - 3, 5);
            graphics2.setColor(foreground);
            graphics2.fillRect(3, 0, i4 - 6, 6);
            graphics2.dispose();
            cache.put(progressKey, bufferedImage);
            obj = bufferedImage;
        }
        for (int i6 = 0; i6 + 6 < rectangle.height; i6 += 8) {
            graphics.drawImage((Image) obj, 0, i6, this.progressBar);
        }
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected Color getSelectionForeground() {
        return Theme.progressSelectForeColor.getColor();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected Color getSelectionBackground() {
        return Theme.progressSelectBackColor.getColor();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void installDefaults() {
        LookAndFeel.installBorder(this.progressBar, "ProgressBar.border");
        LookAndFeel.installColorsAndFont(this.progressBar, "ProgressBar.background", "ProgressBar.foreground", "ProgressBar.font");
    }
}
