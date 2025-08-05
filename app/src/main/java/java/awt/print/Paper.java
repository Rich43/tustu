package java.awt.print;

import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:java/awt/print/Paper.class */
public class Paper implements Cloneable {
    private static final int INCH = 72;
    private static final double LETTER_WIDTH = 612.0d;
    private static final double LETTER_HEIGHT = 792.0d;
    private double mHeight = LETTER_HEIGHT;
    private double mWidth = LETTER_WIDTH;
    private Rectangle2D mImageableArea = new Rectangle2D.Double(72.0d, 72.0d, this.mWidth - 144.0d, this.mHeight - 144.0d);

    public Object clone() {
        Paper paper;
        try {
            paper = (Paper) super.clone();
        } catch (CloneNotSupportedException e2) {
            e2.printStackTrace();
            paper = null;
        }
        return paper;
    }

    public double getHeight() {
        return this.mHeight;
    }

    public void setSize(double d2, double d3) {
        this.mWidth = d2;
        this.mHeight = d3;
    }

    public double getWidth() {
        return this.mWidth;
    }

    public void setImageableArea(double d2, double d3, double d4, double d5) {
        this.mImageableArea = new Rectangle2D.Double(d2, d3, d4, d5);
    }

    public double getImageableX() {
        return this.mImageableArea.getX();
    }

    public double getImageableY() {
        return this.mImageableArea.getY();
    }

    public double getImageableWidth() {
        return this.mImageableArea.getWidth();
    }

    public double getImageableHeight() {
        return this.mImageableArea.getHeight();
    }
}
