package net.lingala.zip4j.crypto.PBKDF2;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/PBKDF2/PRF.class */
interface PRF {
    void init(byte[] bArr);

    byte[] doFinal(byte[] bArr);

    int getHLen();
}
