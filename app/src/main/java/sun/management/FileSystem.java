package sun.management;

import java.io.File;
import java.io.IOException;

/* loaded from: rt.jar:sun/management/FileSystem.class */
public abstract class FileSystem {
    private static final Object lock = new Object();
    private static FileSystem fs;

    public abstract boolean supportsFileSecurity(File file) throws IOException;

    public abstract boolean isAccessUserOnly(File file) throws IOException;

    protected FileSystem() {
    }

    public static FileSystem open() {
        FileSystem fileSystem;
        synchronized (lock) {
            if (fs == null) {
                fs = new FileSystemImpl();
            }
            fileSystem = fs;
        }
        return fileSystem;
    }
}
