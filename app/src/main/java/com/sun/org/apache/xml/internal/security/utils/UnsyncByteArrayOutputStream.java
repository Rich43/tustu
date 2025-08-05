package com.sun.org.apache.xml.internal.security.utils;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/UnsyncByteArrayOutputStream.class */
public class UnsyncByteArrayOutputStream extends OutputStream {
    private static final int VM_ARRAY_INDEX_MAX_VALUE = 2147483639;
    private static final int INITIAL_SIZE = 8192;
    private int pos;
    private int size = 8192;
    private byte[] buf = new byte[8192];

    @Override // java.io.OutputStream
    public void write(byte[] bArr) {
        if (VM_ARRAY_INDEX_MAX_VALUE - this.pos < bArr.length) {
            throw new OutOfMemoryError();
        }
        int length = this.pos + bArr.length;
        if (length > this.size) {
            expandSize(length);
        }
        System.arraycopy(bArr, 0, this.buf, this.pos, bArr.length);
        this.pos = length;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) {
        if (VM_ARRAY_INDEX_MAX_VALUE - this.pos < i3) {
            throw new OutOfMemoryError();
        }
        int i4 = this.pos + i3;
        if (i4 > this.size) {
            expandSize(i4);
        }
        System.arraycopy(bArr, i2, this.buf, this.pos, i3);
        this.pos = i4;
    }

    @Override // java.io.OutputStream
    public void write(int i2) {
        if (VM_ARRAY_INDEX_MAX_VALUE - this.pos == 0) {
            throw new OutOfMemoryError();
        }
        int i3 = this.pos + 1;
        if (i3 > this.size) {
            expandSize(i3);
        }
        byte[] bArr = this.buf;
        int i4 = this.pos;
        this.pos = i4 + 1;
        bArr[i4] = (byte) i2;
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[this.pos];
        System.arraycopy(this.buf, 0, bArr, 0, this.pos);
        return bArr;
    }

    public void reset() {
        this.pos = 0;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.buf, 0, this.pos);
    }

    private void expandSize(int i2) {
        int i3 = this.size;
        while (i2 > i3) {
            i3 <<= 1;
            if (i3 < 0) {
                i3 = VM_ARRAY_INDEX_MAX_VALUE;
            }
        }
        byte[] bArr = new byte[i3];
        System.arraycopy(this.buf, 0, bArr, 0, this.pos);
        this.buf = bArr;
        this.size = i3;
    }
}
