package sun.security.pkcs11;

import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigestSpi;
import java.security.ProviderException;
import javax.crypto.SecretKey;
import sun.nio.ch.DirectBuffer;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.util.MessageDigestSpi2;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Digest.class */
final class P11Digest extends MessageDigestSpi implements Cloneable, MessageDigestSpi2 {
    private static final int S_BLANK = 1;
    private static final int S_BUFFERED = 2;
    private static final int S_INIT = 3;
    private static final int BUFFER_SIZE = 96;
    private final Token token;
    private final String algorithm;
    private final CK_MECHANISM mechanism;
    private final int digestLength;
    private Session session;
    private int state;
    private byte[] buffer;
    private int bufOfs;

    P11Digest(Token token, String str, long j2) {
        this.token = token;
        this.algorithm = str;
        this.mechanism = new CK_MECHANISM(j2);
        switch ((int) j2) {
            case 72:
            case 597:
                this.digestLength = 28;
                break;
            case 76:
            case 592:
                this.digestLength = 32;
                break;
            case 512:
            case 528:
                this.digestLength = 16;
                break;
            case 544:
                this.digestLength = 20;
                break;
            case 608:
                this.digestLength = 48;
                break;
            case 624:
                this.digestLength = 64;
                break;
            default:
                throw new ProviderException("Unknown mechanism: " + j2);
        }
        this.buffer = new byte[96];
        this.state = 1;
    }

    @Override // java.security.MessageDigestSpi
    protected int engineGetDigestLength() {
        return this.digestLength;
    }

    private void fetchSession() {
        this.token.ensureValid();
        if (this.state == 1) {
            try {
                this.session = this.token.getOpSession();
                this.state = 2;
            } catch (PKCS11Exception e2) {
                throw new ProviderException("No more session available", e2);
            }
        }
    }

    @Override // java.security.MessageDigestSpi
    protected void engineReset() {
        this.token.ensureValid();
        if (this.session != null) {
            if (this.state == 3 && this.token.explicitCancel && !this.session.hasObjects()) {
                this.session = this.token.killSession(this.session);
            } else {
                this.session = this.token.releaseSession(this.session);
            }
        }
        this.state = 1;
        this.bufOfs = 0;
    }

    @Override // java.security.MessageDigestSpi
    protected byte[] engineDigest() {
        try {
            byte[] bArr = new byte[this.digestLength];
            engineDigest(bArr, 0, this.digestLength);
            return bArr;
        } catch (DigestException e2) {
            throw new ProviderException("internal error", e2);
        }
    }

    @Override // java.security.MessageDigestSpi
    protected int engineDigest(byte[] bArr, int i2, int i3) throws DigestException {
        int iC_DigestFinal;
        if (i3 < this.digestLength) {
            throw new DigestException("Length must be at least " + this.digestLength);
        }
        fetchSession();
        try {
            try {
                if (this.state == 2) {
                    iC_DigestFinal = this.token.p11.C_DigestSingle(this.session.id(), this.mechanism, this.buffer, 0, this.bufOfs, bArr, i2, i3);
                    this.bufOfs = 0;
                } else {
                    if (this.bufOfs != 0) {
                        this.token.p11.C_DigestUpdate(this.session.id(), 0L, this.buffer, 0, this.bufOfs);
                        this.bufOfs = 0;
                    }
                    iC_DigestFinal = this.token.p11.C_DigestFinal(this.session.id(), bArr, i2, i3);
                }
                if (iC_DigestFinal != this.digestLength) {
                    throw new ProviderException("internal digest length error");
                }
                return iC_DigestFinal;
            } catch (PKCS11Exception e2) {
                throw new ProviderException("digest() failed", e2);
            }
        } finally {
            engineReset();
        }
    }

    @Override // java.security.MessageDigestSpi
    protected void engineUpdate(byte b2) {
        engineUpdate(new byte[]{b2}, 0, 1);
    }

    @Override // java.security.MessageDigestSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) {
        if (i3 <= 0) {
            return;
        }
        fetchSession();
        try {
            if (this.state == 2) {
                this.token.p11.C_DigestInit(this.session.id(), this.mechanism);
                this.state = 3;
            }
            if (this.bufOfs != 0 && this.bufOfs + i3 > this.buffer.length) {
                this.token.p11.C_DigestUpdate(this.session.id(), 0L, this.buffer, 0, this.bufOfs);
                this.bufOfs = 0;
            }
            if (this.bufOfs + i3 > this.buffer.length) {
                this.token.p11.C_DigestUpdate(this.session.id(), 0L, bArr, i2, i3);
            } else {
                System.arraycopy(bArr, i2, this.buffer, this.bufOfs, i3);
                this.bufOfs += i3;
            }
        } catch (PKCS11Exception e2) {
            engineReset();
            throw new ProviderException("update() failed", e2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.security.util.MessageDigestSpi2
    public void engineUpdate(SecretKey secretKey) throws InvalidKeyException {
        if (!(secretKey instanceof P11Key)) {
            throw new InvalidKeyException("Not a P11Key: " + ((Object) secretKey));
        }
        P11Key p11Key = (P11Key) secretKey;
        if (p11Key.token != this.token) {
            throw new InvalidKeyException("Not a P11Key of this provider: " + ((Object) secretKey));
        }
        fetchSession();
        long keyID = p11Key.getKeyID();
        try {
            try {
                if (this.state == 2) {
                    this.token.p11.C_DigestInit(this.session.id(), this.mechanism);
                    this.state = 3;
                }
                if (this.bufOfs != 0) {
                    this.token.p11.C_DigestUpdate(this.session.id(), 0L, this.buffer, 0, this.bufOfs);
                    this.bufOfs = 0;
                }
                this.token.p11.C_DigestKey(this.session.id(), keyID);
                p11Key.releaseKeyID();
            } catch (PKCS11Exception e2) {
                engineReset();
                throw new ProviderException("update(SecretKey) failed", e2);
            }
        } catch (Throwable th) {
            p11Key.releaseKeyID();
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.security.MessageDigestSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        int iRemaining = byteBuffer.remaining();
        if (iRemaining <= 0) {
            return;
        }
        if (!(byteBuffer instanceof DirectBuffer)) {
            super.engineUpdate(byteBuffer);
            return;
        }
        fetchSession();
        long jAddress = ((DirectBuffer) byteBuffer).address();
        int iPosition = byteBuffer.position();
        try {
            if (this.state == 2) {
                this.token.p11.C_DigestInit(this.session.id(), this.mechanism);
                this.state = 3;
            }
            if (this.bufOfs != 0) {
                this.token.p11.C_DigestUpdate(this.session.id(), 0L, this.buffer, 0, this.bufOfs);
                this.bufOfs = 0;
            }
            this.token.p11.C_DigestUpdate(this.session.id(), jAddress + iPosition, null, 0, iRemaining);
            byteBuffer.position(iPosition + iRemaining);
        } catch (PKCS11Exception e2) {
            engineReset();
            throw new ProviderException("update() failed", e2);
        }
    }

    @Override // java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        P11Digest p11Digest = (P11Digest) super.clone();
        p11Digest.buffer = (byte[]) this.buffer.clone();
        try {
            if (this.session != null) {
                p11Digest.session = p11Digest.token.getOpSession();
            }
            if (this.state == 3) {
                this.token.p11.C_SetOperationState(p11Digest.session.id(), this.token.p11.C_GetOperationState(this.session.id()), 0L, 0L);
            }
            return p11Digest;
        } catch (PKCS11Exception e2) {
            throw ((CloneNotSupportedException) new CloneNotSupportedException(this.algorithm).initCause(e2));
        }
    }
}
