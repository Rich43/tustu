package com.sun.security.sasl.digest;

import com.sun.security.sasl.digest.DigestMD5Base;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/security/sasl/digest/DigestMD5Server.class */
final class DigestMD5Server extends DigestMD5Base implements SaslServer {
    private static final String UTF8_DIRECTIVE = "charset=utf-8,";
    private static final String ALGORITHM_DIRECTIVE = "algorithm=md5-sess";
    private static final int NONCE_COUNT_VALUE = 1;
    private static final String UTF8_PROPERTY = "com.sun.security.sasl.digest.utf8";
    private static final String REALM_PROPERTY = "com.sun.security.sasl.digest.realm";
    private static final int USERNAME = 0;
    private static final int REALM = 1;
    private static final int NONCE = 2;
    private static final int CNONCE = 3;
    private static final int NONCE_COUNT = 4;
    private static final int QOP = 5;
    private static final int DIGEST_URI = 6;
    private static final int RESPONSE = 7;
    private static final int MAXBUF = 8;
    private static final int CHARSET = 9;
    private static final int CIPHER = 10;
    private static final int AUTHZID = 11;
    private static final int AUTH_PARAM = 12;
    private String specifiedQops;
    private byte[] myCiphers;
    private List<String> serverRealms;
    private static final String MY_CLASS_NAME = DigestMD5Server.class.getName();
    private static final String[] DIRECTIVE_KEY = {"username", "realm", "nonce", "cnonce", "nonce-count", "qop", "digest-uri", "response", "maxbuf", "charset", "cipher", "authzid", "auth-param"};

    DigestMD5Server(String str, String str2, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        super(map, MY_CLASS_NAME, 1, str + "/" + (str2 == null ? "*" : str2), callbackHandler);
        this.serverRealms = new ArrayList();
        this.useUTF8 = true;
        if (map != null) {
            this.specifiedQops = (String) map.get(Sasl.QOP);
            if ("false".equals((String) map.get(UTF8_PROPERTY))) {
                this.useUTF8 = false;
                logger.log(Level.FINE, "DIGEST80:Server supports ISO-Latin-1");
            }
            String str3 = (String) map.get(REALM_PROPERTY);
            if (str3 != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(str3, ", \t\n");
                int iCountTokens = stringTokenizer.countTokens();
                for (int i2 = 0; i2 < iCountTokens; i2++) {
                    String strNextToken = stringTokenizer.nextToken();
                    logger.log(Level.FINE, "DIGEST81:Server supports realm {0}", strNextToken);
                    this.serverRealms.add(strNextToken);
                }
            }
        }
        this.encoding = this.useUTF8 ? InternalZipConstants.CHARSET_UTF8 : "8859_1";
        if (this.serverRealms.isEmpty()) {
            if (str2 == null) {
                throw new SaslException("A realm must be provided in props or serverName");
            }
            this.serverRealms.add(str2);
        }
    }

    @Override // javax.security.sasl.SaslServer
    public byte[] evaluateResponse(byte[] bArr) throws SaslException {
        if (bArr.length > 4096) {
            throw new SaslException("DIGEST-MD5: Invalid digest response length. Got:  " + bArr.length + " Expected < 4096");
        }
        try {
            switch (this.step) {
                case 1:
                    if (bArr.length != 0) {
                        throw new SaslException("DIGEST-MD5 must not have an initial response");
                    }
                    String string = null;
                    if ((this.allQop & 4) != 0) {
                        this.myCiphers = getPlatformCiphers();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i2 = 0; i2 < CIPHER_TOKENS.length; i2++) {
                            if (this.myCiphers[i2] != 0) {
                                if (stringBuffer.length() > 0) {
                                    stringBuffer.append(',');
                                }
                                stringBuffer.append(CIPHER_TOKENS[i2]);
                            }
                        }
                        string = stringBuffer.toString();
                    }
                    try {
                        byte[] bArrGenerateChallenge = generateChallenge(this.serverRealms, this.specifiedQops, string);
                        this.step = 3;
                        return bArrGenerateChallenge;
                    } catch (UnsupportedEncodingException e2) {
                        throw new SaslException("DIGEST-MD5: Error encoding challenge", e2);
                    } catch (IOException e3) {
                        throw new SaslException("DIGEST-MD5: Error generating challenge", e3);
                    }
                case 3:
                    try {
                        byte[] bArrValidateClientResponse = validateClientResponse(parseDirectives(bArr, DIRECTIVE_KEY, null, 1));
                        this.step = 0;
                        this.completed = true;
                        if (this.integrity && this.privacy) {
                            this.secCtx = new DigestMD5Base.DigestPrivacy(false);
                        } else if (this.integrity) {
                            this.secCtx = new DigestMD5Base.DigestIntegrity(false);
                        }
                        return bArrValidateClientResponse;
                    } catch (UnsupportedEncodingException e4) {
                        throw new SaslException("DIGEST-MD5: Error validating client response", e4);
                    } catch (SaslException e5) {
                        throw e5;
                    }
                default:
                    throw new SaslException("DIGEST-MD5: Server at illegal state");
            }
        } catch (Throwable th) {
            this.step = 0;
            throw th;
        }
        this.step = 0;
        throw th;
    }

    private byte[] generateChallenge(List<String> list, String str, String str2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; list != null && i2 < list.size(); i2++) {
            byteArrayOutputStream.write("realm=\"".getBytes(this.encoding));
            writeQuotedStringValue(byteArrayOutputStream, list.get(i2).getBytes(this.encoding));
            byteArrayOutputStream.write(34);
            byteArrayOutputStream.write(44);
        }
        byteArrayOutputStream.write("nonce=\"".getBytes(this.encoding));
        this.nonce = generateNonce();
        writeQuotedStringValue(byteArrayOutputStream, this.nonce);
        byteArrayOutputStream.write(34);
        byteArrayOutputStream.write(44);
        if (str != null) {
            byteArrayOutputStream.write("qop=\"".getBytes(this.encoding));
            writeQuotedStringValue(byteArrayOutputStream, str.getBytes(this.encoding));
            byteArrayOutputStream.write(34);
            byteArrayOutputStream.write(44);
        }
        if (this.recvMaxBufSize != 65536) {
            byteArrayOutputStream.write(("maxbuf=\"" + this.recvMaxBufSize + "\",").getBytes(this.encoding));
        }
        if (this.useUTF8) {
            byteArrayOutputStream.write(UTF8_DIRECTIVE.getBytes(this.encoding));
        }
        if (str2 != null) {
            byteArrayOutputStream.write("cipher=\"".getBytes(this.encoding));
            writeQuotedStringValue(byteArrayOutputStream, str2.getBytes(this.encoding));
            byteArrayOutputStream.write(34);
            byteArrayOutputStream.write(44);
        }
        byteArrayOutputStream.write(ALGORITHM_DIRECTIVE.getBytes(this.encoding));
        return byteArrayOutputStream.toByteArray();
    }

    /* JADX WARN: Finally extract failed */
    private byte[] validateClientResponse(byte[][] bArr) throws SaslException, UnsupportedEncodingException {
        byte b2;
        if (bArr[9] != null && (!this.useUTF8 || !"utf-8".equals(new String(bArr[9], this.encoding)))) {
            throw new SaslException("DIGEST-MD5: digest response format violation. Incompatible charset value: " + new String(bArr[9]));
        }
        int i2 = bArr[8] == null ? 65536 : Integer.parseInt(new String(bArr[8], this.encoding));
        this.sendMaxBufSize = this.sendMaxBufSize == 0 ? i2 : Math.min(this.sendMaxBufSize, i2);
        if (bArr[0] == null) {
            throw new SaslException("DIGEST-MD5: digest response format violation. Missing username.");
        }
        String str = new String(bArr[0], this.encoding);
        logger.log(Level.FINE, "DIGEST82:Username: {0}", str);
        this.negotiatedRealm = bArr[1] != null ? new String(bArr[1], this.encoding) : "";
        logger.log(Level.FINE, "DIGEST83:Client negotiated realm: {0}", this.negotiatedRealm);
        if (!this.serverRealms.contains(this.negotiatedRealm)) {
            throw new SaslException("DIGEST-MD5: digest response format violation. Nonexistent realm: " + this.negotiatedRealm);
        }
        if (bArr[2] == null) {
            throw new SaslException("DIGEST-MD5: digest response format violation. Missing nonce.");
        }
        if (!Arrays.equals(bArr[2], this.nonce)) {
            throw new SaslException("DIGEST-MD5: digest response format violation. Mismatched nonce.");
        }
        if (bArr[3] == null) {
            throw new SaslException("DIGEST-MD5: digest response format violation. Missing cnonce.");
        }
        byte[] bArr2 = bArr[3];
        if (bArr[4] != null && 1 != Integer.parseInt(new String(bArr[4], this.encoding), 16)) {
            throw new SaslException("DIGEST-MD5: digest response format violation. Nonce count does not match: " + new String(bArr[4]));
        }
        this.negotiatedQop = bArr[5] != null ? new String(bArr[5], this.encoding) : "auth";
        logger.log(Level.FINE, "DIGEST84:Client negotiated qop: {0}", this.negotiatedQop);
        switch (this.negotiatedQop) {
            case "auth":
                b2 = 1;
                break;
            case "auth-int":
                b2 = 2;
                this.integrity = true;
                this.rawSendSize = this.sendMaxBufSize - 16;
                break;
            case "auth-conf":
                b2 = 4;
                this.privacy = true;
                this.integrity = true;
                this.rawSendSize = this.sendMaxBufSize - 26;
                break;
            default:
                throw new SaslException("DIGEST-MD5: digest response format violation. Invalid QOP: " + this.negotiatedQop);
        }
        if ((b2 & this.allQop) == 0) {
            throw new SaslException("DIGEST-MD5: server does not support  qop: " + this.negotiatedQop);
        }
        if (this.privacy) {
            this.negotiatedCipher = bArr[10] != null ? new String(bArr[10], this.encoding) : null;
            if (this.negotiatedCipher == null) {
                throw new SaslException("DIGEST-MD5: digest response format violation. No cipher specified.");
            }
            int i3 = -1;
            logger.log(Level.FINE, "DIGEST85:Client negotiated cipher: {0}", this.negotiatedCipher);
            int i4 = 0;
            while (true) {
                if (i4 < CIPHER_TOKENS.length) {
                    if (!this.negotiatedCipher.equals(CIPHER_TOKENS[i4]) || this.myCiphers[i4] == 0) {
                        i4++;
                    } else {
                        i3 = i4;
                    }
                }
            }
            if (i3 == -1) {
                throw new SaslException("DIGEST-MD5: server does not support cipher: " + this.negotiatedCipher);
            }
            if ((CIPHER_MASKS[i3] & 4) != 0) {
                this.negotiatedStrength = "high";
            } else if ((CIPHER_MASKS[i3] & 2) != 0) {
                this.negotiatedStrength = "medium";
            } else {
                this.negotiatedStrength = "low";
            }
            logger.log(Level.FINE, "DIGEST86:Negotiated strength: {0}", this.negotiatedStrength);
        }
        String str2 = bArr[6] != null ? new String(bArr[6], this.encoding) : null;
        if (str2 != null) {
            logger.log(Level.FINE, "DIGEST87:digest URI: {0}", str2);
        }
        if (!uriMatches(this.digestUri, str2)) {
            throw new SaslException("DIGEST-MD5: digest response format violation. Mismatched URI: " + str2 + "; expecting: " + this.digestUri);
        }
        this.digestUri = str2;
        byte[] bArr3 = bArr[7];
        if (bArr3 == null) {
            throw new SaslException("DIGEST-MD5: digest response format  violation. Missing response.");
        }
        byte[] bArr4 = bArr[11];
        String str3 = bArr4 != null ? new String(bArr4, this.encoding) : str;
        if (bArr4 != null) {
            logger.log(Level.FINE, "DIGEST88:Authzid: {0}", new String(bArr4));
        }
        try {
            RealmCallback realmCallback = new RealmCallback("DIGEST-MD5 realm: ", this.negotiatedRealm);
            NameCallback nameCallback = new NameCallback("DIGEST-MD5 authentication ID: ", str);
            PasswordCallback passwordCallback = new PasswordCallback("DIGEST-MD5 password: ", false);
            this.cbh.handle(new Callback[]{realmCallback, nameCallback, passwordCallback});
            char[] password = passwordCallback.getPassword();
            passwordCallback.clearPassword();
            try {
                if (password == null) {
                    throw new SaslException("DIGEST-MD5: cannot acquire password for " + str + " in realm : " + this.negotiatedRealm);
                }
                try {
                    if (!Arrays.equals(bArr3, generateResponseValue("AUTHENTICATE", this.digestUri, this.negotiatedQop, str, this.negotiatedRealm, password, this.nonce, bArr2, 1, bArr4))) {
                        throw new SaslException("DIGEST-MD5: digest response format violation. Mismatched response.");
                    }
                    try {
                        AuthorizeCallback authorizeCallback = new AuthorizeCallback(str, str3);
                        this.cbh.handle(new Callback[]{authorizeCallback});
                        if (!authorizeCallback.isAuthorized()) {
                            throw new SaslException("DIGEST-MD5: " + str + " is not authorized to act as " + str3);
                        }
                        this.authzid = authorizeCallback.getAuthorizedID();
                        byte[] bArrGenerateResponseAuth = generateResponseAuth(str, password, bArr2, 1, bArr4);
                        for (int i5 = 0; i5 < password.length; i5++) {
                            password[i5] = 0;
                        }
                        return bArrGenerateResponseAuth;
                    } catch (SaslException e2) {
                        throw e2;
                    } catch (IOException e3) {
                        throw new SaslException("DIGEST-MD5: IO error checking authzid", e3);
                    } catch (UnsupportedCallbackException e4) {
                        throw new SaslException("DIGEST-MD5: Cannot perform callback to check authzid", e4);
                    }
                } catch (IOException e5) {
                    throw new SaslException("DIGEST-MD5: problem duplicating client response", e5);
                } catch (NoSuchAlgorithmException e6) {
                    throw new SaslException("DIGEST-MD5: problem duplicating client response", e6);
                }
            } catch (Throwable th) {
                for (int i6 = 0; i6 < password.length; i6++) {
                    password[i6] = 0;
                }
                throw th;
            }
        } catch (IOException e7) {
            throw new SaslException("DIGEST-MD5: IO error acquiring password", e7);
        } catch (UnsupportedCallbackException e8) {
            throw new SaslException("DIGEST-MD5: Cannot perform callback to acquire password", e8);
        }
    }

    private static boolean uriMatches(String str, String str2) {
        if (str.equalsIgnoreCase(str2)) {
            return true;
        }
        if (str.endsWith("/*")) {
            int length = str.length() - 1;
            return str.substring(0, length).equalsIgnoreCase(str2.substring(0, length));
        }
        return false;
    }

    private byte[] generateResponseAuth(String str, char[] cArr, byte[] bArr, int i2, byte[] bArr2) throws SaslException {
        try {
            byte[] bArrGenerateResponseValue = generateResponseValue("", this.digestUri, this.negotiatedQop, str, this.negotiatedRealm, cArr, this.nonce, bArr, i2, bArr2);
            byte[] bArr3 = new byte[bArrGenerateResponseValue.length + 8];
            System.arraycopy("rspauth=".getBytes(this.encoding), 0, bArr3, 0, 8);
            System.arraycopy(bArrGenerateResponseValue, 0, bArr3, 8, bArrGenerateResponseValue.length);
            return bArr3;
        } catch (IOException e2) {
            throw new SaslException("DIGEST-MD5: problem generating response", e2);
        } catch (NoSuchAlgorithmException e3) {
            throw new SaslException("DIGEST-MD5: problem generating response", e3);
        }
    }

    @Override // javax.security.sasl.SaslServer
    public String getAuthorizationID() {
        if (this.completed) {
            return this.authzid;
        }
        throw new IllegalStateException("DIGEST-MD5 server negotiation not complete");
    }
}
