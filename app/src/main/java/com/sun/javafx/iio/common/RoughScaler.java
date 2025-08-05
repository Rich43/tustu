package com.sun.javafx.iio.common;

import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/common/RoughScaler.class */
public class RoughScaler implements PushbroomScaler {
    protected int numBands;
    protected int destWidth;
    protected int destHeight;
    protected double scaleY;
    protected ByteBuffer destBuf;
    protected int[] colPositions;
    protected int sourceLine;
    protected int nextSourceLine;
    protected int destLine;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RoughScaler.class.desiredAssertionStatus();
    }

    public RoughScaler(int sourceWidth, int sourceHeight, int numBands, int destWidth, int destHeight) {
        if (sourceWidth <= 0 || sourceHeight <= 0 || numBands <= 0 || destWidth <= 0 || destHeight <= 0) {
            throw new IllegalArgumentException();
        }
        this.numBands = numBands;
        this.destWidth = destWidth;
        this.destHeight = destHeight;
        this.destBuf = ByteBuffer.wrap(new byte[destHeight * destWidth * numBands]);
        double scaleX = sourceWidth / destWidth;
        this.scaleY = sourceHeight / destHeight;
        this.colPositions = new int[destWidth];
        for (int i2 = 0; i2 < destWidth; i2++) {
            int pos = (int) ((i2 + 0.5d) * scaleX);
            this.colPositions[i2] = pos * numBands;
        }
        this.sourceLine = 0;
        this.destLine = 0;
        this.nextSourceLine = (int) (0.5d * this.scaleY);
    }

    @Override // com.sun.javafx.iio.common.PushbroomScaler
    public ByteBuffer getDestination() {
        return this.destBuf;
    }

    @Override // com.sun.javafx.iio.common.PushbroomScaler
    public boolean putSourceScanline(byte[] scanline, int off) {
        if (off < 0) {
            throw new IllegalArgumentException("off < 0!");
        }
        if (this.destLine < this.destHeight) {
            if (this.sourceLine == this.nextSourceLine) {
                if (!$assertionsDisabled && !this.destBuf.hasArray()) {
                    throw new AssertionError((Object) "destBuf.hasArray() == false => destBuf is direct");
                }
                byte[] dest = this.destBuf.array();
                int destOffset = this.destLine * this.destWidth * this.numBands;
                int doff = destOffset;
                for (int i2 = 0; i2 < this.destWidth; i2++) {
                    int sourceOffset = off + this.colPositions[i2];
                    for (int j2 = 0; j2 < this.numBands; j2++) {
                        int i3 = doff;
                        doff++;
                        dest[i3] = scanline[sourceOffset + j2];
                    }
                }
                while (true) {
                    int i4 = this.destLine + 1;
                    this.destLine = i4;
                    if (((int) ((i4 + 0.5d) * this.scaleY)) != this.sourceLine) {
                        break;
                    }
                    System.arraycopy(dest, destOffset, dest, doff, this.destWidth * this.numBands);
                    doff += this.destWidth * this.numBands;
                }
                this.nextSourceLine = (int) ((this.destLine + 0.5d) * this.scaleY);
            }
            this.sourceLine++;
        }
        return this.destLine == this.destHeight;
    }
}
