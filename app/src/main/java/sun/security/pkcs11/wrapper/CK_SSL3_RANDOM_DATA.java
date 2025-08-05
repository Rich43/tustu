package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_SSL3_RANDOM_DATA.class */
public class CK_SSL3_RANDOM_DATA {
    public byte[] pClientRandom;
    public byte[] pServerRandom;

    public CK_SSL3_RANDOM_DATA(byte[] bArr, byte[] bArr2) {
        this.pClientRandom = bArr;
        this.pServerRandom = bArr2;
    }

    public String toString() {
        return Constants.INDENT + "pClientRandom: " + Functions.toHexString(this.pClientRandom) + Constants.NEWLINE + Constants.INDENT + "ulClientRandomLen: " + this.pClientRandom.length + Constants.NEWLINE + Constants.INDENT + "pServerRandom: " + Functions.toHexString(this.pServerRandom) + Constants.NEWLINE + Constants.INDENT + "ulServerRandomLen: " + this.pServerRandom.length;
    }
}
