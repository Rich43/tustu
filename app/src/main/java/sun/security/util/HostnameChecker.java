package sun.security.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.Normalizer;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringTokenizer;
import javax.net.ssl.SNIHostName;
import sun.net.util.IPAddressUtil;
import sun.security.ssl.Krb5Helper;
import sun.security.ssl.SSLLogger;
import sun.security.util.RegisteredDomain;
import sun.security.x509.X500Name;

/* loaded from: rt.jar:sun/security/util/HostnameChecker.class */
public class HostnameChecker {
    public static final byte TYPE_TLS = 1;
    public static final byte TYPE_LDAP = 2;
    private static final int ALTNAME_DNS = 2;
    private static final int ALTNAME_IP = 7;
    private final byte checkType;
    private static final HostnameChecker INSTANCE_TLS = new HostnameChecker((byte) 1);
    private static final HostnameChecker INSTANCE_LDAP = new HostnameChecker((byte) 2);

    private HostnameChecker(byte b2) {
        this.checkType = b2;
    }

    public static HostnameChecker getInstance(byte b2) {
        if (b2 == 1) {
            return INSTANCE_TLS;
        }
        if (b2 == 2) {
            return INSTANCE_LDAP;
        }
        throw new IllegalArgumentException("Unknown check type: " + ((int) b2));
    }

    public void match(String str, X509Certificate x509Certificate, boolean z2) throws CertificateException {
        if (str == null) {
            throw new CertificateException("Hostname or IP address is undefined.");
        }
        if (isIpAddress(str)) {
            matchIP(str, x509Certificate);
        } else {
            matchDNS(str, x509Certificate, z2);
        }
    }

    public void match(String str, X509Certificate x509Certificate) throws CertificateException {
        match(str, x509Certificate, false);
    }

    public static boolean match(String str, Principal principal) {
        return str.equalsIgnoreCase(getServerName(principal));
    }

    public static String getServerName(Principal principal) {
        return Krb5Helper.getPrincipalHostName(principal);
    }

    private static boolean isIpAddress(String str) {
        if (IPAddressUtil.isIPv4LiteralAddress(str) || IPAddressUtil.isIPv6LiteralAddress(str)) {
            return true;
        }
        return false;
    }

    private static void matchIP(String str, X509Certificate x509Certificate) throws CertificateException {
        Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
        if (subjectAlternativeNames == null) {
            throw new CertificateException("No subject alternative names present");
        }
        for (List<?> list : subjectAlternativeNames) {
            if (((Integer) list.get(0)).intValue() == 7) {
                String str2 = (String) list.get(1);
                if (str.equalsIgnoreCase(str2)) {
                    return;
                }
                try {
                    if (InetAddress.getByName(str).equals(InetAddress.getByName(str2))) {
                        return;
                    }
                } catch (SecurityException e2) {
                } catch (UnknownHostException e3) {
                }
            }
        }
        throw new CertificateException("No subject alternative names matching IP address " + str + " found");
    }

    private void matchDNS(String str, X509Certificate x509Certificate, boolean z2) throws CertificateException {
        try {
            new SNIHostName(str);
            Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames != null) {
                boolean z3 = false;
                for (List<?> list : subjectAlternativeNames) {
                    if (((Integer) list.get(0)).intValue() == 2) {
                        z3 = true;
                        if (isMatched(str, (String) list.get(1), z2)) {
                            return;
                        }
                    }
                }
                if (z3) {
                    throw new CertificateException("No subject alternative DNS name matching " + str + " found.");
                }
            }
            DerValue derValueFindMostSpecificAttribute = getSubjectX500Name(x509Certificate).findMostSpecificAttribute(X500Name.commonName_oid);
            if (derValueFindMostSpecificAttribute != null) {
                try {
                    String asString = derValueFindMostSpecificAttribute.getAsString();
                    if (!Normalizer.isNormalized(asString, Normalizer.Form.NFKC)) {
                        throw new CertificateException("Not a formal name " + asString);
                    }
                    if (isMatched(str, asString, z2)) {
                        return;
                    }
                } catch (IOException e2) {
                }
            }
            throw new CertificateException("No name matching " + str + " found");
        } catch (IllegalArgumentException e3) {
            throw new CertificateException("Illegal given domain name: " + str, e3);
        }
    }

    public static X500Name getSubjectX500Name(X509Certificate x509Certificate) throws CertificateParsingException {
        try {
            Principal subjectDN = x509Certificate.getSubjectDN();
            if (subjectDN instanceof X500Name) {
                return (X500Name) subjectDN;
            }
            return new X500Name(x509Certificate.getSubjectX500Principal().getEncoded());
        } catch (IOException e2) {
            throw ((CertificateParsingException) new CertificateParsingException().initCause(e2));
        }
    }

    private boolean isMatched(String str, String str2, boolean z2) {
        if (hasIllegalWildcard(str, str2, z2)) {
            return false;
        }
        try {
            new SNIHostName(str2.replace('*', 'z'));
            if (this.checkType == 1) {
                return matchAllWildcards(str, str2);
            }
            if (this.checkType == 2) {
                return matchLeftmostWildcard(str, str2);
            }
            return false;
        } catch (IllegalArgumentException e2) {
            return false;
        }
    }

    private static boolean hasIllegalWildcard(String str, String str2, boolean z2) {
        if (str2.equals("*") || str2.equals("*.")) {
            if (SSLLogger.isOn) {
                SSLLogger.fine("Certificate domain name has illegal single wildcard character: " + str2, new Object[0]);
                return true;
            }
            return true;
        }
        int iLastIndexOf = str2.lastIndexOf("*");
        if (iLastIndexOf == -1) {
            return false;
        }
        String strSubstring = str2.substring(iLastIndexOf);
        int iIndexOf = strSubstring.indexOf(".");
        if (iIndexOf == -1) {
            if (SSLLogger.isOn) {
                SSLLogger.fine("Certificate domain name has illegal wildcard, no dot after wildcard character: " + str2, new Object[0]);
                return true;
            }
            return true;
        }
        if (!z2) {
            return false;
        }
        Optional<RegisteredDomain> optionalFilter = RegisteredDomain.from(str).filter(registeredDomain -> {
            return registeredDomain.type() == RegisteredDomain.Type.ICANN;
        });
        if (optionalFilter.isPresent()) {
            if (optionalFilter.get().publicSuffix().equalsIgnoreCase(strSubstring.substring(iIndexOf + 1))) {
                if (SSLLogger.isOn) {
                    SSLLogger.fine("Certificate domain name has illegal wildcard for public suffix: " + str2, new Object[0]);
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private static boolean matchAllWildcards(String str, String str2) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        String lowerCase2 = str2.toLowerCase(Locale.ENGLISH);
        StringTokenizer stringTokenizer = new StringTokenizer(lowerCase, ".");
        StringTokenizer stringTokenizer2 = new StringTokenizer(lowerCase2, ".");
        if (stringTokenizer.countTokens() != stringTokenizer2.countTokens()) {
            return false;
        }
        while (stringTokenizer.hasMoreTokens()) {
            if (!matchWildCards(stringTokenizer.nextToken(), stringTokenizer2.nextToken())) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchLeftmostWildcard(String str, String str2) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        String lowerCase2 = str2.toLowerCase(Locale.ENGLISH);
        int iIndexOf = lowerCase2.indexOf(".");
        int iIndexOf2 = lowerCase.indexOf(".");
        if (iIndexOf == -1) {
            iIndexOf = lowerCase2.length();
        }
        if (iIndexOf2 == -1) {
            iIndexOf2 = lowerCase.length();
        }
        if (matchWildCards(lowerCase.substring(0, iIndexOf2), lowerCase2.substring(0, iIndexOf))) {
            return lowerCase2.substring(iIndexOf).equals(lowerCase.substring(iIndexOf2));
        }
        return false;
    }

    private static boolean matchWildCards(String str, String str2) {
        int iIndexOf = str2.indexOf("*");
        if (iIndexOf == -1) {
            return str.equals(str2);
        }
        boolean z2 = true;
        String strSubstring = str2;
        while (iIndexOf != -1) {
            String strSubstring2 = strSubstring.substring(0, iIndexOf);
            strSubstring = strSubstring.substring(iIndexOf + 1);
            int iIndexOf2 = str.indexOf(strSubstring2);
            if (iIndexOf2 == -1) {
                return false;
            }
            if (z2 && iIndexOf2 != 0) {
                return false;
            }
            z2 = false;
            str = str.substring(iIndexOf2 + strSubstring2.length());
            iIndexOf = strSubstring.indexOf("*");
        }
        return str.endsWith(strSubstring);
    }
}
