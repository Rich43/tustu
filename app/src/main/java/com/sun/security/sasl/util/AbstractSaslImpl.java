package com.sun.security.sasl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import sun.misc.HexDumpEncoder;

/* loaded from: rt.jar:com/sun/security/sasl/util/AbstractSaslImpl.class */
public abstract class AbstractSaslImpl {
    protected boolean completed = false;
    protected boolean privacy = false;
    protected boolean integrity = false;
    protected byte[] qop;
    protected byte allQop;
    protected byte[] strength;
    protected int sendMaxBufSize;
    protected int recvMaxBufSize;
    protected int rawSendSize;
    protected String myClassName;
    protected static final String MAX_SEND_BUF = "javax.security.sasl.sendmaxbuffer";
    protected static final byte NO_PROTECTION = 1;
    protected static final byte INTEGRITY_ONLY_PROTECTION = 2;
    protected static final byte PRIVACY_PROTECTION = 4;
    protected static final byte LOW_STRENGTH = 1;
    protected static final byte MEDIUM_STRENGTH = 2;
    protected static final byte HIGH_STRENGTH = 4;
    private static final String SASL_LOGGER_NAME = "javax.security.sasl";
    protected static final Logger logger = Logger.getLogger(SASL_LOGGER_NAME);
    private static final byte[] DEFAULT_QOP = {1};
    private static final String[] QOP_TOKENS = {"auth-conf", "auth-int", "auth"};
    private static final byte[] QOP_MASKS = {4, 2, 1};
    private static final byte[] DEFAULT_STRENGTH = {4, 2, 1};
    private static final String[] STRENGTH_TOKENS = {"low", "medium", "high"};
    private static final byte[] STRENGTH_MASKS = {1, 2, 4};

    protected AbstractSaslImpl(Map<String, ?> map, String str) throws SaslException {
        this.sendMaxBufSize = 0;
        this.recvMaxBufSize = 65536;
        this.myClassName = str;
        if (map != null) {
            String str2 = (String) map.get(Sasl.QOP);
            this.qop = parseQop(str2);
            logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL01:Preferred qop property: {0}", str2);
            this.allQop = combineMasks(this.qop);
            if (logger.isLoggable(Level.FINE)) {
                logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL02:Preferred qop mask: {0}", new Byte(this.allQop));
                if (this.qop.length > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i2 = 0; i2 < this.qop.length; i2++) {
                        stringBuffer.append(Byte.toString(this.qop[i2]));
                        stringBuffer.append(' ');
                    }
                    logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL03:Preferred qops : {0}", stringBuffer.toString());
                }
            }
            String str3 = (String) map.get(Sasl.STRENGTH);
            this.strength = parseStrength(str3);
            logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL04:Preferred strength property: {0}", str3);
            if (logger.isLoggable(Level.FINE) && this.strength.length > 0) {
                StringBuffer stringBuffer2 = new StringBuffer();
                for (int i3 = 0; i3 < this.strength.length; i3++) {
                    stringBuffer2.append(Byte.toString(this.strength[i3]));
                    stringBuffer2.append(' ');
                }
                logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL05:Cipher strengths: {0}", stringBuffer2.toString());
            }
            String str4 = (String) map.get(Sasl.MAX_BUFFER);
            if (str4 != null) {
                try {
                    logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL06:Max receive buffer size: {0}", str4);
                    this.recvMaxBufSize = Integer.parseInt(str4);
                } catch (NumberFormatException e2) {
                    throw new SaslException("Property must be string representation of integer: javax.security.sasl.maxbuffer");
                }
            }
            String str5 = (String) map.get(MAX_SEND_BUF);
            if (str5 != null) {
                try {
                    logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL07:Max send buffer size: {0}", str5);
                    this.sendMaxBufSize = Integer.parseInt(str5);
                    return;
                } catch (NumberFormatException e3) {
                    throw new SaslException("Property must be string representation of integer: javax.security.sasl.sendmaxbuffer");
                }
            }
            return;
        }
        this.qop = DEFAULT_QOP;
        this.allQop = (byte) 1;
        this.strength = STRENGTH_MASKS;
    }

    public boolean isComplete() {
        return this.completed;
    }

    public Object getNegotiatedProperty(String str) {
        if (!this.completed) {
            throw new IllegalStateException("SASL authentication not completed");
        }
        switch (str) {
            case "javax.security.sasl.qop":
                if (this.privacy) {
                    return "auth-conf";
                }
                if (this.integrity) {
                    return "auth-int";
                }
                return "auth";
            case "javax.security.sasl.maxbuffer":
                return Integer.toString(this.recvMaxBufSize);
            case "javax.security.sasl.rawsendsize":
                return Integer.toString(this.rawSendSize);
            case "javax.security.sasl.sendmaxbuffer":
                return Integer.toString(this.sendMaxBufSize);
            default:
                return null;
        }
    }

    protected static final byte combineMasks(byte[] bArr) {
        byte b2 = 0;
        for (byte b3 : bArr) {
            b2 = (byte) (b2 | b3);
        }
        return b2;
    }

    protected static final byte findPreferredMask(byte b2, byte[] bArr) {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if ((bArr[i2] & b2) != 0) {
                return bArr[i2];
            }
        }
        return (byte) 0;
    }

    private static final byte[] parseQop(String str) throws SaslException {
        return parseQop(str, null, false);
    }

    protected static final byte[] parseQop(String str, String[] strArr, boolean z2) throws SaslException {
        if (str == null) {
            return DEFAULT_QOP;
        }
        return parseProp(Sasl.QOP, str, QOP_TOKENS, QOP_MASKS, strArr, z2);
    }

    private static final byte[] parseStrength(String str) throws SaslException {
        if (str == null) {
            return DEFAULT_STRENGTH;
        }
        return parseProp(Sasl.STRENGTH, str, STRENGTH_TOKENS, STRENGTH_MASKS, null, false);
    }

    private static final byte[] parseProp(String str, String str2, String[] strArr, byte[] bArr, String[] strArr2, boolean z2) throws SaslException {
        StringTokenizer stringTokenizer = new StringTokenizer(str2, ", \t\n");
        byte[] bArr2 = new byte[strArr.length];
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens() && i2 < bArr2.length) {
            String strNextToken = stringTokenizer.nextToken();
            boolean z3 = false;
            for (int i3 = 0; !z3 && i3 < strArr.length; i3++) {
                if (strNextToken.equalsIgnoreCase(strArr[i3])) {
                    z3 = true;
                    int i4 = i2;
                    i2++;
                    bArr2[i4] = bArr[i3];
                    if (strArr2 != null) {
                        strArr2[i3] = strNextToken;
                    }
                }
            }
            if (!z3 && !z2) {
                throw new SaslException("Invalid token in " + str + ": " + str2);
            }
        }
        for (int i5 = i2; i5 < bArr2.length; i5++) {
            bArr2[i5] = 0;
        }
        return bArr2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final void traceOutput(String str, String str2, String str3, byte[] bArr) {
        traceOutput(str, str2, str3, bArr, 0, bArr == null ? 0 : bArr.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final void traceOutput(String str, String str2, String str3, byte[] bArr, int i2, int i3) {
        Level level;
        String string;
        try {
            if (!logger.isLoggable(Level.FINEST)) {
                i3 = Math.min(16, i3);
                level = Level.FINER;
            } else {
                level = Level.FINEST;
            }
            if (bArr != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i3);
                new HexDumpEncoder().encodeBuffer(new ByteArrayInputStream(bArr, i2, i3), byteArrayOutputStream);
                string = byteArrayOutputStream.toString();
            } else {
                string = "NULL";
            }
            logger.logp(level, str, str2, "{0} ( {1} ): {2}", new Object[]{str3, new Integer(i3), string});
        } catch (Exception e2) {
            logger.logp(Level.WARNING, str, str2, "SASLIMPL09:Error generating trace output: {0}", (Throwable) e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final int networkByteOrderToInt(byte[] bArr, int i2, int i3) {
        if (i3 > 4) {
            throw new IllegalArgumentException("Cannot handle more than 4 bytes");
        }
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            i4 = (i4 << 8) | (bArr[i2 + i5] & 255);
        }
        return i4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final void intToNetworkByteOrder(int i2, byte[] bArr, int i3, int i4) {
        if (i4 > 4) {
            throw new IllegalArgumentException("Cannot handle more than 4 bytes");
        }
        for (int i5 = i4 - 1; i5 >= 0; i5--) {
            bArr[i3 + i5] = (byte) (i2 & 255);
            i2 >>>= 8;
        }
    }
}
