package java.net;

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.util.BitSet;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/net/URLEncoder.class */
public class URLEncoder {
    static BitSet dontNeedEncoding = new BitSet(256);
    static final int caseDiff = 32;
    static String dfltEncName;

    static {
        dfltEncName = null;
        for (int i2 = 97; i2 <= 122; i2++) {
            dontNeedEncoding.set(i2);
        }
        for (int i3 = 65; i3 <= 90; i3++) {
            dontNeedEncoding.set(i3);
        }
        for (int i4 = 48; i4 <= 57; i4++) {
            dontNeedEncoding.set(i4);
        }
        dontNeedEncoding.set(32);
        dontNeedEncoding.set(45);
        dontNeedEncoding.set(95);
        dontNeedEncoding.set(46);
        dontNeedEncoding.set(42);
        dfltEncName = (String) AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
    }

    private URLEncoder() {
    }

    @Deprecated
    public static String encode(String str) {
        String strEncode = null;
        try {
            strEncode = encode(str, dfltEncName);
        } catch (UnsupportedEncodingException e2) {
        }
        return strEncode;
    }

    public static String encode(String str, String str2) throws UnsupportedEncodingException {
        BitSet bitSet;
        char cCharAt;
        char cCharAt2;
        boolean z2 = false;
        StringBuffer stringBuffer = new StringBuffer(str.length());
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        if (str2 == null) {
            throw new NullPointerException("charsetName");
        }
        try {
            Charset charsetForName = Charset.forName(str2);
            int i2 = 0;
            while (i2 < str.length()) {
                char cCharAt3 = str.charAt(i2);
                if (dontNeedEncoding.get(cCharAt3)) {
                    if (cCharAt3 == ' ') {
                        cCharAt3 = '+';
                        z2 = true;
                    }
                    stringBuffer.append(cCharAt3);
                    i2++;
                } else {
                    do {
                        charArrayWriter.write(cCharAt3);
                        if (cCharAt3 >= 55296 && cCharAt3 <= 56319 && i2 + 1 < str.length() && (cCharAt2 = str.charAt(i2 + 1)) >= 56320 && cCharAt2 <= 57343) {
                            charArrayWriter.write(cCharAt2);
                            i2++;
                        }
                        i2++;
                        if (i2 >= str.length()) {
                            break;
                        }
                        bitSet = dontNeedEncoding;
                        cCharAt = str.charAt(i2);
                        cCharAt3 = cCharAt;
                    } while (!bitSet.get(cCharAt));
                    charArrayWriter.flush();
                    byte[] bytes = new String(charArrayWriter.toCharArray()).getBytes(charsetForName);
                    for (int i3 = 0; i3 < bytes.length; i3++) {
                        stringBuffer.append('%');
                        char cForDigit = Character.forDigit((bytes[i3] >> 4) & 15, 16);
                        if (Character.isLetter(cForDigit)) {
                            cForDigit = (char) (cForDigit - ' ');
                        }
                        stringBuffer.append(cForDigit);
                        char cForDigit2 = Character.forDigit(bytes[i3] & 15, 16);
                        if (Character.isLetter(cForDigit2)) {
                            cForDigit2 = (char) (cForDigit2 - ' ');
                        }
                        stringBuffer.append(cForDigit2);
                    }
                    charArrayWriter.reset();
                    z2 = true;
                }
            }
            return z2 ? stringBuffer.toString() : str;
        } catch (IllegalCharsetNameException e2) {
            throw new UnsupportedEncodingException(str2);
        } catch (UnsupportedCharsetException e3) {
            throw new UnsupportedEncodingException(str2);
        }
    }
}
