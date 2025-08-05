package com.intel.bluetooth.obex;

import com.intel.bluetooth.DebugLog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;
import javax.obex.Authenticator;
import javax.obex.PasswordAuthentication;
import javax.obex.ServerRequestHandler;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXAuthentication.class */
class OBEXAuthentication {
    private static byte[] privateKey;
    private static long uniqueTimestamp = 0;
    private static final byte[] COLUMN = {58};

    OBEXAuthentication() {
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXAuthentication$Challenge.class */
    static class Challenge {
        private String realm;
        private boolean isUserIdRequired;
        private boolean isFullAccess;
        byte[] nonce;

        Challenge(byte[] data) throws IOException {
            read(data);
        }

        Challenge(String realm, boolean isUserIdRequired, boolean isFullAccess, byte[] nonce) {
            this.realm = realm;
            this.isUserIdRequired = isUserIdRequired;
            this.isFullAccess = isFullAccess;
            this.nonce = nonce;
        }

        byte[] write() {
            byte[] realmArray;
            byte charSetCode;
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            buf.write(0);
            buf.write(16);
            buf.write(this.nonce, 0, 16);
            byte options = (byte) ((this.isUserIdRequired ? 1 : 0) | (!this.isFullAccess ? 2 : 0));
            buf.write(1);
            buf.write(1);
            buf.write(options);
            if (this.realm != null) {
                try {
                    realmArray = OBEXUtils.getUTF16Bytes(this.realm);
                    charSetCode = -1;
                } catch (UnsupportedEncodingException e2) {
                    try {
                        realmArray = this.realm.getBytes("iso-8859-1");
                    } catch (UnsupportedEncodingException e3) {
                        realmArray = new byte[0];
                    }
                    charSetCode = 1;
                }
                buf.write(2);
                buf.write(realmArray.length + 1);
                buf.write(charSetCode);
                buf.write(realmArray, 0, realmArray.length);
            }
            return buf.toByteArray();
        }

        void read(byte[] data) throws IOException {
            DebugLog.debug("authChallenge", data);
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < data.length) {
                    int tag = data[i3] & 255;
                    int len = data[i3 + 1] & 255;
                    int i4 = i3 + 2;
                    switch (tag) {
                        case 0:
                            if (len != 16) {
                                throw new IOException("OBEX Digest Challenge error in tag Nonce");
                            }
                            this.nonce = new byte[16];
                            System.arraycopy(data, i4, this.nonce, 0, 16);
                            break;
                        case 1:
                            byte options = data[i4];
                            DebugLog.debug("authChallenge options", options);
                            this.isUserIdRequired = (options & 1) != 0;
                            this.isFullAccess = (options & 2) == 0;
                            break;
                        case 2:
                            int charSetCode = data[i4] & 255;
                            byte[] chars = new byte[len - 1];
                            System.arraycopy(data, i4 + 1, chars, 0, chars.length);
                            if (charSetCode == 255) {
                                this.realm = OBEXUtils.newStringUTF16(chars);
                                break;
                            } else if (charSetCode == 0) {
                                this.realm = new String(chars, "ASCII");
                                break;
                            } else if (charSetCode <= 9) {
                                this.realm = new String(chars, new StringBuffer().append("ISO-8859-").append(charSetCode).toString());
                                break;
                            } else {
                                DebugLog.error(new StringBuffer().append("Unsupported charset code ").append(charSetCode).append(" in Challenge").toString());
                                this.realm = new String(chars, 0, len - 1, "ASCII");
                                break;
                            }
                        default:
                            DebugLog.error(new StringBuffer().append("invalid authChallenge tag ").append(tag).toString());
                            break;
                    }
                    i2 = i4 + len;
                } else {
                    return;
                }
            }
        }

        public boolean isUserIdRequired() {
            return this.isUserIdRequired;
        }

        public boolean isFullAccess() {
            return this.isFullAccess;
        }

        public String getRealm() {
            return this.realm;
        }
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXAuthentication$DigestResponse.class */
    static class DigestResponse {
        byte[] requestDigest;
        byte[] userName;
        byte[] nonce;

        DigestResponse() {
        }

        byte[] write() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            buf.write(0);
            buf.write(16);
            buf.write(this.requestDigest, 0, 16);
            if (this.userName != null) {
                buf.write(1);
                buf.write(this.userName.length);
                buf.write(this.userName, 0, this.userName.length);
            }
            buf.write(2);
            buf.write(16);
            buf.write(this.nonce, 0, 16);
            return buf.toByteArray();
        }

        void read(byte[] data) throws IOException {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < data.length) {
                    int tag = data[i3] & 255;
                    int len = data[i3 + 1] & 255;
                    int i4 = i3 + 2;
                    switch (tag) {
                        case 0:
                            if (len != 16) {
                                throw new IOException("OBEX Digest Response error in tag request-digest");
                            }
                            this.requestDigest = new byte[16];
                            System.arraycopy(data, i4, this.requestDigest, 0, 16);
                            break;
                        case 1:
                            this.userName = new byte[len];
                            System.arraycopy(data, i4, this.userName, 0, this.userName.length);
                            break;
                        case 2:
                            if (len != 16) {
                                throw new IOException("OBEX Digest Response error in tag Nonce");
                            }
                            this.nonce = new byte[16];
                            System.arraycopy(data, i4, this.nonce, 0, 16);
                            break;
                    }
                    i2 = i4 + len;
                } else {
                    return;
                }
            }
        }
    }

    static byte[] createChallenge(String realm, boolean isUserIdRequired, boolean isFullAccess) {
        Challenge challenge = new Challenge(realm, isUserIdRequired, isFullAccess, createNonce());
        return challenge.write();
    }

    static boolean handleAuthenticationResponse(OBEXHeaderSetImpl incomingHeaders, Authenticator authenticator, ServerRequestHandler serverHandler, Vector authChallengesSent) throws IOException {
        if (!incomingHeaders.hasAuthenticationResponses()) {
            return false;
        }
        Enumeration iter = incomingHeaders.getAuthenticationResponses();
        while (iter.hasMoreElements()) {
            byte[] authResponse = (byte[]) iter.nextElement2();
            DigestResponse dr = new DigestResponse();
            dr.read(authResponse);
            DebugLog.debug("got nonce", dr.nonce);
            Challenge challengeSent = null;
            Enumeration challengeIter = authChallengesSent.elements();
            while (true) {
                if (!challengeIter.hasMoreElements()) {
                    break;
                }
                Challenge c2 = (Challenge) challengeIter.nextElement2();
                if (equals(c2.nonce, dr.nonce)) {
                    challengeSent = c2;
                    break;
                }
            }
            if (challengeSent == null) {
                throw new IOException("Authentication response for unknown challenge");
            }
            byte[] password = authenticator.onAuthenticationResponse(dr.userName);
            if (password == null) {
                throw new IOException("Authentication request failed, password is not supplied");
            }
            MD5DigestWrapper md5 = new MD5DigestWrapper();
            md5.update(dr.nonce);
            md5.update(COLUMN);
            md5.update(password);
            byte[] claulated = md5.digest();
            if (!equals(dr.requestDigest, claulated)) {
                DebugLog.debug("got digest", dr.requestDigest);
                DebugLog.debug("  expected", claulated);
                if (serverHandler != null) {
                    serverHandler.onAuthenticationFailure(dr.userName);
                } else {
                    throw new IOException("Authentication failure");
                }
            } else {
                return true;
            }
        }
        return false;
    }

    static void handleAuthenticationChallenge(OBEXHeaderSetImpl incomingHeaders, OBEXHeaderSetImpl replyHeaders, Authenticator authenticator) throws IOException {
        if (!incomingHeaders.hasAuthenticationChallenge()) {
            return;
        }
        Enumeration iter = incomingHeaders.getAuthenticationChallenges();
        while (iter.hasMoreElements()) {
            byte[] authChallenge = (byte[]) iter.nextElement2();
            Challenge challenge = new Challenge(authChallenge);
            PasswordAuthentication pwd = authenticator.onAuthenticationChallenge(challenge.getRealm(), challenge.isUserIdRequired(), challenge.isFullAccess());
            DigestResponse dr = new DigestResponse();
            dr.nonce = challenge.nonce;
            DebugLog.debug("got nonce", dr.nonce);
            if (challenge.isUserIdRequired()) {
                dr.userName = pwd.getUserName();
            }
            MD5DigestWrapper md5 = new MD5DigestWrapper();
            md5.update(dr.nonce);
            md5.update(COLUMN);
            md5.update(pwd.getPassword());
            dr.requestDigest = md5.digest();
            DebugLog.debug("send digest", dr.requestDigest);
            replyHeaders.addAuthenticationResponse(dr.write());
        }
    }

    private static synchronized byte[] createNonce() {
        MD5DigestWrapper md5 = new MD5DigestWrapper();
        md5.update(createTimestamp());
        md5.update(COLUMN);
        md5.update(getPrivateKey());
        return md5.digest();
    }

    static boolean equals(byte[] digest1, byte[] digest2) {
        for (int i2 = 0; i2 < 16; i2++) {
            if (digest1[i2] != digest2[i2]) {
                return false;
            }
        }
        return true;
    }

    private static synchronized byte[] getPrivateKey() {
        if (privateKey != null) {
            return privateKey;
        }
        MD5DigestWrapper md5 = new MD5DigestWrapper();
        md5.update(createTimestamp());
        privateKey = md5.digest();
        return privateKey;
    }

    private static synchronized byte[] createTimestamp() {
        long t2 = System.currentTimeMillis();
        if (t2 <= uniqueTimestamp) {
            t2 = uniqueTimestamp + 1;
        }
        uniqueTimestamp = t2;
        byte[] buf = new byte[8];
        for (int i2 = 0; i2 < buf.length; i2++) {
            buf[i2] = (byte) (t2 >> ((buf.length - 1) << 3));
            t2 <<= 8;
        }
        return buf;
    }
}
