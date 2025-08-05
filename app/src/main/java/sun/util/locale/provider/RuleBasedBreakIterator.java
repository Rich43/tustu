package sun.util.locale.provider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.MissingResourceException;
import sun.text.CompactByteArray;
import sun.text.SupplementaryCharacterData;

/* loaded from: rt.jar:sun/util/locale/provider/RuleBasedBreakIterator.class */
class RuleBasedBreakIterator extends BreakIterator {
    protected static final byte IGNORE = -1;
    private static final short START_STATE = 1;
    private static final short STOP_STATE = 0;
    static final byte[] LABEL = {66, 73, 100, 97, 116, 97, 0};
    static final int LABEL_LENGTH = LABEL.length;
    static final byte supportedVersion = 1;
    private static final int HEADER_LENGTH = 36;
    private static final int BMP_INDICES_LENGTH = 512;
    private int numCategories;
    private long checksum;
    private CompactByteArray charCategoryTable = null;
    private SupplementaryCharacterData supplementaryCharCategoryTable = null;
    private short[] stateTable = null;
    private short[] backwardsStateTable = null;
    private boolean[] endStates = null;
    private boolean[] lookaheadStates = null;
    private byte[] additionalData = null;
    private CharacterIterator text = null;
    private int cachedLastKnownBreak = -1;

    RuleBasedBreakIterator(String str) throws MissingResourceException, IOException {
        readTables(str);
    }

    protected final void readTables(String str) throws MissingResourceException, IOException {
        byte[] file = readFile(str);
        int i2 = getInt(file, 0);
        int i3 = getInt(file, 4);
        int i4 = getInt(file, 8);
        int i5 = getInt(file, 12);
        int i6 = getInt(file, 16);
        int i7 = getInt(file, 20);
        int i8 = getInt(file, 24);
        this.checksum = getLong(file, 28);
        this.stateTable = new short[i2];
        int i9 = 36;
        int i10 = 0;
        while (i10 < i2) {
            this.stateTable[i10] = getShort(file, i9);
            i10++;
            i9 += 2;
        }
        this.backwardsStateTable = new short[i3];
        int i11 = 0;
        while (i11 < i3) {
            this.backwardsStateTable[i11] = getShort(file, i9);
            i11++;
            i9 += 2;
        }
        this.endStates = new boolean[i4];
        int i12 = 0;
        while (i12 < i4) {
            this.endStates[i12] = file[i9] == 1;
            i12++;
            i9++;
        }
        this.lookaheadStates = new boolean[i5];
        int i13 = 0;
        while (i13 < i5) {
            this.lookaheadStates[i13] = file[i9] == 1;
            i13++;
            i9++;
        }
        short[] sArr = new short[512];
        int i14 = 0;
        while (i14 < 512) {
            sArr[i14] = getShort(file, i9);
            i14++;
            i9 += 2;
        }
        byte[] bArr = new byte[i6];
        System.arraycopy(file, i9, bArr, 0, i6);
        int i15 = i9 + i6;
        this.charCategoryTable = new CompactByteArray(sArr, bArr);
        int[] iArr = new int[i7];
        int i16 = 0;
        while (i16 < i7) {
            iArr[i16] = getInt(file, i15);
            i16++;
            i15 += 4;
        }
        this.supplementaryCharCategoryTable = new SupplementaryCharacterData(iArr);
        if (i8 > 0) {
            this.additionalData = new byte[i8];
            System.arraycopy(file, i15, this.additionalData, 0, i8);
        }
        this.numCategories = this.stateTable.length / this.endStates.length;
    }

    protected byte[] readFile(final String str) throws MissingResourceException, IOException {
        try {
            BufferedInputStream bufferedInputStream = (BufferedInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<BufferedInputStream>() { // from class: sun.util.locale.provider.RuleBasedBreakIterator.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public BufferedInputStream run() throws Exception {
                    return new BufferedInputStream(getClass().getResourceAsStream("/sun/text/resources/" + str));
                }
            });
            int i2 = 0;
            int i3 = LABEL_LENGTH + 5;
            byte[] bArr = new byte[i3];
            if (bufferedInputStream.read(bArr) != i3) {
                throw new MissingResourceException("Wrong header length", str, "");
            }
            int i4 = 0;
            while (i4 < LABEL_LENGTH) {
                if (bArr[i2] == LABEL[i2]) {
                    i4++;
                    i2++;
                } else {
                    throw new MissingResourceException("Wrong magic number", str, "");
                }
            }
            if (bArr[i2] != 1) {
                throw new MissingResourceException("Unsupported version(" + ((int) bArr[i2]) + ")", str, "");
            }
            int i5 = getInt(bArr, i2 + 1);
            byte[] bArr2 = new byte[i5];
            if (bufferedInputStream.read(bArr2) != i5) {
                throw new MissingResourceException("Wrong data length", str, "");
            }
            bufferedInputStream.close();
            return bArr2;
        } catch (PrivilegedActionException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    byte[] getAdditionalData() {
        return this.additionalData;
    }

    void setAdditionalData(byte[] bArr) {
        this.additionalData = bArr;
    }

    @Override // java.text.BreakIterator
    public Object clone() {
        RuleBasedBreakIterator ruleBasedBreakIterator = (RuleBasedBreakIterator) super.clone();
        if (this.text != null) {
            ruleBasedBreakIterator.text = (CharacterIterator) this.text.clone();
        }
        return ruleBasedBreakIterator;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            RuleBasedBreakIterator ruleBasedBreakIterator = (RuleBasedBreakIterator) obj;
            if (this.checksum != ruleBasedBreakIterator.checksum) {
                return false;
            }
            if (this.text == null) {
                return ruleBasedBreakIterator.text == null;
            }
            return this.text.equals(ruleBasedBreakIterator.text);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public String toString() {
        return "[checksum=0x" + Long.toHexString(this.checksum) + ']';
    }

    public int hashCode() {
        return (int) this.checksum;
    }

    @Override // java.text.BreakIterator
    public int first() {
        CharacterIterator text = getText();
        text.first();
        return text.getIndex();
    }

    @Override // java.text.BreakIterator
    public int last() {
        CharacterIterator text = getText();
        text.setIndex(text.getEndIndex());
        return text.getIndex();
    }

    @Override // java.text.BreakIterator
    public int next(int i2) {
        int iCurrent = current();
        while (i2 > 0) {
            iCurrent = handleNext();
            i2--;
        }
        while (i2 < 0) {
            iCurrent = previous();
            i2++;
        }
        return iCurrent;
    }

    @Override // java.text.BreakIterator
    public int next() {
        return handleNext();
    }

    @Override // java.text.BreakIterator
    public int previous() {
        CharacterIterator text = getText();
        if (current() == text.getBeginIndex()) {
            return -1;
        }
        int iCurrent = current();
        int iHandlePrevious = this.cachedLastKnownBreak;
        if (iHandlePrevious >= iCurrent || iHandlePrevious <= -1) {
            getPrevious();
            iHandlePrevious = handlePrevious();
        } else {
            text.setIndex(iHandlePrevious);
        }
        int iHandleNext = iHandlePrevious;
        while (true) {
            int i2 = iHandleNext;
            if (i2 == -1 || i2 >= iCurrent) {
                break;
            }
            iHandlePrevious = i2;
            iHandleNext = handleNext();
        }
        text.setIndex(iHandlePrevious);
        this.cachedLastKnownBreak = iHandlePrevious;
        return iHandlePrevious;
    }

    private int getPrevious() {
        char cPrevious = this.text.previous();
        if (Character.isLowSurrogate(cPrevious) && this.text.getIndex() > this.text.getBeginIndex()) {
            char cPrevious2 = this.text.previous();
            if (Character.isHighSurrogate(cPrevious2)) {
                return Character.toCodePoint(cPrevious2, cPrevious);
            }
            this.text.next();
        }
        return cPrevious;
    }

    int getCurrent() {
        char cCurrent = this.text.current();
        if (Character.isHighSurrogate(cCurrent) && this.text.getIndex() < this.text.getEndIndex()) {
            char next = this.text.next();
            this.text.previous();
            if (Character.isLowSurrogate(next)) {
                return Character.toCodePoint(cCurrent, next);
            }
        }
        return cCurrent;
    }

    private int getCurrentCodePointCount() {
        if (Character.isHighSurrogate(this.text.current()) && this.text.getIndex() < this.text.getEndIndex()) {
            char next = this.text.next();
            this.text.previous();
            if (Character.isLowSurrogate(next)) {
                return 2;
            }
            return 1;
        }
        return 1;
    }

    int getNext() {
        int currentCodePointCount;
        int index = this.text.getIndex();
        int endIndex = this.text.getEndIndex();
        if (index == endIndex || (currentCodePointCount = index + getCurrentCodePointCount()) >= endIndex) {
            return 65535;
        }
        this.text.setIndex(currentCodePointCount);
        return getCurrent();
    }

    private int getNextIndex() {
        int index = this.text.getIndex() + getCurrentCodePointCount();
        int endIndex = this.text.getEndIndex();
        if (index > endIndex) {
            return endIndex;
        }
        return index;
    }

    protected static final void checkOffset(int i2, CharacterIterator characterIterator) {
        if (i2 < characterIterator.getBeginIndex() || i2 > characterIterator.getEndIndex()) {
            throw new IllegalArgumentException("offset out of bounds");
        }
    }

    @Override // java.text.BreakIterator
    public int following(int i2) {
        CharacterIterator text = getText();
        checkOffset(i2, text);
        text.setIndex(i2);
        if (i2 == text.getBeginIndex()) {
            this.cachedLastKnownBreak = handleNext();
            return this.cachedLastKnownBreak;
        }
        int iHandlePrevious = this.cachedLastKnownBreak;
        if (iHandlePrevious >= i2 || iHandlePrevious <= -1) {
            iHandlePrevious = handlePrevious();
        } else {
            text.setIndex(iHandlePrevious);
        }
        while (iHandlePrevious != -1 && iHandlePrevious <= i2) {
            iHandlePrevious = handleNext();
        }
        this.cachedLastKnownBreak = iHandlePrevious;
        return iHandlePrevious;
    }

    @Override // java.text.BreakIterator
    public int preceding(int i2) {
        CharacterIterator text = getText();
        checkOffset(i2, text);
        text.setIndex(i2);
        return previous();
    }

    @Override // java.text.BreakIterator
    public boolean isBoundary(int i2) {
        CharacterIterator text = getText();
        checkOffset(i2, text);
        return i2 == text.getBeginIndex() || following(i2 - 1) == i2;
    }

    @Override // java.text.BreakIterator
    public int current() {
        return getText().getIndex();
    }

    @Override // java.text.BreakIterator
    public CharacterIterator getText() {
        if (this.text == null) {
            this.text = new StringCharacterIterator("");
        }
        return this.text;
    }

    @Override // java.text.BreakIterator
    public void setText(CharacterIterator characterIterator) {
        boolean z2;
        int endIndex = characterIterator.getEndIndex();
        try {
            characterIterator.setIndex(endIndex);
            z2 = characterIterator.getIndex() == endIndex;
        } catch (IllegalArgumentException e2) {
            z2 = false;
        }
        if (z2) {
            this.text = characterIterator;
        } else {
            this.text = new SafeCharIterator(characterIterator);
        }
        this.text.first();
        this.cachedLastKnownBreak = -1;
    }

    protected int handleNext() {
        int i2;
        CharacterIterator text = getText();
        if (text.getIndex() == text.getEndIndex()) {
            return -1;
        }
        int nextIndex = getNextIndex();
        int nextIndex2 = 0;
        int iLookupState = 1;
        int current = getCurrent();
        while (true) {
            i2 = current;
            if (i2 == 65535 || iLookupState == 0) {
                break;
            }
            int iLookupCategory = lookupCategory(i2);
            if (iLookupCategory != -1) {
                iLookupState = lookupState(iLookupState, iLookupCategory);
            }
            if (this.lookaheadStates[iLookupState]) {
                if (this.endStates[iLookupState]) {
                    nextIndex = nextIndex2;
                } else {
                    nextIndex2 = getNextIndex();
                }
            } else if (this.endStates[iLookupState]) {
                nextIndex = getNextIndex();
            }
            current = getNext();
        }
        if (i2 == 65535 && nextIndex2 == text.getEndIndex()) {
            nextIndex = nextIndex2;
        }
        text.setIndex(nextIndex);
        return nextIndex;
    }

    protected int handlePrevious() {
        int i2;
        CharacterIterator text = getText();
        int iLookupBackwardState = 1;
        int iLookupCategory = 0;
        int i3 = 0;
        int current = getCurrent();
        while (true) {
            i2 = current;
            if (i2 == 65535 || iLookupBackwardState == 0) {
                break;
            }
            i3 = iLookupCategory;
            iLookupCategory = lookupCategory(i2);
            if (iLookupCategory != -1) {
                iLookupBackwardState = lookupBackwardState(iLookupBackwardState, iLookupCategory);
            }
            current = getPrevious();
        }
        if (i2 != 65535) {
            if (i3 != -1) {
                getNext();
                getNext();
            } else {
                getNext();
            }
        }
        return text.getIndex();
    }

    protected int lookupCategory(int i2) {
        if (i2 < 65536) {
            return this.charCategoryTable.elementAt((char) i2);
        }
        return this.supplementaryCharCategoryTable.getValue(i2);
    }

    protected int lookupState(int i2, int i3) {
        return this.stateTable[(i2 * this.numCategories) + i3];
    }

    protected int lookupBackwardState(int i2, int i3) {
        return this.backwardsStateTable[(i2 * this.numCategories) + i3];
    }

    static long getLong(byte[] bArr, int i2) {
        long j2 = bArr[i2] & 255;
        for (int i3 = 1; i3 < 8; i3++) {
            j2 = (j2 << 8) | (bArr[i2 + i3] & 255);
        }
        return j2;
    }

    static int getInt(byte[] bArr, int i2) {
        int i3 = bArr[i2] & 255;
        for (int i4 = 1; i4 < 4; i4++) {
            i3 = (i3 << 8) | (bArr[i2 + i4] & 255);
        }
        return i3;
    }

    static short getShort(byte[] bArr, int i2) {
        return (short) ((((short) (bArr[i2] & 255)) << 8) | (bArr[i2 + 1] & 255));
    }

    /* loaded from: rt.jar:sun/util/locale/provider/RuleBasedBreakIterator$SafeCharIterator.class */
    private static final class SafeCharIterator implements CharacterIterator, Cloneable {
        private CharacterIterator base;
        private int rangeStart;
        private int rangeLimit;
        private int currentIndex;

        SafeCharIterator(CharacterIterator characterIterator) {
            this.base = characterIterator;
            this.rangeStart = characterIterator.getBeginIndex();
            this.rangeLimit = characterIterator.getEndIndex();
            this.currentIndex = characterIterator.getIndex();
        }

        @Override // java.text.CharacterIterator
        public char first() {
            return setIndex(this.rangeStart);
        }

        @Override // java.text.CharacterIterator
        public char last() {
            return setIndex(this.rangeLimit - 1);
        }

        @Override // java.text.CharacterIterator
        public char current() {
            if (this.currentIndex < this.rangeStart || this.currentIndex >= this.rangeLimit) {
                return (char) 65535;
            }
            return this.base.setIndex(this.currentIndex);
        }

        @Override // java.text.CharacterIterator
        public char next() {
            this.currentIndex++;
            if (this.currentIndex >= this.rangeLimit) {
                this.currentIndex = this.rangeLimit;
                return (char) 65535;
            }
            return this.base.setIndex(this.currentIndex);
        }

        @Override // java.text.CharacterIterator
        public char previous() {
            this.currentIndex--;
            if (this.currentIndex < this.rangeStart) {
                this.currentIndex = this.rangeStart;
                return (char) 65535;
            }
            return this.base.setIndex(this.currentIndex);
        }

        @Override // java.text.CharacterIterator
        public char setIndex(int i2) {
            if (i2 < this.rangeStart || i2 > this.rangeLimit) {
                throw new IllegalArgumentException("Invalid position");
            }
            this.currentIndex = i2;
            return current();
        }

        @Override // java.text.CharacterIterator
        public int getBeginIndex() {
            return this.rangeStart;
        }

        @Override // java.text.CharacterIterator
        public int getEndIndex() {
            return this.rangeLimit;
        }

        @Override // java.text.CharacterIterator
        public int getIndex() {
            return this.currentIndex;
        }

        @Override // java.text.CharacterIterator
        public Object clone() {
            try {
                SafeCharIterator safeCharIterator = (SafeCharIterator) super.clone();
                safeCharIterator.base = (CharacterIterator) this.base.clone();
                return safeCharIterator;
            } catch (CloneNotSupportedException e2) {
                throw new Error("Clone not supported: " + ((Object) e2));
            }
        }
    }
}
