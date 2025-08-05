package ah;

import ae.C0500d;
import ae.u;
import bH.C;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/* renamed from: ah.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ah/a.class */
public class C0508a {

    /* renamed from: b, reason: collision with root package name */
    private String f4499b;

    /* renamed from: c, reason: collision with root package name */
    private int f4500c;

    /* renamed from: a, reason: collision with root package name */
    private final FTPClient f4498a = new FTPClient();

    /* renamed from: d, reason: collision with root package name */
    private String f4501d = "anonymous";

    /* renamed from: e, reason: collision with root package name */
    private String f4502e = "fwLoader@efianalytics.com";

    public C0508a(String str, int i2) {
        this.f4499b = str;
        this.f4500c = i2;
    }

    public C0500d a(File file) {
        C0500d c0500d = new C0500d();
        try {
            a().deleteFile("./" + file.getName());
            c0500d.a(C0500d.f4346a);
            return c0500d;
        } catch (RemoteAccessException e2) {
            Logger.getLogger(C0508a.class.getName()).log(Level.WARNING, "Failed to delete file from box", (Throwable) e2);
            c0500d.a(C0500d.f4347b);
            return c0500d;
        } catch (IOException e3) {
            Logger.getLogger(C0508a.class.getName()).log(Level.WARNING, "Failed to delete file from box", (Throwable) e3);
            c0500d.a(C0500d.f4347b);
            return c0500d;
        }
    }

    public C0500d a(File file, u uVar) {
        C0500d c0500d = new C0500d();
        OutputStream outputStreamStoreFileStream = null;
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    uVar.a(0.0d);
                    outputStreamStoreFileStream = a().storeFileStream(file.getName());
                    fileInputStream = new FileInputStream(file);
                    byte[] bArr = new byte[4096];
                    long j2 = 0;
                    long length = file.length();
                    if (outputStreamStoreFileStream != null) {
                        while (true) {
                            int i2 = fileInputStream.read(bArr);
                            if (i2 == -1) {
                                break;
                            }
                            outputStreamStoreFileStream.write(bArr, 0, i2);
                            j2 += i2;
                            uVar.a((int) ((j2 * 100) / length));
                        }
                    } else {
                        C.b("Failed to get FTP Stream, using alternative method.");
                        a().storeFile(file.getName(), fileInputStream);
                    }
                    a().completePendingCommand();
                    c0500d.a(C0500d.f4346a);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e2) {
                        }
                    }
                    if (outputStreamStoreFileStream != null) {
                        try {
                            outputStreamStoreFileStream.close();
                        } catch (Exception e3) {
                        }
                    }
                    b();
                    return c0500d;
                } catch (RemoteAccessException e4) {
                    Logger.getLogger(C0508a.class.getName()).log(Level.SEVERE, "Upload File Failed.", (Throwable) e4);
                    c0500d.a(C0500d.f4347b);
                    c0500d.a(e4.getLocalizedMessage());
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e5) {
                        }
                    }
                    if (outputStreamStoreFileStream != null) {
                        try {
                            outputStreamStoreFileStream.close();
                        } catch (Exception e6) {
                        }
                    }
                    b();
                    return c0500d;
                }
            } catch (IOException e7) {
                Logger.getLogger(C0508a.class.getName()).log(Level.SEVERE, "Upload File Failed.", (Throwable) e7);
                c0500d.a(C0500d.f4347b);
                c0500d.a(e7.getLocalizedMessage());
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e8) {
                    }
                }
                if (outputStreamStoreFileStream != null) {
                    try {
                        outputStreamStoreFileStream.close();
                    } catch (Exception e9) {
                    }
                }
                b();
                return c0500d;
            }
        } catch (Throwable th) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e10) {
                }
            }
            if (outputStreamStoreFileStream != null) {
                try {
                    outputStreamStoreFileStream.close();
                } catch (Exception e11) {
                }
            }
            b();
            throw th;
        }
    }

    private FTPClient a() throws RemoteAccessException {
        if (!this.f4498a.isConnected()) {
            try {
                this.f4498a.connect(this.f4499b, this.f4500c);
                a(this.f4498a);
                if (!FTPReply.isPositiveCompletion(this.f4498a.getReplyCode())) {
                    this.f4498a.disconnect();
                    throw new RemoteAccessException("Connection Refused");
                }
                try {
                    if (!this.f4498a.login(this.f4501d, this.f4502e) || !FTPReply.isPositiveCompletion(this.f4498a.getReplyCode())) {
                        a(this.f4498a);
                        b();
                        throw new RemoteAccessException("Invalid User and/or password");
                    }
                    a(this.f4498a);
                    try {
                        this.f4498a.setFileType(2);
                        a(this.f4498a);
                    } catch (IOException e2) {
                        b();
                        throw new RemoteAccessException("Unable to set Transfertype to binary: " + e2.getLocalizedMessage());
                    }
                } catch (IOException e3) {
                    b();
                    throw new RemoteAccessException("Login failed: " + e3.getLocalizedMessage());
                }
            } catch (IOException e4) {
                throw new RemoteAccessException(e4.getMessage());
            }
        }
        return this.f4498a;
    }

    private void b() {
        try {
            if (this.f4498a != null && this.f4498a.isConnected()) {
                this.f4498a.logout();
                C.c("Logged out FTP Session.");
                this.f4498a.disconnect();
                C.c("Disconnected FTP Session.");
                Thread.sleep(10L);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void a(FTPClient fTPClient) {
        String[] replyStrings = fTPClient.getReplyStrings();
        if (replyStrings == null || replyStrings.length <= 0) {
            return;
        }
        for (String str : replyStrings) {
            C.d("SERVER: " + str);
        }
    }

    public void a(String str) {
        this.f4501d = str;
    }

    public void b(String str) {
        this.f4502e = str;
    }
}
