package sun.security.x509;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/X500Name.class */
public class X500Name implements GeneralNameInterface, Principal {
    private String dn;
    private String rfc1779Dn;
    private String rfc2253Dn;
    private String canonicalDn;
    private RDN[] names;
    private X500Principal x500Principal;
    private byte[] encoded;
    private volatile List<RDN> rdnList;
    private volatile List<AVA> allAvaList;
    private static final int[] commonName_data = {2, 5, 4, 3};
    private static final int[] SURNAME_DATA = {2, 5, 4, 4};
    private static final int[] SERIALNUMBER_DATA = {2, 5, 4, 5};
    private static final int[] countryName_data = {2, 5, 4, 6};
    private static final int[] localityName_data = {2, 5, 4, 7};
    private static final int[] stateName_data = {2, 5, 4, 8};
    private static final int[] streetAddress_data = {2, 5, 4, 9};
    private static final int[] orgName_data = {2, 5, 4, 10};
    private static final int[] orgUnitName_data = {2, 5, 4, 11};
    private static final int[] title_data = {2, 5, 4, 12};
    private static final int[] GIVENNAME_DATA = {2, 5, 4, 42};
    private static final int[] INITIALS_DATA = {2, 5, 4, 43};
    private static final int[] GENERATIONQUALIFIER_DATA = {2, 5, 4, 44};
    private static final int[] DNQUALIFIER_DATA = {2, 5, 4, 46};
    private static final int[] ipAddress_data = {1, 3, 6, 1, 4, 1, 42, 2, 11, 2, 1};
    private static final int[] DOMAIN_COMPONENT_DATA = {0, 9, 2342, 19200300, 100, 1, 25};
    private static final int[] userid_data = {0, 9, 2342, 19200300, 100, 1, 1};
    public static final ObjectIdentifier commonName_oid = ObjectIdentifier.newInternal(commonName_data);
    public static final ObjectIdentifier SERIALNUMBER_OID = ObjectIdentifier.newInternal(SERIALNUMBER_DATA);
    public static final ObjectIdentifier countryName_oid = ObjectIdentifier.newInternal(countryName_data);
    public static final ObjectIdentifier localityName_oid = ObjectIdentifier.newInternal(localityName_data);
    public static final ObjectIdentifier orgName_oid = ObjectIdentifier.newInternal(orgName_data);
    public static final ObjectIdentifier orgUnitName_oid = ObjectIdentifier.newInternal(orgUnitName_data);
    public static final ObjectIdentifier stateName_oid = ObjectIdentifier.newInternal(stateName_data);
    public static final ObjectIdentifier streetAddress_oid = ObjectIdentifier.newInternal(streetAddress_data);
    public static final ObjectIdentifier title_oid = ObjectIdentifier.newInternal(title_data);
    public static final ObjectIdentifier DNQUALIFIER_OID = ObjectIdentifier.newInternal(DNQUALIFIER_DATA);
    public static final ObjectIdentifier SURNAME_OID = ObjectIdentifier.newInternal(SURNAME_DATA);
    public static final ObjectIdentifier GIVENNAME_OID = ObjectIdentifier.newInternal(GIVENNAME_DATA);
    public static final ObjectIdentifier INITIALS_OID = ObjectIdentifier.newInternal(INITIALS_DATA);
    public static final ObjectIdentifier GENERATIONQUALIFIER_OID = ObjectIdentifier.newInternal(GENERATIONQUALIFIER_DATA);
    public static final ObjectIdentifier ipAddress_oid = ObjectIdentifier.newInternal(ipAddress_data);
    public static final ObjectIdentifier DOMAIN_COMPONENT_OID = ObjectIdentifier.newInternal(DOMAIN_COMPONENT_DATA);
    public static final ObjectIdentifier userid_oid = ObjectIdentifier.newInternal(userid_data);
    private static final Constructor<X500Principal> principalConstructor;
    private static final Field principalField;

    public X500Name(String str) throws IOException {
        this(str, (Map<String, String>) Collections.emptyMap());
    }

    public X500Name(String str, Map<String, String> map) throws IOException {
        parseDN(str, map);
    }

    public X500Name(String str, String str2) throws IOException {
        if (str == null) {
            throw new NullPointerException("Name must not be null");
        }
        if (str2.equalsIgnoreCase(X500Principal.RFC2253)) {
            parseRFC2253DN(str);
        } else {
            if (str2.equalsIgnoreCase("DEFAULT")) {
                parseDN(str, Collections.emptyMap());
                return;
            }
            throw new IOException("Unsupported format " + str2);
        }
    }

    public X500Name(String str, String str2, String str3, String str4) throws IOException {
        this.names = new RDN[4];
        this.names[3] = new RDN(1);
        this.names[3].assertion[0] = new AVA(commonName_oid, new DerValue(str));
        this.names[2] = new RDN(1);
        this.names[2].assertion[0] = new AVA(orgUnitName_oid, new DerValue(str2));
        this.names[1] = new RDN(1);
        this.names[1].assertion[0] = new AVA(orgName_oid, new DerValue(str3));
        this.names[0] = new RDN(1);
        this.names[0].assertion[0] = new AVA(countryName_oid, new DerValue(str4));
    }

    public X500Name(String str, String str2, String str3, String str4, String str5, String str6) throws IOException {
        this.names = new RDN[6];
        this.names[5] = new RDN(1);
        this.names[5].assertion[0] = new AVA(commonName_oid, new DerValue(str));
        this.names[4] = new RDN(1);
        this.names[4].assertion[0] = new AVA(orgUnitName_oid, new DerValue(str2));
        this.names[3] = new RDN(1);
        this.names[3].assertion[0] = new AVA(orgName_oid, new DerValue(str3));
        this.names[2] = new RDN(1);
        this.names[2].assertion[0] = new AVA(localityName_oid, new DerValue(str4));
        this.names[1] = new RDN(1);
        this.names[1].assertion[0] = new AVA(stateName_oid, new DerValue(str5));
        this.names[0] = new RDN(1);
        this.names[0].assertion[0] = new AVA(countryName_oid, new DerValue(str6));
    }

    public X500Name(RDN[] rdnArr) throws IOException {
        if (rdnArr == null) {
            this.names = new RDN[0];
            return;
        }
        this.names = (RDN[]) rdnArr.clone();
        for (int i2 = 0; i2 < this.names.length; i2++) {
            if (this.names[i2] == null) {
                throw new IOException("Cannot create an X500Name");
            }
        }
    }

    public X500Name(DerValue derValue) throws IOException {
        this(derValue.toDerInputStream());
    }

    public X500Name(DerInputStream derInputStream) throws IOException {
        parseDER(derInputStream);
    }

    public X500Name(byte[] bArr) throws IOException {
        parseDER(new DerInputStream(bArr));
    }

    public List<RDN> rdns() {
        List<RDN> listUnmodifiableList = this.rdnList;
        if (listUnmodifiableList == null) {
            listUnmodifiableList = Collections.unmodifiableList(Arrays.asList(this.names));
            this.rdnList = listUnmodifiableList;
        }
        return listUnmodifiableList;
    }

    public int size() {
        return this.names.length;
    }

    public List<AVA> allAvas() {
        List<AVA> listUnmodifiableList = this.allAvaList;
        if (listUnmodifiableList == null) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < this.names.length; i2++) {
                arrayList.addAll(this.names[i2].avas());
            }
            listUnmodifiableList = Collections.unmodifiableList(arrayList);
            this.allAvaList = listUnmodifiableList;
        }
        return listUnmodifiableList;
    }

    public int avaSize() {
        return allAvas().size();
    }

    public boolean isEmpty() {
        int length = this.names.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (this.names[i2].assertion.length != 0) {
                return false;
            }
        }
        return true;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return getRFC2253CanonicalName().hashCode();
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X500Name)) {
            return false;
        }
        X500Name x500Name = (X500Name) obj;
        if (this.canonicalDn != null && x500Name.canonicalDn != null) {
            return this.canonicalDn.equals(x500Name.canonicalDn);
        }
        int length = this.names.length;
        if (length != x500Name.names.length) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (this.names[i2].assertion.length != x500Name.names[i2].assertion.length) {
                return false;
            }
        }
        return getRFC2253CanonicalName().equals(x500Name.getRFC2253CanonicalName());
    }

    private String getString(DerValue derValue) throws IOException {
        if (derValue == null) {
            return null;
        }
        String asString = derValue.getAsString();
        if (asString == null) {
            throw new IOException("not a DER string encoding, " + ((int) derValue.tag));
        }
        return asString;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int getType() {
        return 4;
    }

    public String getCountry() throws IOException {
        return getString(findAttribute(countryName_oid));
    }

    public String getOrganization() throws IOException {
        return getString(findAttribute(orgName_oid));
    }

    public String getOrganizationalUnit() throws IOException {
        return getString(findAttribute(orgUnitName_oid));
    }

    public String getCommonName() throws IOException {
        return getString(findAttribute(commonName_oid));
    }

    public String getLocality() throws IOException {
        return getString(findAttribute(localityName_oid));
    }

    public String getState() throws IOException {
        return getString(findAttribute(stateName_oid));
    }

    public String getDomain() throws IOException {
        return getString(findAttribute(DOMAIN_COMPONENT_OID));
    }

    public String getDNQualifier() throws IOException {
        return getString(findAttribute(DNQUALIFIER_OID));
    }

    public String getSurname() throws IOException {
        return getString(findAttribute(SURNAME_OID));
    }

    public String getGivenName() throws IOException {
        return getString(findAttribute(GIVENNAME_OID));
    }

    public String getInitials() throws IOException {
        return getString(findAttribute(INITIALS_OID));
    }

    public String getGeneration() throws IOException {
        return getString(findAttribute(GENERATIONQUALIFIER_OID));
    }

    public String getIP() throws IOException {
        return getString(findAttribute(ipAddress_oid));
    }

    @Override // java.security.Principal
    public String toString() {
        if (this.dn == null) {
            generateDN();
        }
        return this.dn;
    }

    public String getRFC1779Name() {
        return getRFC1779Name(Collections.emptyMap());
    }

    public String getRFC1779Name(Map<String, String> map) throws IllegalArgumentException {
        if (map.isEmpty()) {
            if (this.rfc1779Dn != null) {
                return this.rfc1779Dn;
            }
            this.rfc1779Dn = generateRFC1779DN(map);
            return this.rfc1779Dn;
        }
        return generateRFC1779DN(map);
    }

    public String getRFC2253Name() {
        return getRFC2253Name(Collections.emptyMap());
    }

    public String getRFC2253Name(Map<String, String> map) {
        if (map.isEmpty()) {
            if (this.rfc2253Dn != null) {
                return this.rfc2253Dn;
            }
            this.rfc2253Dn = generateRFC2253DN(map);
            return this.rfc2253Dn;
        }
        return generateRFC2253DN(map);
    }

    private String generateRFC2253DN(Map<String, String> map) {
        if (this.names.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(48);
        for (int length = this.names.length - 1; length >= 0; length--) {
            if (length < this.names.length - 1) {
                sb.append(',');
            }
            sb.append(this.names[length].toRFC2253String(map));
        }
        return sb.toString();
    }

    public String getRFC2253CanonicalName() {
        if (this.canonicalDn != null) {
            return this.canonicalDn;
        }
        if (this.names.length == 0) {
            this.canonicalDn = "";
            return this.canonicalDn;
        }
        StringBuilder sb = new StringBuilder(48);
        for (int length = this.names.length - 1; length >= 0; length--) {
            if (length < this.names.length - 1) {
                sb.append(',');
            }
            sb.append(this.names[length].toRFC2253String(true));
        }
        this.canonicalDn = sb.toString();
        return this.canonicalDn;
    }

    @Override // java.security.Principal
    public String getName() {
        return toString();
    }

    private DerValue findAttribute(ObjectIdentifier objectIdentifier) {
        if (this.names != null) {
            for (int i2 = 0; i2 < this.names.length; i2++) {
                DerValue derValueFindAttribute = this.names[i2].findAttribute(objectIdentifier);
                if (derValueFindAttribute != null) {
                    return derValueFindAttribute;
                }
            }
            return null;
        }
        return null;
    }

    public DerValue findMostSpecificAttribute(ObjectIdentifier objectIdentifier) {
        if (this.names != null) {
            for (int length = this.names.length - 1; length >= 0; length--) {
                DerValue derValueFindAttribute = this.names[length].findAttribute(objectIdentifier);
                if (derValueFindAttribute != null) {
                    return derValueFindAttribute;
                }
            }
            return null;
        }
        return null;
    }

    private void parseDER(DerInputStream derInputStream) throws IOException {
        DerValue[] sequence;
        byte[] byteArray = derInputStream.toByteArray();
        try {
            sequence = derInputStream.getSequence(5);
        } catch (IOException e2) {
            if (byteArray == null) {
                sequence = null;
            } else {
                sequence = new DerInputStream(new DerValue((byte) 48, byteArray).toByteArray()).getSequence(5);
            }
        }
        if (sequence == null) {
            this.names = new RDN[0];
            return;
        }
        this.names = new RDN[sequence.length];
        for (int i2 = 0; i2 < sequence.length; i2++) {
            this.names[i2] = new RDN(sequence[i2]);
        }
    }

    @Deprecated
    public void emit(DerOutputStream derOutputStream) throws IOException {
        encode(derOutputStream);
    }

    @Override // sun.security.x509.GeneralNameInterface
    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (int i2 = 0; i2 < this.names.length; i2++) {
            this.names[i2].encode(derOutputStream2);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public byte[] getEncodedInternal() throws IOException {
        if (this.encoded == null) {
            DerOutputStream derOutputStream = new DerOutputStream();
            DerOutputStream derOutputStream2 = new DerOutputStream();
            for (int i2 = 0; i2 < this.names.length; i2++) {
                this.names[i2].encode(derOutputStream2);
            }
            derOutputStream.write((byte) 48, derOutputStream2);
            this.encoded = derOutputStream.toByteArray();
        }
        return this.encoded;
    }

    public byte[] getEncoded() throws IOException {
        return (byte[]) getEncodedInternal().clone();
    }

    private void parseDN(String str, Map<String, String> map) throws IOException {
        int iMin;
        if (str == null || str.length() == 0) {
            this.names = new RDN[0];
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        int iCountQuotes = 0;
        int i3 = 0;
        int iIndexOf = str.indexOf(44);
        int iIndexOf2 = str.indexOf(59);
        while (true) {
            int i4 = iIndexOf2;
            if (iIndexOf >= 0 || i4 >= 0) {
                if (i4 < 0) {
                    iMin = iIndexOf;
                } else if (iIndexOf < 0) {
                    iMin = i4;
                } else {
                    iMin = Math.min(iIndexOf, i4);
                }
                iCountQuotes += countQuotes(str, i3, iMin);
                if (iMin >= 0 && iCountQuotes != 1 && !escaped(iMin, i3, str)) {
                    arrayList.add(new RDN(str.substring(i2, iMin), map));
                    i2 = iMin + 1;
                    iCountQuotes = 0;
                }
                i3 = iMin + 1;
                iIndexOf = str.indexOf(44, i3);
                iIndexOf2 = str.indexOf(59, i3);
            } else {
                arrayList.add(new RDN(str.substring(i2), map));
                Collections.reverse(arrayList);
                this.names = (RDN[]) arrayList.toArray(new RDN[arrayList.size()]);
                return;
            }
        }
    }

    private void parseRFC2253DN(String str) throws IOException {
        if (str.length() == 0) {
            this.names = new RDN[0];
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        int i3 = 0;
        int iIndexOf = str.indexOf(44);
        while (true) {
            int i4 = iIndexOf;
            if (i4 >= 0) {
                if (i4 > 0 && !escaped(i4, i3, str)) {
                    arrayList.add(new RDN(str.substring(i2, i4), X500Principal.RFC2253));
                    i2 = i4 + 1;
                }
                i3 = i4 + 1;
                iIndexOf = str.indexOf(44, i3);
            } else {
                arrayList.add(new RDN(str.substring(i2), X500Principal.RFC2253));
                Collections.reverse(arrayList);
                this.names = (RDN[]) arrayList.toArray(new RDN[arrayList.size()]);
                return;
            }
        }
    }

    static int countQuotes(String str, int i2, int i3) {
        int i4 = 0;
        for (int i5 = i2; i5 < i3; i5++) {
            if ((str.charAt(i5) == '\"' && i5 == i2) || (str.charAt(i5) == '\"' && str.charAt(i5 - 1) != '\\')) {
                i4++;
            }
        }
        return i4;
    }

    private static boolean escaped(int i2, int i3, String str) {
        if (i2 == 1 && str.charAt(i2 - 1) == '\\') {
            return true;
        }
        if (i2 > 1 && str.charAt(i2 - 1) == '\\' && str.charAt(i2 - 2) != '\\') {
            return true;
        }
        if (i2 > 1 && str.charAt(i2 - 1) == '\\' && str.charAt(i2 - 2) == '\\') {
            int i4 = 0;
            while (true) {
                i2--;
                if (i2 < i3) {
                    break;
                }
                if (str.charAt(i2) == '\\') {
                    i4++;
                }
            }
            return i4 % 2 != 0;
        }
        return false;
    }

    private void generateDN() {
        if (this.names.length == 1) {
            this.dn = this.names[0].toString();
            return;
        }
        StringBuilder sb = new StringBuilder(48);
        if (this.names != null) {
            for (int length = this.names.length - 1; length >= 0; length--) {
                if (length != this.names.length - 1) {
                    sb.append(", ");
                }
                sb.append(this.names[length].toString());
            }
        }
        this.dn = sb.toString();
    }

    private String generateRFC1779DN(Map<String, String> map) {
        if (this.names.length == 1) {
            return this.names[0].toRFC1779String(map);
        }
        StringBuilder sb = new StringBuilder(48);
        if (this.names != null) {
            for (int length = this.names.length - 1; length >= 0; length--) {
                if (length != this.names.length - 1) {
                    sb.append(", ");
                }
                sb.append(this.names[length].toRFC1779String(map));
            }
        }
        return sb.toString();
    }

    static {
        try {
            Object[] objArr = (Object[]) AccessController.doPrivileged(new PrivilegedExceptionAction<Object[]>() { // from class: sun.security.x509.X500Name.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Object[] run() throws Exception {
                    Constructor declaredConstructor = X500Principal.class.getDeclaredConstructor(X500Name.class);
                    declaredConstructor.setAccessible(true);
                    Field declaredField = X500Principal.class.getDeclaredField("thisX500Name");
                    declaredField.setAccessible(true);
                    return new Object[]{declaredConstructor, declaredField};
                }
            });
            principalConstructor = (Constructor) objArr[0];
            principalField = (Field) objArr[1];
        } catch (Exception e2) {
            throw new InternalError("Could not obtain X500Principal access", e2);
        }
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        int i2;
        if (generalNameInterface == null || generalNameInterface.getType() != 4) {
            i2 = -1;
        } else {
            X500Name x500Name = (X500Name) generalNameInterface;
            if (x500Name.equals(this)) {
                i2 = 0;
            } else if (x500Name.names.length == 0) {
                i2 = 2;
            } else if (this.names.length == 0 || x500Name.isWithinSubtree(this)) {
                i2 = 1;
            } else if (isWithinSubtree(x500Name)) {
                i2 = 2;
            } else {
                i2 = 3;
            }
        }
        return i2;
    }

    private boolean isWithinSubtree(X500Name x500Name) {
        if (this == x500Name) {
            return true;
        }
        if (x500Name == null) {
            return false;
        }
        if (x500Name.names.length == 0) {
            return true;
        }
        if (this.names.length == 0 || this.names.length < x500Name.names.length) {
            return false;
        }
        for (int i2 = 0; i2 < x500Name.names.length; i2++) {
            if (!this.names[i2].equals(x500Name.names[i2])) {
                return false;
            }
        }
        return true;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int subtreeDepth() throws UnsupportedOperationException {
        return this.names.length;
    }

    public X500Name commonAncestor(X500Name x500Name) {
        if (x500Name == null) {
            return null;
        }
        int length = x500Name.names.length;
        int length2 = this.names.length;
        if (length2 == 0 || length == 0) {
            return null;
        }
        int i2 = length2 < length ? length2 : length;
        int i3 = 0;
        while (true) {
            if (i3 >= i2) {
                break;
            }
            if (this.names[i3].equals(x500Name.names[i3])) {
                i3++;
            } else if (i3 == 0) {
                return null;
            }
        }
        RDN[] rdnArr = new RDN[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            rdnArr[i4] = this.names[i4];
        }
        try {
            return new X500Name(rdnArr);
        } catch (IOException e2) {
            return null;
        }
    }

    public X500Principal asX500Principal() {
        if (this.x500Principal == null) {
            try {
                this.x500Principal = principalConstructor.newInstance(this);
            } catch (Exception e2) {
                throw new RuntimeException("Unexpected exception", e2);
            }
        }
        return this.x500Principal;
    }

    public static X500Name asX500Name(X500Principal x500Principal) {
        try {
            X500Name x500Name = (X500Name) principalField.get(x500Principal);
            x500Name.x500Principal = x500Principal;
            return x500Name;
        } catch (Exception e2) {
            throw new RuntimeException("Unexpected exception", e2);
        }
    }
}
