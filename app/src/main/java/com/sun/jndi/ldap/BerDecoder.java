package com.sun.jndi.ldap;

import com.sun.jndi.ldap.Ber;
import java.io.UnsupportedEncodingException;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/jndi/ldap/BerDecoder.class */
public final class BerDecoder extends Ber {
    private int origOffset;

    public BerDecoder(byte[] bArr, int i2, int i3) {
        this.buf = bArr;
        this.bufsize = i3;
        this.origOffset = i2;
        reset();
    }

    public void reset() {
        this.offset = this.origOffset;
    }

    public int getParsePosition() {
        return this.offset;
    }

    public int parseLength() throws Ber.DecodeException {
        int i2 = parseByte();
        if ((i2 & 128) == 128) {
            int i3 = i2 & 127;
            if (i3 == 0) {
                throw new Ber.DecodeException("Indefinite length not supported");
            }
            if (i3 > 4) {
                throw new Ber.DecodeException("encoding too long");
            }
            if (this.bufsize - this.offset < i3) {
                throw new Ber.DecodeException("Insufficient data");
            }
            int i4 = 0;
            for (int i5 = 0; i5 < i3; i5++) {
                byte[] bArr = this.buf;
                int i6 = this.offset;
                this.offset = i6 + 1;
                i4 = (i4 << 8) + (bArr[i6] & 255);
            }
            if (i4 < 0) {
                throw new Ber.DecodeException("Invalid length bytes");
            }
            return i4;
        }
        return i2;
    }

    public int parseSeq(int[] iArr) throws Ber.DecodeException {
        int i2 = parseByte();
        int length = parseLength();
        if (iArr != null) {
            iArr[0] = length;
        }
        return i2;
    }

    void seek(int i2) throws Ber.DecodeException {
        if (this.offset + i2 > this.bufsize || this.offset + i2 < 0) {
            throw new Ber.DecodeException("array index out of bounds");
        }
        this.offset += i2;
    }

    public int parseByte() throws Ber.DecodeException {
        if (this.bufsize - this.offset < 1) {
            throw new Ber.DecodeException("Insufficient data");
        }
        byte[] bArr = this.buf;
        int i2 = this.offset;
        this.offset = i2 + 1;
        return bArr[i2] & 255;
    }

    public int peekByte() throws Ber.DecodeException {
        if (this.bufsize - this.offset < 1) {
            throw new Ber.DecodeException("Insufficient data");
        }
        return this.buf[this.offset] & 255;
    }

    public boolean parseBoolean() throws Ber.DecodeException {
        return parseIntWithTag(1) != 0;
    }

    public int parseEnumeration() throws Ber.DecodeException {
        return parseIntWithTag(10);
    }

    public int parseInt() throws Ber.DecodeException {
        return parseIntWithTag(2);
    }

    private int parseIntWithTag(int i2) throws Ber.DecodeException {
        String string;
        if (parseByte() != i2) {
            if (this.offset > 0) {
                string = Integer.toString(this.buf[this.offset - 1] & 255);
            } else {
                string = "Empty tag";
            }
            throw new Ber.DecodeException("Encountered ASN.1 tag " + string + " (expected tag " + Integer.toString(i2) + ")");
        }
        int length = parseLength();
        if (length > 4) {
            throw new Ber.DecodeException("INTEGER too long");
        }
        if (length > this.bufsize - this.offset) {
            throw new Ber.DecodeException("Insufficient data");
        }
        byte[] bArr = this.buf;
        int i3 = this.offset;
        this.offset = i3 + 1;
        byte b2 = bArr[i3];
        int i4 = b2 & Byte.MAX_VALUE;
        for (int i5 = 1; i5 < length; i5++) {
            byte[] bArr2 = this.buf;
            int i6 = this.offset;
            this.offset = i6 + 1;
            i4 = (i4 << 8) | (bArr2[i6] & 255);
        }
        if ((b2 & 128) == 128) {
            i4 = -i4;
        }
        return i4;
    }

    public String parseString(boolean z2) throws Ber.DecodeException {
        return parseStringWithTag(4, z2, null);
    }

    public String parseStringWithTag(int i2, boolean z2, int[] iArr) throws Ber.DecodeException {
        String str;
        int i3 = this.offset;
        int i4 = parseByte();
        if (i4 != i2) {
            throw new Ber.DecodeException("Encountered ASN.1 tag " + Integer.toString((byte) i4) + " (expected tag " + i2 + ")");
        }
        int length = parseLength();
        if (length > this.bufsize - this.offset) {
            throw new Ber.DecodeException("Insufficient data");
        }
        if (length == 0) {
            str = "";
        } else {
            byte[] bArr = new byte[length];
            System.arraycopy(this.buf, this.offset, bArr, 0, length);
            if (z2) {
                try {
                    str = new String(bArr, InternalZipConstants.CHARSET_UTF8);
                } catch (UnsupportedEncodingException e2) {
                    throw new Ber.DecodeException("UTF8 not available on platform");
                }
            } else {
                try {
                    str = new String(bArr, "8859_1");
                } catch (UnsupportedEncodingException e3) {
                    throw new Ber.DecodeException("8859_1 not available on platform");
                }
            }
            this.offset += length;
        }
        if (iArr != null) {
            iArr[0] = this.offset - i3;
        }
        return str;
    }

    public byte[] parseOctetString(int i2, int[] iArr) throws Ber.DecodeException {
        int i3 = this.offset;
        int i4 = parseByte();
        if (i4 != i2) {
            throw new Ber.DecodeException("Encountered ASN.1 tag " + Integer.toString(i4) + " (expected tag " + Integer.toString(i2) + ")");
        }
        int length = parseLength();
        if (length > this.bufsize - this.offset) {
            throw new Ber.DecodeException("Insufficient data");
        }
        byte[] bArr = new byte[length];
        if (length > 0) {
            System.arraycopy(this.buf, this.offset, bArr, 0, length);
            this.offset += length;
        }
        if (iArr != null) {
            iArr[0] = this.offset - i3;
        }
        return bArr;
    }

    public int bytesLeft() {
        return this.bufsize - this.offset;
    }
}
