package com.sun.corba.se.impl.orbutil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/HexOutputStream.class */
public class HexOutputStream extends OutputStream {
    private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private StringWriter writer;

    public HexOutputStream(StringWriter stringWriter) {
        this.writer = stringWriter;
    }

    @Override // java.io.OutputStream
    public synchronized void write(int i2) throws IOException {
        this.writer.write(hex[(i2 >> 4) & 15]);
        this.writer.write(hex[(i2 >> 0) & 15]);
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
        for (int i4 = 0; i4 < i3; i4++) {
            write(bArr[i2 + i4]);
        }
    }
}
