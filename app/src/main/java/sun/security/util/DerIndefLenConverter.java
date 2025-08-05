package sun.security.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import sun.misc.IOUtils;

/* loaded from: rt.jar:sun/security/util/DerIndefLenConverter.class */
class DerIndefLenConverter {
    private static final int LEN_LONG = 128;
    private static final int LEN_MASK = 127;
    private byte[] data;
    private byte[] newData;
    private int newDataPos;
    private int dataPos;
    private int dataSize;
    private int index;
    private int unresolved = 0;
    private ArrayList<Object> ndefsList = new ArrayList<>();
    private int numOfTotalLenBytes = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DerIndefLenConverter.class.desiredAssertionStatus();
    }

    private static boolean isEOC(byte[] bArr, int i2) {
        return bArr[i2] == 0 && bArr[i2 + 1] == 0;
    }

    static boolean isLongForm(int i2) {
        return (i2 & 128) == 128;
    }

    DerIndefLenConverter() {
    }

    static boolean isIndefinite(int i2) {
        return isLongForm(i2) && (i2 & 127) == 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v4 */
    private void parseTag() throws IOException {
        if (isEOC(this.data, this.dataPos)) {
            int length = 0;
            Integer num = 0;
            int size = this.ndefsList.size() - 1;
            while (size >= 0) {
                num = this.ndefsList.get(size);
                if (num instanceof Integer) {
                    break;
                }
                length += ((byte[]) num).length - 3;
                size--;
                num = num;
            }
            if (size < 0) {
                throw new IOException("EOC does not have matching indefinite-length tag");
            }
            byte[] lengthBytes = getLengthBytes((this.dataPos - num.intValue()) + length);
            this.ndefsList.set(size, lengthBytes);
            if (!$assertionsDisabled && this.unresolved <= 0) {
                throw new AssertionError();
            }
            this.unresolved--;
            this.numOfTotalLenBytes += lengthBytes.length - 3;
        }
        this.dataPos++;
    }

    private void writeTag() {
        while (this.dataPos < this.dataSize) {
            if (!$assertionsDisabled && this.dataPos + 1 >= this.dataSize) {
                throw new AssertionError();
            }
            if (isEOC(this.data, this.dataPos)) {
                this.dataPos += 2;
            } else {
                byte[] bArr = this.newData;
                int i2 = this.newDataPos;
                this.newDataPos = i2 + 1;
                byte[] bArr2 = this.data;
                int i3 = this.dataPos;
                this.dataPos = i3 + 1;
                bArr[i2] = bArr2[i3];
                return;
            }
        }
    }

    private int parseLength() throws IOException {
        if (this.dataPos == this.dataSize) {
            return 0;
        }
        byte[] bArr = this.data;
        int i2 = this.dataPos;
        this.dataPos = i2 + 1;
        int i3 = bArr[i2] & 255;
        if (isIndefinite(i3)) {
            this.ndefsList.add(new Integer(this.dataPos));
            this.unresolved++;
            return 0;
        }
        int i4 = 0;
        if (isLongForm(i3)) {
            int i5 = i3 & 127;
            if (i5 > 4) {
                throw new IOException("Too much data");
            }
            if (this.dataSize - this.dataPos < i5 + 1) {
                return -1;
            }
            for (int i6 = 0; i6 < i5; i6++) {
                byte[] bArr2 = this.data;
                int i7 = this.dataPos;
                this.dataPos = i7 + 1;
                i4 = (i4 << 8) + (bArr2[i7] & 255);
            }
            if (i4 < 0) {
                throw new IOException("Invalid length bytes");
            }
        } else {
            i4 = i3 & 127;
        }
        return i4;
    }

    private void writeLengthAndValue() throws IOException {
        if (this.dataPos == this.dataSize) {
            return;
        }
        int i2 = 0;
        byte[] bArr = this.data;
        int i3 = this.dataPos;
        this.dataPos = i3 + 1;
        int i4 = bArr[i3] & 255;
        if (isIndefinite(i4)) {
            ArrayList<Object> arrayList = this.ndefsList;
            int i5 = this.index;
            this.index = i5 + 1;
            byte[] bArr2 = (byte[]) arrayList.get(i5);
            System.arraycopy(bArr2, 0, this.newData, this.newDataPos, bArr2.length);
            this.newDataPos += bArr2.length;
            return;
        }
        if (isLongForm(i4)) {
            int i6 = i4 & 127;
            for (int i7 = 0; i7 < i6; i7++) {
                byte[] bArr3 = this.data;
                int i8 = this.dataPos;
                this.dataPos = i8 + 1;
                i2 = (i2 << 8) + (bArr3[i8] & 255);
            }
            if (i2 < 0) {
                throw new IOException("Invalid length bytes");
            }
        } else {
            i2 = i4 & 127;
        }
        writeLength(i2);
        writeValue(i2);
    }

    private void writeLength(int i2) {
        if (i2 < 128) {
            byte[] bArr = this.newData;
            int i3 = this.newDataPos;
            this.newDataPos = i3 + 1;
            bArr[i3] = (byte) i2;
            return;
        }
        if (i2 < 256) {
            byte[] bArr2 = this.newData;
            int i4 = this.newDataPos;
            this.newDataPos = i4 + 1;
            bArr2[i4] = -127;
            byte[] bArr3 = this.newData;
            int i5 = this.newDataPos;
            this.newDataPos = i5 + 1;
            bArr3[i5] = (byte) i2;
            return;
        }
        if (i2 < 65536) {
            byte[] bArr4 = this.newData;
            int i6 = this.newDataPos;
            this.newDataPos = i6 + 1;
            bArr4[i6] = -126;
            byte[] bArr5 = this.newData;
            int i7 = this.newDataPos;
            this.newDataPos = i7 + 1;
            bArr5[i7] = (byte) (i2 >> 8);
            byte[] bArr6 = this.newData;
            int i8 = this.newDataPos;
            this.newDataPos = i8 + 1;
            bArr6[i8] = (byte) i2;
            return;
        }
        if (i2 < 16777216) {
            byte[] bArr7 = this.newData;
            int i9 = this.newDataPos;
            this.newDataPos = i9 + 1;
            bArr7[i9] = -125;
            byte[] bArr8 = this.newData;
            int i10 = this.newDataPos;
            this.newDataPos = i10 + 1;
            bArr8[i10] = (byte) (i2 >> 16);
            byte[] bArr9 = this.newData;
            int i11 = this.newDataPos;
            this.newDataPos = i11 + 1;
            bArr9[i11] = (byte) (i2 >> 8);
            byte[] bArr10 = this.newData;
            int i12 = this.newDataPos;
            this.newDataPos = i12 + 1;
            bArr10[i12] = (byte) i2;
            return;
        }
        byte[] bArr11 = this.newData;
        int i13 = this.newDataPos;
        this.newDataPos = i13 + 1;
        bArr11[i13] = -124;
        byte[] bArr12 = this.newData;
        int i14 = this.newDataPos;
        this.newDataPos = i14 + 1;
        bArr12[i14] = (byte) (i2 >> 24);
        byte[] bArr13 = this.newData;
        int i15 = this.newDataPos;
        this.newDataPos = i15 + 1;
        bArr13[i15] = (byte) (i2 >> 16);
        byte[] bArr14 = this.newData;
        int i16 = this.newDataPos;
        this.newDataPos = i16 + 1;
        bArr14[i16] = (byte) (i2 >> 8);
        byte[] bArr15 = this.newData;
        int i17 = this.newDataPos;
        this.newDataPos = i17 + 1;
        bArr15[i17] = (byte) i2;
    }

    private byte[] getLengthBytes(int i2) {
        byte[] bArr;
        if (i2 < 128) {
            int i3 = 0 + 1;
            bArr = new byte[]{(byte) i2};
        } else if (i2 < 256) {
            bArr = new byte[2];
            int i4 = 0 + 1;
            bArr[0] = -127;
            int i5 = i4 + 1;
            bArr[i4] = (byte) i2;
        } else if (i2 < 65536) {
            bArr = new byte[3];
            int i6 = 0 + 1;
            bArr[0] = -126;
            int i7 = i6 + 1;
            bArr[i6] = (byte) (i2 >> 8);
            int i8 = i7 + 1;
            bArr[i7] = (byte) i2;
        } else if (i2 < 16777216) {
            bArr = new byte[4];
            int i9 = 0 + 1;
            bArr[0] = -125;
            int i10 = i9 + 1;
            bArr[i9] = (byte) (i2 >> 16);
            int i11 = i10 + 1;
            bArr[i10] = (byte) (i2 >> 8);
            int i12 = i11 + 1;
            bArr[i11] = (byte) i2;
        } else {
            bArr = new byte[5];
            int i13 = 0 + 1;
            bArr[0] = -124;
            int i14 = i13 + 1;
            bArr[i13] = (byte) (i2 >> 24);
            int i15 = i14 + 1;
            bArr[i14] = (byte) (i2 >> 16);
            int i16 = i15 + 1;
            bArr[i15] = (byte) (i2 >> 8);
            int i17 = i16 + 1;
            bArr[i16] = (byte) i2;
        }
        return bArr;
    }

    private int getNumOfLenBytes(int i2) {
        int i3;
        if (i2 < 128) {
            i3 = 1;
        } else if (i2 < 256) {
            i3 = 2;
        } else if (i2 < 65536) {
            i3 = 3;
        } else if (i2 < 16777216) {
            i3 = 4;
        } else {
            i3 = 5;
        }
        return i3;
    }

    private void writeValue(int i2) {
        System.arraycopy(this.data, this.dataPos, this.newData, this.newDataPos, i2);
        this.dataPos += i2;
        this.newDataPos += i2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x008b, code lost:
    
        if (r6.unresolved == 0) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x008e, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0090, code lost:
    
        r0 = r6.dataSize - r6.dataPos;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x009d, code lost:
    
        if (sun.security.util.DerIndefLenConverter.$assertionsDisabled != false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00a1, code lost:
    
        if (r0 >= 0) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00ab, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ac, code lost:
    
        r6.dataSize = r6.dataPos;
        r6.newData = new byte[(r6.dataSize + r6.numOfTotalLenBytes) + r0];
        r6.dataPos = 0;
        r6.newDataPos = 0;
        r6.index = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00dc, code lost:
    
        if (r6.dataPos >= r6.dataSize) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00df, code lost:
    
        writeTag();
        writeLengthAndValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00ea, code lost:
    
        java.lang.System.arraycopy(r7, r6.dataSize, r6.newData, r6.dataSize + r6.numOfTotalLenBytes, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0104, code lost:
    
        return r6.newData;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    byte[] convertBytes(byte[] r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 261
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.DerIndefLenConverter.convertBytes(byte[]):byte[]");
    }

    public static byte[] convertStream(InputStream inputStream, byte b2, byte b3) throws IOException {
        int i2 = 2;
        int iAvailable = inputStream.available();
        byte[] bArrCopyOf = new byte[iAvailable + 2];
        bArrCopyOf[0] = b3;
        bArrCopyOf[1] = b2;
        while (true) {
            int nBytes = IOUtils.readNBytes(inputStream, bArrCopyOf, i2, iAvailable);
            if (nBytes != iAvailable) {
                iAvailable = nBytes;
                bArrCopyOf = Arrays.copyOf(bArrCopyOf, i2 + nBytes);
            }
            byte[] bArrConvertBytes = new DerIndefLenConverter().convertBytes(bArrCopyOf);
            if (bArrConvertBytes == null) {
                int i3 = inputStream.read();
                if (i3 == -1) {
                    throw new IOException("not enough data to resolve indef len BER");
                }
                int iAvailable2 = inputStream.available();
                bArrCopyOf = Arrays.copyOf(bArrCopyOf, i2 + iAvailable + 1 + iAvailable2);
                bArrCopyOf[i2 + iAvailable] = (byte) i3;
                i2 = i2 + iAvailable + 1;
                iAvailable = iAvailable2;
            } else {
                return bArrConvertBytes;
            }
        }
    }
}
