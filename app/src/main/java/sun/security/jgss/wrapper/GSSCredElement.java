package sun.security.jgss.wrapper;

import java.security.Provider;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/security/jgss/wrapper/GSSCredElement.class */
public class GSSCredElement implements GSSCredentialSpi {
    private int usage;
    long pCred;
    private GSSNameElement name;
    private GSSLibStub cStub;

    void doServicePermCheck() throws GSSException {
        if (GSSUtil.isKerberosMech(this.cStub.getMech()) && System.getSecurityManager() != null) {
            if (isInitiatorCredential()) {
                Krb5Util.checkServicePermission(Krb5Util.getTGSName(this.name), "initiate");
            }
            if (isAcceptorCredential() && this.name != GSSNameElement.DEF_ACCEPTOR) {
                Krb5Util.checkServicePermission(this.name.getKrbName(), SecurityConstants.SOCKET_ACCEPT_ACTION);
            }
        }
    }

    GSSCredElement(long j2, GSSNameElement gSSNameElement, Oid oid) throws GSSException {
        this.name = null;
        this.pCred = j2;
        this.cStub = GSSLibStub.getInstance(oid);
        this.usage = 1;
        this.name = gSSNameElement;
    }

    GSSCredElement(GSSNameElement gSSNameElement, int i2, int i3, GSSLibStub gSSLibStub) throws GSSException {
        this.name = null;
        this.cStub = gSSLibStub;
        this.usage = i3;
        if (gSSNameElement != null) {
            this.name = gSSNameElement;
            doServicePermCheck();
            this.pCred = this.cStub.acquireCred(this.name.pName, i2, i3);
        } else {
            this.pCred = this.cStub.acquireCred(0L, i2, i3);
            this.name = new GSSNameElement(this.cStub.getCredName(this.pCred), this.cStub);
            doServicePermCheck();
        }
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public Provider getProvider() {
        return SunNativeProvider.INSTANCE;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public void dispose() throws GSSException {
        this.name = null;
        if (this.pCred != 0) {
            this.pCred = this.cStub.releaseCred(this.pCred);
        }
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public GSSNameElement getName() throws GSSException {
        if (this.name == GSSNameElement.DEF_ACCEPTOR) {
            return null;
        }
        return this.name;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getInitLifetime() throws GSSException {
        if (isInitiatorCredential()) {
            return this.cStub.getCredTime(this.pCred);
        }
        return 0;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public int getAcceptLifetime() throws GSSException {
        if (isAcceptorCredential()) {
            return this.cStub.getCredTime(this.pCred);
        }
        return 0;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public boolean isInitiatorCredential() {
        return this.usage != 2;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public boolean isAcceptorCredential() {
        return this.usage != 1;
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public Oid getMechanism() {
        return this.cStub.getMech();
    }

    public String toString() {
        return "N/A";
    }

    protected void finalize() throws Throwable {
        dispose();
    }

    @Override // sun.security.jgss.spi.GSSCredentialSpi
    public GSSCredentialSpi impersonate(GSSNameSpi gSSNameSpi) throws GSSException {
        throw new GSSException(11, -1, "Not supported yet");
    }
}
