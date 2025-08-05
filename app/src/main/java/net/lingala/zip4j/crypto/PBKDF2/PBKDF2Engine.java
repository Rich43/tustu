package net.lingala.zip4j.crypto.PBKDF2;

import net.lingala.zip4j.util.Raw;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/PBKDF2/PBKDF2Engine.class */
public class PBKDF2Engine {
    protected PBKDF2Parameters parameters;
    protected PRF prf;

    public PBKDF2Engine() {
        this.parameters = null;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters parameters) {
        this.parameters = parameters;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters parameters, PRF prf) {
        this.parameters = parameters;
        this.prf = prf;
    }

    public byte[] deriveKey(char[] inputPassword) {
        return deriveKey(inputPassword, 0);
    }

    public byte[] deriveKey(char[] inputPassword, int dkLen) {
        if (inputPassword == null) {
            throw new NullPointerException();
        }
        byte[] P2 = Raw.convertCharArrayToByteArray(inputPassword);
        assertPRF(P2);
        if (dkLen == 0) {
            dkLen = this.prf.getHLen();
        }
        byte[] r2 = PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), dkLen);
        return r2;
    }

    public boolean verifyKey(char[] inputPassword) {
        byte[] inputKey;
        byte[] referenceKey = getParameters().getDerivedKey();
        if (referenceKey == null || referenceKey.length == 0 || (inputKey = deriveKey(inputPassword, referenceKey.length)) == null || inputKey.length != referenceKey.length) {
            return false;
        }
        for (int i2 = 0; i2 < inputKey.length; i2++) {
            if (inputKey[i2] != referenceKey[i2]) {
                return false;
            }
        }
        return true;
    }

    protected void assertPRF(byte[] P2) {
        if (this.prf == null) {
            this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm());
        }
        this.prf.init(P2);
    }

    public PRF getPseudoRandomFunction() {
        return this.prf;
    }

    protected byte[] PBKDF2(PRF prf, byte[] S2, int c2, int dkLen) {
        if (S2 == null) {
            S2 = new byte[0];
        }
        int hLen = prf.getHLen();
        int l2 = ceil(dkLen, hLen);
        int r2 = dkLen - ((l2 - 1) * hLen);
        byte[] T2 = new byte[l2 * hLen];
        int ti_offset = 0;
        for (int i2 = 1; i2 <= l2; i2++) {
            _F(T2, ti_offset, prf, S2, c2, i2);
            ti_offset += hLen;
        }
        if (r2 < hLen) {
            byte[] DK = new byte[dkLen];
            System.arraycopy(T2, 0, DK, 0, dkLen);
            return DK;
        }
        return T2;
    }

    protected int ceil(int a2, int b2) {
        int m2 = 0;
        if (a2 % b2 > 0) {
            m2 = 1;
        }
        return (a2 / b2) + m2;
    }

    protected void _F(byte[] dest, int offset, PRF prf, byte[] S2, int c2, int blockIndex) {
        int hLen = prf.getHLen();
        byte[] U_r = new byte[hLen];
        byte[] U_i = new byte[S2.length + 4];
        System.arraycopy(S2, 0, U_i, 0, S2.length);
        INT(U_i, S2.length, blockIndex);
        for (int i2 = 0; i2 < c2; i2++) {
            U_i = prf.doFinal(U_i);
            xor(U_r, U_i);
        }
        System.arraycopy(U_r, 0, dest, offset, hLen);
    }

    protected void xor(byte[] dest, byte[] src) {
        for (int i2 = 0; i2 < dest.length; i2++) {
            int i3 = i2;
            dest[i3] = (byte) (dest[i3] ^ src[i2]);
        }
    }

    protected void INT(byte[] dest, int offset, int i2) {
        dest[offset + 0] = (byte) (i2 / 16777216);
        dest[offset + 1] = (byte) (i2 / 65536);
        dest[offset + 2] = (byte) (i2 / 256);
        dest[offset + 3] = (byte) i2;
    }

    public PBKDF2Parameters getParameters() {
        return this.parameters;
    }

    public void setParameters(PBKDF2Parameters parameters) {
        this.parameters = parameters;
    }

    public void setPseudoRandomFunction(PRF prf) {
        this.prf = prf;
    }
}
