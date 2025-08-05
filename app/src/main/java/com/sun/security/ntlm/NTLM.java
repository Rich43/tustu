package com.sun.security.ntlm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.HexDumpEncoder;
import sun.security.provider.MD4;

/* loaded from: rt.jar:com/sun/security/ntlm/NTLM.class */
class NTLM {
    private final SecretKeyFactory fac;
    private final Cipher cipher;
    private final MessageDigest md4;
    private final Mac hmac;
    private final MessageDigest md5;
    private static final boolean DEBUG;

    /* renamed from: v, reason: collision with root package name */
    final Version f12049v;
    final boolean writeLM;
    final boolean writeNTLM;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NTLM.class.desiredAssertionStatus();
        DEBUG = System.getProperty("ntlm.debug") != null;
    }

    protected NTLM(String str) throws NTLMException {
        str = str == null ? "LMv2/NTLMv2" : str;
        switch (str) {
            case "LM":
                this.f12049v = Version.NTLM;
                this.writeLM = true;
                this.writeNTLM = false;
                break;
            case "NTLM":
                this.f12049v = Version.NTLM;
                this.writeLM = false;
                this.writeNTLM = true;
                break;
            case "LM/NTLM":
                this.f12049v = Version.NTLM;
                this.writeNTLM = true;
                this.writeLM = true;
                break;
            case "NTLM2":
                this.f12049v = Version.NTLM2;
                this.writeNTLM = true;
                this.writeLM = true;
                break;
            case "LMv2":
                this.f12049v = Version.NTLMv2;
                this.writeLM = true;
                this.writeNTLM = false;
                break;
            case "NTLMv2":
                this.f12049v = Version.NTLMv2;
                this.writeLM = false;
                this.writeNTLM = true;
                break;
            case "LMv2/NTLMv2":
                this.f12049v = Version.NTLMv2;
                this.writeNTLM = true;
                this.writeLM = true;
                break;
            default:
                throw new NTLMException(5, "Unknown version " + str);
        }
        try {
            this.fac = SecretKeyFactory.getInstance("DES");
            this.cipher = Cipher.getInstance("DES/ECB/NoPadding");
            this.md4 = MD4.getInstance();
            this.hmac = Mac.getInstance("HmacMD5");
            this.md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
            throw new AssertionError();
        } catch (NoSuchPaddingException e3) {
            throw new AssertionError();
        }
    }

    public void debug(String str, Object... objArr) {
        if (DEBUG) {
            System.out.printf(str, objArr);
        }
    }

    public void debug(byte[] bArr) {
        if (DEBUG) {
            try {
                new HexDumpEncoder().encodeBuffer(bArr, System.out);
            } catch (IOException e2) {
            }
        }
    }

    /* loaded from: rt.jar:com/sun/security/ntlm/NTLM$Reader.class */
    static class Reader {
        private final byte[] internal;

        Reader(byte[] bArr) {
            this.internal = bArr;
        }

        int readInt(int i2) throws NTLMException {
            try {
                return (this.internal[i2] & 255) + ((this.internal[i2 + 1] & 255) << 8) + ((this.internal[i2 + 2] & 255) << 16) + ((this.internal[i2 + 3] & 255) << 24);
            } catch (ArrayIndexOutOfBoundsException e2) {
                throw new NTLMException(1, "Input message incorrect size");
            }
        }

        int readShort(int i2) throws NTLMException {
            try {
                return (this.internal[i2] & 255) + ((this.internal[i2 + 1] & 255) << 8);
            } catch (ArrayIndexOutOfBoundsException e2) {
                throw new NTLMException(1, "Input message incorrect size");
            }
        }

        byte[] readBytes(int i2, int i3) throws NTLMException {
            try {
                return Arrays.copyOfRange(this.internal, i2, i2 + i3);
            } catch (ArrayIndexOutOfBoundsException e2) {
                throw new NTLMException(1, "Input message incorrect size");
            }
        }

        byte[] readSecurityBuffer(int i2) throws NTLMException {
            int i3 = readInt(i2 + 4);
            if (i3 == 0) {
                return new byte[0];
            }
            try {
                return Arrays.copyOfRange(this.internal, i3, i3 + readShort(i2));
            } catch (ArrayIndexOutOfBoundsException e2) {
                throw new NTLMException(1, "Input message incorrect size");
            }
        }

        String readSecurityBuffer(int i2, boolean z2) throws NTLMException {
            byte[] securityBuffer = readSecurityBuffer(i2);
            if (securityBuffer == null) {
                return null;
            }
            try {
                return new String(securityBuffer, z2 ? "UnicodeLittleUnmarked" : "ISO8859_1");
            } catch (UnsupportedEncodingException e2) {
                throw new NTLMException(1, "Invalid input encoding");
            }
        }
    }

    /* loaded from: rt.jar:com/sun/security/ntlm/NTLM$Writer.class */
    static class Writer {
        private byte[] internal;
        private int current;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !NTLM.class.desiredAssertionStatus();
        }

        Writer(int i2, int i3) {
            if (!$assertionsDisabled && i3 >= 256) {
                throw new AssertionError();
            }
            this.internal = new byte[256];
            this.current = i3;
            System.arraycopy(new byte[]{78, 84, 76, 77, 83, 83, 80, 0, (byte) i2}, 0, this.internal, 0, 9);
        }

        void writeShort(int i2, int i3) {
            this.internal[i2] = (byte) i3;
            this.internal[i2 + 1] = (byte) (i3 >> 8);
        }

        void writeInt(int i2, int i3) {
            this.internal[i2] = (byte) i3;
            this.internal[i2 + 1] = (byte) (i3 >> 8);
            this.internal[i2 + 2] = (byte) (i3 >> 16);
            this.internal[i2 + 3] = (byte) (i3 >> 24);
        }

        void writeBytes(int i2, byte[] bArr) {
            System.arraycopy(bArr, 0, this.internal, i2, bArr.length);
        }

        void writeSecurityBuffer(int i2, byte[] bArr) throws NTLMException {
            if (bArr == null) {
                writeInt(i2 + 4, this.current);
                return;
            }
            int length = bArr.length;
            if (length > 65535) {
                throw new NTLMException(7, "Invalid data length " + length);
            }
            if (this.current + length > this.internal.length) {
                this.internal = Arrays.copyOf(this.internal, this.current + length + 256);
            }
            writeShort(i2, length);
            writeShort(i2 + 2, length);
            writeInt(i2 + 4, this.current);
            System.arraycopy(bArr, 0, this.internal, this.current, length);
            this.current += length;
        }

        void writeSecurityBuffer(int i2, String str, boolean z2) throws NTLMException {
            byte[] bytes;
            if (str == null) {
                bytes = null;
            } else {
                try {
                    bytes = str.getBytes(z2 ? "UnicodeLittleUnmarked" : "ISO8859_1");
                } catch (UnsupportedEncodingException e2) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    return;
                }
            }
            writeSecurityBuffer(i2, bytes);
        }

        byte[] getBytes() {
            return Arrays.copyOf(this.internal, this.current);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [int[]] */
    /* JADX WARN: Type inference failed for: r2v1, types: [int] */
    /* JADX WARN: Type inference failed for: r2v10, types: [int] */
    /* JADX WARN: Type inference failed for: r2v16, types: [int] */
    /* JADX WARN: Type inference failed for: r2v22, types: [int] */
    /* JADX WARN: Type inference failed for: r2v28, types: [int] */
    /* JADX WARN: Type inference failed for: r2v34, types: [int] */
    /* JADX WARN: Type inference failed for: r2v4, types: [int] */
    /* JADX WARN: Type inference failed for: r2v40, types: [int] */
    /* JADX WARN: Type inference failed for: r2v45 */
    /* JADX WARN: Type inference failed for: r2v47 */
    /* JADX WARN: Type inference failed for: r2v48 */
    /* JADX WARN: Type inference failed for: r2v50 */
    /* JADX WARN: Type inference failed for: r2v51, types: [int] */
    /* JADX WARN: Type inference failed for: r3v14, types: [int] */
    /* JADX WARN: Type inference failed for: r3v21, types: [int] */
    /* JADX WARN: Type inference failed for: r3v28, types: [int] */
    /* JADX WARN: Type inference failed for: r3v35, types: [int] */
    /* JADX WARN: Type inference failed for: r3v42, types: [int] */
    /* JADX WARN: Type inference failed for: r3v7, types: [int] */
    byte[] makeDesKey(byte[] bArr, int i2) {
        ?? r0 = new int[bArr.length];
        for (int i3 = 0; i3 < r0.length; i3++) {
            r0[i3] = bArr[i3] < 0 ? (bArr[i3] == true ? 1 : 0) + 256 : bArr[i3];
        }
        return new byte[]{(byte) r0[i2 + 0], (byte) (((r0[i2 + 0] << 7) & 255) | (r0[i2 + 1] >> 1)), (byte) (((r0[i2 + 1] << 6) & 255) | (r0[i2 + 2] >> 2)), (byte) (((r0[i2 + 2] << 5) & 255) | (r0[i2 + 3] >> 3)), (byte) (((r0[i2 + 3] << 4) & 255) | (r0[i2 + 4] >> 4)), (byte) (((r0[i2 + 4] << 3) & 255) | (r0[i2 + 5] >> 5)), (byte) (((r0[i2 + 5] << 2) & 255) | (r0[i2 + 6] >> 6)), (byte) ((r0[i2 + 6] << 1) & 255)};
    }

    byte[] calcLMHash(byte[] bArr) {
        byte[] bArr2 = {75, 71, 83, 33, 64, 35, 36, 37};
        byte[] bArr3 = new byte[14];
        int length = bArr.length;
        if (length > 14) {
            length = 14;
        }
        System.arraycopy(bArr, 0, bArr3, 0, length);
        try {
            DESKeySpec dESKeySpec = new DESKeySpec(makeDesKey(bArr3, 0));
            DESKeySpec dESKeySpec2 = new DESKeySpec(makeDesKey(bArr3, 7));
            SecretKey secretKeyGenerateSecret = this.fac.generateSecret(dESKeySpec);
            SecretKey secretKeyGenerateSecret2 = this.fac.generateSecret(dESKeySpec2);
            this.cipher.init(1, secretKeyGenerateSecret);
            byte[] bArrDoFinal = this.cipher.doFinal(bArr2, 0, 8);
            this.cipher.init(1, secretKeyGenerateSecret2);
            byte[] bArrDoFinal2 = this.cipher.doFinal(bArr2, 0, 8);
            byte[] bArr4 = new byte[21];
            System.arraycopy(bArrDoFinal, 0, bArr4, 0, 8);
            System.arraycopy(bArrDoFinal2, 0, bArr4, 8, 8);
            return bArr4;
        } catch (InvalidKeyException e2) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        } catch (InvalidKeySpecException e3) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        } catch (BadPaddingException e4) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        } catch (IllegalBlockSizeException e5) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
    }

    byte[] calcNTHash(byte[] bArr) {
        byte[] bArr2 = new byte[21];
        System.arraycopy(this.md4.digest(bArr), 0, bArr2, 0, 16);
        return bArr2;
    }

    byte[] calcResponse(byte[] bArr, byte[] bArr2) {
        try {
            if (!$assertionsDisabled && bArr.length != 21) {
                throw new AssertionError();
            }
            DESKeySpec dESKeySpec = new DESKeySpec(makeDesKey(bArr, 0));
            DESKeySpec dESKeySpec2 = new DESKeySpec(makeDesKey(bArr, 7));
            DESKeySpec dESKeySpec3 = new DESKeySpec(makeDesKey(bArr, 14));
            SecretKey secretKeyGenerateSecret = this.fac.generateSecret(dESKeySpec);
            SecretKey secretKeyGenerateSecret2 = this.fac.generateSecret(dESKeySpec2);
            SecretKey secretKeyGenerateSecret3 = this.fac.generateSecret(dESKeySpec3);
            this.cipher.init(1, secretKeyGenerateSecret);
            byte[] bArrDoFinal = this.cipher.doFinal(bArr2, 0, 8);
            this.cipher.init(1, secretKeyGenerateSecret2);
            byte[] bArrDoFinal2 = this.cipher.doFinal(bArr2, 0, 8);
            this.cipher.init(1, secretKeyGenerateSecret3);
            byte[] bArrDoFinal3 = this.cipher.doFinal(bArr2, 0, 8);
            byte[] bArr3 = new byte[24];
            System.arraycopy(bArrDoFinal, 0, bArr3, 0, 8);
            System.arraycopy(bArrDoFinal2, 0, bArr3, 8, 8);
            System.arraycopy(bArrDoFinal3, 0, bArr3, 16, 8);
            return bArr3;
        } catch (InvalidKeyException e2) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        } catch (InvalidKeySpecException e3) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        } catch (BadPaddingException e4) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        } catch (IllegalBlockSizeException e5) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
    }

    byte[] hmacMD5(byte[] bArr, byte[] bArr2) {
        try {
            this.hmac.init(new SecretKeySpec(Arrays.copyOf(bArr, 16), "HmacMD5"));
            return this.hmac.doFinal(bArr2);
        } catch (RuntimeException e2) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        } catch (InvalidKeyException e3) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
    }

    byte[] calcV2(byte[] bArr, String str, byte[] bArr2, byte[] bArr3) {
        try {
            byte[] bArrHmacMD5 = hmacMD5(bArr, str.getBytes("UnicodeLittleUnmarked"));
            byte[] bArr4 = new byte[bArr2.length + 8];
            System.arraycopy(bArr3, 0, bArr4, 0, 8);
            System.arraycopy(bArr2, 0, bArr4, 8, bArr2.length);
            byte[] bArr5 = new byte[16 + bArr2.length];
            System.arraycopy(hmacMD5(bArrHmacMD5, bArr4), 0, bArr5, 0, 16);
            System.arraycopy(bArr2, 0, bArr5, 16, bArr2.length);
            return bArr5;
        } catch (UnsupportedEncodingException e2) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
    }

    static byte[] ntlm2LM(byte[] bArr) {
        return Arrays.copyOf(bArr, 24);
    }

    byte[] ntlm2NTLM(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        byte[] bArrCopyOf = Arrays.copyOf(bArr3, 16);
        System.arraycopy(bArr2, 0, bArrCopyOf, 8, 8);
        return calcResponse(bArr, Arrays.copyOf(this.md5.digest(bArrCopyOf), 8));
    }

    static byte[] getP1(char[] cArr) {
        try {
            return new String(cArr).toUpperCase(Locale.ENGLISH).getBytes("ISO8859_1");
        } catch (UnsupportedEncodingException e2) {
            return null;
        }
    }

    static byte[] getP2(char[] cArr) {
        try {
            return new String(cArr).getBytes("UnicodeLittleUnmarked");
        } catch (UnsupportedEncodingException e2) {
            return null;
        }
    }
}
