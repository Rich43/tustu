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

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/algorithm/ShortEncodingAlgorithm.class */
public class ShortEncodingAlgorithm extends IntegerEncodingAlgorithm {
    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 2 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfShort", new Object[]{2}));
        }
        return octetLength / 2;
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 2;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b2, int start, int length) throws EncodingAlgorithmException {
        short[] data = new short[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToShortArray(data, 0, b2, start, length);
        return data;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s2) throws IOException {
        return decodeFromInputStreamToShortArray(s2);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s2) throws IOException {
        if (!(data instanceof short[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotShortArray"));
        }
        short[] idata = (short[]) data;
        encodeToOutputStreamFromShortArray(idata, s2);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List shortList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.internal.fastinfoset.algorithm.ShortEncodingAlgorithm.1
            @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String iStringValue = cb.subSequence(start2, end).toString();
                shortList.add(Short.valueOf(iStringValue));
            }
        });
        return generateArrayFromList(shortList);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s2) {
        if (!(data instanceof short[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotShortArray"));
        }
        short[] idata = (short[]) data;
        convertToCharactersFromShortArray(idata, s2);
    }

    public final void decodeFromBytesToShortArray(short[] sdata, int istart, byte[] b2, int start, int length) {
        int size = length / 2;
        for (int i2 = 0; i2 < size; i2++) {
            int i3 = istart;
            istart++;
            int i4 = start;
            int start2 = start + 1;
            start = start2 + 1;
            sdata[i3] = (short) (((b2[i4] & 255) << 8) | (b2[start2] & 255));
        }
    }

    public final short[] decodeFromInputStreamToShortArray(InputStream s2) throws IOException {
        List shortList = new ArrayList();
        byte[] b2 = new byte[2];
        while (true) {
            int n2 = s2.read(b2);
            if (n2 != 2) {
                if (n2 != -1) {
                    while (n2 != 2) {
                        int m2 = s2.read(b2, n2, 2 - n2);
                        if (m2 == -1) {
                            throw new EOFException();
                        }
                        n2 += m2;
                    }
                } else {
                    return generateArrayFromList(shortList);
                }
            }
            int i2 = ((b2[0] & 255) << 8) | (b2[1] & 255);
            shortList.add(Short.valueOf((short) i2));
        }
    }

    public final void encodeToOutputStreamFromShortArray(short[] idata, OutputStream s2) throws IOException {
        for (short s3 : idata) {
            s2.write((s3 >>> 8) & 255);
            s2.write(s3 & 255);
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b2, int start) {
        encodeToBytesFromShortArray((short[]) array, astart, alength, b2, start);
    }

    public final void encodeToBytesFromShortArray(short[] sdata, int istart, int ilength, byte[] b2, int start) {
        int iend = istart + ilength;
        for (int i2 = istart; i2 < iend; i2++) {
            short bits = sdata[i2];
            int i3 = start;
            int start2 = start + 1;
            b2[i3] = (byte) ((bits >>> 8) & 255);
            start = start2 + 1;
            b2[start2] = (byte) (bits & 255);
        }
    }

    public final void convertToCharactersFromShortArray(short[] sdata, StringBuffer s2) {
        int end = sdata.length - 1;
        for (int i2 = 0; i2 <= end; i2++) {
            s2.append(Short.toString(sdata[i2]));
            if (i2 != end) {
                s2.append(' ');
            }
        }
    }

    public final short[] generateArrayFromList(List array) {
        short[] sdata = new short[array.size()];
        for (int i2 = 0; i2 < sdata.length; i2++) {
            sdata[i2] = ((Short) array.get(i2)).shortValue();
        }
        return sdata;
    }
}
