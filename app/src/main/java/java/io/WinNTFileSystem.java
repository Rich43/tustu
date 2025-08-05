package java.io;

import java.net.URI;
import java.nio.file.InvalidPathException;
import java.security.AccessController;
import java.util.Locale;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.nio.fs.DefaultFileSystemProvider;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/io/WinNTFileSystem.class */
class WinNTFileSystem extends FileSystem {
    private static final java.nio.file.FileSystem builtInFS = DefaultFileSystemProvider.create().getFileSystem(URI.create("file:///"));
    private final char altSlash;
    private static final boolean ENABLE_ADS;
    private static String[] driveDirCache;
    private ExpiringCache cache = new ExpiringCache();
    private ExpiringCache prefixCache = new ExpiringCache();
    private final char slash = ((String) AccessController.doPrivileged(new GetPropertyAction("file.separator"))).charAt(0);
    private final char semicolon = ((String) AccessController.doPrivileged(new GetPropertyAction("path.separator"))).charAt(0);

    private native String getDriveDirectory(int i2);

    private native String canonicalize0(String str) throws IOException;

    private native String canonicalizeWithPrefix0(String str, String str2) throws IOException;

    @Override // java.io.FileSystem
    public native int getBooleanAttributes(File file);

    @Override // java.io.FileSystem
    public native boolean checkAccess(File file, int i2);

    @Override // java.io.FileSystem
    public native long getLastModifiedTime(File file);

    @Override // java.io.FileSystem
    public native long getLength(File file);

    @Override // java.io.FileSystem
    public native boolean setPermission(File file, int i2, boolean z2, boolean z3);

    @Override // java.io.FileSystem
    public native boolean createFileExclusively(String str) throws IOException;

    @Override // java.io.FileSystem
    public native String[] list(File file);

    @Override // java.io.FileSystem
    public native boolean createDirectory(File file);

    @Override // java.io.FileSystem
    public native boolean setLastModifiedTime(File file, long j2);

    @Override // java.io.FileSystem
    public native boolean setReadOnly(File file);

    private native boolean delete0(File file);

    private native boolean rename0(File file, File file2);

    private static native int listRoots0();

    private native long getSpace0(File file, int i2);

    private static native void initIDs();

    static {
        String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty("jdk.io.File.enableADS");
        if (strPrivilegedGetProperty != null) {
            ENABLE_ADS = !strPrivilegedGetProperty.equalsIgnoreCase(Boolean.FALSE.toString());
        } else {
            ENABLE_ADS = true;
        }
        driveDirCache = new String[26];
        initIDs();
    }

    public WinNTFileSystem() {
        this.altSlash = this.slash == '\\' ? '/' : '\\';
    }

    private boolean isSlash(char c2) {
        return c2 == '\\' || c2 == '/';
    }

    private boolean isLetter(char c2) {
        return (c2 >= 'a' && c2 <= 'z') || (c2 >= 'A' && c2 <= 'Z');
    }

    private String slashify(String str) {
        return (str.length() <= 0 || str.charAt(0) == this.slash) ? str : this.slash + str;
    }

    @Override // java.io.FileSystem
    public char getSeparator() {
        return this.slash;
    }

    @Override // java.io.FileSystem
    public char getPathSeparator() {
        return this.semicolon;
    }

    @Override // java.io.FileSystem
    public String normalize(String str) {
        int length = str.length();
        char c2 = this.slash;
        char c3 = this.altSlash;
        char c4 = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == c3) {
                return normalize(str, length, c4 == c2 ? i2 - 1 : i2);
            }
            if (cCharAt == c2 && c4 == c2 && i2 > 1) {
                return normalize(str, length, i2 - 1);
            }
            if (cCharAt == ':' && i2 > 1) {
                return normalize(str, length, 0);
            }
            c4 = cCharAt;
        }
        return c4 == c2 ? normalize(str, length, length - 1) : str;
    }

    private String normalize(String str, int i2, int i3) {
        int iNormalizePrefix;
        if (i2 == 0) {
            return str;
        }
        if (i3 < 3) {
            i3 = 0;
        }
        char c2 = this.slash;
        StringBuffer stringBuffer = new StringBuffer(i2);
        if (i3 == 0) {
            iNormalizePrefix = normalizePrefix(str, i2, stringBuffer);
        } else {
            iNormalizePrefix = i3;
            stringBuffer.append(str.substring(0, i3));
        }
        while (true) {
            if (iNormalizePrefix >= i2) {
                break;
            }
            int i4 = iNormalizePrefix;
            iNormalizePrefix++;
            char cCharAt = str.charAt(i4);
            if (isSlash(cCharAt)) {
                while (iNormalizePrefix < i2 && isSlash(str.charAt(iNormalizePrefix))) {
                    iNormalizePrefix++;
                }
                if (iNormalizePrefix == i2) {
                    int length = stringBuffer.length();
                    if ((length == 2 && stringBuffer.charAt(1) == ':') || length == 0) {
                        stringBuffer.append(c2);
                    } else if (length == 1 && isSlash(stringBuffer.charAt(0))) {
                        stringBuffer.append(c2);
                    }
                } else {
                    stringBuffer.append(c2);
                }
            } else {
                stringBuffer.append(cCharAt);
            }
        }
        return stringBuffer.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int normalizePrefix(java.lang.String r5, int r6, java.lang.StringBuffer r7) {
        /*
            r4 = this;
            r0 = 0
            r8 = r0
        L3:
            r0 = r8
            r1 = r6
            if (r0 >= r1) goto L1c
            r0 = r4
            r1 = r5
            r2 = r8
            char r1 = r1.charAt(r2)
            boolean r0 = r0.isSlash(r1)
            if (r0 == 0) goto L1c
            int r8 = r8 + 1
            goto L3
        L1c:
            r0 = r6
            r1 = r8
            int r0 = r0 - r1
            r1 = 2
            if (r0 < r1) goto L55
            r0 = r4
            r1 = r5
            r2 = r8
            char r1 = r1.charAt(r2)
            r2 = r1
            r9 = r2
            boolean r0 = r0.isLetter(r1)
            if (r0 == 0) goto L55
            r0 = r5
            r1 = r8
            r2 = 1
            int r1 = r1 + r2
            char r0 = r0.charAt(r1)
            r1 = 58
            if (r0 != r1) goto L55
            r0 = r7
            r1 = r9
            java.lang.StringBuffer r0 = r0.append(r1)
            r0 = r7
            r1 = 58
            java.lang.StringBuffer r0 = r0.append(r1)
            int r8 = r8 + 2
            goto L81
        L55:
            r0 = 0
            r8 = r0
            r0 = r6
            r1 = 2
            if (r0 < r1) goto L81
            r0 = r4
            r1 = r5
            r2 = 0
            char r1 = r1.charAt(r2)
            boolean r0 = r0.isSlash(r1)
            if (r0 == 0) goto L81
            r0 = r4
            r1 = r5
            r2 = 1
            char r1 = r1.charAt(r2)
            boolean r0 = r0.isSlash(r1)
            if (r0 == 0) goto L81
            r0 = 1
            r8 = r0
            r0 = r7
            r1 = r4
            char r1 = r1.slash
            java.lang.StringBuffer r0 = r0.append(r1)
        L81:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.io.WinNTFileSystem.normalizePrefix(java.lang.String, int, java.lang.StringBuffer):int");
    }

    @Override // java.io.FileSystem
    public int prefixLength(String str) {
        char c2 = this.slash;
        int length = str.length();
        if (length == 0) {
            return 0;
        }
        char cCharAt = str.charAt(0);
        char cCharAt2 = length > 1 ? str.charAt(1) : (char) 0;
        if (cCharAt == c2) {
            return cCharAt2 == c2 ? 2 : 1;
        }
        if (isLetter(cCharAt) && cCharAt2 == ':') {
            if (length > 2 && str.charAt(2) == c2) {
                return 3;
            }
            return 2;
        }
        return 0;
    }

    @Override // java.io.FileSystem
    public String resolve(String str, String str2) {
        char[] cArr;
        int length = str.length();
        if (length == 0) {
            return str2;
        }
        int length2 = str2.length();
        if (length2 == 0) {
            return str;
        }
        int i2 = 0;
        int i3 = length;
        if (length2 > 1 && str2.charAt(0) == this.slash) {
            if (str2.charAt(1) == this.slash) {
                i2 = 2;
            } else {
                i2 = 1;
            }
            if (length2 == i2) {
                if (str.charAt(length - 1) == this.slash) {
                    return str.substring(0, length - 1);
                }
                return str;
            }
        }
        if (str.charAt(length - 1) == this.slash) {
            i3--;
        }
        int i4 = (i3 + length2) - i2;
        if (str2.charAt(i2) == this.slash) {
            cArr = new char[i4];
            str.getChars(0, i3, cArr, 0);
            str2.getChars(i2, length2, cArr, i3);
        } else {
            cArr = new char[i4 + 1];
            str.getChars(0, i3, cArr, 0);
            cArr[i3] = this.slash;
            str2.getChars(i2, length2, cArr, i3 + 1);
        }
        return new String(cArr);
    }

    @Override // java.io.FileSystem
    public String getDefaultParent() {
        return "" + this.slash;
    }

    @Override // java.io.FileSystem
    public String fromURIPath(String str) {
        String strSubstring = str;
        if (strSubstring.length() > 2 && strSubstring.charAt(2) == ':') {
            strSubstring = strSubstring.substring(1);
            if (strSubstring.length() > 3 && strSubstring.endsWith("/")) {
                strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
            }
        } else if (strSubstring.length() > 1 && strSubstring.endsWith("/")) {
            strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
        }
        return strSubstring;
    }

    @Override // java.io.FileSystem
    public boolean isAbsolute(File file) {
        int prefixLength = file.getPrefixLength();
        return (prefixLength == 2 && file.getPath().charAt(0) == this.slash) || prefixLength == 3;
    }

    @Override // java.io.FileSystem
    public boolean isInvalid(File file) {
        String path;
        int iLastIndexOf;
        if (file.getPath().indexOf(0) >= 0) {
            return true;
        }
        if (!ENABLE_ADS && (iLastIndexOf = (path = file.getPath()).lastIndexOf(CallSiteDescriptor.TOKEN_DELIMITER)) >= 0) {
            if (iLastIndexOf == 1 && isLetter(path.charAt(0))) {
                return false;
            }
            try {
                builtInFS.getPath(path, new String[0]);
                return false;
            } catch (InvalidPathException e2) {
                return true;
            }
        }
        return false;
    }

    @Override // java.io.FileSystem
    public String resolve(File file) {
        String path = file.getPath();
        int prefixLength = file.getPrefixLength();
        if (prefixLength == 2 && path.charAt(0) == this.slash) {
            return path;
        }
        if (prefixLength == 3) {
            return path;
        }
        if (prefixLength == 0) {
            return getUserPath() + slashify(path);
        }
        if (prefixLength == 1) {
            String userPath = getUserPath();
            String drive = getDrive(userPath);
            return drive != null ? drive + path : userPath + path;
        }
        if (prefixLength == 2) {
            String userPath2 = getUserPath();
            String drive2 = getDrive(userPath2);
            if (drive2 != null && path.startsWith(drive2)) {
                return userPath2 + slashify(path.substring(2));
            }
            char cCharAt = path.charAt(0);
            String driveDirectory = getDriveDirectory(cCharAt);
            if (driveDirectory != null) {
                String str = cCharAt + ':' + driveDirectory + slashify(path.substring(2));
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    try {
                        securityManager.checkRead(str);
                    } catch (SecurityException e2) {
                        throw new SecurityException("Cannot resolve path " + path);
                    }
                }
                return str;
            }
            return cCharAt + CallSiteDescriptor.TOKEN_DELIMITER + slashify(path.substring(2));
        }
        throw new InternalError("Unresolvable path: " + path);
    }

    private String getUserPath() {
        return normalize(System.getProperty("user.dir"));
    }

    private String getDrive(String str) {
        if (prefixLength(str) == 3) {
            return str.substring(0, 2);
        }
        return null;
    }

    private static int driveIndex(char c2) {
        if (c2 >= 'a' && c2 <= 'z') {
            return c2 - 'a';
        }
        if (c2 < 'A' || c2 > 'Z') {
            return -1;
        }
        return c2 - 'A';
    }

    private String getDriveDirectory(char c2) {
        int iDriveIndex = driveIndex(c2);
        if (iDriveIndex < 0) {
            return null;
        }
        String str = driveDirCache[iDriveIndex];
        if (str != null) {
            return str;
        }
        String driveDirectory = getDriveDirectory(iDriveIndex + 1);
        driveDirCache[iDriveIndex] = driveDirectory;
        return driveDirectory;
    }

    @Override // java.io.FileSystem
    public String canonicalize(String str) throws IOException {
        String strParentOrNull;
        String str2;
        int length = str.length();
        if (length == 2 && isLetter(str.charAt(0)) && str.charAt(1) == ':') {
            char cCharAt = str.charAt(0);
            if (cCharAt >= 'A' && cCharAt <= 'Z') {
                return str;
            }
            return "" + ((char) (cCharAt - ' ')) + ':';
        }
        if (length == 3 && isLetter(str.charAt(0)) && str.charAt(1) == ':' && str.charAt(2) == '\\') {
            char cCharAt2 = str.charAt(0);
            if (cCharAt2 >= 'A' && cCharAt2 <= 'Z') {
                return str;
            }
            return "" + ((char) (cCharAt2 - ' ')) + ":\\";
        }
        if (!useCanonCaches) {
            return canonicalize0(str);
        }
        String strCanonicalize0 = this.cache.get(str);
        if (strCanonicalize0 == null) {
            String strParentOrNull2 = null;
            if (useCanonPrefixCache) {
                strParentOrNull2 = parentOrNull(str);
                if (strParentOrNull2 != null && (str2 = this.prefixCache.get(strParentOrNull2)) != null) {
                    String strSubstring = str.substring(1 + strParentOrNull2.length());
                    strCanonicalize0 = canonicalizeWithPrefix(str2, strSubstring);
                    this.cache.put(strParentOrNull2 + File.separatorChar + strSubstring, strCanonicalize0);
                }
            }
            if (strCanonicalize0 == null) {
                strCanonicalize0 = canonicalize0(str);
                this.cache.put(str, strCanonicalize0);
                if (useCanonPrefixCache && strParentOrNull2 != null && (strParentOrNull = parentOrNull(strCanonicalize0)) != null) {
                    File file = new File(strCanonicalize0);
                    if (file.exists() && !file.isDirectory()) {
                        this.prefixCache.put(strParentOrNull2, strParentOrNull);
                    }
                }
            }
        }
        return strCanonicalize0;
    }

    private String canonicalizeWithPrefix(String str, String str2) throws IOException {
        return canonicalizeWithPrefix0(str, str + File.separatorChar + str2);
    }

    private static String parentOrNull(String str) {
        if (str == null) {
            return null;
        }
        char c2 = File.separatorChar;
        int length = str.length() - 1;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = length; i4 > 0; i4--) {
            char cCharAt = str.charAt(i4);
            if (cCharAt == '.') {
                i2++;
                if (i2 >= 2 || i3 == 0) {
                    return null;
                }
            } else {
                if (cCharAt == c2) {
                    if ((i2 == 1 && i3 == 0) || i4 == 0 || i4 >= length - 1 || str.charAt(i4 - 1) == c2 || str.charAt(i4 - 1) == '/') {
                        return null;
                    }
                    return str.substring(0, i4);
                }
                if (cCharAt == '/' || cCharAt == '*' || cCharAt == '?') {
                    return null;
                }
                i3++;
                i2 = 0;
            }
        }
        return null;
    }

    @Override // java.io.FileSystem
    public boolean delete(File file) {
        this.cache.clear();
        this.prefixCache.clear();
        return delete0(file);
    }

    @Override // java.io.FileSystem
    public boolean rename(File file, File file2) {
        this.cache.clear();
        this.prefixCache.clear();
        return rename0(file, file2);
    }

    @Override // java.io.FileSystem
    public File[] listRoots() {
        int iListRoots0 = listRoots0();
        int i2 = 0;
        for (int i3 = 0; i3 < 26; i3++) {
            if (((iListRoots0 >> i3) & 1) != 0) {
                if (!access(((char) (65 + i3)) + CallSiteDescriptor.TOKEN_DELIMITER + this.slash)) {
                    iListRoots0 &= (1 << i3) ^ (-1);
                } else {
                    i2++;
                }
            }
        }
        File[] fileArr = new File[i2];
        int i4 = 0;
        char c2 = this.slash;
        for (int i5 = 0; i5 < 26; i5++) {
            if (((iListRoots0 >> i5) & 1) != 0) {
                int i6 = i4;
                i4++;
                fileArr[i6] = new File(((char) (65 + i5)) + CallSiteDescriptor.TOKEN_DELIMITER + c2);
            }
        }
        return fileArr;
    }

    private boolean access(String str) {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkRead(str);
                return true;
            }
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    @Override // java.io.FileSystem
    public long getSpace(File file, int i2) {
        if (file.exists()) {
            return getSpace0(file, i2);
        }
        return 0L;
    }

    @Override // java.io.FileSystem
    public int compare(File file, File file2) {
        return file.getPath().compareToIgnoreCase(file2.getPath());
    }

    @Override // java.io.FileSystem
    public int hashCode(File file) {
        return file.getPath().toLowerCase(Locale.ENGLISH).hashCode() ^ 1234321;
    }
}
