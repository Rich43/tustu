package sun.awt.image;

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/awt/image/ImagingLib.class */
public class ImagingLib {
    static boolean useLib;
    private static final int NUM_NATIVE_OPS = 3;
    private static final int LOOKUP_OP = 0;
    private static final int AFFINE_OP = 1;
    private static final int CONVOLVE_OP = 2;
    static boolean verbose = false;
    private static Class[] nativeOpClass = new Class[3];

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean init();

    public static native int transformBI(BufferedImage bufferedImage, BufferedImage bufferedImage2, double[] dArr, int i2);

    public static native int transformRaster(Raster raster, Raster raster2, double[] dArr, int i2);

    public static native int convolveBI(BufferedImage bufferedImage, BufferedImage bufferedImage2, Kernel kernel, int i2);

    public static native int convolveRaster(Raster raster, Raster raster2, Kernel kernel, int i2);

    public static native int lookupByteBI(BufferedImage bufferedImage, BufferedImage bufferedImage2, byte[][] bArr);

    public static native int lookupByteRaster(Raster raster, Raster raster2, byte[][] bArr);

    static {
        useLib = true;
        useLib = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.awt.image.ImagingLib.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                String property = System.getProperty("os.arch");
                if (property == null || !property.startsWith("sparc")) {
                    try {
                        System.loadLibrary("mlib_image");
                    } catch (UnsatisfiedLinkError e2) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.valueOf(ImagingLib.init());
            }
        })).booleanValue();
        try {
            nativeOpClass[0] = Class.forName("java.awt.image.LookupOp");
        } catch (ClassNotFoundException e2) {
            System.err.println("Could not find class: " + ((Object) e2));
        }
        try {
            nativeOpClass[1] = Class.forName("java.awt.image.AffineTransformOp");
        } catch (ClassNotFoundException e3) {
            System.err.println("Could not find class: " + ((Object) e3));
        }
        try {
            nativeOpClass[2] = Class.forName("java.awt.image.ConvolveOp");
        } catch (ClassNotFoundException e4) {
            System.err.println("Could not find class: " + ((Object) e4));
        }
    }

    private static int getNativeOpIndex(Class cls) {
        int i2 = -1;
        int i3 = 0;
        while (true) {
            if (i3 >= 3) {
                break;
            }
            if (cls != nativeOpClass[i3]) {
                i3++;
            } else {
                i2 = i3;
                break;
            }
        }
        return i2;
    }

    public static WritableRaster filter(RasterOp rasterOp, Raster raster, WritableRaster writableRaster) {
        if (!useLib) {
            return null;
        }
        if (writableRaster == null) {
            writableRaster = rasterOp.createCompatibleDestRaster(raster);
        }
        WritableRaster writableRaster2 = null;
        switch (getNativeOpIndex(rasterOp.getClass())) {
            case 0:
                LookupTable table = ((LookupOp) rasterOp).getTable();
                if (table.getOffset() != 0) {
                    return null;
                }
                if ((table instanceof ByteLookupTable) && lookupByteRaster(raster, writableRaster, ((ByteLookupTable) table).getTable()) > 0) {
                    writableRaster2 = writableRaster;
                    break;
                }
                break;
            case 1:
                AffineTransformOp affineTransformOp = (AffineTransformOp) rasterOp;
                double[] dArr = new double[6];
                affineTransformOp.getTransform().getMatrix(dArr);
                if (transformRaster(raster, writableRaster, dArr, affineTransformOp.getInterpolationType()) > 0) {
                    writableRaster2 = writableRaster;
                    break;
                }
                break;
            case 2:
                ConvolveOp convolveOp = (ConvolveOp) rasterOp;
                if (convolveRaster(raster, writableRaster, convolveOp.getKernel(), convolveOp.getEdgeCondition()) > 0) {
                    writableRaster2 = writableRaster;
                    break;
                }
                break;
        }
        if (writableRaster2 != null) {
            SunWritableRaster.markDirty(writableRaster2);
        }
        return writableRaster2;
    }

    public static BufferedImage filter(BufferedImageOp bufferedImageOp, BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        if (verbose) {
            System.out.println("in filter and op is " + ((Object) bufferedImageOp) + "bufimage is " + ((Object) bufferedImage) + " and " + ((Object) bufferedImage2));
        }
        if (!useLib) {
            return null;
        }
        if (bufferedImage2 == null) {
            bufferedImage2 = bufferedImageOp.createCompatibleDestImage(bufferedImage, null);
        }
        BufferedImage bufferedImage3 = null;
        switch (getNativeOpIndex(bufferedImageOp.getClass())) {
            case 0:
                LookupTable table = ((LookupOp) bufferedImageOp).getTable();
                if (table.getOffset() != 0) {
                    return null;
                }
                if ((table instanceof ByteLookupTable) && lookupByteBI(bufferedImage, bufferedImage2, ((ByteLookupTable) table).getTable()) > 0) {
                    bufferedImage3 = bufferedImage2;
                    break;
                }
                break;
            case 1:
                AffineTransformOp affineTransformOp = (AffineTransformOp) bufferedImageOp;
                double[] dArr = new double[6];
                affineTransformOp.getTransform();
                affineTransformOp.getTransform().getMatrix(dArr);
                if (transformBI(bufferedImage, bufferedImage2, dArr, affineTransformOp.getInterpolationType()) > 0) {
                    bufferedImage3 = bufferedImage2;
                    break;
                }
                break;
            case 2:
                ConvolveOp convolveOp = (ConvolveOp) bufferedImageOp;
                if (convolveBI(bufferedImage, bufferedImage2, convolveOp.getKernel(), convolveOp.getEdgeCondition()) > 0) {
                    bufferedImage3 = bufferedImage2;
                    break;
                }
                break;
        }
        if (bufferedImage3 != null) {
            SunWritableRaster.markDirty(bufferedImage3);
        }
        return bufferedImage3;
    }
}
