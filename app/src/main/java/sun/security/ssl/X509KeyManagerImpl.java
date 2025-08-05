package sun.security.ssl;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.Socket;
import java.security.AlgorithmConstraints;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.net.ssl.ExtendedSSLSession;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;
import sun.security.provider.certpath.AlgorithmChecker;
import sun.security.validator.Validator;

/* loaded from: jsse.jar:sun/security/ssl/X509KeyManagerImpl.class */
final class X509KeyManagerImpl extends X509ExtendedKeyManager implements X509KeyManager {
    private static Date verificationDate;
    private final List<KeyStore.Builder> builders;
    private final AtomicLong uidCounter;
    private final Map<String, Reference<KeyStore.PrivateKeyEntry>> entryCacheMap;

    /* loaded from: jsse.jar:sun/security/ssl/X509KeyManagerImpl$CheckResult.class */
    private enum CheckResult {
        OK,
        INSENSITIVE,
        EXPIRED,
        EXTENSION_MISMATCH
    }

    X509KeyManagerImpl(KeyStore.Builder builder) {
        this((List<KeyStore.Builder>) Collections.singletonList(builder));
    }

    X509KeyManagerImpl(List<KeyStore.Builder> list) {
        this.builders = list;
        this.uidCounter = new AtomicLong();
        this.entryCacheMap = Collections.synchronizedMap(new SizedMap());
    }

    /* loaded from: jsse.jar:sun/security/ssl/X509KeyManagerImpl$SizedMap.class */
    private static class SizedMap<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = -8211222668790986062L;

        private SizedMap() {
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
            return size() > 10;
        }
    }

    @Override // javax.net.ssl.X509KeyManager
    public X509Certificate[] getCertificateChain(String str) {
        KeyStore.PrivateKeyEntry entry = getEntry(str);
        if (entry == null) {
            return null;
        }
        return (X509Certificate[]) entry.getCertificateChain();
    }

    @Override // javax.net.ssl.X509KeyManager
    public PrivateKey getPrivateKey(String str) {
        KeyStore.PrivateKeyEntry entry = getEntry(str);
        if (entry == null) {
            return null;
        }
        return entry.getPrivateKey();
    }

    @Override // javax.net.ssl.X509KeyManager
    public String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket) {
        return chooseAlias(getKeyTypes(strArr), principalArr, CheckType.CLIENT, getAlgorithmConstraints(socket));
    }

    @Override // javax.net.ssl.X509ExtendedKeyManager
    public String chooseEngineClientAlias(String[] strArr, Principal[] principalArr, SSLEngine sSLEngine) {
        return chooseAlias(getKeyTypes(strArr), principalArr, CheckType.CLIENT, getAlgorithmConstraints(sSLEngine));
    }

    @Override // javax.net.ssl.X509KeyManager
    public String chooseServerAlias(String str, Principal[] principalArr, Socket socket) {
        return chooseAlias(getKeyTypes(str), principalArr, CheckType.SERVER, getAlgorithmConstraints(socket), X509TrustManagerImpl.getRequestedServerNames(socket), "HTTPS");
    }

    @Override // javax.net.ssl.X509ExtendedKeyManager
    public String chooseEngineServerAlias(String str, Principal[] principalArr, SSLEngine sSLEngine) {
        return chooseAlias(getKeyTypes(str), principalArr, CheckType.SERVER, getAlgorithmConstraints(sSLEngine), X509TrustManagerImpl.getRequestedServerNames(sSLEngine), "HTTPS");
    }

    @Override // javax.net.ssl.X509KeyManager
    public String[] getClientAliases(String str, Principal[] principalArr) {
        return getAliases(str, principalArr, CheckType.CLIENT, null);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String[] getServerAliases(String str, Principal[] principalArr) {
        return getAliases(str, principalArr, CheckType.SERVER, null);
    }

    private AlgorithmConstraints getAlgorithmConstraints(Socket socket) {
        if (socket != null && socket.isConnected() && (socket instanceof SSLSocket)) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            SSLSession handshakeSession = sSLSocket.getHandshakeSession();
            if (handshakeSession != null && ProtocolVersion.useTLS12PlusSpec(handshakeSession.getProtocol())) {
                String[] peerSupportedSignatureAlgorithms = null;
                if (handshakeSession instanceof ExtendedSSLSession) {
                    peerSupportedSignatureAlgorithms = ((ExtendedSSLSession) handshakeSession).getPeerSupportedSignatureAlgorithms();
                }
                return new SSLAlgorithmConstraints(sSLSocket, peerSupportedSignatureAlgorithms, true);
            }
            return new SSLAlgorithmConstraints(sSLSocket, true);
        }
        return new SSLAlgorithmConstraints((SSLSocket) null, true);
    }

    private AlgorithmConstraints getAlgorithmConstraints(SSLEngine sSLEngine) {
        SSLSession handshakeSession;
        if (sSLEngine != null && (handshakeSession = sSLEngine.getHandshakeSession()) != null && ProtocolVersion.useTLS12PlusSpec(handshakeSession.getProtocol())) {
            String[] peerSupportedSignatureAlgorithms = null;
            if (handshakeSession instanceof ExtendedSSLSession) {
                peerSupportedSignatureAlgorithms = ((ExtendedSSLSession) handshakeSession).getPeerSupportedSignatureAlgorithms();
            }
            return new SSLAlgorithmConstraints(sSLEngine, peerSupportedSignatureAlgorithms, true);
        }
        return new SSLAlgorithmConstraints(sSLEngine, true);
    }

    private String makeAlias(EntryStatus entryStatus) {
        return this.uidCounter.incrementAndGet() + "." + entryStatus.builderIndex + "." + entryStatus.alias;
    }

    private KeyStore.PrivateKeyEntry getEntry(String str) {
        if (str == null) {
            return null;
        }
        Reference<KeyStore.PrivateKeyEntry> reference = this.entryCacheMap.get(str);
        KeyStore.PrivateKeyEntry privateKeyEntry = reference != null ? reference.get() : null;
        if (privateKeyEntry != null) {
            return privateKeyEntry;
        }
        int iIndexOf = str.indexOf(46);
        int iIndexOf2 = str.indexOf(46, iIndexOf + 1);
        if (iIndexOf == -1 || iIndexOf2 == iIndexOf) {
            return null;
        }
        try {
            int i2 = Integer.parseInt(str.substring(iIndexOf + 1, iIndexOf2));
            String strSubstring = str.substring(iIndexOf2 + 1);
            KeyStore.Builder builder = this.builders.get(i2);
            KeyStore.Entry entry = builder.getKeyStore().getEntry(strSubstring, builder.getProtectionParameter(str));
            if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                return null;
            }
            KeyStore.PrivateKeyEntry privateKeyEntry2 = (KeyStore.PrivateKeyEntry) entry;
            this.entryCacheMap.put(str, new SoftReference(privateKeyEntry2));
            return privateKeyEntry2;
        } catch (Exception e2) {
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/X509KeyManagerImpl$KeyType.class */
    private static class KeyType {
        final String keyAlgorithm;
        final String sigKeyAlgorithm;

        KeyType(String str) {
            int iIndexOf = str.indexOf(95);
            if (iIndexOf == -1) {
                this.keyAlgorithm = str;
                this.sigKeyAlgorithm = null;
            } else {
                this.keyAlgorithm = str.substring(0, iIndexOf);
                this.sigKeyAlgorithm = str.substring(iIndexOf + 1);
            }
        }

        boolean matches(Certificate[] certificateArr) {
            if (!certificateArr[0].getPublicKey().getAlgorithm().equals(this.keyAlgorithm)) {
                return false;
            }
            if (this.sigKeyAlgorithm == null) {
                return true;
            }
            if (certificateArr.length > 1) {
                return this.sigKeyAlgorithm.equals(certificateArr[1].getPublicKey().getAlgorithm());
            }
            return ((X509Certificate) certificateArr[0]).getSigAlgName().toUpperCase(Locale.ENGLISH).contains("WITH" + this.sigKeyAlgorithm.toUpperCase(Locale.ENGLISH));
        }
    }

    private static List<KeyType> getKeyTypes(String... strArr) {
        if (strArr == null || strArr.length == 0 || strArr[0] == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            arrayList.add(new KeyType(str));
        }
        return arrayList;
    }

    private String chooseAlias(List<KeyType> list, Principal[] principalArr, CheckType checkType, AlgorithmConstraints algorithmConstraints) {
        return chooseAlias(list, principalArr, checkType, algorithmConstraints, null, null);
    }

    private String chooseAlias(List<KeyType> list, Principal[] principalArr, CheckType checkType, AlgorithmConstraints algorithmConstraints, List<SNIServerName> list2, String str) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Set<Principal> issuerSet = getIssuerSet(principalArr);
        ArrayList arrayList = null;
        int size = this.builders.size();
        for (int i2 = 0; i2 < size; i2++) {
            try {
                List<EntryStatus> aliases = getAliases(i2, list, issuerSet, false, checkType, algorithmConstraints, list2, str);
                if (aliases != null) {
                    EntryStatus entryStatus = aliases.get(0);
                    if (entryStatus.checkResult == CheckResult.OK) {
                        if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                            SSLLogger.fine("KeyMgr: choosing key: " + ((Object) entryStatus), new Object[0]);
                        }
                        return makeAlias(entryStatus);
                    }
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.addAll(aliases);
                }
            } catch (Exception e2) {
            }
        }
        if (arrayList == null) {
            if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                SSLLogger.fine("KeyMgr: no matching key found", new Object[0]);
                return null;
            }
            return null;
        }
        Collections.sort(arrayList);
        if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
            SSLLogger.fine("KeyMgr: no good matching key found, returning best match out of", arrayList);
        }
        return makeAlias((EntryStatus) arrayList.get(0));
    }

    public String[] getAliases(String str, Principal[] principalArr, CheckType checkType, AlgorithmConstraints algorithmConstraints) {
        if (str == null) {
            return null;
        }
        Set<Principal> issuerSet = getIssuerSet(principalArr);
        List<KeyType> keyTypes = getKeyTypes(str);
        ArrayList arrayList = null;
        int size = this.builders.size();
        for (int i2 = 0; i2 < size; i2++) {
            try {
                List<EntryStatus> aliases = getAliases(i2, keyTypes, issuerSet, true, checkType, algorithmConstraints, null, null);
                if (aliases != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.addAll(aliases);
                }
            } catch (Exception e2) {
            }
        }
        if (arrayList == null || arrayList.isEmpty()) {
            if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                SSLLogger.fine("KeyMgr: no matching alias found", new Object[0]);
                return null;
            }
            return null;
        }
        Collections.sort(arrayList);
        if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
            SSLLogger.fine("KeyMgr: getting aliases", arrayList);
        }
        return toAliases(arrayList);
    }

    private String[] toAliases(List<EntryStatus> list) {
        String[] strArr = new String[list.size()];
        int i2 = 0;
        Iterator<EntryStatus> it = list.iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            strArr[i3] = makeAlias(it.next());
        }
        return strArr;
    }

    private Set<Principal> getIssuerSet(Principal[] principalArr) {
        if (principalArr != null && principalArr.length != 0) {
            return new HashSet(Arrays.asList(principalArr));
        }
        return null;
    }

    /* loaded from: jsse.jar:sun/security/ssl/X509KeyManagerImpl$EntryStatus.class */
    private static class EntryStatus implements Comparable<EntryStatus> {
        final int builderIndex;
        final int keyIndex;
        final String alias;
        final CheckResult checkResult;

        EntryStatus(int i2, int i3, String str, Certificate[] certificateArr, CheckResult checkResult) {
            this.builderIndex = i2;
            this.keyIndex = i3;
            this.alias = str;
            this.checkResult = checkResult;
        }

        @Override // java.lang.Comparable
        public int compareTo(EntryStatus entryStatus) {
            int iCompareTo = this.checkResult.compareTo(entryStatus.checkResult);
            return iCompareTo == 0 ? this.keyIndex - entryStatus.keyIndex : iCompareTo;
        }

        public String toString() {
            String str = this.alias + " (verified: " + ((Object) this.checkResult) + ")";
            if (this.builderIndex == 0) {
                return str;
            }
            return "Builder #" + this.builderIndex + ", alias: " + str;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/X509KeyManagerImpl$CheckType.class */
    private enum CheckType {
        NONE(Collections.emptySet()),
        CLIENT(new HashSet(Arrays.asList("2.5.29.37.0", "1.3.6.1.5.5.7.3.2"))),
        SERVER(new HashSet(Arrays.asList("2.5.29.37.0", "1.3.6.1.5.5.7.3.1", "2.16.840.1.113730.4.1", "1.3.6.1.4.1.311.10.3.3")));

        final Set<String> validEku;

        CheckType(Set set) {
            this.validEku = set;
        }

        private static boolean getBit(boolean[] zArr, int i2) {
            return i2 < zArr.length && zArr[i2];
        }

        CheckResult check(X509Certificate x509Certificate, Date date, List<SNIServerName> list, String str) {
            boolean bit;
            if (this == NONE) {
                return CheckResult.OK;
            }
            try {
                List<String> extendedKeyUsage = x509Certificate.getExtendedKeyUsage();
                if (extendedKeyUsage != null && Collections.disjoint(this.validEku, extendedKeyUsage)) {
                    return CheckResult.EXTENSION_MISMATCH;
                }
                boolean[] keyUsage = x509Certificate.getKeyUsage();
                if (keyUsage != null) {
                    String algorithm = x509Certificate.getPublicKey().getAlgorithm();
                    bit = getBit(keyUsage, 0);
                    switch (algorithm) {
                        case "RSA":
                            if (!bit && (this == CLIENT || !getBit(keyUsage, 2))) {
                                return CheckResult.EXTENSION_MISMATCH;
                            }
                            break;
                        case "RSASSA-PSS":
                            if (!bit && this == SERVER) {
                                return CheckResult.EXTENSION_MISMATCH;
                            }
                            break;
                        case "DSA":
                            if (!bit) {
                                return CheckResult.EXTENSION_MISMATCH;
                            }
                            break;
                        case "DH":
                            if (!getBit(keyUsage, 4)) {
                                return CheckResult.EXTENSION_MISMATCH;
                            }
                            break;
                        case "EC":
                            if (!bit) {
                                return CheckResult.EXTENSION_MISMATCH;
                            }
                            if (this == SERVER && !getBit(keyUsage, 4)) {
                                return CheckResult.EXTENSION_MISMATCH;
                            }
                            break;
                    }
                }
                try {
                    x509Certificate.checkValidity(date);
                    if (list != null && !list.isEmpty()) {
                        Iterator<SNIServerName> it = list.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                SNIServerName next = it.next();
                                if (next.getType() == 0) {
                                    if (!(next instanceof SNIHostName)) {
                                        try {
                                            next = new SNIHostName(next.getEncoded());
                                        } catch (IllegalArgumentException e2) {
                                            if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                                                SSLLogger.fine("Illegal server name: " + ((Object) next), new Object[0]);
                                            }
                                            return CheckResult.INSENSITIVE;
                                        }
                                    }
                                    String asciiName = ((SNIHostName) next).getAsciiName();
                                    try {
                                        X509TrustManagerImpl.checkIdentity(asciiName, x509Certificate, str);
                                    } catch (CertificateException e3) {
                                        if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                                            SSLLogger.fine("Certificate identity does not match Server Name Inidication (SNI): " + asciiName, new Object[0]);
                                        }
                                        return CheckResult.INSENSITIVE;
                                    }
                                }
                            }
                        }
                    }
                    return CheckResult.OK;
                } catch (CertificateException e4) {
                    return CheckResult.EXPIRED;
                }
            } catch (CertificateException e5) {
                return CheckResult.EXTENSION_MISMATCH;
            }
        }

        public String getValidator() {
            if (this == CLIENT) {
                return Validator.VAR_TLS_CLIENT;
            }
            if (this == SERVER) {
                return Validator.VAR_TLS_SERVER;
            }
            return Validator.VAR_GENERIC;
        }
    }

    private List<EntryStatus> getAliases(int i2, List<KeyType> list, Set<Principal> set, boolean z2, CheckType checkType, AlgorithmConstraints algorithmConstraints, List<SNIServerName> list2, String str) throws Exception {
        Certificate[] certificateChain;
        KeyStore keyStore = this.builders.get(i2).getKeyStore();
        ArrayList arrayList = null;
        Date date = verificationDate;
        boolean z3 = false;
        Enumeration<String> enumerationAliases = keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            if (keyStore.isKeyEntry(strNextElement2) && (certificateChain = keyStore.getCertificateChain(strNextElement2)) != null && certificateChain.length != 0) {
                boolean z4 = false;
                int length = certificateChain.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length) {
                        break;
                    }
                    if (certificateChain[i3] instanceof X509Certificate) {
                        i3++;
                    } else {
                        z4 = true;
                        break;
                    }
                }
                if (z4) {
                    continue;
                } else {
                    int i4 = -1;
                    int i5 = 0;
                    Iterator<KeyType> it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (it.next().matches(certificateChain)) {
                            i4 = i5;
                            break;
                        }
                        i5++;
                    }
                    if (i4 == -1) {
                        if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                            SSLLogger.fine("Ignore alias " + strNextElement2 + ": key algorithm does not match", new Object[0]);
                        }
                    } else {
                        if (set != null) {
                            boolean z5 = false;
                            int length2 = certificateChain.length;
                            int i6 = 0;
                            while (true) {
                                if (i6 >= length2) {
                                    break;
                                }
                                if (!set.contains(((X509Certificate) certificateChain[i6]).getIssuerX500Principal())) {
                                    i6++;
                                } else {
                                    z5 = true;
                                    break;
                                }
                            }
                            if (!z5) {
                                if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                                    SSLLogger.fine("Ignore alias " + strNextElement2 + ": issuers do not match", new Object[0]);
                                }
                            }
                        }
                        if (algorithmConstraints != null && !conformsToAlgorithmConstraints(algorithmConstraints, certificateChain, checkType.getValidator())) {
                            if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                                SSLLogger.fine("Ignore alias " + strNextElement2 + ": certificate list does not conform to algorithm constraints", new Object[0]);
                            }
                        } else {
                            if (date == null) {
                                date = new Date();
                            }
                            CheckResult checkResultCheck = checkType.check((X509Certificate) certificateChain[0], date, list2, str);
                            EntryStatus entryStatus = new EntryStatus(i2, i4, strNextElement2, certificateChain, checkResultCheck);
                            if (!z3 && checkResultCheck == CheckResult.OK && i4 == 0) {
                                z3 = true;
                            }
                            if (z3 && !z2) {
                                return Collections.singletonList(entryStatus);
                            }
                            if (arrayList == null) {
                                arrayList = new ArrayList();
                            }
                            arrayList.add(entryStatus);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private static boolean conformsToAlgorithmConstraints(AlgorithmConstraints algorithmConstraints, Certificate[] certificateArr, String str) {
        AlgorithmChecker algorithmChecker = new AlgorithmChecker(algorithmConstraints, str);
        try {
            algorithmChecker.init(false);
            for (int length = certificateArr.length - 1; length >= 0; length--) {
                Certificate certificate = certificateArr[length];
                try {
                    algorithmChecker.check(certificate, Collections.emptySet());
                } catch (CertPathValidatorException e2) {
                    if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                        SSLLogger.fine("Certificate does not conform to algorithm constraints", certificate, e2);
                        return false;
                    }
                    return false;
                }
            }
            return true;
        } catch (CertPathValidatorException e3) {
            if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                SSLLogger.fine("Cannot initialize algorithm constraints checker", e3);
                return false;
            }
            return false;
        }
    }
}
