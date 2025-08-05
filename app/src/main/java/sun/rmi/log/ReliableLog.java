package sun.rmi.log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/log/ReliableLog.class */
public class ReliableLog {
    public static final int PreferredMajorVersion = 0;
    public static final int PreferredMinorVersion = 2;
    private boolean Debug;
    private File dir;
    private int version;
    private String logName;
    private LogFile log;
    private long snapshotBytes;
    private long logBytes;
    private int logEntries;
    private long lastSnapshot;
    private long lastLog;
    private LogHandler handler;
    private final byte[] intBuf;
    private int majorFormatVersion;
    private int minorFormatVersion;
    private static String snapshotPrefix = "Snapshot.";
    private static String logfilePrefix = "Logfile.";
    private static String versionFile = "Version_Number";
    private static String newVersionFile = "New_Version_Number";
    private static int intBytes = 4;
    private static long diskPageSize = 512;
    private static final Constructor<? extends LogFile> logClassConstructor = getLogClassConstructor();

    public ReliableLog(String str, LogHandler logHandler, boolean z2) throws IOException {
        this.Debug = false;
        this.version = 0;
        this.logName = null;
        this.log = null;
        this.snapshotBytes = 0L;
        this.logBytes = 0L;
        this.logEntries = 0;
        this.lastSnapshot = 0L;
        this.lastLog = 0L;
        this.intBuf = new byte[4];
        this.majorFormatVersion = 0;
        this.minorFormatVersion = 0;
        this.Debug = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.rmi.log.debug"))).booleanValue();
        this.dir = new File(str);
        if ((!this.dir.exists() || !this.dir.isDirectory()) && !this.dir.mkdir()) {
            throw new IOException("could not create directory for log: " + str);
        }
        this.handler = logHandler;
        this.lastSnapshot = 0L;
        this.lastLog = 0L;
        getVersion();
        if (this.version == 0) {
            try {
                snapshot(logHandler.initialSnapshot());
            } catch (IOException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new IOException("initial snapshot failed with exception: " + ((Object) e3));
            }
        }
    }

    public ReliableLog(String str, LogHandler logHandler) throws IOException {
        this(str, logHandler, false);
    }

    public synchronized Object recover() throws IOException {
        if (this.Debug) {
            System.err.println("log.debug: recover()");
        }
        if (this.version == 0) {
            return null;
        }
        String strVersionName = versionName(snapshotPrefix);
        File file = new File(strVersionName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        if (this.Debug) {
            System.err.println("log.debug: recovering from " + strVersionName);
        }
        try {
            try {
                Object objRecover = this.handler.recover(bufferedInputStream);
                this.snapshotBytes = file.length();
                bufferedInputStream.close();
                return recoverUpdates(objRecover);
            } catch (IOException e2) {
                throw e2;
            } catch (Exception e3) {
                if (this.Debug) {
                    System.err.println("log.debug: recovery failed: " + ((Object) e3));
                }
                throw new IOException("log recover failed with exception: " + ((Object) e3));
            }
        } catch (Throwable th) {
            bufferedInputStream.close();
            throw th;
        }
    }

    public synchronized void update(Object obj) throws IOException {
        update(obj, true);
    }

    public synchronized void update(Object obj, boolean z2) throws IOException {
        if (this.log == null) {
            throw new IOException("log is inaccessible, it may have been corrupted or closed");
        }
        long filePointer = this.log.getFilePointer();
        boolean zCheckSpansBoundary = this.log.checkSpansBoundary(filePointer);
        writeInt(this.log, zCheckSpansBoundary ? Integer.MIN_VALUE : 0);
        try {
            this.handler.writeUpdate(new LogOutputStream(this.log), obj);
            this.log.sync();
            long filePointer2 = this.log.getFilePointer();
            int i2 = (int) ((filePointer2 - filePointer) - intBytes);
            this.log.seek(filePointer);
            if (zCheckSpansBoundary) {
                writeInt(this.log, i2 | Integer.MIN_VALUE);
                this.log.sync();
                this.log.seek(filePointer);
                this.log.writeByte(i2 >> 24);
                this.log.sync();
            } else {
                writeInt(this.log, i2);
                this.log.sync();
            }
            this.log.seek(filePointer2);
            this.logBytes = filePointer2;
            this.lastLog = System.currentTimeMillis();
            this.logEntries++;
        } catch (IOException e2) {
            throw e2;
        } catch (Exception e3) {
            throw ((IOException) new IOException("write update failed").initCause(e3));
        }
    }

    private static Constructor<? extends LogFile> getLogClassConstructor() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.log.class"));
        if (str != null) {
            try {
                return ((ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: sun.rmi.log.ReliableLog.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public ClassLoader run2() {
                        return ClassLoader.getSystemClassLoader();
                    }
                })).loadClass(str).asSubclass(LogFile.class).getConstructor(String.class, String.class);
            } catch (Exception e2) {
                System.err.println("Exception occurred:");
                e2.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public synchronized void snapshot(Object obj) throws IOException {
        int i2 = this.version;
        incrVersion();
        File file = new File(versionName(snapshotPrefix));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            try {
                this.handler.snapshot(fileOutputStream, obj);
                this.lastSnapshot = System.currentTimeMillis();
                fileOutputStream.close();
                this.snapshotBytes = file.length();
                openLogFile(true);
                writeVersionFile(true);
                commitToNewVersion();
                deleteSnapshot(i2);
                deleteLogFile(i2);
            } catch (IOException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new IOException("snapshot failed", e3);
            }
        } catch (Throwable th) {
            fileOutputStream.close();
            this.snapshotBytes = file.length();
            throw th;
        }
    }

    public synchronized void close() throws IOException {
        if (this.log == null) {
            return;
        }
        try {
            this.log.close();
        } finally {
            this.log = null;
        }
    }

    public long snapshotSize() {
        return this.snapshotBytes;
    }

    public long logSize() {
        return this.logBytes;
    }

    private void writeInt(DataOutput dataOutput, int i2) throws IOException {
        this.intBuf[0] = (byte) (i2 >> 24);
        this.intBuf[1] = (byte) (i2 >> 16);
        this.intBuf[2] = (byte) (i2 >> 8);
        this.intBuf[3] = (byte) i2;
        dataOutput.write(this.intBuf);
    }

    private String fName(String str) {
        return this.dir.getPath() + File.separator + str;
    }

    private String versionName(String str) {
        return versionName(str, 0);
    }

    private String versionName(String str, int i2) {
        return fName(str) + String.valueOf(i2 == 0 ? this.version : i2);
    }

    private void incrVersion() {
        do {
            this.version++;
        } while (this.version == 0);
    }

    private void deleteFile(String str) throws IOException {
        if (!new File(str).delete()) {
            throw new IOException("couldn't remove file: " + str);
        }
    }

    private void deleteNewVersionFile() throws IOException {
        deleteFile(fName(newVersionFile));
    }

    private void deleteSnapshot(int i2) throws IOException {
        if (i2 == 0) {
            return;
        }
        deleteFile(versionName(snapshotPrefix, i2));
    }

    private void deleteLogFile(int i2) throws IOException {
        if (i2 == 0) {
            return;
        }
        deleteFile(versionName(logfilePrefix, i2));
    }

    private void openLogFile(boolean z2) throws IOException {
        try {
            close();
        } catch (IOException e2) {
        }
        this.logName = versionName(logfilePrefix);
        try {
            this.log = logClassConstructor == null ? new LogFile(this.logName, InternalZipConstants.WRITE_MODE) : logClassConstructor.newInstance(this.logName, InternalZipConstants.WRITE_MODE);
            if (z2) {
                initializeLogFile();
            }
        } catch (Exception e3) {
            throw ((IOException) new IOException("unable to construct LogFile instance").initCause(e3));
        }
    }

    private void initializeLogFile() throws IOException {
        this.log.setLength(0L);
        this.majorFormatVersion = 0;
        writeInt(this.log, 0);
        this.minorFormatVersion = 2;
        writeInt(this.log, 2);
        this.logBytes = intBytes * 2;
        this.logEntries = 0;
    }

    private void writeVersionFile(boolean z2) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fName(z2 ? newVersionFile : versionFile));
        Throwable th = null;
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            Throwable th2 = null;
            try {
                try {
                    writeInt(dataOutputStream, this.version);
                    if (dataOutputStream != null) {
                        if (0 != 0) {
                            try {
                                dataOutputStream.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            dataOutputStream.close();
                        }
                    }
                    if (fileOutputStream != null) {
                        if (0 == 0) {
                            fileOutputStream.close();
                            return;
                        }
                        try {
                            fileOutputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    }
                } catch (Throwable th5) {
                    if (dataOutputStream != null) {
                        if (th2 != null) {
                            try {
                                dataOutputStream.close();
                            } catch (Throwable th6) {
                                th2.addSuppressed(th6);
                            }
                        } else {
                            dataOutputStream.close();
                        }
                    }
                    throw th5;
                }
            } catch (Throwable th7) {
                th2 = th7;
                throw th7;
            }
        } catch (Throwable th8) {
            if (fileOutputStream != null) {
                if (0 != 0) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable th9) {
                        th.addSuppressed(th9);
                    }
                } else {
                    fileOutputStream.close();
                }
            }
            throw th8;
        }
    }

    private void createFirstVersion() throws IOException {
        this.version = 0;
        writeVersionFile(false);
    }

    private void commitToNewVersion() throws IOException {
        writeVersionFile(false);
        deleteNewVersionFile();
    }

    private int readVersion(String str) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(str));
        Throwable th = null;
        try {
            int i2 = dataInputStream.readInt();
            if (dataInputStream != null) {
                if (0 != 0) {
                    try {
                        dataInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    dataInputStream.close();
                }
            }
            return i2;
        } catch (Throwable th3) {
            if (dataInputStream != null) {
                if (0 != 0) {
                    try {
                        dataInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    dataInputStream.close();
                }
            }
            throw th3;
        }
    }

    private void getVersion() throws IOException {
        try {
            this.version = readVersion(fName(newVersionFile));
            commitToNewVersion();
        } catch (IOException e2) {
            try {
                deleteNewVersionFile();
            } catch (IOException e3) {
            }
            try {
                this.version = readVersion(fName(versionFile));
            } catch (IOException e4) {
                createFirstVersion();
            }
        }
    }

    private Object recoverUpdates(Object obj) throws IOException {
        this.logBytes = 0L;
        this.logEntries = 0;
        if (this.version == 0) {
            return obj;
        }
        String strVersionName = versionName(logfilePrefix);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(strVersionName));
        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
        if (this.Debug) {
            System.err.println("log.debug: reading updates from " + strVersionName);
        }
        try {
            this.majorFormatVersion = dataInputStream.readInt();
            this.logBytes += intBytes;
            this.minorFormatVersion = dataInputStream.readInt();
            this.logBytes += intBytes;
        } catch (EOFException e2) {
            openLogFile(true);
            bufferedInputStream = null;
        }
        if (this.majorFormatVersion != 0) {
            if (this.Debug) {
                System.err.println("log.debug: major version mismatch: " + this.majorFormatVersion + "." + this.minorFormatVersion);
            }
            throw new IOException("Log file " + this.logName + " has a version " + this.majorFormatVersion + "." + this.minorFormatVersion + " format, and this implementation  understands only version 0.2");
        }
        while (true) {
            if (bufferedInputStream == null) {
                break;
            }
            try {
                try {
                    int i2 = dataInputStream.readInt();
                    if (i2 <= 0) {
                        if (this.Debug) {
                            System.err.println("log.debug: last update incomplete, updateLen = 0x" + Integer.toHexString(i2));
                        }
                    } else if (bufferedInputStream.available() < i2) {
                        if (this.Debug) {
                            System.err.println("log.debug: log was truncated");
                        }
                    } else {
                        if (this.Debug) {
                            System.err.println("log.debug: rdUpdate size " + i2);
                        }
                        try {
                            obj = this.handler.readUpdate(new LogInputStream(bufferedInputStream, i2), obj);
                            this.logBytes += intBytes + i2;
                            this.logEntries++;
                        } catch (IOException e3) {
                            throw e3;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            throw new IOException("read update failed with exception: " + ((Object) e4));
                        }
                    }
                } catch (EOFException e5) {
                    if (this.Debug) {
                        System.err.println("log.debug: log was sync'd cleanly");
                    }
                }
            } finally {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            }
        }
        if (this.Debug) {
            System.err.println("log.debug: recovered updates: " + this.logEntries);
        }
        openLogFile(false);
        if (this.log == null) {
            throw new IOException("rmid's log is inaccessible, it may have been corrupted or closed");
        }
        this.log.seek(this.logBytes);
        this.log.setLength(this.logBytes);
        return obj;
    }

    /* loaded from: rt.jar:sun/rmi/log/ReliableLog$LogFile.class */
    public static class LogFile extends RandomAccessFile {
        private final FileDescriptor fd;

        public LogFile(String str, String str2) throws IOException {
            super(str, str2);
            this.fd = getFD();
        }

        protected void sync() throws IOException {
            this.fd.sync();
        }

        protected boolean checkSpansBoundary(long j2) {
            return j2 % 512 > 508;
        }
    }
}
