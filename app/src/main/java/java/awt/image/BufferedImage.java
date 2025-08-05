package java.awt.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import sun.awt.image.ByteComponentRaster;
import sun.awt.image.BytePackedRaster;
import sun.awt.image.IntegerComponentRaster;
import sun.awt.image.OffScreenImageSource;
import sun.awt.image.ShortComponentRaster;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/awt/image/BufferedImage.class */
public class BufferedImage extends Image implements WritableRenderedImage, Transparency {
    private int imageType;
    private ColorModel colorModel;
    private final WritableRaster raster;
    private OffScreenImageSource osis;
    private Hashtable<String, Object> properties;
    public static final int TYPE_CUSTOM = 0;
    public static final int TYPE_INT_RGB = 1;
    public static final int TYPE_INT_ARGB = 2;
    public static final int TYPE_INT_ARGB_PRE = 3;
    public static final int TYPE_INT_BGR = 4;
    public static final int TYPE_3BYTE_BGR = 5;
    public static final int TYPE_4BYTE_ABGR = 6;
    public static final int TYPE_4BYTE_ABGR_PRE = 7;
    public static final int TYPE_USHORT_565_RGB = 8;
    public static final int TYPE_USHORT_555_RGB = 9;
    public static final int TYPE_BYTE_GRAY = 10;
    public static final int TYPE_USHORT_GRAY = 11;
    public static final int TYPE_BYTE_BINARY = 12;
    public static final int TYPE_BYTE_INDEXED = 13;
    private static final int DCM_RED_MASK = 16711680;
    private static final int DCM_GREEN_MASK = 65280;
    private static final int DCM_BLUE_MASK = 255;
    private static final int DCM_ALPHA_MASK = -16777216;
    private static final int DCM_565_RED_MASK = 63488;
    private static final int DCM_565_GRN_MASK = 2016;
    private static final int DCM_565_BLU_MASK = 31;
    private static final int DCM_555_RED_MASK = 31744;
    private static final int DCM_555_GRN_MASK = 992;
    private static final int DCM_555_BLU_MASK = 31;
    private static final int DCM_BGR_RED_MASK = 255;
    private static final int DCM_BGR_GRN_MASK = 65280;
    private static final int DCM_BGR_BLU_MASK = 16711680;

    private static native void initIDs();

    static {
        ColorModel.loadLibraries();
        initIDs();
    }

    public BufferedImage(int i2, int i3, int i4) {
        this.imageType = 0;
        switch (i4) {
            case 1:
                this.colorModel = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255, 0);
                this.raster = this.colorModel.createCompatibleWritableRaster(i2, i3);
                break;
            case 2:
                this.colorModel = ColorModel.getRGBdefault();
                this.raster = this.colorModel.createCompatibleWritableRaster(i2, i3);
                break;
            case 3:
                this.colorModel = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, true, 3);
                this.raster = this.colorModel.createCompatibleWritableRaster(i2, i3);
                break;
            case 4:
                this.colorModel = new DirectColorModel(24, 255, NormalizerImpl.CC_MASK, 16711680);
                this.raster = this.colorModel.createCompatibleWritableRaster(i2, i3);
                break;
            case 5:
                this.colorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8}, false, false, 1, 0);
                this.raster = Raster.createInterleavedRaster(0, i2, i3, i2 * 3, 3, new int[]{2, 1, 0}, (Point) null);
                break;
            case 6:
                this.colorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, false, 3, 0);
                this.raster = Raster.createInterleavedRaster(0, i2, i3, i2 * 4, 4, new int[]{3, 2, 1, 0}, (Point) null);
                break;
            case 7:
                this.colorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, true, 3, 0);
                this.raster = Raster.createInterleavedRaster(0, i2, i3, i2 * 4, 4, new int[]{3, 2, 1, 0}, (Point) null);
                break;
            case 8:
                this.colorModel = new DirectColorModel(16, DCM_565_RED_MASK, DCM_565_GRN_MASK, 31);
                this.raster = this.colorModel.createCompatibleWritableRaster(i2, i3);
                break;
            case 9:
                this.colorModel = new DirectColorModel(15, DCM_555_RED_MASK, DCM_555_GRN_MASK, 31);
                this.raster = this.colorModel.createCompatibleWritableRaster(i2, i3);
                break;
            case 10:
                this.colorModel = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, true, 1, 0);
                this.raster = this.colorModel.createCompatibleWritableRaster(i2, i3);
                break;
            case 11:
                this.colorModel = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{16}, false, true, 1, 1);
                this.raster = this.colorModel.createCompatibleWritableRaster(i2, i3);
                break;
            case 12:
                byte[] bArr = {0, -1};
                this.colorModel = new IndexColorModel(1, 2, bArr, bArr, bArr);
                this.raster = Raster.createPackedRaster(0, i2, i3, 1, 1, (Point) null);
                break;
            case 13:
                int[] iArr = new int[256];
                int i5 = 0;
                for (int i6 = 0; i6 < 256; i6 += 51) {
                    for (int i7 = 0; i7 < 256; i7 += 51) {
                        for (int i8 = 0; i8 < 256; i8 += 51) {
                            int i9 = i5;
                            i5++;
                            iArr[i9] = (i6 << 16) | (i7 << 8) | i8;
                        }
                    }
                }
                int i10 = 256 / (256 - i5);
                int i11 = i10 * 3;
                while (i5 < 256) {
                    iArr[i5] = (i11 << 16) | (i11 << 8) | i11;
                    i11 += i10;
                    i5++;
                }
                this.colorModel = new IndexColorModel(8, 256, iArr, 0, false, -1, 0);
                this.raster = Raster.createInterleavedRaster(0, i2, i3, 1, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown image type " + i4);
        }
        this.imageType = i4;
    }

    public BufferedImage(int i2, int i3, int i4, IndexColorModel indexColorModel) {
        int i5;
        this.imageType = 0;
        if (indexColorModel.hasAlpha() && indexColorModel.isAlphaPremultiplied()) {
            throw new IllegalArgumentException("This image types do not have premultiplied alpha.");
        }
        switch (i4) {
            case 12:
                int mapSize = indexColorModel.getMapSize();
                if (mapSize <= 2) {
                    i5 = 1;
                } else if (mapSize <= 4) {
                    i5 = 2;
                } else if (mapSize <= 16) {
                    i5 = 4;
                } else {
                    throw new IllegalArgumentException("Color map for TYPE_BYTE_BINARY must have no more than 16 entries");
                }
                this.raster = Raster.createPackedRaster(0, i2, i3, 1, i5, (Point) null);
                break;
            case 13:
                this.raster = Raster.createInterleavedRaster(0, i2, i3, 1, null);
                break;
            default:
                throw new IllegalArgumentException("Invalid image type (" + i4 + ").  Image type must be either TYPE_BYTE_BINARY or  TYPE_BYTE_INDEXED");
        }
        if (!indexColorModel.isCompatibleRaster(this.raster)) {
            throw new IllegalArgumentException("Incompatible image type and IndexColorModel");
        }
        this.colorModel = indexColorModel;
        this.imageType = i4;
    }

    public BufferedImage(ColorModel colorModel, WritableRaster writableRaster, boolean z2, Hashtable<?, ?> hashtable) {
        this.imageType = 0;
        if (!colorModel.isCompatibleRaster(writableRaster)) {
            throw new IllegalArgumentException("Raster " + ((Object) writableRaster) + " is incompatible with ColorModel " + ((Object) colorModel));
        }
        if (writableRaster.minX != 0 || writableRaster.minY != 0) {
            throw new IllegalArgumentException("Raster " + ((Object) writableRaster) + " has minX or minY not equal to zero: " + writableRaster.minX + " " + writableRaster.minY);
        }
        this.colorModel = colorModel;
        this.raster = writableRaster;
        if (hashtable != null && !hashtable.isEmpty()) {
            this.properties = new Hashtable<>();
            for (Object obj : hashtable.keySet()) {
                if (obj instanceof String) {
                    this.properties.put((String) obj, hashtable.get(obj));
                }
            }
        }
        int numBands = writableRaster.getNumBands();
        boolean zIsAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        boolean zIsStandard = isStandard(colorModel, writableRaster);
        coerceData(z2);
        SampleModel sampleModel = writableRaster.getSampleModel();
        int type = colorModel.getColorSpace().getType();
        if (type != 5) {
            if (type == 6 && zIsStandard && (colorModel instanceof ComponentColorModel)) {
                if ((sampleModel instanceof ComponentSampleModel) && ((ComponentSampleModel) sampleModel).getPixelStride() != numBands) {
                    this.imageType = 0;
                    return;
                }
                if ((writableRaster instanceof ByteComponentRaster) && writableRaster.getNumBands() == 1 && colorModel.getComponentSize(0) == 8 && ((ByteComponentRaster) writableRaster).getPixelStride() == 1) {
                    this.imageType = 10;
                    return;
                } else {
                    if ((writableRaster instanceof ShortComponentRaster) && writableRaster.getNumBands() == 1 && colorModel.getComponentSize(0) == 16 && ((ShortComponentRaster) writableRaster).getPixelStride() == 1) {
                        this.imageType = 11;
                        return;
                    }
                    return;
                }
            }
            this.imageType = 0;
            return;
        }
        if ((writableRaster instanceof IntegerComponentRaster) && (numBands == 3 || numBands == 4)) {
            int pixelSize = colorModel.getPixelSize();
            if (((IntegerComponentRaster) writableRaster).getPixelStride() == 1 && zIsStandard && (colorModel instanceof DirectColorModel)) {
                if (pixelSize == 32 || pixelSize == 24) {
                    DirectColorModel directColorModel = (DirectColorModel) colorModel;
                    int redMask = directColorModel.getRedMask();
                    int greenMask = directColorModel.getGreenMask();
                    int blueMask = directColorModel.getBlueMask();
                    if (redMask == 16711680 && greenMask == 65280 && blueMask == 255) {
                        if (directColorModel.getAlphaMask() == -16777216) {
                            this.imageType = zIsAlphaPremultiplied ? 3 : 2;
                            return;
                        } else {
                            if (!directColorModel.hasAlpha()) {
                                this.imageType = 1;
                                return;
                            }
                            return;
                        }
                    }
                    if (redMask == 255 && greenMask == 65280 && blueMask == 16711680 && !directColorModel.hasAlpha()) {
                        this.imageType = 4;
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        if ((colorModel instanceof IndexColorModel) && numBands == 1 && zIsStandard && (!colorModel.hasAlpha() || !zIsAlphaPremultiplied)) {
            int pixelSize2 = ((IndexColorModel) colorModel).getPixelSize();
            if (writableRaster instanceof BytePackedRaster) {
                this.imageType = 12;
                return;
            } else {
                if ((writableRaster instanceof ByteComponentRaster) && ((ByteComponentRaster) writableRaster).getPixelStride() == 1 && pixelSize2 <= 8) {
                    this.imageType = 13;
                    return;
                }
                return;
            }
        }
        if ((writableRaster instanceof ShortComponentRaster) && (colorModel instanceof DirectColorModel) && zIsStandard && numBands == 3 && !colorModel.hasAlpha()) {
            DirectColorModel directColorModel2 = (DirectColorModel) colorModel;
            if (directColorModel2.getRedMask() == DCM_565_RED_MASK) {
                if (directColorModel2.getGreenMask() == DCM_565_GRN_MASK && directColorModel2.getBlueMask() == 31) {
                    this.imageType = 8;
                    return;
                }
                return;
            }
            if (directColorModel2.getRedMask() == DCM_555_RED_MASK && directColorModel2.getGreenMask() == DCM_555_GRN_MASK && directColorModel2.getBlueMask() == 31) {
                this.imageType = 9;
                return;
            }
            return;
        }
        if ((writableRaster instanceof ByteComponentRaster) && (colorModel instanceof ComponentColorModel) && zIsStandard && (writableRaster.getSampleModel() instanceof PixelInterleavedSampleModel)) {
            if (numBands == 3 || numBands == 4) {
                ComponentColorModel componentColorModel = (ComponentColorModel) colorModel;
                ByteComponentRaster byteComponentRaster = (ByteComponentRaster) writableRaster;
                int[] bandOffsets = ((PixelInterleavedSampleModel) writableRaster.getSampleModel()).getBandOffsets();
                if (componentColorModel.getNumComponents() != numBands) {
                    throw new RasterFormatException("Number of components in ColorModel (" + componentColorModel.getNumComponents() + ") does not match # in  Raster (" + numBands + ")");
                }
                int[] componentSize = componentColorModel.getComponentSize();
                boolean z3 = true;
                int i2 = 0;
                while (true) {
                    if (i2 >= numBands) {
                        break;
                    }
                    if (componentSize[i2] == 8) {
                        i2++;
                    } else {
                        z3 = false;
                        break;
                    }
                }
                if (z3 && byteComponentRaster.getPixelStride() == numBands && bandOffsets[0] == numBands - 1 && bandOffsets[1] == numBands - 2 && bandOffsets[2] == numBands - 3) {
                    if (numBands == 3 && !componentColorModel.hasAlpha()) {
                        this.imageType = 5;
                    } else if (bandOffsets[3] == 0 && componentColorModel.hasAlpha()) {
                        this.imageType = zIsAlphaPremultiplied ? 7 : 6;
                    }
                }
            }
        }
    }

    private static boolean isStandard(ColorModel colorModel, WritableRaster writableRaster) {
        final Class<?> cls = colorModel.getClass();
        final Class<?> cls2 = writableRaster.getClass();
        final Class<?> cls3 = writableRaster.getSampleModel().getClass();
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.awt.image.BufferedImage.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() {
                ClassLoader classLoader = System.class.getClassLoader();
                return Boolean.valueOf(cls.getClassLoader() == classLoader && cls3.getClassLoader() == classLoader && cls2.getClassLoader() == classLoader);
            }
        })).booleanValue();
    }

    public int getType() {
        return this.imageType;
    }

    @Override // java.awt.image.RenderedImage
    public ColorModel getColorModel() {
        return this.colorModel;
    }

    public WritableRaster getRaster() {
        return this.raster;
    }

    public WritableRaster getAlphaRaster() {
        return this.colorModel.getAlphaRaster(this.raster);
    }

    public int getRGB(int i2, int i3) {
        return this.colorModel.getRGB(this.raster.getDataElements(i2, i3, null));
    }

    public int[] getRGB(int i2, int i3, int i4, int i5, int[] iArr, int i6, int i7) {
        double[] dArr;
        int i8 = i6;
        int numBands = this.raster.getNumBands();
        int dataType = this.raster.getDataBuffer().getDataType();
        switch (dataType) {
            case 0:
                dArr = new byte[numBands];
                break;
            case 1:
                dArr = new short[numBands];
                break;
            case 2:
            default:
                throw new IllegalArgumentException("Unknown data buffer type: " + dataType);
            case 3:
                dArr = new int[numBands];
                break;
            case 4:
                dArr = new float[numBands];
                break;
            case 5:
                dArr = new double[numBands];
                break;
        }
        if (iArr == null) {
            iArr = new int[i6 + (i5 * i7)];
        }
        int i9 = i3;
        while (i9 < i3 + i5) {
            int i10 = i8;
            for (int i11 = i2; i11 < i2 + i4; i11++) {
                int i12 = i10;
                i10++;
                iArr[i12] = this.colorModel.getRGB(this.raster.getDataElements(i11, i9, dArr));
            }
            i9++;
            i8 += i7;
        }
        return iArr;
    }

    public synchronized void setRGB(int i2, int i3, int i4) {
        this.raster.setDataElements(i2, i3, this.colorModel.getDataElements(i4, null));
    }

    public void setRGB(int i2, int i3, int i4, int i5, int[] iArr, int i6, int i7) {
        int i8 = i6;
        Object dataElements = null;
        int i9 = i3;
        while (i9 < i3 + i5) {
            int i10 = i8;
            for (int i11 = i2; i11 < i2 + i4; i11++) {
                int i12 = i10;
                i10++;
                dataElements = this.colorModel.getDataElements(iArr[i12], dataElements);
                this.raster.setDataElements(i11, i9, dataElements);
            }
            i9++;
            i8 += i7;
        }
    }

    @Override // java.awt.image.RenderedImage
    public int getWidth() {
        return this.raster.getWidth();
    }

    @Override // java.awt.image.RenderedImage
    public int getHeight() {
        return this.raster.getHeight();
    }

    @Override // java.awt.Image
    public int getWidth(ImageObserver imageObserver) {
        return this.raster.getWidth();
    }

    @Override // java.awt.Image
    public int getHeight(ImageObserver imageObserver) {
        return this.raster.getHeight();
    }

    @Override // java.awt.Image
    public ImageProducer getSource() {
        if (this.osis == null) {
            if (this.properties == null) {
                this.properties = new Hashtable<>();
            }
            this.osis = new OffScreenImageSource(this, this.properties);
        }
        return this.osis;
    }

    @Override // java.awt.Image
    public Object getProperty(String str, ImageObserver imageObserver) {
        return getProperty(str);
    }

    @Override // java.awt.image.RenderedImage
    public Object getProperty(String str) {
        if (str == null) {
            throw new NullPointerException("null property name is not allowed");
        }
        if (this.properties == null) {
            return Image.UndefinedProperty;
        }
        Object obj = this.properties.get(str);
        if (obj == null) {
            obj = Image.UndefinedProperty;
        }
        return obj;
    }

    @Override // java.awt.Image
    public Graphics getGraphics() {
        return createGraphics();
    }

    public Graphics2D createGraphics() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(this);
    }

    public BufferedImage getSubimage(int i2, int i3, int i4, int i5) {
        return new BufferedImage(this.colorModel, this.raster.createWritableChild(i2, i3, i4, i5, 0, 0, null), this.colorModel.isAlphaPremultiplied(), this.properties);
    }

    public boolean isAlphaPremultiplied() {
        return this.colorModel.isAlphaPremultiplied();
    }

    public void coerceData(boolean z2) {
        if (this.colorModel.hasAlpha() && this.colorModel.isAlphaPremultiplied() != z2) {
            this.colorModel = this.colorModel.coerceData(this.raster, z2);
        }
    }

    public String toString() {
        return "BufferedImage@" + Integer.toHexString(hashCode()) + ": type = " + this.imageType + " " + ((Object) this.colorModel) + " " + ((Object) this.raster);
    }

    @Override // java.awt.image.RenderedImage
    public Vector<RenderedImage> getSources() {
        return null;
    }

    @Override // java.awt.image.RenderedImage
    public String[] getPropertyNames() {
        if (this.properties == null || this.properties.isEmpty()) {
            return null;
        }
        Set<String> setKeySet = this.properties.keySet();
        return (String[]) setKeySet.toArray(new String[setKeySet.size()]);
    }

    @Override // java.awt.image.RenderedImage
    public int getMinX() {
        return this.raster.getMinX();
    }

    @Override // java.awt.image.RenderedImage
    public int getMinY() {
        return this.raster.getMinY();
    }

    @Override // java.awt.image.RenderedImage
    public SampleModel getSampleModel() {
        return this.raster.getSampleModel();
    }

    @Override // java.awt.image.RenderedImage
    public int getNumXTiles() {
        return 1;
    }

    @Override // java.awt.image.RenderedImage
    public int getNumYTiles() {
        return 1;
    }

    @Override // java.awt.image.RenderedImage
    public int getMinTileX() {
        return 0;
    }

    @Override // java.awt.image.RenderedImage
    public int getMinTileY() {
        return 0;
    }

    @Override // java.awt.image.RenderedImage
    public int getTileWidth() {
        return this.raster.getWidth();
    }

    @Override // java.awt.image.RenderedImage
    public int getTileHeight() {
        return this.raster.getHeight();
    }

    @Override // java.awt.image.RenderedImage
    public int getTileGridXOffset() {
        return this.raster.getSampleModelTranslateX();
    }

    @Override // java.awt.image.RenderedImage
    public int getTileGridYOffset() {
        return this.raster.getSampleModelTranslateY();
    }

    @Override // java.awt.image.RenderedImage
    public Raster getTile(int i2, int i3) {
        if (i2 == 0 && i3 == 0) {
            return this.raster;
        }
        throw new ArrayIndexOutOfBoundsException("BufferedImages only have one tile with index 0,0");
    }

    @Override // java.awt.image.RenderedImage
    public Raster getData() {
        int width = this.raster.getWidth();
        int height = this.raster.getHeight();
        int minX = this.raster.getMinX();
        int minY = this.raster.getMinY();
        WritableRaster writableRasterCreateWritableRaster = Raster.createWritableRaster(this.raster.getSampleModel(), new Point(this.raster.getSampleModelTranslateX(), this.raster.getSampleModelTranslateY()));
        Object dataElements = null;
        for (int i2 = minY; i2 < minY + height; i2++) {
            dataElements = this.raster.getDataElements(minX, i2, width, 1, dataElements);
            writableRasterCreateWritableRaster.setDataElements(minX, i2, width, 1, dataElements);
        }
        return writableRasterCreateWritableRaster;
    }

    @Override // java.awt.image.RenderedImage
    public Raster getData(Rectangle rectangle) {
        WritableRaster writableRasterCreateWritableRaster = Raster.createWritableRaster(this.raster.getSampleModel().createCompatibleSampleModel(rectangle.width, rectangle.height), rectangle.getLocation());
        int i2 = rectangle.width;
        int i3 = rectangle.height;
        int i4 = rectangle.f12372x;
        int i5 = rectangle.f12373y;
        Object dataElements = null;
        for (int i6 = i5; i6 < i5 + i3; i6++) {
            dataElements = this.raster.getDataElements(i4, i6, i2, 1, dataElements);
            writableRasterCreateWritableRaster.setDataElements(i4, i6, i2, 1, dataElements);
        }
        return writableRasterCreateWritableRaster;
    }

    @Override // java.awt.image.RenderedImage
    public WritableRaster copyData(WritableRaster writableRaster) {
        if (writableRaster == null) {
            return (WritableRaster) getData();
        }
        int width = writableRaster.getWidth();
        int height = writableRaster.getHeight();
        int minX = writableRaster.getMinX();
        int minY = writableRaster.getMinY();
        Object dataElements = null;
        for (int i2 = minY; i2 < minY + height; i2++) {
            dataElements = this.raster.getDataElements(minX, i2, width, 1, dataElements);
            writableRaster.setDataElements(minX, i2, width, 1, dataElements);
        }
        return writableRaster;
    }

    @Override // java.awt.image.WritableRenderedImage
    public void setData(Raster raster) {
        int[] pixels = null;
        Rectangle rectangleIntersection = new Rectangle(raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight()).intersection(new Rectangle(0, 0, this.raster.width, this.raster.height));
        if (rectangleIntersection.isEmpty()) {
            return;
        }
        int i2 = rectangleIntersection.width;
        int i3 = rectangleIntersection.height;
        int i4 = rectangleIntersection.f12372x;
        int i5 = rectangleIntersection.f12373y;
        for (int i6 = i5; i6 < i5 + i3; i6++) {
            pixels = raster.getPixels(i4, i6, i2, 1, pixels);
            this.raster.setPixels(i4, i6, i2, 1, pixels);
        }
    }

    @Override // java.awt.image.WritableRenderedImage
    public void addTileObserver(TileObserver tileObserver) {
    }

    @Override // java.awt.image.WritableRenderedImage
    public void removeTileObserver(TileObserver tileObserver) {
    }

    @Override // java.awt.image.WritableRenderedImage
    public boolean isTileWritable(int i2, int i3) {
        if (i2 == 0 && i3 == 0) {
            return true;
        }
        throw new IllegalArgumentException("Only 1 tile in image");
    }

    @Override // java.awt.image.WritableRenderedImage
    public Point[] getWritableTileIndices() {
        return new Point[]{new Point(0, 0)};
    }

    @Override // java.awt.image.WritableRenderedImage
    public boolean hasTileWriters() {
        return true;
    }

    @Override // java.awt.image.WritableRenderedImage
    public WritableRaster getWritableTile(int i2, int i3) {
        return this.raster;
    }

    @Override // java.awt.image.WritableRenderedImage
    public void releaseWritableTile(int i2, int i3) {
    }

    @Override // java.awt.Transparency
    public int getTransparency() {
        return this.colorModel.getTransparency();
    }
}
