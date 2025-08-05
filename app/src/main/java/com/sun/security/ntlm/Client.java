package com.sun.security.ntlm;

import com.sun.security.ntlm.NTLM;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/security/ntlm/Client.class */
public final class Client extends NTLM {
    private final String hostname;
    private final String username;
    private String domain;
    private byte[] pw1;
    private byte[] pw2;

    @Override // com.sun.security.ntlm.NTLM
    public /* bridge */ /* synthetic */ void debug(byte[] bArr) {
        super.debug(bArr);
    }

    @Override // com.sun.security.ntlm.NTLM
    public /* bridge */ /* synthetic */ void debug(String str, Object[] objArr) {
        super.debug(str, objArr);
    }

    public Client(String str, String str2, String str3, String str4, char[] cArr) throws NTLMException {
        super(str);
        if (str3 == null || cArr == null) {
            throw new NTLMException(6, "username/password cannot be null");
        }
        this.hostname = str2;
        this.username = str3;
        this.domain = str4 == null ? "" : str4;
        this.pw1 = getP1(cArr);
        this.pw2 = getP2(cArr);
        debug("NTLM Client: (h,u,t,version(v)) = (%s,%s,%s,%s(%s))\n", new Object[]{str2, str3, str4, str, this.f12049v.toString()});
    }

    public byte[] type1() {
        NTLM.Writer writer = new NTLM.Writer(1, 32);
        int i2 = 33287;
        if (this.f12049v != Version.NTLM) {
            i2 = 33287 | 524288;
        }
        writer.writeInt(12, i2);
        debug("NTLM Client: Type 1 created\n", new Object[0]);
        debug(writer.getBytes());
        return writer.getBytes();
    }

    public byte[] type3(byte[] bArr, byte[] bArr2) throws NTLMException {
        if (bArr == null || ((this.f12049v != Version.NTLM && bArr2 == null) || (bArr2 != null && bArr2.length != 8))) {
            throw new NTLMException(6, "type2 cannot be null, and nonce must be 8-byte long");
        }
        debug("NTLM Client: Type 2 received\n", new Object[0]);
        debug(bArr);
        NTLM.Reader reader = new NTLM.Reader(bArr);
        byte[] bytes = reader.readBytes(24, 8);
        int i2 = reader.readInt(20);
        boolean z2 = (i2 & 1) == 1;
        int i3 = 557568 | (i2 & 3);
        NTLM.Writer writer = new NTLM.Writer(3, 64);
        byte[] bArrCalcV2 = null;
        writer.writeSecurityBuffer(28, this.domain, z2);
        writer.writeSecurityBuffer(36, this.username, z2);
        writer.writeSecurityBuffer(44, this.hostname, z2);
        if (this.f12049v == Version.NTLM) {
            byte[] bArrCalcLMHash = calcLMHash(this.pw1);
            byte[] bArrCalcNTHash = calcNTHash(this.pw2);
            bArrCalcV2 = this.writeLM ? calcResponse(bArrCalcLMHash, bytes) : null;
            if (this.writeNTLM) {
                bArrCalcV2 = calcResponse(bArrCalcNTHash, bytes);
            }
        } else if (this.f12049v == Version.NTLM2) {
            byte[] bArrCalcNTHash2 = calcNTHash(this.pw2);
            bArrCalcV2 = ntlm2LM(bArr2);
            bArrCalcV2 = ntlm2NTLM(bArrCalcNTHash2, bArr2, bytes);
        } else {
            byte[] bArrCalcNTHash3 = calcNTHash(this.pw2);
            if (this.writeLM) {
                bArrCalcV2 = calcV2(bArrCalcNTHash3, this.username.toUpperCase(Locale.US) + this.domain, bArr2, bytes);
            }
            if (this.writeNTLM) {
                byte[] securityBuffer = (i2 & 8388608) != 0 ? reader.readSecurityBuffer(40) : new byte[0];
                byte[] bArr3 = new byte[32 + securityBuffer.length];
                System.arraycopy(new byte[]{1, 1, 0, 0, 0, 0, 0, 0}, 0, bArr3, 0, 8);
                byte[] byteArray = BigInteger.valueOf(new Date().getTime()).add(new BigInteger("11644473600000")).multiply(BigInteger.valueOf(10000L)).toByteArray();
                for (int i4 = 0; i4 < byteArray.length; i4++) {
                    bArr3[((8 + byteArray.length) - i4) - 1] = byteArray[i4];
                }
                System.arraycopy(bArr2, 0, bArr3, 16, 8);
                System.arraycopy(new byte[]{0, 0, 0, 0}, 0, bArr3, 24, 4);
                System.arraycopy(securityBuffer, 0, bArr3, 28, securityBuffer.length);
                System.arraycopy(new byte[]{0, 0, 0, 0}, 0, bArr3, 28 + securityBuffer.length, 4);
                bArrCalcV2 = calcV2(bArrCalcNTHash3, this.username.toUpperCase(Locale.US) + this.domain, bArr3, bytes);
            }
        }
        writer.writeSecurityBuffer(12, bArrCalcV2);
        writer.writeSecurityBuffer(20, bArrCalcV2);
        writer.writeSecurityBuffer(52, new byte[0]);
        writer.writeInt(60, i3);
        debug("NTLM Client: Type 3 created\n", new Object[0]);
        debug(writer.getBytes());
        return writer.getBytes();
    }

    public String getDomain() {
        return this.domain;
    }

    public void dispose() {
        Arrays.fill(this.pw1, (byte) 0);
        Arrays.fill(this.pw2, (byte) 0);
    }
}
