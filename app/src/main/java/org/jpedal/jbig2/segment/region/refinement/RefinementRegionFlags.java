package org.jpedal.jbig2.segment.region.refinement;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Flags;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/refinement/RefinementRegionFlags.class */
public class RefinementRegionFlags extends Flags {
    public static String GR_TEMPLATE = "GR_TEMPLATE";
    public static String TPGDON = "TPGDON";

    @Override // org.jpedal.jbig2.segment.Flags
    public void setFlags(int flagsAsInt) {
        this.flagsAsInt = flagsAsInt;
        this.flags.put(GR_TEMPLATE, new Integer(flagsAsInt & 1));
        this.flags.put(TPGDON, new Integer((flagsAsInt >> 1) & 1));
        if (JBIG2StreamDecoder.debug) {
            System.out.println(this.flags);
        }
    }
}
