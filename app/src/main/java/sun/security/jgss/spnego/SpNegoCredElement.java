package sun.security.jgss.spnego;

import java.security.Provider;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;

/* loaded from: rt.jar:sun/security/jgss/spnego/SpNegoCredElement.class */
public class SpNegoCredElement implements GSSCredentialSpi {
    private GSSCredentialSpi cred;

    public SpNegoCredElement(GSSCredentialSpi gSSCredentialSpi) throws GSSException {
        this.cred = null;
        this.cred = gSSCredentialSpi;
    }

    Oid getInternalMech() {
        return this.cred.getMechanism();
    }

    public GSSCredentialSpi getInternalCred() {
        return this.cred;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public Provider getProvider() {
        return SpNegoMechFactory.PROVIDER;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public void dispose() throws GSSException {
        this.cred.dispose();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public GSSNameSpi getName() throws GSSException {
        return this.cred.getName();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getInitLifetime() throws GSSException {
        return this.cred.getInitLifetime();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getAcceptLifetime() throws GSSException {
        return this.cred.getAcceptLifetime();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public boolean isInitiatorCredential() throws GSSException {
        return this.cred.isInitiatorCredential();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public boolean isAcceptorCredential() throws GSSException {
        return this.cred.isAcceptorCredential();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public Oid getMechanism() {
        return GSSUtil.GSS_SPNEGO_MECH_OID;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public GSSCredentialSpi impersonate(GSSNameSpi gSSNameSpi) throws GSSException {
        return this.cred.impersonate(gSSNameSpi);
    }
}
