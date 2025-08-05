package java.lang.invoke;

import java.io.FilePermission;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Objects;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/lang/invoke/ProxyClassesDumper.class */
final class ProxyClassesDumper {
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] BAD_CHARS = {'\\', ':', '*', '?', '\"', '<', '>', '|'};
    private static final String[] REPLACEMENT = {"%5C", "%3A", "%2A", "%3F", "%22", "%3C", "%3E", "%7C"};
    private final Path dumpDir;

    public static ProxyClassesDumper getInstance(String str) {
        if (null == str) {
            return null;
        }
        try {
            str = str.trim();
            final Path path = Paths.get(str.length() == 0 ? "." : str, new String[0]);
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.invoke.ProxyClassesDumper.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    ProxyClassesDumper.validateDumpDir(path);
                    return null;
                }
            }, (AccessControlContext) null, new FilePermission("<<ALL FILES>>", "read, write"));
            return new ProxyClassesDumper(path);
        } catch (InvalidPathException e2) {
            PlatformLogger.getLogger(ProxyClassesDumper.class.getName()).warning("Path " + str + " is not valid - dumping disabled", e2);
            return null;
        } catch (IllegalArgumentException e3) {
            PlatformLogger.getLogger(ProxyClassesDumper.class.getName()).warning(e3.getMessage() + " - dumping disabled");
            return null;
        }
    }

    private ProxyClassesDumper(Path path) {
        this.dumpDir = (Path) Objects.requireNonNull(path);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateDumpDir(Path path) {
        if (!Files.exists(path, new LinkOption[0])) {
            throw new IllegalArgumentException("Directory " + ((Object) path) + " does not exist");
        }
        if (!Files.isDirectory(path, new LinkOption[0])) {
            throw new IllegalArgumentException("Path " + ((Object) path) + " is not a directory");
        }
        if (!Files.isWritable(path)) {
            throw new IllegalArgumentException("Directory " + ((Object) path) + " is not writable");
        }
    }

    public static String encodeForFilename(String str) {
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt <= 31) {
                sb.append('%');
                sb.append(HEX[(cCharAt >> 4) & 15]);
                sb.append(HEX[cCharAt & 15]);
            } else {
                int i3 = 0;
                while (true) {
                    if (i3 >= BAD_CHARS.length) {
                        break;
                    }
                    if (cCharAt != BAD_CHARS[i3]) {
                        i3++;
                    } else {
                        sb.append(REPLACEMENT[i3]);
                        break;
                    }
                }
                if (i3 >= BAD_CHARS.length) {
                    sb.append(cCharAt);
                }
            }
        }
        return sb.toString();
    }

    public void dumpClass(String str, byte[] bArr) {
        try {
            Path pathResolve = this.dumpDir.resolve(encodeForFilename(str) + ".class");
            try {
                Files.createDirectories(pathResolve.getParent(), new FileAttribute[0]);
                Files.write(pathResolve, bArr, new OpenOption[0]);
            } catch (Exception e2) {
                PlatformLogger.getLogger(ProxyClassesDumper.class.getName()).warning("Exception writing to path at " + pathResolve.toString());
            }
        } catch (InvalidPathException e3) {
            PlatformLogger.getLogger(ProxyClassesDumper.class.getName()).warning("Invalid path for class " + str);
        }
    }
}
