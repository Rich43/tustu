package sun.security.pkcs11;

import com.sun.glass.events.WindowEvent;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.MacSpi;
import sun.nio.ch.DirectBuffer;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Mac.class */
final class P11Mac extends MacSpi {
    private final Token token;
    private final String algorithm;
    private final CK_MECHANISM ckMechanism;
    private final int macLength;
    private P11Key p11Key;
    private Session session;
    private boolean initialized;
    private byte[] oneByte;

    P11Mac(Token token, String str, long j2) throws PKCS11Exception {
        this.token = token;
        this.algorithm = str;
        Long l2 = null;
        switch ((int) j2) {
            case 73:
            case 598:
                this.macLength = 28;
                break;
            case 77:
            case 593:
                this.macLength = 32;
                break;
            case 529:
                this.macLength = 16;
                break;
            case WindowEvent.FOCUS_DISABLED /* 545 */:
                this.macLength = 20;
                break;
            case 609:
                this.macLength = 48;
                break;
            case 625:
                this.macLength = 64;
                break;
            case 896:
                this.macLength = 16;
                l2 = 16L;
                break;
            case 897:
                this.macLength = 20;
                l2 = 20L;
                break;
            default:
                throw new ProviderException("Unknown mechanism: " + j2);
        }
        this.ckMechanism = new CK_MECHANISM(j2, l2);
    }

    private void reset(boolean z2) {
        if (!this.initialized) {
            return;
        }
        this.initialized = false;
        try {
            if (this.session == null) {
                return;
            }
            if (z2 && this.token.explicitCancel) {
                cancelOperation();
            }
        } finally {
            this.p11Key.releaseKeyID();
            this.session = this.token.releaseSession(this.session);
        }
    }

    private void cancelOperation() {
        this.token.ensureValid();
        try {
            this.token.p11.C_SignFinal(this.session.id(), 0);
        } catch (PKCS11Exception e2) {
            if (e2.getErrorCode() == 145) {
            } else {
                throw new ProviderException("Cancel failed", e2);
            }
        }
    }

    private void ensureInitialized() throws PKCS11Exception {
        if (!this.initialized) {
            initialize();
        }
    }

    private void initialize() throws PKCS11Exception {
        if (this.p11Key == null) {
            throw new ProviderException("Operation cannot be performed without calling engineInit first");
        }
        this.token.ensureValid();
        long keyID = this.p11Key.getKeyID();
        try {
            if (this.session == null) {
                this.session = this.token.getOpSession();
            }
            this.token.p11.C_SignInit(this.session.id(), this.ckMechanism, keyID);
            this.initialized = true;
        } catch (PKCS11Exception e2) {
            this.p11Key.releaseKeyID();
            this.session = this.token.releaseSession(this.session);
            throw e2;
        }
    }

    @Override // javax.crypto.MacSpi
    protected int engineGetMacLength() {
        return this.macLength;
    }

    @Override // javax.crypto.MacSpi
    protected void engineReset() {
        reset(true);
    }

    @Override // javax.crypto.MacSpi
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("Parameters not supported");
        }
        reset(true);
        this.p11Key = P11SecretKeyFactory.convertKey(this.token, key, this.algorithm);
        try {
            initialize();
        } catch (PKCS11Exception e2) {
            throw new InvalidKeyException("init() failed", e2);
        }
    }

    @Override // javax.crypto.MacSpi
    protected byte[] engineDoFinal() {
        try {
            try {
                ensureInitialized();
                byte[] bArrC_SignFinal = this.token.p11.C_SignFinal(this.session.id(), 0);
                reset(false);
                return bArrC_SignFinal;
            } catch (PKCS11Exception e2) {
                throw new ProviderException("doFinal() failed", e2);
            }
        } catch (Throwable th) {
            reset(false);
            throw th;
        }
    }

    @Override // javax.crypto.MacSpi
    protected void engineUpdate(byte b2) {
        if (this.oneByte == null) {
            this.oneByte = new byte[1];
        }
        this.oneByte[0] = b2;
        engineUpdate(this.oneByte, 0, 1);
    }

    @Override // javax.crypto.MacSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) {
        try {
            ensureInitialized();
            this.token.p11.C_SignUpdate(this.session.id(), 0L, bArr, i2, i3);
        } catch (PKCS11Exception e2) {
            throw new ProviderException("update() failed", e2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.crypto.MacSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        try {
            ensureInitialized();
            int iRemaining = byteBuffer.remaining();
            if (iRemaining <= 0) {
                return;
            }
            if (!(byteBuffer instanceof DirectBuffer)) {
                super.engineUpdate(byteBuffer);
                return;
            }
            long jAddress = ((DirectBuffer) byteBuffer).address();
            int iPosition = byteBuffer.position();
            this.token.p11.C_SignUpdate(this.session.id(), jAddress + iPosition, null, 0, iRemaining);
            byteBuffer.position(iPosition + iRemaining);
        } catch (PKCS11Exception e2) {
            throw new ProviderException("update() failed", e2);
        }
    }
}
