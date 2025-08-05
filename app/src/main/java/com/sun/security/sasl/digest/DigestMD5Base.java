package com.sun.security.sasl.digest;

import com.sun.security.sasl.util.AbstractSaslImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import jdk.internal.dynalink.CallSiteDescriptor;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/security/sasl/digest/DigestMD5Base.class */
abstract class DigestMD5Base extends AbstractSaslImpl {
    protected static final int MAX_CHALLENGE_LENGTH = 2048;
    protected static final int MAX_RESPONSE_LENGTH = 4096;
    protected static final int DEFAULT_MAXBUF = 65536;
    protected static final int DES3 = 0;
    protected static final int RC4 = 1;
    protected static final int DES = 2;
    protected static final int RC4_56 = 3;
    protected static final int RC4_40 = 4;
    protected static final byte DES_3_STRENGTH = 4;
    protected static final byte RC4_STRENGTH = 4;
    protected static final byte DES_STRENGTH = 2;
    protected static final byte RC4_56_STRENGTH = 2;
    protected static final byte RC4_40_STRENGTH = 1;
    protected static final byte UNSET = 0;
    private static final String SECURITY_LAYER_MARKER = ":00000000000000000000000000000000";
    protected int step;
    protected CallbackHandler cbh;
    protected SecurityCtx secCtx;
    protected byte[] H_A1;
    protected byte[] nonce;
    protected String negotiatedStrength;
    protected String negotiatedCipher;
    protected String negotiatedQop;
    protected String negotiatedRealm;
    protected boolean useUTF8;
    protected String encoding;
    protected String digestUri;
    protected String authzid;
    private static final int RAW_NONCE_SIZE = 30;
    private static final int ENCODED_NONCE_SIZE = 40;
    private static final String DI_CLASS_NAME = DigestIntegrity.class.getName();
    private static final String DP_CLASS_NAME = DigestPrivacy.class.getName();
    protected static final String[] CIPHER_TOKENS = {"3des", "rc4", "des", "rc4-56", "rc4-40"};
    private static final String[] JCE_CIPHER_NAME = {"DESede/CBC/NoPadding", "RC4", "DES/CBC/NoPadding"};
    protected static final byte[] CIPHER_MASKS = {4, 4, 2, 2, 1};
    protected static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final BigInteger MASK = new BigInteger("7f", 16);

    protected DigestMD5Base(Map<String, ?> map, String str, int i2, String str2, CallbackHandler callbackHandler) throws SaslException {
        super(map, str);
        this.useUTF8 = false;
        this.encoding = "8859_1";
        this.step = i2;
        this.digestUri = str2;
        this.cbh = callbackHandler;
    }

    public String getMechanismName() {
        return "DIGEST-MD5";
    }

    public byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (!this.completed) {
            throw new IllegalStateException("DIGEST-MD5 authentication not completed");
        }
        if (this.secCtx == null) {
            throw new IllegalStateException("Neither integrity nor privacy was negotiated");
        }
        return this.secCtx.unwrap(bArr, i2, i3);
    }

    public byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (!this.completed) {
            throw new IllegalStateException("DIGEST-MD5 authentication not completed");
        }
        if (this.secCtx == null) {
            throw new IllegalStateException("Neither integrity nor privacy was negotiated");
        }
        return this.secCtx.wrap(bArr, i2, i3);
    }

    public void dispose() throws SaslException {
        if (this.secCtx != null) {
            this.secCtx = null;
        }
    }

    @Override // com.sun.security.sasl.util.AbstractSaslImpl
    public Object getNegotiatedProperty(String str) {
        if (this.completed) {
            if (str.equals(Sasl.STRENGTH)) {
                return this.negotiatedStrength;
            }
            if (str.equals(Sasl.BOUND_SERVER_NAME)) {
                return this.digestUri.substring(this.digestUri.indexOf(47) + 1);
            }
            return super.getNegotiatedProperty(str);
        }
        throw new IllegalStateException("DIGEST-MD5 authentication not completed");
    }

    protected static final byte[] generateNonce() {
        byte[] bArr = new byte[30];
        new Random().nextBytes(bArr);
        byte[] bArr2 = new byte[40];
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length; i3 += 3) {
            byte b2 = bArr[i3];
            byte b3 = bArr[i3 + 1];
            byte b4 = bArr[i3 + 2];
            int i4 = i2;
            int i5 = i2 + 1;
            bArr2[i4] = (byte) pem_array[(b2 >>> 2) & 63];
            int i6 = i5 + 1;
            bArr2[i5] = (byte) pem_array[((b2 << 4) & 48) + ((b3 >>> 4) & 15)];
            int i7 = i6 + 1;
            bArr2[i6] = (byte) pem_array[((b3 << 2) & 60) + ((b4 >>> 6) & 3)];
            i2 = i7 + 1;
            bArr2[i7] = (byte) pem_array[b4 & 63];
        }
        return bArr2;
    }

    protected static void writeQuotedStringValue(ByteArrayOutputStream byteArrayOutputStream, byte[] bArr) {
        for (byte b2 : bArr) {
            if (needEscape((char) b2)) {
                byteArrayOutputStream.write(92);
            }
            byteArrayOutputStream.write(b2);
        }
    }

    private static boolean needEscape(String str) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            if (needEscape(str.charAt(i2))) {
                return true;
            }
        }
        return false;
    }

    private static boolean needEscape(char c2) {
        return c2 == '\"' || c2 == '\\' || c2 == 127 || !(c2 < 0 || c2 > 31 || c2 == '\r' || c2 == '\t' || c2 == '\n');
    }

    protected static String quotedStringValue(String str) {
        if (needEscape(str)) {
            int length = str.length();
            char[] cArr = new char[length + length];
            int i2 = 0;
            for (int i3 = 0; i3 < length; i3++) {
                char cCharAt = str.charAt(i3);
                if (needEscape(cCharAt)) {
                    int i4 = i2;
                    i2++;
                    cArr[i4] = '\\';
                }
                int i5 = i2;
                i2++;
                cArr[i5] = cCharAt;
            }
            return new String(cArr, 0, i2);
        }
        return str;
    }

    protected byte[] binaryToHex(byte[] bArr) throws UnsupportedEncodingException {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if ((bArr[i2] & 255) < 16) {
                stringBuffer.append("0" + Integer.toHexString(bArr[i2] & 255));
            } else {
                stringBuffer.append(Integer.toHexString(bArr[i2] & 255));
            }
        }
        return stringBuffer.toString().getBytes(this.encoding);
    }

    protected byte[] stringToByte_8859_1(String str) throws SaslException {
        char[] charArray = str.toCharArray();
        try {
            if (this.useUTF8) {
                for (char c2 : charArray) {
                    if (c2 > 255) {
                        return str.getBytes(InternalZipConstants.CHARSET_UTF8);
                    }
                }
            }
            return str.getBytes("8859_1");
        } catch (UnsupportedEncodingException e2) {
            throw new SaslException("cannot encode string in UTF8 or 8859-1 (Latin-1)", e2);
        }
    }

    protected static byte[] getPlatformCiphers() {
        byte[] bArr = new byte[CIPHER_TOKENS.length];
        for (int i2 = 0; i2 < JCE_CIPHER_NAME.length; i2++) {
            try {
                Cipher.getInstance(JCE_CIPHER_NAME[i2]);
                logger.log(Level.FINE, "DIGEST01:Platform supports {0}", JCE_CIPHER_NAME[i2]);
                int i3 = i2;
                bArr[i3] = (byte) (bArr[i3] | CIPHER_MASKS[i2]);
            } catch (NoSuchAlgorithmException e2) {
            } catch (NoSuchPaddingException e3) {
            }
        }
        if (bArr[1] != 0) {
            bArr[3] = (byte) (bArr[3] | CIPHER_MASKS[3]);
            bArr[4] = (byte) (bArr[4] | CIPHER_MASKS[4]);
        }
        return bArr;
    }

    protected byte[] generateResponseValue(String str, String str2, String str3, String str4, String str5, char[] cArr, byte[] bArr, byte[] bArr2, int i2, byte[] bArr3) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write((str + CallSiteDescriptor.TOKEN_DELIMITER + str2).getBytes(this.encoding));
        if (str3.equals("auth-conf") || str3.equals("auth-int")) {
            logger.log(Level.FINE, "DIGEST04:QOP: {0}", str3);
            byteArrayOutputStream.write(SECURITY_LAYER_MARKER.getBytes(this.encoding));
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST05:A2: {0}", byteArrayOutputStream.toString());
        }
        messageDigest.update(byteArrayOutputStream.toByteArray());
        byte[] bArrBinaryToHex = binaryToHex(messageDigest.digest());
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST06:HEX(H(A2)): {0}", new String(bArrBinaryToHex));
        }
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        byteArrayOutputStream2.write(stringToByte_8859_1(str4));
        byteArrayOutputStream2.write(58);
        byteArrayOutputStream2.write(stringToByte_8859_1(str5));
        byteArrayOutputStream2.write(58);
        byteArrayOutputStream2.write(stringToByte_8859_1(new String(cArr)));
        messageDigest.update(byteArrayOutputStream2.toByteArray());
        byte[] bArrDigest = messageDigest.digest();
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST07:H({0}) = {1}", new Object[]{byteArrayOutputStream2.toString(), new String(binaryToHex(bArrDigest))});
        }
        ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
        byteArrayOutputStream3.write(bArrDigest);
        byteArrayOutputStream3.write(58);
        byteArrayOutputStream3.write(bArr);
        byteArrayOutputStream3.write(58);
        byteArrayOutputStream3.write(bArr2);
        if (bArr3 != null) {
            byteArrayOutputStream3.write(58);
            byteArrayOutputStream3.write(bArr3);
        }
        messageDigest.update(byteArrayOutputStream3.toByteArray());
        byte[] bArrDigest2 = messageDigest.digest();
        this.H_A1 = bArrDigest2;
        byte[] bArrBinaryToHex2 = binaryToHex(bArrDigest2);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST08:H(A1) = {0}", new String(bArrBinaryToHex2));
        }
        ByteArrayOutputStream byteArrayOutputStream4 = new ByteArrayOutputStream();
        byteArrayOutputStream4.write(bArrBinaryToHex2);
        byteArrayOutputStream4.write(58);
        byteArrayOutputStream4.write(bArr);
        byteArrayOutputStream4.write(58);
        byteArrayOutputStream4.write(nonceCountToHex(i2).getBytes(this.encoding));
        byteArrayOutputStream4.write(58);
        byteArrayOutputStream4.write(bArr2);
        byteArrayOutputStream4.write(58);
        byteArrayOutputStream4.write(str3.getBytes(this.encoding));
        byteArrayOutputStream4.write(58);
        byteArrayOutputStream4.write(bArrBinaryToHex);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST09:KD: {0}", byteArrayOutputStream4.toString());
        }
        messageDigest.update(byteArrayOutputStream4.toByteArray());
        byte[] bArrBinaryToHex3 = binaryToHex(messageDigest.digest());
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST10:response-value: {0}", new String(bArrBinaryToHex3));
        }
        return bArrBinaryToHex3;
    }

    protected static String nonceCountToHex(int i2) {
        String hexString = Integer.toHexString(i2);
        StringBuffer stringBuffer = new StringBuffer();
        if (hexString.length() < 8) {
            for (int i3 = 0; i3 < 8 - hexString.length(); i3++) {
                stringBuffer.append("0");
            }
        }
        return stringBuffer.toString() + hexString;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    protected static byte[][] parseDirectives(byte[] bArr, String[] strArr, List<byte[]> list, int i2) throws SaslException {
        ?? r0 = new byte[strArr.length];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10);
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream(10);
        boolean z2 = true;
        boolean z3 = false;
        boolean z4 = false;
        int iSkipLws = skipLws(bArr, 0);
        while (iSkipLws < bArr.length) {
            byte b2 = bArr[iSkipLws];
            if (z2) {
                if (b2 == 44) {
                    if (byteArrayOutputStream.size() != 0) {
                        throw new SaslException("Directive key contains a ',':" + ((Object) byteArrayOutputStream));
                    }
                    iSkipLws = skipLws(bArr, iSkipLws + 1);
                } else if (b2 == 61) {
                    if (byteArrayOutputStream.size() == 0) {
                        throw new SaslException("Empty directive key");
                    }
                    z2 = false;
                    iSkipLws = skipLws(bArr, iSkipLws + 1);
                    if (iSkipLws < bArr.length) {
                        if (bArr[iSkipLws] == 34) {
                            z3 = true;
                            iSkipLws++;
                        }
                    } else {
                        throw new SaslException("Valueless directive found: " + byteArrayOutputStream.toString());
                    }
                } else if (isLws(b2)) {
                    iSkipLws = skipLws(bArr, iSkipLws + 1);
                    if (iSkipLws < bArr.length) {
                        if (bArr[iSkipLws] != 61) {
                            throw new SaslException("'=' expected after key: " + byteArrayOutputStream.toString());
                        }
                    } else {
                        throw new SaslException("'=' expected after key: " + byteArrayOutputStream.toString());
                    }
                } else {
                    byteArrayOutputStream.write(b2);
                    iSkipLws++;
                }
            } else if (z3) {
                if (b2 == 92) {
                    int i3 = iSkipLws + 1;
                    if (i3 < bArr.length) {
                        byteArrayOutputStream2.write(bArr[i3]);
                        iSkipLws = i3 + 1;
                    } else {
                        throw new SaslException("Unmatched quote found for directive: " + byteArrayOutputStream.toString() + " with value: " + byteArrayOutputStream2.toString());
                    }
                } else if (b2 == 34) {
                    iSkipLws++;
                    z3 = false;
                    z4 = true;
                } else {
                    byteArrayOutputStream2.write(b2);
                    iSkipLws++;
                }
            } else if (isLws(b2) || b2 == 44) {
                extractDirective(byteArrayOutputStream.toString(), byteArrayOutputStream2.toByteArray(), strArr, r0, list, i2);
                byteArrayOutputStream.reset();
                byteArrayOutputStream2.reset();
                z2 = true;
                z4 = false;
                z3 = false;
                iSkipLws = skipLws(bArr, iSkipLws + 1);
            } else {
                if (z4) {
                    throw new SaslException("Expecting comma or linear whitespace after quoted string: \"" + byteArrayOutputStream2.toString() + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
                byteArrayOutputStream2.write(b2);
                iSkipLws++;
            }
        }
        if (z3) {
            throw new SaslException("Unmatched quote found for directive: " + byteArrayOutputStream.toString() + " with value: " + byteArrayOutputStream2.toString());
        }
        if (byteArrayOutputStream.size() > 0) {
            extractDirective(byteArrayOutputStream.toString(), byteArrayOutputStream2.toByteArray(), strArr, r0, list, i2);
        }
        return r0;
    }

    private static boolean isLws(byte b2) {
        switch (b2) {
            case 9:
            case 10:
            case 13:
            case 32:
                return true;
            default:
                return false;
        }
    }

    private static int skipLws(byte[] bArr, int i2) {
        int i3 = i2;
        while (i3 < bArr.length) {
            if (isLws(bArr[i3])) {
                i3++;
            } else {
                return i3;
            }
        }
        return i3;
    }

    private static void extractDirective(String str, byte[] bArr, String[] strArr, byte[][] bArr2, List<byte[]> list, int i2) throws SaslException {
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (str.equalsIgnoreCase(strArr[i3])) {
                if (bArr2[i3] == null) {
                    bArr2[i3] = bArr;
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE, "DIGEST11:Directive {0} = {1}", new Object[]{strArr[i3], new String(bArr2[i3])});
                        return;
                    }
                    return;
                }
                if (list != null && i3 == i2) {
                    if (list.isEmpty()) {
                        list.add(bArr2[i3]);
                    }
                    list.add(bArr);
                    return;
                }
                throw new SaslException("DIGEST-MD5: peer sent more than one " + str + " directive: " + new String(bArr));
            }
        }
    }

    /* loaded from: rt.jar:com/sun/security/sasl/digest/DigestMD5Base$DigestIntegrity.class */
    class DigestIntegrity implements SecurityCtx {
        private static final String CLIENT_INT_MAGIC = "Digest session key to client-to-server signing key magic constant";
        private static final String SVR_INT_MAGIC = "Digest session key to server-to-client signing key magic constant";
        protected byte[] myKi;
        protected byte[] peerKi;
        protected int mySeqNum = 0;
        protected int peerSeqNum = 0;
        protected final byte[] messageType = new byte[2];
        protected final byte[] sequenceNum = new byte[4];

        DigestIntegrity(boolean z2) throws SaslException {
            try {
                generateIntegrityKeyPair(z2);
                DigestMD5Base.intToNetworkByteOrder(1, this.messageType, 0, 2);
            } catch (UnsupportedEncodingException e2) {
                throw new SaslException("DIGEST-MD5: Error encoding strings into UTF-8", e2);
            } catch (IOException e3) {
                throw new SaslException("DIGEST-MD5: Error accessing buffers required to create integrity key pairs", e3);
            } catch (NoSuchAlgorithmException e4) {
                throw new SaslException("DIGEST-MD5: Unsupported digest algorithm used to create integrity key pairs", e4);
            }
        }

        private void generateIntegrityKeyPair(boolean z2) throws NoSuchAlgorithmException, IOException {
            byte[] bytes = CLIENT_INT_MAGIC.getBytes(DigestMD5Base.this.encoding);
            byte[] bytes2 = SVR_INT_MAGIC.getBytes(DigestMD5Base.this.encoding);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bArr = new byte[DigestMD5Base.this.H_A1.length + bytes.length];
            System.arraycopy(DigestMD5Base.this.H_A1, 0, bArr, 0, DigestMD5Base.this.H_A1.length);
            System.arraycopy(bytes, 0, bArr, DigestMD5Base.this.H_A1.length, bytes.length);
            messageDigest.update(bArr);
            byte[] bArrDigest = messageDigest.digest();
            System.arraycopy(bytes2, 0, bArr, DigestMD5Base.this.H_A1.length, bytes2.length);
            messageDigest.update(bArr);
            byte[] bArrDigest2 = messageDigest.digest();
            if (DigestMD5Base.logger.isLoggable(Level.FINER)) {
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "generateIntegrityKeyPair", "DIGEST12:Kic: ", bArrDigest);
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "generateIntegrityKeyPair", "DIGEST13:Kis: ", bArrDigest2);
            }
            if (z2) {
                this.myKi = bArrDigest;
                this.peerKi = bArrDigest2;
            } else {
                this.myKi = bArrDigest2;
                this.peerKi = bArrDigest;
            }
        }

        @Override // com.sun.security.sasl.digest.SecurityCtx
        public byte[] wrap(byte[] bArr, int i2, int i3) throws IllegalStateException, SaslException {
            if (i3 == 0) {
                return DigestMD5Base.EMPTY_BYTE_ARRAY;
            }
            byte[] bArr2 = new byte[i3 + 10 + 2 + 4];
            System.arraycopy(bArr, i2, bArr2, 0, i3);
            incrementSeqNum();
            byte[] hmac = getHMAC(this.myKi, this.sequenceNum, bArr, i2, i3);
            if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "wrap", "DIGEST14:outgoing: ", bArr, i2, i3);
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "wrap", "DIGEST15:seqNum: ", this.sequenceNum);
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "wrap", "DIGEST16:MAC: ", hmac);
            }
            System.arraycopy(hmac, 0, bArr2, i3, 10);
            System.arraycopy(this.messageType, 0, bArr2, i3 + 10, 2);
            System.arraycopy(this.sequenceNum, 0, bArr2, i3 + 12, 4);
            if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "wrap", "DIGEST17:wrapped: ", bArr2);
            }
            return bArr2;
        }

        @Override // com.sun.security.sasl.digest.SecurityCtx
        public byte[] unwrap(byte[] bArr, int i2, int i3) throws IllegalStateException, SaslException {
            if (i3 == 0) {
                return DigestMD5Base.EMPTY_BYTE_ARRAY;
            }
            byte[] bArr2 = new byte[10];
            byte[] bArr3 = new byte[i3 - 16];
            byte[] bArr4 = new byte[2];
            byte[] bArr5 = new byte[4];
            System.arraycopy(bArr, i2, bArr3, 0, bArr3.length);
            System.arraycopy(bArr, i2 + bArr3.length, bArr2, 0, 10);
            System.arraycopy(bArr, i2 + bArr3.length + 10, bArr4, 0, 2);
            System.arraycopy(bArr, i2 + bArr3.length + 12, bArr5, 0, 4);
            byte[] hmac = getHMAC(this.peerKi, bArr5, bArr3, 0, bArr3.length);
            if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST18:incoming: ", bArr3);
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST19:MAC: ", bArr2);
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST20:messageType: ", bArr4);
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST21:sequenceNum: ", bArr5);
                DigestMD5Base.traceOutput(DigestMD5Base.DI_CLASS_NAME, "unwrap", "DIGEST22:expectedMAC: ", hmac);
            }
            if (!Arrays.equals(bArr2, hmac)) {
                DigestMD5Base.logger.log(Level.INFO, "DIGEST23:Unmatched MACs");
                return DigestMD5Base.EMPTY_BYTE_ARRAY;
            }
            if (this.peerSeqNum != DigestMD5Base.networkByteOrderToInt(bArr5, 0, 4)) {
                throw new SaslException("DIGEST-MD5: Out of order sequencing of messages from server. Got: " + DigestMD5Base.networkByteOrderToInt(bArr5, 0, 4) + " Expected: " + this.peerSeqNum);
            }
            if (!Arrays.equals(this.messageType, bArr4)) {
                throw new SaslException("DIGEST-MD5: invalid message type: " + DigestMD5Base.networkByteOrderToInt(bArr4, 0, 2));
            }
            this.peerSeqNum++;
            return bArr3;
        }

        protected byte[] getHMAC(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, int i3) throws IllegalStateException, SaslException {
            byte[] bArr4 = new byte[4 + i3];
            System.arraycopy(bArr2, 0, bArr4, 0, 4);
            System.arraycopy(bArr3, i2, bArr4, 4, i3);
            try {
                SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "HmacMD5");
                Mac mac = Mac.getInstance("HmacMD5");
                mac.init(secretKeySpec);
                mac.update(bArr4);
                byte[] bArrDoFinal = mac.doFinal();
                byte[] bArr5 = new byte[10];
                System.arraycopy(bArrDoFinal, 0, bArr5, 0, 10);
                return bArr5;
            } catch (InvalidKeyException e2) {
                throw new SaslException("DIGEST-MD5: Invalid bytes used for key of HMAC-MD5 hash.", e2);
            } catch (NoSuchAlgorithmException e3) {
                throw new SaslException("DIGEST-MD5: Error creating instance of MD5 digest algorithm", e3);
            }
        }

        protected void incrementSeqNum() {
            int i2 = this.mySeqNum;
            this.mySeqNum = i2 + 1;
            DigestMD5Base.intToNetworkByteOrder(i2, this.sequenceNum, 0, 4);
        }
    }

    /* loaded from: rt.jar:com/sun/security/sasl/digest/DigestMD5Base$DigestPrivacy.class */
    final class DigestPrivacy extends DigestIntegrity implements SecurityCtx {
        private static final String CLIENT_CONF_MAGIC = "Digest H(A1) to client-to-server sealing key magic constant";
        private static final String SVR_CONF_MAGIC = "Digest H(A1) to server-to-client sealing key magic constant";
        private Cipher encCipher;
        private Cipher decCipher;

        DigestPrivacy(boolean z2) throws SaslException {
            super(z2);
            try {
                generatePrivacyKeyPair(z2);
            } catch (UnsupportedEncodingException e2) {
                throw new SaslException("DIGEST-MD5: Error encoding string value into UTF-8", e2);
            } catch (SaslException e3) {
                throw e3;
            } catch (IOException e4) {
                throw new SaslException("DIGEST-MD5: Error accessing buffers required to generate cipher keys", e4);
            } catch (NoSuchAlgorithmException e5) {
                throw new SaslException("DIGEST-MD5: Error creating instance of required cipher or digest", e5);
            }
        }

        private void generatePrivacyKeyPair(boolean z2) throws NoSuchAlgorithmException, IOException {
            int i2;
            byte[] bArr;
            byte[] bArr2;
            String str;
            String str2;
            byte[] bytes = CLIENT_CONF_MAGIC.getBytes(DigestMD5Base.this.encoding);
            byte[] bytes2 = SVR_CONF_MAGIC.getBytes(DigestMD5Base.this.encoding);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            if (DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[4])) {
                i2 = 5;
            } else if (DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[3])) {
                i2 = 7;
            } else {
                i2 = 16;
            }
            byte[] bArr3 = new byte[i2 + bytes.length];
            System.arraycopy(DigestMD5Base.this.H_A1, 0, bArr3, 0, i2);
            System.arraycopy(bytes, 0, bArr3, i2, bytes.length);
            messageDigest.update(bArr3);
            byte[] bArrDigest = messageDigest.digest();
            System.arraycopy(bytes2, 0, bArr3, i2, bytes2.length);
            messageDigest.update(bArr3);
            byte[] bArrDigest2 = messageDigest.digest();
            if (DigestMD5Base.logger.isLoggable(Level.FINER)) {
                DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST24:Kcc: ", bArrDigest);
                DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST25:Kcs: ", bArrDigest2);
            }
            if (z2) {
                bArr = bArrDigest;
                bArr2 = bArrDigest2;
            } else {
                bArr = bArrDigest2;
                bArr2 = bArrDigest;
            }
            try {
                if (DigestMD5Base.this.negotiatedCipher.indexOf(DigestMD5Base.CIPHER_TOKENS[1]) > -1) {
                    this.encCipher = Cipher.getInstance("RC4");
                    this.decCipher = Cipher.getInstance("RC4");
                    SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "RC4");
                    SecretKeySpec secretKeySpec2 = new SecretKeySpec(bArr2, "RC4");
                    this.encCipher.init(1, secretKeySpec);
                    this.decCipher.init(2, secretKeySpec2);
                } else if (DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[2]) || DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[0])) {
                    if (DigestMD5Base.this.negotiatedCipher.equals(DigestMD5Base.CIPHER_TOKENS[2])) {
                        str = "DES/CBC/NoPadding";
                        str2 = "des";
                    } else {
                        str = "DESede/CBC/NoPadding";
                        str2 = "desede";
                    }
                    this.encCipher = Cipher.getInstance(str);
                    this.decCipher = Cipher.getInstance(str);
                    SecretKey secretKeyMakeDesKeys = DigestMD5Base.makeDesKeys(bArr, str2);
                    SecretKey secretKeyMakeDesKeys2 = DigestMD5Base.makeDesKeys(bArr2, str2);
                    IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr, 8, 8);
                    IvParameterSpec ivParameterSpec2 = new IvParameterSpec(bArr2, 8, 8);
                    this.encCipher.init(1, secretKeyMakeDesKeys, ivParameterSpec);
                    this.decCipher.init(2, secretKeyMakeDesKeys2, ivParameterSpec2);
                    if (DigestMD5Base.logger.isLoggable(Level.FINER)) {
                        DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST26:" + DigestMD5Base.this.negotiatedCipher + " IVcc: ", ivParameterSpec.getIV());
                        DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST27:" + DigestMD5Base.this.negotiatedCipher + " IVcs: ", ivParameterSpec2.getIV());
                        DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST28:" + DigestMD5Base.this.negotiatedCipher + " encryption key: ", secretKeyMakeDesKeys.getEncoded());
                        DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "generatePrivacyKeyPair", "DIGEST29:" + DigestMD5Base.this.negotiatedCipher + " decryption key: ", secretKeyMakeDesKeys2.getEncoded());
                    }
                }
            } catch (InvalidAlgorithmParameterException e2) {
                throw new SaslException("DIGEST-MD5: Invalid cipher algorithem parameter used to create cipher instance", e2);
            } catch (InvalidKeyException e3) {
                throw new SaslException("DIGEST-MD5: Invalid data used to initialize keys", e3);
            } catch (InvalidKeySpecException e4) {
                throw new SaslException("DIGEST-MD5: Unsupported key specification used.", e4);
            } catch (NoSuchPaddingException e5) {
                throw new SaslException("DIGEST-MD5: Unsupported padding used for chosen cipher", e5);
            }
        }

        @Override // com.sun.security.sasl.digest.DigestMD5Base.DigestIntegrity, com.sun.security.sasl.digest.SecurityCtx
        public byte[] wrap(byte[] bArr, int i2, int i3) throws IllegalBlockSizeException, SaslException {
            byte[] bArr2;
            if (i3 == 0) {
                return DigestMD5Base.EMPTY_BYTE_ARRAY;
            }
            incrementSeqNum();
            byte[] hmac = getHMAC(this.myKi, this.sequenceNum, bArr, i2, i3);
            if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "DIGEST30:Outgoing: ", bArr, i2, i3);
                DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "seqNum: ", this.sequenceNum);
                DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "MAC: ", hmac);
            }
            int blockSize = this.encCipher.getBlockSize();
            if (blockSize > 1) {
                int i4 = blockSize - ((i3 + 10) % blockSize);
                bArr2 = new byte[i4];
                for (int i5 = 0; i5 < i4; i5++) {
                    bArr2[i5] = (byte) i4;
                }
            } else {
                bArr2 = DigestMD5Base.EMPTY_BYTE_ARRAY;
            }
            byte[] bArr3 = new byte[i3 + bArr2.length + 10];
            System.arraycopy(bArr, i2, bArr3, 0, i3);
            System.arraycopy(bArr2, 0, bArr3, i3, bArr2.length);
            System.arraycopy(hmac, 0, bArr3, i3 + bArr2.length, 10);
            if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "DIGEST31:{msg, pad, KicMAC}: ", bArr3);
            }
            try {
                byte[] bArrUpdate = this.encCipher.update(bArr3);
                if (bArrUpdate == null) {
                    throw new IllegalBlockSizeException("" + bArr3.length);
                }
                byte[] bArr4 = new byte[bArrUpdate.length + 2 + 4];
                System.arraycopy(bArrUpdate, 0, bArr4, 0, bArrUpdate.length);
                System.arraycopy(this.messageType, 0, bArr4, bArrUpdate.length, 2);
                System.arraycopy(this.sequenceNum, 0, bArr4, bArrUpdate.length + 2, 4);
                if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                    DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "wrap", "DIGEST32:Wrapped: ", bArr4);
                }
                return bArr4;
            } catch (IllegalBlockSizeException e2) {
                throw new SaslException("DIGEST-MD5: Invalid block size for cipher", e2);
            }
        }

        @Override // com.sun.security.sasl.digest.DigestMD5Base.DigestIntegrity, com.sun.security.sasl.digest.SecurityCtx
        public byte[] unwrap(byte[] bArr, int i2, int i3) throws IllegalBlockSizeException, SaslException {
            if (i3 == 0) {
                return DigestMD5Base.EMPTY_BYTE_ARRAY;
            }
            byte[] bArr2 = new byte[i3 - 6];
            byte[] bArr3 = new byte[2];
            byte[] bArr4 = new byte[4];
            System.arraycopy(bArr, i2, bArr2, 0, bArr2.length);
            System.arraycopy(bArr, i2 + bArr2.length, bArr3, 0, 2);
            System.arraycopy(bArr, i2 + bArr2.length + 2, bArr4, 0, 4);
            if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                DigestMD5Base.logger.log(Level.FINEST, "DIGEST33:Expecting sequence num: {0}", new Integer(this.peerSeqNum));
                DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST34:incoming: ", bArr2);
            }
            try {
                byte[] bArrUpdate = this.decCipher.update(bArr2);
                if (bArrUpdate == null) {
                    throw new IllegalBlockSizeException("" + bArr2.length);
                }
                byte[] bArr5 = new byte[bArrUpdate.length - 10];
                byte[] bArr6 = new byte[10];
                System.arraycopy(bArrUpdate, 0, bArr5, 0, bArr5.length);
                System.arraycopy(bArrUpdate, bArr5.length, bArr6, 0, 10);
                if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                    DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST35:Unwrapped (w/padding): ", bArr5);
                    DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST36:MAC: ", bArr6);
                    DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST37:messageType: ", bArr3);
                    DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST38:sequenceNum: ", bArr4);
                }
                int length = bArr5.length;
                if (this.decCipher.getBlockSize() > 1) {
                    length -= bArr5[bArr5.length - 1];
                    if (length < 0) {
                        if (DigestMD5Base.logger.isLoggable(Level.INFO)) {
                            DigestMD5Base.logger.log(Level.INFO, "DIGEST39:Incorrect padding: {0}", new Byte(bArr5[bArr5.length - 1]));
                        }
                        return DigestMD5Base.EMPTY_BYTE_ARRAY;
                    }
                }
                byte[] hmac = getHMAC(this.peerKi, bArr4, bArr5, 0, length);
                if (DigestMD5Base.logger.isLoggable(Level.FINEST)) {
                    DigestMD5Base.traceOutput(DigestMD5Base.DP_CLASS_NAME, "unwrap", "DIGEST40:KisMAC: ", hmac);
                }
                if (!Arrays.equals(bArr6, hmac)) {
                    DigestMD5Base.logger.log(Level.INFO, "DIGEST41:Unmatched MACs");
                    return DigestMD5Base.EMPTY_BYTE_ARRAY;
                }
                if (this.peerSeqNum != DigestMD5Base.networkByteOrderToInt(bArr4, 0, 4)) {
                    throw new SaslException("DIGEST-MD5: Out of order sequencing of messages from server. Got: " + DigestMD5Base.networkByteOrderToInt(bArr4, 0, 4) + " Expected: " + this.peerSeqNum);
                }
                if (!Arrays.equals(this.messageType, bArr3)) {
                    throw new SaslException("DIGEST-MD5: invalid message type: " + DigestMD5Base.networkByteOrderToInt(bArr3, 0, 2));
                }
                this.peerSeqNum++;
                if (length == bArr5.length) {
                    return bArr5;
                }
                byte[] bArr7 = new byte[length];
                System.arraycopy(bArr5, 0, bArr7, 0, length);
                return bArr7;
            } catch (IllegalBlockSizeException e2) {
                throw new SaslException("DIGEST-MD5: Illegal block sizes used with chosen cipher", e2);
            }
        }
    }

    private static void setParityBit(byte[] bArr) {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            int i3 = bArr[i2] & 254;
            bArr[i2] = (byte) (i3 | ((Integer.bitCount(i3) & 1) ^ 1));
        }
    }

    private static byte[] addDesParity(byte[] bArr, int i2, int i3) {
        if (i3 != 7) {
            throw new IllegalArgumentException("Invalid length of DES Key Value:" + i3);
        }
        byte[] bArr2 = new byte[7];
        System.arraycopy(bArr, i2, bArr2, 0, i3);
        byte[] bArr3 = new byte[8];
        BigInteger bigInteger = new BigInteger(bArr2);
        for (int length = bArr3.length - 1; length >= 0; length--) {
            bArr3[length] = bigInteger.and(MASK).toByteArray()[0];
            int i4 = length;
            bArr3[i4] = (byte) (bArr3[i4] << 1);
            bigInteger = bigInteger.shiftRight(7);
        }
        setParityBit(bArr3);
        return bArr3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SecretKey makeDesKeys(byte[] bArr, String str) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] bArrAddDesParity;
        KeySpec dESedeKeySpec;
        bArrAddDesParity = addDesParity(bArr, 0, 7);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(str);
        switch (str) {
            case "des":
                dESedeKeySpec = new DESKeySpec(bArrAddDesParity, 0);
                if (logger.isLoggable(Level.FINEST)) {
                    traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST42:DES key input: ", bArr);
                    traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST43:DES key parity-adjusted: ", bArrAddDesParity);
                    traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST44:DES key material: ", ((DESKeySpec) dESedeKeySpec).getKey());
                    logger.log(Level.FINEST, "DIGEST45: is parity-adjusted? {0}", Boolean.valueOf(DESKeySpec.isParityAdjusted(bArrAddDesParity, 0)));
                    break;
                }
                break;
            case "desede":
                byte[] bArrAddDesParity2 = addDesParity(bArr, 7, 7);
                byte[] bArr2 = new byte[(bArrAddDesParity.length * 2) + bArrAddDesParity2.length];
                System.arraycopy(bArrAddDesParity, 0, bArr2, 0, bArrAddDesParity.length);
                System.arraycopy(bArrAddDesParity2, 0, bArr2, bArrAddDesParity.length, bArrAddDesParity2.length);
                System.arraycopy(bArrAddDesParity, 0, bArr2, bArrAddDesParity.length + bArrAddDesParity2.length, bArrAddDesParity.length);
                dESedeKeySpec = new DESedeKeySpec(bArr2, 0);
                if (logger.isLoggable(Level.FINEST)) {
                    traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST46:3DES key input: ", bArr);
                    traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST47:3DES key ede: ", bArr2);
                    traceOutput(DP_CLASS_NAME, "makeDesKeys", "DIGEST48:3DES key material: ", ((DESedeKeySpec) dESedeKeySpec).getKey());
                    logger.log(Level.FINEST, "DIGEST49: is parity-adjusted? ", Boolean.valueOf(DESedeKeySpec.isParityAdjusted(bArr2, 0)));
                    break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid DES strength:" + str);
        }
        return secretKeyFactory.generateSecret(dESedeKeySpec);
    }
}
