package sun.security.jgss;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.security.jgss.spi.GSSContextSpi;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spi.MechanismFactory;

/* loaded from: rt.jar:sun/security/jgss/GSSManagerImpl.class */
public class GSSManagerImpl extends GSSManager {
    private static final String USE_NATIVE_PROP = "sun.security.jgss.native";
    private static final Boolean USE_NATIVE = (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.security.jgss.GSSManagerImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Boolean run() {
            String property = System.getProperty("os.name");
            if (property.startsWith("SunOS") || property.contains("OS X") || property.startsWith("Linux")) {
                return new Boolean(System.getProperty(GSSManagerImpl.USE_NATIVE_PROP));
            }
            return Boolean.FALSE;
        }
    });
    private ProviderList list;

    public GSSManagerImpl(GSSCaller gSSCaller, boolean z2) {
        this.list = new ProviderList(gSSCaller, z2);
    }

    public GSSManagerImpl(GSSCaller gSSCaller) {
        this.list = new ProviderList(gSSCaller, USE_NATIVE.booleanValue());
    }

    public GSSManagerImpl() {
        this.list = new ProviderList(GSSCaller.CALLER_UNKNOWN, USE_NATIVE.booleanValue());
    }

    @Override // org.ietf.jgss.GSSManager
    public Oid[] getMechs() {
        return this.list.getMechs();
    }

    @Override // org.ietf.jgss.GSSManager
    public Oid[] getNamesForMech(Oid oid) throws GSSException {
        return (Oid[]) this.list.getMechFactory(oid).getNameTypes().clone();
    }

    @Override // org.ietf.jgss.GSSManager
    public Oid[] getMechsForName(Oid oid) {
        Oid[] mechs = this.list.getMechs();
        Oid[] oidArr = new Oid[mechs.length];
        int i2 = 0;
        if (oid.equals(GSSNameImpl.oldHostbasedServiceName)) {
            oid = GSSName.NT_HOSTBASED_SERVICE;
        }
        for (Oid oid2 : mechs) {
            try {
                if (oid.containedIn(getNamesForMech(oid2))) {
                    int i3 = i2;
                    i2++;
                    oidArr[i3] = oid2;
                }
            } catch (GSSException e2) {
                GSSUtil.debug("Skip " + ((Object) oid2) + ": error retrieving supported name types");
            }
        }
        if (i2 < oidArr.length) {
            Oid[] oidArr2 = new Oid[i2];
            for (int i4 = 0; i4 < i2; i4++) {
                oidArr2[i4] = oidArr[i4];
            }
            oidArr = oidArr2;
        }
        return oidArr;
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSName createName(String str, Oid oid) throws GSSException {
        return new GSSNameImpl(this, str, oid);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSName createName(byte[] bArr, Oid oid) throws GSSException {
        return new GSSNameImpl(this, bArr, oid);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSName createName(String str, Oid oid, Oid oid2) throws GSSException {
        return new GSSNameImpl(this, str, oid, oid2);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSName createName(byte[] bArr, Oid oid, Oid oid2) throws GSSException {
        return new GSSNameImpl(this, bArr, oid, oid2);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSCredential createCredential(int i2) throws GSSException {
        return new GSSCredentialImpl(this, i2);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSCredential createCredential(GSSName gSSName, int i2, Oid oid, int i3) throws GSSException {
        return new GSSCredentialImpl(this, gSSName, i2, oid, i3);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSCredential createCredential(GSSName gSSName, int i2, Oid[] oidArr, int i3) throws GSSException {
        return new GSSCredentialImpl(this, gSSName, i2, oidArr, i3);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSContext createContext(GSSName gSSName, Oid oid, GSSCredential gSSCredential, int i2) throws GSSException {
        return new GSSContextImpl(this, gSSName, oid, gSSCredential, i2);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSContext createContext(GSSCredential gSSCredential) throws GSSException {
        return new GSSContextImpl(this, gSSCredential);
    }

    @Override // org.ietf.jgss.GSSManager
    public GSSContext createContext(byte[] bArr) throws GSSException {
        return new GSSContextImpl(this, bArr);
    }

    @Override // org.ietf.jgss.GSSManager
    public void addProviderAtFront(Provider provider, Oid oid) throws GSSException {
        this.list.addProviderAtFront(provider, oid);
    }

    @Override // org.ietf.jgss.GSSManager
    public void addProviderAtEnd(Provider provider, Oid oid) throws GSSException {
        this.list.addProviderAtEnd(provider, oid);
    }

    public GSSCredentialSpi getCredentialElement(GSSNameSpi gSSNameSpi, int i2, int i3, Oid oid, int i4) throws GSSException {
        return this.list.getMechFactory(oid).getCredentialElement(gSSNameSpi, i2, i3, i4);
    }

    public GSSNameSpi getNameElement(String str, Oid oid, Oid oid2) throws GSSException {
        return this.list.getMechFactory(oid2).getNameElement(str, oid);
    }

    public GSSNameSpi getNameElement(byte[] bArr, Oid oid, Oid oid2) throws GSSException {
        return this.list.getMechFactory(oid2).getNameElement(bArr, oid);
    }

    GSSContextSpi getMechanismContext(GSSNameSpi gSSNameSpi, GSSCredentialSpi gSSCredentialSpi, int i2, Oid oid) throws GSSException {
        Provider provider = null;
        if (gSSCredentialSpi != null) {
            provider = gSSCredentialSpi.getProvider();
        }
        return this.list.getMechFactory(oid, provider).getMechanismContext(gSSNameSpi, gSSCredentialSpi, i2);
    }

    GSSContextSpi getMechanismContext(GSSCredentialSpi gSSCredentialSpi, Oid oid) throws GSSException {
        Provider provider = null;
        if (gSSCredentialSpi != null) {
            provider = gSSCredentialSpi.getProvider();
        }
        return this.list.getMechFactory(oid, provider).getMechanismContext(gSSCredentialSpi);
    }

    GSSContextSpi getMechanismContext(byte[] bArr) throws GSSException {
        if (bArr == null || bArr.length == 0) {
            throw new GSSException(12);
        }
        GSSContextSpi mechanismContext = null;
        for (Oid oid : this.list.getMechs()) {
            MechanismFactory mechFactory = this.list.getMechFactory(oid);
            if (mechFactory.getProvider().getName().equals("SunNativeGSS")) {
                mechanismContext = mechFactory.getMechanismContext(bArr);
                if (mechanismContext != null) {
                    break;
                }
            }
        }
        if (mechanismContext == null) {
            throw new GSSException(16);
        }
        return mechanismContext;
    }
}
