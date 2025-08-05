package sun.nio.fs;

import com.sun.nio.file.ExtendedOpenOption;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;
import sun.nio.ch.FileChannelImpl;
import sun.nio.ch.ThreadPool;
import sun.nio.ch.WindowsAsynchronousFileChannelImpl;

/* loaded from: rt.jar:sun/nio/fs/WindowsChannelFactory.class */
class WindowsChannelFactory {
    private static final JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
    static final OpenOption OPEN_REPARSE_POINT = new OpenOption() { // from class: sun.nio.fs.WindowsChannelFactory.1
    };

    private WindowsChannelFactory() {
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsChannelFactory$Flags.class */
    private static class Flags {
        boolean read;
        boolean write;
        boolean append;
        boolean truncateExisting;
        boolean create;
        boolean createNew;
        boolean deleteOnClose;
        boolean sparse;
        boolean overlapped;
        boolean sync;
        boolean dsync;
        boolean shareRead = true;
        boolean shareWrite = true;
        boolean shareDelete = true;
        boolean noFollowLinks;
        boolean openReparsePoint;

        private Flags() {
        }

        static Flags toFlags(Set<? extends OpenOption> set) {
            Flags flags = new Flags();
            for (OpenOption openOption : set) {
                if (openOption instanceof StandardOpenOption) {
                    switch ((StandardOpenOption) openOption) {
                        case READ:
                            flags.read = true;
                            break;
                        case WRITE:
                            flags.write = true;
                            break;
                        case APPEND:
                            flags.append = true;
                            break;
                        case TRUNCATE_EXISTING:
                            flags.truncateExisting = true;
                            break;
                        case CREATE:
                            flags.create = true;
                            break;
                        case CREATE_NEW:
                            flags.createNew = true;
                            break;
                        case DELETE_ON_CLOSE:
                            flags.deleteOnClose = true;
                            break;
                        case SPARSE:
                            flags.sparse = true;
                            break;
                        case SYNC:
                            flags.sync = true;
                            break;
                        case DSYNC:
                            flags.dsync = true;
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                } else if (openOption instanceof ExtendedOpenOption) {
                    switch ((ExtendedOpenOption) openOption) {
                        case NOSHARE_READ:
                            flags.shareRead = false;
                            break;
                        case NOSHARE_WRITE:
                            flags.shareWrite = false;
                            break;
                        case NOSHARE_DELETE:
                            flags.shareDelete = false;
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                } else if (openOption == LinkOption.NOFOLLOW_LINKS) {
                    flags.noFollowLinks = true;
                } else if (openOption == WindowsChannelFactory.OPEN_REPARSE_POINT) {
                    flags.openReparsePoint = true;
                } else {
                    if (openOption == null) {
                        throw new NullPointerException();
                    }
                    throw new UnsupportedOperationException();
                }
            }
            return flags;
        }
    }

    static FileChannel newFileChannel(String str, String str2, Set<? extends OpenOption> set, long j2) throws WindowsException {
        Flags flags = Flags.toFlags(set);
        if (!flags.read && !flags.write) {
            if (flags.append) {
                flags.write = true;
            } else {
                flags.read = true;
            }
        }
        if (flags.read && flags.append) {
            throw new IllegalArgumentException("READ + APPEND not allowed");
        }
        if (flags.append && flags.truncateExisting) {
            throw new IllegalArgumentException("APPEND + TRUNCATE_EXISTING not allowed");
        }
        return FileChannelImpl.open(open(str, str2, flags, j2), str, flags.read, flags.write, flags.append, null);
    }

    static AsynchronousFileChannel newAsynchronousFileChannel(String str, String str2, Set<? extends OpenOption> set, long j2, ThreadPool threadPool) throws IOException {
        Flags flags = Flags.toFlags(set);
        flags.overlapped = true;
        if (!flags.read && !flags.write) {
            flags.read = true;
        }
        if (flags.append) {
            throw new UnsupportedOperationException("APPEND not allowed");
        }
        try {
            FileDescriptor fileDescriptorOpen = open(str, str2, flags, j2);
            try {
                return WindowsAsynchronousFileChannelImpl.open(fileDescriptorOpen, flags.read, flags.write, threadPool);
            } catch (IOException e2) {
                WindowsNativeDispatcher.CloseHandle(fdAccess.getHandle(fileDescriptorOpen));
                throw e2;
            }
        } catch (WindowsException e3) {
            e3.rethrowAsIOException(str);
            return null;
        }
    }

    private static FileDescriptor open(String str, String str2, Flags flags, long j2) throws WindowsException {
        SecurityManager securityManager;
        boolean z2 = false;
        int i2 = 0;
        if (flags.read) {
            i2 = 0 | Integer.MIN_VALUE;
        }
        if (flags.write) {
            i2 |= 1073741824;
        }
        int i3 = 0;
        if (flags.shareRead) {
            i3 = 0 | 1;
        }
        if (flags.shareWrite) {
            i3 |= 2;
        }
        if (flags.shareDelete) {
            i3 |= 4;
        }
        int i4 = 128;
        int i5 = 3;
        if (flags.write) {
            if (flags.createNew) {
                i5 = 1;
                i4 = 128 | 2097152;
            } else {
                if (flags.create) {
                    i5 = 4;
                }
                if (flags.truncateExisting) {
                    if (i5 == 4) {
                        z2 = true;
                    } else {
                        i5 = 5;
                    }
                }
            }
        }
        if (flags.dsync || flags.sync) {
            i4 |= Integer.MIN_VALUE;
        }
        if (flags.overlapped) {
            i4 |= 1073741824;
        }
        if (flags.deleteOnClose) {
            i4 |= 67108864;
        }
        boolean z3 = true;
        if (i5 != 1 && (flags.noFollowLinks || flags.openReparsePoint || flags.deleteOnClose)) {
            if (flags.noFollowLinks || flags.deleteOnClose) {
                z3 = false;
            }
            i4 |= 2097152;
        }
        if (str2 != null && (securityManager = System.getSecurityManager()) != null) {
            if (flags.read) {
                securityManager.checkRead(str2);
            }
            if (flags.write) {
                securityManager.checkWrite(str2);
            }
            if (flags.deleteOnClose) {
                securityManager.checkDelete(str2);
            }
        }
        long jCreateFile = WindowsNativeDispatcher.CreateFile(str, i2, i3, j2, i5, i4);
        if (!z3) {
            try {
                if (WindowsFileAttributes.readAttributes(jCreateFile).isSymbolicLink()) {
                    throw new WindowsException("File is symbolic link");
                }
            } catch (WindowsException e2) {
                WindowsNativeDispatcher.CloseHandle(jCreateFile);
                throw e2;
            }
        }
        if (z2) {
            try {
                WindowsNativeDispatcher.SetEndOfFile(jCreateFile);
            } catch (WindowsException e3) {
                WindowsNativeDispatcher.CloseHandle(jCreateFile);
                throw e3;
            }
        }
        if (i5 == 1 && flags.sparse) {
            try {
                WindowsNativeDispatcher.DeviceIoControlSetSparse(jCreateFile);
            } catch (WindowsException e4) {
            }
        }
        FileDescriptor fileDescriptor = new FileDescriptor();
        fdAccess.setHandle(fileDescriptor, jCreateFile);
        return fileDescriptor;
    }
}
