package sun.security.jgss.spi;

import java.security.Provider;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

/* loaded from: rt.jar:sun/security/jgss/spi/MechanismFactory.class */
public interface MechanismFactory {
    Oid getMechanismOid();

    Provider getProvider();

    Oid[] getNameTypes() throws GSSException;

    GSSCredentialSpi getCredentialElement(GSSNameSpi gSSNameSpi, int i2, int i3, int i4) throws GSSException;

    GSSNameSpi getNameElement(String str, Oid oid) throws GSSException;

    GSSNameSpi getNameElement(byte[] bArr, Oid oid) throws GSSException;

    GSSContextSpi getMechanismContext(GSSNameSpi gSSNameSpi, GSSCredentialSpi gSSCredentialSpi, int i2) throws GSSException;

    GSSContextSpi getMechanismContext(GSSCredentialSpi gSSCredentialSpi) throws GSSException;

    GSSContextSpi getMechanismContext(byte[] bArr) throws GSSException;
}
