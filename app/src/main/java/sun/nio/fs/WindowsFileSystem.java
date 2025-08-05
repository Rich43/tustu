package sun.nio.fs;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import sun.nio.fs.WindowsPathParser;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/nio/fs/WindowsFileSystem.class */
class WindowsFileSystem extends FileSystem {
    private final WindowsFileSystemProvider provider;
    private final String defaultDirectory;
    private final String defaultRoot;
    private final boolean supportsLinks;
    private final boolean supportsStreamEnumeration;
    private static final Set<String> supportedFileAttributeViews = Collections.unmodifiableSet(new HashSet(Arrays.asList("basic", "dos", "acl", "owner", "user")));
    private static final String GLOB_SYNTAX = "glob";
    private static final String REGEX_SYNTAX = "regex";

    WindowsFileSystem(WindowsFileSystemProvider windowsFileSystemProvider, String str) throws NumberFormatException {
        this.provider = windowsFileSystemProvider;
        WindowsPathParser.Result result = WindowsPathParser.parse(str);
        if (result.type() != WindowsPathType.ABSOLUTE && result.type() != WindowsPathType.UNC) {
            throw new AssertionError((Object) "Default directory is not an absolute path");
        }
        this.defaultDirectory = result.path();
        this.defaultRoot = result.root();
        String[] strArrSplit = Util.split((String) AccessController.doPrivileged(new GetPropertyAction("os.version")), '.');
        int i2 = Integer.parseInt(strArrSplit[0]);
        int i3 = Integer.parseInt(strArrSplit[1]);
        this.supportsLinks = i2 >= 6;
        this.supportsStreamEnumeration = i2 >= 6 || (i2 == 5 && i3 >= 2);
    }

    String defaultDirectory() {
        return this.defaultDirectory;
    }

    String defaultRoot() {
        return this.defaultRoot;
    }

    boolean supportsLinks() {
        return this.supportsLinks;
    }

    boolean supportsStreamEnumeration() {
        return this.supportsStreamEnumeration;
    }

    @Override // java.nio.file.FileSystem
    public FileSystemProvider provider() {
        return this.provider;
    }

    @Override // java.nio.file.FileSystem
    public String getSeparator() {
        return FXMLLoader.ESCAPE_PREFIX;
    }

    @Override // java.nio.file.FileSystem
    public boolean isOpen() {
        return true;
    }

    @Override // java.nio.file.FileSystem
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.file.FileSystem, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.nio.file.FileSystem
    public Iterable<Path> getRootDirectories() {
        try {
            int iGetLogicalDrives = WindowsNativeDispatcher.GetLogicalDrives();
            ArrayList arrayList = new ArrayList();
            SecurityManager securityManager = System.getSecurityManager();
            for (int i2 = 0; i2 <= 25; i2++) {
                if ((iGetLogicalDrives & (1 << i2)) != 0) {
                    StringBuilder sb = new StringBuilder(3);
                    sb.append((char) (65 + i2));
                    sb.append(":\\");
                    String string = sb.toString();
                    if (securityManager != null) {
                        try {
                            securityManager.checkRead(string);
                            arrayList.add(WindowsPath.createFromNormalizedPath(this, string));
                        } catch (SecurityException e2) {
                        }
                    } else {
                        arrayList.add(WindowsPath.createFromNormalizedPath(this, string));
                    }
                }
            }
            return Collections.unmodifiableList(arrayList);
        } catch (WindowsException e3) {
            throw new AssertionError((Object) e3.getMessage());
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsFileSystem$FileStoreIterator.class */
    private class FileStoreIterator implements Iterator<FileStore> {
        private final Iterator<Path> roots;
        private FileStore next;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WindowsFileSystem.class.desiredAssertionStatus();
        }

        FileStoreIterator() {
            this.roots = WindowsFileSystem.this.getRootDirectories().iterator();
        }

        private FileStore readNext() {
            WindowsFileStore windowsFileStoreCreate;
            if (!$assertionsDisabled && !Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            while (this.roots.hasNext()) {
                WindowsPath windowsPath = (WindowsPath) this.roots.next();
                try {
                    windowsPath.checkRead();
                    try {
                        windowsFileStoreCreate = WindowsFileStore.create(windowsPath.toString(), true);
                    } catch (IOException e2) {
                    }
                } catch (SecurityException e3) {
                }
                if (windowsFileStoreCreate != null) {
                    return windowsFileStoreCreate;
                }
            }
            return null;
        }

        @Override // java.util.Iterator
        public synchronized boolean hasNext() {
            if (this.next != null) {
                return true;
            }
            this.next = readNext();
            return this.next != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public synchronized FileStore next() {
            if (this.next == null) {
                this.next = readNext();
            }
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            FileStore fileStore = this.next;
            this.next = null;
            return fileStore;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override // java.nio.file.FileSystem
    public Iterable<FileStore> getFileStores() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission(new RuntimePermission("getFileStoreAttributes"));
            } catch (SecurityException e2) {
                return Collections.emptyList();
            }
        }
        return new Iterable<FileStore>() { // from class: sun.nio.fs.WindowsFileSystem.1
            @Override // java.lang.Iterable, java.util.List
            public Iterator<FileStore> iterator() {
                return WindowsFileSystem.this.new FileStoreIterator();
            }
        };
    }

    @Override // java.nio.file.FileSystem
    public Set<String> supportedFileAttributeViews() {
        return supportedFileAttributeViews;
    }

    @Override // java.nio.file.FileSystem
    public final Path getPath(String str, String... strArr) {
        String string;
        if (strArr.length == 0) {
            string = str;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            for (String str2 : strArr) {
                if (str2.length() > 0) {
                    if (sb.length() > 0) {
                        sb.append('\\');
                    }
                    sb.append(str2);
                }
            }
            string = sb.toString();
        }
        return WindowsPath.parse(this, string);
    }

    @Override // java.nio.file.FileSystem
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return LookupService.instance;
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsFileSystem$LookupService.class */
    private static class LookupService {
        static final UserPrincipalLookupService instance = new UserPrincipalLookupService() { // from class: sun.nio.fs.WindowsFileSystem.LookupService.1
            @Override // java.nio.file.attribute.UserPrincipalLookupService
            public UserPrincipal lookupPrincipalByName(String str) throws IOException {
                return WindowsUserPrincipals.lookup(str);
            }

            @Override // java.nio.file.attribute.UserPrincipalLookupService
            public GroupPrincipal lookupPrincipalByGroupName(String str) throws IOException {
                UserPrincipal userPrincipalLookup = WindowsUserPrincipals.lookup(str);
                if (!(userPrincipalLookup instanceof GroupPrincipal)) {
                    throw new UserPrincipalNotFoundException(str);
                }
                return (GroupPrincipal) userPrincipalLookup;
            }
        };

        private LookupService() {
        }
    }

    @Override // java.nio.file.FileSystem
    public PathMatcher getPathMatcher(String str) {
        String windowsRegexPattern;
        int iIndexOf = str.indexOf(58);
        if (iIndexOf <= 0 || iIndexOf == str.length()) {
            throw new IllegalArgumentException();
        }
        String strSubstring = str.substring(0, iIndexOf);
        String strSubstring2 = str.substring(iIndexOf + 1);
        if (strSubstring.equals(GLOB_SYNTAX)) {
            windowsRegexPattern = Globs.toWindowsRegexPattern(strSubstring2);
        } else if (strSubstring.equals(REGEX_SYNTAX)) {
            windowsRegexPattern = strSubstring2;
        } else {
            throw new UnsupportedOperationException("Syntax '" + strSubstring + "' not recognized");
        }
        final Pattern patternCompile = Pattern.compile(windowsRegexPattern, 66);
        return new PathMatcher() { // from class: sun.nio.fs.WindowsFileSystem.2
            @Override // java.nio.file.PathMatcher
            public boolean matches(Path path) {
                return patternCompile.matcher(path.toString()).matches();
            }
        };
    }

    @Override // java.nio.file.FileSystem
    public WatchService newWatchService() throws IOException {
        return new WindowsWatchService(this);
    }
}
