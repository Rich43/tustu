package sun.security.rsa;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: rt.jar:sun/security/rsa/MGF1.class */
public final class MGF1 {
    private final MessageDigest md;

    MGF1(String str) throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance(str);
    }

    void generateAndXor(byte[] bArr, int i2, int i3, int i4, byte[] bArr2, int i5) throws RuntimeException {
        byte[] bArr3 = new byte[4];
        byte[] bArr4 = new byte[this.md.getDigestLength()];
        while (i4 > 0) {
            this.md.update(bArr, i2, i3);
            this.md.update(bArr3);
            try {
                this.md.digest(bArr4, 0, bArr4.length);
                int i6 = 0;
                while (i6 < bArr4.length && i4 > 0) {
                    int i7 = i5;
                    i5++;
                    int i8 = i6;
                    i6++;
                    bArr2[i7] = (byte) (bArr2[i7] ^ bArr4[i8]);
                    i4--;
                }
                if (i4 > 0) {
                    int length = bArr3.length - 1;
                    while (true) {
                        int i9 = length;
                        byte b2 = (byte) (bArr3[i9] + 1);
                        bArr3[i9] = b2;
                        if (b2 != 0 || length <= 0) {
                            break;
                        } else {
                            length--;
                        }
                    }
                }
            } catch (DigestException e2) {
                throw new RuntimeException(e2.toString());
            }
        }
    }

    String getName() {
        return "MGF1" + this.md.getAlgorithm();
    }
}
