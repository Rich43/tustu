package sun.text.normalizer;

import java.text.CharacterIterator;
import java.text.Normalizer;

/* loaded from: rt.jar:sun/text/normalizer/NormalizerBase.class */
public final class NormalizerBase implements Cloneable {
    private char[] buffer;
    private int bufferStart;
    private int bufferPos;
    private int bufferLimit;
    private UCharacterIterator text;
    private Mode mode;
    private int options;
    private int currentIndex;
    private int nextIndex;
    public static final int UNICODE_3_2 = 32;
    public static final int DONE = -1;
    public static final Mode NONE = new Mode(1);
    public static final Mode NFD = new NFDMode(2);
    public static final Mode NFKD = new NFKDMode(3);
    public static final Mode NFC = new NFCMode(4);
    public static final Mode NFKC = new NFKCMode(5);
    public static final QuickCheckResult NO = new QuickCheckResult(0);
    public static final QuickCheckResult YES = new QuickCheckResult(1);
    public static final QuickCheckResult MAYBE = new QuickCheckResult(2);
    private static final int MAX_BUF_SIZE_COMPOSE = 2;
    private static final int MAX_BUF_SIZE_DECOMPOSE = 3;
    public static final int UNICODE_3_2_0_ORIGINAL = 262432;
    public static final int UNICODE_LATEST = 0;

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$IsNextBoundary.class */
    private interface IsNextBoundary {
        boolean isNextBoundary(UCharacterIterator uCharacterIterator, int i2, int i3, int[] iArr);
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$IsPrevBoundary.class */
    private interface IsPrevBoundary {
        boolean isPrevBoundary(UCharacterIterator uCharacterIterator, int i2, int i3, char[] cArr);
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$Mode.class */
    public static class Mode {
        private int modeValue;

        private Mode(int i2) {
            this.modeValue = i2;
        }

        protected int normalize(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, UnicodeSet unicodeSet) {
            int i6 = i3 - i2;
            if (i6 > i5 - i4) {
                return i6;
            }
            System.arraycopy(cArr, i2, cArr2, i4, i6);
            return i6;
        }

        protected int normalize(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, int i6) {
            return normalize(cArr, i2, i3, cArr2, i4, i5, NormalizerImpl.getNX(i6));
        }

        protected String normalize(String str, int i2) {
            return str;
        }

        protected int getMinC() {
            return -1;
        }

        protected int getMask() {
            return -1;
        }

        protected IsPrevBoundary getPrevBoundary() {
            return null;
        }

        protected IsNextBoundary getNextBoundary() {
            return null;
        }

        protected QuickCheckResult quickCheck(char[] cArr, int i2, int i3, boolean z2, UnicodeSet unicodeSet) {
            if (z2) {
                return NormalizerBase.MAYBE;
            }
            return NormalizerBase.NO;
        }

        protected boolean isNFSkippable(int i2) {
            return true;
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$NFDMode.class */
    private static final class NFDMode extends Mode {
        private NFDMode(int i2) {
            super(i2);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int normalize(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, UnicodeSet unicodeSet) {
            return NormalizerImpl.decompose(cArr, i2, i3, cArr2, i4, i5, false, new int[1], unicodeSet);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected String normalize(String str, int i2) {
            return NormalizerBase.decompose(str, false, i2);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int getMinC() {
            return 768;
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected IsPrevBoundary getPrevBoundary() {
            return new IsPrevNFDSafe();
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected IsNextBoundary getNextBoundary() {
            return new IsNextNFDSafe();
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int getMask() {
            return 65284;
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected QuickCheckResult quickCheck(char[] cArr, int i2, int i3, boolean z2, UnicodeSet unicodeSet) {
            return NormalizerImpl.quickCheck(cArr, i2, i3, NormalizerImpl.getFromIndexesArr(8), 4, 0, z2, unicodeSet);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected boolean isNFSkippable(int i2) {
            return NormalizerImpl.isNFSkippable(i2, this, 65284L);
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$NFKDMode.class */
    private static final class NFKDMode extends Mode {
        private NFKDMode(int i2) {
            super(i2);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int normalize(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, UnicodeSet unicodeSet) {
            return NormalizerImpl.decompose(cArr, i2, i3, cArr2, i4, i5, true, new int[1], unicodeSet);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected String normalize(String str, int i2) {
            return NormalizerBase.decompose(str, true, i2);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int getMinC() {
            return 768;
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected IsPrevBoundary getPrevBoundary() {
            return new IsPrevNFDSafe();
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected IsNextBoundary getNextBoundary() {
            return new IsNextNFDSafe();
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int getMask() {
            return 65288;
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected QuickCheckResult quickCheck(char[] cArr, int i2, int i3, boolean z2, UnicodeSet unicodeSet) {
            return NormalizerImpl.quickCheck(cArr, i2, i3, NormalizerImpl.getFromIndexesArr(9), 8, 4096, z2, unicodeSet);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected boolean isNFSkippable(int i2) {
            return NormalizerImpl.isNFSkippable(i2, this, 65288L);
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$NFCMode.class */
    private static final class NFCMode extends Mode {
        private NFCMode(int i2) {
            super(i2);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int normalize(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, UnicodeSet unicodeSet) {
            return NormalizerImpl.compose(cArr, i2, i3, cArr2, i4, i5, 0, unicodeSet);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected String normalize(String str, int i2) {
            return NormalizerBase.compose(str, false, i2);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int getMinC() {
            return NormalizerImpl.getFromIndexesArr(6);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected IsPrevBoundary getPrevBoundary() {
            return new IsPrevTrueStarter();
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected IsNextBoundary getNextBoundary() {
            return new IsNextTrueStarter();
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int getMask() {
            return 65297;
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected QuickCheckResult quickCheck(char[] cArr, int i2, int i3, boolean z2, UnicodeSet unicodeSet) {
            return NormalizerImpl.quickCheck(cArr, i2, i3, NormalizerImpl.getFromIndexesArr(6), 17, 0, z2, unicodeSet);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected boolean isNFSkippable(int i2) {
            return NormalizerImpl.isNFSkippable(i2, this, 65473L);
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$NFKCMode.class */
    private static final class NFKCMode extends Mode {
        private NFKCMode(int i2) {
            super(i2);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int normalize(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, UnicodeSet unicodeSet) {
            return NormalizerImpl.compose(cArr, i2, i3, cArr2, i4, i5, 4096, unicodeSet);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected String normalize(String str, int i2) {
            return NormalizerBase.compose(str, true, i2);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int getMinC() {
            return NormalizerImpl.getFromIndexesArr(7);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected IsPrevBoundary getPrevBoundary() {
            return new IsPrevTrueStarter();
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected IsNextBoundary getNextBoundary() {
            return new IsNextTrueStarter();
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected int getMask() {
            return 65314;
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected QuickCheckResult quickCheck(char[] cArr, int i2, int i3, boolean z2, UnicodeSet unicodeSet) {
            return NormalizerImpl.quickCheck(cArr, i2, i3, NormalizerImpl.getFromIndexesArr(7), 34, 4096, z2, unicodeSet);
        }

        @Override // sun.text.normalizer.NormalizerBase.Mode
        protected boolean isNFSkippable(int i2) {
            return NormalizerImpl.isNFSkippable(i2, this, 65474L);
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$QuickCheckResult.class */
    public static final class QuickCheckResult {
        private int resultValue;

        private QuickCheckResult(int i2) {
            this.resultValue = i2;
        }
    }

    public NormalizerBase(String str, Mode mode, int i2) {
        this.buffer = new char[100];
        this.bufferStart = 0;
        this.bufferPos = 0;
        this.bufferLimit = 0;
        this.mode = NFC;
        this.options = 0;
        this.text = UCharacterIterator.getInstance(str);
        this.mode = mode;
        this.options = i2;
    }

    public NormalizerBase(CharacterIterator characterIterator, Mode mode) {
        this(characterIterator, mode, 0);
    }

    public NormalizerBase(CharacterIterator characterIterator, Mode mode, int i2) {
        this.buffer = new char[100];
        this.bufferStart = 0;
        this.bufferPos = 0;
        this.bufferLimit = 0;
        this.mode = NFC;
        this.options = 0;
        this.text = UCharacterIterator.getInstance((CharacterIterator) characterIterator.clone());
        this.mode = mode;
        this.options = i2;
    }

    public Object clone() {
        try {
            NormalizerBase normalizerBase = (NormalizerBase) super.clone();
            normalizerBase.text = (UCharacterIterator) this.text.clone();
            if (this.buffer != null) {
                normalizerBase.buffer = new char[this.buffer.length];
                System.arraycopy(this.buffer, 0, normalizerBase.buffer, 0, this.buffer.length);
            }
            return normalizerBase;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    public static String compose(String str, boolean z2, int i2) {
        char[] cArr;
        char[] charArray;
        if (i2 == 262432) {
            String strConvert = NormalizerImpl.convert(str);
            cArr = new char[strConvert.length() * 2];
            charArray = strConvert.toCharArray();
        } else {
            cArr = new char[str.length() * 2];
            charArray = str.toCharArray();
        }
        UnicodeSet nx = NormalizerImpl.getNX(i2);
        int i3 = i2 & (-12544);
        if (z2) {
            i3 |= 4096;
        }
        while (true) {
            int iCompose = NormalizerImpl.compose(charArray, 0, charArray.length, cArr, 0, cArr.length, i3, nx);
            if (iCompose <= cArr.length) {
                return new String(cArr, 0, iCompose);
            }
            cArr = new char[iCompose];
        }
    }

    public static String decompose(String str, boolean z2) {
        return decompose(str, z2, 0);
    }

    public static String decompose(String str, boolean z2, int i2) {
        int[] iArr = new int[1];
        UnicodeSet nx = NormalizerImpl.getNX(i2);
        if (i2 == 262432) {
            String strConvert = NormalizerImpl.convert(str);
            char[] cArr = new char[strConvert.length() * 3];
            while (true) {
                char[] cArr2 = cArr;
                int iDecompose = NormalizerImpl.decompose(strConvert.toCharArray(), 0, strConvert.length(), cArr2, 0, cArr2.length, z2, iArr, nx);
                if (iDecompose <= cArr2.length) {
                    return new String(cArr2, 0, iDecompose);
                }
                cArr = new char[iDecompose];
            }
        } else {
            char[] cArr3 = new char[str.length() * 3];
            while (true) {
                char[] cArr4 = cArr3;
                int iDecompose2 = NormalizerImpl.decompose(str.toCharArray(), 0, str.length(), cArr4, 0, cArr4.length, z2, iArr, nx);
                if (iDecompose2 <= cArr4.length) {
                    return new String(cArr4, 0, iDecompose2);
                }
                cArr3 = new char[iDecompose2];
            }
        }
    }

    public static int normalize(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, Mode mode, int i6) {
        int iNormalize = mode.normalize(cArr, i2, i3, cArr2, i4, i5, i6);
        if (iNormalize <= i5 - i4) {
            return iNormalize;
        }
        throw new IndexOutOfBoundsException(Integer.toString(iNormalize));
    }

    public int current() {
        if (this.bufferPos < this.bufferLimit || nextNormalize()) {
            return getCodePointAt(this.bufferPos);
        }
        return -1;
    }

    public int next() {
        if (this.bufferPos < this.bufferLimit || nextNormalize()) {
            int codePointAt = getCodePointAt(this.bufferPos);
            this.bufferPos += codePointAt > 65535 ? 2 : 1;
            return codePointAt;
        }
        return -1;
    }

    public int previous() {
        if (this.bufferPos > 0 || previousNormalize()) {
            int codePointAt = getCodePointAt(this.bufferPos - 1);
            this.bufferPos -= codePointAt > 65535 ? 2 : 1;
            return codePointAt;
        }
        return -1;
    }

    public void reset() {
        this.text.setIndex(0);
        this.nextIndex = 0;
        this.currentIndex = 0;
        clearBuffer();
    }

    public void setIndexOnly(int i2) {
        this.text.setIndex(i2);
        this.nextIndex = i2;
        this.currentIndex = i2;
        clearBuffer();
    }

    @Deprecated
    public int setIndex(int i2) {
        setIndexOnly(i2);
        return current();
    }

    @Deprecated
    public int getBeginIndex() {
        return 0;
    }

    @Deprecated
    public int getEndIndex() {
        return endIndex();
    }

    public int getIndex() {
        if (this.bufferPos < this.bufferLimit) {
            return this.currentIndex;
        }
        return this.nextIndex;
    }

    public int endIndex() {
        return this.text.getLength();
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setText(String str) {
        UCharacterIterator uCharacterIterator = UCharacterIterator.getInstance(str);
        if (uCharacterIterator == null) {
            throw new InternalError("Could not create a new UCharacterIterator");
        }
        this.text = uCharacterIterator;
        reset();
    }

    public void setText(CharacterIterator characterIterator) {
        UCharacterIterator uCharacterIterator = UCharacterIterator.getInstance(characterIterator);
        if (uCharacterIterator == null) {
            throw new InternalError("Could not create a new UCharacterIterator");
        }
        this.text = uCharacterIterator;
        this.nextIndex = 0;
        this.currentIndex = 0;
        clearBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getPrevNorm32(UCharacterIterator uCharacterIterator, int i2, int i3, char[] cArr) {
        int iPrevious = uCharacterIterator.previous();
        if (iPrevious == -1) {
            return 0L;
        }
        cArr[0] = (char) iPrevious;
        cArr[1] = 0;
        if (cArr[0] < i2) {
            return 0L;
        }
        if (!UTF16.isSurrogate(cArr[0])) {
            return NormalizerImpl.getNorm32(cArr[0]);
        }
        if (UTF16.isLeadSurrogate(cArr[0]) || uCharacterIterator.getIndex() == 0) {
            cArr[1] = (char) uCharacterIterator.current();
            return 0L;
        }
        char cPrevious = (char) uCharacterIterator.previous();
        cArr[1] = cPrevious;
        if (UTF16.isLeadSurrogate(cPrevious)) {
            long norm32 = NormalizerImpl.getNorm32(cArr[1]);
            if ((norm32 & i3) == 0) {
                return 0L;
            }
            return NormalizerImpl.getNorm32FromSurrogatePair(norm32, cArr[0]);
        }
        uCharacterIterator.moveIndex(1);
        return 0L;
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$IsPrevNFDSafe.class */
    private static final class IsPrevNFDSafe implements IsPrevBoundary {
        private IsPrevNFDSafe() {
        }

        @Override // sun.text.normalizer.NormalizerBase.IsPrevBoundary
        public boolean isPrevBoundary(UCharacterIterator uCharacterIterator, int i2, int i3, char[] cArr) {
            return NormalizerImpl.isNFDSafe(NormalizerBase.getPrevNorm32(uCharacterIterator, i2, i3, cArr), i3, i3 & 63);
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$IsPrevTrueStarter.class */
    private static final class IsPrevTrueStarter implements IsPrevBoundary {
        private IsPrevTrueStarter() {
        }

        @Override // sun.text.normalizer.NormalizerBase.IsPrevBoundary
        public boolean isPrevBoundary(UCharacterIterator uCharacterIterator, int i2, int i3, char[] cArr) {
            int i4 = (i3 << 2) & 15;
            return NormalizerImpl.isTrueStarter(NormalizerBase.getPrevNorm32(uCharacterIterator, i2, i3 | i4, cArr), i3, i4);
        }
    }

    private static int findPreviousIterationBoundary(UCharacterIterator uCharacterIterator, IsPrevBoundary isPrevBoundary, int i2, int i3, char[] cArr, int[] iArr) {
        iArr[0] = cArr.length;
        char[] cArr2 = {0, 0};
        while (uCharacterIterator.getIndex() > 0 && cArr2[0] != 65535) {
            boolean zIsPrevBoundary = isPrevBoundary.isPrevBoundary(uCharacterIterator, i2, i3, cArr2);
            if (iArr[0] < (cArr2[1] == 0 ? 1 : 2)) {
                char[] cArr3 = new char[cArr.length * 2];
                System.arraycopy(cArr, iArr[0], cArr3, cArr3.length - (cArr.length - iArr[0]), cArr.length - iArr[0]);
                iArr[0] = iArr[0] + (cArr3.length - cArr.length);
                cArr = cArr3;
            }
            int i4 = iArr[0] - 1;
            iArr[0] = i4;
            cArr[i4] = cArr2[0];
            if (cArr2[1] != 0) {
                int i5 = iArr[0] - 1;
                iArr[0] = i5;
                cArr[i5] = cArr2[1];
            }
            if (zIsPrevBoundary) {
                break;
            }
        }
        return cArr.length - iArr[0];
    }

    private static int previous(UCharacterIterator uCharacterIterator, char[] cArr, int i2, int i3, Mode mode, boolean z2, boolean[] zArr, int i4) {
        int iPrevious;
        int i5 = i3 - i2;
        int iNormalize = 0;
        if (zArr != null) {
            zArr[0] = false;
        }
        char minC = (char) mode.getMinC();
        int mask = mode.getMask();
        IsPrevBoundary prevBoundary = mode.getPrevBoundary();
        if (prevBoundary == null) {
            int i6 = 0;
            int iPrevious2 = uCharacterIterator.previous();
            int i7 = iPrevious2;
            if (iPrevious2 >= 0) {
                i6 = 1;
                if (UTF16.isTrailSurrogate((char) i7) && (iPrevious = uCharacterIterator.previous()) != -1) {
                    if (UTF16.isLeadSurrogate((char) iPrevious)) {
                        if (i5 >= 2) {
                            cArr[1] = (char) i7;
                            i6 = 2;
                        }
                        i7 = iPrevious;
                    } else {
                        uCharacterIterator.moveIndex(1);
                    }
                }
                if (i5 > 0) {
                    cArr[0] = (char) i7;
                }
            }
            return i6;
        }
        char[] cArr2 = new char[100];
        int[] iArr = new int[1];
        int iFindPreviousIterationBoundary = findPreviousIterationBoundary(uCharacterIterator, prevBoundary, minC, mask, cArr2, iArr);
        if (iFindPreviousIterationBoundary > 0) {
            if (z2) {
                iNormalize = normalize(cArr2, iArr[0], iArr[0] + iFindPreviousIterationBoundary, cArr, i2, i3, mode, i4);
                if (zArr != null) {
                    zArr[0] = iNormalize != iFindPreviousIterationBoundary || Utility.arrayRegionMatches(cArr2, 0, cArr, i2, i3);
                }
            } else if (i5 > 0) {
                System.arraycopy(cArr2, iArr[0], cArr, 0, iFindPreviousIterationBoundary < i5 ? iFindPreviousIterationBoundary : i5);
            }
        }
        return iNormalize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getNextNorm32(UCharacterIterator uCharacterIterator, int i2, int i3, int[] iArr) {
        iArr[0] = uCharacterIterator.next();
        iArr[1] = 0;
        if (iArr[0] < i2) {
            return 0L;
        }
        long norm32 = NormalizerImpl.getNorm32((char) iArr[0]);
        if (UTF16.isLeadSurrogate((char) iArr[0])) {
            if (uCharacterIterator.current() != -1) {
                int iCurrent = uCharacterIterator.current();
                iArr[1] = iCurrent;
                if (UTF16.isTrailSurrogate((char) iCurrent)) {
                    uCharacterIterator.moveIndex(1);
                    if ((norm32 & i3) == 0) {
                        return 0L;
                    }
                    return NormalizerImpl.getNorm32FromSurrogatePair(norm32, (char) iArr[1]);
                }
                return 0L;
            }
            return 0L;
        }
        return norm32;
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$IsNextNFDSafe.class */
    private static final class IsNextNFDSafe implements IsNextBoundary {
        private IsNextNFDSafe() {
        }

        @Override // sun.text.normalizer.NormalizerBase.IsNextBoundary
        public boolean isNextBoundary(UCharacterIterator uCharacterIterator, int i2, int i3, int[] iArr) {
            return NormalizerImpl.isNFDSafe(NormalizerBase.getNextNorm32(uCharacterIterator, i2, i3, iArr), i3, i3 & 63);
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerBase$IsNextTrueStarter.class */
    private static final class IsNextTrueStarter implements IsNextBoundary {
        private IsNextTrueStarter() {
        }

        @Override // sun.text.normalizer.NormalizerBase.IsNextBoundary
        public boolean isNextBoundary(UCharacterIterator uCharacterIterator, int i2, int i3, int[] iArr) {
            int i4 = (i3 << 2) & 15;
            return NormalizerImpl.isTrueStarter(NormalizerBase.getNextNorm32(uCharacterIterator, i2, i3 | i4, iArr), i3, i4);
        }
    }

    private static int findNextIterationBoundary(UCharacterIterator uCharacterIterator, IsNextBoundary isNextBoundary, int i2, int i3, char[] cArr) {
        if (uCharacterIterator.current() == -1) {
            return 0;
        }
        int[] iArr = new int[2];
        iArr[0] = uCharacterIterator.next();
        cArr[0] = (char) iArr[0];
        int i4 = 1;
        if (UTF16.isLeadSurrogate((char) iArr[0]) && uCharacterIterator.current() != -1) {
            int next = uCharacterIterator.next();
            iArr[1] = next;
            if (UTF16.isTrailSurrogate((char) next)) {
                i4 = 1 + 1;
                cArr[1] = (char) iArr[1];
            } else {
                uCharacterIterator.moveIndex(-1);
            }
        }
        while (true) {
            if (uCharacterIterator.current() == -1) {
                break;
            }
            if (isNextBoundary.isNextBoundary(uCharacterIterator, i2, i3, iArr)) {
                uCharacterIterator.moveIndex(iArr[1] == 0 ? -1 : -2);
            } else {
                if (i4 + (iArr[1] == 0 ? 1 : 2) <= cArr.length) {
                    int i5 = i4;
                    i4++;
                    cArr[i5] = (char) iArr[0];
                    if (iArr[1] != 0) {
                        i4++;
                        cArr[i4] = (char) iArr[1];
                    }
                } else {
                    char[] cArr2 = new char[cArr.length * 2];
                    System.arraycopy(cArr, 0, cArr2, 0, i4);
                    cArr = cArr2;
                    int i6 = i4;
                    i4++;
                    cArr[i6] = (char) iArr[0];
                    if (iArr[1] != 0) {
                        i4++;
                        cArr[i4] = (char) iArr[1];
                    }
                }
            }
        }
        return i4;
    }

    private static int next(UCharacterIterator uCharacterIterator, char[] cArr, int i2, int i3, Mode mode, boolean z2, boolean[] zArr, int i4) {
        int next;
        int i5 = i3 - i2;
        int iNormalize = 0;
        if (zArr != null) {
            zArr[0] = false;
        }
        char minC = (char) mode.getMinC();
        int mask = mode.getMask();
        IsNextBoundary nextBoundary = mode.getNextBoundary();
        if (nextBoundary == null) {
            int i6 = 0;
            int next2 = uCharacterIterator.next();
            if (next2 != -1) {
                i6 = 1;
                if (UTF16.isLeadSurrogate((char) next2) && (next = uCharacterIterator.next()) != -1) {
                    if (UTF16.isTrailSurrogate((char) next)) {
                        if (i5 >= 2) {
                            cArr[1] = (char) next;
                            i6 = 2;
                        }
                    } else {
                        uCharacterIterator.moveIndex(-1);
                    }
                }
                if (i5 > 0) {
                    cArr[0] = (char) next2;
                }
            }
            return i6;
        }
        char[] cArr2 = new char[100];
        int[] iArr = new int[1];
        int iFindNextIterationBoundary = findNextIterationBoundary(uCharacterIterator, nextBoundary, minC, mask, cArr2);
        if (iFindNextIterationBoundary > 0) {
            if (z2) {
                iNormalize = mode.normalize(cArr2, iArr[0], iFindNextIterationBoundary, cArr, i2, i3, i4);
                if (zArr != null) {
                    zArr[0] = iNormalize != iFindNextIterationBoundary || Utility.arrayRegionMatches(cArr2, iArr[0], cArr, i2, iNormalize);
                }
            } else if (i5 > 0) {
                System.arraycopy(cArr2, 0, cArr, i2, Math.min(iFindNextIterationBoundary, i5));
            }
        }
        return iNormalize;
    }

    private void clearBuffer() {
        this.bufferPos = 0;
        this.bufferStart = 0;
        this.bufferLimit = 0;
    }

    private boolean nextNormalize() {
        clearBuffer();
        this.currentIndex = this.nextIndex;
        this.text.setIndex(this.nextIndex);
        this.bufferLimit = next(this.text, this.buffer, this.bufferStart, this.buffer.length, this.mode, true, null, this.options);
        this.nextIndex = this.text.getIndex();
        return this.bufferLimit > 0;
    }

    private boolean previousNormalize() {
        clearBuffer();
        this.nextIndex = this.currentIndex;
        this.text.setIndex(this.currentIndex);
        this.bufferLimit = previous(this.text, this.buffer, this.bufferStart, this.buffer.length, this.mode, true, null, this.options);
        this.currentIndex = this.text.getIndex();
        this.bufferPos = this.bufferLimit;
        return this.bufferLimit > 0;
    }

    private int getCodePointAt(int i2) {
        if (UTF16.isSurrogate(this.buffer[i2])) {
            if (UTF16.isLeadSurrogate(this.buffer[i2])) {
                if (i2 + 1 < this.bufferLimit && UTF16.isTrailSurrogate(this.buffer[i2 + 1])) {
                    return UCharacterProperty.getRawSupplementary(this.buffer[i2], this.buffer[i2 + 1]);
                }
            } else if (UTF16.isTrailSurrogate(this.buffer[i2]) && i2 > 0 && UTF16.isLeadSurrogate(this.buffer[i2 - 1])) {
                return UCharacterProperty.getRawSupplementary(this.buffer[i2 - 1], this.buffer[i2]);
            }
        }
        return this.buffer[i2];
    }

    public static boolean isNFSkippable(int i2, Mode mode) {
        return mode.isNFSkippable(i2);
    }

    public NormalizerBase(String str, Mode mode) {
        this(str, mode, 0);
    }

    public static String normalize(String str, Normalizer.Form form) {
        return normalize(str, form, 0);
    }

    public static String normalize(String str, Normalizer.Form form, int i2) {
        int length = str.length();
        boolean z2 = true;
        if (length < 80) {
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                if (str.charAt(i3) <= 127) {
                    i3++;
                } else {
                    z2 = false;
                    break;
                }
            }
        } else {
            char[] charArray = str.toCharArray();
            int i4 = 0;
            while (true) {
                if (i4 >= length) {
                    break;
                }
                if (charArray[i4] <= 127) {
                    i4++;
                } else {
                    z2 = false;
                    break;
                }
            }
        }
        switch (form) {
            case NFC:
                return z2 ? str : NFC.normalize(str, i2);
            case NFD:
                return z2 ? str : NFD.normalize(str, i2);
            case NFKC:
                return z2 ? str : NFKC.normalize(str, i2);
            case NFKD:
                return z2 ? str : NFKD.normalize(str, i2);
            default:
                throw new IllegalArgumentException("Unexpected normalization form: " + ((Object) form));
        }
    }

    public static boolean isNormalized(String str, Normalizer.Form form) {
        return isNormalized(str, form, 0);
    }

    public static boolean isNormalized(String str, Normalizer.Form form, int i2) {
        switch (form) {
            case NFC:
                return NFC.quickCheck(str.toCharArray(), 0, str.length(), false, NormalizerImpl.getNX(i2)) == YES;
            case NFD:
                return NFD.quickCheck(str.toCharArray(), 0, str.length(), false, NormalizerImpl.getNX(i2)) == YES;
            case NFKC:
                return NFKC.quickCheck(str.toCharArray(), 0, str.length(), false, NormalizerImpl.getNX(i2)) == YES;
            case NFKD:
                return NFKD.quickCheck(str.toCharArray(), 0, str.length(), false, NormalizerImpl.getNX(i2)) == YES;
            default:
                throw new IllegalArgumentException("Unexpected normalization form: " + ((Object) form));
        }
    }
}
