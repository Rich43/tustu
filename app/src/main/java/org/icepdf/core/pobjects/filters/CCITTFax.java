package org.icepdf.core.pobjects.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.icepdf.core.io.BitStream;
import org.icepdf.core.io.SequenceInputStream;
import org.icepdf.core.io.ZeroPaddedInputStream;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/CCITTFax.class */
public class CCITTFax {
    private static final short TIFF_COMPRESSION_NONE_default = 1;
    private static final short TIFF_COMPRESSION_GROUP3_1D = 2;
    private static final short TIFF_COMPRESSION_GROUP3_2D = 3;
    private static final short TIFF_COMPRESSION_GROUP4 = 4;
    private static final short TIFF_PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO_default = 0;
    private static final short TIFF_PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
    private static boolean USE_JAI_IMAGE_LIBRARY;
    private static Method jaiCreate;
    private static Method ssWrapInputStream;
    private static Method roGetAsBufferedImage;
    private static final Logger logger = Logger.getLogger(CCITTFax.class.toString());
    static final String[] _twcodes = {"00110101", "000111", "0111", "1000", "1011", "1100", "1110", "1111", "10011", "10100", "00111", "01000", "001000", "000011", "110100", "110101", "101010", "101011", "0100111", "0001100", "0001000", "0010111", "0000011", "0000100", "0101000", "0101011", "0010011", "0100100", "0011000", "00000010", "00000011", "00011010", "00011011", "00010010", "00010011", "00010100", "00010101", "00010110", "00010111", "00101000", "00101001", "00101010", "00101011", "00101100", "00101101", "00000100", "00000101", "00001010", "00001011", "01010010", "01010011", "01010100", "01010101", "00100100", "00100101", "01011000", "01011001", "01011010", "01011011", "01001010", "01001011", "00110010", "00110011", "00110100"};
    static final String[] _mwcodes = {"11011", "10010", "010111", "0110111", "00110110", "00110111", "01100100", "01100101", "01101000", "01100111", "011001100", "011001101", "011010010", "011010011", "011010100", "011010101", "011010110", "011010111", "011011000", "011011001", "011011010", "011011011", "010011000", "010011001", "010011010", "011000", "010011011"};
    static final String[] _tbcodes = {"0000110111", "010", "11", "10", "011", "0011", "0010", "00011", "000101", "000100", "0000100", "0000101", "0000111", "00000100", "00000111", "000011000", "0000010111", "0000011000", "0000001000", "00001100111", "00001101000", "00001101100", "00000110111", "00000101000", "00000010111", "00000011000", "000011001010", "000011001011", "000011001100", "000011001101", "000001101000", "000001101001", "000001101010", "000001101011", "000011010010", "000011010011", "000011010100", "000011010101", "000011010110", "000011010111", "000001101100", "000001101101", "000011011010", "000011011011", "000001010100", "000001010101", "000001010110", "000001010111", "000001100100", "000001100101", "000001010010", "000001010011", "000000100100", "000000110111", "000000111000", "000000100111", "000000101000", "000001011000", "000001011001", "000000101011", "000000101100", "000001011010", "000001100110", "000001100111"};
    static final String[] _mbcodes = {"0000001111", "000011001000", "000011001001", "000001011011", "000000110011", "000000110100", "000000110101", "0000001101100", "0000001101101", "0000001001010", "0000001001011", "0000001001100", "0000001001101", "0000001110010", "0000001110011", "0000001110100", "0000001110101", "0000001110110", "0000001110111", "0000001010010", "0000001010011", "0000001010100", "0000001010101", "0000001011010", "0000001011011", "0000001100100", "0000001100101"};
    static final String[] _extmcodes = {"00000001000", "00000001100", "00000001101", "000000010010", "000000010011", "000000010100", "000000010101", "000000010110", "000000010111", "000000011100", "000000011101", "000000011110", "000000011111"};
    static final String[] _modecodes = {"0001", "001", "1", "011", "000011", "0000011", "010", "000010", "0000010", "0000001111", "000000001111", "000000000001"};
    static final Code[][] twcodes = convertStringArrayToCodeArray2D(_twcodes);
    static final Code[][] mwcodes = convertStringArrayToCodeArray2D(_mwcodes);
    static final Code[][] tbcodes = convertStringArrayToCodeArray2D(_tbcodes);
    static final Code[][] mbcodes = convertStringArrayToCodeArray2D(_mbcodes);
    static final Code[][] extmcodes = convertStringArrayToCodeArray2D(_extmcodes);
    static final Code[][] modecodes = convertStringArrayToCodeArray2D(_modecodes);
    static int black = 0;
    static int white = 1;
    private static final String[] TIFF_COMPRESSION_NAMES = {"TIFF_COMPRESSION_NONE_default", "TIFF_COMPRESSION_GROUP3_1D", "TIFF_COMPRESSION_GROUP3_2D", "TIFF_COMPRESSION_GROUP4"};

    static {
        USE_JAI_IMAGE_LIBRARY = false;
        jaiCreate = null;
        ssWrapInputStream = null;
        roGetAsBufferedImage = null;
        try {
            Class<?> jaiClass = Class.forName("javax.media.jai.JAI");
            jaiCreate = jaiClass.getMethod("create", String.class, ParameterBlock.class);
            Class<?> ssClass = Class.forName("com.sun.media.jai.codec.SeekableStream");
            ssWrapInputStream = ssClass.getMethod("wrapInputStream", InputStream.class, Boolean.TYPE);
            Class<?> roClass = Class.forName("javax.media.jai.RenderedOp");
            roGetAsBufferedImage = roClass.getMethod("getAsBufferedImage", new Class[0]);
            USE_JAI_IMAGE_LIBRARY = true;
        } catch (Exception e2) {
        }
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/CCITTFax$Code.class */
    private static class Code {
        private long value = 0;
        private int length = 0;
        private int tablePosition;

        public Code() {
        }

        public Code(String strValue, int tablePosition) {
            this.tablePosition = tablePosition;
            for (int i2 = 0; i2 < strValue.length(); i2++) {
                append(strValue.charAt(i2) == '1');
            }
        }

        public final void append(boolean bit) {
            if (bit && this.length <= 63) {
                long mask = 1 << this.length;
                this.value |= mask;
            }
            this.length++;
        }

        public final boolean equals(Object ob) {
            if (ob instanceof Code) {
                Code c2 = (Code) ob;
                return this.value == c2.value && this.length == c2.length;
            }
            return false;
        }

        public final void reset() {
            this.value = 0L;
            this.length = 0;
        }

        public final int getLength() {
            return this.length;
        }

        public final int getTablePosition() {
            return this.tablePosition;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [org.icepdf.core.pobjects.filters.CCITTFax$Code[], org.icepdf.core.pobjects.filters.CCITTFax$Code[][]] */
    private static Code[][] convertStringArrayToCodeArray2D(String[] strArray) {
        int len = strArray.length;
        int[] codeLengths = new int[64];
        for (String str : strArray) {
            int entryLength = str.length();
            codeLengths[entryLength] = codeLengths[entryLength] + 1;
        }
        int largestLength = codeLengths.length - 1;
        while (largestLength > 0 && codeLengths[largestLength] == 0) {
            largestLength--;
        }
        ?? r0 = new Code[largestLength + 1];
        for (int i2 = 0; i2 < r0.length; i2++) {
            r0[i2] = new Code[codeLengths[i2]];
        }
        for (int i3 = 0; i3 < len; i3++) {
            Code[] codeArr = r0[strArray[i3].length()];
            int j2 = 0;
            while (true) {
                if (j2 >= codeArr.length) {
                    break;
                }
                if (codeArr[j2] != 0) {
                    j2++;
                } else {
                    codeArr[j2] = new Code(strArray[i3], i3);
                    break;
                }
            }
        }
        return r0;
    }

    private static int findPositionInTable(Code lookFor, Code[][] lookIn) {
        Code[] lookInWithSameLength;
        int lookForIndex = lookFor.getLength();
        if (lookForIndex >= lookIn.length || (lookInWithSameLength = lookIn[lookForIndex]) == null) {
            return -1;
        }
        for (Code potentialMatch : lookInWithSameLength) {
            if (lookFor.equals(potentialMatch)) {
                return potentialMatch.getTablePosition();
            }
        }
        return -1;
    }

    static int findWhite(BitStream inb, Code code) throws IOException {
        code.reset();
        while (!inb.atEndOfFile()) {
            int i2 = inb.getBits(1);
            code.append(i2 != 0);
            int j2 = findPositionInTable(code, twcodes);
            if (j2 >= 0) {
                return j2;
            }
            int j3 = findPositionInTable(code, mwcodes);
            if (j3 >= 0) {
                return (j3 + 1) * 64;
            }
            int j4 = findPositionInTable(code, extmcodes);
            if (j4 >= 0) {
                return 1792 + (j4 * 64);
            }
        }
        inb.close();
        return 0;
    }

    static int findBlack(BitStream inb, Code code) throws IOException {
        code.reset();
        while (!inb.atEndOfFile()) {
            int i2 = inb.getBits(1);
            code.append(i2 != 0);
            int j2 = findPositionInTable(code, tbcodes);
            if (j2 >= 0) {
                return j2;
            }
            int j3 = findPositionInTable(code, mbcodes);
            if (j3 >= 0) {
                return (j3 + 1) * 64;
            }
            int j4 = findPositionInTable(code, extmcodes);
            if (j4 >= 0) {
                return 1792 + (j4 * 64);
            }
        }
        inb.close();
        return 0;
    }

    static void addRun(int x2, G4State s2, BitStream out) throws IOException {
        s2.runLength += x2;
        int[] iArr = s2.cur;
        int i2 = s2.curIndex;
        s2.curIndex = i2 + 1;
        iArr[i2] = s2.runLength;
        s2.a0 += x2;
        if (s2.runLength > 0) {
            out.putRunBits(s2.white ? white : black, s2.runLength);
        }
        out.close();
        s2.runLength = 0;
    }

    static int readmode(BitStream inb, Code code) throws IOException {
        code.reset();
        while (!inb.atEndOfFile()) {
            int i2 = inb.getBits(1);
            code.append(i2 != 0);
            int j2 = findPositionInTable(code, modecodes);
            if (j2 >= 0) {
                return j2;
            }
        }
        inb.close();
        return -1;
    }

    static void detectB1(G4State s2) {
        if (s2.curIndex != 0) {
            while (s2.b1 <= s2.a0 && s2.b1 < s2.width) {
                int r2 = s2.ref[s2.refIndex] + s2.ref[s2.refIndex + 1];
                if (r2 == 0) {
                    s2.b1 = s2.width;
                }
                s2.b1 += r2;
                if (s2.refIndex + 2 < s2.ref.length) {
                    s2.refIndex += 2;
                }
            }
        }
    }

    static void decodePass(G4State s2) {
        detectB1(s2);
        int i2 = s2.b1;
        int[] iArr = s2.ref;
        int i3 = s2.refIndex;
        s2.refIndex = i3 + 1;
        s2.b1 = i2 + iArr[i3];
        s2.runLength += s2.b1 - s2.a0;
        s2.a0 = s2.b1;
        int i4 = s2.b1;
        int[] iArr2 = s2.ref;
        int i5 = s2.refIndex;
        s2.refIndex = i5 + 1;
        s2.b1 = i4 + iArr2[i5];
    }

    static void decodeHorizontal(BitStream in, BitStream out, G4State s2, Code code) throws IOException {
        int rl;
        do {
            rl = s2.white ? findWhite(in, code) : findBlack(in, code);
            if (rl >= 0) {
                if (rl < 64) {
                    addRun(rl + s2.longrun, s2, out);
                    s2.white = !s2.white;
                    s2.longrun = 0;
                } else {
                    s2.longrun += rl;
                }
            } else {
                addRun(rl, s2, out);
            }
        } while (rl >= 64);
        out.close();
    }

    static void resetRuns(BitStream outb, G4State state) throws IOException {
        state.white = true;
        addRun(0, state, outb);
        if (state.a0 != state.width) {
            while (state.a0 > state.width) {
                int i2 = state.a0;
                int[] iArr = state.cur;
                int i3 = state.curIndex - 1;
                state.curIndex = i3;
                state.a0 = i2 - iArr[i3];
            }
            if (state.a0 < state.width) {
                if (state.a0 < 0) {
                    state.a0 = 0;
                }
                if ((state.curIndex & 1) != 0) {
                    addRun(0, state, outb);
                }
                addRun(state.width - state.a0, state, outb);
            } else if (state.a0 > state.width) {
                addRun(state.width, state, outb);
                addRun(0, state, outb);
            }
        }
        int[] tmp = state.ref;
        state.ref = state.cur;
        state.cur = tmp;
        for (int i4 = state.curIndex; i4 < state.width; i4++) {
            state.ref[i4] = 0;
        }
        for (int i5 = 0; i5 < state.width; i5++) {
            state.cur[i5] = 0;
        }
        state.runLength = 0;
        state.a0 = 0;
        state.b1 = state.ref[0];
        state.refIndex = 1;
        state.curIndex = 0;
        outb.close();
    }

    public static void Group4Decode(InputStream in, OutputStream out, int width, boolean blackIs1) {
        BitStream inb = new BitStream(in);
        BitStream outb = new BitStream(out);
        black = 0;
        white = 1;
        if (blackIs1) {
            black = 1;
            white = 0;
        }
        Code code = new Code();
        try {
            G4State graphicState = new G4State(width);
            while (!inb.atEndOfFile()) {
                int mode = readmode(inb, code);
                switch (mode) {
                    case 0:
                        decodePass(graphicState);
                        continue;
                    case 1:
                        decodeHorizontal(inb, outb, graphicState, code);
                        decodeHorizontal(inb, outb, graphicState, code);
                        detectB1(graphicState);
                        break;
                    case 2:
                        detectB1(graphicState);
                        addRun(graphicState.b1 - graphicState.a0, graphicState, outb);
                        graphicState.white = !graphicState.white;
                        int i2 = graphicState.b1;
                        int[] iArr = graphicState.ref;
                        int i3 = graphicState.refIndex;
                        graphicState.refIndex = i3 + 1;
                        graphicState.b1 = i2 + iArr[i3];
                        break;
                    case 3:
                        detectB1(graphicState);
                        addRun((graphicState.b1 - graphicState.a0) + 1, graphicState, outb);
                        graphicState.white = !graphicState.white;
                        int i4 = graphicState.b1;
                        int[] iArr2 = graphicState.ref;
                        int i5 = graphicState.refIndex;
                        graphicState.refIndex = i5 + 1;
                        graphicState.b1 = i4 + iArr2[i5];
                        break;
                    case 4:
                        detectB1(graphicState);
                        addRun((graphicState.b1 - graphicState.a0) + 2, graphicState, outb);
                        graphicState.white = !graphicState.white;
                        int i6 = graphicState.b1;
                        int[] iArr3 = graphicState.ref;
                        int i7 = graphicState.refIndex;
                        graphicState.refIndex = i7 + 1;
                        graphicState.b1 = i6 + iArr3[i7];
                        break;
                    case 5:
                        detectB1(graphicState);
                        addRun((graphicState.b1 - graphicState.a0) + 3, graphicState, outb);
                        graphicState.white = !graphicState.white;
                        int i8 = graphicState.b1;
                        int[] iArr4 = graphicState.ref;
                        int i9 = graphicState.refIndex;
                        graphicState.refIndex = i9 + 1;
                        graphicState.b1 = i8 + iArr4[i9];
                        break;
                    case 6:
                        detectB1(graphicState);
                        addRun((graphicState.b1 - graphicState.a0) - 1, graphicState, outb);
                        graphicState.white = !graphicState.white;
                        if (graphicState.refIndex > 0) {
                            int i10 = graphicState.b1;
                            int[] iArr5 = graphicState.ref;
                            int i11 = graphicState.refIndex - 1;
                            graphicState.refIndex = i11;
                            graphicState.b1 = i10 - iArr5[i11];
                            break;
                        }
                        break;
                    case 7:
                        detectB1(graphicState);
                        addRun((graphicState.b1 - graphicState.a0) - 2, graphicState, outb);
                        graphicState.white = !graphicState.white;
                        if (graphicState.refIndex > 0) {
                            int i12 = graphicState.b1;
                            int[] iArr6 = graphicState.ref;
                            int i13 = graphicState.refIndex - 1;
                            graphicState.refIndex = i13;
                            graphicState.b1 = i12 - iArr6[i13];
                            break;
                        }
                        break;
                    case 8:
                        detectB1(graphicState);
                        addRun((graphicState.b1 - graphicState.a0) - 3, graphicState, outb);
                        graphicState.white = !graphicState.white;
                        if (graphicState.refIndex > 0) {
                            int i14 = graphicState.b1;
                            int[] iArr7 = graphicState.ref;
                            int i15 = graphicState.refIndex - 1;
                            graphicState.refIndex = i15;
                            graphicState.b1 = i14 - iArr7[i15];
                            break;
                        }
                        break;
                    case 11:
                        resetRuns(outb, graphicState);
                        break;
                }
                if (graphicState.a0 >= graphicState.width) {
                    resetRuns(outb, graphicState);
                }
            }
            inb.close();
            outb.close();
            in.close();
            out.close();
        } catch (Exception e2) {
            logger.log(Level.FINE, "Error decoding group4 CITTFax", (Throwable) e2);
        }
    }

    public static BufferedImage attemptDeriveBufferedImageFromBytes(ImageStream stream, Library library, HashMap streamDictionary, Color fill) throws IllegalAccessException, InvocationTargetException {
        BufferedImage img;
        int columns;
        if (!USE_JAI_IMAGE_LIBRARY) {
            return null;
        }
        boolean imageMask = stream.isImageMask();
        List decodeArray = (List) library.getObject(streamDictionary, ImageStream.DECODE_KEY);
        HashMap decodeParmsDictionary = library.getDictionary(streamDictionary, ImageStream.DECODEPARMS_KEY);
        boolean blackIs1 = stream.getBlackIs1(library, decodeParmsDictionary);
        if (!blackIs1) {
            blackIs1 = stream.getBlackIs1(library, streamDictionary);
        }
        float k2 = library.getFloat(decodeParmsDictionary, ImageStream.K_KEY);
        InputStream input = stream.getDecodedByteArrayInputStream();
        if (logger.isLoggable(Level.FINER)) {
            try {
                ImageInputStream imageInputStream = ImageIO.createImageInputStream(stream.getDecodedByteArrayInputStream());
                Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("TIFF");
                while (iter.hasNext()) {
                    ImageReader reader = iter.next();
                    logger.finer("CCITTFaxDecode Image reader: " + ((Object) reader) + " canReastRaster: " + reader.canReadRaster());
                }
                imageInputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (input == null) {
            return null;
        }
        InputStream input2 = new ZeroPaddedInputStream(input);
        BufferedInputStream bufferedInput = new BufferedInputStream(input2, 1024);
        bufferedInput.mark(4);
        try {
            int hb1 = bufferedInput.read();
            int hb2 = bufferedInput.read();
            bufferedInput.reset();
            if (hb1 < 0 || hb2 < 0) {
                input2.close();
                return null;
            }
            boolean hasHeader = (hb1 == 77 && hb2 == 77) || (hb1 == 73 && hb2 == 73);
            if (!hasHeader) {
                byte[] fakeHeaderBytes = {77, 77, 0, 42, 0, 0, 0, 8, 0, 12, 0, -2, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 1, 2, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 1, 3, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 1, 6, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 17, 0, 4, 0, 0, 0, 1, 0, 0, 0, -82, 1, 22, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 1, 23, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 1, 26, 0, 5, 0, 0, 0, 1, 0, 0, 0, -98, 1, 27, 0, 5, 0, 0, 0, 1, 0, 0, 0, -90, 1, 40, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1};
                boolean pdfStatesBlackAndWhite = false;
                if (blackIs1) {
                    pdfStatesBlackAndWhite = true;
                }
                int width = library.getInt(streamDictionary, ImageStream.WIDTH_KEY);
                int height = library.getInt(streamDictionary, ImageStream.HEIGHT_KEY);
                Object columnsObj = library.getObject(decodeParmsDictionary, ImageStream.COLUMNS_KEY);
                if (columnsObj != null && (columnsObj instanceof Number) && (columns = ((Number) columnsObj).intValue()) > width) {
                    width = columns;
                }
                Utils.setIntIntoByteArrayBE(width, fakeHeaderBytes, 30);
                Utils.setIntIntoByteArrayBE(height, fakeHeaderBytes, 42);
                Object bitsPerComponent = library.getObject(streamDictionary, ImageStream.BITSPERCOMPONENT_KEY);
                if (bitsPerComponent != null && (bitsPerComponent instanceof Number)) {
                    Utils.setShortIntoByteArrayBE(((Number) bitsPerComponent).shortValue(), fakeHeaderBytes, 54);
                }
                short compression = 1;
                if (k2 < 0.0f) {
                    compression = 4;
                } else if (k2 > 0.0f) {
                    compression = 3;
                } else if (k2 == 0.0f) {
                    compression = 2;
                }
                Utils.setShortIntoByteArrayBE(compression, fakeHeaderBytes, 66);
                short photometricInterpretation = 0;
                if (pdfStatesBlackAndWhite && !blackIs1) {
                    photometricInterpretation = 1;
                }
                Utils.setShortIntoByteArrayBE(photometricInterpretation, fakeHeaderBytes, 78);
                Utils.setIntIntoByteArrayBE(height, fakeHeaderBytes, 102);
                int lengthOfCompressedData = 2147483646;
                Object lengthValue = library.getObject(streamDictionary, Stream.LENGTH_KEY);
                if (lengthValue != null && (lengthValue instanceof Number)) {
                    lengthOfCompressedData = ((Number) lengthValue).intValue();
                } else {
                    int approxLen = width * height;
                    if (approxLen > 0) {
                        lengthOfCompressedData = approxLen;
                    }
                }
                Utils.setIntIntoByteArrayBE(lengthOfCompressedData, fakeHeaderBytes, 114);
                ByteArrayInputStream fakeHeaderBytesIn = new ByteArrayInputStream(fakeHeaderBytes);
                SequenceInputStream sin = new SequenceInputStream(fakeHeaderBytesIn, bufferedInput);
                img = deriveBufferedImageFromTIFFBytes(sin, library, lengthOfCompressedData, width, height);
                if (img == null) {
                    for (int i2 = 1; i2 <= 2; i2++) {
                        compression = (short) (compression + 1);
                        if (compression > 4) {
                            compression = 2;
                        }
                        Utils.setShortIntoByteArrayBE(compression, fakeHeaderBytes, 66);
                        InputStream input3 = stream.getDecodedByteArrayInputStream();
                        if (input3 == null) {
                            return null;
                        }
                        InputStream input4 = new ZeroPaddedInputStream(input3);
                        ByteArrayInputStream fakeHeaderBytesIn2 = new ByteArrayInputStream(fakeHeaderBytes);
                        SequenceInputStream sin2 = new SequenceInputStream(fakeHeaderBytesIn2, input4);
                        img = deriveBufferedImageFromTIFFBytes(sin2, library, lengthOfCompressedData, width, height);
                        if (img != null) {
                            break;
                        }
                    }
                }
            } else {
                int width2 = library.getInt(streamDictionary, ImageStream.WIDTH_KEY);
                int height2 = library.getInt(streamDictionary, ImageStream.HEIGHT_KEY);
                img = deriveBufferedImageFromTIFFBytes(bufferedInput, library, width2 * height2, width2, height2);
            }
            if (img != null) {
                img = applyImageMaskAndDecodeArray(img, imageMask, Boolean.valueOf(blackIs1), decodeArray, fill);
            }
            return img;
        } catch (IOException e3) {
            try {
                input2.close();
                return null;
            } catch (IOException e4) {
                return null;
            }
        }
    }

    private static BufferedImage deriveBufferedImageFromTIFFBytes(InputStream in, Library library, int compressedBytes, int width, int height) throws IllegalAccessException, InvocationTargetException {
        BufferedImage img = null;
        try {
            Object com_sun_media_jai_codec_SeekableStream_s = ssWrapInputStream.invoke(null, in, Boolean.TRUE);
            ParameterBlock pb = new ParameterBlock();
            pb.add(com_sun_media_jai_codec_SeekableStream_s);
            Object javax_media_jai_RenderedOp_op = jaiCreate.invoke(null, "tiff", pb);
            if (javax_media_jai_RenderedOp_op != null) {
                RenderedImage ri = (RenderedImage) javax_media_jai_RenderedOp_op;
                Raster r2 = ri.getTile(0, 0);
                if (r2 instanceof WritableRaster) {
                    ColorModel cm = ri.getColorModel();
                    img = new BufferedImage(cm, (WritableRaster) r2, false, (Hashtable<?, ?>) null);
                } else {
                    img = (BufferedImage) roGetAsBufferedImage.invoke(javax_media_jai_RenderedOp_op, new Object[0]);
                }
            }
            return img;
        } finally {
            try {
                in.close();
            } catch (IOException e2) {
            }
        }
    }

    private static BufferedImage applyImageMaskAndDecodeArray(BufferedImage img, boolean imageMask, Boolean blackIs1, List decode, Color fill) {
        ColorModel cm = img.getColorModel();
        if ((cm instanceof IndexColorModel) && cm.getPixelSize() == 1) {
            boolean defaultDecode = decode == null || 0.0f == ((Number) decode.get(0)).floatValue();
            boolean flag = (blackIs1 == null && !defaultDecode) || (blackIs1 != null && blackIs1.booleanValue() && decode == null);
            if (imageMask) {
                int[] cmap = new int[2];
                cmap[0] = flag ? fill.getRGB() : 16777215;
                cmap[1] = flag ? 16777215 : fill.getRGB();
                int transparentIndex = flag ? 1 : 0;
                IndexColorModel icm = new IndexColorModel(cm.getPixelSize(), cmap.length, cmap, 0, true, transparentIndex, cm.getTransferType());
                img = new BufferedImage(icm, img.getRaster(), img.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
            } else {
                int[] cmap2 = new int[2];
                cmap2[0] = flag ? -16777216 : -1;
                cmap2[1] = flag ? -1 : -16777216;
                IndexColorModel icm2 = new IndexColorModel(cm.getPixelSize(), cmap2.length, cmap2, 0, false, -1, cm.getTransferType());
                img = new BufferedImage(icm2, img.getRaster(), img.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
            }
        }
        return img;
    }
}
