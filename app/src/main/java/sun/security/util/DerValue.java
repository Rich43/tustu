package sun.security.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.util.PdfOps;
import sun.misc.IOUtils;

/* loaded from: rt.jar:sun/security/util/DerValue.class */
public class DerValue {
    public static final byte TAG_UNIVERSAL = 0;
    public static final byte TAG_APPLICATION = 64;
    public static final byte TAG_CONTEXT = Byte.MIN_VALUE;
    public static final byte TAG_PRIVATE = -64;
    public byte tag;
    protected DerInputBuffer buffer;
    public final DerInputStream data;
    private int length;
    public static final byte tag_Boolean = 1;
    public static final byte tag_Integer = 2;
    public static final byte tag_BitString = 3;
    public static final byte tag_OctetString = 4;
    public static final byte tag_Null = 5;
    public static final byte tag_ObjectId = 6;
    public static final byte tag_Enumerated = 10;
    public static final byte tag_UTF8String = 12;
    public static final byte tag_PrintableString = 19;
    public static final byte tag_T61String = 20;
    public static final byte tag_IA5String = 22;
    public static final byte tag_UtcTime = 23;
    public static final byte tag_GeneralizedTime = 24;
    public static final byte tag_GeneralString = 27;
    public static final byte tag_UniversalString = 28;
    public static final byte tag_BMPString = 30;
    public static final byte tag_Sequence = 48;
    public static final byte tag_SequenceOf = 48;
    public static final byte tag_Set = 49;
    public static final byte tag_SetOf = 49;

    public boolean isUniversal() {
        return (this.tag & 192) == 0;
    }

    public boolean isApplication() {
        return (this.tag & 192) == 64;
    }

    public boolean isContextSpecific() {
        return (this.tag & 192) == 128;
    }

    public boolean isContextSpecific(byte b2) {
        return isContextSpecific() && (this.tag & 31) == b2;
    }

    boolean isPrivate() {
        return (this.tag & 192) == 192;
    }

    public boolean isConstructed() {
        return (this.tag & 32) == 32;
    }

    public boolean isConstructed(byte b2) {
        return isConstructed() && (this.tag & 31) == b2;
    }

    public DerValue(String str) throws IOException {
        boolean z2 = true;
        int i2 = 0;
        while (true) {
            if (i2 >= str.length()) {
                break;
            }
            if (isPrintableStringChar(str.charAt(i2))) {
                i2++;
            } else {
                z2 = false;
                break;
            }
        }
        this.data = init(z2 ? (byte) 19 : (byte) 12, str);
    }

    public DerValue(byte b2, String str) throws IOException {
        this.data = init(b2, str);
    }

    DerValue(byte b2, byte[] bArr, boolean z2) {
        this.tag = b2;
        this.buffer = new DerInputBuffer((byte[]) bArr.clone(), z2);
        this.length = bArr.length;
        this.data = new DerInputStream(this.buffer);
        this.data.mark(Integer.MAX_VALUE);
    }

    public DerValue(byte b2, byte[] bArr) {
        this(b2, bArr, true);
    }

    DerValue(DerInputBuffer derInputBuffer) throws IOException {
        this.tag = (byte) derInputBuffer.read();
        byte b2 = (byte) derInputBuffer.read();
        this.length = DerInputStream.getLength(b2, derInputBuffer);
        if (this.length == -1) {
            DerInputBuffer derInputBuffer2 = new DerInputBuffer(DerIndefLenConverter.convertStream(derInputBuffer.dup(), b2, this.tag), derInputBuffer.allowBER);
            if (this.tag != derInputBuffer2.read()) {
                throw new IOException("Indefinite length encoding not supported");
            }
            this.length = DerInputStream.getDefiniteLength(derInputBuffer2);
            this.buffer = derInputBuffer2.dup();
            this.buffer.truncate(this.length);
            this.data = new DerInputStream(this.buffer);
            derInputBuffer.skip(this.length + 2);
            return;
        }
        this.buffer = derInputBuffer.dup();
        this.buffer.truncate(this.length);
        this.data = new DerInputStream(this.buffer);
        derInputBuffer.skip(this.length);
    }

    DerValue(byte[] bArr, boolean z2) throws IOException {
        this.data = init(true, new ByteArrayInputStream(bArr), z2);
    }

    public DerValue(byte[] bArr) throws IOException {
        this(bArr, true);
    }

    DerValue(byte[] bArr, int i2, int i3, boolean z2) throws IOException {
        this.data = init(true, new ByteArrayInputStream(bArr, i2, i3), z2);
    }

    public static DerValue wrap(byte[] bArr) throws IOException {
        return wrap(bArr, 0, bArr.length);
    }

    public static DerValue wrap(byte[] bArr, int i2, int i3) throws IOException {
        return new DerValue(bArr, i2, i3);
    }

    public DerValue(byte[] bArr, int i2, int i3) throws IOException {
        this(bArr, i2, i3, true);
    }

    DerValue(InputStream inputStream, boolean z2) throws IOException {
        this.data = init(false, inputStream, z2);
    }

    public DerValue(InputStream inputStream) throws IOException {
        this(inputStream, true);
    }

    private DerInputStream init(byte b2, String str) throws IOException {
        String str2;
        this.tag = b2;
        switch (b2) {
            case 12:
                str2 = InternalZipConstants.CHARSET_UTF8;
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 21:
            case 23:
            case 24:
            case 25:
            case 26:
            case 28:
            case 29:
            default:
                throw new IllegalArgumentException("Unsupported DER string type");
            case 19:
            case 22:
            case 27:
                str2 = "ASCII";
                break;
            case 20:
                str2 = FTP.DEFAULT_CONTROL_ENCODING;
                break;
            case 30:
                str2 = "UnicodeBigUnmarked";
                break;
        }
        byte[] bytes = str.getBytes(str2);
        this.length = bytes.length;
        this.buffer = new DerInputBuffer(bytes, true);
        DerInputStream derInputStream = new DerInputStream(this.buffer);
        derInputStream.mark(Integer.MAX_VALUE);
        return derInputStream;
    }

    private DerInputStream init(boolean z2, InputStream inputStream, boolean z3) throws IOException {
        this.tag = (byte) inputStream.read();
        byte b2 = (byte) inputStream.read();
        this.length = DerInputStream.getLength(b2, inputStream);
        if (this.length == -1) {
            inputStream = new ByteArrayInputStream(DerIndefLenConverter.convertStream(inputStream, b2, this.tag));
            if (this.tag != inputStream.read()) {
                throw new IOException("Indefinite length encoding not supported");
            }
            this.length = DerInputStream.getDefiniteLength(inputStream);
        }
        if (z2 && inputStream.available() != this.length) {
            throw new IOException("extra data given to DerValue constructor");
        }
        this.buffer = new DerInputBuffer(IOUtils.readExactlyNBytes(inputStream, this.length), z3);
        return new DerInputStream(this.buffer);
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.write(this.tag);
        derOutputStream.putLength(this.length);
        if (this.length > 0) {
            byte[] bArr = new byte[this.length];
            synchronized (this.data) {
                this.buffer.reset();
                if (this.buffer.read(bArr) != this.length) {
                    throw new IOException("short DER value read (encode)");
                }
                derOutputStream.write(bArr);
            }
        }
    }

    public final DerInputStream getData() {
        return this.data;
    }

    public final byte getTag() {
        return this.tag;
    }

    public boolean getBoolean() throws IOException {
        if (this.tag != 1) {
            throw new IOException("DerValue.getBoolean, not a BOOLEAN " + ((int) this.tag));
        }
        if (this.length != 1) {
            throw new IOException("DerValue.getBoolean, invalid length " + this.length);
        }
        if (this.buffer.read() != 0) {
            return true;
        }
        return false;
    }

    public ObjectIdentifier getOID() throws IOException {
        if (this.tag != 6) {
            throw new IOException("DerValue.getOID, not an OID " + ((int) this.tag));
        }
        return new ObjectIdentifier(this.buffer);
    }

    private byte[] append(byte[] bArr, byte[] bArr2) {
        if (bArr == null) {
            return bArr2;
        }
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public byte[] getOctetString() throws IOException {
        if (this.tag != 4 && !isConstructed((byte) 4)) {
            throw new IOException("DerValue.getOctetString, not an Octet String: " + ((int) this.tag));
        }
        if (this.length == 0) {
            return new byte[0];
        }
        DerInputBuffer derInputBuffer = this.buffer;
        if (derInputBuffer.available() < this.length) {
            throw new IOException("short read on DerValue buffer");
        }
        byte[] bArr = new byte[this.length];
        derInputBuffer.read(bArr);
        if (isConstructed()) {
            DerInputStream derInputStream = new DerInputStream(bArr, 0, bArr.length, this.buffer.allowBER);
            byte[] bArrAppend = null;
            while (true) {
                bArr = bArrAppend;
                if (derInputStream.available() == 0) {
                    break;
                }
                bArrAppend = append(bArr, derInputStream.getOctetString());
            }
        }
        return bArr;
    }

    public int getInteger() throws IOException {
        if (this.tag != 2) {
            throw new IOException("DerValue.getInteger, not an int " + ((int) this.tag));
        }
        return this.buffer.getInteger(this.data.available());
    }

    public BigInteger getBigInteger() throws IOException {
        if (this.tag != 2) {
            throw new IOException("DerValue.getBigInteger, not an int " + ((int) this.tag));
        }
        return this.buffer.getBigInteger(this.data.available(), false);
    }

    public BigInteger getPositiveBigInteger() throws IOException {
        if (this.tag != 2) {
            throw new IOException("DerValue.getBigInteger, not an int " + ((int) this.tag));
        }
        return this.buffer.getBigInteger(this.data.available(), true);
    }

    public int getEnumerated() throws IOException {
        if (this.tag != 10) {
            throw new IOException("DerValue.getEnumerated, incorrect tag: " + ((int) this.tag));
        }
        return this.buffer.getInteger(this.data.available());
    }

    public byte[] getBitString() throws IOException {
        if (this.tag != 3) {
            throw new IOException("DerValue.getBitString, not a bit string " + ((int) this.tag));
        }
        return this.buffer.getBitString();
    }

    public BitArray getUnalignedBitString() throws IOException {
        if (this.tag != 3) {
            throw new IOException("DerValue.getBitString, not a bit string " + ((int) this.tag));
        }
        return this.buffer.getUnalignedBitString();
    }

    public String getAsString() throws IOException {
        if (this.tag == 12) {
            return getUTF8String();
        }
        if (this.tag == 19) {
            return getPrintableString();
        }
        if (this.tag == 20) {
            return getT61String();
        }
        if (this.tag == 22) {
            return getIA5String();
        }
        if (this.tag == 30) {
            return getBMPString();
        }
        if (this.tag == 27) {
            return getGeneralString();
        }
        return null;
    }

    public byte[] getBitString(boolean z2) throws IOException {
        if (!z2 && this.tag != 3) {
            throw new IOException("DerValue.getBitString, not a bit string " + ((int) this.tag));
        }
        return this.buffer.getBitString();
    }

    public BitArray getUnalignedBitString(boolean z2) throws IOException {
        if (!z2 && this.tag != 3) {
            throw new IOException("DerValue.getBitString, not a bit string " + ((int) this.tag));
        }
        return this.buffer.getUnalignedBitString();
    }

    public byte[] getDataBytes() throws IOException {
        byte[] bArr = new byte[this.length];
        synchronized (this.data) {
            this.data.reset();
            this.data.getBytes(bArr);
        }
        return bArr;
    }

    public String getPrintableString() throws IOException {
        if (this.tag != 19) {
            throw new IOException("DerValue.getPrintableString, not a string " + ((int) this.tag));
        }
        return new String(getDataBytes(), "ASCII");
    }

    public String getT61String() throws IOException {
        if (this.tag != 20) {
            throw new IOException("DerValue.getT61String, not T61 " + ((int) this.tag));
        }
        return new String(getDataBytes(), FTP.DEFAULT_CONTROL_ENCODING);
    }

    public String getIA5String() throws IOException {
        if (this.tag != 22) {
            throw new IOException("DerValue.getIA5String, not IA5 " + ((int) this.tag));
        }
        return new String(getDataBytes(), "ASCII");
    }

    public String getBMPString() throws IOException {
        if (this.tag != 30) {
            throw new IOException("DerValue.getBMPString, not BMP " + ((int) this.tag));
        }
        return new String(getDataBytes(), "UnicodeBigUnmarked");
    }

    public String getUTF8String() throws IOException {
        if (this.tag != 12) {
            throw new IOException("DerValue.getUTF8String, not UTF-8 " + ((int) this.tag));
        }
        return new String(getDataBytes(), InternalZipConstants.CHARSET_UTF8);
    }

    public String getGeneralString() throws IOException {
        if (this.tag != 27) {
            throw new IOException("DerValue.getGeneralString, not GeneralString " + ((int) this.tag));
        }
        return new String(getDataBytes(), "ASCII");
    }

    public Date getUTCTime() throws IOException {
        if (this.tag != 23) {
            throw new IOException("DerValue.getUTCTime, not a UtcTime: " + ((int) this.tag));
        }
        return this.buffer.getUTCTime(this.data.available());
    }

    public Date getGeneralizedTime() throws IOException {
        if (this.tag != 24) {
            throw new IOException("DerValue.getGeneralizedTime, not a GeneralizedTime: " + ((int) this.tag));
        }
        return this.buffer.getGeneralizedTime(this.data.available());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DerValue)) {
            return false;
        }
        DerValue derValue = (DerValue) obj;
        if (this.tag != derValue.tag) {
            return false;
        }
        if (this.data == derValue.data) {
            return true;
        }
        if (System.identityHashCode(this.data) > System.identityHashCode(derValue.data)) {
            return doEquals(this, derValue);
        }
        return doEquals(derValue, this);
    }

    private static boolean doEquals(DerValue derValue, DerValue derValue2) {
        boolean zEquals;
        synchronized (derValue.data) {
            synchronized (derValue2.data) {
                derValue.data.reset();
                derValue2.data.reset();
                zEquals = derValue.buffer.equals(derValue2.buffer);
            }
        }
        return zEquals;
    }

    public String toString() {
        try {
            String asString = getAsString();
            if (asString != null) {
                return PdfOps.DOUBLE_QUOTE__TOKEN + asString + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            if (this.tag == 5) {
                return "[DerValue, null]";
            }
            if (this.tag == 6) {
                return "OID." + ((Object) getOID());
            }
            return "[DerValue, tag = " + ((int) this.tag) + ", length = " + this.length + "]";
        } catch (IOException e2) {
            throw new IllegalArgumentException("misformatted DER value");
        }
    }

    public byte[] toByteArray() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        encode(derOutputStream);
        this.data.reset();
        return derOutputStream.toByteArray();
    }

    public DerInputStream toDerInputStream() throws IOException {
        if (this.tag == 48 || this.tag == 49) {
            return new DerInputStream(this.buffer);
        }
        throw new IOException("toDerInputStream rejects tag type " + ((int) this.tag));
    }

    public int length() {
        return this.length;
    }

    public static boolean isPrintableStringChar(char c2) {
        if (c2 >= 'a' && c2 <= 'z') {
            return true;
        }
        if (c2 >= 'A' && c2 <= 'Z') {
            return true;
        }
        if (c2 < '0' || c2 > '9') {
            switch (c2) {
            }
            return true;
        }
        return true;
    }

    public static byte createTag(byte b2, boolean z2, byte b3) {
        byte b4 = (byte) (b2 | b3);
        if (z2) {
            b4 = (byte) (b4 | 32);
        }
        return b4;
    }

    public void resetTag(byte b2) {
        this.tag = b2;
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
