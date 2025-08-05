package sun.awt.image;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/* loaded from: rt.jar:sun/awt/image/GifImageDecoder.class */
public class GifImageDecoder extends ImageDecoder {
    private static final boolean verbose = false;
    private static final int IMAGESEP = 44;
    private static final int EXBLOCK = 33;
    private static final int EX_GRAPHICS_CONTROL = 249;
    private static final int EX_COMMENT = 254;
    private static final int EX_APPLICATION = 255;
    private static final int TERMINATOR = 59;
    private static final int TRANSPARENCYMASK = 1;
    private static final int INTERLACEMASK = 64;
    private static final int COLORMAPMASK = 128;
    int num_global_colors;
    byte[] global_colormap;
    int trans_pixel;
    IndexColorModel global_model;
    Hashtable props;
    byte[] saved_image;
    IndexColorModel saved_model;
    int global_width;
    int global_height;
    int global_bgpixel;
    GifFrame curframe;
    private static final int normalflags = 30;
    private static final int interlaceflags = 29;
    private short[] prefix;
    private byte[] suffix;
    private byte[] outCode;

    private static native void initIDs();

    private native boolean parseImage(int i2, int i3, int i4, int i5, boolean z2, int i6, byte[] bArr, byte[] bArr2, IndexColorModel indexColorModel);

    public GifImageDecoder(InputStreamImageSource inputStreamImageSource, InputStream inputStream) {
        super(inputStreamImageSource, inputStream);
        this.trans_pixel = -1;
        this.props = new Hashtable();
        this.prefix = new short[4096];
        this.suffix = new byte[4096];
        this.outCode = new byte[4097];
    }

    private static void error(String str) throws ImageFormatException {
        throw new ImageFormatException(str);
    }

    private int readBytes(byte[] bArr, int i2, int i3) {
        while (i3 > 0) {
            try {
                int i4 = this.input.read(bArr, i2, i3);
                if (i4 < 0) {
                    break;
                }
                i2 += i4;
                i3 -= i4;
            } catch (IOException e2) {
            }
        }
        return i3;
    }

    private static final int ExtractByte(byte[] bArr, int i2) {
        return bArr[i2] & 255;
    }

    private static final int ExtractWord(byte[] bArr, int i2) {
        return (bArr[i2] & 255) | ((bArr[i2 + 1] & 255) << 8);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:95:0x020f A[Catch: all -> 0x025d, TryCatch #1 {all -> 0x025d, blocks: (B:2:0x0000, B:3:0x0016, B:5:0x001d, B:6:0x0027, B:7:0x0050, B:8:0x005a, B:9:0x0084, B:14:0x009b, B:16:0x00a3, B:21:0x00af, B:25:0x00c4, B:26:0x00ca, B:28:0x00de, B:29:0x00eb, B:31:0x00fa, B:61:0x01a5, B:66:0x01bb, B:34:0x010b, B:41:0x012a, B:48:0x015e, B:52:0x016b, B:53:0x0175, B:55:0x0185, B:72:0x01ce, B:77:0x01e0, B:87:0x01f9, B:95:0x020f, B:105:0x0247, B:97:0x0216, B:99:0x021d, B:100:0x022a), top: B:118:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x021d A[Catch: IOException -> 0x0240, all -> 0x025d, TryCatch #0 {IOException -> 0x0240, blocks: (B:97:0x0216, B:99:0x021d, B:100:0x022a), top: B:116:0x0216 }] */
    @Override // sun.awt.image.ImageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void produceImage() throws java.io.IOException, sun.awt.image.ImageFormatException {
        /*
            Method dump skipped, instructions count: 615
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.awt.image.GifImageDecoder.produceImage():void");
    }

    private void readHeader() throws IOException, ImageFormatException {
        byte[] bArr = new byte[13];
        if (readBytes(bArr, 0, 13) != 0) {
            throw new IOException();
        }
        if (bArr[0] != 71 || bArr[1] != 73 || bArr[2] != 70) {
            error("not a GIF file.");
        }
        this.global_width = ExtractWord(bArr, 6);
        this.global_height = ExtractWord(bArr, 8);
        int iExtractByte = ExtractByte(bArr, 10);
        if ((iExtractByte & 128) == 0) {
            this.num_global_colors = 2;
            this.global_bgpixel = 0;
            this.global_colormap = new byte[6];
            byte[] bArr2 = this.global_colormap;
            byte[] bArr3 = this.global_colormap;
            this.global_colormap[2] = 0;
            bArr3[1] = 0;
            bArr2[0] = 0;
            byte[] bArr4 = this.global_colormap;
            byte[] bArr5 = this.global_colormap;
            this.global_colormap[5] = -1;
            bArr5[4] = -1;
            bArr4[3] = -1;
        } else {
            this.num_global_colors = 1 << ((iExtractByte & 7) + 1);
            this.global_bgpixel = ExtractByte(bArr, 11);
            if (bArr[12] != 0) {
                this.props.put("aspectratio", "" + ((ExtractByte(bArr, 12) + 15) / 64.0d));
            }
            this.global_colormap = new byte[this.num_global_colors * 3];
            if (readBytes(this.global_colormap, 0, this.num_global_colors * 3) != 0) {
                throw new IOException();
            }
        }
        this.input.mark(Integer.MAX_VALUE);
    }

    static {
        NativeLibLoader.loadLibraries();
        initIDs();
    }

    private int sendPixels(int i2, int i3, int i4, int i5, byte[] bArr, ColorModel colorModel) {
        int i6;
        int i7;
        if (i3 < 0) {
            i5 += i3;
            i3 = 0;
        }
        if (i3 + i5 > this.global_height) {
            i5 = this.global_height - i3;
        }
        if (i5 <= 0) {
            return 1;
        }
        if (i2 < 0) {
            i6 = -i2;
            i4 += i2;
            i7 = 0;
        } else {
            i6 = 0;
            i7 = i2;
        }
        if (i7 + i4 > this.global_width) {
            i4 = this.global_width - i7;
        }
        if (i4 <= 0) {
            return 1;
        }
        int i8 = i6 + i4;
        int i9 = (i3 * this.global_width) + i7;
        boolean z2 = this.curframe.disposal_method == 1;
        if (this.trans_pixel >= 0 && !this.curframe.initialframe) {
            if (this.saved_image != null && colorModel.equals(this.saved_model)) {
                int i10 = i6;
                while (i10 < i8) {
                    byte b2 = bArr[i10];
                    if ((b2 & 255) == this.trans_pixel) {
                        bArr[i10] = this.saved_image[i9];
                    } else if (z2) {
                        this.saved_image[i9] = b2;
                    }
                    i10++;
                    i9++;
                }
            } else {
                int i11 = -1;
                int pixels = 1;
                int i12 = i6;
                while (i12 < i8) {
                    byte b3 = bArr[i12];
                    if ((b3 & 255) == this.trans_pixel) {
                        if (i11 >= 0) {
                            pixels = setPixels(i2 + i11, i3, i12 - i11, 1, colorModel, bArr, i11, 0);
                            if (pixels == 0) {
                                break;
                            }
                        }
                        i11 = -1;
                    } else {
                        if (i11 < 0) {
                            i11 = i12;
                        }
                        if (z2) {
                            this.saved_image[i9] = b3;
                        }
                    }
                    i12++;
                    i9++;
                }
                if (i11 >= 0) {
                    pixels = setPixels(i2 + i11, i3, i8 - i11, 1, colorModel, bArr, i11, 0);
                }
                return pixels;
            }
        } else if (z2) {
            System.arraycopy(bArr, i6, this.saved_image, i9, i4);
        }
        return setPixels(i7, i3, i4, i5, colorModel, bArr, i6, 0);
    }

    private boolean readImage(boolean z2, int i2, int i3) throws IOException {
        byte transparentPixel;
        if (this.curframe != null && !this.curframe.dispose()) {
            abort();
            return false;
        }
        byte[] bArr = new byte[259];
        if (readBytes(bArr, 0, 10) != 0) {
            throw new IOException();
        }
        int iExtractWord = ExtractWord(bArr, 0);
        int iExtractWord2 = ExtractWord(bArr, 2);
        int iExtractWord3 = ExtractWord(bArr, 4);
        int iExtractWord4 = ExtractWord(bArr, 6);
        if (iExtractWord3 == 0 && this.global_width != 0) {
            iExtractWord3 = this.global_width - iExtractWord;
        }
        if (iExtractWord4 == 0 && this.global_height != 0) {
            iExtractWord4 = this.global_height - iExtractWord2;
        }
        boolean z3 = (bArr[8] & 64) != 0;
        IndexColorModel indexColorModel = this.global_model;
        if ((bArr[8] & 128) != 0) {
            int i4 = 1 << ((bArr[8] & 7) + 1);
            byte[] bArrGrow_colormap = new byte[i4 * 3];
            bArrGrow_colormap[0] = bArr[9];
            if (readBytes(bArrGrow_colormap, 1, (i4 * 3) - 1) != 0) {
                throw new IOException();
            }
            if (readBytes(bArr, 9, 1) != 0) {
                throw new IOException();
            }
            if (this.trans_pixel >= i4) {
                i4 = this.trans_pixel + 1;
                bArrGrow_colormap = grow_colormap(bArrGrow_colormap, i4);
            }
            indexColorModel = new IndexColorModel(8, i4, bArrGrow_colormap, 0, false, this.trans_pixel);
        } else if (indexColorModel == null || this.trans_pixel != indexColorModel.getTransparentPixel()) {
            if (this.trans_pixel >= this.num_global_colors) {
                this.num_global_colors = this.trans_pixel + 1;
                this.global_colormap = grow_colormap(this.global_colormap, this.num_global_colors);
            }
            indexColorModel = new IndexColorModel(8, this.num_global_colors, this.global_colormap, 0, false, this.trans_pixel);
            this.global_model = indexColorModel;
        }
        if (z2) {
            if (this.global_width == 0) {
                this.global_width = iExtractWord3;
            }
            if (this.global_height == 0) {
                this.global_height = iExtractWord4;
            }
            setDimensions(this.global_width, this.global_height);
            setProperties(this.props);
            setColorModel(indexColorModel);
            headerComplete();
        }
        if (i2 == 1 && this.saved_image == null) {
            this.saved_image = new byte[this.global_width * this.global_height];
            if (iExtractWord4 < this.global_height && indexColorModel != null && (transparentPixel = (byte) indexColorModel.getTransparentPixel()) >= 0) {
                byte[] bArr2 = new byte[this.global_width];
                for (int i5 = 0; i5 < this.global_width; i5++) {
                    bArr2[i5] = transparentPixel;
                }
                setPixels(0, 0, this.global_width, iExtractWord2, (ColorModel) indexColorModel, bArr2, 0, 0);
                setPixels(0, iExtractWord2 + iExtractWord4, this.global_width, (this.global_height - iExtractWord4) - iExtractWord2, (ColorModel) indexColorModel, bArr2, 0, 0);
            }
        }
        setHints(z3 ? 29 : 30);
        this.curframe = new GifFrame(this, i2, i3, this.curframe == null, indexColorModel, iExtractWord, iExtractWord2, iExtractWord3, iExtractWord4);
        byte[] bArr3 = new byte[iExtractWord3];
        int iExtractByte = ExtractByte(bArr, 9);
        if (iExtractByte >= 12) {
            return false;
        }
        boolean image = parseImage(iExtractWord, iExtractWord2, iExtractWord3, iExtractWord4, z3, iExtractByte, bArr, bArr3, indexColorModel);
        if (!image) {
            abort();
        }
        return image;
    }

    public static byte[] grow_colormap(byte[] bArr, int i2) {
        byte[] bArr2 = new byte[i2 * 3];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }
}
