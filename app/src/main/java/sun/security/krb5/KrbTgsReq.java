package sun.security.krb5;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import sun.security.krb5.internal.APOptions;
import sun.security.krb5.internal.AuthorizationData;
import sun.security.krb5.internal.HostAddresses;
import sun.security.krb5.internal.KDCOptions;
import sun.security.krb5.internal.KDCReqBody;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.PAData;
import sun.security.krb5.internal.TGSReq;
import sun.security.krb5.internal.Ticket;
import sun.security.krb5.internal.crypto.EType;
import sun.security.krb5.internal.crypto.Nonce;

/* loaded from: rt.jar:sun/security/krb5/KrbTgsReq.class */
public class KrbTgsReq {
    private PrincipalName princName;
    private PrincipalName clientAlias;
    private PrincipalName servName;
    private PrincipalName serverAlias;
    private TGSReq tgsReqMessg;
    private KerberosTime ctime;
    private Ticket secondTicket;
    private boolean useSubkey;
    EncryptionKey tgsReqKey;
    private static final boolean DEBUG = Krb5.DEBUG;
    private byte[] obuf;
    private byte[] ibuf;

    public KrbTgsReq(KDCOptions kDCOptions, Credentials credentials, PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, PrincipalName principalName4, Ticket[] ticketArr, PAData[] pADataArr) throws IOException, KrbException {
        this(kDCOptions, credentials, principalName, principalName2, principalName3, principalName4, null, null, null, null, null, null, ticketArr, null, pADataArr);
    }

    KrbTgsReq(KDCOptions kDCOptions, Credentials credentials, PrincipalName principalName, PrincipalName principalName2, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, int[] iArr, HostAddresses hostAddresses, AuthorizationData authorizationData, Ticket[] ticketArr, EncryptionKey encryptionKey) throws IOException, KrbException {
        this(kDCOptions, credentials, credentials.getClient(), credentials.getClientAlias(), principalName, principalName2, kerberosTime, kerberosTime2, kerberosTime3, iArr, hostAddresses, authorizationData, ticketArr, encryptionKey, null);
    }

    private KrbTgsReq(KDCOptions kDCOptions, Credentials credentials, PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, PrincipalName principalName4, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, int[] iArr, HostAddresses hostAddresses, AuthorizationData authorizationData, Ticket[] ticketArr, EncryptionKey encryptionKey, PAData[] pADataArr) throws IOException, ArrayIndexOutOfBoundsException, KrbException {
        this.secondTicket = null;
        this.useSubkey = false;
        this.princName = principalName;
        this.clientAlias = principalName2;
        this.servName = principalName3;
        this.serverAlias = principalName4;
        this.ctime = KerberosTime.now();
        if (kDCOptions.get(1) && !credentials.flags.get(1)) {
            kDCOptions.set(1, false);
        }
        if (kDCOptions.get(2) && !credentials.flags.get(1)) {
            throw new KrbException(101);
        }
        if (kDCOptions.get(3) && !credentials.flags.get(3)) {
            throw new KrbException(101);
        }
        if (kDCOptions.get(4) && !credentials.flags.get(3)) {
            throw new KrbException(101);
        }
        if (kDCOptions.get(5) && !credentials.flags.get(5)) {
            throw new KrbException(101);
        }
        if (kDCOptions.get(8) && !credentials.flags.get(8)) {
            throw new KrbException(101);
        }
        if (kDCOptions.get(6)) {
            if (!credentials.flags.get(6)) {
                throw new KrbException(101);
            }
        } else if (kerberosTime != null) {
            kerberosTime = null;
        }
        if (kDCOptions.get(8)) {
            if (!credentials.flags.get(8)) {
                throw new KrbException(101);
            }
        } else if (kerberosTime3 != null) {
            kerberosTime3 = null;
        }
        if (kDCOptions.get(28) || kDCOptions.get(14)) {
            if (ticketArr == null) {
                throw new KrbException(101);
            }
            this.secondTicket = ticketArr[0];
        } else if (ticketArr != null) {
            ticketArr = null;
        }
        this.tgsReqMessg = createRequest(kDCOptions, credentials.ticket, credentials.key, this.ctime, this.princName, this.servName, kerberosTime, kerberosTime2, kerberosTime3, iArr, hostAddresses, authorizationData, ticketArr, encryptionKey, pADataArr);
        this.obuf = this.tgsReqMessg.asn1Encode();
        if (credentials.flags.get(2)) {
            kDCOptions.set(2, true);
        }
    }

    public void send() throws IOException, KrbException {
        String realmString = null;
        if (this.servName != null) {
            realmString = this.servName.getRealmString();
        }
        this.ibuf = new KdcComm(realmString).send(this.obuf);
    }

    public KrbTgsRep getReply() throws IOException, KrbException {
        return new KrbTgsRep(this.ibuf, this);
    }

    public Credentials sendAndGetCreds() throws IOException, KrbException {
        send();
        return getReply().getCreds();
    }

    KerberosTime getCtime() {
        return this.ctime;
    }

    private TGSReq createRequest(KDCOptions kDCOptions, Ticket ticket, EncryptionKey encryptionKey, KerberosTime kerberosTime, PrincipalName principalName, PrincipalName principalName2, KerberosTime kerberosTime2, KerberosTime kerberosTime3, KerberosTime kerberosTime4, int[] iArr, HostAddresses hostAddresses, AuthorizationData authorizationData, Ticket[] ticketArr, EncryptionKey encryptionKey2, PAData[] pADataArr) throws IOException, KrbException {
        KerberosTime kerberosTime5;
        int[] defaults;
        PAData[] pADataArr2;
        if (kerberosTime3 == null) {
            if (Config.getInstance().get("libdefaults", "ticket_lifetime") != null) {
                kerberosTime5 = new KerberosTime(Instant.now().plusSeconds(Config.duration(r0)));
            } else {
                kerberosTime5 = new KerberosTime(0L);
            }
        } else {
            kerberosTime5 = kerberosTime3;
        }
        this.tgsReqKey = encryptionKey;
        if (iArr == null) {
            defaults = EType.getDefaults("default_tgs_enctypes");
        } else {
            defaults = iArr;
        }
        EncryptionKey encryptionKey3 = null;
        EncryptedData encryptedData = null;
        if (authorizationData != null) {
            byte[] bArrAsn1Encode = authorizationData.asn1Encode();
            if (encryptionKey2 != null) {
                encryptionKey3 = encryptionKey2;
                this.tgsReqKey = encryptionKey2;
                this.useSubkey = true;
                encryptedData = new EncryptedData(encryptionKey3, bArrAsn1Encode, 5);
            } else {
                encryptedData = new EncryptedData(encryptionKey, bArrAsn1Encode, 4);
            }
        }
        KDCReqBody kDCReqBody = new KDCReqBody(kDCOptions, principalName, principalName2, kerberosTime2, kerberosTime5, kerberosTime4, Nonce.value(), defaults, hostAddresses, encryptedData, ticketArr);
        PAData pAData = new PAData(1, new KrbApReq(new APOptions(), ticket, encryptionKey, principalName, new Checksum(Checksum.CKSUMTYPE_DEFAULT, kDCReqBody.asn1Encode(12), encryptionKey, 6), kerberosTime, encryptionKey3, null, null).getMessage());
        if (pADataArr != null) {
            pADataArr2 = (PAData[]) Arrays.copyOf(pADataArr, pADataArr.length + 1);
            pADataArr2[pADataArr.length] = pAData;
        } else {
            pADataArr2 = new PAData[]{pAData};
        }
        return new TGSReq(pADataArr2, kDCReqBody);
    }

    TGSReq getMessage() {
        return this.tgsReqMessg;
    }

    Ticket getSecondTicket() {
        return this.secondTicket;
    }

    PrincipalName getClientAlias() {
        return this.clientAlias;
    }

    PrincipalName getServerAlias() {
        return this.serverAlias;
    }

    private static void debug(String str) {
    }

    boolean usedSubkey() {
        return this.useSubkey;
    }
}
