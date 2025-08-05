package java.util.logging;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/util/logging/FileHandler.class */
public class FileHandler extends StreamHandler {
    private MeteredStream meter;
    private boolean append;
    private int limit;
    private int count;
    private String pattern;
    private String lockFileName;
    private FileChannel lockFileChannel;
    private File[] files;
    private static final int DEFAULT_MAX_LOCKS = 100;
    private static int maxLocks;
    private static final Set<String> locks = new HashSet();

    private static native boolean isSetUID();

    static {
        maxLocks = ((Integer) AccessController.doPrivileged(() -> {
            return Integer.getInteger("jdk.internal.FileHandlerLogging.maxLocks", 100);
        })).intValue();
        if (maxLocks <= 0) {
            maxLocks = 100;
        }
    }

    /* loaded from: rt.jar:java/util/logging/FileHandler$MeteredStream.class */
    private class MeteredStream extends OutputStream {
        final OutputStream out;
        int written;

        MeteredStream(OutputStream outputStream, int i2) {
            this.out = outputStream;
            this.written = i2;
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            this.out.write(i2);
            this.written++;
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            this.out.write(bArr);
            this.written += bArr.length;
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            this.out.write(bArr, i2, i3);
            this.written += i3;
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this.out.flush();
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.out.close();
        }
    }

    private void open(File file, boolean z2) throws IOException {
        int length = 0;
        if (z2) {
            length = (int) file.length();
        }
        this.meter = new MeteredStream(new BufferedOutputStream(new FileOutputStream(file.toString(), z2)), length);
        setOutputStream(this.meter);
    }

    private void configure() {
        LogManager logManager = LogManager.getLogManager();
        String name = getClass().getName();
        this.pattern = logManager.getStringProperty(name + ".pattern", "%h/java%u.log");
        this.limit = logManager.getIntProperty(name + ".limit", 0);
        if (this.limit < 0) {
            this.limit = 0;
        }
        this.count = logManager.getIntProperty(name + ".count", 1);
        if (this.count <= 0) {
            this.count = 1;
        }
        this.append = logManager.getBooleanProperty(name + ".append", false);
        setLevel(logManager.getLevelProperty(name + ".level", Level.ALL));
        setFilter(logManager.getFilterProperty(name + ".filter", null));
        setFormatter(logManager.getFormatterProperty(name + ".formatter", new XMLFormatter()));
        try {
            setEncoding(logManager.getStringProperty(name + ".encoding", null));
        } catch (Exception e2) {
            try {
                setEncoding(null);
            } catch (Exception e3) {
            }
        }
    }

    public FileHandler() throws IOException, SecurityException {
        checkPermission();
        configure();
        openFiles();
    }

    public FileHandler(String str) throws IOException, SecurityException {
        if (str.length() < 1) {
            throw new IllegalArgumentException();
        }
        checkPermission();
        configure();
        this.pattern = str;
        this.limit = 0;
        this.count = 1;
        openFiles();
    }

    public FileHandler(String str, boolean z2) throws IOException, SecurityException {
        if (str.length() < 1) {
            throw new IllegalArgumentException();
        }
        checkPermission();
        configure();
        this.pattern = str;
        this.limit = 0;
        this.count = 1;
        this.append = z2;
        openFiles();
    }

    public FileHandler(String str, int i2, int i3) throws IOException, SecurityException {
        if (i2 < 0 || i3 < 1 || str.length() < 1) {
            throw new IllegalArgumentException();
        }
        checkPermission();
        configure();
        this.pattern = str;
        this.limit = i2;
        this.count = i3;
        openFiles();
    }

    public FileHandler(String str, int i2, int i3, boolean z2) throws IOException, SecurityException {
        if (i2 < 0 || i3 < 1 || str.length() < 1) {
            throw new IllegalArgumentException();
        }
        checkPermission();
        configure();
        this.pattern = str;
        this.limit = i2;
        this.count = i3;
        this.append = z2;
        openFiles();
    }

    private boolean isParentWritable(Path path) {
        Path parent = path.getParent();
        if (parent == null) {
            parent = path.toAbsolutePath().getParent();
        }
        return parent != null && Files.isWritable(parent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:76:0x01ca, code lost:
    
        r7.files = new java.io.File[r7.count];
        r11 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x01de, code lost:
    
        if (r11 >= r7.count) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x01e1, code lost:
    
        r7.files[r11] = generate(r7.pattern, r11, r10);
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01fd, code lost:
    
        if (r7.append == false) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0200, code lost:
    
        open(r7.files[0], true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x020e, code lost:
    
        rotate();
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0212, code lost:
    
        r0 = r0.lastException;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x021a, code lost:
    
        if (r0 == null) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0222, code lost:
    
        if ((r0 instanceof java.io.IOException) == false) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x022a, code lost:
    
        throw ((java.io.IOException) r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0230, code lost:
    
        if ((r0 instanceof java.lang.SecurityException) == false) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0238, code lost:
    
        throw ((java.lang.SecurityException) r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0254, code lost:
    
        throw new java.io.IOException("Exception: " + ((java.lang.Object) r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0255, code lost:
    
        setErrorManager(new java.util.logging.ErrorManager());
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0260, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void openFiles() throws java.io.IOException, java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 609
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.FileHandler.openFiles():void");
    }

    private File generate(String str, int i2, int i3) throws IOException {
        File file;
        File file2 = null;
        String str2 = "";
        int i4 = 0;
        boolean z2 = false;
        boolean z3 = false;
        while (i4 < str.length()) {
            char cCharAt = str.charAt(i4);
            i4++;
            char lowerCase = 0;
            if (i4 < str.length()) {
                lowerCase = Character.toLowerCase(str.charAt(i4));
            }
            if (cCharAt == '/') {
                if (file2 == null) {
                    file = new File(str2);
                } else {
                    file = new File(file2, str2);
                }
                file2 = file;
                str2 = "";
            } else {
                if (cCharAt == '%') {
                    if (lowerCase == 't') {
                        String property = System.getProperty("java.io.tmpdir");
                        if (property == null) {
                            property = System.getProperty("user.home");
                        }
                        file2 = new File(property);
                        i4++;
                        str2 = "";
                    } else if (lowerCase == 'h') {
                        file2 = new File(System.getProperty("user.home"));
                        if (isSetUID()) {
                            throw new IOException("can't use %h in set UID program");
                        }
                        i4++;
                        str2 = "";
                    } else if (lowerCase == 'g') {
                        str2 = str2 + i2;
                        z2 = true;
                        i4++;
                    } else if (lowerCase == 'u') {
                        str2 = str2 + i3;
                        z3 = true;
                        i4++;
                    } else if (lowerCase == '%') {
                        str2 = str2 + FXMLLoader.RESOURCE_KEY_PREFIX;
                        i4++;
                    }
                }
                str2 = str2 + cCharAt;
            }
        }
        if (this.count > 1 && !z2) {
            str2 = str2 + "." + i2;
        }
        if (i3 > 0 && !z3) {
            str2 = str2 + "." + i3;
        }
        if (str2.length() > 0) {
            if (file2 == null) {
                file2 = new File(str2);
            } else {
                file2 = new File(file2, str2);
            }
        }
        return file2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void rotate() throws SecurityException {
        Level level = getLevel();
        setLevel(Level.OFF);
        super.close();
        for (int i2 = this.count - 2; i2 >= 0; i2--) {
            File file = this.files[i2];
            File file2 = this.files[i2 + 1];
            if (file.exists()) {
                if (file2.exists()) {
                    file2.delete();
                }
                file.renameTo(file2);
            }
        }
        try {
            open(this.files[0], false);
        } catch (IOException e2) {
            reportError(null, e2, 4);
        }
        setLevel(level);
    }

    @Override // java.util.logging.StreamHandler, java.util.logging.Handler
    public synchronized void publish(LogRecord logRecord) {
        if (!isLoggable(logRecord)) {
            return;
        }
        super.publish(logRecord);
        flush();
        if (this.limit > 0 && this.meter.written >= this.limit) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.util.logging.FileHandler.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() throws SecurityException {
                    FileHandler.this.rotate();
                    return null;
                }
            });
        }
    }

    @Override // java.util.logging.StreamHandler, java.util.logging.Handler
    public synchronized void close() throws SecurityException {
        super.close();
        if (this.lockFileName == null) {
            return;
        }
        try {
            this.lockFileChannel.close();
        } catch (Exception e2) {
        }
        synchronized (locks) {
            locks.remove(this.lockFileName);
        }
        new File(this.lockFileName).delete();
        this.lockFileName = null;
        this.lockFileChannel = null;
    }

    /* loaded from: rt.jar:java/util/logging/FileHandler$InitializationErrorManager.class */
    private static class InitializationErrorManager extends ErrorManager {
        Exception lastException;

        private InitializationErrorManager() {
        }

        @Override // java.util.logging.ErrorManager
        public void error(String str, Exception exc, int i2) {
            this.lastException = exc;
        }
    }
}
