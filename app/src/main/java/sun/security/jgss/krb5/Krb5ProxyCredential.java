package sun.security.jgss.krb5;

import java.io.IOException;
import java.security.Provider;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.kerberos.KerberosTicket;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.krb5.Credentials;
import sun.security.krb5.KrbException;
import sun.security.krb5.internal.Ticket;

/* loaded from: rt.jar:sun/security/jgss/krb5/Krb5ProxyCredential.class */
public class Krb5ProxyCredential implements Krb5CredElement {
    public final Krb5InitCredential self;
    private final Krb5NameElement client;
    public final Ticket tkt;

    Krb5ProxyCredential(Krb5InitCredential krb5InitCredential, Krb5NameElement krb5NameElement, Ticket ticket) {
        this.self = krb5InitCredential;
        this.tkt = ticket;
        this.client = krb5NameElement;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public final Krb5NameElement getName() throws GSSException {
        return this.client;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getInitLifetime() throws GSSException {
        return this.self.getInitLifetime();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getAcceptLifetime() throws GSSException {
        return 0;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public boolean isInitiatorCredential() throws GSSException {
        return true;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public boolean isAcceptorCredential() throws GSSException {
        return false;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public final Oid getMechanism() {
        return Krb5MechFactory.GSS_KRB5_MECH_OID;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public final Provider getProvider() {
        return Krb5MechFactory.PROVIDER;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public void dispose() throws GSSException {
        try {
            this.self.destroy();
        } catch (DestroyFailedException e2) {
            new GSSException(11, -1, "Could not destroy credentials - " + e2.getMessage()).initCause(e2);
        }
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public GSSCredentialSpi impersonate(GSSNameSpi gSSNameSpi) throws GSSException {
        throw new GSSException(11, -1, "Only an initiate credentials can impersonate");
    }

    static Krb5CredElement tryImpersonation(GSSCaller gSSCaller, Krb5InitCredential krb5InitCredential) throws GSSException {
        try {
            KerberosTicket kerberosTicket = krb5InitCredential.proxyTicket;
            if (kerberosTicket != null) {
                Credentials credentialsTicketToCreds = Krb5Util.ticketToCreds(kerberosTicket);
                return new Krb5ProxyCredential(krb5InitCredential, Krb5NameElement.getInstance(credentialsTicketToCreds.getClient()), credentialsTicketToCreds.getTicket());
            }
            return krb5InitCredential;
        } catch (IOException | KrbException e2) {
            throw new GSSException(9, -1, "Cannot create proxy credential");
        }
    }
}
