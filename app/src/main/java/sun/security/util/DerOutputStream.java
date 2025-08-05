package sun.security.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.net.ftp.FTP;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/security/util/DerOutputStream.class */
public class DerOutputStream extends ByteArrayOutputStream implements DerEncoder {
    private static ByteArrayLexOrder lexOrder = new ByteArrayLexOrder();
    private static ByteArrayTagOrder tagOrder = new ByteArrayTagOrder();

    public DerOutputStream(int i2) {
        super(i2);
    }

    public DerOutputStream() {
    }

    public void write(byte b2, byte[] bArr) throws IOException {
        write(b2);
        putLength(bArr.length);
        write(bArr, 0, bArr.length);
    }

    public void write(byte b2, DerOutputStream derOutputStream) throws IOException {
        write(b2);
        putLength(derOutputStream.count);
        write(derOutputStream.buf, 0, derOutputStream.count);
    }

    public void writeImplicit(byte b2, DerOutputStream derOutputStream) throws IOException {
        write(b2);
        write(derOutputStream.buf, 1, derOutputStream.count - 1);
    }

    public void putDerValue(DerValue derValue) throws IOException {
        derValue.encode(this);
    }

    public void putBoolean(boolean z2) throws IOException {
        write(1);
        putLength(1);
        if (z2) {
            write(255);
        } else {
            write(0);
        }
    }

    public void putEnumerated(int i2) throws IOException {
        write(10);
        putIntegerContents(i2);
    }

    public void putInteger(BigInteger bigInteger) throws IOException {
        write(2);
        byte[] byteArray = bigInteger.toByteArray();
        putLength(byteArray.length);
        write(byteArray, 0, byteArray.length);
    }

    public void putInteger(Integer num) throws IOException {
        putInteger(num.intValue());
    }

    public void putInteger(int i2) throws IOException {
        write(2);
        putIntegerContents(i2);
    }

    private void putIntegerContents(int i2) throws IOException {
        int i3 = 0;
        byte[] bArr = {(byte) ((i2 & (-16777216)) >>> 24), (byte) ((i2 & 16711680) >>> 16), (byte) ((i2 & NormalizerImpl.CC_MASK) >>> 8), (byte) (i2 & 255)};
        if (bArr[0] == -1) {
            for (int i4 = 0; i4 < 3 && bArr[i4] == -1 && (bArr[i4 + 1] & 128) == 128; i4++) {
                i3++;
            }
        } else if (bArr[0] == 0) {
            for (int i5 = 0; i5 < 3 && bArr[i5] == 0 && (bArr[i5 + 1] & 128) == 0; i5++) {
                i3++;
            }
        }
        putLength(4 - i3);
        for (int i6 = i3; i6 < 4; i6++) {
            write(bArr[i6]);
        }
    }

    public void putBitString(byte[] bArr) throws IOException {
        write(3);
        putLength(bArr.length + 1);
        write(0);
        write(bArr);
    }

    public void putUnalignedBitString(BitArray bitArray) throws IOException {
        byte[] byteArray = bitArray.toByteArray();
        write(3);
        putLength(byteArray.length + 1);
        write((byteArray.length * 8) - bitArray.length());
        write(byteArray);
    }

    public void putTruncatedUnalignedBitString(BitArray bitArray) throws IOException {
        putUnalignedBitString(bitArray.truncate());
    }

    public void putOctetString(byte[] bArr) throws IOException {
        write((byte) 4, bArr);
    }

    public void putNull() throws IOException {
        write(5);
        putLength(0);
    }

    public void putOID(ObjectIdentifier objectIdentifier) throws IOException {
        objectIdentifier.encode(this);
    }

    public void putSequence(DerValue[] derValueArr) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        for (DerValue derValue : derValueArr) {
            derValue.encode(derOutputStream);
        }
        write((byte) 48, derOutputStream);
    }

    public void putSet(DerValue[] derValueArr) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        for (DerValue derValue : derValueArr) {
            derValue.encode(derOutputStream);
        }
        write((byte) 49, derOutputStream);
    }

    public void putOrderedSetOf(byte b2, DerEncoder[] derEncoderArr) throws IOException {
        putOrderedSet(b2, derEncoderArr, lexOrder);
    }

    public void putOrderedSet(byte b2, DerEncoder[] derEncoderArr) throws IOException {
        putOrderedSet(b2, derEncoderArr, tagOrder);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7, types: [byte[], java.lang.Object[]] */
    private void putOrderedSet(byte b2, DerEncoder[] derEncoderArr, Comparator<byte[]> comparator) throws IOException {
        DerOutputStream[] derOutputStreamArr = new DerOutputStream[derEncoderArr.length];
        for (int i2 = 0; i2 < derEncoderArr.length; i2++) {
            derOutputStreamArr[i2] = new DerOutputStream();
            derEncoderArr[i2].derEncode(derOutputStreamArr[i2]);
        }
        ?? r0 = new byte[derOutputStreamArr.length];
        for (int i3 = 0; i3 < derOutputStreamArr.length; i3++) {
            r0[i3] = derOutputStreamArr[i3].toByteArray();
        }
        Arrays.sort(r0, comparator);
        DerOutputStream derOutputStream = new DerOutputStream();
        for (int i4 = 0; i4 < derOutputStreamArr.length; i4++) {
            derOutputStream.write(r0[i4]);
        }
        write(b2, derOutputStream);
    }

    public void putUTF8String(String str) throws IOException {
        writeString(str, (byte) 12, InternalZipConstants.CHARSET_UTF8);
    }

    public void putPrintableString(String str) throws IOException {
        writeString(str, (byte) 19, "ASCII");
    }

    public void putT61String(String str) throws IOException {
        writeString(str, (byte) 20, FTP.DEFAULT_CONTROL_ENCODING);
    }

    public void putIA5String(String str) throws IOException {
        writeString(str, (byte) 22, "ASCII");
    }

    public void putBMPString(String str) throws IOException {
        writeString(str, (byte) 30, "UnicodeBigUnmarked");
    }

    public void putGeneralString(String str) throws IOException {
        writeString(str, (byte) 27, "ASCII");
    }

    private void writeString(String str, byte b2, String str2) throws IOException {
        byte[] bytes = str.getBytes(str2);
        write(b2);
        putLength(bytes.length);
        write(bytes);
    }

    public void putUTCTime(Date date) throws IOException {
        putTime(date, (byte) 23);
    }

    public void putGeneralizedTime(Date date) throws IOException {
        putTime(date, (byte) 24);
    }

    private void putTime(Date date, byte b2) throws IOException {
        String str;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        if (b2 == 23) {
            str = "yyMMddHHmmss'Z'";
        } else {
            b2 = 24;
            str = "yyyyMMddHHmmss'Z'";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        byte[] bytes = simpleDateFormat.format(date).getBytes(FTP.DEFAULT_CONTROL_ENCODING);
        write(b2);
        putLength(bytes.length);
        write(bytes);
    }

    public void putLength(int i2) throws IOException {
        if (i2 < 128) {
            write((byte) i2);
            return;
        }
        if (i2 < 256) {
            write(-127);
            write((byte) i2);
            return;
        }
        if (i2 < 65536) {
            write(-126);
            write((byte) (i2 >> 8));
            write((byte) i2);
        } else {
            if (i2 < 16777216) {
                write(-125);
                write((byte) (i2 >> 16));
                write((byte) (i2 >> 8));
                write((byte) i2);
                return;
            }
            write(-124);
            write((byte) (i2 >> 24));
            write((byte) (i2 >> 16));
            write((byte) (i2 >> 8));
            write((byte) i2);
        }
    }

    public void putTag(byte b2, boolean z2, byte b3) {
        byte b4 = (byte) (b2 | b3);
        if (z2) {
            b4 = (byte) (b4 | 32);
        }
        write(b4);
    }

    @Override // sun.security.util.DerEncoder
    public void derEncode(OutputStream outputStream) throws IOException {
        outputStream.write(toByteArray());
    }
}
