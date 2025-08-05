package sun.security.jgss.krb5;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.security.auth.kerberos.DelegationPermission;
import org.icepdf.core.util.PdfOps;
import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.GSSException;
import sun.security.jgss.GSSToken;
import sun.security.krb5.Checksum;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbCred;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;

/* loaded from: rt.jar:sun/security/jgss/krb5/InitialToken.class */
abstract class InitialToken extends Krb5Token {
    private static final int CHECKSUM_TYPE = 32771;
    private static final int CHECKSUM_LENGTH_SIZE = 4;
    private static final int CHECKSUM_BINDINGS_SIZE = 16;
    private static final int CHECKSUM_FLAGS_SIZE = 4;
    private static final int CHECKSUM_DELEG_OPT_SIZE = 2;
    private static final int CHECKSUM_DELEG_LGTH_SIZE = 2;
    private static final int CHECKSUM_DELEG_FLAG = 1;
    private static final int CHECKSUM_MUTUAL_FLAG = 2;
    private static final int CHECKSUM_REPLAY_FLAG = 4;
    private static final int CHECKSUM_SEQUENCE_FLAG = 8;
    private static final int CHECKSUM_CONF_FLAG = 16;
    private static final int CHECKSUM_INTEG_FLAG = 32;
    private final byte[] CHECKSUM_FIRST_BYTES = {16, 0, 0, 0};
    private static final int CHANNEL_BINDING_AF_INET = 2;
    private static final int CHANNEL_BINDING_AF_INET6 = 24;
    private static final int CHANNEL_BINDING_AF_NULL_ADDR = 255;
    private static final int Inet4_ADDRSZ = 4;
    private static final int Inet6_ADDRSZ = 16;

    public abstract byte[] encode() throws IOException;

    InitialToken() {
    }

    /* loaded from: rt.jar:sun/security/jgss/krb5/InitialToken$OverloadedChecksum.class */
    protected class OverloadedChecksum {
        private byte[] checksumBytes;
        private Credentials delegCreds;
        private int flags;

        public OverloadedChecksum(Krb5Context krb5Context, Credentials credentials, Credentials credentials2) throws IOException, GSSException, KrbException {
            KrbCred krbCred;
            this.checksumBytes = null;
            this.delegCreds = null;
            this.flags = 0;
            byte[] message = null;
            int length = 24;
            if (!credentials.isForwardable()) {
                krb5Context.setCredDelegState(false);
                krb5Context.setDelegPolicyState(false);
            } else if (krb5Context.getCredDelegState()) {
                if (krb5Context.getDelegPolicyState() && !credentials2.checkDelegate()) {
                    krb5Context.setDelegPolicyState(false);
                }
            } else if (krb5Context.getDelegPolicyState()) {
                if (credentials2.checkDelegate()) {
                    krb5Context.setCredDelegState(true);
                } else {
                    krb5Context.setDelegPolicyState(false);
                }
            }
            if (krb5Context.getCredDelegState()) {
                if (useNullKey(krb5Context.getCipherHelper(credentials2.getSessionKey()))) {
                    krbCred = new KrbCred(credentials, credentials2, EncryptionKey.NULL_KEY);
                } else {
                    krbCred = new KrbCred(credentials, credentials2, credentials2.getSessionKey());
                }
                message = krbCred.getMessage();
                length = 24 + 4 + message.length;
            }
            this.checksumBytes = new byte[length];
            int i2 = 0 + 1;
            this.checksumBytes[0] = InitialToken.this.CHECKSUM_FIRST_BYTES[0];
            int i3 = i2 + 1;
            this.checksumBytes[i2] = InitialToken.this.CHECKSUM_FIRST_BYTES[1];
            int i4 = i3 + 1;
            this.checksumBytes[i3] = InitialToken.this.CHECKSUM_FIRST_BYTES[2];
            int i5 = i4 + 1;
            this.checksumBytes[i4] = InitialToken.this.CHECKSUM_FIRST_BYTES[3];
            if (krb5Context.getChannelBinding() != null) {
                byte[] bArrComputeChannelBinding = InitialToken.this.computeChannelBinding(krb5Context.getChannelBinding());
                System.arraycopy(bArrComputeChannelBinding, 0, this.checksumBytes, i5, bArrComputeChannelBinding.length);
            }
            int i6 = i5 + 16;
            if (krb5Context.getCredDelegState()) {
                this.flags |= 1;
            }
            if (krb5Context.getMutualAuthState()) {
                this.flags |= 2;
            }
            if (krb5Context.getReplayDetState()) {
                this.flags |= 4;
            }
            if (krb5Context.getSequenceDetState()) {
                this.flags |= 8;
            }
            if (krb5Context.getIntegState()) {
                this.flags |= 32;
            }
            if (krb5Context.getConfState()) {
                this.flags |= 16;
            }
            byte[] bArr = new byte[4];
            GSSToken.writeLittleEndian(this.flags, bArr);
            int i7 = i6 + 1;
            this.checksumBytes[i6] = bArr[0];
            int i8 = i7 + 1;
            this.checksumBytes[i7] = bArr[1];
            int i9 = i8 + 1;
            this.checksumBytes[i8] = bArr[2];
            int i10 = i9 + 1;
            this.checksumBytes[i9] = bArr[3];
            if (krb5Context.getCredDelegState()) {
                PrincipalName server = credentials2.getServer();
                StringBuffer stringBuffer = new StringBuffer(PdfOps.DOUBLE_QUOTE__TOKEN);
                stringBuffer.append(server.getName()).append('\"');
                String realmAsString = server.getRealmAsString();
                stringBuffer.append(" \"krbtgt/").append(realmAsString).append('@');
                stringBuffer.append(realmAsString).append('\"');
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkPermission(new DelegationPermission(stringBuffer.toString()));
                }
                int i11 = i10 + 1;
                this.checksumBytes[i10] = 1;
                int i12 = i11 + 1;
                this.checksumBytes[i11] = 0;
                if (message.length > 65535) {
                    throw new GSSException(11, -1, "Incorrect message length");
                }
                GSSToken.writeLittleEndian(message.length, bArr);
                int i13 = i12 + 1;
                this.checksumBytes[i12] = bArr[0];
                this.checksumBytes[i13] = bArr[1];
                System.arraycopy(message, 0, this.checksumBytes, i13 + 1, message.length);
            }
        }

        public OverloadedChecksum(Krb5Context krb5Context, Checksum checksum, EncryptionKey encryptionKey, EncryptionKey encryptionKey2) throws IOException, GSSException, KrbException {
            KrbCred krbCred;
            this.checksumBytes = null;
            this.delegCreds = null;
            this.flags = 0;
            if (checksum == null) {
                GSSException gSSException = new GSSException(11, -1, "No cksum in AP_REQ's authenticator");
                gSSException.initCause(new KrbException(50));
                throw gSSException;
            }
            this.checksumBytes = checksum.getBytes();
            if (this.checksumBytes[0] != InitialToken.this.CHECKSUM_FIRST_BYTES[0] || this.checksumBytes[1] != InitialToken.this.CHECKSUM_FIRST_BYTES[1] || this.checksumBytes[2] != InitialToken.this.CHECKSUM_FIRST_BYTES[2] || this.checksumBytes[3] != InitialToken.this.CHECKSUM_FIRST_BYTES[3]) {
                throw new GSSException(11, -1, "Incorrect checksum");
            }
            ChannelBinding channelBinding = krb5Context.getChannelBinding();
            if (channelBinding != null) {
                byte[] bArr = new byte[16];
                System.arraycopy(this.checksumBytes, 4, bArr, 0, 16);
                if (!Arrays.equals(new byte[16], bArr)) {
                    if (!Arrays.equals(InitialToken.this.computeChannelBinding(channelBinding), bArr)) {
                        throw new GSSException(1, -1, "Bytes mismatch!");
                    }
                } else {
                    throw new GSSException(1, -1, "Token missing ChannelBinding!");
                }
            }
            this.flags = GSSToken.readLittleEndian(this.checksumBytes, 20, 4);
            if ((this.flags & 1) > 0) {
                int littleEndian = GSSToken.readLittleEndian(this.checksumBytes, 26, 2);
                byte[] bArr2 = new byte[littleEndian];
                System.arraycopy(this.checksumBytes, 28, bArr2, 0, littleEndian);
                try {
                    krbCred = new KrbCred(bArr2, encryptionKey);
                } catch (KrbException e2) {
                    if (encryptionKey2 != null) {
                        krbCred = new KrbCred(bArr2, encryptionKey2);
                    } else {
                        throw e2;
                    }
                }
                this.delegCreds = krbCred.getDelegatedCreds()[0];
            }
        }

        private boolean useNullKey(CipherHelper cipherHelper) {
            boolean z2 = true;
            if (cipherHelper.getProto() == 1 || cipherHelper.isArcFour()) {
                z2 = false;
            }
            return z2;
        }

        public Checksum getChecksum() throws KrbException {
            return new Checksum(this.checksumBytes, InitialToken.CHECKSUM_TYPE);
        }

        public Credentials getDelegatedCreds() {
            return this.delegCreds;
        }

        public void setContextFlags(Krb5Context krb5Context) {
            if ((this.flags & 1) > 0) {
                krb5Context.setCredDelegState(true);
            }
            if ((this.flags & 2) == 0) {
                krb5Context.setMutualAuthState(false);
            }
            if ((this.flags & 4) == 0) {
                krb5Context.setReplayDetState(false);
            }
            if ((this.flags & 8) == 0) {
                krb5Context.setSequenceDetState(false);
            }
            if ((this.flags & 16) == 0) {
                krb5Context.setConfState(false);
            }
            if ((this.flags & 32) == 0) {
                krb5Context.setIntegState(false);
            }
        }
    }

    private int getAddrType(InetAddress inetAddress) {
        int i2 = 255;
        if (inetAddress instanceof Inet4Address) {
            i2 = 2;
        } else if (inetAddress instanceof Inet6Address) {
            i2 = 24;
        }
        return i2;
    }

    private byte[] getAddrBytes(InetAddress inetAddress) throws GSSException {
        int addrType = getAddrType(inetAddress);
        byte[] address = inetAddress.getAddress();
        if (address != null) {
            switch (addrType) {
                case 2:
                    if (address.length != 4) {
                        throw new GSSException(11, -1, "Incorrect AF-INET address length in ChannelBinding.");
                    }
                    return address;
                case 24:
                    if (address.length != 16) {
                        throw new GSSException(11, -1, "Incorrect AF-INET6 address length in ChannelBinding.");
                    }
                    return address;
                default:
                    throw new GSSException(11, -1, "Cannot handle non AF-INET addresses in ChannelBinding.");
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] computeChannelBinding(ChannelBinding channelBinding) throws GSSException {
        int length;
        int length2;
        InetAddress initiatorAddress = channelBinding.getInitiatorAddress();
        InetAddress acceptorAddress = channelBinding.getAcceptorAddress();
        int length3 = 20;
        int addrType = getAddrType(initiatorAddress);
        int addrType2 = getAddrType(acceptorAddress);
        byte[] addrBytes = null;
        if (initiatorAddress != null) {
            addrBytes = getAddrBytes(initiatorAddress);
            length3 = 20 + addrBytes.length;
        }
        byte[] addrBytes2 = null;
        if (acceptorAddress != null) {
            addrBytes2 = getAddrBytes(acceptorAddress);
            length3 += addrBytes2.length;
        }
        byte[] applicationData = channelBinding.getApplicationData();
        if (applicationData != null) {
            length3 += applicationData.length;
        }
        byte[] bArr = new byte[length3];
        writeLittleEndian(addrType, bArr, 0);
        int i2 = 0 + 4;
        if (addrBytes != null) {
            writeLittleEndian(addrBytes.length, bArr, i2);
            int i3 = i2 + 4;
            System.arraycopy(addrBytes, 0, bArr, i3, addrBytes.length);
            length = i3 + addrBytes.length;
        } else {
            length = i2 + 4;
        }
        writeLittleEndian(addrType2, bArr, length);
        int i4 = length + 4;
        if (addrBytes2 != null) {
            writeLittleEndian(addrBytes2.length, bArr, i4);
            int i5 = i4 + 4;
            System.arraycopy(addrBytes2, 0, bArr, i5, addrBytes2.length);
            length2 = i5 + addrBytes2.length;
        } else {
            length2 = i4 + 4;
        }
        if (applicationData != null) {
            writeLittleEndian(applicationData.length, bArr, length2);
            int i6 = length2 + 4;
            System.arraycopy(applicationData, 0, bArr, i6, applicationData.length);
            int length4 = i6 + applicationData.length;
        } else {
            int i7 = length2 + 4;
        }
        try {
            return MessageDigest.getInstance("MD5").digest(bArr);
        } catch (NoSuchAlgorithmException e2) {
            throw new GSSException(11, -1, "Could not get MD5 Message Digest - " + e2.getMessage());
        }
    }
}
