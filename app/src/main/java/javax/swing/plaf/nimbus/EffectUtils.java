package javax.swing.plaf.nimbus;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Hashtable;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/EffectUtils.class */
class EffectUtils {
    EffectUtils() {
    }

    static void clearImage(BufferedImage bufferedImage) {
        Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
        graphics2DCreateGraphics.setComposite(AlphaComposite.Clear);
        graphics2DCreateGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics2DCreateGraphics.dispose();
    }

    static BufferedImage gaussianBlur(BufferedImage bufferedImage, BufferedImage bufferedImage2, int i2) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (bufferedImage2 == null || bufferedImage2.getWidth() != width || bufferedImage2.getHeight() != height || bufferedImage.getType() != bufferedImage2.getType()) {
            bufferedImage2 = createColorModelCompatibleImage(bufferedImage);
        }
        float[] fArrCreateGaussianKernel = createGaussianKernel(i2);
        if (bufferedImage.getType() == 2) {
            int[] iArr = new int[width * height];
            int[] iArr2 = new int[width * height];
            getPixels(bufferedImage, 0, 0, width, height, iArr);
            blur(iArr, iArr2, width, height, fArrCreateGaussianKernel, i2);
            blur(iArr2, iArr, height, width, fArrCreateGaussianKernel, i2);
            setPixels(bufferedImage2, 0, 0, width, height, iArr);
        } else if (bufferedImage.getType() == 10) {
            byte[] bArr = new byte[width * height];
            byte[] bArr2 = new byte[width * height];
            getPixels(bufferedImage, 0, 0, width, height, bArr);
            blur(bArr, bArr2, width, height, fArrCreateGaussianKernel, i2);
            blur(bArr2, bArr, height, width, fArrCreateGaussianKernel, i2);
            setPixels(bufferedImage2, 0, 0, width, height, bArr);
        } else {
            throw new IllegalArgumentException("EffectUtils.gaussianBlur() src image is not a supported type, type=[" + bufferedImage.getType() + "]");
        }
        return bufferedImage2;
    }

    private static void blur(int[] iArr, int[] iArr2, int i2, int i3, float[] fArr, int i4) {
        for (int i5 = 0; i5 < i3; i5++) {
            int i6 = i5;
            int i7 = i5 * i2;
            for (int i8 = 0; i8 < i2; i8++) {
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                float f5 = 0.0f;
                for (int i9 = -i4; i9 <= i4; i9++) {
                    int i10 = i8 + i9;
                    if (i10 < 0 || i10 >= i2) {
                        i10 = (i8 + i2) % i2;
                    }
                    int i11 = iArr[i7 + i10];
                    float f6 = fArr[i4 + i9];
                    f5 += f6 * ((i11 >> 24) & 255);
                    f4 += f6 * ((i11 >> 16) & 255);
                    f3 += f6 * ((i11 >> 8) & 255);
                    f2 += f6 * (i11 & 255);
                }
                int i12 = (int) (f5 + 0.5f);
                int i13 = (int) (f4 + 0.5f);
                int i14 = (int) (f3 + 0.5f);
                int i15 = (int) (f2 + 0.5f);
                iArr2[i6] = ((i12 > 255 ? 255 : i12) << 24) | ((i13 > 255 ? 255 : i13) << 16) | ((i14 > 255 ? 255 : i14) << 8) | (i15 > 255 ? 255 : i15);
                i6 += i3;
            }
        }
    }

    static void blur(byte[] bArr, byte[] bArr2, int i2, int i3, float[] fArr, int i4) {
        for (int i5 = 0; i5 < i3; i5++) {
            int i6 = i5;
            int i7 = i5 * i2;
            for (int i8 = 0; i8 < i2; i8++) {
                float f2 = 0.0f;
                for (int i9 = -i4; i9 <= i4; i9++) {
                    int i10 = i8 + i9;
                    if (i10 < 0 || i10 >= i2) {
                        i10 = (i8 + i2) % i2;
                    }
                    f2 += fArr[i4 + i9] * (bArr[i7 + i10] & 255);
                }
                int i11 = (int) (f2 + 0.5f);
                bArr2[i6] = (byte) (i11 > 255 ? 255 : i11);
                i6 += i3;
            }
        }
    }

    static float[] createGaussianKernel(int i2) {
        if (i2 < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }
        float[] fArr = new float[(i2 * 2) + 1];
        float f2 = i2 / 3.0f;
        float fSqrt = (float) Math.sqrt(2.0f * f2 * f2 * 3.141592653589793d);
        float f3 = 0.0f;
        for (int i3 = -i2; i3 <= i2; i3++) {
            int i4 = i3 + i2;
            fArr[i4] = ((float) Math.exp((-(i3 * i3)) / r0)) / fSqrt;
            f3 += fArr[i4];
        }
        for (int i5 = 0; i5 < fArr.length; i5++) {
            int i6 = i5;
            fArr[i6] = fArr[i6] / f3;
        }
        return fArr;
    }

    static byte[] getPixels(BufferedImage bufferedImage, int i2, int i3, int i4, int i5, byte[] bArr) {
        if (i4 == 0 || i5 == 0) {
            return new byte[0];
        }
        if (bArr == null) {
            bArr = new byte[i4 * i5];
        } else if (bArr.length < i4 * i5) {
            throw new IllegalArgumentException("pixels array must have a length >= w*h");
        }
        if (bufferedImage.getType() == 10) {
            return (byte[]) bufferedImage.getRaster().getDataElements(i2, i3, i4, i5, bArr);
        }
        throw new IllegalArgumentException("Only type BYTE_GRAY is supported");
    }

    static void setPixels(BufferedImage bufferedImage, int i2, int i3, int i4, int i5, byte[] bArr) {
        if (bArr == null || i4 == 0 || i5 == 0) {
            return;
        }
        if (bArr.length < i4 * i5) {
            throw new IllegalArgumentException("pixels array must have a length >= w*h");
        }
        if (bufferedImage.getType() == 10) {
            bufferedImage.getRaster().setDataElements(i2, i3, i4, i5, bArr);
            return;
        }
        throw new IllegalArgumentException("Only type BYTE_GRAY is supported");
    }

    public static int[] getPixels(BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int[] iArr) {
        if (i4 == 0 || i5 == 0) {
            return new int[0];
        }
        if (iArr == null) {
            iArr = new int[i4 * i5];
        } else if (iArr.length < i4 * i5) {
            throw new IllegalArgumentException("pixels array must have a length >= w*h");
        }
        int type = bufferedImage.getType();
        if (type == 2 || type == 1) {
            return (int[]) bufferedImage.getRaster().getDataElements(i2, i3, i4, i5, iArr);
        }
        return bufferedImage.getRGB(i2, i3, i4, i5, iArr, 0, i4);
    }

    public static void setPixels(BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int[] iArr) {
        if (iArr == null || i4 == 0 || i5 == 0) {
            return;
        }
        if (iArr.length < i4 * i5) {
            throw new IllegalArgumentException("pixels array must have a length >= w*h");
        }
        int type = bufferedImage.getType();
        if (type == 2 || type == 1) {
            bufferedImage.getRaster().setDataElements(i2, i3, i4, i5, iArr);
        } else {
            bufferedImage.setRGB(i2, i3, i4, i5, iArr, 0, i4);
        }
    }

    public static BufferedImage createColorModelCompatibleImage(BufferedImage bufferedImage) {
        ColorModel colorModel = bufferedImage.getColorModel();
        return new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight()), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
    }

    public static BufferedImage createCompatibleTranslucentImage(int i2, int i3) {
        return isHeadless() ? new BufferedImage(i2, i3, 2) : getGraphicsConfiguration().createCompatibleImage(i2, i3, 3);
    }

    private static boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }

    private static GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }
}
