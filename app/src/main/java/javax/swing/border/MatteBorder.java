package javax.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.Icon;

/* loaded from: rt.jar:javax/swing/border/MatteBorder.class */
public class MatteBorder extends EmptyBorder {
    protected Color color;
    protected Icon tileIcon;

    public MatteBorder(int i2, int i3, int i4, int i5, Color color) {
        super(i2, i3, i4, i5);
        this.color = color;
    }

    public MatteBorder(Insets insets, Color color) {
        super(insets);
        this.color = color;
    }

    public MatteBorder(int i2, int i3, int i4, int i5, Icon icon) {
        super(i2, i3, i4, i5);
        this.tileIcon = icon;
    }

    public MatteBorder(Insets insets, Icon icon) {
        super(insets);
        this.tileIcon = icon;
    }

    public MatteBorder(Icon icon) {
        this(-1, -1, -1, -1, icon);
    }

    @Override // javax.swing.border.EmptyBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        Insets borderInsets = getBorderInsets(component);
        Color color = graphics.getColor();
        graphics.translate(i2, i3);
        if (this.tileIcon != null) {
            this.color = this.tileIcon.getIconWidth() == -1 ? Color.gray : null;
        }
        if (this.color != null) {
            graphics.setColor(this.color);
            graphics.fillRect(0, 0, i4 - borderInsets.right, borderInsets.top);
            graphics.fillRect(0, borderInsets.top, borderInsets.left, i5 - borderInsets.top);
            graphics.fillRect(borderInsets.left, i5 - borderInsets.bottom, i4 - borderInsets.left, borderInsets.bottom);
            graphics.fillRect(i4 - borderInsets.right, 0, borderInsets.right, i5 - borderInsets.bottom);
        } else if (this.tileIcon != null) {
            int iconWidth = this.tileIcon.getIconWidth();
            int iconHeight = this.tileIcon.getIconHeight();
            paintEdge(component, graphics, 0, 0, i4 - borderInsets.right, borderInsets.top, iconWidth, iconHeight);
            paintEdge(component, graphics, 0, borderInsets.top, borderInsets.left, i5 - borderInsets.top, iconWidth, iconHeight);
            paintEdge(component, graphics, borderInsets.left, i5 - borderInsets.bottom, i4 - borderInsets.left, borderInsets.bottom, iconWidth, iconHeight);
            paintEdge(component, graphics, i4 - borderInsets.right, 0, borderInsets.right, i5 - borderInsets.bottom, iconWidth, iconHeight);
        }
        graphics.translate(-i2, -i3);
        graphics.setColor(color);
    }

    private void paintEdge(Component component, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Graphics graphicsCreate = graphics.create(i2, i3, i4, i5);
        int i8 = -(i3 % i7);
        int i9 = -(i2 % i6);
        while (true) {
            int i10 = i9;
            if (i10 < i4) {
                int i11 = i8;
                while (true) {
                    int i12 = i11;
                    if (i12 < i5) {
                        this.tileIcon.paintIcon(component, graphicsCreate, i10, i12);
                        i11 = i12 + i7;
                    }
                }
                i9 = i10 + i6;
            } else {
                graphicsCreate.dispose();
                return;
            }
        }
    }

    @Override // javax.swing.border.EmptyBorder, javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        return computeInsets(insets);
    }

    @Override // javax.swing.border.EmptyBorder
    public Insets getBorderInsets() {
        return computeInsets(new Insets(0, 0, 0, 0));
    }

    private Insets computeInsets(Insets insets) {
        if (this.tileIcon != null && this.top == -1 && this.bottom == -1 && this.left == -1 && this.right == -1) {
            int iconWidth = this.tileIcon.getIconWidth();
            int iconHeight = this.tileIcon.getIconHeight();
            insets.top = iconHeight;
            insets.right = iconWidth;
            insets.bottom = iconHeight;
            insets.left = iconWidth;
        } else {
            insets.left = this.left;
            insets.top = this.top;
            insets.right = this.right;
            insets.bottom = this.bottom;
        }
        return insets;
    }

    public Color getMatteColor() {
        return this.color;
    }

    public Icon getTileIcon() {
        return this.tileIcon;
    }

    @Override // javax.swing.border.EmptyBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return this.color != null;
    }
}
