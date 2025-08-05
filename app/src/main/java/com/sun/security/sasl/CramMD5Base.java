package com.sun.security.sasl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;

/* loaded from: rt.jar:com/sun/security/sasl/CramMD5Base.class */
abstract class CramMD5Base {
    protected boolean completed = false;
    protected boolean aborted = false;
    protected byte[] pw;
    private static final int MD5_BLOCKSIZE = 64;
    private static final String SASL_LOGGER_NAME = "javax.security.sasl";
    protected static Logger logger;

    protected CramMD5Base() {
        initLogger();
    }

    public String getMechanismName() {
        return "CRAM-MD5";
    }

    public boolean isComplete() {
        return this.completed;
    }

    public byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("CRAM-MD5 supports neither integrity nor privacy");
        }
        throw new IllegalStateException("CRAM-MD5 authentication not completed");
    }

    public byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("CRAM-MD5 supports neither integrity nor privacy");
        }
        throw new IllegalStateException("CRAM-MD5 authentication not completed");
    }

    public Object getNegotiatedProperty(String str) {
        if (this.completed) {
            if (str.equals(Sasl.QOP)) {
                return "auth";
            }
            return null;
        }
        throw new IllegalStateException("CRAM-MD5 authentication not completed");
    }

    public void dispose() throws SaslException {
        clearPassword();
    }

    protected void clearPassword() {
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

    static final String HMAC_MD5(byte[] bArr, byte[] bArr2) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        if (bArr.length > 64) {
            bArr = messageDigest.digest(bArr);
        }
        byte[] bArr3 = new byte[64];
        byte[] bArr4 = new byte[64];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr3[i2] = bArr[i2];
            bArr4[i2] = bArr[i2];
        }
        for (int i3 = 0; i3 < 64; i3++) {
            int i4 = i3;
            bArr3[i4] = (byte) (bArr3[i4] ^ 54);
            int i5 = i3;
            bArr4[i5] = (byte) (bArr4[i5] ^ 92);
        }
        messageDigest.update(bArr3);
        messageDigest.update(bArr2);
        byte[] bArrDigest = messageDigest.digest();
        messageDigest.update(bArr4);
        messageDigest.update(bArrDigest);
        byte[] bArrDigest2 = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i6 = 0; i6 < bArrDigest2.length; i6++) {
            if ((bArrDigest2[i6] & 255) < 16) {
                stringBuffer.append("0" + Integer.toHexString(bArrDigest2[i6] & 255));
            } else {
                stringBuffer.append(Integer.toHexString(bArrDigest2[i6] & 255));
            }
        }
        Arrays.fill(bArr3, (byte) 0);
        Arrays.fill(bArr4, (byte) 0);
        return stringBuffer.toString();
    }

    private static synchronized void initLogger() {
        if (logger == null) {
            logger = Logger.getLogger(SASL_LOGGER_NAME);
        }
    }
}
