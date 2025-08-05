package com.sun.xml.internal.fastinfoset.algorithm;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/algorithm/FloatEncodingAlgorithm.class */
public class FloatEncodingAlgorithm extends IEEE754FloatingPointEncodingAlgorithm {
    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 4 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfFloat", new Object[]{4}));
        }
        return octetLength / 4;
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 4;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b2, int start, int length) throws EncodingAlgorithmException {
        float[] data = new float[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToFloatArray(data, 0, b2, start, length);
        return data;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s2) throws IOException {
        return decodeFromInputStreamToFloatArray(s2);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s2) throws IOException {
        if (!(data instanceof float[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotFloat"));
        }
        float[] fdata = (float[]) data;
        encodeToOutputStreamFromFloatArray(fdata, s2);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List floatList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.internal.fastinfoset.algorithm.FloatEncodingAlgorithm.1
            @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String fStringValue = cb.subSequence(start2, end).toString();
                floatList.add(Float.valueOf(fStringValue));
            }
        });
        return generateArrayFromList(floatList);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s2) {
        if (!(data instanceof float[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotFloat"));
        }
        float[] fdata = (float[]) data;
        convertToCharactersFromFloatArray(fdata, s2);
    }

    public final void decodeFromBytesToFloatArray(float[] data, int fstart, byte[] b2, int start, int length) {
        int size = length / 4;
        for (int i2 = 0; i2 < size; i2++) {
            int i3 = start;
            int start2 = start + 1;
            int start3 = start2 + 1;
            int i4 = ((b2[i3] & 255) << 24) | ((b2[start2] & 255) << 16);
            int start4 = start3 + 1;
            int i5 = i4 | ((b2[start3] & 255) << 8);
            start = start4 + 1;
            int bits = i5 | (b2[start4] & 255);
            int i6 = fstart;
            fstart++;
            data[i6] = Float.intBitsToFloat(bits);
        }
    }

    public final float[] decodeFromInputStreamToFloatArray(InputStream s2) throws IOException {
        List floatList = new ArrayList();
        byte[] b2 = new byte[4];
        while (true) {
            int n2 = s2.read(b2);
            if (n2 != 4) {
                if (n2 != -1) {
                    while (n2 != 4) {
                        int m2 = s2.read(b2, n2, 4 - n2);
                        if (m2 == -1) {
                            throw new EOFException();
                        }
                        n2 += m2;
                    }
                } else {
                    return generateArrayFromList(floatList);
                }
            }
            int bits = ((b2[0] & 255) << 24) | ((b2[1] & 255) << 16) | ((b2[2] & 255) << 8) | (b2[3] & 255);
            floatList.add(Float.valueOf(Float.intBitsToFloat(bits)));
        }
    }

    public final void encodeToOutputStreamFromFloatArray(float[] fdata, OutputStream s2) throws IOException {
        for (float f2 : fdata) {
            int bits = Float.floatToIntBits(f2);
            s2.write((bits >>> 24) & 255);
            s2.write((bits >>> 16) & 255);
            s2.write((bits >>> 8) & 255);
            s2.write(bits & 255);
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b2, int start) {
        encodeToBytesFromFloatArray((float[]) array, astart, alength, b2, start);
    }

    public final void encodeToBytesFromFloatArray(float[] fdata, int fstart, int flength, byte[] b2, int start) {
        int fend = fstart + flength;
        for (int i2 = fstart; i2 < fend; i2++) {
            int bits = Float.floatToIntBits(fdata[i2]);
            int i3 = start;
            int start2 = start + 1;
            b2[i3] = (byte) ((bits >>> 24) & 255);
            int start3 = start2 + 1;
            b2[start2] = (byte) ((bits >>> 16) & 255);
            int start4 = start3 + 1;
            b2[start3] = (byte) ((bits >>> 8) & 255);
            start = start4 + 1;
            b2[start4] = (byte) (bits & 255);
        }
    }

    public final void convertToCharactersFromFloatArray(float[] fdata, StringBuffer s2) {
        int end = fdata.length - 1;
        for (int i2 = 0; i2 <= end; i2++) {
            s2.append(Float.toString(fdata[i2]));
            if (i2 != end) {
                s2.append(' ');
            }
        }
    }

    public final float[] generateArrayFromList(List array) {
        float[] fdata = new float[array.size()];
        for (int i2 = 0; i2 < fdata.length; i2++) {
            fdata[i2] = ((Float) array.get(i2)).floatValue();
        }
        return fdata;
    }
}
