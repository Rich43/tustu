package java.security.cert;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import sun.misc.HexDumpEncoder;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificatePoliciesExtension;
import sun.security.x509.CertificatePolicyId;
import sun.security.x509.CertificatePolicySet;
import sun.security.x509.DNSName;
import sun.security.x509.EDIPartyName;
import sun.security.x509.ExtendedKeyUsageExtension;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.GeneralSubtree;
import sun.security.x509.GeneralSubtrees;
import sun.security.x509.IPAddressName;
import sun.security.x509.NameConstraintsExtension;
import sun.security.x509.OIDName;
import sun.security.x509.OtherName;
import sun.security.x509.PolicyInformation;
import sun.security.x509.PrivateKeyUsageExtension;
import sun.security.x509.RFC822Name;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.URIName;
import sun.security.x509.X400Address;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509Key;

/* loaded from: rt.jar:java/security/cert/X509CertSelector.class */
public class X509CertSelector implements CertSelector {
    private static final Debug debug = Debug.getInstance("certpath");
    private static final ObjectIdentifier ANY_EXTENDED_KEY_USAGE = ObjectIdentifier.newInternal(new int[]{2, 5, 29, 37, 0});
    private BigInteger serialNumber;
    private X500Principal issuer;
    private X500Principal subject;
    private byte[] subjectKeyID;
    private byte[] authorityKeyID;
    private Date certificateValid;
    private Date privateKeyValid;
    private ObjectIdentifier subjectPublicKeyAlgID;
    private PublicKey subjectPublicKey;
    private byte[] subjectPublicKeyBytes;
    private boolean[] keyUsage;
    private Set<String> keyPurposeSet;
    private Set<ObjectIdentifier> keyPurposeOIDSet;
    private Set<List<?>> subjectAlternativeNames;
    private Set<GeneralNameInterface> subjectAlternativeGeneralNames;
    private CertificatePolicySet policy;
    private Set<String> policySet;
    private Set<List<?>> pathToNames;
    private Set<GeneralNameInterface> pathToGeneralNames;
    private NameConstraintsExtension nc;
    private byte[] ncBytes;
    private X509Certificate x509Cert;
    private static final Boolean FALSE;
    private static final int PRIVATE_KEY_USAGE_ID = 0;
    private static final int SUBJECT_ALT_NAME_ID = 1;
    private static final int NAME_CONSTRAINTS_ID = 2;
    private static final int CERT_POLICIES_ID = 3;
    private static final int EXTENDED_KEY_USAGE_ID = 4;
    private static final int NUM_OF_EXTENSIONS = 5;
    private static final String[] EXTENSION_OIDS;
    static final int NAME_ANY = 0;
    static final int NAME_RFC822 = 1;
    static final int NAME_DNS = 2;
    static final int NAME_X400 = 3;
    static final int NAME_DIRECTORY = 4;
    static final int NAME_EDI = 5;
    static final int NAME_URI = 6;
    static final int NAME_IP = 7;
    static final int NAME_OID = 8;
    private int basicConstraints = -1;
    private boolean matchAllSubjectAltNames = true;

    static {
        CertPathHelperImpl.initialize();
        FALSE = Boolean.FALSE;
        EXTENSION_OIDS = new String[5];
        EXTENSION_OIDS[0] = "2.5.29.16";
        EXTENSION_OIDS[1] = "2.5.29.17";
        EXTENSION_OIDS[2] = "2.5.29.30";
        EXTENSION_OIDS[3] = "2.5.29.32";
        EXTENSION_OIDS[4] = "2.5.29.37";
    }

    public void setCertificate(X509Certificate x509Certificate) {
        this.x509Cert = x509Certificate;
    }

    public void setSerialNumber(BigInteger bigInteger) {
        this.serialNumber = bigInteger;
    }

    public void setIssuer(X500Principal x500Principal) {
        this.issuer = x500Principal;
    }

    public void setIssuer(String str) throws IOException {
        if (str == null) {
            this.issuer = null;
        } else {
            this.issuer = new X500Name(str).asX500Principal();
        }
    }

    public void setIssuer(byte[] bArr) throws IOException {
        X500Principal x500Principal;
        if (bArr == null) {
            x500Principal = null;
        } else {
            try {
                x500Principal = new X500Principal(bArr);
            } catch (IllegalArgumentException e2) {
                throw new IOException("Invalid name", e2);
            }
        }
        this.issuer = x500Principal;
    }

    public void setSubject(X500Principal x500Principal) {
        this.subject = x500Principal;
    }

    public void setSubject(String str) throws IOException {
        if (str == null) {
            this.subject = null;
        } else {
            this.subject = new X500Name(str).asX500Principal();
        }
    }

    public void setSubject(byte[] bArr) throws IOException {
        X500Principal x500Principal;
        if (bArr == null) {
            x500Principal = null;
        } else {
            try {
                x500Principal = new X500Principal(bArr);
            } catch (IllegalArgumentException e2) {
                throw new IOException("Invalid name", e2);
            }
        }
        this.subject = x500Principal;
    }

    public void setSubjectKeyIdentifier(byte[] bArr) {
        if (bArr == null) {
            this.subjectKeyID = null;
        } else {
            this.subjectKeyID = (byte[]) bArr.clone();
        }
    }

    public void setAuthorityKeyIdentifier(byte[] bArr) {
        if (bArr == null) {
            this.authorityKeyID = null;
        } else {
            this.authorityKeyID = (byte[]) bArr.clone();
        }
    }

    public void setCertificateValid(Date date) {
        if (date == null) {
            this.certificateValid = null;
        } else {
            this.certificateValid = (Date) date.clone();
        }
    }

    public void setPrivateKeyValid(Date date) {
        if (date == null) {
            this.privateKeyValid = null;
        } else {
            this.privateKeyValid = (Date) date.clone();
        }
    }

    public void setSubjectPublicKeyAlgID(String str) throws IOException {
        if (str == null) {
            this.subjectPublicKeyAlgID = null;
        } else {
            this.subjectPublicKeyAlgID = new ObjectIdentifier(str);
        }
    }

    public void setSubjectPublicKey(PublicKey publicKey) {
        if (publicKey == null) {
            this.subjectPublicKey = null;
            this.subjectPublicKeyBytes = null;
        } else {
            this.subjectPublicKey = publicKey;
            this.subjectPublicKeyBytes = publicKey.getEncoded();
        }
    }

    public void setSubjectPublicKey(byte[] bArr) throws IOException {
        if (bArr == null) {
            this.subjectPublicKey = null;
            this.subjectPublicKeyBytes = null;
        } else {
            this.subjectPublicKeyBytes = (byte[]) bArr.clone();
            this.subjectPublicKey = X509Key.parse(new DerValue(this.subjectPublicKeyBytes));
        }
    }

    public void setKeyUsage(boolean[] zArr) {
        if (zArr == null) {
            this.keyUsage = null;
        } else {
            this.keyUsage = (boolean[]) zArr.clone();
        }
    }

    public void setExtendedKeyUsage(Set<String> set) throws IOException {
        if (set == null || set.isEmpty()) {
            this.keyPurposeSet = null;
            this.keyPurposeOIDSet = null;
            return;
        }
        this.keyPurposeSet = Collections.unmodifiableSet(new HashSet(set));
        this.keyPurposeOIDSet = new HashSet();
        Iterator<String> it = this.keyPurposeSet.iterator();
        while (it.hasNext()) {
            this.keyPurposeOIDSet.add(new ObjectIdentifier(it.next()));
        }
    }

    public void setMatchAllSubjectAltNames(boolean z2) {
        this.matchAllSubjectAltNames = z2;
    }

    public void setSubjectAlternativeNames(Collection<List<?>> collection) throws IOException {
        if (collection == null) {
            this.subjectAlternativeNames = null;
            this.subjectAlternativeGeneralNames = null;
        } else if (collection.isEmpty()) {
            this.subjectAlternativeNames = null;
            this.subjectAlternativeGeneralNames = null;
        } else {
            Set<List<?>> setCloneAndCheckNames = cloneAndCheckNames(collection);
            this.subjectAlternativeGeneralNames = parseNames(setCloneAndCheckNames);
            this.subjectAlternativeNames = setCloneAndCheckNames;
        }
    }

    public void addSubjectAlternativeName(int i2, String str) throws IOException {
        addSubjectAlternativeNameInternal(i2, str);
    }

    public void addSubjectAlternativeName(int i2, byte[] bArr) throws IOException {
        addSubjectAlternativeNameInternal(i2, bArr.clone());
    }

    private void addSubjectAlternativeNameInternal(int i2, Object obj) throws IOException {
        GeneralNameInterface generalNameInterfaceMakeGeneralNameInterface = makeGeneralNameInterface(i2, obj);
        if (this.subjectAlternativeNames == null) {
            this.subjectAlternativeNames = new HashSet();
        }
        if (this.subjectAlternativeGeneralNames == null) {
            this.subjectAlternativeGeneralNames = new HashSet();
        }
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(i2));
        arrayList.add(obj);
        this.subjectAlternativeNames.add(arrayList);
        this.subjectAlternativeGeneralNames.add(generalNameInterfaceMakeGeneralNameInterface);
    }

    private static Set<GeneralNameInterface> parseNames(Collection<List<?>> collection) throws IOException {
        HashSet hashSet = new HashSet();
        for (List<?> list : collection) {
            if (list.size() != 2) {
                throw new IOException("name list size not 2");
            }
            Object obj = list.get(0);
            if (!(obj instanceof Integer)) {
                throw new IOException("expected an Integer");
            }
            hashSet.add(makeGeneralNameInterface(((Integer) obj).intValue(), list.get(1)));
        }
        return hashSet;
    }

    static boolean equalNames(Collection<?> collection, Collection<?> collection2) {
        if (collection == null || collection2 == null) {
            return collection == collection2;
        }
        return collection.equals(collection2);
    }

    static GeneralNameInterface makeGeneralNameInterface(int i2, Object obj) throws IOException {
        GeneralNameInterface oIDName;
        if (debug != null) {
            debug.println("X509CertSelector.makeGeneralNameInterface(" + i2 + ")...");
        }
        if (obj instanceof String) {
            if (debug != null) {
                debug.println("X509CertSelector.makeGeneralNameInterface() name is String: " + obj);
            }
            switch (i2) {
                case 1:
                    oIDName = new RFC822Name((String) obj);
                    break;
                case 2:
                    oIDName = new DNSName((String) obj);
                    break;
                case 3:
                case 5:
                default:
                    throw new IOException("unable to parse String names of type " + i2);
                case 4:
                    oIDName = new X500Name((String) obj);
                    break;
                case 6:
                    oIDName = new URIName((String) obj);
                    break;
                case 7:
                    oIDName = new IPAddressName((String) obj);
                    break;
                case 8:
                    oIDName = new OIDName((String) obj);
                    break;
            }
            if (debug != null) {
                debug.println("X509CertSelector.makeGeneralNameInterface() result: " + oIDName.toString());
            }
        } else if (obj instanceof byte[]) {
            DerValue derValue = new DerValue((byte[]) obj);
            if (debug != null) {
                debug.println("X509CertSelector.makeGeneralNameInterface() is byte[]");
            }
            switch (i2) {
                case 0:
                    oIDName = new OtherName(derValue);
                    break;
                case 1:
                    oIDName = new RFC822Name(derValue);
                    break;
                case 2:
                    oIDName = new DNSName(derValue);
                    break;
                case 3:
                    oIDName = new X400Address(derValue);
                    break;
                case 4:
                    oIDName = new X500Name(derValue);
                    break;
                case 5:
                    oIDName = new EDIPartyName(derValue);
                    break;
                case 6:
                    oIDName = new URIName(derValue);
                    break;
                case 7:
                    oIDName = new IPAddressName(derValue);
                    break;
                case 8:
                    oIDName = new OIDName(derValue);
                    break;
                default:
                    throw new IOException("unable to parse byte array names of type " + i2);
            }
            if (debug != null) {
                debug.println("X509CertSelector.makeGeneralNameInterface() result: " + oIDName.toString());
            }
        } else {
            if (debug != null) {
                debug.println("X509CertSelector.makeGeneralName() input name not String or byte array");
            }
            throw new IOException("name not String or byte array");
        }
        return oIDName;
    }

    public void setNameConstraints(byte[] bArr) throws IOException {
        if (bArr == null) {
            this.ncBytes = null;
            this.nc = null;
        } else {
            this.ncBytes = (byte[]) bArr.clone();
            this.nc = new NameConstraintsExtension(FALSE, bArr);
        }
    }

    public void setBasicConstraints(int i2) {
        if (i2 < -2) {
            throw new IllegalArgumentException("basic constraints less than -2");
        }
        this.basicConstraints = i2;
    }

    public void setPolicy(Set<String> set) throws IOException {
        if (set == null) {
            this.policySet = null;
            this.policy = null;
            return;
        }
        Set<String> setUnmodifiableSet = Collections.unmodifiableSet(new HashSet(set));
        Vector vector = new Vector();
        for (String str : setUnmodifiableSet) {
            if (!(str instanceof String)) {
                throw new IOException("non String in certPolicySet");
            }
            vector.add(new CertificatePolicyId(new ObjectIdentifier(str)));
        }
        this.policySet = setUnmodifiableSet;
        this.policy = new CertificatePolicySet((Vector<CertificatePolicyId>) vector);
    }

    public void setPathToNames(Collection<List<?>> collection) throws IOException {
        if (collection == null || collection.isEmpty()) {
            this.pathToNames = null;
            this.pathToGeneralNames = null;
        } else {
            Set<List<?>> setCloneAndCheckNames = cloneAndCheckNames(collection);
            this.pathToGeneralNames = parseNames(setCloneAndCheckNames);
            this.pathToNames = setCloneAndCheckNames;
        }
    }

    void setPathToNamesInternal(Set<GeneralNameInterface> set) {
        this.pathToNames = Collections.emptySet();
        this.pathToGeneralNames = set;
    }

    public void addPathToName(int i2, String str) throws IOException {
        addPathToNameInternal(i2, str);
    }

    public void addPathToName(int i2, byte[] bArr) throws IOException {
        addPathToNameInternal(i2, bArr.clone());
    }

    private void addPathToNameInternal(int i2, Object obj) throws IOException {
        GeneralNameInterface generalNameInterfaceMakeGeneralNameInterface = makeGeneralNameInterface(i2, obj);
        if (this.pathToGeneralNames == null) {
            this.pathToNames = new HashSet();
            this.pathToGeneralNames = new HashSet();
        }
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(i2));
        arrayList.add(obj);
        this.pathToNames.add(arrayList);
        this.pathToGeneralNames.add(generalNameInterfaceMakeGeneralNameInterface);
    }

    public X509Certificate getCertificate() {
        return this.x509Cert;
    }

    public BigInteger getSerialNumber() {
        return this.serialNumber;
    }

    public X500Principal getIssuer() {
        return this.issuer;
    }

    public String getIssuerAsString() {
        if (this.issuer == null) {
            return null;
        }
        return this.issuer.getName();
    }

    public byte[] getIssuerAsBytes() throws IOException {
        if (this.issuer == null) {
            return null;
        }
        return this.issuer.getEncoded();
    }

    public X500Principal getSubject() {
        return this.subject;
    }

    public String getSubjectAsString() {
        if (this.subject == null) {
            return null;
        }
        return this.subject.getName();
    }

    public byte[] getSubjectAsBytes() throws IOException {
        if (this.subject == null) {
            return null;
        }
        return this.subject.getEncoded();
    }

    public byte[] getSubjectKeyIdentifier() {
        if (this.subjectKeyID == null) {
            return null;
        }
        return (byte[]) this.subjectKeyID.clone();
    }

    public byte[] getAuthorityKeyIdentifier() {
        if (this.authorityKeyID == null) {
            return null;
        }
        return (byte[]) this.authorityKeyID.clone();
    }

    public Date getCertificateValid() {
        if (this.certificateValid == null) {
            return null;
        }
        return (Date) this.certificateValid.clone();
    }

    public Date getPrivateKeyValid() {
        if (this.privateKeyValid == null) {
            return null;
        }
        return (Date) this.privateKeyValid.clone();
    }

    public String getSubjectPublicKeyAlgID() {
        if (this.subjectPublicKeyAlgID == null) {
            return null;
        }
        return this.subjectPublicKeyAlgID.toString();
    }

    public PublicKey getSubjectPublicKey() {
        return this.subjectPublicKey;
    }

    public boolean[] getKeyUsage() {
        if (this.keyUsage == null) {
            return null;
        }
        return (boolean[]) this.keyUsage.clone();
    }

    public Set<String> getExtendedKeyUsage() {
        return this.keyPurposeSet;
    }

    public boolean getMatchAllSubjectAltNames() {
        return this.matchAllSubjectAltNames;
    }

    public Collection<List<?>> getSubjectAlternativeNames() {
        if (this.subjectAlternativeNames == null) {
            return null;
        }
        return cloneNames(this.subjectAlternativeNames);
    }

    private static Set<List<?>> cloneNames(Collection<List<?>> collection) {
        try {
            return cloneAndCheckNames(collection);
        } catch (IOException e2) {
            throw new RuntimeException("cloneNames encountered IOException: " + e2.getMessage());
        }
    }

    private static Set<List<?>> cloneAndCheckNames(Collection<List<?>> collection) throws IOException {
        HashSet<List> hashSet = new HashSet();
        Iterator<List<?>> it = collection.iterator();
        while (it.hasNext()) {
            hashSet.add(new ArrayList(it.next()));
        }
        for (List list : hashSet) {
            if (list.size() != 2) {
                throw new IOException("name list size not 2");
            }
            Object obj = list.get(0);
            if (!(obj instanceof Integer)) {
                throw new IOException("expected an Integer");
            }
            int iIntValue = ((Integer) obj).intValue();
            if (iIntValue < 0 || iIntValue > 8) {
                throw new IOException("name type not 0-8");
            }
            Object obj2 = list.get(1);
            if (!(obj2 instanceof byte[]) && !(obj2 instanceof String)) {
                if (debug != null) {
                    debug.println("X509CertSelector.cloneAndCheckNames() name not byte array");
                }
                throw new IOException("name not byte array or String");
            }
            if (obj2 instanceof byte[]) {
                list.set(1, ((byte[]) obj2).clone());
            }
        }
        return hashSet;
    }

    public byte[] getNameConstraints() {
        if (this.ncBytes == null) {
            return null;
        }
        return (byte[]) this.ncBytes.clone();
    }

    public int getBasicConstraints() {
        return this.basicConstraints;
    }

    public Set<String> getPolicy() {
        return this.policySet;
    }

    public Collection<List<?>> getPathToNames() {
        if (this.pathToNames == null) {
            return null;
        }
        return cloneNames(this.pathToNames);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("X509CertSelector: [\n");
        if (this.x509Cert != null) {
            stringBuffer.append("  Certificate: " + this.x509Cert.toString() + "\n");
        }
        if (this.serialNumber != null) {
            stringBuffer.append("  Serial Number: " + this.serialNumber.toString() + "\n");
        }
        if (this.issuer != null) {
            stringBuffer.append("  Issuer: " + getIssuerAsString() + "\n");
        }
        if (this.subject != null) {
            stringBuffer.append("  Subject: " + getSubjectAsString() + "\n");
        }
        stringBuffer.append("  matchAllSubjectAltNames flag: " + String.valueOf(this.matchAllSubjectAltNames) + "\n");
        if (this.subjectAlternativeNames != null) {
            stringBuffer.append("  SubjectAlternativeNames:\n");
            for (List<?> list : this.subjectAlternativeNames) {
                stringBuffer.append("    type " + list.get(0) + ", name " + list.get(1) + "\n");
            }
        }
        if (this.subjectKeyID != null) {
            stringBuffer.append("  Subject Key Identifier: " + new HexDumpEncoder().encodeBuffer(this.subjectKeyID) + "\n");
        }
        if (this.authorityKeyID != null) {
            stringBuffer.append("  Authority Key Identifier: " + new HexDumpEncoder().encodeBuffer(this.authorityKeyID) + "\n");
        }
        if (this.certificateValid != null) {
            stringBuffer.append("  Certificate Valid: " + this.certificateValid.toString() + "\n");
        }
        if (this.privateKeyValid != null) {
            stringBuffer.append("  Private Key Valid: " + this.privateKeyValid.toString() + "\n");
        }
        if (this.subjectPublicKeyAlgID != null) {
            stringBuffer.append("  Subject Public Key AlgID: " + this.subjectPublicKeyAlgID.toString() + "\n");
        }
        if (this.subjectPublicKey != null) {
            stringBuffer.append("  Subject Public Key: " + this.subjectPublicKey.toString() + "\n");
        }
        if (this.keyUsage != null) {
            stringBuffer.append("  Key Usage: " + keyUsageToString(this.keyUsage) + "\n");
        }
        if (this.keyPurposeSet != null) {
            stringBuffer.append("  Extended Key Usage: " + this.keyPurposeSet.toString() + "\n");
        }
        if (this.policy != null) {
            stringBuffer.append("  Policy: " + this.policy.toString() + "\n");
        }
        if (this.pathToGeneralNames != null) {
            stringBuffer.append("  Path to names:\n");
            Iterator<GeneralNameInterface> it = this.pathToGeneralNames.iterator();
            while (it.hasNext()) {
                stringBuffer.append("    " + ((Object) it.next()) + "\n");
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    private static String keyUsageToString(boolean[] zArr) {
        String str = "KeyUsage [\n";
        try {
            if (zArr[0]) {
                str = str + "  DigitalSignature\n";
            }
            if (zArr[1]) {
                str = str + "  Non_repudiation\n";
            }
            if (zArr[2]) {
                str = str + "  Key_Encipherment\n";
            }
            if (zArr[3]) {
                str = str + "  Data_Encipherment\n";
            }
            if (zArr[4]) {
                str = str + "  Key_Agreement\n";
            }
            if (zArr[5]) {
                str = str + "  Key_CertSign\n";
            }
            if (zArr[6]) {
                str = str + "  Crl_Sign\n";
            }
            if (zArr[7]) {
                str = str + "  Encipher_Only\n";
            }
            if (zArr[8]) {
                str = str + "  Decipher_Only\n";
            }
        } catch (ArrayIndexOutOfBoundsException e2) {
        }
        return str + "]\n";
    }

    private static Extension getExtensionObject(X509Certificate x509Certificate, int i2) throws IOException {
        if (x509Certificate instanceof X509CertImpl) {
            X509CertImpl x509CertImpl = (X509CertImpl) x509Certificate;
            switch (i2) {
                case 0:
                    return x509CertImpl.getPrivateKeyUsageExtension();
                case 1:
                    return x509CertImpl.getSubjectAlternativeNameExtension();
                case 2:
                    return x509CertImpl.getNameConstraintsExtension();
                case 3:
                    return x509CertImpl.getCertificatePoliciesExtension();
                case 4:
                    return x509CertImpl.getExtendedKeyUsageExtension();
                default:
                    return null;
            }
        }
        byte[] extensionValue = x509Certificate.getExtensionValue(EXTENSION_OIDS[i2]);
        if (extensionValue == null) {
            return null;
        }
        byte[] octetString = new DerInputStream(extensionValue).getOctetString();
        switch (i2) {
            case 0:
                try {
                    return new PrivateKeyUsageExtension(FALSE, octetString);
                } catch (CertificateException e2) {
                    throw new IOException(e2.getMessage());
                }
            case 1:
                return new SubjectAlternativeNameExtension(FALSE, octetString);
            case 2:
                return new NameConstraintsExtension(FALSE, octetString);
            case 3:
                return new CertificatePoliciesExtension(FALSE, octetString);
            case 4:
                return new ExtendedKeyUsageExtension(FALSE, octetString);
            default:
                return null;
        }
    }

    @Override // java.security.cert.CertSelector
    public boolean match(Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            return false;
        }
        X509Certificate x509Certificate = (X509Certificate) certificate;
        if (debug != null) {
            debug.println("X509CertSelector.match(SN: " + x509Certificate.getSerialNumber().toString(16) + "\n  Issuer: " + ((Object) x509Certificate.getIssuerDN()) + "\n  Subject: " + ((Object) x509Certificate.getSubjectDN()) + ")");
        }
        if (this.x509Cert != null && !this.x509Cert.equals(x509Certificate)) {
            if (debug != null) {
                debug.println("X509CertSelector.match: certs don't match");
                return false;
            }
            return false;
        }
        if (this.serialNumber != null && !this.serialNumber.equals(x509Certificate.getSerialNumber())) {
            if (debug != null) {
                debug.println("X509CertSelector.match: serial numbers don't match");
                return false;
            }
            return false;
        }
        if (this.issuer != null && !this.issuer.equals(x509Certificate.getIssuerX500Principal())) {
            if (debug != null) {
                debug.println("X509CertSelector.match: issuer DNs don't match");
                return false;
            }
            return false;
        }
        if (this.subject != null && !this.subject.equals(x509Certificate.getSubjectX500Principal())) {
            if (debug != null) {
                debug.println("X509CertSelector.match: subject DNs don't match");
                return false;
            }
            return false;
        }
        if (this.certificateValid != null) {
            try {
                x509Certificate.checkValidity(this.certificateValid);
            } catch (CertificateException e2) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: certificate not within validity period");
                    return false;
                }
                return false;
            }
        }
        if (this.subjectPublicKeyBytes != null) {
            if (!Arrays.equals(this.subjectPublicKeyBytes, x509Certificate.getPublicKey().getEncoded())) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: subject public keys don't match");
                    return false;
                }
                return false;
            }
        }
        boolean z2 = matchBasicConstraints(x509Certificate) && matchKeyUsage(x509Certificate) && matchExtendedKeyUsage(x509Certificate) && matchSubjectKeyID(x509Certificate) && matchAuthorityKeyID(x509Certificate) && matchPrivateKeyValid(x509Certificate) && matchSubjectPublicKeyAlgID(x509Certificate) && matchPolicy(x509Certificate) && matchSubjectAlternativeNames(x509Certificate) && matchPathToNames(x509Certificate) && matchNameConstraints(x509Certificate);
        if (z2 && debug != null) {
            debug.println("X509CertSelector.match returning: true");
        }
        return z2;
    }

    private boolean matchSubjectKeyID(X509Certificate x509Certificate) {
        if (this.subjectKeyID == null) {
            return true;
        }
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue(XMLX509SKI.SKI_OID);
            if (extensionValue == null) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: no subject key ID extension");
                    return false;
                }
                return false;
            }
            byte[] octetString = new DerInputStream(extensionValue).getOctetString();
            if (octetString == null || !Arrays.equals(this.subjectKeyID, octetString)) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: subject key IDs don't match\nX509CertSelector.match: subjectKeyID: " + Arrays.toString(this.subjectKeyID) + "\nX509CertSelector.match: certSubjectKeyID: " + Arrays.toString(octetString));
                    return false;
                }
                return false;
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: exception in subject key ID check");
                return false;
            }
            return false;
        }
    }

    private boolean matchAuthorityKeyID(X509Certificate x509Certificate) {
        if (this.authorityKeyID == null) {
            return true;
        }
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue("2.5.29.35");
            if (extensionValue == null) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: no authority key ID extension");
                    return false;
                }
                return false;
            }
            byte[] octetString = new DerInputStream(extensionValue).getOctetString();
            if (octetString == null || !Arrays.equals(this.authorityKeyID, octetString)) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: authority key IDs don't match");
                    return false;
                }
                return false;
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: exception in authority key ID check");
                return false;
            }
            return false;
        }
    }

    private boolean matchPrivateKeyValid(X509Certificate x509Certificate) {
        if (this.privateKeyValid == null) {
            return true;
        }
        PrivateKeyUsageExtension privateKeyUsageExtension = null;
        try {
            privateKeyUsageExtension = (PrivateKeyUsageExtension) getExtensionObject(x509Certificate, 0);
            if (privateKeyUsageExtension != null) {
                privateKeyUsageExtension.valid(this.privateKeyValid);
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: IOException in private key usage check; X509CertSelector: " + toString());
                e2.printStackTrace();
                return false;
            }
            return false;
        } catch (CertificateExpiredException e3) {
            if (debug != null) {
                String string = "n/a";
                try {
                    string = privateKeyUsageExtension.get(PrivateKeyUsageExtension.NOT_AFTER).toString();
                } catch (CertificateException e4) {
                }
                debug.println("X509CertSelector.match: private key usage not within validity date; ext.NOT_After: " + string + "; X509CertSelector: " + toString());
                e3.printStackTrace();
                return false;
            }
            return false;
        } catch (CertificateNotYetValidException e5) {
            if (debug != null) {
                String string2 = "n/a";
                try {
                    string2 = privateKeyUsageExtension.get(PrivateKeyUsageExtension.NOT_BEFORE).toString();
                } catch (CertificateException e6) {
                }
                debug.println("X509CertSelector.match: private key usage not within validity date; ext.NOT_BEFORE: " + string2 + "; X509CertSelector: " + toString());
                e5.printStackTrace();
                return false;
            }
            return false;
        }
    }

    private boolean matchSubjectPublicKeyAlgID(X509Certificate x509Certificate) throws IOException {
        if (this.subjectPublicKeyAlgID == null) {
            return true;
        }
        try {
            DerValue derValue = new DerValue(x509Certificate.getPublicKey().getEncoded());
            if (derValue.tag != 48) {
                throw new IOException("invalid key format");
            }
            AlgorithmId algorithmId = AlgorithmId.parse(derValue.data.getDerValue());
            if (debug != null) {
                debug.println("X509CertSelector.match: subjectPublicKeyAlgID = " + ((Object) this.subjectPublicKeyAlgID) + ", xcert subjectPublicKeyAlgID = " + ((Object) algorithmId.getOID()));
            }
            if (!this.subjectPublicKeyAlgID.equals((Object) algorithmId.getOID())) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: subject public key alg IDs don't match");
                    return false;
                }
                return false;
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: IOException in subject public key algorithm OID check");
                return false;
            }
            return false;
        }
    }

    private boolean matchKeyUsage(X509Certificate x509Certificate) {
        boolean[] keyUsage;
        if (this.keyUsage != null && (keyUsage = x509Certificate.getKeyUsage()) != null) {
            for (int i2 = 0; i2 < this.keyUsage.length; i2++) {
                if (this.keyUsage[i2] && (i2 >= keyUsage.length || !keyUsage[i2])) {
                    if (debug != null) {
                        debug.println("X509CertSelector.match: key usage bits don't match");
                        return false;
                    }
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private boolean matchExtendedKeyUsage(X509Certificate x509Certificate) {
        if (this.keyPurposeSet == null || this.keyPurposeSet.isEmpty()) {
            return true;
        }
        try {
            ExtendedKeyUsageExtension extendedKeyUsageExtension = (ExtendedKeyUsageExtension) getExtensionObject(x509Certificate, 4);
            if (extendedKeyUsageExtension != null) {
                Vector<ObjectIdentifier> vector = extendedKeyUsageExtension.get(ExtendedKeyUsageExtension.USAGES);
                if (!vector.contains(ANY_EXTENDED_KEY_USAGE) && !vector.containsAll(this.keyPurposeOIDSet)) {
                    if (debug != null) {
                        debug.println("X509CertSelector.match: cert failed extendedKeyUsage criterion");
                        return false;
                    }
                    return false;
                }
                return true;
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: IOException in extended key usage check");
                return false;
            }
            return false;
        }
    }

    private boolean matchSubjectAlternativeNames(X509Certificate x509Certificate) {
        if (this.subjectAlternativeNames == null || this.subjectAlternativeNames.isEmpty()) {
            return true;
        }
        try {
            SubjectAlternativeNameExtension subjectAlternativeNameExtension = (SubjectAlternativeNameExtension) getExtensionObject(x509Certificate, 1);
            if (subjectAlternativeNameExtension == null) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: no subject alternative name extension");
                    return false;
                }
                return false;
            }
            GeneralNames generalNames = subjectAlternativeNameExtension.get(SubjectAlternativeNameExtension.SUBJECT_NAME);
            Iterator<GeneralNameInterface> it = this.subjectAlternativeGeneralNames.iterator();
            while (it.hasNext()) {
                GeneralNameInterface next = it.next();
                boolean zEquals = false;
                Iterator<GeneralName> it2 = generalNames.iterator();
                while (it2.hasNext() && !zEquals) {
                    zEquals = it2.next().getName().equals(next);
                }
                if (!zEquals && (this.matchAllSubjectAltNames || !it.hasNext())) {
                    if (debug != null) {
                        debug.println("X509CertSelector.match: subject alternative name " + ((Object) next) + " not found");
                        return false;
                    }
                    return false;
                }
                if (zEquals && !this.matchAllSubjectAltNames) {
                    break;
                }
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: IOException in subject alternative name check");
                return false;
            }
            return false;
        }
    }

    private boolean matchNameConstraints(X509Certificate x509Certificate) {
        if (this.nc == null) {
            return true;
        }
        try {
            if (!this.nc.verify(x509Certificate)) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: name constraints not satisfied");
                    return false;
                }
                return false;
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: IOException in name constraints check");
                return false;
            }
            return false;
        }
    }

    private boolean matchPolicy(X509Certificate x509Certificate) {
        if (this.policy == null) {
            return true;
        }
        try {
            CertificatePoliciesExtension certificatePoliciesExtension = (CertificatePoliciesExtension) getExtensionObject(x509Certificate, 3);
            if (certificatePoliciesExtension == null) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: no certificate policy extension");
                    return false;
                }
                return false;
            }
            List<PolicyInformation> list = certificatePoliciesExtension.get(CertificatePoliciesExtension.POLICIES);
            ArrayList arrayList = new ArrayList(list.size());
            Iterator<PolicyInformation> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getPolicyIdentifier());
            }
            if (this.policy != null) {
                boolean z2 = false;
                if (this.policy.getCertPolicyIds().isEmpty()) {
                    if (arrayList.isEmpty()) {
                        if (debug != null) {
                            debug.println("X509CertSelector.match: cert failed policyAny criterion");
                            return false;
                        }
                        return false;
                    }
                    return true;
                }
                Iterator<CertificatePolicyId> it2 = this.policy.getCertPolicyIds().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    if (arrayList.contains(it2.next())) {
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    if (debug != null) {
                        debug.println("X509CertSelector.match: cert failed policyAny criterion");
                        return false;
                    }
                    return false;
                }
                return true;
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: IOException in certificate policy ID check");
                return false;
            }
            return false;
        }
    }

    private boolean matchPathToNames(X509Certificate x509Certificate) {
        if (this.pathToGeneralNames == null) {
            return true;
        }
        try {
            NameConstraintsExtension nameConstraintsExtension = (NameConstraintsExtension) getExtensionObject(x509Certificate, 2);
            if (nameConstraintsExtension == null) {
                return true;
            }
            if (debug != null && Debug.isOn("certpath")) {
                debug.println("X509CertSelector.match pathToNames:\n");
                Iterator<GeneralNameInterface> it = this.pathToGeneralNames.iterator();
                while (it.hasNext()) {
                    debug.println("    " + ((Object) it.next()) + "\n");
                }
            }
            GeneralSubtrees generalSubtrees = nameConstraintsExtension.get(NameConstraintsExtension.PERMITTED_SUBTREES);
            GeneralSubtrees generalSubtrees2 = nameConstraintsExtension.get(NameConstraintsExtension.EXCLUDED_SUBTREES);
            if (generalSubtrees2 != null && !matchExcluded(generalSubtrees2)) {
                return false;
            }
            if (generalSubtrees != null) {
                if (!matchPermitted(generalSubtrees)) {
                    return false;
                }
                return true;
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("X509CertSelector.match: IOException in name constraints check");
                return false;
            }
            return false;
        }
    }

    private boolean matchExcluded(GeneralSubtrees generalSubtrees) {
        Iterator<GeneralSubtree> it = generalSubtrees.iterator();
        while (it.hasNext()) {
            GeneralNameInterface name = it.next().getName().getName();
            for (GeneralNameInterface generalNameInterface : this.pathToGeneralNames) {
                if (name.getType() == generalNameInterface.getType()) {
                    switch (generalNameInterface.constrains(name)) {
                        case 0:
                        case 2:
                            if (debug != null) {
                                debug.println("X509CertSelector.match: name constraints inhibit path to specified name");
                                debug.println("X509CertSelector.match: excluded name: " + ((Object) generalNameInterface));
                                return false;
                            }
                            return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean matchPermitted(GeneralSubtrees generalSubtrees) {
        for (GeneralNameInterface generalNameInterface : this.pathToGeneralNames) {
            Iterator<GeneralSubtree> it = generalSubtrees.iterator();
            boolean z2 = false;
            boolean z3 = false;
            String str = "";
            while (it.hasNext() && !z2) {
                GeneralNameInterface name = it.next().getName().getName();
                if (name.getType() == generalNameInterface.getType()) {
                    z3 = true;
                    str = str + Constants.INDENT + ((Object) name);
                    switch (generalNameInterface.constrains(name)) {
                        case 0:
                        case 2:
                            z2 = true;
                            break;
                    }
                }
            }
            if (!z2 && z3) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: name constraints inhibit path to specified name; permitted names of type " + generalNameInterface.getType() + ": " + str);
                    return false;
                }
                return false;
            }
        }
        return true;
    }

    private boolean matchBasicConstraints(X509Certificate x509Certificate) {
        if (this.basicConstraints == -1) {
            return true;
        }
        int basicConstraints = x509Certificate.getBasicConstraints();
        if (this.basicConstraints == -2) {
            if (basicConstraints != -1) {
                if (debug != null) {
                    debug.println("X509CertSelector.match: not an EE cert");
                    return false;
                }
                return false;
            }
            return true;
        }
        if (basicConstraints < this.basicConstraints) {
            if (debug != null) {
                debug.println("X509CertSelector.match: cert's maxPathLen is less than the min maxPathLen set by basicConstraints. (" + basicConstraints + " < " + this.basicConstraints + ")");
                return false;
            }
            return false;
        }
        return true;
    }

    private static <T> Set<T> cloneSet(Set<T> set) {
        if (set instanceof HashSet) {
            return (Set) ((HashSet) set).clone();
        }
        return new HashSet(set);
    }

    @Override // java.security.cert.CertSelector
    public Object clone() {
        try {
            X509CertSelector x509CertSelector = (X509CertSelector) super.clone();
            if (this.subjectAlternativeNames != null) {
                x509CertSelector.subjectAlternativeNames = cloneSet(this.subjectAlternativeNames);
                x509CertSelector.subjectAlternativeGeneralNames = cloneSet(this.subjectAlternativeGeneralNames);
            }
            if (this.pathToGeneralNames != null) {
                x509CertSelector.pathToNames = cloneSet(this.pathToNames);
                x509CertSelector.pathToGeneralNames = cloneSet(this.pathToGeneralNames);
            }
            return x509CertSelector;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }
}
