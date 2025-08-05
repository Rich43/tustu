package java.nio.file.spi;

import java.io.IOException;
import java.nio.file.Path;

/* loaded from: rt.jar:java/nio/file/spi/FileTypeDetector.class */
public abstract class FileTypeDetector {
    public abstract String probeContentType(Path path) throws IOException;

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("fileTypeDetector"));
            return null;
        }
        return null;
    }

    private FileTypeDetector(Void r3) {
    }

    protected FileTypeDetector() {
        this(checkPermission());
    }
}
