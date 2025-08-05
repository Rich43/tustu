package sun.nio.fs;

import java.nio.file.spi.FileSystemProvider;

/* loaded from: rt.jar:sun/nio/fs/DefaultFileSystemProvider.class */
public class DefaultFileSystemProvider {
    private DefaultFileSystemProvider() {
    }

    public static FileSystemProvider create() {
        return new WindowsFileSystemProvider();
    }
}
