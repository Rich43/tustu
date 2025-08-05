package sun.java2d.pipe;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.IndexColorModel;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.awt.image.ShortLookupTable;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/pipe/BufferedBufImgOps.class */
public class BufferedBufImgOps {
    public static void enableBufImgOp(RenderQueue renderQueue, SurfaceData surfaceData, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp) {
        if (bufferedImageOp instanceof ConvolveOp) {
            enableConvolveOp(renderQueue, surfaceData, (ConvolveOp) bufferedImageOp);
        } else if (bufferedImageOp instanceof RescaleOp) {
            enableRescaleOp(renderQueue, surfaceData, bufferedImage, (RescaleOp) bufferedImageOp);
        } else {
            if (bufferedImageOp instanceof LookupOp) {
                enableLookupOp(renderQueue, surfaceData, bufferedImage, (LookupOp) bufferedImageOp);
                return;
            }
            throw new InternalError("Unknown BufferedImageOp");
        }
    }

    public static void disableBufImgOp(RenderQueue renderQueue, BufferedImageOp bufferedImageOp) {
        if (bufferedImageOp instanceof ConvolveOp) {
            disableConvolveOp(renderQueue);
        } else if (bufferedImageOp instanceof RescaleOp) {
            disableRescaleOp(renderQueue);
        } else {
            if (bufferedImageOp instanceof LookupOp) {
                disableLookupOp(renderQueue);
                return;
            }
            throw new InternalError("Unknown BufferedImageOp");
        }
    }

    public static boolean isConvolveOpValid(ConvolveOp convolveOp) {
        Kernel kernel = convolveOp.getKernel();
        int width = kernel.getWidth();
        int height = kernel.getHeight();
        if (width == 3 && height == 3) {
            return true;
        }
        if (width != 5 || height != 5) {
            return false;
        }
        return true;
    }

    private static void enableConvolveOp(RenderQueue renderQueue, SurfaceData surfaceData, ConvolveOp convolveOp) {
        boolean z2 = convolveOp.getEdgeCondition() == 0;
        Kernel kernel = convolveOp.getKernel();
        int width = kernel.getWidth();
        int height = kernel.getHeight();
        int i2 = 24 + (width * height * 4);
        RenderBuffer buffer = renderQueue.getBuffer();
        renderQueue.ensureCapacityAndAlignment(i2, 4);
        buffer.putInt(120);
        buffer.putLong(surfaceData.getNativeOps());
        buffer.putInt(z2 ? 1 : 0);
        buffer.putInt(width);
        buffer.putInt(height);
        buffer.put(kernel.getKernelData(null));
    }

    private static void disableConvolveOp(RenderQueue renderQueue) {
        RenderBuffer buffer = renderQueue.getBuffer();
        renderQueue.ensureCapacity(4);
        buffer.putInt(121);
    }

    public static boolean isRescaleOpValid(RescaleOp rescaleOp, BufferedImage bufferedImage) {
        int numFactors = rescaleOp.getNumFactors();
        ColorModel colorModel = bufferedImage.getColorModel();
        if (colorModel instanceof IndexColorModel) {
            throw new IllegalArgumentException("Rescaling cannot be performed on an indexed image");
        }
        if (numFactors != 1 && numFactors != colorModel.getNumColorComponents() && numFactors != colorModel.getNumComponents()) {
            throw new IllegalArgumentException("Number of scaling constants does not equal the number of of color or color/alpha  components");
        }
        int type = colorModel.getColorSpace().getType();
        if ((type != 5 && type != 6) || numFactors == 2 || numFactors > 4) {
            return false;
        }
        return true;
    }

    private static void enableRescaleOp(RenderQueue renderQueue, SurfaceData surfaceData, BufferedImage bufferedImage, RescaleOp rescaleOp) {
        float[] fArr;
        float[] fArr2;
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean z2 = colorModel.hasAlpha() && colorModel.isAlphaPremultiplied();
        int numFactors = rescaleOp.getNumFactors();
        float[] scaleFactors = rescaleOp.getScaleFactors(null);
        float[] offsets = rescaleOp.getOffsets(null);
        if (numFactors == 1) {
            fArr = new float[4];
            fArr2 = new float[4];
            for (int i2 = 0; i2 < 3; i2++) {
                fArr[i2] = scaleFactors[0];
                fArr2[i2] = offsets[0];
            }
            fArr[3] = 1.0f;
            fArr2[3] = 0.0f;
        } else if (numFactors == 3) {
            fArr = new float[4];
            fArr2 = new float[4];
            for (int i3 = 0; i3 < 3; i3++) {
                fArr[i3] = scaleFactors[i3];
                fArr2[i3] = offsets[i3];
            }
            fArr[3] = 1.0f;
            fArr2[3] = 0.0f;
        } else {
            fArr = scaleFactors;
            fArr2 = offsets;
        }
        if (colorModel.getNumComponents() == 1) {
            int componentSize = (1 << colorModel.getComponentSize(0)) - 1;
            for (int i4 = 0; i4 < 3; i4++) {
                float[] fArr3 = fArr2;
                int i5 = i4;
                fArr3[i5] = fArr3[i5] / componentSize;
            }
        } else {
            for (int i6 = 0; i6 < colorModel.getNumComponents(); i6++) {
                int componentSize2 = (1 << colorModel.getComponentSize(i6)) - 1;
                float[] fArr4 = fArr2;
                int i7 = i6;
                fArr4[i7] = fArr4[i7] / componentSize2;
            }
        }
        RenderBuffer buffer = renderQueue.getBuffer();
        renderQueue.ensureCapacityAndAlignment(16 + (4 * 4 * 2), 4);
        buffer.putInt(122);
        buffer.putLong(surfaceData.getNativeOps());
        buffer.putInt(z2 ? 1 : 0);
        buffer.put(fArr);
        buffer.put(fArr2);
    }

    private static void disableRescaleOp(RenderQueue renderQueue) {
        RenderBuffer buffer = renderQueue.getBuffer();
        renderQueue.ensureCapacity(4);
        buffer.putInt(123);
    }

    public static boolean isLookupOpValid(LookupOp lookupOp, BufferedImage bufferedImage) {
        LookupTable table = lookupOp.getTable();
        int numComponents = table.getNumComponents();
        ColorModel colorModel = bufferedImage.getColorModel();
        if (colorModel instanceof IndexColorModel) {
            throw new IllegalArgumentException("LookupOp cannot be performed on an indexed image");
        }
        if (numComponents != 1 && numComponents != colorModel.getNumComponents() && numComponents != colorModel.getNumColorComponents()) {
            throw new IllegalArgumentException("Number of arrays in the  lookup table (" + numComponents + ") is not compatible with the src image: " + ((Object) bufferedImage));
        }
        int type = colorModel.getColorSpace().getType();
        if ((type != 5 && type != 6) || numComponents == 2 || numComponents > 4) {
            return false;
        }
        if (table instanceof ByteLookupTable) {
            byte[][] table2 = ((ByteLookupTable) table).getTable();
            for (int i2 = 1; i2 < table2.length; i2++) {
                if (table2[i2].length > 256 || table2[i2].length != table2[i2 - 1].length) {
                    return false;
                }
            }
            return true;
        }
        if (table instanceof ShortLookupTable) {
            short[][] table3 = ((ShortLookupTable) table).getTable();
            for (int i3 = 1; i3 < table3.length; i3++) {
                if (table3[i3].length > 256 || table3[i3].length != table3[i3 - 1].length) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static void enableLookupOp(RenderQueue renderQueue, SurfaceData surfaceData, BufferedImage bufferedImage, LookupOp lookupOp) {
        int length;
        int i2;
        boolean z2;
        boolean z3 = bufferedImage.getColorModel().hasAlpha() && bufferedImage.isAlphaPremultiplied();
        LookupTable table = lookupOp.getTable();
        int numComponents = table.getNumComponents();
        int offset = table.getOffset();
        if (table instanceof ShortLookupTable) {
            length = ((ShortLookupTable) table).getTable()[0].length;
            i2 = 2;
            z2 = true;
        } else {
            length = ((ByteLookupTable) table).getTable()[0].length;
            i2 = 1;
            z2 = false;
        }
        int i3 = numComponents * length * i2;
        int i4 = (i3 + 3) & (-4);
        int i5 = i4 - i3;
        int i6 = 32 + i4;
        RenderBuffer buffer = renderQueue.getBuffer();
        renderQueue.ensureCapacityAndAlignment(i6, 4);
        buffer.putInt(124);
        buffer.putLong(surfaceData.getNativeOps());
        buffer.putInt(z3 ? 1 : 0);
        buffer.putInt(z2 ? 1 : 0);
        buffer.putInt(numComponents);
        buffer.putInt(length);
        buffer.putInt(offset);
        if (z2) {
            short[][] table2 = ((ShortLookupTable) table).getTable();
            for (int i7 = 0; i7 < numComponents; i7++) {
                buffer.put(table2[i7]);
            }
        } else {
            byte[][] table3 = ((ByteLookupTable) table).getTable();
            for (int i8 = 0; i8 < numComponents; i8++) {
                buffer.put(table3[i8]);
            }
        }
        if (i5 != 0) {
            buffer.position(buffer.position() + i5);
        }
    }

    private static void disableLookupOp(RenderQueue renderQueue) {
        RenderBuffer buffer = renderQueue.getBuffer();
        renderQueue.ensureCapacity(4);
        buffer.putInt(125);
    }
}
