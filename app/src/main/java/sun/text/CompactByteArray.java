package sun.text;

/* loaded from: rt.jar:sun/text/CompactByteArray.class */
public final class CompactByteArray implements Cloneable {
    public static final int UNICODECOUNT = 65536;
    private static final int BLOCKSHIFT = 7;
    private static final int BLOCKCOUNT = 128;
    private static final int INDEXSHIFT = 9;
    private static final int INDEXCOUNT = 512;
    private static final int BLOCKMASK = 127;
    private byte[] values;
    private short[] indices;
    private boolean isCompact;
    private int[] hashes;

    public CompactByteArray(byte b2) {
        this.values = new byte[65536];
        this.indices = new short[512];
        this.hashes = new int[512];
        for (int i2 = 0; i2 < 65536; i2++) {
            this.values[i2] = b2;
        }
        for (int i3 = 0; i3 < 512; i3++) {
            this.indices[i3] = (short) (i3 << 7);
            this.hashes[i3] = 0;
        }
        this.isCompact = false;
    }

    public CompactByteArray(short[] sArr, byte[] bArr) {
        if (sArr.length != 512) {
            throw new IllegalArgumentException("Index out of bounds!");
        }
        for (int i2 = 0; i2 < 512; i2++) {
            short s2 = sArr[i2];
            if (s2 < 0 || s2 >= bArr.length + 128) {
                throw new IllegalArgumentException("Index out of bounds!");
            }
        }
        this.indices = sArr;
        this.values = bArr;
        this.isCompact = true;
    }

    public byte elementAt(char c2) {
        return this.values[(this.indices[c2 >> 7] & 65535) + (c2 & 127)];
    }

    public void setElementAt(char c2, byte b2) {
        if (this.isCompact) {
            expand();
        }
        this.values[c2] = b2;
        touchBlock(c2 >> 7, b2);
    }

    public void setElementAt(char c2, char c3, byte b2) {
        if (this.isCompact) {
            expand();
        }
        for (int i2 = c2; i2 <= c3; i2++) {
            this.values[i2] = b2;
            touchBlock(i2 >> 7, b2);
        }
    }

    public void compact() {
        if (!this.isCompact) {
            int i2 = 0;
            int i3 = 0;
            short s2 = -1;
            int i4 = 0;
            while (i4 < this.indices.length) {
                this.indices[i4] = -1;
                boolean zBlockTouched = blockTouched(i4);
                if (!zBlockTouched && s2 != -1) {
                    this.indices[i4] = s2;
                } else {
                    int i5 = 0;
                    int i6 = 0;
                    while (true) {
                        if (i6 < i2) {
                            if (this.hashes[i4] != this.hashes[i6] || !arrayRegionMatches(this.values, i3, this.values, i5, 128)) {
                                i6++;
                                i5 += 128;
                            } else {
                                this.indices[i4] = (short) i5;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (this.indices[i4] == -1) {
                        System.arraycopy(this.values, i3, this.values, i5, 128);
                        this.indices[i4] = (short) i5;
                        this.hashes[i6] = this.hashes[i4];
                        i2++;
                        if (!zBlockTouched) {
                            s2 = (short) i5;
                        }
                    }
                }
                i4++;
                i3 += 128;
            }
            int i7 = i2 * 128;
            byte[] bArr = new byte[i7];
            System.arraycopy(this.values, 0, bArr, 0, i7);
            this.values = bArr;
            this.isCompact = true;
            this.hashes = null;
        }
    }

    static final boolean arrayRegionMatches(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) {
        int i5 = i2 + i4;
        int i6 = i3 - i2;
        for (int i7 = i2; i7 < i5; i7++) {
            if (bArr[i7] != bArr2[i7 + i6]) {
                return false;
            }
        }
        return true;
    }

    private final void touchBlock(int i2, int i3) {
        this.hashes[i2] = (this.hashes[i2] + (i3 << 1)) | 1;
    }

    private final boolean blockTouched(int i2) {
        return this.hashes[i2] != 0;
    }

    public short[] getIndexArray() {
        return this.indices;
    }

    public byte[] getStringArray() {
        return this.values;
    }

    public Object clone() {
        try {
            CompactByteArray compactByteArray = (CompactByteArray) super.clone();
            compactByteArray.values = (byte[]) this.values.clone();
            compactByteArray.indices = (short[]) this.indices.clone();
            if (this.hashes != null) {
                compactByteArray.hashes = (int[]) this.hashes.clone();
            }
            return compactByteArray;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CompactByteArray compactByteArray = (CompactByteArray) obj;
        for (int i2 = 0; i2 < 65536; i2++) {
            if (elementAt((char) i2) != compactByteArray.elementAt((char) i2)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i2 = 0;
        int iMin = Math.min(3, this.values.length / 16);
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < this.values.length) {
                i2 = (i2 * 37) + this.values[i4];
                i3 = i4 + iMin;
            } else {
                return i2;
            }
        }
    }

    private void expand() {
        if (this.isCompact) {
            this.hashes = new int[512];
            byte[] bArr = new byte[65536];
            for (int i2 = 0; i2 < 65536; i2++) {
                byte bElementAt = elementAt((char) i2);
                bArr[i2] = bElementAt;
                touchBlock(i2 >> 7, bElementAt);
            }
            for (int i3 = 0; i3 < 512; i3++) {
                this.indices[i3] = (short) (i3 << 7);
            }
            this.values = null;
            this.values = bArr;
            this.isCompact = false;
        }
    }

    private byte[] getArray() {
        return this.values;
    }
}
