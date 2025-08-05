package sun.security.provider;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.Map;
import sun.security.action.GetPropertyAction;
import sun.security.provider.NativePRNG;

/* loaded from: rt.jar:sun/security/provider/SunEntries.class */
final class SunEntries {
    private static final String PROP_EGD = "java.security.egd";
    private static final String PROP_RNDSOURCE = "securerandom.source";
    static final String URL_DEV_RANDOM = "file:/dev/random";
    static final String URL_DEV_URANDOM = "file:/dev/urandom";
    private static final boolean useLegacyDSA = Boolean.parseBoolean(GetPropertyAction.privilegedGetProperty("jdk.security.legacyDSAKeyPairGenerator"));
    private static final String seedSource = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.provider.SunEntries.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public String run2() {
            String property = System.getProperty(SunEntries.PROP_EGD, "");
            if (property.length() != 0) {
                return property;
            }
            String property2 = Security.getProperty(SunEntries.PROP_RNDSOURCE);
            if (property2 == null) {
                return "";
            }
            return property2;
        }
    });

    private SunEntries() {
    }

    static void putEntries(Map<Object, Object> map) {
        boolean zIsAvailable = NativePRNG.isAvailable();
        boolean z2 = seedSource.equals(URL_DEV_URANDOM) || seedSource.equals(URL_DEV_RANDOM);
        if (zIsAvailable && z2) {
            map.put("SecureRandom.NativePRNG", "sun.security.provider.NativePRNG");
        }
        map.put("SecureRandom.SHA1PRNG", "sun.security.provider.SecureRandom");
        if (zIsAvailable && !z2) {
            map.put("SecureRandom.NativePRNG", "sun.security.provider.NativePRNG");
        }
        if (NativePRNG.Blocking.isAvailable()) {
            map.put("SecureRandom.NativePRNGBlocking", "sun.security.provider.NativePRNG$Blocking");
        }
        if (NativePRNG.NonBlocking.isAvailable()) {
            map.put("SecureRandom.NativePRNGNonBlocking", "sun.security.provider.NativePRNG$NonBlocking");
        }
        map.put("Signature.SHA1withDSA", "sun.security.provider.DSA$SHA1withDSA");
        map.put("Signature.NONEwithDSA", "sun.security.provider.DSA$RawDSA");
        map.put("Alg.Alias.Signature.RawDSA", "NONEwithDSA");
        map.put("Signature.SHA224withDSA", "sun.security.provider.DSA$SHA224withDSA");
        map.put("Signature.SHA256withDSA", "sun.security.provider.DSA$SHA256withDSA");
        map.put("Signature.SHA1withDSA SupportedKeyClasses", "java.security.interfaces.DSAPublicKey|java.security.interfaces.DSAPrivateKey");
        map.put("Signature.NONEwithDSA SupportedKeyClasses", "java.security.interfaces.DSAPublicKey|java.security.interfaces.DSAPrivateKey");
        map.put("Signature.SHA224withDSA SupportedKeyClasses", "java.security.interfaces.DSAPublicKey|java.security.interfaces.DSAPrivateKey");
        map.put("Signature.SHA256withDSA SupportedKeyClasses", "java.security.interfaces.DSAPublicKey|java.security.interfaces.DSAPrivateKey");
        map.put("Alg.Alias.Signature.DSA", "SHA1withDSA");
        map.put("Alg.Alias.Signature.DSS", "SHA1withDSA");
        map.put("Alg.Alias.Signature.SHA/DSA", "SHA1withDSA");
        map.put("Alg.Alias.Signature.SHA-1/DSA", "SHA1withDSA");
        map.put("Alg.Alias.Signature.SHA1/DSA", "SHA1withDSA");
        map.put("Alg.Alias.Signature.SHAwithDSA", "SHA1withDSA");
        map.put("Alg.Alias.Signature.DSAWithSHA1", "SHA1withDSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.10040.4.3", "SHA1withDSA");
        map.put("Alg.Alias.Signature.1.2.840.10040.4.3", "SHA1withDSA");
        map.put("Alg.Alias.Signature.1.3.14.3.2.13", "SHA1withDSA");
        map.put("Alg.Alias.Signature.1.3.14.3.2.27", "SHA1withDSA");
        map.put("Alg.Alias.Signature.OID.2.16.840.1.101.3.4.3.1", "SHA224withDSA");
        map.put("Alg.Alias.Signature.2.16.840.1.101.3.4.3.1", "SHA224withDSA");
        map.put("Alg.Alias.Signature.OID.2.16.840.1.101.3.4.3.2", "SHA256withDSA");
        map.put("Alg.Alias.Signature.2.16.840.1.101.3.4.3.2", "SHA256withDSA");
        map.put("KeyPairGenerator.DSA", "sun.security.provider.DSAKeyPairGenerator$" + (useLegacyDSA ? "Legacy" : "Current"));
        map.put("Alg.Alias.KeyPairGenerator.OID.1.2.840.10040.4.1", "DSA");
        map.put("Alg.Alias.KeyPairGenerator.1.2.840.10040.4.1", "DSA");
        map.put("Alg.Alias.KeyPairGenerator.1.3.14.3.2.12", "DSA");
        map.put("MessageDigest.MD2", "sun.security.provider.MD2");
        map.put("MessageDigest.MD5", "sun.security.provider.MD5");
        map.put("MessageDigest.SHA", "sun.security.provider.SHA");
        map.put("Alg.Alias.MessageDigest.SHA-1", "SHA");
        map.put("Alg.Alias.MessageDigest.SHA1", "SHA");
        map.put("Alg.Alias.MessageDigest.1.3.14.3.2.26", "SHA");
        map.put("Alg.Alias.MessageDigest.OID.1.3.14.3.2.26", "SHA");
        map.put("MessageDigest.SHA-224", "sun.security.provider.SHA2$SHA224");
        map.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.4", "SHA-224");
        map.put("Alg.Alias.MessageDigest.OID.2.16.840.1.101.3.4.2.4", "SHA-224");
        map.put("MessageDigest.SHA-256", "sun.security.provider.SHA2$SHA256");
        map.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.1", "SHA-256");
        map.put("Alg.Alias.MessageDigest.OID.2.16.840.1.101.3.4.2.1", "SHA-256");
        map.put("MessageDigest.SHA-384", "sun.security.provider.SHA5$SHA384");
        map.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.2", "SHA-384");
        map.put("Alg.Alias.MessageDigest.OID.2.16.840.1.101.3.4.2.2", "SHA-384");
        map.put("MessageDigest.SHA-512", "sun.security.provider.SHA5$SHA512");
        map.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.3", "SHA-512");
        map.put("Alg.Alias.MessageDigest.OID.2.16.840.1.101.3.4.2.3", "SHA-512");
        map.put("MessageDigest.SHA-512/224", "sun.security.provider.SHA5$SHA512_224");
        map.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.5", "SHA-512/224");
        map.put("Alg.Alias.MessageDigest.OID.2.16.840.1.101.3.4.2.5", "SHA-512/224");
        map.put("MessageDigest.SHA-512/256", "sun.security.provider.SHA5$SHA512_256");
        map.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.6", "SHA-512/256");
        map.put("Alg.Alias.MessageDigest.OID.2.16.840.1.101.3.4.2.6", "SHA-512/256");
        map.put("AlgorithmParameterGenerator.DSA", "sun.security.provider.DSAParameterGenerator");
        map.put("AlgorithmParameters.DSA", "sun.security.provider.DSAParameters");
        map.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.10040.4.1", "DSA");
        map.put("Alg.Alias.AlgorithmParameters.1.2.840.10040.4.1", "DSA");
        map.put("Alg.Alias.AlgorithmParameters.1.3.14.3.2.12", "DSA");
        map.put("KeyFactory.DSA", "sun.security.provider.DSAKeyFactory");
        map.put("Alg.Alias.KeyFactory.OID.1.2.840.10040.4.1", "DSA");
        map.put("Alg.Alias.KeyFactory.1.2.840.10040.4.1", "DSA");
        map.put("Alg.Alias.KeyFactory.1.3.14.3.2.12", "DSA");
        map.put("CertificateFactory.X.509", "sun.security.provider.X509Factory");
        map.put("Alg.Alias.CertificateFactory.X509", XMLX509Certificate.JCA_CERT_ID);
        map.put("KeyStore.JKS", "sun.security.provider.JavaKeyStore$DualFormatJKS");
        map.put("KeyStore.CaseExactJKS", "sun.security.provider.JavaKeyStore$CaseExactJKS");
        map.put("KeyStore.DKS", "sun.security.provider.DomainKeyStore$DKS");
        map.put("Policy.JavaPolicy", "sun.security.provider.PolicySpiFile");
        map.put("Configuration.JavaLoginConfig", "sun.security.provider.ConfigFile$Spi");
        map.put("CertPathBuilder.PKIX", "sun.security.provider.certpath.SunCertPathBuilder");
        map.put("CertPathBuilder.PKIX ValidationAlgorithm", "RFC5280");
        map.put("CertPathValidator.PKIX", "sun.security.provider.certpath.PKIXCertPathValidator");
        map.put("CertPathValidator.PKIX ValidationAlgorithm", "RFC5280");
        map.put("CertStore.LDAP", "sun.security.provider.certpath.ldap.LDAPCertStore");
        map.put("CertStore.LDAP LDAPSchema", "RFC2587");
        map.put("CertStore.Collection", "sun.security.provider.certpath.CollectionCertStore");
        map.put("CertStore.com.sun.security.IndexedCollection", "sun.security.provider.certpath.IndexedCollectionCertStore");
        map.put("Signature.NONEwithDSA KeySize", "1024");
        map.put("Signature.SHA1withDSA KeySize", "1024");
        map.put("Signature.SHA224withDSA KeySize", "2048");
        map.put("Signature.SHA256withDSA KeySize", "2048");
        map.put("KeyPairGenerator.DSA KeySize", "2048");
        map.put("AlgorithmParameterGenerator.DSA KeySize", "2048");
        map.put("Signature.SHA1withDSA ImplementedIn", "Software");
        map.put("KeyPairGenerator.DSA ImplementedIn", "Software");
        map.put("MessageDigest.MD5 ImplementedIn", "Software");
        map.put("MessageDigest.SHA ImplementedIn", "Software");
        map.put("AlgorithmParameterGenerator.DSA ImplementedIn", "Software");
        map.put("AlgorithmParameters.DSA ImplementedIn", "Software");
        map.put("KeyFactory.DSA ImplementedIn", "Software");
        map.put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        map.put("CertificateFactory.X.509 ImplementedIn", "Software");
        map.put("KeyStore.JKS ImplementedIn", "Software");
        map.put("CertPathValidator.PKIX ImplementedIn", "Software");
        map.put("CertPathBuilder.PKIX ImplementedIn", "Software");
        map.put("CertStore.LDAP ImplementedIn", "Software");
        map.put("CertStore.Collection ImplementedIn", "Software");
        map.put("CertStore.com.sun.security.IndexedCollection ImplementedIn", "Software");
    }

    static String getSeedSource() {
        return seedSource;
    }

    static File getDeviceFile(URL url) throws IOException {
        try {
            URI uri = url.toURI();
            if (uri.isOpaque()) {
                return new File(URI.create(new File(System.getProperty("user.dir")).toURI().toString() + uri.toString().substring(5)));
            }
            return new File(uri);
        } catch (URISyntaxException e2) {
            return new File(url.getPath());
        }
    }
}
