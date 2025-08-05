package sun.security.krb5;

import java.io.IOException;
import java.security.AccessController;
import java.util.LinkedList;
import sun.security.action.GetBooleanAction;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.util.KerberosString;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/Realm.class */
public class Realm implements Cloneable {
    public static final boolean AUTODEDUCEREALM = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.security.krb5.autodeducerealm"))).booleanValue();
    private final String realm;

    public Realm(String str) throws RealmException {
        this.realm = parseRealm(str);
    }

    public static Realm getDefault() throws RealmException {
        try {
            return new Realm(Config.getInstance().getDefaultRealm());
        } catch (RealmException e2) {
            throw e2;
        } catch (KrbException e3) {
            throw new RealmException(e3);
        }
    }

    public Object clone() {
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Realm)) {
            return false;
        }
        return this.realm.equals(((Realm) obj).realm);
    }

    public int hashCode() {
        return this.realm.hashCode();
    }

    public Realm(DerValue derValue) throws Asn1Exception, RealmException, IOException {
        if (derValue == null) {
            throw new IllegalArgumentException("encoding can not be null");
        }
        this.realm = new KerberosString(derValue).toString();
        if (this.realm == null || this.realm.length() == 0) {
            throw new RealmException(601);
        }
        if (!isValidRealmString(this.realm)) {
            throw new RealmException(600);
        }
    }

    public String toString() {
        return this.realm;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x006a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String parseRealmAtSeparator(java.lang.String r4) throws sun.security.krb5.RealmException {
        /*
            r0 = r4
            if (r0 != 0) goto Le
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.String r2 = "null input name is not allowed"
            r1.<init>(r2)
            throw r0
        Le:
            java.lang.String r0 = new java.lang.String
            r1 = r0
            r2 = r4
            r1.<init>(r2)
            r5 = r0
            r0 = 0
            r6 = r0
            r0 = 0
            r7 = r0
        L1b:
            r0 = r7
            r1 = r5
            int r1 = r1.length()
            if (r0 >= r1) goto L66
            r0 = r5
            r1 = r7
            char r0 = r0.charAt(r1)
            r1 = 64
            if (r0 != r1) goto L60
            r0 = r7
            if (r0 == 0) goto L3d
            r0 = r5
            r1 = r7
            r2 = 1
            int r1 = r1 - r2
            char r0 = r0.charAt(r1)
            r1 = 92
            if (r0 == r1) goto L60
        L3d:
            r0 = r7
            r1 = 1
            int r0 = r0 + r1
            r1 = r5
            int r1 = r1.length()
            if (r0 >= r1) goto L56
            r0 = r5
            r1 = r7
            r2 = 1
            int r1 = r1 + r2
            r2 = r5
            int r2 = r2.length()
            java.lang.String r0 = r0.substring(r1, r2)
            r6 = r0
            goto L66
        L56:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.String r2 = "empty realm part not allowed"
            r1.<init>(r2)
            throw r0
        L60:
            int r7 = r7 + 1
            goto L1b
        L66:
            r0 = r6
            if (r0 == 0) goto L8e
            r0 = r6
            int r0 = r0.length()
            if (r0 != 0) goto L7c
            sun.security.krb5.RealmException r0 = new sun.security.krb5.RealmException
            r1 = r0
            r2 = 601(0x259, float:8.42E-43)
            r1.<init>(r2)
            throw r0
        L7c:
            r0 = r6
            boolean r0 = isValidRealmString(r0)
            if (r0 != 0) goto L8e
            sun.security.krb5.RealmException r0 = new sun.security.krb5.RealmException
            r1 = r0
            r2 = 600(0x258, float:8.41E-43)
            r1.<init>(r2)
            throw r0
        L8e:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.krb5.Realm.parseRealmAtSeparator(java.lang.String):java.lang.String");
    }

    public static String parseRealmComponent(String str) {
        if (str == null) {
            throw new IllegalArgumentException("null input name is not allowed");
        }
        String str2 = new String(str);
        String strSubstring = null;
        for (int i2 = 0; i2 < str2.length(); i2++) {
            if (str2.charAt(i2) == '.' && (i2 == 0 || str2.charAt(i2 - 1) != '\\')) {
                if (i2 + 1 < str2.length()) {
                    strSubstring = str2.substring(i2 + 1, str2.length());
                }
                return strSubstring;
            }
        }
        return strSubstring;
    }

    protected static String parseRealm(String str) throws RealmException {
        String realmAtSeparator = parseRealmAtSeparator(str);
        if (realmAtSeparator == null) {
            realmAtSeparator = str;
        }
        if (realmAtSeparator == null || realmAtSeparator.length() == 0) {
            throw new RealmException(601);
        }
        if (!isValidRealmString(realmAtSeparator)) {
            throw new RealmException(600);
        }
        return realmAtSeparator;
    }

    protected static boolean isValidRealmString(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == '/' || str.charAt(i2) == 0) {
                return false;
            }
        }
        return true;
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putDerValue(new KerberosString(this.realm).toDerValue());
        return derOutputStream.toByteArray();
    }

    public static Realm parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException, RealmException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new Realm(derValue.getData().getDerValue());
    }

    public static String[] getRealmsList(String str, String str2) {
        try {
            return parseCapaths(str, str2);
        } catch (KrbException e2) {
            return parseHierarchy(str, str2);
        }
    }

    private static String[] parseCapaths(String str, String str2) throws KrbException {
        Config config = Config.getInstance();
        if (!config.exists("capaths", str, str2)) {
            throw new KrbException("No conf");
        }
        LinkedList linkedList = new LinkedList();
        String str3 = str2;
        while (true) {
            String str4 = str3;
            String all = config.getAll("capaths", str, str4);
            if (all == null) {
                break;
            }
            String[] strArrSplit = all.split("\\s+");
            boolean z2 = false;
            for (int length = strArrSplit.length - 1; length >= 0; length--) {
                if (!linkedList.contains(strArrSplit[length]) && !strArrSplit[length].equals(".") && !strArrSplit[length].equals(str) && !strArrSplit[length].equals(str2) && !strArrSplit[length].equals(str4)) {
                    z2 = true;
                    linkedList.addFirst(strArrSplit[length]);
                }
            }
            if (!z2) {
                break;
            }
            str3 = (String) linkedList.getFirst();
        }
        linkedList.addFirst(str);
        return (String[]) linkedList.toArray(new String[linkedList.size()]);
    }

    private static String[] parseHierarchy(String str, String str2) {
        boolean z2;
        String[] strArrSplit = str.split("\\.");
        String[] strArrSplit2 = str2.split("\\.");
        int length = strArrSplit.length;
        int length2 = strArrSplit2.length;
        boolean z3 = false;
        while (true) {
            z2 = z3;
            length2--;
            length--;
            if (length2 < 0 || length < 0 || !strArrSplit2[length2].equals(strArrSplit[length])) {
                break;
            }
            z3 = true;
        }
        LinkedList linkedList = new LinkedList();
        for (int i2 = 0; i2 <= length; i2++) {
            linkedList.addLast(subStringFrom(strArrSplit, i2));
        }
        if (z2) {
            linkedList.addLast(subStringFrom(strArrSplit, length + 1));
        }
        for (int i3 = length2; i3 >= 0; i3--) {
            linkedList.addLast(subStringFrom(strArrSplit2, i3));
        }
        linkedList.removeLast();
        return (String[]) linkedList.toArray(new String[linkedList.size()]);
    }

    private static String subStringFrom(String[] strArr, int i2) {
        StringBuilder sb = new StringBuilder();
        for (int i3 = i2; i3 < strArr.length; i3++) {
            if (sb.length() != 0) {
                sb.append('.');
            }
            sb.append(strArr[i3]);
        }
        return sb.toString();
    }
}
