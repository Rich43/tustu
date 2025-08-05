package org.jpedal.jbig2.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.DecodeIntResult;
import org.jpedal.jbig2.decoders.HuffmanDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.decoders.MMRDecoder;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/image/JBIG2Bitmap.class */
public final class JBIG2Bitmap {
    private int width;
    private int height;
    private int line;
    private int bitmapNumber;
    private BitSet data;
    private static int counter = 0;
    private ArithmeticDecoder arithmeticDecoder;
    private HuffmanDecoder huffmanDecoder;
    private MMRDecoder mmrDecoder;

    public JBIG2Bitmap(int width, int height, ArithmeticDecoder arithmeticDecoder, HuffmanDecoder huffmanDecoder, MMRDecoder mmrDecoder) {
        this.width = width;
        this.height = height;
        this.arithmeticDecoder = arithmeticDecoder;
        this.huffmanDecoder = huffmanDecoder;
        this.mmrDecoder = mmrDecoder;
        this.line = (width + 7) >> 3;
        this.data = new BitSet(width * height);
    }

    /* JADX WARN: Removed duplicated region for block: B:142:0x0465 A[PHI: r24
  0x0465: PHI (r24v2 'ltp' boolean) = (r24v1 'ltp' boolean), (r24v4 'ltp' boolean) binds: [B:131:0x042d, B:140:0x0455] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void readBitmap(boolean r8, int r9, boolean r10, boolean r11, org.jpedal.jbig2.image.JBIG2Bitmap r12, short[] r13, short[] r14, int r15) throws org.jpedal.jbig2.JBIG2Exception, java.io.IOException {
        /*
            Method dump skipped, instructions count: 2233
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jpedal.jbig2.image.JBIG2Bitmap.readBitmap(boolean, int, boolean, boolean, org.jpedal.jbig2.image.JBIG2Bitmap, short[], short[], int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x02f8 A[PHI: r37 r39 r41 r43
  0x02f8: PHI (r37v12 'typicalPredictionGenericRefinementCX0' long) = 
  (r37v11 'typicalPredictionGenericRefinementCX0' long)
  (r37v14 'typicalPredictionGenericRefinementCX0' long)
  (r37v14 'typicalPredictionGenericRefinementCX0' long)
  (r37v14 'typicalPredictionGenericRefinementCX0' long)
 binds: [B:18:0x024e, B:34:0x02d7, B:36:0x02e0, B:38:0x02e9] A[DONT_GENERATE, DONT_INLINE]
  0x02f8: PHI (r39v12 'typicalPredictionGenericRefinementCX1' long) = 
  (r39v11 'typicalPredictionGenericRefinementCX1' long)
  (r39v14 'typicalPredictionGenericRefinementCX1' long)
  (r39v14 'typicalPredictionGenericRefinementCX1' long)
  (r39v14 'typicalPredictionGenericRefinementCX1' long)
 binds: [B:18:0x024e, B:34:0x02d7, B:36:0x02e0, B:38:0x02e9] A[DONT_GENERATE, DONT_INLINE]
  0x02f8: PHI (r41v12 'typicalPredictionGenericRefinementCX2' long) = 
  (r41v11 'typicalPredictionGenericRefinementCX2' long)
  (r41v14 'typicalPredictionGenericRefinementCX2' long)
  (r41v14 'typicalPredictionGenericRefinementCX2' long)
  (r41v14 'typicalPredictionGenericRefinementCX2' long)
 binds: [B:18:0x024e, B:34:0x02d7, B:36:0x02e0, B:38:0x02e9] A[DONT_GENERATE, DONT_INLINE]
  0x02f8: PHI (r43v9 'ltp' boolean) = (r43v8 'ltp' boolean), (r43v11 'ltp' boolean), (r43v11 'ltp' boolean), (r43v11 'ltp' boolean) binds: [B:18:0x024e, B:34:0x02d7, B:36:0x02e0, B:38:0x02e9] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x05aa A[PHI: r37 r39 r41 r43
  0x05aa: PHI (r37v3 'typicalPredictionGenericRefinementCX0' long) = 
  (r37v2 'typicalPredictionGenericRefinementCX0' long)
  (r37v5 'typicalPredictionGenericRefinementCX0' long)
  (r37v5 'typicalPredictionGenericRefinementCX0' long)
  (r37v5 'typicalPredictionGenericRefinementCX0' long)
 binds: [B:52:0x04ff, B:68:0x0589, B:70:0x0592, B:72:0x059b] A[DONT_GENERATE, DONT_INLINE]
  0x05aa: PHI (r39v3 'typicalPredictionGenericRefinementCX1' long) = 
  (r39v2 'typicalPredictionGenericRefinementCX1' long)
  (r39v5 'typicalPredictionGenericRefinementCX1' long)
  (r39v5 'typicalPredictionGenericRefinementCX1' long)
  (r39v5 'typicalPredictionGenericRefinementCX1' long)
 binds: [B:52:0x04ff, B:68:0x0589, B:70:0x0592, B:72:0x059b] A[DONT_GENERATE, DONT_INLINE]
  0x05aa: PHI (r41v3 'typicalPredictionGenericRefinementCX2' long) = 
  (r41v2 'typicalPredictionGenericRefinementCX2' long)
  (r41v5 'typicalPredictionGenericRefinementCX2' long)
  (r41v5 'typicalPredictionGenericRefinementCX2' long)
  (r41v5 'typicalPredictionGenericRefinementCX2' long)
 binds: [B:52:0x04ff, B:68:0x0589, B:70:0x0592, B:72:0x059b] A[DONT_GENERATE, DONT_INLINE]
  0x05aa: PHI (r43v3 'ltp' boolean) = (r43v2 'ltp' boolean), (r43v5 'ltp' boolean), (r43v5 'ltp' boolean), (r43v5 'ltp' boolean) binds: [B:52:0x04ff, B:68:0x0589, B:70:0x0592, B:72:0x059b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void readGenericRefinementRegion(int r8, boolean r9, org.jpedal.jbig2.image.JBIG2Bitmap r10, int r11, int r12, short[] r13, short[] r14) throws org.jpedal.jbig2.JBIG2Exception, java.io.IOException {
        /*
            Method dump skipped, instructions count: 1557
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jpedal.jbig2.image.JBIG2Bitmap.readGenericRefinementRegion(int, boolean, org.jpedal.jbig2.image.JBIG2Bitmap, int, int, short[], short[]):void");
    }

    public void readTextRegion(boolean huffman, boolean symbolRefine, int noOfSymbolInstances, int logStrips, int noOfSymbols, int[][] symbolCodeTable, int symbolCodeLength, JBIG2Bitmap[] symbols, int defaultPixel, int combinationOperator, boolean transposed, int referenceCorner, int sOffset, int[][] huffmanFSTable, int[][] huffmanDSTable, int[][] huffmanDTTable, int[][] huffmanRDWTable, int[][] huffmanRDHTable, int[][] huffmanRDXTable, int[][] huffmanRDYTable, int[][] huffmanRSizeTable, int template, short[] symbolRegionAdaptiveTemplateX, short[] symbolRegionAdaptiveTemplateY, JBIG2StreamDecoder decoder) throws JBIG2Exception, IOException {
        int t2;
        int dt;
        int ds;
        int dt2;
        long symbolID;
        int ri;
        JBIG2Bitmap symbolBitmap;
        int refinementDeltaWidth;
        int refinementDeltaHeight;
        int refinementDeltaX;
        int refinementDeltaY;
        DecodeIntResult decodeIntResult;
        int strips = 1 << logStrips;
        clear(defaultPixel);
        if (huffman) {
            t2 = this.huffmanDecoder.decodeInt(huffmanDTTable).intResult();
        } else {
            t2 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadtStats).intResult();
        }
        int t3 = t2 * (-strips);
        int currentInstance = 0;
        int firstS = 0;
        while (currentInstance < noOfSymbolInstances) {
            if (huffman) {
                dt = this.huffmanDecoder.decodeInt(huffmanDTTable).intResult();
            } else {
                dt = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadtStats).intResult();
            }
            t3 += dt * strips;
            if (huffman) {
                ds = this.huffmanDecoder.decodeInt(huffmanFSTable).intResult();
            } else {
                ds = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iafsStats).intResult();
            }
            firstS += ds;
            int i2 = firstS;
            while (true) {
                int s2 = i2;
                if (strips == 1) {
                    dt2 = 0;
                } else if (huffman) {
                    dt2 = decoder.readBits(logStrips);
                } else {
                    dt2 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iaitStats).intResult();
                }
                int tt = t3 + dt2;
                if (huffman) {
                    if (symbolCodeTable != null) {
                        symbolID = this.huffmanDecoder.decodeInt(symbolCodeTable).intResult();
                    } else {
                        symbolID = decoder.readBits(symbolCodeLength);
                    }
                } else {
                    symbolID = this.arithmeticDecoder.decodeIAID(symbolCodeLength, this.arithmeticDecoder.iaidStats);
                }
                if (symbolID >= noOfSymbols) {
                    if (JBIG2StreamDecoder.debug) {
                        System.out.println("Invalid symbol number in JBIG2 text region");
                    }
                } else {
                    if (symbolRefine) {
                        if (huffman) {
                            ri = decoder.readBit();
                        } else {
                            ri = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iariStats).intResult();
                        }
                    } else {
                        ri = 0;
                    }
                    if (ri != 0) {
                        if (huffman) {
                            refinementDeltaWidth = this.huffmanDecoder.decodeInt(huffmanRDWTable).intResult();
                            refinementDeltaHeight = this.huffmanDecoder.decodeInt(huffmanRDHTable).intResult();
                            refinementDeltaX = this.huffmanDecoder.decodeInt(huffmanRDXTable).intResult();
                            refinementDeltaY = this.huffmanDecoder.decodeInt(huffmanRDYTable).intResult();
                            decoder.consumeRemainingBits();
                            this.arithmeticDecoder.start();
                        } else {
                            refinementDeltaWidth = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardwStats).intResult();
                            refinementDeltaHeight = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardhStats).intResult();
                            refinementDeltaX = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardxStats).intResult();
                            refinementDeltaY = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardyStats).intResult();
                        }
                        int refinementDeltaX2 = ((refinementDeltaWidth >= 0 ? refinementDeltaWidth : refinementDeltaWidth - 1) / 2) + refinementDeltaX;
                        int refinementDeltaY2 = ((refinementDeltaHeight >= 0 ? refinementDeltaHeight : refinementDeltaHeight - 1) / 2) + refinementDeltaY;
                        symbolBitmap = new JBIG2Bitmap(refinementDeltaWidth + symbols[(int) symbolID].width, refinementDeltaHeight + symbols[(int) symbolID].height, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
                        symbolBitmap.readGenericRefinementRegion(template, false, symbols[(int) symbolID], refinementDeltaX2, refinementDeltaY2, symbolRegionAdaptiveTemplateX, symbolRegionAdaptiveTemplateY);
                    } else {
                        symbolBitmap = symbols[(int) symbolID];
                    }
                    int bitmapWidth = symbolBitmap.width - 1;
                    int bitmapHeight = symbolBitmap.height - 1;
                    if (transposed) {
                        switch (referenceCorner) {
                            case 0:
                                combine(symbolBitmap, tt, s2, combinationOperator);
                                break;
                            case 1:
                                combine(symbolBitmap, tt, s2, combinationOperator);
                                break;
                            case 2:
                                combine(symbolBitmap, tt - bitmapWidth, s2, combinationOperator);
                                break;
                            case 3:
                                combine(symbolBitmap, tt - bitmapWidth, s2, combinationOperator);
                                break;
                        }
                        s2 += bitmapHeight;
                    } else {
                        switch (referenceCorner) {
                            case 0:
                                combine(symbolBitmap, s2, tt - bitmapHeight, combinationOperator);
                                break;
                            case 1:
                                combine(symbolBitmap, s2, tt, combinationOperator);
                                break;
                            case 2:
                                combine(symbolBitmap, s2, tt - bitmapHeight, combinationOperator);
                                break;
                            case 3:
                                combine(symbolBitmap, s2, tt, combinationOperator);
                                break;
                        }
                        s2 += bitmapWidth;
                    }
                }
                currentInstance++;
                if (huffman) {
                    decodeIntResult = this.huffmanDecoder.decodeInt(huffmanDSTable);
                } else {
                    decodeIntResult = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadsStats);
                }
                if (!decodeIntResult.booleanResult()) {
                    break;
                }
                int ds2 = decodeIntResult.intResult();
                i2 = s2 + sOffset + ds2;
            }
        }
    }

    public void clear(int defPixel) {
        this.data.set(0, this.data.size(), defPixel == 1);
    }

    public void combine(JBIG2Bitmap bitmap, int x2, int y2, long combOp) {
        int srcWidth = bitmap.width;
        int srcHeight = bitmap.height;
        int srcRow = 0;
        int srcCol = 0;
        for (int row = y2; row < y2 + srcHeight; row++) {
            for (int col = x2; col < x2 + srcWidth; col++) {
                int srcPixel = bitmap.getPixel(srcCol, srcRow);
                switch ((int) combOp) {
                    case 0:
                        setPixel(col, row, getPixel(col, row) | srcPixel);
                        break;
                    case 1:
                        setPixel(col, row, getPixel(col, row) & srcPixel);
                        break;
                    case 2:
                        setPixel(col, row, getPixel(col, row) ^ srcPixel);
                        break;
                    case 3:
                        if ((getPixel(col, row) == 1 && srcPixel == 1) || (getPixel(col, row) == 0 && srcPixel == 0)) {
                            setPixel(col, row, 1);
                            break;
                        } else {
                            setPixel(col, row, 0);
                            break;
                        }
                    case 4:
                        setPixel(col, row, srcPixel);
                        break;
                }
                srcCol++;
            }
            srcCol = 0;
            srcRow++;
        }
    }

    private void duplicateRow(int yDest, int ySrc) {
        for (int i2 = 0; i2 < this.width; i2++) {
            setPixel(i2, yDest, getPixel(i2, ySrc));
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public byte[] getData(boolean switchPixelColor) {
        byte[] bytes = new byte[this.height * this.line];
        getData(bytes, switchPixelColor);
        return bytes;
    }

    public void getData(byte[] bytes, boolean switchPixelColor) {
        int count = 0;
        int offset = 0;
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                if (this.data.get(count)) {
                    int bite = (count + offset) / 8;
                    int bit = (count + offset) % 8;
                    bytes[bite] = (byte) (bytes[bite] | (1 << (7 - bit)));
                }
                count++;
            }
            offset = ((this.line * 8) * (row + 1)) - count;
        }
        if (switchPixelColor) {
            for (int i2 = 0; i2 < bytes.length; i2++) {
                int i3 = i2;
                bytes[i3] = (byte) (bytes[i3] ^ 255);
            }
        }
    }

    public JBIG2Bitmap getSlice(int x2, int y2, int width, int height) {
        JBIG2Bitmap slice = new JBIG2Bitmap(width, height, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
        int sliceRow = 0;
        int sliceCol = 0;
        for (int row = y2; row < height; row++) {
            for (int col = x2; col < x2 + width; col++) {
                slice.setPixel(sliceCol, sliceRow, getPixel(col, row));
                sliceCol++;
            }
            sliceCol = 0;
            sliceRow++;
        }
        return slice;
    }

    private void setPixel(int col, int row, FastBitSet data, int value) {
        if (value == 1) {
            data.set(row, col);
        } else {
            data.clear(row, col);
        }
    }

    private void setPixel(int col, int row, BitSet data, int value) {
        int index = (row * this.width) + col;
        data.set(index, value == 1);
    }

    public void setPixel(int col, int row, int value) {
        setPixel(col, row, this.data, value);
    }

    public int getPixel(int col, int row) {
        if (row < 0) {
            row = 0;
        }
        return this.data.get((row * this.width) + col) ? 1 : 0;
    }

    public void expand(int newHeight, int defaultPixel) {
        BitSet newData = new BitSet(newHeight * this.width);
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                setPixel(col, row, newData, getPixel(col, row));
            }
        }
        this.height = newHeight;
        this.data = newData;
    }

    public void setBitmapNumber(int segmentNumber) {
        this.bitmapNumber = segmentNumber;
    }

    public int getBitmapNumber() {
        return this.bitmapNumber;
    }

    public BufferedImage getBufferedImage() {
        byte[] bytes = getData(true);
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        byte[] copy = new byte[len];
        System.arraycopy(bytes, 0, copy, 0, len);
        DataBuffer db = new DataBufferByte(copy, copy.length);
        WritableRaster raster = Raster.createPackedRaster(db, this.width, this.height, 1, (Point) null);
        BufferedImage image = new BufferedImage(this.width, this.height, 12);
        image.setData(raster);
        return image;
    }

    /* loaded from: icepdf-core.jar:org/jpedal/jbig2/image/JBIG2Bitmap$FastBitSet.class */
    static final class FastBitSet {
        byte[][] bytes;

        /* renamed from: w, reason: collision with root package name */
        int f13140w;

        /* renamed from: h, reason: collision with root package name */
        int f13141h;

        public FastBitSet(int width, int height) {
            this.bytes = new byte[height][(width + 7) / 8];
            this.f13140w = width;
            this.f13141h = height;
        }

        public byte getByte(int row, int col) {
            int offset = col / 8;
            int mod = col % 8;
            if (mod == 0) {
                return this.bytes[row][offset];
            }
            byte leftMask = (byte) (255 >> (8 - mod));
            byte rightMask = (byte) (255 << mod);
            byte left = (byte) ((this.bytes[row][offset] & leftMask) << (8 - mod));
            if (offset + 1 >= this.bytes[row].length) {
                System.out.println("returning");
                return left;
            }
            byte right = (byte) ((this.bytes[row][offset + 1] & rightMask) >> mod);
            return (byte) (left | right);
        }

        public void setByte(int row, int col, byte bits) {
            int offset = col / 8;
            int mod = col % 8;
            if (mod == 0) {
                this.bytes[row][offset] = bits;
                return;
            }
            byte left = (byte) (bits >> mod);
            byte leftMask = (byte) (255 << (8 - mod));
            byte[] bArr = this.bytes[row];
            bArr[offset] = (byte) (bArr[offset] & leftMask);
            byte[] bArr2 = this.bytes[row];
            bArr2[offset] = (byte) (bArr2[offset] | left);
            if (offset + 1 >= this.bytes[row].length) {
                return;
            }
            byte right = (byte) (bits << (8 - mod));
            byte rightMask = (byte) (255 >> mod);
            byte[] bArr3 = this.bytes[row];
            int i2 = offset + 1;
            bArr3[i2] = (byte) (bArr3[i2] & rightMask);
            byte[] bArr4 = this.bytes[row];
            int i3 = offset + 1;
            bArr4[i3] = (byte) (bArr4[i3] | right);
        }

        public void set(int row, int col) {
            byte bit = (byte) (1 << (col % 8));
            byte[] bArr = this.bytes[row];
            int i2 = col / 8;
            bArr[i2] = (byte) (bArr[i2] | bit);
        }

        public void clear(int row, int col) {
            byte bit = (byte) (1 << (col % 8));
            byte[] bArr = this.bytes[row];
            int i2 = col / 8;
            bArr[i2] = (byte) (bArr[i2] & (bit ^ (-1)));
        }

        public boolean get(int row, int col) {
            byte bit = (byte) (1 << (col % 8));
            return (this.bytes[row][col / 8] & bit) != 0;
        }

        public void reset(boolean set) {
            byte[][] arr$ = this.bytes;
            for (byte[] aByte : arr$) {
                Arrays.fill(aByte, set ? (byte) -1 : (byte) 0);
            }
        }
    }
}
