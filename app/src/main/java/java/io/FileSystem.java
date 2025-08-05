package java.io;

/* loaded from: rt.jar:java/io/FileSystem.class */
abstract class FileSystem {
    public static final int BA_EXISTS = 1;
    public static final int BA_REGULAR = 2;
    public static final int BA_DIRECTORY = 4;
    public static final int BA_HIDDEN = 8;
    public static final int ACCESS_READ = 4;
    public static final int ACCESS_WRITE = 2;
    public static final int ACCESS_EXECUTE = 1;
    public static final int SPACE_TOTAL = 0;
    public static final int SPACE_FREE = 1;
    public static final int SPACE_USABLE = 2;
    static boolean useCanonCaches;
    static boolean useCanonPrefixCache;

    public abstract char getSeparator();

    public abstract char getPathSeparator();

    public abstract String normalize(String str);

    public abstract int prefixLength(String str);

    public abstract String resolve(String str, String str2);

    public abstract String getDefaultParent();

    public abstract String fromURIPath(String str);

    public abstract boolean isAbsolute(File file);

    public abstract boolean isInvalid(File file);

    public abstract String resolve(File file);

    public abstract String canonicalize(String str) throws IOException;

    public abstract int getBooleanAttributes(File file);

    public abstract boolean checkAccess(File file, int i2);

    public abstract boolean setPermission(File file, int i2, boolean z2, boolean z3);

    public abstract long getLastModifiedTime(File file);

    public abstract long getLength(File file);

    public abstract boolean createFileExclusively(String str) throws IOException;

    public abstract boolean delete(File file);

    public abstract String[] list(File file);

    public abstract boolean createDirectory(File file);

    public abstract boolean rename(File file, File file2);

    public abstract boolean setLastModifiedTime(File file, long j2);

    public abstract boolean setReadOnly(File file);

    public abstract File[] listRoots();

    public abstract long getSpace(File file, int i2);

    public abstract int compare(File file, File file2);

    public abstract int hashCode(File file);

    FileSystem() {
    }

    static {
        useCanonCaches = true;
        useCanonPrefixCache = true;
        useCanonCaches = getBooleanProperty("sun.io.useCanonCaches", useCanonCaches);
        useCanonPrefixCache = getBooleanProperty("sun.io.useCanonPrefixCache", useCanonPrefixCache);
    }

    private static boolean getBooleanProperty(String str, boolean z2) {
        String property = System.getProperty(str);
        if (property == null) {
            return z2;
        }
        if (property.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }
}
