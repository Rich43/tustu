package sun.text.normalizer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.text.normalizer.ICUBinary;

/* loaded from: rt.jar:sun/text/normalizer/UBiDiProps.class */
public final class UBiDiProps {
    private int[] indexes;
    private int[] mirrors;
    private byte[] jgArray;
    private CharTrie trie;
    private static final String DATA_FILE_NAME = "/sun/text/resources/ubidi.icu";
    private static final int IX_INDEX_TOP = 0;
    private static final int IX_MIRROR_LENGTH = 3;
    private static final int IX_JG_START = 4;
    private static final int IX_JG_LIMIT = 5;
    private static final int IX_TOP = 16;
    private static final int CLASS_MASK = 31;
    private static UBiDiProps gBdp = null;
    private static UBiDiProps gBdpDummy = null;
    private static final byte[] FMT = {66, 105, 68, 105};

    public UBiDiProps() throws IOException {
        InputStream stream = ICUData.getStream(DATA_FILE_NAME);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(stream, 4096);
        readData(bufferedInputStream);
        bufferedInputStream.close();
        stream.close();
    }

    private void readData(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        ICUBinary.readHeader(dataInputStream, FMT, new IsAcceptable());
        int i2 = dataInputStream.readInt();
        if (i2 < 0) {
            throw new IOException("indexes[0] too small in /sun/text/resources/ubidi.icu");
        }
        this.indexes = new int[i2];
        this.indexes[0] = i2;
        for (int i3 = 1; i3 < i2; i3++) {
            this.indexes[i3] = dataInputStream.readInt();
        }
        this.trie = new CharTrie(dataInputStream, null);
        int i4 = this.indexes[3];
        if (i4 > 0) {
            this.mirrors = new int[i4];
            for (int i5 = 0; i5 < i4; i5++) {
                this.mirrors[i5] = dataInputStream.readInt();
            }
        }
        int i6 = this.indexes[5] - this.indexes[4];
        this.jgArray = new byte[i6];
        for (int i7 = 0; i7 < i6; i7++) {
            this.jgArray[i7] = dataInputStream.readByte();
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/UBiDiProps$IsAcceptable.class */
    private final class IsAcceptable implements ICUBinary.Authenticate {
        private IsAcceptable() {
        }

        @Override // sun.text.normalizer.ICUBinary.Authenticate
        public boolean isDataVersionAcceptable(byte[] bArr) {
            return bArr[0] == 1 && bArr[2] == 5 && bArr[3] == 2;
        }
    }

    public static final synchronized UBiDiProps getSingleton() throws IOException {
        if (gBdp == null) {
            gBdp = new UBiDiProps();
        }
        return gBdp;
    }

    private UBiDiProps(boolean z2) {
        this.indexes = new int[16];
        this.indexes[0] = 16;
        this.trie = new CharTrie(0, 0, null);
    }

    public static final synchronized UBiDiProps getDummy() {
        if (gBdpDummy == null) {
            gBdpDummy = new UBiDiProps(true);
        }
        return gBdpDummy;
    }

    public final int getClass(int i2) {
        return getClassFromProps(this.trie.getCodePointValue(i2));
    }

    private static final int getClassFromProps(int i2) {
        return i2 & 31;
    }
}
