package org.jpedal.jbig2.segment.region.generic;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Flags;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/generic/GenericRegionFlags.class */
public class GenericRegionFlags extends Flags {
    public static String MMR = "MMR";
    public static String GB_TEMPLATE = "GB_TEMPLATE";
    public static String TPGDON = "TPGDON";

    @Override // org.jpedal.jbig2.segment.Flags
    public void setFlags(int flagsAsInt) {
        this.flagsAsInt = flagsAsInt;
        this.flags.put(MMR, new Integer(flagsAsInt & 1));
        this.flags.put(GB_TEMPLATE, new Integer((flagsAsInt >> 1) & 3));
        this.flags.put(TPGDON, new Integer((flagsAsInt >> 3) & 1));
        if (JBIG2StreamDecoder.debug) {
            System.out.println(this.flags);
        }
    }
}
