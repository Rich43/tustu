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

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/algorithm/IntEncodingAlgorithm.class */
public class IntEncodingAlgorithm extends IntegerEncodingAlgorithm {
    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 4 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfInt", new Object[]{4}));
        }
        return octetLength / 4;
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 4;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b2, int start, int length) throws EncodingAlgorithmException {
        int[] data = new int[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToIntArray(data, 0, b2, start, length);
        return data;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s2) throws IOException {
        return decodeFromInputStreamToIntArray(s2);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s2) throws IOException {
        if (!(data instanceof int[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotIntArray"));
        }
        int[] idata = (int[]) data;
        encodeToOutputStreamFromIntArray(idata, s2);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List integerList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.internal.fastinfoset.algorithm.IntEncodingAlgorithm.1
            @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String iStringValue = cb.subSequence(start2, end).toString();
                integerList.add(Integer.valueOf(iStringValue));
            }
        });
        return generateArrayFromList(integerList);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s2) {
        if (!(data instanceof int[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotIntArray"));
        }
        int[] idata = (int[]) data;
        convertToCharactersFromIntArray(idata, s2);
    }

    public final void decodeFromBytesToIntArray(int[] idata, int istart, byte[] b2, int start, int length) {
        int size = length / 4;
        for (int i2 = 0; i2 < size; i2++) {
            int i3 = istart;
            istart++;
            int i4 = start;
            int start2 = start + 1;
            int start3 = start2 + 1;
            int i5 = ((b2[i4] & 255) << 24) | ((b2[start2] & 255) << 16);
            int start4 = start3 + 1;
            int i6 = i5 | ((b2[start3] & 255) << 8);
            start = start4 + 1;
            idata[i3] = i6 | (b2[start4] & 255);
        }
    }

    public final int[] decodeFromInputStreamToIntArray(InputStream s2) throws IOException {
        List integerList = new ArrayList();
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
                    return generateArrayFromList(integerList);
                }
            }
            int i2 = ((b2[0] & 255) << 24) | ((b2[1] & 255) << 16) | ((b2[2] & 255) << 8) | (b2[3] & 255);
            integerList.add(Integer.valueOf(i2));
        }
    }

    public final void encodeToOutputStreamFromIntArray(int[] idata, OutputStream s2) throws IOException {
        for (int bits : idata) {
            s2.write((bits >>> 24) & 255);
            s2.write((bits >>> 16) & 255);
            s2.write((bits >>> 8) & 255);
            s2.write(bits & 255);
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b2, int start) {
        encodeToBytesFromIntArray((int[]) array, astart, alength, b2, start);
    }

    public final void encodeToBytesFromIntArray(int[] idata, int istart, int ilength, byte[] b2, int start) {
        int iend = istart + ilength;
        for (int i2 = istart; i2 < iend; i2++) {
            int bits = idata[i2];
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

    public final void convertToCharactersFromIntArray(int[] idata, StringBuffer s2) {
        int end = idata.length - 1;
        for (int i2 = 0; i2 <= end; i2++) {
            s2.append(Integer.toString(idata[i2]));
            if (i2 != end) {
                s2.append(' ');
            }
        }
    }

    public final int[] generateArrayFromList(List array) {
        int[] idata = new int[array.size()];
        for (int i2 = 0; i2 < idata.length; i2++) {
            idata[i2] = ((Integer) array.get(i2)).intValue();
        }
        return idata;
    }
}
