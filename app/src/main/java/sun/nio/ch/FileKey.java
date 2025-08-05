package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;

/* loaded from: rt.jar:sun/nio/ch/FileKey.class */
public class FileKey {
    private long dwVolumeSerialNumber;
    private long nFileIndexHigh;
    private long nFileIndexLow;

    private native void init(FileDescriptor fileDescriptor) throws IOException;

    private static native void initIDs();

    private FileKey() {
    }

    public static FileKey create(FileDescriptor fileDescriptor) {
        FileKey fileKey = new FileKey();
        try {
            fileKey.init(fileDescriptor);
            return fileKey;
        } catch (IOException e2) {
            throw new Error(e2);
        }
    }

    public int hashCode() {
        return ((int) (this.dwVolumeSerialNumber ^ (this.dwVolumeSerialNumber >>> 32))) + ((int) (this.nFileIndexHigh ^ (this.nFileIndexHigh >>> 32))) + ((int) (this.nFileIndexLow ^ (this.nFileIndexHigh >>> 32)));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FileKey)) {
            return false;
        }
        FileKey fileKey = (FileKey) obj;
        if (this.dwVolumeSerialNumber != fileKey.dwVolumeSerialNumber || this.nFileIndexHigh != fileKey.nFileIndexHigh || this.nFileIndexLow != fileKey.nFileIndexLow) {
            return false;
        }
        return true;
    }

    static {
        IOUtil.load();
        initIDs();
    }
}
