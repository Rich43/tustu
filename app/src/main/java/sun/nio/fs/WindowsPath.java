package sun.nio.fs;

import com.sun.nio.file.ExtendedWatchEventModifier;
import java.io.IOError;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.nio.fs.WindowsPathParser;

/* loaded from: rt.jar:sun/nio/fs/WindowsPath.class */
class WindowsPath extends AbstractPath {
    private static final int MAX_PATH = 247;
    private static final int MAX_LONG_PATH = 32000;
    private final WindowsFileSystem fs;
    private final WindowsPathType type;
    private final String root;
    private final String path;
    private volatile WeakReference<String> pathForWin32Calls;
    private volatile Integer[] offsets;
    private int hash;

    private WindowsPath(WindowsFileSystem windowsFileSystem, WindowsPathType windowsPathType, String str, String str2) {
        this.fs = windowsFileSystem;
        this.type = windowsPathType;
        this.root = str;
        this.path = str2;
    }

    static WindowsPath parse(WindowsFileSystem windowsFileSystem, String str) {
        WindowsPathParser.Result result = WindowsPathParser.parse(str);
        return new WindowsPath(windowsFileSystem, result.type(), result.root(), result.path());
    }

    static WindowsPath createFromNormalizedPath(WindowsFileSystem windowsFileSystem, String str, BasicFileAttributes basicFileAttributes) {
        try {
            WindowsPathParser.Result normalizedPath = WindowsPathParser.parseNormalizedPath(str);
            if (basicFileAttributes == null) {
                return new WindowsPath(windowsFileSystem, normalizedPath.type(), normalizedPath.root(), normalizedPath.path());
            }
            return new WindowsPathWithAttributes(windowsFileSystem, normalizedPath.type(), normalizedPath.root(), normalizedPath.path(), basicFileAttributes);
        } catch (InvalidPathException e2) {
            throw new AssertionError((Object) e2.getMessage());
        }
    }

    static WindowsPath createFromNormalizedPath(WindowsFileSystem windowsFileSystem, String str) {
        return createFromNormalizedPath(windowsFileSystem, str, null);
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsPath$WindowsPathWithAttributes.class */
    private static class WindowsPathWithAttributes extends WindowsPath implements BasicFileAttributesHolder {
        final WeakReference<BasicFileAttributes> ref;

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ Path toRealPath(LinkOption[] linkOptionArr) throws IOException {
            return super.toRealPath(linkOptionArr);
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ Path toAbsolutePath() {
            return super.toAbsolutePath();
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ Path relativize(Path path) {
            return super.relativize(path);
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ Path resolve(Path path) {
            return super.resolve(path);
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ Path subpath(int i2, int i3) {
            return super.subpath(i2, i3);
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ Path getName(int i2) {
            return super.getName(i2);
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ Path getParent() {
            return super.getParent();
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ Path getRoot() {
            return super.getRoot();
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path
        public /* bridge */ /* synthetic */ FileSystem getFileSystem() {
            return super.getFileSystem();
        }

        @Override // sun.nio.fs.WindowsPath, java.nio.file.Path, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Path path) {
            return super.compareTo(path);
        }

        WindowsPathWithAttributes(WindowsFileSystem windowsFileSystem, WindowsPathType windowsPathType, String str, String str2, BasicFileAttributes basicFileAttributes) {
            super(windowsFileSystem, windowsPathType, str, str2);
            this.ref = new WeakReference<>(basicFileAttributes);
        }

        @Override // sun.nio.fs.BasicFileAttributesHolder
        public BasicFileAttributes get() {
            return this.ref.get();
        }

        @Override // sun.nio.fs.BasicFileAttributesHolder
        public void invalidate() {
            this.ref.clear();
        }
    }

    String getPathForExceptionMessage() {
        return this.path;
    }

    String getPathForPermissionCheck() {
        return this.path;
    }

    String getPathForWin32Calls() throws WindowsException {
        if (isAbsolute() && this.path.length() <= 247) {
            return this.path;
        }
        WeakReference<String> weakReference = this.pathForWin32Calls;
        String str = weakReference != null ? weakReference.get() : null;
        if (str != null) {
            return str;
        }
        String absolutePath = getAbsolutePath();
        if (absolutePath.length() > 247) {
            if (absolutePath.length() > MAX_LONG_PATH) {
                throw new WindowsException("Cannot access file with path exceeding 32000 characters");
            }
            absolutePath = addPrefixIfNeeded(WindowsNativeDispatcher.GetFullPathName(absolutePath));
        }
        if (this.type != WindowsPathType.DRIVE_RELATIVE) {
            synchronized (this.path) {
                this.pathForWin32Calls = new WeakReference<>(absolutePath);
            }
        }
        return absolutePath;
    }

    private String getAbsolutePath() throws WindowsException {
        String str;
        if (isAbsolute()) {
            return this.path;
        }
        if (this.type == WindowsPathType.RELATIVE) {
            String strDefaultDirectory = getFileSystem().defaultDirectory();
            if (isEmpty()) {
                return strDefaultDirectory;
            }
            if (strDefaultDirectory.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                return strDefaultDirectory + this.path;
            }
            return new StringBuilder(strDefaultDirectory.length() + this.path.length() + 1).append(strDefaultDirectory).append('\\').append(this.path).toString();
        }
        if (this.type == WindowsPathType.DIRECTORY_RELATIVE) {
            return getFileSystem().defaultRoot() + this.path.substring(1);
        }
        if (isSameDrive(this.root, getFileSystem().defaultRoot())) {
            String strSubstring = this.path.substring(this.root.length());
            String strDefaultDirectory2 = getFileSystem().defaultDirectory();
            if (strDefaultDirectory2.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                str = strDefaultDirectory2 + strSubstring;
            } else {
                str = strDefaultDirectory2 + FXMLLoader.ESCAPE_PREFIX + strSubstring;
            }
            return str;
        }
        try {
            int iGetDriveType = WindowsNativeDispatcher.GetDriveType(this.root + FXMLLoader.ESCAPE_PREFIX);
            if (iGetDriveType == 0 || iGetDriveType == 1) {
                throw new WindowsException("");
            }
            String strGetFullPathName = WindowsNativeDispatcher.GetFullPathName(this.root + ".");
            String str2 = strGetFullPathName;
            if (strGetFullPathName.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                str2 = str2 + this.path.substring(this.root.length());
            } else if (this.path.length() > this.root.length()) {
                str2 = str2 + FXMLLoader.ESCAPE_PREFIX + this.path.substring(this.root.length());
            }
            return str2;
        } catch (WindowsException e2) {
            throw new WindowsException("Unable to get working directory of drive '" + Character.toUpperCase(this.root.charAt(0)) + PdfOps.SINGLE_QUOTE_TOKEN);
        }
    }

    private static boolean isSameDrive(String str, String str2) {
        return Character.toUpperCase(str.charAt(0)) == Character.toUpperCase(str2.charAt(0));
    }

    static String addPrefixIfNeeded(String str) {
        if (str.length() > 247) {
            if (str.startsWith("\\\\")) {
                str = "\\\\?\\UNC" + str.substring(1, str.length());
            } else {
                str = "\\\\?\\" + str;
            }
        }
        return str;
    }

    @Override // java.nio.file.Path
    public WindowsFileSystem getFileSystem() {
        return this.fs;
    }

    private boolean isEmpty() {
        return this.path.length() == 0;
    }

    private WindowsPath emptyPath() {
        return new WindowsPath(getFileSystem(), WindowsPathType.RELATIVE, "", "");
    }

    @Override // java.nio.file.Path
    public Path getFileName() {
        int length;
        int length2 = this.path.length();
        if (length2 == 0) {
            return this;
        }
        if (this.root.length() == length2) {
            return null;
        }
        int iLastIndexOf = this.path.lastIndexOf(92);
        if (iLastIndexOf < this.root.length()) {
            length = this.root.length();
        } else {
            length = iLastIndexOf + 1;
        }
        return new WindowsPath(getFileSystem(), WindowsPathType.RELATIVE, "", this.path.substring(length));
    }

    @Override // java.nio.file.Path
    public WindowsPath getParent() {
        if (this.root.length() == this.path.length()) {
            return null;
        }
        int iLastIndexOf = this.path.lastIndexOf(92);
        if (iLastIndexOf < this.root.length()) {
            return getRoot();
        }
        return new WindowsPath(getFileSystem(), this.type, this.root, this.path.substring(0, iLastIndexOf));
    }

    @Override // java.nio.file.Path
    public WindowsPath getRoot() {
        if (this.root.length() == 0) {
            return null;
        }
        return new WindowsPath(getFileSystem(), this.type, this.root, this.root);
    }

    WindowsPathType type() {
        return this.type;
    }

    boolean isUnc() {
        return this.type == WindowsPathType.UNC;
    }

    boolean needsSlashWhenResolving() {
        return !this.path.endsWith(FXMLLoader.ESCAPE_PREFIX) && this.path.length() > this.root.length();
    }

    @Override // java.nio.file.Path
    public boolean isAbsolute() {
        return this.type == WindowsPathType.ABSOLUTE || this.type == WindowsPathType.UNC;
    }

    static WindowsPath toWindowsPath(Path path) {
        if (path == null) {
            throw new NullPointerException();
        }
        if (!(path instanceof WindowsPath)) {
            throw new ProviderMismatchException();
        }
        return (WindowsPath) path;
    }

    @Override // java.nio.file.Path
    public WindowsPath relativize(Path path) {
        WindowsPath windowsPath = toWindowsPath(path);
        if (equals(windowsPath)) {
            return emptyPath();
        }
        if (this.type != windowsPath.type) {
            throw new IllegalArgumentException("'other' is different type of Path");
        }
        if (!this.root.equalsIgnoreCase(windowsPath.root)) {
            throw new IllegalArgumentException("'other' has different root");
        }
        int nameCount = getNameCount();
        int nameCount2 = windowsPath.getNameCount();
        int i2 = nameCount > nameCount2 ? nameCount2 : nameCount;
        int i3 = 0;
        while (i3 < i2 && getName(i3).equals(windowsPath.getName(i3))) {
            i3++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i4 = i3; i4 < nameCount; i4++) {
            sb.append("..\\");
        }
        for (int i5 = i3; i5 < nameCount2; i5++) {
            sb.append(windowsPath.getName(i5).toString());
            sb.append(FXMLLoader.ESCAPE_PREFIX);
        }
        sb.setLength(sb.length() - 1);
        return createFromNormalizedPath(getFileSystem(), sb.toString());
    }

    @Override // java.nio.file.Path
    public Path normalize() {
        int i2;
        int nameCount = getNameCount();
        if (nameCount == 0 || isEmpty()) {
            return this;
        }
        boolean[] zArr = new boolean[nameCount];
        int i3 = nameCount;
        do {
            i2 = i3;
            int i4 = -1;
            for (int i5 = 0; i5 < nameCount; i5++) {
                if (!zArr[i5]) {
                    String strElementAsString = elementAsString(i5);
                    if (strElementAsString.length() > 2) {
                        i4 = i5;
                    } else if (strElementAsString.length() == 1) {
                        if (strElementAsString.charAt(0) == '.') {
                            zArr[i5] = true;
                            i3--;
                        } else {
                            i4 = i5;
                        }
                    } else if (strElementAsString.charAt(0) != '.' || strElementAsString.charAt(1) != '.') {
                        i4 = i5;
                    } else if (i4 >= 0) {
                        zArr[i4] = true;
                        zArr[i5] = true;
                        i3 -= 2;
                        i4 = -1;
                    } else if (isAbsolute() || this.type == WindowsPathType.DIRECTORY_RELATIVE) {
                        boolean z2 = false;
                        int i6 = 0;
                        while (true) {
                            if (i6 >= i5) {
                                break;
                            }
                            if (zArr[i6]) {
                                i6++;
                            } else {
                                z2 = true;
                                break;
                            }
                        }
                        if (!z2) {
                            zArr[i5] = true;
                            i3--;
                        }
                    }
                }
            }
        } while (i2 > i3);
        if (i3 == nameCount) {
            return this;
        }
        if (i3 == 0) {
            return this.root.length() == 0 ? emptyPath() : getRoot();
        }
        StringBuilder sb = new StringBuilder();
        if (this.root != null) {
            sb.append(this.root);
        }
        for (int i7 = 0; i7 < nameCount; i7++) {
            if (!zArr[i7]) {
                sb.append((Object) getName(i7));
                sb.append(FXMLLoader.ESCAPE_PREFIX);
            }
        }
        sb.setLength(sb.length() - 1);
        return createFromNormalizedPath(getFileSystem(), sb.toString());
    }

    @Override // java.nio.file.Path
    public WindowsPath resolve(Path path) {
        String str;
        String str2;
        String str3;
        WindowsPath windowsPath = toWindowsPath(path);
        if (windowsPath.isEmpty()) {
            return this;
        }
        if (windowsPath.isAbsolute()) {
            return windowsPath;
        }
        switch (windowsPath.type) {
            case RELATIVE:
                if (this.path.endsWith(FXMLLoader.ESCAPE_PREFIX) || this.root.length() == this.path.length()) {
                    str3 = this.path + windowsPath.path;
                } else {
                    str3 = this.path + FXMLLoader.ESCAPE_PREFIX + windowsPath.path;
                }
                return new WindowsPath(getFileSystem(), this.type, this.root, str3);
            case DIRECTORY_RELATIVE:
                if (this.root.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                    str2 = this.root + windowsPath.path.substring(1);
                } else {
                    str2 = this.root + windowsPath.path;
                }
                return createFromNormalizedPath(getFileSystem(), str2);
            case DRIVE_RELATIVE:
                if (!this.root.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                    return windowsPath;
                }
                if (!this.root.substring(0, this.root.length() - 1).equalsIgnoreCase(windowsPath.root)) {
                    return windowsPath;
                }
                String strSubstring = windowsPath.path.substring(windowsPath.root.length());
                if (this.path.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                    str = this.path + strSubstring;
                } else {
                    str = this.path + FXMLLoader.ESCAPE_PREFIX + strSubstring;
                }
                return createFromNormalizedPath(getFileSystem(), str);
            default:
                throw new AssertionError();
        }
    }

    private void initOffsets() {
        if (this.offsets == null) {
            ArrayList arrayList = new ArrayList();
            if (isEmpty()) {
                arrayList.add(0);
            } else {
                int length = this.root.length();
                int length2 = this.root.length();
                while (length2 < this.path.length()) {
                    if (this.path.charAt(length2) != '\\') {
                        length2++;
                    } else {
                        arrayList.add(Integer.valueOf(length));
                        length2++;
                        length = length2;
                    }
                }
                if (length != length2) {
                    arrayList.add(Integer.valueOf(length));
                }
            }
            synchronized (this) {
                if (this.offsets == null) {
                    this.offsets = (Integer[]) arrayList.toArray(new Integer[arrayList.size()]);
                }
            }
        }
    }

    @Override // java.nio.file.Path
    public int getNameCount() {
        initOffsets();
        return this.offsets.length;
    }

    private String elementAsString(int i2) {
        initOffsets();
        if (i2 == this.offsets.length - 1) {
            return this.path.substring(this.offsets[i2].intValue());
        }
        return this.path.substring(this.offsets[i2].intValue(), this.offsets[i2 + 1].intValue() - 1);
    }

    @Override // java.nio.file.Path
    public WindowsPath getName(int i2) {
        initOffsets();
        if (i2 < 0 || i2 >= this.offsets.length) {
            throw new IllegalArgumentException();
        }
        return new WindowsPath(getFileSystem(), WindowsPathType.RELATIVE, "", elementAsString(i2));
    }

    @Override // java.nio.file.Path
    public WindowsPath subpath(int i2, int i3) {
        initOffsets();
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        if (i2 >= this.offsets.length) {
            throw new IllegalArgumentException();
        }
        if (i3 > this.offsets.length) {
            throw new IllegalArgumentException();
        }
        if (i2 >= i3) {
            throw new IllegalArgumentException();
        }
        StringBuilder sb = new StringBuilder();
        Integer[] numArr = new Integer[i3 - i2];
        for (int i4 = i2; i4 < i3; i4++) {
            numArr[i4 - i2] = Integer.valueOf(sb.length());
            sb.append(elementAsString(i4));
            if (i4 != i3 - 1) {
                sb.append(FXMLLoader.ESCAPE_PREFIX);
            }
        }
        return new WindowsPath(getFileSystem(), WindowsPathType.RELATIVE, "", sb.toString());
    }

    @Override // java.nio.file.Path
    public boolean startsWith(Path path) {
        if (!(Objects.requireNonNull(path) instanceof WindowsPath)) {
            return false;
        }
        WindowsPath windowsPath = (WindowsPath) path;
        if (!this.root.equalsIgnoreCase(windowsPath.root)) {
            return false;
        }
        if (windowsPath.isEmpty()) {
            return isEmpty();
        }
        int nameCount = getNameCount();
        int nameCount2 = windowsPath.getNameCount();
        if (nameCount2 <= nameCount) {
            do {
                nameCount2--;
                if (nameCount2 < 0) {
                    return true;
                }
            } while (elementAsString(nameCount2).equalsIgnoreCase(windowsPath.elementAsString(nameCount2)));
            return false;
        }
        return false;
    }

    @Override // java.nio.file.Path
    public boolean endsWith(Path path) {
        if (!(Objects.requireNonNull(path) instanceof WindowsPath)) {
            return false;
        }
        WindowsPath windowsPath = (WindowsPath) path;
        if (windowsPath.path.length() > this.path.length()) {
            return false;
        }
        if (windowsPath.isEmpty()) {
            return isEmpty();
        }
        int nameCount = getNameCount();
        int nameCount2 = windowsPath.getNameCount();
        if (nameCount2 > nameCount) {
            return false;
        }
        if (windowsPath.root.length() > 0 && (nameCount2 < nameCount || !this.root.equalsIgnoreCase(windowsPath.root))) {
            return false;
        }
        int i2 = nameCount - nameCount2;
        do {
            nameCount2--;
            if (nameCount2 < 0) {
                return true;
            }
        } while (elementAsString(i2 + nameCount2).equalsIgnoreCase(windowsPath.elementAsString(nameCount2)));
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.nio.file.Path, java.lang.Comparable
    public int compareTo(Path path) {
        char upperCase;
        char upperCase2;
        if (path == null) {
            throw new NullPointerException();
        }
        String str = this.path;
        String str2 = ((WindowsPath) path).path;
        int length = str.length();
        int length2 = str2.length();
        int iMin = Math.min(length, length2);
        for (int i2 = 0; i2 < iMin; i2++) {
            char cCharAt = str.charAt(i2);
            char cCharAt2 = str2.charAt(i2);
            if (cCharAt != cCharAt2 && (upperCase = Character.toUpperCase(cCharAt)) != (upperCase2 = Character.toUpperCase(cCharAt2))) {
                return upperCase - upperCase2;
            }
        }
        return length - length2;
    }

    @Override // java.nio.file.Path
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof WindowsPath) && compareTo((Path) obj) == 0;
    }

    @Override // java.nio.file.Path
    public int hashCode() {
        int upperCase = this.hash;
        if (upperCase == 0) {
            for (int i2 = 0; i2 < this.path.length(); i2++) {
                upperCase = (31 * upperCase) + Character.toUpperCase(this.path.charAt(i2));
            }
            this.hash = upperCase;
        }
        return upperCase;
    }

    @Override // java.nio.file.Path
    public String toString() {
        return this.path;
    }

    long openForReadAttributeAccess(boolean z2) throws WindowsException {
        int i2 = 33554432;
        if (!z2 && getFileSystem().supportsLinks()) {
            i2 = 33554432 | 2097152;
        }
        return WindowsNativeDispatcher.CreateFile(getPathForWin32Calls(), 128, 7, 0L, 3, i2);
    }

    void checkRead() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(getPathForPermissionCheck());
        }
    }

    void checkWrite() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(getPathForPermissionCheck());
        }
    }

    void checkDelete() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(getPathForPermissionCheck());
        }
    }

    @Override // java.nio.file.Path
    public URI toUri() {
        return WindowsUriSupport.toUri(this);
    }

    @Override // java.nio.file.Path
    public WindowsPath toAbsolutePath() {
        if (isAbsolute()) {
            return this;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertyAccess("user.dir");
        }
        try {
            return createFromNormalizedPath(getFileSystem(), getAbsolutePath());
        } catch (WindowsException e2) {
            throw new IOError(new IOException(e2.getMessage()));
        }
    }

    @Override // java.nio.file.Path
    public WindowsPath toRealPath(LinkOption... linkOptionArr) throws IOException {
        checkRead();
        return createFromNormalizedPath(getFileSystem(), WindowsLinkSupport.getRealPath(this, Util.followLinks(linkOptionArr)));
    }

    @Override // java.nio.file.Path, java.nio.file.Watchable
    public WatchKey register(WatchService watchService, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException {
        if (watchService == null) {
            throw new NullPointerException();
        }
        if (!(watchService instanceof WindowsWatchService)) {
            throw new ProviderMismatchException();
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            boolean z2 = false;
            int length = modifierArr.length;
            if (length > 0) {
                modifierArr = (WatchEvent.Modifier[]) Arrays.copyOf(modifierArr, length);
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    int i3 = i2;
                    i2++;
                    if (modifierArr[i3] == ExtendedWatchEventModifier.FILE_TREE) {
                        z2 = true;
                        break;
                    }
                }
            }
            String pathForPermissionCheck = getPathForPermissionCheck();
            securityManager.checkRead(pathForPermissionCheck);
            if (z2) {
                securityManager.checkRead(pathForPermissionCheck + "\\-");
            }
        }
        return ((WindowsWatchService) watchService).register(this, kindArr, modifierArr);
    }
}
