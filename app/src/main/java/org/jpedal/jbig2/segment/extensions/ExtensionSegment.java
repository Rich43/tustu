package org.jpedal.jbig2.segment.extensions;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Segment;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/extensions/ExtensionSegment.class */
public class ExtensionSegment extends Segment {
    public ExtensionSegment(JBIG2StreamDecoder streamDecoder) {
        super(streamDecoder);
    }

    @Override // org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        for (int i2 = 0; i2 < getSegmentHeader().getSegmentDataLength(); i2++) {
            this.decoder.readByte();
        }
    }
}
