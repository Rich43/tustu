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

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/algorithm/BooleanEncodingAlgorithm.class */
public class BooleanEncodingAlgorithm extends BuiltInEncodingAlgorithm {
    private static final int[] BIT_TABLE = {128, 64, 32, 16, 8, 4, 2, 1};

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        if (primitiveLength < 5) {
            return 1;
        }
        int div = primitiveLength / 8;
        if (div == 0) {
            return 2;
        }
        return 1 + div;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b2, int start, int length) throws EncodingAlgorithmException {
        int blength = getPrimtiveLengthFromOctetLength(length, b2[start]);
        boolean[] data = new boolean[blength];
        decodeFromBytesToBooleanArray(data, 0, blength, b2, start, length);
        return data;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s2) throws IOException {
        List booleanList = new ArrayList();
        int value = s2.read();
        if (value == -1) {
            throw new EOFException();
        }
        int unusedBits = (value >> 4) & 255;
        int bitPosition = 4;
        int bitPositionEnd = 8;
        do {
            int valueNext = s2.read();
            if (valueNext == -1) {
                bitPositionEnd -= unusedBits;
            }
            while (bitPosition < bitPositionEnd) {
                int i2 = bitPosition;
                bitPosition++;
                booleanList.add(Boolean.valueOf((value & BIT_TABLE[i2]) > 0));
            }
            value = valueNext;
        } while (value != -1);
        return generateArrayFromList(booleanList);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s2) throws IOException {
        if (!(data instanceof boolean[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotBoolean"));
        }
        boolean[] array = (boolean[]) data;
        int alength = array.length;
        int mod = (alength + 4) % 8;
        int unusedBits = mod == 0 ? 0 : 8 - mod;
        int bitPosition = 4;
        int value = unusedBits << 4;
        int astart = 0;
        while (astart < alength) {
            int i2 = astart;
            astart++;
            if (array[i2]) {
                value |= BIT_TABLE[bitPosition];
            }
            bitPosition++;
            if (bitPosition == 8) {
                s2.write(value);
                value = 0;
                bitPosition = 0;
            }
        }
        if (bitPosition != 8) {
            s2.write(value);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        if (length == 0) {
            return new boolean[0];
        }
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List booleanList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm.1
            @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                if (cb.charAt(start2) == 't') {
                    booleanList.add(Boolean.TRUE);
                } else {
                    booleanList.add(Boolean.FALSE);
                }
            }
        });
        return generateArrayFromList(booleanList);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s2) {
        if (data == null) {
            return;
        }
        boolean[] value = (boolean[]) data;
        if (value.length == 0) {
            return;
        }
        s2.ensureCapacity(value.length * 5);
        int end = value.length - 1;
        for (int i2 = 0; i2 <= end; i2++) {
            if (value[i2]) {
                s2.append("true");
            } else {
                s2.append("false");
            }
            if (i2 != end) {
                s2.append(' ');
            }
        }
    }

    public int getPrimtiveLengthFromOctetLength(int octetLength, int firstOctet) throws EncodingAlgorithmException {
        int unusedBits = (firstOctet >> 4) & 255;
        if (octetLength == 1) {
            if (unusedBits > 3) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.unusedBits4"));
            }
            return 4 - unusedBits;
        }
        if (unusedBits > 7) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.unusedBits8"));
        }
        return ((octetLength * 8) - 4) - unusedBits;
    }

    public final void decodeFromBytesToBooleanArray(boolean[] bdata, int bstart, int blength, byte[] b2, int start, int length) {
        int start2 = start + 1;
        int value = b2[start] & 255;
        int bitPosition = 4;
        int bend = bstart + blength;
        while (bstart < bend) {
            if (bitPosition == 8) {
                int i2 = start2;
                start2++;
                value = b2[i2] & 255;
                bitPosition = 0;
            }
            int i3 = bstart;
            bstart++;
            int i4 = bitPosition;
            bitPosition++;
            bdata[i3] = (value & BIT_TABLE[i4]) > 0;
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public void encodeToBytes(Object array, int astart, int alength, byte[] b2, int start) {
        if (!(array instanceof boolean[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotBoolean"));
        }
        encodeToBytesFromBooleanArray((boolean[]) array, astart, alength, b2, start);
    }

    public void encodeToBytesFromBooleanArray(boolean[] array, int astart, int alength, byte[] b2, int start) {
        int mod = (alength + 4) % 8;
        int unusedBits = mod == 0 ? 0 : 8 - mod;
        int bitPosition = 4;
        int value = unusedBits << 4;
        int aend = astart + alength;
        while (astart < aend) {
            int i2 = astart;
            astart++;
            if (array[i2]) {
                value |= BIT_TABLE[bitPosition];
            }
            bitPosition++;
            if (bitPosition == 8) {
                int i3 = start;
                start++;
                b2[i3] = (byte) value;
                value = 0;
                bitPosition = 0;
            }
        }
        if (bitPosition > 0) {
            b2[start] = (byte) value;
        }
    }

    private boolean[] generateArrayFromList(List array) {
        boolean[] bdata = new boolean[array.size()];
        for (int i2 = 0; i2 < bdata.length; i2++) {
            bdata[i2] = ((Boolean) array.get(i2)).booleanValue();
        }
        return bdata;
    }
}
