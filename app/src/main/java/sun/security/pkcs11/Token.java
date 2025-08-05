package sun.security.pkcs11;

import java.io.NotSerializableException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.login.LoginException;
import sun.security.jca.JCAUtil;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM_INFO;
import sun.security.pkcs11.wrapper.CK_SESSION_INFO;
import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/Token.class */
class Token implements Serializable {
    private static final long serialVersionUID = 2541527649100571747L;
    private static final long CHECK_INTERVAL = 50;
    final SunPKCS11 provider;
    final PKCS11 p11;
    final Config config;
    final CK_TOKEN_INFO tokenInfo;
    final SessionManager sessionManager;
    private final TemplateManager templateManager;
    final boolean explicitCancel;
    final KeyCache secretCache;
    final KeyCache privateCache;
    private volatile P11KeyFactory rsaFactory;
    private volatile P11KeyFactory dsaFactory;
    private volatile P11KeyFactory dhFactory;
    private volatile P11KeyFactory ecFactory;
    private final Map<Long, CK_MECHANISM_INFO> mechInfoMap;
    private volatile P11SecureRandom secureRandom;
    private volatile P11KeyStore keyStore;
    private final boolean removable;
    private volatile boolean valid = true;
    private long lastPresentCheck;
    private byte[] tokenId;
    private boolean writeProtected;
    private volatile boolean loggedIn;
    private long lastLoginCheck;
    private Boolean supportsRawSecretKeyImport;
    private static final Object CHECK_LOCK = new Object();
    private static final CK_MECHANISM_INFO INVALID_MECH = new CK_MECHANISM_INFO(0, 0, 0);
    private static final List<Reference<Token>> serializedTokens = new ArrayList();

    Token(SunPKCS11 sunPKCS11) throws PKCS11Exception {
        SessionManager sessionManager;
        this.provider = sunPKCS11;
        this.removable = sunPKCS11.removable;
        this.p11 = sunPKCS11.p11;
        this.config = sunPKCS11.config;
        this.tokenInfo = this.p11.C_GetTokenInfo(sunPKCS11.slotID);
        this.writeProtected = (this.tokenInfo.flags & 2) != 0;
        try {
            sessionManager = new SessionManager(this);
            sessionManager.releaseSession(sessionManager.getOpSession());
        } catch (PKCS11Exception e2) {
            if (this.writeProtected) {
                throw e2;
            }
            this.writeProtected = true;
            sessionManager = new SessionManager(this);
            sessionManager.releaseSession(sessionManager.getOpSession());
        }
        this.sessionManager = sessionManager;
        this.secretCache = new KeyCache();
        this.privateCache = new KeyCache();
        this.templateManager = this.config.getTemplateManager();
        this.explicitCancel = this.config.getExplicitCancel();
        this.mechInfoMap = new ConcurrentHashMap(10);
    }

    boolean isWriteProtected() {
        return this.writeProtected;
    }

    boolean supportsRawSecretKeyImport() {
        if (this.supportsRawSecretKeyImport == null) {
            byte[] bArr = new byte[48];
            JCAUtil.getSecureRandom().nextBytes(bArr);
            Session objSession = null;
            try {
                try {
                    CK_ATTRIBUTE[] attributes = getAttributes("import", 4L, 16L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(256L, 16L), new CK_ATTRIBUTE(17L, bArr)});
                    objSession = getObjSession();
                    this.p11.C_CreateObject(objSession.id(), attributes);
                    this.supportsRawSecretKeyImport = Boolean.TRUE;
                    releaseSession(objSession);
                } catch (PKCS11Exception e2) {
                    this.supportsRawSecretKeyImport = Boolean.FALSE;
                    releaseSession(objSession);
                }
            } catch (Throwable th) {
                releaseSession(objSession);
                throw th;
            }
        }
        return this.supportsRawSecretKeyImport.booleanValue();
    }

    boolean isLoggedIn(Session session) throws PKCS11Exception {
        boolean zIsLoggedInNow = this.loggedIn;
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.lastLoginCheck > 50) {
            zIsLoggedInNow = isLoggedInNow(session);
            this.lastLoginCheck = jCurrentTimeMillis;
        }
        return zIsLoggedInNow;
    }

    boolean isLoggedInNow(Session session) throws PKCS11Exception {
        boolean z2 = session == null;
        if (z2) {
            try {
                session = getOpSession();
            } catch (Throwable th) {
                if (z2) {
                    releaseSession(session);
                }
                throw th;
            }
        }
        CK_SESSION_INFO ck_session_infoC_GetSessionInfo = this.p11.C_GetSessionInfo(session.id());
        boolean z3 = ck_session_infoC_GetSessionInfo.state == 1 || ck_session_infoC_GetSessionInfo.state == 3;
        this.loggedIn = z3;
        if (z2) {
            releaseSession(session);
        }
        return z3;
    }

    void ensureLoggedIn(Session session) throws LoginException, PKCS11Exception {
        if (!isLoggedIn(session)) {
            this.provider.login(null, null);
        }
    }

    boolean isValid() {
        if (!this.removable) {
            return true;
        }
        return this.valid;
    }

    void ensureValid() {
        if (!isValid()) {
            throw new ProviderException("Token has been removed");
        }
    }

    boolean isPresent(long j2) {
        if (!this.removable) {
            return true;
        }
        if (!this.valid) {
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.lastPresentCheck >= 50) {
            synchronized (CHECK_LOCK) {
                if (jCurrentTimeMillis - this.lastPresentCheck >= 50) {
                    boolean z2 = false;
                    try {
                        if ((this.provider.p11.C_GetSlotInfo(this.provider.slotID).flags & 1) != 0) {
                            this.provider.p11.C_GetSessionInfo(j2);
                            z2 = true;
                        }
                    } catch (PKCS11Exception e2) {
                    }
                    this.valid = z2;
                    this.lastPresentCheck = System.currentTimeMillis();
                    if (!z2) {
                        destroy();
                    }
                }
            }
        }
        return this.valid;
    }

    void destroy() {
        this.valid = false;
        this.provider.uninitToken(this);
    }

    Session getObjSession() throws PKCS11Exception {
        return this.sessionManager.getObjSession();
    }

    Session getOpSession() throws PKCS11Exception {
        return this.sessionManager.getOpSession();
    }

    Session releaseSession(Session session) {
        return this.sessionManager.releaseSession(session);
    }

    Session killSession(Session session) {
        return this.sessionManager.killSession(session);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0059, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    sun.security.pkcs11.wrapper.CK_ATTRIBUTE[] getAttributes(java.lang.String r9, long r10, long r12, sun.security.pkcs11.wrapper.CK_ATTRIBUTE[] r14) throws sun.security.pkcs11.wrapper.PKCS11Exception {
        /*
            r8 = this;
            r0 = r8
            sun.security.pkcs11.TemplateManager r0 = r0.templateManager
            r1 = r9
            r2 = r10
            r3 = r12
            r4 = r14
            sun.security.pkcs11.wrapper.CK_ATTRIBUTE[] r0 = r0.getAttributes(r1, r2, r3, r4)
            r15 = r0
            r0 = r15
            r16 = r0
            r0 = r16
            int r0 = r0.length
            r17 = r0
            r0 = 0
            r18 = r0
        L1b:
            r0 = r18
            r1 = r17
            if (r0 >= r1) goto L57
            r0 = r16
            r1 = r18
            r0 = r0[r1]
            r19 = r0
            r0 = r19
            long r0 = r0.type
            r1 = 1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L51
            r0 = r19
            boolean r0 = r0.getBoolean()
            if (r0 == 0) goto L57
            r0 = r8
            r1 = 0
            r0.ensureLoggedIn(r1)     // Catch: javax.security.auth.login.LoginException -> L43
            goto L57
        L43:
            r20 = move-exception
            java.security.ProviderException r0 = new java.security.ProviderException
            r1 = r0
            java.lang.String r2 = "Login failed"
            r3 = r20
            r1.<init>(r2, r3)
            throw r0
        L51:
            int r18 = r18 + 1
            goto L1b
        L57:
            r0 = r15
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs11.Token.getAttributes(java.lang.String, long, long, sun.security.pkcs11.wrapper.CK_ATTRIBUTE[]):sun.security.pkcs11.wrapper.CK_ATTRIBUTE[]");
    }

    P11KeyFactory getKeyFactory(String str) {
        P11KeyFactory p11ECKeyFactory;
        if (str.equals("RSA")) {
            p11ECKeyFactory = this.rsaFactory;
            if (p11ECKeyFactory == null) {
                p11ECKeyFactory = new P11RSAKeyFactory(this, str);
                this.rsaFactory = p11ECKeyFactory;
            }
        } else if (str.equals("DSA")) {
            p11ECKeyFactory = this.dsaFactory;
            if (p11ECKeyFactory == null) {
                p11ECKeyFactory = new P11DSAKeyFactory(this, str);
                this.dsaFactory = p11ECKeyFactory;
            }
        } else if (str.equals("DH")) {
            p11ECKeyFactory = this.dhFactory;
            if (p11ECKeyFactory == null) {
                p11ECKeyFactory = new P11DHKeyFactory(this, str);
                this.dhFactory = p11ECKeyFactory;
            }
        } else if (str.equals("EC")) {
            p11ECKeyFactory = this.ecFactory;
            if (p11ECKeyFactory == null) {
                p11ECKeyFactory = new P11ECKeyFactory(this, str);
                this.ecFactory = p11ECKeyFactory;
            }
        } else {
            throw new ProviderException("Unknown algorithm " + str);
        }
        return p11ECKeyFactory;
    }

    P11SecureRandom getRandom() {
        if (this.secureRandom == null) {
            this.secureRandom = new P11SecureRandom(this);
        }
        return this.secureRandom;
    }

    P11KeyStore getKeyStore() {
        if (this.keyStore == null) {
            this.keyStore = new P11KeyStore(this);
        }
        return this.keyStore;
    }

    CK_MECHANISM_INFO getMechanismInfo(long j2) throws PKCS11Exception {
        CK_MECHANISM_INFO ck_mechanism_infoC_GetMechanismInfo = this.mechInfoMap.get(Long.valueOf(j2));
        if (ck_mechanism_infoC_GetMechanismInfo == null) {
            try {
                ck_mechanism_infoC_GetMechanismInfo = this.p11.C_GetMechanismInfo(this.provider.slotID, j2);
                this.mechInfoMap.put(Long.valueOf(j2), ck_mechanism_infoC_GetMechanismInfo);
            } catch (PKCS11Exception e2) {
                if (e2.getErrorCode() != 112) {
                    throw e2;
                }
                this.mechInfoMap.put(Long.valueOf(j2), INVALID_MECH);
            }
        } else if (ck_mechanism_infoC_GetMechanismInfo == INVALID_MECH) {
            ck_mechanism_infoC_GetMechanismInfo = null;
        }
        return ck_mechanism_infoC_GetMechanismInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized byte[] getTokenId() {
        if (this.tokenId == null) {
            SecureRandom secureRandom = JCAUtil.getSecureRandom();
            this.tokenId = new byte[20];
            secureRandom.nextBytes(this.tokenId);
            serializedTokens.add(new WeakReference(this));
        }
        return this.tokenId;
    }

    private Object writeReplace() throws ObjectStreamException {
        if (!isValid()) {
            throw new NotSerializableException("Token has been removed");
        }
        return new TokenRep(this);
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/Token$TokenRep.class */
    private static class TokenRep implements Serializable {
        private static final long serialVersionUID = 3503721168218219807L;
        private final byte[] tokenId;

        TokenRep(Token token) {
            this.tokenId = token.getTokenId();
        }

        private Object readResolve() throws ObjectStreamException {
            Iterator it = Token.serializedTokens.iterator();
            while (it.hasNext()) {
                Token token = (Token) ((Reference) it.next()).get();
                if (token != null && token.isValid() && Arrays.equals(token.getTokenId(), this.tokenId)) {
                    return token;
                }
            }
            throw new NotSerializableException("Could not find token");
        }
    }
}
