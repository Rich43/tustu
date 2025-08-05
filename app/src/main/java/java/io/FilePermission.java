package java.io;

import java.net.URI;
import java.nio.file.InvalidPathException;
import java.security.AccessController;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import sun.nio.fs.DefaultFileSystemProvider;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/io/FilePermission.class */
public final class FilePermission extends Permission implements Serializable {
    private static final int EXECUTE = 1;
    private static final int WRITE = 2;
    private static final int READ = 4;
    private static final int DELETE = 8;
    private static final int READLINK = 16;
    private static final int ALL = 31;
    private static final int NONE = 0;
    private transient int mask;
    private transient boolean directory;
    private transient boolean recursive;
    private String actions;
    private transient String cpath;
    private transient boolean allFiles;
    private transient boolean invalid;
    private static final char RECURSIVE_CHAR = '-';
    private static final char WILD_CHAR = '*';
    private static final long serialVersionUID = 7930732926638008763L;
    private static final java.nio.file.FileSystem builtInFS = DefaultFileSystemProvider.create().getFileSystem(URI.create("file:///"));

    private void init(int i2) {
        if ((i2 & 31) != i2) {
            throw new IllegalArgumentException("invalid actions mask");
        }
        if (i2 == 0) {
            throw new IllegalArgumentException("invalid actions mask");
        }
        String name = getName();
        this.cpath = name;
        if (name == null) {
            throw new NullPointerException("name can't be null");
        }
        this.mask = i2;
        if (this.cpath.equals("<<ALL FILES>>")) {
            this.allFiles = true;
            this.directory = true;
            this.recursive = true;
            this.cpath = "";
            return;
        }
        if (builtInFS != null) {
            try {
                builtInFS.getPath(new File(this.cpath.endsWith("*") ? this.cpath.substring(0, this.cpath.length() - 1) + LanguageTag.SEP : this.cpath).getPath(), new String[0]);
            } catch (InvalidPathException e2) {
                this.invalid = true;
                return;
            }
        }
        this.cpath = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.io.FilePermission.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                try {
                    String str = FilePermission.this.cpath;
                    if (FilePermission.this.cpath.endsWith("*")) {
                        String canonicalPath = new File(str.substring(0, str.length() - 1) + LanguageTag.SEP).getCanonicalPath();
                        return canonicalPath.substring(0, canonicalPath.length() - 1) + "*";
                    }
                    return new File(str).getCanonicalPath();
                } catch (IOException e3) {
                    return FilePermission.this.cpath;
                }
            }
        });
        int length = this.cpath.length();
        char cCharAt = length > 0 ? this.cpath.charAt(length - 1) : (char) 0;
        if (cCharAt == '-' && this.cpath.charAt(length - 2) == File.separatorChar) {
            this.directory = true;
            this.recursive = true;
            this.cpath = this.cpath.substring(0, length - 1);
        } else if (cCharAt == '*' && this.cpath.charAt(length - 2) == File.separatorChar) {
            this.directory = true;
            this.cpath = this.cpath.substring(0, length - 1);
        }
    }

    public FilePermission(String str, String str2) {
        super(str);
        init(getMask(str2));
    }

    FilePermission(String str, int i2) {
        super(str);
        init(i2);
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof FilePermission)) {
            return false;
        }
        FilePermission filePermission = (FilePermission) permission;
        return (this.mask & filePermission.mask) == filePermission.mask && impliesIgnoreMask(filePermission);
    }

    boolean impliesIgnoreMask(FilePermission filePermission) {
        if (this == filePermission || this.allFiles) {
            return true;
        }
        if (this.invalid || filePermission.invalid || filePermission.allFiles) {
            return false;
        }
        if (this.directory) {
            if (this.recursive) {
                return filePermission.directory ? filePermission.cpath.length() >= this.cpath.length() && filePermission.cpath.startsWith(this.cpath) : filePermission.cpath.length() > this.cpath.length() && filePermission.cpath.startsWith(this.cpath);
            }
            if (filePermission.directory) {
                if (filePermission.recursive) {
                    return false;
                }
                return this.cpath.equals(filePermission.cpath);
            }
            int iLastIndexOf = filePermission.cpath.lastIndexOf(File.separatorChar);
            return iLastIndexOf != -1 && this.cpath.length() == iLastIndexOf + 1 && this.cpath.regionMatches(0, filePermission.cpath, 0, iLastIndexOf + 1);
        }
        if (filePermission.directory) {
            return false;
        }
        return this.cpath.equals(filePermission.cpath);
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FilePermission)) {
            return false;
        }
        FilePermission filePermission = (FilePermission) obj;
        return !this.invalid && !filePermission.invalid && this.mask == filePermission.mask && this.allFiles == filePermission.allFiles && this.cpath.equals(filePermission.cpath) && this.directory == filePermission.directory && this.recursive == filePermission.recursive;
    }

    @Override // java.security.Permission
    public int hashCode() {
        return 0;
    }

    private static int getMask(String str) {
        int i2;
        char c2;
        int i3 = 0;
        if (str == null) {
            return 0;
        }
        if (str == "read") {
            return 4;
        }
        if (str == "write") {
            return 2;
        }
        if (str == SecurityConstants.FILE_EXECUTE_ACTION) {
            return 1;
        }
        if (str == SecurityConstants.FILE_DELETE_ACTION) {
            return 8;
        }
        if (str == SecurityConstants.FILE_READLINK_ACTION) {
            return 16;
        }
        char[] charArray = str.toCharArray();
        int length = charArray.length - 1;
        if (length < 0) {
            return 0;
        }
        while (length != -1) {
            while (length != -1 && ((c2 = charArray[length]) == ' ' || c2 == '\r' || c2 == '\n' || c2 == '\f' || c2 == '\t')) {
                length--;
            }
            if (length >= 3 && ((charArray[length - 3] == 'r' || charArray[length - 3] == 'R') && ((charArray[length - 2] == 'e' || charArray[length - 2] == 'E') && ((charArray[length - 1] == 'a' || charArray[length - 1] == 'A') && (charArray[length] == 'd' || charArray[length] == 'D'))))) {
                i2 = 4;
                i3 |= 4;
            } else if (length >= 4 && ((charArray[length - 4] == 'w' || charArray[length - 4] == 'W') && ((charArray[length - 3] == 'r' || charArray[length - 3] == 'R') && ((charArray[length - 2] == 'i' || charArray[length - 2] == 'I') && ((charArray[length - 1] == 't' || charArray[length - 1] == 'T') && (charArray[length] == 'e' || charArray[length] == 'E')))))) {
                i2 = 5;
                i3 |= 2;
            } else if (length >= 6 && ((charArray[length - 6] == 'e' || charArray[length - 6] == 'E') && ((charArray[length - 5] == 'x' || charArray[length - 5] == 'X') && ((charArray[length - 4] == 'e' || charArray[length - 4] == 'E') && ((charArray[length - 3] == 'c' || charArray[length - 3] == 'C') && ((charArray[length - 2] == 'u' || charArray[length - 2] == 'U') && ((charArray[length - 1] == 't' || charArray[length - 1] == 'T') && (charArray[length] == 'e' || charArray[length] == 'E')))))))) {
                i2 = 7;
                i3 |= 1;
            } else if (length >= 5 && ((charArray[length - 5] == 'd' || charArray[length - 5] == 'D') && ((charArray[length - 4] == 'e' || charArray[length - 4] == 'E') && ((charArray[length - 3] == 'l' || charArray[length - 3] == 'L') && ((charArray[length - 2] == 'e' || charArray[length - 2] == 'E') && ((charArray[length - 1] == 't' || charArray[length - 1] == 'T') && (charArray[length] == 'e' || charArray[length] == 'E'))))))) {
                i2 = 6;
                i3 |= 8;
            } else if (length >= 7 && ((charArray[length - 7] == 'r' || charArray[length - 7] == 'R') && ((charArray[length - 6] == 'e' || charArray[length - 6] == 'E') && ((charArray[length - 5] == 'a' || charArray[length - 5] == 'A') && ((charArray[length - 4] == 'd' || charArray[length - 4] == 'D') && ((charArray[length - 3] == 'l' || charArray[length - 3] == 'L') && ((charArray[length - 2] == 'i' || charArray[length - 2] == 'I') && ((charArray[length - 1] == 'n' || charArray[length - 1] == 'N') && (charArray[length] == 'k' || charArray[length] == 'K'))))))))) {
                i2 = 8;
                i3 |= 16;
            } else {
                throw new IllegalArgumentException("invalid permission: " + str);
            }
            boolean z2 = false;
            while (length >= i2 && !z2) {
                switch (charArray[length - i2]) {
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        break;
                    case ',':
                        z2 = true;
                        break;
                    default:
                        throw new IllegalArgumentException("invalid permission: " + str);
                }
                length--;
            }
            length -= i2;
        }
        return i3;
    }

    int getMask() {
        return this.mask;
    }

    private static String getActions(int i2) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = false;
        if ((i2 & 4) == 4) {
            z2 = true;
            sb.append("read");
        }
        if ((i2 & 2) == 2) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("write");
        }
        if ((i2 & 1) == 1) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append(SecurityConstants.FILE_EXECUTE_ACTION);
        }
        if ((i2 & 8) == 8) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append(SecurityConstants.FILE_DELETE_ACTION);
        }
        if ((i2 & 16) == 16) {
            if (z2) {
                sb.append(',');
            }
            sb.append(SecurityConstants.FILE_READLINK_ACTION);
        }
        return sb.toString();
    }

    @Override // java.security.Permission
    public String getActions() {
        if (this.actions == null) {
            this.actions = getActions(this.mask);
        }
        return this.actions;
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new FilePermissionCollection();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.actions == null) {
            getActions();
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(getMask(this.actions));
    }
}
