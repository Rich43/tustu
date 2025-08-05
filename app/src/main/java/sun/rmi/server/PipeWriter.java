package sun.rmi.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.AccessController;
import java.util.Date;
import sun.rmi.runtime.NewThreadAction;
import sun.security.action.GetPropertyAction;

/* compiled from: Activation.java */
/* loaded from: rt.jar:sun/rmi/server/PipeWriter.class */
class PipeWriter implements Runnable {
    private int cLast;
    private PrintWriter out;
    private InputStream in;
    private String pipeString;
    private String execString;
    private static int numExecs = 0;
    private static String lineSeparator = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));
    private static int lineSeparatorLength = lineSeparator.length();
    private ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
    private byte[] currSep = new byte[lineSeparatorLength];

    private PipeWriter(InputStream inputStream, OutputStream outputStream, String str, int i2) {
        this.in = inputStream;
        this.out = new PrintWriter(outputStream);
        this.execString = ":ExecGroup-" + Integer.toString(i2) + ':' + str + ':';
    }

    @Override // java.lang.Runnable
    public void run() {
        byte[] bArr = new byte[256];
        while (true) {
            try {
                int i2 = this.in.read(bArr);
                if (i2 == -1) {
                    break;
                } else {
                    write(bArr, 0, i2);
                }
            } catch (IOException e2) {
                return;
            }
        }
        String string = this.bufOut.toString();
        this.bufOut.reset();
        if (string.length() > 0) {
            this.out.println(createAnnotation() + string);
            this.out.flush();
        }
    }

    private void write(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 < 0) {
            throw new ArrayIndexOutOfBoundsException(i3);
        }
        for (int i4 = 0; i4 < i3; i4++) {
            write(bArr[i2 + i4]);
        }
    }

    private void write(byte b2) throws IOException {
        int i2 = 1;
        while (i2 < this.currSep.length) {
            this.currSep[i2 - 1] = this.currSep[i2];
            i2++;
        }
        this.currSep[i2 - 1] = b2;
        this.bufOut.write(b2);
        if (this.cLast >= lineSeparatorLength - 1 && lineSeparator.equals(new String(this.currSep))) {
            this.cLast = 0;
            this.out.print(createAnnotation() + this.bufOut.toString());
            this.out.flush();
            this.bufOut.reset();
            if (this.out.checkError()) {
                throw new IOException("PipeWriter: IO Exception when writing to output stream.");
            }
            return;
        }
        this.cLast++;
    }

    private String createAnnotation() {
        return new Date().toString() + this.execString;
    }

    static void plugTogetherPair(InputStream inputStream, OutputStream outputStream, InputStream inputStream2, OutputStream outputStream2) {
        int numExec = getNumExec();
        Thread thread = (Thread) AccessController.doPrivileged(new NewThreadAction(new PipeWriter(inputStream, outputStream, "out", numExec), "out", true));
        Thread thread2 = (Thread) AccessController.doPrivileged(new NewThreadAction(new PipeWriter(inputStream2, outputStream2, "err", numExec), "err", true));
        thread.start();
        thread2.start();
    }

    private static synchronized int getNumExec() {
        int i2 = numExecs;
        numExecs = i2 + 1;
        return i2;
    }
}
