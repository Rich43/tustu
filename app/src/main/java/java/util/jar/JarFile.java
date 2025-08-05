package java.util.jar;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.jar.JarVerifier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sun.misc.IOUtils;
import sun.misc.JavaUtilZipFileAccess;
import sun.misc.SharedSecrets;
import sun.security.action.GetPropertyAction;
import sun.security.util.ManifestEntryVerifier;
import sun.security.util.SignatureFileVerifier;

/* loaded from: rt.jar:java/util/jar/JarFile.class */
public class JarFile extends ZipFile {
    private SoftReference<Manifest> manRef;
    private JarEntry manEntry;
    private JarVerifier jv;
    private boolean jvInitialized;
    private boolean verify;
    private boolean hasClassPathAttribute;
    private volatile boolean hasCheckedSpecialAttributes;
    private static final JavaUtilZipFileAccess JUZFA;
    public static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
    private static final char[] CLASSPATH_CHARS;
    private static final int[] CLASSPATH_LASTOCC;
    private static final int[] CLASSPATH_OPTOSFT;
    private static String javaHome;
    private static volatile String[] jarNames;

    private native String[] getMetaInfEntryNames();

    static {
        SharedSecrets.setJavaUtilJarAccess(new JavaUtilJarAccessImpl());
        JUZFA = SharedSecrets.getJavaUtilZipFileAccess();
        CLASSPATH_CHARS = new char[]{'c', 'l', 'a', 's', 's', '-', 'p', 'a', 't', 'h'};
        CLASSPATH_LASTOCC = new int[128];
        CLASSPATH_OPTOSFT = new int[10];
        CLASSPATH_LASTOCC[99] = 1;
        CLASSPATH_LASTOCC[108] = 2;
        CLASSPATH_LASTOCC[115] = 5;
        CLASSPATH_LASTOCC[45] = 6;
        CLASSPATH_LASTOCC[112] = 7;
        CLASSPATH_LASTOCC[97] = 8;
        CLASSPATH_LASTOCC[116] = 9;
        CLASSPATH_LASTOCC[104] = 10;
        for (int i2 = 0; i2 < 9; i2++) {
            CLASSPATH_OPTOSFT[i2] = 10;
        }
        CLASSPATH_OPTOSFT[9] = 1;
    }

    public JarFile(String str) throws IOException {
        this(new File(str), true, 1);
    }

    public JarFile(String str, boolean z2) throws IOException {
        this(new File(str), z2, 1);
    }

    public JarFile(File file) throws IOException {
        this(file, true, 1);
    }

    public JarFile(File file, boolean z2) throws IOException {
        this(file, z2, 1);
    }

    public JarFile(File file, boolean z2, int i2) throws IOException {
        super(file, i2);
        this.verify = z2;
    }

    public Manifest getManifest() throws IOException {
        return getManifestFromReference();
    }

    private Manifest getManifestFromReference() throws IOException {
        JarEntry manEntry;
        Manifest manifest = this.manRef != null ? this.manRef.get() : null;
        if (manifest == null && (manEntry = getManEntry()) != null) {
            if (this.verify) {
                byte[] bytes = getBytes(manEntry);
                if (!this.jvInitialized) {
                    if (JUZFA.getManifestNum(this) == 1) {
                        this.jv = new JarVerifier(manEntry.getName(), bytes);
                    } else if (JarVerifier.debug != null) {
                        JarVerifier.debug.println("Multiple MANIFEST.MF found. Treat JAR file as unsigned");
                    }
                }
                manifest = new Manifest(this.jv, new ByteArrayInputStream(bytes));
            } else {
                manifest = new Manifest(super.getInputStream(manEntry));
            }
            this.manRef = new SoftReference<>(manifest);
        }
        return manifest;
    }

    public JarEntry getJarEntry(String str) {
        return (JarEntry) getEntry(str);
    }

    @Override // java.util.zip.ZipFile
    public ZipEntry getEntry(String str) {
        ZipEntry entry = super.getEntry(str);
        if (entry != null) {
            return new JarFileEntry(entry);
        }
        return null;
    }

    /* loaded from: rt.jar:java/util/jar/JarFile$JarEntryIterator.class */
    private class JarEntryIterator implements Enumeration<JarEntry>, Iterator<JarEntry> {

        /* renamed from: e, reason: collision with root package name */
        final Enumeration<? extends ZipEntry> f12595e;

        private JarEntryIterator() {
            this.f12595e = JarFile.super.entries();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f12595e.hasMoreElements();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public JarEntry next() {
            return JarFile.this.new JarFileEntry(this.f12595e.nextElement2());
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public JarEntry nextElement2() {
            return next();
        }
    }

    @Override // java.util.zip.ZipFile
    public Enumeration<JarEntry> entries() {
        return new JarEntryIterator();
    }

    @Override // java.util.zip.ZipFile
    public Stream<JarEntry> stream() {
        return StreamSupport.stream(Spliterators.spliterator(new JarEntryIterator(), size(), 1297), false);
    }

    /* loaded from: rt.jar:java/util/jar/JarFile$JarFileEntry.class */
    private class JarFileEntry extends JarEntry {
        JarFileEntry(ZipEntry zipEntry) {
            super(zipEntry);
        }

        @Override // java.util.jar.JarEntry
        public Attributes getAttributes() throws IOException {
            Manifest manifest = JarFile.this.getManifest();
            if (manifest != null) {
                return manifest.getAttributes(getName());
            }
            return null;
        }

        @Override // java.util.jar.JarEntry
        public Certificate[] getCertificates() {
            try {
                JarFile.this.maybeInstantiateVerifier();
                if (this.certs == null && JarFile.this.jv != null) {
                    this.certs = JarFile.this.jv.getCerts(JarFile.this, this);
                }
                if (this.certs == null) {
                    return null;
                }
                return (Certificate[]) this.certs.clone();
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }

        @Override // java.util.jar.JarEntry
        public CodeSigner[] getCodeSigners() {
            try {
                JarFile.this.maybeInstantiateVerifier();
                if (this.signers == null && JarFile.this.jv != null) {
                    this.signers = JarFile.this.jv.getCodeSigners(JarFile.this, this);
                }
                if (this.signers == null) {
                    return null;
                }
                return (CodeSigner[]) this.signers.clone();
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeInstantiateVerifier() throws IOException {
        if (this.jv == null && this.verify) {
            String[] metaInfEntryNames = getMetaInfEntryNames();
            if (metaInfEntryNames != null) {
                for (String str : metaInfEntryNames) {
                    String upperCase = str.toUpperCase(Locale.ENGLISH);
                    if (upperCase.endsWith(".DSA") || upperCase.endsWith(".RSA") || upperCase.endsWith(".EC") || upperCase.endsWith(".SF")) {
                        getManifest();
                        return;
                    }
                }
            }
            this.verify = false;
        }
    }

    private void initializeVerifier() throws JarException {
        ManifestEntryVerifier manifestEntryVerifier = null;
        try {
            String[] metaInfEntryNames = getMetaInfEntryNames();
            if (metaInfEntryNames != null) {
                for (int i2 = 0; i2 < metaInfEntryNames.length; i2++) {
                    String upperCase = metaInfEntryNames[i2].toUpperCase(Locale.ENGLISH);
                    if (MANIFEST_NAME.equals(upperCase) || SignatureFileVerifier.isBlockOrSF(upperCase)) {
                        JarEntry jarEntry = getJarEntry(metaInfEntryNames[i2]);
                        if (jarEntry == null) {
                            throw new JarException("corrupted jar file");
                        }
                        if (manifestEntryVerifier == null) {
                            manifestEntryVerifier = new ManifestEntryVerifier(getManifestFromReference(), this.jv.manifestName);
                        }
                        byte[] bytes = getBytes(jarEntry);
                        if (bytes != null && bytes.length > 0) {
                            this.jv.beginEntry(jarEntry, manifestEntryVerifier);
                            this.jv.update(bytes.length, bytes, 0, bytes.length, manifestEntryVerifier);
                            this.jv.update(-1, null, 0, 0, manifestEntryVerifier);
                        }
                    }
                }
            }
        } catch (IOException e2) {
            this.jv = null;
            this.verify = false;
            if (JarVerifier.debug != null) {
                JarVerifier.debug.println("jarfile parsing error!");
                e2.printStackTrace();
            }
        }
        if (this.jv != null) {
            this.jv.doneWithMeta();
            if (JarVerifier.debug != null) {
                JarVerifier.debug.println("done with meta!");
            }
            if (this.jv.nothingToVerify()) {
                if (JarVerifier.debug != null) {
                    JarVerifier.debug.println("nothing to verify!");
                }
                this.jv = null;
                this.verify = false;
            }
        }
    }

    private byte[] getBytes(ZipEntry zipEntry) throws IOException {
        InputStream inputStream = super.getInputStream(zipEntry);
        Throwable th = null;
        try {
            long size = zipEntry.getSize();
            if (size > SignatureFileVerifier.MAX_SIG_FILE_SIZE) {
                throw new IOException("Unsupported size: " + size + " for JarEntry " + zipEntry.getName() + ". Allowed max size: " + SignatureFileVerifier.MAX_SIG_FILE_SIZE + " bytes");
            }
            int i2 = (int) size;
            byte[] allBytes = IOUtils.readAllBytes(inputStream);
            if (i2 != -1 && allBytes.length != i2) {
                throw new EOFException("Expected:" + i2 + ", read:" + allBytes.length);
            }
            return allBytes;
        } finally {
            if (inputStream != null) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    inputStream.close();
                }
            }
        }
    }

    @Override // java.util.zip.ZipFile
    public synchronized InputStream getInputStream(ZipEntry zipEntry) throws IOException {
        maybeInstantiateVerifier();
        if (this.jv == null) {
            return super.getInputStream(zipEntry);
        }
        if (!this.jvInitialized) {
            initializeVerifier();
            this.jvInitialized = true;
            if (this.jv == null) {
                return super.getInputStream(zipEntry);
            }
        }
        return new JarVerifier.VerifierStream(getManifestFromReference(), zipEntry instanceof JarFileEntry ? (JarEntry) zipEntry : getJarEntry(zipEntry.getName()), super.getInputStream(zipEntry), this.jv);
    }

    private JarEntry getManEntry() {
        String[] metaInfEntryNames;
        if (this.manEntry == null) {
            this.manEntry = getJarEntry(MANIFEST_NAME);
            if (this.manEntry == null && (metaInfEntryNames = getMetaInfEntryNames()) != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= metaInfEntryNames.length) {
                        break;
                    }
                    if (!MANIFEST_NAME.equals(metaInfEntryNames[i2].toUpperCase(Locale.ENGLISH))) {
                        i2++;
                    } else {
                        this.manEntry = getJarEntry(metaInfEntryNames[i2]);
                        break;
                    }
                }
            }
        }
        return this.manEntry;
    }

    boolean hasClassPathAttribute() throws IOException {
        checkForSpecialAttributes();
        return this.hasClassPathAttribute;
    }

    private boolean match(char[] cArr, byte[] bArr, int[] iArr, int[] iArr2) {
        int i2;
        char c2;
        int length = cArr.length;
        int length2 = bArr.length - length;
        int iMax = 0;
        while (true) {
            int i3 = iMax;
            if (i3 <= length2) {
                i2 = length - 1;
                while (i2 >= 0) {
                    char c3 = (char) bArr[i3 + i2];
                    c2 = ((c3 - 'A') | ('Z' - c3)) >= 0 ? (char) (c3 + ' ') : c3;
                    if (c2 != cArr[i2]) {
                        break;
                    }
                    i2--;
                }
                return true;
            }
            return false;
            iMax = i3 + Math.max((i2 + 1) - iArr[c2 & 127], iArr2[i2]);
        }
    }

    private void checkForSpecialAttributes() throws IOException {
        JarEntry manEntry;
        if (this.hasCheckedSpecialAttributes) {
            return;
        }
        if (!isKnownNotToHaveSpecialAttributes() && (manEntry = getManEntry()) != null) {
            if (match(CLASSPATH_CHARS, getBytes(manEntry), CLASSPATH_LASTOCC, CLASSPATH_OPTOSFT)) {
                this.hasClassPathAttribute = true;
            }
        }
        this.hasCheckedSpecialAttributes = true;
    }

    private boolean isKnownNotToHaveSpecialAttributes() {
        if (javaHome == null) {
            javaHome = (String) AccessController.doPrivileged(new GetPropertyAction("java.home"));
        }
        if (jarNames == null) {
            String[] strArr = new String[11];
            String str = File.separator;
            int i2 = 0 + 1;
            strArr[0] = str + "rt.jar";
            int i3 = i2 + 1;
            strArr[i2] = str + "jsse.jar";
            int i4 = i3 + 1;
            strArr[i3] = str + "jce.jar";
            int i5 = i4 + 1;
            strArr[i4] = str + "charsets.jar";
            int i6 = i5 + 1;
            strArr[i5] = str + "dnsns.jar";
            int i7 = i6 + 1;
            strArr[i6] = str + "zipfs.jar";
            int i8 = i7 + 1;
            strArr[i7] = str + "localedata.jar";
            int i9 = i8 + 1;
            strArr[i8] = "cldrdata.jar";
            int i10 = i9 + 1;
            strArr[i9] = "cldrdata.jarsunjce_provider.jar";
            int i11 = i10 + 1;
            strArr[i10] = "cldrdata.jarsunpkcs11.jar";
            int i12 = i11 + 1;
            strArr[i11] = "cldrdata.jarsunec.jar";
            jarNames = strArr;
        }
        String name = getName();
        if (name.startsWith(javaHome)) {
            for (String str2 : jarNames) {
                if (name.endsWith(str2)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    synchronized void ensureInitialization() throws JarException {
        try {
            maybeInstantiateVerifier();
            if (this.jv != null && !this.jvInitialized) {
                initializeVerifier();
                this.jvInitialized = true;
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    JarEntry newEntry(ZipEntry zipEntry) {
        return new JarFileEntry(zipEntry);
    }

    Enumeration<String> entryNames(CodeSource[] codeSourceArr) throws JarException {
        ensureInitialization();
        if (this.jv != null) {
            return this.jv.entryNames(this, codeSourceArr);
        }
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= codeSourceArr.length) {
                break;
            }
            if (codeSourceArr[i2].getCodeSigners() != null) {
                i2++;
            } else {
                z2 = true;
                break;
            }
        }
        if (z2) {
            return unsignedEntryNames();
        }
        return new Enumeration<String>() { // from class: java.util.jar.JarFile.1
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
    }

    Enumeration<JarEntry> entries2() throws JarException {
        ensureInitialization();
        if (this.jv != null) {
            return this.jv.entries2(this, super.entries());
        }
        final Enumeration<? extends ZipEntry> enumerationEntries = super.entries();
        return new Enumeration<JarEntry>() { // from class: java.util.jar.JarFile.2
            ZipEntry entry;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                if (this.entry != null) {
                    return true;
                }
                while (enumerationEntries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enumerationEntries.nextElement2();
                    if (!JarVerifier.isSigningRelated(zipEntry.getName())) {
                        this.entry = zipEntry;
                        return true;
                    }
                }
                return false;
            }

            @Override // java.util.Enumeration
            /* renamed from: nextElement, reason: merged with bridge method [inline-methods] */
            public JarEntry nextElement2() {
                if (hasMoreElements()) {
                    ZipEntry zipEntry = this.entry;
                    this.entry = null;
                    return JarFile.this.new JarFileEntry(zipEntry);
                }
                throw new NoSuchElementException();
            }
        };
    }

    CodeSource[] getCodeSources(URL url) throws JarException {
        ensureInitialization();
        if (this.jv != null) {
            return this.jv.getCodeSources(this, url);
        }
        if (unsignedEntryNames().hasMoreElements()) {
            return new CodeSource[]{JarVerifier.getUnsignedCS(url)};
        }
        return null;
    }

    private Enumeration<String> unsignedEntryNames() {
        final Enumeration<JarEntry> enumerationEntries = entries();
        return new Enumeration<String>() { // from class: java.util.jar.JarFile.3
            String name;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                if (this.name != null) {
                    return true;
                }
                while (enumerationEntries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enumerationEntries.nextElement2();
                    String name = zipEntry.getName();
                    if (!zipEntry.isDirectory() && !JarVerifier.isSigningRelated(name)) {
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

    CodeSource getCodeSource(URL url, String str) throws JarException {
        CodeSource codeSource;
        ensureInitialization();
        if (this.jv != null) {
            if (this.jv.eagerValidation) {
                JarEntry jarEntry = getJarEntry(str);
                if (jarEntry != null) {
                    codeSource = this.jv.getCodeSource(url, this, jarEntry);
                } else {
                    codeSource = this.jv.getCodeSource(url, str);
                }
                return codeSource;
            }
            return this.jv.getCodeSource(url, str);
        }
        return JarVerifier.getUnsignedCS(url);
    }

    void setEagerValidation(boolean z2) {
        try {
            maybeInstantiateVerifier();
            if (this.jv != null) {
                this.jv.setEagerValidation(z2);
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    List<Object> getManifestDigests() throws JarException {
        ensureInitialization();
        if (this.jv != null) {
            return this.jv.getManifestDigests();
        }
        return new ArrayList();
    }
}
