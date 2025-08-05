package sun.awt.image;

import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.jar.Pack200;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

/* loaded from: rt.jar:sun/awt/image/PNGImageDecoder.class */
public class PNGImageDecoder extends ImageDecoder {
    private static final int GRAY = 0;
    private static final int PALETTE = 1;
    private static final int COLOR = 2;
    private static final int ALPHA = 4;
    private static final int bKGDChunk = 1649100612;
    private static final int cHRMChunk = 1665684045;
    private static final int gAMAChunk = 1732332865;
    private static final int hISTChunk = 1749635924;
    private static final int IDATChunk = 1229209940;
    private static final int IENDChunk = 1229278788;
    private static final int IHDRChunk = 1229472850;
    private static final int PLTEChunk = 1347179589;
    private static final int pHYsChunk = 1883789683;
    private static final int sBITChunk = 1933723988;
    private static final int tEXtChunk = 1950701684;
    private static final int tIMEChunk = 1950960965;
    private static final int tRNSChunk = 1951551059;
    private static final int zTXtChunk = 2052348020;
    private int width;
    private int height;
    private int bitDepth;
    private int colorType;
    private int compressionMethod;
    private int filterMethod;
    private int interlaceMethod;
    private int gamma;
    private Hashtable properties;
    private ColorModel cm;
    private byte[] red_map;
    private byte[] green_map;
    private byte[] blue_map;
    private byte[] alpha_map;
    private int transparentPixel;
    private byte[] transparentPixel_16;
    int pos;
    int limit;
    int chunkStart;
    int chunkKey;
    int chunkLength;
    int chunkCRC;
    boolean seenEOF;
    PNGFilterInputStream inputStream;
    InputStream underlyingInputStream;
    byte[] inbuf;
    private static ColorModel[] greyModels = new ColorModel[4];
    private static final byte[] startingRow = {0, 0, 0, 4, 0, 2, 0, 1};
    private static final byte[] startingCol = {0, 0, 4, 0, 2, 0, 1, 0};
    private static final byte[] rowIncrement = {1, 8, 8, 8, 4, 4, 2, 2};
    private static final byte[] colIncrement = {1, 8, 8, 4, 4, 2, 2, 1};
    private static final byte[] blockHeight = {1, 8, 8, 4, 4, 2, 2, 1};
    private static final byte[] blockWidth = {1, 8, 4, 4, 2, 2, 1, 1};
    private static final byte[] signature = {-119, 80, 78, 71, 13, 10, 26, 10};
    private static boolean checkCRC = true;
    private static final int[] crc_table = new int[256];

    static {
        int i2;
        for (int i3 = 0; i3 < 256; i3++) {
            int i4 = i3;
            for (int i5 = 0; i5 < 8; i5++) {
                if ((i4 & 1) != 0) {
                    i2 = (-306674912) ^ (i4 >>> 1);
                } else {
                    i2 = i4 >>> 1;
                }
                i4 = i2;
            }
            crc_table[i3] = i4;
        }
    }

    private void property(String str, Object obj) {
        if (obj == null) {
            return;
        }
        if (this.properties == null) {
            this.properties = new Hashtable();
        }
        this.properties.put(str, obj);
    }

    private void property(String str, float f2) {
        property(str, new Float(f2));
    }

    private final void pngassert(boolean z2) throws IOException {
        if (!z2) {
            PNGException pNGException = new PNGException("Broken file");
            pNGException.printStackTrace();
            throw pNGException;
        }
    }

    protected boolean handleChunk(int i2, byte[] bArr, int i3, int i4) throws IOException {
        switch (i2) {
            case IDATChunk /* 1229209940 */:
                return false;
            case IENDChunk /* 1229278788 */:
            case hISTChunk /* 1749635924 */:
            case pHYsChunk /* 1883789683 */:
            case sBITChunk /* 1933723988 */:
            case zTXtChunk /* 2052348020 */:
            default:
                return true;
            case IHDRChunk /* 1229472850 */:
                if (i4 == 13) {
                    int i5 = getInt(i3);
                    this.width = i5;
                    if (i5 != 0) {
                        int i6 = getInt(i3 + 4);
                        this.height = i6;
                        if (i6 != 0) {
                            this.bitDepth = getByte(i3 + 8);
                            this.colorType = getByte(i3 + 9);
                            this.compressionMethod = getByte(i3 + 10);
                            this.filterMethod = getByte(i3 + 11);
                            this.interlaceMethod = getByte(i3 + 12);
                            return true;
                        }
                    }
                }
                throw new PNGException("bogus IHDR");
            case PLTEChunk /* 1347179589 */:
                int i7 = i4 / 3;
                this.red_map = new byte[i7];
                this.green_map = new byte[i7];
                this.blue_map = new byte[i7];
                int i8 = 0;
                int i9 = i3;
                while (i8 < i7) {
                    this.red_map[i8] = bArr[i9];
                    this.green_map[i8] = bArr[i9 + 1];
                    this.blue_map[i8] = bArr[i9 + 2];
                    i8++;
                    i9 += 3;
                }
                return true;
            case bKGDChunk /* 1649100612 */:
                Color color = null;
                switch (this.colorType) {
                    case 0:
                    case 4:
                        pngassert(i4 == 2);
                        int i10 = bArr[i3] & 255;
                        color = new Color(i10, i10, i10);
                        break;
                    case 2:
                    case 6:
                        pngassert(i4 == 6);
                        color = new Color(bArr[i3] & 255, bArr[i3 + 2] & 255, bArr[i3 + 4] & 255);
                        break;
                    case 3:
                    case 7:
                        pngassert(i4 == 1);
                        int i11 = bArr[i3] & 255;
                        pngassert(this.red_map != null && i11 < this.red_map.length);
                        color = new Color(this.red_map[i11] & 255, this.green_map[i11] & 255, this.blue_map[i11] & 255);
                        break;
                }
                if (color != null) {
                    property("background", color);
                    return true;
                }
                return true;
            case cHRMChunk /* 1665684045 */:
                property("chromaticities", new Chromaticities(getInt(i3), getInt(i3 + 4), getInt(i3 + 8), getInt(i3 + 12), getInt(i3 + 16), getInt(i3 + 20), getInt(i3 + 24), getInt(i3 + 28)));
                return true;
            case gAMAChunk /* 1732332865 */:
                if (i4 != 4) {
                    throw new PNGException("bogus gAMA");
                }
                this.gamma = getInt(i3);
                if (this.gamma != 100000) {
                    property("gamma", this.gamma / 100000.0f);
                    return true;
                }
                return true;
            case tEXtChunk /* 1950701684 */:
                int i12 = 0;
                while (i12 < i4 && bArr[i3 + i12] != 0) {
                    i12++;
                }
                if (i12 < i4) {
                    property(new String(bArr, i3, i12), new String(bArr, i3 + i12 + 1, (i4 - i12) - 1));
                    return true;
                }
                return true;
            case tIMEChunk /* 1950960965 */:
                property("modtime", new GregorianCalendar(getShort(i3 + 0), getByte(i3 + 2) - 1, getByte(i3 + 3), getByte(i3 + 4), getByte(i3 + 5), getByte(i3 + 6)).getTime());
                return true;
            case tRNSChunk /* 1951551059 */:
                switch (this.colorType) {
                    case 0:
                    case 4:
                        pngassert(i4 == 2);
                        int i13 = getShort(i3);
                        int i14 = 255 & (this.bitDepth == 16 ? i13 >> 8 : i13);
                        this.transparentPixel = (i14 << 16) | (i14 << 8) | i14;
                        return true;
                    case 1:
                    case 5:
                    default:
                        return true;
                    case 2:
                    case 6:
                        pngassert(i4 == 6);
                        if (this.bitDepth == 16) {
                            this.transparentPixel_16 = new byte[6];
                            for (int i15 = 0; i15 < 6; i15++) {
                                this.transparentPixel_16[i15] = (byte) getByte(i3 + i15);
                            }
                            return true;
                        }
                        this.transparentPixel = ((getShort(i3 + 0) & 255) << 16) | ((getShort(i3 + 2) & 255) << 8) | (getShort(i3 + 4) & 255);
                        return true;
                    case 3:
                    case 7:
                        int length = i4;
                        if (this.red_map != null) {
                            length = this.red_map.length;
                        }
                        this.alpha_map = new byte[length];
                        System.arraycopy(bArr, i3, this.alpha_map, 0, i4 < length ? i4 : length);
                        while (true) {
                            length--;
                            if (length < i4) {
                                return true;
                            }
                            this.alpha_map[length] = -1;
                        }
                }
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PNGImageDecoder$PNGException.class */
    public class PNGException extends IOException {
        PNGException(String str) {
            super(str);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v123, types: [int] */
    /* JADX WARN: Type inference failed for: r0v149, types: [int] */
    /* JADX WARN: Type inference failed for: r0v151, types: [int] */
    /* JADX WARN: Type inference failed for: r0v171, types: [int] */
    /* JADX WARN: Type inference failed for: r0v238, types: [int] */
    /* JADX WARN: Type inference failed for: r11v0, types: [sun.awt.image.PNGImageDecoder] */
    @Override // sun.awt.image.ImageDecoder
    public void produceImage() throws IOException, ImageFormatException {
        int i2;
        byte b2;
        int i3;
        int i4;
        for (int i5 = 0; i5 < signature.length; i5++) {
            try {
                try {
                    if ((signature[i5] & 255) != this.underlyingInputStream.read()) {
                        throw new PNGException("Chunk signature mismatch");
                    }
                } finally {
                    try {
                        close();
                    } catch (Throwable th) {
                    }
                }
            } catch (IOException e2) {
                if (!this.aborted) {
                    property(Pack200.Packer.ERROR, e2);
                    imageComplete(3, true);
                    throw e2;
                }
                try {
                    close();
                    return;
                } catch (Throwable th2) {
                    return;
                }
            }
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new InflaterInputStream(this.inputStream, new Inflater()));
        getData();
        byte[] bArr = null;
        int[] iArr = null;
        int i6 = this.width;
        switch (this.bitDepth) {
            case 1:
                i2 = 0;
                break;
            case 2:
                i2 = 1;
                break;
            case 4:
                i2 = 2;
                break;
            case 8:
                i2 = 3;
                break;
            case 16:
                i2 = 4;
                break;
            default:
                throw new PNGException("invalid depth");
        }
        if (this.interlaceMethod != 0) {
            i6 *= this.height;
            b2 = this.width;
        } else {
            b2 = 0;
        }
        int i7 = this.colorType | (this.bitDepth << 3);
        int i8 = (1 << (this.bitDepth >= 8 ? 8 : this.bitDepth)) - 1;
        switch (this.colorType) {
            case 0:
                int i9 = i2 >= 4 ? 3 : i2;
                ColorModel colorModel = greyModels[i9];
                this.cm = colorModel;
                if (colorModel == null) {
                    int i10 = 1 << (1 << i9);
                    byte[] bArr2 = new byte[i10];
                    for (int i11 = 0; i11 < i10; i11++) {
                        bArr2[i11] = (byte) ((255 * i11) / (i10 - 1));
                    }
                    if (this.transparentPixel == -1) {
                        this.cm = new IndexColorModel(this.bitDepth, bArr2.length, bArr2, bArr2, bArr2);
                    } else {
                        this.cm = new IndexColorModel(this.bitDepth, bArr2.length, bArr2, bArr2, bArr2, this.transparentPixel & 255);
                    }
                    greyModels[i9] = this.cm;
                }
                bArr = new byte[i6];
                break;
            case 1:
            case 5:
            default:
                throw new PNGException("invalid color type");
            case 2:
            case 4:
            case 6:
                this.cm = ColorModel.getRGBdefault();
                iArr = new int[i6];
                break;
            case 3:
            case 7:
                if (this.red_map == null) {
                    throw new PNGException("palette expected");
                }
                if (this.alpha_map == null) {
                    this.cm = new IndexColorModel(this.bitDepth, this.red_map.length, this.red_map, this.green_map, this.blue_map);
                } else {
                    this.cm = new IndexColorModel(this.bitDepth, this.red_map.length, this.red_map, this.green_map, this.blue_map, this.alpha_map);
                }
                bArr = new byte[i6];
                break;
        }
        setDimensions(this.width, this.height);
        setColorModel(this.cm);
        setHints(this.interlaceMethod != 0 ? 6 : 30);
        headerComplete();
        int i12 = ((this.colorType & 1) != 0 ? 1 : ((this.colorType & 2) != 0 ? 3 : 1) + ((this.colorType & 4) != 0 ? 1 : 0)) * this.bitDepth;
        int i13 = (i12 + 7) >> 3;
        if (this.interlaceMethod == 0) {
            i3 = -1;
            i4 = 0;
        } else {
            i3 = 0;
            i4 = 7;
        }
        while (true) {
            i3++;
            if (i3 <= i4) {
                byte b3 = startingRow[i3];
                byte b4 = rowIncrement[i3];
                byte b5 = colIncrement[i3];
                byte b6 = blockWidth[i3];
                byte b7 = blockHeight[i3];
                int i14 = (((((this.width - startingCol[i3]) + (b5 - 1)) / b5) * i12) + 7) >> 3;
                if (i14 != 0) {
                    int i15 = this.interlaceMethod == 0 ? b4 * this.width : 0;
                    byte b8 = b2 * b3;
                    boolean z2 = true;
                    byte[] bArr3 = new byte[i14];
                    byte[] bArr4 = new byte[i14];
                    while (b3 < this.height) {
                        int i16 = bufferedInputStream.read();
                        int i17 = 0;
                        while (i17 < i14) {
                            int i18 = bufferedInputStream.read(bArr3, i17, i14 - i17);
                            if (i18 <= 0) {
                                throw new PNGException("missing data");
                            }
                            i17 += i18;
                        }
                        filterRow(bArr3, z2 ? null : bArr4, i16, i14, i13);
                        int i19 = 0;
                        for (byte b9 = r0; b9 < this.width; b9 += b5) {
                            if (iArr != null) {
                                switch (i7) {
                                    case 66:
                                        int i20 = ((bArr3[i19] & 255) << 16) | ((bArr3[i19 + 1] & 255) << 8) | (bArr3[i19 + 2] & 255);
                                        if (i20 != this.transparentPixel) {
                                            i20 |= -16777216;
                                        }
                                        iArr[b9 + b8] = i20;
                                        i19 += 3;
                                        break;
                                    case 68:
                                        int i21 = bArr3[i19] & 255;
                                        iArr[b9 + b8] = (i21 << 16) | (i21 << 8) | i21 | ((bArr3[i19 + 1] & 255) << 24);
                                        i19 += 2;
                                        break;
                                    case 70:
                                        iArr[b9 + b8] = ((bArr3[i19] & 255) << 16) | ((bArr3[i19 + 1] & 255) << 8) | (bArr3[i19 + 2] & 255) | ((bArr3[i19 + 3] & 255) << 24);
                                        i19 += 4;
                                        break;
                                    case 130:
                                        int i22 = ((bArr3[i19] & 255) << 16) | ((bArr3[i19 + 2] & 255) << 8) | (bArr3[i19 + 4] & 255);
                                        boolean z3 = this.transparentPixel_16 != null;
                                        for (int i23 = 0; z3 && i23 < 6; i23++) {
                                            z3 &= (bArr3[i19 + i23] & 255) == (this.transparentPixel_16[i23] & 255);
                                        }
                                        if (!z3) {
                                            i22 |= -16777216;
                                        }
                                        iArr[b9 + b8] = i22;
                                        i19 += 6;
                                        break;
                                    case 132:
                                        int i24 = bArr3[i19] & 255;
                                        iArr[b9 + b8] = (i24 << 16) | (i24 << 8) | i24 | ((bArr3[i19 + 2] & 255) << 24);
                                        i19 += 4;
                                        break;
                                    case 134:
                                        iArr[b9 + b8] = ((bArr3[i19] & 255) << 16) | ((bArr3[i19 + 2] & 255) << 8) | (bArr3[i19 + 4] & 255) | ((bArr3[i19 + 6] & 255) << 24);
                                        i19 += 8;
                                        break;
                                    default:
                                        throw new PNGException("illegal type/depth");
                                }
                            } else {
                                switch (this.bitDepth) {
                                    case 1:
                                        bArr[b9 + b8] = (byte) ((bArr3[i19 >> 3] >> (7 - (i19 & 7))) & 1);
                                        i19++;
                                        break;
                                    case 2:
                                        bArr[b9 + b8] = (byte) ((bArr3[i19 >> 2] >> ((3 - (i19 & 3)) * 2)) & 3);
                                        i19++;
                                        break;
                                    case 4:
                                        bArr[b9 + b8] = (byte) ((bArr3[i19 >> 1] >> ((1 - (i19 & 1)) * 4)) & 15);
                                        i19++;
                                        break;
                                    case 8:
                                        int i25 = i19;
                                        i19++;
                                        bArr[b9 + b8] = bArr3[i25];
                                        break;
                                    case 16:
                                        bArr[b9 + b8] = bArr3[i19];
                                        i19 += 2;
                                        break;
                                    default:
                                        throw new PNGException("illegal type/depth");
                                }
                            }
                        }
                        if (this.interlaceMethod == 0) {
                            if (iArr != null) {
                                sendPixels(0, b3, this.width, 1, iArr, 0, this.width);
                            } else {
                                sendPixels(0, b3, this.width, 1, bArr, 0, this.width);
                            }
                        }
                        b3 += b4;
                        b8 += b4 * b2;
                        byte[] bArr5 = bArr3;
                        bArr3 = bArr4;
                        bArr4 = bArr5;
                        z2 = false;
                    }
                    if (this.interlaceMethod != 0) {
                        if (iArr != null) {
                            sendPixels(0, 0, this.width, this.height, iArr, 0, this.width);
                        } else {
                            sendPixels(0, 0, this.width, this.height, bArr, 0, this.width);
                        }
                    }
                }
            } else {
                imageComplete(3, true);
                try {
                    return;
                } catch (Throwable th3) {
                    return;
                }
            }
        }
    }

    private boolean sendPixels(int i2, int i3, int i4, int i5, int[] iArr, int i6, int i7) {
        if (setPixels(i2, i3, i4, i5, this.cm, iArr, i6, i7) <= 0) {
            this.aborted = true;
        }
        return !this.aborted;
    }

    private boolean sendPixels(int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7) {
        if (setPixels(i2, i3, i4, i5, this.cm, bArr, i6, i7) <= 0) {
            this.aborted = true;
        }
        return !this.aborted;
    }

    private void filterRow(byte[] bArr, byte[] bArr2, int i2, int i3, int i4) throws IOException {
        int i5 = 0;
        switch (i2) {
            case 0:
                return;
            case 1:
                for (int i6 = i4; i6 < i3; i6++) {
                    int i7 = i6;
                    bArr[i7] = (byte) (bArr[i7] + bArr[i6 - i4]);
                }
                return;
            case 2:
                if (bArr2 != null) {
                    while (i5 < i3) {
                        int i8 = i5;
                        bArr[i8] = (byte) (bArr[i8] + bArr2[i5]);
                        i5++;
                    }
                    return;
                }
                return;
            case 3:
                if (bArr2 != null) {
                    while (i5 < i4) {
                        int i9 = i5;
                        bArr[i9] = (byte) (bArr[i9] + ((255 & bArr2[i5]) >> 1));
                        i5++;
                    }
                    while (i5 < i3) {
                        int i10 = i5;
                        bArr[i10] = (byte) (bArr[i10] + (((bArr2[i5] & 255) + (bArr[i5 - i4] & 255)) >> 1));
                        i5++;
                    }
                    return;
                }
                for (int i11 = i4; i11 < i3; i11++) {
                    int i12 = i11;
                    bArr[i12] = (byte) (bArr[i12] + ((bArr[i11 - i4] & 255) >> 1));
                }
                return;
            case 4:
                if (bArr2 != null) {
                    while (i5 < i4) {
                        int i13 = i5;
                        bArr[i13] = (byte) (bArr[i13] + bArr2[i5]);
                        i5++;
                    }
                    while (i5 < i3) {
                        int i14 = bArr[i5 - i4] & 255;
                        int i15 = bArr2[i5] & 255;
                        int i16 = bArr2[i5 - i4] & 255;
                        int i17 = ((i14 == true ? 1 : 0) + (i15 == true ? 1 : 0)) - (i16 == true ? 1 : 0);
                        int i18 = i17 > i14 ? i17 - (i14 == true ? 1 : 0) : (i14 == true ? 1 : 0) - i17;
                        int i19 = i17 > i15 ? i17 - (i15 == true ? 1 : 0) : (i15 == true ? 1 : 0) - i17;
                        int i20 = i17 > i16 ? i17 - (i16 == true ? 1 : 0) : (i16 == true ? 1 : 0) - i17;
                        int i21 = i5;
                        bArr[i21] = (byte) (bArr[i21] + ((i18 > i19 || i18 > i20) ? i19 <= i20 ? i15 == true ? 1 : 0 : i16 == true ? 1 : 0 : i14 == true ? 1 : 0));
                        i5++;
                    }
                    return;
                }
                for (int i22 = i4; i22 < i3; i22++) {
                    int i23 = i22;
                    bArr[i23] = (byte) (bArr[i23] + bArr[i22 - i4]);
                }
                return;
            default:
                throw new PNGException("Illegal filter");
        }
    }

    public PNGImageDecoder(InputStreamImageSource inputStreamImageSource, InputStream inputStream) throws IOException {
        super(inputStreamImageSource, inputStream);
        this.gamma = Config.MAX_REPEAT_NUM;
        this.transparentPixel = -1;
        this.transparentPixel_16 = null;
        this.inbuf = new byte[4096];
        this.inputStream = new PNGFilterInputStream(this, inputStream);
        this.underlyingInputStream = this.inputStream.underlyingInputStream;
    }

    private void fill() throws IOException {
        if (!this.seenEOF) {
            if (this.pos > 0 && this.pos < this.limit) {
                System.arraycopy(this.inbuf, this.pos, this.inbuf, 0, this.limit - this.pos);
                this.limit -= this.pos;
                this.pos = 0;
            } else if (this.pos >= this.limit) {
                this.pos = 0;
                this.limit = 0;
            }
            int length = this.inbuf.length;
            while (this.limit < length) {
                int i2 = this.underlyingInputStream.read(this.inbuf, this.limit, length - this.limit);
                if (i2 > 0) {
                    this.limit += i2;
                } else {
                    this.seenEOF = true;
                    return;
                }
            }
        }
    }

    private boolean need(int i2) throws IOException {
        if (this.limit - this.pos >= i2) {
            return true;
        }
        fill();
        if (this.limit - this.pos >= i2) {
            return true;
        }
        if (this.seenEOF) {
            return false;
        }
        byte[] bArr = new byte[i2 + 100];
        System.arraycopy(this.inbuf, this.pos, bArr, 0, this.limit - this.pos);
        this.limit -= this.pos;
        this.pos = 0;
        this.inbuf = bArr;
        fill();
        return this.limit - this.pos >= i2;
    }

    private final int getInt(int i2) {
        return ((this.inbuf[i2] & 255) << 24) | ((this.inbuf[i2 + 1] & 255) << 16) | ((this.inbuf[i2 + 2] & 255) << 8) | (this.inbuf[i2 + 3] & 255);
    }

    private final int getShort(int i2) {
        return (short) (((this.inbuf[i2] & 255) << 8) | (this.inbuf[i2 + 1] & 255));
    }

    private final int getByte(int i2) {
        return this.inbuf[i2] & 255;
    }

    private final boolean getChunk() throws IOException {
        this.chunkLength = 0;
        if (!need(8)) {
            return false;
        }
        this.chunkLength = getInt(this.pos);
        this.chunkKey = getInt(this.pos + 4);
        if (this.chunkLength < 0) {
            throw new PNGException("bogus length: " + this.chunkLength);
        }
        if (!need(this.chunkLength + 12)) {
            return false;
        }
        this.chunkCRC = getInt(this.pos + 8 + this.chunkLength);
        this.chunkStart = this.pos + 8;
        if (this.chunkCRC != crc(this.inbuf, this.pos + 4, this.chunkLength + 4) && checkCRC) {
            throw new PNGException("crc corruption");
        }
        this.pos += this.chunkLength + 12;
        return true;
    }

    private void readAll() throws IOException {
        while (getChunk()) {
            handleChunk(this.chunkKey, this.inbuf, this.chunkStart, this.chunkLength);
        }
    }

    boolean getData() throws IOException {
        while (this.chunkLength == 0 && getChunk()) {
            if (handleChunk(this.chunkKey, this.inbuf, this.chunkStart, this.chunkLength)) {
                this.chunkLength = 0;
            }
        }
        return this.chunkLength > 0;
    }

    public static boolean getCheckCRC() {
        return checkCRC;
    }

    public static void setCheckCRC(boolean z2) {
        checkCRC = z2;
    }

    protected void wrc(int i2) {
        int i3 = i2 & 255;
        if (i3 <= 32 || i3 > 122) {
            i3 = 63;
        }
        System.out.write(i3);
    }

    protected void wrk(int i2) {
        wrc(i2 >> 24);
        wrc(i2 >> 16);
        wrc(i2 >> 8);
        wrc(i2);
    }

    public void print() {
        wrk(this.chunkKey);
        System.out.print(" " + this.chunkLength + "\n");
    }

    private static int update_crc(int i2, byte[] bArr, int i3, int i4) {
        int i5 = i2;
        while (true) {
            int i6 = i5;
            i4--;
            if (i4 >= 0) {
                int i7 = i3;
                i3++;
                i5 = crc_table[(i6 ^ bArr[i7]) & 255] ^ (i6 >>> 8);
            } else {
                return i6;
            }
        }
    }

    private static int crc(byte[] bArr, int i2, int i3) {
        return update_crc(-1, bArr, i2, i3) ^ (-1);
    }

    /* loaded from: rt.jar:sun/awt/image/PNGImageDecoder$Chromaticities.class */
    public static class Chromaticities {
        public float whiteX;
        public float whiteY;
        public float redX;
        public float redY;
        public float greenX;
        public float greenY;
        public float blueX;
        public float blueY;

        Chromaticities(int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
            this.whiteX = i2 / 100000.0f;
            this.whiteY = i3 / 100000.0f;
            this.redX = i4 / 100000.0f;
            this.redY = i5 / 100000.0f;
            this.greenX = i6 / 100000.0f;
            this.greenY = i7 / 100000.0f;
            this.blueX = i8 / 100000.0f;
            this.blueY = i9 / 100000.0f;
        }

        public String toString() {
            return "Chromaticities(white=" + this.whiteX + "," + this.whiteY + ";red=" + this.redX + "," + this.redY + ";green=" + this.greenX + "," + this.greenY + ";blue=" + this.blueX + "," + this.blueY + ")";
        }
    }
}
