package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.ByteArrayOutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/DigesterOutputStream.class */
public class DigesterOutputStream extends ByteArrayOutputStream {
    private static final Logger LOG = LoggerFactory.getLogger(DigesterOutputStream.class);
    final MessageDigestAlgorithm mda;

    public DigesterOutputStream(MessageDigestAlgorithm messageDigestAlgorithm) {
        this.mda = messageDigestAlgorithm;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(int i2) {
        this.mda.update((byte) i2);
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Pre-digested input:");
            StringBuilder sb = new StringBuilder(i3);
            for (int i4 = i2; i4 < i2 + i3; i4++) {
                sb.append((char) bArr[i4]);
            }
            LOG.debug(sb.toString());
        }
        this.mda.update(bArr, i2, i3);
    }

    public byte[] getDigestValue() {
        return this.mda.digest();
    }
}
