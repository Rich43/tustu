package sun.security.jgss.krb5;

import java.security.Provider;
import java.util.Vector;
import javax.security.auth.kerberos.ServicePermission;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.SunProvider;
import sun.security.jgss.spi.GSSContextSpi;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spi.MechanismFactory;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/security/jgss/krb5/Krb5MechFactory.class */
public final class Krb5MechFactory implements MechanismFactory {
    private static final boolean DEBUG = Krb5Util.DEBUG;
    static final Provider PROVIDER = new SunProvider();
    static final Oid GSS_KRB5_MECH_OID = createOid("1.2.840.113554.1.2.2");
    static final Oid NT_GSS_KRB5_PRINCIPAL = createOid("1.2.840.113554.1.2.2.1");
    private static Oid[] nameTypes = {GSSName.NT_USER_NAME, GSSName.NT_HOSTBASED_SERVICE, GSSName.NT_EXPORT_NAME, NT_GSS_KRB5_PRINCIPAL};
    private final GSSCaller caller;

    private static Krb5CredElement getCredFromSubject(GSSNameSpi gSSNameSpi, boolean z2) throws GSSException {
        Vector vectorSearchSubject = GSSUtil.searchSubject(gSSNameSpi, GSS_KRB5_MECH_OID, z2, z2 ? Krb5InitCredential.class : Krb5AcceptCredential.class);
        Krb5CredElement krb5CredElement = (vectorSearchSubject == null || vectorSearchSubject.isEmpty()) ? null : (Krb5CredElement) vectorSearchSubject.firstElement();
        if (krb5CredElement != null) {
            if (z2) {
                checkInitCredPermission((Krb5NameElement) krb5CredElement.getName());
            } else {
                checkAcceptCredPermission((Krb5NameElement) krb5CredElement.getName(), gSSNameSpi);
            }
        }
        return krb5CredElement;
    }

    public Krb5MechFactory(GSSCaller gSSCaller) {
        this.caller = gSSCaller;
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSNameSpi getNameElement(String str, Oid oid) throws GSSException {
        return Krb5NameElement.getInstance(str, oid);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSNameSpi getNameElement(byte[] bArr, Oid oid) throws GSSException {
        return Krb5NameElement.getInstance(new String(bArr), oid);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSCredentialSpi getCredentialElement(GSSNameSpi gSSNameSpi, int i2, int i3, int i4) throws GSSException {
        if (gSSNameSpi != null && !(gSSNameSpi instanceof Krb5NameElement)) {
            gSSNameSpi = Krb5NameElement.getInstance(gSSNameSpi.toString(), gSSNameSpi.getStringNameType());
        }
        Krb5CredElement credFromSubject = getCredFromSubject(gSSNameSpi, i4 != 2);
        if (credFromSubject == null) {
            if (i4 == 1 || i4 == 0) {
                credFromSubject = Krb5ProxyCredential.tryImpersonation(this.caller, Krb5InitCredential.getInstance(this.caller, (Krb5NameElement) gSSNameSpi, i2));
                checkInitCredPermission((Krb5NameElement) credFromSubject.getName());
            } else if (i4 == 2) {
                credFromSubject = Krb5AcceptCredential.getInstance(this.caller, (Krb5NameElement) gSSNameSpi);
                checkAcceptCredPermission((Krb5NameElement) credFromSubject.getName(), gSSNameSpi);
            } else {
                throw new GSSException(11, -1, "Unknown usage mode requested");
            }
        }
        return credFromSubject;
    }

    public static void checkInitCredPermission(Krb5NameElement krb5NameElement) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            String realmAsString = krb5NameElement.getKrb5PrincipalName().getRealmAsString();
            try {
                securityManager.checkPermission(new ServicePermission(new String("krbtgt/" + realmAsString + '@' + realmAsString), "initiate"));
            } catch (SecurityException e2) {
                if (DEBUG) {
                    System.out.println("Permission to initiatekerberos init credential" + e2.getMessage());
                }
                throw e2;
            }
        }
    }

    public static void checkAcceptCredPermission(Krb5NameElement krb5NameElement, GSSNameSpi gSSNameSpi) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && krb5NameElement != null) {
            try {
                securityManager.checkPermission(new ServicePermission(krb5NameElement.getKrb5PrincipalName().getName(), SecurityConstants.SOCKET_ACCEPT_ACTION));
            } catch (SecurityException e2) {
                e = e2;
                if (gSSNameSpi == null) {
                    e = new SecurityException("No permission to acquire Kerberos accept credential");
                }
                throw e;
            }
        }
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(GSSNameSpi gSSNameSpi, GSSCredentialSpi gSSCredentialSpi, int i2) throws GSSException {
        if (gSSNameSpi != null && !(gSSNameSpi instanceof Krb5NameElement)) {
            gSSNameSpi = Krb5NameElement.getInstance(gSSNameSpi.toString(), gSSNameSpi.getStringNameType());
        }
        if (gSSCredentialSpi == null) {
            gSSCredentialSpi = getCredentialElement(null, i2, 0, 1);
        }
        return new Krb5Context(this.caller, (Krb5NameElement) gSSNameSpi, (Krb5CredElement) gSSCredentialSpi, i2);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(GSSCredentialSpi gSSCredentialSpi) throws GSSException {
        if (gSSCredentialSpi == null) {
            gSSCredentialSpi = getCredentialElement(null, 0, Integer.MAX_VALUE, 2);
        }
        return new Krb5Context(this.caller, (Krb5CredElement) gSSCredentialSpi);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(byte[] bArr) throws GSSException {
        return new Krb5Context(this.caller, bArr);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public final Oid getMechanismOid() {
        return GSS_KRB5_MECH_OID;
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public Provider getProvider() {
        return PROVIDER;
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public Oid[] getNameTypes() {
        return nameTypes;
    }

    private static Oid createOid(String str) {
        Oid oid = null;
        try {
            oid = new Oid(str);
        } catch (GSSException e2) {
        }
        return oid;
    }
}
