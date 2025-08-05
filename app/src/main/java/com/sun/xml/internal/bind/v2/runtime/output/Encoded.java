package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/Encoded.class */
public final class Encoded {
    public byte[] buf;
    public int len;
    private static final byte[][] entities = new byte[128];
    private static final byte[][] attributeEntities = new byte[128];

    public Encoded() {
    }

    public Encoded(String text) {
        set(text);
    }

    public void ensureSize(int size) {
        if (this.buf == null || this.buf.length < size) {
            this.buf = new byte[size];
        }
    }

    public final void set(String text) {
        int ptr;
        int length = text.length();
        ensureSize((length * 3) + 1);
        int ptr2 = 0;
        int i2 = 0;
        while (i2 < length) {
            char chr = text.charAt(i2);
            if (chr > 127) {
                if (chr > 2047) {
                    if (55296 <= chr && chr <= 57343) {
                        i2++;
                        int uc = (((chr & 1023) << 10) | (text.charAt(i2) & 1023)) + 65536;
                        int i3 = ptr2;
                        int ptr3 = ptr2 + 1;
                        this.buf[i3] = (byte) (240 | (uc >> 18));
                        int ptr4 = ptr3 + 1;
                        this.buf[ptr3] = (byte) (128 | ((uc >> 12) & 63));
                        int ptr5 = ptr4 + 1;
                        this.buf[ptr4] = (byte) (128 | ((uc >> 6) & 63));
                        ptr2 = ptr5 + 1;
                        this.buf[ptr5] = (byte) (128 + (uc & 63));
                    } else {
                        int i4 = ptr2;
                        int ptr6 = ptr2 + 1;
                        this.buf[i4] = (byte) (224 + (chr >> '\f'));
                        ptr = ptr6 + 1;
                        this.buf[ptr6] = (byte) (128 + ((chr >> 6) & 63));
                    }
                } else {
                    int i5 = ptr2;
                    ptr = ptr2 + 1;
                    this.buf[i5] = (byte) (192 + (chr >> 6));
                }
                int i6 = ptr;
                ptr2 = ptr + 1;
                this.buf[i6] = (byte) (128 + (chr & '?'));
            } else {
                int i7 = ptr2;
                ptr2++;
                this.buf[i7] = (byte) chr;
            }
            i2++;
        }
        this.len = ptr2;
    }

    public final void setEscape(String text, boolean isAttribute) {
        int ptr1;
        int ptr12;
        int length = text.length();
        ensureSize((length * 6) + 1);
        int ptr = 0;
        int i2 = 0;
        while (i2 < length) {
            char chr = text.charAt(i2);
            int ptr13 = ptr;
            if (chr > 127) {
                if (chr > 2047) {
                    if (55296 <= chr && chr <= 57343) {
                        i2++;
                        int uc = (((chr & 1023) << 10) | (text.charAt(i2) & 1023)) + 65536;
                        int i3 = ptr;
                        int ptr2 = ptr + 1;
                        this.buf[i3] = (byte) (240 | (uc >> 18));
                        int ptr3 = ptr2 + 1;
                        this.buf[ptr2] = (byte) (128 | ((uc >> 12) & 63));
                        int ptr4 = ptr3 + 1;
                        this.buf[ptr3] = (byte) (128 | ((uc >> 6) & 63));
                        ptr = ptr4 + 1;
                        this.buf[ptr4] = (byte) (128 + (uc & 63));
                        i2++;
                    } else {
                        int ptr14 = ptr13 + 1;
                        this.buf[ptr13] = (byte) (224 + (chr >> '\f'));
                        ptr12 = ptr14 + 1;
                        this.buf[ptr14] = (byte) (128 + ((chr >> 6) & 63));
                    }
                } else {
                    ptr12 = ptr13 + 1;
                    this.buf[ptr13] = (byte) (192 + (chr >> 6));
                }
                int i4 = ptr12;
                ptr1 = ptr12 + 1;
                this.buf[i4] = (byte) (128 + (chr & '?'));
            } else {
                byte[] ent = attributeEntities[chr];
                if (ent != null) {
                    if (isAttribute || entities[chr] != null) {
                        ptr1 = writeEntity(ent, ptr13);
                    } else {
                        ptr1 = ptr13 + 1;
                        this.buf[ptr13] = (byte) chr;
                    }
                } else {
                    ptr1 = ptr13 + 1;
                    this.buf[ptr13] = (byte) chr;
                }
            }
            ptr = ptr1;
            i2++;
        }
        this.len = ptr;
    }

    private int writeEntity(byte[] entity, int ptr) {
        System.arraycopy(entity, 0, this.buf, ptr, entity.length);
        return ptr + entity.length;
    }

    public final void write(UTF8XmlOutput out) throws IOException {
        out.write(this.buf, 0, this.len);
    }

    public void append(char b2) {
        byte[] bArr = this.buf;
        int i2 = this.len;
        this.len = i2 + 1;
        bArr[i2] = (byte) b2;
    }

    public void compact() {
        byte[] b2 = new byte[this.len];
        System.arraycopy(this.buf, 0, b2, 0, this.len);
        this.buf = b2;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    static {
        add('&', SerializerConstants.ENTITY_AMP, false);
        add('<', SerializerConstants.ENTITY_LT, false);
        add('>', SerializerConstants.ENTITY_GT, false);
        add('\"', SerializerConstants.ENTITY_QUOT, true);
        add('\t', "&#x9;", true);
        add('\r', "&#xD;", false);
        add('\n', SerializerConstants.ENTITY_CRLF, true);
    }

    private static void add(char c2, String s2, boolean attOnly) {
        byte[] image = UTF8XmlOutput.toBytes(s2);
        attributeEntities[c2] = image;
        if (!attOnly) {
            entities[c2] = image;
        }
    }
}
