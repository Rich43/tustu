package sun.security.jgss;

import com.sun.security.jgss.ExtendedGSSCredential;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spnego.SpNegoCredElement;

/* loaded from: rt.jar:sun/security/jgss/GSSCredentialImpl.class */
public class GSSCredentialImpl implements ExtendedGSSCredential {
    private GSSManagerImpl gssManager;
    private boolean destroyed;
    private Hashtable<SearchKey, GSSCredentialSpi> hashtable;
    private GSSCredentialSpi tempCred;

    GSSCredentialImpl(GSSManagerImpl gSSManagerImpl, int i2) throws GSSException {
        this(gSSManagerImpl, (GSSName) null, 0, (Oid[]) null, i2);
    }

    GSSCredentialImpl(GSSManagerImpl gSSManagerImpl, GSSName gSSName, int i2, Oid oid, int i3) throws GSSException {
        this.gssManager = null;
        this.destroyed = false;
        this.hashtable = null;
        this.tempCred = null;
        oid = oid == null ? ProviderList.DEFAULT_MECH_OID : oid;
        init(gSSManagerImpl);
        add(gSSName, i2, i2, oid, i3);
    }

    GSSCredentialImpl(GSSManagerImpl gSSManagerImpl, GSSName gSSName, int i2, Oid[] oidArr, int i3) throws GSSException {
        this.gssManager = null;
        this.destroyed = false;
        this.hashtable = null;
        this.tempCred = null;
        init(gSSManagerImpl);
        boolean z2 = false;
        if (oidArr == null) {
            oidArr = gSSManagerImpl.getMechs();
            z2 = true;
        }
        for (int i4 = 0; i4 < oidArr.length; i4++) {
            try {
                add(gSSName, i2, i2, oidArr[i4], i3);
            } catch (GSSException e2) {
                if (z2) {
                    GSSUtil.debug("Ignore " + ((Object) e2) + " while acquring cred for " + ((Object) oidArr[i4]));
                } else {
                    throw e2;
                }
            }
        }
        if (this.hashtable.size() == 0 || i3 != getUsage()) {
            throw new GSSException(13);
        }
    }

    public GSSCredentialImpl(GSSManagerImpl gSSManagerImpl, GSSCredentialSpi gSSCredentialSpi) throws GSSException {
        this.gssManager = null;
        this.destroyed = false;
        this.hashtable = null;
        this.tempCred = null;
        init(gSSManagerImpl);
        int i2 = 2;
        if (gSSCredentialSpi.isInitiatorCredential()) {
            if (gSSCredentialSpi.isAcceptorCredential()) {
                i2 = 0;
            } else {
                i2 = 1;
            }
        }
        SearchKey searchKey = new SearchKey(gSSCredentialSpi.getMechanism(), i2);
        this.tempCred = gSSCredentialSpi;
        this.hashtable.put(searchKey, this.tempCred);
        if (!GSSUtil.isSpNegoMech(gSSCredentialSpi.getMechanism())) {
            this.hashtable.put(new SearchKey(GSSUtil.GSS_SPNEGO_MECH_OID, i2), new SpNegoCredElement(gSSCredentialSpi));
        }
    }

    void init(GSSManagerImpl gSSManagerImpl) {
        this.gssManager = gSSManagerImpl;
        this.hashtable = new Hashtable<>(gSSManagerImpl.getMechs().length);
    }

    @Override // org.ietf.jgss.GSSCredential
    public void dispose() throws GSSException {
        if (!this.destroyed) {
            Enumeration<GSSCredentialSpi> enumerationElements = this.hashtable.elements();
            while (enumerationElements.hasMoreElements()) {
                enumerationElements.nextElement2().dispose();
            }
            this.destroyed = true;
        }
    }

    @Override // com.sun.security.jgss.ExtendedGSSCredential
    public GSSCredential impersonate(GSSName gSSName) throws GSSException {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        GSSCredentialSpi gSSCredentialSpiImpersonate = this.tempCred.impersonate(gSSName == null ? null : ((GSSNameImpl) gSSName).getElement(this.tempCred.getMechanism()));
        if (gSSCredentialSpiImpersonate == null) {
            return null;
        }
        return new GSSCredentialImpl(this.gssManager, gSSCredentialSpiImpersonate);
    }

    @Override // org.ietf.jgss.GSSCredential
    public GSSName getName() throws GSSException {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        return GSSNameImpl.wrapElement(this.gssManager, this.tempCred.getName());
    }

    @Override // org.ietf.jgss.GSSCredential
    public GSSName getName(Oid oid) throws GSSException {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        if (oid == null) {
            oid = ProviderList.DEFAULT_MECH_OID;
        }
        GSSCredentialSpi gSSCredentialSpi = this.hashtable.get(new SearchKey(oid, 1));
        if (gSSCredentialSpi == null) {
            gSSCredentialSpi = this.hashtable.get(new SearchKey(oid, 2));
        }
        if (gSSCredentialSpi == null) {
            gSSCredentialSpi = this.hashtable.get(new SearchKey(oid, 0));
        }
        if (gSSCredentialSpi == null) {
            throw new GSSExceptionImpl(2, oid);
        }
        return GSSNameImpl.wrapElement(this.gssManager, gSSCredentialSpi.getName());
    }

    @Override // org.ietf.jgss.GSSCredential
    public int getRemainingLifetime() throws GSSException {
        int acceptLifetime;
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        int i2 = Integer.MAX_VALUE;
        Enumeration<SearchKey> enumerationKeys = this.hashtable.keys();
        while (enumerationKeys.hasMoreElements()) {
            SearchKey searchKeyNextElement2 = enumerationKeys.nextElement2();
            GSSCredentialSpi gSSCredentialSpi = this.hashtable.get(searchKeyNextElement2);
            if (searchKeyNextElement2.getUsage() == 1) {
                acceptLifetime = gSSCredentialSpi.getInitLifetime();
            } else if (searchKeyNextElement2.getUsage() == 2) {
                acceptLifetime = gSSCredentialSpi.getAcceptLifetime();
            } else {
                int initLifetime = gSSCredentialSpi.getInitLifetime();
                int acceptLifetime2 = gSSCredentialSpi.getAcceptLifetime();
                acceptLifetime = initLifetime < acceptLifetime2 ? initLifetime : acceptLifetime2;
            }
            if (i2 > acceptLifetime) {
                i2 = acceptLifetime;
            }
        }
        return i2;
    }

    @Override // org.ietf.jgss.GSSCredential
    public int getRemainingInitLifetime(Oid oid) throws GSSException {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        boolean z2 = false;
        int initLifetime = 0;
        if (oid == null) {
            oid = ProviderList.DEFAULT_MECH_OID;
        }
        GSSCredentialSpi gSSCredentialSpi = this.hashtable.get(new SearchKey(oid, 1));
        if (gSSCredentialSpi != null) {
            z2 = true;
            if (0 < gSSCredentialSpi.getInitLifetime()) {
                initLifetime = gSSCredentialSpi.getInitLifetime();
            }
        }
        GSSCredentialSpi gSSCredentialSpi2 = this.hashtable.get(new SearchKey(oid, 0));
        if (gSSCredentialSpi2 != null) {
            z2 = true;
            if (initLifetime < gSSCredentialSpi2.getInitLifetime()) {
                initLifetime = gSSCredentialSpi2.getInitLifetime();
            }
        }
        if (!z2) {
            throw new GSSExceptionImpl(2, oid);
        }
        return initLifetime;
    }

    @Override // org.ietf.jgss.GSSCredential
    public int getRemainingAcceptLifetime(Oid oid) throws GSSException {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        boolean z2 = false;
        int acceptLifetime = 0;
        if (oid == null) {
            oid = ProviderList.DEFAULT_MECH_OID;
        }
        GSSCredentialSpi gSSCredentialSpi = this.hashtable.get(new SearchKey(oid, 2));
        if (gSSCredentialSpi != null) {
            z2 = true;
            if (0 < gSSCredentialSpi.getAcceptLifetime()) {
                acceptLifetime = gSSCredentialSpi.getAcceptLifetime();
            }
        }
        GSSCredentialSpi gSSCredentialSpi2 = this.hashtable.get(new SearchKey(oid, 0));
        if (gSSCredentialSpi2 != null) {
            z2 = true;
            if (acceptLifetime < gSSCredentialSpi2.getAcceptLifetime()) {
                acceptLifetime = gSSCredentialSpi2.getAcceptLifetime();
            }
        }
        if (!z2) {
            throw new GSSExceptionImpl(2, oid);
        }
        return acceptLifetime;
    }

    @Override // org.ietf.jgss.GSSCredential
    public int getUsage() throws GSSException {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        boolean z2 = false;
        boolean z3 = false;
        Enumeration<SearchKey> enumerationKeys = this.hashtable.keys();
        while (enumerationKeys.hasMoreElements()) {
            SearchKey searchKeyNextElement2 = enumerationKeys.nextElement2();
            if (searchKeyNextElement2.getUsage() == 1) {
                z2 = true;
            } else if (searchKeyNextElement2.getUsage() == 2) {
                z3 = true;
            } else {
                return 0;
            }
        }
        if (z2) {
            if (z3) {
                return 0;
            }
            return 1;
        }
        return 2;
    }

    @Override // org.ietf.jgss.GSSCredential
    public int getUsage(Oid oid) throws GSSException {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        boolean z2 = false;
        boolean z3 = false;
        if (oid == null) {
            oid = ProviderList.DEFAULT_MECH_OID;
        }
        if (this.hashtable.get(new SearchKey(oid, 1)) != null) {
            z2 = true;
        }
        if (this.hashtable.get(new SearchKey(oid, 2)) != null) {
            z3 = true;
        }
        if (this.hashtable.get(new SearchKey(oid, 0)) != null) {
            z2 = true;
            z3 = true;
        }
        if (z2 && z3) {
            return 0;
        }
        if (z2) {
            return 1;
        }
        if (z3) {
            return 2;
        }
        throw new GSSExceptionImpl(2, oid);
    }

    @Override // org.ietf.jgss.GSSCredential
    public Oid[] getMechs() throws GSSException {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        Vector vector = new Vector(this.hashtable.size());
        Enumeration<SearchKey> enumerationKeys = this.hashtable.keys();
        while (enumerationKeys.hasMoreElements()) {
            vector.addElement(enumerationKeys.nextElement2().getMech());
        }
        return (Oid[]) vector.toArray(new Oid[0]);
    }

    @Override // org.ietf.jgss.GSSCredential
    public void add(GSSName gSSName, int i2, int i3, Oid oid, int i4) throws GSSException {
        int i5;
        int i6;
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        if (oid == null) {
            oid = ProviderList.DEFAULT_MECH_OID;
        }
        SearchKey searchKey = new SearchKey(oid, i4);
        if (this.hashtable.containsKey(searchKey)) {
            throw new GSSExceptionImpl(17, "Duplicate element found: " + getElementStr(oid, i4));
        }
        GSSNameSpi element = gSSName == null ? null : ((GSSNameImpl) gSSName).getElement(oid);
        this.tempCred = this.gssManager.getCredentialElement(element, i2, i3, oid, i4);
        if (this.tempCred != null) {
            if (i4 == 0 && (!this.tempCred.isAcceptorCredential() || !this.tempCred.isInitiatorCredential())) {
                if (!this.tempCred.isInitiatorCredential()) {
                    i5 = 2;
                    i6 = 1;
                } else {
                    i5 = 1;
                    i6 = 2;
                }
                this.hashtable.put(new SearchKey(oid, i5), this.tempCred);
                this.tempCred = this.gssManager.getCredentialElement(element, i2, i3, oid, i6);
                this.hashtable.put(new SearchKey(oid, i6), this.tempCred);
                return;
            }
            this.hashtable.put(searchKey, this.tempCred);
        }
    }

    @Override // org.ietf.jgss.GSSCredential
    public boolean equals(Object obj) {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GSSCredentialImpl)) {
            return false;
        }
        return false;
    }

    @Override // org.ietf.jgss.GSSCredential
    public int hashCode() {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        return 1;
    }

    public GSSCredentialSpi getElement(Oid oid, boolean z2) throws GSSException {
        SearchKey searchKey;
        GSSCredentialSpi gSSCredentialSpi;
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        if (oid == null) {
            oid = ProviderList.DEFAULT_MECH_OID;
            gSSCredentialSpi = this.hashtable.get(new SearchKey(oid, z2 ? 1 : 2));
            if (gSSCredentialSpi == null) {
                gSSCredentialSpi = this.hashtable.get(new SearchKey(oid, 0));
                if (gSSCredentialSpi == null) {
                    for (Object obj : this.hashtable.entrySet().toArray()) {
                        gSSCredentialSpi = (GSSCredentialSpi) ((Map.Entry) obj).getValue();
                        if (gSSCredentialSpi.isInitiatorCredential() == z2) {
                            break;
                        }
                    }
                }
            }
        } else {
            if (z2) {
                searchKey = new SearchKey(oid, 1);
            } else {
                searchKey = new SearchKey(oid, 2);
            }
            gSSCredentialSpi = this.hashtable.get(searchKey);
            if (gSSCredentialSpi == null) {
                gSSCredentialSpi = this.hashtable.get(new SearchKey(oid, 0));
            }
        }
        if (gSSCredentialSpi == null) {
            throw new GSSExceptionImpl(13, "No credential found for: " + getElementStr(oid, z2 ? 1 : 2));
        }
        return gSSCredentialSpi;
    }

    Set<GSSCredentialSpi> getElements() {
        HashSet hashSet = new HashSet(this.hashtable.size());
        Enumeration<GSSCredentialSpi> enumerationElements = this.hashtable.elements();
        while (enumerationElements.hasMoreElements()) {
            hashSet.add(enumerationElements.nextElement2());
        }
        return hashSet;
    }

    private static String getElementStr(Oid oid, int i2) {
        String strConcat;
        String string = oid.toString();
        if (i2 == 1) {
            strConcat = string.concat(" usage: Initiate");
        } else if (i2 == 2) {
            strConcat = string.concat(" usage: Accept");
        } else {
            strConcat = string.concat(" usage: Initiate and Accept");
        }
        return strConcat;
    }

    public String toString() {
        if (this.destroyed) {
            throw new IllegalStateException("This credential is no longer valid");
        }
        StringBuffer stringBuffer = new StringBuffer("[GSSCredential: ");
        for (Object obj : this.hashtable.entrySet().toArray()) {
            try {
                stringBuffer.append('\n');
                GSSCredentialSpi gSSCredentialSpi = (GSSCredentialSpi) ((Map.Entry) obj).getValue();
                stringBuffer.append((Object) gSSCredentialSpi.getName());
                stringBuffer.append(' ');
                stringBuffer.append((Object) gSSCredentialSpi.getMechanism());
                stringBuffer.append(gSSCredentialSpi.isInitiatorCredential() ? " Initiate" : "");
                stringBuffer.append(gSSCredentialSpi.isAcceptorCredential() ? " Accept" : "");
                stringBuffer.append(" [");
                stringBuffer.append((Object) gSSCredentialSpi.getClass());
                stringBuffer.append(']');
            } catch (GSSException e2) {
            }
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }

    /* loaded from: rt.jar:sun/security/jgss/GSSCredentialImpl$SearchKey.class */
    static class SearchKey {
        private Oid mechOid;
        private int usage;

        public SearchKey(Oid oid, int i2) {
            this.mechOid = null;
            this.usage = 0;
            this.mechOid = oid;
            this.usage = i2;
        }

        public Oid getMech() {
            return this.mechOid;
        }

        public int getUsage() {
            return this.usage;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof SearchKey)) {
                return false;
            }
            SearchKey searchKey = (SearchKey) obj;
            return this.mechOid.equals(searchKey.mechOid) && this.usage == searchKey.usage;
        }

        public int hashCode() {
            return this.mechOid.hashCode();
        }
    }
}
