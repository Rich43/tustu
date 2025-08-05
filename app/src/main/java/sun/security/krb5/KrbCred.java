package sun.security.krb5;

import java.io.IOException;
import sun.security.krb5.internal.EncKrbCredPart;
import sun.security.krb5.internal.HostAddresses;
import sun.security.krb5.internal.KDCOptions;
import sun.security.krb5.internal.KRBCred;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbCredInfo;
import sun.security.krb5.internal.Ticket;
import sun.security.krb5.internal.TicketFlags;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/KrbCred.class */
public class KrbCred {
    private static boolean DEBUG = Krb5.DEBUG;
    private byte[] obuf;
    private KRBCred credMessg;
    private Ticket ticket;
    private EncKrbCredPart encPart;
    private Credentials creds;
    private KerberosTime timeStamp;

    public KrbCred(Credentials credentials, Credentials credentials2, EncryptionKey encryptionKey) throws IOException, ArrayIndexOutOfBoundsException, KrbException {
        this.obuf = null;
        this.credMessg = null;
        this.ticket = null;
        this.encPart = null;
        this.creds = null;
        this.timeStamp = null;
        PrincipalName client = credentials.getClient();
        PrincipalName server = credentials.getServer();
        if (!credentials2.getClient().equals(client)) {
            throw new KrbException(60, "Client principal does not match");
        }
        KDCOptions kDCOptions = new KDCOptions();
        kDCOptions.set(2, true);
        kDCOptions.set(1, true);
        this.credMessg = createMessage(new KrbTgsReq(kDCOptions, credentials, server, null, null, null, null, null, null, null, null, null).sendAndGetCreds(), encryptionKey);
        this.obuf = this.credMessg.asn1Encode();
    }

    KRBCred createMessage(Credentials credentials, EncryptionKey encryptionKey) throws IOException, KrbException {
        KrbCredInfo krbCredInfo = new KrbCredInfo(credentials.getSessionKey(), credentials.getClient(), credentials.flags, credentials.authTime, credentials.startTime, credentials.endTime, credentials.renewTill, credentials.getServer(), credentials.cAddr);
        this.timeStamp = KerberosTime.now();
        this.credMessg = new KRBCred(new Ticket[]{credentials.ticket}, new EncryptedData(encryptionKey, new EncKrbCredPart(new KrbCredInfo[]{krbCredInfo}, this.timeStamp, null, null, null, null).asn1Encode(), 14));
        return this.credMessg;
    }

    public KrbCred(byte[] bArr, EncryptionKey encryptionKey) throws IOException, KrbException {
        this.obuf = null;
        this.credMessg = null;
        this.ticket = null;
        this.encPart = null;
        this.creds = null;
        this.timeStamp = null;
        this.credMessg = new KRBCred(bArr);
        this.ticket = this.credMessg.tickets[0];
        EncKrbCredPart encKrbCredPart = new EncKrbCredPart(new DerValue(this.credMessg.encPart.reset(this.credMessg.encPart.decrypt(this.credMessg.encPart.getEType() == 0 ? EncryptionKey.NULL_KEY : encryptionKey, 14))));
        this.timeStamp = encKrbCredPart.timeStamp;
        KrbCredInfo krbCredInfo = encKrbCredPart.ticketInfo[0];
        EncryptionKey encryptionKey2 = krbCredInfo.key;
        PrincipalName principalName = krbCredInfo.pname;
        TicketFlags ticketFlags = krbCredInfo.flags;
        KerberosTime kerberosTime = krbCredInfo.authtime;
        KerberosTime kerberosTime2 = krbCredInfo.starttime;
        KerberosTime kerberosTime3 = krbCredInfo.endtime;
        KerberosTime kerberosTime4 = krbCredInfo.renewTill;
        PrincipalName principalName2 = krbCredInfo.sname;
        HostAddresses hostAddresses = krbCredInfo.caddr;
        if (DEBUG) {
            System.out.println(">>>Delegated Creds have pname=" + ((Object) principalName) + " sname=" + ((Object) principalName2) + " authtime=" + ((Object) kerberosTime) + " starttime=" + ((Object) kerberosTime2) + " endtime=" + ((Object) kerberosTime3) + "renewTill=" + ((Object) kerberosTime4));
        }
        this.creds = new Credentials(this.ticket, principalName, null, principalName2, null, encryptionKey2, ticketFlags, kerberosTime, kerberosTime2, kerberosTime3, kerberosTime4, hostAddresses);
    }

    public Credentials[] getDelegatedCreds() {
        return new Credentials[]{this.creds};
    }

    public byte[] getMessage() {
        return this.obuf;
    }
}
