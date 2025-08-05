package com.sun.xml.internal.fastinfoset;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/DecoderStateTables.class */
public class DecoderStateTables {
    public static final int STATE_ILLEGAL = 255;
    public static final int STATE_UNSUPPORTED = 254;
    public static final int EII_NO_AIIS_INDEX_SMALL = 0;
    public static final int EII_AIIS_INDEX_SMALL = 1;
    public static final int EII_INDEX_MEDIUM = 2;
    public static final int EII_INDEX_LARGE = 3;
    public static final int EII_NAMESPACES = 4;
    public static final int EII_LITERAL = 5;
    public static final int CII_UTF8_SMALL_LENGTH = 6;
    public static final int CII_UTF8_MEDIUM_LENGTH = 7;
    public static final int CII_UTF8_LARGE_LENGTH = 8;
    public static final int CII_UTF16_SMALL_LENGTH = 9;
    public static final int CII_UTF16_MEDIUM_LENGTH = 10;
    public static final int CII_UTF16_LARGE_LENGTH = 11;
    public static final int CII_RA = 12;
    public static final int CII_EA = 13;
    public static final int CII_INDEX_SMALL = 14;
    public static final int CII_INDEX_MEDIUM = 15;
    public static final int CII_INDEX_LARGE = 16;
    public static final int CII_INDEX_LARGE_LARGE = 17;
    public static final int COMMENT_II = 18;
    public static final int PROCESSING_INSTRUCTION_II = 19;
    public static final int DOCUMENT_TYPE_DECLARATION_II = 20;
    public static final int UNEXPANDED_ENTITY_REFERENCE_II = 21;
    public static final int TERMINATOR_SINGLE = 22;
    public static final int TERMINATOR_DOUBLE = 23;
    public static final int AII_INDEX_SMALL = 0;
    public static final int AII_INDEX_MEDIUM = 1;
    public static final int AII_INDEX_LARGE = 2;
    public static final int AII_LITERAL = 3;
    public static final int AII_TERMINATOR_SINGLE = 4;
    public static final int AII_TERMINATOR_DOUBLE = 5;
    public static final int NISTRING_UTF8_SMALL_LENGTH = 0;
    public static final int NISTRING_UTF8_MEDIUM_LENGTH = 1;
    public static final int NISTRING_UTF8_LARGE_LENGTH = 2;
    public static final int NISTRING_UTF16_SMALL_LENGTH = 3;
    public static final int NISTRING_UTF16_MEDIUM_LENGTH = 4;
    public static final int NISTRING_UTF16_LARGE_LENGTH = 5;
    public static final int NISTRING_RA = 6;
    public static final int NISTRING_EA = 7;
    public static final int NISTRING_INDEX_SMALL = 8;
    public static final int NISTRING_INDEX_MEDIUM = 9;
    public static final int NISTRING_INDEX_LARGE = 10;
    public static final int NISTRING_EMPTY = 11;
    static final int ISTRING_SMALL_LENGTH = 0;
    static final int ISTRING_MEDIUM_LENGTH = 1;
    static final int ISTRING_LARGE_LENGTH = 2;
    static final int ISTRING_INDEX_SMALL = 3;
    static final int ISTRING_INDEX_MEDIUM = 4;
    static final int ISTRING_INDEX_LARGE = 5;
    static final int ISTRING_PREFIX_NAMESPACE_LENGTH_3 = 6;
    static final int ISTRING_PREFIX_NAMESPACE_LENGTH_5 = 7;
    static final int ISTRING_PREFIX_NAMESPACE_LENGTH_29 = 8;
    static final int ISTRING_PREFIX_NAMESPACE_LENGTH_36 = 9;
    static final int ISTRING_PREFIX_NAMESPACE_INDEX_ZERO = 10;
    static final int UTF8_NCNAME_NCNAME = 0;
    static final int UTF8_NCNAME_NCNAME_CHAR = 1;
    static final int UTF8_TWO_BYTES = 2;
    static final int UTF8_THREE_BYTES = 3;
    static final int UTF8_FOUR_BYTES = 4;
    static final int UTF8_ONE_BYTE = 1;
    private static int RANGE_INDEX_END = 0;
    private static int RANGE_INDEX_VALUE = 1;
    private static final int[] DII = new int[256];
    private static final int[][] DII_RANGES = {new int[]{31, 0}, new int[]{39, 2}, new int[]{48, 3}, new int[]{55, 255}, new int[]{56, 4}, new int[]{59, 255}, new int[]{60, 5}, new int[]{61, 5}, new int[]{62, 255}, new int[]{63, 5}, new int[]{95, 1}, new int[]{103, 2}, new int[]{112, 3}, new int[]{119, 255}, new int[]{120, 4}, new int[]{123, 255}, new int[]{124, 5}, new int[]{125, 5}, new int[]{126, 255}, new int[]{127, 5}, new int[]{195, 255}, new int[]{199, 20}, new int[]{224, 255}, new int[]{225, 19}, new int[]{226, 18}, new int[]{239, 255}, new int[]{240, 22}, new int[]{254, 255}, new int[]{255, 23}};
    private static final int[] EII = new int[256];
    private static final int[][] EII_RANGES = {new int[]{31, 0}, new int[]{39, 2}, new int[]{48, 3}, new int[]{55, 255}, new int[]{56, 4}, new int[]{59, 255}, new int[]{60, 5}, new int[]{61, 5}, new int[]{62, 255}, new int[]{63, 5}, new int[]{95, 1}, new int[]{103, 2}, new int[]{112, 3}, new int[]{119, 255}, new int[]{120, 4}, new int[]{123, 255}, new int[]{124, 5}, new int[]{125, 5}, new int[]{126, 255}, new int[]{127, 5}, new int[]{129, 6}, new int[]{130, 7}, new int[]{131, 8}, new int[]{133, 9}, new int[]{134, 10}, new int[]{135, 11}, new int[]{139, 12}, new int[]{143, 13}, new int[]{145, 6}, new int[]{146, 7}, new int[]{147, 8}, new int[]{149, 9}, new int[]{150, 10}, new int[]{151, 11}, new int[]{155, 12}, new int[]{159, 13}, new int[]{175, 14}, new int[]{179, 15}, new int[]{183, 16}, new int[]{184, 17}, new int[]{199, 255}, new int[]{203, 21}, new int[]{224, 255}, new int[]{225, 19}, new int[]{226, 18}, new int[]{239, 255}, new int[]{240, 22}, new int[]{254, 255}, new int[]{255, 23}};
    private static final int[] AII = new int[256];
    private static final int[][] AII_RANGES = {new int[]{63, 0}, new int[]{95, 1}, new int[]{111, 2}, new int[]{119, 255}, new int[]{121, 3}, new int[]{122, 255}, new int[]{123, 3}, new int[]{239, 255}, new int[]{240, 4}, new int[]{254, 255}, new int[]{255, 5}};
    private static final int[] NISTRING = new int[256];
    private static final int[][] NISTRING_RANGES = {new int[]{7, 0}, new int[]{8, 1}, new int[]{11, 255}, new int[]{12, 2}, new int[]{15, 255}, new int[]{23, 3}, new int[]{24, 4}, new int[]{27, 255}, new int[]{28, 5}, new int[]{31, 255}, new int[]{47, 6}, new int[]{63, 7}, new int[]{71, 0}, new int[]{72, 1}, new int[]{75, 255}, new int[]{76, 2}, new int[]{79, 255}, new int[]{87, 3}, new int[]{88, 4}, new int[]{91, 255}, new int[]{92, 5}, new int[]{95, 255}, new int[]{111, 6}, new int[]{127, 7}, new int[]{191, 8}, new int[]{223, 9}, new int[]{239, 10}, new int[]{254, 255}, new int[]{255, 11}};
    private static final int[] ISTRING = new int[256];
    private static final int[][] ISTRING_RANGES = {new int[]{63, 0}, new int[]{64, 1}, new int[]{95, 255}, new int[]{96, 2}, new int[]{127, 255}, new int[]{191, 3}, new int[]{223, 4}, new int[]{239, 5}, new int[]{255, 255}};
    private static final int[] ISTRING_PREFIX_NAMESPACE = new int[256];
    private static final int[][] ISTRING_PREFIX_NAMESPACE_RANGES = {new int[]{1, 0}, new int[]{2, 6}, new int[]{3, 0}, new int[]{4, 7}, new int[]{27, 0}, new int[]{28, 8}, new int[]{34, 0}, new int[]{35, 9}, new int[]{63, 0}, new int[]{64, 1}, new int[]{95, 255}, new int[]{96, 2}, new int[]{127, 255}, new int[]{128, 10}, new int[]{191, 3}, new int[]{223, 4}, new int[]{239, 5}, new int[]{255, 255}};
    private static final int[] UTF8_NCNAME = new int[256];
    private static final int[][] UTF8_NCNAME_RANGES = {new int[]{44, 255}, new int[]{46, 1}, new int[]{47, 255}, new int[]{57, 1}, new int[]{64, 255}, new int[]{90, 0}, new int[]{94, 255}, new int[]{95, 0}, new int[]{96, 255}, new int[]{122, 0}, new int[]{127, 255}, new int[]{193, 255}, new int[]{223, 2}, new int[]{239, 3}, new int[]{247, 4}, new int[]{255, 255}};
    private static final int[] UTF8 = new int[256];
    private static final int[][] UTF8_RANGES = {new int[]{8, 255}, new int[]{10, 1}, new int[]{12, 255}, new int[]{13, 1}, new int[]{31, 255}, new int[]{127, 1}, new int[]{193, 255}, new int[]{223, 2}, new int[]{239, 3}, new int[]{247, 4}, new int[]{255, 255}};

    /* JADX WARN: Type inference failed for: r0v13, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v17, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v21, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v25, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v29, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v33, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v5, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v9, types: [int[], int[][]] */
    static {
        constructTable(DII, DII_RANGES);
        constructTable(EII, EII_RANGES);
        constructTable(AII, AII_RANGES);
        constructTable(NISTRING, NISTRING_RANGES);
        constructTable(ISTRING, ISTRING_RANGES);
        constructTable(ISTRING_PREFIX_NAMESPACE, ISTRING_PREFIX_NAMESPACE_RANGES);
        constructTable(UTF8_NCNAME, UTF8_NCNAME_RANGES);
        constructTable(UTF8, UTF8_RANGES);
    }

    private static void constructTable(int[] table, int[][] ranges) {
        int start = 0;
        for (int range = 0; range < ranges.length; range++) {
            int end = ranges[range][RANGE_INDEX_END];
            int value = ranges[range][RANGE_INDEX_VALUE];
            for (int i2 = start; i2 <= end; i2++) {
                table[i2] = value;
            }
            start = end + 1;
        }
    }

    public static final int DII(int index) {
        return DII[index];
    }

    public static final int EII(int index) {
        return EII[index];
    }

    public static final int AII(int index) {
        return AII[index];
    }

    public static final int NISTRING(int index) {
        return NISTRING[index];
    }

    public static final int ISTRING(int index) {
        return ISTRING[index];
    }

    public static final int ISTRING_PREFIX_NAMESPACE(int index) {
        return ISTRING_PREFIX_NAMESPACE[index];
    }

    public static final int UTF8(int index) {
        return UTF8[index];
    }

    public static final int UTF8_NCNAME(int index) {
        return UTF8_NCNAME[index];
    }

    private DecoderStateTables() {
    }
}
