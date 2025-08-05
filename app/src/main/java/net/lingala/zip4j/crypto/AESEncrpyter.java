package net.lingala.zip4j.crypto;

import java.util.Random;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.Raw;
import org.apache.commons.net.ftp.FTP;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/AESEncrpyter.class */
public class AESEncrpyter implements IEncrypter {
    private char[] password;
    private int keyStrength;
    private AESEngine aesEngine;
    private MacBasedPRF mac;
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private int SALT_LENGTH;
    private byte[] aesKey;
    private byte[] macKey;
    private byte[] derivedPasswordVerifier;
    private byte[] saltBytes;
    private boolean finished;
    private byte[] iv;
    private byte[] counterBlock;
    private final int PASSWORD_VERIFIER_LENGTH = 2;
    private int nonce = 1;
    private int loopCount = 0;

    public AESEncrpyter(char[] password, int keyStrength) throws ZipException {
        if (password == null || password.length == 0) {
            throw new ZipException("input password is empty or null in AES encrypter constructor");
        }
        if (keyStrength != 1 && keyStrength != 3) {
            throw new ZipException("Invalid key strength in AES encrypter constructor");
        }
        this.password = password;
        this.keyStrength = keyStrength;
        this.finished = false;
        this.counterBlock = new byte[16];
        this.iv = new byte[16];
        init();
    }

    private void init() throws ZipException {
        switch (this.keyStrength) {
            case 1:
                this.KEY_LENGTH = 16;
                this.MAC_LENGTH = 16;
                this.SALT_LENGTH = 8;
                break;
            case 2:
            default:
                throw new ZipException("invalid aes key strength, cannot determine key sizes");
            case 3:
                this.KEY_LENGTH = 32;
                this.MAC_LENGTH = 32;
                this.SALT_LENGTH = 16;
                break;
        }
        this.saltBytes = generateSalt(this.SALT_LENGTH);
        byte[] keyBytes = deriveKey(this.saltBytes, this.password);
        if (keyBytes == null || keyBytes.length != this.KEY_LENGTH + this.MAC_LENGTH + 2) {
            throw new ZipException("invalid key generated, cannot decrypt file");
        }
        this.aesKey = new byte[this.KEY_LENGTH];
        this.macKey = new byte[this.MAC_LENGTH];
        this.derivedPasswordVerifier = new byte[2];
        System.arraycopy(keyBytes, 0, this.aesKey, 0, this.KEY_LENGTH);
        System.arraycopy(keyBytes, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
        System.arraycopy(keyBytes, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
        this.aesEngine = new AESEngine(this.aesKey);
        this.mac = new MacBasedPRF("HmacSHA1");
        this.mac.init(this.macKey);
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

    @Override // net.lingala.zip4j.crypto.IEncrypter
    public int encryptData(byte[] buff) throws ZipException {
        if (buff == null) {
            throw new ZipException("input bytes are null, cannot perform AES encrpytion");
        }
        return encryptData(buff, 0, buff.length);
    }

    @Override // net.lingala.zip4j.crypto.IEncrypter
    public int encryptData(byte[] buff, int start, int len) throws ZipException {
        if (this.finished) {
            throw new ZipException("AES Encrypter is in finished state (A non 16 byte block has already been passed to encrypter)");
        }
        if (len % 16 != 0) {
            this.finished = true;
        }
        for (int j2 = start; j2 < start + len; j2 += 16) {
            this.loopCount = j2 + 16 <= start + len ? 16 : (start + len) - j2;
            Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
            this.aesEngine.processBlock(this.iv, this.counterBlock);
            for (int k2 = 0; k2 < this.loopCount; k2++) {
                buff[j2 + k2] = (byte) (buff[j2 + k2] ^ this.counterBlock[k2]);
            }
            this.mac.update(buff, j2, this.loopCount);
            this.nonce++;
        }
        return len;
    }

    private static byte[] generateSalt(int size) throws ZipException {
        if (size != 8 && size != 16) {
            throw new ZipException("invalid salt size, cannot generate salt");
        }
        int rounds = 0;
        if (size == 8) {
            rounds = 2;
        }
        if (size == 16) {
            rounds = 4;
        }
        byte[] salt = new byte[size];
        for (int j2 = 0; j2 < rounds; j2++) {
            Random rand = new Random();
            int i2 = rand.nextInt();
            salt[0 + (j2 * 4)] = (byte) (i2 >> 24);
            salt[1 + (j2 * 4)] = (byte) (i2 >> 16);
            salt[2 + (j2 * 4)] = (byte) (i2 >> 8);
            salt[3 + (j2 * 4)] = (byte) i2;
        }
        return salt;
    }

    public byte[] getFinalMac() {
        byte[] rawMacBytes = this.mac.doFinal();
        byte[] macBytes = new byte[10];
        System.arraycopy(rawMacBytes, 0, macBytes, 0, 10);
        return macBytes;
    }

    public byte[] getDerivedPasswordVerifier() {
        return this.derivedPasswordVerifier;
    }

    public void setDerivedPasswordVerifier(byte[] derivedPasswordVerifier) {
        this.derivedPasswordVerifier = derivedPasswordVerifier;
    }

    public byte[] getSaltBytes() {
        return this.saltBytes;
    }

    public void setSaltBytes(byte[] saltBytes) {
        this.saltBytes = saltBytes;
    }

    public int getSaltLength() {
        return this.SALT_LENGTH;
    }

    public int getPasswordVeriifierLength() {
        return 2;
    }
}
