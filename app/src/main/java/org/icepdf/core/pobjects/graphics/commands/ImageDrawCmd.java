package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.ImageReference;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/ImageDrawCmd.class */
public class ImageDrawCmd extends AbstractDrawCmd {
    private ImageReference image;
    private int xScale = 1;
    private int yScale = 1;
    private boolean xIsScale;
    private boolean yIsScale;
    private static boolean isScaledPaint = Defs.booleanProperty("org.icepdf.core.imageDrawCmd.scale.enabled", false);
    public static int MIN_DIMENSION = Defs.intProperty("org.icepdf.core.imageDrawCmd.maxDimension", 5);
    private static final double[][] SCALE_LOOKUP = {new double[]{1.5d, 2.0d}, new double[]{0.7d, 3.0d}, new double[]{0.4d, 4.0d}, new double[]{0.3d, 6.0d}, new double[]{0.2d, 8.0d}, new double[]{0.1d, 10.0d}, new double[]{0.05d, 12.0d}};

    public ImageDrawCmd(ImageReference image) {
        this.xIsScale = false;
        this.yIsScale = false;
        this.image = image;
        if (isScaledPaint) {
            if (image.getHeight() <= MIN_DIMENSION) {
                this.yIsScale = true;
            }
            if (image.getWidth() <= MIN_DIMENSION) {
                this.xIsScale = true;
            }
        }
    }

    public Image getImage() {
        return this.image.getImage();
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        if (optionalContentState.isVisible()) {
            if (isScaledPaint && (this.xIsScale || this.yIsScale)) {
                calculateThinScale(base.getScaleX());
            }
            this.image.drawImage(g2, 0, 0, this.xScale, this.yScale);
            if (parentPage != null && paintTimer.shouldTriggerRepaint()) {
                parentPage.notifyPaintPageListeners();
            }
        }
        return currentShape;
    }

    private void calculateThinScale(double scale) {
        if (this.xIsScale) {
            this.xScale = commonScaling(scale, this.image.getWidth());
        }
        if (this.yIsScale) {
            this.yScale = commonScaling(scale, this.image.getHeight());
        }
    }

    private int commonScaling(double scale, int size) {
        for (int i2 = SCALE_LOOKUP.length - 1; i2 >= 0; i2--) {
            if (scale < SCALE_LOOKUP[i2][0]) {
                double neededSize = SCALE_LOOKUP[i2][1];
                double scaleFactor = neededSize / size;
                return (int) Math.ceil(scaleFactor);
            }
        }
        return 1;
    }
}
