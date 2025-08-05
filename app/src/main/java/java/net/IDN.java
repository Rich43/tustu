package java.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.ParseException;
import sun.net.idn.Punycode;
import sun.net.idn.StringPrep;
import sun.text.normalizer.UCharacterIterator;

/* loaded from: rt.jar:java/net/IDN.class */
public final class IDN {
    public static final int ALLOW_UNASSIGNED = 1;
    public static final int USE_STD3_ASCII_RULES = 2;
    private static final String ACE_PREFIX = "xn--";
    private static final int ACE_PREFIX_LENGTH;
    private static final int MAX_LABEL_LENGTH = 63;
    private static StringPrep namePrep;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        InputStream resourceAsStream;
        $assertionsDisabled = !IDN.class.desiredAssertionStatus();
        ACE_PREFIX_LENGTH = ACE_PREFIX.length();
        namePrep = null;
        try {
            if (System.getSecurityManager() != null) {
                resourceAsStream = (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: java.net.IDN.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public InputStream run2() {
                        return StringPrep.class.getResourceAsStream("uidna.spp");
                    }
                });
            } else {
                resourceAsStream = StringPrep.class.getResourceAsStream("uidna.spp");
            }
            namePrep = new StringPrep(resourceAsStream);
            resourceAsStream.close();
        } catch (IOException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
    }

    public static String toASCII(String str, int i2) {
        int i3 = 0;
        StringBuffer stringBuffer = new StringBuffer();
        if (isRootLabel(str)) {
            return ".";
        }
        while (i3 < str.length()) {
            int iSearchDots = searchDots(str, i3);
            stringBuffer.append(toASCIIInternal(str.substring(i3, iSearchDots), i2));
            if (iSearchDots != str.length()) {
                stringBuffer.append('.');
            }
            i3 = iSearchDots + 1;
        }
        return stringBuffer.toString();
    }

    public static String toASCII(String str) {
        return toASCII(str, 0);
    }

    public static String toUnicode(String str, int i2) {
        int i3 = 0;
        StringBuffer stringBuffer = new StringBuffer();
        if (isRootLabel(str)) {
            return ".";
        }
        while (i3 < str.length()) {
            int iSearchDots = searchDots(str, i3);
            stringBuffer.append(toUnicodeInternal(str.substring(i3, iSearchDots), i2));
            if (iSearchDots != str.length()) {
                stringBuffer.append('.');
            }
            i3 = iSearchDots + 1;
        }
        return stringBuffer.toString();
    }

    public static String toUnicode(String str) {
        return toUnicode(str, 0);
    }

    private IDN() {
    }

    private static String toASCIIInternal(String str, int i2) {
        StringBuffer stringBuffer;
        boolean zIsAllASCII = isAllASCII(str);
        if (!zIsAllASCII) {
            try {
                stringBuffer = namePrep.prepare(UCharacterIterator.getInstance(str), i2);
            } catch (ParseException e2) {
                throw new IllegalArgumentException(e2);
            }
        } else {
            stringBuffer = new StringBuffer(str);
        }
        if (stringBuffer.length() == 0) {
            throw new IllegalArgumentException("Empty label is not a legal name");
        }
        if ((i2 & 2) != 0) {
            for (int i3 = 0; i3 < stringBuffer.length(); i3++) {
                if (isNonLDHAsciiCodePoint(stringBuffer.charAt(i3))) {
                    throw new IllegalArgumentException("Contains non-LDH ASCII characters");
                }
            }
            if (stringBuffer.charAt(0) == '-' || stringBuffer.charAt(stringBuffer.length() - 1) == '-') {
                throw new IllegalArgumentException("Has leading or trailing hyphen");
            }
        }
        if (!zIsAllASCII && !isAllASCII(stringBuffer.toString())) {
            if (!startsWithACEPrefix(stringBuffer)) {
                try {
                    stringBuffer = toASCIILower(Punycode.encode(stringBuffer, null));
                    stringBuffer.insert(0, ACE_PREFIX);
                } catch (ParseException e3) {
                    throw new IllegalArgumentException(e3);
                }
            } else {
                throw new IllegalArgumentException("The input starts with the ACE Prefix");
            }
        }
        if (stringBuffer.length() > 63) {
            throw new IllegalArgumentException("The label in the input is too long");
        }
        return stringBuffer.toString();
    }

    private static String toUnicodeInternal(String str, int i2) {
        StringBuffer stringBufferPrepare;
        if (!isAllASCII(str)) {
            try {
                stringBufferPrepare = namePrep.prepare(UCharacterIterator.getInstance(str), i2);
            } catch (Exception e2) {
                return str;
            }
        } else {
            stringBufferPrepare = new StringBuffer(str);
        }
        if (startsWithACEPrefix(stringBufferPrepare)) {
            try {
                StringBuffer stringBufferDecode = Punycode.decode(new StringBuffer(stringBufferPrepare.substring(ACE_PREFIX_LENGTH, stringBufferPrepare.length())), null);
                if (toASCII(stringBufferDecode.toString(), i2).equalsIgnoreCase(stringBufferPrepare.toString())) {
                    return stringBufferDecode.toString();
                }
            } catch (Exception e3) {
            }
        }
        return str;
    }

    private static boolean isNonLDHAsciiCodePoint(int i2) {
        return (0 <= i2 && i2 <= 44) || (46 <= i2 && i2 <= 47) || ((58 <= i2 && i2 <= 64) || ((91 <= i2 && i2 <= 96) || (123 <= i2 && i2 <= 127)));
    }

    private static int searchDots(String str, int i2) {
        int i3 = i2;
        while (i3 < str.length() && !isLabelSeparator(str.charAt(i3))) {
            i3++;
        }
        return i3;
    }

    private static boolean isRootLabel(String str) {
        return str.length() == 1 && isLabelSeparator(str.charAt(0));
    }

    private static boolean isLabelSeparator(char c2) {
        return c2 == '.' || c2 == 12290 || c2 == 65294 || c2 == 65377;
    }

    private static boolean isAllASCII(String str) {
        boolean z2 = true;
        int i2 = 0;
        while (true) {
            if (i2 >= str.length()) {
                break;
            }
            if (str.charAt(i2) <= 127) {
                i2++;
            } else {
                z2 = false;
                break;
            }
        }
        return z2;
    }

    private static boolean startsWithACEPrefix(StringBuffer stringBuffer) {
        boolean z2 = true;
        if (stringBuffer.length() < ACE_PREFIX_LENGTH) {
            return false;
        }
        for (int i2 = 0; i2 < ACE_PREFIX_LENGTH; i2++) {
            if (toASCIILower(stringBuffer.charAt(i2)) != ACE_PREFIX.charAt(i2)) {
                z2 = false;
            }
        }
        return z2;
    }

    private static char toASCIILower(char c2) {
        if ('A' <= c2 && c2 <= 'Z') {
            return (char) ((c2 + 'a') - 65);
        }
        return c2;
    }

    private static StringBuffer toASCIILower(StringBuffer stringBuffer) {
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i2 = 0; i2 < stringBuffer.length(); i2++) {
            stringBuffer2.append(toASCIILower(stringBuffer.charAt(i2)));
        }
        return stringBuffer2;
    }
}
