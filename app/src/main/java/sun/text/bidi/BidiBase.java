package sun.text.bidi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.text.Bidi;
import java.util.Arrays;
import java.util.MissingResourceException;
import org.icepdf.core.pobjects.graphics.Separation;
import sun.text.normalizer.UBiDiProps;
import sun.text.normalizer.UCharacter;
import sun.text.normalizer.UTF16;

/* loaded from: rt.jar:sun/text/bidi/BidiBase.class */
public class BidiBase {
    public static final byte INTERNAL_LEVEL_DEFAULT_LTR = 126;
    public static final byte INTERNAL_LEVEL_DEFAULT_RTL = Byte.MAX_VALUE;
    public static final byte MAX_EXPLICIT_LEVEL = 61;
    public static final byte INTERNAL_LEVEL_OVERRIDE = Byte.MIN_VALUE;
    public static final int MAP_NOWHERE = -1;
    public static final byte MIXED = 2;
    public static final short DO_MIRRORING = 2;
    private static final short REORDER_DEFAULT = 0;
    private static final short REORDER_NUMBERS_SPECIAL = 1;
    private static final short REORDER_GROUP_NUMBERS_WITH_R = 2;
    private static final short REORDER_RUNS_ONLY = 3;
    private static final short REORDER_INVERSE_NUMBERS_AS_L = 4;
    private static final short REORDER_INVERSE_LIKE_DIRECT = 5;
    private static final short REORDER_INVERSE_FOR_NUMBERS_SPECIAL = 6;
    private static final short REORDER_LAST_LOGICAL_TO_VISUAL = 1;
    private static final int OPTION_INSERT_MARKS = 1;
    private static final int OPTION_REMOVE_CONTROLS = 2;
    private static final int OPTION_STREAMING = 4;

    /* renamed from: L, reason: collision with root package name */
    private static final byte f13674L = 0;

    /* renamed from: R, reason: collision with root package name */
    private static final byte f13675R = 1;
    private static final byte EN = 2;
    private static final byte ES = 3;
    private static final byte ET = 4;
    private static final byte AN = 5;
    private static final byte CS = 6;

    /* renamed from: B, reason: collision with root package name */
    static final byte f13676B = 7;

    /* renamed from: S, reason: collision with root package name */
    private static final byte f13677S = 8;
    private static final byte WS = 9;
    private static final byte ON = 10;
    private static final byte LRE = 11;
    private static final byte LRO = 12;
    private static final byte AL = 13;
    private static final byte RLE = 14;
    private static final byte RLO = 15;
    private static final byte PDF = 16;
    private static final byte NSM = 17;
    private static final byte BN = 18;
    private static final int MASK_R_AL = 8194;
    private static final char CR = '\r';
    private static final char LF = '\n';
    static final int LRM_BEFORE = 1;
    static final int LRM_AFTER = 2;
    static final int RLM_BEFORE = 4;
    static final int RLM_AFTER = 8;
    BidiBase paraBidi;
    final UBiDiProps bdp;
    char[] text;
    int originalLength;
    public int length;
    int resultLength;
    boolean mayAllocateText;
    boolean mayAllocateRuns;
    byte[] dirPropsMemory;
    byte[] levelsMemory;
    byte[] dirProps;
    byte[] levels;
    boolean orderParagraphsLTR;
    byte paraLevel;
    byte defaultParaLevel;
    ImpTabPair impTabPair;
    byte direction;
    int flags;
    int lastArabicPos;
    int trailingWSStart;
    int paraCount;
    int[] parasMemory;
    int[] paras;
    int[] simpleParas;
    int runCount;
    BidiRun[] runsMemory;
    BidiRun[] runs;
    BidiRun[] simpleRuns;
    int[] logicalToVisualRunsMap;
    boolean isGoodLogicalToVisualRunsMap;
    InsertPoints insertPoints;
    int controlCount;
    static final byte CONTEXT_RTL_SHIFT = 6;
    static final byte CONTEXT_RTL = 64;
    private static final int IMPTABPROPS_COLUMNS = 14;
    private static final int IMPTABPROPS_RES = 13;
    private static final short _L = 0;
    private static final short _R = 1;
    private static final short _EN = 2;
    private static final short _AN = 3;
    private static final short _ON = 4;
    private static final short _S = 5;
    private static final short _B = 6;
    private static final int IMPTABLEVELS_COLUMNS = 8;
    private static final int IMPTABLEVELS_RES = 7;
    static final int FIRSTALLOC = 10;
    private static final int INTERNAL_DIRECTION_DEFAULT_LEFT_TO_RIGHT = 126;
    private static final int INTERMAL_DIRECTION_DEFAULT_RIGHT_TO_LEFT = 127;
    static final int DirPropFlagMultiRuns = DirPropFlag((byte) 31);
    static final int[] DirPropFlagLR = {DirPropFlag((byte) 0), DirPropFlag((byte) 1)};
    static final int[] DirPropFlagE = {DirPropFlag((byte) 11), DirPropFlag((byte) 14)};
    static final int[] DirPropFlagO = {DirPropFlag((byte) 12), DirPropFlag((byte) 15)};
    static final int MASK_LTR = (((DirPropFlag((byte) 0) | DirPropFlag((byte) 2)) | DirPropFlag((byte) 5)) | DirPropFlag((byte) 11)) | DirPropFlag((byte) 12);
    static final int MASK_RTL = ((DirPropFlag((byte) 1) | DirPropFlag((byte) 13)) | DirPropFlag((byte) 14)) | DirPropFlag((byte) 15);
    private static final int MASK_LRX = DirPropFlag((byte) 11) | DirPropFlag((byte) 12);
    private static final int MASK_RLX = DirPropFlag((byte) 14) | DirPropFlag((byte) 15);
    private static final int MASK_EXPLICIT = (MASK_LRX | MASK_RLX) | DirPropFlag((byte) 16);
    private static final int MASK_BN_EXPLICIT = DirPropFlag((byte) 18) | MASK_EXPLICIT;
    private static final int MASK_B_S = DirPropFlag((byte) 7) | DirPropFlag((byte) 8);
    static final int MASK_WS = (MASK_B_S | DirPropFlag((byte) 9)) | MASK_BN_EXPLICIT;
    private static final int MASK_N = DirPropFlag((byte) 10) | MASK_WS;
    private static final int MASK_POSSIBLE_N = ((DirPropFlag((byte) 6) | DirPropFlag((byte) 3)) | DirPropFlag((byte) 4)) | MASK_N;
    static final int MASK_EMBEDDING = DirPropFlag((byte) 17) | MASK_POSSIBLE_N;
    private static final short[] groupProp = {0, 1, 2, 7, 8, 3, 9, 6, 5, 4, 4, 10, 10, 12, 10, 10, 10, 11, 10};
    private static final short[][] impTabProps = {new short[]{1, 2, 4, 5, 7, 15, 17, 7, 9, 7, 0, 7, 3, 4}, new short[]{1, 34, 36, 37, 39, 47, 49, 39, 41, 39, 1, 1, 35, 0}, new short[]{33, 2, 36, 37, 39, 47, 49, 39, 41, 39, 2, 2, 35, 1}, new short[]{33, 34, 38, 38, 40, 48, 49, 40, 40, 40, 3, 3, 3, 1}, new short[]{33, 34, 4, 37, 39, 47, 49, 74, 11, 74, 4, 4, 35, 2}, new short[]{33, 34, 36, 5, 39, 47, 49, 39, 41, 76, 5, 5, 35, 3}, new short[]{33, 34, 6, 6, 40, 48, 49, 40, 40, 77, 6, 6, 35, 3}, new short[]{33, 34, 36, 37, 7, 47, 49, 7, 78, 7, 7, 7, 35, 4}, new short[]{33, 34, 38, 38, 8, 48, 49, 8, 8, 8, 8, 8, 35, 4}, new short[]{33, 34, 4, 37, 7, 47, 49, 7, 9, 7, 9, 9, 35, 4}, new short[]{97, 98, 4, 101, 135, 111, 113, 135, 142, 135, 10, 135, 99, 2}, new short[]{33, 34, 4, 37, 39, 47, 49, 39, 11, 39, 11, 11, 35, 2}, new short[]{97, 98, 100, 5, 135, 111, 113, 135, 142, 135, 12, 135, 99, 3}, new short[]{97, 98, 6, 6, 136, 112, 113, 136, 136, 136, 13, 136, 99, 3}, new short[]{33, 34, 132, 37, 7, 47, 49, 7, 14, 7, 14, 14, 35, 4}, new short[]{33, 34, 36, 37, 39, 15, 49, 39, 41, 39, 15, 39, 35, 5}, new short[]{33, 34, 38, 38, 40, 16, 49, 40, 40, 40, 16, 40, 35, 5}, new short[]{33, 34, 36, 37, 39, 47, 17, 39, 41, 39, 17, 39, 35, 6}};
    private static final byte[][] impTabL_DEFAULT = {new byte[]{0, 1, 0, 2, 0, 0, 0, 0}, new byte[]{0, 1, 3, 3, 20, 20, 0, 1}, new byte[]{0, 1, 0, 2, 21, 21, 0, 2}, new byte[]{0, 1, 3, 3, 20, 20, 0, 2}, new byte[]{32, 1, 3, 3, 4, 4, 32, 1}, new byte[]{32, 1, 32, 2, 5, 5, 32, 1}};
    private static final byte[][] impTabR_DEFAULT = {new byte[]{1, 0, 2, 2, 0, 0, 0, 0}, new byte[]{1, 0, 1, 3, 20, 20, 0, 1}, new byte[]{1, 0, 2, 2, 0, 0, 0, 1}, new byte[]{1, 0, 1, 3, 5, 5, 0, 1}, new byte[]{33, 0, 33, 3, 4, 4, 0, 0}, new byte[]{1, 0, 1, 3, 5, 5, 0, 0}};
    private static final short[] impAct0 = {0, 1, 2, 3, 4, 5, 6};
    private static final ImpTabPair impTab_DEFAULT = new ImpTabPair(impTabL_DEFAULT, impTabR_DEFAULT, impAct0, impAct0);
    private static final byte[][] impTabL_NUMBERS_SPECIAL = {new byte[]{0, 2, 1, 1, 0, 0, 0, 0}, new byte[]{0, 2, 1, 1, 0, 0, 0, 2}, new byte[]{0, 2, 4, 4, 19, 0, 0, 1}, new byte[]{32, 2, 4, 4, 3, 3, 32, 1}, new byte[]{0, 2, 4, 4, 19, 19, 0, 2}};
    private static final ImpTabPair impTab_NUMBERS_SPECIAL = new ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_DEFAULT, impAct0, impAct0);
    private static final byte[][] impTabL_GROUP_NUMBERS_WITH_R = {new byte[]{0, 3, 17, 17, 0, 0, 0, 0}, new byte[]{32, 3, 1, 1, 2, 32, 32, 2}, new byte[]{32, 3, 1, 1, 2, 32, 32, 1}, new byte[]{0, 3, 5, 5, 20, 0, 0, 1}, new byte[]{32, 3, 5, 5, 4, 32, 32, 1}, new byte[]{0, 3, 5, 5, 20, 0, 0, 2}};
    private static final byte[][] impTabR_GROUP_NUMBERS_WITH_R = {new byte[]{2, 0, 1, 1, 0, 0, 0, 0}, new byte[]{2, 0, 1, 1, 0, 0, 0, 1}, new byte[]{2, 0, 20, 20, 19, 0, 0, 1}, new byte[]{34, 0, 4, 4, 3, 0, 0, 0}, new byte[]{34, 0, 4, 4, 3, 0, 0, 1}};
    private static final ImpTabPair impTab_GROUP_NUMBERS_WITH_R = new ImpTabPair(impTabL_GROUP_NUMBERS_WITH_R, impTabR_GROUP_NUMBERS_WITH_R, impAct0, impAct0);
    private static final byte[][] impTabL_INVERSE_NUMBERS_AS_L = {new byte[]{0, 1, 0, 0, 0, 0, 0, 0}, new byte[]{0, 1, 0, 0, 20, 20, 0, 1}, new byte[]{0, 1, 0, 0, 21, 21, 0, 2}, new byte[]{0, 1, 0, 0, 20, 20, 0, 2}, new byte[]{32, 1, 32, 32, 4, 4, 32, 1}, new byte[]{32, 1, 32, 32, 5, 5, 32, 1}};
    private static final byte[][] impTabR_INVERSE_NUMBERS_AS_L = {new byte[]{1, 0, 1, 1, 0, 0, 0, 0}, new byte[]{1, 0, 1, 1, 20, 20, 0, 1}, new byte[]{1, 0, 1, 1, 0, 0, 0, 1}, new byte[]{1, 0, 1, 1, 5, 5, 0, 1}, new byte[]{33, 0, 33, 33, 4, 4, 0, 0}, new byte[]{1, 0, 1, 1, 5, 5, 0, 0}};
    private static final ImpTabPair impTab_INVERSE_NUMBERS_AS_L = new ImpTabPair(impTabL_INVERSE_NUMBERS_AS_L, impTabR_INVERSE_NUMBERS_AS_L, impAct0, impAct0);
    private static final byte[][] impTabR_INVERSE_LIKE_DIRECT = {new byte[]{1, 0, 2, 2, 0, 0, 0, 0}, new byte[]{1, 0, 1, 2, 19, 19, 0, 1}, new byte[]{1, 0, 2, 2, 0, 0, 0, 1}, new byte[]{33, 48, 6, 4, 3, 3, 48, 0}, new byte[]{33, 48, 6, 4, 5, 5, 48, 3}, new byte[]{33, 48, 6, 4, 5, 5, 48, 2}, new byte[]{33, 48, 6, 4, 3, 3, 48, 1}};
    private static final short[] impAct1 = {0, 1, 11, 12};
    private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT = new ImpTabPair(impTabL_DEFAULT, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
    private static final byte[][] impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS = {new byte[]{0, 99, 0, 1, 0, 0, 0, 0}, new byte[]{0, 99, 0, 1, 18, 48, 0, 4}, new byte[]{32, 99, 32, 1, 2, 48, 32, 3}, new byte[]{0, 99, 85, 86, 20, 48, 0, 3}, new byte[]{48, 67, 85, 86, 4, 48, 48, 3}, new byte[]{48, 67, 5, 86, 20, 48, 48, 4}, new byte[]{48, 67, 85, 6, 20, 48, 48, 4}};
    private static final byte[][] impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS = {new byte[]{19, 0, 1, 1, 0, 0, 0, 0}, new byte[]{35, 0, 1, 1, 2, 64, 0, 1}, new byte[]{35, 0, 1, 1, 2, 64, 0, 0}, new byte[]{3, 0, 3, 54, 20, 64, 0, 1}, new byte[]{83, 64, 5, 54, 4, 64, 64, 0}, new byte[]{83, 64, 5, 54, 4, 64, 64, 1}, new byte[]{83, 64, 6, 6, 4, 64, 64, 3}};
    private static final short[] impAct2 = {0, 1, 7, 8, 9, 10};
    private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT_WITH_MARKS = new ImpTabPair(impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct0, impAct2);
    private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL = new ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
    private static final byte[][] impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = {new byte[]{0, 98, 1, 1, 0, 0, 0, 0}, new byte[]{0, 98, 1, 1, 0, 48, 0, 4}, new byte[]{0, 98, 84, 84, 19, 48, 0, 3}, new byte[]{48, 66, 84, 84, 3, 48, 48, 3}, new byte[]{48, 66, 4, 4, 19, 48, 48, 4}};
    private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new ImpTabPair(impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct0, impAct2);

    /* loaded from: rt.jar:sun/text/bidi/BidiBase$Point.class */
    class Point {
        int pos;
        int flag;

        Point() {
        }
    }

    /* loaded from: rt.jar:sun/text/bidi/BidiBase$InsertPoints.class */
    class InsertPoints {
        int size;
        int confirmed;
        Point[] points = new Point[0];

        InsertPoints() {
        }
    }

    static int DirPropFlag(byte b2) {
        return 1 << b2;
    }

    static byte NoContextRTL(byte b2) {
        return (byte) (b2 & (-65));
    }

    static int DirPropFlagNC(byte b2) {
        return 1 << (b2 & (-65));
    }

    static final int DirPropFlagLR(byte b2) {
        return DirPropFlagLR[b2 & 1];
    }

    static final int DirPropFlagE(byte b2) {
        return DirPropFlagE[b2 & 1];
    }

    static final int DirPropFlagO(byte b2) {
        return DirPropFlagO[b2 & 1];
    }

    private static byte GetLRFromLevel(byte b2) {
        return (byte) (b2 & 1);
    }

    private static boolean IsDefaultLevel(byte b2) {
        return (b2 & 126) == 126;
    }

    byte GetParaLevelAt(int i2) {
        return this.defaultParaLevel != 0 ? (byte) (this.dirProps[i2] >> 6) : this.paraLevel;
    }

    static boolean IsBidiControlChar(int i2) {
        return (i2 & (-4)) == 8204 || (i2 >= 8234 && i2 <= 8238);
    }

    public void verifyValidPara() {
        if (this != this.paraBidi) {
            throw new IllegalStateException("");
        }
    }

    public void verifyValidParaOrLine() {
        BidiBase bidiBase = this.paraBidi;
        if (this == bidiBase) {
            return;
        }
        if (bidiBase == null || bidiBase != bidiBase.paraBidi) {
            throw new IllegalStateException();
        }
    }

    public void verifyRange(int i2, int i3, int i4) {
        if (i2 < i3 || i2 >= i4) {
            throw new IllegalArgumentException("Value " + i2 + " is out of range " + i3 + " to " + i4);
        }
    }

    public void verifyIndex(int i2, int i3, int i4) {
        if (i2 < i3 || i2 >= i4) {
            throw new ArrayIndexOutOfBoundsException("Index " + i2 + " is out of range " + i3 + " to " + i4);
        }
    }

    public BidiBase(int i2, int i3) {
        this.dirPropsMemory = new byte[1];
        this.levelsMemory = new byte[1];
        this.parasMemory = new int[1];
        this.simpleParas = new int[]{0};
        this.runsMemory = new BidiRun[0];
        this.simpleRuns = new BidiRun[]{new BidiRun()};
        this.insertPoints = new InsertPoints();
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException();
        }
        try {
            this.bdp = UBiDiProps.getSingleton();
            if (i2 > 0) {
                getInitialDirPropsMemory(i2);
                getInitialLevelsMemory(i2);
            } else {
                this.mayAllocateText = true;
            }
            if (i3 > 0) {
                if (i3 > 1) {
                    getInitialRunsMemory(i3);
                    return;
                }
                return;
            }
            this.mayAllocateRuns = true;
        } catch (IOException e2) {
            throw new MissingResourceException(e2.getMessage(), "(BidiProps)", "");
        }
    }

    private Object getMemory(String str, Object obj, Class<?> cls, boolean z2, int i2) throws IllegalArgumentException {
        int length = Array.getLength(obj);
        if (i2 == length) {
            return obj;
        }
        if (!z2) {
            if (i2 <= length) {
                return obj;
            }
            throw new OutOfMemoryError("Failed to allocate memory for " + str);
        }
        try {
            return Array.newInstance(cls, i2);
        } catch (Exception e2) {
            throw new OutOfMemoryError("Failed to allocate memory for " + str);
        }
    }

    private void getDirPropsMemory(boolean z2, int i2) {
        this.dirPropsMemory = (byte[]) getMemory("DirProps", this.dirPropsMemory, Byte.TYPE, z2, i2);
    }

    void getDirPropsMemory(int i2) {
        getDirPropsMemory(this.mayAllocateText, i2);
    }

    private void getLevelsMemory(boolean z2, int i2) {
        this.levelsMemory = (byte[]) getMemory("Levels", this.levelsMemory, Byte.TYPE, z2, i2);
    }

    void getLevelsMemory(int i2) {
        getLevelsMemory(this.mayAllocateText, i2);
    }

    private void getRunsMemory(boolean z2, int i2) {
        this.runsMemory = (BidiRun[]) getMemory("Runs", this.runsMemory, BidiRun.class, z2, i2);
    }

    void getRunsMemory(int i2) {
        getRunsMemory(this.mayAllocateRuns, i2);
    }

    private void getInitialDirPropsMemory(int i2) {
        getDirPropsMemory(true, i2);
    }

    private void getInitialLevelsMemory(int i2) {
        getLevelsMemory(true, i2);
    }

    private void getInitialParasMemory(int i2) {
        this.parasMemory = (int[]) getMemory("Paras", this.parasMemory, Integer.TYPE, true, i2);
    }

    private void getInitialRunsMemory(int i2) {
        getRunsMemory(true, i2);
    }

    private void getDirProps() {
        boolean z2;
        byte b2;
        this.flags = 0;
        byte b3 = 0;
        boolean zIsDefaultLevel = IsDefaultLevel(this.paraLevel);
        this.lastArabicPos = -1;
        this.controlCount = 0;
        int i2 = 0;
        if (zIsDefaultLevel) {
            b3 = (this.paraLevel & 1) != 0 ? (byte) 64 : (byte) 0;
            b2 = b3;
            z2 = true;
        } else {
            z2 = false;
            b2 = 0;
        }
        int iCharCount = 0;
        while (iCharCount < this.originalLength) {
            int i3 = iCharCount;
            int iCharAt = UTF16.charAt(this.text, 0, this.originalLength, iCharCount);
            iCharCount += Character.charCount(iCharAt);
            int i4 = iCharCount - 1;
            byte b4 = (byte) this.bdp.getClass(iCharAt);
            this.flags |= DirPropFlag(b4);
            this.dirProps[i4] = (byte) (b4 | b2);
            if (i4 > i3) {
                this.flags |= DirPropFlag((byte) 18);
                do {
                    i4--;
                    this.dirProps[i4] = (byte) (18 | b2);
                } while (i4 > i3);
            }
            if (z2) {
                if (b4 == 0) {
                    z2 = 2;
                    if (b2 != 0) {
                        b2 = 0;
                        for (int i5 = i2; i5 < iCharCount; i5++) {
                            byte[] bArr = this.dirProps;
                            int i6 = i5;
                            bArr[i6] = (byte) (bArr[i6] & (-65));
                        }
                    }
                } else if (b4 == 1 || b4 == 13) {
                    z2 = 2;
                    if (b2 == 0) {
                        b2 = 64;
                        for (int i7 = i2; i7 < iCharCount; i7++) {
                            byte[] bArr2 = this.dirProps;
                            int i8 = i7;
                            bArr2[i8] = (byte) (bArr2[i8] | 64);
                        }
                    }
                }
            }
            if (b4 != 0 && b4 != 1) {
                if (b4 == 13) {
                    this.lastArabicPos = iCharCount - 1;
                } else if (b4 == 7 && iCharCount < this.originalLength) {
                    if (iCharAt != 13 || this.text[iCharCount] != '\n') {
                        this.paraCount++;
                    }
                    if (zIsDefaultLevel) {
                        z2 = true;
                        i2 = iCharCount;
                        b2 = b3;
                    }
                }
            }
        }
        if (zIsDefaultLevel) {
            this.paraLevel = GetParaLevelAt(0);
        }
        this.flags |= DirPropFlagLR(this.paraLevel);
        if (this.orderParagraphsLTR && (this.flags & DirPropFlag((byte) 7)) != 0) {
            this.flags |= DirPropFlag((byte) 0);
        }
    }

    private byte directionFromFlags() {
        if ((this.flags & MASK_RTL) == 0 && ((this.flags & DirPropFlag((byte) 5)) == 0 || (this.flags & MASK_POSSIBLE_N) == 0)) {
            return (byte) 0;
        }
        if ((this.flags & MASK_LTR) == 0) {
            return (byte) 1;
        }
        return (byte) 2;
    }

    private byte resolveExplicitLevels() {
        byte bGetParaLevelAt = GetParaLevelAt(0);
        int i2 = 0;
        byte bDirectionFromFlags = directionFromFlags();
        if (bDirectionFromFlags == 2 || this.paraCount != 1) {
            if (this.paraCount == 1 && (this.flags & MASK_EXPLICIT) == 0) {
                for (int i3 = 0; i3 < this.length; i3++) {
                    this.levels[i3] = bGetParaLevelAt;
                }
            } else {
                byte bGetParaLevelAt2 = bGetParaLevelAt;
                byte b2 = 0;
                byte[] bArr = new byte[61];
                int i4 = 0;
                int i5 = 0;
                this.flags = 0;
                for (int i6 = 0; i6 < this.length; i6++) {
                    byte bNoContextRTL = NoContextRTL(this.dirProps[i6]);
                    switch (bNoContextRTL) {
                        case 7:
                            b2 = 0;
                            i4 = 0;
                            i5 = 0;
                            bGetParaLevelAt = GetParaLevelAt(i6);
                            if (i6 + 1 < this.length) {
                                bGetParaLevelAt2 = GetParaLevelAt(i6 + 1);
                                if (this.text[i6] != '\r' || this.text[i6 + 1] != '\n') {
                                    int i7 = i2;
                                    i2++;
                                    this.paras[i7] = i6 + 1;
                                }
                            }
                            this.flags |= DirPropFlag((byte) 7);
                            break;
                        case 8:
                        case 9:
                        case 10:
                        case 13:
                        case 17:
                        default:
                            if (bGetParaLevelAt != bGetParaLevelAt2) {
                                bGetParaLevelAt = bGetParaLevelAt2;
                                if ((bGetParaLevelAt & Byte.MIN_VALUE) != 0) {
                                    this.flags |= DirPropFlagO(bGetParaLevelAt) | DirPropFlagMultiRuns;
                                } else {
                                    this.flags |= DirPropFlagE(bGetParaLevelAt) | DirPropFlagMultiRuns;
                                }
                            }
                            if ((bGetParaLevelAt & Byte.MIN_VALUE) == 0) {
                                this.flags |= DirPropFlag(bNoContextRTL);
                                break;
                            } else {
                                break;
                            }
                        case 11:
                        case 12:
                            byte b3 = (byte) ((bGetParaLevelAt2 + 2) & 126);
                            if (b3 <= 61) {
                                bArr[b2] = bGetParaLevelAt2;
                                b2 = (byte) (b2 + 1);
                                bGetParaLevelAt2 = b3;
                                if (bNoContextRTL == 12) {
                                    bGetParaLevelAt2 = (byte) (bGetParaLevelAt2 | Byte.MIN_VALUE);
                                }
                            } else if ((bGetParaLevelAt2 & Byte.MAX_VALUE) == 61) {
                                i5++;
                            } else {
                                i4++;
                            }
                            this.flags |= DirPropFlag((byte) 18);
                            break;
                        case 14:
                        case 15:
                            byte b4 = (byte) (((bGetParaLevelAt2 & Byte.MAX_VALUE) + 1) | 1);
                            if (b4 <= 61) {
                                bArr[b2] = bGetParaLevelAt2;
                                b2 = (byte) (b2 + 1);
                                bGetParaLevelAt2 = b4;
                                if (bNoContextRTL == 15) {
                                    bGetParaLevelAt2 = (byte) (bGetParaLevelAt2 | Byte.MIN_VALUE);
                                }
                            } else {
                                i5++;
                            }
                            this.flags |= DirPropFlag((byte) 18);
                            break;
                        case 16:
                            if (i5 > 0) {
                                i5--;
                            } else if (i4 > 0 && (bGetParaLevelAt2 & Byte.MAX_VALUE) != 61) {
                                i4--;
                            } else if (b2 > 0) {
                                b2 = (byte) (b2 - 1);
                                bGetParaLevelAt2 = bArr[b2];
                            }
                            this.flags |= DirPropFlag((byte) 18);
                            break;
                        case 18:
                            this.flags |= DirPropFlag((byte) 18);
                            break;
                    }
                    this.levels[i6] = bGetParaLevelAt;
                }
                if ((this.flags & MASK_EMBEDDING) != 0) {
                    this.flags |= DirPropFlagLR(this.paraLevel);
                }
                if (this.orderParagraphsLTR && (this.flags & DirPropFlag((byte) 7)) != 0) {
                    this.flags |= DirPropFlag((byte) 0);
                }
                bDirectionFromFlags = directionFromFlags();
            }
        }
        return bDirectionFromFlags;
    }

    private byte checkExplicitLevels() {
        this.flags = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.length; i3++) {
            if (this.levels[i3] == 0) {
                this.levels[i3] = this.paraLevel;
            }
            if (61 < (this.levels[i3] & Byte.MAX_VALUE)) {
                if ((this.levels[i3] & Byte.MIN_VALUE) != 0) {
                    this.levels[i3] = (byte) (this.paraLevel | Byte.MIN_VALUE);
                } else {
                    this.levels[i3] = this.paraLevel;
                }
            }
            byte b2 = this.levels[i3];
            byte bNoContextRTL = NoContextRTL(this.dirProps[i3]);
            if ((b2 & Byte.MIN_VALUE) != 0) {
                b2 = (byte) (b2 & Byte.MAX_VALUE);
                this.flags |= DirPropFlagO(b2);
            } else {
                this.flags |= DirPropFlagE(b2) | DirPropFlag(bNoContextRTL);
            }
            if ((b2 < GetParaLevelAt(i3) && (0 != b2 || bNoContextRTL != 7)) || 61 < b2) {
                throw new IllegalArgumentException("level " + ((int) b2) + " out of bounds at index " + i3);
            }
            if (bNoContextRTL == 7 && i3 + 1 < this.length && (this.text[i3] != '\r' || this.text[i3 + 1] != '\n')) {
                int i4 = i2;
                i2++;
                this.paras[i4] = i3 + 1;
            }
        }
        if ((this.flags & MASK_EMBEDDING) != 0) {
            this.flags |= DirPropFlagLR(this.paraLevel);
        }
        return directionFromFlags();
    }

    private static short GetStateProps(short s2) {
        return (short) (s2 & 31);
    }

    private static short GetActionProps(short s2) {
        return (short) (s2 >> 5);
    }

    private static short GetState(byte b2) {
        return (short) (b2 & 15);
    }

    private static short GetAction(byte b2) {
        return (short) (b2 >> 4);
    }

    /* loaded from: rt.jar:sun/text/bidi/BidiBase$ImpTabPair.class */
    private static class ImpTabPair {
        byte[][][] imptab;
        short[][] impact;

        /* JADX WARN: Type inference failed for: r1v1, types: [byte[][], byte[][][]] */
        /* JADX WARN: Type inference failed for: r1v3, types: [short[], short[][]] */
        ImpTabPair(byte[][] bArr, byte[][] bArr2, short[] sArr, short[] sArr2) {
            this.imptab = new byte[][]{bArr, bArr2};
            this.impact = new short[]{sArr, sArr2};
        }
    }

    /* loaded from: rt.jar:sun/text/bidi/BidiBase$LevState.class */
    private class LevState {
        byte[][] impTab;
        short[] impAct;
        int startON;
        int startL2EN;
        int lastStrongRTL;
        short state;
        byte runLevel;

        private LevState() {
        }
    }

    private void addPoint(int i2, int i3) {
        Point point = new Point();
        int length = this.insertPoints.points.length;
        if (length == 0) {
            this.insertPoints.points = new Point[10];
            length = 10;
        }
        if (this.insertPoints.size >= length) {
            Point[] pointArr = this.insertPoints.points;
            this.insertPoints.points = new Point[length * 2];
            System.arraycopy(pointArr, 0, this.insertPoints.points, 0, length);
        }
        point.pos = i2;
        point.flag = i3;
        this.insertPoints.points[this.insertPoints.size] = point;
        this.insertPoints.size++;
    }

    private void processPropertySeq(LevState levState, short s2, int i2, int i3) {
        byte[][] bArr = levState.impTab;
        short[] sArr = levState.impAct;
        short s3 = levState.state;
        byte b2 = bArr[s3][s2];
        levState.state = GetState(b2);
        short s4 = sArr[GetAction(b2)];
        byte b3 = bArr[levState.state][7];
        if (s4 != 0) {
            switch (s4) {
                case 1:
                    levState.startON = i2;
                    break;
                case 2:
                    i2 = levState.startON;
                    break;
                case 3:
                    if (levState.startL2EN >= 0) {
                        addPoint(levState.startL2EN, 1);
                    }
                    levState.startL2EN = -1;
                    if (this.insertPoints.points.length == 0 || this.insertPoints.size <= this.insertPoints.confirmed) {
                        levState.lastStrongRTL = -1;
                        if ((bArr[s3][7] & 1) != 0 && levState.startON > 0) {
                            i2 = levState.startON;
                        }
                        if (s2 == 5) {
                            addPoint(i2, 1);
                            this.insertPoints.confirmed = this.insertPoints.size;
                            break;
                        }
                    } else {
                        for (int i4 = levState.lastStrongRTL + 1; i4 < i2; i4++) {
                            this.levels[i4] = (byte) ((this.levels[i4] - 2) & (-2));
                        }
                        this.insertPoints.confirmed = this.insertPoints.size;
                        levState.lastStrongRTL = -1;
                        if (s2 == 5) {
                            addPoint(i2, 1);
                            this.insertPoints.confirmed = this.insertPoints.size;
                            break;
                        }
                    }
                    break;
                case 4:
                    if (this.insertPoints.points.length > 0) {
                        this.insertPoints.size = this.insertPoints.confirmed;
                    }
                    levState.startON = -1;
                    levState.startL2EN = -1;
                    levState.lastStrongRTL = i3 - 1;
                    break;
                case 5:
                    if (s2 == 3 && NoContextRTL(this.dirProps[i2]) == 5) {
                        if (levState.startL2EN == -1) {
                            levState.lastStrongRTL = i3 - 1;
                            break;
                        } else {
                            if (levState.startL2EN >= 0) {
                                addPoint(levState.startL2EN, 1);
                                levState.startL2EN = -2;
                            }
                            addPoint(i2, 1);
                            break;
                        }
                    } else if (levState.startL2EN == -1) {
                        levState.startL2EN = i2;
                        break;
                    }
                    break;
                case 6:
                    levState.lastStrongRTL = i3 - 1;
                    levState.startON = -1;
                    break;
                case 7:
                    int i5 = i2 - 1;
                    while (i5 >= 0 && (this.levels[i5] & 1) == 0) {
                        i5--;
                    }
                    if (i5 >= 0) {
                        addPoint(i5, 4);
                        this.insertPoints.confirmed = this.insertPoints.size;
                    }
                    levState.startON = i2;
                    break;
                case 8:
                    addPoint(i2, 1);
                    addPoint(i2, 2);
                    break;
                case 9:
                    this.insertPoints.size = this.insertPoints.confirmed;
                    if (s2 == 5) {
                        addPoint(i2, 4);
                        this.insertPoints.confirmed = this.insertPoints.size;
                        break;
                    }
                    break;
                case 10:
                    byte b4 = (byte) (levState.runLevel + b3);
                    for (int i6 = levState.startON; i6 < i2; i6++) {
                        if (this.levels[i6] < b4) {
                            this.levels[i6] = b4;
                        }
                    }
                    this.insertPoints.confirmed = this.insertPoints.size;
                    levState.startON = i2;
                    break;
                case 11:
                    byte b5 = levState.runLevel;
                    int i7 = i2 - 1;
                    while (i7 >= levState.startON) {
                        if (this.levels[i7] == b5 + 3) {
                            while (this.levels[i7] == b5 + 3) {
                                byte[] bArr2 = this.levels;
                                int i8 = i7;
                                i7--;
                                bArr2[i8] = (byte) (bArr2[i8] - 2);
                            }
                            while (this.levels[i7] == b5) {
                                i7--;
                            }
                        }
                        if (this.levels[i7] == b5 + 2) {
                            this.levels[i7] = b5;
                        } else {
                            this.levels[i7] = (byte) (b5 + 1);
                        }
                        i7--;
                    }
                    break;
                case 12:
                    byte b6 = (byte) (levState.runLevel + 1);
                    for (int i9 = i2 - 1; i9 >= levState.startON; i9--) {
                        if (this.levels[i9] > b6) {
                            byte[] bArr3 = this.levels;
                            int i10 = i9;
                            bArr3[i10] = (byte) (bArr3[i10] - 2);
                        }
                    }
                    break;
                default:
                    throw new IllegalStateException("Internal ICU error in processPropertySeq");
            }
        }
        if (b3 != 0 || i2 < i2) {
            byte b7 = (byte) (levState.runLevel + b3);
            for (int i11 = i2; i11 < i3; i11++) {
                this.levels[i11] = b7;
            }
        }
    }

    private void resolveImplicitLevels(int i2, int i3, short s2, short s3) {
        short sGetStateProps;
        short s4;
        LevState levState = new LevState();
        levState.startL2EN = -1;
        levState.lastStrongRTL = -1;
        levState.state = (short) 0;
        levState.runLevel = this.levels[i2];
        levState.impTab = this.impTabPair.imptab[levState.runLevel & 1];
        levState.impAct = this.impTabPair.impact[levState.runLevel & 1];
        processPropertySeq(levState, s2, i2, i2);
        if (this.dirProps[i2] == 17) {
            sGetStateProps = (short) (1 + s2);
        } else {
            sGetStateProps = 0;
        }
        int i4 = i2;
        int i5 = 0;
        for (int i6 = i2; i6 <= i3; i6++) {
            if (i6 >= i3) {
                s4 = s3;
            } else {
                s4 = groupProp[NoContextRTL(this.dirProps[i6])];
            }
            short s5 = sGetStateProps;
            short s6 = impTabProps[s5][s4];
            sGetStateProps = GetStateProps(s6);
            short sGetActionProps = GetActionProps(s6);
            if (i6 == i3 && sGetActionProps == 0) {
                sGetActionProps = 1;
            }
            if (sGetActionProps != 0) {
                short s7 = impTabProps[s5][13];
                switch (sGetActionProps) {
                    case 1:
                        processPropertySeq(levState, s7, i4, i6);
                        i4 = i6;
                        break;
                    case 2:
                        i5 = i6;
                        break;
                    case 3:
                        processPropertySeq(levState, s7, i4, i5);
                        processPropertySeq(levState, (short) 4, i5, i6);
                        i4 = i6;
                        break;
                    case 4:
                        processPropertySeq(levState, s7, i4, i5);
                        i4 = i5;
                        i5 = i6;
                        break;
                    default:
                        throw new IllegalStateException("Internal ICU error in resolveImplicitLevels");
                }
            }
        }
        processPropertySeq(levState, s3, i3, i3);
    }

    private void adjustWSLevels() {
        if ((this.flags & MASK_WS) != 0) {
            int i2 = this.trailingWSStart;
            while (i2 > 0) {
                while (i2 > 0) {
                    i2--;
                    int iDirPropFlagNC = DirPropFlagNC(this.dirProps[i2]);
                    if ((iDirPropFlagNC & MASK_WS) == 0) {
                        break;
                    }
                    if (this.orderParagraphsLTR && (iDirPropFlagNC & DirPropFlag((byte) 7)) != 0) {
                        this.levels[i2] = 0;
                    } else {
                        this.levels[i2] = GetParaLevelAt(i2);
                    }
                }
                while (true) {
                    if (i2 > 0) {
                        i2--;
                        int iDirPropFlagNC2 = DirPropFlagNC(this.dirProps[i2]);
                        if ((iDirPropFlagNC2 & MASK_BN_EXPLICIT) != 0) {
                            this.levels[i2] = this.levels[i2 + 1];
                        } else if (this.orderParagraphsLTR && (iDirPropFlagNC2 & DirPropFlag((byte) 7)) != 0) {
                            this.levels[i2] = 0;
                            break;
                        } else if ((iDirPropFlagNC2 & MASK_B_S) != 0) {
                            this.levels[i2] = GetParaLevelAt(i2);
                            break;
                        }
                    }
                }
            }
        }
    }

    private int Bidi_Min(int i2, int i3) {
        return i2 < i3 ? i2 : i3;
    }

    private int Bidi_Abs(int i2) {
        return i2 >= 0 ? i2 : -i2;
    }

    void setPara(String str, byte b2, byte[] bArr) {
        if (str == null) {
            setPara(new char[0], b2, bArr);
        } else {
            setPara(str.toCharArray(), b2, bArr);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0280  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x028b  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x02aa A[LOOP:2: B:68:0x02aa->B:83:?, LOOP_START, PHI: r12
  0x02aa: PHI (r12v1 int) = (r12v0 int), (r12v2 int) binds: [B:66:0x0298, B:83:?] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setPara(char[] r9, byte r10, byte[] r11) {
        /*
            Method dump skipped, instructions count: 740
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.text.bidi.BidiBase.setPara(char[], byte, byte[]):void");
    }

    public void setPara(AttributedCharacterIterator attributedCharacterIterator) {
        byte b2;
        byte bByteValue;
        char cFirst = attributedCharacterIterator.first();
        Boolean bool = (Boolean) attributedCharacterIterator.getAttribute(TextAttributeConstants.RUN_DIRECTION);
        Object attribute = attributedCharacterIterator.getAttribute(TextAttributeConstants.NUMERIC_SHAPING);
        if (bool == null) {
            b2 = 126;
        } else {
            b2 = bool.equals(TextAttributeConstants.RUN_DIRECTION_LTR) ? (byte) 0 : (byte) 1;
        }
        byte[] bArr = null;
        int endIndex = attributedCharacterIterator.getEndIndex() - attributedCharacterIterator.getBeginIndex();
        byte[] bArr2 = new byte[endIndex];
        char[] cArr = new char[endIndex];
        int i2 = 0;
        while (cFirst != 65535) {
            cArr[i2] = cFirst;
            Integer num = (Integer) attributedCharacterIterator.getAttribute(TextAttributeConstants.BIDI_EMBEDDING);
            if (num != null && (bByteValue = num.byteValue()) != 0) {
                if (bByteValue < 0) {
                    bArr = bArr2;
                    bArr2[i2] = (byte) ((0 - bByteValue) | (-128));
                } else {
                    bArr = bArr2;
                    bArr2[i2] = bByteValue;
                }
            }
            cFirst = attributedCharacterIterator.next();
            i2++;
        }
        if (attribute != null) {
            NumericShapings.shape(attribute, cArr, 0, endIndex);
        }
        setPara(cArr, b2, bArr);
    }

    private void orderParagraphsLTR(boolean z2) {
        this.orderParagraphsLTR = z2;
    }

    private byte getDirection() {
        verifyValidParaOrLine();
        return this.direction;
    }

    public int getLength() {
        verifyValidParaOrLine();
        return this.originalLength;
    }

    public byte getParaLevel() {
        verifyValidParaOrLine();
        return this.paraLevel;
    }

    public int getParagraphIndex(int i2) {
        verifyValidParaOrLine();
        BidiBase bidiBase = this.paraBidi;
        verifyRange(i2, 0, bidiBase.length);
        int i3 = 0;
        while (i2 >= bidiBase.paras[i3]) {
            i3++;
        }
        return i3;
    }

    public Bidi setLine(Bidi bidi, BidiBase bidiBase, Bidi bidi2, BidiBase bidiBase2, int i2, int i3) {
        verifyValidPara();
        verifyRange(i2, 0, i3);
        verifyRange(i3, 0, this.length + 1);
        return BidiLine.setLine(bidi, this, bidi2, bidiBase2, i2, i3);
    }

    public byte getLevelAt(int i2) {
        if (i2 < 0 || i2 >= this.length) {
            return (byte) getBaseLevel();
        }
        verifyValidParaOrLine();
        verifyRange(i2, 0, this.length);
        return BidiLine.getLevelAt(this, i2);
    }

    private byte[] getLevels() {
        verifyValidParaOrLine();
        if (this.length <= 0) {
            return new byte[0];
        }
        return BidiLine.getLevels(this);
    }

    public int countRuns() {
        verifyValidParaOrLine();
        BidiLine.getRuns(this);
        return this.runCount;
    }

    private int[] getVisualMap() {
        countRuns();
        if (this.resultLength <= 0) {
            return new int[0];
        }
        return BidiLine.getVisualMap(this);
    }

    private static int[] reorderVisual(byte[] bArr) {
        return BidiLine.reorderVisual(bArr);
    }

    public BidiBase(char[] cArr, int i2, byte[] bArr, int i3, int i4, int i5) {
        byte b2;
        byte[] bArr2;
        this(0, 0);
        switch (i5) {
            case -2:
                b2 = 126;
                break;
            case -1:
                b2 = Byte.MAX_VALUE;
                break;
            case 0:
            default:
                b2 = 0;
                break;
            case 1:
                b2 = 1;
                break;
        }
        if (bArr == null) {
            bArr2 = null;
        } else {
            bArr2 = new byte[i4];
            for (int i6 = 0; i6 < i4; i6++) {
                byte b3 = bArr[i6 + i3];
                if (b3 < 0) {
                    b3 = (byte) ((-b3) | (-128));
                } else if (b3 == 0) {
                    b3 = b2;
                    if (b2 > 61) {
                        b3 = (byte) (b3 & 1);
                    }
                }
                bArr2[i6] = b3;
            }
        }
        if (i2 == 0 && i3 == 0 && i4 == cArr.length) {
            setPara(cArr, b2, bArr2);
            return;
        }
        char[] cArr2 = new char[i4];
        System.arraycopy(cArr, i2, cArr2, 0, i4);
        setPara(cArr2, b2, bArr2);
    }

    public boolean isMixed() {
        return (isLeftToRight() || isRightToLeft()) ? false : true;
    }

    public boolean isLeftToRight() {
        return getDirection() == 0 && (this.paraLevel & 1) == 0;
    }

    public boolean isRightToLeft() {
        return getDirection() == 1 && (this.paraLevel & 1) == 1;
    }

    public boolean baseIsLeftToRight() {
        return getParaLevel() == 0;
    }

    public int getBaseLevel() {
        return getParaLevel();
    }

    private void getLogicalToVisualRunsMap() {
        if (this.isGoodLogicalToVisualRunsMap) {
            return;
        }
        int iCountRuns = countRuns();
        if (this.logicalToVisualRunsMap == null || this.logicalToVisualRunsMap.length < iCountRuns) {
            this.logicalToVisualRunsMap = new int[iCountRuns];
        }
        long[] jArr = new long[iCountRuns];
        for (int i2 = 0; i2 < iCountRuns; i2++) {
            jArr[i2] = (this.runs[i2].start << 32) + i2;
        }
        Arrays.sort(jArr);
        for (int i3 = 0; i3 < iCountRuns; i3++) {
            this.logicalToVisualRunsMap[i3] = (int) (jArr[i3] & (-1));
        }
        this.isGoodLogicalToVisualRunsMap = true;
    }

    public int getRunLevel(int i2) {
        verifyValidParaOrLine();
        BidiLine.getRuns(this);
        if (i2 < 0 || i2 >= this.runCount) {
            return getParaLevel();
        }
        getLogicalToVisualRunsMap();
        return this.runs[this.logicalToVisualRunsMap[i2]].level;
    }

    public int getRunStart(int i2) {
        verifyValidParaOrLine();
        BidiLine.getRuns(this);
        if (this.runCount == 1) {
            return 0;
        }
        if (i2 == this.runCount) {
            return this.length;
        }
        verifyIndex(i2, 0, this.runCount);
        getLogicalToVisualRunsMap();
        return this.runs[this.logicalToVisualRunsMap[i2]].start;
    }

    public int getRunLimit(int i2) {
        verifyValidParaOrLine();
        BidiLine.getRuns(this);
        if (this.runCount == 1) {
            return this.length;
        }
        verifyIndex(i2, 0, this.runCount);
        getLogicalToVisualRunsMap();
        int i3 = this.logicalToVisualRunsMap[i2];
        return this.runs[i3].start + (i3 == 0 ? this.runs[i3].limit : this.runs[i3].limit - this.runs[i3 - 1].limit);
    }

    public static boolean requiresBidi(char[] cArr, int i2, int i3) {
        if (0 > i2 || i2 > i3 || i3 > cArr.length) {
            throw new IllegalArgumentException("Value start " + i2 + " is out of range 0 to " + i3);
        }
        for (int i4 = i2; i4 < i3; i4++) {
            if (Character.isHighSurrogate(cArr[i4]) && i4 < i3 - 1 && Character.isLowSurrogate(cArr[i4 + 1])) {
                if (((1 << UCharacter.getDirection(Character.codePointAt(cArr, i4))) & 57378) != 0) {
                    return true;
                }
            } else if (((1 << UCharacter.getDirection(cArr[i4])) & 57378) != 0) {
                return true;
            }
        }
        return false;
    }

    public static void reorderVisually(byte[] bArr, int i2, Object[] objArr, int i3, int i4) {
        if (0 > i2 || bArr.length <= i2) {
            throw new IllegalArgumentException("Value levelStart " + i2 + " is out of range 0 to " + (bArr.length - 1));
        }
        if (0 > i3 || objArr.length <= i3) {
            throw new IllegalArgumentException("Value objectStart " + i2 + " is out of range 0 to " + (objArr.length - 1));
        }
        if (0 > i4 || objArr.length < i3 + i4) {
            throw new IllegalArgumentException("Value count " + i2 + " is out of range 0 to " + (objArr.length - i3));
        }
        byte[] bArr2 = new byte[i4];
        System.arraycopy(bArr, i2, bArr2, 0, i4);
        int[] iArrReorderVisual = reorderVisual(bArr2);
        Object[] objArr2 = new Object[i4];
        System.arraycopy(objArr, i3, objArr2, 0, i4);
        for (int i5 = 0; i5 < i4; i5++) {
            objArr[i3 + i5] = objArr2[iArrReorderVisual[i5]];
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[dir: ");
        sb.append((int) this.direction);
        sb.append(" baselevel: ");
        sb.append((int) this.paraLevel);
        sb.append(" length: ");
        sb.append(this.length);
        sb.append(" runs: ");
        if (this.levels == null) {
            sb.append(Separation.COLORANT_NONE);
        } else {
            sb.append('[');
            sb.append((int) this.levels[0]);
            for (int i2 = 1; i2 < this.levels.length; i2++) {
                sb.append(' ');
                sb.append((int) this.levels[i2]);
            }
            sb.append(']');
        }
        sb.append(" text: [0x");
        sb.append(Integer.toHexString(this.text[0]));
        for (int i3 = 1; i3 < this.text.length; i3++) {
            sb.append(" 0x");
            sb.append(Integer.toHexString(this.text[i3]));
        }
        sb.append("]]");
        return sb.toString();
    }

    /* loaded from: rt.jar:sun/text/bidi/BidiBase$TextAttributeConstants.class */
    private static class TextAttributeConstants {
        private static final Class<?> clazz = getClass("java.awt.font.TextAttribute");
        static final AttributedCharacterIterator.Attribute RUN_DIRECTION = getTextAttribute("RUN_DIRECTION");
        static final AttributedCharacterIterator.Attribute NUMERIC_SHAPING = getTextAttribute("NUMERIC_SHAPING");
        static final AttributedCharacterIterator.Attribute BIDI_EMBEDDING = getTextAttribute("BIDI_EMBEDDING");
        static final Boolean RUN_DIRECTION_LTR;

        private TextAttributeConstants() {
        }

        static {
            RUN_DIRECTION_LTR = clazz == null ? Boolean.FALSE : (Boolean) getStaticField(clazz, "RUN_DIRECTION_LTR");
        }

        private static Class<?> getClass(String str) {
            try {
                return Class.forName(str, true, null);
            } catch (ClassNotFoundException e2) {
                return null;
            }
        }

        private static Object getStaticField(Class<?> cls, String str) {
            try {
                return cls.getField(str).get(null);
            } catch (IllegalAccessException | NoSuchFieldException e2) {
                throw new AssertionError(e2);
            }
        }

        private static AttributedCharacterIterator.Attribute getTextAttribute(String str) {
            if (clazz == null) {
                return new AttributedCharacterIterator.Attribute(str) { // from class: sun.text.bidi.BidiBase.TextAttributeConstants.1
                };
            }
            return (AttributedCharacterIterator.Attribute) getStaticField(clazz, str);
        }
    }

    /* loaded from: rt.jar:sun/text/bidi/BidiBase$NumericShapings.class */
    private static class NumericShapings {
        private static final Class<?> clazz = getClass("java.awt.font.NumericShaper");
        private static final Method shapeMethod = getMethod(clazz, "shape", char[].class, Integer.TYPE, Integer.TYPE);

        private NumericShapings() {
        }

        private static Class<?> getClass(String str) {
            try {
                return Class.forName(str, true, null);
            } catch (ClassNotFoundException e2) {
                return null;
            }
        }

        private static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
            if (cls != null) {
                try {
                    return cls.getMethod(str, clsArr);
                } catch (NoSuchMethodException e2) {
                    throw new AssertionError(e2);
                }
            }
            return null;
        }

        static void shape(Object obj, char[] cArr, int i2, int i3) throws IllegalArgumentException {
            if (shapeMethod == null) {
                throw new AssertionError((Object) "Should not get here");
            }
            try {
                shapeMethod.invoke(obj, cArr, Integer.valueOf(i2), Integer.valueOf(i3));
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new AssertionError(e3);
            }
        }
    }
}
