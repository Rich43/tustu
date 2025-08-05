package sun.security.jgss.spi;

import java.security.Provider;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

/* loaded from: rt.jar:sun/security/jgss/spi/GSSCredentialSpi.class */
public interface GSSCredentialSpi {
    Provider getProvider();

    void dispose() throws GSSException;

    GSSNameSpi getName() throws GSSException;

    int getInitLifetime() throws GSSException;

    int getAcceptLifetime() throws GSSException;

    boolean isInitiatorCredential() throws GSSException;

    boolean isAcceptorCredential() throws GSSException;

    Oid getMechanism();

    GSSCredentialSpi impersonate(GSSNameSpi gSSNameSpi) throws GSSException;
}
