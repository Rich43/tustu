package com.sun.xml.internal.fastinfoset.algorithm;

import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/algorithm/BuiltInEncodingAlgorithm.class */
public abstract class BuiltInEncodingAlgorithm implements EncodingAlgorithm {
    protected static final Pattern SPACE_PATTERN = Pattern.compile("\\s");

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/algorithm/BuiltInEncodingAlgorithm$WordListener.class */
    public interface WordListener {
        void word(int i2, int i3);
    }

    public abstract int getPrimtiveLengthFromOctetLength(int i2) throws EncodingAlgorithmException;

    public abstract int getOctetLengthFromPrimitiveLength(int i2);

    public abstract void encodeToBytes(Object obj, int i2, int i3, byte[] bArr, int i4);

    public void matchWhiteSpaceDelimnatedWords(CharBuffer cb, WordListener wl) {
        Matcher m2 = SPACE_PATTERN.matcher(cb);
        int i2 = 0;
        while (m2.find()) {
            int s2 = m2.start();
            if (s2 != i2) {
                wl.word(i2, s2);
            }
            i2 = m2.end();
        }
        if (i2 != cb.length()) {
            wl.word(i2, cb.length());
        }
    }

    public StringBuilder removeWhitespace(char[] ch, int start, int length) {
        StringBuilder buf = new StringBuilder();
        int firstNonWS = 0;
        int idx = 0;
        while (idx < length) {
            if (Character.isWhitespace(ch[idx + start])) {
                if (firstNonWS < idx) {
                    buf.append(ch, firstNonWS + start, idx - firstNonWS);
                }
                firstNonWS = idx + 1;
            }
            idx++;
        }
        if (firstNonWS < idx) {
            buf.append(ch, firstNonWS + start, idx - firstNonWS);
        }
        return buf;
    }
}
