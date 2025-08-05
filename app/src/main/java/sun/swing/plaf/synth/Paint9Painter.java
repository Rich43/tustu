package sun.swing.plaf.synth;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import sun.swing.CachedPainter;

/* loaded from: rt.jar:sun/swing/plaf/synth/Paint9Painter.class */
public class Paint9Painter extends CachedPainter {
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    public static final int PAINT_TOP_LEFT = 1;
    public static final int PAINT_TOP = 2;
    public static final int PAINT_TOP_RIGHT = 4;
    public static final int PAINT_LEFT = 8;
    public static final int PAINT_CENTER = 16;
    public static final int PAINT_RIGHT = 32;
    public static final int PAINT_BOTTOM_RIGHT = 64;
    public static final int PAINT_BOTTOM = 128;
    public static final int PAINT_BOTTOM_LEFT = 256;
    public static final int PAINT_ALL = 512;

    /* loaded from: rt.jar:sun/swing/plaf/synth/Paint9Painter$PaintType.class */
    public enum PaintType {
        CENTER,
        TILE,
        PAINT9_STRETCH,
        PAINT9_TILE
    }

    public static boolean validImage(Image image) {
        return image != null && image.getWidth(null) > 0 && image.getHeight(null) > 0;
    }

    public Paint9Painter(int i2) {
        super(i2);
    }

    public void paint(Component component, Graphics graphics, int i2, int i3, int i4, int i5, Image image, Insets insets, Insets insets2, PaintType paintType, int i6) {
        if (image == null) {
            return;
        }
        super.paint(component, graphics, i2, i3, i4, i5, image, insets, insets2, paintType, Integer.valueOf(i6));
    }

    @Override // sun.swing.CachedPainter
    protected void paintToImage(Component component, Image image, Graphics graphics, int i2, int i3, Object[] objArr) {
        int i4 = 0;
        while (i4 < objArr.length) {
            int i5 = i4;
            int i6 = i4 + 1;
            Image image2 = (Image) objArr[i5];
            int i7 = i6 + 1;
            Insets insets = (Insets) objArr[i6];
            int i8 = i7 + 1;
            Insets insets2 = (Insets) objArr[i7];
            int i9 = i8 + 1;
            PaintType paintType = (PaintType) objArr[i8];
            i4 = i9 + 1;
            paint9(graphics, 0, 0, i2, i3, image2, insets, insets2, paintType, ((Integer) objArr[i9]).intValue());
        }
    }

    protected void paint9(Graphics graphics, int i2, int i3, int i4, int i5, Image image, Insets insets, Insets insets2, PaintType paintType, int i6) {
        if (!validImage(image)) {
            return;
        }
        if (insets == null) {
            insets = EMPTY_INSETS;
        }
        if (insets2 == null) {
            insets2 = EMPTY_INSETS;
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (paintType == PaintType.CENTER) {
            graphics.drawImage(image, i2 + ((i4 - width) / 2), i3 + ((i5 - height) / 2), null);
            return;
        }
        if (paintType == PaintType.TILE) {
            int i7 = 0;
            int i8 = i3;
            int i9 = i3 + i5;
            while (i8 < i9) {
                int i10 = 0;
                int i11 = i2;
                int i12 = i2 + i4;
                while (i11 < i12) {
                    int iMin = Math.min(i12, (i11 + width) - i10);
                    int iMin2 = Math.min(i9, (i8 + height) - i7);
                    graphics.drawImage(image, i11, i8, iMin, iMin2, i10, i7, (i10 + iMin) - i11, (i7 + iMin2) - i8, null);
                    i11 += width - i10;
                    i10 = 0;
                }
                i8 += height - i7;
                i7 = 0;
            }
            return;
        }
        int i13 = insets.top;
        int i14 = insets.left;
        int i15 = insets.bottom;
        int i16 = insets.right;
        int i17 = insets2.top;
        int i18 = insets2.left;
        int i19 = insets2.bottom;
        int i20 = insets2.right;
        if (i13 + i15 > height) {
            int iMax = Math.max(0, height / 2);
            i13 = iMax;
            i15 = iMax;
            i17 = iMax;
            i19 = iMax;
        }
        if (i14 + i16 > width) {
            int iMax2 = Math.max(0, width / 2);
            i16 = iMax2;
            i14 = iMax2;
            i20 = iMax2;
            i18 = iMax2;
        }
        if (i17 + i19 > i5) {
            int iMax3 = Math.max(0, (i5 / 2) - 1);
            i19 = iMax3;
            i17 = iMax3;
        }
        if (i18 + i20 > i4) {
            int iMax4 = Math.max(0, (i4 / 2) - 1);
            i20 = iMax4;
            i18 = iMax4;
        }
        boolean z2 = paintType == PaintType.PAINT9_STRETCH;
        if ((i6 & 512) != 0) {
            i6 = 511 & (i6 ^ (-1));
        }
        if ((i6 & 8) != 0) {
            drawChunk(image, graphics, z2, i2, i3 + i17, i2 + i18, (i3 + i5) - i19, 0, i13, i14, height - i15, false);
        }
        if ((i6 & 1) != 0) {
            drawImage(image, graphics, i2, i3, i2 + i18, i3 + i17, 0, 0, i14, i13);
        }
        if ((i6 & 2) != 0) {
            drawChunk(image, graphics, z2, i2 + i18, i3, (i2 + i4) - i20, i3 + i17, i14, 0, width - i16, i13, true);
        }
        if ((i6 & 4) != 0) {
            drawImage(image, graphics, (i2 + i4) - i20, i3, i2 + i4, i3 + i17, width - i16, 0, width, i13);
        }
        if ((i6 & 32) != 0) {
            drawChunk(image, graphics, z2, (i2 + i4) - i20, i3 + i17, i2 + i4, (i3 + i5) - i19, width - i16, i13, width, height - i15, false);
        }
        if ((i6 & 64) != 0) {
            drawImage(image, graphics, (i2 + i4) - i20, (i3 + i5) - i19, i2 + i4, i3 + i5, width - i16, height - i15, width, height);
        }
        if ((i6 & 128) != 0) {
            drawChunk(image, graphics, z2, i2 + i18, (i3 + i5) - i19, (i2 + i4) - i20, i3 + i5, i14, height - i15, width - i16, height, true);
        }
        if ((i6 & 256) != 0) {
            drawImage(image, graphics, i2, (i3 + i5) - i19, i2 + i18, i3 + i5, 0, height - i15, i14, height);
        }
        if ((i6 & 16) != 0) {
            drawImage(image, graphics, i2 + i18, i3 + i17, (i2 + i4) - i20, (i3 + i5) - i19, i14, i13, width - i16, height - i15);
        }
    }

    private void drawImage(Image image, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        if (i4 - i2 <= 0 || i5 - i3 <= 0 || i8 - i6 <= 0 || i9 - i7 <= 0) {
            return;
        }
        graphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, null);
    }

    private void drawChunk(Image image, Graphics graphics, boolean z2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, boolean z3) {
        int i10;
        int i11;
        if (i4 - i2 <= 0 || i5 - i3 <= 0 || i8 - i6 <= 0 || i9 - i7 <= 0) {
            return;
        }
        if (z2) {
            graphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, null);
            return;
        }
        int i12 = i8 - i6;
        int i13 = i9 - i7;
        if (z3) {
            i10 = i12;
            i11 = 0;
        } else {
            i10 = 0;
            i11 = i13;
        }
        while (i2 < i4 && i3 < i5) {
            int iMin = Math.min(i4, i2 + i12);
            int iMin2 = Math.min(i5, i3 + i13);
            graphics.drawImage(image, i2, i3, iMin, iMin2, i6, i7, (i6 + iMin) - i2, (i7 + iMin2) - i3, null);
            i2 += i10;
            i3 += i11;
        }
    }

    @Override // sun.swing.CachedPainter
    protected Image createImage(Component component, int i2, int i3, GraphicsConfiguration graphicsConfiguration, Object[] objArr) {
        if (graphicsConfiguration == null) {
            return new BufferedImage(i2, i3, 2);
        }
        return graphicsConfiguration.createCompatibleImage(i2, i3, 3);
    }
}
