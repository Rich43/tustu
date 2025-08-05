package org.jpedal.jbig2.segment.region.text;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Flags;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/text/TextRegionFlags.class */
public class TextRegionFlags extends Flags {
    public static String SB_HUFF = "SB_HUFF";
    public static String SB_REFINE = "SB_REFINE";
    public static String LOG_SB_STRIPES = "LOG_SB_STRIPES";
    public static String REF_CORNER = "REF_CORNER";
    public static String TRANSPOSED = "TRANSPOSED";
    public static String SB_COMB_OP = "SB_COMB_OP";
    public static String SB_DEF_PIXEL = "SB_DEF_PIXEL";
    public static String SB_DS_OFFSET = "SB_DS_OFFSET";
    public static String SB_R_TEMPLATE = "SB_R_TEMPLATE";

    @Override // org.jpedal.jbig2.segment.Flags
    public void setFlags(int flagsAsInt) {
        this.flagsAsInt = flagsAsInt;
        this.flags.put(SB_HUFF, new Integer(flagsAsInt & 1));
        this.flags.put(SB_REFINE, new Integer((flagsAsInt >> 1) & 1));
        this.flags.put(LOG_SB_STRIPES, new Integer((flagsAsInt >> 2) & 3));
        this.flags.put(REF_CORNER, new Integer((flagsAsInt >> 4) & 3));
        this.flags.put(TRANSPOSED, new Integer((flagsAsInt >> 6) & 1));
        this.flags.put(SB_COMB_OP, new Integer((flagsAsInt >> 7) & 3));
        this.flags.put(SB_DEF_PIXEL, new Integer((flagsAsInt >> 9) & 1));
        int sOffset = (flagsAsInt >> 10) & 31;
        if ((sOffset & 16) != 0) {
            sOffset |= -16;
        }
        this.flags.put(SB_DS_OFFSET, new Integer(sOffset));
        this.flags.put(SB_R_TEMPLATE, new Integer((flagsAsInt >> 15) & 1));
        if (JBIG2StreamDecoder.debug) {
            System.out.println(this.flags);
        }
    }
}
