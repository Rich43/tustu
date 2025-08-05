package sun.security.krb5;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;
import sun.misc.Unsafe;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.ccache.CCacheOutputStream;
import sun.security.krb5.internal.util.KerberosString;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/PrincipalName.class */
public class PrincipalName implements Cloneable {
    public static final int KRB_NT_UNKNOWN = 0;
    public static final int KRB_NT_PRINCIPAL = 1;
    public static final int KRB_NT_SRV_INST = 2;
    public static final int KRB_NT_SRV_HST = 3;
    public static final int KRB_NT_SRV_XHST = 4;
    public static final int KRB_NT_UID = 5;
    public static final int KRB_NT_ENTERPRISE = 10;
    public static final String TGS_DEFAULT_SRV_NAME = "krbtgt";
    public static final int TGS_DEFAULT_NT = 2;
    public static final char NAME_COMPONENT_SEPARATOR = '/';
    public static final char NAME_REALM_SEPARATOR = '@';
    public static final char REALM_COMPONENT_SEPARATOR = '.';
    public static final String NAME_COMPONENT_SEPARATOR_STR = "/";
    public static final String NAME_REALM_SEPARATOR_STR = "@";
    public static final String REALM_COMPONENT_SEPARATOR_STR = ".";
    private final int nameType;
    private final String[] nameStrings;
    private final Realm nameRealm;
    private final boolean realmDeduced;
    private transient String salt;
    private static final long NAME_STRINGS_OFFSET;
    private static final Unsafe UNSAFE;

    public PrincipalName(int i2, String[] strArr, Realm realm) {
        this.salt = null;
        if (realm == null) {
            throw new IllegalArgumentException("Null realm not allowed");
        }
        validateNameStrings(strArr);
        this.nameType = i2;
        this.nameStrings = (String[]) strArr.clone();
        this.nameRealm = realm;
        this.realmDeduced = false;
    }

    public PrincipalName(String[] strArr, String str) throws RealmException {
        this(0, strArr, new Realm(str));
    }

    private static void validateNameStrings(String[] strArr) {
        if (strArr == null) {
            throw new IllegalArgumentException("Null nameStrings not allowed");
        }
        if (strArr.length == 0) {
            throw new IllegalArgumentException("Empty nameStrings not allowed");
        }
        for (String str : strArr) {
            if (str == null) {
                throw new IllegalArgumentException("Null nameString not allowed");
            }
            if (str.isEmpty()) {
                throw new IllegalArgumentException("Empty nameString not allowed");
            }
        }
    }

    public Object clone() {
        try {
            PrincipalName principalName = (PrincipalName) super.clone();
            UNSAFE.putObject(this, NAME_STRINGS_OFFSET, this.nameStrings.clone());
            return principalName;
        } catch (CloneNotSupportedException e2) {
            throw new AssertionError((Object) "Should never happen");
        }
    }

    static {
        try {
            Unsafe unsafe = Unsafe.getUnsafe();
            NAME_STRINGS_OFFSET = unsafe.objectFieldOffset(PrincipalName.class.getDeclaredField("nameStrings"));
            UNSAFE = unsafe;
        } catch (ReflectiveOperationException e2) {
            throw new Error(e2);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PrincipalName) {
            PrincipalName principalName = (PrincipalName) obj;
            return this.nameRealm.equals(principalName.nameRealm) && Arrays.equals(this.nameStrings, principalName.nameStrings);
        }
        return false;
    }

    public PrincipalName(DerValue derValue, Realm realm) throws Asn1Exception, IOException {
        this.salt = null;
        if (realm == null) {
            throw new IllegalArgumentException("Null realm not allowed");
        }
        this.realmDeduced = false;
        this.nameRealm = realm;
        if (derValue == null) {
            throw new IllegalArgumentException("Null encoding not allowed");
        }
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.nameType = derValue2.getData().getBigInteger().intValue();
            DerValue derValue3 = derValue.getData().getDerValue();
            if ((derValue3.getTag() & 31) == 1) {
                DerValue derValue4 = derValue3.getData().getDerValue();
                if (derValue4.getTag() != 48) {
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                }
                Vector vector = new Vector();
                while (derValue4.getData().available() > 0) {
                    vector.addElement(new KerberosString(derValue4.getData().getDerValue()).toString());
                }
                this.nameStrings = new String[vector.size()];
                vector.copyInto(this.nameStrings);
                validateNameStrings(this.nameStrings);
                return;
            }
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public static PrincipalName parse(DerInputStream derInputStream, byte b2, boolean z2, Realm realm) throws Asn1Exception, IOException, RealmException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (realm == null) {
            realm = Realm.getDefault();
        }
        return new PrincipalName(derValue2, realm);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00dd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String[] parseName(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 254
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.krb5.PrincipalName.parseName(java.lang.String):java.lang.String[]");
    }

    public PrincipalName(String str, int i2, String str2) throws RealmException {
        this.salt = null;
        if (str == null) {
            throw new IllegalArgumentException("Null name not allowed");
        }
        String[] name = parseName(str);
        validateNameStrings(name);
        str2 = str2 == null ? Realm.parseRealmAtSeparator(str) : str2;
        this.realmDeduced = str2 == null;
        switch (i2) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 5:
            case 10:
                this.nameStrings = name;
                this.nameType = i2;
                if (str2 != null) {
                    this.nameRealm = new Realm(str2);
                    return;
                } else {
                    this.nameRealm = Realm.getDefault();
                    return;
                }
            case 3:
                if (name.length >= 2) {
                    String str3 = name[1];
                    try {
                        String canonicalHostName = InetAddress.getByName(str3).getCanonicalHostName();
                        str3 = canonicalHostName.toLowerCase(Locale.ENGLISH).startsWith(new StringBuilder().append(str3.toLowerCase(Locale.ENGLISH)).append(".").toString()) ? canonicalHostName : str3;
                    } catch (SecurityException | UnknownHostException e2) {
                    }
                    name[1] = (str3.endsWith(".") ? str3.substring(0, str3.length() - 1) : str3).toLowerCase(Locale.ENGLISH);
                }
                this.nameStrings = name;
                this.nameType = i2;
                if (str2 != null) {
                    this.nameRealm = new Realm(str2);
                    return;
                }
                String strMapHostToRealm = mapHostToRealm(name[1]);
                if (strMapHostToRealm != null) {
                    this.nameRealm = new Realm(strMapHostToRealm);
                    return;
                } else {
                    this.nameRealm = Realm.getDefault();
                    return;
                }
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                throw new IllegalArgumentException("Illegal name type");
        }
    }

    public PrincipalName(String str, int i2) throws RealmException {
        this(str, i2, (String) null);
    }

    public PrincipalName(String str) throws RealmException {
        this(str, 0);
    }

    public PrincipalName(String str, String str2) throws RealmException {
        this(str, 0, str2);
    }

    public static PrincipalName tgsService(String str, String str2) throws KrbException {
        return new PrincipalName(2, new String[]{TGS_DEFAULT_SRV_NAME, str}, new Realm(str2));
    }

    public String getRealmAsString() {
        return getRealmString();
    }

    public String getPrincipalNameAsString() {
        StringBuffer stringBuffer = new StringBuffer(this.nameStrings[0]);
        for (int i2 = 1; i2 < this.nameStrings.length; i2++) {
            stringBuffer.append(this.nameStrings[i2]);
        }
        return stringBuffer.toString();
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public String getName() {
        return toString();
    }

    public int getNameType() {
        return this.nameType;
    }

    public String[] getNameStrings() {
        return (String[]) this.nameStrings.clone();
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    public byte[][] toByteArray() {
        ?? r0 = new byte[this.nameStrings.length];
        for (int i2 = 0; i2 < this.nameStrings.length; i2++) {
            r0[i2] = new byte[this.nameStrings[i2].length()];
            r0[i2] = this.nameStrings[i2].getBytes();
        }
        return r0;
    }

    public String getRealmString() {
        return this.nameRealm.toString();
    }

    public Realm getRealm() {
        return this.nameRealm;
    }

    public String getSalt() {
        if (this.salt == null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.nameRealm.toString());
            for (int i2 = 0; i2 < this.nameStrings.length; i2++) {
                stringBuffer.append(this.nameStrings[i2]);
            }
            return stringBuffer.toString();
        }
        return this.salt;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.nameStrings.length; i2++) {
            if (i2 > 0) {
                stringBuffer.append("/");
            }
            stringBuffer.append(this.nameStrings[i2].replace("@", "\\@"));
        }
        stringBuffer.append("@");
        stringBuffer.append(this.nameRealm.toString());
        return stringBuffer.toString();
    }

    public String getNameString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.nameStrings.length; i2++) {
            if (i2 > 0) {
                stringBuffer.append("/");
            }
            stringBuffer.append(this.nameStrings[i2]);
        }
        return stringBuffer.toString();
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(BigInteger.valueOf(this.nameType));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        DerValue[] derValueArr = new DerValue[this.nameStrings.length];
        for (int i2 = 0; i2 < this.nameStrings.length; i2++) {
            derValueArr[i2] = new KerberosString(this.nameStrings[i2]).toDerValue();
        }
        derOutputStream3.putSequence(derValueArr);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }

    public boolean match(PrincipalName principalName) {
        boolean z2 = true;
        if (this.nameRealm != null && principalName.nameRealm != null && !this.nameRealm.toString().equalsIgnoreCase(principalName.nameRealm.toString())) {
            z2 = false;
        }
        if (this.nameStrings.length != principalName.nameStrings.length) {
            z2 = false;
        } else {
            for (int i2 = 0; i2 < this.nameStrings.length; i2++) {
                if (!this.nameStrings[i2].equalsIgnoreCase(principalName.nameStrings[i2])) {
                    z2 = false;
                }
            }
        }
        return z2;
    }

    public void writePrincipal(CCacheOutputStream cCacheOutputStream) throws IOException {
        cCacheOutputStream.write32(this.nameType);
        cCacheOutputStream.write32(this.nameStrings.length);
        byte[] bytes = this.nameRealm.toString().getBytes();
        cCacheOutputStream.write32(bytes.length);
        cCacheOutputStream.write(bytes, 0, bytes.length);
        for (int i2 = 0; i2 < this.nameStrings.length; i2++) {
            byte[] bytes2 = this.nameStrings[i2].getBytes();
            cCacheOutputStream.write32(bytes2.length);
            cCacheOutputStream.write(bytes2, 0, bytes2.length);
        }
    }

    public String getInstanceComponent() {
        if (this.nameStrings != null && this.nameStrings.length >= 2) {
            return new String(this.nameStrings[1]);
        }
        return null;
    }

    static String mapHostToRealm(String str) {
        Config config;
        String str2;
        String str3 = null;
        try {
            config = Config.getInstance();
            str2 = config.get("domain_realm", str);
            str3 = str2;
        } catch (KrbException e2) {
        }
        if (str2 != null) {
            return str3;
        }
        for (int i2 = 1; i2 < str.length(); i2++) {
            if (str.charAt(i2) == '.' && i2 != str.length() - 1) {
                str3 = config.get("domain_realm", str.substring(i2));
                if (str3 != null) {
                    break;
                }
                str3 = config.get("domain_realm", str.substring(i2 + 1));
                if (str3 != null) {
                    break;
                }
            }
        }
        return str3;
    }

    public boolean isRealmDeduced() {
        return this.realmDeduced;
    }
}
