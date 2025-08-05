package sun.security.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.Vector;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.net.ftp.FTP;

/* loaded from: rt.jar:sun/security/util/DerInputStream.class */
public class DerInputStream {
    DerInputBuffer buffer;
    public byte tag;

    public DerInputStream(byte[] bArr) throws IOException {
        init(bArr, 0, bArr.length, true);
    }

    public DerInputStream(byte[] bArr, int i2, int i3, boolean z2) throws IOException {
        init(bArr, i2, i3, z2);
    }

    public DerInputStream(byte[] bArr, int i2, int i3) throws IOException {
        init(bArr, i2, i3, true);
    }

    private void init(byte[] bArr, int i2, int i3, boolean z2) throws IOException {
        if (i2 + 2 > bArr.length || i2 + i3 > bArr.length) {
            throw new IOException("Encoding bytes too short");
        }
        if (DerIndefLenConverter.isIndefinite(bArr[i2 + 1])) {
            if (!z2) {
                throw new IOException("Indefinite length BER encoding found");
            }
            byte[] bArr2 = new byte[i3];
            System.arraycopy(bArr, i2, bArr2, 0, i3);
            byte[] bArrConvertBytes = new DerIndefLenConverter().convertBytes(bArr2);
            if (bArrConvertBytes == null) {
                throw new IOException("not all indef len BER resolved");
            }
            this.buffer = new DerInputBuffer(bArrConvertBytes, z2);
        } else {
            this.buffer = new DerInputBuffer(bArr, i2, i3, z2);
        }
        this.buffer.mark(Integer.MAX_VALUE);
    }

    DerInputStream(DerInputBuffer derInputBuffer) {
        this.buffer = derInputBuffer;
        this.buffer.mark(Integer.MAX_VALUE);
    }

    public DerInputStream subStream(int i2, boolean z2) throws IOException {
        DerInputBuffer derInputBufferDup = this.buffer.dup();
        derInputBufferDup.truncate(i2);
        if (z2) {
            this.buffer.skip(i2);
        }
        return new DerInputStream(derInputBufferDup);
    }

    public byte[] toByteArray() {
        return this.buffer.toByteArray();
    }

    public int getInteger() throws IOException {
        if (this.buffer.read() != 2) {
            throw new IOException("DER input, Integer tag error");
        }
        return this.buffer.getInteger(getDefiniteLength(this.buffer));
    }

    public BigInteger getBigInteger() throws IOException {
        if (this.buffer.read() != 2) {
            throw new IOException("DER input, Integer tag error");
        }
        return this.buffer.getBigInteger(getDefiniteLength(this.buffer), false);
    }

    public BigInteger getPositiveBigInteger() throws IOException {
        if (this.buffer.read() != 2) {
            throw new IOException("DER input, Integer tag error");
        }
        return this.buffer.getBigInteger(getDefiniteLength(this.buffer), true);
    }

    public int getEnumerated() throws IOException {
        if (this.buffer.read() != 10) {
            throw new IOException("DER input, Enumerated tag error");
        }
        return this.buffer.getInteger(getDefiniteLength(this.buffer));
    }

    public byte[] getBitString() throws IOException {
        if (this.buffer.read() != 3) {
            throw new IOException("DER input not an bit string");
        }
        return this.buffer.getBitString(getDefiniteLength(this.buffer));
    }

    public BitArray getUnalignedBitString() throws IOException {
        if (this.buffer.read() != 3) {
            throw new IOException("DER input not a bit string");
        }
        int definiteLength = getDefiniteLength(this.buffer);
        if (definiteLength == 0) {
            return new BitArray(0);
        }
        return this.buffer.getUnalignedBitString(definiteLength);
    }

    public byte[] getOctetString() throws IOException {
        if (this.buffer.read() != 4) {
            throw new IOException("DER input not an octet string");
        }
        int definiteLength = getDefiniteLength(this.buffer);
        if (definiteLength > this.buffer.available()) {
            throw new IOException("short read of an octet string");
        }
        byte[] bArr = new byte[definiteLength];
        if (definiteLength != 0 && this.buffer.read(bArr) != definiteLength) {
            throw new IOException("Short read of DER octet string");
        }
        return bArr;
    }

    public void getBytes(byte[] bArr) throws IOException {
        if (bArr.length != 0 && this.buffer.read(bArr) != bArr.length) {
            throw new IOException("Short read of DER octet string");
        }
    }

    public void getNull() throws IOException {
        if (this.buffer.read() != 5 || this.buffer.read() != 0) {
            throw new IOException("getNull, bad data");
        }
    }

    public ObjectIdentifier getOID() throws IOException {
        return new ObjectIdentifier(this);
    }

    public DerValue[] getSequence(int i2) throws IOException {
        this.tag = (byte) this.buffer.read();
        if (this.tag != 48) {
            throw new IOException("Sequence tag error");
        }
        return readVector(i2);
    }

    public DerValue[] getSet(int i2) throws IOException {
        this.tag = (byte) this.buffer.read();
        if (this.tag != 49) {
            throw new IOException("Set tag error");
        }
        return readVector(i2);
    }

    public DerValue[] getSet(int i2, boolean z2) throws IOException {
        this.tag = (byte) this.buffer.read();
        if (!z2 && this.tag != 49) {
            throw new IOException("Set tag error");
        }
        return readVector(i2);
    }

    protected DerValue[] readVector(int i2) throws IOException {
        DerInputStream derInputStreamSubStream;
        byte b2 = (byte) this.buffer.read();
        int length = getLength(b2, this.buffer);
        if (length == -1) {
            this.buffer = new DerInputBuffer(DerIndefLenConverter.convertStream(this.buffer, b2, this.tag), this.buffer.allowBER);
            if (this.tag != this.buffer.read()) {
                throw new IOException("Indefinite length encoding not supported");
            }
            length = getDefiniteLength(this.buffer);
        }
        if (length == 0) {
            return new DerValue[0];
        }
        if (this.buffer.available() == length) {
            derInputStreamSubStream = this;
        } else {
            derInputStreamSubStream = subStream(length, true);
        }
        Vector vector = new Vector(i2);
        do {
            vector.addElement(new DerValue(derInputStreamSubStream.buffer, this.buffer.allowBER));
        } while (derInputStreamSubStream.available() > 0);
        if (derInputStreamSubStream.available() != 0) {
            throw new IOException("Extra data at end of vector");
        }
        int size = vector.size();
        DerValue[] derValueArr = new DerValue[size];
        for (int i3 = 0; i3 < size; i3++) {
            derValueArr[i3] = (DerValue) vector.elementAt(i3);
        }
        return derValueArr;
    }

    public DerValue getDerValue() throws IOException {
        return new DerValue(this.buffer);
    }

    public String getUTF8String() throws IOException {
        return readString((byte) 12, "UTF-8", InternalZipConstants.CHARSET_UTF8);
    }

    public String getPrintableString() throws IOException {
        return readString((byte) 19, "Printable", "ASCII");
    }

    public String getT61String() throws IOException {
        return readString((byte) 20, "T61", FTP.DEFAULT_CONTROL_ENCODING);
    }

    public String getIA5String() throws IOException {
        return readString((byte) 22, "IA5", "ASCII");
    }

    public String getBMPString() throws IOException {
        return readString((byte) 30, "BMP", "UnicodeBigUnmarked");
    }

    public String getGeneralString() throws IOException {
        return readString((byte) 27, "General", "ASCII");
    }

    private String readString(byte b2, String str, String str2) throws IOException {
        if (this.buffer.read() != b2) {
            throw new IOException("DER input not a " + str + " string");
        }
        int definiteLength = getDefiniteLength(this.buffer);
        if (definiteLength > this.buffer.available()) {
            throw new IOException("short read of " + str + " string");
        }
        byte[] bArr = new byte[definiteLength];
        if (definiteLength != 0 && this.buffer.read(bArr) != definiteLength) {
            throw new IOException("Short read of DER " + str + " string");
        }
        return new String(bArr, str2);
    }

    public Date getUTCTime() throws IOException {
        if (this.buffer.read() != 23) {
            throw new IOException("DER input, UTCtime tag invalid ");
        }
        return this.buffer.getUTCTime(getDefiniteLength(this.buffer));
    }

    public Date getGeneralizedTime() throws IOException {
        if (this.buffer.read() != 24) {
            throw new IOException("DER input, GeneralizedTime tag invalid ");
        }
        return this.buffer.getGeneralizedTime(getDefiniteLength(this.buffer));
    }

    int getByte() throws IOException {
        return 255 & this.buffer.read();
    }

    public int peekByte() throws IOException {
        return this.buffer.peek();
    }

    int getLength() throws IOException {
        return getLength(this.buffer);
    }

    static int getLength(InputStream inputStream) throws IOException {
        return getLength(inputStream.read(), inputStream);
    }

    static int getLength(int i2, InputStream inputStream) throws IOException {
        int i3;
        if (i2 == -1) {
            throw new IOException("Short read of DER length");
        }
        if ((i2 & 128) == 0) {
            i3 = i2;
        } else {
            int i4 = i2 & 127;
            if (i4 == 0) {
                return -1;
            }
            if (i4 < 0 || i4 > 4) {
                throw new IOException("DerInputStream.getLength(): lengthTag=" + i4 + ", " + (i4 < 0 ? "incorrect DER encoding." : "too big."));
            }
            i3 = 255 & inputStream.read();
            int i5 = i4 - 1;
            if (i3 == 0) {
                throw new IOException("DerInputStream.getLength(): Redundant length bytes found");
            }
            while (true) {
                int i6 = i5;
                i5--;
                if (i6 <= 0) {
                    break;
                }
                i3 = (i3 << 8) + (255 & inputStream.read());
            }
            if (i3 < 0) {
                throw new IOException("DerInputStream.getLength(): Invalid length bytes");
            }
            if (i3 <= 127) {
                throw new IOException("DerInputStream.getLength(): Should use short form for length");
            }
        }
        return i3;
    }

    int getDefiniteLength() throws IOException {
        return getDefiniteLength(this.buffer);
    }

    static int getDefiniteLength(InputStream inputStream) throws IOException {
        int length = getLength(inputStream);
        if (length < 0) {
            throw new IOException("Indefinite length encoding not supported");
        }
        return length;
    }

    public void mark(int i2) {
        this.buffer.mark(i2);
    }

    public void reset() {
        this.buffer.reset();
    }

    public int available() {
        return this.buffer.available();
    }
}
