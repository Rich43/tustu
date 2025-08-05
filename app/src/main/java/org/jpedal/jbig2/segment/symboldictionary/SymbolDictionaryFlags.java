package org.jpedal.jbig2.segment.symboldictionary;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Flags;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/symboldictionary/SymbolDictionaryFlags.class */
public class SymbolDictionaryFlags extends Flags {
    public static String SD_HUFF = "SD_HUFF";
    public static String SD_REF_AGG = "SD_REF_AGG";
    public static String SD_HUFF_DH = "SD_HUFF_DH";
    public static String SD_HUFF_DW = "SD_HUFF_DW";
    public static String SD_HUFF_BM_SIZE = "SD_HUFF_BM_SIZE";
    public static String SD_HUFF_AGG_INST = "SD_HUFF_AGG_INST";
    public static String BITMAP_CC_USED = "BITMAP_CC_USED";
    public static String BITMAP_CC_RETAINED = "BITMAP_CC_RETAINED";
    public static String SD_TEMPLATE = "SD_TEMPLATE";
    public static String SD_R_TEMPLATE = "SD_R_TEMPLATE";

    @Override // org.jpedal.jbig2.segment.Flags
    public void setFlags(int flagsAsInt) {
        this.flagsAsInt = flagsAsInt;
        this.flags.put(SD_HUFF, new Integer(flagsAsInt & 1));
        this.flags.put(SD_REF_AGG, new Integer((flagsAsInt >> 1) & 1));
        this.flags.put(SD_HUFF_DH, new Integer((flagsAsInt >> 2) & 3));
        this.flags.put(SD_HUFF_DW, new Integer((flagsAsInt >> 4) & 3));
        this.flags.put(SD_HUFF_BM_SIZE, new Integer((flagsAsInt >> 6) & 1));
        this.flags.put(SD_HUFF_AGG_INST, new Integer((flagsAsInt >> 7) & 1));
        this.flags.put(BITMAP_CC_USED, new Integer((flagsAsInt >> 8) & 1));
        this.flags.put(BITMAP_CC_RETAINED, new Integer((flagsAsInt >> 9) & 1));
        this.flags.put(SD_TEMPLATE, new Integer((flagsAsInt >> 10) & 3));
        this.flags.put(SD_R_TEMPLATE, new Integer((flagsAsInt >> 12) & 1));
        if (JBIG2StreamDecoder.debug) {
            System.out.println(this.flags);
        }
    }
}
