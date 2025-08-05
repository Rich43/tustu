package org.icepdf.core.pobjects.filters;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/PredictorDecode.class */
public class PredictorDecode extends ChunkingInputStream {
    protected static final int PREDICTOR_NONE = 1;
    protected static final int PREDICTOR_TIFF_2 = 2;
    protected static final int PREDICTOR_PNG_NONE = 10;
    protected static final int PREDICTOR_PNG_SUB = 11;
    protected static final int PREDICTOR_PNG_UP = 12;
    protected static final int PREDICTOR_PNG_AVG = 13;
    protected static final int PREDICTOR_PNG_PAETH = 14;
    protected static final int PREDICTOR_PNG_OPTIMUM = 15;
    protected static final Name DECODE_PARMS_VALUE = new Name(PdfOps.DP_NAME);
    protected static final Name PREDICTOR_VALUE = new Name("Predictor");
    protected static final Name WIDTH_VALUE = new Name("Width");
    protected static final Name COLUMNS_VALUE = new Name("Columns");
    protected static final Name COLORS_VALUE = new Name("Colors");
    protected static final Name BITS_PER_COMPONENT_VALUE = new Name(PdfOps.BPC_NAME);
    protected static final Name EARLY_CHANGE_VALUE = new Name("EarlyChange");
    protected int predictor;
    protected int numComponents;
    protected int bitsPerComponent;
    protected int width;
    protected int bytesPerPixel;
    protected byte[] aboveBuffer;

    public PredictorDecode(InputStream input, Library library, HashMap entries) {
        this.bytesPerPixel = 1;
        HashMap decodeParmsDictionary = library.getDictionary(entries, DECODE_PARMS_VALUE);
        this.predictor = library.getInt(decodeParmsDictionary, PREDICTOR_VALUE);
        Number widthNumber = library.getNumber(entries, WIDTH_VALUE);
        if (widthNumber != null) {
            this.width = widthNumber.intValue();
        } else {
            this.width = library.getInt(decodeParmsDictionary, COLUMNS_VALUE);
        }
        this.numComponents = 1;
        this.bitsPerComponent = 8;
        Object numComponentsDecodeParmsObj = library.getObject(decodeParmsDictionary, COLORS_VALUE);
        if (numComponentsDecodeParmsObj instanceof Number) {
            this.numComponents = ((Number) numComponentsDecodeParmsObj).intValue();
        }
        Object bitsPerComponentDecodeParmsObj = library.getObject(decodeParmsDictionary, BITS_PER_COMPONENT_VALUE);
        if (bitsPerComponentDecodeParmsObj instanceof Number) {
            this.bitsPerComponent = ((Number) bitsPerComponentDecodeParmsObj).intValue();
        }
        this.bytesPerPixel = Math.max(1, Utils.numBytesToHoldBits(this.numComponents * this.bitsPerComponent));
        int intermediateBufferSize = Utils.numBytesToHoldBits(this.width * this.numComponents * this.bitsPerComponent);
        this.aboveBuffer = new byte[intermediateBufferSize];
        setBufferSize(intermediateBufferSize);
        setInputStream(input);
    }

    @Override // org.icepdf.core.pobjects.filters.ChunkingInputStream
    protected int fillInternalBuffer() throws IOException {
        byte[] temp = this.aboveBuffer;
        this.aboveBuffer = this.buffer;
        this.buffer = temp;
        int cp = this.in.read();
        if (cp < 0) {
            return -1;
        }
        int currPredictor = cp + 10;
        int numRead = fillBufferFromInputStream();
        if (numRead <= 0) {
            return -1;
        }
        applyPredictor(numRead, currPredictor);
        return numRead;
    }

    protected void applyPredictor(int numRead, int currPredictor) {
        for (int i2 = 0; i2 < numRead && currPredictor != 10; i2++) {
            if (currPredictor == 11) {
                if (i2 - this.bytesPerPixel >= 0) {
                    byte[] bArr = this.buffer;
                    int i3 = i2;
                    bArr[i3] = (byte) (bArr[i3] + applyLeftPredictor(this.buffer, this.bytesPerPixel, i2));
                }
            } else if (currPredictor == 12) {
                if (this.aboveBuffer != null) {
                    byte[] bArr2 = this.buffer;
                    int i4 = i2;
                    bArr2[i4] = (byte) (bArr2[i4] + applyAbovePredictor(this.aboveBuffer, i2));
                }
            } else if (currPredictor == 13) {
                int left = 0;
                if (i2 - this.bytesPerPixel >= 0) {
                    left = applyLeftPredictor(this.buffer, this.bytesPerPixel, i2);
                }
                int above = 0;
                if (this.aboveBuffer != null) {
                    above = applyAbovePredictor(this.aboveBuffer, i2);
                }
                int sum = left + above;
                byte avg = (byte) ((sum >>> 1) & 255);
                byte[] bArr3 = this.buffer;
                int i5 = i2;
                bArr3[i5] = (byte) (bArr3[i5] + avg);
            } else if (currPredictor == 14) {
                int left2 = 0;
                if (i2 - this.bytesPerPixel >= 0) {
                    left2 = applyLeftPredictor(this.buffer, this.bytesPerPixel, i2);
                }
                int above2 = 0;
                if (this.aboveBuffer != null) {
                    above2 = applyAbovePredictor(this.aboveBuffer, i2);
                }
                int aboveLeft = 0;
                if (i2 - this.bytesPerPixel >= 0 && this.aboveBuffer != null) {
                    aboveLeft = applyAboveLeftPredictor(this.aboveBuffer, this.bytesPerPixel, i2);
                }
                int p2 = (left2 + above2) - aboveLeft;
                int pLeft = Math.abs(p2 - left2);
                int pAbove = Math.abs(p2 - above2);
                int pAboveLeft = Math.abs(p2 - aboveLeft);
                int paeth = (pLeft > pAbove || pLeft > pAboveLeft) ? pAbove <= pAboveLeft ? above2 : aboveLeft : left2;
                byte[] bArr4 = this.buffer;
                int i6 = i2;
                bArr4[i6] = (byte) (bArr4[i6] + ((byte) (paeth & 255)));
            }
        }
    }

    private static int applyLeftPredictor(byte[] buffer, int bytesPerPixel, int i2) {
        return buffer[i2 - bytesPerPixel] & 255;
    }

    private static int applyAbovePredictor(byte[] aboveBuffer, int i2) {
        return aboveBuffer[i2] & 255;
    }

    private static int applyAboveLeftPredictor(byte[] aboveBuffer, int bytesPerPixel, int i2) {
        return aboveBuffer[i2 - bytesPerPixel] & 255;
    }

    public static boolean isPredictor(Library library, HashMap entries) {
        HashMap decodeParmsDictionary = library.getDictionary(entries, DECODE_PARMS_VALUE);
        if (decodeParmsDictionary == null) {
            return false;
        }
        int predictor = library.getInt(decodeParmsDictionary, PREDICTOR_VALUE);
        if (predictor != 10 && predictor != 11 && predictor != 12 && predictor != 13 && predictor != 14 && predictor != 15) {
            return false;
        }
        return true;
    }
}
