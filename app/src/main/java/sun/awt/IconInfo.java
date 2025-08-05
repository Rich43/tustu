package sun.awt;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.util.Arrays;
import java.util.Hashtable;
import sun.awt.image.ImageRepresentation;
import sun.awt.image.ToolkitImage;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/awt/IconInfo.class */
public class IconInfo {
    private int[] intIconData;
    private long[] longIconData;
    private Image image;
    private final int width;
    private final int height;
    private int scaledWidth;
    private int scaledHeight;
    private int rawLength;

    public IconInfo(int[] iArr) {
        this.intIconData = null == iArr ? null : Arrays.copyOf(iArr, iArr.length);
        this.width = iArr[0];
        this.height = iArr[1];
        this.scaledWidth = this.width;
        this.scaledHeight = this.height;
        this.rawLength = (this.width * this.height) + 2;
    }

    public IconInfo(long[] jArr) {
        this.longIconData = null == jArr ? null : Arrays.copyOf(jArr, jArr.length);
        this.width = (int) jArr[0];
        this.height = (int) jArr[1];
        this.scaledWidth = this.width;
        this.scaledHeight = this.height;
        this.rawLength = (this.width * this.height) + 2;
    }

    public IconInfo(Image image) {
        this.image = image;
        if (image instanceof ToolkitImage) {
            ImageRepresentation imageRep = ((ToolkitImage) image).getImageRep();
            imageRep.reconstruct(32);
            this.width = imageRep.getWidth();
            this.height = imageRep.getHeight();
        } else {
            this.width = image.getWidth(null);
            this.height = image.getHeight(null);
        }
        this.scaledWidth = this.width;
        this.scaledHeight = this.height;
        this.rawLength = (this.width * this.height) + 2;
    }

    public void setScaledSize(int i2, int i3) {
        this.scaledWidth = i2;
        this.scaledHeight = i3;
        this.rawLength = (i2 * i3) + 2;
    }

    public boolean isValid() {
        return this.width > 0 && this.height > 0;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String toString() {
        return "IconInfo[w=" + this.width + ",h=" + this.height + ",sw=" + this.scaledWidth + ",sh=" + this.scaledHeight + "]";
    }

    public int getRawLength() {
        return this.rawLength;
    }

    public int[] getIntData() {
        if (this.intIconData == null) {
            if (this.longIconData != null) {
                this.intIconData = longArrayToIntArray(this.longIconData);
            } else if (this.image != null) {
                this.intIconData = imageToIntArray(this.image, this.scaledWidth, this.scaledHeight);
            }
        }
        return this.intIconData;
    }

    public long[] getLongData() {
        if (this.longIconData == null) {
            if (this.intIconData != null) {
                this.longIconData = intArrayToLongArray(this.intIconData);
            } else if (this.image != null) {
                this.longIconData = intArrayToLongArray(imageToIntArray(this.image, this.scaledWidth, this.scaledHeight));
            }
        }
        return this.longIconData;
    }

    public Image getImage() {
        if (this.image == null) {
            if (this.intIconData != null) {
                this.image = intArrayToImage(this.intIconData);
            } else if (this.longIconData != null) {
                this.image = intArrayToImage(longArrayToIntArray(this.longIconData));
            }
        }
        return this.image;
    }

    private static int[] longArrayToIntArray(long[] jArr) {
        int[] iArr = new int[jArr.length];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            iArr[i2] = (int) jArr[i2];
        }
        return iArr;
    }

    private static long[] intArrayToLongArray(int[] iArr) {
        long[] jArr = new long[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            jArr[i2] = iArr[i2];
        }
        return jArr;
    }

    static Image intArrayToImage(int[] iArr) {
        return new BufferedImage((ColorModel) new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, false, 3), Raster.createPackedRaster(new DataBufferInt(iArr, iArr.length - 2, 2), iArr[0], iArr[1], iArr[0], new int[]{16711680, NormalizerImpl.CC_MASK, 255, -16777216}, (Point) null), false, (Hashtable<?, ?>) null);
    }

    static int[] imageToIntArray(Image image, int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            return null;
        }
        DirectColorModel directColorModel = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, false, 3);
        DataBufferInt dataBufferInt = new DataBufferInt(i2 * i3);
        Graphics graphics = new BufferedImage((ColorModel) directColorModel, Raster.createPackedRaster(dataBufferInt, i2, i3, i2, new int[]{16711680, NormalizerImpl.CC_MASK, 255, -16777216}, (Point) null), false, (Hashtable<?, ?>) null).getGraphics();
        graphics.drawImage(image, 0, 0, i2, i3, null);
        graphics.dispose();
        int[] data = dataBufferInt.getData();
        int[] iArr = new int[(i2 * i3) + 2];
        iArr[0] = i2;
        iArr[1] = i3;
        System.arraycopy(data, 0, iArr, 2, i2 * i3);
        return iArr;
    }
}
