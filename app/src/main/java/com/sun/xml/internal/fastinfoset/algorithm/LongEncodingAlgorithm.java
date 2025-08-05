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

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/algorithm/LongEncodingAlgorithm.class */
public class LongEncodingAlgorithm extends IntegerEncodingAlgorithm {
    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 8 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfLong", new Object[]{8}));
        }
        return octetLength / 8;
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 8;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b2, int start, int length) throws EncodingAlgorithmException {
        long[] data = new long[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToLongArray(data, 0, b2, start, length);
        return data;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s2) throws IOException {
        return decodeFromInputStreamToIntArray(s2);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s2) throws IOException {
        if (!(data instanceof long[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotLongArray"));
        }
        long[] ldata = (long[]) data;
        encodeToOutputStreamFromLongArray(ldata, s2);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List longList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.internal.fastinfoset.algorithm.LongEncodingAlgorithm.1
            @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String lStringValue = cb.subSequence(start2, end).toString();
                longList.add(Long.valueOf(lStringValue));
            }
        });
        return generateArrayFromList(longList);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public void convertToCharacters(Object data, StringBuffer s2) {
        if (!(data instanceof long[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotLongArray"));
        }
        long[] ldata = (long[]) data;
        convertToCharactersFromLongArray(ldata, s2);
    }

    public final void decodeFromBytesToLongArray(long[] ldata, int istart, byte[] b2, int start, int length) {
        int size = length / 8;
        for (int i2 = 0; i2 < size; i2++) {
            int i3 = istart;
            istart++;
            int i4 = start;
            long j2 = ((b2[i4] & 255) << 56) | ((b2[r12] & 255) << 48);
            long j3 = j2 | ((b2[r12] & 255) << 40);
            long j4 = j3 | ((b2[r12] & 255) << 32);
            long j5 = j4 | ((b2[r12] & 255) << 24);
            long j6 = j5 | ((b2[r12] & 255) << 16);
            long j7 = j6 | ((b2[r12] & 255) << 8);
            start = start + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1;
            ldata[i3] = j7 | (b2[r12] & 255);
        }
    }

    public final long[] decodeFromInputStreamToIntArray(InputStream s2) throws IOException {
        List longList = new ArrayList();
        byte[] b2 = new byte[8];
        while (true) {
            int n2 = s2.read(b2);
            if (n2 != 8) {
                if (n2 != -1) {
                    while (n2 != 8) {
                        int m2 = s2.read(b2, n2, 8 - n2);
                        if (m2 == -1) {
                            throw new EOFException();
                        }
                        n2 += m2;
                    }
                } else {
                    return generateArrayFromList(longList);
                }
            }
            long l2 = (b2[0] << 56) + ((b2[1] & 255) << 48) + ((b2[2] & 255) << 40) + ((b2[3] & 255) << 32) + ((b2[4] & 255) << 24) + ((b2[5] & 255) << 16) + ((b2[6] & 255) << 8) + ((b2[7] & 255) << 0);
            longList.add(Long.valueOf(l2));
        }
    }

    public final void encodeToOutputStreamFromLongArray(long[] ldata, OutputStream s2) throws IOException {
        for (long bits : ldata) {
            s2.write((int) ((bits >>> 56) & 255));
            s2.write((int) ((bits >>> 48) & 255));
            s2.write((int) ((bits >>> 40) & 255));
            s2.write((int) ((bits >>> 32) & 255));
            s2.write((int) ((bits >>> 24) & 255));
            s2.write((int) ((bits >>> 16) & 255));
            s2.write((int) ((bits >>> 8) & 255));
            s2.write((int) (bits & 255));
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b2, int start) {
        encodeToBytesFromLongArray((long[]) array, astart, alength, b2, start);
    }

    public final void encodeToBytesFromLongArray(long[] ldata, int lstart, int llength, byte[] b2, int start) {
        int lend = lstart + llength;
        for (int i2 = lstart; i2 < lend; i2++) {
            long bits = ldata[i2];
            int i3 = start;
            int start2 = start + 1;
            b2[i3] = (byte) ((bits >>> 56) & 255);
            int start3 = start2 + 1;
            b2[start2] = (byte) ((bits >>> 48) & 255);
            int start4 = start3 + 1;
            b2[start3] = (byte) ((bits >>> 40) & 255);
            int start5 = start4 + 1;
            b2[start4] = (byte) ((bits >>> 32) & 255);
            int start6 = start5 + 1;
            b2[start5] = (byte) ((bits >>> 24) & 255);
            int start7 = start6 + 1;
            b2[start6] = (byte) ((bits >>> 16) & 255);
            int start8 = start7 + 1;
            b2[start7] = (byte) ((bits >>> 8) & 255);
            start = start8 + 1;
            b2[start8] = (byte) (bits & 255);
        }
    }

    public final void convertToCharactersFromLongArray(long[] ldata, StringBuffer s2) {
        int end = ldata.length - 1;
        for (int i2 = 0; i2 <= end; i2++) {
            s2.append(Long.toString(ldata[i2]));
            if (i2 != end) {
                s2.append(' ');
            }
        }
    }

    public final long[] generateArrayFromList(List array) {
        long[] ldata = new long[array.size()];
        for (int i2 = 0; i2 < ldata.length; i2++) {
            ldata[i2] = ((Long) array.get(i2)).longValue();
        }
        return ldata;
    }
}
