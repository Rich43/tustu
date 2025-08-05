package sun.net.smtp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/* compiled from: SmtpClient.java */
/* loaded from: rt.jar:sun/net/smtp/SmtpPrintStream.class */
class SmtpPrintStream extends PrintStream {
    private SmtpClient target;
    private int lastc;

    SmtpPrintStream(OutputStream outputStream, SmtpClient smtpClient) throws UnsupportedEncodingException {
        super(outputStream, false, smtpClient.getEncoding());
        this.lastc = 10;
        this.target = smtpClient;
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.target == null) {
            return;
        }
        if (this.lastc != 10) {
            write(10);
        }
        try {
            this.target.issueCommand(".\r\n", 250);
            this.target.message = null;
            this.out = null;
            this.target = null;
        } catch (IOException e2) {
        }
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) {
        try {
            if (this.lastc == 10 && i2 == 46) {
                this.out.write(46);
            }
            if (i2 == 10 && this.lastc != 13) {
                this.out.write(13);
            }
            this.out.write(i2);
            this.lastc = i2;
        } catch (IOException e2) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) {
        try {
            byte b2 = this.lastc;
            while (true) {
                i3--;
                if (i3 >= 0) {
                    int i4 = i2;
                    i2++;
                    byte b3 = bArr[i4];
                    if (b2 == 10 && b3 == 46) {
                        this.out.write(46);
                    }
                    if (b3 == 10 && b2 != 13) {
                        this.out.write(13);
                    }
                    this.out.write(b3);
                    b2 = b3;
                } else {
                    this.lastc = b2;
                    return;
                }
            }
        } catch (IOException e2) {
        }
    }

    @Override // java.io.PrintStream
    public void print(String str) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            write(str.charAt(i2));
        }
    }
}
