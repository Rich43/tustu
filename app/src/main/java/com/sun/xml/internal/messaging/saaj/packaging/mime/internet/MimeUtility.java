package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64DecoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64EncoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QDecoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPDecoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPEncoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.UUDecoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.UUEncoderStream;
import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/MimeUtility.class */
public class MimeUtility {
    public static final int ALL = -1;
    private static final int BUFFER_SIZE = 1024;
    private static boolean decodeStrict;
    private static boolean encodeEolStrict;
    private static boolean foldEncodedWords;
    private static boolean foldText;
    private static String defaultJavaCharset;
    private static String defaultMIMECharset;
    private static Hashtable mime2java;
    private static Hashtable java2mime;
    static final int ALL_ASCII = 1;
    static final int MOSTLY_ASCII = 2;
    static final int MOSTLY_NONASCII = 3;

    private MimeUtility() {
    }

    static {
        decodeStrict = true;
        encodeEolStrict = false;
        foldEncodedWords = false;
        foldText = true;
        try {
            String s2 = SAAJUtil.getSystemProperty("mail.mime.decodetext.strict");
            decodeStrict = s2 == null || !s2.equalsIgnoreCase("false");
            String s3 = SAAJUtil.getSystemProperty("mail.mime.encodeeol.strict");
            encodeEolStrict = s3 != null && s3.equalsIgnoreCase("true");
            String s4 = SAAJUtil.getSystemProperty("mail.mime.foldencodedwords");
            foldEncodedWords = s4 != null && s4.equalsIgnoreCase("true");
            String s5 = SAAJUtil.getSystemProperty("mail.mime.foldtext");
            foldText = s5 == null || !s5.equalsIgnoreCase("false");
        } catch (SecurityException e2) {
        }
        java2mime = new Hashtable(40);
        mime2java = new Hashtable(10);
        try {
            InputStream is = MimeUtility.class.getResourceAsStream("/META-INF/javamail.charset.map");
            if (is != null) {
                InputStream is2 = new LineInputStream(is);
                loadMappings((LineInputStream) is2, java2mime);
                loadMappings((LineInputStream) is2, mime2java);
            }
        } catch (Exception e3) {
        }
        if (java2mime.isEmpty()) {
            java2mime.put("8859_1", FTP.DEFAULT_CONTROL_ENCODING);
            java2mime.put("iso8859_1", FTP.DEFAULT_CONTROL_ENCODING);
            java2mime.put("ISO8859-1", FTP.DEFAULT_CONTROL_ENCODING);
            java2mime.put("8859_2", "ISO-8859-2");
            java2mime.put("iso8859_2", "ISO-8859-2");
            java2mime.put("ISO8859-2", "ISO-8859-2");
            java2mime.put("8859_3", "ISO-8859-3");
            java2mime.put("iso8859_3", "ISO-8859-3");
            java2mime.put("ISO8859-3", "ISO-8859-3");
            java2mime.put("8859_4", "ISO-8859-4");
            java2mime.put("iso8859_4", "ISO-8859-4");
            java2mime.put("ISO8859-4", "ISO-8859-4");
            java2mime.put("8859_5", "ISO-8859-5");
            java2mime.put("iso8859_5", "ISO-8859-5");
            java2mime.put("ISO8859-5", "ISO-8859-5");
            java2mime.put("8859_6", "ISO-8859-6");
            java2mime.put("iso8859_6", "ISO-8859-6");
            java2mime.put("ISO8859-6", "ISO-8859-6");
            java2mime.put("8859_7", "ISO-8859-7");
            java2mime.put("iso8859_7", "ISO-8859-7");
            java2mime.put("ISO8859-7", "ISO-8859-7");
            java2mime.put("8859_8", "ISO-8859-8");
            java2mime.put("iso8859_8", "ISO-8859-8");
            java2mime.put("ISO8859-8", "ISO-8859-8");
            java2mime.put("8859_9", "ISO-8859-9");
            java2mime.put("iso8859_9", "ISO-8859-9");
            java2mime.put("ISO8859-9", "ISO-8859-9");
            java2mime.put("SJIS", "Shift_JIS");
            java2mime.put("MS932", "Shift_JIS");
            java2mime.put("JIS", "ISO-2022-JP");
            java2mime.put("ISO2022JP", "ISO-2022-JP");
            java2mime.put("EUC_JP", "euc-jp");
            java2mime.put("KOI8_R", "koi8-r");
            java2mime.put("EUC_CN", "euc-cn");
            java2mime.put("EUC_TW", "euc-tw");
            java2mime.put("EUC_KR", "euc-kr");
        }
        if (mime2java.isEmpty()) {
            mime2java.put("iso-2022-cn", "ISO2022CN");
            mime2java.put("iso-2022-kr", "ISO2022KR");
            mime2java.put("utf-8", InternalZipConstants.CHARSET_UTF8);
            mime2java.put("utf8", InternalZipConstants.CHARSET_UTF8);
            mime2java.put("ja_jp.iso2022-7", "ISO2022JP");
            mime2java.put("ja_jp.eucjp", "EUCJIS");
            mime2java.put("euc-kr", "KSC5601");
            mime2java.put("euckr", "KSC5601");
            mime2java.put("us-ascii", FTP.DEFAULT_CONTROL_ENCODING);
            mime2java.put("x-us-ascii", FTP.DEFAULT_CONTROL_ENCODING);
        }
    }

    public static String getEncoding(DataSource ds) {
        String encoding;
        try {
            ContentType cType = new ContentType(ds.getContentType());
            InputStream is = ds.getInputStream();
            boolean isText = cType.match("text/*");
            int i2 = checkAscii(is, -1, !isText);
            switch (i2) {
                case 1:
                    encoding = "7bit";
                    break;
                case 2:
                    encoding = "quoted-printable";
                    break;
                default:
                    encoding = "base64";
                    break;
            }
            try {
                is.close();
            } catch (IOException e2) {
            }
            return encoding;
        } catch (Exception e3) {
            return "base64";
        }
    }

    public static String getEncoding(DataHandler dh) {
        String encoding;
        if (dh.getName() != null) {
            return getEncoding(dh.getDataSource());
        }
        try {
            ContentType cType = new ContentType(dh.getContentType());
            if (cType.match("text/*")) {
                AsciiOutputStream aos = new AsciiOutputStream(false, false);
                try {
                    dh.writeTo(aos);
                } catch (IOException e2) {
                }
                switch (aos.getAscii()) {
                    case 1:
                        encoding = "7bit";
                        break;
                    case 2:
                        encoding = "quoted-printable";
                        break;
                    default:
                        encoding = "base64";
                        break;
                }
            } else {
                AsciiOutputStream aos2 = new AsciiOutputStream(true, encodeEolStrict);
                try {
                    dh.writeTo(aos2);
                } catch (IOException e3) {
                }
                if (aos2.getAscii() == 1) {
                    encoding = "7bit";
                } else {
                    encoding = "base64";
                }
            }
            return encoding;
        } catch (Exception e4) {
            return "base64";
        }
    }

    public static InputStream decode(InputStream is, String encoding) throws MessagingException {
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64DecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPDecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUDecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return is;
        }
        throw new MessagingException("Unknown encoding: " + encoding);
    }

    public static OutputStream encode(OutputStream os, String encoding) throws MessagingException {
        if (encoding == null) {
            return os;
        }
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64EncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return os;
        }
        throw new MessagingException("Unknown encoding: " + encoding);
    }

    public static OutputStream encode(OutputStream os, String encoding, String filename) throws MessagingException {
        if (encoding == null) {
            return os;
        }
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64EncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUEncoderStream(os, filename);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return os;
        }
        throw new MessagingException("Unknown encoding: " + encoding);
    }

    public static String encodeText(String text) throws UnsupportedEncodingException {
        return encodeText(text, null, null);
    }

    public static String encodeText(String text, String charset, String encoding) throws UnsupportedEncodingException {
        return encodeWord(text, charset, encoding, false);
    }

    public static String decodeText(String etext) throws UnsupportedEncodingException {
        String word;
        if (etext.indexOf("=?") == -1) {
            return etext;
        }
        StringTokenizer st = new StringTokenizer(etext, " \t\n\r", true);
        StringBuffer sb = new StringBuffer();
        StringBuffer wsb = new StringBuffer();
        boolean prevWasEncoded = false;
        while (st.hasMoreTokens()) {
            String s2 = st.nextToken();
            char c2 = s2.charAt(0);
            if (c2 == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n') {
                wsb.append(c2);
            } else {
                try {
                    word = decodeWord(s2);
                    if (!prevWasEncoded && wsb.length() > 0) {
                        sb.append(wsb);
                    }
                    prevWasEncoded = true;
                } catch (ParseException e2) {
                    word = s2;
                    if (!decodeStrict) {
                        word = decodeInnerWords(word);
                    }
                    if (wsb.length() > 0) {
                        sb.append(wsb);
                    }
                    prevWasEncoded = false;
                }
                sb.append(word);
                wsb.setLength(0);
            }
        }
        return sb.toString();
    }

    public static String encodeWord(String word) throws UnsupportedEncodingException {
        return encodeWord(word, null, null);
    }

    public static String encodeWord(String word, String charset, String encoding) throws UnsupportedEncodingException {
        return encodeWord(word, charset, encoding, true);
    }

    private static String encodeWord(String string, String charset, String encoding, boolean encodingWord) throws UnsupportedEncodingException {
        String jcharset;
        boolean b64;
        int ascii = checkAscii(string);
        if (ascii == 1) {
            return string;
        }
        if (charset == null) {
            jcharset = getDefaultJavaCharset();
            charset = getDefaultMIMECharset();
        } else {
            jcharset = javaCharset(charset);
        }
        if (encoding == null) {
            if (ascii != 3) {
                encoding = "Q";
            } else {
                encoding = PdfOps.B_TOKEN;
            }
        }
        if (encoding.equalsIgnoreCase(PdfOps.B_TOKEN)) {
            b64 = true;
        } else if (encoding.equalsIgnoreCase("Q")) {
            b64 = false;
        } else {
            throw new UnsupportedEncodingException("Unknown transfer encoding: " + encoding);
        }
        StringBuffer outb = new StringBuffer();
        doEncode(string, b64, jcharset, 68 - charset.length(), "=?" + charset + "?" + encoding + "?", true, encodingWord, outb);
        return outb.toString();
    }

    private static void doEncode(String string, boolean b64, String jcharset, int avail, String prefix, boolean first, boolean encodingWord, StringBuffer buf) throws UnsupportedEncodingException {
        int len;
        OutputStream eos;
        int size;
        byte[] bytes = string.getBytes(jcharset);
        if (b64) {
            len = BEncoderStream.encodedLength(bytes);
        } else {
            len = QEncoderStream.encodedLength(bytes, encodingWord);
        }
        if (len > avail && (size = string.length()) > 1) {
            doEncode(string.substring(0, size / 2), b64, jcharset, avail, prefix, first, encodingWord, buf);
            doEncode(string.substring(size / 2, size), b64, jcharset, avail, prefix, false, encodingWord, buf);
            return;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        if (b64) {
            eos = new BEncoderStream(os);
        } else {
            eos = new QEncoderStream(os, encodingWord);
        }
        try {
            eos.write(bytes);
            eos.close();
        } catch (IOException e2) {
        }
        byte[] encodedBytes = os.toByteArray();
        if (!first) {
            if (foldEncodedWords) {
                buf.append("\r\n ");
            } else {
                buf.append(" ");
            }
        }
        buf.append(prefix);
        for (byte b2 : encodedBytes) {
            buf.append((char) b2);
        }
        buf.append("?=");
    }

    public static String decodeWord(String eword) throws ParseException, UnsupportedEncodingException {
        InputStream is;
        if (!eword.startsWith("=?")) {
            throw new ParseException();
        }
        int pos = eword.indexOf(63, 2);
        if (pos == -1) {
            throw new ParseException();
        }
        String charset = javaCharset(eword.substring(2, pos));
        int start = pos + 1;
        int pos2 = eword.indexOf(63, start);
        if (pos2 == -1) {
            throw new ParseException();
        }
        String encoding = eword.substring(start, pos2);
        int start2 = pos2 + 1;
        int pos3 = eword.indexOf("?=", start2);
        if (pos3 == -1) {
            throw new ParseException();
        }
        String word = eword.substring(start2, pos3);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(ASCIIUtility.getBytes(word));
            if (encoding.equalsIgnoreCase(PdfOps.B_TOKEN)) {
                is = new BASE64DecoderStream(bis);
            } else if (encoding.equalsIgnoreCase("Q")) {
                is = new QDecoderStream(bis);
            } else {
                throw new UnsupportedEncodingException("unknown encoding: " + encoding);
            }
            int count = bis.available();
            byte[] bytes = new byte[count];
            String s2 = new String(bytes, 0, is.read(bytes, 0, count), charset);
            if (pos3 + 2 < eword.length()) {
                String rest = eword.substring(pos3 + 2);
                if (!decodeStrict) {
                    rest = decodeInnerWords(rest);
                }
                s2 = s2 + rest;
            }
            return s2;
        } catch (UnsupportedEncodingException uex) {
            throw uex;
        } catch (IOException e2) {
            throw new ParseException();
        } catch (IllegalArgumentException e3) {
            throw new UnsupportedEncodingException();
        }
    }

    private static String decodeInnerWords(String word) throws UnsupportedEncodingException {
        int start = 0;
        StringBuffer buf = new StringBuffer();
        while (true) {
            int i2 = word.indexOf("=?", start);
            if (i2 < 0) {
                break;
            }
            buf.append(word.substring(start, i2));
            int end = word.indexOf("?=", i2);
            if (end < 0) {
                break;
            }
            String s2 = word.substring(i2, end + 2);
            try {
                s2 = decodeWord(s2);
            } catch (ParseException e2) {
            }
            buf.append(s2);
            start = end + 2;
        }
        if (start == 0) {
            return word;
        }
        if (start < word.length()) {
            buf.append(word.substring(start));
        }
        return buf.toString();
    }

    public static String quote(String word, String specials) {
        int len = word.length();
        boolean needQuoting = false;
        for (int i2 = 0; i2 < len; i2++) {
            char c2 = word.charAt(i2);
            if (c2 == '\"' || c2 == '\\' || c2 == '\r' || c2 == '\n') {
                StringBuffer sb = new StringBuffer(len + 3);
                sb.append('\"');
                sb.append(word.substring(0, i2));
                int lastc = 0;
                for (int j2 = i2; j2 < len; j2++) {
                    char cc = word.charAt(j2);
                    if ((cc == '\"' || cc == '\\' || cc == '\r' || cc == '\n') && (cc != '\n' || lastc != 13)) {
                        sb.append('\\');
                    }
                    sb.append(cc);
                    lastc = cc;
                }
                sb.append('\"');
                return sb.toString();
            }
            if (c2 < ' ' || c2 >= 127 || specials.indexOf(c2) >= 0) {
                needQuoting = true;
            }
        }
        if (needQuoting) {
            StringBuffer sb2 = new StringBuffer(len + 2);
            sb2.append('\"').append(word).append('\"');
            return sb2.toString();
        }
        return word;
    }

    static String fold(int used, String s2) {
        char c2;
        if (!foldText) {
            return s2;
        }
        int end = s2.length() - 1;
        while (end >= 0 && ((c2 = s2.charAt(end)) == ' ' || c2 == '\t')) {
            end--;
        }
        if (end != s2.length() - 1) {
            s2 = s2.substring(0, end + 1);
        }
        if (used + s2.length() <= 76) {
            return s2;
        }
        StringBuffer sb = new StringBuffer(s2.length() + 4);
        char lastc = 0;
        while (true) {
            if (used + s2.length() <= 76) {
                break;
            }
            int lastspace = -1;
            for (int i2 = 0; i2 < s2.length() && (lastspace == -1 || used + i2 <= 76); i2++) {
                char c3 = s2.charAt(i2);
                if ((c3 == ' ' || c3 == '\t') && lastc != ' ' && lastc != '\t') {
                    lastspace = i2;
                }
                lastc = c3;
            }
            if (lastspace == -1) {
                sb.append(s2);
                s2 = "";
                break;
            }
            sb.append(s2.substring(0, lastspace));
            sb.append("\r\n");
            lastc = s2.charAt(lastspace);
            sb.append(lastc);
            s2 = s2.substring(lastspace + 1);
            used = 1;
        }
        sb.append(s2);
        return sb.toString();
    }

    static String unfold(String s2) {
        char c2;
        char c3;
        if (!foldText) {
            return s2;
        }
        StringBuffer sb = null;
        while (true) {
            int i2 = indexOfAny(s2, "\r\n");
            if (i2 < 0) {
                break;
            }
            int l2 = s2.length();
            int i3 = i2 + 1;
            if (i3 < l2 && s2.charAt(i3 - 1) == '\r' && s2.charAt(i3) == '\n') {
                i3++;
            }
            if (i2 == 0 || s2.charAt(i2 - 1) != '\\') {
                if (i3 < l2 && ((c2 = s2.charAt(i3)) == ' ' || c2 == '\t')) {
                    while (true) {
                        i3++;
                        if (i3 >= l2 || ((c3 = s2.charAt(i3)) != ' ' && c3 != '\t')) {
                            break;
                        }
                    }
                    if (sb == null) {
                        sb = new StringBuffer(s2.length());
                    }
                    if (i2 != 0) {
                        sb.append(s2.substring(0, i2));
                        sb.append(' ');
                    }
                    s2 = s2.substring(i3);
                } else {
                    if (sb == null) {
                        sb = new StringBuffer(s2.length());
                    }
                    sb.append(s2.substring(0, i3));
                    s2 = s2.substring(i3);
                }
            } else {
                if (sb == null) {
                    sb = new StringBuffer(s2.length());
                }
                sb.append(s2.substring(0, i2 - 1));
                sb.append(s2.substring(i2, i3));
                s2 = s2.substring(i3);
            }
        }
        if (sb != null) {
            sb.append(s2);
            return sb.toString();
        }
        return s2;
    }

    private static int indexOfAny(String s2, String any) {
        return indexOfAny(s2, any, 0);
    }

    private static int indexOfAny(String s2, String any, int start) {
        try {
            int len = s2.length();
            for (int i2 = start; i2 < len; i2++) {
                if (any.indexOf(s2.charAt(i2)) >= 0) {
                    return i2;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException e2) {
            return -1;
        }
    }

    public static String javaCharset(String charset) {
        if (mime2java == null || charset == null) {
            return charset;
        }
        String alias = (String) mime2java.get(charset.toLowerCase());
        return alias == null ? charset : alias;
    }

    public static String mimeCharset(String charset) {
        if (java2mime == null || charset == null) {
            return charset;
        }
        String alias = (String) java2mime.get(charset.toLowerCase());
        return alias == null ? charset : alias;
    }

    public static String getDefaultJavaCharset() {
        if (defaultJavaCharset == null) {
            String mimecs = SAAJUtil.getSystemProperty("mail.mime.charset");
            if (mimecs != null && mimecs.length() > 0) {
                defaultJavaCharset = javaCharset(mimecs);
                return defaultJavaCharset;
            }
            try {
                defaultJavaCharset = System.getProperty("file.encoding", "8859_1");
            } catch (SecurityException e2) {
                InputStreamReader reader = new InputStreamReader(new InputStream() { // from class: com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility.1NullInputStream
                    @Override // java.io.InputStream
                    public int read() {
                        return 0;
                    }
                });
                defaultJavaCharset = reader.getEncoding();
                if (defaultJavaCharset == null) {
                    defaultJavaCharset = "8859_1";
                }
            }
        }
        return defaultJavaCharset;
    }

    static String getDefaultMIMECharset() {
        if (defaultMIMECharset == null) {
            defaultMIMECharset = SAAJUtil.getSystemProperty("mail.mime.charset");
        }
        if (defaultMIMECharset == null) {
            defaultMIMECharset = mimeCharset(getDefaultJavaCharset());
        }
        return defaultMIMECharset;
    }

    private static void loadMappings(LineInputStream is, Hashtable table) {
        while (true) {
            try {
                String currLine = is.readLine();
                if (currLine != null) {
                    if (!currLine.startsWith("--") || !currLine.endsWith("--")) {
                        if (currLine.trim().length() != 0 && !currLine.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                            StringTokenizer tk = new StringTokenizer(currLine, " \t");
                            try {
                                String key = tk.nextToken();
                                String value = tk.nextToken();
                                table.put(key.toLowerCase(), value);
                            } catch (NoSuchElementException e2) {
                            }
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } catch (IOException e3) {
                return;
            }
        }
    }

    static int checkAscii(String s2) {
        int ascii = 0;
        int non_ascii = 0;
        int l2 = s2.length();
        for (int i2 = 0; i2 < l2; i2++) {
            if (nonascii(s2.charAt(i2))) {
                non_ascii++;
            } else {
                ascii++;
            }
        }
        if (non_ascii == 0) {
            return 1;
        }
        if (ascii > non_ascii) {
            return 2;
        }
        return 3;
    }

    static int checkAscii(byte[] b2) {
        int ascii = 0;
        int non_ascii = 0;
        for (byte b3 : b2) {
            if (nonascii(b3 & 255)) {
                non_ascii++;
            } else {
                ascii++;
            }
        }
        if (non_ascii == 0) {
            return 1;
        }
        if (ascii > non_ascii) {
            return 2;
        }
        return 3;
    }

    static int checkAscii(InputStream is, int max, boolean breakOnNonAscii) {
        int ascii = 0;
        int non_ascii = 0;
        int block = 4096;
        int linelen = 0;
        boolean longLine = false;
        boolean badEOL = false;
        boolean checkEOL = encodeEolStrict && breakOnNonAscii;
        byte[] buf = null;
        if (max != 0) {
            block = max == -1 ? 4096 : Math.min(max, 4096);
            buf = new byte[block];
        }
        while (max != 0) {
            try {
                int len = is.read(buf, 0, block);
                if (len == -1) {
                    break;
                }
                int lastb = 0;
                for (int i2 = 0; i2 < len; i2++) {
                    int b2 = buf[i2] & 255;
                    if (checkEOL && ((lastb == 13 && b2 != 10) || (lastb != 13 && b2 == 10))) {
                        badEOL = true;
                    }
                    if (b2 == 13 || b2 == 10) {
                        linelen = 0;
                    } else {
                        linelen++;
                        if (linelen > 998) {
                            longLine = true;
                        }
                    }
                    if (nonascii(b2)) {
                        if (breakOnNonAscii) {
                            return 3;
                        }
                        non_ascii++;
                    } else {
                        ascii++;
                    }
                    lastb = b2;
                }
                if (max != -1) {
                    max -= len;
                }
            } catch (IOException e2) {
            }
        }
        if (max == 0 && breakOnNonAscii) {
            return 3;
        }
        if (non_ascii == 0) {
            if (badEOL) {
                return 3;
            }
            if (longLine) {
                return 2;
            }
            return 1;
        }
        if (ascii > non_ascii) {
            return 2;
        }
        return 3;
    }

    static final boolean nonascii(int b2) {
        return b2 >= 127 || !(b2 >= 32 || b2 == 13 || b2 == 10 || b2 == 9);
    }
}
