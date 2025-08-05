package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

@Deprecated
/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/Base64.class */
public class Base64 {
    public static final int BASE64DEFAULTLENGTH = 76;
    private static final int BASELENGTH = 255;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final byte[] base64Alphabet = new byte[255];
    private static final char[] lookUpBase64Alphabet = new char[64];

    static {
        for (int i2 = 0; i2 < 255; i2++) {
            base64Alphabet[i2] = -1;
        }
        for (int i3 = 90; i3 >= 65; i3--) {
            base64Alphabet[i3] = (byte) (i3 - 65);
        }
        for (int i4 = 122; i4 >= 97; i4--) {
            base64Alphabet[i4] = (byte) ((i4 - 97) + 26);
        }
        for (int i5 = 57; i5 >= 48; i5--) {
            base64Alphabet[i5] = (byte) ((i5 - 48) + 52);
        }
        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;
        for (int i6 = 0; i6 <= 25; i6++) {
            lookUpBase64Alphabet[i6] = (char) (65 + i6);
        }
        int i7 = 26;
        int i8 = 0;
        while (i7 <= 51) {
            lookUpBase64Alphabet[i7] = (char) (97 + i8);
            i7++;
            i8++;
        }
        int i9 = 52;
        int i10 = 0;
        while (i9 <= 61) {
            lookUpBase64Alphabet[i9] = (char) (48 + i10);
            i9++;
            i10++;
        }
        lookUpBase64Alphabet[62] = '+';
        lookUpBase64Alphabet[63] = '/';
    }

    private Base64() {
    }

    static final byte[] getBytes(BigInteger bigInteger, int i2) {
        int i3 = ((i2 + 7) >> 3) << 3;
        if (i3 < bigInteger.bitLength()) {
            throw new IllegalArgumentException(I18n.translate("utils.Base64.IllegalBitlength"));
        }
        byte[] byteArray = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 != 0 && (bigInteger.bitLength() / 8) + 1 == i3 / 8) {
            return byteArray;
        }
        int i4 = 0;
        int length = byteArray.length;
        if (bigInteger.bitLength() % 8 == 0) {
            i4 = 1;
            length--;
        }
        int i5 = (i3 / 8) - length;
        byte[] bArr = new byte[i3 / 8];
        System.arraycopy(byteArray, i4, bArr, i5, length);
        return bArr;
    }

    public static final String encode(BigInteger bigInteger) {
        return XMLUtils.encodeToString(XMLUtils.getBytes(bigInteger, bigInteger.bitLength()));
    }

    public static final byte[] encode(BigInteger bigInteger, int i2) {
        int i3 = ((i2 + 7) >> 3) << 3;
        if (i3 < bigInteger.bitLength()) {
            throw new IllegalArgumentException(I18n.translate("utils.Base64.IllegalBitlength"));
        }
        byte[] byteArray = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 != 0 && (bigInteger.bitLength() / 8) + 1 == i3 / 8) {
            return byteArray;
        }
        int i4 = 0;
        int length = byteArray.length;
        if (bigInteger.bitLength() % 8 == 0) {
            i4 = 1;
            length--;
        }
        int i5 = (i3 / 8) - length;
        byte[] bArr = new byte[i3 / 8];
        System.arraycopy(byteArray, i4, bArr, i5, length);
        return bArr;
    }

    public static final BigInteger decodeBigIntegerFromElement(Element element) throws Base64DecodingException {
        return new BigInteger(1, decode(element));
    }

    public static final BigInteger decodeBigIntegerFromText(Text text) throws Base64DecodingException {
        return new BigInteger(1, decode(text.getData()));
    }

    public static final void fillElementWithBigInteger(Element element, BigInteger bigInteger) {
        String strEncode = encode(bigInteger);
        if (!XMLUtils.ignoreLineBreaks() && strEncode.length() > 76) {
            strEncode = "\n" + strEncode + "\n";
        }
        element.appendChild(element.getOwnerDocument().createTextNode(strEncode));
    }

    public static final byte[] decode(Element element) throws Base64DecodingException {
        StringBuffer stringBuffer = new StringBuffer();
        for (Node firstChild = element.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 3) {
                stringBuffer.append(((Text) firstChild).getData());
            }
        }
        return decode(stringBuffer.toString());
    }

    public static final Element encodeToElement(Document document, String str, byte[] bArr) {
        Element elementCreateElementInSignatureSpace = XMLUtils.createElementInSignatureSpace(document, str);
        elementCreateElementInSignatureSpace.appendChild(document.createTextNode(encode(bArr)));
        return elementCreateElementInSignatureSpace;
    }

    public static final byte[] decode(byte[] bArr) throws Base64DecodingException {
        return decodeInternal(bArr, -1);
    }

    public static final String encode(byte[] bArr) {
        if (XMLUtils.ignoreLineBreaks()) {
            return encode(bArr, Integer.MAX_VALUE);
        }
        return encode(bArr, 76);
    }

    public static final byte[] decode(BufferedReader bufferedReader) throws Base64DecodingException, IOException {
        UnsyncByteArrayOutputStream unsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (null != line) {
                    unsyncByteArrayOutputStream.write(decode(line));
                } else {
                    byte[] byteArray = unsyncByteArrayOutputStream.toByteArray();
                    unsyncByteArrayOutputStream.close();
                    return byteArray;
                }
            } catch (Throwable th) {
                unsyncByteArrayOutputStream.close();
                throw th;
            }
        }
    }

    protected static final boolean isWhiteSpace(byte b2) {
        return b2 == 32 || b2 == 13 || b2 == 10 || b2 == 9;
    }

    protected static final boolean isPad(byte b2) {
        return b2 == 61;
    }

    public static final String encode(byte[] bArr, int i2) {
        if (i2 < 4) {
            i2 = Integer.MAX_VALUE;
        }
        if (bArr == null) {
            return null;
        }
        long length = bArr.length * 8;
        if (length == 0) {
            return "";
        }
        long j2 = length % 24;
        int i3 = (int) (length / 24);
        int i4 = j2 != 0 ? i3 + 1 : i3;
        int i5 = (i4 - 1) / (i2 / 4);
        char[] cArr = new char[(i4 * 4) + (i5 * 2)];
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < i5; i9++) {
            for (int i10 = 0; i10 < 19; i10++) {
                int i11 = i7;
                int i12 = i7 + 1;
                byte b2 = bArr[i11];
                int i13 = i12 + 1;
                byte b3 = bArr[i12];
                i7 = i13 + 1;
                byte b4 = bArr[i13];
                byte b5 = (byte) (b3 & 15);
                byte b6 = (byte) (b2 & 3);
                byte b7 = (b2 & Byte.MIN_VALUE) == 0 ? (byte) (b2 >> 2) : (byte) ((b2 >> 2) ^ 192);
                byte b8 = (b3 & Byte.MIN_VALUE) == 0 ? (byte) (b3 >> 4) : (byte) ((b3 >> 4) ^ 240);
                int i14 = (b4 & Byte.MIN_VALUE) == 0 ? b4 >> 6 : (b4 >> 6) ^ 252;
                int i15 = i6;
                int i16 = i6 + 1;
                cArr[i15] = lookUpBase64Alphabet[b7];
                int i17 = i16 + 1;
                cArr[i16] = lookUpBase64Alphabet[b8 | (b6 << 4)];
                int i18 = i17 + 1;
                cArr[i17] = lookUpBase64Alphabet[(b5 << 2) | ((byte) i14)];
                i6 = i18 + 1;
                cArr[i18] = lookUpBase64Alphabet[b4 & 63];
                i8++;
            }
            int i19 = i6;
            int i20 = i6 + 1;
            cArr[i19] = '\r';
            i6 = i20 + 1;
            cArr[i20] = '\n';
        }
        while (i8 < i3) {
            int i21 = i7;
            int i22 = i7 + 1;
            byte b9 = bArr[i21];
            int i23 = i22 + 1;
            byte b10 = bArr[i22];
            i7 = i23 + 1;
            byte b11 = bArr[i23];
            byte b12 = (byte) (b10 & 15);
            byte b13 = (byte) (b9 & 3);
            byte b14 = (b9 & Byte.MIN_VALUE) == 0 ? (byte) (b9 >> 2) : (byte) ((b9 >> 2) ^ 192);
            byte b15 = (b10 & Byte.MIN_VALUE) == 0 ? (byte) (b10 >> 4) : (byte) ((b10 >> 4) ^ 240);
            int i24 = (b11 & Byte.MIN_VALUE) == 0 ? b11 >> 6 : (b11 >> 6) ^ 252;
            int i25 = i6;
            int i26 = i6 + 1;
            cArr[i25] = lookUpBase64Alphabet[b14];
            int i27 = i26 + 1;
            cArr[i26] = lookUpBase64Alphabet[b15 | (b13 << 4)];
            int i28 = i27 + 1;
            cArr[i27] = lookUpBase64Alphabet[(b12 << 2) | ((byte) i24)];
            i6 = i28 + 1;
            cArr[i28] = lookUpBase64Alphabet[b11 & 63];
            i8++;
        }
        if (j2 == 8) {
            byte b16 = bArr[i7];
            byte b17 = (byte) (b16 & 3);
            int i29 = i6;
            int i30 = i6 + 1;
            cArr[i29] = lookUpBase64Alphabet[(b16 & Byte.MIN_VALUE) == 0 ? (byte) (b16 >> 2) : (byte) ((b16 >> 2) ^ 192)];
            int i31 = i30 + 1;
            cArr[i30] = lookUpBase64Alphabet[b17 << 4];
            int i32 = i31 + 1;
            cArr[i31] = '=';
            int i33 = i32 + 1;
            cArr[i32] = '=';
        } else if (j2 == 16) {
            byte b18 = bArr[i7];
            byte b19 = bArr[i7 + 1];
            byte b20 = (byte) (b19 & 15);
            byte b21 = (byte) (b18 & 3);
            byte b22 = (b18 & Byte.MIN_VALUE) == 0 ? (byte) (b18 >> 2) : (byte) ((b18 >> 2) ^ 192);
            byte b23 = (b19 & Byte.MIN_VALUE) == 0 ? (byte) (b19 >> 4) : (byte) ((b19 >> 4) ^ 240);
            int i34 = i6;
            int i35 = i6 + 1;
            cArr[i34] = lookUpBase64Alphabet[b22];
            int i36 = i35 + 1;
            cArr[i35] = lookUpBase64Alphabet[b23 | (b21 << 4)];
            int i37 = i36 + 1;
            cArr[i36] = lookUpBase64Alphabet[b20 << 2];
            int i38 = i37 + 1;
            cArr[i37] = '=';
        }
        return new String(cArr);
    }

    public static final byte[] decode(String str) throws Base64DecodingException {
        if (str == null) {
            return null;
        }
        byte[] bArr = new byte[str.length()];
        return decodeInternal(bArr, getBytesInternal(str, bArr));
    }

    protected static final int getBytesInternal(String str, byte[] bArr) {
        int length = str.length();
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            byte bCharAt = (byte) str.charAt(i3);
            if (!isWhiteSpace(bCharAt)) {
                int i4 = i2;
                i2++;
                bArr[i4] = bCharAt;
            }
        }
        return i2;
    }

    protected static final byte[] decodeInternal(byte[] bArr, int i2) throws Base64DecodingException {
        byte[] bArr2;
        if (i2 == -1) {
            i2 = removeWhiteSpace(bArr);
        }
        if (i2 % 4 != 0) {
            throw new Base64DecodingException("decoding.divisible.four");
        }
        int i3 = i2 / 4;
        if (i3 == 0) {
            return new byte[0];
        }
        int i4 = (i3 - 1) * 4;
        int i5 = (i3 - 1) * 3;
        int i6 = i4 + 1;
        byte b2 = base64Alphabet[bArr[i4]];
        int i7 = i6 + 1;
        byte b3 = base64Alphabet[bArr[i6]];
        if (b2 == -1 || b3 == -1) {
            throw new Base64DecodingException("decoding.general");
        }
        byte[] bArr3 = base64Alphabet;
        int i8 = i7 + 1;
        byte b4 = bArr[i7];
        byte b5 = bArr3[b4];
        byte[] bArr4 = base64Alphabet;
        int i9 = i8 + 1;
        byte b6 = bArr[i8];
        byte b7 = bArr4[b6];
        if (b5 == -1 || b7 == -1) {
            if (isPad(b4) && isPad(b6)) {
                if ((b3 & 15) != 0) {
                    throw new Base64DecodingException("decoding.general");
                }
                bArr2 = new byte[i5 + 1];
                bArr2[i5] = (byte) ((b2 << 2) | (b3 >> 4));
            } else if (!isPad(b4) && isPad(b6)) {
                if ((b5 & 3) != 0) {
                    throw new Base64DecodingException("decoding.general");
                }
                bArr2 = new byte[i5 + 2];
                bArr2[i5] = (byte) ((b2 << 2) | (b3 >> 4));
                bArr2[i5 + 1] = (byte) (((b3 & 15) << 4) | ((b5 >> 2) & 15));
            } else {
                throw new Base64DecodingException("decoding.general");
            }
        } else {
            bArr2 = new byte[i5 + 3];
            int i10 = i5 + 1;
            bArr2[i5] = (byte) ((b2 << 2) | (b3 >> 4));
            int i11 = i10 + 1;
            bArr2[i10] = (byte) (((b3 & 15) << 4) | ((b5 >> 2) & 15));
            int i12 = i11 + 1;
            bArr2[i11] = (byte) ((b5 << 6) | b7);
        }
        int i13 = 0;
        int i14 = 0;
        for (int i15 = i3 - 1; i15 > 0; i15--) {
            int i16 = i14;
            int i17 = i14 + 1;
            byte b8 = base64Alphabet[bArr[i16]];
            int i18 = i17 + 1;
            byte b9 = base64Alphabet[bArr[i17]];
            int i19 = i18 + 1;
            byte b10 = base64Alphabet[bArr[i18]];
            i14 = i19 + 1;
            byte b11 = base64Alphabet[bArr[i19]];
            if (b8 == -1 || b9 == -1 || b10 == -1 || b11 == -1) {
                throw new Base64DecodingException("decoding.general");
            }
            int i20 = i13;
            int i21 = i13 + 1;
            bArr2[i20] = (byte) ((b8 << 2) | (b9 >> 4));
            int i22 = i21 + 1;
            bArr2[i21] = (byte) (((b9 & 15) << 4) | ((b10 >> 2) & 15));
            i13 = i22 + 1;
            bArr2[i22] = (byte) ((b10 << 6) | b11);
        }
        return bArr2;
    }

    public static final void decode(String str, OutputStream outputStream) throws Base64DecodingException, IOException {
        byte[] bArr = new byte[str.length()];
        decode(bArr, outputStream, getBytesInternal(str, bArr));
    }

    public static final void decode(byte[] bArr, OutputStream outputStream) throws Base64DecodingException, IOException {
        decode(bArr, outputStream, -1);
    }

    protected static final void decode(byte[] bArr, OutputStream outputStream, int i2) throws Base64DecodingException, IOException {
        if (i2 == -1) {
            i2 = removeWhiteSpace(bArr);
        }
        if (i2 % 4 != 0) {
            throw new Base64DecodingException("decoding.divisible.four");
        }
        int i3 = i2 / 4;
        if (i3 == 0) {
            return;
        }
        int i4 = 0;
        for (int i5 = i3 - 1; i5 > 0; i5--) {
            int i6 = i4;
            int i7 = i4 + 1;
            byte b2 = base64Alphabet[bArr[i6]];
            int i8 = i7 + 1;
            byte b3 = base64Alphabet[bArr[i7]];
            int i9 = i8 + 1;
            byte b4 = base64Alphabet[bArr[i8]];
            i4 = i9 + 1;
            byte b5 = base64Alphabet[bArr[i9]];
            if (b2 == -1 || b3 == -1 || b4 == -1 || b5 == -1) {
                throw new Base64DecodingException("decoding.general");
            }
            outputStream.write((byte) ((b2 << 2) | (b3 >> 4)));
            outputStream.write((byte) (((b3 & 15) << 4) | ((b4 >> 2) & 15)));
            outputStream.write((byte) ((b4 << 6) | b5));
        }
        int i10 = i4;
        int i11 = i4 + 1;
        byte b6 = base64Alphabet[bArr[i10]];
        int i12 = i11 + 1;
        byte b7 = base64Alphabet[bArr[i11]];
        if (b6 == -1 || b7 == -1) {
            throw new Base64DecodingException("decoding.general");
        }
        byte[] bArr2 = base64Alphabet;
        int i13 = i12 + 1;
        byte b8 = bArr[i12];
        byte b9 = bArr2[b8];
        byte[] bArr3 = base64Alphabet;
        int i14 = i13 + 1;
        byte b10 = bArr[i13];
        byte b11 = bArr3[b10];
        if (b9 == -1 || b11 == -1) {
            if (isPad(b8) && isPad(b10)) {
                if ((b7 & 15) != 0) {
                    throw new Base64DecodingException("decoding.general");
                }
                outputStream.write((byte) ((b6 << 2) | (b7 >> 4)));
                return;
            } else {
                if (!isPad(b8) && isPad(b10)) {
                    if ((b9 & 3) != 0) {
                        throw new Base64DecodingException("decoding.general");
                    }
                    outputStream.write((byte) ((b6 << 2) | (b7 >> 4)));
                    outputStream.write((byte) (((b7 & 15) << 4) | ((b9 >> 2) & 15)));
                    return;
                }
                throw new Base64DecodingException("decoding.general");
            }
        }
        outputStream.write((byte) ((b6 << 2) | (b7 >> 4)));
        outputStream.write((byte) (((b7 & 15) << 4) | ((b9 >> 2) & 15)));
        outputStream.write((byte) ((b9 << 6) | b11));
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x00ce, code lost:
    
        r0 = r0[0];
        r0 = r0[1];
        r0 = r0[2];
        r0 = r0[3];
        r0 = com.sun.org.apache.xml.internal.security.utils.Base64.base64Alphabet[r0];
        r0 = com.sun.org.apache.xml.internal.security.utils.Base64.base64Alphabet[r0];
        r0 = com.sun.org.apache.xml.internal.security.utils.Base64.base64Alphabet[r0];
        r0 = com.sun.org.apache.xml.internal.security.utils.Base64.base64Alphabet[r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0107, code lost:
    
        if (r0 == (-1)) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x010d, code lost:
    
        if (r0 != (-1)) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0115, code lost:
    
        if (isPad(r0) == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x011d, code lost:
    
        if (isPad(r0) == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0124, code lost:
    
        if ((r0 & 15) == 0) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0130, code lost:
    
        throw new com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException("decoding.general");
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0131, code lost:
    
        r6.write((byte) ((r0 << 2) | (r0 >> 4)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0145, code lost:
    
        if (isPad(r0) != false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x014d, code lost:
    
        if (isPad(r0) == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0150, code lost:
    
        r0 = com.sun.org.apache.xml.internal.security.utils.Base64.base64Alphabet[r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x015c, code lost:
    
        if ((r0 & 3) == 0) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0168, code lost:
    
        throw new com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException("decoding.general");
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0169, code lost:
    
        r6.write((byte) ((r0 << 2) | (r0 >> 4)));
        r6.write((byte) (((r0 & 15) << 4) | ((r0 >> 2) & 15)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0194, code lost:
    
        throw new com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException("decoding.general");
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0195, code lost:
    
        r6.write((byte) ((r0 << 2) | (r0 >> 4)));
        r6.write((byte) (((r0 & 15) << 4) | ((r0 >> 2) & 15)));
        r6.write((byte) ((r0 << 6) | r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x01c1, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final void decode(java.io.InputStream r5, java.io.OutputStream r6) throws com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 450
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xml.internal.security.utils.Base64.decode(java.io.InputStream, java.io.OutputStream):void");
    }

    protected static final int removeWhiteSpace(byte[] bArr) {
        if (bArr == null) {
            return 0;
        }
        int i2 = 0;
        for (byte b2 : bArr) {
            if (!isWhiteSpace(b2)) {
                int i3 = i2;
                i2++;
                bArr[i3] = b2;
            }
        }
        return i2;
    }
}
