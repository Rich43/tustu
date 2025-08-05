package sun.swing.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:sun/swing/icon/SortArrowIcon.class */
public class SortArrowIcon implements Icon, UIResource, Serializable {
    private static final int ARROW_HEIGHT = 5;
    private static final int X_PADDING = 7;
    private boolean ascending;
    private Color color;
    private String colorKey;

    public SortArrowIcon(boolean z2, Color color) {
        this.ascending = z2;
        this.color = color;
        if (color == null) {
            throw new IllegalArgumentException();
        }
    }

    public SortArrowIcon(boolean z2, String str) {
        this.ascending = z2;
        this.colorKey = str;
        if (str == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override // javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        graphics.setColor(getColor());
        int i4 = 7 + i2 + 2;
        if (this.ascending) {
            graphics.fillRect(i4, i3, 1, 1);
            for (int i5 = 1; i5 < 5; i5++) {
                graphics.fillRect(i4 - i5, i3 + i5, i5 + i5 + 1, 1);
            }
            return;
        }
        int i6 = (i3 + 5) - 1;
        graphics.fillRect(i4, i6, 1, 1);
        for (int i7 = 1; i7 < 5; i7++) {
            graphics.fillRect(i4 - i7, i6 - i7, i7 + i7 + 1, 1);
        }
    }

    @Override // javax.swing.Icon
    public int getIconWidth() {
        return 17;
    }

    @Override // javax.swing.Icon
    public int getIconHeight() {
        return 7;
    }

    private Color getColor() {
        if (this.color != null) {
            return this.color;
        }
        return UIManager.getColor(this.colorKey);
    }
}
