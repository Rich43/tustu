package java.security;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:java/security/DigestInputStream.class */
public class DigestInputStream extends FilterInputStream {
    private boolean on;
    protected MessageDigest digest;

    public DigestInputStream(InputStream inputStream, MessageDigest messageDigest) {
        super(inputStream);
        this.on = true;
        setMessageDigest(messageDigest);
    }

    public MessageDigest getMessageDigest() {
        return this.digest;
    }

    public void setMessageDigest(MessageDigest messageDigest) {
        this.digest = messageDigest;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int i2 = this.in.read();
        if (this.on && i2 != -1) {
            this.digest.update((byte) i2);
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = this.in.read(bArr, i2, i3);
        if (this.on && i4 != -1) {
            this.digest.update(bArr, i2, i4);
        }
        return i4;
    }

    public void on(boolean z2) {
        this.on = z2;
    }

    public String toString() {
        return "[Digest Input Stream] " + this.digest.toString();
    }
}
