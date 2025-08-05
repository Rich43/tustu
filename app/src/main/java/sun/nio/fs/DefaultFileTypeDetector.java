package sun.nio.fs;

import java.nio.file.spi.FileTypeDetector;

/* loaded from: rt.jar:sun/nio/fs/DefaultFileTypeDetector.class */
public class DefaultFileTypeDetector {
    private DefaultFileTypeDetector() {
    }

    public static FileTypeDetector create() {
        return new RegistryFileTypeDetector();
    }
}
