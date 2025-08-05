package com.sun.security.ntlm;

import com.sun.security.ntlm.NTLM;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/security/ntlm/Server.class */
public abstract class Server extends NTLM {
    private final String domain;
    private final boolean allVersion;

    public abstract char[] getPassword(String str, String str2);

    @Override // com.sun.security.ntlm.NTLM
    public /* bridge */ /* synthetic */ void debug(byte[] bArr) {
        super.debug(bArr);
    }

    @Override // com.sun.security.ntlm.NTLM
    public /* bridge */ /* synthetic */ void debug(String str, Object[] objArr) {
        super.debug(str, objArr);
    }

    public Server(String str, String str2) throws NTLMException {
        super(str);
        if (str2 == null) {
            throw new NTLMException(6, "domain cannot be null");
        }
        this.allVersion = str == null;
        this.domain = str2;
        debug("NTLM Server: (t,version) = (%s,%s)\n", new Object[]{str2, str});
    }

    public byte[] type2(byte[] bArr, byte[] bArr2) throws NTLMException {
        if (bArr2 == null || bArr2.length != 8) {
            throw new NTLMException(6, "nonce must be 8-byte long");
        }
        debug("NTLM Server: Type 1 received\n", new Object[0]);
        if (bArr != null) {
            debug(bArr);
        }
        NTLM.Writer writer = new NTLM.Writer(2, 32);
        writer.writeSecurityBuffer(12, this.domain, true);
        writer.writeInt(20, 590341);
        writer.writeBytes(24, bArr2);
        debug("NTLM Server: Type 2 created\n", new Object[0]);
        debug(writer.getBytes());
        return writer.getBytes();
    }

    public String[] verify(byte[] bArr, byte[] bArr2) throws NTLMException {
        if (bArr == null || bArr2 == null) {
            throw new NTLMException(6, "type1 or nonce cannot be null");
        }
        debug("NTLM Server: Type 3 received\n", new Object[0]);
        if (bArr != null) {
            debug(bArr);
        }
        NTLM.Reader reader = new NTLM.Reader(bArr);
        String securityBuffer = reader.readSecurityBuffer(36, true);
        String securityBuffer2 = reader.readSecurityBuffer(44, true);
        String securityBuffer3 = reader.readSecurityBuffer(28, true);
        boolean z2 = false;
        char[] password = getPassword(securityBuffer3, securityBuffer);
        if (password == null) {
            throw new NTLMException(3, "Unknown user");
        }
        byte[] securityBuffer4 = reader.readSecurityBuffer(12);
        byte[] securityBuffer5 = reader.readSecurityBuffer(20);
        if (0 == 0 && (this.allVersion || this.f12049v == Version.NTLM)) {
            if (securityBuffer4.length > 0 && Arrays.equals(calcResponse(calcLMHash(getP1(password)), bArr2), securityBuffer4)) {
                z2 = true;
            }
            if (securityBuffer5.length > 0 && Arrays.equals(calcResponse(calcNTHash(getP2(password)), bArr2), securityBuffer5)) {
                z2 = true;
            }
            debug("NTLM Server: verify using NTLM: " + z2 + "\n", new Object[0]);
        }
        if (!z2 && (this.allVersion || this.f12049v == Version.NTLM2)) {
            if (Arrays.equals(securityBuffer5, ntlm2NTLM(calcNTHash(getP2(password)), Arrays.copyOf(securityBuffer4, 8), bArr2))) {
                z2 = true;
            }
            debug("NTLM Server: verify using NTLM2: " + z2 + "\n", new Object[0]);
        }
        if (!z2 && (this.allVersion || this.f12049v == Version.NTLMv2)) {
            byte[] bArrCalcNTHash = calcNTHash(getP2(password));
            if (securityBuffer4.length > 0 && Arrays.equals(calcV2(bArrCalcNTHash, securityBuffer.toUpperCase(Locale.US) + securityBuffer3, Arrays.copyOfRange(securityBuffer4, 16, securityBuffer4.length), bArr2), securityBuffer4)) {
                z2 = true;
            }
            if (securityBuffer5.length > 0 && Arrays.equals(calcV2(bArrCalcNTHash, securityBuffer.toUpperCase(Locale.US) + securityBuffer3, Arrays.copyOfRange(securityBuffer5, 16, securityBuffer5.length), bArr2), securityBuffer5)) {
                z2 = true;
            }
            debug("NTLM Server: verify using NTLMv2: " + z2 + "\n", new Object[0]);
        }
        if (!z2) {
            throw new NTLMException(4, "None of LM and NTLM verified");
        }
        return new String[]{securityBuffer, securityBuffer2, securityBuffer3};
    }
}
