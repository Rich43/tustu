package javafx.scene.web;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: jfxrt.jar:javafx/scene/web/DirectoryLock.class */
final class DirectoryLock {
    private static final Logger logger = Logger.getLogger(DirectoryLock.class.getName());
    private static final Map<File, Descriptor> descriptors = new HashMap();
    private Descriptor descriptor;

    DirectoryLock(File directory) throws IOException, DirectoryAlreadyInUseException {
        File directory2 = canonicalize(directory);
        this.descriptor = descriptors.get(directory2);
        if (this.descriptor == null) {
            File lockFile = lockFile(directory2);
            RandomAccessFile lockRaf = new RandomAccessFile(lockFile, InternalZipConstants.WRITE_MODE);
            try {
                try {
                    FileLock lock = lockRaf.getChannel().tryLock();
                    if (lock == null) {
                        throw new DirectoryAlreadyInUseException(directory2.toString(), null);
                    }
                    this.descriptor = new Descriptor(directory2, lockRaf, lock);
                    descriptors.put(directory2, this.descriptor);
                    if (this.descriptor == null) {
                        try {
                            lockRaf.close();
                        } catch (IOException ex) {
                            logger.log(Level.WARNING, String.format("Error closing [%s]", lockFile), (Throwable) ex);
                        }
                    }
                } catch (OverlappingFileLockException ex2) {
                    throw new DirectoryAlreadyInUseException(directory2.toString(), ex2);
                }
            } catch (Throwable th) {
                if (this.descriptor == null) {
                    try {
                        lockRaf.close();
                    } catch (IOException ex3) {
                        logger.log(Level.WARNING, String.format("Error closing [%s]", lockFile), (Throwable) ex3);
                    }
                }
                throw th;
            }
        }
        Descriptor.access$108(this.descriptor);
    }

    void close() {
        if (this.descriptor == null) {
            return;
        }
        Descriptor.access$110(this.descriptor);
        if (this.descriptor.referenceCount == 0) {
            try {
                this.descriptor.lock.release();
            } catch (IOException ex) {
                logger.log(Level.WARNING, String.format("Error releasing lock on [%s]", lockFile(this.descriptor.directory)), (Throwable) ex);
            }
            try {
                this.descriptor.lockRaf.close();
            } catch (IOException ex2) {
                logger.log(Level.WARNING, String.format("Error closing [%s]", lockFile(this.descriptor.directory)), (Throwable) ex2);
            }
            descriptors.remove(this.descriptor.directory);
        }
        this.descriptor = null;
    }

    static int referenceCount(File directory) throws IOException {
        Descriptor d2 = descriptors.get(canonicalize(directory));
        if (d2 == null) {
            return 0;
        }
        return d2.referenceCount;
    }

    static File canonicalize(File directory) throws IOException {
        String path = directory.getCanonicalPath();
        if (path.length() > 0 && path.charAt(path.length() - 1) != File.separatorChar) {
            path = path + File.separatorChar;
        }
        return new File(path);
    }

    private static File lockFile(File directory) {
        return new File(directory, ".lock");
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/DirectoryLock$Descriptor.class */
    private static class Descriptor {
        private final File directory;
        private final RandomAccessFile lockRaf;
        private final FileLock lock;
        private int referenceCount;

        static /* synthetic */ int access$108(Descriptor x0) {
            int i2 = x0.referenceCount;
            x0.referenceCount = i2 + 1;
            return i2;
        }

        static /* synthetic */ int access$110(Descriptor x0) {
            int i2 = x0.referenceCount;
            x0.referenceCount = i2 - 1;
            return i2;
        }

        private Descriptor(File directory, RandomAccessFile lockRaf, FileLock lock) {
            this.directory = directory;
            this.lockRaf = lockRaf;
            this.lock = lock;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/DirectoryLock$DirectoryAlreadyInUseException.class */
    final class DirectoryAlreadyInUseException extends Exception {
        DirectoryAlreadyInUseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
