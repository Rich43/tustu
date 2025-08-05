package com.sun.corba.se.impl.encoding;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import org.apache.commons.net.ftp.FTP;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/OSFCodeSetRegistry.class */
public final class OSFCodeSetRegistry {
    public static final int ISO_8859_1_VALUE = 65537;
    public static final Entry ISO_8859_1 = new Entry(FTP.DEFAULT_CONTROL_ENCODING, ISO_8859_1_VALUE, true, 1);
    static final Entry UTF_16BE = new Entry(FastInfosetSerializer.UTF_16BE, -1, true, 2);
    static final Entry UTF_16LE = new Entry("UTF-16LE", -2, true, 2);
    public static final int UTF_16_VALUE = 65801;
    public static final Entry UTF_16 = new Entry("UTF-16", UTF_16_VALUE, true, 4);
    public static final int UTF_8_VALUE = 83951617;
    public static final Entry UTF_8 = new Entry("UTF-8", UTF_8_VALUE, false, 6);
    public static final int UCS_2_VALUE = 65792;
    public static final Entry UCS_2 = new Entry("UCS-2", UCS_2_VALUE, true, 2);
    public static final int ISO_646_VALUE = 65568;
    public static final Entry ISO_646 = new Entry("US-ASCII", ISO_646_VALUE, true, 1);

    private OSFCodeSetRegistry() {
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/OSFCodeSetRegistry$Entry.class */
    public static final class Entry {
        private String javaName;
        private int encodingNum;
        private boolean isFixedWidth;
        private int maxBytesPerChar;

        private Entry(String str, int i2, boolean z2, int i3) {
            this.javaName = str;
            this.encodingNum = i2;
            this.isFixedWidth = z2;
            this.maxBytesPerChar = i3;
        }

        public String getName() {
            return this.javaName;
        }

        public int getNumber() {
            return this.encodingNum;
        }

        public boolean isFixedWidth() {
            return this.isFixedWidth;
        }

        public int getMaxBytesPerChar() {
            return this.maxBytesPerChar;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.javaName.equals(entry.javaName) && this.encodingNum == entry.encodingNum && this.isFixedWidth == entry.isFixedWidth && this.maxBytesPerChar == entry.maxBytesPerChar;
        }

        public int hashCode() {
            return this.encodingNum;
        }
    }

    public static Entry lookupEntry(int i2) {
        switch (i2) {
            case ISO_8859_1_VALUE /* 65537 */:
                return ISO_8859_1;
            case ISO_646_VALUE /* 65568 */:
                return ISO_646;
            case UCS_2_VALUE /* 65792 */:
                return UCS_2;
            case UTF_16_VALUE /* 65801 */:
                return UTF_16;
            case UTF_8_VALUE /* 83951617 */:
                return UTF_8;
            default:
                return null;
        }
    }
}
