package sun.security.jgss.spi;

import java.security.Provider;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

/* loaded from: rt.jar:sun/security/jgss/spi/GSSNameSpi.class */
public interface GSSNameSpi {
    Provider getProvider();

    boolean equals(GSSNameSpi gSSNameSpi) throws GSSException;

    boolean equals(Object obj);

    int hashCode();

    byte[] export() throws GSSException;

    Oid getMechanism();

    String toString();

    Oid getStringNameType();

    boolean isAnonymousName();
}
