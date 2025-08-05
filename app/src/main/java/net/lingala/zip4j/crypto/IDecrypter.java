package net.lingala.zip4j.crypto;

import net.lingala.zip4j.exception.ZipException;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/IDecrypter.class */
public interface IDecrypter {
    int decryptData(byte[] bArr, int i2, int i3) throws ZipException;

    int decryptData(byte[] bArr) throws ZipException;
}
