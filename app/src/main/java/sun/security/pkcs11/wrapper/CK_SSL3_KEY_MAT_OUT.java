package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_SSL3_KEY_MAT_OUT.class */
public class CK_SSL3_KEY_MAT_OUT {
    public long hClientMacSecret;
    public long hServerMacSecret;
    public long hClientKey;
    public long hServerKey;
    public byte[] pIVClient;
    public byte[] pIVServer;

    public String toString() {
        return Constants.INDENT + "hClientMacSecret: " + this.hClientMacSecret + Constants.NEWLINE + Constants.INDENT + "hServerMacSecret: " + this.hServerMacSecret + Constants.NEWLINE + Constants.INDENT + "hClientKey: " + this.hClientKey + Constants.NEWLINE + Constants.INDENT + "hServerKey: " + this.hServerKey + Constants.NEWLINE + Constants.INDENT + "pIVClient: " + Functions.toHexString(this.pIVClient) + Constants.NEWLINE + Constants.INDENT + "pIVServer: " + Functions.toHexString(this.pIVServer);
    }
}
