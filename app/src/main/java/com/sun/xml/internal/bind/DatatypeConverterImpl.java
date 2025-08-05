package com.sun.xml.internal.bind;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.WeakHashMap;
import javax.xml.bind.DatatypeConverterInterface;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.lingala.zip4j.crypto.PBKDF2.BinTools;

@Deprecated
/* loaded from: rt.jar:com/sun/xml/internal/bind/DatatypeConverterImpl.class */
public final class DatatypeConverterImpl implements DatatypeConverterInterface {

    @Deprecated
    public static final DatatypeConverterInterface theInstance;
    private static final byte[] decodeMap;
    private static final byte PADDING = Byte.MAX_VALUE;
    private static final char[] encodeMap;
    private static final Map<ClassLoader, DatatypeFactory> DF_CACHE;

    @Deprecated
    private static final char[] hexCode;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DatatypeConverterImpl.class.desiredAssertionStatus();
        theInstance = new DatatypeConverterImpl();
        decodeMap = initDecodeMap();
        encodeMap = initEncodeMap();
        DF_CACHE = Collections.synchronizedMap(new WeakHashMap());
        hexCode = BinTools.hex.toCharArray();
    }

    protected DatatypeConverterImpl() {
    }

    public static BigInteger _parseInteger(CharSequence s2) {
        return new BigInteger(removeOptionalPlus(WhiteSpaceProcessor.trim(s2)).toString());
    }

    public static String _printInteger(BigInteger val) {
        return val.toString();
    }

    public static int _parseInt(CharSequence s2) {
        int len = s2.length();
        int sign = 1;
        int r2 = 0;
        for (int i2 = 0; i2 < len; i2++) {
            char ch = s2.charAt(i2);
            if (!WhiteSpaceProcessor.isWhiteSpace(ch)) {
                if ('0' <= ch && ch <= '9') {
                    r2 = (r2 * 10) + (ch - '0');
                } else if (ch == '-') {
                    sign = -1;
                } else if (ch != '+') {
                    throw new NumberFormatException("Not a number: " + ((Object) s2));
                }
            }
        }
        return r2 * sign;
    }

    public static long _parseLong(CharSequence s2) {
        return Long.valueOf(removeOptionalPlus(WhiteSpaceProcessor.trim(s2)).toString()).longValue();
    }

    public static short _parseShort(CharSequence s2) {
        return (short) _parseInt(s2);
    }

    public static String _printShort(short val) {
        return String.valueOf((int) val);
    }

    public static BigDecimal _parseDecimal(CharSequence content) {
        CharSequence content2 = WhiteSpaceProcessor.trim(content);
        if (content2.length() <= 0) {
            return null;
        }
        return new BigDecimal(content2.toString());
    }

    public static float _parseFloat(CharSequence _val) {
        String s2 = WhiteSpaceProcessor.trim(_val).toString();
        if (s2.equals("NaN")) {
            return Float.NaN;
        }
        if (s2.equals("INF")) {
            return Float.POSITIVE_INFINITY;
        }
        if (s2.equals("-INF")) {
            return Float.NEGATIVE_INFINITY;
        }
        if (s2.length() == 0 || !isDigitOrPeriodOrSign(s2.charAt(0)) || !isDigitOrPeriodOrSign(s2.charAt(s2.length() - 1))) {
            throw new NumberFormatException();
        }
        return Float.parseFloat(s2);
    }

    public static String _printFloat(float v2) {
        if (Float.isNaN(v2)) {
            return "NaN";
        }
        if (v2 == Float.POSITIVE_INFINITY) {
            return "INF";
        }
        if (v2 == Float.NEGATIVE_INFINITY) {
            return "-INF";
        }
        return String.valueOf(v2);
    }

    public static double _parseDouble(CharSequence _val) {
        String val = WhiteSpaceProcessor.trim(_val).toString();
        if (val.equals("NaN")) {
            return Double.NaN;
        }
        if (val.equals("INF")) {
            return Double.POSITIVE_INFINITY;
        }
        if (val.equals("-INF")) {
            return Double.NEGATIVE_INFINITY;
        }
        if (val.length() == 0 || !isDigitOrPeriodOrSign(val.charAt(0)) || !isDigitOrPeriodOrSign(val.charAt(val.length() - 1))) {
            throw new NumberFormatException(val);
        }
        return Double.parseDouble(val);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00dd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Boolean _parseBoolean(java.lang.CharSequence r3) {
        /*
            Method dump skipped, instructions count: 267
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.bind.DatatypeConverterImpl._parseBoolean(java.lang.CharSequence):java.lang.Boolean");
    }

    public static String _printBoolean(boolean val) {
        return val ? "true" : "false";
    }

    public static byte _parseByte(CharSequence literal) {
        return (byte) _parseInt(literal);
    }

    public static String _printByte(byte val) {
        return String.valueOf((int) val);
    }

    public static QName _parseQName(CharSequence text, NamespaceContext nsc) {
        String prefix;
        String localPart;
        String uri;
        int length = text.length();
        int start = 0;
        while (start < length && WhiteSpaceProcessor.isWhiteSpace(text.charAt(start))) {
            start++;
        }
        int end = length;
        while (end > start && WhiteSpaceProcessor.isWhiteSpace(text.charAt(end - 1))) {
            end--;
        }
        if (end == start) {
            throw new IllegalArgumentException("input is empty");
        }
        int idx = start + 1;
        while (idx < end && text.charAt(idx) != ':') {
            idx++;
        }
        if (idx == end) {
            uri = nsc.getNamespaceURI("");
            localPart = text.subSequence(start, end).toString();
            prefix = "";
        } else {
            prefix = text.subSequence(start, idx).toString();
            localPart = text.subSequence(idx + 1, end).toString();
            uri = nsc.getNamespaceURI(prefix);
            if (uri == null || uri.length() == 0) {
                throw new IllegalArgumentException("prefix " + prefix + " is not bound to a namespace");
            }
        }
        return new QName(uri, localPart, prefix);
    }

    public static GregorianCalendar _parseDateTime(CharSequence s2) {
        String val = WhiteSpaceProcessor.trim(s2).toString();
        return getDatatypeFactory().newXMLGregorianCalendar(val).toGregorianCalendar();
    }

    public static String _printDateTime(Calendar val) {
        return CalendarFormatter.doFormat("%Y-%M-%DT%h:%m:%s%z", val);
    }

    public static String _printDate(Calendar val) {
        return CalendarFormatter.doFormat("%Y-%M-%D%z", val);
    }

    public static String _printInt(int val) {
        return String.valueOf(val);
    }

    public static String _printLong(long val) {
        return String.valueOf(val);
    }

    public static String _printDecimal(BigDecimal val) {
        return val.toPlainString();
    }

    public static String _printDouble(double v2) {
        if (Double.isNaN(v2)) {
            return "NaN";
        }
        if (v2 == Double.POSITIVE_INFINITY) {
            return "INF";
        }
        if (v2 == Double.NEGATIVE_INFINITY) {
            return "-INF";
        }
        return String.valueOf(v2);
    }

    public static String _printQName(QName val, NamespaceContext nsc) {
        String qname;
        String prefix = nsc.getPrefix(val.getNamespaceURI());
        String localPart = val.getLocalPart();
        if (prefix == null || prefix.length() == 0) {
            qname = localPart;
        } else {
            qname = prefix + ':' + localPart;
        }
        return qname;
    }

    private static byte[] initDecodeMap() {
        byte[] map = new byte[128];
        for (int i2 = 0; i2 < 128; i2++) {
            map[i2] = -1;
        }
        for (int i3 = 65; i3 <= 90; i3++) {
            map[i3] = (byte) (i3 - 65);
        }
        for (int i4 = 97; i4 <= 122; i4++) {
            map[i4] = (byte) ((i4 - 97) + 26);
        }
        for (int i5 = 48; i5 <= 57; i5++) {
            map[i5] = (byte) ((i5 - 48) + 52);
        }
        map[43] = 62;
        map[47] = 63;
        map[61] = Byte.MAX_VALUE;
        return map;
    }

    private static int guessLength(String text) {
        int len = text.length();
        int j2 = len - 1;
        while (true) {
            if (j2 < 0) {
                break;
            }
            byte code = decodeMap[text.charAt(j2)];
            if (code == Byte.MAX_VALUE) {
                j2--;
            } else if (code == -1) {
                return (text.length() / 4) * 3;
            }
        }
        int padSize = len - (j2 + 1);
        if (padSize > 2) {
            return (text.length() / 4) * 3;
        }
        return ((text.length() / 4) * 3) - padSize;
    }

    public static byte[] _parseBase64Binary(String text) {
        int buflen = guessLength(text);
        byte[] out = new byte[buflen];
        int o2 = 0;
        int len = text.length();
        byte[] quadruplet = new byte[4];
        int q2 = 0;
        for (int i2 = 0; i2 < len; i2++) {
            char ch = text.charAt(i2);
            byte v2 = decodeMap[ch];
            if (v2 != -1) {
                int i3 = q2;
                q2++;
                quadruplet[i3] = v2;
            }
            if (q2 == 4) {
                int i4 = o2;
                o2++;
                out[i4] = (byte) ((quadruplet[0] << 2) | (quadruplet[1] >> 4));
                if (quadruplet[2] != Byte.MAX_VALUE) {
                    o2++;
                    out[o2] = (byte) ((quadruplet[1] << 4) | (quadruplet[2] >> 2));
                }
                if (quadruplet[3] != Byte.MAX_VALUE) {
                    int i5 = o2;
                    o2++;
                    out[i5] = (byte) ((quadruplet[2] << 6) | quadruplet[3]);
                }
                q2 = 0;
            }
        }
        if (buflen == o2) {
            return out;
        }
        byte[] nb = new byte[o2];
        System.arraycopy(out, 0, nb, 0, o2);
        return nb;
    }

    private static char[] initEncodeMap() {
        char[] map = new char[64];
        for (int i2 = 0; i2 < 26; i2++) {
            map[i2] = (char) (65 + i2);
        }
        for (int i3 = 26; i3 < 52; i3++) {
            map[i3] = (char) (97 + (i3 - 26));
        }
        for (int i4 = 52; i4 < 62; i4++) {
            map[i4] = (char) (48 + (i4 - 52));
        }
        map[62] = '+';
        map[63] = '/';
        return map;
    }

    public static char encode(int i2) {
        return encodeMap[i2 & 63];
    }

    public static byte encodeByte(int i2) {
        return (byte) encodeMap[i2 & 63];
    }

    public static String _printBase64Binary(byte[] input) {
        return _printBase64Binary(input, 0, input.length);
    }

    public static String _printBase64Binary(byte[] input, int offset, int len) {
        char[] buf = new char[((len + 2) / 3) * 4];
        int ptr = _printBase64Binary(input, offset, len, buf, 0);
        if ($assertionsDisabled || ptr == buf.length) {
            return new String(buf);
        }
        throw new AssertionError();
    }

    public static int _printBase64Binary(byte[] input, int offset, int len, char[] buf, int ptr) {
        int remaining = len;
        int i2 = offset;
        while (remaining >= 3) {
            int i3 = ptr;
            int ptr2 = ptr + 1;
            buf[i3] = encode(input[i2] >> 2);
            int ptr3 = ptr2 + 1;
            buf[ptr2] = encode(((input[i2] & 3) << 4) | ((input[i2 + 1] >> 4) & 15));
            int ptr4 = ptr3 + 1;
            buf[ptr3] = encode(((input[i2 + 1] & 15) << 2) | ((input[i2 + 2] >> 6) & 3));
            ptr = ptr4 + 1;
            buf[ptr4] = encode(input[i2 + 2] & 63);
            remaining -= 3;
            i2 += 3;
        }
        if (remaining == 1) {
            int i4 = ptr;
            int ptr5 = ptr + 1;
            buf[i4] = encode(input[i2] >> 2);
            int ptr6 = ptr5 + 1;
            buf[ptr5] = encode((input[i2] & 3) << 4);
            int ptr7 = ptr6 + 1;
            buf[ptr6] = '=';
            ptr = ptr7 + 1;
            buf[ptr7] = '=';
        }
        if (remaining == 2) {
            int i5 = ptr;
            int ptr8 = ptr + 1;
            buf[i5] = encode(input[i2] >> 2);
            int ptr9 = ptr8 + 1;
            buf[ptr8] = encode(((input[i2] & 3) << 4) | ((input[i2 + 1] >> 4) & 15));
            int ptr10 = ptr9 + 1;
            buf[ptr9] = encode((input[i2 + 1] & 15) << 2);
            ptr = ptr10 + 1;
            buf[ptr10] = '=';
        }
        return ptr;
    }

    public static void _printBase64Binary(byte[] input, int offset, int len, XMLStreamWriter output) throws XMLStreamException {
        int remaining = len;
        char[] buf = new char[4];
        int i2 = offset;
        while (remaining >= 3) {
            buf[0] = encode(input[i2] >> 2);
            buf[1] = encode(((input[i2] & 3) << 4) | ((input[i2 + 1] >> 4) & 15));
            buf[2] = encode(((input[i2 + 1] & 15) << 2) | ((input[i2 + 2] >> 6) & 3));
            buf[3] = encode(input[i2 + 2] & 63);
            output.writeCharacters(buf, 0, 4);
            remaining -= 3;
            i2 += 3;
        }
        if (remaining == 1) {
            buf[0] = encode(input[i2] >> 2);
            buf[1] = encode((input[i2] & 3) << 4);
            buf[2] = '=';
            buf[3] = '=';
            output.writeCharacters(buf, 0, 4);
        }
        if (remaining == 2) {
            buf[0] = encode(input[i2] >> 2);
            buf[1] = encode(((input[i2] & 3) << 4) | ((input[i2 + 1] >> 4) & 15));
            buf[2] = encode((input[i2 + 1] & 15) << 2);
            buf[3] = '=';
            output.writeCharacters(buf, 0, 4);
        }
    }

    public static int _printBase64Binary(byte[] input, int offset, int len, byte[] out, int ptr) {
        int remaining = len;
        int i2 = offset;
        while (remaining >= 3) {
            int i3 = ptr;
            int ptr2 = ptr + 1;
            out[i3] = encodeByte(input[i2] >> 2);
            int ptr3 = ptr2 + 1;
            out[ptr2] = encodeByte(((input[i2] & 3) << 4) | ((input[i2 + 1] >> 4) & 15));
            int ptr4 = ptr3 + 1;
            out[ptr3] = encodeByte(((input[i2 + 1] & 15) << 2) | ((input[i2 + 2] >> 6) & 3));
            ptr = ptr4 + 1;
            out[ptr4] = encodeByte(input[i2 + 2] & 63);
            remaining -= 3;
            i2 += 3;
        }
        if (remaining == 1) {
            int i4 = ptr;
            int ptr5 = ptr + 1;
            out[i4] = encodeByte(input[i2] >> 2);
            int ptr6 = ptr5 + 1;
            out[ptr5] = encodeByte((input[i2] & 3) << 4);
            int ptr7 = ptr6 + 1;
            out[ptr6] = 61;
            ptr = ptr7 + 1;
            out[ptr7] = 61;
        }
        if (remaining == 2) {
            int i5 = ptr;
            int ptr8 = ptr + 1;
            out[i5] = encodeByte(input[i2] >> 2);
            int ptr9 = ptr8 + 1;
            out[ptr8] = encodeByte(((input[i2] & 3) << 4) | ((input[i2 + 1] >> 4) & 15));
            int ptr10 = ptr9 + 1;
            out[ptr9] = encodeByte((input[i2 + 1] & 15) << 2);
            ptr = ptr10 + 1;
            out[ptr10] = 61;
        }
        return ptr;
    }

    private static CharSequence removeOptionalPlus(CharSequence s2) {
        int len = s2.length();
        if (len <= 1 || s2.charAt(0) != '+') {
            return s2;
        }
        CharSequence s3 = s2.subSequence(1, len);
        char ch = s3.charAt(0);
        if ('0' <= ch && ch <= '9') {
            return s3;
        }
        if ('.' == ch) {
            return s3;
        }
        throw new NumberFormatException();
    }

    private static boolean isDigitOrPeriodOrSign(char ch) {
        if (('0' <= ch && ch <= '9') || ch == '+' || ch == '-' || ch == '.') {
            return true;
        }
        return false;
    }

    public static DatatypeFactory getDatatypeFactory() {
        ClassLoader tccl = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.xml.internal.bind.DatatypeConverterImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
        DatatypeFactory df = DF_CACHE.get(tccl);
        if (df == null) {
            synchronized (DatatypeConverterImpl.class) {
                df = DF_CACHE.get(tccl);
                if (df == null) {
                    try {
                        df = DatatypeFactory.newInstance();
                        DF_CACHE.put(tccl, df);
                    } catch (DatatypeConfigurationException e2) {
                        throw new Error(Messages.FAILED_TO_INITIALE_DATATYPE_FACTORY.format(new Object[0]), e2);
                    }
                }
            }
        }
        return df;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/DatatypeConverterImpl$CalendarFormatter.class */
    private static final class CalendarFormatter {
        private CalendarFormatter() {
        }

        public static String doFormat(String format, Calendar cal) throws IllegalArgumentException {
            int fidx = 0;
            int flen = format.length();
            StringBuilder buf = new StringBuilder();
            while (fidx < flen) {
                int i2 = fidx;
                fidx++;
                char fch = format.charAt(i2);
                if (fch != '%') {
                    buf.append(fch);
                } else {
                    fidx++;
                    switch (format.charAt(fidx)) {
                        case 'D':
                            formatDays(cal, buf);
                            break;
                        case 'M':
                            formatMonth(cal, buf);
                            break;
                        case 'Y':
                            formatYear(cal, buf);
                            break;
                        case 'h':
                            formatHours(cal, buf);
                            break;
                        case 'm':
                            formatMinutes(cal, buf);
                            break;
                        case 's':
                            formatSeconds(cal, buf);
                            break;
                        case 'z':
                            formatTimeZone(cal, buf);
                            break;
                        default:
                            throw new InternalError();
                    }
                }
            }
            return buf.toString();
        }

        private static void formatYear(Calendar cal, StringBuilder buf) {
            String string;
            String s2;
            int year = cal.get(1);
            if (year <= 0) {
                string = Integer.toString(1 - year);
            } else {
                string = Integer.toString(year);
            }
            while (true) {
                s2 = string;
                if (s2.length() >= 4) {
                    break;
                } else {
                    string = '0' + s2;
                }
            }
            if (year <= 0) {
                s2 = '-' + s2;
            }
            buf.append(s2);
        }

        private static void formatMonth(Calendar cal, StringBuilder buf) {
            formatTwoDigits(cal.get(2) + 1, buf);
        }

        private static void formatDays(Calendar cal, StringBuilder buf) {
            formatTwoDigits(cal.get(5), buf);
        }

        private static void formatHours(Calendar cal, StringBuilder buf) {
            formatTwoDigits(cal.get(11), buf);
        }

        private static void formatMinutes(Calendar cal, StringBuilder buf) {
            formatTwoDigits(cal.get(12), buf);
        }

        private static void formatSeconds(Calendar cal, StringBuilder buf) {
            int n2;
            formatTwoDigits(cal.get(13), buf);
            if (cal.isSet(14) && (n2 = cal.get(14)) != 0) {
                String string = Integer.toString(n2);
                while (true) {
                    String ms = string;
                    if (ms.length() < 3) {
                        string = '0' + ms;
                    } else {
                        buf.append('.');
                        buf.append(ms);
                        return;
                    }
                }
            }
        }

        private static void formatTimeZone(Calendar cal, StringBuilder buf) {
            TimeZone tz = cal.getTimeZone();
            if (tz == null) {
                return;
            }
            int offset = tz.getOffset(cal.getTime().getTime());
            if (offset == 0) {
                buf.append('Z');
                return;
            }
            if (offset >= 0) {
                buf.append('+');
            } else {
                buf.append('-');
                offset *= -1;
            }
            int offset2 = offset / 60000;
            formatTwoDigits(offset2 / 60, buf);
            buf.append(':');
            formatTwoDigits(offset2 % 60, buf);
        }

        private static void formatTwoDigits(int n2, StringBuilder buf) {
            if (n2 < 10) {
                buf.append('0');
            }
            buf.append(n2);
        }
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String parseString(String lexicalXSDString) {
        return lexicalXSDString;
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public BigInteger parseInteger(String lexicalXSDInteger) {
        return _parseInteger(lexicalXSDInteger);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printInteger(BigInteger val) {
        return _printInteger(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public int parseInt(String s2) {
        return _parseInt(s2);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public long parseLong(String lexicalXSLong) {
        return _parseLong(lexicalXSLong);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public short parseShort(String lexicalXSDShort) {
        return _parseShort(lexicalXSDShort);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printShort(short val) {
        return _printShort(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public BigDecimal parseDecimal(String content) {
        return _parseDecimal(content);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public float parseFloat(String lexicalXSDFloat) {
        return _parseFloat(lexicalXSDFloat);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printFloat(float v2) {
        return _printFloat(v2);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public double parseDouble(String lexicalXSDDouble) {
        return _parseDouble(lexicalXSDDouble);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public boolean parseBoolean(String lexicalXSDBoolean) {
        Boolean b2 = _parseBoolean(lexicalXSDBoolean);
        if (b2 == null) {
            return false;
        }
        return b2.booleanValue();
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printBoolean(boolean val) {
        return val ? "true" : "false";
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public byte parseByte(String lexicalXSDByte) {
        return _parseByte(lexicalXSDByte);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printByte(byte val) {
        return _printByte(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public QName parseQName(String lexicalXSDQName, NamespaceContext nsc) {
        return _parseQName(lexicalXSDQName, nsc);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public Calendar parseDateTime(String lexicalXSDDateTime) {
        return _parseDateTime(lexicalXSDDateTime);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printDateTime(Calendar val) {
        return _printDateTime(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public byte[] parseBase64Binary(String lexicalXSDBase64Binary) {
        return _parseBase64Binary(lexicalXSDBase64Binary);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public byte[] parseHexBinary(String s2) {
        int len = s2.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + s2);
        }
        byte[] out = new byte[len / 2];
        for (int i2 = 0; i2 < len; i2 += 2) {
            int h2 = hexToBin(s2.charAt(i2));
            int l2 = hexToBin(s2.charAt(i2 + 1));
            if (h2 == -1 || l2 == -1) {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + s2);
            }
            out[i2 / 2] = (byte) ((h2 * 16) + l2);
        }
        return out;
    }

    @Deprecated
    private static int hexToBin(char ch) {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        if ('A' <= ch && ch <= 'F') {
            return (ch - 'A') + 10;
        }
        if ('a' <= ch && ch <= 'f') {
            return (ch - 'a') + 10;
        }
        return -1;
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printHexBinary(byte[] data) {
        StringBuilder r2 = new StringBuilder(data.length * 2);
        for (byte b2 : data) {
            r2.append(hexCode[(b2 >> 4) & 15]);
            r2.append(hexCode[b2 & 15]);
        }
        return r2.toString();
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public long parseUnsignedInt(String lexicalXSDUnsignedInt) {
        return _parseLong(lexicalXSDUnsignedInt);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printUnsignedInt(long val) {
        return _printLong(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public int parseUnsignedShort(String lexicalXSDUnsignedShort) {
        return _parseInt(lexicalXSDUnsignedShort);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public Calendar parseTime(String lexicalXSDTime) {
        return getDatatypeFactory().newXMLGregorianCalendar(lexicalXSDTime).toGregorianCalendar();
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printTime(Calendar val) {
        return CalendarFormatter.doFormat("%h:%m:%s%z", val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public Calendar parseDate(String lexicalXSDDate) {
        return getDatatypeFactory().newXMLGregorianCalendar(lexicalXSDDate).toGregorianCalendar();
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printDate(Calendar val) {
        return _printDate(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String parseAnySimpleType(String lexicalXSDAnySimpleType) {
        return lexicalXSDAnySimpleType;
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printString(String val) {
        return val;
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printInt(int val) {
        return _printInt(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printLong(long val) {
        return _printLong(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printDecimal(BigDecimal val) {
        return _printDecimal(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printDouble(double v2) {
        return _printDouble(v2);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printQName(QName val, NamespaceContext nsc) {
        return _printQName(val, nsc);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printBase64Binary(byte[] val) {
        return _printBase64Binary(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printUnsignedShort(int val) {
        return String.valueOf(val);
    }

    @Override // javax.xml.bind.DatatypeConverterInterface
    @Deprecated
    public String printAnySimpleType(String val) {
        return val;
    }
}
