package org.apache.commons.net.imap;

import com.sun.istack.internal.localization.Localizable;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import org.apache.commons.net.imap.IMAP;
import org.apache.commons.net.util.Base64;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/imap/AuthenticatingIMAPClient.class */
public class AuthenticatingIMAPClient extends IMAPSClient {
    public AuthenticatingIMAPClient() {
        this(IMAPSClient.DEFAULT_PROTOCOL, false);
    }

    public AuthenticatingIMAPClient(boolean implicit) {
        this(IMAPSClient.DEFAULT_PROTOCOL, implicit);
    }

    public AuthenticatingIMAPClient(String proto) {
        this(proto, false);
    }

    public AuthenticatingIMAPClient(String proto, boolean implicit) {
        this(proto, implicit, null);
    }

    public AuthenticatingIMAPClient(String proto, boolean implicit, SSLContext ctx) {
        super(proto, implicit, ctx);
    }

    public AuthenticatingIMAPClient(boolean implicit, SSLContext ctx) {
        this(IMAPSClient.DEFAULT_PROTOCOL, implicit, ctx);
    }

    public AuthenticatingIMAPClient(SSLContext context) {
        this(false, context);
    }

    public boolean authenticate(AUTH_METHOD method, String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        return auth(method, username, password);
    }

    public boolean auth(AUTH_METHOD method, String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        if (!IMAPReply.isContinuation(sendCommand(IMAPCommand.AUTHENTICATE, method.getAuthName()))) {
            return false;
        }
        switch (method) {
            case PLAIN:
                int result = sendData(Base64.encodeBase64StringUnChunked((Localizable.NOT_LOCALIZABLE + username + Localizable.NOT_LOCALIZABLE + password).getBytes(getCharset())));
                if (result == 0) {
                    setState(IMAP.IMAPState.AUTH_STATE);
                }
                if (result == 0) {
                }
                break;
            case CRAM_MD5:
                byte[] serverChallenge = Base64.decodeBase64(getReplyString().substring(2).trim());
                Mac hmac_md5 = Mac.getInstance("HmacMD5");
                hmac_md5.init(new SecretKeySpec(password.getBytes(getCharset()), "HmacMD5"));
                byte[] hmacResult = _convertToHexString(hmac_md5.doFinal(serverChallenge)).getBytes(getCharset());
                byte[] usernameBytes = username.getBytes(getCharset());
                byte[] toEncode = new byte[usernameBytes.length + 1 + hmacResult.length];
                System.arraycopy(usernameBytes, 0, toEncode, 0, usernameBytes.length);
                toEncode[usernameBytes.length] = 32;
                System.arraycopy(hmacResult, 0, toEncode, usernameBytes.length + 1, hmacResult.length);
                int result2 = sendData(Base64.encodeBase64StringUnChunked(toEncode));
                if (result2 == 0) {
                    setState(IMAP.IMAPState.AUTH_STATE);
                }
                if (result2 == 0) {
                }
                break;
            case LOGIN:
                if (sendData(Base64.encodeBase64StringUnChunked(username.getBytes(getCharset()))) == 3) {
                    int result3 = sendData(Base64.encodeBase64StringUnChunked(password.getBytes(getCharset())));
                    if (result3 == 0) {
                        setState(IMAP.IMAPState.AUTH_STATE);
                    }
                    if (result3 == 0) {
                    }
                }
                break;
            case XOAUTH:
                int result4 = sendData(username);
                if (result4 == 0) {
                    setState(IMAP.IMAPState.AUTH_STATE);
                }
                if (result4 == 0) {
                }
                break;
        }
        return false;
    }

    private String _convertToHexString(byte[] a2) {
        StringBuilder result = new StringBuilder(a2.length * 2);
        for (byte element : a2) {
            if ((element & 255) <= 15) {
                result.append("0");
            }
            result.append(Integer.toHexString(element & 255));
        }
        return result.toString();
    }

    /* loaded from: commons-net-3.6.jar:org/apache/commons/net/imap/AuthenticatingIMAPClient$AUTH_METHOD.class */
    public enum AUTH_METHOD {
        PLAIN("PLAIN"),
        CRAM_MD5("CRAM-MD5"),
        LOGIN("LOGIN"),
        XOAUTH("XOAUTH");

        private final String authName;

        AUTH_METHOD(String name) {
            this.authName = name;
        }

        public final String getAuthName() {
            return this.authName;
        }
    }
}
