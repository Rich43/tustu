package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyFrameBorder.class */
public class TinyFrameBorder extends AbstractBorder implements UIResource {
    public static final int FRAME_BORDER_WIDTH = 3;
    public static final int FRAME_TITLE_HEIGHT = 29;
    public static final int FRAME_INTERNAL_TITLE_HEIGHT = 25;
    public static final int FRAME_PALETTE_TITLE_HEIGHT = 21;
    public static Color buttonUpperDisabledColor;
    public static Color buttonLowerDisabledColor;
    private static final Rectangle theRect = new Rectangle();
    private static TinyFrameBorder onlyInstance;
    private Window window;
    private int titleHeight;
    private boolean isActive;

    public static TinyFrameBorder getInstance() {
        if (onlyInstance == null) {
            onlyInstance = new TinyFrameBorder();
        }
        return onlyInstance;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        this.window = SwingUtilities.getWindowAncestor(component);
        this.isActive = this.window.isActive();
        if ((this.window instanceof JFrame) || (this.window instanceof JDialog)) {
            this.titleHeight = 29;
        } else {
            this.titleHeight = 25;
        }
        if (this.isActive) {
            graphics.setColor(Theme.frameBorderColor.getColor());
        } else {
            graphics.setColor(Theme.frameBorderDisabledColor.getColor());
        }
        drawXpBorder(graphics, i2, i3, i4, i5);
    }

    private void drawXpBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.drawLine(i2, i3 + 6, i2, (i3 + i5) - 1);
        graphics.drawLine(i2 + 2, i3 + this.titleHeight, i2 + 2, (i3 + i5) - 3);
        graphics.drawLine((i2 + i4) - 1, i3 + 6, (i2 + i4) - 1, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 3, i3 + this.titleHeight, (i2 + i4) - 3, (i3 + i5) - 3);
        graphics.drawLine(i2 + 2, (i3 + i5) - 3, (i2 + i4) - 3, (i3 + i5) - 3);
        graphics.drawLine(i2, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        if (TinyLookAndFeel.ROBOT != null) {
            int i6 = this.window.getLocationOnScreen().f12370x - 4;
            int i7 = this.window.getLocationOnScreen().f12371y;
            theRect.setBounds(i6, i7, 4, 4);
            graphics.drawImage(TinyLookAndFeel.ROBOT.createScreenCapture(theRect), i2, i3, null);
            theRect.setBounds(this.window.getLocationOnScreen().f12370x + this.window.getWidth() + 1, i7, 4, 4);
            graphics.drawImage(TinyLookAndFeel.ROBOT.createScreenCapture(theRect), (i2 + i4) - 4, i3, null);
        } else {
            graphics.setColor(Theme.backColor.getColor());
            graphics.fillRect(0, 0, i4, 3);
        }
        if (this.isActive) {
            graphics.setColor(Theme.frameCaptionColor.getColor());
        } else {
            graphics.setColor(Theme.frameCaptionDisabledColor.getColor());
        }
        graphics.drawLine(i2 + 1, i3 + this.titleHeight, i2 + 1, (i3 + i5) - 2);
        graphics.drawLine((i2 + i4) - 2, i3 + this.titleHeight, (i2 + i4) - 2, (i3 + i5) - 2);
        graphics.drawLine(i2 + 1, (i3 + i5) - 2, (i2 + i4) - 2, (i3 + i5) - 2);
        ColorUIResource color = this.isActive ? Theme.frameBorderColor.getColor() : Theme.frameBorderDisabledColor.getColor();
        graphics.setColor(ColorRoutines.getAlphaColor(color, 82));
        graphics.drawLine(i2, i3 + 3, i2, i3 + 3);
        graphics.drawLine((i2 + i4) - 1, i3 + 3, (i2 + i4) - 1, i3 + 3);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 156));
        graphics.drawLine(i2, i3 + 4, i2, i3 + 4);
        graphics.drawLine((i2 + i4) - 1, i3 + 4, (i2 + i4) - 1, i3 + 4);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 215));
        graphics.drawLine(i2, i3 + 5, i2, i3 + 5);
        graphics.drawLine((i2 + i4) - 1, i3 + 5, (i2 + i4) - 1, i3 + 5);
        ColorUIResource color2 = this.isActive ? Theme.frameCaptionColor.getColor() : Theme.frameCaptionDisabledColor.getColor();
        int value = Theme.frameSpreadDarkDisabled.getValue();
        int value2 = Theme.frameSpreadLightDisabled.getValue();
        if (this.isActive) {
            Theme.frameBorderColor.getColor();
            value = Theme.frameSpreadDark.getValue();
            value2 = Theme.frameSpreadLight.getValue();
        }
        Color colorDarken = ColorRoutines.darken(color2, 4 * value);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 139));
        graphics.drawLine(i2 + 2, 1, i2 + 2, 1);
        graphics.drawLine((i2 + i4) - 3, 1, (i2 + i4) - 3, 1);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 23));
        graphics.drawLine(i2 + 1, 1, i2 + 1, 1);
        graphics.drawLine((i2 + i4) - 2, 1, (i2 + i4) - 2, 1);
        int i8 = 1 + 1;
        Color colorDarken2 = ColorRoutines.darken(color2, 6 * value);
        graphics.setColor(colorDarken2);
        graphics.drawLine(i2 + 2, i8, i2 + 2, i8);
        graphics.drawLine((i2 + i4) - 3, i8, (i2 + i4) - 3, i8);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken2, 139));
        graphics.drawLine(i2 + 1, i8, i2 + 1, i8);
        graphics.drawLine((i2 + i4) - 2, i8, (i2 + i4) - 2, i8);
        int i9 = i8 + 1;
        graphics.setColor(color2);
        graphics.drawLine(i2 + 2, i9, i2 + 2, i9);
        graphics.drawLine((i2 + i4) - 3, i9, (i2 + i4) - 3, i9);
        graphics.setColor(ColorRoutines.darken(color2, 6 * value));
        graphics.drawLine(i2 + 1, i9, i2 + 1, i9);
        graphics.drawLine((i2 + i4) - 2, i9, (i2 + i4) - 2, i9);
        int i10 = i9 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 6 * value));
        graphics.drawLine(i2 + 1, i10, i2 + 1, i10);
        graphics.drawLine((i2 + i4) - 2, i10, (i2 + i4) - 2, i10);
        graphics.setColor(ColorRoutines.lighten(color2, 10 * value2));
        graphics.drawLine(i2 + 2, i10, i2 + 2, i10);
        graphics.drawLine((i2 + i4) - 3, i10, (i2 + i4) - 3, i10);
        int i11 = i10 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics.drawLine(i2 + 2, i11, i2 + 2, i11);
        graphics.drawLine((i2 + i4) - 3, i11, (i2 + i4) - 3, i11);
        graphics.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics.drawLine(i2 + 1, i11, i2 + 1, i11);
        graphics.drawLine((i2 + i4) - 2, i11, (i2 + i4) - 2, i11);
        int i12 = i11 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics.fillRect(i2 + 1, i12, 2, 2);
        graphics.fillRect((i2 + i4) - 3, i12, 2, 2);
        int i13 = i12 + 2;
        graphics.setColor(ColorRoutines.darken(color2, 3 * value));
        graphics.fillRect(i2 + 1, i13, 2, 4);
        graphics.fillRect((i2 + i4) - 3, i13, 2, 4);
        int i14 = i13 + 4;
        graphics.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics.fillRect(i2 + 1, i14, 2, 3);
        graphics.fillRect((i2 + i4) - 3, i14, 2, 3);
        int i15 = i14 + 3;
        graphics.setColor(ColorRoutines.darken(color2, 1 * value));
        graphics.fillRect(i2 + 1, i15, 2, 2);
        graphics.fillRect((i2 + i4) - 3, i15, 2, 2);
        int i16 = i15 + 2;
        graphics.setColor(color2);
        graphics.fillRect(i2 + 1, i16, 2, 2);
        graphics.fillRect((i2 + i4) - 3, i16, 2, 2);
        int i17 = i16 + 2;
        graphics.setColor(ColorRoutines.lighten(color2, 2 * value2));
        graphics.drawLine(i2 + 1, i17, i2 + 2, i17);
        graphics.drawLine((i2 + i4) - 2, i17, (i2 + i4) - 3, i17);
        int i18 = i17 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 4 * value2));
        graphics.drawLine(i2 + 1, i18, i2 + 2, i18);
        graphics.drawLine((i2 + i4) - 2, i18, (i2 + i4) - 3, i18);
        int i19 = i18 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 5 * value2));
        graphics.drawLine(i2 + 1, i19, i2 + 2, i19);
        graphics.drawLine((i2 + i4) - 2, i19, (i2 + i4) - 3, i19);
        int i20 = i19 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 6 * value2));
        graphics.drawLine(i2 + 1, i20, i2 + 2, i20);
        graphics.drawLine((i2 + i4) - 2, i20, (i2 + i4) - 3, i20);
        int i21 = i20 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 8 * value2));
        graphics.drawLine(i2 + 1, i21, i2 + 2, i21);
        graphics.drawLine((i2 + i4) - 2, i21, (i2 + i4) - 3, i21);
        int i22 = i21 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 9 * value2));
        graphics.drawLine(i2 + 1, i22, i2 + 2, i22);
        graphics.drawLine((i2 + i4) - 2, i22, (i2 + i4) - 3, i22);
        int i23 = i22 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 10 * value2));
        graphics.drawLine(i2 + 1, i23, i2 + 2, i23);
        graphics.drawLine((i2 + i4) - 2, i23, (i2 + i4) - 3, i23);
        int i24 = i23 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 4 * value2));
        graphics.drawLine(i2 + 1, i24, i2 + 2, i24);
        graphics.drawLine((i2 + i4) - 2, i24, (i2 + i4) - 3, i24);
        int i25 = i24 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics.drawLine(i2 + 1, i25, i2 + 2, i25);
        graphics.drawLine((i2 + i4) - 2, i25, (i2 + i4) - 3, i25);
        int i26 = i25 + 1;
        if (this.isActive) {
            graphics.setColor(Theme.frameLightColor.getColor());
        } else {
            graphics.setColor(Theme.frameLightDisabledColor.getColor());
        }
        graphics.drawLine(i2 + 1, i26, i2 + 2, i26);
        graphics.drawLine((i2 + i4) - 2, i26, (i2 + i4) - 3, i26);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        Window windowAncestor = SwingUtilities.getWindowAncestor(component);
        if (windowAncestor != null && (windowAncestor instanceof Frame)) {
            Frame frame = (Frame) windowAncestor;
            if (frame.getExtendedState() == (frame.getExtendedState() | 6)) {
                return new Insets(0, 0, 0, 0);
            }
        }
        return new Insets(0, 3, 3, 3);
    }
}
