package org.jpedal.jbig2.segment.pattern;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/pattern/PatternDictionarySegment.class */
public class PatternDictionarySegment extends Segment {
    PatternDictionaryFlags patternDictionaryFlags;
    private int width;
    private int height;
    private int grayMax;
    private JBIG2Bitmap[] bitmaps;
    private int size;

    public PatternDictionarySegment(JBIG2StreamDecoder streamDecoder) {
        super(streamDecoder);
        this.patternDictionaryFlags = new PatternDictionaryFlags();
    }

    @Override // org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        readPatternDictionaryFlags();
        this.width = this.decoder.readByte();
        this.height = this.decoder.readByte();
        if (JBIG2StreamDecoder.debug) {
            System.out.println("pattern dictionary size = " + this.width + " , " + this.height);
        }
        short[] buf = new short[4];
        this.decoder.readByte(buf);
        this.grayMax = BinaryOperation.getInt32(buf);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("grey max = " + this.grayMax);
        }
        boolean useMMR = this.patternDictionaryFlags.getFlagValue(PatternDictionaryFlags.HD_MMR) == 1;
        int template = this.patternDictionaryFlags.getFlagValue(PatternDictionaryFlags.HD_TEMPLATE);
        if (!useMMR) {
            this.arithmeticDecoder.resetGenericStats(template, null);
            this.arithmeticDecoder.start();
        }
        short[] genericBAdaptiveTemplateX = {(short) (-this.width), -3, 2, -2};
        short[] genericBAdaptiveTemplateY = {0, -1, -2, -2};
        this.size = this.grayMax + 1;
        JBIG2Bitmap bitmap = new JBIG2Bitmap(this.size * this.width, this.height, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
        bitmap.clear(0);
        bitmap.readBitmap(useMMR, template, false, false, null, genericBAdaptiveTemplateX, genericBAdaptiveTemplateY, this.segmentHeader.getSegmentDataLength() - 7);
        JBIG2Bitmap[] bitmaps = new JBIG2Bitmap[this.size];
        int x2 = 0;
        for (int i2 = 0; i2 < this.size; i2++) {
            bitmaps[i2] = bitmap.getSlice(x2, 0, this.width, this.height);
            x2 += this.width;
        }
        this.bitmaps = bitmaps;
    }

    public JBIG2Bitmap[] getBitmaps() {
        return this.bitmaps;
    }

    private void readPatternDictionaryFlags() throws IOException {
        short patternDictionaryFlagsField = this.decoder.readByte();
        this.patternDictionaryFlags.setFlags(patternDictionaryFlagsField);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("pattern Dictionary flags = " + ((int) patternDictionaryFlagsField));
        }
    }

    public PatternDictionaryFlags getPatternDictionaryFlags() {
        return this.patternDictionaryFlags;
    }

    public int getSize() {
        return this.size;
    }
}
