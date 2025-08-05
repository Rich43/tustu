package sun.text.normalizer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import sun.text.normalizer.NormalizerBase;
import sun.text.normalizer.RangeValueIterator;
import sun.text.normalizer.Trie;

/* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl.class */
public final class NormalizerImpl {
    static final NormalizerImpl IMPL;
    static final int UNSIGNED_BYTE_MASK = 255;
    static final long UNSIGNED_INT_MASK = 4294967295L;
    private static final String DATA_FILE_NAME = "/sun/text/resources/unorm.icu";
    public static final int QC_NFC = 17;
    public static final int QC_NFKC = 34;
    public static final int QC_NFD = 4;
    public static final int QC_NFKD = 8;
    public static final int QC_ANY_NO = 15;
    public static final int QC_MAYBE = 16;
    public static final int QC_ANY_MAYBE = 48;
    public static final int QC_MASK = 63;
    private static final int COMBINES_FWD = 64;
    private static final int COMBINES_BACK = 128;
    public static final int COMBINES_ANY = 192;
    private static final int CC_SHIFT = 8;
    public static final int CC_MASK = 65280;
    private static final int EXTRA_SHIFT = 16;
    private static final long MIN_SPECIAL = 4227858432L;
    private static final long SURROGATES_TOP = 4293918720L;
    private static final long MIN_HANGUL = 4293918720L;
    private static final long JAMO_V_TOP = 4294115328L;
    static final int INDEX_TRIE_SIZE = 0;
    static final int INDEX_CHAR_COUNT = 1;
    static final int INDEX_COMBINE_DATA_COUNT = 2;
    public static final int INDEX_MIN_NFC_NO_MAYBE = 6;
    public static final int INDEX_MIN_NFKC_NO_MAYBE = 7;
    public static final int INDEX_MIN_NFD_NO_MAYBE = 8;
    public static final int INDEX_MIN_NFKD_NO_MAYBE = 9;
    static final int INDEX_FCD_TRIE_SIZE = 10;
    static final int INDEX_AUX_TRIE_SIZE = 11;
    static final int INDEX_TOP = 32;
    private static final int AUX_UNSAFE_SHIFT = 11;
    private static final int AUX_COMP_EX_SHIFT = 10;
    private static final int AUX_NFC_SKIPPABLE_F_SHIFT = 12;
    private static final int AUX_MAX_FNC = 1024;
    private static final int AUX_UNSAFE_MASK = 2048;
    private static final int AUX_FNC_MASK = 1023;
    private static final int AUX_COMP_EX_MASK = 1024;
    private static final long AUX_NFC_SKIP_F_MASK = 4096;
    private static final int MAX_BUFFER_SIZE = 20;
    private static FCDTrieImpl fcdTrieImpl;
    private static NormTrieImpl normTrieImpl;
    private static AuxTrieImpl auxTrieImpl;
    private static int[] indexes;
    private static char[] combiningTable;
    private static char[] extraData;
    private static boolean isDataLoaded;
    private static boolean isFormatVersion_2_1;
    private static boolean isFormatVersion_2_2;
    private static byte[] unicodeVersion;
    private static final int DATA_BUFFER_SIZE = 25000;
    public static final int MIN_WITH_LEAD_CC = 768;
    private static final int DECOMP_FLAG_LENGTH_HAS_CC = 128;
    private static final int DECOMP_LENGTH_MASK = 127;
    private static final int BMP_INDEX_LENGTH = 2048;
    private static final int SURROGATE_BLOCK_BITS = 5;
    public static final int JAMO_L_BASE = 4352;
    public static final int JAMO_V_BASE = 4449;
    public static final int JAMO_T_BASE = 4519;
    public static final int HANGUL_BASE = 44032;
    public static final int JAMO_L_COUNT = 19;
    public static final int JAMO_V_COUNT = 21;
    public static final int JAMO_T_COUNT = 28;
    public static final int HANGUL_COUNT = 11172;
    private static final int OPTIONS_NX_MASK = 31;
    private static final int OPTIONS_UNICODE_MASK = 224;
    public static final int OPTIONS_SETS_MASK = 255;
    private static final UnicodeSet[] nxCache;
    private static final int NX_HANGUL = 1;
    private static final int NX_CJK_COMPAT = 2;
    public static final int BEFORE_PRI_29 = 256;
    public static final int OPTIONS_COMPAT = 4096;
    public static final int OPTIONS_COMPOSE_CONTIGUOUS = 8192;
    public static final int WITHOUT_CORRIGENDUM4_CORRECTIONS = 262144;
    private static final char[][] corrigendum4MappingTable;

    /* JADX WARN: Type inference failed for: r0v5, types: [char[], char[][]] */
    static {
        try {
            IMPL = new NormalizerImpl();
            nxCache = new UnicodeSet[256];
            corrigendum4MappingTable = new char[]{new char[]{55364, 57194}, new char[]{24371}, new char[]{17323}, new char[]{31406}, new char[]{19799}};
        } catch (Exception e2) {
            throw new RuntimeException(e2.getMessage());
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$NormTrieImpl.class */
    static final class NormTrieImpl implements Trie.DataManipulate {
        static IntTrie normTrie = null;

        NormTrieImpl() {
        }

        @Override // sun.text.normalizer.Trie.DataManipulate
        public int getFoldingOffset(int i2) {
            return 2048 + ((i2 >> 11) & 32736);
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$FCDTrieImpl.class */
    static final class FCDTrieImpl implements Trie.DataManipulate {
        static CharTrie fcdTrie = null;

        FCDTrieImpl() {
        }

        @Override // sun.text.normalizer.Trie.DataManipulate
        public int getFoldingOffset(int i2) {
            return i2;
        }
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$AuxTrieImpl.class */
    static final class AuxTrieImpl implements Trie.DataManipulate {
        static CharTrie auxTrie = null;

        AuxTrieImpl() {
        }

        @Override // sun.text.normalizer.Trie.DataManipulate
        public int getFoldingOffset(int i2) {
            return (i2 & 1023) << 5;
        }
    }

    public static int getFromIndexesArr(int i2) {
        return indexes[i2];
    }

    private NormalizerImpl() throws IOException {
        if (!isDataLoaded) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(ICUData.getRequiredStream(DATA_FILE_NAME), DATA_BUFFER_SIZE);
            NormalizerDataReader normalizerDataReader = new NormalizerDataReader(bufferedInputStream);
            indexes = normalizerDataReader.readIndexes(32);
            byte[] bArr = new byte[indexes[0]];
            combiningTable = new char[indexes[2]];
            extraData = new char[indexes[1]];
            byte[] bArr2 = new byte[indexes[10]];
            byte[] bArr3 = new byte[indexes[11]];
            fcdTrieImpl = new FCDTrieImpl();
            normTrieImpl = new NormTrieImpl();
            auxTrieImpl = new AuxTrieImpl();
            normalizerDataReader.read(bArr, bArr2, bArr3, extraData, combiningTable);
            NormTrieImpl.normTrie = new IntTrie(new ByteArrayInputStream(bArr), normTrieImpl);
            FCDTrieImpl.fcdTrie = new CharTrie(new ByteArrayInputStream(bArr2), fcdTrieImpl);
            AuxTrieImpl.auxTrie = new CharTrie(new ByteArrayInputStream(bArr3), auxTrieImpl);
            isDataLoaded = true;
            byte[] dataFormatVersion = normalizerDataReader.getDataFormatVersion();
            isFormatVersion_2_1 = dataFormatVersion[0] > 2 || (dataFormatVersion[0] == 2 && dataFormatVersion[1] >= 1);
            isFormatVersion_2_2 = dataFormatVersion[0] > 2 || (dataFormatVersion[0] == 2 && dataFormatVersion[1] >= 2);
            unicodeVersion = normalizerDataReader.getUnicodeVersion();
            bufferedInputStream.close();
        }
    }

    private static boolean isHangulWithoutJamoT(char c2) {
        char c3 = (char) (c2 - HANGUL_BASE);
        return c3 < 11172 && c3 % 28 == 0;
    }

    private static boolean isNorm32Regular(long j2) {
        return j2 < MIN_SPECIAL;
    }

    private static boolean isNorm32LeadSurrogate(long j2) {
        return MIN_SPECIAL <= j2 && j2 < 4293918720L;
    }

    private static boolean isNorm32HangulOrJamo(long j2) {
        return j2 >= 4293918720L;
    }

    private static boolean isJamoVTNorm32JamoV(long j2) {
        return j2 < JAMO_V_TOP;
    }

    public static long getNorm32(char c2) {
        return 4294967295L & NormTrieImpl.normTrie.getLeadValue(c2);
    }

    public static long getNorm32FromSurrogatePair(long j2, char c2) {
        return 4294967295L & NormTrieImpl.normTrie.getTrailValue((int) j2, c2);
    }

    private static long getNorm32(int i2) {
        return 4294967295L & NormTrieImpl.normTrie.getCodePointValue(i2);
    }

    private static long getNorm32(char[] cArr, int i2, int i3) {
        long norm32 = getNorm32(cArr[i2]);
        if ((norm32 & i3) > 0 && isNorm32LeadSurrogate(norm32)) {
            norm32 = getNorm32FromSurrogatePair(norm32, cArr[i2 + 1]);
        }
        return norm32;
    }

    public static VersionInfo getUnicodeVersion() {
        return VersionInfo.getInstance(unicodeVersion[0], unicodeVersion[1], unicodeVersion[2], unicodeVersion[3]);
    }

    public static char getFCD16(char c2) {
        return FCDTrieImpl.fcdTrie.getLeadValue(c2);
    }

    public static char getFCD16FromSurrogatePair(char c2, char c3) {
        return FCDTrieImpl.fcdTrie.getTrailValue(c2, c3);
    }

    public static int getFCD16(int i2) {
        return FCDTrieImpl.fcdTrie.getCodePointValue(i2);
    }

    private static int getExtraDataIndex(long j2) {
        return (int) (j2 >> 16);
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$DecomposeArgs.class */
    private static final class DecomposeArgs {
        int cc;
        int trailCC;
        int length;

        private DecomposeArgs() {
        }
    }

    private static int decompose(long j2, int i2, DecomposeArgs decomposeArgs) {
        int extraDataIndex = getExtraDataIndex(j2);
        int i3 = extraDataIndex + 1;
        decomposeArgs.length = extraData[extraDataIndex];
        if ((j2 & i2 & 8) != 0 && decomposeArgs.length >= 256) {
            i3 += ((decomposeArgs.length >> 7) & 1) + (decomposeArgs.length & 127);
            decomposeArgs.length >>= 8;
        }
        if ((decomposeArgs.length & 128) > 0) {
            int i4 = i3;
            i3++;
            char c2 = extraData[i4];
            decomposeArgs.cc = 255 & (c2 >> '\b');
            decomposeArgs.trailCC = 255 & c2;
        } else {
            decomposeArgs.trailCC = 0;
            decomposeArgs.cc = 0;
        }
        decomposeArgs.length &= 127;
        return i3;
    }

    private static int decompose(long j2, DecomposeArgs decomposeArgs) {
        int extraDataIndex = getExtraDataIndex(j2);
        int i2 = extraDataIndex + 1;
        decomposeArgs.length = extraData[extraDataIndex];
        if ((decomposeArgs.length & 128) > 0) {
            i2++;
            char c2 = extraData[i2];
            decomposeArgs.cc = 255 & (c2 >> '\b');
            decomposeArgs.trailCC = 255 & c2;
        } else {
            decomposeArgs.trailCC = 0;
            decomposeArgs.cc = 0;
        }
        decomposeArgs.length &= 127;
        return i2;
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$NextCCArgs.class */
    private static final class NextCCArgs {
        char[] source;
        int next;
        int limit;

        /* renamed from: c, reason: collision with root package name */
        char f13678c;
        char c2;

        private NextCCArgs() {
        }
    }

    private static int getNextCC(NextCCArgs nextCCArgs) {
        char[] cArr = nextCCArgs.source;
        int i2 = nextCCArgs.next;
        nextCCArgs.next = i2 + 1;
        nextCCArgs.f13678c = cArr[i2];
        long norm32 = getNorm32(nextCCArgs.f13678c);
        if ((norm32 & 65280) == 0) {
            nextCCArgs.c2 = (char) 0;
            return 0;
        }
        if (!isNorm32LeadSurrogate(norm32)) {
            nextCCArgs.c2 = (char) 0;
        } else {
            if (nextCCArgs.next != nextCCArgs.limit) {
                char c2 = nextCCArgs.source[nextCCArgs.next];
                nextCCArgs.c2 = c2;
                if (UTF16.isTrailSurrogate(c2)) {
                    nextCCArgs.next++;
                    norm32 = getNorm32FromSurrogatePair(norm32, nextCCArgs.c2);
                }
            }
            nextCCArgs.c2 = (char) 0;
            return 0;
        }
        return (int) (255 & (norm32 >> 8));
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$PrevArgs.class */
    private static final class PrevArgs {
        char[] src;
        int start;
        int current;

        /* renamed from: c, reason: collision with root package name */
        char f13680c;
        char c2;

        private PrevArgs() {
        }
    }

    private static long getPrevNorm32(PrevArgs prevArgs, int i2, int i3) {
        char[] cArr = prevArgs.src;
        int i4 = prevArgs.current - 1;
        prevArgs.current = i4;
        prevArgs.f13680c = cArr[i4];
        prevArgs.c2 = (char) 0;
        if (prevArgs.f13680c < i2) {
            return 0L;
        }
        if (!UTF16.isSurrogate(prevArgs.f13680c)) {
            return getNorm32(prevArgs.f13680c);
        }
        if (UTF16.isLeadSurrogate(prevArgs.f13680c)) {
            return 0L;
        }
        if (prevArgs.current != prevArgs.start) {
            char c2 = prevArgs.src[prevArgs.current - 1];
            prevArgs.c2 = c2;
            if (UTF16.isLeadSurrogate(c2)) {
                prevArgs.current--;
                long norm32 = getNorm32(prevArgs.c2);
                if ((norm32 & i3) == 0) {
                    return 0L;
                }
                return getNorm32FromSurrogatePair(norm32, prevArgs.f13680c);
            }
        }
        prevArgs.c2 = (char) 0;
        return 0L;
    }

    private static int getPrevCC(PrevArgs prevArgs) {
        return (int) (255 & (getPrevNorm32(prevArgs, 768, CC_MASK) >> 8));
    }

    public static boolean isNFDSafe(long j2, int i2, int i3) {
        if ((j2 & i2) == 0) {
            return true;
        }
        if (!isNorm32Regular(j2) || (j2 & i3) == 0) {
            return (j2 & 65280) == 0;
        }
        DecomposeArgs decomposeArgs = new DecomposeArgs();
        decompose(j2, i3, decomposeArgs);
        return decomposeArgs.cc == 0;
    }

    public static boolean isTrueStarter(long j2, int i2, int i3) {
        if ((j2 & i2) == 0) {
            return true;
        }
        if ((j2 & i3) != 0) {
            DecomposeArgs decomposeArgs = new DecomposeArgs();
            int iDecompose = decompose(j2, i3, decomposeArgs);
            if (decomposeArgs.cc == 0) {
                int i4 = i2 & 63;
                if ((getNorm32(extraData, iDecompose, i4) & i4) == 0) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private static int insertOrdered(char[] cArr, int i2, int i3, int i4, char c2, char c3, int i5) {
        int i6;
        int prevCC;
        int i7 = i5;
        if (i2 < i3 && i5 != 0) {
            PrevArgs prevArgs = new PrevArgs();
            prevArgs.current = i3;
            prevArgs.start = i2;
            prevArgs.src = cArr;
            int prevCC2 = getPrevCC(prevArgs);
            int i8 = prevArgs.current;
            if (i5 < prevCC2) {
                i7 = prevCC2;
                do {
                    i6 = i8;
                    if (i2 >= i8) {
                        break;
                    }
                    prevCC = getPrevCC(prevArgs);
                    i8 = prevArgs.current;
                } while (i5 < prevCC);
                int i9 = i4;
                do {
                    i9--;
                    i3--;
                    cArr[i9] = cArr[i3];
                } while (i6 != i3);
            }
        }
        cArr[i3] = c2;
        if (c3 != 0) {
            cArr[i3 + 1] = c3;
        }
        return i7;
    }

    private static int mergeOrdered(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, boolean z2) {
        int iInsertOrdered = 0;
        boolean z3 = i3 == i4;
        NextCCArgs nextCCArgs = new NextCCArgs();
        nextCCArgs.source = cArr2;
        nextCCArgs.next = i4;
        nextCCArgs.limit = i5;
        if (i2 != i3 || !z2) {
            while (nextCCArgs.next < nextCCArgs.limit) {
                int nextCC = getNextCC(nextCCArgs);
                if (nextCC == 0) {
                    iInsertOrdered = 0;
                    if (z3) {
                        i3 = nextCCArgs.next;
                    } else {
                        int i6 = i3;
                        i3++;
                        cArr2[i6] = nextCCArgs.f13678c;
                        if (nextCCArgs.c2 != 0) {
                            i3++;
                            cArr2[i3] = nextCCArgs.c2;
                        }
                    }
                    if (z2) {
                        break;
                    }
                    i2 = i3;
                } else {
                    int i7 = i3 + (nextCCArgs.c2 == 0 ? 1 : 2);
                    iInsertOrdered = insertOrdered(cArr, i2, i3, i7, nextCCArgs.f13678c, nextCCArgs.c2, nextCC);
                    i3 = i7;
                }
            }
        }
        if (nextCCArgs.next == nextCCArgs.limit) {
            return iInsertOrdered;
        }
        if (!z3) {
            do {
                int i8 = i3;
                i3++;
                int i9 = nextCCArgs.next;
                nextCCArgs.next = i9 + 1;
                cArr[i8] = cArr2[i9];
            } while (nextCCArgs.next != nextCCArgs.limit);
            nextCCArgs.limit = i3;
        }
        PrevArgs prevArgs = new PrevArgs();
        prevArgs.src = cArr2;
        prevArgs.start = i2;
        prevArgs.current = nextCCArgs.limit;
        return getPrevCC(prevArgs);
    }

    private static int mergeOrdered(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5) {
        return mergeOrdered(cArr, i2, i3, cArr2, i4, i5, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x007d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static sun.text.normalizer.NormalizerBase.QuickCheckResult quickCheck(char[] r9, int r10, int r11, int r12, int r13, int r14, boolean r15, sun.text.normalizer.UnicodeSet r16) {
        /*
            Method dump skipped, instructions count: 345
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.text.normalizer.NormalizerImpl.quickCheck(char[], int, int, int, int, int, boolean, sun.text.normalizer.UnicodeSet):sun.text.normalizer.NormalizerBase$QuickCheckResult");
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0164  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int decompose(char[] r8, int r9, int r10, char[] r11, int r12, int r13, boolean r14, int[] r15, sun.text.normalizer.UnicodeSet r16) {
        /*
            Method dump skipped, instructions count: 680
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.text.normalizer.NormalizerImpl.decompose(char[], int, int, char[], int, int, boolean, int[], sun.text.normalizer.UnicodeSet):int");
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$NextCombiningArgs.class */
    private static final class NextCombiningArgs {
        char[] source;
        int start;

        /* renamed from: c, reason: collision with root package name */
        char f13679c;
        char c2;
        int combiningIndex;
        char cc;

        private NextCombiningArgs() {
        }
    }

    private static int getNextCombining(NextCombiningArgs nextCombiningArgs, int i2, UnicodeSet unicodeSet) {
        char[] cArr = nextCombiningArgs.source;
        int i3 = nextCombiningArgs.start;
        nextCombiningArgs.start = i3 + 1;
        nextCombiningArgs.f13679c = cArr[i3];
        long norm32 = getNorm32(nextCombiningArgs.f13679c);
        nextCombiningArgs.c2 = (char) 0;
        nextCombiningArgs.combiningIndex = 0;
        nextCombiningArgs.cc = (char) 0;
        if ((norm32 & 65472) == 0) {
            return 0;
        }
        if (!isNorm32Regular(norm32)) {
            if (isNorm32HangulOrJamo(norm32)) {
                nextCombiningArgs.combiningIndex = (int) (4294967295L & (65520 | (norm32 >> 16)));
                return (int) (norm32 & 192);
            }
            if (nextCombiningArgs.start != i2) {
                char c2 = nextCombiningArgs.source[nextCombiningArgs.start];
                nextCombiningArgs.c2 = c2;
                if (UTF16.isTrailSurrogate(c2)) {
                    nextCombiningArgs.start++;
                    norm32 = getNorm32FromSurrogatePair(norm32, nextCombiningArgs.c2);
                }
            }
            nextCombiningArgs.c2 = (char) 0;
            return 0;
        }
        if (nx_contains(unicodeSet, nextCombiningArgs.f13679c, nextCombiningArgs.c2)) {
            return 0;
        }
        nextCombiningArgs.cc = (char) ((norm32 >> 8) & 255);
        int i4 = (int) (norm32 & 192);
        if (i4 != 0) {
            int extraDataIndex = getExtraDataIndex(norm32);
            nextCombiningArgs.combiningIndex = extraDataIndex > 0 ? extraData[extraDataIndex - 1] : (char) 0;
        }
        return i4;
    }

    private static int getCombiningIndexFromStarter(char c2, char c3) {
        long norm32 = getNorm32(c2);
        if (c3 != 0) {
            norm32 = getNorm32FromSurrogatePair(norm32, c3);
        }
        return extraData[getExtraDataIndex(norm32) - 1];
    }

    private static int combine(char[] cArr, int i2, int i3, int[] iArr) {
        int i4;
        char c2;
        int i5;
        char c3;
        if (iArr.length < 2) {
            throw new IllegalArgumentException();
        }
        while (true) {
            int i6 = i2;
            i4 = i2 + 1;
            c2 = cArr[i6];
            if (c2 >= i3) {
                break;
            }
            i2 = i4 + ((cArr[i4] & 32768) != 0 ? 2 : 1);
        }
        if ((c2 & 32767) == i3) {
            char c4 = cArr[i4];
            int i7 = (int) (4294967295L & ((c4 & 8192) + 1));
            if ((c4 & 32768) != 0) {
                if ((c4 & 16384) != 0) {
                    i5 = (int) (4294967295L & ((c4 & 1023) | 55296));
                    c3 = cArr[i4 + 1];
                } else {
                    i5 = cArr[i4 + 1];
                    c3 = 0;
                }
            } else {
                i5 = c4 & 8191;
                c3 = 0;
            }
            iArr[0] = i5;
            iArr[1] = c3;
            return i7;
        }
        return 0;
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$RecomposeArgs.class */
    private static final class RecomposeArgs {
        char[] source;
        int start;
        int limit;

        private RecomposeArgs() {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x021b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static char recompose(sun.text.normalizer.NormalizerImpl.RecomposeArgs r6, int r7, sun.text.normalizer.UnicodeSet r8) {
        /*
            Method dump skipped, instructions count: 911
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.text.normalizer.NormalizerImpl.recompose(sun.text.normalizer.NormalizerImpl$RecomposeArgs, int, sun.text.normalizer.UnicodeSet):char");
    }

    private static int findPreviousStarter(char[] cArr, int i2, int i3, int i4, int i5, char c2) {
        PrevArgs prevArgs = new PrevArgs();
        prevArgs.src = cArr;
        prevArgs.start = i2;
        prevArgs.current = i3;
        while (prevArgs.start < prevArgs.current && !isTrueStarter(getPrevNorm32(prevArgs, c2, i4 | i5), i4, i5)) {
        }
        return prevArgs.current;
    }

    private static int findNextStarter(char[] cArr, int i2, int i3, int i4, int i5, char c2) {
        char c3;
        char c4;
        int i6 = 65280 | i4;
        DecomposeArgs decomposeArgs = new DecomposeArgs();
        while (i2 != i3 && (c3 = cArr[i2]) >= c2) {
            long norm32 = getNorm32(c3);
            if ((norm32 & i6) == 0) {
                break;
            }
            if (isNorm32LeadSurrogate(norm32)) {
                if (i2 + 1 == i3) {
                    break;
                }
                char c5 = cArr[i2 + 1];
                c4 = c5;
                if (!UTF16.isTrailSurrogate(c5)) {
                    break;
                }
                norm32 = getNorm32FromSurrogatePair(norm32, c4);
                if ((norm32 & i6) != 0) {
                    break;
                    break;
                }
                break;
            }
            c4 = 0;
            if ((norm32 & i5) != 0) {
                int iDecompose = decompose(norm32, i5, decomposeArgs);
                if (decomposeArgs.cc == 0 && (getNorm32(extraData, iDecompose, i4) & i4) == 0) {
                    break;
                }
            }
            i2 += c4 == 0 ? 1 : 2;
        }
        return i2;
    }

    /* loaded from: rt.jar:sun/text/normalizer/NormalizerImpl$ComposePartArgs.class */
    private static final class ComposePartArgs {
        int prevCC;
        int length;

        private ComposePartArgs() {
        }
    }

    private static char[] composePart(ComposePartArgs composePartArgs, int i2, char[] cArr, int i3, int i4, int i5, UnicodeSet unicodeSet) {
        char[] cArr2;
        boolean z2 = (i5 & 4096) != 0;
        int[] iArr = new int[1];
        char[] cArr3 = new char[(i4 - i2) * 20];
        while (true) {
            cArr2 = cArr3;
            composePartArgs.length = decompose(cArr, i2, i3, cArr2, 0, cArr2.length, z2, iArr, unicodeSet);
            if (composePartArgs.length <= cArr2.length) {
                break;
            }
            cArr3 = new char[composePartArgs.length];
        }
        int i6 = composePartArgs.length;
        if (composePartArgs.length >= 2) {
            RecomposeArgs recomposeArgs = new RecomposeArgs();
            recomposeArgs.source = cArr2;
            recomposeArgs.start = 0;
            recomposeArgs.limit = i6;
            composePartArgs.prevCC = recompose(recomposeArgs, i5, unicodeSet);
            i6 = recomposeArgs.limit;
        }
        composePartArgs.length = i6;
        return cArr2;
    }

    private static boolean composeHangul(char c2, char c3, long j2, char[] cArr, int[] iArr, int i2, boolean z2, char[] cArr2, int i3, UnicodeSet unicodeSet) {
        char c4;
        int i4 = iArr[0];
        if (isJamoVTNorm32JamoV(j2)) {
            char c5 = (char) (c2 - JAMO_L_BASE);
            if (c5 < 19) {
                char c6 = (char) (HANGUL_BASE + (((c5 * 21) + (c3 - JAMO_V_BASE)) * 28));
                if (i4 != i2) {
                    char c7 = cArr[i4];
                    char c8 = (char) (c7 - JAMO_T_BASE);
                    if (c8 < 28) {
                        i4++;
                        c6 = (char) (c6 + c8);
                    } else if (z2) {
                        long norm32 = getNorm32(c7);
                        if (isNorm32Regular(norm32) && (norm32 & 8) != 0) {
                            DecomposeArgs decomposeArgs = new DecomposeArgs();
                            int iDecompose = decompose(norm32, 8, decomposeArgs);
                            if (decomposeArgs.length == 1 && (c4 = (char) (extraData[iDecompose] - JAMO_T_BASE)) < 28) {
                                i4++;
                                c6 = (char) (c6 + c4);
                            }
                        }
                    }
                }
                if (nx_contains(unicodeSet, c6)) {
                    if (!isHangulWithoutJamoT(c6)) {
                        int i5 = i4 - 1;
                        return false;
                    }
                    return false;
                }
                cArr2[i3] = c6;
                iArr[0] = i4;
                return true;
            }
            return false;
        }
        if (isHangulWithoutJamoT(c2)) {
            char c9 = (char) (c2 + (c3 - JAMO_T_BASE));
            if (nx_contains(unicodeSet, c9)) {
                return false;
            }
            cArr2[i3] = c9;
            iArr[0] = i4;
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x027a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x02c4 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int compose(char[] r12, int r13, int r14, char[] r15, int r16, int r17, int r18, sun.text.normalizer.UnicodeSet r19) {
        /*
            Method dump skipped, instructions count: 728
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.text.normalizer.NormalizerImpl.compose(char[], int, int, char[], int, int, int, sun.text.normalizer.UnicodeSet):int");
    }

    public static int getCombiningClass(int i2) {
        return (int) ((getNorm32(i2) >> 8) & 255);
    }

    public static boolean isFullCompositionExclusion(int i2) {
        return isFormatVersion_2_1 && (AuxTrieImpl.auxTrie.getCodePointValue(i2) & 1024) != 0;
    }

    public static boolean isCanonSafeStart(int i2) {
        return isFormatVersion_2_1 && (AuxTrieImpl.auxTrie.getCodePointValue(i2) & 2048) == 0;
    }

    public static boolean isNFSkippable(int i2, NormalizerBase.Mode mode, long j2) {
        long norm32 = getNorm32(i2);
        if ((norm32 & j2 & 4294967295L) != 0) {
            return false;
        }
        if (mode == NormalizerBase.NFD || mode == NormalizerBase.NFKD || mode == NormalizerBase.NONE || (norm32 & 4) == 0) {
            return true;
        }
        return isNorm32HangulOrJamo(norm32) ? !isHangulWithoutJamoT((char) i2) : isFormatVersion_2_2 && (((long) AuxTrieImpl.auxTrie.getCodePointValue(i2)) & 4096) == 0;
    }

    public static UnicodeSet addPropertyStarts(UnicodeSet unicodeSet) {
        TrieIterator trieIterator = new TrieIterator(NormTrieImpl.normTrie);
        RangeValueIterator.Element element = new RangeValueIterator.Element();
        while (trieIterator.next(element)) {
            unicodeSet.add(element.start);
        }
        TrieIterator trieIterator2 = new TrieIterator(FCDTrieImpl.fcdTrie);
        RangeValueIterator.Element element2 = new RangeValueIterator.Element();
        while (trieIterator2.next(element2)) {
            unicodeSet.add(element2.start);
        }
        if (isFormatVersion_2_1) {
            TrieIterator trieIterator3 = new TrieIterator(AuxTrieImpl.auxTrie);
            RangeValueIterator.Element element3 = new RangeValueIterator.Element();
            while (trieIterator3.next(element3)) {
                unicodeSet.add(element3.start);
            }
        }
        for (int i2 = 44032; i2 < 55204; i2 += 28) {
            unicodeSet.add(i2);
            unicodeSet.add(i2 + 1);
        }
        unicodeSet.add(55204);
        return unicodeSet;
    }

    public static final int quickCheck(int i2, int i3) {
        int norm32 = ((int) getNorm32(i2)) & new int[]{0, 0, 4, 8, 17, 34}[i3];
        if (norm32 == 0) {
            return 1;
        }
        if ((norm32 & 15) != 0) {
            return 0;
        }
        return 2;
    }

    private static int strCompare(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, boolean z2) {
        int i6;
        int i7;
        int i8 = i3 - i2;
        int i9 = i5 - i4;
        if (i8 < i9) {
            i6 = -1;
            i7 = i2 + i8;
        } else if (i8 == i9) {
            i6 = 0;
            i7 = i2 + i8;
        } else {
            i6 = 1;
            i7 = i2 + i9;
        }
        if (cArr == cArr2) {
            return i6;
        }
        while (i2 != i7) {
            char c2 = cArr[i2];
            char c3 = cArr2[i4];
            if (c2 == c3) {
                i2++;
                i4++;
            } else {
                int i10 = i2 + i8;
                int i11 = i4 + i9;
                if (c2 >= 55296 && c3 >= 55296 && z2) {
                    if ((c2 > 56319 || i2 + 1 == i10 || !UTF16.isTrailSurrogate(cArr[i2 + 1])) && (!UTF16.isTrailSurrogate(c2) || i2 == i2 || !UTF16.isLeadSurrogate(cArr[i2 - 1]))) {
                        c2 = (char) (c2 - 10240);
                    }
                    if ((c3 > 56319 || i4 + 1 == i11 || !UTF16.isTrailSurrogate(cArr2[i4 + 1])) && (!UTF16.isTrailSurrogate(c3) || i4 == i4 || !UTF16.isLeadSurrogate(cArr2[i4 - 1]))) {
                        c3 = (char) (c3 - 10240);
                    }
                }
                return c2 - c3;
            }
        }
        return i6;
    }

    private static final synchronized UnicodeSet internalGetNXHangul() {
        if (nxCache[1] == null) {
            nxCache[1] = new UnicodeSet(HANGUL_BASE, 55203);
        }
        return nxCache[1];
    }

    private static final synchronized UnicodeSet internalGetNXCJKCompat() {
        if (nxCache[2] == null) {
            UnicodeSet unicodeSet = new UnicodeSet("[:Ideographic:]");
            UnicodeSet unicodeSet2 = new UnicodeSet();
            UnicodeSetIterator unicodeSetIterator = new UnicodeSetIterator(unicodeSet);
            while (unicodeSetIterator.nextRange() && unicodeSetIterator.codepoint != UnicodeSetIterator.IS_STRING) {
                int i2 = unicodeSetIterator.codepointEnd;
                for (int i3 = unicodeSetIterator.codepoint; i3 <= i2; i3++) {
                    if ((getNorm32(i3) & 4) > 0) {
                        unicodeSet2.add(i3);
                    }
                }
            }
            nxCache[2] = unicodeSet2;
        }
        return nxCache[2];
    }

    private static final synchronized UnicodeSet internalGetNXUnicode(int i2) {
        int i3 = i2 & 224;
        if (i3 == 0) {
            return null;
        }
        if (nxCache[i3] == null) {
            UnicodeSet unicodeSet = new UnicodeSet();
            switch (i3) {
                case 32:
                    unicodeSet.applyPattern("[:^Age=3.2:]");
                    nxCache[i3] = unicodeSet;
                    break;
                default:
                    return null;
            }
        }
        return nxCache[i3];
    }

    private static final synchronized UnicodeSet internalGetNX(int i2) {
        UnicodeSet unicodeSetInternalGetNXUnicode;
        UnicodeSet unicodeSetInternalGetNXCJKCompat;
        UnicodeSet unicodeSetInternalGetNXHangul;
        int i3 = i2 & 255;
        if (nxCache[i3] == null) {
            if (i3 == 1) {
                return internalGetNXHangul();
            }
            if (i3 == 2) {
                return internalGetNXCJKCompat();
            }
            if ((i3 & 224) != 0 && (i3 & 31) == 0) {
                return internalGetNXUnicode(i3);
            }
            UnicodeSet unicodeSet = new UnicodeSet();
            if ((i3 & 1) != 0 && null != (unicodeSetInternalGetNXHangul = internalGetNXHangul())) {
                unicodeSet.addAll(unicodeSetInternalGetNXHangul);
            }
            if ((i3 & 2) != 0 && null != (unicodeSetInternalGetNXCJKCompat = internalGetNXCJKCompat())) {
                unicodeSet.addAll(unicodeSetInternalGetNXCJKCompat);
            }
            if ((i3 & 224) != 0 && null != (unicodeSetInternalGetNXUnicode = internalGetNXUnicode(i3))) {
                unicodeSet.addAll(unicodeSetInternalGetNXUnicode);
            }
            nxCache[i3] = unicodeSet;
        }
        return nxCache[i3];
    }

    public static final UnicodeSet getNX(int i2) {
        int i3 = i2 & 255;
        if (i3 == 0) {
            return null;
        }
        return internalGetNX(i3);
    }

    private static final boolean nx_contains(UnicodeSet unicodeSet, int i2) {
        return unicodeSet != null && unicodeSet.contains(i2);
    }

    private static final boolean nx_contains(UnicodeSet unicodeSet, char c2, char c3) {
        if (unicodeSet != null) {
            if (unicodeSet.contains(c3 == 0 ? c2 : UCharacterProperty.getRawSupplementary(c2, c3))) {
                return true;
            }
        }
        return false;
    }

    public static int getDecompose(int[] iArr, String[] strArr) {
        DecomposeArgs decomposeArgs = new DecomposeArgs();
        int i2 = -1;
        int i3 = 0;
        while (true) {
            i2++;
            if (i2 < 195102) {
                if (i2 == 12543) {
                    i2 = 63744;
                } else if (i2 == 65536) {
                    i2 = 119134;
                } else if (i2 == 119233) {
                    i2 = 194560;
                }
                long norm32 = getNorm32(i2);
                if ((norm32 & 4) != 0 && i3 < iArr.length) {
                    iArr[i3] = i2;
                    int i4 = i3;
                    i3++;
                    strArr[i4] = new String(extraData, decompose(norm32, decomposeArgs), decomposeArgs.length);
                }
            } else {
                return i3;
            }
        }
    }

    private static boolean needSingleQuotation(char c2) {
        return (c2 >= '\t' && c2 <= '\r') || (c2 >= ' ' && c2 <= '/') || ((c2 >= ':' && c2 <= '@') || ((c2 >= '[' && c2 <= '`') || (c2 >= '{' && c2 <= '~')));
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00ee  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String canonicalDecomposeWithSingleQuotation(java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 685
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.text.normalizer.NormalizerImpl.canonicalDecomposeWithSingleQuotation(java.lang.String):java.lang.String");
    }

    public static String convert(String str) {
        if (str == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        UCharacterIterator uCharacterIterator = UCharacterIterator.getInstance(str);
        while (true) {
            int iNextCodePoint = uCharacterIterator.nextCodePoint();
            if (iNextCodePoint != -1) {
                switch (iNextCodePoint) {
                    case 194664:
                        stringBuffer.append(corrigendum4MappingTable[0]);
                        break;
                    case 194676:
                        stringBuffer.append(corrigendum4MappingTable[1]);
                        break;
                    case 194847:
                        stringBuffer.append(corrigendum4MappingTable[2]);
                        break;
                    case 194911:
                        stringBuffer.append(corrigendum4MappingTable[3]);
                        break;
                    case 195007:
                        stringBuffer.append(corrigendum4MappingTable[4]);
                        break;
                    default:
                        UTF16.append(stringBuffer, iNextCodePoint);
                        break;
                }
            } else {
                return stringBuffer.toString();
            }
        }
    }
}
