package org.jpedal.jbig2.segment.region.halftone;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Flags;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/halftone/HalftoneRegionFlags.class */
public class HalftoneRegionFlags extends Flags {
    public static String H_MMR = "H_MMR";
    public static String H_TEMPLATE = "H_TEMPLATE";
    public static String H_ENABLE_SKIP = "H_ENABLE_SKIP";
    public static String H_COMB_OP = "H_COMB_OP";
    public static String H_DEF_PIXEL = "H_DEF_PIXEL";

    @Override // org.jpedal.jbig2.segment.Flags
    public void setFlags(int flagsAsInt) {
        this.flagsAsInt = flagsAsInt;
        this.flags.put(H_MMR, new Integer(flagsAsInt & 1));
        this.flags.put(H_TEMPLATE, new Integer((flagsAsInt >> 1) & 3));
        this.flags.put(H_ENABLE_SKIP, new Integer((flagsAsInt >> 3) & 1));
        this.flags.put(H_COMB_OP, new Integer((flagsAsInt >> 4) & 7));
        this.flags.put(H_DEF_PIXEL, new Integer((flagsAsInt >> 7) & 1));
        if (JBIG2StreamDecoder.debug) {
            System.out.println(this.flags);
        }
    }
}
