package sun.swing.plaf.windows;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:sun/swing/plaf/windows/ClassicSortArrowIcon.class */
public class ClassicSortArrowIcon implements Icon, UIResource, Serializable {
    private static final int X_OFFSET = 9;
    private boolean ascending;

    public ClassicSortArrowIcon(boolean z2) {
        this.ascending = z2;
    }

    @Override // javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        int i4 = i2 + 9;
        if (this.ascending) {
            graphics.setColor(UIManager.getColor("Table.sortIconHighlight"));
            drawSide(graphics, i4 + 3, i3, -1);
            graphics.setColor(UIManager.getColor("Table.sortIconLight"));
            drawSide(graphics, i4 + 4, i3, 1);
            graphics.fillRect(i4 + 1, i3 + 6, 6, 1);
            return;
        }
        graphics.setColor(UIManager.getColor("Table.sortIconHighlight"));
        drawSide(graphics, i4 + 3, i3 + 6, -1);
        graphics.fillRect(i4 + 1, i3, 6, 1);
        graphics.setColor(UIManager.getColor("Table.sortIconLight"));
        drawSide(graphics, i4 + 4, i3 + 6, 1);
    }

    private void drawSide(Graphics graphics, int i2, int i3, int i4) {
        int i5;
        int i6 = 2;
        if (this.ascending) {
            graphics.fillRect(i2, i3, 1, 2);
            i5 = i3 + 1;
        } else {
            int i7 = i3 - 1;
            graphics.fillRect(i2, i7, 1, 2);
            i6 = -2;
            i5 = i7 - 2;
        }
        int i8 = i2 + i4;
        for (int i9 = 0; i9 < 2; i9++) {
            graphics.fillRect(i8, i5, 1, 3);
            i8 += i4;
            i5 += i6;
        }
        if (!this.ascending) {
            i5++;
        }
        graphics.fillRect(i8, i5, 1, 2);
    }

    @Override // javax.swing.Icon
    public int getIconWidth() {
        return 17;
    }

    @Override // javax.swing.Icon
    public int getIconHeight() {
        return 9;
    }
}
