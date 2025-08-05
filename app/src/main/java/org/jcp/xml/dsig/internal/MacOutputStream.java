package org.jcp.xml.dsig.internal;

import java.io.ByteArrayOutputStream;
import javax.crypto.Mac;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/MacOutputStream.class */
public class MacOutputStream extends ByteArrayOutputStream {
    private final Mac mac;

    public MacOutputStream(Mac mac) {
        this.mac = mac;
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(int i2) throws IllegalStateException {
        super.write(i2);
        this.mac.update((byte) i2);
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IllegalStateException {
        super.write(bArr, i2, i3);
        this.mac.update(bArr, i2, i3);
    }
}
