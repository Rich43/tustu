package sun.text.normalizer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.text.normalizer.ICUBinary;

/* loaded from: rt.jar:sun/text/normalizer/NormalizerDataReader.class */
final class NormalizerDataReader implements ICUBinary.Authenticate {
    private DataInputStream dataInputStream;
    private byte[] unicodeVersion;
    private static final byte[] DATA_FORMAT_ID = {78, 111, 114, 109};
    private static final byte[] DATA_FORMAT_VERSION = {2, 2, 5, 2};

    protected NormalizerDataReader(InputStream inputStream) throws IOException {
        this.unicodeVersion = ICUBinary.readHeader(inputStream, DATA_FORMAT_ID, this);
        this.dataInputStream = new DataInputStream(inputStream);
    }

    protected int[] readIndexes(int i2) throws IOException {
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = this.dataInputStream.readInt();
        }
        return iArr;
    }

    protected void read(byte[] bArr, byte[] bArr2, byte[] bArr3, char[] cArr, char[] cArr2) throws IOException {
        this.dataInputStream.readFully(bArr);
        for (int i2 = 0; i2 < cArr.length; i2++) {
            cArr[i2] = this.dataInputStream.readChar();
        }
        for (int i3 = 0; i3 < cArr2.length; i3++) {
            cArr2[i3] = this.dataInputStream.readChar();
        }
        this.dataInputStream.readFully(bArr2);
        this.dataInputStream.readFully(bArr3);
    }

    public byte[] getDataFormatVersion() {
        return DATA_FORMAT_VERSION;
    }

    @Override // sun.text.normalizer.ICUBinary.Authenticate
    public boolean isDataVersionAcceptable(byte[] bArr) {
        return bArr[0] == DATA_FORMAT_VERSION[0] && bArr[2] == DATA_FORMAT_VERSION[2] && bArr[3] == DATA_FORMAT_VERSION[3];
    }

    public byte[] getUnicodeVersion() {
        return this.unicodeVersion;
    }
}
