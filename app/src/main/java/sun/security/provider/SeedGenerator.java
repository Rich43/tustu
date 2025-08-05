package sun.security.provider;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import sun.security.util.Debug;

/* loaded from: rt.jar:sun/security/provider/SeedGenerator.class */
abstract class SeedGenerator {
    private static SeedGenerator instance;
    private static final Debug debug = Debug.getInstance("provider");

    abstract void getSeedBytes(byte[] bArr);

    SeedGenerator() {
    }

    static {
        String seedSource = SunEntries.getSeedSource();
        if (seedSource.equals("file:/dev/random") || seedSource.equals("file:/dev/urandom")) {
            try {
                instance = new NativeSeedGenerator(seedSource);
                if (debug != null) {
                    debug.println("Using operating system seed generator" + seedSource);
                }
            } catch (IOException e2) {
                if (debug != null) {
                    debug.println("Failed to use operating system seed generator: " + e2.toString());
                }
            }
        } else if (seedSource.length() != 0) {
            try {
                instance = new URLSeedGenerator(seedSource);
                if (debug != null) {
                    debug.println("Using URL seed generator reading from " + seedSource);
                }
            } catch (IOException e3) {
                if (debug != null) {
                    debug.println("Failed to create seed generator with " + seedSource + ": " + e3.toString());
                }
            }
        }
        if (instance == null) {
            if (debug != null) {
                debug.println("Using default threaded seed generator");
            }
            instance = new ThreadedSeedGenerator();
        }
    }

    public static void generateSeed(byte[] bArr) {
        instance.getSeedBytes(bArr);
    }

    static byte[] getSystemEntropy() {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update((byte) System.currentTimeMillis());
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.provider.SeedGenerator.1
                /* JADX WARN: Can't rename method to resolve collision */
                /* JADX WARN: Finally extract failed */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    try {
                        Properties properties = System.getProperties();
                        Enumeration<?> enumerationPropertyNames = properties.propertyNames();
                        while (enumerationPropertyNames.hasMoreElements()) {
                            String str = (String) enumerationPropertyNames.nextElement();
                            messageDigest.update(str.getBytes());
                            messageDigest.update(properties.getProperty(str).getBytes());
                        }
                        SeedGenerator.addNetworkAdapterInfo(messageDigest);
                        int i2 = 0;
                        DirectoryStream<Path> directoryStreamNewDirectoryStream = Files.newDirectoryStream(new File(properties.getProperty("java.io.tmpdir")).toPath());
                        Throwable th = null;
                        try {
                            Random random = new Random();
                            for (Path path : directoryStreamNewDirectoryStream) {
                                if (i2 < 512 || random.nextBoolean()) {
                                    messageDigest.update(path.getFileName().toString().getBytes());
                                }
                                int i3 = i2;
                                i2++;
                                if (i3 > 1024) {
                                    break;
                                }
                            }
                            if (directoryStreamNewDirectoryStream != null) {
                                if (0 != 0) {
                                    try {
                                        directoryStreamNewDirectoryStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    directoryStreamNewDirectoryStream.close();
                                }
                            }
                        } catch (Throwable th3) {
                            if (directoryStreamNewDirectoryStream != null) {
                                if (0 != 0) {
                                    try {
                                        directoryStreamNewDirectoryStream.close();
                                    } catch (Throwable th4) {
                                        th.addSuppressed(th4);
                                    }
                                } else {
                                    directoryStreamNewDirectoryStream.close();
                                }
                            }
                            throw th3;
                        }
                    } catch (Exception e2) {
                        messageDigest.update((byte) e2.hashCode());
                    }
                    Runtime runtime = Runtime.getRuntime();
                    byte[] bArrLongToByteArray = SeedGenerator.longToByteArray(runtime.totalMemory());
                    messageDigest.update(bArrLongToByteArray, 0, bArrLongToByteArray.length);
                    byte[] bArrLongToByteArray2 = SeedGenerator.longToByteArray(runtime.freeMemory());
                    messageDigest.update(bArrLongToByteArray2, 0, bArrLongToByteArray2.length);
                    return null;
                }
            });
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e2) {
            throw new InternalError("internal error: SHA-1 not available.", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0032, code lost:
    
        r3.update(r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void addNetworkAdapterInfo(java.security.MessageDigest r3) {
        /*
            java.util.Enumeration r0 = java.net.NetworkInterface.getNetworkInterfaces()     // Catch: java.lang.Exception -> L40
            r4 = r0
        L4:
            r0 = r4
            boolean r0 = r0.hasMoreElements()     // Catch: java.lang.Exception -> L40
            if (r0 == 0) goto L3d
            r0 = r4
            java.lang.Object r0 = r0.nextElement()     // Catch: java.lang.Exception -> L40
            java.net.NetworkInterface r0 = (java.net.NetworkInterface) r0     // Catch: java.lang.Exception -> L40
            r5 = r0
            r0 = r3
            r1 = r5
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Exception -> L40
            byte[] r1 = r1.getBytes()     // Catch: java.lang.Exception -> L40
            r0.update(r1)     // Catch: java.lang.Exception -> L40
            r0 = r5
            boolean r0 = r0.isVirtual()     // Catch: java.lang.Exception -> L40
            if (r0 != 0) goto L3a
            r0 = r5
            byte[] r0 = r0.getHardwareAddress()     // Catch: java.lang.Exception -> L40
            r6 = r0
            r0 = r6
            if (r0 == 0) goto L3a
            r0 = r3
            r1 = r6
            r0.update(r1)     // Catch: java.lang.Exception -> L40
            goto L3d
        L3a:
            goto L4
        L3d:
            goto L41
        L40:
            r4 = move-exception
        L41:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.SeedGenerator.addNetworkAdapterInfo(java.security.MessageDigest):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] longToByteArray(long j2) {
        byte[] bArr = new byte[8];
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i2] = (byte) j2;
            j2 >>= 8;
        }
        return bArr;
    }

    /* loaded from: rt.jar:sun/security/provider/SeedGenerator$ThreadedSeedGenerator.class */
    private static class ThreadedSeedGenerator extends SeedGenerator implements Runnable {
        private int count;
        ThreadGroup seedGroup;
        private static byte[] rndTab = {56, 30, -107, -6, -86, 25, -83, 75, -12, -64, 5, Byte.MIN_VALUE, 78, 21, 16, 32, 70, -81, 37, -51, -43, -46, -108, 87, 29, 17, -55, 22, -11, -111, -115, 84, -100, 108, -45, -15, -98, 72, -33, -28, 31, -52, -37, -117, -97, -27, 93, -123, 47, 126, -80, -62, -93, -79, 61, -96, -65, -5, -47, -119, 14, 89, 81, -118, -88, 20, 67, -126, -113, 60, -102, 55, 110, 28, 85, 121, 122, -58, 2, 45, 43, 24, -9, 103, -13, 102, -68, -54, -101, -104, 19, 13, -39, -26, -103, 62, 77, 51, 44, 111, 73, 18, -127, -82, 4, -30, 11, -99, -74, 40, -89, 42, -76, -77, -94, -35, -69, 35, 120, 76, 33, -73, -7, 82, -25, -10, 88, 125, -112, 58, 83, 95, 6, 10, 98, -34, 80, 15, -91, 86, -19, 52, -17, 117, 49, -63, 118, -90, 36, -116, -40, -71, 97, -53, -109, -85, 109, -16, -3, 104, -95, 68, 54, 34, 26, 114, -1, 106, -121, 3, 66, 0, 100, -84, 57, 107, 119, -42, 112, -61, 1, 48, 38, 12, -56, -57, 39, -106, -72, 41, 7, 71, -29, -59, -8, -38, 79, -31, 124, -124, 8, 91, 116, 99, -4, 9, -36, -78, 63, -49, -67, -87, 59, 101, -32, 92, 94, 53, -41, 115, -66, -70, -122, 50, -50, -22, -20, -18, -21, 23, -2, -48, 96, 65, -105, 123, -14, -110, 69, -24, -120, -75, 74, Byte.MAX_VALUE, -60, 113, 90, -114, 105, 46, 27, -125, -23, -44, 64};
        private byte[] pool = new byte[20];
        private int end = 0;
        private int start = 0;

        ThreadedSeedGenerator() {
            try {
                MessageDigest.getInstance("SHA");
                final ThreadGroup[] threadGroupArr = new ThreadGroup[1];
                Thread thread = (Thread) AccessController.doPrivileged(new PrivilegedAction<Thread>() { // from class: sun.security.provider.SeedGenerator.ThreadedSeedGenerator.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Thread run() {
                        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                        while (true) {
                            ThreadGroup threadGroup2 = threadGroup;
                            ThreadGroup parent = threadGroup2.getParent();
                            if (parent != null) {
                                threadGroup = parent;
                            } else {
                                threadGroupArr[0] = new ThreadGroup(threadGroup2, "SeedGenerator ThreadGroup");
                                Thread thread2 = new Thread(threadGroupArr[0], ThreadedSeedGenerator.this, "SeedGenerator Thread");
                                thread2.setPriority(1);
                                thread2.setDaemon(true);
                                return thread2;
                            }
                        }
                    }
                });
                this.seedGroup = threadGroupArr[0];
                thread.start();
            } catch (NoSuchAlgorithmException e2) {
                throw new InternalError("internal error: SHA-1 not available.", e2);
            }
        }

        @Override // java.lang.Runnable
        public final void run() {
            while (true) {
                try {
                    synchronized (this) {
                        while (this.count >= this.pool.length) {
                            wait();
                        }
                    }
                    byte b2 = 0;
                    int i2 = 0;
                    for (int i3 = 0; i2 < 64000 && i3 < 6; i3++) {
                        try {
                            new Thread(this.seedGroup, new BogusThread(), "SeedGenerator Thread").start();
                            int i4 = 0;
                            long jCurrentTimeMillis = System.currentTimeMillis() + 250;
                            while (System.currentTimeMillis() < jCurrentTimeMillis) {
                                synchronized (this) {
                                }
                                i4++;
                            }
                            b2 = (byte) (b2 ^ rndTab[i4 % 255]);
                            i2 += i4;
                        } catch (Exception e2) {
                            throw new InternalError("internal error: SeedGenerator thread creation error.", e2);
                        }
                    }
                    synchronized (this) {
                        this.pool[this.end] = b2;
                        this.end++;
                        this.count++;
                        if (this.end >= this.pool.length) {
                            this.end = 0;
                        }
                        notifyAll();
                    }
                } catch (Exception e3) {
                    throw new InternalError("internal error: SeedGenerator thread generated an exception.", e3);
                }
            }
        }

        @Override // sun.security.provider.SeedGenerator
        void getSeedBytes(byte[] bArr) {
            for (int i2 = 0; i2 < bArr.length; i2++) {
                bArr[i2] = getSeedByte();
            }
        }

        byte getSeedByte() {
            byte b2;
            try {
            } catch (Exception e2) {
                if (this.count <= 0) {
                    throw new InternalError("internal error: SeedGenerator thread generated an exception.", e2);
                }
            }
            synchronized (this) {
                while (this.count <= 0) {
                    wait();
                }
                synchronized (this) {
                    b2 = this.pool[this.start];
                    this.pool[this.start] = 0;
                    this.start++;
                    this.count--;
                    if (this.start == this.pool.length) {
                        this.start = 0;
                    }
                    notifyAll();
                }
            }
            return b2;
        }

        /* loaded from: rt.jar:sun/security/provider/SeedGenerator$ThreadedSeedGenerator$BogusThread.class */
        private static class BogusThread implements Runnable {
            private BogusThread() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                for (int i2 = 0; i2 < 5; i2++) {
                    try {
                        Thread.sleep(50L);
                    } catch (Exception e2) {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/security/provider/SeedGenerator$URLSeedGenerator.class */
    static class URLSeedGenerator extends SeedGenerator {
        private String deviceName;
        private InputStream seedStream;

        URLSeedGenerator(String str) throws IOException {
            if (str == null) {
                throw new IOException("No random source specified");
            }
            this.deviceName = str;
            init();
        }

        private void init() throws IOException {
            final URL url = new URL(this.deviceName);
            try {
                this.seedStream = (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() { // from class: sun.security.provider.SeedGenerator.URLSeedGenerator.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public InputStream run() throws IOException {
                        if (url.getProtocol().equalsIgnoreCase(DeploymentDescriptorParser.ATTR_FILE)) {
                            return new FileInputStream(SunEntries.getDeviceFile(url));
                        }
                        return url.openStream();
                    }
                });
            } catch (Exception e2) {
                throw new IOException("Failed to open " + this.deviceName, e2.getCause());
            }
        }

        @Override // sun.security.provider.SeedGenerator
        void getSeedBytes(byte[] bArr) {
            int length = bArr.length;
            int i2 = 0;
            while (i2 < length) {
                try {
                    int i3 = this.seedStream.read(bArr, i2, length - i2);
                    if (i3 < 0) {
                        throw new InternalError("URLSeedGenerator " + this.deviceName + " reached end of file");
                    }
                    i2 += i3;
                } catch (IOException e2) {
                    throw new InternalError("URLSeedGenerator " + this.deviceName + " generated exception: " + e2.getMessage(), e2);
                }
            }
        }
    }
}
