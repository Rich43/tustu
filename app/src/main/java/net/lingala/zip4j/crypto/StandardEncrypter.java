package net.lingala.zip4j.crypto;

import java.util.Random;
import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/StandardEncrypter.class */
public class StandardEncrypter implements IEncrypter {
    private ZipCryptoEngine zipCryptoEngine;
    private byte[] headerBytes;

    public StandardEncrypter(char[] password, int crc) throws ZipException {
        if (password == null || password.length <= 0) {
            throw new ZipException("input password is null or empty in standard encrpyter constructor");
        }
        this.zipCryptoEngine = new ZipCryptoEngine();
        this.headerBytes = new byte[12];
        init(password, crc);
    }

    private void init(char[] password, int crc) throws ZipException {
        if (password == null || password.length <= 0) {
            throw new ZipException("input password is null or empty, cannot initialize standard encrypter");
        }
        this.zipCryptoEngine.initKeys(password);
        this.headerBytes = generateRandomBytes(12);
        this.zipCryptoEngine.initKeys(password);
        this.headerBytes[11] = (byte) (crc >>> 24);
        this.headerBytes[10] = (byte) (crc >>> 16);
        if (this.headerBytes.length < 12) {
            throw new ZipException("invalid header bytes generated, cannot perform standard encryption");
        }
        encryptData(this.headerBytes);
    }

    @Override // net.lingala.zip4j.crypto.IEncrypter
    public int encryptData(byte[] buff) throws ZipException {
        if (buff == null) {
            throw new NullPointerException();
        }
        return encryptData(buff, 0, buff.length);
    }

    @Override // net.lingala.zip4j.crypto.IEncrypter
    public int encryptData(byte[] buff, int start, int len) throws ZipException {
        if (len < 0) {
            throw new ZipException("invalid length specified to decrpyt data");
        }
        for (int i2 = start; i2 < start + len; i2++) {
            try {
                buff[i2] = encryptByte(buff[i2]);
            } catch (Exception e2) {
                throw new ZipException(e2);
            }
        }
        return len;
    }

    protected byte encryptByte(byte val) {
        byte temp_val = (byte) (val ^ (this.zipCryptoEngine.decryptByte() & 255));
        this.zipCryptoEngine.updateKeys(val);
        return temp_val;
    }

    protected byte[] generateRandomBytes(int size) throws ZipException {
        if (size <= 0) {
            throw new ZipException("size is either 0 or less than 0, cannot generate header for standard encryptor");
        }
        byte[] buff = new byte[size];
        Random rand = new Random();
        for (int i2 = 0; i2 < buff.length; i2++) {
            buff[i2] = encryptByte((byte) rand.nextInt(256));
        }
        return buff;
    }

    public byte[] getHeaderBytes() {
        return this.headerBytes;
    }
}
