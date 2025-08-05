package org.icepdf.core.pobjects;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.icepdf.core.io.BitStream;
import org.icepdf.core.io.SeekableInputConstrainedWrapper;
import org.icepdf.core.pobjects.filters.CCITTFax;
import org.icepdf.core.pobjects.graphics.DeviceCMYK;
import org.icepdf.core.pobjects.graphics.DeviceGray;
import org.icepdf.core.pobjects.graphics.DeviceRGB;
import org.icepdf.core.pobjects.graphics.ICCBased;
import org.icepdf.core.pobjects.graphics.Indexed;
import org.icepdf.core.pobjects.graphics.PColorSpace;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/ImageStream.class */
public class ImageStream extends Stream {
    private static final Logger logger = Logger.getLogger(ImageStream.class.toString());
    public static final Name TYPE_VALUE = new Name("Image");
    public static final Name BITSPERCOMPONENT_KEY = new Name(PdfOps.BPC_NAME);
    public static final Name BPC_KEY = new Name(PdfOps.BPC_TOKEN);
    public static final Name DECODE_KEY = new Name(PdfOps.D_NAME);
    public static final Name D_KEY = new Name(PdfOps.D_TOKEN);
    public static final Name SMASK_KEY = new Name("SMask");
    public static final Name MASK_KEY = new Name("Mask");
    public static final Name JBIG2GLOBALS_KEY = new Name("JBIG2Globals");
    public static final Name DECODEPARMS_KEY = new Name(PdfOps.DP_NAME);
    public static final Name DP_KEY = new Name(PdfOps.DP_TOKEN);
    public static final Name K_KEY = new Name(PdfOps.K_TOKEN);
    public static final Name ENCODEDBYTEALIGN_KEY = new Name("EncodedByteAlign");
    public static final Name COLUMNS_KEY = new Name("Columns");
    public static final Name ROWS_KEY = new Name("Rows");
    public static final Name BLACKIS1_KEY = new Name("BlackIs1");
    protected static final String[] CCITTFAX_DECODE_FILTERS = {"CCITTFaxDecode", "/CCF", "CCF"};
    protected static final String[] DCT_DECODE_FILTERS = {"DCTDecode", "/DCT", "DCT"};
    protected static final String[] JBIG2_DECODE_FILTERS = {"JBIG2Decode"};
    protected static final String[] JPX_DECODE_FILTERS = {"JPXDecode"};
    private static double pageRatio = Defs.sysPropertyDouble("org.icepdf.core.pageRatio", 0.7071917808219178d);
    private static boolean forceJaiccittfax = Defs.sysPropertyBoolean("org.icepdf.core.ccittfax.jai", false);
    private PColorSpace colourSpace;
    private final Object colorSpaceAssignmentLock;
    private static boolean isLevigoJBIG2ImageReaderClass;
    private int width;
    private int height;

    static {
        try {
            Class.forName("com.levigo.jbig2.JBIG2ImageReader");
            isLevigoJBIG2ImageReaderClass = true;
            logger.info("Levigo JBIG2 image library was found on classpath");
        } catch (ClassNotFoundException e2) {
            logger.info("Levigo JBIG2 image library was not found on classpath");
        }
    }

    public ImageStream(Library l2, HashMap h2, SeekableInputConstrainedWrapper streamInputWrapper) {
        super(l2, h2, streamInputWrapper);
        this.colorSpaceAssignmentLock = new Object();
        init();
    }

    public ImageStream(Library l2, HashMap h2, byte[] rawBytes) {
        super(l2, h2, rawBytes);
        this.colorSpaceAssignmentLock = new Object();
        init();
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public void init() {
        this.width = this.library.getInt(this.entries, WIDTH_KEY);
        this.height = this.library.getInt(this.entries, HEIGHT_KEY);
        if (this.height == 0) {
            this.height = (int) ((1.0d / pageRatio) * this.width);
        } else if (this.width == 0) {
            this.width = (int) (pageRatio * this.height);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public synchronized BufferedImage getImage(Color fill, Resources resources) {
        if (this.pObjectReference != null && this.library.getImagePool().containsKey(this.pObjectReference)) {
            return this.library.getImagePool().get(this.pObjectReference);
        }
        synchronized (this.colorSpaceAssignmentLock) {
            Object o2 = this.entries.get(COLORSPACE_KEY);
            if (resources != null && o2 != null) {
                this.colourSpace = resources.getColorSpace(o2);
            }
            if (this.colourSpace == null) {
                this.colourSpace = new DeviceGray(this.library, null);
            }
        }
        boolean isImageMask = isImageMask();
        int bitsPerComponent = this.library.getInt(this.entries, BITSPERCOMPONENT_KEY);
        if (isImageMask && bitsPerComponent == 0) {
            bitsPerComponent = 1;
        }
        int colorSpaceCompCount = this.colourSpace.getNumComponents();
        int maxValue = ((int) Math.pow(2.0d, bitsPerComponent)) - 1;
        float[] decode = new float[2 * colorSpaceCompCount];
        List<Number> decodeVec = (List) this.library.getObject(this.entries, DECODE_KEY);
        if (decodeVec == null) {
            int j2 = 0;
            for (int i2 = 0; i2 < colorSpaceCompCount; i2++) {
                int i3 = j2;
                int j3 = j2 + 1;
                decode[i3] = 0.0f;
                j2 = j3 + 1;
                decode[j3] = 1.0f / maxValue;
            }
        } else {
            int j4 = 0;
            for (int i4 = 0; i4 < colorSpaceCompCount; i4++) {
                float Dmin = decodeVec.get(j4).floatValue();
                float Dmax = decodeVec.get(j4 + 1).floatValue();
                int i5 = j4;
                int j5 = j4 + 1;
                decode[i5] = Dmin;
                j4 = j5 + 1;
                decode[j5] = (Dmax - Dmin) / maxValue;
            }
        }
        BufferedImage smaskImage = null;
        BufferedImage maskImage = null;
        int[] maskMinRGB = null;
        int[] maskMaxRGB = null;
        int maskMinIndex = -1;
        int maskMaxIndex = -1;
        Object smaskObj = this.library.getObject(this.entries, SMASK_KEY);
        Object maskObj = this.library.getObject(this.entries, MASK_KEY);
        if (smaskObj instanceof Stream) {
            ImageStream smaskStream = (ImageStream) smaskObj;
            if (smaskStream.isImageSubtype()) {
                smaskImage = smaskStream.getImage(fill, resources);
            }
        }
        if (maskObj != null && smaskImage == null) {
            if (maskObj instanceof Stream) {
                ImageStream maskStream = (ImageStream) maskObj;
                if (maskStream.isImageSubtype()) {
                    maskImage = maskStream.getImage(fill, resources);
                }
            } else if (maskObj instanceof List) {
                List maskVector = (List) maskObj;
                int[] maskMinOrigCompsInt = new int[colorSpaceCompCount];
                int[] maskMaxOrigCompsInt = new int[colorSpaceCompCount];
                for (int i6 = 0; i6 < colorSpaceCompCount; i6++) {
                    if (i6 * 2 < maskVector.size()) {
                        maskMinOrigCompsInt[i6] = ((Number) maskVector.get(i6 * 2)).intValue();
                    }
                    if ((i6 * 2) + 1 < maskVector.size()) {
                        maskMaxOrigCompsInt[i6] = ((Number) maskVector.get((i6 * 2) + 1)).intValue();
                    }
                }
                if (this.colourSpace instanceof Indexed) {
                    Indexed icolourSpace = (Indexed) this.colourSpace;
                    Color[] colors = icolourSpace.accessColorTable();
                    if (colors != null && maskMinOrigCompsInt.length >= 1 && maskMaxOrigCompsInt.length >= 1) {
                        maskMinIndex = maskMinOrigCompsInt[0];
                        maskMaxIndex = maskMaxOrigCompsInt[0];
                        if (maskMinIndex >= 0 && maskMinIndex < colors.length && maskMaxIndex >= 0 && maskMaxIndex < colors.length) {
                            Color minColor = colors[maskMinOrigCompsInt[0]];
                            Color maxColor = colors[maskMaxOrigCompsInt[0]];
                            maskMinRGB = new int[]{minColor.getRed(), minColor.getGreen(), minColor.getBlue()};
                            maskMaxRGB = new int[]{maxColor.getRed(), maxColor.getGreen(), maxColor.getBlue()};
                        }
                    }
                } else {
                    PColorSpace.reverseInPlace(maskMinOrigCompsInt);
                    PColorSpace.reverseInPlace(maskMaxOrigCompsInt);
                    float[] maskMinOrigComps = new float[colorSpaceCompCount];
                    float[] maskMaxOrigComps = new float[colorSpaceCompCount];
                    this.colourSpace.normaliseComponentsToFloats(maskMinOrigCompsInt, maskMinOrigComps, (1 << bitsPerComponent) - 1);
                    this.colourSpace.normaliseComponentsToFloats(maskMaxOrigCompsInt, maskMaxOrigComps, (1 << bitsPerComponent) - 1);
                    Color minColor2 = this.colourSpace.getColor(maskMinOrigComps);
                    Color maxColor2 = this.colourSpace.getColor(maskMaxOrigComps);
                    PColorSpace.reverseInPlace(maskMinOrigComps);
                    PColorSpace.reverseInPlace(maskMaxOrigComps);
                    maskMinRGB = new int[]{minColor2.getRed(), minColor2.getGreen(), minColor2.getBlue()};
                    maskMaxRGB = new int[]{maxColor2.getRed(), maxColor2.getGreen(), maxColor2.getBlue()};
                }
            }
        }
        BufferedImage image = getImage(this.colourSpace, fill, this.width, this.height, colorSpaceCompCount, bitsPerComponent, isImageMask, decode, smaskImage, maskImage, maskMinRGB, maskMaxRGB, maskMinIndex, maskMaxIndex);
        if (this.pObjectReference != null) {
            this.library.getImagePool().put(this.pObjectReference, image);
        }
        return image;
    }

    private BufferedImage getImage(PColorSpace colourSpace, Color fill, int width, int height, int colorSpaceCompCount, int bitsPerComponent, boolean isImageMask, float[] decode, BufferedImage sMaskImage, BufferedImage maskImage, int[] maskMinRGB, int[] maskMaxRGB, int maskMinIndex, int maskMaxIndex) {
        BufferedImage decodedImage = null;
        if (shouldUseDCTDecode()) {
            decodedImage = dctDecode(width, height, colourSpace, bitsPerComponent, decode);
        } else if (shouldUseJBIG2Decode()) {
            decodedImage = jbig2Decode(width, height, colourSpace, bitsPerComponent);
        } else if (shouldUseJPXDecode()) {
            decodedImage = jpxDecode(width, height, colourSpace, bitsPerComponent, decode);
        } else {
            byte[] data = getDecodedStreamBytes((((width * height) * colourSpace.getNumComponents()) * bitsPerComponent) / 8);
            int dataLength = data.length;
            if (shouldUseCCITTFaxDecode()) {
                try {
                    if (forceJaiccittfax) {
                        throw new Throwable("Forcing CCITTFAX decode via JAI");
                    }
                    data = ccittFaxDecode(data, width, height);
                    dataLength = data.length;
                } catch (Throwable th) {
                    try {
                        decodedImage = CCITTFax.attemptDeriveBufferedImageFromBytes(this, this.library, this.entries, fill);
                    } catch (Throwable th2) {
                        int length = ccittFaxDecode(data, width, height).length;
                    }
                    return decodedImage;
                }
            }
            try {
                decodedImage = ImageUtility.makeImageWithRasterFromBytes(colourSpace, fill, width, height, colorSpaceCompCount, bitsPerComponent, isImageMask, decode, sMaskImage, maskImage, maskMinRGB, maskMaxRGB, maskMinIndex, maskMaxIndex, data, dataLength);
            } catch (Exception e2) {
                logger.log(Level.FINE, "Error building image raster.", (Throwable) e2);
            }
        }
        if (decodedImage == null) {
            decodedImage = parseImage(width, height, colourSpace, isImageMask, fill, bitsPerComponent, decode, getDecodedStreamBytes((((width * height) * colourSpace.getNumComponents()) * bitsPerComponent) / 8));
        }
        if (decodedImage != null) {
            if (isImageMask) {
                decodedImage = ImageUtility.applyExplicitMask(decodedImage, fill);
            }
            if (sMaskImage != null) {
                decodedImage = ImageUtility.applyExplicitSMask(decodedImage, sMaskImage);
            }
            if (maskImage != null) {
                decodedImage = ImageUtility.applyExplicitMask(decodedImage, maskImage);
            }
            return decodedImage;
        }
        return null;
    }

    private BufferedImage dctDecode(int width, int height, PColorSpace colourSpace, int bitspercomponent, float[] decode) {
        int jpegEncoding;
        ImageInputStream imageInputStream;
        InputStream input = getDecodedByteArrayInputStream();
        BufferedInputStream bufferedInput = new BufferedInputStream(input, 2048);
        bufferedInput.mark(2048);
        BufferedImage tmpImage = null;
        ImageReader reader = null;
        ImageInputStream imageInputStream2 = null;
        try {
            try {
                byte[] data = getDecodedStreamBytes((((width * height) * colourSpace.getNumComponents()) * bitspercomponent) / 8);
                int dataRead = data.length;
                if (dataRead > 2048) {
                    dataRead = 2048;
                }
                jpegEncoding = ImageUtility.getJPEGEncoding(data, dataRead);
                imageInputStream = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
                Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
                while (true) {
                    if (!iter.hasNext()) {
                        break;
                    }
                    reader = iter.next();
                    if (reader.canReadRaster()) {
                        if (logger.isLoggable(Level.FINER)) {
                            logger.finer("DCTDecode Image reader: " + ((Object) reader));
                        }
                    }
                }
            } catch (IOException e2) {
                logger.log(Level.FINE, "Problem loading JPEG image via ImageIO: ", (Throwable) e2);
                try {
                    input.close();
                    if (0 != 0) {
                        reader.dispose();
                    }
                    if (0 != 0) {
                        imageInputStream2.close();
                    }
                } catch (IOException e3) {
                    logger.log(Level.FINE, "Problem loading JPEG image via ImageIO: ", (Throwable) e3);
                }
            }
            if (reader == null) {
                imageInputStream.close();
                try {
                    input.close();
                    if (reader != null) {
                        reader.dispose();
                    }
                    if (imageInputStream != null) {
                        imageInputStream.close();
                    }
                } catch (IOException e4) {
                    logger.log(Level.FINE, "Problem loading JPEG image via ImageIO: ", (Throwable) e4);
                }
                return null;
            }
            reader.setInput(imageInputStream, true, true);
            ImageReadParam param = reader.getDefaultReadParam();
            WritableRaster wr = (WritableRaster) reader.readRaster(0, param);
            tmpImage = (jpegEncoding == 1 && bitspercomponent == 8) ? ImageUtility.convertSpaceToRgb(wr, colourSpace, decode) : (jpegEncoding == 2 && bitspercomponent == 8) ? ImageUtility.convertCmykToRgb(wr, decode) : (jpegEncoding == 3 && bitspercomponent == 8) ? ImageUtility.convertYCbCrToRGB(wr, decode) : (jpegEncoding == 4 && bitspercomponent == 8) ? ImageUtility.convertYCCKToRgb(wr, decode) : (jpegEncoding == 5 && bitspercomponent == 8) ? ((colourSpace instanceof DeviceGray) || (colourSpace instanceof ICCBased) || (colourSpace instanceof Indexed)) ? ImageUtility.makeGrayBufferedImage(wr) : ((colourSpace instanceof Separation) && ((Separation) colourSpace).isNamedColor()) ? ImageUtility.convertGrayToRgb(wr, decode) : ImageUtility.convertSpaceToRgb(wr, colourSpace, decode) : wr.getNumBands() == 1 ? ImageUtility.convertSpaceToRgb(wr, colourSpace, decode) : ImageUtility.convertYCbCrToRGB(wr, decode);
            try {
                input.close();
                if (reader != null) {
                    reader.dispose();
                }
                if (imageInputStream != null) {
                    imageInputStream.close();
                }
            } catch (IOException e5) {
                logger.log(Level.FINE, "Problem loading JPEG image via ImageIO: ", (Throwable) e5);
            }
            return tmpImage;
        } catch (Throwable th) {
            try {
                input.close();
                if (0 != 0) {
                    reader.dispose();
                }
                if (0 != 0) {
                    imageInputStream2.close();
                }
            } catch (IOException e6) {
                logger.log(Level.FINE, "Problem loading JPEG image via ImageIO: ", (Throwable) e6);
            }
            throw th;
        }
    }

    private BufferedImage jbig2Decode(int width, int height, PColorSpace colourSpace, int bitspercomponent) {
        BufferedImage tmpImage;
        HashMap decodeParms = this.library.getDictionary(this.entries, DECODEPARMS_KEY);
        Stream globalsStream = null;
        if (decodeParms != null) {
            Object jbigGlobals = this.library.getObject(decodeParms, JBIG2GLOBALS_KEY);
            if (jbigGlobals instanceof Stream) {
                globalsStream = (Stream) jbigGlobals;
            }
        }
        byte[] data = getDecodedStreamBytes((((width * height) * colourSpace.getNumComponents()) * bitspercomponent) / 8);
        if (isLevigoJBIG2ImageReaderClass) {
            try {
                tmpImage = ImageUtility.proJbig2Decode(ImageIO.createImageInputStream(new ByteArrayInputStream(data)), decodeParms, globalsStream);
            } catch (Exception e2) {
                logger.log(Level.WARNING, "Problem loading JBIG2 imageusing Levigo: ", (Throwable) e2);
                tmpImage = ImageUtility.jbig2Decode(data, decodeParms, globalsStream);
            }
        } else {
            tmpImage = ImageUtility.jbig2Decode(data, decodeParms, globalsStream);
        }
        return tmpImage;
    }

    /* JADX WARN: Finally extract failed */
    private BufferedImage jpxDecode(int width, int height, PColorSpace colourSpace, int bitsPerComponent, float[] decode) {
        Iterator<ImageReader> iterator;
        BufferedImage tmpImage = null;
        try {
            iterator = ImageIO.getImageReadersByFormatName("JPEG2000");
        } catch (IOException e2) {
            logger.log(Level.FINE, "Problem loading JPEG2000 image: ", (Throwable) e2);
        }
        if (!iterator.hasNext()) {
            logger.info("ImageIO missing required plug-in to read JPEG 2000 images. You can download the JAI ImageIO Tools from: http://www.oracle.com/technetwork/java/current-142188.html");
            return null;
        }
        byte[] data = getDecodedStreamBytes((((width * height) * colourSpace.getNumComponents()) * bitsPerComponent) / 8);
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
        Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
        ImageReader reader = null;
        while (true) {
            if (!iter.hasNext()) {
                break;
            }
            reader = iter.next();
            if (reader.canReadRaster()) {
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("JPXDecode Image reader: " + ((Object) reader));
                }
            }
        }
        if (reader == null) {
            imageInputStream.close();
            return null;
        }
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(imageInputStream, true, true);
        try {
            tmpImage = reader.read(0, param);
            reader.dispose();
            imageInputStream.close();
            WritableRaster wr = tmpImage.getRaster();
            if (colourSpace instanceof ICCBased) {
                ICCBased iccBased = (ICCBased) colourSpace;
                try {
                    ColorSpace cs = iccBased.getColorSpace();
                    ColorConvertOp cco = new ColorConvertOp(cs, (RenderingHints) null);
                    tmpImage = ImageUtility.makeRGBBufferedImage(wr);
                    cco.filter(tmpImage, tmpImage);
                } catch (Throwable th) {
                    logger.warning("Error processing ICC Color profile, failing back to alternative.");
                    colourSpace = iccBased.getAlternate();
                }
            }
            if ((colourSpace instanceof DeviceRGB) && bitsPerComponent == 8) {
                tmpImage = ImageUtility.convertSpaceToRgb(wr, colourSpace, decode);
            } else if ((colourSpace instanceof DeviceCMYK) && bitsPerComponent == 8) {
                tmpImage = ImageUtility.convertCmykToRgb(wr, decode);
            } else if ((colourSpace instanceof DeviceGray) && bitsPerComponent == 8) {
                tmpImage = ImageUtility.makeGrayBufferedImage(wr);
            } else if (colourSpace instanceof Separation) {
                if ((colourSpace instanceof Separation) && ((Separation) colourSpace).isNamedColor()) {
                    tmpImage = ImageUtility.convertGrayToRgb(wr, decode);
                } else {
                    tmpImage = ImageUtility.convertSpaceToRgb(wr, colourSpace, decode);
                }
            } else if (colourSpace instanceof Indexed) {
                tmpImage = ImageUtility.applyIndexColourModel(wr, colourSpace, bitsPerComponent);
            }
            return tmpImage;
        } catch (Throwable th2) {
            reader.dispose();
            imageInputStream.close();
            throw th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private byte[] ccittFaxDecode(byte[] r7, int r8, int r9) {
        /*
            Method dump skipped, instructions count: 319
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.pobjects.ImageStream.ccittFaxDecode(byte[], int, int):byte[]");
    }

    private BufferedImage parseImage(int width, int height, PColorSpace colorSpace, boolean imageMask, Color fill, int bitsPerColour, float[] decode, byte[] baCCITTFaxData) {
        BitStream in;
        int[] imageBits = new int[width];
        int fillRGB = fill.getRGB();
        int colorSpaceCompCount = colorSpace.getNumComponents();
        boolean isDeviceRGB = colorSpace instanceof DeviceRGB;
        boolean isDeviceGray = colorSpace instanceof DeviceGray;
        int maxColourValue = (1 << bitsPerColour) - 1;
        int[] f2 = new int[colorSpaceCompCount];
        float[] ff = new float[colorSpaceCompCount];
        float imageMaskValue = decode[0];
        BufferedImage bim = new BufferedImage(width, height, 2);
        if (baCCITTFaxData != null) {
            in = new BitStream(new ByteArrayInputStream(baCCITTFaxData));
        } else {
            InputStream dataInput = getDecodedByteArrayInputStream();
            if (dataInput == null) {
                return null;
            }
            in = new BitStream(dataInput);
        }
        for (int y2 = 0; y2 < height; y2++) {
            for (int x2 = 0; x2 < width; x2++) {
                if (imageMask) {
                    try {
                        int bit = in.getBits(bitsPerColour);
                        int bit2 = ((float) bit) == imageMaskValue ? fillRGB : 0;
                        imageBits[x2] = bit2;
                    } catch (IOException e2) {
                        logger.log(Level.FINE, "Error parsing image.", (Throwable) e2);
                    }
                } else if (colorSpaceCompCount == 1) {
                    int bit3 = in.getBits(bitsPerColour);
                    if (decode != null && decode[0] > decode[1]) {
                        bit3 = bit3 == maxColourValue ? 0 : maxColourValue;
                    }
                    if (isDeviceGray) {
                        if (bitsPerColour == 1) {
                            bit3 = ImageUtility.GRAY_1_BIT_INDEX_TO_RGB[bit3];
                        } else if (bitsPerColour == 2) {
                            bit3 = ImageUtility.GRAY_2_BIT_INDEX_TO_RGB[bit3];
                        } else if (bitsPerColour == 4) {
                            bit3 = ImageUtility.GRAY_4_BIT_INDEX_TO_RGB[bit3];
                        } else if (bitsPerColour == 8) {
                            bit3 = (bit3 << 24) | (bit3 << 16) | (bit3 << 8) | bit3;
                        }
                        imageBits[x2] = bit3;
                    } else {
                        f2[0] = bit3;
                        colorSpace.normaliseComponentsToFloats(f2, ff, maxColourValue);
                        Color color = colorSpace.getColor(ff);
                        imageBits[x2] = color.getRGB();
                    }
                } else if (colorSpaceCompCount == 3) {
                    if (isDeviceRGB) {
                        int red = in.getBits(bitsPerColour);
                        int green = in.getBits(bitsPerColour);
                        int blue = in.getBits(bitsPerColour);
                        imageBits[x2] = (255 << 24) | (red << 16) | (green << 8) | blue;
                    } else {
                        for (int i2 = 0; i2 < colorSpaceCompCount; i2++) {
                            f2[i2] = in.getBits(bitsPerColour);
                        }
                        PColorSpace.reverseInPlace(f2);
                        colorSpace.normaliseComponentsToFloats(f2, ff, maxColourValue);
                        Color color2 = colorSpace.getColor(ff);
                        imageBits[x2] = color2.getRGB();
                    }
                } else if (colorSpaceCompCount == 4) {
                    for (int i3 = 0; i3 < colorSpaceCompCount; i3++) {
                        f2[i3] = in.getBits(bitsPerColour);
                        if (decode[0] > decode[1]) {
                            f2[i3] = maxColourValue - f2[i3];
                        }
                    }
                    PColorSpace.reverseInPlace(f2);
                    colorSpace.normaliseComponentsToFloats(f2, ff, maxColourValue);
                    Color color3 = colorSpace.getColor(ff);
                    imageBits[x2] = color3.getRGB();
                } else {
                    imageBits[x2] = (255 << 24) | (255 << 16) | (255 << 8) | 255;
                }
            }
            bim.setRGB(0, y2, width, 1, imageBits, 0, 1);
        }
        in.close();
        return bim;
    }

    public boolean getBlackIs1(Library library, HashMap decodeParmsDictionary) {
        Object blackIs1Obj = library.getObject(decodeParmsDictionary, BLACKIS1_KEY);
        if (blackIs1Obj != null) {
            if (blackIs1Obj instanceof Boolean) {
                return ((Boolean) blackIs1Obj).booleanValue();
            }
            if (blackIs1Obj instanceof String) {
                String blackIs1String = (String) blackIs1Obj;
                if (blackIs1String.equalsIgnoreCase("true") || blackIs1String.equalsIgnoreCase("t") || blackIs1String.equals("1")) {
                    return true;
                }
                if (!blackIs1String.equalsIgnoreCase("false") && !blackIs1String.equalsIgnoreCase(PdfOps.f_TOKEN) && blackIs1String.equals("0")) {
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean containsFilter(String[] searchFilterNames) {
        List filterNames = getFilterNames();
        if (filterNames == null) {
            return false;
        }
        for (Object filterName1 : filterNames) {
            String filterName = filterName1.toString();
            for (String search : searchFilterNames) {
                if (search.equals(filterName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isImageMask() {
        return this.library.getBoolean(this.entries, IMAGEMASK_KEY).booleanValue();
    }

    private boolean shouldUseCCITTFaxDecode() {
        return containsFilter(CCITTFAX_DECODE_FILTERS);
    }

    private boolean shouldUseDCTDecode() {
        return containsFilter(DCT_DECODE_FILTERS);
    }

    private boolean shouldUseJBIG2Decode() {
        return containsFilter(JBIG2_DECODE_FILTERS);
    }

    private boolean shouldUseJPXDecode() {
        return containsFilter(JPX_DECODE_FILTERS);
    }

    public static void forceJaiCcittFax(boolean enable) {
        forceJaiccittfax = enable;
    }

    public PColorSpace getColourSpace() {
        PColorSpace pColorSpace;
        synchronized (this.colorSpaceAssignmentLock) {
            pColorSpace = this.colourSpace;
        }
        return pColorSpace;
    }

    @Override // org.icepdf.core.pobjects.Stream, org.icepdf.core.pobjects.Dictionary
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Image stream= ");
        sb.append((Object) this.entries);
        if (getPObjectReference() != null) {
            sb.append(Constants.INDENT);
            sb.append((Object) getPObjectReference());
        }
        return sb.toString();
    }
}
