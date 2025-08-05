package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/Encodings.class */
public final class Encodings {
    private static final int m_defaultLastPrintable = 127;
    private static final String ENCODINGS_FILE = "com/sun/org/apache/xml/internal/serializer/Encodings.properties";
    private static final String ENCODINGS_PROP = "com.sun.org.apache.xalan.internal.serialize.encodings";
    static final String DEFAULT_MIME_ENCODING = "UTF-8";
    private static final EncodingInfos _encodingInfos = new EncodingInfos();

    static Writer getWriter(OutputStream output, String encoding) throws UnsupportedEncodingException {
        EncodingInfo ei = _encodingInfos.findEncoding(toUpperCaseFast(encoding));
        if (ei != null) {
            try {
                return new BufferedWriter(new OutputStreamWriter(output, ei.javaName));
            } catch (UnsupportedEncodingException e2) {
            }
        }
        return new BufferedWriter(new OutputStreamWriter(output, encoding));
    }

    public static int getLastPrintable() {
        return 127;
    }

    static EncodingInfo getEncodingInfo(String encoding) {
        String normalizedEncoding = toUpperCaseFast(encoding);
        EncodingInfo ei = _encodingInfos.findEncoding(normalizedEncoding);
        if (ei == null) {
            try {
                Charset c2 = Charset.forName(encoding);
                String name = c2.name();
                ei = new EncodingInfo(name, name);
                _encodingInfos.putEncoding(normalizedEncoding, ei);
            } catch (IllegalCharsetNameException | UnsupportedCharsetException e2) {
                ei = new EncodingInfo(null, null);
            }
        }
        return ei;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String toUpperCaseFast(String s2) {
        String upper;
        boolean different = false;
        int mx = s2.length();
        char[] chars = new char[mx];
        for (int i2 = 0; i2 < mx; i2++) {
            char ch = s2.charAt(i2);
            if ('a' <= ch && ch <= 'z') {
                ch = (char) (ch - ' ');
                different = true;
            }
            chars[i2] = ch;
        }
        if (different) {
            upper = String.valueOf(chars);
        } else {
            upper = s2;
        }
        return upper;
    }

    static String getMimeEncoding(String encoding) {
        String encoding2;
        if (null == encoding) {
            try {
                String encoding3 = SecuritySupport.getSystemProperty("file.encoding", InternalZipConstants.CHARSET_UTF8);
                if (null != encoding3) {
                    String jencoding = (encoding3.equalsIgnoreCase("Cp1252") || encoding3.equalsIgnoreCase("ISO8859_1") || encoding3.equalsIgnoreCase("8859_1") || encoding3.equalsIgnoreCase(InternalZipConstants.CHARSET_UTF8)) ? "UTF-8" : convertJava2MimeEncoding(encoding3);
                    encoding2 = null != jencoding ? jencoding : "UTF-8";
                } else {
                    encoding2 = "UTF-8";
                }
            } catch (SecurityException e2) {
                encoding2 = "UTF-8";
            }
        } else {
            encoding2 = convertJava2MimeEncoding(encoding);
        }
        return encoding2;
    }

    private static String convertJava2MimeEncoding(String encoding) {
        EncodingInfo enc = _encodingInfos.getEncodingFromJavaKey(toUpperCaseFast(encoding));
        if (null != enc) {
            return enc.name;
        }
        return encoding;
    }

    public static String convertMime2JavaEncoding(String encoding) {
        EncodingInfo info = _encodingInfos.findEncoding(toUpperCaseFast(encoding));
        return info != null ? info.javaName : encoding;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/Encodings$EncodingInfos.class */
    private static final class EncodingInfos {
        private final Map<String, EncodingInfo> _encodingTableKeyJava;
        private final Map<String, EncodingInfo> _encodingTableKeyMime;
        private final Map<String, EncodingInfo> _encodingDynamicTable;

        private EncodingInfos() {
            this._encodingTableKeyJava = new HashMap();
            this._encodingTableKeyMime = new HashMap();
            this._encodingDynamicTable = Collections.synchronizedMap(new HashMap());
            loadEncodingInfo();
        }

        private InputStream openEncodingsFileStream() throws IOException {
            String urlString = null;
            InputStream is = null;
            try {
                urlString = SecuritySupport.getSystemProperty(Encodings.ENCODINGS_PROP, "");
            } catch (SecurityException e2) {
            }
            if (urlString != null && urlString.length() > 0) {
                URL url = new URL(urlString);
                is = url.openStream();
            }
            if (is == null) {
                is = SecuritySupport.getResourceAsStream(Encodings.ENCODINGS_FILE);
            }
            return is;
        }

        private Properties loadProperties() throws IOException {
            Properties props = new Properties();
            InputStream is = openEncodingsFileStream();
            Throwable th = null;
            try {
                if (is != null) {
                    props.load(is);
                }
                return props;
            } finally {
                if (is != null) {
                    if (0 != 0) {
                        try {
                            is.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        is.close();
                    }
                }
            }
        }

        private String[] parseMimeTypes(String val) {
            int pos = val.indexOf(32);
            if (pos < 0) {
                return new String[]{val};
            }
            StringTokenizer st = new StringTokenizer(val.substring(0, pos), ",");
            String[] values = new String[st.countTokens()];
            int i2 = 0;
            while (st.hasMoreTokens()) {
                values[i2] = st.nextToken();
                i2++;
            }
            return values;
        }

        private String findCharsetNameFor(String name) {
            try {
                return Charset.forName(name).name();
            } catch (Exception e2) {
                return null;
            }
        }

        private String findCharsetNameFor(String javaName, String[] mimes) {
            String cs = findCharsetNameFor(javaName);
            if (cs != null) {
                return javaName;
            }
            for (String m2 : mimes) {
                cs = findCharsetNameFor(m2);
                if (cs != null) {
                    break;
                }
            }
            return cs;
        }

        private void loadEncodingInfo() {
            try {
                Properties props = loadProperties();
                Enumeration keys = props.keys();
                Map<String, EncodingInfo> canonicals = new HashMap<>();
                while (keys.hasMoreElements()) {
                    String javaName = (String) keys.nextElement();
                    String[] mimes = parseMimeTypes(props.getProperty(javaName));
                    String charsetName = findCharsetNameFor(javaName, mimes);
                    if (charsetName != null) {
                        String kj = Encodings.toUpperCaseFast(javaName);
                        String kc = Encodings.toUpperCaseFast(charsetName);
                        for (String mimeName : mimes) {
                            String km = Encodings.toUpperCaseFast(mimeName);
                            EncodingInfo info = new EncodingInfo(mimeName, charsetName);
                            this._encodingTableKeyMime.put(km, info);
                            if (!canonicals.containsKey(kc)) {
                                canonicals.put(kc, info);
                                this._encodingTableKeyJava.put(kc, info);
                            }
                            this._encodingTableKeyJava.put(kj, info);
                        }
                    }
                }
                for (Map.Entry<String, EncodingInfo> e2 : this._encodingTableKeyJava.entrySet()) {
                    e2.setValue(canonicals.get(Encodings.toUpperCaseFast(e2.getValue().javaName)));
                }
            } catch (MalformedURLException mue) {
                throw new WrappedRuntimeException(mue);
            } catch (IOException ioe) {
                throw new WrappedRuntimeException(ioe);
            }
        }

        EncodingInfo findEncoding(String normalizedEncoding) {
            EncodingInfo info = this._encodingTableKeyJava.get(normalizedEncoding);
            if (info == null) {
                info = this._encodingTableKeyMime.get(normalizedEncoding);
            }
            if (info == null) {
                info = this._encodingDynamicTable.get(normalizedEncoding);
            }
            return info;
        }

        EncodingInfo getEncodingFromMimeKey(String normalizedMimeName) {
            return this._encodingTableKeyMime.get(normalizedMimeName);
        }

        EncodingInfo getEncodingFromJavaKey(String normalizedJavaName) {
            return this._encodingTableKeyJava.get(normalizedJavaName);
        }

        void putEncoding(String key, EncodingInfo info) {
            this._encodingDynamicTable.put(key, info);
        }
    }

    static boolean isHighUTF16Surrogate(char ch) {
        return 55296 <= ch && ch <= 56319;
    }

    static boolean isLowUTF16Surrogate(char ch) {
        return 56320 <= ch && ch <= 57343;
    }

    static int toCodePoint(char highSurrogate, char lowSurrogate) {
        int codePoint = ((highSurrogate - 55296) << 10) + (lowSurrogate - 56320) + 65536;
        return codePoint;
    }

    static int toCodePoint(char ch) {
        return ch;
    }
}
