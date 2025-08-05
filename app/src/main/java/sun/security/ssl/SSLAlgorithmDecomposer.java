package sun.security.ssl;

import java.util.HashSet;
import java.util.Set;
import sun.security.ssl.CipherSuite;
import sun.security.util.AlgorithmDecomposer;

/* loaded from: jsse.jar:sun/security/ssl/SSLAlgorithmDecomposer.class */
class SSLAlgorithmDecomposer extends AlgorithmDecomposer {
    private final boolean onlyX509;

    SSLAlgorithmDecomposer(boolean z2) {
        this.onlyX509 = z2;
    }

    SSLAlgorithmDecomposer() {
        this(false);
    }

    private Set<String> decomposes(CipherSuite.KeyExchange keyExchange) {
        HashSet hashSet = new HashSet();
        switch (keyExchange) {
            case K_NULL:
                if (!this.onlyX509) {
                    hashSet.add("K_NULL");
                    break;
                }
                break;
            case K_RSA:
                hashSet.add("RSA");
                break;
            case K_RSA_EXPORT:
                hashSet.add("RSA");
                hashSet.add("RSA_EXPORT");
                break;
            case K_DH_RSA:
                hashSet.add("RSA");
                hashSet.add("DH");
                hashSet.add("DiffieHellman");
                hashSet.add("DH_RSA");
                break;
            case K_DH_DSS:
                hashSet.add("DSA");
                hashSet.add("DSS");
                hashSet.add("DH");
                hashSet.add("DiffieHellman");
                hashSet.add("DH_DSS");
                break;
            case K_DHE_DSS:
                hashSet.add("DSA");
                hashSet.add("DSS");
                hashSet.add("DH");
                hashSet.add("DHE");
                hashSet.add("DiffieHellman");
                hashSet.add("DHE_DSS");
                break;
            case K_DHE_RSA:
                hashSet.add("RSA");
                hashSet.add("DH");
                hashSet.add("DHE");
                hashSet.add("DiffieHellman");
                hashSet.add("DHE_RSA");
                break;
            case K_DH_ANON:
                if (!this.onlyX509) {
                    hashSet.add("ANON");
                    hashSet.add("DH");
                    hashSet.add("DiffieHellman");
                    hashSet.add("DH_ANON");
                    break;
                }
                break;
            case K_ECDH_ECDSA:
                hashSet.add("ECDH");
                hashSet.add("ECDSA");
                hashSet.add("ECDH_ECDSA");
                break;
            case K_ECDH_RSA:
                hashSet.add("ECDH");
                hashSet.add("RSA");
                hashSet.add("ECDH_RSA");
                break;
            case K_ECDHE_ECDSA:
                hashSet.add("ECDHE");
                hashSet.add("ECDSA");
                hashSet.add("ECDHE_ECDSA");
                break;
            case K_ECDHE_RSA:
                hashSet.add("ECDHE");
                hashSet.add("RSA");
                hashSet.add("ECDHE_RSA");
                break;
            case K_ECDH_ANON:
                if (!this.onlyX509) {
                    hashSet.add("ECDH");
                    hashSet.add("ANON");
                    hashSet.add("ECDH_ANON");
                    break;
                }
                break;
        }
        return hashSet;
    }

    private Set<String> decomposes(SSLCipher sSLCipher) {
        HashSet hashSet = new HashSet();
        if (sSLCipher.transformation != null) {
            hashSet.addAll(super.decompose(sSLCipher.transformation));
        }
        switch (sSLCipher) {
            case B_NULL:
                hashSet.add("C_NULL");
                break;
            case B_RC2_40:
                hashSet.add("RC2_CBC_40");
                break;
            case B_RC4_40:
                hashSet.add("RC4_40");
                break;
            case B_RC4_128:
                hashSet.add("RC4_128");
                break;
            case B_DES_40:
                hashSet.add("DES40_CBC");
                hashSet.add("DES_CBC_40");
                break;
            case B_DES:
                hashSet.add("DES_CBC");
                break;
            case B_3DES:
                hashSet.add("3DES_EDE_CBC");
                break;
            case B_AES_128:
                hashSet.add("AES_128_CBC");
                break;
            case B_AES_256:
                hashSet.add("AES_256_CBC");
                break;
            case B_AES_128_GCM:
                hashSet.add("AES_128_GCM");
                break;
            case B_AES_256_GCM:
                hashSet.add("AES_256_GCM");
                break;
        }
        return hashSet;
    }

    private Set<String> decomposes(CipherSuite.MacAlg macAlg, SSLCipher sSLCipher) {
        HashSet hashSet = new HashSet();
        if (macAlg == CipherSuite.MacAlg.M_NULL && sSLCipher.cipherType != CipherType.AEAD_CIPHER) {
            hashSet.add("M_NULL");
        } else if (macAlg == CipherSuite.MacAlg.M_MD5) {
            hashSet.add("MD5");
            hashSet.add("HmacMD5");
        } else if (macAlg == CipherSuite.MacAlg.M_SHA) {
            hashSet.add("SHA1");
            hashSet.add("SHA-1");
            hashSet.add("HmacSHA1");
        } else if (macAlg == CipherSuite.MacAlg.M_SHA256) {
            hashSet.add("SHA256");
            hashSet.add("SHA-256");
            hashSet.add("HmacSHA256");
        } else if (macAlg == CipherSuite.MacAlg.M_SHA384) {
            hashSet.add("SHA384");
            hashSet.add("SHA-384");
            hashSet.add("HmacSHA384");
        }
        return hashSet;
    }

    private Set<String> decomposes(CipherSuite.HashAlg hashAlg) {
        HashSet hashSet = new HashSet();
        if (hashAlg == CipherSuite.HashAlg.H_SHA256) {
            hashSet.add("SHA256");
            hashSet.add("SHA-256");
            hashSet.add("HmacSHA256");
        } else if (hashAlg == CipherSuite.HashAlg.H_SHA384) {
            hashSet.add("SHA384");
            hashSet.add("SHA-384");
            hashSet.add("HmacSHA384");
        }
        return hashSet;
    }

    private Set<String> decompose(CipherSuite.KeyExchange keyExchange, SSLCipher sSLCipher, CipherSuite.MacAlg macAlg, CipherSuite.HashAlg hashAlg) {
        HashSet hashSet = new HashSet();
        if (keyExchange != null) {
            hashSet.addAll(decomposes(keyExchange));
        }
        if (this.onlyX509) {
            return hashSet;
        }
        if (sSLCipher != null) {
            hashSet.addAll(decomposes(sSLCipher));
        }
        if (macAlg != null) {
            hashSet.addAll(decomposes(macAlg, sSLCipher));
        }
        if (hashAlg != null) {
            hashSet.addAll(decomposes(hashAlg));
        }
        return hashSet;
    }

    @Override // sun.security.util.AlgorithmDecomposer
    public Set<String> decompose(String str) {
        if (str.startsWith("SSL_") || str.startsWith("TLS_")) {
            CipherSuite cipherSuiteNameOf = null;
            try {
                cipherSuiteNameOf = CipherSuite.nameOf(str);
            } catch (IllegalArgumentException e2) {
            }
            if (cipherSuiteNameOf != null && cipherSuiteNameOf != CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV) {
                return decompose(cipherSuiteNameOf.keyExchange, cipherSuiteNameOf.bulkCipher, cipherSuiteNameOf.macAlg, cipherSuiteNameOf.hashAlg);
            }
        }
        return super.decompose(str);
    }
}
