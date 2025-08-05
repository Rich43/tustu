package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

/* compiled from: MetalBumps.java */
/* loaded from: rt.jar:javax/swing/plaf/metal/BumpBuffer.class */
class BumpBuffer {
    static final int IMAGE_SIZE = 64;
    transient Image image;
    Color topColor;
    Color shadowColor;
    Color backColor;
    private GraphicsConfiguration gc;

    public BumpBuffer(GraphicsConfiguration graphicsConfiguration, Color color, Color color2, Color color3) {
        this.gc = graphicsConfiguration;
        this.topColor = color;
        this.shadowColor = color2;
        this.backColor = color3;
        createImage();
        fillBumpBuffer();
    }

    public boolean hasSameConfiguration(GraphicsConfiguration graphicsConfiguration, Color color, Color color2, Color color3) {
        if (this.gc != null) {
            if (!this.gc.equals(graphicsConfiguration)) {
                return false;
            }
        } else if (graphicsConfiguration != null) {
            return false;
        }
        return this.topColor.equals(color) && this.shadowColor.equals(color2) && this.backColor.equals(color3);
    }

    public Image getImage() {
        return this.image;
    }

    private void fillBumpBuffer() {
        Graphics graphics = this.image.getGraphics();
        graphics.setColor(this.backColor);
        graphics.fillRect(0, 0, 64, 64);
        graphics.setColor(this.topColor);
        for (int i2 = 0; i2 < 64; i2 += 4) {
            for (int i3 = 0; i3 < 64; i3 += 4) {
                graphics.drawLine(i2, i3, i2, i3);
                graphics.drawLine(i2 + 2, i3 + 2, i2 + 2, i3 + 2);
            }
        }
        graphics.setColor(this.shadowColor);
        for (int i4 = 0; i4 < 64; i4 += 4) {
            for (int i5 = 0; i5 < 64; i5 += 4) {
                graphics.drawLine(i4 + 1, i5 + 1, i4 + 1, i5 + 1);
                graphics.drawLine(i4 + 3, i5 + 3, i4 + 3, i5 + 3);
            }
        }
        graphics.dispose();
    }

    private void createImage() {
        if (this.gc != null) {
            this.image = this.gc.createCompatibleImage(64, 64, this.backColor != MetalBumps.ALPHA ? 1 : 2);
        } else {
            this.image = new BufferedImage(64, 64, 13, new IndexColorModel(8, 3, new int[]{this.backColor.getRGB(), this.topColor.getRGB(), this.shadowColor.getRGB()}, 0, false, this.backColor == MetalBumps.ALPHA ? 0 : -1, 0));
        }
    }
}
