package sun.security.validator;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.security.AccessController;
import java.security.AlgorithmConstraints;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Timestamp;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathValidator;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.action.GetBooleanAction;
import sun.security.provider.certpath.AlgorithmChecker;
import sun.security.provider.certpath.PKIXExtendedParameters;
import sun.security.util.SecurityProperties;

/* loaded from: rt.jar:sun/security/validator/PKIXValidator.class */
public final class PKIXValidator extends Validator {
    private static final boolean TRY_VALIDATOR = true;
    private final Set<X509Certificate> trustedCerts;
    private final PKIXBuilderParameters parameterTemplate;
    private int certPathLength;
    private final Map<X500Principal, List<PublicKey>> trustedSubjects;
    private final CertificateFactory factory;
    private final boolean plugin;
    private static final boolean checkTLSRevocation = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("com.sun.net.ssl.checkRevocation"))).booleanValue();
    private static final boolean ALLOW_NON_CA_ANCHOR = allowNonCaAnchor();

    private static boolean allowNonCaAnchor() {
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("jdk.security.allowNonCaAnchor");
        return strPrivilegedGetOverridable != null && (strPrivilegedGetOverridable.isEmpty() || strPrivilegedGetOverridable.equalsIgnoreCase("true"));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v38, types: [java.util.List] */
    PKIXValidator(String str, Collection<X509Certificate> collection) {
        ArrayList arrayList;
        super(Validator.TYPE_PKIX, str);
        this.certPathLength = -1;
        if (collection instanceof Set) {
            this.trustedCerts = (Set) collection;
        } else {
            this.trustedCerts = new HashSet(collection);
        }
        HashSet hashSet = new HashSet();
        Iterator<X509Certificate> it = collection.iterator();
        while (it.hasNext()) {
            hashSet.add(new TrustAnchor(it.next(), null));
        }
        try {
            this.parameterTemplate = new PKIXBuilderParameters(hashSet, (CertSelector) null);
            setDefaultParameters(str);
            this.trustedSubjects = new HashMap();
            for (X509Certificate x509Certificate : collection) {
                X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
                if (this.trustedSubjects.containsKey(subjectX500Principal)) {
                    arrayList = (List) this.trustedSubjects.get(subjectX500Principal);
                } else {
                    arrayList = new ArrayList();
                    this.trustedSubjects.put(subjectX500Principal, arrayList);
                }
                arrayList.add(x509Certificate.getPublicKey());
            }
            try {
                this.factory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
                this.plugin = str.equals(Validator.VAR_PLUGIN_CODE_SIGNING);
            } catch (CertificateException e2) {
                throw new RuntimeException("Internal error", e2);
            }
        } catch (InvalidAlgorithmParameterException e3) {
            throw new RuntimeException("Unexpected error: " + e3.toString(), e3);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v35, types: [java.util.List] */
    PKIXValidator(String str, PKIXBuilderParameters pKIXBuilderParameters) {
        ArrayList arrayList;
        super(Validator.TYPE_PKIX, str);
        this.certPathLength = -1;
        this.trustedCerts = new HashSet();
        Iterator<TrustAnchor> it = pKIXBuilderParameters.getTrustAnchors().iterator();
        while (it.hasNext()) {
            X509Certificate trustedCert = it.next().getTrustedCert();
            if (trustedCert != null) {
                this.trustedCerts.add(trustedCert);
            }
        }
        this.parameterTemplate = pKIXBuilderParameters;
        this.trustedSubjects = new HashMap();
        for (X509Certificate x509Certificate : this.trustedCerts) {
            X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
            if (this.trustedSubjects.containsKey(subjectX500Principal)) {
                arrayList = (List) this.trustedSubjects.get(subjectX500Principal);
            } else {
                arrayList = new ArrayList();
                this.trustedSubjects.put(subjectX500Principal, arrayList);
            }
            arrayList.add(x509Certificate.getPublicKey());
        }
        try {
            this.factory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
            this.plugin = str.equals(Validator.VAR_PLUGIN_CODE_SIGNING);
        } catch (CertificateException e2) {
            throw new RuntimeException("Internal error", e2);
        }
    }

    @Override // sun.security.validator.Validator
    public Collection<X509Certificate> getTrustedCertificates() {
        return this.trustedCerts;
    }

    public int getCertPathLength() {
        return this.certPathLength;
    }

    private void setDefaultParameters(String str) {
        if (str == Validator.VAR_TLS_SERVER || str == Validator.VAR_TLS_CLIENT) {
            this.parameterTemplate.setRevocationEnabled(checkTLSRevocation);
        } else {
            this.parameterTemplate.setRevocationEnabled(false);
        }
    }

    public PKIXBuilderParameters getParameters() {
        return this.parameterTemplate;
    }

    @Override // sun.security.validator.Validator
    X509Certificate[] engineValidate(X509Certificate[] x509CertificateArr, Collection<X509Certificate> collection, List<byte[]> list, AlgorithmConstraints algorithmConstraints, Object obj) throws CertificateException {
        if (x509CertificateArr == null || x509CertificateArr.length == 0) {
            throw new CertificateException("null or zero-length certificate chain");
        }
        PKIXExtendedParameters pKIXExtendedParameters = null;
        try {
            pKIXExtendedParameters = new PKIXExtendedParameters((PKIXBuilderParameters) this.parameterTemplate.clone(), obj instanceof Timestamp ? (Timestamp) obj : null, this.variant);
        } catch (InvalidAlgorithmParameterException e2) {
        }
        if (algorithmConstraints != null) {
            pKIXExtendedParameters.addCertPathChecker(new AlgorithmChecker(algorithmConstraints, this.variant));
        }
        if (!list.isEmpty()) {
            addResponses(pKIXExtendedParameters, x509CertificateArr, list);
        }
        X500Principal issuerX500Principal = null;
        for (int i2 = 0; i2 < x509CertificateArr.length; i2++) {
            X509Certificate x509Certificate = x509CertificateArr[i2];
            X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
            if (i2 == 0) {
                if (this.trustedCerts.contains(x509Certificate)) {
                    return new X509Certificate[]{x509CertificateArr[0]};
                }
            } else {
                if (!subjectX500Principal.equals(issuerX500Principal)) {
                    return doBuild(x509CertificateArr, collection, pKIXExtendedParameters);
                }
                if (this.trustedCerts.contains(x509Certificate) || (this.trustedSubjects.containsKey(subjectX500Principal) && this.trustedSubjects.get(subjectX500Principal).contains(x509Certificate.getPublicKey()))) {
                    X509Certificate[] x509CertificateArr2 = new X509Certificate[i2];
                    System.arraycopy(x509CertificateArr, 0, x509CertificateArr2, 0, i2);
                    return doValidate(x509CertificateArr2, pKIXExtendedParameters);
                }
            }
            issuerX500Principal = x509Certificate.getIssuerX500Principal();
        }
        X509Certificate x509Certificate2 = x509CertificateArr[x509CertificateArr.length - 1];
        X500Principal issuerX500Principal2 = x509Certificate2.getIssuerX500Principal();
        x509Certificate2.getSubjectX500Principal();
        if (this.trustedSubjects.containsKey(issuerX500Principal2) && isSignatureValid(this.trustedSubjects.get(issuerX500Principal2), x509Certificate2)) {
            return doValidate(x509CertificateArr, pKIXExtendedParameters);
        }
        if (this.plugin) {
            if (x509CertificateArr.length > 1) {
                X509Certificate[] x509CertificateArr3 = new X509Certificate[x509CertificateArr.length - 1];
                System.arraycopy(x509CertificateArr, 0, x509CertificateArr3, 0, x509CertificateArr3.length);
                try {
                    pKIXExtendedParameters.setTrustAnchors(Collections.singleton(new TrustAnchor(x509CertificateArr[x509CertificateArr.length - 1], null)));
                    doValidate(x509CertificateArr3, pKIXExtendedParameters);
                } catch (InvalidAlgorithmParameterException e3) {
                    throw new CertificateException(e3);
                }
            }
            throw new ValidatorException(ValidatorException.T_NO_TRUST_ANCHOR);
        }
        return doBuild(x509CertificateArr, collection, pKIXExtendedParameters);
    }

    private boolean isSignatureValid(List<PublicKey> list, X509Certificate x509Certificate) {
        if (this.plugin) {
            Iterator<PublicKey> it = list.iterator();
            while (it.hasNext()) {
                try {
                    x509Certificate.verify(it.next());
                    return true;
                } catch (Exception e2) {
                }
            }
            return false;
        }
        return true;
    }

    private static X509Certificate[] toArray(CertPath certPath, TrustAnchor trustAnchor) throws CertificateException {
        X509Certificate trustedCert = trustAnchor.getTrustedCert();
        if (trustedCert == null) {
            throw new ValidatorException("TrustAnchor must be specified as certificate");
        }
        verifyTrustAnchor(trustedCert);
        List<? extends Certificate> certificates = certPath.getCertificates();
        X509Certificate[] x509CertificateArr = new X509Certificate[certificates.size() + 1];
        certificates.toArray(x509CertificateArr);
        x509CertificateArr[x509CertificateArr.length - 1] = trustedCert;
        return x509CertificateArr;
    }

    private void setDate(PKIXBuilderParameters pKIXBuilderParameters) {
        Date date = this.validationDate;
        if (date != null) {
            pKIXBuilderParameters.setDate(date);
        }
    }

    private X509Certificate[] doValidate(X509Certificate[] x509CertificateArr, PKIXBuilderParameters pKIXBuilderParameters) throws CertificateException {
        try {
            setDate(pKIXBuilderParameters);
            CertPathValidator certPathValidator = CertPathValidator.getInstance(Validator.TYPE_PKIX);
            CertPath certPathGenerateCertPath = this.factory.generateCertPath(Arrays.asList(x509CertificateArr));
            this.certPathLength = x509CertificateArr.length;
            return toArray(certPathGenerateCertPath, ((PKIXCertPathValidatorResult) certPathValidator.validate(certPathGenerateCertPath, pKIXBuilderParameters)).getTrustAnchor());
        } catch (GeneralSecurityException e2) {
            throw new ValidatorException("PKIX path validation failed: " + e2.toString(), e2);
        }
    }

    private static void verifyTrustAnchor(X509Certificate x509Certificate) throws ValidatorException {
        if (ALLOW_NON_CA_ANCHOR || x509Certificate.getVersion() < 3) {
            return;
        }
        if (x509Certificate.getBasicConstraints() == -1) {
            throw new ValidatorException("TrustAnchor with subject \"" + ((Object) x509Certificate.getSubjectX500Principal()) + "\" is not a CA certificate");
        }
        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage != null && !keyUsage[5]) {
            throw new ValidatorException("TrustAnchor with subject \"" + ((Object) x509Certificate.getSubjectX500Principal()) + "\" does not have keyCertSign bit set in KeyUsage extension");
        }
    }

    private X509Certificate[] doBuild(X509Certificate[] x509CertificateArr, Collection<X509Certificate> collection, PKIXBuilderParameters pKIXBuilderParameters) throws CertificateException {
        try {
            setDate(pKIXBuilderParameters);
            X509CertSelector x509CertSelector = new X509CertSelector();
            x509CertSelector.setCertificate(x509CertificateArr[0]);
            pKIXBuilderParameters.setTargetCertConstraints(x509CertSelector);
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(x509CertificateArr));
            if (collection != null) {
                arrayList.addAll(collection);
            }
            pKIXBuilderParameters.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters(arrayList)));
            PKIXCertPathBuilderResult pKIXCertPathBuilderResult = (PKIXCertPathBuilderResult) CertPathBuilder.getInstance(Validator.TYPE_PKIX).build(pKIXBuilderParameters);
            return toArray(pKIXCertPathBuilderResult.getCertPath(), pKIXCertPathBuilderResult.getTrustAnchor());
        } catch (GeneralSecurityException e2) {
            throw new ValidatorException("PKIX path building failed: " + e2.toString(), e2);
        }
    }

    private static void addResponses(PKIXBuilderParameters pKIXBuilderParameters, X509Certificate[] x509CertificateArr, List<byte[]> list) {
        if (pKIXBuilderParameters.isRevocationEnabled()) {
            try {
                PKIXRevocationChecker pKIXRevocationChecker = null;
                ArrayList arrayList = new ArrayList(pKIXBuilderParameters.getCertPathCheckers());
                Iterator<PKIXCertPathChecker> it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    PKIXCertPathChecker next = it.next();
                    if (next instanceof PKIXRevocationChecker) {
                        pKIXRevocationChecker = (PKIXRevocationChecker) next;
                        break;
                    }
                }
                if (pKIXRevocationChecker == null) {
                    pKIXRevocationChecker = (PKIXRevocationChecker) CertPathValidator.getInstance(Validator.TYPE_PKIX).getRevocationChecker();
                    arrayList.add(pKIXRevocationChecker);
                }
                Map<X509Certificate, byte[]> ocspResponses = pKIXRevocationChecker.getOcspResponses();
                int iMin = Integer.min(x509CertificateArr.length, list.size());
                for (int i2 = 0; i2 < iMin; i2++) {
                    byte[] bArr = list.get(i2);
                    if (bArr != null && bArr.length > 0 && !ocspResponses.containsKey(x509CertificateArr[i2])) {
                        ocspResponses.put(x509CertificateArr[i2], bArr);
                    }
                }
                pKIXRevocationChecker.setOcspResponses(ocspResponses);
                pKIXBuilderParameters.setCertPathCheckers(arrayList);
            } catch (NoSuchAlgorithmException e2) {
            }
        }
    }
}
