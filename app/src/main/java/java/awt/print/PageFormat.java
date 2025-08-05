package java.awt.print;

/* loaded from: rt.jar:java/awt/print/PageFormat.class */
public class PageFormat implements Cloneable {
    public static final int LANDSCAPE = 0;
    public static final int PORTRAIT = 1;
    public static final int REVERSE_LANDSCAPE = 2;
    private int mOrientation = 1;
    private Paper mPaper = new Paper();

    public Object clone() {
        PageFormat pageFormat;
        try {
            pageFormat = (PageFormat) super.clone();
            pageFormat.mPaper = (Paper) this.mPaper.clone();
        } catch (CloneNotSupportedException e2) {
            e2.printStackTrace();
            pageFormat = null;
        }
        return pageFormat;
    }

    public double getWidth() {
        double height;
        if (getOrientation() == 1) {
            height = this.mPaper.getWidth();
        } else {
            height = this.mPaper.getHeight();
        }
        return height;
    }

    public double getHeight() {
        double width;
        if (getOrientation() == 1) {
            width = this.mPaper.getHeight();
        } else {
            width = this.mPaper.getWidth();
        }
        return width;
    }

    public double getImageableX() {
        double imageableY;
        switch (getOrientation()) {
            case 0:
                imageableY = this.mPaper.getHeight() - (this.mPaper.getImageableY() + this.mPaper.getImageableHeight());
                break;
            case 1:
                imageableY = this.mPaper.getImageableX();
                break;
            case 2:
                imageableY = this.mPaper.getImageableY();
                break;
            default:
                throw new InternalError("unrecognized orientation");
        }
        return imageableY;
    }

    public double getImageableY() {
        double width;
        switch (getOrientation()) {
            case 0:
                width = this.mPaper.getImageableX();
                break;
            case 1:
                width = this.mPaper.getImageableY();
                break;
            case 2:
                width = this.mPaper.getWidth() - (this.mPaper.getImageableX() + this.mPaper.getImageableWidth());
                break;
            default:
                throw new InternalError("unrecognized orientation");
        }
        return width;
    }

    public double getImageableWidth() {
        double imageableHeight;
        if (getOrientation() == 1) {
            imageableHeight = this.mPaper.getImageableWidth();
        } else {
            imageableHeight = this.mPaper.getImageableHeight();
        }
        return imageableHeight;
    }

    public double getImageableHeight() {
        double imageableWidth;
        if (getOrientation() == 1) {
            imageableWidth = this.mPaper.getImageableHeight();
        } else {
            imageableWidth = this.mPaper.getImageableWidth();
        }
        return imageableWidth;
    }

    public Paper getPaper() {
        return (Paper) this.mPaper.clone();
    }

    public void setPaper(Paper paper) {
        this.mPaper = (Paper) paper.clone();
    }

    public void setOrientation(int i2) throws IllegalArgumentException {
        if (0 <= i2 && i2 <= 2) {
            this.mOrientation = i2;
            return;
        }
        throw new IllegalArgumentException();
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public double[] getMatrix() {
        double[] dArr = new double[6];
        switch (this.mOrientation) {
            case 0:
                dArr[0] = 0.0d;
                dArr[1] = -1.0d;
                dArr[2] = 1.0d;
                dArr[3] = 0.0d;
                dArr[4] = 0.0d;
                dArr[5] = this.mPaper.getHeight();
                break;
            case 1:
                dArr[0] = 1.0d;
                dArr[1] = 0.0d;
                dArr[2] = 0.0d;
                dArr[3] = 1.0d;
                dArr[4] = 0.0d;
                dArr[5] = 0.0d;
                break;
            case 2:
                dArr[0] = 0.0d;
                dArr[1] = 1.0d;
                dArr[2] = -1.0d;
                dArr[3] = 0.0d;
                dArr[4] = this.mPaper.getWidth();
                dArr[5] = 0.0d;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return dArr;
    }
}
