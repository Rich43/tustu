package javafx.embed.swt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: jfxswt.jar:javafx/embed/swt/SWTFXUtils.class */
public class SWTFXUtils {
    private static int blitSrc;
    private static boolean blitSrcCache;
    private static int alphaOpaque;
    private static boolean alphaOpaqueCache;
    private static int msbFirst;
    private static boolean msbFirstCache;
    private static Method blitDirect;
    private static Method blitPalette;
    private static Method getByteOrderMethod;

    private SWTFXUtils() {
    }

    public static WritableImage toFXImage(ImageData imageData, WritableImage image) {
        byte[] data = convertImage(imageData);
        if (data == null) {
            return null;
        }
        int width = imageData.width;
        int height = imageData.height;
        if (image != null) {
            int iw = (int) image.getWidth();
            int ih = (int) image.getHeight();
            if (iw < width || ih < height) {
                image = null;
            } else if (width < iw || height < ih) {
                int[] empty = new int[iw];
                PixelWriter pw = image.getPixelWriter();
                PixelFormat<IntBuffer> pf = PixelFormat.getIntArgbPreInstance();
                if (width < iw) {
                    pw.setPixels(width, 0, iw - width, height, pf, empty, 0, 0);
                }
                if (height < ih) {
                    pw.setPixels(0, height, iw, ih - height, pf, empty, 0, 0);
                }
            }
        }
        if (image == null) {
            image = new WritableImage(width, height);
        }
        int scan = width * 4;
        image.getPixelWriter().setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), data, 0, scan);
        return image;
    }

    public static ImageData fromFXImage(Image image, ImageData imageData) {
        PixelReader pr = image.getPixelReader();
        if (pr == null) {
            return null;
        }
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        int bpr = width * 4;
        int dataSize = bpr * height;
        byte[] buffer = new byte[dataSize];
        WritablePixelFormat<ByteBuffer> pf = PixelFormat.getByteBgraInstance();
        pr.getPixels(0, 0, width, height, pf, buffer, 0, bpr);
        byte[] alphaData = new byte[width * height];
        int offset = 0;
        int alphaOffset = 0;
        for (int y2 = 0; y2 < height; y2++) {
            int x2 = 0;
            while (x2 < width) {
                byte alpha = buffer[offset + 3];
                buffer[offset + 3] = 0;
                int i2 = alphaOffset;
                alphaOffset++;
                alphaData[i2] = alpha;
                x2++;
                offset += 4;
            }
        }
        PaletteData palette = new PaletteData(NormalizerImpl.CC_MASK, 16711680, -16777216);
        ImageData imageData2 = new ImageData(width, height, 32, palette, 4, buffer);
        imageData2.alphaData = alphaData;
        return imageData2;
    }

    private static int BLIT_SRC() throws Exception {
        if (!blitSrcCache) {
            blitSrc = readValue("BLIT_SRC");
            blitSrcCache = true;
        }
        return blitSrc;
    }

    private static int ALPHA_OPAQUE() throws Exception {
        if (!alphaOpaqueCache) {
            alphaOpaque = readValue("ALPHA_OPAQUE");
            alphaOpaqueCache = true;
        }
        return alphaOpaque;
    }

    private static int MSB_FIRST() throws Exception {
        if (!msbFirstCache) {
            msbFirst = readValue("MSB_FIRST");
            msbFirstCache = true;
        }
        return msbFirst;
    }

    private static int readValue(String name) throws Exception {
        Class<ImageData> cls = ImageData.class;
        return ((Integer) AccessController.doPrivileged(() -> {
            Field field = cls.getDeclaredField(name);
            field.setAccessible(true);
            return Integer.valueOf(field.getInt(cls));
        })).intValue();
    }

    private static void blit(int op, byte[] srcData, int srcDepth, int srcStride, int srcOrder, int srcX, int srcY, int srcWidth, int srcHeight, int srcRedMask, int srcGreenMask, int srcBlueMask, int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY, byte[] destData, int destDepth, int destStride, int destOrder, int destX, int destY, int destWidth, int destHeight, int destRedMask, int destGreenMask, int destBlueMask, boolean flipX, boolean flipY) throws Exception {
        Class<ImageData> cls = ImageData.class;
        if (blitDirect == null) {
            Class<?> I2 = Integer.TYPE;
            Class<?> B2 = Boolean.TYPE;
            Class<?>[] argClasses = {I2, byte[].class, I2, I2, I2, I2, I2, I2, I2, I2, I2, I2, I2, byte[].class, I2, I2, I2, byte[].class, I2, I2, I2, I2, I2, I2, I2, I2, I2, I2, B2, B2};
            blitDirect = (Method) AccessController.doPrivileged(() -> {
                Method method = cls.getDeclaredMethod("blit", argClasses);
                method.setAccessible(true);
                return method;
            });
        }
        if (blitDirect != null) {
            blitDirect.invoke(ImageData.class, Integer.valueOf(op), srcData, Integer.valueOf(srcDepth), Integer.valueOf(srcStride), Integer.valueOf(srcOrder), Integer.valueOf(srcX), Integer.valueOf(srcY), Integer.valueOf(srcWidth), Integer.valueOf(srcHeight), Integer.valueOf(srcRedMask), Integer.valueOf(srcGreenMask), Integer.valueOf(srcBlueMask), Integer.valueOf(alphaMode), alphaData, Integer.valueOf(alphaStride), Integer.valueOf(alphaX), Integer.valueOf(alphaY), destData, Integer.valueOf(destDepth), Integer.valueOf(destStride), Integer.valueOf(destOrder), Integer.valueOf(destX), Integer.valueOf(destY), Integer.valueOf(destWidth), Integer.valueOf(destHeight), Integer.valueOf(destRedMask), Integer.valueOf(destGreenMask), Integer.valueOf(destBlueMask), Boolean.valueOf(flipX), Boolean.valueOf(flipY));
        }
    }

    private static void blit(int op, byte[] srcData, int srcDepth, int srcStride, int srcOrder, int srcX, int srcY, int srcWidth, int srcHeight, byte[] srcReds, byte[] srcGreens, byte[] srcBlues, int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY, byte[] destData, int destDepth, int destStride, int destOrder, int destX, int destY, int destWidth, int destHeight, int destRedMask, int destGreenMask, int destBlueMask, boolean flipX, boolean flipY) throws Exception {
        Class<ImageData> cls = ImageData.class;
        if (blitPalette == null) {
            Class<?> I2 = Integer.TYPE;
            Class<?> B2 = Boolean.TYPE;
            Class<?>[] argClasses = {I2, byte[].class, I2, I2, I2, I2, I2, I2, I2, byte[].class, byte[].class, byte[].class, I2, byte[].class, I2, I2, I2, byte[].class, I2, I2, I2, I2, I2, I2, I2, I2, I2, I2, B2, B2};
            blitPalette = (Method) AccessController.doPrivileged(() -> {
                Method method = cls.getDeclaredMethod("blit", argClasses);
                method.setAccessible(true);
                return method;
            });
        }
        if (blitPalette != null) {
            blitPalette.invoke(ImageData.class, Integer.valueOf(op), srcData, Integer.valueOf(srcDepth), Integer.valueOf(srcStride), Integer.valueOf(srcOrder), Integer.valueOf(srcX), Integer.valueOf(srcY), Integer.valueOf(srcWidth), Integer.valueOf(srcHeight), srcReds, srcGreens, srcBlues, Integer.valueOf(alphaMode), alphaData, Integer.valueOf(alphaStride), Integer.valueOf(alphaX), Integer.valueOf(alphaY), destData, Integer.valueOf(destDepth), Integer.valueOf(destStride), Integer.valueOf(destOrder), Integer.valueOf(destX), Integer.valueOf(destY), Integer.valueOf(destWidth), Integer.valueOf(destHeight), Integer.valueOf(destRedMask), Integer.valueOf(destGreenMask), Integer.valueOf(destBlueMask), Boolean.valueOf(flipX), Boolean.valueOf(flipY));
        }
    }

    private static int getByteOrder(ImageData image) throws Exception {
        Class<ImageData> cls = ImageData.class;
        if (getByteOrderMethod != null) {
            getByteOrderMethod = (Method) AccessController.doPrivileged(() -> {
                Method method = cls.getDeclaredMethod("getByteOrder", new Class[0]);
                method.setAccessible(true);
                return method;
            });
        }
        if (getByteOrderMethod != null) {
            return ((Integer) getByteOrderMethod.invoke(image, new Object[0])).intValue();
        }
        return MSB_FIRST();
    }

    private static byte[] convertImage(ImageData image) {
        try {
            PaletteData palette = image.palette;
            if (((image.depth != 1 && image.depth != 2 && image.depth != 4 && image.depth != 8) || palette.isDirect) && image.depth != 8 && ((image.depth != 16 && image.depth != 24 && image.depth != 32) || !palette.isDirect)) {
                return null;
            }
            int BLIT_SRC = BLIT_SRC();
            int ALPHA_OPAQUE = ALPHA_OPAQUE();
            int MSB_FIRST = MSB_FIRST();
            int width = image.width;
            int height = image.height;
            int byteOrder = getByteOrder(image);
            int dataSize = width * height * 4;
            int bpr = width * 4;
            byte[] buffer = new byte[dataSize];
            if (palette.isDirect) {
                blit(BLIT_SRC, image.data, image.depth, image.bytesPerLine, byteOrder, 0, 0, width, height, palette.redMask, palette.greenMask, palette.blueMask, ALPHA_OPAQUE, (byte[]) null, 0, 0, 0, buffer, 32, bpr, MSB_FIRST, 0, 0, width, height, NormalizerImpl.CC_MASK, 16711680, -16777216, false, false);
            } else {
                RGB[] rgbs = palette.getRGBs();
                int length = rgbs.length;
                byte[] srcReds = new byte[length];
                byte[] srcGreens = new byte[length];
                byte[] srcBlues = new byte[length];
                for (int i2 = 0; i2 < rgbs.length; i2++) {
                    RGB rgb = rgbs[i2];
                    if (rgb != null) {
                        srcReds[i2] = (byte) rgb.red;
                        srcGreens[i2] = (byte) rgb.green;
                        srcBlues[i2] = (byte) rgb.blue;
                    }
                }
                blit(BLIT_SRC, image.data, image.depth, image.bytesPerLine, byteOrder, 0, 0, width, height, srcReds, srcGreens, srcBlues, ALPHA_OPAQUE, (byte[]) null, 0, 0, 0, buffer, 32, bpr, MSB_FIRST, 0, 0, width, height, NormalizerImpl.CC_MASK, 16711680, -16777216, false, false);
            }
            int transparency = image.getTransparencyType();
            boolean hasAlpha = transparency != 0;
            if (transparency == 2 || image.transparentPixel != -1) {
                ImageData maskImage = image.getTransparencyMask();
                byte[] maskData = maskImage.data;
                int maskBpl = maskImage.bytesPerLine;
                int offset = 0;
                int maskOffset = 0;
                for (int y2 = 0; y2 < height; y2++) {
                    for (int x2 = 0; x2 < width; x2++) {
                        byte b2 = maskData[maskOffset + (x2 >> 3)];
                        int v2 = 1 << (7 - (x2 & 7));
                        buffer[offset + 3] = (b2 & v2) != 0 ? (byte) -1 : (byte) 0;
                        offset += 4;
                    }
                    maskOffset += maskBpl;
                }
            } else if (image.alpha != -1) {
                hasAlpha = true;
                int alpha = image.alpha;
                byte a2 = (byte) alpha;
                for (int offset2 = 0; offset2 < buffer.length; offset2 += 4) {
                    buffer[offset2 + 3] = a2;
                }
            } else if (image.alphaData != null) {
                hasAlpha = true;
                byte[] alphaData = new byte[image.alphaData.length];
                System.arraycopy(image.alphaData, 0, alphaData, 0, alphaData.length);
                int offset3 = 0;
                int alphaOffset = 0;
                for (int y3 = 0; y3 < height; y3++) {
                    for (int x3 = 0; x3 < width; x3++) {
                        buffer[offset3 + 3] = alphaData[alphaOffset];
                        offset3 += 4;
                        alphaOffset++;
                    }
                }
            }
            if (!hasAlpha) {
                for (int offset4 = 0; offset4 < buffer.length; offset4 += 4) {
                    buffer[offset4 + 3] = -1;
                }
            }
            return buffer;
        } catch (Exception e2) {
            return null;
        }
    }
}
