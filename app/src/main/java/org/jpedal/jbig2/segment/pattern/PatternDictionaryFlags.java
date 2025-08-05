package org.jpedal.jbig2.segment.pattern;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Flags;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/pattern/PatternDictionaryFlags.class */
public class PatternDictionaryFlags extends Flags {
    public static String HD_MMR = "HD_MMR";
    public static String HD_TEMPLATE = "HD_TEMPLATE";

    @Override // org.jpedal.jbig2.segment.Flags
    public void setFlags(int flagsAsInt) {
        this.flagsAsInt = flagsAsInt;
        this.flags.put(HD_MMR, new Integer(flagsAsInt & 1));
        this.flags.put(HD_TEMPLATE, new Integer((flagsAsInt >> 1) & 3));
        if (JBIG2StreamDecoder.debug) {
            System.out.println(this.flags);
        }
    }
}
