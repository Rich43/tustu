package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.ByteArrayOutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/SignerOutputStream.class */
public class SignerOutputStream extends ByteArrayOutputStream {
    private static final Logger LOG = LoggerFactory.getLogger(SignerOutputStream.class);
    final SignatureAlgorithm sa;

    public SignerOutputStream(SignatureAlgorithm signatureAlgorithm) {
        this.sa = signatureAlgorithm;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) {
        try {
            this.sa.update(bArr);
        } catch (XMLSignatureException e2) {
            throw new RuntimeException("" + ((Object) e2));
        }
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(int i2) {
        try {
            this.sa.update((byte) i2);
        } catch (XMLSignatureException e2) {
            throw new RuntimeException("" + ((Object) e2));
        }
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Canonicalized SignedInfo:");
            StringBuilder sb = new StringBuilder(i3);
            for (int i4 = i2; i4 < i2 + i3; i4++) {
                sb.append((char) bArr[i4]);
            }
            LOG.debug(sb.toString());
        }
        try {
            this.sa.update(bArr, i2, i3);
        } catch (XMLSignatureException e2) {
            throw new RuntimeException("" + ((Object) e2));
        }
    }
}
