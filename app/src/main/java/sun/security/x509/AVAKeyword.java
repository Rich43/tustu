package sun.security.x509;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.util.ObjectIdentifier;

/* compiled from: AVA.java */
/* loaded from: rt.jar:sun/security/x509/AVAKeyword.class */
class AVAKeyword {
    private static final Map<ObjectIdentifier, AVAKeyword> oidMap = new HashMap();
    private static final Map<String, AVAKeyword> keywordMap = new HashMap();
    private String keyword;
    private ObjectIdentifier oid;
    private boolean rfc1779Compliant;
    private boolean rfc2253Compliant;

    private AVAKeyword(String str, ObjectIdentifier objectIdentifier, boolean z2, boolean z3) {
        this.keyword = str;
        this.oid = objectIdentifier;
        this.rfc1779Compliant = z2;
        this.rfc2253Compliant = z3;
        oidMap.put(objectIdentifier, this);
        keywordMap.put(str, this);
    }

    private boolean isCompliant(int i2) {
        switch (i2) {
            case 1:
                return true;
            case 2:
                return this.rfc1779Compliant;
            case 3:
                return this.rfc2253Compliant;
            default:
                throw new IllegalArgumentException("Invalid standard " + i2);
        }
    }

    static ObjectIdentifier getOID(String str, int i2, Map<String, String> map) throws IOException {
        char cCharAt;
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        if (i2 == 3) {
            if (upperCase.startsWith(" ") || upperCase.endsWith(" ")) {
                throw new IOException("Invalid leading or trailing space in keyword \"" + upperCase + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        } else {
            upperCase = upperCase.trim();
        }
        String str2 = map.get(upperCase);
        if (str2 == null) {
            AVAKeyword aVAKeyword = keywordMap.get(upperCase);
            if (aVAKeyword != null && aVAKeyword.isCompliant(i2)) {
                return aVAKeyword.oid;
            }
            if (i2 == 1 && upperCase.startsWith("OID.")) {
                upperCase = upperCase.substring(4);
            }
            boolean z2 = false;
            if (upperCase.length() != 0 && (cCharAt = upperCase.charAt(0)) >= '0' && cCharAt <= '9') {
                z2 = true;
            }
            if (!z2) {
                throw new IOException("Invalid keyword \"" + upperCase + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            return new ObjectIdentifier(upperCase);
        }
        return new ObjectIdentifier(str2);
    }

    static String getKeyword(ObjectIdentifier objectIdentifier, int i2) {
        return getKeyword(objectIdentifier, i2, Collections.emptyMap());
    }

    static String getKeyword(ObjectIdentifier objectIdentifier, int i2, Map<String, String> map) {
        String string = objectIdentifier.toString();
        String str = map.get(string);
        if (str == null) {
            AVAKeyword aVAKeyword = oidMap.get(objectIdentifier);
            if (aVAKeyword != null && aVAKeyword.isCompliant(i2)) {
                return aVAKeyword.keyword;
            }
            if (i2 == 3) {
                return string;
            }
            return "OID." + string;
        }
        if (str.length() == 0) {
            throw new IllegalArgumentException("keyword cannot be empty");
        }
        String strTrim = str.trim();
        char cCharAt = strTrim.charAt(0);
        if (cCharAt < 'A' || cCharAt > 'z' || (cCharAt > 'Z' && cCharAt < 'a')) {
            throw new IllegalArgumentException("keyword does not start with letter");
        }
        for (int i3 = 1; i3 < strTrim.length(); i3++) {
            char cCharAt2 = strTrim.charAt(i3);
            if ((cCharAt2 < 'A' || cCharAt2 > 'z' || (cCharAt2 > 'Z' && cCharAt2 < 'a')) && ((cCharAt2 < '0' || cCharAt2 > '9') && cCharAt2 != '_')) {
                throw new IllegalArgumentException("keyword character is not a letter, digit, or underscore");
            }
        }
        return strTrim;
    }

    static boolean hasKeyword(ObjectIdentifier objectIdentifier, int i2) {
        AVAKeyword aVAKeyword = oidMap.get(objectIdentifier);
        if (aVAKeyword == null) {
            return false;
        }
        return aVAKeyword.isCompliant(i2);
    }

    static {
        new AVAKeyword("CN", X500Name.commonName_oid, true, true);
        new AVAKeyword("C", X500Name.countryName_oid, true, true);
        new AVAKeyword("L", X500Name.localityName_oid, true, true);
        new AVAKeyword(PdfOps.S_TOKEN, X500Name.stateName_oid, false, false);
        new AVAKeyword("ST", X500Name.stateName_oid, true, true);
        new AVAKeyword("O", X500Name.orgName_oid, true, true);
        new AVAKeyword("OU", X500Name.orgUnitName_oid, true, true);
        new AVAKeyword("T", X500Name.title_oid, false, false);
        new AVAKeyword("IP", X500Name.ipAddress_oid, false, false);
        new AVAKeyword("STREET", X500Name.streetAddress_oid, true, true);
        new AVAKeyword("DC", X500Name.DOMAIN_COMPONENT_OID, false, true);
        new AVAKeyword("DNQUALIFIER", X500Name.DNQUALIFIER_OID, false, false);
        new AVAKeyword("DNQ", X500Name.DNQUALIFIER_OID, false, false);
        new AVAKeyword("SURNAME", X500Name.SURNAME_OID, false, false);
        new AVAKeyword("GIVENNAME", X500Name.GIVENNAME_OID, false, false);
        new AVAKeyword("INITIALS", X500Name.INITIALS_OID, false, false);
        new AVAKeyword("GENERATION", X500Name.GENERATIONQUALIFIER_OID, false, false);
        new AVAKeyword("EMAIL", PKCS9Attribute.EMAIL_ADDRESS_OID, false, false);
        new AVAKeyword("EMAILADDRESS", PKCS9Attribute.EMAIL_ADDRESS_OID, false, false);
        new AVAKeyword("UID", X500Name.userid_oid, false, true);
        new AVAKeyword("SERIALNUMBER", X500Name.SERIALNUMBER_OID, false, false);
    }
}
