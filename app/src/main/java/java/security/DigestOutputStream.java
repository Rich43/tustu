package java.security;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:java/security/DigestOutputStream.class */
public class DigestOutputStream extends FilterOutputStream {
    private boolean on;
    protected MessageDigest digest;

    public DigestOutputStream(OutputStream outputStream, MessageDigest messageDigest) {
        super(outputStream);
        this.on = true;
        setMessageDigest(messageDigest);
    }

    public MessageDigest getMessageDigest() {
        return this.digest;
    }

    public void setMessageDigest(MessageDigest messageDigest) {
        this.digest = messageDigest;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        this.out.write(i2);
        if (this.on) {
            this.digest.update((byte) i2);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        this.out.write(bArr, i2, i3);
        if (this.on) {
            this.digest.update(bArr, i2, i3);
        }
    }

    public void on(boolean z2) {
        this.on = z2;
    }

    public String toString() {
        return "[Digest Output Stream] " + this.digest.toString();
    }
}
