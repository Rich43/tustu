package sun.security.ec;

import java.util.Map;
import java.util.regex.Pattern;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.util.CurveDB;
import sun.security.util.NamedCurve;

/* loaded from: sunec.jar:sun/security/ec/SunECEntries.class */
final class SunECEntries {
    private SunECEntries() {
    }

    static void putEntries(Map<Object, Object> map, boolean z2) {
        map.put("KeyFactory.EC", "sun.security.ec.ECKeyFactory");
        map.put("Alg.Alias.KeyFactory.EllipticCurve", "EC");
        map.put("KeyFactory.EC ImplementedIn", "Software");
        map.put("AlgorithmParameters.EC", "sun.security.util.ECParameters");
        map.put("Alg.Alias.AlgorithmParameters.EllipticCurve", "EC");
        map.put("Alg.Alias.AlgorithmParameters.1.2.840.10045.2.1", "EC");
        map.put("AlgorithmParameters.EC KeySize", "256");
        map.put("AlgorithmParameters.EC ImplementedIn", "Software");
        boolean z3 = true;
        StringBuilder sb = new StringBuilder();
        Pattern patternCompile = Pattern.compile(CurveDB.SPLIT_PATTERN);
        for (NamedCurve namedCurve : CurveDB.getSupportedCurves()) {
            if (!z3) {
                sb.append(CallSiteDescriptor.OPERATOR_DELIMITER);
            } else {
                z3 = false;
            }
            sb.append("[");
            for (String str : patternCompile.split(namedCurve.getName())) {
                sb.append(str.trim());
                sb.append(",");
            }
            sb.append(namedCurve.getObjectId());
            sb.append("]");
        }
        map.put("AlgorithmParameters.EC SupportedCurves", sb.toString());
        if (!z2) {
            return;
        }
        map.put("Signature.NONEwithECDSA", "sun.security.ec.ECDSASignature$Raw");
        map.put("Signature.SHA1withECDSA", "sun.security.ec.ECDSASignature$SHA1");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.1", "SHA1withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.1", "SHA1withECDSA");
        map.put("Signature.SHA224withECDSA", "sun.security.ec.ECDSASignature$SHA224");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.1", "SHA224withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.3.1", "SHA224withECDSA");
        map.put("Signature.SHA256withECDSA", "sun.security.ec.ECDSASignature$SHA256");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.2", "SHA256withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.3.2", "SHA256withECDSA");
        map.put("Signature.SHA384withECDSA", "sun.security.ec.ECDSASignature$SHA384");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.3", "SHA384withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.3.3", "SHA384withECDSA");
        map.put("Signature.SHA512withECDSA", "sun.security.ec.ECDSASignature$SHA512");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.4", "SHA512withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.3.4", "SHA512withECDSA");
        map.put("Signature.NONEwithECDSA SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
        map.put("Signature.SHA1withECDSA SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
        map.put("Signature.SHA224withECDSA SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
        map.put("Signature.SHA256withECDSA SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
        map.put("Signature.SHA384withECDSA SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
        map.put("Signature.SHA512withECDSA SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
        map.put("Signature.SHA1withECDSA KeySize", "256");
        map.put("Signature.NONEwithECDSA ImplementedIn", "Software");
        map.put("Signature.SHA1withECDSA ImplementedIn", "Software");
        map.put("Signature.SHA224withECDSA ImplementedIn", "Software");
        map.put("Signature.SHA256withECDSA ImplementedIn", "Software");
        map.put("Signature.SHA384withECDSA ImplementedIn", "Software");
        map.put("Signature.SHA512withECDSA ImplementedIn", "Software");
        map.put("KeyPairGenerator.EC", "sun.security.ec.ECKeyPairGenerator");
        map.put("Alg.Alias.KeyPairGenerator.EllipticCurve", "EC");
        map.put("KeyPairGenerator.EC KeySize", "256");
        map.put("KeyPairGenerator.EC ImplementedIn", "Software");
        map.put("KeyAgreement.ECDH", "sun.security.ec.ECDHKeyAgreement");
        map.put("KeyAgreement.ECDH SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
        map.put("KeyAgreement.ECDH ImplementedIn", "Software");
    }
}
