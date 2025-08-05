package org.jpedal.jbig2.examples.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/examples/pdf/PDFSegment.class */
public class PDFSegment {
    private ByteArrayOutputStream header = new ByteArrayOutputStream();
    private ByteArrayOutputStream data = new ByteArrayOutputStream();
    private int segmentDataLength;

    public void writeToHeader(short bite) {
        this.header.write(bite);
    }

    public void writeToHeader(short[] bites) throws IOException {
        for (short s2 : bites) {
            this.header.write(s2);
        }
    }

    public void writeToData(short bite) {
        this.data.write(bite);
    }

    public ByteArrayOutputStream getHeader() {
        return this.header;
    }

    public ByteArrayOutputStream getData() {
        return this.data;
    }

    public void setDataLength(int segmentDataLength) {
        this.segmentDataLength = segmentDataLength;
    }

    public int getSegmentDataLength() {
        return this.segmentDataLength;
    }
}
