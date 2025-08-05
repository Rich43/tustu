package net.lingala.zip4j.crypto.PBKDF2;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/PBKDF2/MacBasedPRF.class */
public class MacBasedPRF implements PRF {
    protected Mac mac;
    protected int hLen;
    protected String macAlgorithm;

    public MacBasedPRF(String macAlgorithm) {
        this.macAlgorithm = macAlgorithm;
        try {
            this.mac = Mac.getInstance(macAlgorithm);
            this.hLen = this.mac.getMacLength();
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        }
    }

    public MacBasedPRF(String macAlgorithm, String provider) {
        this.macAlgorithm = macAlgorithm;
        try {
            this.mac = Mac.getInstance(macAlgorithm, provider);
            this.hLen = this.mac.getMacLength();
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        } catch (NoSuchProviderException e3) {
            throw new RuntimeException(e3);
        }
    }

    @Override // net.lingala.zip4j.crypto.PBKDF2.PRF
    public byte[] doFinal(byte[] M2) throws IllegalStateException {
        byte[] r2 = this.mac.doFinal(M2);
        return r2;
    }

    public byte[] doFinal() {
        byte[] r2 = this.mac.doFinal();
        return r2;
    }

    @Override // net.lingala.zip4j.crypto.PBKDF2.PRF
    public int getHLen() {
        return this.hLen;
    }

    @Override // net.lingala.zip4j.crypto.PBKDF2.PRF
    public void init(byte[] P2) {
        try {
            this.mac.init(new SecretKeySpec(P2, this.macAlgorithm));
        } catch (InvalidKeyException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void update(byte[] U2) {
        try {
            this.mac.update(U2);
        } catch (IllegalStateException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void update(byte[] U2, int start, int len) {
        try {
            this.mac.update(U2, start, len);
        } catch (IllegalStateException e2) {
            throw new RuntimeException(e2);
        }
    }
}
