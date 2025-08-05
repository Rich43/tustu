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
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/security/sasl/digest/DigestMD5Client.class */
final class DigestMD5Client extends DigestMD5Base implements SaslClient {
    private static final String CIPHER_PROPERTY = "com.sun.security.sasl.digest.cipher";
    private static final int REALM = 0;
    private static final int QOP = 1;
    private static final int ALGORITHM = 2;
    private static final int NONCE = 3;
    private static final int MAXBUF = 4;
    private static final int CHARSET = 5;
    private static final int CIPHER = 6;
    private static final int RESPONSE_AUTH = 7;
    private static final int STALE = 8;
    private int nonceCount;
    private String specifiedCipher;
    private byte[] cnonce;
    private String username;
    private char[] passwd;
    private byte[] authzidBytes;
    private static final String MY_CLASS_NAME = DigestMD5Client.class.getName();
    private static final String[] DIRECTIVE_KEY = {"realm", "qop", "algorithm", "nonce", "maxbuf", "charset", "cipher", "rspauth", "stale"};

    DigestMD5Client(String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        super(map, MY_CLASS_NAME, 2, str2 + "/" + str3, callbackHandler);
        if (str != null) {
            this.authzid = str;
            try {
                this.authzidBytes = str.getBytes(InternalZipConstants.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e2) {
                throw new SaslException("DIGEST-MD5: Error encoding authzid value into UTF-8", e2);
            }
        }
        if (map != null) {
            this.specifiedCipher = (String) map.get(CIPHER_PROPERTY);
            logger.log(Level.FINE, "DIGEST60:Explicitly specified cipher: {0}", this.specifiedCipher);
        }
    }

    @Override // javax.security.sasl.SaslClient
    public boolean hasInitialResponse() {
        return false;
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        if (bArr.length > 2048) {
            throw new SaslException("DIGEST-MD5: Invalid digest-challenge length. Got:  " + bArr.length + " Expected < 2048");
        }
        switch (this.step) {
            case 2:
                ArrayList arrayList = new ArrayList(3);
                byte[][] directives = parseDirectives(bArr, DIRECTIVE_KEY, arrayList, 0);
                try {
                    processChallenge(directives, arrayList);
                    checkQopSupport(directives[1], directives[6]);
                    this.step++;
                    return generateClientResponse(directives[5]);
                } catch (SaslException e2) {
                    this.step = 0;
                    clearPassword();
                    throw e2;
                } catch (IOException e3) {
                    this.step = 0;
                    clearPassword();
                    throw new SaslException("DIGEST-MD5: Error generating digest response-value", e3);
                }
            case 3:
                try {
                    validateResponseValue(parseDirectives(bArr, DIRECTIVE_KEY, null, 0)[7]);
                    if (this.integrity && this.privacy) {
                        this.secCtx = new DigestMD5Base.DigestPrivacy(true);
                    } else if (this.integrity) {
                        this.secCtx = new DigestMD5Base.DigestIntegrity(true);
                    }
                    return null;
                } finally {
                    clearPassword();
                    this.step = 0;
                    this.completed = true;
                }
            default:
                throw new SaslException("DIGEST-MD5: Client at illegal state");
        }
    }

    private void processChallenge(byte[][] bArr, List<byte[]> list) throws SaslException, UnsupportedEncodingException {
        if (bArr[5] != null) {
            if (!"utf-8".equals(new String(bArr[5], this.encoding))) {
                throw new SaslException("DIGEST-MD5: digest-challenge format violation. Unrecognised charset value: " + new String(bArr[5]));
            }
            this.encoding = InternalZipConstants.CHARSET_UTF8;
            this.useUTF8 = true;
        }
        if (bArr[2] == null) {
            throw new SaslException("DIGEST-MD5: Digest-challenge format violation: algorithm directive missing");
        }
        if (!"md5-sess".equals(new String(bArr[2], this.encoding))) {
            throw new SaslException("DIGEST-MD5: Digest-challenge format violation. Invalid value for 'algorithm' directive: " + ((Object) bArr[2]));
        }
        if (bArr[3] == null) {
            throw new SaslException("DIGEST-MD5: Digest-challenge format violation: nonce directive missing");
        }
        this.nonce = bArr[3];
        try {
            String[] strArr = null;
            if (bArr[0] != null) {
                if (list == null || list.size() <= 1) {
                    this.negotiatedRealm = new String(bArr[0], this.encoding);
                } else {
                    strArr = new String[list.size()];
                    for (int i2 = 0; i2 < strArr.length; i2++) {
                        strArr[i2] = new String(list.get(i2), this.encoding);
                    }
                }
            }
            NameCallback nameCallback = this.authzid == null ? new NameCallback("DIGEST-MD5 authentication ID: ") : new NameCallback("DIGEST-MD5 authentication ID: ", this.authzid);
            PasswordCallback passwordCallback = new PasswordCallback("DIGEST-MD5 password: ", false);
            if (strArr == null) {
                RealmCallback realmCallback = this.negotiatedRealm == null ? new RealmCallback("DIGEST-MD5 realm: ") : new RealmCallback("DIGEST-MD5 realm: ", this.negotiatedRealm);
                this.cbh.handle(new Callback[]{realmCallback, nameCallback, passwordCallback});
                this.negotiatedRealm = realmCallback.getText();
                if (this.negotiatedRealm == null) {
                    this.negotiatedRealm = "";
                }
            } else {
                RealmChoiceCallback realmChoiceCallback = new RealmChoiceCallback("DIGEST-MD5 realm: ", strArr, 0, false);
                this.cbh.handle(new Callback[]{realmChoiceCallback, nameCallback, passwordCallback});
                int[] selectedIndexes = realmChoiceCallback.getSelectedIndexes();
                if (selectedIndexes == null || selectedIndexes[0] < 0 || selectedIndexes[0] >= strArr.length) {
                    throw new SaslException("DIGEST-MD5: Invalid realm chosen");
                }
                this.negotiatedRealm = strArr[selectedIndexes[0]];
            }
            this.passwd = passwordCallback.getPassword();
            passwordCallback.clearPassword();
            this.username = nameCallback.getName();
            if (this.username == null || this.passwd == null) {
                throw new SaslException("DIGEST-MD5: authentication ID and password must be specified");
            }
            int i3 = bArr[4] == null ? 65536 : Integer.parseInt(new String(bArr[4], this.encoding));
            this.sendMaxBufSize = this.sendMaxBufSize == 0 ? i3 : Math.min(this.sendMaxBufSize, i3);
        } catch (SaslException e2) {
            throw e2;
        } catch (IOException e3) {
            throw new SaslException("DIGEST-MD5: Error acquiring realm, authentication ID or password", e3);
        } catch (UnsupportedCallbackException e4) {
            throw new SaslException("DIGEST-MD5: Cannot perform callback to acquire realm, authentication ID or password", e4);
        }
    }

    private void checkQopSupport(byte[] bArr, byte[] bArr2) throws IOException {
        String str;
        if (bArr == null) {
            str = "auth";
        } else {
            str = new String(bArr, this.encoding);
        }
        switch (findPreferredMask(combineMasks(parseQop(str, new String[3], true)), this.qop)) {
            case 0:
                throw new SaslException("DIGEST-MD5: No common protection layer between client and server");
            case 1:
                this.negotiatedQop = "auth";
                break;
            case 2:
                this.negotiatedQop = "auth-int";
                this.integrity = true;
                this.rawSendSize = this.sendMaxBufSize - 16;
                break;
            case 4:
                this.negotiatedQop = "auth-conf";
                this.integrity = true;
                this.privacy = true;
                this.rawSendSize = this.sendMaxBufSize - 26;
                checkStrengthSupport(bArr2);
                break;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST61:Raw send size: {0}", new Integer(this.rawSendSize));
        }
    }

    private void checkStrengthSupport(byte[] bArr) throws IOException {
        if (bArr == null) {
            throw new SaslException("DIGEST-MD5: server did not specify cipher to use for 'auth-conf'");
        }
        String str = new String(bArr, this.encoding);
        StringTokenizer stringTokenizer = new StringTokenizer(str, ", \t\n");
        int iCountTokens = stringTokenizer.countTokens();
        byte[] bArr2 = new byte[5];
        bArr2[0] = 0;
        bArr2[1] = 0;
        bArr2[2] = 0;
        bArr2[3] = 0;
        bArr2[4] = 0;
        String[] strArr = new String[bArr2.length];
        for (int i2 = 0; i2 < iCountTokens; i2++) {
            String strNextToken = stringTokenizer.nextToken();
            for (int i3 = 0; i3 < CIPHER_TOKENS.length; i3++) {
                if (strNextToken.equals(CIPHER_TOKENS[i3])) {
                    int i4 = i3;
                    bArr2[i4] = (byte) (bArr2[i4] | CIPHER_MASKS[i3]);
                    strArr[i3] = strNextToken;
                    logger.log(Level.FINE, "DIGEST62:Server supports {0}", strNextToken);
                }
            }
        }
        byte[] platformCiphers = getPlatformCiphers();
        byte b2 = 0;
        for (int i5 = 0; i5 < bArr2.length; i5++) {
            int i6 = i5;
            bArr2[i6] = (byte) (bArr2[i6] & platformCiphers[i5]);
            b2 = (byte) (b2 | bArr2[i5]);
        }
        if (b2 == 0) {
            throw new SaslException("DIGEST-MD5: Client supports none of these cipher suites: " + str);
        }
        this.negotiatedCipher = findCipherAndStrength(bArr2, strArr);
        if (this.negotiatedCipher == null) {
            throw new SaslException("DIGEST-MD5: Unable to negotiate a strength level for 'auth-conf'");
        }
        logger.log(Level.FINE, "DIGEST63:Cipher suite: {0}", this.negotiatedCipher);
    }

    private String findCipherAndStrength(byte[] bArr, String[] strArr) {
        for (int i2 = 0; i2 < this.strength.length; i2++) {
            byte b2 = this.strength[i2];
            if (b2 != 0) {
                for (int i3 = 0; i3 < bArr.length; i3++) {
                    if (b2 == bArr[i3] && (this.specifiedCipher == null || this.specifiedCipher.equals(strArr[i3]))) {
                        switch (b2) {
                            case 1:
                                this.negotiatedStrength = "low";
                                break;
                            case 2:
                                this.negotiatedStrength = "medium";
                                break;
                            case 4:
                                this.negotiatedStrength = "high";
                                break;
                        }
                        return strArr[i3];
                    }
                }
            }
        }
        return null;
    }

    private byte[] generateClientResponse(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.useUTF8) {
            byteArrayOutputStream.write("charset=".getBytes(this.encoding));
            byteArrayOutputStream.write(bArr);
            byteArrayOutputStream.write(44);
        }
        byteArrayOutputStream.write(("username=\"" + quotedStringValue(this.username) + "\",").getBytes(this.encoding));
        if (this.negotiatedRealm.length() > 0) {
            byteArrayOutputStream.write(("realm=\"" + quotedStringValue(this.negotiatedRealm) + "\",").getBytes(this.encoding));
        }
        byteArrayOutputStream.write("nonce=\"".getBytes(this.encoding));
        writeQuotedStringValue(byteArrayOutputStream, this.nonce);
        byteArrayOutputStream.write(34);
        byteArrayOutputStream.write(44);
        this.nonceCount = getNonceCount(this.nonce);
        byteArrayOutputStream.write(("nc=" + nonceCountToHex(this.nonceCount) + ",").getBytes(this.encoding));
        this.cnonce = generateNonce();
        byteArrayOutputStream.write("cnonce=\"".getBytes(this.encoding));
        writeQuotedStringValue(byteArrayOutputStream, this.cnonce);
        byteArrayOutputStream.write("\",".getBytes(this.encoding));
        byteArrayOutputStream.write(("digest-uri=\"" + this.digestUri + "\",").getBytes(this.encoding));
        byteArrayOutputStream.write("maxbuf=".getBytes(this.encoding));
        byteArrayOutputStream.write(String.valueOf(this.recvMaxBufSize).getBytes(this.encoding));
        byteArrayOutputStream.write(44);
        try {
            byteArrayOutputStream.write("response=".getBytes(this.encoding));
            byteArrayOutputStream.write(generateResponseValue("AUTHENTICATE", this.digestUri, this.negotiatedQop, this.username, this.negotiatedRealm, this.passwd, this.nonce, this.cnonce, this.nonceCount, this.authzidBytes));
            byteArrayOutputStream.write(44);
            byteArrayOutputStream.write(("qop=" + this.negotiatedQop).getBytes(this.encoding));
            if (this.negotiatedCipher != null) {
                byteArrayOutputStream.write((",cipher=\"" + this.negotiatedCipher + PdfOps.DOUBLE_QUOTE__TOKEN).getBytes(this.encoding));
            }
            if (this.authzidBytes != null) {
                byteArrayOutputStream.write(",authzid=\"".getBytes(this.encoding));
                writeQuotedStringValue(byteArrayOutputStream, this.authzidBytes);
                byteArrayOutputStream.write(PdfOps.DOUBLE_QUOTE__TOKEN.getBytes(this.encoding));
            }
            if (byteArrayOutputStream.size() > 4096) {
                throw new SaslException("DIGEST-MD5: digest-response size too large. Length: " + byteArrayOutputStream.size());
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e2) {
            throw new SaslException("DIGEST-MD5: Error generating response value", e2);
        }
    }

    private void validateResponseValue(byte[] bArr) throws SaslException {
        if (bArr == null) {
            throw new SaslException("DIGEST-MD5: Authenication failed. Expecting 'rspauth' authentication success message");
        }
        try {
            if (!Arrays.equals(generateResponseValue("", this.digestUri, this.negotiatedQop, this.username, this.negotiatedRealm, this.passwd, this.nonce, this.cnonce, this.nonceCount, this.authzidBytes), bArr)) {
                throw new SaslException("Server's rspauth value does not match what client expects");
            }
        } catch (IOException e2) {
            throw new SaslException("Problem generating response value for verification", e2);
        } catch (NoSuchAlgorithmException e3) {
            throw new SaslException("Problem generating response value for verification", e3);
        }
    }

    private static int getNonceCount(byte[] bArr) {
        return 1;
    }

    private void clearPassword() {
        if (this.passwd != null) {
            for (int i2 = 0; i2 < this.passwd.length; i2++) {
                this.passwd[i2] = 0;
            }
            this.passwd = null;
        }
    }
}
