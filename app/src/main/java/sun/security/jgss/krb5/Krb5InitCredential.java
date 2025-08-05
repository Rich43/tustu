package sun.security.jgss.krb5;

import java.io.IOException;
import java.net.InetAddress;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Provider;
import java.util.Date;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KerberosSecrets;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;

/* loaded from: rt.jar:sun/security/jgss/krb5/Krb5InitCredential.class */
public class Krb5InitCredential extends KerberosTicket implements Krb5CredElement {
    private static final long serialVersionUID = 7723415700837898232L;
    private Krb5NameElement name;
    private Credentials krb5Credentials;
    public KerberosTicket proxyTicket;

    private Krb5InitCredential(Krb5NameElement krb5NameElement, byte[] bArr, KerberosPrincipal kerberosPrincipal, KerberosPrincipal kerberosPrincipal2, KerberosPrincipal kerberosPrincipal3, KerberosPrincipal kerberosPrincipal4, byte[] bArr2, int i2, boolean[] zArr, Date date, Date date2, Date date3, Date date4, InetAddress[] inetAddressArr) throws GSSException {
        super(bArr, kerberosPrincipal, kerberosPrincipal3, bArr2, i2, zArr, date, date2, date3, date4, inetAddressArr);
        KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketSetClientAlias(this, kerberosPrincipal2);
        KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketSetServerAlias(this, kerberosPrincipal4);
        this.name = krb5NameElement;
        try {
            this.krb5Credentials = new Credentials(bArr, kerberosPrincipal.getName(), kerberosPrincipal2 != null ? kerberosPrincipal2.getName() : null, kerberosPrincipal3.getName(), kerberosPrincipal4 != null ? kerberosPrincipal4.getName() : null, bArr2, i2, zArr, date, date2, date3, date4, inetAddressArr);
        } catch (IOException e2) {
            throw new GSSException(13, -1, e2.getMessage());
        } catch (KrbException e3) {
            throw new GSSException(13, -1, e3.getMessage());
        }
    }

    private Krb5InitCredential(Krb5NameElement krb5NameElement, Credentials credentials, byte[] bArr, KerberosPrincipal kerberosPrincipal, KerberosPrincipal kerberosPrincipal2, KerberosPrincipal kerberosPrincipal3, KerberosPrincipal kerberosPrincipal4, byte[] bArr2, int i2, boolean[] zArr, Date date, Date date2, Date date3, Date date4, InetAddress[] inetAddressArr) throws GSSException {
        super(bArr, kerberosPrincipal, kerberosPrincipal3, bArr2, i2, zArr, date, date2, date3, date4, inetAddressArr);
        KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketSetClientAlias(this, kerberosPrincipal2);
        KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketSetServerAlias(this, kerberosPrincipal4);
        this.name = krb5NameElement;
        this.krb5Credentials = credentials;
    }

    static Krb5InitCredential getInstance(GSSCaller gSSCaller, Krb5NameElement krb5NameElement, int i2) throws GSSException {
        KerberosTicket tgt = getTgt(gSSCaller, krb5NameElement, i2);
        if (tgt == null) {
            throw new GSSException(13, -1, "Failed to find any Kerberos tgt");
        }
        if (krb5NameElement == null) {
            krb5NameElement = Krb5NameElement.getInstance(tgt.getClient().getName(), Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL);
        }
        Krb5InitCredential krb5InitCredential = new Krb5InitCredential(krb5NameElement, tgt.getEncoded(), tgt.getClient(), KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketGetClientAlias(tgt), tgt.getServer(), KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketGetServerAlias(tgt), tgt.getSessionKey().getEncoded(), tgt.getSessionKeyType(), tgt.getFlags(), tgt.getAuthTime(), tgt.getStartTime(), tgt.getEndTime(), tgt.getRenewTill(), tgt.getClientAddresses());
        krb5InitCredential.proxyTicket = KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketGetProxy(tgt);
        return krb5InitCredential;
    }

    static Krb5InitCredential getInstance(Krb5NameElement krb5NameElement, Credentials credentials) throws GSSException {
        EncryptionKey sessionKey = credentials.getSessionKey();
        PrincipalName client = credentials.getClient();
        PrincipalName clientAlias = credentials.getClientAlias();
        PrincipalName server = credentials.getServer();
        PrincipalName serverAlias = credentials.getServerAlias();
        KerberosPrincipal kerberosPrincipal = null;
        KerberosPrincipal kerberosPrincipal2 = null;
        KerberosPrincipal kerberosPrincipal3 = null;
        KerberosPrincipal kerberosPrincipal4 = null;
        Krb5NameElement krb5NameElement2 = null;
        if (client != null) {
            String name = client.getName();
            krb5NameElement2 = Krb5NameElement.getInstance(name, Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL);
            kerberosPrincipal = new KerberosPrincipal(name);
        }
        if (clientAlias != null) {
            kerberosPrincipal2 = new KerberosPrincipal(clientAlias.getName());
        }
        if (server != null) {
            kerberosPrincipal3 = new KerberosPrincipal(server.getName(), 2);
        }
        if (serverAlias != null) {
            kerberosPrincipal4 = new KerberosPrincipal(serverAlias.getName());
        }
        return new Krb5InitCredential(krb5NameElement2, credentials, credentials.getEncoded(), kerberosPrincipal, kerberosPrincipal2, kerberosPrincipal3, kerberosPrincipal4, sessionKey.getBytes(), sessionKey.getEType(), credentials.getFlags(), credentials.getAuthTime(), credentials.getStartTime(), credentials.getEndTime(), credentials.getRenewTill(), credentials.getClientAddresses());
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public final GSSNameSpi getName() throws GSSException {
        return this.name;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getInitLifetime() throws GSSException {
        Date endTime = getEndTime();
        if (endTime == null) {
            return 0;
        }
        return (int) ((endTime.getTime() - System.currentTimeMillis()) / 1000);
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

    Credentials getKrb5Credentials() {
        return this.krb5Credentials;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public void dispose() throws GSSException {
        try {
            destroy();
        } catch (DestroyFailedException e2) {
            new GSSException(11, -1, "Could not destroy credentials - " + e2.getMessage()).initCause(e2);
        }
    }

    private static KerberosTicket getTgt(GSSCaller gSSCaller, Krb5NameElement krb5NameElement, int i2) throws GSSException {
        String name;
        if (krb5NameElement != null) {
            name = krb5NameElement.getKrb5PrincipalName().getName();
        } else {
            name = null;
        }
        final AccessControlContext context = AccessController.getContext();
        try {
            final GSSCaller gSSCaller2 = gSSCaller == GSSCaller.CALLER_UNKNOWN ? GSSCaller.CALLER_INITIATE : gSSCaller;
            final String str = name;
            return (KerberosTicket) AccessController.doPrivileged(new PrivilegedExceptionAction<KerberosTicket>() { // from class: sun.security.jgss.krb5.Krb5InitCredential.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public KerberosTicket run() throws Exception {
                    return Krb5Util.getInitialTicket(gSSCaller2, str, context);
                }
            });
        } catch (PrivilegedActionException e2) {
            GSSException gSSException = new GSSException(13, -1, "Attempt to obtain new INITIATE credentials failed! (" + e2.getMessage() + ")");
            gSSException.initCause(e2.getException());
            throw gSSException;
        }
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public GSSCredentialSpi impersonate(GSSNameSpi gSSNameSpi) throws GSSException {
        try {
            Krb5NameElement krb5NameElement = (Krb5NameElement) gSSNameSpi;
            return new Krb5ProxyCredential(this, krb5NameElement, Credentials.acquireS4U2selfCreds(krb5NameElement.getKrb5PrincipalName(), this.krb5Credentials).getTicket());
        } catch (IOException | KrbException e2) {
            GSSException gSSException = new GSSException(11, -1, "Attempt to obtain S4U2self credentials failed!");
            gSSException.initCause(e2);
            throw gSSException;
        }
    }
}
