package com.sun.security.sasl.ntlm;

import com.sun.security.ntlm.NTLMException;
import com.sun.security.ntlm.Server;
import java.util.Map;
import java.util.Random;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

/* loaded from: rt.jar:com/sun/security/sasl/ntlm/NTLMServer.class */
final class NTLMServer implements SaslServer {
    private static final String NTLM_VERSION = "com.sun.security.sasl.ntlm.version";
    private static final String NTLM_DOMAIN = "com.sun.security.sasl.ntlm.domain";
    private static final String NTLM_HOSTNAME = "com.sun.security.sasl.ntlm.hostname";
    private static final String NTLM_RANDOM = "com.sun.security.sasl.ntlm.random";
    private final Random random;
    private final Server server;
    private byte[] nonce;
    private int step = 0;
    private String authzId;
    private final String mech;
    private String hostname;
    private String target;

    NTLMServer(String str, String str2, String str3, Map<String, ?> map, final CallbackHandler callbackHandler) throws SaslException {
        this.mech = str;
        String property = null;
        String str4 = null;
        Random random = null;
        if (map != null) {
            str4 = (String) map.get(NTLM_DOMAIN);
            property = (String) map.get(NTLM_VERSION);
            random = (Random) map.get(NTLM_RANDOM);
        }
        this.random = random != null ? random : new Random();
        property = property == null ? System.getProperty("ntlm.version") : property;
        str4 = str4 == null ? str3 : str4;
        if (str4 == null) {
            throw new SaslException("Domain must be provided as the serverName argument or in props");
        }
        try {
            this.server = new Server(property, str4) { // from class: com.sun.security.sasl.ntlm.NTLMServer.1
                /* JADX WARN: Removed duplicated region for block: B:6:0x000b A[Catch: IOException -> 0x0064, UnsupportedCallbackException -> 0x0067, TryCatch #2 {IOException -> 0x0064, UnsupportedCallbackException -> 0x0067, blocks: (B:4:0x0004, B:7:0x0017, B:8:0x0021, B:6:0x000b), top: B:16:0x0004 }] */
                @Override // com.sun.security.ntlm.Server
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public char[] getPassword(java.lang.String r7, java.lang.String r8) {
                    /*
                        r6 = this;
                        r0 = r7
                        if (r0 == 0) goto Lb
                        r0 = r7
                        boolean r0 = r0.isEmpty()     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        if (r0 == 0) goto L17
                    Lb:
                        javax.security.sasl.RealmCallback r0 = new javax.security.sasl.RealmCallback     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r1 = r0
                        java.lang.String r2 = "Domain: "
                        r1.<init>(r2)     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        goto L21
                    L17:
                        javax.security.sasl.RealmCallback r0 = new javax.security.sasl.RealmCallback     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r1 = r0
                        java.lang.String r2 = "Domain: "
                        r3 = r7
                        r1.<init>(r2, r3)     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                    L21:
                        r9 = r0
                        javax.security.auth.callback.NameCallback r0 = new javax.security.auth.callback.NameCallback     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r1 = r0
                        java.lang.String r2 = "Name: "
                        r3 = r8
                        r1.<init>(r2, r3)     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r10 = r0
                        javax.security.auth.callback.PasswordCallback r0 = new javax.security.auth.callback.PasswordCallback     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r1 = r0
                        java.lang.String r2 = "Password: "
                        r3 = 0
                        r1.<init>(r2, r3)     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r11 = r0
                        r0 = r6
                        javax.security.auth.callback.CallbackHandler r0 = r8     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r1 = 3
                        javax.security.auth.callback.Callback[] r1 = new javax.security.auth.callback.Callback[r1]     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r2 = r1
                        r3 = 0
                        r4 = r9
                        r2[r3] = r4     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r2 = r1
                        r3 = 1
                        r4 = r10
                        r2[r3] = r4     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r2 = r1
                        r3 = 2
                        r4 = r11
                        r2[r3] = r4     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r0.handle(r1)     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r0 = r11
                        char[] r0 = r0.getPassword()     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r12 = r0
                        r0 = r11
                        r0.clearPassword()     // Catch: java.io.IOException -> L64 javax.security.auth.callback.UnsupportedCallbackException -> L67
                        r0 = r12
                        return r0
                    L64:
                        r9 = move-exception
                        r0 = 0
                        return r0
                    L67:
                        r9 = move-exception
                        r0 = 0
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.sun.security.sasl.ntlm.NTLMServer.AnonymousClass1.getPassword(java.lang.String, java.lang.String):char[]");
                }
            };
            this.nonce = new byte[8];
        } catch (NTLMException e2) {
            throw new SaslException("NTLM: server creation failure", e2);
        }
    }

    @Override // javax.security.sasl.SaslServer
    public String getMechanismName() {
        return this.mech;
    }

    @Override // javax.security.sasl.SaslServer
    public byte[] evaluateResponse(byte[] bArr) throws SaslException {
        try {
            this.step++;
            if (this.step == 1) {
                this.random.nextBytes(this.nonce);
                return this.server.type2(bArr, this.nonce);
            }
            String[] strArrVerify = this.server.verify(bArr, this.nonce);
            this.authzId = strArrVerify[0];
            this.hostname = strArrVerify[1];
            this.target = strArrVerify[2];
            return null;
        } catch (NTLMException e2) {
            throw new SaslException("NTLM: generate response failure", e2);
        }
    }

    @Override // javax.security.sasl.SaslServer
    public boolean isComplete() {
        return this.step >= 2;
    }

    @Override // javax.security.sasl.SaslServer
    public String getAuthorizationID() {
        if (!isComplete()) {
            throw new IllegalStateException("authentication not complete");
        }
        return this.authzId;
    }

    @Override // javax.security.sasl.SaslServer
    public byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException {
        throw new IllegalStateException("Not supported yet.");
    }

    @Override // javax.security.sasl.SaslServer
    public byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException {
        throw new IllegalStateException("Not supported yet.");
    }

    @Override // javax.security.sasl.SaslServer
    public Object getNegotiatedProperty(String str) {
        if (!isComplete()) {
            throw new IllegalStateException("authentication not complete");
        }
        switch (str) {
            case "javax.security.sasl.qop":
                return "auth";
            case "javax.security.sasl.bound.server.name":
                return this.target;
            case "com.sun.security.sasl.ntlm.hostname":
                return this.hostname;
            default:
                return null;
        }
    }

    @Override // javax.security.sasl.SaslServer
    public void dispose() throws SaslException {
    }
}
