package com.sun.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteGrayAlpha;
import com.sun.javafx.image.impl.ByteGrayAlphaPre;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.ByteRgba;
import com.sun.javafx.tk.PlatformImage;
import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BufferUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

/* loaded from: jfxrt.jar:com/sun/prism/Image.class */
public class Image implements PlatformImage {
    static final WritablePixelFormat<ByteBuffer> FX_ByteBgraPre_FORMAT = javafx.scene.image.PixelFormat.getByteBgraPreInstance();
    static final WritablePixelFormat<IntBuffer> FX_IntArgbPre_FORMAT = javafx.scene.image.PixelFormat.getIntArgbPreInstance();
    static final javafx.scene.image.PixelFormat<ByteBuffer> FX_ByteRgb_FORMAT = javafx.scene.image.PixelFormat.getByteRgbInstance();
    private final Buffer pixelBuffer;
    private final int minX;
    private final int minY;
    private final int width;
    private final int height;
    private final int scanlineStride;
    private final PixelFormat pixelFormat;
    private final float pixelScale;
    int[] serial;
    private Accessor<?> pixelaccessor;
    static javafx.scene.image.PixelFormat<ByteBuffer> FX_ByteGray_FORMAT;

    public static Image fromIntArgbPreData(int[] pixels, int width, int height) {
        return new Image(PixelFormat.INT_ARGB_PRE, pixels, width, height);
    }

    public static Image fromIntArgbPreData(IntBuffer pixels, int width, int height) {
        return new Image(PixelFormat.INT_ARGB_PRE, pixels, width, height);
    }

    public static Image fromIntArgbPreData(IntBuffer pixels, int width, int height, int scanlineStride) {
        return new Image(PixelFormat.INT_ARGB_PRE, pixels, width, height, 0, 0, scanlineStride);
    }

    public static Image fromIntArgbPreData(IntBuffer pixels, int width, int height, int scanlineStride, float pixelScale) {
        return new Image(PixelFormat.INT_ARGB_PRE, pixels, width, height, 0, 0, scanlineStride, pixelScale);
    }

    public static Image fromByteBgraPreData(byte[] pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, pixels, width, height);
    }

    public static Image fromByteBgraPreData(byte[] pixels, int width, int height, float pixelScale) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, ByteBuffer.wrap(pixels), width, height, 0, 0, 0, pixelScale);
    }

    public static Image fromByteBgraPreData(ByteBuffer pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, pixels, width, height);
    }

    public static Image fromByteBgraPreData(ByteBuffer pixels, int width, int height, int scanlineStride) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, pixels, width, height, 0, 0, scanlineStride);
    }

    public static Image fromByteBgraPreData(ByteBuffer pixels, int width, int height, int scanlineStride, float pixelScale) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, pixels, width, height, 0, 0, scanlineStride, pixelScale);
    }

    public static Image fromByteRgbData(byte[] pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_RGB, pixels, width, height);
    }

    public static Image fromByteRgbData(ByteBuffer pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_RGB, pixels, width, height);
    }

    public static Image fromByteRgbData(ByteBuffer pixels, int width, int height, int scanlineStride) {
        return new Image(PixelFormat.BYTE_RGB, pixels, width, height, 0, 0, scanlineStride);
    }

    public static Image fromByteRgbData(ByteBuffer pixels, int width, int height, int scanlineStride, float pixelScale) {
        return new Image(PixelFormat.BYTE_RGB, pixels, width, height, 0, 0, scanlineStride, pixelScale);
    }

    public static Image fromByteGrayData(byte[] pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_GRAY, pixels, width, height);
    }

    public static Image fromByteGrayData(ByteBuffer pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_GRAY, pixels, width, height);
    }

    public static Image fromByteGrayData(ByteBuffer pixels, int width, int height, int scanlineStride) {
        return new Image(PixelFormat.BYTE_GRAY, pixels, width, height, 0, 0, scanlineStride);
    }

    public static Image fromByteGrayData(ByteBuffer pixels, int width, int height, int scanlineStride, float pixelScale) {
        return new Image(PixelFormat.BYTE_GRAY, pixels, width, height, 0, 0, scanlineStride, pixelScale);
    }

    public static Image fromByteAlphaData(byte[] pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_ALPHA, pixels, width, height);
    }

    public static Image fromByteAlphaData(ByteBuffer pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_ALPHA, pixels, width, height);
    }

    public static Image fromByteAlphaData(ByteBuffer pixels, int width, int height, int scanlineStride) {
        return new Image(PixelFormat.BYTE_ALPHA, pixels, width, height, 0, 0, scanlineStride);
    }

    public static Image fromByteApple422Data(byte[] pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_APPLE_422, pixels, width, height);
    }

    public static Image fromByteApple422Data(ByteBuffer pixels, int width, int height) {
        return new Image(PixelFormat.BYTE_APPLE_422, pixels, width, height);
    }

    public static Image fromByteApple422Data(ByteBuffer pixels, int width, int height, int scanlineStride) {
        return new Image(PixelFormat.BYTE_APPLE_422, pixels, width, height, 0, 0, scanlineStride);
    }

    public static Image fromFloatMapData(FloatBuffer pixels, int width, int height) {
        return new Image(PixelFormat.FLOAT_XYZW, pixels, width, height);
    }

    public static Image convertImageFrame(ImageFrame frame) {
        ByteBuffer buffer = (ByteBuffer) frame.getImageData();
        ImageStorage.ImageType type = frame.getImageType();
        int w2 = frame.getWidth();
        int h2 = frame.getHeight();
        int scanBytes = frame.getStride();
        float ps = frame.getPixelScale();
        switch (type) {
            case GRAY:
                return fromByteGrayData(buffer, w2, h2, scanBytes, ps);
            case RGB:
                return fromByteRgbData(buffer, w2, h2, scanBytes, ps);
            case RGBA:
                ByteBgra.ToByteBgraPreConverter().convert((ByteToBytePixelConverter) buffer, 0, scanBytes, (int) buffer, 0, scanBytes, w2, h2);
            case RGBA_PRE:
                ByteRgba.ToByteBgraConverter().convert((ByteToBytePixelConverter) buffer, 0, scanBytes, (int) buffer, 0, scanBytes, w2, h2);
                return fromByteBgraPreData(buffer, w2, h2, scanBytes, ps);
            case GRAY_ALPHA:
                ByteGrayAlpha.ToByteGrayAlphaPreConverter().convert((ByteToBytePixelConverter) buffer, 0, scanBytes, (int) buffer, 0, scanBytes, w2, h2);
            case GRAY_ALPHA_PRE:
                if (scanBytes != w2 * 2) {
                    throw new AssertionError((Object) "Bad stride for GRAY_ALPHA");
                }
                byte[] newbuf = new byte[w2 * h2 * 4];
                ByteGrayAlphaPre.ToByteBgraPreConverter().convert(buffer, 0, scanBytes, newbuf, 0, w2 * 4, w2, h2);
                return fromByteBgraPreData(newbuf, w2, h2, ps);
            default:
                throw new RuntimeException("Unknown image type: " + ((Object) type));
        }
    }

    private Image(PixelFormat pixelFormat, int[] pixels, int width, int height) {
        this(pixelFormat, IntBuffer.wrap(pixels), width, height, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, byte[] pixels, int width, int height) {
        this(pixelFormat, ByteBuffer.wrap(pixels), width, height, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer pixelBuffer, int width, int height) {
        this(pixelFormat, pixelBuffer, width, height, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer pixelBuffer, int width, int height, int minX, int minY, int scanlineStride) {
        this(pixelFormat, pixelBuffer, width, height, minX, minY, scanlineStride, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer pixelBuffer, int width, int height, int minX, int minY, int scanlineStride, float pixelScale) {
        this.serial = new int[1];
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format not supported " + pixelFormat.name());
        }
        scanlineStride = scanlineStride == 0 ? width * pixelFormat.getBytesPerPixelUnit() : scanlineStride;
        if (pixelBuffer == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Image dimensions must be > 0");
        }
        if (minX < 0 || minY < 0) {
            throw new IllegalArgumentException("Image minX and minY must be >= 0");
        }
        if ((minX + width) * pixelFormat.getBytesPerPixelUnit() > scanlineStride) {
            throw new IllegalArgumentException("Image scanlineStride is too small");
        }
        if (scanlineStride % pixelFormat.getBytesPerPixelUnit() != 0) {
            throw new IllegalArgumentException("Image scanlineStride must be a multiple of the pixel stride");
        }
        this.pixelFormat = pixelFormat;
        this.pixelBuffer = pixelBuffer;
        this.width = width;
        this.height = height;
        this.minX = minX;
        this.minY = minY;
        this.scanlineStride = scanlineStride;
        this.pixelScale = pixelScale;
    }

    public PixelFormat getPixelFormat() {
        return this.pixelFormat;
    }

    public PixelFormat.DataType getDataType() {
        return this.pixelFormat.getDataType();
    }

    public int getBytesPerPixelUnit() {
        return this.pixelFormat.getBytesPerPixelUnit();
    }

    public Buffer getPixelBuffer() {
        return this.pixelBuffer;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public float getPixelScale() {
        return this.pixelScale;
    }

    public int getRowLength() {
        return this.scanlineStride / this.pixelFormat.getBytesPerPixelUnit();
    }

    public boolean isTightlyPacked() {
        return this.minX == 0 && this.minY == 0 && this.width == getRowLength();
    }

    public Image createSubImage(int x2, int y2, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            throw new IllegalArgumentException("Subimage dimensions must be > 0");
        }
        if (x2 < 0 || y2 < 0) {
            throw new IllegalArgumentException("Subimage minX and minY must be >= 0");
        }
        if (x2 + w2 > this.width) {
            throw new IllegalArgumentException("Subimage minX+width must be <= width of parent image");
        }
        if (y2 + h2 > this.height) {
            throw new IllegalArgumentException("Subimage minY+height must be <= height of parent image");
        }
        Image subimg = new Image(this.pixelFormat, this.pixelBuffer, w2, h2, this.minX + x2, this.minY + y2, this.scanlineStride);
        subimg.serial = this.serial;
        return subimg;
    }

    public Image createPackedCopy() {
        int newBytesPerRow = this.width * this.pixelFormat.getBytesPerPixelUnit();
        Buffer newBuf = createPackedBuffer(this.pixelBuffer, this.pixelFormat, this.minX, this.minY, this.width, this.height, this.scanlineStride);
        return new Image(this.pixelFormat, newBuf, this.width, this.height, 0, 0, newBytesPerRow);
    }

    public Image createPackedCopyIfNeeded() {
        int newBytesPerRow = this.width * this.pixelFormat.getBytesPerPixelUnit();
        if (newBytesPerRow == this.scanlineStride && this.minX == 0 && this.minY == 0) {
            return this;
        }
        return createPackedCopy();
    }

    public static Buffer createPackedBuffer(Buffer pixels, PixelFormat format, int minX, int minY, int width, int height, int scanlineStride) {
        Buffer newBuf;
        if (scanlineStride % format.getBytesPerPixelUnit() != 0) {
            throw new IllegalArgumentException("Image scanlineStride must be a multiple of the pixel stride");
        }
        if (format == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format unsupported " + ((Object) format));
        }
        int elemsPerPixel = format.getElemsPerPixelUnit();
        int oldRowLength = scanlineStride / format.getBytesPerPixelUnit();
        int oldElemsPerRow = oldRowLength * elemsPerPixel;
        int newElemsPerRow = width * elemsPerPixel;
        int newSizeInElems = newElemsPerRow * height;
        int oldpos = (minX * elemsPerPixel) + (minY * oldElemsPerRow);
        int newpos = 0;
        switch (format.getDataType()) {
            case BYTE:
                ByteBuffer oldbbuf = (ByteBuffer) pixels;
                ByteBuffer newbbuf = BufferUtil.newByteBuffer(newSizeInElems);
                for (int y2 = 0; y2 < height; y2++) {
                    oldbbuf.limit(oldpos + newElemsPerRow);
                    oldbbuf.position(oldpos);
                    newbbuf.limit(newpos + newElemsPerRow);
                    newbbuf.position(newpos);
                    newbbuf.put(oldbbuf);
                    oldpos += oldElemsPerRow;
                    newpos += newElemsPerRow;
                }
                newBuf = newbbuf;
                break;
            case INT:
                IntBuffer oldibuf = (IntBuffer) pixels;
                IntBuffer newibuf = BufferUtil.newIntBuffer(newSizeInElems);
                for (int y3 = 0; y3 < height; y3++) {
                    oldibuf.limit(oldpos + newElemsPerRow);
                    oldibuf.position(oldpos);
                    newibuf.limit(newpos + newElemsPerRow);
                    newibuf.position(newpos);
                    newibuf.put(oldibuf);
                    oldpos += oldElemsPerRow;
                    newpos += newElemsPerRow;
                }
                newBuf = newibuf;
                break;
            case FLOAT:
                FloatBuffer oldfbuf = (FloatBuffer) pixels;
                FloatBuffer newfbuf = BufferUtil.newFloatBuffer(newSizeInElems);
                for (int y4 = 0; y4 < height; y4++) {
                    oldfbuf.limit(oldpos + newElemsPerRow);
                    oldfbuf.position(oldpos);
                    newfbuf.limit(newpos + newElemsPerRow);
                    newfbuf.position(newpos);
                    newfbuf.put(oldfbuf);
                    oldpos += oldElemsPerRow;
                    newpos += newElemsPerRow;
                }
                newBuf = newfbuf;
                break;
            default:
                throw new InternalError("Unknown data type");
        }
        pixels.limit(pixels.capacity());
        pixels.rewind();
        newBuf.limit(newBuf.capacity());
        newBuf.rewind();
        return newBuf;
    }

    public Image iconify(ByteBuffer iconBuffer, int twidth, int theight) {
        ByteToIntPixelConverter converter;
        if (this.pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format not supported " + ((Object) this.pixelFormat));
        }
        int tnumBands = getBytesPerPixelUnit();
        int tscanlineStride = twidth * tnumBands;
        if (tnumBands == 1) {
            converter = ByteGray.ToIntArgbPreConverter();
        } else if (this.pixelFormat == PixelFormat.BYTE_BGRA_PRE) {
            converter = ByteBgraPre.ToIntArgbPreConverter();
        } else {
            converter = ByteRgb.ToIntArgbPreConverter();
        }
        int[] newImage = new int[twidth * theight];
        converter.convert(iconBuffer, 0, tscanlineStride, newImage, 0, twidth, twidth, theight);
        return new Image(PixelFormat.INT_ARGB_PRE, newImage, twidth, theight);
    }

    public String toString() {
        return super.toString() + " [format=" + ((Object) this.pixelFormat) + " width=" + this.width + " height=" + this.height + " scanlineStride=" + this.scanlineStride + " minX=" + this.minX + " minY=" + this.minY + " pixelBuffer=" + ((Object) this.pixelBuffer) + " bpp=" + getBytesPerPixelUnit() + "]";
    }

    public int getSerial() {
        return this.serial[0];
    }

    public Image promoteByteRgbToByteBgra() {
        ByteBuffer oldbuf = (ByteBuffer) this.pixelBuffer;
        ByteBuffer newbuf = ByteBuffer.allocate(this.width * this.height * 4);
        int oldpos = (this.minY * this.scanlineStride) + (this.minX * 3);
        ByteRgb.ToByteBgraPreConverter().convert((ByteToBytePixelConverter) oldbuf, oldpos, this.scanlineStride, (int) newbuf, 0, this.width * 4, this.width, this.height);
        return new Image(PixelFormat.BYTE_BGRA_PRE, newbuf, this.width, this.height, 0, 0, this.width * 4, getPixelScale());
    }

    private Accessor<?> getPixelAccessor() {
        if (this.pixelaccessor == null) {
            switch (getPixelFormat()) {
                case BYTE_ALPHA:
                case BYTE_APPLE_422:
                case FLOAT_XYZW:
                case MULTI_YCbCr_420:
                default:
                    this.pixelaccessor = new UnsupportedAccess();
                    break;
                case BYTE_GRAY:
                    this.pixelaccessor = new ByteAccess(getGrayFXPixelFormat(), ByteGray.getter, null, (ByteBuffer) this.pixelBuffer, 1);
                    break;
                case BYTE_RGB:
                    this.pixelaccessor = new ByteRgbAccess((ByteBuffer) this.pixelBuffer);
                    break;
                case BYTE_BGRA_PRE:
                    this.pixelaccessor = new ByteAccess(FX_ByteBgraPre_FORMAT, (ByteBuffer) this.pixelBuffer, 4);
                    break;
                case INT_ARGB_PRE:
                    this.pixelaccessor = new IntAccess(FX_IntArgbPre_FORMAT, (IntBuffer) this.pixelBuffer);
                    break;
            }
            if (this.pixelScale != 1.0f) {
                this.pixelaccessor = new ScaledAccessor(this.pixelaccessor, this.pixelScale);
            }
        }
        return this.pixelaccessor;
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public javafx.scene.image.PixelFormat<?> getPlatformPixelFormat() {
        return getPixelAccessor().getPlatformPixelFormat();
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public boolean isWritable() {
        return getPixelAccessor().isWritable();
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public PlatformImage promoteToWritableImage() {
        return getPixelAccessor().promoteToWritableImage();
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public int getArgb(int x2, int y2) {
        return getPixelAccessor().getArgb(x2, y2);
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public void setArgb(int x2, int y2, int argb) {
        getPixelAccessor().setArgb(x2, y2, argb);
        int[] iArr = this.serial;
        iArr[0] = iArr[0] + 1;
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public <T extends Buffer> void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<T> pixelformat, T pixels, int scanlineBytes) {
        getPixelAccessor().getPixels(x2, y2, w2, h2, pixelformat, pixels, scanlineBytes);
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<ByteBuffer> pixelformat, byte[] pixels, int offset, int scanlineBytes) {
        getPixelAccessor().getPixels(x2, y2, w2, h2, pixelformat, pixels, offset, scanlineBytes);
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<IntBuffer> pixelformat, int[] pixels, int offset, int scanlineInts) {
        getPixelAccessor().getPixels(x2, y2, w2, h2, pixelformat, pixels, offset, scanlineInts);
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public <T extends Buffer> void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<T> pixelformat, T pixels, int scanlineBytes) {
        getPixelAccessor().setPixels(x2, y2, w2, h2, (javafx.scene.image.PixelFormat<javafx.scene.image.PixelFormat<T>>) pixelformat, (javafx.scene.image.PixelFormat<T>) pixels, scanlineBytes);
        int[] iArr = this.serial;
        iArr[0] = iArr[0] + 1;
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<ByteBuffer> pixelformat, byte[] pixels, int offset, int scanlineBytes) {
        getPixelAccessor().setPixels(x2, y2, w2, h2, pixelformat, pixels, offset, scanlineBytes);
        int[] iArr = this.serial;
        iArr[0] = iArr[0] + 1;
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<IntBuffer> pixelformat, int[] pixels, int offset, int scanlineInts) {
        getPixelAccessor().setPixels(x2, y2, w2, h2, pixelformat, pixels, offset, scanlineInts);
        int[] iArr = this.serial;
        iArr[0] = iArr[0] + 1;
    }

    @Override // com.sun.javafx.tk.PlatformImage
    public void setPixels(int dstx, int dsty, int w2, int h2, PixelReader reader, int srcx, int srcy) {
        getPixelAccessor().setPixels(dstx, dsty, w2, h2, reader, srcx, srcy);
        int[] iArr = this.serial;
        iArr[0] = iArr[0] + 1;
    }

    public boolean isOpaque() {
        return this.pixelFormat.isOpaque();
    }

    /* loaded from: jfxrt.jar:com/sun/prism/Image$Accessor.class */
    abstract class Accessor<I extends Buffer> {
        public abstract int getArgb(int i2, int i3);

        public abstract void setArgb(int i2, int i3, int i4);

        public abstract javafx.scene.image.PixelFormat<I> getPlatformPixelFormat();

        public abstract boolean isWritable();

        public abstract PlatformImage promoteToWritableImage();

        public abstract <T extends Buffer> void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<T> writablePixelFormat, T t2, int i6);

        public abstract void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] bArr, int i6, int i7);

        public abstract void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] iArr, int i6, int i7);

        public abstract <T extends Buffer> void setPixels(int i2, int i3, int i4, int i5, javafx.scene.image.PixelFormat<T> pixelFormat, T t2, int i6);

        public abstract void setPixels(int i2, int i3, int i4, int i5, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] bArr, int i6, int i7);

        public abstract void setPixels(int i2, int i3, int i4, int i5, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] iArr, int i6, int i7);

        public abstract void setPixels(int i2, int i3, int i4, int i5, PixelReader pixelReader, int i6, int i7);

        Accessor() {
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/Image$ScaledAccessor.class */
    class ScaledAccessor<I extends Buffer> extends Accessor<I> {
        Accessor<I> theDelegate;
        float pixelScale;

        ScaledAccessor(Accessor<I> delegate, float pixelScale) {
            super();
            this.theDelegate = delegate;
            this.pixelScale = pixelScale;
        }

        private int scale(int v2) {
            return (int) ((v2 + 0.5f) * this.pixelScale);
        }

        @Override // com.sun.prism.Image.Accessor
        public int getArgb(int x2, int y2) {
            return this.theDelegate.getArgb(scale(x2), scale(y2));
        }

        @Override // com.sun.prism.Image.Accessor
        public void setArgb(int x2, int y2, int argb) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override // com.sun.prism.Image.Accessor
        public javafx.scene.image.PixelFormat<I> getPlatformPixelFormat() {
            return this.theDelegate.getPlatformPixelFormat();
        }

        @Override // com.sun.prism.Image.Accessor
        public boolean isWritable() {
            return this.theDelegate.isWritable();
        }

        @Override // com.sun.prism.Image.Accessor
        public PlatformImage promoteToWritableImage() {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override // com.sun.prism.Image.Accessor
        public <T extends Buffer> void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<T> pixelformat, T pixels, int scanlineElems) {
            PixelSetter<T> setter = PixelUtils.getSetter(pixelformat);
            int offset = pixels.position();
            int numElem = setter.getNumElements();
            for (int rely = 0; rely < h2; rely++) {
                int sy = scale(y2 + rely);
                int rowoff = offset;
                for (int relx = 0; relx < w2; relx++) {
                    int sx = scale(x2 + relx);
                    setter.setArgb(pixels, rowoff, this.theDelegate.getArgb(sx, sy));
                    rowoff += numElem;
                }
                offset += scanlineElems;
            }
        }

        @Override // com.sun.prism.Image.Accessor
        public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<ByteBuffer> pixelformat, byte[] pixels, int offset, int scanlineBytes) {
            ByteBuffer bb2 = ByteBuffer.wrap(pixels);
            bb2.position(offset);
            getPixels(x2, y2, w2, h2, pixelformat, bb2, scanlineBytes);
        }

        @Override // com.sun.prism.Image.Accessor
        public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<IntBuffer> pixelformat, int[] pixels, int offset, int scanlineInts) {
            IntBuffer ib = IntBuffer.wrap(pixels);
            ib.position(offset);
            getPixels(x2, y2, w2, h2, pixelformat, ib, scanlineInts);
        }

        @Override // com.sun.prism.Image.Accessor
        public <T extends Buffer> void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<T> pixelformat, T pixels, int scanlineElems) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<ByteBuffer> pixelformat, byte[] pixels, int offset, int scanlineBytes) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<IntBuffer> pixelformat, int[] pixels, int offset, int scanlineInts) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int dstx, int dsty, int w2, int h2, PixelReader reader, int srcx, int srcy) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }
    }

    static <I extends Buffer> PixelSetter<I> getSetterIfWritable(javafx.scene.image.PixelFormat<I> theFormat) {
        if (theFormat instanceof WritablePixelFormat) {
            return PixelUtils.getSetter((WritablePixelFormat) theFormat);
        }
        return null;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/Image$BaseAccessor.class */
    abstract class BaseAccessor<I extends Buffer> extends Accessor<I> {
        javafx.scene.image.PixelFormat<I> theFormat;
        PixelGetter<I> theGetter;
        PixelSetter<I> theSetter;
        I theBuffer;
        int pixelElems;
        int scanlineElems;
        int offsetElems;

        BaseAccessor(Image this$0, javafx.scene.image.PixelFormat<I> theFormat, I buffer, int pixelStride) {
            this(theFormat, PixelUtils.getGetter(theFormat), Image.getSetterIfWritable(theFormat), buffer, pixelStride);
        }

        BaseAccessor(javafx.scene.image.PixelFormat<I> theFormat, PixelGetter<I> getter, PixelSetter<I> setter, I buffer, int pixelStride) {
            super();
            this.theFormat = theFormat;
            this.theGetter = getter;
            this.theSetter = setter;
            this.theBuffer = buffer;
            this.pixelElems = pixelStride;
            this.scanlineElems = Image.this.scanlineStride / Image.this.pixelFormat.getDataType().getSizeInBytes();
            this.offsetElems = (Image.this.minY * this.scanlineElems) + (Image.this.minX * pixelStride);
        }

        public int getIndex(int x2, int y2) {
            if (x2 < 0 || y2 < 0 || x2 >= Image.this.width || y2 >= Image.this.height) {
                throw new IndexOutOfBoundsException(x2 + ", " + y2);
            }
            return this.offsetElems + (y2 * this.scanlineElems) + (x2 * this.pixelElems);
        }

        public I getBuffer() {
            return this.theBuffer;
        }

        public PixelGetter<I> getGetter() {
            if (this.theGetter == null) {
                throw new UnsupportedOperationException("Unsupported Image type");
            }
            return this.theGetter;
        }

        public PixelSetter<I> getSetter() {
            if (this.theSetter == null) {
                throw new UnsupportedOperationException("Unsupported Image type");
            }
            return this.theSetter;
        }

        @Override // com.sun.prism.Image.Accessor
        public javafx.scene.image.PixelFormat<I> getPlatformPixelFormat() {
            return this.theFormat;
        }

        @Override // com.sun.prism.Image.Accessor
        public boolean isWritable() {
            return this.theSetter != null;
        }

        @Override // com.sun.prism.Image.Accessor
        public PlatformImage promoteToWritableImage() {
            return Image.this;
        }

        @Override // com.sun.prism.Image.Accessor
        public int getArgb(int x2, int y2) {
            return getGetter().getArgb(getBuffer(), getIndex(x2, y2));
        }

        @Override // com.sun.prism.Image.Accessor
        public void setArgb(int x2, int y2, int argb) {
            getSetter().setArgb(getBuffer(), getIndex(x2, y2), argb);
        }

        @Override // com.sun.prism.Image.Accessor
        public <T extends Buffer> void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<T> pixelformat, T dstbuf, int dstScanlineElems) {
            PixelSetter<T> setter = PixelUtils.getSetter(pixelformat);
            PixelConverter<I, T> converter = PixelUtils.getConverter(getGetter(), setter);
            int dstoff = dstbuf.position();
            converter.convert(getBuffer(), getIndex(x2, y2), this.scanlineElems, dstbuf, dstoff, dstScanlineElems, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public <T extends Buffer> void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<T> pixelformat, T srcbuf, int srcScanlineBytes) {
            PixelGetter<T> getter = PixelUtils.getGetter(pixelformat);
            PixelConverter<T, I> converter = PixelUtils.getConverter(getter, getSetter());
            int srcoff = srcbuf.position();
            converter.convert(srcbuf, srcoff, srcScanlineBytes, getBuffer(), getIndex(x2, y2), this.scanlineElems, w2, h2);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/Image$ByteAccess.class */
    class ByteAccess extends BaseAccessor<ByteBuffer> {
        ByteAccess(javafx.scene.image.PixelFormat<ByteBuffer> fmt, PixelGetter<ByteBuffer> getter, PixelSetter<ByteBuffer> setter, ByteBuffer buffer, int numbytes) {
            super(fmt, getter, setter, buffer, numbytes);
        }

        ByteAccess(javafx.scene.image.PixelFormat<ByteBuffer> fmt, ByteBuffer buffer, int numbytes) {
            super(Image.this, fmt, buffer, numbytes);
        }

        @Override // com.sun.prism.Image.Accessor
        public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<ByteBuffer> pixelformat, byte[] dstarr, int dstoff, int dstScanlineBytes) {
            BytePixelSetter setter = PixelUtils.getByteSetter(pixelformat);
            ByteToBytePixelConverter b2bconverter = PixelUtils.getB2BConverter(getGetter(), setter);
            b2bconverter.convert(getBuffer(), getIndex(x2, y2), this.scanlineElems, dstarr, dstoff, dstScanlineBytes, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<IntBuffer> pixelformat, int[] dstarr, int dstoff, int dstScanlineInts) {
            IntPixelSetter setter = PixelUtils.getIntSetter(pixelformat);
            ByteToIntPixelConverter b2iconverter = PixelUtils.getB2IConverter(getGetter(), setter);
            b2iconverter.convert(getBuffer(), getIndex(x2, y2), this.scanlineElems, dstarr, dstoff, dstScanlineInts, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<ByteBuffer> pixelformat, byte[] srcarr, int srcoff, int srcScanlineBytes) {
            BytePixelGetter getter = PixelUtils.getByteGetter(pixelformat);
            ByteToBytePixelConverter b2bconverter = PixelUtils.getB2BConverter(getter, getSetter());
            b2bconverter.convert(srcarr, srcoff, srcScanlineBytes, getBuffer(), getIndex(x2, y2), this.scanlineElems, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<IntBuffer> pixelformat, int[] srcarr, int srcoff, int srcScanlineInts) {
            IntPixelGetter getter = PixelUtils.getIntGetter(pixelformat);
            IntToBytePixelConverter i2bconverter = PixelUtils.getI2BConverter(getter, getSetter());
            i2bconverter.convert(srcarr, srcoff, srcScanlineInts, getBuffer(), getIndex(x2, y2), this.scanlineElems, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int dstx, int dsty, int w2, int h2, PixelReader reader, int srcx, int srcy) {
            ByteBuffer b2 = ((ByteBuffer) this.theBuffer).duplicate();
            b2.position(b2.position() + getIndex(dstx, dsty));
            reader.getPixels(srcx, srcy, w2, h2, (WritablePixelFormat) this.theFormat, b2, this.scanlineElems);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/Image$IntAccess.class */
    class IntAccess extends BaseAccessor<IntBuffer> {
        IntAccess(javafx.scene.image.PixelFormat<IntBuffer> fmt, IntBuffer buffer) {
            super(Image.this, fmt, buffer, 1);
        }

        @Override // com.sun.prism.Image.Accessor
        public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<ByteBuffer> pixelformat, byte[] dstarr, int dstoff, int dstScanlineBytes) {
            BytePixelSetter setter = PixelUtils.getByteSetter(pixelformat);
            IntToBytePixelConverter i2bconverter = PixelUtils.getI2BConverter(getGetter(), setter);
            i2bconverter.convert(getBuffer(), getIndex(x2, y2), this.scanlineElems, dstarr, dstoff, dstScanlineBytes, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<IntBuffer> pixelformat, int[] dstarr, int dstoff, int dstScanlineInts) {
            IntPixelSetter setter = PixelUtils.getIntSetter(pixelformat);
            IntToIntPixelConverter i2iconverter = PixelUtils.getI2IConverter(getGetter(), setter);
            i2iconverter.convert(getBuffer(), getIndex(x2, y2), this.scanlineElems, dstarr, dstoff, dstScanlineInts, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<ByteBuffer> pixelformat, byte[] srcarr, int srcoff, int srcScanlineBytes) {
            BytePixelGetter getter = PixelUtils.getByteGetter(pixelformat);
            ByteToIntPixelConverter b2iconverter = PixelUtils.getB2IConverter(getter, getSetter());
            b2iconverter.convert(srcarr, srcoff, srcScanlineBytes, getBuffer(), getIndex(x2, y2), this.scanlineElems, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int x2, int y2, int w2, int h2, javafx.scene.image.PixelFormat<IntBuffer> pixelformat, int[] srcarr, int srcoff, int srcScanlineInts) {
            IntPixelGetter getter = PixelUtils.getIntGetter(pixelformat);
            IntToIntPixelConverter i2iconverter = PixelUtils.getI2IConverter(getter, getSetter());
            i2iconverter.convert(srcarr, srcoff, srcScanlineInts, getBuffer(), getIndex(x2, y2), this.scanlineElems, w2, h2);
        }

        @Override // com.sun.prism.Image.Accessor
        public void setPixels(int dstx, int dsty, int w2, int h2, PixelReader reader, int srcx, int srcy) {
            IntBuffer b2 = ((IntBuffer) this.theBuffer).duplicate();
            b2.position(b2.position() + getIndex(dstx, dsty));
            reader.getPixels(srcx, srcy, w2, h2, (WritablePixelFormat) this.theFormat, b2, this.scanlineElems);
        }
    }

    static javafx.scene.image.PixelFormat<ByteBuffer> getGrayFXPixelFormat() {
        if (FX_ByteGray_FORMAT == null) {
            int[] grays = new int[256];
            int gray = -16777216;
            for (int i2 = 0; i2 < 256; i2++) {
                grays[i2] = gray;
                gray += 65793;
            }
            FX_ByteGray_FORMAT = javafx.scene.image.PixelFormat.createByteIndexedPremultipliedInstance(grays);
        }
        return FX_ByteGray_FORMAT;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/Image$UnsupportedAccess.class */
    class UnsupportedAccess extends ByteAccess {
        private UnsupportedAccess() {
            super(null, null, null, null, 0);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/Image$ByteRgbAccess.class */
    class ByteRgbAccess extends ByteAccess {
        public ByteRgbAccess(ByteBuffer buffer) {
            super(Image.FX_ByteRgb_FORMAT, buffer, 3);
        }

        @Override // com.sun.prism.Image.BaseAccessor, com.sun.prism.Image.Accessor
        public PlatformImage promoteToWritableImage() {
            return Image.this.promoteByteRgbToByteBgra();
        }
    }
}
