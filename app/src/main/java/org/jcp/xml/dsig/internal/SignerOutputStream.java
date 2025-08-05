package org.jcp.xml.dsig.internal;

import java.io.ByteArrayOutputStream;
import java.security.Signature;
import java.security.SignatureException;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/SignerOutputStream.class */
public class SignerOutputStream extends ByteArrayOutputStream {
    private final Signature sig;

    public SignerOutputStream(Signature signature) {
        this.sig = signature;
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(int i2) {
        super.write(i2);
        try {
            this.sig.update((byte) i2);
        } catch (SignatureException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) {
        super.write(bArr, i2, i3);
        try {
            this.sig.update(bArr, i2, i3);
        } catch (SignatureException e2) {
            throw new RuntimeException(e2);
        }
    }
}
