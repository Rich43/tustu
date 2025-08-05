package com.sun.security.sasl;

import java.io.UnsupportedEncodingException;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/security/sasl/PlainClient.class */
final class PlainClient implements SaslClient {
    private boolean completed = false;
    private byte[] pw;
    private String authorizationID;
    private String authenticationID;
    private static byte SEP = 0;

    PlainClient(String str, String str2, byte[] bArr) throws SaslException {
        if (str2 == null || bArr == null) {
            throw new SaslException("PLAIN: authorization ID and password must be specified");
        }
        this.authorizationID = str;
        this.authenticationID = str2;
        this.pw = bArr;
    }

    @Override // javax.security.sasl.SaslClient
    public String getMechanismName() {
        return "PLAIN";
    }

    @Override // javax.security.sasl.SaslClient
    public boolean hasInitialResponse() {
        return true;
    }

    @Override // javax.security.sasl.SaslClient
    public void dispose() throws SaslException {
        clearPassword();
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("PLAIN authentication already completed");
        }
        this.completed = true;
        try {
            byte[] bytes = this.authorizationID != null ? this.authorizationID.getBytes(InternalZipConstants.CHARSET_UTF8) : null;
            byte[] bytes2 = this.authenticationID.getBytes(InternalZipConstants.CHARSET_UTF8);
            byte[] bArr2 = new byte[this.pw.length + bytes2.length + 2 + (bytes == null ? 0 : bytes.length)];
            int length = 0;
            if (bytes != null) {
                System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
                length = bytes.length;
            }
            int i2 = length;
            int i3 = length + 1;
            bArr2[i2] = SEP;
            System.arraycopy(bytes2, 0, bArr2, i3, bytes2.length);
            int length2 = i3 + bytes2.length;
            bArr2[length2] = SEP;
            System.arraycopy(this.pw, 0, bArr2, length2 + 1, this.pw.length);
            clearPassword();
            return bArr2;
        } catch (UnsupportedEncodingException e2) {
            throw new SaslException("Cannot get UTF-8 encoding of ids", e2);
        }
    }

    @Override // javax.security.sasl.SaslClient
    public boolean isComplete() {
        return this.completed;
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (this.completed) {
            throw new SaslException("PLAIN supports neither integrity nor privacy");
        }
        throw new IllegalStateException("PLAIN authentication not completed");
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (this.completed) {
            throw new SaslException("PLAIN supports neither integrity nor privacy");
        }
        throw new IllegalStateException("PLAIN authentication not completed");
    }

    @Override // javax.security.sasl.SaslClient
    public Object getNegotiatedProperty(String str) {
        if (this.completed) {
            if (str.equals(Sasl.QOP)) {
                return "auth";
            }
            return null;
        }
        throw new IllegalStateException("PLAIN authentication not completed");
    }

    private void clearPassword() {
        if (this.pw != null) {
            for (int i2 = 0; i2 < this.pw.length; i2++) {
                this.pw[i2] = 0;
            }
            this.pw = null;
        }
    }

    protected void finalize() {
        clearPassword();
    }
}
