package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/StandardDecrypter.class */
public class StandardDecrypter implements IDecrypter {
    private FileHeader fileHeader;
    private byte[] crc = new byte[4];
    private ZipCryptoEngine zipCryptoEngine;

    public StandardDecrypter(FileHeader fileHeader, byte[] headerBytes) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("one of more of the input parameters were null in StandardDecryptor");
        }
        this.fileHeader = fileHeader;
        this.zipCryptoEngine = new ZipCryptoEngine();
        init(headerBytes);
    }

    @Override // net.lingala.zip4j.crypto.IDecrypter
    public int decryptData(byte[] buff) throws ZipException {
        return decryptData(buff, 0, buff.length);
    }

    @Override // net.lingala.zip4j.crypto.IDecrypter
    public int decryptData(byte[] buff, int start, int len) throws ZipException {
        if (start < 0 || len < 0) {
            throw new ZipException("one of the input parameters were null in standard decrpyt data");
        }
        for (int i2 = 0; i2 < len; i2++) {
            try {
                int val = ((buff[i2] & 255) ^ this.zipCryptoEngine.decryptByte()) & 255;
                this.zipCryptoEngine.updateKeys((byte) val);
                buff[i2] = (byte) val;
            } catch (Exception e2) {
                throw new ZipException(e2);
            }
        }
        return len;
    }

    public void init(byte[] headerBytes) throws ZipException {
        byte[] crcBuff = this.fileHeader.getCrcBuff();
        this.crc[3] = (byte) (crcBuff[3] & 255);
        this.crc[2] = (byte) ((crcBuff[3] >> 8) & 255);
        this.crc[1] = (byte) ((crcBuff[3] >> 16) & 255);
        this.crc[0] = (byte) ((crcBuff[3] >> 24) & 255);
        if (this.crc[2] > 0 || this.crc[1] > 0 || this.crc[0] > 0) {
            throw new IllegalStateException("Invalid CRC in File Header");
        }
        if (this.fileHeader.getPassword() == null || this.fileHeader.getPassword().length <= 0) {
            throw new ZipException("Wrong password!", 5);
        }
        this.zipCryptoEngine.initKeys(this.fileHeader.getPassword());
        try {
            int result = headerBytes[0];
            for (int i2 = 0; i2 < 12; i2++) {
                this.zipCryptoEngine.updateKeys((byte) (result ^ this.zipCryptoEngine.decryptByte()));
                if (i2 + 1 != 12) {
                    result = headerBytes[i2 + 1];
                }
            }
        } catch (Exception e2) {
            throw new ZipException(e2);
        }
    }
}
