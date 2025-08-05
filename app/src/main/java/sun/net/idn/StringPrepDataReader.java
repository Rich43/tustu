package sun.net.idn;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.text.normalizer.ICUBinary;

/* loaded from: rt.jar:sun/net/idn/StringPrepDataReader.class */
final class StringPrepDataReader implements ICUBinary.Authenticate {
    private DataInputStream dataInputStream;
    private byte[] unicodeVersion;
    private static final byte[] DATA_FORMAT_ID = {83, 80, 82, 80};
    private static final byte[] DATA_FORMAT_VERSION = {3, 2, 5, 2};

    public StringPrepDataReader(InputStream inputStream) throws IOException {
        this.unicodeVersion = ICUBinary.readHeader(inputStream, DATA_FORMAT_ID, this);
        this.dataInputStream = new DataInputStream(inputStream);
    }

    public void read(byte[] bArr, char[] cArr) throws IOException {
        this.dataInputStream.read(bArr);
        for (int i2 = 0; i2 < cArr.length; i2++) {
            cArr[i2] = this.dataInputStream.readChar();
        }
    }

    public byte[] getDataFormatVersion() {
        return DATA_FORMAT_VERSION;
    }

    @Override // sun.text.normalizer.ICUBinary.Authenticate
    public boolean isDataVersionAcceptable(byte[] bArr) {
        return bArr[0] == DATA_FORMAT_VERSION[0] && bArr[2] == DATA_FORMAT_VERSION[2] && bArr[3] == DATA_FORMAT_VERSION[3];
    }

    public int[] readIndexes(int i2) throws IOException {
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = this.dataInputStream.readInt();
        }
        return iArr;
    }

    public byte[] getUnicodeVersion() {
        return this.unicodeVersion;
    }
}
