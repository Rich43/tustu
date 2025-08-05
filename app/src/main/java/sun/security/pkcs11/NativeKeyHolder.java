package sun.security.pkcs11;

import java.security.ProviderException;
import sun.security.jca.JCAUtil;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* compiled from: P11Key.java */
/* loaded from: sunpkcs11.jar:sun/security/pkcs11/NativeKeyHolder.class */
final class NativeKeyHolder {
    private static long nativeKeyWrapperKeyID;
    private static CK_MECHANISM nativeKeyWrapperMechanism;
    private static long nativeKeyWrapperRefCount;
    private static Session nativeKeyWrapperSession;
    private final P11Key p11Key;
    private final byte[] nativeKeyInfo;
    private boolean wrapperKeyUsed;
    private long keyID;
    private SessionKeyRef ref;
    private int refCount = -1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NativeKeyHolder.class.desiredAssertionStatus();
        nativeKeyWrapperKeyID = 0L;
        nativeKeyWrapperMechanism = null;
        nativeKeyWrapperRefCount = 0L;
        nativeKeyWrapperSession = null;
    }

    private static void createNativeKeyWrapper(Token token) throws PKCS11Exception {
        if (!$assertionsDisabled && nativeKeyWrapperKeyID != 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && nativeKeyWrapperRefCount != 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && nativeKeyWrapperSession != null) {
            throw new AssertionError();
        }
        CK_ATTRIBUTE[] attributes = token.getAttributes("generate", 4L, 31L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(353L, 32L)});
        Session objSession = null;
        try {
            objSession = token.getObjSession();
            nativeKeyWrapperKeyID = token.p11.C_GenerateKey(objSession.id(), new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_GEN), attributes);
            nativeKeyWrapperSession = objSession;
            nativeKeyWrapperSession.addObject();
            byte[] bArr = new byte[16];
            JCAUtil.getSecureRandom().nextBytes(bArr);
            nativeKeyWrapperMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, bArr);
            token.releaseSession(objSession);
        } catch (PKCS11Exception e2) {
            token.releaseSession(objSession);
        } catch (Throwable th) {
            token.releaseSession(objSession);
            throw th;
        }
    }

    private static void deleteNativeKeyWrapper() {
        Token token = nativeKeyWrapperSession.token;
        if (token.isValid()) {
            Session opSession = null;
            try {
                opSession = token.getOpSession();
                token.p11.C_DestroyObject(opSession.id(), nativeKeyWrapperKeyID);
                nativeKeyWrapperSession.removeObject();
                token.releaseSession(opSession);
            } catch (PKCS11Exception e2) {
                token.releaseSession(opSession);
            } catch (Throwable th) {
                token.releaseSession(opSession);
                throw th;
            }
        }
        nativeKeyWrapperKeyID = 0L;
        nativeKeyWrapperMechanism = null;
        nativeKeyWrapperSession = null;
    }

    static void decWrapperKeyRef() {
        synchronized (NativeKeyHolder.class) {
            if (!$assertionsDisabled && nativeKeyWrapperKeyID == 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && nativeKeyWrapperRefCount <= 0) {
                throw new AssertionError();
            }
            nativeKeyWrapperRefCount--;
            if (nativeKeyWrapperRefCount == 0) {
                deleteNativeKeyWrapper();
            }
        }
    }

    NativeKeyHolder(P11Key p11Key, long j2, Session session, boolean z2, boolean z3) {
        this.p11Key = p11Key;
        this.keyID = j2;
        byte[] nativeKeyInfo = null;
        if (z3) {
            this.ref = null;
        } else {
            Token token = p11Key.token;
            if (z2) {
                try {
                    if (p11Key.sensitive) {
                        synchronized (NativeKeyHolder.class) {
                            if (nativeKeyWrapperKeyID == 0) {
                                createNativeKeyWrapper(token);
                            }
                            if (nativeKeyWrapperKeyID != 0) {
                                nativeKeyWrapperRefCount++;
                                this.wrapperKeyUsed = true;
                            }
                        }
                    }
                    Session opSession = null;
                    try {
                        opSession = token.getOpSession();
                        nativeKeyInfo = p11Key.token.p11.getNativeKeyInfo(opSession.id(), j2, nativeKeyWrapperKeyID, nativeKeyWrapperMechanism);
                        token.releaseSession(opSession);
                    } catch (PKCS11Exception e2) {
                        token.releaseSession(opSession);
                    } catch (Throwable th) {
                        token.releaseSession(opSession);
                        throw th;
                    }
                } catch (PKCS11Exception e3) {
                }
            }
            this.ref = new SessionKeyRef(p11Key, j2, this.wrapperKeyUsed, session);
        }
        this.nativeKeyInfo = (nativeKeyInfo == null || nativeKeyInfo.length == 0) ? null : nativeKeyInfo;
    }

    long getKeyID() throws ProviderException {
        if (this.nativeKeyInfo != null) {
            synchronized (this.nativeKeyInfo) {
                if (this.refCount == -1) {
                    this.refCount = 0;
                }
                int i2 = this.refCount;
                this.refCount = i2 + 1;
                if (this.keyID == 0) {
                    if (i2 != 0) {
                        throw new RuntimeException("Error: null keyID with non-zero refCount " + i2);
                    }
                    Token token = this.p11Key.token;
                    try {
                        try {
                            Session objSession = token.getObjSession();
                            this.keyID = token.p11.createNativeKey(objSession.id(), this.nativeKeyInfo, nativeKeyWrapperKeyID, nativeKeyWrapperMechanism);
                            this.ref.registerNativeKey(this.keyID, objSession);
                            token.releaseSession(objSession);
                        } catch (PKCS11Exception e2) {
                            this.refCount--;
                            throw new ProviderException("Error recreating native key", e2);
                        }
                    } catch (Throwable th) {
                        token.releaseSession(null);
                        throw th;
                    }
                } else if (i2 < 0) {
                    throw new RuntimeException("ERROR: negative refCount");
                }
            }
        }
        return this.keyID;
    }

    void releaseKeyID() {
        if (this.nativeKeyInfo != null) {
            synchronized (this.nativeKeyInfo) {
                if (this.refCount == -1) {
                    throw new RuntimeException("Error: miss match getKeyID call");
                }
                int i2 = this.refCount - 1;
                this.refCount = i2;
                if (i2 == 0) {
                    if (this.keyID == 0) {
                        throw new RuntimeException("ERROR: null keyID can't be destroyed");
                    }
                    this.keyID = 0L;
                    this.ref.removeNativeKey();
                } else if (i2 < 0) {
                    throw new RuntimeException("wrong refCount value: " + i2);
                }
            }
        }
    }
}
