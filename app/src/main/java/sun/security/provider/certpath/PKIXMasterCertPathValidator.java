package sun.security.provider.certpath;

import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.security.util.Debug;

/* loaded from: rt.jar:sun/security/provider/certpath/PKIXMasterCertPathValidator.class */
class PKIXMasterCertPathValidator {
    private static final Debug debug = Debug.getInstance("certpath");

    PKIXMasterCertPathValidator() {
    }

    static void validate(CertPath certPath, List<X509Certificate> list, List<PKIXCertPathChecker> list2) throws CertPathValidatorException {
        int size = list.size();
        if (debug != null) {
            debug.println("--------------------------------------------------------------");
            debug.println("Executing PKIX certification path validation algorithm.");
        }
        for (int i2 = 0; i2 < size; i2++) {
            X509Certificate x509Certificate = list.get(i2);
            if (debug != null) {
                debug.println("Checking cert" + (i2 + 1) + " - Subject: " + ((Object) x509Certificate.getSubjectX500Principal()));
            }
            Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
            if (criticalExtensionOIDs == null) {
                criticalExtensionOIDs = Collections.emptySet();
            }
            if (debug != null && !criticalExtensionOIDs.isEmpty()) {
                StringJoiner stringJoiner = new StringJoiner(", ", VectorFormat.DEFAULT_PREFIX, "}");
                Iterator<String> it = criticalExtensionOIDs.iterator();
                while (it.hasNext()) {
                    stringJoiner.add(it.next());
                }
                debug.println("Set of critical extensions: " + stringJoiner.toString());
            }
            for (int i3 = 0; i3 < list2.size(); i3++) {
                PKIXCertPathChecker pKIXCertPathChecker = list2.get(i3);
                if (debug != null) {
                    debug.println("-Using checker" + (i3 + 1) + " ... [" + pKIXCertPathChecker.getClass().getName() + "]");
                }
                if (i2 == 0) {
                    pKIXCertPathChecker.init(false);
                }
                try {
                    pKIXCertPathChecker.check(x509Certificate, criticalExtensionOIDs);
                    if (debug != null) {
                        debug.println("-checker" + (i3 + 1) + " validation succeeded");
                    }
                } catch (CertPathValidatorException e2) {
                    throw new CertPathValidatorException(e2.getMessage(), e2.getCause() != null ? e2.getCause() : e2, certPath, size - (i2 + 1), e2.getReason());
                }
            }
            if (!criticalExtensionOIDs.isEmpty()) {
                throw new CertPathValidatorException("unrecognized critical extension(s)", null, certPath, size - (i2 + 1), PKIXReason.UNRECOGNIZED_CRIT_EXT);
            }
            if (debug != null) {
                debug.println("\ncert" + (i2 + 1) + " validation succeeded.\n");
            }
        }
        if (debug != null) {
            debug.println("Cert path validation succeeded. (PKIX validation algorithm)");
            debug.println("--------------------------------------------------------------");
        }
    }
}
