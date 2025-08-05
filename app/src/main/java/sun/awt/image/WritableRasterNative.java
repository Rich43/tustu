package sun.awt.image;

import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/awt/image/WritableRasterNative.class */
public class WritableRasterNative extends WritableRaster {
    public static WritableRasterNative createNativeRaster(SampleModel sampleModel, DataBuffer dataBuffer) {
        return new WritableRasterNative(sampleModel, dataBuffer);
    }

    protected WritableRasterNative(SampleModel sampleModel, DataBuffer dataBuffer) {
        super(sampleModel, dataBuffer, new Point(0, 0));
    }

    public static WritableRasterNative createNativeRaster(ColorModel colorModel, SurfaceData surfaceData, int i2, int i3) {
        int i4;
        SampleModel singlePixelPackedSampleModel;
        switch (colorModel.getPixelSize()) {
            case 8:
            case 12:
                if (colorModel.getPixelSize() == 8) {
                    i4 = 0;
                } else {
                    i4 = 1;
                }
                singlePixelPackedSampleModel = new PixelInterleavedSampleModel(i4, i2, i3, 1, i2, new int[]{0});
                break;
            case 15:
            case 16:
                i4 = 1;
                DirectColorModel directColorModel = (DirectColorModel) colorModel;
                singlePixelPackedSampleModel = new SinglePixelPackedSampleModel(1, i2, i3, i2, new int[]{directColorModel.getRedMask(), directColorModel.getGreenMask(), directColorModel.getBlueMask()});
                break;
            case 24:
            case 32:
                i4 = 3;
                DirectColorModel directColorModel2 = (DirectColorModel) colorModel;
                singlePixelPackedSampleModel = new SinglePixelPackedSampleModel(3, i2, i3, i2, new int[]{directColorModel2.getRedMask(), directColorModel2.getGreenMask(), directColorModel2.getBlueMask()});
                break;
            default:
                throw new InternalError("Unsupported depth " + colorModel.getPixelSize());
        }
        return new WritableRasterNative(singlePixelPackedSampleModel, new DataBufferNative(surfaceData, i4, i2, i3));
    }
}
