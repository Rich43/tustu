package com.sun.security.sasl;

import java.io.UnsupportedEncodingException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/security/sasl/ExternalClient.class */
final class ExternalClient implements SaslClient {
    private byte[] username;
    private boolean completed = false;

    ExternalClient(String str) throws SaslException {
        if (str != null) {
            try {
                this.username = str.getBytes(InternalZipConstants.CHARSET_UTF8);
                return;
            } catch (UnsupportedEncodingException e2) {
                throw new SaslException("Cannot convert " + str + " into UTF-8", e2);
            }
        }
        this.username = new byte[0];
    }

    @Override // javax.security.sasl.SaslClient
    public String getMechanismName() {
        return "EXTERNAL";
    }

    @Override // javax.security.sasl.SaslClient
    public boolean hasInitialResponse() {
        return true;
    }

    @Override // javax.security.sasl.SaslClient
    public void dispose() throws SaslException {
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("EXTERNAL authentication already completed");
        }
        this.completed = true;
        return this.username;
    }

    @Override // javax.security.sasl.SaslClient
    public boolean isComplete() {
        return this.completed;
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (this.completed) {
            throw new SaslException("EXTERNAL has no supported QOP");
        }
        throw new IllegalStateException("EXTERNAL authentication Not completed");
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (this.completed) {
            throw new SaslException("EXTERNAL has no supported QOP");
        }
        throw new IllegalStateException("EXTERNAL authentication not completed");
    }

    @Override // javax.security.sasl.SaslClient
    public Object getNegotiatedProperty(String str) {
        if (this.completed) {
            return null;
        }
        throw new IllegalStateException("EXTERNAL authentication not completed");
    }
}
