package sun.security.jgss.krb5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import sun.security.jgss.GSSHeader;
import sun.security.jgss.GSSToken;

/* loaded from: rt.jar:sun/security/jgss/krb5/MessageToken.class */
abstract class MessageToken extends Krb5Token {
    private static final int TOKEN_NO_CKSUM_SIZE = 16;
    private static final int FILLER = 65535;
    static final int SGN_ALG_DES_MAC_MD5 = 0;
    static final int SGN_ALG_DES_MAC = 512;
    static final int SGN_ALG_HMAC_SHA1_DES3_KD = 1024;
    static final int SEAL_ALG_NONE = 65535;
    static final int SEAL_ALG_DES = 0;
    static final int SEAL_ALG_DES3_KD = 512;
    static final int SEAL_ALG_ARCFOUR_HMAC = 4096;
    static final int SGN_ALG_HMAC_MD5_ARCFOUR = 4352;
    private static final int TOKEN_ID_POS = 0;
    private static final int SIGN_ALG_POS = 2;
    private static final int SEAL_ALG_POS = 4;
    private int seqNumber;
    private boolean confState;
    private boolean initiator;
    private int tokenId;
    private GSSHeader gssHeader;
    private MessageTokenHeader tokenHeader;
    private byte[] checksum;
    private byte[] encSeqNumber;
    private byte[] seqNumberData;
    CipherHelper cipherHelper;

    protected abstract int getSealAlg(boolean z2, int i2) throws GSSException;

    MessageToken(int i2, Krb5Context krb5Context, byte[] bArr, int i3, int i4, MessageProp messageProp) throws GSSException {
        this(i2, krb5Context, new ByteArrayInputStream(bArr, i3, i4), messageProp);
    }

    MessageToken(int i2, Krb5Context krb5Context, InputStream inputStream, MessageProp messageProp) throws GSSException {
        this.confState = true;
        this.initiator = true;
        this.tokenId = 0;
        this.gssHeader = null;
        this.tokenHeader = null;
        this.checksum = null;
        this.encSeqNumber = null;
        this.seqNumberData = null;
        this.cipherHelper = null;
        init(i2, krb5Context);
        try {
            this.gssHeader = new GSSHeader(inputStream);
            if (!this.gssHeader.getOid().equals((Object) OID)) {
                throw new GSSException(10, -1, getTokenName(i2));
            }
            if (!this.confState) {
                messageProp.setPrivacy(false);
            }
            this.tokenHeader = new MessageTokenHeader(inputStream, messageProp);
            this.encSeqNumber = new byte[8];
            readFully(inputStream, this.encSeqNumber);
            this.checksum = new byte[this.cipherHelper.getChecksumLength()];
            readFully(inputStream, this.checksum);
        } catch (IOException e2) {
            throw new GSSException(10, -1, getTokenName(i2) + CallSiteDescriptor.TOKEN_DELIMITER + e2.getMessage());
        }
    }

    public final GSSHeader getGSSHeader() {
        return this.gssHeader;
    }

    public final int getTokenId() {
        return this.tokenId;
    }

    public final byte[] getEncSeqNumber() {
        return this.encSeqNumber;
    }

    public final byte[] getChecksum() {
        return this.checksum;
    }

    public final boolean getConfState() {
        return this.confState;
    }

    public void genSignAndSeqNumber(MessageProp messageProp, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws GSSException {
        int qop = messageProp.getQOP();
        if (qop != 0) {
            qop = 0;
            messageProp.setQOP(0);
        }
        if (!this.confState) {
            messageProp.setPrivacy(false);
        }
        this.tokenHeader = new MessageTokenHeader(this.tokenId, messageProp.getPrivacy(), qop);
        this.checksum = getChecksum(bArr, bArr2, i2, i3, bArr3);
        this.seqNumberData = new byte[8];
        if (this.cipherHelper.isArcFour()) {
            writeBigEndian(this.seqNumber, this.seqNumberData);
        } else {
            writeLittleEndian(this.seqNumber, this.seqNumberData);
        }
        if (!this.initiator) {
            this.seqNumberData[4] = -1;
            this.seqNumberData[5] = -1;
            this.seqNumberData[6] = -1;
            this.seqNumberData[7] = -1;
        }
        this.encSeqNumber = this.cipherHelper.encryptSeq(this.checksum, this.seqNumberData, 0, 8);
    }

    public final boolean verifySignAndSeqNumber(byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws GSSException {
        if (MessageDigest.isEqual(this.checksum, getChecksum(bArr, bArr2, i2, i3, bArr3))) {
            this.seqNumberData = this.cipherHelper.decryptSeq(this.checksum, this.encSeqNumber, 0, 8);
            byte b2 = 0;
            if (this.initiator) {
                b2 = -1;
            }
            if (this.seqNumberData[4] == b2 && this.seqNumberData[5] == b2 && this.seqNumberData[6] == b2 && this.seqNumberData[7] == b2) {
                return true;
            }
            return false;
        }
        return false;
    }

    public final int getSequenceNumber() {
        int littleEndian;
        if (this.cipherHelper.isArcFour()) {
            littleEndian = readBigEndian(this.seqNumberData, 0, 4);
        } else {
            littleEndian = readLittleEndian(this.seqNumberData, 0, 4);
        }
        return littleEndian;
    }

    private byte[] getChecksum(byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws GSSException {
        byte[] bytes = this.tokenHeader.getBytes();
        byte[] bArr4 = bytes;
        if (bArr != null) {
            bArr4 = new byte[bytes.length + bArr.length];
            System.arraycopy(bytes, 0, bArr4, 0, bytes.length);
            System.arraycopy(bArr, 0, bArr4, bytes.length, bArr.length);
        }
        return this.cipherHelper.calculateChecksum(this.tokenHeader.getSignAlg(), bArr4, bArr3, bArr2, i2, i3, this.tokenId);
    }

    MessageToken(int i2, Krb5Context krb5Context) throws GSSException {
        this.confState = true;
        this.initiator = true;
        this.tokenId = 0;
        this.gssHeader = null;
        this.tokenHeader = null;
        this.checksum = null;
        this.encSeqNumber = null;
        this.seqNumberData = null;
        this.cipherHelper = null;
        init(i2, krb5Context);
        this.seqNumber = krb5Context.incrementMySequenceNumber();
    }

    private void init(int i2, Krb5Context krb5Context) throws GSSException {
        this.tokenId = i2;
        this.confState = krb5Context.getConfState();
        this.initiator = krb5Context.isInitiator();
        this.cipherHelper = krb5Context.getCipherHelper(null);
    }

    public void encode(OutputStream outputStream) throws IOException, GSSException {
        this.gssHeader = new GSSHeader(OID, getKrb5TokenSize());
        this.gssHeader.encode(outputStream);
        this.tokenHeader.encode(outputStream);
        outputStream.write(this.encSeqNumber);
        outputStream.write(this.checksum);
    }

    protected int getKrb5TokenSize() throws GSSException {
        return getTokenSize();
    }

    protected final int getTokenSize() throws GSSException {
        return 16 + this.cipherHelper.getChecksumLength();
    }

    protected static final int getTokenSize(CipherHelper cipherHelper) throws GSSException {
        return 16 + cipherHelper.getChecksumLength();
    }

    /* loaded from: rt.jar:sun/security/jgss/krb5/MessageToken$MessageTokenHeader.class */
    class MessageTokenHeader {
        private int tokenId;
        private int signAlg;
        private int sealAlg;
        private byte[] bytes;

        public MessageTokenHeader(int i2, boolean z2, int i3) throws GSSException {
            this.bytes = new byte[8];
            this.tokenId = i2;
            this.signAlg = MessageToken.this.getSgnAlg(i3);
            this.sealAlg = MessageToken.this.getSealAlg(z2, i3);
            this.bytes[0] = (byte) (i2 >>> 8);
            this.bytes[1] = (byte) i2;
            this.bytes[2] = (byte) (this.signAlg >>> 8);
            this.bytes[3] = (byte) this.signAlg;
            this.bytes[4] = (byte) (this.sealAlg >>> 8);
            this.bytes[5] = (byte) this.sealAlg;
            this.bytes[6] = -1;
            this.bytes[7] = -1;
        }

        public MessageTokenHeader(InputStream inputStream, MessageProp messageProp) throws IOException {
            this.bytes = new byte[8];
            GSSToken.readFully(inputStream, this.bytes);
            this.tokenId = GSSToken.readInt(this.bytes, 0);
            this.signAlg = GSSToken.readInt(this.bytes, 2);
            this.sealAlg = GSSToken.readInt(this.bytes, 4);
            GSSToken.readInt(this.bytes, 6);
            switch (this.sealAlg) {
                case 0:
                case 512:
                case 4096:
                    messageProp.setPrivacy(true);
                    break;
                default:
                    messageProp.setPrivacy(false);
                    break;
            }
            messageProp.setQOP(0);
        }

        public final void encode(OutputStream outputStream) throws IOException {
            outputStream.write(this.bytes);
        }

        public final int getTokenId() {
            return this.tokenId;
        }

        public final int getSignAlg() {
            return this.signAlg;
        }

        public final int getSealAlg() {
            return this.sealAlg;
        }

        public final byte[] getBytes() {
            return this.bytes;
        }
    }

    protected int getSgnAlg(int i2) throws GSSException {
        return this.cipherHelper.getSgnAlg();
    }
}
