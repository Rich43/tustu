package com.sun.jndi.ldap;

import com.sun.jndi.ldap.Ber;
import java.io.UnsupportedEncodingException;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/jndi/ldap/BerEncoder.class */
public final class BerEncoder extends Ber {
    private int curSeqIndex;
    private int[] seqOffset;
    private static final int INITIAL_SEQUENCES = 16;
    private static final int DEFAULT_BUFSIZE = 1024;
    private static final int BUF_GROWTH_FACTOR = 8;

    public BerEncoder() {
        this(1024);
    }

    public BerEncoder(int i2) {
        this.buf = new byte[i2];
        this.bufsize = i2;
        this.offset = 0;
        this.seqOffset = new int[16];
        this.curSeqIndex = 0;
    }

    public void reset() {
        while (this.offset > 0) {
            byte[] bArr = this.buf;
            int i2 = this.offset - 1;
            this.offset = i2;
            bArr[i2] = 0;
        }
        while (this.curSeqIndex > 0) {
            int[] iArr = this.seqOffset;
            int i3 = this.curSeqIndex - 1;
            this.curSeqIndex = i3;
            iArr[i3] = 0;
        }
    }

    public int getDataLen() {
        return this.offset;
    }

    public byte[] getBuf() {
        if (this.curSeqIndex != 0) {
            throw new IllegalStateException("BER encode error: Unbalanced SEQUENCEs.");
        }
        return this.buf;
    }

    public byte[] getTrimmedBuf() {
        int dataLen = getDataLen();
        byte[] bArr = new byte[dataLen];
        System.arraycopy(getBuf(), 0, bArr, 0, dataLen);
        return bArr;
    }

    public void beginSeq(int i2) {
        if (this.curSeqIndex >= this.seqOffset.length) {
            int[] iArr = new int[this.seqOffset.length * 2];
            for (int i3 = 0; i3 < this.seqOffset.length; i3++) {
                iArr[i3] = this.seqOffset[i3];
            }
            this.seqOffset = iArr;
        }
        encodeByte(i2);
        this.seqOffset[this.curSeqIndex] = this.offset;
        ensureFreeBytes(3);
        this.offset += 3;
        this.curSeqIndex++;
    }

    public void endSeq() throws Ber.EncodeException {
        this.curSeqIndex--;
        if (this.curSeqIndex < 0) {
            throw new IllegalStateException("BER encode error: Unbalanced SEQUENCEs.");
        }
        int i2 = this.seqOffset[this.curSeqIndex] + 3;
        int i3 = this.offset - i2;
        if (i3 <= 127) {
            shiftSeqData(i2, i3, -2);
            this.buf[this.seqOffset[this.curSeqIndex]] = (byte) i3;
            return;
        }
        if (i3 <= 255) {
            shiftSeqData(i2, i3, -1);
            this.buf[this.seqOffset[this.curSeqIndex]] = -127;
            this.buf[this.seqOffset[this.curSeqIndex] + 1] = (byte) i3;
        } else if (i3 <= 65535) {
            this.buf[this.seqOffset[this.curSeqIndex]] = -126;
            this.buf[this.seqOffset[this.curSeqIndex] + 1] = (byte) (i3 >> 8);
            this.buf[this.seqOffset[this.curSeqIndex] + 2] = (byte) i3;
        } else {
            if (i3 <= 16777215) {
                shiftSeqData(i2, i3, 1);
                this.buf[this.seqOffset[this.curSeqIndex]] = -125;
                this.buf[this.seqOffset[this.curSeqIndex] + 1] = (byte) (i3 >> 16);
                this.buf[this.seqOffset[this.curSeqIndex] + 2] = (byte) (i3 >> 8);
                this.buf[this.seqOffset[this.curSeqIndex] + 3] = (byte) i3;
                return;
            }
            throw new Ber.EncodeException("SEQUENCE too long");
        }
    }

    private void shiftSeqData(int i2, int i3, int i4) {
        if (i4 > 0) {
            ensureFreeBytes(i4);
        }
        System.arraycopy(this.buf, i2, this.buf, i2 + i4, i3);
        this.offset += i4;
    }

    public void encodeByte(int i2) {
        ensureFreeBytes(1);
        byte[] bArr = this.buf;
        int i3 = this.offset;
        this.offset = i3 + 1;
        bArr[i3] = (byte) i2;
    }

    public void encodeInt(int i2) {
        encodeInt(i2, 2);
    }

    public void encodeInt(int i2, int i3) {
        int i4 = 4;
        while (true) {
            if (((i2 & (-8388608)) != 0 && (i2 & (-8388608)) != -8388608) || i4 <= 1) {
                break;
            }
            i4--;
            i2 <<= 8;
        }
        encodeInt(i2, i3, i4);
    }

    private void encodeInt(int i2, int i3, int i4) {
        if (i4 > 4) {
            throw new IllegalArgumentException("BER encode error: INTEGER too long.");
        }
        ensureFreeBytes(2 + i4);
        byte[] bArr = this.buf;
        int i5 = this.offset;
        this.offset = i5 + 1;
        bArr[i5] = (byte) i3;
        byte[] bArr2 = this.buf;
        int i6 = this.offset;
        this.offset = i6 + 1;
        bArr2[i6] = (byte) i4;
        while (true) {
            int i7 = i4;
            i4--;
            if (i7 > 0) {
                byte[] bArr3 = this.buf;
                int i8 = this.offset;
                this.offset = i8 + 1;
                bArr3[i8] = (byte) ((i2 & (-16777216)) >> 24);
                i2 <<= 8;
            } else {
                return;
            }
        }
    }

    public void encodeBoolean(boolean z2) {
        encodeBoolean(z2, 1);
    }

    public void encodeBoolean(boolean z2, int i2) {
        ensureFreeBytes(3);
        byte[] bArr = this.buf;
        int i3 = this.offset;
        this.offset = i3 + 1;
        bArr[i3] = (byte) i2;
        byte[] bArr2 = this.buf;
        int i4 = this.offset;
        this.offset = i4 + 1;
        bArr2[i4] = 1;
        byte[] bArr3 = this.buf;
        int i5 = this.offset;
        this.offset = i5 + 1;
        bArr3[i5] = z2 ? (byte) -1 : (byte) 0;
    }

    public void encodeString(String str, boolean z2) throws Ber.EncodeException {
        encodeString(str, 4, z2);
    }

    public void encodeString(String str, int i2, boolean z2) throws Ber.EncodeException {
        int length;
        encodeByte(i2);
        int i3 = 0;
        byte[] bytes = null;
        if (str == null) {
            length = 0;
        } else if (z2) {
            try {
                bytes = str.getBytes(InternalZipConstants.CHARSET_UTF8);
                length = bytes.length;
            } catch (UnsupportedEncodingException e2) {
                throw new Ber.EncodeException("UTF8 not available on platform");
            }
        } else {
            try {
                bytes = str.getBytes("8859_1");
                length = bytes.length;
            } catch (UnsupportedEncodingException e3) {
                throw new Ber.EncodeException("8859_1 not available on platform");
            }
        }
        encodeLength(length);
        ensureFreeBytes(length);
        while (i3 < length) {
            byte[] bArr = this.buf;
            int i4 = this.offset;
            this.offset = i4 + 1;
            int i5 = i3;
            i3++;
            bArr[i4] = bytes[i5];
        }
    }

    public void encodeOctetString(byte[] bArr, int i2, int i3, int i4) throws Ber.EncodeException {
        encodeByte(i2);
        encodeLength(i4);
        if (i4 > 0) {
            ensureFreeBytes(i4);
            System.arraycopy(bArr, i3, this.buf, this.offset, i4);
            this.offset += i4;
        }
    }

    public void encodeOctetString(byte[] bArr, int i2) throws Ber.EncodeException {
        encodeOctetString(bArr, i2, 0, bArr.length);
    }

    private void encodeLength(int i2) throws Ber.EncodeException {
        ensureFreeBytes(4);
        if (i2 < 128) {
            byte[] bArr = this.buf;
            int i3 = this.offset;
            this.offset = i3 + 1;
            bArr[i3] = (byte) i2;
            return;
        }
        if (i2 <= 255) {
            byte[] bArr2 = this.buf;
            int i4 = this.offset;
            this.offset = i4 + 1;
            bArr2[i4] = -127;
            byte[] bArr3 = this.buf;
            int i5 = this.offset;
            this.offset = i5 + 1;
            bArr3[i5] = (byte) i2;
            return;
        }
        if (i2 <= 65535) {
            byte[] bArr4 = this.buf;
            int i6 = this.offset;
            this.offset = i6 + 1;
            bArr4[i6] = -126;
            byte[] bArr5 = this.buf;
            int i7 = this.offset;
            this.offset = i7 + 1;
            bArr5[i7] = (byte) (i2 >> 8);
            byte[] bArr6 = this.buf;
            int i8 = this.offset;
            this.offset = i8 + 1;
            bArr6[i8] = (byte) (i2 & 255);
            return;
        }
        if (i2 <= 16777215) {
            byte[] bArr7 = this.buf;
            int i9 = this.offset;
            this.offset = i9 + 1;
            bArr7[i9] = -125;
            byte[] bArr8 = this.buf;
            int i10 = this.offset;
            this.offset = i10 + 1;
            bArr8[i10] = (byte) (i2 >> 16);
            byte[] bArr9 = this.buf;
            int i11 = this.offset;
            this.offset = i11 + 1;
            bArr9[i11] = (byte) (i2 >> 8);
            byte[] bArr10 = this.buf;
            int i12 = this.offset;
            this.offset = i12 + 1;
            bArr10[i12] = (byte) (i2 & 255);
            return;
        }
        throw new Ber.EncodeException("string too long");
    }

    public void encodeStringArray(String[] strArr, boolean z2) throws Ber.EncodeException {
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            encodeString(str, z2);
        }
    }

    private void ensureFreeBytes(int i2) {
        if (this.bufsize - this.offset < i2) {
            int i3 = this.bufsize * 8;
            if (i3 - this.offset < i2) {
                i3 += i2;
            }
            byte[] bArr = new byte[i3];
            System.arraycopy(this.buf, 0, bArr, 0, this.offset);
            this.buf = bArr;
            this.bufsize = i3;
        }
    }
}
