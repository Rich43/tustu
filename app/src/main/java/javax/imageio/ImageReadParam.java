package javax.imageio;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/* loaded from: rt.jar:javax/imageio/ImageReadParam.class */
public class ImageReadParam extends IIOParam {
    protected boolean canSetSourceRenderSize = false;
    protected Dimension sourceRenderSize = null;
    protected BufferedImage destination = null;
    protected int[] destinationBands = null;
    protected int minProgressivePass = 0;
    protected int numProgressivePasses = Integer.MAX_VALUE;

    @Override // javax.imageio.IIOParam
    public void setDestinationType(ImageTypeSpecifier imageTypeSpecifier) {
        super.setDestinationType(imageTypeSpecifier);
        setDestination(null);
    }

    public void setDestination(BufferedImage bufferedImage) {
        this.destination = bufferedImage;
    }

    public BufferedImage getDestination() {
        return this.destination;
    }

    public void setDestinationBands(int[] iArr) {
        if (iArr == null) {
            this.destinationBands = null;
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
        this.destinationBands = (int[]) iArr.clone();
    }

    public int[] getDestinationBands() {
        if (this.destinationBands == null) {
            return null;
        }
        return (int[]) this.destinationBands.clone();
    }

    public boolean canSetSourceRenderSize() {
        return this.canSetSourceRenderSize;
    }

    public void setSourceRenderSize(Dimension dimension) throws UnsupportedOperationException {
        if (!canSetSourceRenderSize()) {
            throw new UnsupportedOperationException("Can't set source render size!");
        }
        if (dimension == null) {
            this.sourceRenderSize = null;
        } else {
            if (dimension.width <= 0 || dimension.height <= 0) {
                throw new IllegalArgumentException("width or height <= 0!");
            }
            this.sourceRenderSize = (Dimension) dimension.clone();
        }
    }

    public Dimension getSourceRenderSize() {
        if (this.sourceRenderSize == null) {
            return null;
        }
        return (Dimension) this.sourceRenderSize.clone();
    }

    public void setSourceProgressivePasses(int i2, int i3) {
        if (i2 < 0) {
            throw new IllegalArgumentException("minPass < 0!");
        }
        if (i3 <= 0) {
            throw new IllegalArgumentException("numPasses <= 0!");
        }
        if (i3 != Integer.MAX_VALUE && (((i2 + i3) - 1) & Integer.MIN_VALUE) != 0) {
            throw new IllegalArgumentException("minPass + numPasses - 1 > INTEGER.MAX_VALUE!");
        }
        this.minProgressivePass = i2;
        this.numProgressivePasses = i3;
    }

    public int getSourceMinProgressivePass() {
        return this.minProgressivePass;
    }

    public int getSourceMaxProgressivePass() {
        if (this.numProgressivePasses == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (this.minProgressivePass + this.numProgressivePasses) - 1;
    }

    public int getSourceNumProgressivePasses() {
        return this.numProgressivePasses;
    }
}
