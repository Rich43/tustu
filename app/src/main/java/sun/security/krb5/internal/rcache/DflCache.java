package sun.security.krb5.internal.rcache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import sun.security.action.GetPropertyAction;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.ReplayCache;

/* loaded from: rt.jar:sun/security/krb5/internal/rcache/DflCache.class */
public class DflCache extends ReplayCache {
    private static final int KRB5_RV_VNO = 1281;
    private static final int EXCESSREPS = 30;
    private final String source;
    private static int uid;

    static {
        try {
            Class<?> cls = Class.forName("com.sun.security.auth.module.UnixSystem");
            uid = (int) ((Long) cls.getMethod("getUid", new Class[0]).invoke(cls.newInstance(), new Object[0])).longValue();
        } catch (Exception e2) {
            uid = -1;
        }
    }

    public DflCache(String str) {
        this.source = str;
    }

    private static String defaultPath() {
        return (String) AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir"));
    }

    private static String defaultFile(String str) {
        int iIndexOf = str.indexOf(47);
        if (iIndexOf == -1) {
            iIndexOf = str.indexOf(64);
        }
        if (iIndexOf != -1) {
            str = str.substring(0, iIndexOf);
        }
        if (uid != -1) {
            str = str + "_" + uid;
        }
        return str;
    }

    private static Path getFileName(String str, String str2) {
        String strDefaultPath;
        String strDefaultFile;
        if (str.equals("dfl")) {
            strDefaultPath = defaultPath();
            strDefaultFile = defaultFile(str2);
        } else if (str.startsWith("dfl:")) {
            String strSubstring = str.substring(4);
            int iLastIndexOf = strSubstring.lastIndexOf(47);
            int iLastIndexOf2 = strSubstring.lastIndexOf(92);
            if (iLastIndexOf2 > iLastIndexOf) {
                iLastIndexOf = iLastIndexOf2;
            }
            if (iLastIndexOf == -1) {
                strDefaultPath = defaultPath();
                strDefaultFile = strSubstring;
            } else if (new File(strSubstring).isDirectory()) {
                strDefaultPath = strSubstring;
                strDefaultFile = defaultFile(str2);
            } else {
                strDefaultPath = null;
                strDefaultFile = strSubstring;
            }
        } else {
            throw new IllegalArgumentException();
        }
        return new File(strDefaultPath, strDefaultFile).toPath();
    }

    @Override // sun.security.krb5.internal.ReplayCache
    public void checkAndStore(KerberosTime kerberosTime, AuthTimeWithHash authTimeWithHash) throws KrbApErrException {
        try {
            checkAndStore0(kerberosTime, authTimeWithHash);
        } catch (IOException e2) {
            KrbApErrException krbApErrException = new KrbApErrException(60);
            krbApErrException.initCause(e2);
            throw krbApErrException;
        }
    }

    private synchronized void checkAndStore0(KerberosTime kerberosTime, AuthTimeWithHash authTimeWithHash) throws KrbApErrException, IOException {
        int iLoadAndCheck;
        Path fileName = getFileName(this.source, authTimeWithHash.server);
        Storage storage = new Storage();
        Throwable th = null;
        try {
            try {
                try {
                    iLoadAndCheck = storage.loadAndCheck(fileName, authTimeWithHash, kerberosTime);
                } catch (Throwable th2) {
                    th = th2;
                    throw th2;
                }
            } catch (Throwable th3) {
                if (storage != null) {
                    if (th != null) {
                        try {
                            storage.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        storage.close();
                    }
                }
                throw th3;
            }
        } catch (IOException e2) {
            Storage.create(fileName);
            iLoadAndCheck = storage.loadAndCheck(fileName, authTimeWithHash, kerberosTime);
        }
        storage.append(authTimeWithHash);
        if (storage != null) {
            if (0 != 0) {
                try {
                    storage.close();
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                }
            } else {
                storage.close();
            }
        }
        if (iLoadAndCheck <= 30) {
            return;
        }
        Storage.expunge(fileName, kerberosTime);
    }

    /* loaded from: rt.jar:sun/security/krb5/internal/rcache/DflCache$Storage.class */
    private static class Storage implements Closeable {
        SeekableByteChannel chan;

        private Storage() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void create(Path path) throws IOException {
            SeekableByteChannel seekableByteChannelCreateNoClose = createNoClose(path);
            Throwable th = null;
            if (seekableByteChannelCreateNoClose != null) {
                if (0 != 0) {
                    try {
                        seekableByteChannelCreateNoClose.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    seekableByteChannelCreateNoClose.close();
                }
            }
            makeMine(path);
        }

        private static void makeMine(Path path) throws IOException {
            try {
                HashSet hashSet = new HashSet();
                hashSet.add(PosixFilePermission.OWNER_READ);
                hashSet.add(PosixFilePermission.OWNER_WRITE);
                Files.setPosixFilePermissions(path, hashSet);
            } catch (UnsupportedOperationException e2) {
            }
        }

        private static SeekableByteChannel createNoClose(Path path) throws IOException {
            SeekableByteChannel seekableByteChannelNewByteChannel = Files.newByteChannel(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(6);
            byteBufferAllocate.putShort((short) 1281);
            byteBufferAllocate.order(ByteOrder.nativeOrder());
            byteBufferAllocate.putInt(KerberosTime.getDefaultSkew());
            byteBufferAllocate.flip();
            seekableByteChannelNewByteChannel.write(byteBufferAllocate);
            return seekableByteChannelNewByteChannel;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void expunge(Path path, KerberosTime kerberosTime) throws IOException {
            Path pathCreateTempFile = Files.createTempFile(path.getParent(), "rcache", null, new FileAttribute[0]);
            SeekableByteChannel seekableByteChannelNewByteChannel = Files.newByteChannel(path, new OpenOption[0]);
            Throwable th = null;
            try {
                SeekableByteChannel seekableByteChannelCreateNoClose = createNoClose(pathCreateTempFile);
                Throwable th2 = null;
                try {
                    try {
                        long seconds = kerberosTime.getSeconds() - readHeader(seekableByteChannelNewByteChannel);
                        while (true) {
                            try {
                                AuthTime from = AuthTime.readFrom(seekableByteChannelNewByteChannel);
                                if (from.ctime > seconds) {
                                    seekableByteChannelCreateNoClose.write(ByteBuffer.wrap(from.encode(true)));
                                }
                            } catch (BufferUnderflowException e2) {
                                if (seekableByteChannelCreateNoClose != null) {
                                    if (0 != 0) {
                                        try {
                                            seekableByteChannelCreateNoClose.close();
                                        } catch (Throwable th3) {
                                            th2.addSuppressed(th3);
                                        }
                                    } else {
                                        seekableByteChannelCreateNoClose.close();
                                    }
                                }
                                makeMine(pathCreateTempFile);
                                Files.move(pathCreateTempFile, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                                return;
                            }
                        }
                    } catch (Throwable th4) {
                        if (seekableByteChannelCreateNoClose != null) {
                            if (th2 != null) {
                                try {
                                    seekableByteChannelCreateNoClose.close();
                                } catch (Throwable th5) {
                                    th2.addSuppressed(th5);
                                }
                            } else {
                                seekableByteChannelCreateNoClose.close();
                            }
                        }
                        throw th4;
                    }
                } finally {
                }
            } finally {
                if (seekableByteChannelNewByteChannel != null) {
                    if (0 != 0) {
                        try {
                            seekableByteChannelNewByteChannel.close();
                        } catch (Throwable th6) {
                            th.addSuppressed(th6);
                        }
                    } else {
                        seekableByteChannelNewByteChannel.close();
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int loadAndCheck(Path path, AuthTimeWithHash authTimeWithHash, KerberosTime kerberosTime) throws KrbApErrException, IOException {
            Set<PosixFilePermission> posixFilePermissions;
            int i2 = 0;
            if (Files.isSymbolicLink(path)) {
                throw new IOException("Symlink not accepted");
            }
            try {
                posixFilePermissions = Files.getPosixFilePermissions(path, new LinkOption[0]);
            } catch (UnsupportedOperationException e2) {
            }
            if (DflCache.uid != -1 && ((Integer) Files.getAttribute(path, "unix:uid", new LinkOption[0])).intValue() != DflCache.uid) {
                throw new IOException("Not mine");
            }
            if (posixFilePermissions.contains(PosixFilePermission.GROUP_READ) || posixFilePermissions.contains(PosixFilePermission.GROUP_WRITE) || posixFilePermissions.contains(PosixFilePermission.GROUP_EXECUTE) || posixFilePermissions.contains(PosixFilePermission.OTHERS_READ) || posixFilePermissions.contains(PosixFilePermission.OTHERS_WRITE) || posixFilePermissions.contains(PosixFilePermission.OTHERS_EXECUTE)) {
                throw new IOException("Accessible by someone else");
            }
            this.chan = Files.newByteChannel(path, StandardOpenOption.WRITE, StandardOpenOption.READ);
            long seconds = kerberosTime.getSeconds() - readHeader(this.chan);
            long jPosition = 0;
            boolean z2 = false;
            while (true) {
                try {
                    jPosition = this.chan.position();
                    AuthTime from = AuthTime.readFrom(this.chan);
                    if (from instanceof AuthTimeWithHash) {
                        if (authTimeWithHash.equals(from)) {
                            throw new KrbApErrException(34);
                        }
                        if (authTimeWithHash.isSameIgnoresHash(from)) {
                            z2 = true;
                        }
                    } else if (authTimeWithHash.isSameIgnoresHash(from) && !z2) {
                        throw new KrbApErrException(34);
                    }
                    if (from.ctime < seconds) {
                        i2++;
                    } else {
                        i2--;
                    }
                } catch (BufferUnderflowException e3) {
                    this.chan.position(jPosition);
                    return i2;
                }
            }
        }

        private static int readHeader(SeekableByteChannel seekableByteChannel) throws IOException {
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(6);
            seekableByteChannel.read(byteBufferAllocate);
            if (byteBufferAllocate.getShort(0) != 1281) {
                throw new IOException("Not correct rcache version");
            }
            byteBufferAllocate.order(ByteOrder.nativeOrder());
            return byteBufferAllocate.getInt(2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void append(AuthTimeWithHash authTimeWithHash) throws IOException {
            this.chan.write(ByteBuffer.wrap(authTimeWithHash.encode(true)));
            this.chan.write(ByteBuffer.wrap(authTimeWithHash.encode(false)));
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.chan != null) {
                this.chan.close();
            }
            this.chan = null;
        }
    }
}
