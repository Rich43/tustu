package sun.security.jgss.krb5;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Provider;
import javax.security.auth.DestroyFailedException;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.PrincipalName;

/* loaded from: rt.jar:sun/security/jgss/krb5/Krb5AcceptCredential.class */
public class Krb5AcceptCredential implements Krb5CredElement {
    private final Krb5NameElement name;
    private final ServiceCreds screds;

    private Krb5AcceptCredential(Krb5NameElement krb5NameElement, ServiceCreds serviceCreds) {
        this.name = krb5NameElement;
        this.screds = serviceCreds;
    }

    static Krb5AcceptCredential getInstance(final GSSCaller gSSCaller, Krb5NameElement krb5NameElement) throws GSSException {
        String name;
        final String name2 = krb5NameElement == null ? null : krb5NameElement.getKrb5PrincipalName().getName();
        final AccessControlContext context = AccessController.getContext();
        try {
            ServiceCreds serviceCreds = (ServiceCreds) AccessController.doPrivileged(new PrivilegedExceptionAction<ServiceCreds>() { // from class: sun.security.jgss.krb5.Krb5AcceptCredential.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public ServiceCreds run() throws Exception {
                    return Krb5Util.getServiceCreds(gSSCaller == GSSCaller.CALLER_UNKNOWN ? GSSCaller.CALLER_ACCEPT : gSSCaller, name2, context);
                }
            });
            if (serviceCreds == null) {
                throw new GSSException(13, -1, "Failed to find any Kerberos credentails");
            }
            if (krb5NameElement == null && (name = serviceCreds.getName()) != null) {
                krb5NameElement = Krb5NameElement.getInstance(name, Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL);
            }
            return new Krb5AcceptCredential(krb5NameElement, serviceCreds);
        } catch (PrivilegedActionException e2) {
            GSSException gSSException = new GSSException(13, -1, "Attempt to obtain new ACCEPT credentials failed!");
            gSSException.initCause(e2.getException());
            throw gSSException;
        }
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public final GSSNameSpi getName() throws GSSException {
        return this.name;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getInitLifetime() throws GSSException {
        return 0;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getAcceptLifetime() throws GSSException {
        return Integer.MAX_VALUE;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public boolean isInitiatorCredential() throws GSSException {
        return false;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public boolean isAcceptorCredential() throws GSSException {
        return true;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public final Oid getMechanism() {
        return Krb5MechFactory.GSS_KRB5_MECH_OID;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public final Provider getProvider() {
        return Krb5MechFactory.PROVIDER;
    }

    public EncryptionKey[] getKrb5EncryptionKeys(PrincipalName principalName) {
        return this.screds.getEKeys(principalName);
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public void dispose() throws GSSException {
        try {
            destroy();
        } catch (DestroyFailedException e2) {
            new GSSException(11, -1, "Could not destroy credentials - " + e2.getMessage()).initCause(e2);
        }
    }

    public void destroy() throws DestroyFailedException {
        this.screds.destroy();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public GSSCredentialSpi impersonate(GSSNameSpi gSSNameSpi) throws GSSException {
        Credentials initCred = this.screds.getInitCred();
        if (initCred != null) {
            return Krb5InitCredential.getInstance(this.name, initCred).impersonate(gSSNameSpi);
        }
        throw new GSSException(11, -1, "Only an initiate credentials can impersonate");
    }
}
