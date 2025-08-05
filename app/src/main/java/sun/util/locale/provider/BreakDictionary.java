package sun.util.locale.provider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.MissingResourceException;
import sun.text.CompactByteArray;
import sun.text.SupplementaryCharacterData;

/* loaded from: rt.jar:sun/util/locale/provider/BreakDictionary.class */
class BreakDictionary {
    private static int supportedVersion = 1;
    private int numCols;
    private int numColGroups;
    private CompactByteArray columnMap = null;
    private SupplementaryCharacterData supplementaryCharColumnMap = null;
    private short[] table = null;
    private short[] rowIndex = null;
    private int[] rowIndexFlags = null;
    private short[] rowIndexFlagsIndex = null;
    private byte[] rowIndexShifts = null;

    BreakDictionary(String str) throws MissingResourceException, IOException {
        readDictionaryFile(str);
    }

    private void readDictionaryFile(final String str) throws MissingResourceException, IOException {
        try {
            BufferedInputStream bufferedInputStream = (BufferedInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<BufferedInputStream>() { // from class: sun.util.locale.provider.BreakDictionary.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public BufferedInputStream run() throws Exception {
                    return new BufferedInputStream(getClass().getResourceAsStream("/sun/text/resources/" + str));
                }
            });
            byte[] bArr = new byte[8];
            if (bufferedInputStream.read(bArr) != 8) {
                throw new MissingResourceException("Wrong data length", str, "");
            }
            int i2 = RuleBasedBreakIterator.getInt(bArr, 0);
            if (i2 != supportedVersion) {
                throw new MissingResourceException("Dictionary version(" + i2 + ") is unsupported", str, "");
            }
            int i3 = RuleBasedBreakIterator.getInt(bArr, 4);
            byte[] bArr2 = new byte[i3];
            if (bufferedInputStream.read(bArr2) != i3) {
                throw new MissingResourceException("Wrong data length", str, "");
            }
            bufferedInputStream.close();
            int i4 = RuleBasedBreakIterator.getInt(bArr2, 0);
            int i5 = 0 + 4;
            short[] sArr = new short[i4];
            int i6 = 0;
            while (i6 < i4) {
                sArr[i6] = RuleBasedBreakIterator.getShort(bArr2, i5);
                i6++;
                i5 += 2;
            }
            int i7 = RuleBasedBreakIterator.getInt(bArr2, i5);
            int i8 = i5 + 4;
            byte[] bArr3 = new byte[i7];
            int i9 = 0;
            while (i9 < i7) {
                bArr3[i9] = bArr2[i8];
                i9++;
                i8++;
            }
            this.columnMap = new CompactByteArray(sArr, bArr3);
            this.numCols = RuleBasedBreakIterator.getInt(bArr2, i8);
            int i10 = i8 + 4;
            this.numColGroups = RuleBasedBreakIterator.getInt(bArr2, i10);
            int i11 = i10 + 4;
            int i12 = RuleBasedBreakIterator.getInt(bArr2, i11);
            int i13 = i11 + 4;
            this.rowIndex = new short[i12];
            int i14 = 0;
            while (i14 < i12) {
                this.rowIndex[i14] = RuleBasedBreakIterator.getShort(bArr2, i13);
                i14++;
                i13 += 2;
            }
            int i15 = RuleBasedBreakIterator.getInt(bArr2, i13);
            int i16 = i13 + 4;
            this.rowIndexFlagsIndex = new short[i15];
            int i17 = 0;
            while (i17 < i15) {
                this.rowIndexFlagsIndex[i17] = RuleBasedBreakIterator.getShort(bArr2, i16);
                i17++;
                i16 += 2;
            }
            int i18 = RuleBasedBreakIterator.getInt(bArr2, i16);
            int i19 = i16 + 4;
            this.rowIndexFlags = new int[i18];
            int i20 = 0;
            while (i20 < i18) {
                this.rowIndexFlags[i20] = RuleBasedBreakIterator.getInt(bArr2, i19);
                i20++;
                i19 += 4;
            }
            int i21 = RuleBasedBreakIterator.getInt(bArr2, i19);
            int i22 = i19 + 4;
            this.rowIndexShifts = new byte[i21];
            int i23 = 0;
            while (i23 < i21) {
                this.rowIndexShifts[i23] = bArr2[i22];
                i23++;
                i22++;
            }
            int i24 = RuleBasedBreakIterator.getInt(bArr2, i22);
            int i25 = i22 + 4;
            this.table = new short[i24];
            int i26 = 0;
            while (i26 < i24) {
                this.table[i26] = RuleBasedBreakIterator.getShort(bArr2, i25);
                i26++;
                i25 += 2;
            }
            int i27 = RuleBasedBreakIterator.getInt(bArr2, i25);
            int i28 = i25 + 4;
            int[] iArr = new int[i27];
            int i29 = 0;
            while (i29 < i27) {
                iArr[i29] = RuleBasedBreakIterator.getInt(bArr2, i28);
                i29++;
                i28 += 4;
            }
            this.supplementaryCharColumnMap = new SupplementaryCharacterData(iArr);
        } catch (PrivilegedActionException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    public final short getNextStateFromCharacter(int i2, int i3) {
        int value;
        if (i3 < 65536) {
            value = this.columnMap.elementAt((char) i3);
        } else {
            value = this.supplementaryCharColumnMap.getValue(i3);
        }
        return getNextState(i2, value);
    }

    public final short getNextState(int i2, int i3) {
        if (cellIsPopulated(i2, i3)) {
            return internalAt(this.rowIndex[i2], i3 + this.rowIndexShifts[i2]);
        }
        return (short) 0;
    }

    private boolean cellIsPopulated(int i2, int i3) {
        return this.rowIndexFlagsIndex[i2] < 0 ? i3 == (-this.rowIndexFlagsIndex[i2]) : (this.rowIndexFlags[this.rowIndexFlagsIndex[i2] + (i3 >> 5)] & (1 << (i3 & 31))) != 0;
    }

    private short internalAt(int i2, int i3) {
        return this.table[(i2 * this.numCols) + i3];
    }
}
