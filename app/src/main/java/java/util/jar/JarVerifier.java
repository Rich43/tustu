package java.util.jar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import sun.misc.JarIndex;
import sun.security.util.Debug;
import sun.security.util.ManifestDigester;
import sun.security.util.ManifestEntryVerifier;
import sun.security.util.SignatureFileVerifier;

/* loaded from: rt.jar:java/util/jar/JarVerifier.class */
class JarVerifier {
    static final Debug debug = Debug.getInstance("jar");
    private ArrayList<CodeSigner[]> signerCache;
    private volatile ManifestDigester manDig;
    byte[] manifestRawBytes;
    final String manifestName;
    boolean eagerValidation;
    private URL lastURL;
    private Map<CodeSigner[], CodeSource> lastURLMap;
    private Map<String, CodeSigner[]> signerMap;
    private List<CodeSigner[]> jarCodeSigners;
    private boolean parsingBlockOrSF = false;
    private boolean parsingMeta = true;
    private boolean anyToVerify = true;
    private Object csdomain = new Object();
    private Map<URL, Map<CodeSigner[], CodeSource>> urlToCodeSourceMap = new HashMap();
    private Map<CodeSigner[], CodeSource> signerToCodeSource = new HashMap();
    private CodeSigner[] emptySigner = new CodeSigner[0];
    private Enumeration<String> emptyEnumeration = new Enumeration<String>() { // from class: java.util.jar.JarVerifier.3
        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public String nextElement2() {
            throw new NoSuchElementException();
        }
    };
    private Hashtable<String, CodeSigner[]> sigFileSigners = new Hashtable<>();
    private Hashtable<String, CodeSigner[]> verifiedSigners = new Hashtable<>();
    private Hashtable<String, byte[]> sigFileData = new Hashtable<>(11);
    private ArrayList<SignatureFileVerifier> pendingBlocks = new ArrayList<>();
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private List<Object> manifestDigests = new ArrayList();
    private Map<CodeSigner[], Map<String, Boolean>> signersToAlgs = new HashMap();

    public JarVerifier(String str, byte[] bArr) {
        this.manifestRawBytes = null;
        this.manifestName = str;
        this.manifestRawBytes = bArr;
    }

    public void beginEntry(JarEntry jarEntry, ManifestEntryVerifier manifestEntryVerifier) throws IOException {
        if (jarEntry == null) {
            return;
        }
        if (debug != null) {
            debug.println("beginEntry " + jarEntry.getName());
        }
        String name = jarEntry.getName();
        if (this.parsingMeta) {
            String upperCase = name.toUpperCase(Locale.ENGLISH);
            if (upperCase.startsWith("META-INF/") || upperCase.startsWith("/META-INF/")) {
                if (jarEntry.isDirectory()) {
                    manifestEntryVerifier.setEntry(null, jarEntry);
                    return;
                }
                if (upperCase.equals(JarFile.MANIFEST_NAME) || upperCase.equals(JarIndex.INDEX_NAME)) {
                    return;
                }
                if (SignatureFileVerifier.isBlockOrSF(upperCase)) {
                    this.parsingBlockOrSF = true;
                    this.baos.reset();
                    manifestEntryVerifier.setEntry(null, jarEntry);
                    return;
                }
            }
        }
        if (this.parsingMeta) {
            doneWithMeta();
        }
        if (jarEntry.isDirectory()) {
            manifestEntryVerifier.setEntry(null, jarEntry);
            return;
        }
        if (name.startsWith("./")) {
            name = name.substring(2);
        }
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        if (!name.equalsIgnoreCase(JarFile.MANIFEST_NAME) && (this.sigFileSigners.get(name) != null || this.verifiedSigners.get(name) != null)) {
            manifestEntryVerifier.setEntry(name, jarEntry);
        } else {
            manifestEntryVerifier.setEntry(null, jarEntry);
        }
    }

    public void update(int i2, ManifestEntryVerifier manifestEntryVerifier) throws IOException {
        if (i2 != -1) {
            if (this.parsingBlockOrSF) {
                this.baos.write(i2);
                return;
            } else {
                manifestEntryVerifier.update((byte) i2);
                return;
            }
        }
        processEntry(manifestEntryVerifier);
    }

    public void update(int i2, byte[] bArr, int i3, int i4, ManifestEntryVerifier manifestEntryVerifier) throws IOException {
        if (i2 != -1) {
            if (this.parsingBlockOrSF) {
                this.baos.write(bArr, i3, i2);
                return;
            } else {
                manifestEntryVerifier.update(bArr, i3, i2);
                return;
            }
        }
        processEntry(manifestEntryVerifier);
    }

    private void processEntry(ManifestEntryVerifier manifestEntryVerifier) throws IOException {
        if (!this.parsingBlockOrSF) {
            JarEntry entry = manifestEntryVerifier.getEntry();
            if (entry == null || entry.signers != null) {
                return;
            }
            entry.signers = manifestEntryVerifier.verify(this.verifiedSigners, this.sigFileSigners, this.signersToAlgs);
            entry.certs = mapSignersToCertArray(entry.signers);
            return;
        }
        try {
            this.parsingBlockOrSF = false;
            if (debug != null) {
                debug.println("processEntry: processing block");
            }
            String upperCase = manifestEntryVerifier.getEntry().getName().toUpperCase(Locale.ENGLISH);
            if (upperCase.endsWith(".SF")) {
                String strSubstring = upperCase.substring(0, upperCase.length() - 3);
                byte[] byteArray = this.baos.toByteArray();
                this.sigFileData.put(strSubstring, byteArray);
                Iterator<SignatureFileVerifier> it = this.pendingBlocks.iterator();
                while (it.hasNext()) {
                    SignatureFileVerifier next = it.next();
                    if (next.needSignatureFile(strSubstring)) {
                        if (debug != null) {
                            debug.println("processEntry: processing pending block");
                        }
                        next.setSignatureFile(byteArray);
                        next.process(this.sigFileSigners, this.manifestDigests, this.manifestName);
                    }
                }
                return;
            }
            String strSubstring2 = upperCase.substring(0, upperCase.lastIndexOf("."));
            if (this.signerCache == null) {
                this.signerCache = new ArrayList<>();
            }
            if (this.manDig == null) {
                synchronized (this.manifestRawBytes) {
                    if (this.manDig == null) {
                        this.manDig = new ManifestDigester(this.manifestRawBytes);
                        this.manifestRawBytes = null;
                    }
                }
            }
            SignatureFileVerifier signatureFileVerifier = new SignatureFileVerifier(this.signerCache, this.manDig, upperCase, this.baos.toByteArray());
            if (signatureFileVerifier.needSignatureFileBytes()) {
                byte[] bArr = this.sigFileData.get(strSubstring2);
                if (bArr == null) {
                    if (debug != null) {
                        debug.println("adding pending block");
                    }
                    this.pendingBlocks.add(signatureFileVerifier);
                    return;
                }
                signatureFileVerifier.setSignatureFile(bArr);
            }
            signatureFileVerifier.process(this.sigFileSigners, this.manifestDigests, this.manifestName);
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("processEntry caught: " + ((Object) e2));
            }
        } catch (NoSuchAlgorithmException e3) {
            if (debug != null) {
                debug.println("processEntry caught: " + ((Object) e3));
            }
        } catch (SignatureException e4) {
            if (debug != null) {
                debug.println("processEntry caught: " + ((Object) e4));
            }
        } catch (CertificateException e5) {
            if (debug != null) {
                debug.println("processEntry caught: " + ((Object) e5));
            }
        }
    }

    @Deprecated
    public Certificate[] getCerts(String str) {
        return mapSignersToCertArray(getCodeSigners(str));
    }

    public Certificate[] getCerts(JarFile jarFile, JarEntry jarEntry) {
        return mapSignersToCertArray(getCodeSigners(jarFile, jarEntry));
    }

    public CodeSigner[] getCodeSigners(String str) {
        return this.verifiedSigners.get(str);
    }

    public CodeSigner[] getCodeSigners(JarFile jarFile, JarEntry jarEntry) {
        String name = jarEntry.getName();
        if (this.eagerValidation && this.sigFileSigners.get(name) != null) {
            try {
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                byte[] bArr = new byte[1024];
                for (int length = bArr.length; length != -1; length = inputStream.read(bArr, 0, bArr.length)) {
                }
                inputStream.close();
            } catch (IOException e2) {
            }
        }
        return getCodeSigners(name);
    }

    private static Certificate[] mapSignersToCertArray(CodeSigner[] codeSignerArr) {
        if (codeSignerArr != null) {
            ArrayList arrayList = new ArrayList();
            for (CodeSigner codeSigner : codeSignerArr) {
                arrayList.addAll(codeSigner.getSignerCertPath().getCertificates());
            }
            return (Certificate[]) arrayList.toArray(new Certificate[arrayList.size()]);
        }
        return null;
    }

    boolean nothingToVerify() {
        return !this.anyToVerify;
    }

    void doneWithMeta() {
        this.parsingMeta = false;
        this.anyToVerify = !this.sigFileSigners.isEmpty();
        this.baos = null;
        this.sigFileData = null;
        this.pendingBlocks = null;
        this.signerCache = null;
        this.manDig = null;
        if (this.sigFileSigners.containsKey(this.manifestName)) {
            this.verifiedSigners.put(this.manifestName, this.sigFileSigners.remove(this.manifestName));
        }
    }

    /* loaded from: rt.jar:java/util/jar/JarVerifier$VerifierStream.class */
    static class VerifierStream extends InputStream {
        private InputStream is;
        private JarVerifier jv;
        private ManifestEntryVerifier mev;
        private long numLeft;

        VerifierStream(Manifest manifest, JarEntry jarEntry, InputStream inputStream, JarVerifier jarVerifier) throws IOException {
            this.is = inputStream;
            this.jv = jarVerifier;
            this.mev = new ManifestEntryVerifier(manifest, jarVerifier.manifestName);
            this.jv.beginEntry(jarEntry, this.mev);
            this.numLeft = jarEntry.getSize();
            if (this.numLeft == 0) {
                this.jv.update(-1, this.mev);
            }
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.numLeft > 0) {
                int i2 = this.is.read();
                this.jv.update(i2, this.mev);
                this.numLeft--;
                if (this.numLeft == 0) {
                    this.jv.update(-1, this.mev);
                }
                return i2;
            }
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            if (this.numLeft > 0 && this.numLeft < i3) {
                i3 = (int) this.numLeft;
            }
            if (this.numLeft > 0) {
                int i4 = this.is.read(bArr, i2, i3);
                this.jv.update(i4, bArr, i2, i3, this.mev);
                this.numLeft -= i4;
                if (this.numLeft == 0) {
                    this.jv.update(-1, bArr, i2, i3, this.mev);
                }
                return i4;
            }
            return -1;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.is != null) {
                this.is.close();
            }
            this.is = null;
            this.mev = null;
            this.jv = null;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.is.available();
        }
    }

    private synchronized CodeSource mapSignersToCodeSource(URL url, CodeSigner[] codeSignerArr) {
        Map<CodeSigner[], CodeSource> map;
        if (url == this.lastURL) {
            map = this.lastURLMap;
        } else {
            map = this.urlToCodeSourceMap.get(url);
            if (map == null) {
                map = new HashMap();
                this.urlToCodeSourceMap.put(url, map);
            }
            this.lastURLMap = map;
            this.lastURL = url;
        }
        CodeSource verifierCodeSource = map.get(codeSignerArr);
        if (verifierCodeSource == null) {
            verifierCodeSource = new VerifierCodeSource(this.csdomain, url, codeSignerArr);
            this.signerToCodeSource.put(codeSignerArr, verifierCodeSource);
        }
        return verifierCodeSource;
    }

    private CodeSource[] mapSignersToCodeSources(URL url, List<CodeSigner[]> list, boolean z2) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            arrayList.add(mapSignersToCodeSource(url, list.get(i2)));
        }
        if (z2) {
            arrayList.add(mapSignersToCodeSource(url, null));
        }
        return (CodeSource[]) arrayList.toArray(new CodeSource[arrayList.size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private CodeSigner[] findMatchingSigners(CodeSource codeSource) {
        if (!(codeSource instanceof VerifierCodeSource) || !((VerifierCodeSource) codeSource).isSameDomain(this.csdomain)) {
            CodeSource[] codeSourceArrMapSignersToCodeSources = mapSignersToCodeSources(codeSource.getLocation(), getJarCodeSigners(), true);
            ArrayList arrayList = new ArrayList();
            for (CodeSource codeSource2 : codeSourceArrMapSignersToCodeSources) {
                arrayList.add(codeSource2);
            }
            int iIndexOf = arrayList.indexOf(codeSource);
            if (iIndexOf == -1) {
                return null;
            }
            CodeSigner[] privateSigners = ((VerifierCodeSource) arrayList.get(iIndexOf)).getPrivateSigners();
            if (privateSigners == null) {
                privateSigners = this.emptySigner;
            }
            return privateSigners;
        }
        return ((VerifierCodeSource) codeSource).getPrivateSigners();
    }

    /* loaded from: rt.jar:java/util/jar/JarVerifier$VerifierCodeSource.class */
    private static class VerifierCodeSource extends CodeSource {
        private static final long serialVersionUID = -9047366145967768825L;
        URL vlocation;
        CodeSigner[] vsigners;
        Certificate[] vcerts;
        Object csdomain;

        VerifierCodeSource(Object obj, URL url, CodeSigner[] codeSignerArr) {
            super(url, codeSignerArr);
            this.csdomain = obj;
            this.vlocation = url;
            this.vsigners = codeSignerArr;
        }

        VerifierCodeSource(Object obj, URL url, Certificate[] certificateArr) {
            super(url, certificateArr);
            this.csdomain = obj;
            this.vlocation = url;
            this.vcerts = certificateArr;
        }

        @Override // java.security.CodeSource
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof VerifierCodeSource) {
                VerifierCodeSource verifierCodeSource = (VerifierCodeSource) obj;
                if (isSameDomain(verifierCodeSource.csdomain)) {
                    if (verifierCodeSource.vsigners != this.vsigners || verifierCodeSource.vcerts != this.vcerts) {
                        return false;
                    }
                    if (verifierCodeSource.vlocation != null) {
                        return verifierCodeSource.vlocation.equals(this.vlocation);
                    }
                    if (this.vlocation != null) {
                        return this.vlocation.equals(verifierCodeSource.vlocation);
                    }
                    return true;
                }
            }
            return super.equals(obj);
        }

        boolean isSameDomain(Object obj) {
            return this.csdomain == obj;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CodeSigner[] getPrivateSigners() {
            return this.vsigners;
        }

        private Certificate[] getPrivateCertificates() {
            return this.vcerts;
        }
    }

    private synchronized Map<String, CodeSigner[]> signerMap() {
        if (this.signerMap == null) {
            this.signerMap = new HashMap(this.verifiedSigners.size() + this.sigFileSigners.size());
            this.signerMap.putAll(this.verifiedSigners);
            this.signerMap.putAll(this.sigFileSigners);
        }
        return this.signerMap;
    }

    public synchronized Enumeration<String> entryNames(JarFile jarFile, CodeSource[] codeSourceArr) {
        final Iterator<Map.Entry<String, CodeSigner[]>> it = signerMap().entrySet().iterator();
        boolean z2 = false;
        final ArrayList arrayList = new ArrayList(codeSourceArr.length);
        for (CodeSource codeSource : codeSourceArr) {
            CodeSigner[] codeSignerArrFindMatchingSigners = findMatchingSigners(codeSource);
            if (codeSignerArrFindMatchingSigners != null) {
                if (codeSignerArrFindMatchingSigners.length > 0) {
                    arrayList.add(codeSignerArrFindMatchingSigners);
                } else {
                    z2 = true;
                }
            } else {
                z2 = true;
            }
        }
        final Enumeration<String> enumerationUnsignedEntryNames = z2 ? unsignedEntryNames(jarFile) : this.emptyEnumeration;
        return new Enumeration<String>() { // from class: java.util.jar.JarVerifier.1
            String name;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                if (this.name != null) {
                    return true;
                }
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (arrayList.contains(entry.getValue())) {
                        this.name = (String) entry.getKey();
                        return true;
                    }
                }
                if (enumerationUnsignedEntryNames.hasMoreElements()) {
                    this.name = (String) enumerationUnsignedEntryNames.nextElement2();
                    return true;
                }
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public String nextElement2() {
                if (hasMoreElements()) {
                    String str = this.name;
                    this.name = null;
                    return str;
                }
                throw new NoSuchElementException();
            }
        };
    }

    public Enumeration<JarEntry> entries2(final JarFile jarFile, final Enumeration<? extends ZipEntry> enumeration) {
        final HashMap map = new HashMap();
        map.putAll(signerMap());
        return new Enumeration<JarEntry>() { // from class: java.util.jar.JarVerifier.2
            Enumeration<String> signers = null;
            JarEntry entry;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                if (this.entry != null) {
                    return true;
                }
                while (enumeration.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enumeration.nextElement2();
                    if (!JarVerifier.isSigningRelated(zipEntry.getName())) {
                        this.entry = jarFile.newEntry(zipEntry);
                        return true;
                    }
                }
                if (this.signers == null) {
                    this.signers = Collections.enumeration(map.keySet());
                }
                if (this.signers.hasMoreElements()) {
                    this.entry = jarFile.newEntry(new ZipEntry(this.signers.nextElement2()));
                    return true;
                }
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public JarEntry nextElement2() {
                if (hasMoreElements()) {
                    JarEntry jarEntry = this.entry;
                    map.remove(jarEntry.getName());
                    this.entry = null;
                    return jarEntry;
                }
                throw new NoSuchElementException();
            }
        };
    }

    static boolean isSigningRelated(String str) {
        return SignatureFileVerifier.isSigningRelated(str);
    }

    private Enumeration<String> unsignedEntryNames(JarFile jarFile) {
        final Map<String, CodeSigner[]> mapSignerMap = signerMap();
        final Enumeration<JarEntry> enumerationEntries = jarFile.entries();
        return new Enumeration<String>() { // from class: java.util.jar.JarVerifier.4
            String name;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                if (this.name != null) {
                    return true;
                }
                while (enumerationEntries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enumerationEntries.nextElement2();
                    String name = zipEntry.getName();
                    if (!zipEntry.isDirectory() && !JarVerifier.isSigningRelated(name) && mapSignerMap.get(name) == null) {
                        this.name = name;
                        return true;
                    }
                }
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public String nextElement2() {
                if (hasMoreElements()) {
                    String str = this.name;
                    this.name = null;
                    return str;
                }
                throw new NoSuchElementException();
            }
        };
    }

    private synchronized List<CodeSigner[]> getJarCodeSigners() {
        if (this.jarCodeSigners == null) {
            HashSet hashSet = new HashSet();
            hashSet.addAll(signerMap().values());
            this.jarCodeSigners = new ArrayList();
            this.jarCodeSigners.addAll(hashSet);
        }
        return this.jarCodeSigners;
    }

    public synchronized CodeSource[] getCodeSources(JarFile jarFile, URL url) {
        return mapSignersToCodeSources(url, getJarCodeSigners(), unsignedEntryNames(jarFile).hasMoreElements());
    }

    public CodeSource getCodeSource(URL url, String str) {
        return mapSignersToCodeSource(url, signerMap().get(str));
    }

    public CodeSource getCodeSource(URL url, JarFile jarFile, JarEntry jarEntry) {
        return mapSignersToCodeSource(url, getCodeSigners(jarFile, jarEntry));
    }

    public void setEagerValidation(boolean z2) {
        this.eagerValidation = z2;
    }

    public synchronized List<Object> getManifestDigests() {
        return Collections.unmodifiableList(this.manifestDigests);
    }

    static CodeSource getUnsignedCS(URL url) {
        return new VerifierCodeSource((Object) null, url, (Certificate[]) null);
    }

    boolean isTrustedManifestEntry(String str) {
        CodeSigner[] codeSignerArr = this.verifiedSigners.get(this.manifestName);
        if (codeSignerArr == null) {
            return true;
        }
        CodeSigner[] codeSignerArr2 = this.sigFileSigners.get(str);
        if (codeSignerArr2 == null) {
            codeSignerArr2 = this.verifiedSigners.get(str);
        }
        return codeSignerArr2 != null && codeSignerArr2.length == codeSignerArr.length;
    }
}
