package javax.imageio;

import java.awt.Point;
import java.awt.Rectangle;

/* loaded from: rt.jar:javax/imageio/IIOParam.class */
public abstract class IIOParam {
    protected Rectangle sourceRegion = null;
    protected int sourceXSubsampling = 1;
    protected int sourceYSubsampling = 1;
    protected int subsamplingXOffset = 0;
    protected int subsamplingYOffset = 0;
    protected int[] sourceBands = null;
    protected ImageTypeSpecifier destinationType = null;
    protected Point destinationOffset = new Point(0, 0);
    protected IIOParamController defaultController = null;
    protected IIOParamController controller;

    protected IIOParam() {
        this.controller = null;
        this.controller = this.defaultController;
    }

    public void setSourceRegion(Rectangle rectangle) {
        if (rectangle == null) {
            this.sourceRegion = null;
            return;
        }
        if (rectangle.f12372x < 0) {
            throw new IllegalArgumentException("sourceRegion.x < 0!");
        }
        if (rectangle.f12373y < 0) {
            throw new IllegalArgumentException("sourceRegion.y < 0!");
        }
        if (rectangle.width <= 0) {
            throw new IllegalArgumentException("sourceRegion.width <= 0!");
        }
        if (rectangle.height <= 0) {
            throw new IllegalArgumentException("sourceRegion.height <= 0!");
        }
        if (rectangle.width <= this.subsamplingXOffset) {
            throw new IllegalStateException("sourceRegion.width <= subsamplingXOffset!");
        }
        if (rectangle.height <= this.subsamplingYOffset) {
            throw new IllegalStateException("sourceRegion.height <= subsamplingYOffset!");
        }
        this.sourceRegion = (Rectangle) rectangle.clone();
    }

    public Rectangle getSourceRegion() {
        if (this.sourceRegion == null) {
            return null;
        }
        return (Rectangle) this.sourceRegion.clone();
    }

    public void setSourceSubsampling(int i2, int i3, int i4, int i5) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("sourceXSubsampling <= 0!");
        }
        if (i3 <= 0) {
            throw new IllegalArgumentException("sourceYSubsampling <= 0!");
        }
        if (i4 < 0 || i4 >= i2) {
            throw new IllegalArgumentException("subsamplingXOffset out of range!");
        }
        if (i5 < 0 || i5 >= i3) {
            throw new IllegalArgumentException("subsamplingYOffset out of range!");
        }
        if (this.sourceRegion != null && (i4 >= this.sourceRegion.width || i5 >= this.sourceRegion.height)) {
            throw new IllegalStateException("region contains no pixels!");
        }
        this.sourceXSubsampling = i2;
        this.sourceYSubsampling = i3;
        this.subsamplingXOffset = i4;
        this.subsamplingYOffset = i5;
    }

    public int getSourceXSubsampling() {
        return this.sourceXSubsampling;
    }

    public int getSourceYSubsampling() {
        return this.sourceYSubsampling;
    }

    public int getSubsamplingXOffset() {
        return this.subsamplingXOffset;
    }

    public int getSubsamplingYOffset() {
        return this.subsamplingYOffset;
    }

    public void setSourceBands(int[] iArr) {
        if (iArr == null) {
            this.sourceBands = null;
            return;
        }
        int length = iArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = iArr[i2];
            if (i3 < 0) {
                throw new IllegalArgumentException("Band value < 0!");
            }
            for (int i4 = i2 + 1; i4 < length; i4++) {
                if (i3 == iArr[i4]) {
                    throw new IllegalArgumentException("Duplicate band value!");
                }
            }
        }
        this.sourceBands = (int[]) iArr.clone();
    }

    public int[] getSourceBands() {
        if (this.sourceBands == null) {
            return null;
        }
        return (int[]) this.sourceBands.clone();
    }

    public void setDestinationType(ImageTypeSpecifier imageTypeSpecifier) {
        this.destinationType = imageTypeSpecifier;
    }

    public ImageTypeSpecifier getDestinationType() {
        return this.destinationType;
    }

    public void setDestinationOffset(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("destinationOffset == null!");
        }
        this.destinationOffset = (Point) point.clone();
    }

    public Point getDestinationOffset() {
        return (Point) this.destinationOffset.clone();
    }

    public void setController(IIOParamController iIOParamController) {
        this.controller = iIOParamController;
    }

    public IIOParamController getController() {
        return this.controller;
    }

    public IIOParamController getDefaultController() {
        return this.defaultController;
    }

    public boolean hasController() {
        return this.controller != null;
    }

    public boolean activateController() {
        if (!hasController()) {
            throw new IllegalStateException("hasController() == false!");
        }
        return getController().activate(this);
    }
}
