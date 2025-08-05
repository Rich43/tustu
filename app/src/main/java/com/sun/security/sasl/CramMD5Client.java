package com.sun.security.sasl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/security/sasl/CramMD5Client.class */
final class CramMD5Client extends CramMD5Base implements SaslClient {
    private String username;

    CramMD5Client(String str, byte[] bArr) throws SaslException {
        if (str == null || bArr == null) {
            throw new SaslException("CRAM-MD5: authentication ID and password must be specified");
        }
        this.username = str;
        this.pw = bArr;
    }

    @Override // javax.security.sasl.SaslClient
    public boolean hasInitialResponse() {
        return false;
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("CRAM-MD5 authentication already completed");
        }
        if (this.aborted) {
            throw new IllegalStateException("CRAM-MD5 authentication previously aborted due to error");
        }
        try {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "CRAMCLNT01:Received challenge: {0}", new String(bArr, InternalZipConstants.CHARSET_UTF8));
            }
            String strHMAC_MD5 = HMAC_MD5(this.pw, bArr);
            clearPassword();
            String str = this.username + " " + strHMAC_MD5;
            logger.log(Level.FINE, "CRAMCLNT02:Sending response: {0}", str);
            this.completed = true;
            return str.getBytes(InternalZipConstants.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e2) {
            this.aborted = true;
            throw new SaslException("UTF8 not available on platform", e2);
        } catch (NoSuchAlgorithmException e3) {
            this.aborted = true;
            throw new SaslException("MD5 algorithm not available on platform", e3);
        }
    }
}
