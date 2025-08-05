package net.lingala.zip4j.crypto;

import java.util.Arrays;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.util.Raw;
import org.apache.commons.net.ftp.FTP;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/AESDecrypter.class */
public class AESDecrypter implements IDecrypter {
    private LocalFileHeader localFileHeader;
    private AESEngine aesEngine;
    private MacBasedPRF mac;
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private int SALT_LENGTH;
    private byte[] aesKey;
    private byte[] macKey;
    private byte[] derivedPasswordVerifier;
    private byte[] storedMac;
    private byte[] iv;
    private byte[] counterBlock;
    private final int PASSWORD_VERIFIER_LENGTH = 2;
    private int nonce = 1;
    private int loopCount = 0;

    public AESDecrypter(LocalFileHeader localFileHeader, byte[] salt, byte[] passwordVerifier) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("one of the input parameters is null in AESDecryptor Constructor");
        }
        this.localFileHeader = localFileHeader;
        this.storedMac = null;
        this.iv = new byte[16];
        this.counterBlock = new byte[16];
        init(salt, passwordVerifier);
    }

    private void init(byte[] salt, byte[] passwordVerifier) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("invalid file header in init method of AESDecryptor");
        }
        AESExtraDataRecord aesExtraDataRecord = this.localFileHeader.getAesExtraDataRecord();
        if (aesExtraDataRecord == null) {
            throw new ZipException("invalid aes extra data record - in init method of AESDecryptor");
        }
        switch (aesExtraDataRecord.getAesStrength()) {
            case 1:
                this.KEY_LENGTH = 16;
                this.MAC_LENGTH = 16;
                this.SALT_LENGTH = 8;
                break;
            case 2:
                this.KEY_LENGTH = 24;
                this.MAC_LENGTH = 24;
                this.SALT_LENGTH = 12;
                break;
            case 3:
                this.KEY_LENGTH = 32;
                this.MAC_LENGTH = 32;
                this.SALT_LENGTH = 16;
                break;
            default:
                throw new ZipException(new StringBuffer("invalid aes key strength for file: ").append(this.localFileHeader.getFileName()).toString());
        }
        if (this.localFileHeader.getPassword() == null || this.localFileHeader.getPassword().length <= 0) {
            throw new ZipException("empty or null password provided for AES Decryptor");
        }
        byte[] derivedKey = deriveKey(salt, this.localFileHeader.getPassword());
        if (derivedKey == null || derivedKey.length != this.KEY_LENGTH + this.MAC_LENGTH + 2) {
            throw new ZipException("invalid derived key");
        }
        this.aesKey = new byte[this.KEY_LENGTH];
        this.macKey = new byte[this.MAC_LENGTH];
        this.derivedPasswordVerifier = new byte[2];
        System.arraycopy(derivedKey, 0, this.aesKey, 0, this.KEY_LENGTH);
        System.arraycopy(derivedKey, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
        System.arraycopy(derivedKey, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
        if (this.derivedPasswordVerifier == null) {
            throw new ZipException("invalid derived password verifier for AES");
        }
        if (!Arrays.equals(passwordVerifier, this.derivedPasswordVerifier)) {
            throw new ZipException(new StringBuffer("Wrong Password for file: ").append(this.localFileHeader.getFileName()).toString(), 5);
        }
        this.aesEngine = new AESEngine(this.aesKey);
        this.mac = new MacBasedPRF("HmacSHA1");
        this.mac.init(this.macKey);
    }

    @Override // net.lingala.zip4j.crypto.IDecrypter
    public int decryptData(byte[] buff, int start, int len) throws ZipException {
        if (this.aesEngine == null) {
            throw new ZipException("AES not initialized properly");
        }
        for (int j2 = start; j2 < start + len; j2 += 16) {
            try {
                this.loopCount = j2 + 16 <= start + len ? 16 : (start + len) - j2;
                this.mac.update(buff, j2, this.loopCount);
                Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
                this.aesEngine.processBlock(this.iv, this.counterBlock);
                for (int k2 = 0; k2 < this.loopCount; k2++) {
                    buff[j2 + k2] = (byte) (buff[j2 + k2] ^ this.counterBlock[k2]);
                }
                this.nonce++;
            } catch (ZipException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new ZipException(e3);
            }
        }
        return len;
    }

    @Override // net.lingala.zip4j.crypto.IDecrypter
    public int decryptData(byte[] buff) throws ZipException {
        return decryptData(buff, 0, buff.length);
    }

    private byte[] deriveKey(byte[] salt, char[] password) throws ZipException {
        try {
            PBKDF2Parameters p2 = new PBKDF2Parameters("HmacSHA1", FTP.DEFAULT_CONTROL_ENCODING, salt, 1000);
            PBKDF2Engine e2 = new PBKDF2Engine(p2);
            byte[] derivedKey = e2.deriveKey(password, this.KEY_LENGTH + this.MAC_LENGTH + 2);
            return derivedKey;
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    public int getPasswordVerifierLength() {
        return 2;
    }

    public int getSaltLength() {
        return this.SALT_LENGTH;
    }

    public byte[] getCalculatedAuthenticationBytes() {
        return this.mac.doFinal();
    }

    public void setStoredMac(byte[] storedMac) {
        this.storedMac = storedMac;
    }

    public byte[] getStoredMac() {
        return this.storedMac;
    }
}
