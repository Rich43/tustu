package java.lang;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.icepdf.core.util.PdfOps;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/lang/ProcessImpl.class */
final class ProcessImpl extends Process {
    private static final int VERIFICATION_CMD_BAT = 0;
    private static final int VERIFICATION_WIN32 = 1;
    private static final int VERIFICATION_WIN32_SAFE = 2;
    private static final int VERIFICATION_LEGACY = 3;
    private static final char DOUBLEQUOTE = '\"';
    private static final char BACKSLASH = '\\';
    private long handle;
    private OutputStream stdin_stream;
    private InputStream stdout_stream;
    private InputStream stderr_stream;
    private static final JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
    private static final char[][] ESCAPE_VERIFICATION = {new char[]{' ', '\t', '\"', '<', '>', '&', '|', '^'}, new char[]{' ', '\t', '\"', '<', '>'}, new char[]{' ', '\t', '\"', '<', '>'}, new char[]{' ', '\t'}};
    private static final int STILL_ACTIVE = getStillActive();

    private static native int getStillActive();

    private static native int getExitCodeProcess(long j2);

    private static native void waitForInterruptibly(long j2);

    private static native void waitForTimeoutInterruptibly(long j2, long j3);

    private static native void terminateProcess(long j2);

    private static native boolean isProcessAlive(long j2);

    private static native synchronized long create(String str, String str2, String str3, long[] jArr, boolean z2) throws IOException;

    private static native long openForAtomicAppend(String str) throws IOException;

    private static native boolean closeHandle(long j2);

    private static FileOutputStream newFileOutputStream(File file, boolean z2) throws IOException {
        if (z2) {
            String path = file.getPath();
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkWrite(path);
            }
            long jOpenForAtomicAppend = openForAtomicAppend(path);
            final FileDescriptor fileDescriptor = new FileDescriptor();
            fdAccess.setHandle(fileDescriptor, jOpenForAtomicAppend);
            return (FileOutputStream) AccessController.doPrivileged(new PrivilegedAction<FileOutputStream>() { // from class: java.lang.ProcessImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public FileOutputStream run2() {
                    return new FileOutputStream(fileDescriptor);
                }
            });
        }
        return new FileOutputStream(file);
    }

    static Process start(String[] strArr, Map<String, String> map, String str, ProcessBuilder.Redirect[] redirectArr, boolean z2) throws IOException {
        long[] jArr;
        String environmentBlock = ProcessEnvironment.toEnvironmentBlock(map);
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStreamNewFileOutputStream = null;
        FileOutputStream fileOutputStreamNewFileOutputStream2 = null;
        try {
            if (redirectArr == null) {
                jArr = new long[]{-1, -1, -1};
            } else {
                jArr = new long[3];
                if (redirectArr[0] == ProcessBuilder.Redirect.PIPE) {
                    jArr[0] = -1;
                } else if (redirectArr[0] == ProcessBuilder.Redirect.INHERIT) {
                    jArr[0] = fdAccess.getHandle(FileDescriptor.in);
                } else {
                    fileInputStream = new FileInputStream(redirectArr[0].file());
                    jArr[0] = fdAccess.getHandle(fileInputStream.getFD());
                }
                if (redirectArr[1] == ProcessBuilder.Redirect.PIPE) {
                    jArr[1] = -1;
                } else if (redirectArr[1] == ProcessBuilder.Redirect.INHERIT) {
                    jArr[1] = fdAccess.getHandle(FileDescriptor.out);
                } else {
                    fileOutputStreamNewFileOutputStream = newFileOutputStream(redirectArr[1].file(), redirectArr[1].append());
                    jArr[1] = fdAccess.getHandle(fileOutputStreamNewFileOutputStream.getFD());
                }
                if (redirectArr[2] == ProcessBuilder.Redirect.PIPE) {
                    jArr[2] = -1;
                } else if (redirectArr[2] == ProcessBuilder.Redirect.INHERIT) {
                    jArr[2] = fdAccess.getHandle(FileDescriptor.err);
                } else {
                    fileOutputStreamNewFileOutputStream2 = newFileOutputStream(redirectArr[2].file(), redirectArr[2].append());
                    jArr[2] = fdAccess.getHandle(fileOutputStreamNewFileOutputStream2.getFD());
                }
            }
            ProcessImpl processImpl = new ProcessImpl(strArr, environmentBlock, str, jArr, z2);
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Throwable th) {
                    if (fileOutputStreamNewFileOutputStream != null) {
                        try {
                            fileOutputStreamNewFileOutputStream.close();
                        } finally {
                        }
                    }
                    if (fileOutputStreamNewFileOutputStream2 != null) {
                        fileOutputStreamNewFileOutputStream2.close();
                    }
                    throw th;
                }
            }
            if (fileOutputStreamNewFileOutputStream != null) {
                try {
                    fileOutputStreamNewFileOutputStream.close();
                } finally {
                }
            }
            if (fileOutputStreamNewFileOutputStream2 != null) {
                fileOutputStreamNewFileOutputStream2.close();
            }
            return processImpl;
        } catch (Throwable th2) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Throwable th3) {
                    if (fileOutputStreamNewFileOutputStream != null) {
                        try {
                            fileOutputStreamNewFileOutputStream.close();
                        } finally {
                            if (fileOutputStreamNewFileOutputStream2 != null) {
                                fileOutputStreamNewFileOutputStream2.close();
                            }
                        }
                    }
                    if (fileOutputStreamNewFileOutputStream2 != null) {
                        fileOutputStreamNewFileOutputStream2.close();
                    }
                    throw th3;
                }
            }
            if (fileOutputStreamNewFileOutputStream != null) {
                try {
                    fileOutputStreamNewFileOutputStream.close();
                } finally {
                    if (fileOutputStreamNewFileOutputStream2 != null) {
                        fileOutputStreamNewFileOutputStream2.close();
                    }
                }
            }
            if (fileOutputStreamNewFileOutputStream2 != null) {
                fileOutputStreamNewFileOutputStream2.close();
            }
            throw th2;
        }
    }

    /* loaded from: rt.jar:java/lang/ProcessImpl$LazyPattern.class */
    private static class LazyPattern {
        private static final Pattern PATTERN = Pattern.compile("[^\\s\"]+|\"[^\"]*\"");

        private LazyPattern() {
        }
    }

    private static String[] getTokensFromCommand(String str) {
        ArrayList arrayList = new ArrayList(8);
        Matcher matcher = LazyPattern.PATTERN.matcher(str);
        while (matcher.find()) {
            arrayList.add(matcher.group());
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private static String createCommandLine(int i2, String str, String[] strArr) {
        StringBuilder sb = new StringBuilder(80);
        sb.append(str);
        for (int i3 = 1; i3 < strArr.length; i3++) {
            sb.append(' ');
            String str2 = strArr[i3];
            if (needsEscaping(i2, str2)) {
                sb.append('\"');
                if (i2 == 2) {
                    int length = str2.length();
                    for (int i4 = 0; i4 < length; i4++) {
                        char cCharAt = str2.charAt(i4);
                        if (cCharAt == '\"') {
                            int iCountLeadingBackslash = countLeadingBackslash(i2, str2, i4);
                            while (true) {
                                int i5 = iCountLeadingBackslash;
                                iCountLeadingBackslash--;
                                if (i5 <= 0) {
                                    break;
                                }
                                sb.append('\\');
                            }
                            sb.append('\\');
                        }
                        sb.append(cCharAt);
                    }
                } else {
                    sb.append(str2);
                }
                int iCountLeadingBackslash2 = countLeadingBackslash(i2, str2, str2.length());
                while (true) {
                    int i6 = iCountLeadingBackslash2;
                    iCountLeadingBackslash2--;
                    if (i6 <= 0) {
                        break;
                    }
                    sb.append('\\');
                }
                sb.append('\"');
            } else {
                sb.append(str2);
            }
        }
        return sb.toString();
    }

    private static String unQuote(String str) {
        if (!str.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || !str.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || str.length() < 2) {
            return str;
        }
        if (str.endsWith("\\\"")) {
            return str;
        }
        return str.substring(1, str.length() - 1);
    }

    private static boolean needsEscaping(int i2, String str) {
        if (str.isEmpty()) {
            return true;
        }
        String strUnQuote = unQuote(str);
        boolean z2 = !str.equals(strUnQuote);
        boolean z3 = strUnQuote.indexOf(34) >= 0;
        switch (i2) {
            case 0:
                if (z3) {
                    throw new IllegalArgumentException("Argument has embedded quote, use the explicit CMD.EXE call.");
                }
                break;
            case 2:
                if (z2 && z3) {
                    throw new IllegalArgumentException("Malformed argument has embedded quote: " + strUnQuote);
                }
                break;
        }
        if (!z2) {
            for (char c2 : ESCAPE_VERIFICATION[i2]) {
                if (str.indexOf(c2) >= 0) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static String getExecutablePath(String str) throws IOException {
        String strUnQuote = unQuote(str);
        if (strUnQuote.indexOf(34) >= 0) {
            throw new IllegalArgumentException("Executable name has embedded quote, split the arguments: " + strUnQuote);
        }
        return new File(strUnQuote).getPath();
    }

    private boolean isExe(String str) {
        String upperCase = new File(str).getName().toUpperCase(Locale.ROOT);
        return upperCase.endsWith(".EXE") || upperCase.indexOf(46) < 0;
    }

    private boolean isShellFile(String str) {
        String upperCase = str.toUpperCase();
        return upperCase.endsWith(".CMD") || upperCase.endsWith(".BAT");
    }

    private String quoteString(String str) {
        return new StringBuilder(str.length() + 2).append('\"').append(str).append('\"').toString();
    }

    private static int countLeadingBackslash(int i2, CharSequence charSequence, int i3) {
        if (i2 == 0) {
            return 0;
        }
        int i4 = i3 - 1;
        while (i4 >= 0 && charSequence.charAt(i4) == '\\') {
            i4--;
        }
        return (i3 - 1) - i4;
    }

    private ProcessImpl(String[] strArr, String str, String str2, final long[] jArr, boolean z2) throws IOException {
        String executablePath;
        boolean zIsShellFile;
        String strCreateCommandLine;
        this.handle = 0L;
        SecurityManager securityManager = System.getSecurityManager();
        String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty("jdk.lang.Process.allowAmbiguousCommands");
        boolean z3 = !"false".equalsIgnoreCase(strPrivilegedGetProperty != null ? strPrivilegedGetProperty : securityManager == null ? "true" : "false");
        if (z3 && securityManager == null) {
            String path = new File(strArr[0]).getPath();
            strCreateCommandLine = createCommandLine(3, needsEscaping(3, path) ? quoteString(path) : path, strArr);
        } else {
            try {
                executablePath = getExecutablePath(strArr[0]);
            } catch (IllegalArgumentException e2) {
                StringBuilder sb = new StringBuilder();
                for (String str3 : strArr) {
                    sb.append(str3).append(' ');
                }
                strArr = getTokensFromCommand(sb.toString());
                executablePath = getExecutablePath(strArr[0]);
                if (securityManager != null) {
                    securityManager.checkExec(executablePath);
                }
            }
            if (z3) {
                zIsShellFile = isShellFile(executablePath);
            } else {
                zIsShellFile = !isExe(executablePath);
            }
            strCreateCommandLine = createCommandLine(zIsShellFile ? 0 : z3 ? 1 : 2, quoteString(executablePath), strArr);
        }
        this.handle = create(strCreateCommandLine, str, str2, jArr, z2);
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.ProcessImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                if (jArr[0] == -1) {
                    ProcessImpl.this.stdin_stream = ProcessBuilder.NullOutputStream.INSTANCE;
                } else {
                    FileDescriptor fileDescriptor = new FileDescriptor();
                    ProcessImpl.fdAccess.setHandle(fileDescriptor, jArr[0]);
                    ProcessImpl.this.stdin_stream = new BufferedOutputStream(new FileOutputStream(fileDescriptor));
                }
                if (jArr[1] == -1) {
                    ProcessImpl.this.stdout_stream = ProcessBuilder.NullInputStream.INSTANCE;
                } else {
                    FileDescriptor fileDescriptor2 = new FileDescriptor();
                    ProcessImpl.fdAccess.setHandle(fileDescriptor2, jArr[1]);
                    ProcessImpl.this.stdout_stream = new BufferedInputStream(new FileInputStream(fileDescriptor2));
                }
                if (jArr[2] == -1) {
                    ProcessImpl.this.stderr_stream = ProcessBuilder.NullInputStream.INSTANCE;
                    return null;
                }
                FileDescriptor fileDescriptor3 = new FileDescriptor();
                ProcessImpl.fdAccess.setHandle(fileDescriptor3, jArr[2]);
                ProcessImpl.this.stderr_stream = new FileInputStream(fileDescriptor3);
                return null;
            }
        });
    }

    @Override // java.lang.Process
    public OutputStream getOutputStream() {
        return this.stdin_stream;
    }

    @Override // java.lang.Process
    public InputStream getInputStream() {
        return this.stdout_stream;
    }

    @Override // java.lang.Process
    public InputStream getErrorStream() {
        return this.stderr_stream;
    }

    protected void finalize() {
        closeHandle(this.handle);
    }

    @Override // java.lang.Process
    public int exitValue() {
        int exitCodeProcess = getExitCodeProcess(this.handle);
        if (exitCodeProcess == STILL_ACTIVE) {
            throw new IllegalThreadStateException("process has not exited");
        }
        return exitCodeProcess;
    }

    @Override // java.lang.Process
    public int waitFor() throws InterruptedException {
        waitForInterruptibly(this.handle);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return exitValue();
    }

    @Override // java.lang.Process
    public boolean waitFor(long j2, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j2);
        if (getExitCodeProcess(this.handle) != STILL_ACTIVE) {
            return true;
        }
        if (j2 <= 0) {
            return false;
        }
        long jNanoTime = System.nanoTime() + nanos;
        do {
            long millis = TimeUnit.NANOSECONDS.toMillis(nanos + 999999);
            if (millis < 0) {
                millis = 2147483647L;
            }
            waitForTimeoutInterruptibly(this.handle, millis);
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            if (getExitCodeProcess(this.handle) != STILL_ACTIVE) {
                return true;
            }
            nanos = jNanoTime - System.nanoTime();
        } while (nanos > 0);
        return getExitCodeProcess(this.handle) != STILL_ACTIVE;
    }

    @Override // java.lang.Process
    public void destroy() {
        terminateProcess(this.handle);
    }

    @Override // java.lang.Process
    public Process destroyForcibly() {
        destroy();
        return this;
    }

    @Override // java.lang.Process
    public boolean isAlive() {
        return isProcessAlive(this.handle);
    }
}
