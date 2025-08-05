package com.sun.javafx.iio.common;

import java.nio.ByteBuffer;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/common/SmoothMinifier.class */
public class SmoothMinifier implements PushbroomScaler {
    protected int sourceWidth;
    protected int sourceHeight;
    protected int numBands;
    protected int destWidth;
    protected int destHeight;
    protected double scaleY;
    protected ByteBuffer destBuf;
    protected int boxHeight;
    protected byte[][] sourceData;
    protected int[] leftPoints;
    protected int[] rightPoints;
    protected int[] topPoints;
    protected int[] bottomPoints;
    protected int sourceLine;
    protected int sourceDataLine;
    protected int destLine;
    protected int[] tmpBuf;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SmoothMinifier.class.desiredAssertionStatus();
    }

    SmoothMinifier(int sourceWidth, int sourceHeight, int numBands, int destWidth, int destHeight) {
        if (sourceWidth <= 0 || sourceHeight <= 0 || numBands <= 0 || destWidth <= 0 || destHeight <= 0 || destWidth > sourceWidth || destHeight > sourceHeight) {
            throw new IllegalArgumentException();
        }
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
        this.numBands = numBands;
        this.destWidth = destWidth;
        this.destHeight = destHeight;
        this.destBuf = ByteBuffer.wrap(new byte[destHeight * destWidth * numBands]);
        double scaleX = sourceWidth / destWidth;
        this.scaleY = sourceHeight / destHeight;
        int boxWidth = ((sourceWidth + destWidth) - 1) / destWidth;
        this.boxHeight = ((sourceHeight + destHeight) - 1) / destHeight;
        int boxLeft = boxWidth / 2;
        int boxRight = (boxWidth - boxLeft) - 1;
        int boxTop = this.boxHeight / 2;
        int boxBottom = (this.boxHeight - boxTop) - 1;
        this.sourceData = new byte[this.boxHeight][destWidth * numBands];
        this.leftPoints = new int[destWidth];
        this.rightPoints = new int[destWidth];
        for (int dx = 0; dx < destWidth; dx++) {
            int sx = (int) (dx * scaleX);
            this.leftPoints[dx] = sx - boxLeft;
            this.rightPoints[dx] = sx + boxRight;
        }
        this.topPoints = new int[destHeight];
        this.bottomPoints = new int[destHeight];
        for (int dy = 0; dy < destHeight; dy++) {
            int sy = (int) (dy * this.scaleY);
            this.topPoints[dy] = sy - boxTop;
            this.bottomPoints[dy] = sy + boxBottom;
        }
        this.sourceLine = 0;
        this.sourceDataLine = 0;
        this.destLine = 0;
        this.tmpBuf = new int[destWidth * numBands];
    }

    @Override // com.sun.javafx.iio.common.PushbroomScaler
    public ByteBuffer getDestination() {
        return this.destBuf;
    }

    @Override // com.sun.javafx.iio.common.PushbroomScaler
    public boolean putSourceScanline(byte[] scanline, int off) {
        int i2;
        int i3;
        int index;
        int i4;
        int i5;
        if (off < 0) {
            throw new IllegalArgumentException("off < 0!");
        }
        if (this.numBands == 1) {
            int leftSample = scanline[off] & 255;
            int rightSample = scanline[(off + this.sourceWidth) - 1] & 255;
            for (int i6 = 0; i6 < this.destWidth; i6++) {
                int val = 0;
                int rightBound = this.rightPoints[i6];
                for (int j2 = this.leftPoints[i6]; j2 <= rightBound; j2++) {
                    if (j2 < 0) {
                        i4 = val;
                        i5 = leftSample;
                    } else if (j2 >= this.sourceWidth) {
                        i4 = val;
                        i5 = rightSample;
                    } else {
                        i4 = val;
                        i5 = scanline[off + j2] & 255;
                    }
                    val = i4 + i5;
                }
                this.sourceData[this.sourceDataLine][i6] = (byte) (val / ((rightBound - this.leftPoints[i6]) + 1));
            }
        } else {
            int rightOff = off + ((this.sourceWidth - 1) * this.numBands);
            for (int i7 = 0; i7 < this.destWidth; i7++) {
                int leftBound = this.leftPoints[i7];
                int rightBound2 = this.rightPoints[i7];
                int numPoints = (rightBound2 - leftBound) + 1;
                int iBands = i7 * this.numBands;
                for (int k2 = 0; k2 < this.numBands; k2++) {
                    int leftSample2 = scanline[off + k2] & 255;
                    int rightSample2 = scanline[rightOff + k2] & 255;
                    int val2 = 0;
                    for (int j3 = leftBound; j3 <= rightBound2; j3++) {
                        if (j3 < 0) {
                            i2 = val2;
                            i3 = leftSample2;
                        } else if (j3 >= this.sourceWidth) {
                            i2 = val2;
                            i3 = rightSample2;
                        } else {
                            i2 = val2;
                            i3 = scanline[off + (j3 * this.numBands) + k2] & 255;
                        }
                        val2 = i2 + i3;
                    }
                    this.sourceData[this.sourceDataLine][iBands + k2] = (byte) (val2 / numPoints);
                }
            }
        }
        if (this.sourceLine == this.bottomPoints[this.destLine] || (this.destLine == this.destHeight - 1 && this.sourceLine == this.sourceHeight - 1)) {
            if (!$assertionsDisabled && !this.destBuf.hasArray()) {
                throw new AssertionError((Object) "destBuf.hasArray() == false => destBuf is direct");
            }
            byte[] dest = this.destBuf.array();
            int destOffset = this.destLine * this.destWidth * this.numBands;
            Arrays.fill(this.tmpBuf, 0);
            for (int y2 = this.topPoints[this.destLine]; y2 <= this.bottomPoints[this.destLine]; y2++) {
                if (y2 < 0) {
                    index = (0 - this.sourceLine) + this.sourceDataLine;
                } else if (y2 >= this.sourceHeight) {
                    index = (((this.sourceHeight - 1) - this.sourceLine) + this.sourceDataLine) % this.boxHeight;
                } else {
                    index = ((y2 - this.sourceLine) + this.sourceDataLine) % this.boxHeight;
                }
                if (index < 0) {
                    index += this.boxHeight;
                }
                byte[] b2 = this.sourceData[index];
                int destLen = b2.length;
                for (int x2 = 0; x2 < destLen; x2++) {
                    int[] iArr = this.tmpBuf;
                    int i8 = x2;
                    iArr[i8] = iArr[i8] + (b2[x2] & 255);
                }
            }
            int sourceLen = this.tmpBuf.length;
            for (int x3 = 0; x3 < sourceLen; x3++) {
                dest[destOffset + x3] = (byte) (this.tmpBuf[x3] / this.boxHeight);
            }
            if (this.destLine < this.destHeight - 1) {
                this.destLine++;
            }
        }
        int i9 = this.sourceLine + 1;
        this.sourceLine = i9;
        if (i9 != this.sourceHeight) {
            this.sourceDataLine = (this.sourceDataLine + 1) % this.boxHeight;
        }
        return this.destLine == this.destHeight;
    }
}
