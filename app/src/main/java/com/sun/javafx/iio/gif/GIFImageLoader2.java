package com.sun.javafx.iio.gif;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/gif/GIFImageLoader2.class */
public class GIFImageLoader2 extends ImageLoaderImpl {
    static final byte[] FILE_SIG87 = {71, 73, 70, 56, 55, 97};
    static final byte[] FILE_SIG89 = {71, 73, 70, 56, 57, 97};
    static final byte[] NETSCAPE_SIG = {78, 69, 84, 83, 67, 65, 80, 69, 50, 46, 48};
    static final int DEFAULT_FPS = 25;
    InputStream stream;
    int screenW;
    int screenH;
    int bgColor;
    byte[][] globalPalette;
    byte[] image;
    int loopCount;

    public GIFImageLoader2(InputStream input) throws IOException {
        super(GIFDescriptor.getInstance());
        this.stream = null;
        this.loopCount = 1;
        this.stream = input;
        readGlobalHeader();
    }

    private void readGlobalHeader() throws IOException {
        byte[] signature = readBytes(new byte[6]);
        if (!Arrays.equals(FILE_SIG87, signature) && !Arrays.equals(FILE_SIG89, signature)) {
            throw new IOException("Bad GIF signature!");
        }
        this.screenW = readShort();
        this.screenH = readShort();
        int cInfo = readByte();
        this.bgColor = readByte();
        readByte();
        if ((cInfo & 128) != 0) {
            this.globalPalette = readPalete(2 << (cInfo & 7), -1);
        }
        this.image = new byte[this.screenW * this.screenH * 4];
    }

    private byte[][] readPalete(int size, int trnsIndex) throws IOException {
        byte[][] palette = new byte[4][size];
        byte[] paletteData = readBytes(new byte[size * 3]);
        int i2 = 0;
        int idx = 0;
        while (i2 != size) {
            for (int k2 = 0; k2 != 3; k2++) {
                int i3 = idx;
                idx++;
                palette[k2][i2] = paletteData[i3];
            }
            palette[3][i2] = i2 == trnsIndex ? (byte) 0 : (byte) -1;
            i2++;
        }
        return palette;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void consumeAnExtension() throws IOException {
        int i2 = readByte();
        while (true) {
            int blSize = i2;
            if (blSize != 0) {
                skipBytes(blSize);
                i2 = readByte();
            } else {
                return;
            }
        }
    }

    private void readAppExtension() throws IOException {
        int size = readByte();
        byte[] buf = readBytes(new byte[size]);
        if (Arrays.equals(NETSCAPE_SIG, buf)) {
            int i2 = readByte();
            while (true) {
                int subBlockSize = i2;
                if (subBlockSize != 0) {
                    byte[] subBlock = readBytes(new byte[subBlockSize]);
                    byte b2 = subBlock[0];
                    if (subBlockSize == 3 && b2 == 1) {
                        this.loopCount = (subBlock[1] & 255) | ((subBlock[2] & 255) << 8);
                    }
                    i2 = readByte();
                } else {
                    return;
                }
            }
        } else {
            consumeAnExtension();
        }
    }

    private int readControlCode() throws IOException {
        int size = readByte();
        int pField = readByte();
        int frameDelay = readShort();
        int trnsIndex = readByte();
        if (size != 4 || readByte() != 0) {
            throw new IOException("Bad GIF GraphicControlExtension");
        }
        return ((pField & 31) << 24) + (trnsIndex << 16) + frameDelay;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private int waitForImageFrame() throws java.io.IOException {
        /*
            r9 = this;
            r0 = 0
            r10 = r0
        L2:
            r0 = r9
            java.io.InputStream r0 = r0.stream
            int r0 = r0.read()
            r11 = r0
            r0 = r11
            switch(r0) {
                case -1: goto L6a;
                case 33: goto L36;
                case 44: goto L34;
                case 59: goto L6a;
                default: goto L6c;
            }
        L34:
            r0 = r10
            return r0
        L36:
            r0 = r9
            int r0 = r0.readByte()
            switch(r0) {
                case 249: goto L54;
                case 255: goto L5c;
                default: goto L63;
            }
        L54:
            r0 = r9
            int r0 = r0.readControlCode()
            r10 = r0
            goto L96
        L5c:
            r0 = r9
            r0.readAppExtension()
            goto L96
        L63:
            r0 = r9
            r0.consumeAnExtension()
            goto L96
        L6a:
            r0 = -1
            return r0
        L6c:
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Unexpected GIF control characher 0x"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "%02X"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r5 = r4
            r6 = 0
            r7 = r11
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r5[r6] = r7
            java.lang.String r3 = java.lang.String.format(r3, r4)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L96:
            goto L2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.iio.gif.GIFImageLoader2.waitForImageFrame():int");
    }

    private void decodeImage(byte[] image, int w2, int h2, int[] interlace) throws IOException {
        LZWDecoder dec = new LZWDecoder();
        byte[] data = dec.getString();
        int y2 = 0;
        int iPos = 0;
        int xr = w2;
        while (true) {
            int len = dec.readString();
            if (len == -1) {
                dec.waitForTerminator();
                return;
            }
            int pos = 0;
            while (pos != len) {
                int ax2 = xr < len - pos ? xr : len - pos;
                System.arraycopy(data, pos, image, iPos, ax2);
                iPos += ax2;
                pos += ax2;
                int i2 = xr - ax2;
                xr = i2;
                if (i2 == 0) {
                    y2++;
                    if (y2 == h2) {
                        dec.waitForTerminator();
                        return;
                    } else {
                        int iY = interlace == null ? y2 : interlace[y2];
                        iPos = iY * w2;
                        xr = w2;
                    }
                }
            }
        }
    }

    private int[] computeInterlaceReIndex(int h2) {
        int[] data = new int[h2];
        int pos = 0;
        for (int i2 = 0; i2 < h2; i2 += 8) {
            int i3 = pos;
            pos++;
            data[i3] = i2;
        }
        for (int i4 = 4; i4 < h2; i4 += 8) {
            int i5 = pos;
            pos++;
            data[i5] = i4;
        }
        for (int i6 = 2; i6 < h2; i6 += 4) {
            int i7 = pos;
            pos++;
            data[i7] = i6;
        }
        for (int i8 = 1; i8 < h2; i8 += 2) {
            int i9 = pos;
            pos++;
            data[i9] = i8;
        }
        return data;
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public ImageFrame load(int imageIndex, int width, int height, boolean preserveAspectRatio, boolean smooth) throws IOException {
        int imageControlCode = waitForImageFrame();
        if (imageControlCode < 0) {
            return null;
        }
        int left = readShort();
        int top = readShort();
        int w2 = readShort();
        int h2 = readShort();
        if (left + w2 > this.screenW || top + h2 > this.screenH) {
            throw new IOException("Wrong GIF image frame size");
        }
        int imgCtrl = readByte();
        boolean isTRNS = ((imageControlCode >>> 24) & 1) == 1;
        int trnsIndex = isTRNS ? (imageControlCode >>> 16) & 255 : -1;
        boolean localPalette = (imgCtrl & 128) != 0;
        boolean isInterlaced = (imgCtrl & 64) != 0;
        byte[][] palette = localPalette ? readPalete(2 << (imgCtrl & 7), trnsIndex) : this.globalPalette;
        int[] outWH = ImageTools.computeDimensions(this.screenW, this.screenH, width, height, preserveAspectRatio);
        int width2 = outWH[0];
        int height2 = outWH[1];
        ImageMetadata metadata = updateMetadata(width2, height2, imageControlCode & 65535);
        int disposalCode = (imageControlCode >>> 26) & 7;
        byte[] pImage = new byte[w2 * h2];
        decodeImage(pImage, w2, h2, isInterlaced ? computeInterlaceReIndex(h2) : null);
        ByteBuffer img = decodePalette(pImage, palette, trnsIndex, left, top, w2, h2, disposalCode);
        if (this.screenW != width2 || this.screenH != height2) {
            img = ImageTools.scaleImage(img, this.screenW, this.screenH, 4, width2, height2, smooth);
        }
        return new ImageFrame(ImageStorage.ImageType.RGBA, img, width2, height2, width2 * 4, (byte[][]) null, metadata);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int readByte() throws IOException {
        int ch = this.stream.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return ch;
    }

    private int readShort() throws IOException {
        int lsb = readByte();
        int msb = readByte();
        return lsb + (msb << 8);
    }

    private byte[] readBytes(byte[] data) throws IOException {
        return readBytes(data, 0, data.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] readBytes(byte[] data, int offs, int size) throws IOException {
        while (size > 0) {
            int sz = this.stream.read(data, offs, size);
            if (sz < 0) {
                throw new EOFException();
            }
            offs += sz;
            size -= sz;
        }
        return data;
    }

    private void skipBytes(int n2) throws IOException {
        ImageTools.skipFully(this.stream, n2);
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public void dispose() {
    }

    private void restoreToBackground(byte[] img, int left, int top, int w2, int h2) {
        for (int y2 = 0; y2 != h2; y2++) {
            int iPos = (((top + y2) * this.screenW) + left) * 4;
            for (int x2 = 0; x2 != w2; x2++) {
                img[iPos + 3] = 0;
                iPos += 4;
            }
        }
    }

    private ByteBuffer decodePalette(byte[] srcImage, byte[][] palette, int trnsIndex, int left, int top, int w2, int h2, int disposalCode) {
        byte[] img = disposalCode == 3 ? (byte[]) this.image.clone() : this.image;
        for (int y2 = 0; y2 != h2; y2++) {
            int iPos = (((top + y2) * this.screenW) + left) * 4;
            int i2 = y2 * w2;
            if (trnsIndex < 0) {
                for (int x2 = 0; x2 != w2; x2++) {
                    int index = 255 & srcImage[i2 + x2];
                    img[iPos + 0] = palette[0][index];
                    img[iPos + 1] = palette[1][index];
                    img[iPos + 2] = palette[2][index];
                    img[iPos + 3] = palette[3][index];
                    iPos += 4;
                }
            } else {
                for (int x3 = 0; x3 != w2; x3++) {
                    int index2 = 255 & srcImage[i2 + x3];
                    if (index2 != trnsIndex) {
                        img[iPos + 0] = palette[0][index2];
                        img[iPos + 1] = palette[1][index2];
                        img[iPos + 2] = palette[2][index2];
                        img[iPos + 3] = palette[3][index2];
                    }
                    iPos += 4;
                }
            }
        }
        if (disposalCode != 3) {
            img = (byte[]) img.clone();
        }
        if (disposalCode == 2) {
            restoreToBackground(this.image, left, top, w2, h2);
        }
        return ByteBuffer.wrap(img);
    }

    private ImageMetadata updateMetadata(int w2, int h2, int delayTime) {
        ImageMetadata metaData = new ImageMetadata(null, true, null, null, null, Integer.valueOf(delayTime != 0 ? delayTime * 10 : 40), Integer.valueOf(this.loopCount), Integer.valueOf(w2), Integer.valueOf(h2), null, null, null);
        updateImageMetadata(metaData);
        return metaData;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/iio/gif/GIFImageLoader2$LZWDecoder.class */
    class LZWDecoder {
        private final int initCodeSize;
        private final int clearCode;
        private final int eofCode;
        private int codeSize;
        private int codeMask;
        private int tableIndex;
        private int oldCode;
        private int blockLength = 0;
        private int blockPos = 0;
        private byte[] block = new byte[255];
        private int inData = 0;
        private int inBits = 0;
        private int[] prefix = new int[4096];
        private byte[] suffix = new byte[4096];
        private byte[] initial = new byte[4096];
        private int[] length = new int[4096];
        private byte[] string = new byte[4096];

        public LZWDecoder() throws IOException {
            this.initCodeSize = GIFImageLoader2.this.readByte();
            this.clearCode = 1 << this.initCodeSize;
            this.eofCode = this.clearCode + 1;
            initTable();
        }

        public final int readString() throws IOException {
            int newSuffixIndex;
            int code = getCode();
            if (code == this.eofCode) {
                return -1;
            }
            if (code == this.clearCode) {
                initTable();
                code = getCode();
                if (code == this.eofCode) {
                    return -1;
                }
            } else {
                int ti = this.tableIndex;
                if (code < ti) {
                    newSuffixIndex = code;
                } else {
                    newSuffixIndex = this.oldCode;
                    if (code != ti) {
                        throw new IOException("Bad GIF LZW: Out-of-sequence code!");
                    }
                }
                int oc = this.oldCode;
                this.prefix[ti] = oc;
                this.suffix[ti] = this.initial[newSuffixIndex];
                this.initial[ti] = this.initial[oc];
                this.length[ti] = this.length[oc] + 1;
                this.tableIndex++;
                if (this.tableIndex == (1 << this.codeSize) && this.tableIndex < 4096) {
                    this.codeSize++;
                    this.codeMask = (1 << this.codeSize) - 1;
                }
            }
            int c2 = code;
            int len = this.length[c2];
            for (int i2 = len - 1; i2 >= 0; i2--) {
                this.string[i2] = this.suffix[c2];
                c2 = this.prefix[c2];
            }
            this.oldCode = code;
            return len;
        }

        public final byte[] getString() {
            return this.string;
        }

        public final void waitForTerminator() throws IOException {
            GIFImageLoader2.this.consumeAnExtension();
        }

        private void initTable() {
            int numEntries = 1 << this.initCodeSize;
            for (int i2 = 0; i2 < numEntries; i2++) {
                this.prefix[i2] = -1;
                this.suffix[i2] = (byte) i2;
                this.initial[i2] = (byte) i2;
                this.length[i2] = 1;
            }
            for (int i3 = numEntries; i3 < 4096; i3++) {
                this.prefix[i3] = -1;
                this.length[i3] = 1;
            }
            this.codeSize = this.initCodeSize + 1;
            this.codeMask = (1 << this.codeSize) - 1;
            this.tableIndex = numEntries + 2;
            this.oldCode = 0;
        }

        private int getCode() throws IOException {
            while (this.inBits < this.codeSize) {
                this.inData |= nextByte() << this.inBits;
                this.inBits += 8;
            }
            int code = this.inData & this.codeMask;
            this.inBits -= this.codeSize;
            this.inData >>>= this.codeSize;
            return code;
        }

        private int nextByte() throws IOException {
            if (this.blockPos == this.blockLength) {
                readData();
            }
            byte[] bArr = this.block;
            int i2 = this.blockPos;
            this.blockPos = i2 + 1;
            return bArr[i2] & 255;
        }

        private void readData() throws IOException {
            this.blockPos = 0;
            this.blockLength = GIFImageLoader2.this.readByte();
            if (this.blockLength > 0) {
                GIFImageLoader2.this.readBytes(this.block, 0, this.blockLength);
                return;
            }
            throw new EOFException();
        }
    }
}
