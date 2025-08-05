package sun.security.x509;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.datatype.DatatypeConstants;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/OIDMap.class */
public class OIDMap {
    private static final String ROOT = "x509.info.extensions";
    private static final String AUTH_KEY_IDENTIFIER = "x509.info.extensions.AuthorityKeyIdentifier";
    private static final String SUB_KEY_IDENTIFIER = "x509.info.extensions.SubjectKeyIdentifier";
    private static final String KEY_USAGE = "x509.info.extensions.KeyUsage";
    private static final String PRIVATE_KEY_USAGE = "x509.info.extensions.PrivateKeyUsage";
    private static final String POLICY_MAPPINGS = "x509.info.extensions.PolicyMappings";
    private static final String SUB_ALT_NAME = "x509.info.extensions.SubjectAlternativeName";
    private static final String ISSUER_ALT_NAME = "x509.info.extensions.IssuerAlternativeName";
    private static final String BASIC_CONSTRAINTS = "x509.info.extensions.BasicConstraints";
    private static final String NAME_CONSTRAINTS = "x509.info.extensions.NameConstraints";
    private static final String POLICY_CONSTRAINTS = "x509.info.extensions.PolicyConstraints";
    private static final String CRL_NUMBER = "x509.info.extensions.CRLNumber";
    private static final String CRL_REASON = "x509.info.extensions.CRLReasonCode";
    private static final String NETSCAPE_CERT = "x509.info.extensions.NetscapeCertType";
    private static final String CERT_POLICIES = "x509.info.extensions.CertificatePolicies";
    private static final String EXT_KEY_USAGE = "x509.info.extensions.ExtendedKeyUsage";
    private static final String INHIBIT_ANY_POLICY = "x509.info.extensions.InhibitAnyPolicy";
    private static final String CRL_DIST_POINTS = "x509.info.extensions.CRLDistributionPoints";
    private static final String CERT_ISSUER = "x509.info.extensions.CertificateIssuer";
    private static final String SUBJECT_INFO_ACCESS = "x509.info.extensions.SubjectInfoAccess";
    private static final String AUTH_INFO_ACCESS = "x509.info.extensions.AuthorityInfoAccess";
    private static final String ISSUING_DIST_POINT = "x509.info.extensions.IssuingDistributionPoint";
    private static final String DELTA_CRL_INDICATOR = "x509.info.extensions.DeltaCRLIndicator";
    private static final String FRESHEST_CRL = "x509.info.extensions.FreshestCRL";
    private static final String OCSPNOCHECK = "x509.info.extensions.OCSPNoCheck";
    private static final int[] NetscapeCertType_data = {2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 113730, 1, 1};
    private static final Map<ObjectIdentifier, OIDInfo> oidMap = new HashMap();
    private static final Map<String, OIDInfo> nameMap = new HashMap();

    private OIDMap() {
    }

    static {
        addInternal("x509.info.extensions.SubjectKeyIdentifier", PKIXExtensions.SubjectKey_Id, "sun.security.x509.SubjectKeyIdentifierExtension");
        addInternal("x509.info.extensions.KeyUsage", PKIXExtensions.KeyUsage_Id, "sun.security.x509.KeyUsageExtension");
        addInternal("x509.info.extensions.PrivateKeyUsage", PKIXExtensions.PrivateKeyUsage_Id, "sun.security.x509.PrivateKeyUsageExtension");
        addInternal("x509.info.extensions.SubjectAlternativeName", PKIXExtensions.SubjectAlternativeName_Id, "sun.security.x509.SubjectAlternativeNameExtension");
        addInternal("x509.info.extensions.IssuerAlternativeName", PKIXExtensions.IssuerAlternativeName_Id, "sun.security.x509.IssuerAlternativeNameExtension");
        addInternal("x509.info.extensions.BasicConstraints", PKIXExtensions.BasicConstraints_Id, "sun.security.x509.BasicConstraintsExtension");
        addInternal(CRL_NUMBER, PKIXExtensions.CRLNumber_Id, "sun.security.x509.CRLNumberExtension");
        addInternal(CRL_REASON, PKIXExtensions.ReasonCode_Id, "sun.security.x509.CRLReasonCodeExtension");
        addInternal("x509.info.extensions.NameConstraints", PKIXExtensions.NameConstraints_Id, "sun.security.x509.NameConstraintsExtension");
        addInternal("x509.info.extensions.PolicyMappings", PKIXExtensions.PolicyMappings_Id, "sun.security.x509.PolicyMappingsExtension");
        addInternal("x509.info.extensions.AuthorityKeyIdentifier", PKIXExtensions.AuthorityKey_Id, "sun.security.x509.AuthorityKeyIdentifierExtension");
        addInternal("x509.info.extensions.PolicyConstraints", PKIXExtensions.PolicyConstraints_Id, "sun.security.x509.PolicyConstraintsExtension");
        addInternal("x509.info.extensions.NetscapeCertType", ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 113730, 1, 1}), "sun.security.x509.NetscapeCertTypeExtension");
        addInternal("x509.info.extensions.CertificatePolicies", PKIXExtensions.CertificatePolicies_Id, "sun.security.x509.CertificatePoliciesExtension");
        addInternal("x509.info.extensions.ExtendedKeyUsage", PKIXExtensions.ExtendedKeyUsage_Id, "sun.security.x509.ExtendedKeyUsageExtension");
        addInternal("x509.info.extensions.InhibitAnyPolicy", PKIXExtensions.InhibitAnyPolicy_Id, "sun.security.x509.InhibitAnyPolicyExtension");
        addInternal("x509.info.extensions.CRLDistributionPoints", PKIXExtensions.CRLDistributionPoints_Id, "sun.security.x509.CRLDistributionPointsExtension");
        addInternal(CERT_ISSUER, PKIXExtensions.CertificateIssuer_Id, "sun.security.x509.CertificateIssuerExtension");
        addInternal("x509.info.extensions.SubjectInfoAccess", PKIXExtensions.SubjectInfoAccess_Id, "sun.security.x509.SubjectInfoAccessExtension");
        addInternal("x509.info.extensions.AuthorityInfoAccess", PKIXExtensions.AuthInfoAccess_Id, "sun.security.x509.AuthorityInfoAccessExtension");
        addInternal("x509.info.extensions.IssuingDistributionPoint", PKIXExtensions.IssuingDistributionPoint_Id, "sun.security.x509.IssuingDistributionPointExtension");
        addInternal(DELTA_CRL_INDICATOR, PKIXExtensions.DeltaCRLIndicator_Id, "sun.security.x509.DeltaCRLIndicatorExtension");
        addInternal(FRESHEST_CRL, PKIXExtensions.FreshestCRL_Id, "sun.security.x509.FreshestCRLExtension");
        addInternal("x509.info.extensions.OCSPNoCheck", PKIXExtensions.OCSPNoCheck_Id, "sun.security.x509.OCSPNoCheckExtension");
    }

    private static void addInternal(String str, ObjectIdentifier objectIdentifier, String str2) {
        OIDInfo oIDInfo = new OIDInfo(str, objectIdentifier, str2);
        oidMap.put(objectIdentifier, oIDInfo);
        nameMap.put(str, oIDInfo);
    }

    /* loaded from: rt.jar:sun/security/x509/OIDMap$OIDInfo.class */
    private static class OIDInfo {
        final ObjectIdentifier oid;
        final String name;
        final String className;
        private volatile Class<?> clazz;

        OIDInfo(String str, ObjectIdentifier objectIdentifier, String str2) {
            this.name = str;
            this.oid = objectIdentifier;
            this.className = str2;
        }

        OIDInfo(String str, ObjectIdentifier objectIdentifier, Class<?> cls) {
            this.name = str;
            this.oid = objectIdentifier;
            this.className = cls.getName();
            this.clazz = cls;
        }

        Class<?> getClazz() throws CertificateException {
            try {
                Class<?> cls = this.clazz;
                if (cls == null) {
                    cls = Class.forName(this.className);
                    this.clazz = cls;
                }
                return cls;
            } catch (ClassNotFoundException e2) {
                throw new CertificateException("Could not load class: " + ((Object) e2), e2);
            }
        }
    }

    public static void addAttribute(String str, String str2, Class<?> cls) throws CertificateException {
        try {
            ObjectIdentifier objectIdentifier = new ObjectIdentifier(str2);
            OIDInfo oIDInfo = new OIDInfo(str, objectIdentifier, cls);
            if (oidMap.put(objectIdentifier, oIDInfo) != null) {
                throw new CertificateException("Object identifier already exists: " + str2);
            }
            if (nameMap.put(str, oIDInfo) != null) {
                throw new CertificateException("Name already exists: " + str);
            }
        } catch (IOException e2) {
            throw new CertificateException("Invalid Object identifier: " + str2);
        }
    }

    public static String getName(ObjectIdentifier objectIdentifier) {
        OIDInfo oIDInfo = oidMap.get(objectIdentifier);
        if (oIDInfo == null) {
            return null;
        }
        return oIDInfo.name;
    }

    public static ObjectIdentifier getOID(String str) {
        OIDInfo oIDInfo = nameMap.get(str);
        if (oIDInfo == null) {
            return null;
        }
        return oIDInfo.oid;
    }

    public static Class<?> getClass(String str) throws CertificateException {
        OIDInfo oIDInfo = nameMap.get(str);
        if (oIDInfo == null) {
            return null;
        }
        return oIDInfo.getClazz();
    }

    public static Class<?> getClass(ObjectIdentifier objectIdentifier) throws CertificateException {
        OIDInfo oIDInfo = oidMap.get(objectIdentifier);
        if (oIDInfo == null) {
            return null;
        }
        return oIDInfo.getClazz();
    }
}
