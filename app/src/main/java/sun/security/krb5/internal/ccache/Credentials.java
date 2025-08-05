package sun.security.krb5.internal.ccache;

import sun.security.krb5.EncryptionKey;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.AuthorizationData;
import sun.security.krb5.internal.HostAddresses;
import sun.security.krb5.internal.KDCRep;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.Ticket;
import sun.security.krb5.internal.TicketFlags;

/* loaded from: rt.jar:sun/security/krb5/internal/ccache/Credentials.class */
public class Credentials {
    PrincipalName cname;
    PrincipalName sname;
    EncryptionKey key;
    KerberosTime authtime;
    KerberosTime starttime;
    KerberosTime endtime;
    KerberosTime renewTill;
    HostAddresses caddr;
    AuthorizationData authorizationData;
    public boolean isEncInSKey;
    TicketFlags flags;
    Ticket ticket;
    Ticket secondTicket;
    private boolean DEBUG;

    public Credentials(PrincipalName principalName, PrincipalName principalName2, EncryptionKey encryptionKey, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, KerberosTime kerberosTime4, boolean z2, TicketFlags ticketFlags, HostAddresses hostAddresses, AuthorizationData authorizationData, Ticket ticket, Ticket ticket2) {
        this.DEBUG = Krb5.DEBUG;
        this.cname = (PrincipalName) principalName.clone();
        this.sname = (PrincipalName) principalName2.clone();
        this.key = (EncryptionKey) encryptionKey.clone();
        this.authtime = kerberosTime;
        this.starttime = kerberosTime2;
        this.endtime = kerberosTime3;
        this.renewTill = kerberosTime4;
        if (hostAddresses != null) {
            this.caddr = (HostAddresses) hostAddresses.clone();
        }
        if (authorizationData != null) {
            this.authorizationData = (AuthorizationData) authorizationData.clone();
        }
        this.isEncInSKey = z2;
        this.flags = (TicketFlags) ticketFlags.clone();
        this.ticket = (Ticket) ticket.clone();
        if (ticket2 != null) {
            this.secondTicket = (Ticket) ticket2.clone();
        }
    }

    public Credentials(KDCRep kDCRep, Ticket ticket, AuthorizationData authorizationData, boolean z2) {
        this.DEBUG = Krb5.DEBUG;
        if (kDCRep.encKDCRepPart == null) {
            return;
        }
        this.cname = (PrincipalName) kDCRep.cname.clone();
        this.ticket = (Ticket) kDCRep.ticket.clone();
        this.key = (EncryptionKey) kDCRep.encKDCRepPart.key.clone();
        this.flags = (TicketFlags) kDCRep.encKDCRepPart.flags.clone();
        this.authtime = kDCRep.encKDCRepPart.authtime;
        this.starttime = kDCRep.encKDCRepPart.starttime;
        this.endtime = kDCRep.encKDCRepPart.endtime;
        this.renewTill = kDCRep.encKDCRepPart.renewTill;
        this.sname = (PrincipalName) kDCRep.encKDCRepPart.sname.clone();
        this.caddr = (HostAddresses) kDCRep.encKDCRepPart.caddr.clone();
        this.secondTicket = (Ticket) ticket.clone();
        this.authorizationData = (AuthorizationData) authorizationData.clone();
        this.isEncInSKey = z2;
    }

    public Credentials(KDCRep kDCRep) {
        this(kDCRep, null);
    }

    public Credentials(KDCRep kDCRep, Ticket ticket) {
        this.DEBUG = Krb5.DEBUG;
        this.sname = (PrincipalName) kDCRep.encKDCRepPart.sname.clone();
        this.cname = (PrincipalName) kDCRep.cname.clone();
        this.key = (EncryptionKey) kDCRep.encKDCRepPart.key.clone();
        this.authtime = kDCRep.encKDCRepPart.authtime;
        this.starttime = kDCRep.encKDCRepPart.starttime;
        this.endtime = kDCRep.encKDCRepPart.endtime;
        this.renewTill = kDCRep.encKDCRepPart.renewTill;
        this.flags = kDCRep.encKDCRepPart.flags;
        if (kDCRep.encKDCRepPart.caddr != null) {
            this.caddr = (HostAddresses) kDCRep.encKDCRepPart.caddr.clone();
        } else {
            this.caddr = null;
        }
        this.ticket = (Ticket) kDCRep.ticket.clone();
        if (ticket != null) {
            this.secondTicket = (Ticket) ticket.clone();
            this.isEncInSKey = true;
        } else {
            this.secondTicket = null;
            this.isEncInSKey = false;
        }
    }

    public boolean isValid() {
        boolean z2 = true;
        if (this.endtime.getTime() < System.currentTimeMillis()) {
            z2 = false;
        } else if (this.starttime != null) {
            if (this.starttime.getTime() > System.currentTimeMillis()) {
                z2 = false;
            }
        } else if (this.authtime.getTime() > System.currentTimeMillis()) {
            z2 = false;
        }
        return z2;
    }

    public PrincipalName getServicePrincipal() throws RealmException {
        return this.sname;
    }

    public Ticket getTicket() throws RealmException {
        return this.ticket;
    }

    public PrincipalName getServicePrincipal2() throws RealmException {
        if (this.secondTicket == null) {
            return null;
        }
        return this.secondTicket.sname;
    }

    public PrincipalName getClientPrincipal() throws RealmException {
        return this.cname;
    }

    public sun.security.krb5.Credentials setKrbCreds() {
        return new sun.security.krb5.Credentials(this.ticket, this.cname, null, this.sname, null, this.key, this.flags, this.authtime, this.starttime, this.endtime, this.renewTill, this.caddr);
    }

    public KerberosTime getStartTime() {
        return this.starttime;
    }

    public KerberosTime getAuthTime() {
        return this.authtime;
    }

    public KerberosTime getEndTime() {
        return this.endtime;
    }

    public KerberosTime getRenewTill() {
        return this.renewTill;
    }

    public TicketFlags getTicketFlags() {
        return this.flags;
    }

    public int getEType() {
        return this.key.getEType();
    }

    public EncryptionKey getKey() {
        return this.key;
    }

    public int getTktEType() {
        return this.ticket.encPart.getEType();
    }

    public int getTktEType2() {
        if (this.secondTicket == null) {
            return 0;
        }
        return this.secondTicket.encPart.getEType();
    }
}
