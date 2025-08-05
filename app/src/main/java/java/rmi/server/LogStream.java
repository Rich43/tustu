package java.rmi.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LoggingPermission;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/LogStream.class */
public class LogStream extends PrintStream {
    private static Map<String, LogStream> known = new HashMap(5);
    private static PrintStream defaultStream = System.err;
    private String name;
    private OutputStream logOut;
    private OutputStreamWriter logWriter;
    private StringBuffer buffer;
    private ByteArrayOutputStream bufOut;
    public static final int SILENT = 0;
    public static final int BRIEF = 10;
    public static final int VERBOSE = 20;

    @Deprecated
    private LogStream(String str, OutputStream outputStream) {
        super(new ByteArrayOutputStream());
        this.buffer = new StringBuffer();
        this.bufOut = (ByteArrayOutputStream) this.out;
        this.name = str;
        setOutputStream(outputStream);
    }

    @Deprecated
    public static LogStream log(String str) {
        LogStream logStream;
        synchronized (known) {
            logStream = known.get(str);
            if (logStream == null) {
                logStream = new LogStream(str, defaultStream);
            }
            known.put(str, logStream);
        }
        return logStream;
    }

    @Deprecated
    public static synchronized PrintStream getDefaultStream() {
        return defaultStream;
    }

    @Deprecated
    public static synchronized void setDefaultStream(PrintStream printStream) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new LoggingPermission("control", null));
        }
        defaultStream = printStream;
    }

    @Deprecated
    public synchronized OutputStream getOutputStream() {
        return this.logOut;
    }

    @Deprecated
    public synchronized void setOutputStream(OutputStream outputStream) {
        this.logOut = outputStream;
        this.logWriter = new OutputStreamWriter(this.logOut);
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
    @Deprecated
    public void write(int i2) {
        if (i2 == 10) {
            synchronized (this) {
                synchronized (this.logOut) {
                    this.buffer.setLength(0);
                    this.buffer.append(new Date().toString());
                    this.buffer.append(':');
                    this.buffer.append(this.name);
                    this.buffer.append(':');
                    this.buffer.append(Thread.currentThread().getName());
                    this.buffer.append(':');
                    try {
                        try {
                            this.logWriter.write(this.buffer.toString());
                            this.logWriter.flush();
                            this.bufOut.writeTo(this.logOut);
                            this.logOut.write(i2);
                            this.logOut.flush();
                            this.bufOut.reset();
                        } catch (IOException e2) {
                            setError();
                            this.bufOut.reset();
                        }
                    } catch (Throwable th) {
                        this.bufOut.reset();
                        throw th;
                    }
                }
            }
            return;
        }
        super.write(i2);
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
    @Deprecated
    public void write(byte[] bArr, int i2, int i3) {
        if (i3 < 0) {
            throw new ArrayIndexOutOfBoundsException(i3);
        }
        for (int i4 = 0; i4 < i3; i4++) {
            write(bArr[i2 + i4]);
        }
    }

    @Deprecated
    public String toString() {
        return this.name;
    }

    @Deprecated
    public static int parseLevel(String str) {
        if (str == null || str.length() < 1) {
            return -1;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e2) {
            if (str.length() < 1) {
                return -1;
            }
            if ("SILENT".startsWith(str.toUpperCase())) {
                return 0;
            }
            if ("BRIEF".startsWith(str.toUpperCase())) {
                return 10;
            }
            if ("VERBOSE".startsWith(str.toUpperCase())) {
                return 20;
            }
            return -1;
        }
    }
}
