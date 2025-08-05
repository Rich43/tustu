package com.sun.security.sasl.ntlm;

import com.sun.security.ntlm.Client;
import com.sun.security.ntlm.NTLMException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Random;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/* loaded from: rt.jar:com/sun/security/sasl/ntlm/NTLMClient.class */
final class NTLMClient implements SaslClient {
    private static final String NTLM_VERSION = "com.sun.security.sasl.ntlm.version";
    private static final String NTLM_RANDOM = "com.sun.security.sasl.ntlm.random";
    private static final String NTLM_DOMAIN = "com.sun.security.sasl.ntlm.domain";
    private static final String NTLM_HOSTNAME = "com.sun.security.sasl.ntlm.hostname";
    private final Client client;
    private final String mech;
    private final Random random;
    private int step = 0;

    NTLMClient(String str, String str2, String str3, String str4, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        this.mech = str;
        String property = null;
        Random random = null;
        String canonicalHostName = null;
        if (map != null) {
            String str5 = (String) map.get(Sasl.QOP);
            if (str5 != null && !str5.equals("auth")) {
                throw new SaslException("NTLM only support auth");
            }
            property = (String) map.get(NTLM_VERSION);
            random = (Random) map.get(NTLM_RANDOM);
            canonicalHostName = (String) map.get(NTLM_HOSTNAME);
        }
        this.random = random != null ? random : new Random();
        property = property == null ? System.getProperty("ntlm.version") : property;
        RealmCallback realmCallback = (str4 == null || str4.isEmpty()) ? new RealmCallback("Realm: ") : new RealmCallback("Realm: ", str4);
        NameCallback nameCallback = (str2 == null || str2.isEmpty()) ? new NameCallback("User name: ") : new NameCallback("User name: ", str2);
        PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
        try {
            callbackHandler.handle(new Callback[]{realmCallback, nameCallback, passwordCallback});
            if (canonicalHostName == null) {
                try {
                    canonicalHostName = InetAddress.getLocalHost().getCanonicalHostName();
                } catch (UnknownHostException e2) {
                    canonicalHostName = "localhost";
                }
            }
            try {
                String name = nameCallback.getName();
                name = name == null ? str2 : name;
                String text = realmCallback.getText();
                this.client = new Client(property, canonicalHostName, name, text == null ? str4 : text, passwordCallback.getPassword());
            } catch (NTLMException e3) {
                throw new SaslException("NTLM: client creation failure", e3);
            }
        } catch (IOException e4) {
            throw new SaslException("NTLM: Error acquiring realm, username or password", e4);
        } catch (UnsupportedCallbackException e5) {
            throw new SaslException("NTLM: Cannot perform callback to acquire realm, username or password", e5);
        }
    }

    @Override // javax.security.sasl.SaslClient
    public String getMechanismName() {
        return this.mech;
    }

    @Override // javax.security.sasl.SaslClient
    public boolean isComplete() {
        return this.step >= 2;
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException {
        throw new IllegalStateException("Not supported.");
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException {
        throw new IllegalStateException("Not supported.");
    }

    @Override // javax.security.sasl.SaslClient
    public Object getNegotiatedProperty(String str) {
        if (!isComplete()) {
            throw new IllegalStateException("authentication not complete");
        }
        switch (str) {
            case "javax.security.sasl.qop":
                return "auth";
            case "com.sun.security.sasl.ntlm.domain":
                return this.client.getDomain();
            default:
                return null;
        }
    }

    @Override // javax.security.sasl.SaslClient
    public void dispose() throws SaslException {
        this.client.dispose();
    }

    @Override // javax.security.sasl.SaslClient
    public boolean hasInitialResponse() {
        return true;
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        this.step++;
        if (this.step == 1) {
            return this.client.type1();
        }
        try {
            byte[] bArr2 = new byte[8];
            this.random.nextBytes(bArr2);
            return this.client.type3(bArr, bArr2);
        } catch (NTLMException e2) {
            throw new SaslException("Type3 creation failed", e2);
        }
    }
}
