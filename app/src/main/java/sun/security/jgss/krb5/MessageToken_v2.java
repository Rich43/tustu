package sun.security.jgss.krb5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import sun.security.jgss.GSSToken;

/* loaded from: rt.jar:sun/security/jgss/krb5/MessageToken_v2.class */
abstract class MessageToken_v2 extends Krb5Token {
    protected static final int TOKEN_HEADER_SIZE = 16;
    private static final int TOKEN_ID_POS = 0;
    private static final int TOKEN_FLAG_POS = 2;
    private static final int TOKEN_EC_POS = 4;
    private static final int TOKEN_RRC_POS = 6;
    protected static final int CONFOUNDER_SIZE = 16;
    static final int KG_USAGE_ACCEPTOR_SEAL = 22;
    static final int KG_USAGE_ACCEPTOR_SIGN = 23;
    static final int KG_USAGE_INITIATOR_SEAL = 24;
    static final int KG_USAGE_INITIATOR_SIGN = 25;
    private static final int FLAG_SENDER_IS_ACCEPTOR = 1;
    private static final int FLAG_WRAP_CONFIDENTIAL = 2;
    private static final int FLAG_ACCEPTOR_SUBKEY = 4;
    private static final int FILLER = 255;
    private MessageTokenHeader tokenHeader;
    private int tokenId;
    private int seqNumber;
    protected byte[] tokenData;
    protected int tokenDataLen;
    private int key_usage;
    private int ec;
    private int rrc;
    byte[] checksum;
    private boolean confState;
    private boolean initiator;
    private boolean have_acceptor_subkey;
    CipherHelper cipherHelper;

    public abstract void encode(OutputStream outputStream) throws IOException;

    MessageToken_v2(int i2, Krb5Context krb5Context, byte[] bArr, int i3, int i4, MessageProp messageProp) throws GSSException {
        this(i2, krb5Context, new ByteArrayInputStream(bArr, i3, i4), messageProp);
    }

    MessageToken_v2(int i2, Krb5Context krb5Context, InputStream inputStream, MessageProp messageProp) throws GSSException {
        int checksumLength;
        this.tokenHeader = null;
        this.tokenId = 0;
        this.key_usage = 0;
        this.ec = 0;
        this.rrc = 0;
        this.checksum = null;
        this.confState = true;
        this.initiator = true;
        this.have_acceptor_subkey = false;
        this.cipherHelper = null;
        init(i2, krb5Context);
        try {
            if (!this.confState) {
                messageProp.setPrivacy(false);
            }
            this.tokenHeader = new MessageTokenHeader(inputStream, messageProp, i2);
            if (i2 == 1284) {
                this.key_usage = !this.initiator ? 24 : 22;
            } else if (i2 == 1028) {
                this.key_usage = !this.initiator ? 25 : 23;
            }
            if (i2 == 1284 && messageProp.getPrivacy()) {
                checksumLength = 32 + this.cipherHelper.getChecksumLength();
            } else {
                checksumLength = this.cipherHelper.getChecksumLength();
            }
            if (i2 == 1028) {
                this.tokenDataLen = checksumLength;
                this.tokenData = new byte[checksumLength];
                readFully(inputStream, this.tokenData);
            } else {
                this.tokenDataLen = inputStream.available();
                if (this.tokenDataLen >= checksumLength) {
                    this.tokenData = new byte[this.tokenDataLen];
                    readFully(inputStream, this.tokenData);
                } else {
                    byte[] bArr = new byte[checksumLength];
                    readFully(inputStream, bArr);
                    int iAvailable = inputStream.available();
                    this.tokenDataLen = checksumLength + iAvailable;
                    this.tokenData = Arrays.copyOf(bArr, this.tokenDataLen);
                    readFully(inputStream, this.tokenData, checksumLength, iAvailable);
                }
            }
            if (i2 == 1284) {
                rotate();
            }
            if (i2 == 1028 || (i2 == 1284 && !messageProp.getPrivacy())) {
                int checksumLength2 = this.cipherHelper.getChecksumLength();
                this.checksum = new byte[checksumLength2];
                System.arraycopy(this.tokenData, this.tokenDataLen - checksumLength2, this.checksum, 0, checksumLength2);
                if (i2 == 1284 && !messageProp.getPrivacy() && checksumLength2 != this.ec) {
                    throw new GSSException(10, -1, getTokenName(i2) + ":EC incorrect!");
                }
            }
        } catch (IOException e2) {
            throw new GSSException(10, -1, getTokenName(i2) + CallSiteDescriptor.TOKEN_DELIMITER + e2.getMessage());
        }
    }

    public final int getTokenId() {
        return this.tokenId;
    }

    public final int getKeyUsage() {
        return this.key_usage;
    }

    public final boolean getConfState() {
        return this.confState;
    }

    public void genSignAndSeqNumber(MessageProp messageProp, byte[] bArr, int i2, int i3) throws GSSException {
        if (messageProp.getQOP() != 0) {
            messageProp.setQOP(0);
        }
        if (!this.confState) {
            messageProp.setPrivacy(false);
        }
        this.tokenHeader = new MessageTokenHeader(this.tokenId, messageProp.getPrivacy());
        if (this.tokenId == 1284) {
            this.key_usage = this.initiator ? 24 : 22;
        } else if (this.tokenId == 1028) {
            this.key_usage = this.initiator ? 25 : 23;
        }
        if (this.tokenId == 1028 || (!messageProp.getPrivacy() && this.tokenId == 1284)) {
            this.checksum = getChecksum(bArr, i2, i3);
        }
        if (!messageProp.getPrivacy() && this.tokenId == 1284) {
            byte[] bytes = this.tokenHeader.getBytes();
            bytes[4] = (byte) (this.checksum.length >>> 8);
            bytes[5] = (byte) this.checksum.length;
        }
    }

    public final boolean verifySign(byte[] bArr, int i2, int i3) throws GSSException {
        if (MessageDigest.isEqual(this.checksum, getChecksum(bArr, i2, i3))) {
            return true;
        }
        return false;
    }

    private void rotate() {
        if (this.rrc % this.tokenDataLen != 0) {
            this.rrc %= this.tokenDataLen;
            byte[] bArr = new byte[this.tokenDataLen];
            System.arraycopy(this.tokenData, this.rrc, bArr, 0, this.tokenDataLen - this.rrc);
            System.arraycopy(this.tokenData, 0, bArr, this.tokenDataLen - this.rrc, this.rrc);
            this.tokenData = bArr;
        }
    }

    public final int getSequenceNumber() {
        return this.seqNumber;
    }

    byte[] getChecksum(byte[] bArr, int i2, int i3) throws GSSException {
        byte[] bytes = this.tokenHeader.getBytes();
        if ((bytes[2] & 2) == 0 && this.tokenId == 1284) {
            bytes[4] = 0;
            bytes[5] = 0;
            bytes[6] = 0;
            bytes[7] = 0;
        }
        return this.cipherHelper.calculateChecksum(bytes, bArr, i2, i3, this.key_usage);
    }

    MessageToken_v2(int i2, Krb5Context krb5Context) throws GSSException {
        this.tokenHeader = null;
        this.tokenId = 0;
        this.key_usage = 0;
        this.ec = 0;
        this.rrc = 0;
        this.checksum = null;
        this.confState = true;
        this.initiator = true;
        this.have_acceptor_subkey = false;
        this.cipherHelper = null;
        init(i2, krb5Context);
        this.seqNumber = krb5Context.incrementMySequenceNumber();
    }

    private void init(int i2, Krb5Context krb5Context) throws GSSException {
        this.tokenId = i2;
        this.confState = krb5Context.getConfState();
        this.initiator = krb5Context.isInitiator();
        this.have_acceptor_subkey = krb5Context.getKeySrc() == 2;
        this.cipherHelper = krb5Context.getCipherHelper(null);
    }

    protected void encodeHeader(OutputStream outputStream) throws IOException {
        this.tokenHeader.encode(outputStream);
    }

    protected final byte[] getTokenHeader() {
        return this.tokenHeader.getBytes();
    }

    /* loaded from: rt.jar:sun/security/jgss/krb5/MessageToken_v2$MessageTokenHeader.class */
    class MessageTokenHeader {
        private int tokenId;
        private byte[] bytes;

        public MessageTokenHeader(int i2, boolean z2) throws GSSException {
            this.bytes = new byte[16];
            this.tokenId = i2;
            this.bytes[0] = (byte) (i2 >>> 8);
            this.bytes[1] = (byte) i2;
            this.bytes[2] = (byte) ((MessageToken_v2.this.initiator ? 0 : 1) | ((!z2 || i2 == 1028) ? 0 : 2) | (MessageToken_v2.this.have_acceptor_subkey ? 4 : 0));
            this.bytes[3] = -1;
            if (i2 == 1284) {
                this.bytes[4] = 0;
                this.bytes[5] = 0;
                this.bytes[6] = 0;
                this.bytes[7] = 0;
            } else if (i2 == 1028) {
                for (int i3 = 4; i3 < 8; i3++) {
                    this.bytes[i3] = -1;
                }
            }
            GSSToken.writeBigEndian(MessageToken_v2.this.seqNumber, this.bytes, 12);
        }

        public MessageTokenHeader(InputStream inputStream, MessageProp messageProp, int i2) throws IOException, GSSException {
            this.bytes = new byte[16];
            GSSToken.readFully(inputStream, this.bytes, 0, 16);
            this.tokenId = GSSToken.readInt(this.bytes, 0);
            if (this.tokenId != i2) {
                throw new GSSException(10, -1, Krb5Token.getTokenName(this.tokenId) + ":Defective Token ID!");
            }
            if ((this.bytes[2] & 1) != (MessageToken_v2.this.initiator)) {
                throw new GSSException(10, -1, Krb5Token.getTokenName(this.tokenId) + ":Acceptor Flag Error!");
            }
            if ((this.bytes[2] & 2) == 2 && this.tokenId == 1284) {
                messageProp.setPrivacy(true);
            } else {
                messageProp.setPrivacy(false);
            }
            if (this.tokenId == 1284) {
                if ((this.bytes[3] & 255) == 255) {
                    MessageToken_v2.this.ec = GSSToken.readBigEndian(this.bytes, 4, 2);
                    MessageToken_v2.this.rrc = GSSToken.readBigEndian(this.bytes, 6, 2);
                } else {
                    throw new GSSException(10, -1, Krb5Token.getTokenName(this.tokenId) + ":Defective Token Filler!");
                }
            } else if (this.tokenId == 1028) {
                for (int i3 = 3; i3 < 8; i3++) {
                    if ((this.bytes[i3] & 255) != 255) {
                        throw new GSSException(10, -1, Krb5Token.getTokenName(this.tokenId) + ":Defective Token Filler!");
                    }
                }
            }
            messageProp.setQOP(0);
            MessageToken_v2.this.seqNumber = GSSToken.readBigEndian(this.bytes, 12, 4);
        }

        public final void encode(OutputStream outputStream) throws IOException {
            outputStream.write(this.bytes);
        }

        public final int getTokenId() {
            return this.tokenId;
        }

        public final byte[] getBytes() {
            return this.bytes;
        }
    }
}
