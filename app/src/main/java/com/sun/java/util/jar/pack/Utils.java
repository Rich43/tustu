package com.sun.java.util.jar.pack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Utils.class */
class Utils {
    static final String COM_PREFIX = "com.sun.java.util.jar.pack.";
    static final String METAINF = "META-INF";
    static final String DEBUG_VERBOSE = "com.sun.java.util.jar.pack.verbose";
    static final String DEBUG_DISABLE_NATIVE = "com.sun.java.util.jar.pack.disable.native";
    static final String PACK_DEFAULT_TIMEZONE = "com.sun.java.util.jar.pack.default.timezone";
    static final String UNPACK_MODIFICATION_TIME = "com.sun.java.util.jar.pack.unpack.modification.time";
    static final String UNPACK_STRIP_DEBUG = "com.sun.java.util.jar.pack.unpack.strip.debug";
    static final String UNPACK_REMOVE_PACKFILE = "com.sun.java.util.jar.pack.unpack.remove.packfile";
    static final String NOW = "now";
    static final String PACK_KEEP_CLASS_ORDER = "com.sun.java.util.jar.pack.keep.class.order";
    static final String PACK_ZIP_ARCHIVE_MARKER_COMMENT = "PACK200";
    static final String CLASS_FORMAT_ERROR = "com.sun.java.util.jar.pack.class.format.error";
    private static TimeZone tz;
    static final ThreadLocal<TLGlobals> currentInstance = new ThreadLocal<>();
    private static int workingPackerCount = 0;
    static final boolean nolog = Boolean.getBoolean("com.sun.java.util.jar.pack.nolog");
    static final boolean SORT_MEMBERS_DESCR_MAJOR = Boolean.getBoolean("com.sun.java.util.jar.pack.sort.members.descr.major");
    static final boolean SORT_HANDLES_KIND_MAJOR = Boolean.getBoolean("com.sun.java.util.jar.pack.sort.handles.kind.major");
    static final boolean SORT_INDY_BSS_MAJOR = Boolean.getBoolean("com.sun.java.util.jar.pack.sort.indy.bss.major");
    static final boolean SORT_BSS_BSM_MAJOR = Boolean.getBoolean("com.sun.java.util.jar.pack.sort.bss.bsm.major");
    static final Pack200Logger log = new Pack200Logger("java.util.jar.Pack200");

    static TLGlobals getTLGlobals() {
        return currentInstance.get();
    }

    static PropMap currentPropMap() {
        TLGlobals tLGlobals = currentInstance.get();
        if (tLGlobals instanceof PackerImpl) {
            return ((PackerImpl) tLGlobals).props;
        }
        if (tLGlobals instanceof UnpackerImpl) {
            return ((UnpackerImpl) tLGlobals).props;
        }
        return null;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Utils$Pack200Logger.class */
    static class Pack200Logger {
        private final String name;
        private PlatformLogger log;

        Pack200Logger(String str) {
            this.name = str;
        }

        private synchronized PlatformLogger getLogger() {
            if (this.log == null) {
                this.log = PlatformLogger.getLogger(this.name);
            }
            return this.log;
        }

        public void warning(String str, Object obj) {
            getLogger().warning(str, obj);
        }

        public void warning(String str) {
            warning(str, null);
        }

        public void info(String str) {
            if (Utils.currentPropMap().getInteger(Utils.DEBUG_VERBOSE) > 0) {
                if (Utils.nolog) {
                    System.out.println(str);
                } else {
                    getLogger().info(str);
                }
            }
        }

        public void fine(String str) {
            if (Utils.currentPropMap().getInteger(Utils.DEBUG_VERBOSE) > 0) {
                System.out.println(str);
            }
        }
    }

    static synchronized void changeDefaultTimeZoneToUtc() {
        int i2 = workingPackerCount;
        workingPackerCount = i2 + 1;
        if (i2 == 0) {
            tz = TimeZone.getDefault();
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        }
    }

    static synchronized void restoreDefaultTimeZone() {
        int i2 = workingPackerCount - 1;
        workingPackerCount = i2;
        if (i2 == 0) {
            if (tz != null) {
                TimeZone.setDefault(tz);
            }
            tz = null;
        }
    }

    static String getVersionString() {
        return "Pack200, Vendor: " + System.getProperty("java.vendor") + ", Version: " + ((Object) Constants.MAX_PACKAGE_VERSION);
    }

    static void markJarFile(JarOutputStream jarOutputStream) throws IOException {
        jarOutputStream.setComment(PACK_ZIP_ARCHIVE_MARKER_COMMENT);
    }

    static void copyJarFile(JarInputStream jarInputStream, JarOutputStream jarOutputStream) throws IOException {
        if (jarInputStream.getManifest() != null) {
            jarOutputStream.putNextEntry(new ZipEntry(JarFile.MANIFEST_NAME));
            jarInputStream.getManifest().write(jarOutputStream);
            jarOutputStream.closeEntry();
        }
        byte[] bArr = new byte[16384];
        while (true) {
            JarEntry nextJarEntry = jarInputStream.getNextJarEntry();
            if (nextJarEntry != null) {
                nextJarEntry.setCompressedSize(-1L);
                jarOutputStream.putNextEntry(nextJarEntry);
                while (true) {
                    int i2 = jarInputStream.read(bArr);
                    if (0 < i2) {
                        jarOutputStream.write(bArr, 0, i2);
                    }
                }
            } else {
                jarInputStream.close();
                markJarFile(jarOutputStream);
                return;
            }
        }
    }

    static void copyJarFile(JarFile jarFile, JarOutputStream jarOutputStream) throws IOException {
        byte[] bArr = new byte[16384];
        Iterator it = Collections.list(jarFile.entries()).iterator();
        while (it.hasNext()) {
            JarEntry jarEntry = (JarEntry) it.next();
            jarEntry.setCompressedSize(-1L);
            jarOutputStream.putNextEntry(jarEntry);
            InputStream inputStream = jarFile.getInputStream(jarEntry);
            while (true) {
                int i2 = inputStream.read(bArr);
                if (0 < i2) {
                    jarOutputStream.write(bArr, 0, i2);
                }
            }
        }
        jarFile.close();
        markJarFile(jarOutputStream);
    }

    static void copyJarFile(JarInputStream jarInputStream, OutputStream outputStream) throws IOException {
        JarOutputStream jarOutputStream = new JarOutputStream(new NonCloser(new BufferedOutputStream(outputStream)));
        Throwable th = null;
        try {
            try {
                copyJarFile(jarInputStream, jarOutputStream);
                if (jarOutputStream != null) {
                    if (0 != 0) {
                        try {
                            jarOutputStream.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    jarOutputStream.close();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (jarOutputStream != null) {
                if (th != null) {
                    try {
                        jarOutputStream.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    jarOutputStream.close();
                }
            }
            throw th4;
        }
    }

    static void copyJarFile(JarFile jarFile, OutputStream outputStream) throws IOException {
        JarOutputStream jarOutputStream = new JarOutputStream(new NonCloser(new BufferedOutputStream(outputStream)));
        Throwable th = null;
        try {
            try {
                copyJarFile(jarFile, jarOutputStream);
                if (jarOutputStream != null) {
                    if (0 != 0) {
                        try {
                            jarOutputStream.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    jarOutputStream.close();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (jarOutputStream != null) {
                if (th != null) {
                    try {
                        jarOutputStream.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    jarOutputStream.close();
                }
            }
            throw th4;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Utils$NonCloser.class */
    private static class NonCloser extends FilterOutputStream {
        NonCloser(OutputStream outputStream) {
            super(outputStream);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            flush();
        }
    }

    static String getJarEntryName(String str) {
        if (str == null) {
            return null;
        }
        return str.replace(File.separatorChar, '/');
    }

    static String zeString(ZipEntry zipEntry) {
        return zipEntry.getSize() + "\t" + zipEntry.getMethod() + "\t" + zipEntry.getCompressedSize() + "\t" + (zipEntry.getCompressedSize() > 0 ? (int) ((1.0d - (zipEntry.getCompressedSize() / zipEntry.getSize())) * 100.0d) : 0) + "%\t" + ((Object) new Date(zipEntry.getTime())) + "\t" + Long.toHexString(zipEntry.getCrc()) + "\t" + zipEntry.getName();
    }

    static byte[] readMagic(BufferedInputStream bufferedInputStream) throws IOException {
        bufferedInputStream.mark(4);
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < bArr.length && 1 == bufferedInputStream.read(bArr, i2, 1); i2++) {
        }
        bufferedInputStream.reset();
        return bArr;
    }

    static boolean isJarMagic(byte[] bArr) {
        return bArr[0] == 80 && bArr[1] == 75 && bArr[2] >= 1 && bArr[2] < 8 && bArr[3] == bArr[2] + 1;
    }

    static boolean isPackMagic(byte[] bArr) {
        return bArr[0] == -54 && bArr[1] == -2 && bArr[2] == -48 && bArr[3] == 13;
    }

    static boolean isGZIPMagic(byte[] bArr) {
        return bArr[0] == 31 && bArr[1] == -117 && bArr[2] == 8;
    }

    private Utils() {
    }
}
