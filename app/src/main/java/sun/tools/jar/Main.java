package sun.tools.jar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import sun.misc.JarIndex;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/tools/jar/Main.class */
public class Main {
    String program;
    PrintStream out;
    PrintStream err;
    String fname;
    String mname;
    String ename;
    String[] files;
    boolean cflag;
    boolean uflag;
    boolean xflag;
    boolean tflag;
    boolean vflag;
    boolean flag0;
    boolean Mflag;
    boolean iflag;
    boolean nflag;
    boolean pflag;
    static final String MANIFEST_DIR = "META-INF/";
    static final String VERSION = "1.0";
    private static ResourceBundle rsrc;
    private static final boolean useExtractionTime;
    private boolean ok;
    static final /* synthetic */ boolean $assertionsDisabled;
    String zname = "";
    String rootjar = null;
    Map<String, File> entryMap = new HashMap();
    Set<File> entries = new LinkedHashSet();
    Set<String> paths = new HashSet();
    private byte[] copyBuf = new byte[8192];
    private HashSet<String> jarPaths = new HashSet<>();

    static {
        $assertionsDisabled = !Main.class.desiredAssertionStatus();
        useExtractionTime = Boolean.getBoolean("sun.tools.jar.useExtractionTime");
        try {
            rsrc = ResourceBundle.getBundle("sun.tools.jar.resources.jar");
        } catch (MissingResourceException e2) {
            throw new Error("Fatal: Resource for jar is missing");
        }
    }

    private String getMsg(String str) {
        try {
            return rsrc.getString(str);
        } catch (MissingResourceException e2) {
            throw new Error("Error in message file");
        }
    }

    private String formatMsg(String str, String str2) {
        return MessageFormat.format(getMsg(str), str2);
    }

    private String formatMsg2(String str, String str2, String str3) {
        return MessageFormat.format(getMsg(str), str2, str3);
    }

    public Main(PrintStream printStream, PrintStream printStream2, String str) {
        this.out = printStream;
        this.err = printStream2;
        this.program = str;
    }

    private static File createTempFileInSameDirectoryAs(File file) throws IOException {
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            parentFile = new File(".");
        }
        return File.createTempFile("jartmp", null, parentFile);
    }

    /* JADX WARN: Finally extract failed */
    public synchronized boolean run(String[] strArr) {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2;
        this.ok = true;
        if (!parseArgs(strArr)) {
            return false;
        }
        try {
            if ((this.cflag || this.uflag) && this.fname != null) {
                this.zname = this.fname.replace(File.separatorChar, '/');
                if (this.zname.startsWith("./")) {
                    this.zname = this.zname.substring(2);
                }
            }
            if (this.cflag) {
                java.util.jar.Manifest manifest = null;
                FileInputStream fileInputStream2 = null;
                if (!this.Mflag) {
                    if (this.mname != null) {
                        fileInputStream2 = new FileInputStream(this.mname);
                        manifest = new java.util.jar.Manifest(new BufferedInputStream(fileInputStream2));
                    } else {
                        manifest = new java.util.jar.Manifest();
                    }
                    addVersion(manifest);
                    addCreatedBy(manifest);
                    if (isAmbiguousMainClass(manifest)) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                            return false;
                        }
                        return false;
                    }
                    if (this.ename != null) {
                        addMainClass(manifest, this.ename);
                    }
                }
                expand(null, this.files, false);
                if (this.fname != null) {
                    fileOutputStream2 = new FileOutputStream(this.fname);
                } else {
                    fileOutputStream2 = new FileOutputStream(FileDescriptor.out);
                    if (this.vflag) {
                        this.vflag = false;
                    }
                }
                File fileCreateTemporaryFile = null;
                FileOutputStream fileOutputStream3 = fileOutputStream2;
                String strSubstring = this.fname == null ? "tmpjar" : this.fname.substring(this.fname.indexOf(File.separatorChar) + 1);
                if (this.nflag) {
                    fileCreateTemporaryFile = createTemporaryFile(strSubstring, ".jar");
                    fileOutputStream2 = new FileOutputStream(fileCreateTemporaryFile);
                }
                create(new BufferedOutputStream(fileOutputStream2, 4096), manifest);
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                fileOutputStream2.close();
                if (this.nflag) {
                    JarFile jarFile = null;
                    File fileCreateTemporaryFile2 = null;
                    JarOutputStream jarOutputStream = null;
                    try {
                        try {
                            Pack200.Packer packerNewPacker = Pack200.newPacker();
                            packerNewPacker.properties().put(Pack200.Packer.EFFORT, "1");
                            jarFile = new JarFile(fileCreateTemporaryFile.getCanonicalPath());
                            fileCreateTemporaryFile2 = createTemporaryFile(strSubstring, ".pack");
                            fileOutputStream2 = new FileOutputStream(fileCreateTemporaryFile2);
                            packerNewPacker.pack(jarFile, fileOutputStream2);
                            jarOutputStream = new JarOutputStream(fileOutputStream3);
                            Pack200.newUnpacker().unpack(fileCreateTemporaryFile2, jarOutputStream);
                            if (jarFile != null) {
                                jarFile.close();
                            }
                            if (fileOutputStream2 != null) {
                                fileOutputStream2.close();
                            }
                            if (jarOutputStream != null) {
                                jarOutputStream.close();
                            }
                            if (fileCreateTemporaryFile != null && fileCreateTemporaryFile.exists()) {
                                fileCreateTemporaryFile.delete();
                            }
                            if (fileCreateTemporaryFile2 != null && fileCreateTemporaryFile2.exists()) {
                                fileCreateTemporaryFile2.delete();
                            }
                        } catch (IOException e2) {
                            fatalError(e2);
                            if (jarFile != null) {
                                jarFile.close();
                            }
                            if (fileOutputStream2 != null) {
                                fileOutputStream2.close();
                            }
                            if (jarOutputStream != null) {
                                jarOutputStream.close();
                            }
                            if (fileCreateTemporaryFile != null && fileCreateTemporaryFile.exists()) {
                                fileCreateTemporaryFile.delete();
                            }
                            if (fileCreateTemporaryFile2 != null && fileCreateTemporaryFile2.exists()) {
                                fileCreateTemporaryFile2.delete();
                            }
                        }
                    } catch (Throwable th) {
                        if (jarFile != null) {
                            jarFile.close();
                        }
                        if (fileOutputStream2 != null) {
                            fileOutputStream2.close();
                        }
                        if (jarOutputStream != null) {
                            jarOutputStream.close();
                        }
                        if (fileCreateTemporaryFile != null && fileCreateTemporaryFile.exists()) {
                            fileCreateTemporaryFile.delete();
                        }
                        if (fileCreateTemporaryFile2 != null && fileCreateTemporaryFile2.exists()) {
                            fileCreateTemporaryFile2.delete();
                        }
                        throw th;
                    }
                }
            } else if (this.uflag) {
                File file = null;
                File fileCreateTempFileInSameDirectoryAs = null;
                if (this.fname != null) {
                    file = new File(this.fname);
                    fileCreateTempFileInSameDirectoryAs = createTempFileInSameDirectoryAs(file);
                    fileInputStream = new FileInputStream(file);
                    fileOutputStream = new FileOutputStream(fileCreateTempFileInSameDirectoryAs);
                } else {
                    fileInputStream = new FileInputStream(FileDescriptor.in);
                    fileOutputStream = new FileOutputStream(FileDescriptor.out);
                    this.vflag = false;
                }
                FileInputStream fileInputStream3 = (this.Mflag || this.mname == null) ? null : new FileInputStream(this.mname);
                expand(null, this.files, true);
                boolean zUpdate = update(fileInputStream, new BufferedOutputStream(fileOutputStream), fileInputStream3, null);
                if (this.ok) {
                    this.ok = zUpdate;
                }
                fileInputStream.close();
                fileOutputStream.close();
                if (fileInputStream3 != null) {
                    fileInputStream3.close();
                }
                if (this.ok && this.fname != null) {
                    file.delete();
                    if (!fileCreateTempFileInSameDirectoryAs.renameTo(file)) {
                        fileCreateTempFileInSameDirectoryAs.delete();
                        throw new IOException(getMsg("error.write.file"));
                    }
                    fileCreateTempFileInSameDirectoryAs.delete();
                }
            } else if (this.tflag) {
                replaceFSC(this.files);
                if (this.fname != null) {
                    list(this.fname, this.files);
                } else {
                    FileInputStream fileInputStream4 = new FileInputStream(FileDescriptor.in);
                    try {
                        list(new BufferedInputStream(fileInputStream4), this.files);
                        fileInputStream4.close();
                    } catch (Throwable th2) {
                        fileInputStream4.close();
                        throw th2;
                    }
                }
            } else if (this.xflag) {
                replaceFSC(this.files);
                if (this.fname != null && this.files != null) {
                    extract(this.fname, this.files);
                } else {
                    FileInputStream fileInputStream5 = this.fname == null ? new FileInputStream(FileDescriptor.in) : new FileInputStream(this.fname);
                    try {
                        extract(new BufferedInputStream(fileInputStream5), this.files);
                        fileInputStream5.close();
                    } catch (Throwable th3) {
                        fileInputStream5.close();
                        throw th3;
                    }
                }
            } else if (this.iflag) {
                genIndex(this.rootjar, this.files);
            }
        } catch (IOException e3) {
            fatalError(e3);
            this.ok = false;
        } catch (Error e4) {
            e4.printStackTrace();
            this.ok = false;
        } catch (Throwable th4) {
            th4.printStackTrace();
            this.ok = false;
        }
        this.out.flush();
        this.err.flush();
        return this.ok;
    }

    boolean parseArgs(String[] strArr) {
        try {
            String[] strArr2 = CommandLine.parse(strArr);
            int i2 = 1;
            try {
                String strSubstring = strArr2[0];
                if (strSubstring.startsWith(LanguageTag.SEP)) {
                    strSubstring = strSubstring.substring(1);
                }
                for (int i3 = 0; i3 < strSubstring.length(); i3++) {
                    switch (strSubstring.charAt(i3)) {
                        case '0':
                            this.flag0 = true;
                            break;
                        case 'M':
                            this.Mflag = true;
                            break;
                        case 'P':
                            this.pflag = true;
                            break;
                        case 'c':
                            if (this.xflag || this.tflag || this.uflag || this.iflag) {
                                usageError();
                                return false;
                            }
                            this.cflag = true;
                            break;
                            break;
                        case 'e':
                            int i4 = i2;
                            i2++;
                            this.ename = strArr2[i4];
                            break;
                        case 'f':
                            int i5 = i2;
                            i2++;
                            this.fname = strArr2[i5];
                            break;
                        case 'i':
                            if (this.cflag || this.uflag || this.xflag || this.tflag) {
                                usageError();
                                return false;
                            }
                            int i6 = i2;
                            i2++;
                            this.rootjar = strArr2[i6];
                            this.iflag = true;
                            break;
                        case 'm':
                            int i7 = i2;
                            i2++;
                            this.mname = strArr2[i7];
                            break;
                        case 'n':
                            this.nflag = true;
                            break;
                        case 't':
                            if (this.cflag || this.uflag || this.xflag || this.iflag) {
                                usageError();
                                return false;
                            }
                            this.tflag = true;
                            break;
                            break;
                        case 'u':
                            if (this.cflag || this.xflag || this.tflag || this.iflag) {
                                usageError();
                                return false;
                            }
                            this.uflag = true;
                            break;
                        case 'v':
                            this.vflag = true;
                            break;
                        case 'x':
                            if (this.cflag || this.uflag || this.tflag || this.iflag) {
                                usageError();
                                return false;
                            }
                            this.xflag = true;
                            break;
                            break;
                        default:
                            error(formatMsg("error.illegal.option", String.valueOf(strSubstring.charAt(i3))));
                            usageError();
                            return false;
                    }
                }
                if (!this.cflag && !this.tflag && !this.xflag && !this.uflag && !this.iflag) {
                    error(getMsg("error.bad.option"));
                    usageError();
                    return false;
                }
                int length = strArr2.length - i2;
                if (length <= 0) {
                    if (this.cflag && this.mname == null) {
                        error(getMsg("error.bad.cflag"));
                        usageError();
                        return false;
                    }
                    if (!this.uflag || this.mname != null || this.ename != null) {
                        return true;
                    }
                    error(getMsg("error.bad.uflag"));
                    usageError();
                    return false;
                }
                int i8 = 0;
                String[] strArr3 = new String[length];
                int i9 = i2;
                while (i9 < strArr2.length) {
                    try {
                        if (strArr2[i9].equals("-C")) {
                            int i10 = i9 + 1;
                            String str = strArr2[i10];
                            String strReplace = (str.endsWith(File.separator) ? str : str + File.separator).replace(File.separatorChar, '/');
                            while (strReplace.indexOf("//") > -1) {
                                strReplace = strReplace.replace("//", "/");
                            }
                            this.paths.add(strReplace.replace(File.separatorChar, '/'));
                            int i11 = i8;
                            i8++;
                            i9 = i10 + 1;
                            strArr3[i11] = strReplace + strArr2[i9];
                        } else {
                            int i12 = i8;
                            i8++;
                            strArr3[i12] = strArr2[i9];
                        }
                        i9++;
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        usageError();
                        return false;
                    }
                }
                this.files = new String[i8];
                System.arraycopy(strArr3, 0, this.files, 0, i8);
                return true;
            } catch (ArrayIndexOutOfBoundsException e3) {
                usageError();
                return false;
            }
        } catch (FileNotFoundException e4) {
            fatalError(formatMsg("error.cant.open", e4.getMessage()));
            return false;
        } catch (IOException e5) {
            fatalError(e5);
            return false;
        }
    }

    void expand(File file, String[] strArr, boolean z2) {
        File file2;
        if (strArr == null) {
            return;
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (file == null) {
                file2 = new File(strArr[i2]);
            } else {
                file2 = new File(file, strArr[i2]);
            }
            if (file2.isFile()) {
                if (this.entries.add(file2) && z2) {
                    this.entryMap.put(entryName(file2.getPath()), file2);
                }
            } else if (file2.isDirectory()) {
                if (this.entries.add(file2)) {
                    if (z2) {
                        String path = file2.getPath();
                        this.entryMap.put(entryName(path.endsWith(File.separator) ? path : path + File.separator), file2);
                    }
                    expand(file2, file2.list(), z2);
                }
            } else {
                error(formatMsg("error.nosuch.fileordir", String.valueOf(file2)));
                this.ok = false;
            }
        }
    }

    void create(OutputStream outputStream, java.util.jar.Manifest manifest) throws IOException {
        JarOutputStream jarOutputStream = new JarOutputStream(outputStream);
        if (this.flag0) {
            jarOutputStream.setMethod(0);
        }
        if (manifest != null) {
            if (this.vflag) {
                output(getMsg("out.added.manifest"));
            }
            ZipEntry zipEntry = new ZipEntry(MANIFEST_DIR);
            zipEntry.setTime(System.currentTimeMillis());
            zipEntry.setSize(0L);
            zipEntry.setCrc(0L);
            jarOutputStream.putNextEntry(zipEntry);
            ZipEntry zipEntry2 = new ZipEntry(JarFile.MANIFEST_NAME);
            zipEntry2.setTime(System.currentTimeMillis());
            if (this.flag0) {
                crc32Manifest(zipEntry2, manifest);
            }
            jarOutputStream.putNextEntry(zipEntry2);
            manifest.write(jarOutputStream);
            jarOutputStream.closeEntry();
        }
        Iterator<File> it = this.entries.iterator();
        while (it.hasNext()) {
            addFile(jarOutputStream, it.next());
        }
        jarOutputStream.close();
    }

    private char toUpperCaseASCII(char c2) {
        return (c2 < 'a' || c2 > 'z') ? c2 : (char) ((c2 + 'A') - 97);
    }

    private boolean equalsIgnoreCase(String str, String str2) {
        if (!$assertionsDisabled && !str2.toUpperCase(Locale.ENGLISH).equals(str2)) {
            throw new AssertionError();
        }
        int length = str.length();
        if (length != str2.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            char cCharAt2 = str2.charAt(i2);
            if (cCharAt != cCharAt2 && toUpperCaseASCII(cCharAt) != cCharAt2) {
                return false;
            }
        }
        return true;
    }

    boolean update(InputStream inputStream, OutputStream outputStream, InputStream inputStream2, JarIndex jarIndex) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipOutputStream jarOutputStream = new JarOutputStream(outputStream);
        boolean z2 = false;
        boolean z3 = true;
        if (jarIndex != null) {
            addIndex(jarIndex, jarOutputStream);
        }
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                String name = nextEntry.getName();
                boolean zEqualsIgnoreCase = equalsIgnoreCase(name, JarFile.MANIFEST_NAME);
                if (jarIndex == null || !equalsIgnoreCase(name, JarIndex.INDEX_NAME)) {
                    if (!this.Mflag || !zEqualsIgnoreCase) {
                        if (zEqualsIgnoreCase && (inputStream2 != null || this.ename != null)) {
                            z2 = true;
                            if (inputStream2 != null) {
                                FileInputStream fileInputStream = new FileInputStream(this.mname);
                                boolean zIsAmbiguousMainClass = isAmbiguousMainClass(new java.util.jar.Manifest(fileInputStream));
                                fileInputStream.close();
                                if (zIsAmbiguousMainClass) {
                                    return false;
                                }
                            }
                            java.util.jar.Manifest manifest = new java.util.jar.Manifest(zipInputStream);
                            if (inputStream2 != null) {
                                manifest.read(inputStream2);
                            }
                            if (!updateManifest(manifest, jarOutputStream)) {
                                return false;
                            }
                        } else if (!this.entryMap.containsKey(name)) {
                            ZipEntry zipEntry = new ZipEntry(name);
                            zipEntry.setMethod(nextEntry.getMethod());
                            zipEntry.setTime(nextEntry.getTime());
                            zipEntry.setComment(nextEntry.getComment());
                            zipEntry.setExtra(nextEntry.getExtra());
                            if (nextEntry.getMethod() == 0) {
                                zipEntry.setSize(nextEntry.getSize());
                                zipEntry.setCrc(nextEntry.getCrc());
                            }
                            jarOutputStream.putNextEntry(zipEntry);
                            copy(zipInputStream, jarOutputStream);
                        } else {
                            File file = this.entryMap.get(name);
                            addFile(jarOutputStream, file);
                            this.entryMap.remove(name);
                            this.entries.remove(file);
                        }
                    }
                }
            } else {
                Iterator<File> it = this.entries.iterator();
                while (it.hasNext()) {
                    addFile(jarOutputStream, it.next());
                }
                if (!z2) {
                    if (inputStream2 != null) {
                        java.util.jar.Manifest manifest2 = new java.util.jar.Manifest(inputStream2);
                        z3 = !isAmbiguousMainClass(manifest2);
                        if (z3 && !updateManifest(manifest2, jarOutputStream)) {
                            z3 = false;
                        }
                    } else if (this.ename != null && !updateManifest(new java.util.jar.Manifest(), jarOutputStream)) {
                        z3 = false;
                    }
                }
                zipInputStream.close();
                jarOutputStream.close();
                return z3;
            }
        }
    }

    private void addIndex(JarIndex jarIndex, ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(JarIndex.INDEX_NAME);
        zipEntry.setTime(System.currentTimeMillis());
        if (this.flag0) {
            CRC32OutputStream cRC32OutputStream = new CRC32OutputStream();
            jarIndex.write(cRC32OutputStream);
            cRC32OutputStream.updateEntry(zipEntry);
        }
        zipOutputStream.putNextEntry(zipEntry);
        jarIndex.write(zipOutputStream);
        zipOutputStream.closeEntry();
    }

    private boolean updateManifest(java.util.jar.Manifest manifest, ZipOutputStream zipOutputStream) throws IOException {
        addVersion(manifest);
        addCreatedBy(manifest);
        if (this.ename != null) {
            addMainClass(manifest, this.ename);
        }
        ZipEntry zipEntry = new ZipEntry(JarFile.MANIFEST_NAME);
        zipEntry.setTime(System.currentTimeMillis());
        if (this.flag0) {
            crc32Manifest(zipEntry, manifest);
        }
        zipOutputStream.putNextEntry(zipEntry);
        manifest.write(zipOutputStream);
        if (this.vflag) {
            output(getMsg("out.update.manifest"));
            return true;
        }
        return true;
    }

    private static final boolean isWinDriveLetter(char c2) {
        return (c2 >= 'a' && c2 <= 'z') || (c2 >= 'A' && c2 <= 'Z');
    }

    private String safeName(String str) {
        int i2;
        if (!this.pflag) {
            int length = str.length();
            int iLastIndexOf = str.lastIndexOf("../");
            if (iLastIndexOf == -1) {
                i2 = 0;
            } else {
                i2 = iLastIndexOf + 3;
            }
            if (File.separatorChar == '\\') {
                while (i2 < length) {
                    int i3 = i2;
                    if (i2 + 1 < length && str.charAt(i2 + 1) == ':' && isWinDriveLetter(str.charAt(i2))) {
                        i2 += 2;
                    }
                    while (i2 < length && str.charAt(i2) == '/') {
                        i2++;
                    }
                    if (i2 == i3) {
                        break;
                    }
                }
            } else {
                while (i2 < length && str.charAt(i2) == '/') {
                    i2++;
                }
            }
            if (i2 != 0) {
                str = str.substring(i2);
            }
        }
        return str;
    }

    private String entryName(String str) {
        String strReplace = str.replace(File.separatorChar, '/');
        String str2 = "";
        for (String str3 : this.paths) {
            if (strReplace.startsWith(str3) && str3.length() > str2.length()) {
                str2 = str3;
            }
        }
        String strSafeName = safeName(strReplace.substring(str2.length()));
        if (strSafeName.startsWith("./")) {
            strSafeName = strSafeName.substring(2);
        }
        return strSafeName;
    }

    private void addVersion(java.util.jar.Manifest manifest) {
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes.getValue(Attributes.Name.MANIFEST_VERSION) == null) {
            mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        }
    }

    private void addCreatedBy(java.util.jar.Manifest manifest) {
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes.getValue(new Attributes.Name("Created-By")) == null) {
            mainAttributes.put(new Attributes.Name("Created-By"), System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")");
        }
    }

    private void addMainClass(java.util.jar.Manifest manifest, String str) {
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, str);
    }

    private boolean isAmbiguousMainClass(java.util.jar.Manifest manifest) {
        if (this.ename != null && manifest.getMainAttributes().get(Attributes.Name.MAIN_CLASS) != null) {
            error(getMsg("error.bad.eflag"));
            usageError();
            return true;
        }
        return false;
    }

    void addFile(ZipOutputStream zipOutputStream, File file) throws IOException {
        String path = file.getPath();
        boolean zIsDirectory = file.isDirectory();
        if (zIsDirectory) {
            path = path.endsWith(File.separator) ? path : path + File.separator;
        }
        String strEntryName = entryName(path);
        if (strEntryName.equals("") || strEntryName.equals(".") || strEntryName.equals(this.zname)) {
            return;
        }
        if ((strEntryName.equals(MANIFEST_DIR) || strEntryName.equals(JarFile.MANIFEST_NAME)) && !this.Mflag) {
            if (this.vflag) {
                output(formatMsg("out.ignore.entry", strEntryName));
                return;
            }
            return;
        }
        long length = zIsDirectory ? 0L : file.length();
        if (this.vflag) {
            this.out.print(formatMsg("out.adding", strEntryName));
        }
        ZipEntry zipEntry = new ZipEntry(strEntryName);
        zipEntry.setTime(file.lastModified());
        if (length == 0) {
            zipEntry.setMethod(0);
            zipEntry.setSize(0L);
            zipEntry.setCrc(0L);
        } else if (this.flag0) {
            crc32File(zipEntry, file);
        }
        zipOutputStream.putNextEntry(zipEntry);
        if (!zIsDirectory) {
            copy(file, zipOutputStream);
        }
        zipOutputStream.closeEntry();
        if (this.vflag) {
            long size = zipEntry.getSize();
            long compressedSize = zipEntry.getCompressedSize();
            this.out.print(formatMsg2("out.size", String.valueOf(size), String.valueOf(compressedSize)));
            if (zipEntry.getMethod() == 8) {
                long j2 = 0;
                if (size != 0) {
                    j2 = ((size - compressedSize) * 100) / size;
                }
                output(formatMsg("out.deflated", String.valueOf(j2)));
                return;
            }
            output(getMsg("out.stored"));
        }
    }

    private void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        while (true) {
            int i2 = inputStream.read(this.copyBuf);
            if (i2 != -1) {
                outputStream.write(this.copyBuf, 0, i2);
            } else {
                return;
            }
        }
    }

    private void copy(File file, OutputStream outputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            copy(fileInputStream, outputStream);
            fileInputStream.close();
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    private void copy(InputStream inputStream, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            copy(inputStream, fileOutputStream);
            fileOutputStream.close();
        } catch (Throwable th) {
            fileOutputStream.close();
            throw th;
        }
    }

    private void crc32Manifest(ZipEntry zipEntry, java.util.jar.Manifest manifest) throws IOException {
        CRC32OutputStream cRC32OutputStream = new CRC32OutputStream();
        manifest.write(cRC32OutputStream);
        cRC32OutputStream.updateEntry(zipEntry);
    }

    private void crc32File(ZipEntry zipEntry, File file) throws IOException {
        CRC32OutputStream cRC32OutputStream = new CRC32OutputStream();
        copy(file, cRC32OutputStream);
        if (cRC32OutputStream.f13681n != file.length()) {
            throw new JarException(formatMsg("error.incorrect.length", file.getPath()));
        }
        cRC32OutputStream.updateEntry(zipEntry);
    }

    void replaceFSC(String[] strArr) {
        if (strArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                strArr[i2] = strArr[i2].replace(File.separatorChar, '/');
            }
        }
    }

    Set<ZipEntry> newDirSet() {
        return new HashSet<ZipEntry>() { // from class: sun.tools.jar.Main.1
            @Override // java.util.HashSet, java.util.AbstractCollection, java.util.Collection, java.util.List
            public boolean add(ZipEntry zipEntry) {
                if (zipEntry == null || Main.useExtractionTime) {
                    return false;
                }
                return super.add((AnonymousClass1) zipEntry);
            }
        };
    }

    void updateLastModifiedTime(Set<ZipEntry> set) throws IOException {
        for (ZipEntry zipEntry : set) {
            long time = zipEntry.getTime();
            if (time != -1) {
                String strSafeName = safeName(zipEntry.getName().replace(File.separatorChar, '/'));
                if (strSafeName.length() != 0) {
                    new File(strSafeName.replace('/', File.separatorChar)).setLastModified(time);
                }
            }
        }
    }

    void extract(InputStream inputStream, String[] strArr) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Set<ZipEntry> setNewDirSet = newDirSet();
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                if (strArr == null) {
                    setNewDirSet.add(extractFile(zipInputStream, nextEntry));
                } else {
                    String name = nextEntry.getName();
                    int length = strArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        if (!name.startsWith(strArr[i2])) {
                            i2++;
                        } else {
                            setNewDirSet.add(extractFile(zipInputStream, nextEntry));
                            break;
                        }
                    }
                }
            } else {
                updateLastModifiedTime(setNewDirSet);
                return;
            }
        }
    }

    void extract(String str, String[] strArr) throws IOException {
        ZipFile zipFile = new ZipFile(str);
        Set<ZipEntry> setNewDirSet = newDirSet();
        Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
        while (enumerationEntries.hasMoreElements()) {
            ZipEntry zipEntryNextElement2 = enumerationEntries.nextElement2();
            if (strArr == null) {
                setNewDirSet.add(extractFile(zipFile.getInputStream(zipEntryNextElement2), zipEntryNextElement2));
            } else {
                String name = zipEntryNextElement2.getName();
                int length = strArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    if (!name.startsWith(strArr[i2])) {
                        i2++;
                    } else {
                        setNewDirSet.add(extractFile(zipFile.getInputStream(zipEntryNextElement2), zipEntryNextElement2));
                        break;
                    }
                }
            }
        }
        zipFile.close();
        updateLastModifiedTime(setNewDirSet);
    }

    ZipEntry extractFile(InputStream inputStream, ZipEntry zipEntry) throws IOException {
        ZipEntry zipEntry2 = null;
        String strSafeName = safeName(zipEntry.getName().replace(File.separatorChar, '/'));
        if (strSafeName.length() == 0) {
            return null;
        }
        File file = new File(strSafeName.replace('/', File.separatorChar));
        if (zipEntry.isDirectory()) {
            if (file.exists()) {
                if (!file.isDirectory()) {
                    throw new IOException(formatMsg("error.create.dir", file.getPath()));
                }
            } else {
                if (!file.mkdirs()) {
                    throw new IOException(formatMsg("error.create.dir", file.getPath()));
                }
                zipEntry2 = zipEntry;
            }
            if (this.vflag) {
                output(formatMsg("out.create", strSafeName));
            }
        } else {
            if (file.getParent() != null) {
                File file2 = new File(file.getParent());
                if ((!file2.exists() && !file2.mkdirs()) || !file2.isDirectory()) {
                    throw new IOException(formatMsg("error.create.dir", file2.getPath()));
                }
            }
            try {
                copy(inputStream, file);
                if (inputStream instanceof ZipInputStream) {
                    ((ZipInputStream) inputStream).closeEntry();
                } else {
                    inputStream.close();
                }
                if (this.vflag) {
                    if (zipEntry.getMethod() == 8) {
                        output(formatMsg("out.inflated", strSafeName));
                    } else {
                        output(formatMsg("out.extracted", strSafeName));
                    }
                }
            } catch (Throwable th) {
                if (inputStream instanceof ZipInputStream) {
                    ((ZipInputStream) inputStream).closeEntry();
                } else {
                    inputStream.close();
                }
                throw th;
            }
        }
        if (!useExtractionTime) {
            long time = zipEntry.getTime();
            if (time != -1) {
                file.setLastModified(time);
            }
        }
        return zipEntry2;
    }

    void list(InputStream inputStream, String[] strArr) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                zipInputStream.closeEntry();
                printEntry(nextEntry, strArr);
            } else {
                return;
            }
        }
    }

    void list(String str, String[] strArr) throws IOException {
        ZipFile zipFile = new ZipFile(str);
        Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
        while (enumerationEntries.hasMoreElements()) {
            printEntry(enumerationEntries.nextElement2(), strArr);
        }
        zipFile.close();
    }

    void dumpIndex(String str, JarIndex jarIndex) throws IOException {
        File file = new File(str);
        Path path = file.toPath();
        Path path2 = createTempFileInSameDirectoryAs(file).toPath();
        try {
            if (update(Files.newInputStream(path, new OpenOption[0]), Files.newOutputStream(path2, new OpenOption[0]), null, jarIndex)) {
                try {
                    Files.move(path2, path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e2) {
                    throw new IOException(getMsg("error.write.file"), e2);
                }
            }
        } finally {
            Files.deleteIfExists(path2);
        }
    }

    List<String> getJarPath(String str) throws IOException {
        java.util.jar.Manifest manifest;
        Attributes mainAttributes;
        String value;
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        this.jarPaths.add(str);
        String strSubstring = str.substring(0, Math.max(0, str.lastIndexOf(47) + 1));
        JarFile jarFile = new JarFile(str.replace('/', File.separatorChar));
        if (jarFile != null && (manifest = jarFile.getManifest()) != null && (mainAttributes = manifest.getMainAttributes()) != null && (value = mainAttributes.getValue(Attributes.Name.CLASS_PATH)) != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(value);
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                if (!strNextToken.endsWith("/")) {
                    String strConcat = strSubstring.concat(strNextToken);
                    if (!this.jarPaths.contains(strConcat)) {
                        arrayList.addAll(getJarPath(strConcat));
                    }
                }
            }
        }
        jarFile.close();
        return arrayList;
    }

    void genIndex(String str, String[] strArr) throws IOException {
        List<String> jarPath = getJarPath(str);
        int size = jarPath.size();
        if (size == 1 && strArr != null) {
            for (String str2 : strArr) {
                jarPath.addAll(getJarPath(str2));
            }
            size = jarPath.size();
        }
        dumpIndex(str, new JarIndex((String[]) jarPath.toArray(new String[size])));
    }

    void printEntry(ZipEntry zipEntry, String[] strArr) throws IOException {
        if (strArr == null) {
            printEntry(zipEntry);
            return;
        }
        String name = zipEntry.getName();
        for (String str : strArr) {
            if (name.startsWith(str)) {
                printEntry(zipEntry);
                return;
            }
        }
    }

    void printEntry(ZipEntry zipEntry) throws IOException {
        if (this.vflag) {
            StringBuilder sb = new StringBuilder();
            String string = Long.toString(zipEntry.getSize());
            for (int length = 6 - string.length(); length > 0; length--) {
                sb.append(' ');
            }
            sb.append(string).append(' ').append(new Date(zipEntry.getTime()).toString());
            sb.append(' ').append(zipEntry.getName());
            output(sb.toString());
            return;
        }
        output(zipEntry.getName());
    }

    void usageError() {
        error(getMsg("usage"));
    }

    void fatalError(Exception exc) {
        exc.printStackTrace();
    }

    void fatalError(String str) {
        error(this.program + ": " + str);
    }

    protected void output(String str) {
        this.out.println(str);
    }

    protected void error(String str) {
        this.err.println(str);
    }

    public static void main(String[] strArr) {
        System.exit(new Main(System.out, System.err, "jar").run(strArr) ? 0 : 1);
    }

    /* loaded from: rt.jar:sun/tools/jar/Main$CRC32OutputStream.class */
    private static class CRC32OutputStream extends OutputStream {
        final CRC32 crc = new CRC32();

        /* renamed from: n, reason: collision with root package name */
        long f13681n = 0;

        CRC32OutputStream() {
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            this.crc.update(i2);
            this.f13681n++;
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            this.crc.update(bArr, i2, i3);
            this.f13681n += i3;
        }

        public void updateEntry(ZipEntry zipEntry) {
            zipEntry.setMethod(0);
            zipEntry.setSize(this.f13681n);
            zipEntry.setCrc(this.crc.getValue());
        }
    }

    private File createTemporaryFile(String str, String str2) {
        File fileCreateTempFile = null;
        try {
            fileCreateTempFile = File.createTempFile(str, str2);
        } catch (IOException | SecurityException e2) {
        }
        if (fileCreateTempFile == null) {
            if (this.fname != null) {
                try {
                    fileCreateTempFile = File.createTempFile(this.fname, ".tmp" + str2, new File(this.fname).getAbsoluteFile().getParentFile());
                } catch (IOException e3) {
                    fatalError(e3);
                }
            } else {
                fatalError(new IOException(getMsg("error.create.tempfile")));
            }
        }
        return fileCreateTempFile;
    }
}
