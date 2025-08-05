package java.nio.file;

import java.io.IOException;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.AccessController;
import java.security.SecureRandom;
import java.util.EnumSet;
import java.util.Set;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/nio/file/TempFileHelper.class */
class TempFileHelper {
    private static final Path tmpdir = Paths.get((String) AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")), new String[0]);
    private static final boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
    private static final SecureRandom random = new SecureRandom();

    private TempFileHelper() {
    }

    private static Path generatePath(String str, String str2, Path path) {
        long jNextLong = random.nextLong();
        Path path2 = path.getFileSystem().getPath(str + Long.toString(jNextLong == Long.MIN_VALUE ? 0L : Math.abs(jNextLong)) + str2, new String[0]);
        if (path2.getParent() != null) {
            throw new IllegalArgumentException("Invalid prefix or suffix");
        }
        return path.resolve(path2);
    }

    /* loaded from: rt.jar:java/nio/file/TempFileHelper$PosixPermissions.class */
    private static class PosixPermissions {
        static final FileAttribute<Set<PosixFilePermission>> filePermissions = PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE));
        static final FileAttribute<Set<PosixFilePermission>> dirPermissions = PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE));

        private PosixPermissions() {
        }
    }

    private static Path create(Path path, String str, String str2, boolean z2, FileAttribute<?>[] fileAttributeArr) throws IOException {
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = z2 ? "" : ".tmp";
        }
        if (path == null) {
            path = tmpdir;
        }
        if (isPosix && path.getFileSystem() == FileSystems.getDefault()) {
            if (fileAttributeArr.length == 0) {
                fileAttributeArr = new FileAttribute[1];
                fileAttributeArr[0] = z2 ? PosixPermissions.dirPermissions : PosixPermissions.filePermissions;
            } else {
                boolean z3 = false;
                int i2 = 0;
                while (true) {
                    if (i2 >= fileAttributeArr.length) {
                        break;
                    }
                    if (!fileAttributeArr[i2].name().equals("posix:permissions")) {
                        i2++;
                    } else {
                        z3 = true;
                        break;
                    }
                }
                if (!z3) {
                    FileAttribute<?>[] fileAttributeArr2 = new FileAttribute[fileAttributeArr.length + 1];
                    System.arraycopy(fileAttributeArr, 0, fileAttributeArr2, 0, fileAttributeArr.length);
                    fileAttributeArr = fileAttributeArr2;
                    fileAttributeArr[fileAttributeArr.length - 1] = z2 ? PosixPermissions.dirPermissions : PosixPermissions.filePermissions;
                }
            }
        }
        SecurityManager securityManager = System.getSecurityManager();
        while (true) {
            try {
                Path pathGeneratePath = generatePath(str, str2, path);
                try {
                    if (z2) {
                        return Files.createDirectory(pathGeneratePath, fileAttributeArr);
                    }
                    return Files.createFile(pathGeneratePath, fileAttributeArr);
                } catch (SecurityException e2) {
                    if (path == tmpdir && securityManager != null) {
                        throw new SecurityException("Unable to create temporary file or directory");
                    }
                    throw e2;
                } catch (FileAlreadyExistsException e3) {
                }
            } catch (InvalidPathException e4) {
                if (securityManager != null) {
                    throw new IllegalArgumentException("Invalid prefix or suffix");
                }
                throw e4;
            }
        }
    }

    static Path createTempFile(Path path, String str, String str2, FileAttribute<?>[] fileAttributeArr) throws IOException {
        return create(path, str, str2, false, fileAttributeArr);
    }

    static Path createTempDirectory(Path path, String str, FileAttribute<?>[] fileAttributeArr) throws IOException {
        return create(path, str, null, true, fileAttributeArr);
    }
}
