package org.jpedal.jbig2.segment.pageinformation;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Flags;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/pageinformation/PageInformationFlags.class */
public class PageInformationFlags extends Flags {
    public static String DEFAULT_PIXEL_VALUE = "DEFAULT_PIXEL_VALUE";
    public static String DEFAULT_COMBINATION_OPERATOR = "DEFAULT_COMBINATION_OPERATOR";

    @Override // org.jpedal.jbig2.segment.Flags
    public void setFlags(int flagsAsInt) {
        this.flagsAsInt = flagsAsInt;
        this.flags.put(DEFAULT_PIXEL_VALUE, Integer.valueOf((flagsAsInt >> 2) & 1));
        this.flags.put(DEFAULT_COMBINATION_OPERATOR, Integer.valueOf((flagsAsInt >> 3) & 3));
        if (JBIG2StreamDecoder.debug) {
            System.out.println(this.flags);
        }
    }
}
