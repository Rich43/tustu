package org.icepdf.core.pobjects.filters;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.InflaterInputStream;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/FlateDecode.class */
public class FlateDecode extends ChunkingInputStream {
    private static int DEFAULT_BUFFER_SIZE = Defs.sysPropertyInt("org.icepdf.core.flateDecode.bufferSize", 16384);
    public static final Name DECODE_PARMS_VALUE = new Name(PdfOps.DP_NAME);
    public static final Name PREDICTOR_VALUE = new Name("Predictor");
    public static final Name WIDTH_VALUE = new Name("Width");
    public static final Name COLUMNS_VALUE = new Name("Columns");
    public static final Name COLORS_VALUE = new Name("Colors");
    public static final Name BITS_PER_COMPONENT_VALUE = new Name(PdfOps.BPC_NAME);
    private InputStream originalInputKeptSolelyForDebugging;
    private int width;
    private int numComponents;
    private int bitsPerComponent;
    private int bpp;
    private int predictor;

    public FlateDecode(Library library, HashMap props, InputStream input) {
        this.bpp = 1;
        this.originalInputKeptSolelyForDebugging = input;
        this.width = 0;
        this.numComponents = 0;
        this.bitsPerComponent = 0;
        this.bpp = 1;
        int intermediateBufferSize = DEFAULT_BUFFER_SIZE;
        HashMap decodeParmsDictionary = library.getDictionary(props, DECODE_PARMS_VALUE);
        this.predictor = library.getInt(decodeParmsDictionary, PREDICTOR_VALUE);
        if (this.predictor != 1 && this.predictor != 2 && this.predictor != 10 && this.predictor != 11 && this.predictor != 12 && this.predictor != 13 && this.predictor != 14 && this.predictor != 15) {
            this.predictor = 1;
        }
        if (this.predictor != 1) {
            Number widthNumber = library.getNumber(props, WIDTH_VALUE);
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
            this.bpp = Math.max(1, Utils.numBytesToHoldBits(this.numComponents * this.bitsPerComponent));
            intermediateBufferSize = Utils.numBytesToHoldBits(this.width * this.numComponents * this.bitsPerComponent);
        }
        setInputStream(new InflaterInputStream(input));
        setBufferSize(intermediateBufferSize);
    }

    @Override // org.icepdf.core.pobjects.filters.ChunkingInputStream
    protected int fillInternalBuffer() throws IOException {
        int numRead;
        if (this.predictor == 1) {
            int numRead2 = fillBufferFromInputStream();
            if (numRead2 <= 0) {
                return -1;
            }
            return numRead2;
        }
        if (this.predictor == 2) {
            int numRead3 = fillBufferFromInputStream();
            if (numRead3 <= 0) {
                return -1;
            }
            if (this.bitsPerComponent == 8) {
                for (int i2 = 0; i2 < numRead3; i2++) {
                    int prevIndex = i2 - this.numComponents;
                    if (prevIndex >= 0) {
                        byte[] bArr = this.buffer;
                        int i3 = i2;
                        bArr[i3] = (byte) (bArr[i3] + this.buffer[prevIndex]);
                    }
                }
            }
            return numRead3;
        }
        if (this.predictor < 10 || this.predictor > 15 || (numRead = fillBufferFromInputStream()) <= 0) {
            return -1;
        }
        return numRead;
    }

    @Override // org.icepdf.core.pobjects.filters.ChunkingInputStream
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(", orig: ");
        if (this.originalInputKeptSolelyForDebugging == null) {
            sb.append(FXMLLoader.NULL_KEYWORD);
        } else {
            sb.append(this.originalInputKeptSolelyForDebugging.toString());
        }
        return sb.toString();
    }
}
