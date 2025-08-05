package java.io;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.SecureRandom;
import java.util.ArrayList;
import sun.misc.Unsafe;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/io/File.class */
public class File implements Serializable, Comparable<File> {
    private static final FileSystem fs;
    private final String path;
    private transient PathStatus status = null;
    private final transient int prefixLength;
    public static final char separatorChar;
    public static final String separator;
    public static final char pathSeparatorChar;
    public static final String pathSeparator;
    private static final long PATH_OFFSET;
    private static final long PREFIX_LENGTH_OFFSET;
    private static final Unsafe UNSAFE;
    private static final long serialVersionUID = 301077366599181567L;
    private volatile transient Path filePath;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:java/io/File$PathStatus.class */
    private enum PathStatus {
        INVALID,
        CHECKED
    }

    static {
        $assertionsDisabled = !File.class.desiredAssertionStatus();
        fs = DefaultFileSystem.getFileSystem();
        separatorChar = fs.getSeparator();
        separator = "" + separatorChar;
        pathSeparatorChar = fs.getPathSeparator();
        pathSeparator = "" + pathSeparatorChar;
        try {
            Unsafe unsafe = Unsafe.getUnsafe();
            PATH_OFFSET = unsafe.objectFieldOffset(File.class.getDeclaredField("path"));
            PREFIX_LENGTH_OFFSET = unsafe.objectFieldOffset(File.class.getDeclaredField("prefixLength"));
            UNSAFE = unsafe;
        } catch (ReflectiveOperationException e2) {
            throw new Error(e2);
        }
    }

    final boolean isInvalid() {
        PathStatus pathStatus = this.status;
        if (pathStatus == null) {
            pathStatus = fs.isInvalid(this) ? PathStatus.INVALID : PathStatus.CHECKED;
            this.status = pathStatus;
        }
        return pathStatus == PathStatus.INVALID;
    }

    int getPrefixLength() {
        return this.prefixLength;
    }

    private File(String str, int i2) {
        this.path = str;
        this.prefixLength = i2;
    }

    private File(String str, File file) {
        if (!$assertionsDisabled && file.path == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && file.path.equals("")) {
            throw new AssertionError();
        }
        this.path = fs.resolve(file.path, str);
        this.prefixLength = file.prefixLength;
    }

    public File(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.path = fs.normalize(str);
        this.prefixLength = fs.prefixLength(this.path);
    }

    public File(String str, String str2) {
        if (str2 == null) {
            throw new NullPointerException();
        }
        if (str != null) {
            if (str.equals("")) {
                this.path = fs.resolve(fs.getDefaultParent(), fs.normalize(str2));
            } else {
                this.path = fs.resolve(fs.normalize(str), fs.normalize(str2));
            }
        } else {
            this.path = fs.normalize(str2);
        }
        this.prefixLength = fs.prefixLength(this.path);
    }

    public File(File file, String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        if (file != null) {
            if (file.path.equals("")) {
                this.path = fs.resolve(fs.getDefaultParent(), fs.normalize(str));
            } else {
                this.path = fs.resolve(file.path, fs.normalize(str));
            }
        } else {
            this.path = fs.normalize(str);
        }
        this.prefixLength = fs.prefixLength(this.path);
    }

    public File(URI uri) {
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("URI is not absolute");
        }
        if (uri.isOpaque()) {
            throw new IllegalArgumentException("URI is not hierarchical");
        }
        String scheme = uri.getScheme();
        if (scheme == null || !scheme.equalsIgnoreCase(DeploymentDescriptorParser.ATTR_FILE)) {
            throw new IllegalArgumentException("URI scheme is not \"file\"");
        }
        if (uri.getAuthority() != null) {
            throw new IllegalArgumentException("URI has an authority component");
        }
        if (uri.getFragment() != null) {
            throw new IllegalArgumentException("URI has a fragment component");
        }
        if (uri.getQuery() != null) {
            throw new IllegalArgumentException("URI has a query component");
        }
        String path = uri.getPath();
        if (path.equals("")) {
            throw new IllegalArgumentException("URI path component is empty");
        }
        String strFromURIPath = fs.fromURIPath(path);
        this.path = fs.normalize(separatorChar != '/' ? strFromURIPath.replace('/', separatorChar) : strFromURIPath);
        this.prefixLength = fs.prefixLength(this.path);
    }

    public String getName() {
        int iLastIndexOf = this.path.lastIndexOf(separatorChar);
        return iLastIndexOf < this.prefixLength ? this.path.substring(this.prefixLength) : this.path.substring(iLastIndexOf + 1);
    }

    public String getParent() {
        int iLastIndexOf = this.path.lastIndexOf(separatorChar);
        if (iLastIndexOf < this.prefixLength) {
            if (this.prefixLength > 0 && this.path.length() > this.prefixLength) {
                return this.path.substring(0, this.prefixLength);
            }
            return null;
        }
        return this.path.substring(0, iLastIndexOf);
    }

    public File getParentFile() {
        String parent = getParent();
        if (parent == null) {
            return null;
        }
        if (getClass() != File.class) {
            parent = fs.normalize(parent);
        }
        return new File(parent, this.prefixLength);
    }

    public String getPath() {
        return this.path;
    }

    public boolean isAbsolute() {
        return fs.isAbsolute(this);
    }

    public String getAbsolutePath() {
        return fs.resolve(this);
    }

    public File getAbsoluteFile() {
        String absolutePath = getAbsolutePath();
        if (getClass() != File.class) {
            absolutePath = fs.normalize(absolutePath);
        }
        return new File(absolutePath, fs.prefixLength(absolutePath));
    }

    public String getCanonicalPath() throws IOException {
        if (isInvalid()) {
            throw new IOException("Invalid file path");
        }
        return fs.canonicalize(fs.resolve(this));
    }

    public File getCanonicalFile() throws IOException {
        String canonicalPath = getCanonicalPath();
        if (getClass() != File.class) {
            canonicalPath = fs.normalize(canonicalPath);
        }
        return new File(canonicalPath, fs.prefixLength(canonicalPath));
    }

    private static String slashify(String str, boolean z2) {
        String strReplace = str;
        if (separatorChar != '/') {
            strReplace = strReplace.replace(separatorChar, '/');
        }
        if (!strReplace.startsWith("/")) {
            strReplace = "/" + strReplace;
        }
        if (!strReplace.endsWith("/") && z2) {
            strReplace = strReplace + "/";
        }
        return strReplace;
    }

    @Deprecated
    public URL toURL() throws MalformedURLException {
        if (isInvalid()) {
            throw new MalformedURLException("Invalid file path");
        }
        return new URL(DeploymentDescriptorParser.ATTR_FILE, "", slashify(getAbsolutePath(), isDirectory()));
    }

    public URI toURI() {
        try {
            File absoluteFile = getAbsoluteFile();
            String strSlashify = slashify(absoluteFile.getPath(), absoluteFile.isDirectory());
            if (strSlashify.startsWith("//")) {
                strSlashify = "//" + strSlashify;
            }
            return new URI(DeploymentDescriptorParser.ATTR_FILE, null, strSlashify, null);
        } catch (URISyntaxException e2) {
            throw new Error(e2);
        }
    }

    public boolean canRead() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.checkAccess(this, 4);
    }

    public boolean canWrite() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.checkAccess(this, 2);
    }

    public boolean exists() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        return (isInvalid() || (fs.getBooleanAttributes(this) & 1) == 0) ? false : true;
    }

    public boolean isDirectory() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        return (isInvalid() || (fs.getBooleanAttributes(this) & 4) == 0) ? false : true;
    }

    public boolean isFile() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        return (isInvalid() || (fs.getBooleanAttributes(this) & 2) == 0) ? false : true;
    }

    public boolean isHidden() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        return (isInvalid() || (fs.getBooleanAttributes(this) & 8) == 0) ? false : true;
    }

    public long lastModified() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0L;
        }
        return fs.getLastModifiedTime(this);
    }

    public long length() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0L;
        }
        return fs.getLength(this);
    }

    public boolean createNewFile() throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            throw new IOException("Invalid file path");
        }
        return fs.createFileExclusively(this.path);
    }

    public boolean delete() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.delete(this);
    }

    public void deleteOnExit() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(this.path);
        }
        if (isInvalid()) {
            return;
        }
        DeleteOnExitHook.add(this.path);
    }

    public String[] list() {
        return normalizedList();
    }

    private final String[] normalizedList() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return null;
        }
        String[] list = fs.list(this);
        if (list != null && getClass() != File.class) {
            String[] strArr = new String[list.length];
            for (int i2 = 0; i2 < list.length; i2++) {
                strArr[i2] = fs.normalize(list[i2]);
            }
            list = strArr;
        }
        return list;
    }

    public String[] list(FilenameFilter filenameFilter) {
        String[] strArrNormalizedList = normalizedList();
        if (strArrNormalizedList == null || filenameFilter == null) {
            return strArrNormalizedList;
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < strArrNormalizedList.length; i2++) {
            if (filenameFilter.accept(this, strArrNormalizedList[i2])) {
                arrayList.add(strArrNormalizedList[i2]);
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public File[] listFiles() {
        String[] strArrNormalizedList = normalizedList();
        if (strArrNormalizedList == null) {
            return null;
        }
        int length = strArrNormalizedList.length;
        File[] fileArr = new File[length];
        for (int i2 = 0; i2 < length; i2++) {
            fileArr[i2] = new File(strArrNormalizedList[i2], this);
        }
        return fileArr;
    }

    public File[] listFiles(FilenameFilter filenameFilter) {
        String[] strArrNormalizedList = normalizedList();
        if (strArrNormalizedList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : strArrNormalizedList) {
            if (filenameFilter == null || filenameFilter.accept(this, str)) {
                arrayList.add(new File(str, this));
            }
        }
        return (File[]) arrayList.toArray(new File[arrayList.size()]);
    }

    public File[] listFiles(FileFilter fileFilter) {
        String[] strArrNormalizedList = normalizedList();
        if (strArrNormalizedList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : strArrNormalizedList) {
            File file = new File(str, this);
            if (fileFilter == null || fileFilter.accept(file)) {
                arrayList.add(file);
            }
        }
        return (File[]) arrayList.toArray(new File[arrayList.size()]);
    }

    public boolean mkdir() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.createDirectory(this);
    }

    public boolean mkdirs() {
        if (exists()) {
            return false;
        }
        if (mkdir()) {
            return true;
        }
        try {
            File canonicalFile = getCanonicalFile();
            File parentFile = canonicalFile.getParentFile();
            return parentFile != null && (parentFile.mkdirs() || parentFile.exists()) && canonicalFile.mkdir();
        } catch (IOException e2) {
            return false;
        }
    }

    public boolean renameTo(File file) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
            securityManager.checkWrite(file.path);
        }
        if (file == null) {
            throw new NullPointerException();
        }
        if (isInvalid() || file.isInvalid()) {
            return false;
        }
        return fs.rename(this, file);
    }

    public boolean setLastModified(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative time");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.setLastModifiedTime(this, j2);
    }

    public boolean setReadOnly() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.setReadOnly(this);
    }

    public boolean setWritable(boolean z2, boolean z3) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.setPermission(this, 2, z2, z3);
    }

    public boolean setWritable(boolean z2) {
        return setWritable(z2, true);
    }

    public boolean setReadable(boolean z2, boolean z3) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.setPermission(this, 4, z2, z3);
    }

    public boolean setReadable(boolean z2) {
        return setReadable(z2, true);
    }

    public boolean setExecutable(boolean z2, boolean z3) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.setPermission(this, 1, z2, z3);
    }

    public boolean setExecutable(boolean z2) {
        return setExecutable(z2, true);
    }

    public boolean canExecute() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkExec(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return fs.checkAccess(this, 1);
    }

    public static File[] listRoots() {
        return fs.listRoots();
    }

    public long getTotalSpace() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0L;
        }
        return fs.getSpace(this, 0);
    }

    public long getFreeSpace() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0L;
        }
        return fs.getSpace(this, 1);
    }

    public long getUsableSpace() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0L;
        }
        return fs.getSpace(this, 2);
    }

    /* loaded from: rt.jar:java/io/File$TempDirectory.class */
    private static class TempDirectory {
        private static final File tmpdir = new File((String) AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));
        private static final SecureRandom random = new SecureRandom();

        private TempDirectory() {
        }

        static File location() {
            return tmpdir;
        }

        static File generateFile(String str, String str2, File file) throws IOException {
            long jAbs;
            long jNextLong = random.nextLong();
            if (jNextLong == Long.MIN_VALUE) {
                jAbs = 0;
            } else {
                jAbs = Math.abs(jNextLong);
            }
            String str3 = new File(str).getName() + Long.toString(jAbs) + str2;
            File file2 = new File(file, str3);
            if (!str3.equals(file2.getName()) || file2.isInvalid()) {
                if (System.getSecurityManager() != null) {
                    throw new IOException("Unable to create temporary file");
                }
                throw new IOException("Unable to create temporary file, " + ((Object) file2));
            }
            return file2;
        }
    }

    public static File createTempFile(String str, String str2, File file) throws IOException {
        File fileGenerateFile;
        if (str.length() < 3) {
            throw new IllegalArgumentException("Prefix string too short");
        }
        if (str2 == null) {
            str2 = ".tmp";
        }
        File fileLocation = file != null ? file : TempDirectory.location();
        SecurityManager securityManager = System.getSecurityManager();
        do {
            fileGenerateFile = TempDirectory.generateFile(str, str2, fileLocation);
            if (securityManager != null) {
                try {
                    securityManager.checkWrite(fileGenerateFile.getPath());
                } catch (SecurityException e2) {
                    if (file == null) {
                        throw new SecurityException("Unable to create temporary file");
                    }
                    throw e2;
                }
            }
        } while ((fs.getBooleanAttributes(fileGenerateFile) & 1) != 0);
        if (!fs.createFileExclusively(fileGenerateFile.getPath())) {
            throw new IOException("Unable to create temporary file");
        }
        return fileGenerateFile;
    }

    public static File createTempFile(String str, String str2) throws IOException {
        return createTempFile(str, str2, null);
    }

    @Override // java.lang.Comparable
    public int compareTo(File file) {
        return fs.compare(this, file);
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof File) && compareTo((File) obj) == 0;
    }

    public int hashCode() {
        return fs.hashCode(this);
    }

    public String toString() {
        return getPath();
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeChar(separatorChar);
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String strReplace = (String) objectInputStream.readFields().get("path", (Object) null);
        char c2 = objectInputStream.readChar();
        if (c2 != separatorChar) {
            strReplace = strReplace.replace(c2, separatorChar);
        }
        String strNormalize = fs.normalize(strReplace);
        UNSAFE.putObject(this, PATH_OFFSET, strNormalize);
        UNSAFE.putIntVolatile(this, PREFIX_LENGTH_OFFSET, fs.prefixLength(strNormalize));
    }

    public Path toPath() {
        Path path = this.filePath;
        if (path == null) {
            synchronized (this) {
                path = this.filePath;
                if (path == null) {
                    path = FileSystems.getDefault().getPath(this.path, new String[0]);
                    this.filePath = path;
                }
            }
        }
        return path;
    }
}
