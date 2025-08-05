package org.icepdf.core.pobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.icepdf.core.pobjects.graphics.CalGray;
import org.icepdf.core.pobjects.graphics.DeviceCMYK;
import org.icepdf.core.pobjects.graphics.DeviceGray;
import org.icepdf.core.pobjects.graphics.DeviceRGB;
import org.icepdf.core.pobjects.graphics.ICCBased;
import org.icepdf.core.pobjects.graphics.Indexed;
import org.icepdf.core.pobjects.graphics.PColorSpace;
import org.icepdf.core.pobjects.graphics.RasterOps.CMYKRasterOp;
import org.icepdf.core.pobjects.graphics.RasterOps.DecodeRasterOp;
import org.icepdf.core.pobjects.graphics.RasterOps.GrayRasterOp;
import org.icepdf.core.pobjects.graphics.RasterOps.PColorSpaceRasterOp;
import org.icepdf.core.pobjects.graphics.RasterOps.YCCKRasterOp;
import org.icepdf.core.pobjects.graphics.RasterOps.YCbCrRasterOp;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.util.Defs;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/ImageUtility.class */
public class ImageUtility {
    protected static final int JPEG_ENC_UNKNOWN_PROBABLY_YCbCr = 0;
    protected static final int JPEG_ENC_RGB = 1;
    protected static final int JPEG_ENC_CMYK = 2;
    protected static final int JPEG_ENC_YCbCr = 3;
    protected static final int JPEG_ENC_YCCK = 4;
    protected static final int JPEG_ENC_GRAY = 5;
    private static ImageUtility imageUtility;
    private static final Logger logger = Logger.getLogger(ImageUtility.class.toString());
    protected static final int[] GRAY_1_BIT_INDEX_TO_RGB_REVERSED = {-1, -16777216};
    protected static final int[] GRAY_1_BIT_INDEX_TO_RGB = {-16777216, -1};
    protected static final int[] GRAY_2_BIT_INDEX_TO_RGB = {-16777216, -11184811, -5592406, -1};
    protected static final int[] GRAY_4_BIT_INDEX_TO_RGB = {-16777216, -15658735, -14540254, -13421773, -12303292, -11184811, -10066330, -8947849, -7829368, -6710887, -5592406, -4473925, -3355444, -2236963, -1118482, -1};
    private static boolean scaleQuality = Defs.booleanProperty("org.icepdf.core.imageMaskScale.quality", true);

    private ImageUtility() {
    }

    public static ImageUtility getInstance() {
        if (imageUtility == null) {
            imageUtility = new ImageUtility();
        }
        return imageUtility;
    }

    protected static BufferedImage alterBufferedImageAlpha(BufferedImage bi2, int[] maskMinRGB, int[] maskMaxRGB) {
        if (!hasAlpha(bi2)) {
            bi2 = createBufferedImage(bi2);
        }
        int width = bi2.getWidth();
        int height = bi2.getHeight();
        int maskMinRed = 255;
        int maskMinGreen = 255;
        int maskMinBlue = 255;
        int maskMaxRed = 0;
        int maskMaxGreen = 0;
        int maskMaxBlue = 0;
        if (maskMinRGB != null && maskMaxRGB != null) {
            maskMinRed = maskMinRGB[0];
            maskMinGreen = maskMinRGB[1];
            maskMinBlue = maskMinRGB[2];
            maskMaxRed = maskMaxRGB[0];
            maskMaxGreen = maskMaxRGB[1];
            maskMaxBlue = maskMaxRGB[2];
        }
        for (int y2 = 0; y2 < height; y2++) {
            for (int x2 = 0; x2 < width; x2++) {
                int alpha = 255;
                int argb = bi2.getRGB(x2, y2);
                int red = (argb >> 16) & 255;
                int green = (argb >> 8) & 255;
                int blue = argb & 255;
                if (blue >= maskMinBlue && blue <= maskMaxBlue && green >= maskMinGreen && green <= maskMaxGreen && red >= maskMinRed && red <= maskMaxRed) {
                    alpha = 0;
                }
                if (alpha != 255) {
                    bi2.setRGB(x2, y2, (bi2.getRGB(x2, y2) & 16777215) | ((alpha << 24) & (-16777216)));
                }
            }
        }
        return bi2;
    }

    public static void displayImage(final BufferedImage bufferedImage, final String title) {
        if (bufferedImage == null) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.core.pobjects.ImageUtility.1
            @Override // java.lang.Runnable
            public void run() {
                final JFrame f2 = new JFrame("Image - " + title);
                f2.setDefaultCloseOperation(1);
                JComponent image = new JComponent() { // from class: org.icepdf.core.pobjects.ImageUtility.1.1
                    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
                    public void paint(Graphics g_) {
                        super.paint(g_);
                        g_.setColor(Color.green);
                        g_.fillRect(0, 0, 800, 800);
                        g_.drawImage(bufferedImage, 0, 0, f2);
                        g_.setColor(Color.red);
                        g_.drawRect(0, 0, bufferedImage.getWidth() - 2, bufferedImage.getHeight() - 2);
                    }
                };
                image.setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
                image.setSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
                JPanel test = new JPanel();
                test.setPreferredSize(new Dimension(1200, 1200));
                JScrollPane tmp = new JScrollPane(image);
                tmp.revalidate();
                f2.setSize(new Dimension(800, 800));
                f2.getContentPane().add(tmp);
                f2.validate();
                f2.setVisible(true);
            }
        });
    }

    protected static BufferedImage makeRGBABufferedImage(WritableRaster wr) {
        return makeRGBABufferedImage(wr, 1);
    }

    protected static BufferedImage makeRGBABufferedImage(WritableRaster wr, int transparency) {
        ColorSpace cs = ColorSpace.getInstance(1000);
        int[] bits = new int[4];
        for (int i2 = 0; i2 < bits.length; i2++) {
            bits[i2] = 8;
        }
        ColorModel cm = new ComponentColorModel(cs, bits, true, false, transparency, wr.getTransferType());
        return new BufferedImage(cm, wr, false, (Hashtable<?, ?>) null);
    }

    protected static BufferedImage makeBufferedImage(Raster raster) {
        DirectColorModel colorModel = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255, 0);
        return new BufferedImage((ColorModel) colorModel, (WritableRaster) colorModel.createCompatibleWritableRaster(raster.getWidth(), raster.getHeight()), false, (Hashtable<?, ?>) null);
    }

    protected static BufferedImage makeRGBBufferedImage(WritableRaster wr) {
        ColorSpace cs = ColorSpace.getInstance(1000);
        int[] bits = new int[3];
        for (int i2 = 0; i2 < bits.length; i2++) {
            bits[i2] = 8;
        }
        ColorModel cm = new ComponentColorModel(cs, bits, false, false, 1, wr.getTransferType());
        return new BufferedImage(cm, wr, false, (Hashtable<?, ?>) null);
    }

    protected static BufferedImage makeGrayBufferedImage(WritableRaster wr) {
        ColorSpace cs = ColorSpace.getInstance(1003);
        int[] bits = new int[1];
        for (int i2 = 0; i2 < bits.length; i2++) {
            bits[i2] = 8;
        }
        ColorModel cm = new ComponentColorModel(cs, bits, false, false, 1, wr.getTransferType());
        return new BufferedImage(cm, wr, false, (Hashtable<?, ?>) null);
    }

    protected static BufferedImage makeRGBABufferedImageFromImage(Image image) {
        GraphicsConfiguration gc;
        int transparency;
        int width;
        int height;
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        Image image2 = new ImageIcon(image).getImage();
        boolean hasAlpha = hasAlpha(image2);
        BufferedImage bImage = null;
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            gc = gs.getDefaultConfiguration();
            transparency = 1;
            if (hasAlpha) {
                transparency = 2;
            }
            width = image2.getWidth(null);
            height = image2.getHeight(null);
        } catch (HeadlessException e2) {
        }
        if (width == -1 || height == -1) {
            return null;
        }
        bImage = gc.createCompatibleImage(image2.getWidth(null), image2.getHeight(null), transparency);
        if (bImage == null) {
            int type = 1;
            if (hasAlpha) {
                type = 2;
            }
            int width2 = image2.getWidth(null);
            int height2 = image2.getHeight(null);
            if (width2 == -1 || height2 == -1) {
                return null;
            }
            bImage = new BufferedImage(width2, height2, type);
        }
        Graphics g2 = bImage.createGraphics();
        g2.drawImage(image2, 0, 0, null);
        g2.dispose();
        image2.flush();
        return bImage;
    }

    public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bufferedImage = (BufferedImage) image;
            return bufferedImage.getColorModel().hasAlpha();
        }
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pixelGrabber.grabPixels();
        } catch (InterruptedException e2) {
        }
        ColorModel cm = pixelGrabber.getColorModel();
        return cm == null || cm.hasAlpha();
    }

    protected static void getNormalizedComponents(byte[] pixels, float[] decode, float[] out) {
        for (int i2 = 0; i2 < pixels.length; i2++) {
            out[i2] = decode[i2 * 2] + ((pixels[i2] & 255) * decode[(i2 * 2) + 1]);
        }
    }

    protected static WritableRaster alterRasterRGBA(WritableRaster wr, BufferedImage smaskImage, BufferedImage maskImage, int[] maskMinRGB, int[] maskMaxRGB) {
        Raster smaskRaster = null;
        int smaskWidth = 0;
        int smaskHeight = 0;
        if (smaskImage != null) {
            smaskRaster = smaskImage.getRaster();
            smaskWidth = smaskRaster.getWidth();
            smaskHeight = smaskRaster.getHeight();
        }
        Raster maskRaster = null;
        int maskWidth = 0;
        int maskHeight = 0;
        if (maskImage != null) {
            maskRaster = maskImage.getRaster();
            maskWidth = maskRaster.getWidth();
            maskHeight = maskRaster.getHeight();
        }
        int maskMinRed = 255;
        int maskMinGreen = 255;
        int maskMinBlue = 255;
        int maskMaxRed = 0;
        int maskMaxGreen = 0;
        int maskMaxBlue = 0;
        if (maskMinRGB != null && maskMaxRGB != null) {
            maskMinRed = maskMinRGB[0];
            maskMinGreen = maskMinRGB[1];
            maskMinBlue = maskMinRGB[2];
            maskMaxRed = maskMaxRGB[0];
            maskMaxGreen = maskMaxRGB[1];
            maskMaxBlue = maskMaxRGB[2];
        }
        if (smaskRaster == null && maskRaster == null && (maskMinRGB == null || maskMaxRGB == null)) {
            return null;
        }
        int[] rgbaValues = new int[4];
        int width = wr.getWidth();
        int height = wr.getHeight();
        for (int y2 = 0; y2 < height; y2++) {
            for (int x2 = 0; x2 < width; x2++) {
                wr.getPixel(x2, y2, rgbaValues);
                int red = rgbaValues[0];
                int green = rgbaValues[1];
                int blue = rgbaValues[2];
                int alpha = 255;
                if (y2 < smaskHeight && x2 < smaskWidth && smaskRaster != null) {
                    alpha = smaskImage.getRGB(x2, y2) & 255;
                } else if (y2 < maskHeight && x2 < maskWidth && maskRaster != null) {
                    alpha = (maskImage.getRGB(x2, y2) >>> 24) & 255;
                } else if (blue >= maskMinBlue && blue <= maskMaxBlue && green >= maskMinGreen && green <= maskMaxGreen && red >= maskMinRed && red <= maskMaxRed) {
                    alpha = 0;
                }
                if (alpha != 255) {
                    rgbaValues[3] = alpha;
                    wr.setPixel(x2, y2, rgbaValues);
                }
            }
        }
        return wr;
    }

    public static BufferedImage applyExplicitMask(BufferedImage baseImage, BufferedImage maskImage) {
        BufferedImage[] images = scaleImagesToSameSize(baseImage, maskImage);
        BufferedImage baseImage2 = images[0];
        BufferedImage maskImage2 = images[1];
        int baseWidth = baseImage2.getWidth();
        int baseHeight = baseImage2.getHeight();
        BufferedImage argbImage = new BufferedImage(baseWidth, baseHeight, 2);
        int[] srcBand = new int[baseWidth];
        int[] maskBnd = new int[baseWidth];
        for (int i2 = 0; i2 < baseHeight; i2++) {
            baseImage2.getRGB(0, i2, baseWidth, 1, srcBand, 0, baseWidth);
            maskImage2.getRGB(0, i2, baseWidth, 1, maskBnd, 0, baseWidth);
            for (int j2 = 0; j2 < baseWidth; j2++) {
                if (maskBnd[j2] == 0 || maskBnd[j2] == 16777215) {
                    maskBnd[j2] = 255;
                } else {
                    maskBnd[j2] = srcBand[j2];
                }
            }
            argbImage.setRGB(0, i2, baseWidth, 1, maskBnd, 0, baseWidth);
        }
        baseImage2.flush();
        return argbImage;
    }

    public static BufferedImage applyExplicitSMask(BufferedImage baseImage, BufferedImage sMaskImage) {
        BufferedImage argbImage;
        BufferedImage[] images = scaleImagesToSameSize(baseImage, sMaskImage);
        BufferedImage baseImage2 = images[0];
        BufferedImage sMaskImage2 = images[1];
        int baseWidth = baseImage2.getWidth();
        int baseHeight = baseImage2.getHeight();
        if (hasAlpha(baseImage2)) {
            argbImage = baseImage2;
        } else {
            argbImage = new BufferedImage(baseWidth, baseHeight, 2);
        }
        int[] srcBand = new int[baseWidth];
        int[] sMaskBand = new int[baseWidth];
        for (int i2 = 0; i2 < baseHeight; i2++) {
            baseImage2.getRGB(0, i2, baseWidth, 1, srcBand, 0, baseWidth);
            sMaskImage2.getRGB(0, i2, baseWidth, 1, sMaskBand, 0, baseWidth);
            for (int j2 = 0; j2 < baseWidth; j2++) {
                if (sMaskBand[j2] != -1 || sMaskBand[j2] != 16777215 || sMaskBand[j2] != 0) {
                    sMaskBand[j2] = ((sMaskBand[j2] & 255) << 24) | (srcBand[j2] & 16777215);
                }
            }
            argbImage.setRGB(0, i2, baseWidth, 1, sMaskBand, 0, baseWidth);
        }
        baseImage2.flush();
        BufferedImage baseImage3 = argbImage;
        return baseImage3;
    }

    protected static BufferedImage applyExplicitMask(BufferedImage baseImage, Color fill) {
        BufferedImage imageMask;
        int baseWidth = baseImage.getWidth();
        int baseHeight = baseImage.getHeight();
        if (hasAlpha(baseImage)) {
            imageMask = baseImage;
        } else {
            imageMask = new BufferedImage(baseWidth, baseHeight, 2);
        }
        for (int y2 = 0; y2 < baseHeight; y2++) {
            for (int x2 = 0; x2 < baseWidth; x2++) {
                int maskPixel = baseImage.getRGB(x2, y2);
                if (maskPixel != -1 && maskPixel != 16777215) {
                    imageMask.setRGB(x2, y2, fill.getRGB());
                }
            }
        }
        baseImage.flush();
        return imageMask;
    }

    protected static BufferedImage applyIndexColourModel(WritableRaster wr, PColorSpace colourSpace, int bitsPerComponent) {
        colourSpace.init();
        Color[] colors = ((Indexed) colourSpace).accessColorTable();
        int colorsLength = colors == null ? 0 : colors.length;
        int[] cmap = new int[256];
        for (int i2 = 0; i2 < colorsLength; i2++) {
            cmap[i2] = colors[i2].getRGB();
        }
        for (int i3 = colorsLength; i3 < cmap.length; i3++) {
            cmap[i3] = -16777216;
        }
        DataBuffer db = wr.getDataBuffer();
        ColorModel cm = new IndexColorModel(bitsPerComponent, cmap.length, cmap, 0, false, -1, db.getDataType());
        BufferedImage img = new BufferedImage(cm, wr, false, (Hashtable<?, ?>) null);
        return img;
    }

    protected static BufferedImage proJbig2Decode(ImageInputStream imageInputStream, HashMap decodeParms, Stream globalsStream) throws IllegalAccessException, NoSuchMethodException, InstantiationException, IOException, ClassNotFoundException, InvocationTargetException {
        byte[] globals;
        Class<?> levigoJBIG2ImageReaderClass = Class.forName("com.levigo.jbig2.JBIG2ImageReader");
        Class<?> jbig2ImageReaderSpiClass = Class.forName("com.levigo.jbig2.JBIG2ImageReaderSpi");
        Class jbig2GlobalsClass = Class.forName("com.levigo.jbig2.JBIG2Globals");
        Object jbig2ImageReaderSpi = jbig2ImageReaderSpiClass.newInstance();
        Constructor levigoJbig2DecoderClassConstructor = levigoJBIG2ImageReaderClass.getDeclaredConstructor(ImageReaderSpi.class);
        Object levigoJbig2Reader = levigoJbig2DecoderClassConstructor.newInstance(jbig2ImageReaderSpi);
        Class[] partypes = {Object.class};
        Object[] arglist = {imageInputStream};
        Method setInput = levigoJBIG2ImageReaderClass.getMethod("setInput", partypes);
        setInput.invoke(levigoJbig2Reader, arglist);
        if (decodeParms != null && globalsStream != null && (globals = globalsStream.getDecodedStreamBytes(0)) != null && globals.length > 0) {
            Class[] partypes2 = {ImageInputStream.class};
            Object[] arglist2 = {ImageIO.createImageInputStream(new ByteArrayInputStream(globals))};
            Method processGlobals = levigoJBIG2ImageReaderClass.getMethod("processGlobals", partypes2);
            Object globalSegments = processGlobals.invoke(levigoJbig2Reader, arglist2);
            if (globalSegments != null) {
                Class[] partypes3 = {jbig2GlobalsClass};
                Object[] arglist3 = {globalSegments};
                Method setGlobalData = levigoJBIG2ImageReaderClass.getMethod("setGlobals", partypes3);
                setGlobalData.invoke(levigoJbig2Reader, arglist3);
            }
        }
        Class[] partypes4 = {Integer.TYPE};
        Object[] arglist4 = {0};
        Method read = levigoJBIG2ImageReaderClass.getMethod("read", partypes4);
        BufferedImage tmpImage = (BufferedImage) read.invoke(levigoJbig2Reader, arglist4);
        Method dispose = levigoJBIG2ImageReaderClass.getMethod("dispose", (Class[]) null);
        dispose.invoke(levigoJbig2Reader, new Object[0]);
        if (imageInputStream != null) {
            imageInputStream.close();
        }
        return tmpImage;
    }

    protected static BufferedImage jbig2Decode(byte[] data, HashMap decodeParms, Stream globalsStream) {
        byte[] globals;
        BufferedImage tmpImage = null;
        try {
            Class<?> jbig2DecoderClass = Class.forName("org.jpedal.jbig2.JBIG2Decoder");
            Constructor jbig2DecoderClassConstructor = jbig2DecoderClass.getDeclaredConstructor(new Class[0]);
            Object jbig2Decoder = jbig2DecoderClassConstructor.newInstance(new Object[0]);
            if (decodeParms != null && globalsStream != null && (globals = globalsStream.getDecodedStreamBytes(0)) != null && globals.length > 0) {
                Class[] partypes = {byte[].class};
                Object[] arglist = {globals};
                Method setGlobalData = jbig2DecoderClass.getMethod("setGlobalData", partypes);
                setGlobalData.invoke(jbig2Decoder, arglist);
            }
            Class<?>[] argTypes = {byte[].class};
            Object[] arglist2 = {data};
            Method decodeJBIG2 = jbig2DecoderClass.getMethod("decodeJBIG2", argTypes);
            decodeJBIG2.invoke(jbig2Decoder, arglist2);
            Method cleanupPostDecode = jbig2DecoderClass.getMethod("cleanupPostDecode", new Class[0]);
            cleanupPostDecode.invoke(jbig2Decoder, new Object[0]);
            Class<?>[] argTypes2 = {Integer.TYPE};
            Object[] arglist3 = {0};
            Method getPageAsBufferedImage = jbig2DecoderClass.getMethod("getPageAsBufferedImage", argTypes2);
            tmpImage = (BufferedImage) getPageAsBufferedImage.invoke(jbig2Decoder, arglist3);
        } catch (Exception e2) {
            logger.log(Level.WARNING, "Problem loading JBIG2 image: ", (Throwable) e2);
        }
        return tmpImage;
    }

    protected static int getJPEGEncoding(byte[] data, int dataLength) {
        int jpegEncoding = 0;
        boolean foundAPP14 = false;
        byte compsTypeFromAPP14 = 0;
        boolean foundSOF = false;
        int numCompsFromSOF = 0;
        boolean foundSOS = false;
        int numCompsFromSOS = 0;
        int index = 0;
        while (index < dataLength && data[index] == -1 && (!foundAPP14 || !foundSOF)) {
            byte segmentType = data[index + 1];
            index += 2;
            if (segmentType != -40) {
                int length = ((data[index] << 8) & NormalizerImpl.CC_MASK) + (data[index + 1] & 255);
                if (segmentType == -18) {
                    if (length >= 14) {
                        foundAPP14 = true;
                        compsTypeFromAPP14 = data[index + 13];
                    }
                } else if (segmentType == -64) {
                    foundSOF = true;
                    numCompsFromSOF = data[index + 7] & 255;
                } else if (segmentType == -38) {
                    foundSOS = true;
                    numCompsFromSOS = data[index + 2] & 255;
                }
                index += length;
            }
        }
        if (foundAPP14 && foundSOF) {
            if (compsTypeFromAPP14 == 0) {
                if (numCompsFromSOF == 1) {
                    jpegEncoding = 5;
                }
                if (numCompsFromSOF == 3) {
                    jpegEncoding = 1;
                } else if (numCompsFromSOF == 4) {
                    jpegEncoding = 2;
                }
            } else if (compsTypeFromAPP14 == 1) {
                jpegEncoding = 3;
            } else if (compsTypeFromAPP14 == 2) {
                jpegEncoding = 4;
            }
        } else if (foundSOS && numCompsFromSOS == 1) {
            jpegEncoding = 5;
        }
        return jpegEncoding;
    }

    public static BufferedImage convertSpaceToRgb(Raster colourRaster, PColorSpace colorSpace, float[] decode) {
        BufferedImage rgbImage = makeBufferedImage(colourRaster);
        WritableRaster rgbRaster = rgbImage.getRaster();
        DecodeRasterOp decodeRasterOp = new DecodeRasterOp(decode, null);
        decodeRasterOp.filter(colourRaster, (WritableRaster) colourRaster);
        PColorSpaceRasterOp pColorSpaceRasterOp = new PColorSpaceRasterOp(colorSpace, null);
        pColorSpaceRasterOp.filter(colourRaster, rgbRaster);
        return rgbImage;
    }

    public static BufferedImage convertGrayToRgb(Raster grayRaster, float[] decode) {
        DecodeRasterOp decodeRasterOp = new DecodeRasterOp(decode, null);
        decodeRasterOp.filter(grayRaster, (WritableRaster) grayRaster);
        GrayRasterOp grayRasterOp = new GrayRasterOp(decode, null);
        grayRasterOp.filter(grayRaster, (WritableRaster) grayRaster);
        return makeGrayBufferedImage((WritableRaster) grayRaster);
    }

    public static BufferedImage convertCmykToRgb(Raster cmykRaster, float[] decode) {
        BufferedImage rgbImage = makeBufferedImage(cmykRaster);
        if (!DeviceCMYK.isDisableICCCmykColorSpace()) {
            WritableRaster rgbRaster = rgbImage.getRaster();
            ColorSpace rgbCS = rgbImage.getColorModel().getColorSpace();
            DecodeRasterOp decodeRasterOp = new DecodeRasterOp(decode, null);
            decodeRasterOp.filter(cmykRaster, (WritableRaster) cmykRaster);
            ColorConvertOp cmykToRgb = new ColorConvertOp(DeviceCMYK.getIccCmykColorSpace(), rgbCS, null);
            cmykToRgb.filter(cmykRaster, rgbRaster);
            return rgbImage;
        }
        WritableRaster rgbRaster2 = rgbImage.getRaster();
        DecodeRasterOp decodeRasterOp2 = new DecodeRasterOp(decode, null);
        decodeRasterOp2.filter(cmykRaster, (WritableRaster) cmykRaster);
        CMYKRasterOp cmykRasterOp = new CMYKRasterOp(null);
        cmykRasterOp.filter(cmykRaster, rgbRaster2);
        return rgbImage;
    }

    public static BufferedImage convertYCbCrToRGB(Raster yCbCrRaster, float[] decode) {
        BufferedImage rgbImage = makeBufferedImage(yCbCrRaster);
        WritableRaster rgbRaster = rgbImage.getRaster();
        DecodeRasterOp decodeRasterOp = new DecodeRasterOp(decode, null);
        decodeRasterOp.filter(yCbCrRaster, (WritableRaster) yCbCrRaster);
        RasterOp rasterOp = new YCbCrRasterOp(null);
        rasterOp.filter(yCbCrRaster, rgbRaster);
        return rgbImage;
    }

    public static BufferedImage convertYCCKToRgb(Raster ycckRaster, float[] decode) {
        BufferedImage rgbImage = makeBufferedImage(ycckRaster);
        if (!DeviceCMYK.isDisableICCCmykColorSpace()) {
            WritableRaster rgbRaster = rgbImage.getRaster();
            ColorSpace rgbCS = rgbImage.getColorModel().getColorSpace();
            DecodeRasterOp decodeRasterOp = new DecodeRasterOp(decode, null);
            decodeRasterOp.filter(ycckRaster, (WritableRaster) ycckRaster);
            YCCKRasterOp ycckRasterOp = new YCCKRasterOp(null);
            ycckRasterOp.filter(ycckRaster, (WritableRaster) ycckRaster);
            ColorConvertOp cmykToRgb = new ColorConvertOp(DeviceCMYK.getIccCmykColorSpace(), rgbCS, null);
            cmykToRgb.filter(ycckRaster, rgbRaster);
            return rgbImage;
        }
        WritableRaster rgbRaster2 = rgbImage.getRaster();
        DecodeRasterOp decodeRasterOp2 = new DecodeRasterOp(decode, null);
        decodeRasterOp2.filter(ycckRaster, (WritableRaster) ycckRaster);
        YCCKRasterOp ycckRasterOp2 = new YCCKRasterOp(null);
        ycckRasterOp2.filter(ycckRaster, (WritableRaster) ycckRaster);
        CMYKRasterOp cmykRasterOp = new CMYKRasterOp(null);
        cmykRasterOp.filter(ycckRaster, rgbRaster2);
        return rgbImage;
    }

    protected static BufferedImage makeImageWithRasterFromBytes(PColorSpace colourSpace, Color fill, int width, int height, int colorSpaceCompCount, int bitsPerComponent, boolean imageMask, float[] decode, BufferedImage smaskImage, BufferedImage maskImage, int[] maskMinRGB, int[] maskMaxRGB, int maskMinIndex, int maskMaxIndex, byte[] data, int dataLength) {
        BufferedImage img = null;
        if (colourSpace instanceof ICCBased) {
            ICCBased iccBased = (ICCBased) colourSpace;
            if (iccBased.getAlternate() != null) {
                colourSpace = iccBased.getAlternate();
            }
        }
        if (colourSpace instanceof DeviceGray) {
            if (imageMask && bitsPerComponent == 1) {
                DataBuffer db = new DataBufferByte(data, dataLength);
                WritableRaster wr = Raster.createPackedRaster(db, width, height, bitsPerComponent, new Point(0, 0));
                boolean defaultDecode = decode[0] == 0.0f;
                int[] cmap = new int[2];
                cmap[0] = defaultDecode ? fill.getRGB() : 16777215;
                cmap[1] = defaultDecode ? 16777215 : fill.getRGB();
                int transparentIndex = defaultDecode ? 1 : 0;
                IndexColorModel icm = new IndexColorModel(bitsPerComponent, cmap.length, cmap, 0, true, transparentIndex, db.getDataType());
                img = new BufferedImage((ColorModel) icm, wr, false, (Hashtable<?, ?>) null);
            } else if (bitsPerComponent == 1 || bitsPerComponent == 2 || bitsPerComponent == 4) {
                DataBuffer db2 = new DataBufferByte(data, dataLength);
                WritableRaster wr2 = Raster.createPackedRaster(db2, width, height, bitsPerComponent, new Point(0, 0));
                int[] cmap2 = null;
                if (bitsPerComponent == 1) {
                    cmap2 = (0.0f > decode[0] ? 1 : (0.0f == decode[0] ? 0 : -1)) == 0 ? GRAY_1_BIT_INDEX_TO_RGB : GRAY_1_BIT_INDEX_TO_RGB_REVERSED;
                } else if (bitsPerComponent == 2) {
                    cmap2 = GRAY_2_BIT_INDEX_TO_RGB;
                } else if (bitsPerComponent == 4) {
                    cmap2 = GRAY_4_BIT_INDEX_TO_RGB;
                }
                ColorModel cm = new IndexColorModel(bitsPerComponent, cmap2.length, cmap2, 0, false, -1, db2.getDataType());
                img = new BufferedImage(cm, wr2, false, (Hashtable<?, ?>) null);
            } else if (bitsPerComponent == 8) {
                DataBuffer db3 = new DataBufferByte(data, dataLength);
                SampleModel sm = new PixelInterleavedSampleModel(db3.getDataType(), width, height, 1, width, new int[]{0});
                WritableRaster wr3 = Raster.createWritableRaster(sm, db3, new Point(0, 0));
                byte[] dataValues = new byte[sm.getNumBands()];
                float[] origValues = new float[sm.getNumBands()];
                for (int y2 = 0; y2 < height; y2++) {
                    for (int x2 = 0; x2 < width; x2++) {
                        getNormalizedComponents((byte[]) wr3.getDataElements(x2, y2, dataValues), decode, origValues);
                        float gray = origValues[0] * 255.0f;
                        byte rByte = gray < 0.0f ? (byte) 0 : gray > 255.0f ? (byte) -1 : (byte) gray;
                        origValues[0] = rByte;
                        wr3.setPixel(x2, y2, origValues);
                    }
                }
                ColorSpace cs = ColorSpace.getInstance(1003);
                ColorModel cm2 = new ComponentColorModel(cs, new int[]{bitsPerComponent}, false, false, 1, db3.getDataType());
                img = new BufferedImage(cm2, wr3, false, (Hashtable<?, ?>) null);
            }
        } else if (colourSpace instanceof DeviceRGB) {
            if (bitsPerComponent == 8) {
                boolean usingAlpha = (smaskImage == null && maskImage == null && (maskMinRGB == null || maskMaxRGB == null)) ? false : true;
                int type = usingAlpha ? 2 : 1;
                img = new BufferedImage(width, height, type);
                int[] dataToRGB = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
                copyDecodedStreamBytesIntoRGB(data, dataToRGB);
                if (usingAlpha) {
                    img = alterBufferedImageAlpha(img, maskMinRGB, maskMaxRGB);
                }
            }
        } else if (!(colourSpace instanceof DeviceCMYK)) {
            if (colourSpace instanceof Indexed) {
                if (bitsPerComponent == 1 || bitsPerComponent == 2 || bitsPerComponent == 4) {
                    colourSpace.init();
                    Color[] colors = ((Indexed) colourSpace).accessColorTable();
                    int[] cmap3 = new int[colors == null ? 0 : colors.length];
                    for (int i2 = 0; i2 < cmap3.length; i2++) {
                        cmap3[i2] = colors[i2].getRGB();
                    }
                    int cmapMaxLength = 1 << bitsPerComponent;
                    if (cmap3.length > cmapMaxLength) {
                        int[] cmapTruncated = new int[cmapMaxLength];
                        System.arraycopy(cmap3, 0, cmapTruncated, 0, cmapMaxLength);
                        cmap3 = cmapTruncated;
                    }
                    boolean z2 = maskMinIndex >= 0 && maskMaxIndex >= 0;
                    boolean usingAlpha2 = (smaskImage == null && maskImage == null && (maskMinRGB == null || maskMaxRGB == null)) ? false : true;
                    if (usingAlpha2) {
                        DataBuffer db4 = new DataBufferByte(data, dataLength);
                        WritableRaster wr4 = Raster.createPackedRaster(db4, width, height, bitsPerComponent, new Point(0, 0));
                        ColorModel cm3 = new IndexColorModel(bitsPerComponent, cmap3.length, cmap3, 0, true, -1, db4.getDataType());
                        img = alterBufferedImageAlpha(new BufferedImage(cm3, wr4, false, (Hashtable<?, ?>) null), maskMinRGB, maskMaxRGB);
                    } else {
                        DataBuffer db5 = new DataBufferByte(data, dataLength);
                        WritableRaster wr5 = Raster.createPackedRaster(db5, width, height, bitsPerComponent, new Point(0, 0));
                        ColorModel cm4 = new IndexColorModel(bitsPerComponent, cmap3.length, cmap3, 0, false, -1, db5.getDataType());
                        img = new BufferedImage(cm4, wr5, false, (Hashtable<?, ?>) null);
                    }
                } else if (bitsPerComponent == 8) {
                    colourSpace.init();
                    Color[] colors2 = ((Indexed) colourSpace).accessColorTable();
                    int colorsLength = colors2 == null ? 0 : colors2.length;
                    int[] cmap4 = new int[256];
                    for (int i3 = 0; i3 < colorsLength; i3++) {
                        cmap4[i3] = colors2[i3].getRGB();
                    }
                    for (int i4 = colorsLength; i4 < cmap4.length; i4++) {
                        cmap4[i4] = -16777216;
                    }
                    boolean usingIndexedAlpha = maskMinIndex >= 0 && maskMaxIndex >= 0;
                    boolean usingAlpha3 = (smaskImage == null && maskImage == null && (maskMinRGB == null || maskMaxRGB == null)) ? false : true;
                    if (usingIndexedAlpha) {
                        for (int i5 = maskMinIndex; i5 <= maskMaxIndex; i5++) {
                            cmap4[i5] = 0;
                        }
                        DataBuffer db6 = new DataBufferByte(data, dataLength);
                        SampleModel sm2 = new PixelInterleavedSampleModel(db6.getDataType(), width, height, 1, width, new int[]{0});
                        WritableRaster wr6 = Raster.createWritableRaster(sm2, db6, new Point(0, 0));
                        ColorModel cm5 = new IndexColorModel(bitsPerComponent, cmap4.length, cmap4, 0, true, -1, db6.getDataType());
                        img = new BufferedImage(cm5, wr6, false, (Hashtable<?, ?>) null);
                    } else if (usingAlpha3) {
                        int[] rgbaData = new int[width * height];
                        for (int index = 0; index < dataLength; index++) {
                            int cmapIndex = data[index] & 255;
                            rgbaData[index] = cmap4[cmapIndex];
                        }
                        DataBuffer db7 = new DataBufferInt(rgbaData, rgbaData.length);
                        int[] masks = {16711680, NormalizerImpl.CC_MASK, 255, -16777216};
                        WritableRaster wr7 = Raster.createPackedRaster(db7, width, height, width, masks, new Point(0, 0));
                        ColorSpace cs2 = ColorSpace.getInstance(1000);
                        ColorModel cm6 = new DirectColorModel(cs2, 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, false, db7.getDataType());
                        img = new BufferedImage(cm6, wr7, false, (Hashtable<?, ?>) null);
                    } else {
                        DataBuffer db8 = new DataBufferByte(data, dataLength);
                        SampleModel sm3 = new PixelInterleavedSampleModel(db8.getDataType(), width, height, 1, width, new int[]{0});
                        WritableRaster wr8 = Raster.createWritableRaster(sm3, db8, new Point(0, 0));
                        ColorModel cm7 = new IndexColorModel(bitsPerComponent, cmap4.length, cmap4, 0, false, -1, db8.getDataType());
                        img = new BufferedImage(cm7, wr8, false, (Hashtable<?, ?>) null);
                    }
                }
            } else if (((colourSpace instanceof Separation) || (colourSpace instanceof CalGray)) && ((colourSpace instanceof CalGray) || ((Separation) colourSpace).isNamedColor())) {
                DataBuffer db9 = new DataBufferByte(data, dataLength);
                WritableRaster wr9 = Raster.createPackedRaster(db9, width, height, bitsPerComponent, new Point(0, 0));
                int[] cmap5 = null;
                if (bitsPerComponent == 1) {
                    cmap5 = GRAY_1_BIT_INDEX_TO_RGB;
                } else if (bitsPerComponent == 2) {
                    cmap5 = GRAY_2_BIT_INDEX_TO_RGB;
                } else if (bitsPerComponent == 4) {
                    cmap5 = GRAY_4_BIT_INDEX_TO_RGB;
                }
                ColorModel cm8 = new IndexColorModel(bitsPerComponent, cmap5.length, cmap5, 0, false, -1, db9.getDataType());
                img = new BufferedImage(cm8, wr9, false, (Hashtable<?, ?>) null);
            }
        }
        return img;
    }

    private static void copyDecodedStreamBytesIntoRGB(byte[] data, int[] pixels) {
        int currRead;
        byte[] rgb = new byte[3];
        try {
            InputStream input = new ByteArrayInputStream(data);
            for (int pixelIndex = 0; pixelIndex < pixels.length; pixelIndex++) {
                int argb = -16777216;
                int haveRead = 0;
                while (haveRead < 3 && (currRead = input.read(rgb, haveRead, 3 - haveRead)) >= 0) {
                    haveRead += currRead;
                }
                if (haveRead >= 1) {
                    argb = (-16777216) | ((rgb[0] << 16) & 16711680);
                }
                if (haveRead >= 2) {
                    argb |= (rgb[1] << 8) & NormalizerImpl.CC_MASK;
                }
                if (haveRead >= 3) {
                    argb |= rgb[2] & 255;
                }
                pixels[pixelIndex] = argb;
            }
            input.close();
        } catch (IOException e2) {
            logger.log(Level.FINE, "Problem copying decoding stream bytes: ", (Throwable) e2);
        }
    }

    public static BufferedImage createBufferedImage(Image imageIn) {
        return createBufferedImage(imageIn, 2);
    }

    public static BufferedImage createBufferedImage(Image imageIn, int imageType) {
        BufferedImage bufferedImageOut = new BufferedImage(imageIn.getWidth(null), imageIn.getHeight(null), imageType);
        Graphics g2 = bufferedImageOut.getGraphics();
        g2.drawImage(imageIn, 0, 0, null);
        imageIn.flush();
        return bufferedImageOut;
    }

    public static BufferedImage[] scaleImagesToSameSize(BufferedImage baseImage, BufferedImage maskImage) {
        if (scaleQuality) {
            int width = baseImage.getWidth();
            int height = baseImage.getHeight();
            WritableRaster maskRaster = maskImage.getRaster();
            int maskWidth = maskRaster.getWidth();
            int maskHeight = maskRaster.getHeight();
            if (width < maskWidth || height < maskHeight) {
                double scaleX = maskWidth / width;
                double scaleY = maskHeight / height;
                AffineTransform tx = new AffineTransform();
                tx.scale(scaleX, scaleY);
                AffineTransformOp op = new AffineTransformOp(tx, 1);
                BufferedImage bim = op.filter(baseImage, (BufferedImage) null);
                baseImage.flush();
                baseImage = bim;
            } else if (width > maskWidth || height > maskHeight) {
                double scaleX2 = width / maskWidth;
                double scaleY2 = height / maskHeight;
                AffineTransform tx2 = new AffineTransform();
                tx2.scale(scaleX2, scaleY2);
                AffineTransformOp op2 = new AffineTransformOp(tx2, 1);
                BufferedImage bim2 = op2.filter(maskImage, (BufferedImage) null);
                maskImage.flush();
                maskImage = bim2;
            }
            return new BufferedImage[]{baseImage, maskImage};
        }
        int width2 = baseImage.getWidth();
        int height2 = baseImage.getHeight();
        WritableRaster maskRaster2 = maskImage.getRaster();
        int maskWidth2 = maskRaster2.getWidth();
        int maskHeight2 = maskRaster2.getHeight();
        if (width2 < maskWidth2 || height2 < maskHeight2) {
            double scaleX3 = width2 / maskWidth2;
            double scaleY3 = height2 / maskHeight2;
            AffineTransform tx3 = new AffineTransform();
            tx3.scale(scaleX3, scaleY3);
            AffineTransformOp op3 = new AffineTransformOp(tx3, 1);
            BufferedImage bim3 = op3.filter(maskImage, (BufferedImage) null);
            maskImage.flush();
            maskImage = bim3;
        }
        return new BufferedImage[]{baseImage, maskImage};
    }
}
