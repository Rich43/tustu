package sun.security.jgss.spnego;

import java.security.Provider;
import java.util.Vector;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.GSSManagerImpl;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.ProviderList;
import sun.security.jgss.SunProvider;
import sun.security.jgss.krb5.Krb5AcceptCredential;
import sun.security.jgss.krb5.Krb5InitCredential;
import sun.security.jgss.krb5.Krb5MechFactory;
import sun.security.jgss.krb5.Krb5NameElement;
import sun.security.jgss.spi.GSSContextSpi;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spi.MechanismFactory;

/* loaded from: rt.jar:sun/security/jgss/spnego/SpNegoMechFactory.class */
public final class SpNegoMechFactory implements MechanismFactory {
    static final Provider PROVIDER = new SunProvider();
    static final Oid GSS_SPNEGO_MECH_OID = GSSUtil.createOid("1.3.6.1.5.5.2");
    private static Oid[] nameTypes = {GSSName.NT_USER_NAME, GSSName.NT_HOSTBASED_SERVICE, GSSName.NT_EXPORT_NAME};
    private static final Oid DEFAULT_SPNEGO_MECH_OID;
    final GSSManagerImpl manager;
    final Oid[] availableMechs;

    static {
        DEFAULT_SPNEGO_MECH_OID = ProviderList.DEFAULT_MECH_OID.equals(GSS_SPNEGO_MECH_OID) ? GSSUtil.GSS_KRB5_MECH_OID : ProviderList.DEFAULT_MECH_OID;
    }

    private static SpNegoCredElement getCredFromSubject(GSSNameSpi gSSNameSpi, boolean z2) throws GSSException {
        Vector vectorSearchSubject = GSSUtil.searchSubject(gSSNameSpi, GSS_SPNEGO_MECH_OID, z2, SpNegoCredElement.class);
        SpNegoCredElement spNegoCredElement = (vectorSearchSubject == null || vectorSearchSubject.isEmpty()) ? null : (SpNegoCredElement) vectorSearchSubject.firstElement();
        if (spNegoCredElement != null) {
            GSSCredentialSpi internalCred = spNegoCredElement.getInternalCred();
            if (GSSUtil.isKerberosMech(internalCred.getMechanism())) {
                if (z2) {
                    Krb5MechFactory.checkInitCredPermission((Krb5NameElement) ((Krb5InitCredential) internalCred).getName());
                } else {
                    Krb5MechFactory.checkAcceptCredPermission((Krb5NameElement) ((Krb5AcceptCredential) internalCred).getName(), gSSNameSpi);
                }
            }
        }
        return spNegoCredElement;
    }

    public SpNegoMechFactory(GSSCaller gSSCaller) {
        this.manager = new GSSManagerImpl(gSSCaller, false);
        Oid[] mechs = this.manager.getMechs();
        this.availableMechs = new Oid[mechs.length - 1];
        int i2 = 0;
        for (int i3 = 0; i3 < mechs.length; i3++) {
            if (!mechs[i3].equals(GSS_SPNEGO_MECH_OID)) {
                int i4 = i2;
                i2++;
                this.availableMechs[i4] = mechs[i3];
            }
        }
        for (int i5 = 0; i5 < this.availableMechs.length; i5++) {
            if (this.availableMechs[i5].equals(DEFAULT_SPNEGO_MECH_OID)) {
                if (i5 != 0) {
                    this.availableMechs[i5] = this.availableMechs[0];
                    this.availableMechs[0] = DEFAULT_SPNEGO_MECH_OID;
                    return;
                }
                return;
            }
        }
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSNameSpi getNameElement(String str, Oid oid) throws GSSException {
        return this.manager.getNameElement(str, oid, DEFAULT_SPNEGO_MECH_OID);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSNameSpi getNameElement(byte[] bArr, Oid oid) throws GSSException {
        return this.manager.getNameElement(bArr, oid, DEFAULT_SPNEGO_MECH_OID);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSCredentialSpi getCredentialElement(GSSNameSpi gSSNameSpi, int i2, int i3, int i4) throws GSSException {
        SpNegoCredElement credFromSubject = getCredFromSubject(gSSNameSpi, i4 != 2);
        if (credFromSubject == null) {
            credFromSubject = new SpNegoCredElement(this.manager.getCredentialElement(gSSNameSpi, i2, i3, null, i4));
        }
        return credFromSubject;
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(GSSNameSpi gSSNameSpi, GSSCredentialSpi gSSCredentialSpi, int i2) throws GSSException {
        if (gSSCredentialSpi == null) {
            gSSCredentialSpi = getCredFromSubject(null, true);
        } else if (!(gSSCredentialSpi instanceof SpNegoCredElement)) {
            return new SpNegoContext(this, gSSNameSpi, new SpNegoCredElement(gSSCredentialSpi), i2);
        }
        return new SpNegoContext(this, gSSNameSpi, gSSCredentialSpi, i2);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(GSSCredentialSpi gSSCredentialSpi) throws GSSException {
        if (gSSCredentialSpi == null) {
            gSSCredentialSpi = getCredFromSubject(null, false);
        } else if (!(gSSCredentialSpi instanceof SpNegoCredElement)) {
            return new SpNegoContext(this, new SpNegoCredElement(gSSCredentialSpi));
        }
        return new SpNegoContext(this, gSSCredentialSpi);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(byte[] bArr) throws GSSException {
        return new SpNegoContext(this, bArr);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public final Oid getMechanismOid() {
        return GSS_SPNEGO_MECH_OID;
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public Provider getProvider() {
        return PROVIDER;
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public Oid[] getNameTypes() {
        return nameTypes;
    }
}
