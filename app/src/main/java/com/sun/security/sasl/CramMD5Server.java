package com.sun.security.sasl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/security/sasl/CramMD5Server.class */
final class CramMD5Server extends CramMD5Base implements SaslServer {
    private String fqdn;
    private byte[] challengeData = null;
    private String authzid;
    private CallbackHandler cbh;

    CramMD5Server(String str, String str2, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        if (str2 == null) {
            throw new SaslException("CRAM-MD5: fully qualified server name must be specified");
        }
        this.fqdn = str2;
        this.cbh = callbackHandler;
    }

    @Override // javax.security.sasl.SaslServer
    public byte[] evaluateResponse(byte[] bArr) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("CRAM-MD5 authentication already completed");
        }
        if (this.aborted) {
            throw new IllegalStateException("CRAM-MD5 authentication previously aborted due to error");
        }
        try {
            if (this.challengeData == null) {
                if (bArr.length != 0) {
                    this.aborted = true;
                    throw new SaslException("CRAM-MD5 does not expect any initial response");
                }
                long jNextLong = new Random().nextLong();
                long jCurrentTimeMillis = System.currentTimeMillis();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append('<');
                stringBuffer.append(jNextLong);
                stringBuffer.append('.');
                stringBuffer.append(jCurrentTimeMillis);
                stringBuffer.append('@');
                stringBuffer.append(this.fqdn);
                stringBuffer.append('>');
                String string = stringBuffer.toString();
                logger.log(Level.FINE, "CRAMSRV01:Generated challenge: {0}", string);
                this.challengeData = string.getBytes(InternalZipConstants.CHARSET_UTF8);
                return (byte[]) this.challengeData.clone();
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "CRAMSRV02:Received response: {0}", new String(bArr, InternalZipConstants.CHARSET_UTF8));
            }
            int i2 = 0;
            int i3 = 0;
            while (true) {
                if (i3 >= bArr.length) {
                    break;
                }
                if (bArr[i3] != 32) {
                    i3++;
                } else {
                    i2 = i3;
                    break;
                }
            }
            if (i2 == 0) {
                this.aborted = true;
                throw new SaslException("CRAM-MD5: Invalid response; space missing");
            }
            String str = new String(bArr, 0, i2, InternalZipConstants.CHARSET_UTF8);
            logger.log(Level.FINE, "CRAMSRV03:Extracted username: {0}", str);
            NameCallback nameCallback = new NameCallback("CRAM-MD5 authentication ID: ", str);
            PasswordCallback passwordCallback = new PasswordCallback("CRAM-MD5 password: ", false);
            this.cbh.handle(new Callback[]{nameCallback, passwordCallback});
            char[] password = passwordCallback.getPassword();
            if (password == null || password.length == 0) {
                this.aborted = true;
                throw new SaslException("CRAM-MD5: username not found: " + str);
            }
            passwordCallback.clearPassword();
            String str2 = new String(password);
            for (int i4 = 0; i4 < password.length; i4++) {
                password[i4] = 0;
            }
            this.pw = str2.getBytes(InternalZipConstants.CHARSET_UTF8);
            String strHMAC_MD5 = HMAC_MD5(this.pw, this.challengeData);
            logger.log(Level.FINE, "CRAMSRV04:Expecting digest: {0}", strHMAC_MD5);
            clearPassword();
            byte[] bytes = strHMAC_MD5.getBytes(InternalZipConstants.CHARSET_UTF8);
            if (bytes.length != (bArr.length - i2) - 1) {
                this.aborted = true;
                throw new SaslException("Invalid response");
            }
            int i5 = 0;
            for (int i6 = i2 + 1; i6 < bArr.length; i6++) {
                int i7 = i5;
                i5++;
                if (bytes[i7] != bArr[i6]) {
                    this.aborted = true;
                    throw new SaslException("Invalid response");
                }
            }
            AuthorizeCallback authorizeCallback = new AuthorizeCallback(str, str);
            this.cbh.handle(new Callback[]{authorizeCallback});
            if (authorizeCallback.isAuthorized()) {
                this.authzid = authorizeCallback.getAuthorizedID();
                logger.log(Level.FINE, "CRAMSRV05:Authorization id: {0}", this.authzid);
                this.completed = true;
                return null;
            }
            this.aborted = true;
            throw new SaslException("CRAM-MD5: user not authorized: " + str);
        } catch (UnsupportedEncodingException e2) {
            this.aborted = true;
            throw new SaslException("UTF8 not available on platform", e2);
        } catch (NoSuchAlgorithmException e3) {
            this.aborted = true;
            throw new SaslException("MD5 algorithm not available on platform", e3);
        } catch (SaslException e4) {
            throw e4;
        } catch (IOException e5) {
            this.aborted = true;
            throw new SaslException("CRAM-MD5 authentication failed", e5);
        } catch (UnsupportedCallbackException e6) {
            this.aborted = true;
            throw new SaslException("CRAM-MD5 authentication failed", e6);
        }
    }

    @Override // javax.security.sasl.SaslServer
    public String getAuthorizationID() {
        if (this.completed) {
            return this.authzid;
        }
        throw new IllegalStateException("CRAM-MD5 authentication not completed");
    }
}
