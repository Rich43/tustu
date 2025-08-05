package com.sun.org.apache.xml.internal.serializer;

import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/EncodingInfo.class */
public final class EncodingInfo {
    final String name;
    final String javaName;
    private InEncoding m_encoding;

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/EncodingInfo$InEncoding.class */
    private interface InEncoding {
        boolean isInEncoding(char c2);

        boolean isInEncoding(char c2, char c3);
    }

    public boolean isInEncoding(char ch) {
        if (this.m_encoding == null) {
            this.m_encoding = new EncodingImpl();
        }
        return this.m_encoding.isInEncoding(ch);
    }

    public boolean isInEncoding(char high, char low) {
        if (this.m_encoding == null) {
            this.m_encoding = new EncodingImpl();
        }
        return this.m_encoding.isInEncoding(high, low);
    }

    public EncodingInfo(String name, String javaName) {
        this.name = name;
        this.javaName = javaName;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/EncodingInfo$EncodingImpl.class */
    private class EncodingImpl implements InEncoding {
        private final String m_encoding;
        private final int m_first;
        private final int m_explFirst;
        private final int m_explLast;
        private final int m_last;
        private InEncoding m_before;
        private InEncoding m_after;
        private static final int RANGE = 128;
        private final boolean[] m_alreadyKnown;
        private final boolean[] m_isInEncoding;

        @Override // com.sun.org.apache.xml.internal.serializer.EncodingInfo.InEncoding
        public boolean isInEncoding(char ch1) {
            boolean ret;
            int codePoint = Encodings.toCodePoint(ch1);
            if (codePoint < this.m_explFirst) {
                if (this.m_before == null) {
                    this.m_before = EncodingInfo.this.new EncodingImpl(this.m_encoding, this.m_first, this.m_explFirst - 1, codePoint);
                }
                ret = this.m_before.isInEncoding(ch1);
            } else if (this.m_explLast < codePoint) {
                if (this.m_after == null) {
                    this.m_after = EncodingInfo.this.new EncodingImpl(this.m_encoding, this.m_explLast + 1, this.m_last, codePoint);
                }
                ret = this.m_after.isInEncoding(ch1);
            } else {
                int idx = codePoint - this.m_explFirst;
                if (!this.m_alreadyKnown[idx]) {
                    ret = EncodingInfo.inEncoding(ch1, this.m_encoding);
                    this.m_alreadyKnown[idx] = true;
                    this.m_isInEncoding[idx] = ret;
                } else {
                    ret = this.m_isInEncoding[idx];
                }
            }
            return ret;
        }

        @Override // com.sun.org.apache.xml.internal.serializer.EncodingInfo.InEncoding
        public boolean isInEncoding(char high, char low) {
            boolean ret;
            int codePoint = Encodings.toCodePoint(high, low);
            if (codePoint < this.m_explFirst) {
                if (this.m_before == null) {
                    this.m_before = EncodingInfo.this.new EncodingImpl(this.m_encoding, this.m_first, this.m_explFirst - 1, codePoint);
                }
                ret = this.m_before.isInEncoding(high, low);
            } else if (this.m_explLast < codePoint) {
                if (this.m_after == null) {
                    this.m_after = EncodingInfo.this.new EncodingImpl(this.m_encoding, this.m_explLast + 1, this.m_last, codePoint);
                }
                ret = this.m_after.isInEncoding(high, low);
            } else {
                int idx = codePoint - this.m_explFirst;
                if (!this.m_alreadyKnown[idx]) {
                    ret = EncodingInfo.inEncoding(high, low, this.m_encoding);
                    this.m_alreadyKnown[idx] = true;
                    this.m_isInEncoding[idx] = ret;
                } else {
                    ret = this.m_isInEncoding[idx];
                }
            }
            return ret;
        }

        private EncodingImpl(EncodingInfo encodingInfo) {
            this(encodingInfo.javaName, 0, Integer.MAX_VALUE, 0);
        }

        private EncodingImpl(String encoding, int first, int last, int codePoint) {
            this.m_alreadyKnown = new boolean[128];
            this.m_isInEncoding = new boolean[128];
            this.m_first = first;
            this.m_last = last;
            this.m_explFirst = (codePoint / 128) * 128;
            this.m_explLast = this.m_explFirst + 127;
            this.m_encoding = encoding;
            if (EncodingInfo.this.javaName != null) {
                if (0 <= this.m_explFirst && this.m_explFirst <= 127 && (InternalZipConstants.CHARSET_UTF8.equals(EncodingInfo.this.javaName) || "UTF-16".equals(EncodingInfo.this.javaName) || "ASCII".equals(EncodingInfo.this.javaName) || "US-ASCII".equals(EncodingInfo.this.javaName) || "Unicode".equals(EncodingInfo.this.javaName) || "UNICODE".equals(EncodingInfo.this.javaName) || EncodingInfo.this.javaName.startsWith("ISO8859"))) {
                    for (int unicode = 1; unicode < 127; unicode++) {
                        int idx = unicode - this.m_explFirst;
                        if (0 <= idx && idx < 128) {
                            this.m_alreadyKnown[idx] = true;
                            this.m_isInEncoding[idx] = true;
                        }
                    }
                }
                if (EncodingInfo.this.javaName == null) {
                    for (int idx2 = 0; idx2 < this.m_alreadyKnown.length; idx2++) {
                        this.m_alreadyKnown[idx2] = true;
                        this.m_isInEncoding[idx2] = true;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean inEncoding(char ch, String encoding) {
        boolean isInEncoding;
        try {
            char[] cArray = {ch};
            String s2 = new String(cArray);
            byte[] bArray = s2.getBytes(encoding);
            isInEncoding = inEncoding(ch, bArray);
        } catch (Exception e2) {
            isInEncoding = false;
            if (encoding == null) {
                isInEncoding = true;
            }
        }
        return isInEncoding;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean inEncoding(char high, char low, String encoding) {
        boolean isInEncoding;
        try {
            char[] cArray = {high, low};
            String s2 = new String(cArray);
            byte[] bArray = s2.getBytes(encoding);
            isInEncoding = inEncoding(high, bArray);
        } catch (Exception e2) {
            isInEncoding = false;
        }
        return isInEncoding;
    }

    private static boolean inEncoding(char ch, byte[] data) {
        boolean isInEncoding;
        if (data == null || data.length == 0 || data[0] == 0) {
            isInEncoding = false;
        } else if (data[0] == 63 && ch != '?') {
            isInEncoding = false;
        } else {
            isInEncoding = true;
        }
        return isInEncoding;
    }
}
