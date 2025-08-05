package sun.nio.cs;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Comparator;

/* loaded from: rt.jar:sun/nio/cs/CharsetMapping.class */
public class CharsetMapping {
    public static final char UNMAPPABLE_DECODING = 65533;
    public static final int UNMAPPABLE_ENCODING = 65533;
    char[] b2cSB;
    char[] b2cDB1;
    char[] b2cDB2;
    int b2Min;
    int b2Max;
    int b1MinDB1;
    int b1MaxDB1;
    int b1MinDB2;
    int b1MaxDB2;
    int dbSegSize;
    char[] c2b;
    char[] c2bIndex;
    char[] b2cSupp;
    char[] c2bSupp;
    Entry[] b2cComp;
    Entry[] c2bComp;
    static Comparator<Entry> comparatorBytes = new Comparator<Entry>() { // from class: sun.nio.cs.CharsetMapping.2
        @Override // java.util.Comparator
        public int compare(Entry entry, Entry entry2) {
            return entry.f13589bs - entry2.f13589bs;
        }

        @Override // java.util.Comparator
        public boolean equals(Object obj) {
            return this == obj;
        }
    };
    static Comparator<Entry> comparatorCP = new Comparator<Entry>() { // from class: sun.nio.cs.CharsetMapping.3
        @Override // java.util.Comparator
        public int compare(Entry entry, Entry entry2) {
            return entry.cp - entry2.cp;
        }

        @Override // java.util.Comparator
        public boolean equals(Object obj) {
            return this == obj;
        }
    };
    static Comparator<Entry> comparatorComp = new Comparator<Entry>() { // from class: sun.nio.cs.CharsetMapping.4
        @Override // java.util.Comparator
        public int compare(Entry entry, Entry entry2) {
            int i2 = entry.cp - entry2.cp;
            if (i2 == 0) {
                i2 = entry.cp2 - entry2.cp2;
            }
            return i2;
        }

        @Override // java.util.Comparator
        public boolean equals(Object obj) {
            return this == obj;
        }
    };
    private static final int MAP_SINGLEBYTE = 1;
    private static final int MAP_DOUBLEBYTE1 = 2;
    private static final int MAP_DOUBLEBYTE2 = 3;
    private static final int MAP_SUPPLEMENT = 5;
    private static final int MAP_SUPPLEMENT_C2B = 6;
    private static final int MAP_COMPOSITE = 7;
    private static final int MAP_INDEXC2B = 8;
    int off = 0;

    /* renamed from: bb, reason: collision with root package name */
    byte[] f13588bb;

    /* loaded from: rt.jar:sun/nio/cs/CharsetMapping$Entry.class */
    public static class Entry {

        /* renamed from: bs, reason: collision with root package name */
        public int f13589bs;
        public int cp;
        public int cp2;
    }

    public char decodeSingle(int i2) {
        return this.b2cSB[i2];
    }

    public char decodeDouble(int i2, int i3) {
        if (i3 >= this.b2Min && i3 < this.b2Max) {
            int i4 = i3 - this.b2Min;
            if (i2 >= this.b1MinDB1 && i2 <= this.b1MaxDB1) {
                return this.b2cDB1[((i2 - this.b1MinDB1) * this.dbSegSize) + i4];
            }
            if (i2 >= this.b1MinDB2 && i2 <= this.b1MaxDB2) {
                return this.b2cDB2[((i2 - this.b1MinDB2) * this.dbSegSize) + i4];
            }
            return (char) 65533;
        }
        return (char) 65533;
    }

    public char[] decodeSurrogate(int i2, char[] cArr) {
        int length = this.b2cSupp.length / 2;
        int iBinarySearch = Arrays.binarySearch(this.b2cSupp, 0, length, (char) i2);
        if (iBinarySearch >= 0) {
            Character.toChars(this.b2cSupp[length + iBinarySearch] + 0, cArr, 0);
            return cArr;
        }
        return null;
    }

    public char[] decodeComposite(Entry entry, char[] cArr) {
        int iFindBytes = findBytes(this.b2cComp, entry);
        if (iFindBytes >= 0) {
            cArr[0] = (char) this.b2cComp[iFindBytes].cp;
            cArr[1] = (char) this.b2cComp[iFindBytes].cp2;
            return cArr;
        }
        return null;
    }

    public int encodeChar(char c2) {
        char c3 = this.c2bIndex[c2 >> '\b'];
        if (c3 == 65535) {
            return 65533;
        }
        return this.c2b[c3 + (c2 & 255)];
    }

    public int encodeSurrogate(char c2, char c3) {
        int length;
        int iBinarySearch;
        int codePoint = Character.toCodePoint(c2, c3);
        if (codePoint >= 131072 && codePoint < 196608 && (iBinarySearch = Arrays.binarySearch(this.c2bSupp, 0, (length = this.c2bSupp.length / 2), (char) codePoint)) >= 0) {
            return this.c2bSupp[length + iBinarySearch];
        }
        return 65533;
    }

    public boolean isCompositeBase(Entry entry) {
        return entry.cp <= 12791 && entry.cp >= 230 && findCP(this.c2bComp, entry) >= 0;
    }

    public int encodeComposite(Entry entry) {
        int iFindComp = findComp(this.c2bComp, entry);
        if (iFindComp >= 0) {
            return this.c2bComp[iFindComp].f13589bs;
        }
        return 65533;
    }

    public static CharsetMapping get(final InputStream inputStream) {
        return (CharsetMapping) AccessController.doPrivileged(new PrivilegedAction<CharsetMapping>() { // from class: sun.nio.cs.CharsetMapping.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public CharsetMapping run2() {
                return new CharsetMapping().load(inputStream);
            }
        });
    }

    static int findBytes(Entry[] entryArr, Entry entry) {
        return Arrays.binarySearch(entryArr, 0, entryArr.length, entry, comparatorBytes);
    }

    static int findCP(Entry[] entryArr, Entry entry) {
        return Arrays.binarySearch(entryArr, 0, entryArr.length, entry, comparatorCP);
    }

    static int findComp(Entry[] entryArr, Entry entry) {
        return Arrays.binarySearch(entryArr, 0, entryArr.length, entry, comparatorComp);
    }

    private static final boolean readNBytes(InputStream inputStream, byte[] bArr, int i2) throws IOException {
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i2 > 0) {
                int i5 = inputStream.read(bArr, i4, i2);
                if (i5 == -1) {
                    return false;
                }
                i2 -= i5;
                i3 = i4 + i5;
            } else {
                return true;
            }
        }
    }

    private char[] readCharArray() {
        byte[] bArr = this.f13588bb;
        int i2 = this.off;
        this.off = i2 + 1;
        int i3 = (bArr[i2] & 255) << 8;
        byte[] bArr2 = this.f13588bb;
        int i4 = this.off;
        this.off = i4 + 1;
        int i5 = i3 | (bArr2[i4] & 255);
        char[] cArr = new char[i5];
        for (int i6 = 0; i6 < i5; i6++) {
            byte[] bArr3 = this.f13588bb;
            int i7 = this.off;
            this.off = i7 + 1;
            int i8 = (bArr3[i7] & 255) << 8;
            byte[] bArr4 = this.f13588bb;
            int i9 = this.off;
            this.off = i9 + 1;
            cArr[i6] = (char) (i8 | (bArr4[i9] & 255));
        }
        return cArr;
    }

    void readSINGLEBYTE() {
        char[] charArray = readCharArray();
        for (int i2 = 0; i2 < charArray.length; i2++) {
            char c2 = charArray[i2];
            if (c2 != 65533) {
                this.c2b[this.c2bIndex[c2 >> '\b'] + (c2 & 255)] = (char) i2;
            }
        }
        this.b2cSB = charArray;
    }

    void readINDEXC2B() {
        char[] charArray = readCharArray();
        int length = charArray.length - 1;
        while (true) {
            if (length >= 0) {
                if (this.c2b != null || charArray[length] == 65535) {
                    length--;
                } else {
                    this.c2b = new char[charArray[length] + 256];
                    Arrays.fill(this.c2b, (char) 65533);
                    break;
                }
            } else {
                break;
            }
        }
        this.c2bIndex = charArray;
    }

    char[] readDB(int i2, int i3, int i4) {
        char[] charArray = readCharArray();
        for (int i5 = 0; i5 < charArray.length; i5++) {
            char c2 = charArray[i5];
            if (c2 != 65533) {
                this.c2b[this.c2bIndex[c2 >> '\b'] + (c2 & 255)] = (char) ((((i5 / i4) + i2) * 256) + (i5 % i4) + i3);
            }
        }
        return charArray;
    }

    void readDOUBLEBYTE1() {
        byte[] bArr = this.f13588bb;
        int i2 = this.off;
        this.off = i2 + 1;
        int i3 = (bArr[i2] & 255) << 8;
        byte[] bArr2 = this.f13588bb;
        int i4 = this.off;
        this.off = i4 + 1;
        this.b1MinDB1 = i3 | (bArr2[i4] & 255);
        byte[] bArr3 = this.f13588bb;
        int i5 = this.off;
        this.off = i5 + 1;
        int i6 = (bArr3[i5] & 255) << 8;
        byte[] bArr4 = this.f13588bb;
        int i7 = this.off;
        this.off = i7 + 1;
        this.b1MaxDB1 = i6 | (bArr4[i7] & 255);
        byte[] bArr5 = this.f13588bb;
        int i8 = this.off;
        this.off = i8 + 1;
        int i9 = (bArr5[i8] & 255) << 8;
        byte[] bArr6 = this.f13588bb;
        int i10 = this.off;
        this.off = i10 + 1;
        this.b2Min = i9 | (bArr6[i10] & 255);
        byte[] bArr7 = this.f13588bb;
        int i11 = this.off;
        this.off = i11 + 1;
        int i12 = (bArr7[i11] & 255) << 8;
        byte[] bArr8 = this.f13588bb;
        int i13 = this.off;
        this.off = i13 + 1;
        this.b2Max = i12 | (bArr8[i13] & 255);
        this.dbSegSize = (this.b2Max - this.b2Min) + 1;
        this.b2cDB1 = readDB(this.b1MinDB1, this.b2Min, this.dbSegSize);
    }

    void readDOUBLEBYTE2() {
        byte[] bArr = this.f13588bb;
        int i2 = this.off;
        this.off = i2 + 1;
        int i3 = (bArr[i2] & 255) << 8;
        byte[] bArr2 = this.f13588bb;
        int i4 = this.off;
        this.off = i4 + 1;
        this.b1MinDB2 = i3 | (bArr2[i4] & 255);
        byte[] bArr3 = this.f13588bb;
        int i5 = this.off;
        this.off = i5 + 1;
        int i6 = (bArr3[i5] & 255) << 8;
        byte[] bArr4 = this.f13588bb;
        int i7 = this.off;
        this.off = i7 + 1;
        this.b1MaxDB2 = i6 | (bArr4[i7] & 255);
        byte[] bArr5 = this.f13588bb;
        int i8 = this.off;
        this.off = i8 + 1;
        int i9 = (bArr5[i8] & 255) << 8;
        byte[] bArr6 = this.f13588bb;
        int i10 = this.off;
        this.off = i10 + 1;
        this.b2Min = i9 | (bArr6[i10] & 255);
        byte[] bArr7 = this.f13588bb;
        int i11 = this.off;
        this.off = i11 + 1;
        int i12 = (bArr7[i11] & 255) << 8;
        byte[] bArr8 = this.f13588bb;
        int i13 = this.off;
        this.off = i13 + 1;
        this.b2Max = i12 | (bArr8[i13] & 255);
        this.dbSegSize = (this.b2Max - this.b2Min) + 1;
        this.b2cDB2 = readDB(this.b1MinDB2, this.b2Min, this.dbSegSize);
    }

    void readCOMPOSITE() {
        char[] charArray = readCharArray();
        int length = charArray.length / 3;
        this.b2cComp = new Entry[length];
        this.c2bComp = new Entry[length];
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            Entry entry = new Entry();
            int i4 = i2;
            int i5 = i2 + 1;
            entry.f13589bs = charArray[i4];
            int i6 = i5 + 1;
            entry.cp = charArray[i5];
            i2 = i6 + 1;
            entry.cp2 = charArray[i6];
            this.b2cComp[i3] = entry;
            this.c2bComp[i3] = entry;
        }
        Arrays.sort(this.c2bComp, 0, this.c2bComp.length, comparatorComp);
    }

    CharsetMapping load(InputStream inputStream) {
        try {
            int i2 = ((inputStream.read() & 255) << 24) | ((inputStream.read() & 255) << 16) | ((inputStream.read() & 255) << 8) | (inputStream.read() & 255);
            this.f13588bb = new byte[i2];
            this.off = 0;
            if (!readNBytes(inputStream, this.f13588bb, i2)) {
                throw new RuntimeException("Corrupted data file");
            }
            inputStream.close();
            while (this.off < i2) {
                byte[] bArr = this.f13588bb;
                int i3 = this.off;
                this.off = i3 + 1;
                int i4 = (bArr[i3] & 255) << 8;
                byte[] bArr2 = this.f13588bb;
                int i5 = this.off;
                this.off = i5 + 1;
                switch (i4 | (bArr2[i5] & 255)) {
                    case 1:
                        readSINGLEBYTE();
                        break;
                    case 2:
                        readDOUBLEBYTE1();
                        break;
                    case 3:
                        readDOUBLEBYTE2();
                        break;
                    case 4:
                    default:
                        throw new RuntimeException("Corrupted data file");
                    case 5:
                        this.b2cSupp = readCharArray();
                        break;
                    case 6:
                        this.c2bSupp = readCharArray();
                        break;
                    case 7:
                        readCOMPOSITE();
                        break;
                    case 8:
                        readINDEXC2B();
                        break;
                }
            }
            this.f13588bb = null;
            return this;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
