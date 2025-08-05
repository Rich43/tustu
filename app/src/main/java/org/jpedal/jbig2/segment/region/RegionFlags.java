package org.jpedal.jbig2.segment.region;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Flags;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/RegionFlags.class */
public class RegionFlags extends Flags {
    public static String EXTERNAL_COMBINATION_OPERATOR = "EXTERNAL_COMBINATION_OPERATOR";

    @Override // org.jpedal.jbig2.segment.Flags
    public void setFlags(int flagsAsInt) {
        this.flagsAsInt = flagsAsInt;
        this.flags.put(EXTERNAL_COMBINATION_OPERATOR, new Integer(flagsAsInt & 7));
        if (JBIG2StreamDecoder.debug) {
            System.out.println(this.flags);
        }
    }
}
