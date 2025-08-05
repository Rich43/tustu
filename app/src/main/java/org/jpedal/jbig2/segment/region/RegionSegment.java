package org.jpedal.jbig2.segment.region;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/RegionSegment.class */
public abstract class RegionSegment extends Segment {
    protected int regionBitmapWidth;
    protected int regionBitmapHeight;
    protected int regionBitmapXLocation;
    protected int regionBitmapYLocation;
    protected RegionFlags regionFlags;

    public RegionSegment(JBIG2StreamDecoder streamDecoder) {
        super(streamDecoder);
        this.regionFlags = new RegionFlags();
    }

    @Override // org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        short[] buff = new short[4];
        this.decoder.readByte(buff);
        this.regionBitmapWidth = BinaryOperation.getInt32(buff);
        short[] buff2 = new short[4];
        this.decoder.readByte(buff2);
        this.regionBitmapHeight = BinaryOperation.getInt32(buff2);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("Bitmap size = " + this.regionBitmapWidth + 'x' + this.regionBitmapHeight);
        }
        short[] buff3 = new short[4];
        this.decoder.readByte(buff3);
        this.regionBitmapXLocation = BinaryOperation.getInt32(buff3);
        short[] buff4 = new short[4];
        this.decoder.readByte(buff4);
        this.regionBitmapYLocation = BinaryOperation.getInt32(buff4);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("Bitmap location = " + this.regionBitmapXLocation + ',' + this.regionBitmapYLocation);
        }
        short regionFlagsField = this.decoder.readByte();
        this.regionFlags.setFlags(regionFlagsField);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("region Segment flags = " + ((int) regionFlagsField));
        }
    }
}
