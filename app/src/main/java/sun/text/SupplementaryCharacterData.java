package sun.text;

/* loaded from: rt.jar:sun/text/SupplementaryCharacterData.class */
public final class SupplementaryCharacterData implements Cloneable {
    private static final byte IGNORE = -1;
    private int[] dataTable;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SupplementaryCharacterData.class.desiredAssertionStatus();
    }

    public SupplementaryCharacterData(int[] iArr) {
        this.dataTable = iArr;
    }

    public int getValue(int i2) {
        int i3;
        if (!$assertionsDisabled && (i2 < 65536 || i2 > 1114111)) {
            throw new AssertionError((Object) ("Invalid code point:" + Integer.toHexString(i2)));
        }
        int i4 = 0;
        int length = this.dataTable.length - 1;
        while (true) {
            i3 = (i4 + length) / 2;
            int i5 = this.dataTable[i3] >> 8;
            int i6 = this.dataTable[i3 + 1] >> 8;
            if (i2 < i5) {
                length = i3;
            } else {
                if (i2 <= i6 - 1) {
                    break;
                }
                i4 = i3;
            }
        }
        int i7 = this.dataTable[i3] & 255;
        if (i7 == 255) {
            return -1;
        }
        return i7;
    }

    public int[] getArray() {
        return this.dataTable;
    }
}
