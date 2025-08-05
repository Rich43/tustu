package net.lingala.zip4j.crypto;

import net.lingala.zip4j.exception.ZipException;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/IEncrypter.class */
public interface IEncrypter {
    int encryptData(byte[] bArr) throws ZipException;

    int encryptData(byte[] bArr, int i2, int i3) throws ZipException;
}
