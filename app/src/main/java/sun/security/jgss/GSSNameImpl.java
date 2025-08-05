package sun.security.jgss;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/jgss/GSSNameImpl.class */
public class GSSNameImpl implements GSSName {
    static final Oid oldHostbasedServiceName;
    private GSSManagerImpl gssManager;
    private String appNameStr;
    private byte[] appNameBytes;
    private Oid appNameType;
    private String printableName;
    private Oid printableNameType;
    private HashMap<Oid, GSSNameSpi> elements;
    private GSSNameSpi mechElement;

    static {
        Oid oid = null;
        try {
            oid = new Oid("1.3.6.1.5.6.2");
        } catch (Exception e2) {
        }
        oldHostbasedServiceName = oid;
    }

    static GSSNameImpl wrapElement(GSSManagerImpl gSSManagerImpl, GSSNameSpi gSSNameSpi) throws GSSException {
        if (gSSNameSpi == null) {
            return null;
        }
        return new GSSNameImpl(gSSManagerImpl, gSSNameSpi);
    }

    GSSNameImpl(GSSManagerImpl gSSManagerImpl, GSSNameSpi gSSNameSpi) {
        this.gssManager = null;
        this.appNameStr = null;
        this.appNameBytes = null;
        this.appNameType = null;
        this.printableName = null;
        this.printableNameType = null;
        this.elements = null;
        this.mechElement = null;
        this.gssManager = gSSManagerImpl;
        String string = gSSNameSpi.toString();
        this.printableName = string;
        this.appNameStr = string;
        Oid stringNameType = gSSNameSpi.getStringNameType();
        this.printableNameType = stringNameType;
        this.appNameType = stringNameType;
        this.mechElement = gSSNameSpi;
        this.elements = new HashMap<>(1);
        this.elements.put(gSSNameSpi.getMechanism(), this.mechElement);
    }

    GSSNameImpl(GSSManagerImpl gSSManagerImpl, Object obj, Oid oid) throws GSSException {
        this(gSSManagerImpl, obj, oid, null);
    }

    GSSNameImpl(GSSManagerImpl gSSManagerImpl, Object obj, Oid oid, Oid oid2) throws GSSException {
        this.gssManager = null;
        this.appNameStr = null;
        this.appNameBytes = null;
        this.appNameType = null;
        this.printableName = null;
        this.printableNameType = null;
        this.elements = null;
        this.mechElement = null;
        oid = oldHostbasedServiceName.equals(oid) ? GSSName.NT_HOSTBASED_SERVICE : oid;
        if (obj == null) {
            throw new GSSExceptionImpl(3, "Cannot import null name");
        }
        oid2 = oid2 == null ? ProviderList.DEFAULT_MECH_OID : oid2;
        if (NT_EXPORT_NAME.equals(oid)) {
            importName(gSSManagerImpl, obj);
        } else {
            init(gSSManagerImpl, obj, oid, oid2);
        }
    }

    private void init(GSSManagerImpl gSSManagerImpl, Object obj, Oid oid, Oid oid2) throws GSSException {
        this.gssManager = gSSManagerImpl;
        this.elements = new HashMap<>(gSSManagerImpl.getMechs().length);
        if (obj instanceof String) {
            this.appNameStr = (String) obj;
            if (oid != null) {
                this.printableName = this.appNameStr;
                this.printableNameType = oid;
            }
        } else {
            this.appNameBytes = (byte[]) obj;
        }
        this.appNameType = oid;
        this.mechElement = getElement(oid2);
        if (this.printableName == null) {
            this.printableName = this.mechElement.toString();
            this.printableNameType = this.mechElement.getStringNameType();
        }
    }

    private void importName(GSSManagerImpl gSSManagerImpl, Object obj) throws GSSException {
        byte[] bytes = null;
        if (obj instanceof String) {
            try {
                bytes = ((String) obj).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e2) {
            }
        } else {
            bytes = (byte[]) obj;
        }
        int i2 = 0 + 1;
        if (bytes[0] == 4) {
            int i3 = i2 + 1;
            if (bytes[i2] == 1) {
                int i4 = i3 + 1;
                int i5 = (255 & bytes[i3]) << 8;
                int i6 = i4 + 1;
                int i7 = i5 | (255 & bytes[i4]);
                try {
                    Oid oid = new Oid(new ObjectIdentifier(new DerInputStream(bytes, i6, i7)).toString());
                    int i8 = i6 + i7;
                    int i9 = i8 + 1;
                    int i10 = i9 + 1;
                    int i11 = ((255 & bytes[i8]) << 24) | ((255 & bytes[i9]) << 16);
                    int i12 = i10 + 1;
                    int i13 = i11 | ((255 & bytes[i10]) << 8);
                    int i14 = i12 + 1;
                    int i15 = i13 | (255 & bytes[i12]);
                    if (i15 < 0 || i14 > bytes.length - i15) {
                        throw new GSSExceptionImpl(3, "Exported name mech name is corrupted!");
                    }
                    byte[] bArr = new byte[i15];
                    System.arraycopy(bytes, i14, bArr, 0, i15);
                    init(gSSManagerImpl, bArr, NT_EXPORT_NAME, oid);
                    return;
                } catch (IOException e3) {
                    throw new GSSExceptionImpl(3, "Exported name Object identifier is corrupted!");
                }
            }
        }
        throw new GSSExceptionImpl(3, "Exported name token id is corrupted!");
    }

    @Override // org.ietf.jgss.GSSName
    public GSSName canonicalize(Oid oid) throws GSSException {
        if (oid == null) {
            oid = ProviderList.DEFAULT_MECH_OID;
        }
        return wrapElement(this.gssManager, getElement(oid));
    }

    @Override // org.ietf.jgss.GSSName
    public boolean equals(GSSName gSSName) throws GSSException {
        if (isAnonymous() || gSSName.isAnonymous()) {
            return false;
        }
        if (gSSName == this) {
            return true;
        }
        if (!(gSSName instanceof GSSNameImpl)) {
            return equals(this.gssManager.createName(gSSName.toString(), gSSName.getStringNameType()));
        }
        GSSNameImpl gSSNameImpl = (GSSNameImpl) gSSName;
        GSSNameSpi element = this.mechElement;
        GSSNameSpi element2 = gSSNameImpl.mechElement;
        if (element == null && element2 != null) {
            element = getElement(element2.getMechanism());
        } else if (element != null && element2 == null) {
            element2 = gSSNameImpl.getElement(element.getMechanism());
        }
        if (element != null && element2 != null) {
            return element.equals(element2);
        }
        if (this.appNameType == null || gSSNameImpl.appNameType == null || !this.appNameType.equals(gSSNameImpl.appNameType)) {
            return false;
        }
        byte[] bytes = null;
        byte[] bytes2 = null;
        try {
            bytes = this.appNameStr != null ? this.appNameStr.getBytes("UTF-8") : this.appNameBytes;
            bytes2 = gSSNameImpl.appNameStr != null ? gSSNameImpl.appNameStr.getBytes("UTF-8") : gSSNameImpl.appNameBytes;
        } catch (UnsupportedEncodingException e2) {
        }
        return Arrays.equals(bytes, bytes2);
    }

    @Override // org.ietf.jgss.GSSName
    public int hashCode() {
        return 1;
    }

    @Override // org.ietf.jgss.GSSName
    public boolean equals(Object obj) {
        try {
            if (obj instanceof GSSName) {
                return equals((GSSName) obj);
            }
            return false;
        } catch (GSSException e2) {
            return false;
        }
    }

    @Override // org.ietf.jgss.GSSName
    public byte[] export() throws GSSException {
        if (this.mechElement == null) {
            this.mechElement = getElement(ProviderList.DEFAULT_MECH_OID);
        }
        byte[] bArrExport = this.mechElement.export();
        try {
            ObjectIdentifier objectIdentifier = new ObjectIdentifier(this.mechElement.getMechanism().toString());
            DerOutputStream derOutputStream = new DerOutputStream();
            try {
                derOutputStream.putOID(objectIdentifier);
                byte[] byteArray = derOutputStream.toByteArray();
                byte[] bArr = new byte[4 + byteArray.length + 4 + bArrExport.length];
                int i2 = 0 + 1;
                bArr[0] = 4;
                int i3 = i2 + 1;
                bArr[i2] = 1;
                int i4 = i3 + 1;
                bArr[i3] = (byte) (byteArray.length >>> 8);
                int i5 = i4 + 1;
                bArr[i4] = (byte) byteArray.length;
                System.arraycopy(byteArray, 0, bArr, i5, byteArray.length);
                int length = i5 + byteArray.length;
                int i6 = length + 1;
                bArr[length] = (byte) (bArrExport.length >>> 24);
                int i7 = i6 + 1;
                bArr[i6] = (byte) (bArrExport.length >>> 16);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (bArrExport.length >>> 8);
                bArr[i8] = (byte) bArrExport.length;
                System.arraycopy(bArrExport, 0, bArr, i8 + 1, bArrExport.length);
                return bArr;
            } catch (IOException e2) {
                throw new GSSExceptionImpl(11, "Could not ASN.1 Encode " + objectIdentifier.toString());
            }
        } catch (IOException e3) {
            throw new GSSExceptionImpl(11, "Invalid OID String ");
        }
    }

    @Override // org.ietf.jgss.GSSName
    public String toString() {
        return this.printableName;
    }

    @Override // org.ietf.jgss.GSSName
    public Oid getStringNameType() throws GSSException {
        return this.printableNameType;
    }

    @Override // org.ietf.jgss.GSSName
    public boolean isAnonymous() {
        if (this.printableNameType == null) {
            return false;
        }
        return GSSName.NT_ANONYMOUS.equals(this.printableNameType);
    }

    @Override // org.ietf.jgss.GSSName
    public boolean isMN() {
        return true;
    }

    public synchronized GSSNameSpi getElement(Oid oid) throws GSSException {
        GSSNameSpi nameElement = this.elements.get(oid);
        if (nameElement == null) {
            if (this.appNameStr != null) {
                nameElement = this.gssManager.getNameElement(this.appNameStr, this.appNameType, oid);
            } else {
                nameElement = this.gssManager.getNameElement(this.appNameBytes, this.appNameType, oid);
            }
            this.elements.put(oid, nameElement);
        }
        return nameElement;
    }

    Set<GSSNameSpi> getElements() {
        return new HashSet(this.elements.values());
    }

    private static String getNameTypeStr(Oid oid) {
        if (oid == null) {
            return "(NT is null)";
        }
        if (oid.equals(NT_USER_NAME)) {
            return "NT_USER_NAME";
        }
        if (oid.equals(NT_HOSTBASED_SERVICE)) {
            return "NT_HOSTBASED_SERVICE";
        }
        if (oid.equals(NT_EXPORT_NAME)) {
            return "NT_EXPORT_NAME";
        }
        if (oid.equals(GSSUtil.NT_GSS_KRB5_PRINCIPAL)) {
            return "NT_GSS_KRB5_PRINCIPAL";
        }
        return "Unknown";
    }
}
