package sun.security.krb5;

import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import net.lingala.zip4j.crypto.PBKDF2.BinTools;
import sun.security.jgss.krb5.Krb5AcceptCredential;
import sun.security.krb5.internal.APOptions;
import sun.security.krb5.internal.APReq;
import sun.security.krb5.internal.Authenticator;
import sun.security.krb5.internal.AuthorizationData;
import sun.security.krb5.internal.EncTicketPart;
import sun.security.krb5.internal.HostAddress;
import sun.security.krb5.internal.KRBError;
import sun.security.krb5.internal.KdcErrException;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.LocalSeqNumber;
import sun.security.krb5.internal.ReplayCache;
import sun.security.krb5.internal.SeqNumber;
import sun.security.krb5.internal.Ticket;
import sun.security.krb5.internal.crypto.EType;
import sun.security.krb5.internal.rcache.AuthTimeWithHash;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/KrbApReq.class */
public class KrbApReq {
    private byte[] obuf;
    private KerberosTime ctime;
    private int cusec;
    private Authenticator authenticator;
    private Credentials creds;
    private APReq apReqMessg;
    private static ReplayCache rcache = ReplayCache.getInstance();
    private static boolean DEBUG = Krb5.DEBUG;
    private static final char[] hexConst = BinTools.hex.toCharArray();

    public KrbApReq(Credentials credentials, boolean z2, boolean z3, boolean z4, Checksum checksum) throws IOException, KrbException {
        APOptions aPOptions = z2 ? new APOptions(2) : new APOptions();
        if (DEBUG) {
            System.out.println(">>> KrbApReq: APOptions are " + ((Object) aPOptions));
        }
        init(aPOptions, credentials, checksum, z3 ? new EncryptionKey(credentials.getSessionKey()) : null, new LocalSeqNumber(), null, 11);
    }

    public KrbApReq(byte[] bArr, Krb5AcceptCredential krb5AcceptCredential, InetAddress inetAddress) throws IOException, KrbException {
        this.obuf = bArr;
        if (this.apReqMessg == null) {
            decode();
        }
        authenticate(krb5AcceptCredential, inetAddress);
    }

    KrbApReq(APOptions aPOptions, Ticket ticket, EncryptionKey encryptionKey, PrincipalName principalName, Checksum checksum, KerberosTime kerberosTime, EncryptionKey encryptionKey2, SeqNumber seqNumber, AuthorizationData authorizationData) throws KrbCryptoException, KdcErrException, Asn1Exception, IOException {
        init(aPOptions, ticket, encryptionKey, principalName, checksum, kerberosTime, encryptionKey2, seqNumber, authorizationData, 7);
    }

    private void init(APOptions aPOptions, Credentials credentials, Checksum checksum, EncryptionKey encryptionKey, SeqNumber seqNumber, AuthorizationData authorizationData, int i2) throws IOException, KrbException {
        this.ctime = KerberosTime.now();
        init(aPOptions, credentials.ticket, credentials.key, credentials.client, checksum, this.ctime, encryptionKey, seqNumber, authorizationData, i2);
    }

    private void init(APOptions aPOptions, Ticket ticket, EncryptionKey encryptionKey, PrincipalName principalName, Checksum checksum, KerberosTime kerberosTime, EncryptionKey encryptionKey2, SeqNumber seqNumber, AuthorizationData authorizationData, int i2) throws KrbCryptoException, KdcErrException, Asn1Exception, IOException {
        createMessage(aPOptions, ticket, encryptionKey, principalName, checksum, kerberosTime, encryptionKey2, seqNumber, authorizationData, i2);
        this.obuf = this.apReqMessg.asn1Encode();
    }

    void decode() throws IOException, KrbException {
        decode(new DerValue(this.obuf));
    }

    void decode(DerValue derValue) throws IOException, KrbException {
        String strSubstring;
        this.apReqMessg = null;
        try {
            this.apReqMessg = new APReq(derValue);
        } catch (Asn1Exception e2) {
            this.apReqMessg = null;
            KRBError kRBError = new KRBError(derValue);
            String errorString = kRBError.getErrorString();
            if (errorString.charAt(errorString.length() - 1) == 0) {
                strSubstring = errorString.substring(0, errorString.length() - 1);
            } else {
                strSubstring = errorString;
            }
            KrbException krbException = new KrbException(kRBError.getErrorCode(), strSubstring);
            krbException.initCause(e2);
            throw krbException;
        }
    }

    private void authenticate(Krb5AcceptCredential krb5AcceptCredential, InetAddress inetAddress) throws IOException, KrbException {
        int eType = this.apReqMessg.ticket.encPart.getEType();
        EncryptionKey encryptionKeyFindKey = EncryptionKey.findKey(eType, this.apReqMessg.ticket.encPart.getKeyVersionNumber(), krb5AcceptCredential.getKrb5EncryptionKeys(this.apReqMessg.ticket.sname));
        if (encryptionKeyFindKey == null) {
            throw new KrbException(400, "Cannot find key of appropriate type to decrypt AP REP - " + EType.toString(eType));
        }
        EncTicketPart encTicketPart = new EncTicketPart(this.apReqMessg.ticket.encPart.reset(this.apReqMessg.ticket.encPart.decrypt(encryptionKeyFindKey, 2)));
        checkPermittedEType(encTicketPart.key.getEType());
        this.authenticator = new Authenticator(this.apReqMessg.authenticator.reset(this.apReqMessg.authenticator.decrypt(encTicketPart.key, 11)));
        this.ctime = this.authenticator.ctime;
        this.cusec = this.authenticator.cusec;
        this.authenticator.ctime = this.authenticator.ctime.withMicroSeconds(this.authenticator.cusec);
        if (!this.authenticator.cname.equals(encTicketPart.cname)) {
            throw new KrbApErrException(36);
        }
        if (!this.authenticator.ctime.inClockSkew()) {
            throw new KrbApErrException(37);
        }
        try {
            byte[] bArrDigest = MessageDigest.getInstance("MD5").digest(this.apReqMessg.authenticator.cipher);
            char[] cArr = new char[bArrDigest.length * 2];
            for (int i2 = 0; i2 < bArrDigest.length; i2++) {
                cArr[2 * i2] = hexConst[(bArrDigest[i2] & 255) >> 4];
                cArr[(2 * i2) + 1] = hexConst[bArrDigest[i2] & 15];
            }
            rcache.checkAndStore(KerberosTime.now(), new AuthTimeWithHash(this.authenticator.cname.toString(), this.apReqMessg.ticket.sname.toString(), this.authenticator.ctime.getSeconds(), this.authenticator.cusec, new String(cArr)));
            if (inetAddress != null) {
                HostAddress hostAddress = new HostAddress(inetAddress);
                if (encTicketPart.caddr != null && !encTicketPart.caddr.inList(hostAddress)) {
                    if (DEBUG) {
                        System.out.println(">>> KrbApReq: initiator is " + ((Object) hostAddress.getInetAddress()) + ", but caddr is " + Arrays.toString(encTicketPart.caddr.getInetAddresses()));
                    }
                    throw new KrbApErrException(38);
                }
            }
            KerberosTime kerberosTimeNow = KerberosTime.now();
            if ((encTicketPart.starttime != null && encTicketPart.starttime.greaterThanWRTClockSkew(kerberosTimeNow)) || encTicketPart.flags.get(7)) {
                throw new KrbApErrException(33);
            }
            if (encTicketPart.endtime != null && kerberosTimeNow.greaterThanWRTClockSkew(encTicketPart.endtime)) {
                throw new KrbApErrException(32);
            }
            this.creds = new Credentials(this.apReqMessg.ticket, this.authenticator.cname, (PrincipalName) null, this.apReqMessg.ticket.sname, (PrincipalName) null, encTicketPart.key, encTicketPart.flags, encTicketPart.authtime, encTicketPart.starttime, encTicketPart.endtime, encTicketPart.renewTill, encTicketPart.caddr, encTicketPart.authorizationData);
            if (DEBUG) {
                System.out.println(">>> KrbApReq: authenticate succeed.");
            }
        } catch (NoSuchAlgorithmException e2) {
            throw new AssertionError((Object) "Impossible");
        }
    }

    public Credentials getCreds() {
        return this.creds;
    }

    KerberosTime getCtime() {
        if (this.ctime != null) {
            return this.ctime;
        }
        return this.authenticator.ctime;
    }

    int cusec() {
        return this.cusec;
    }

    APOptions getAPOptions() throws IOException, KrbException {
        if (this.apReqMessg == null) {
            decode();
        }
        if (this.apReqMessg != null) {
            return this.apReqMessg.apOptions;
        }
        return null;
    }

    public boolean getMutualAuthRequired() throws IOException, KrbException {
        if (this.apReqMessg == null) {
            decode();
        }
        if (this.apReqMessg != null) {
            return this.apReqMessg.apOptions.get(2);
        }
        return false;
    }

    boolean useSessionKey() throws IOException, KrbException {
        if (this.apReqMessg == null) {
            decode();
        }
        if (this.apReqMessg != null) {
            return this.apReqMessg.apOptions.get(1);
        }
        return false;
    }

    public EncryptionKey getSubKey() {
        return this.authenticator.getSubKey();
    }

    public Integer getSeqNumber() {
        return this.authenticator.getSeqNumber();
    }

    public Checksum getChecksum() {
        return this.authenticator.getChecksum();
    }

    public byte[] getMessage() {
        return this.obuf;
    }

    public PrincipalName getClient() {
        return this.creds.getClient();
    }

    private void createMessage(APOptions aPOptions, Ticket ticket, EncryptionKey encryptionKey, PrincipalName principalName, Checksum checksum, KerberosTime kerberosTime, EncryptionKey encryptionKey2, SeqNumber seqNumber, AuthorizationData authorizationData, int i2) throws KrbCryptoException, KdcErrException, Asn1Exception, IOException {
        Integer num = null;
        if (seqNumber != null) {
            num = new Integer(seqNumber.current());
        }
        this.authenticator = new Authenticator(principalName, checksum, kerberosTime.getMicroSeconds(), kerberosTime, encryptionKey2, num, authorizationData);
        this.apReqMessg = new APReq(aPOptions, ticket, new EncryptedData(encryptionKey, this.authenticator.asn1Encode(), i2));
    }

    private static void checkPermittedEType(int i2) throws KrbException {
        if (!EType.isSupported(i2, EType.getDefaults("permitted_enctypes"))) {
            throw new KrbException(EType.toString(i2) + " encryption type not in permitted_enctypes list");
        }
    }
}
