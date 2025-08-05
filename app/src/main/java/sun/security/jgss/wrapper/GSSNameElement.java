package sun.security.jgss.wrapper;

import java.io.IOException;
import java.security.Provider;
import javax.security.auth.kerberos.ServicePermission;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSExceptionImpl;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.krb5.Realm;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.ObjectIdentifier;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/jgss/wrapper/GSSNameElement.class */
public class GSSNameElement implements GSSNameSpi {
    long pName;
    private String printableName;
    private Oid printableType;
    private GSSLibStub cStub;
    static final GSSNameElement DEF_ACCEPTOR;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GSSNameElement.class.desiredAssertionStatus();
        DEF_ACCEPTOR = new GSSNameElement();
    }

    private static Oid getNativeNameType(Oid oid, GSSLibStub gSSLibStub) {
        if (GSSUtil.NT_GSS_KRB5_PRINCIPAL.equals(oid)) {
            Oid[] oidArrInquireNamesForMech = null;
            try {
                oidArrInquireNamesForMech = gSSLibStub.inquireNamesForMech();
            } catch (GSSException e2) {
                if (e2.getMajor() == 2 && GSSUtil.isSpNegoMech(gSSLibStub.getMech())) {
                    try {
                        oidArrInquireNamesForMech = GSSLibStub.getInstance(GSSUtil.GSS_KRB5_MECH_OID).inquireNamesForMech();
                    } catch (GSSException e3) {
                        SunNativeProvider.debug("Name type list unavailable: " + e3.getMajorString());
                    }
                } else {
                    SunNativeProvider.debug("Name type list unavailable: " + e2.getMajorString());
                }
            }
            if (oidArrInquireNamesForMech != null) {
                for (Oid oid2 : oidArrInquireNamesForMech) {
                    if (oid2.equals(oid)) {
                        return oid;
                    }
                }
                SunNativeProvider.debug("Override " + ((Object) oid) + " with mechanism default(null)");
                return null;
            }
        }
        return oid;
    }

    private GSSNameElement() {
        this.pName = 0L;
        this.printableName = "<DEFAULT ACCEPTOR>";
    }

    GSSNameElement(long j2, GSSLibStub gSSLibStub) throws GSSException {
        this.pName = 0L;
        if (!$assertionsDisabled && gSSLibStub == null) {
            throw new AssertionError();
        }
        if (j2 == 0) {
            throw new GSSException(3);
        }
        this.pName = j2;
        this.cStub = gSSLibStub;
        setPrintables();
    }

    GSSNameElement(byte[] bArr, Oid oid, GSSLibStub gSSLibStub) throws GSSException {
        String krbName;
        int iLastIndexOf;
        this.pName = 0L;
        if (!$assertionsDisabled && gSSLibStub == null) {
            throw new AssertionError();
        }
        if (bArr == null) {
            throw new GSSException(3);
        }
        this.cStub = gSSLibStub;
        byte[] bArr2 = bArr;
        if (oid != null) {
            oid = getNativeNameType(oid, gSSLibStub);
            if (GSSName.NT_EXPORT_NAME.equals(oid)) {
                DerOutputStream derOutputStream = new DerOutputStream();
                try {
                    derOutputStream.putOID(new ObjectIdentifier(this.cStub.getMech().toString()));
                    byte[] byteArray = derOutputStream.toByteArray();
                    bArr2 = new byte[4 + byteArray.length + 4 + bArr.length];
                    int i2 = 0 + 1;
                    bArr2[0] = 4;
                    int i3 = i2 + 1;
                    bArr2[i2] = 1;
                    int i4 = i3 + 1;
                    bArr2[i3] = (byte) (byteArray.length >>> 8);
                    int i5 = i4 + 1;
                    bArr2[i4] = (byte) byteArray.length;
                    System.arraycopy(byteArray, 0, bArr2, i5, byteArray.length);
                    int length = i5 + byteArray.length;
                    int i6 = length + 1;
                    bArr2[length] = (byte) (bArr.length >>> 24);
                    int i7 = i6 + 1;
                    bArr2[i6] = (byte) (bArr.length >>> 16);
                    int i8 = i7 + 1;
                    bArr2[i7] = (byte) (bArr.length >>> 8);
                    bArr2[i8] = (byte) bArr.length;
                    System.arraycopy(bArr, 0, bArr2, i8 + 1, bArr.length);
                } catch (IOException e2) {
                    throw new GSSExceptionImpl(11, e2);
                }
            }
        }
        this.pName = this.cStub.importName(bArr2, oid);
        setPrintables();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && !Realm.AUTODEDUCEREALM && (iLastIndexOf = (krbName = getKrbName()).lastIndexOf(64)) != -1) {
            String strSubstring = krbName.substring(iLastIndexOf);
            if ((oid != null && !oid.equals(GSSUtil.NT_GSS_KRB5_PRINCIPAL)) || !new String(bArr).endsWith(strSubstring)) {
                try {
                    securityManager.checkPermission(new ServicePermission(strSubstring, LanguageTag.SEP));
                } catch (SecurityException e3) {
                    throw new GSSException(11);
                }
            }
        }
        SunNativeProvider.debug("Imported " + this.printableName + " w/ type " + ((Object) this.printableType));
    }

    private void setPrintables() throws GSSException {
        Object[] objArrDisplayName = this.cStub.displayName(this.pName);
        if (!$assertionsDisabled && (objArrDisplayName == null || objArrDisplayName.length != 2)) {
            throw new AssertionError();
        }
        this.printableName = (String) objArrDisplayName[0];
        if (!$assertionsDisabled && this.printableName == null) {
            throw new AssertionError();
        }
        this.printableType = (Oid) objArrDisplayName[1];
        if (this.printableType == null) {
            this.printableType = GSSName.NT_USER_NAME;
        }
    }

    public String getKrbName() throws GSSException {
        GSSLibStub gSSLibStub = this.cStub;
        if (!GSSUtil.isKerberosMech(this.cStub.getMech())) {
            gSSLibStub = GSSLibStub.getInstance(GSSUtil.GSS_KRB5_MECH_OID);
        }
        long jCanonicalizeName = gSSLibStub.canonicalizeName(this.pName);
        Object[] objArrDisplayName = gSSLibStub.displayName(jCanonicalizeName);
        gSSLibStub.releaseName(jCanonicalizeName);
        SunNativeProvider.debug("Got kerberized name: " + objArrDisplayName[0]);
        return (String) objArrDisplayName[0];
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public Provider getProvider() {
        return SunNativeProvider.INSTANCE;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public boolean equals(GSSNameSpi gSSNameSpi) throws GSSException {
        if (!(gSSNameSpi instanceof GSSNameElement)) {
            return false;
        }
        return this.cStub.compareName(this.pName, ((GSSNameElement) gSSNameSpi).pName);
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public boolean equals(Object obj) {
        if (!(obj instanceof GSSNameElement)) {
            return false;
        }
        try {
            return equals((GSSNameSpi) obj);
        } catch (GSSException e2) {
            return false;
        }
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public int hashCode() {
        return new Long(this.pName).hashCode();
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public byte[] export() throws GSSException {
        byte[] bArrExportName = this.cStub.exportName(this.pName);
        int i2 = 0 + 1;
        if (bArrExportName[0] == 4) {
            int i3 = i2 + 1;
            if (bArrExportName[i2] == 1) {
                int i4 = i3 + 1;
                int i5 = (255 & bArrExportName[i3]) << 8;
                int i6 = i4 + 1;
                int i7 = i5 | (255 & bArrExportName[i4]);
                try {
                    Oid oid = new Oid(new ObjectIdentifier(new DerInputStream(bArrExportName, i6, i7)).toString());
                    if (!$assertionsDisabled && !oid.equals(getMechanism())) {
                        throw new AssertionError();
                    }
                    int i8 = i6 + i7;
                    int i9 = i8 + 1;
                    int i10 = i9 + 1;
                    int i11 = ((255 & bArrExportName[i8]) << 24) | ((255 & bArrExportName[i9]) << 16);
                    int i12 = i10 + 1;
                    int i13 = i11 | ((255 & bArrExportName[i10]) << 8);
                    int i14 = i12 + 1;
                    int i15 = i13 | (255 & bArrExportName[i12]);
                    if (i15 < 0) {
                        throw new GSSException(3);
                    }
                    byte[] bArr = new byte[i15];
                    System.arraycopy(bArrExportName, i14, bArr, 0, i15);
                    return bArr;
                } catch (IOException e2) {
                    throw new GSSExceptionImpl(3, e2);
                }
            }
        }
        throw new GSSException(3);
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public Oid getMechanism() {
        return this.cStub.getMech();
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public String toString() {
        return this.printableName;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public Oid getStringNameType() {
        return this.printableType;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public boolean isAnonymousName() {
        return GSSName.NT_ANONYMOUS.equals(this.printableType);
    }

    public void dispose() {
        if (this.pName != 0) {
            this.cStub.releaseName(this.pName);
            this.pName = 0L;
        }
    }

    protected void finalize() throws Throwable {
        dispose();
    }
}
