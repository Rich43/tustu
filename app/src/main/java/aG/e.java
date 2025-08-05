package aG;

import B.o;
import G.C0081bn;
import G.R;
import bH.C;
import bH.C1007o;
import bQ.l;
import com.efiAnalytics.remotefileaccess.DirectoryFiles;
import com.efiAnalytics.remotefileaccess.DirectoryIdentifier;
import com.efiAnalytics.remotefileaccess.DirectoryInformation;
import com.efiAnalytics.remotefileaccess.FileDownloadProgressListener;
import com.efiAnalytics.remotefileaccess.RefreshNeededListener;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.remotefileaccess.RemoteFileAccess;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: TunerStudioMS.jar:aG/e.class */
public class e implements RemoteFileAccess {

    /* renamed from: a, reason: collision with root package name */
    C0081bn f2391a;

    /* renamed from: b, reason: collision with root package name */
    R f2392b;

    /* renamed from: c, reason: collision with root package name */
    g f2393c;

    /* renamed from: i, reason: collision with root package name */
    private String f2394i;

    /* renamed from: j, reason: collision with root package name */
    private int f2395j;

    /* renamed from: d, reason: collision with root package name */
    String f2396d;

    /* renamed from: e, reason: collision with root package name */
    String f2397e;

    /* renamed from: f, reason: collision with root package name */
    File f2398f;

    /* renamed from: h, reason: collision with root package name */
    private final FTPClient f2390h = new FTPClient();

    /* renamed from: k, reason: collision with root package name */
    private final List f2399k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    private final List f2400l = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    boolean f2401g = false;

    public e(R r2, C0081bn c0081bn, File file) {
        this.f2393c = null;
        this.f2394i = "connectedController";
        this.f2395j = 22021;
        this.f2396d = "anonymous";
        this.f2397e = "sd@efianalytics.com";
        this.f2392b = r2;
        this.f2391a = c0081bn;
        this.f2398f = file;
        if (c0081bn.a() != null && !c0081bn.a().isEmpty()) {
            this.f2394i = c0081bn.a();
        }
        if (this.f2394i == null || this.f2394i.equals("connectedController")) {
            this.f2394i = b();
        }
        this.f2395j = c0081bn.b();
        if (c0081bn.c() != null && !c0081bn.c().isEmpty()) {
            this.f2396d = c0081bn.c();
        }
        if (c0081bn.d() != null && !c0081bn.d().isEmpty()) {
            this.f2397e = c0081bn.d();
        }
        this.f2393c = new g(this);
        this.f2393c.start();
    }

    private String b() {
        if (this.f2392b.C() instanceof l) {
            return ((l) this.f2392b.C()).a().a(o.f194i).toString();
        }
        return null;
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public DirectoryFiles getFilesIn(DirectoryIdentifier directoryIdentifier) throws RemoteAccessException {
        c();
        try {
            try {
                FTPClient fTPClientE = e();
                DirectoryFiles directoryFiles = new DirectoryFiles();
                d dVar = (d) a(directoryIdentifier);
                directoryFiles.setDirectoryInformation(dVar);
                ArrayList arrayList = new ArrayList();
                FTPFile[] fTPFileArrListFiles = fTPClientE.listFiles((directoryIdentifier == null || directoryIdentifier.getDirectoryId() == null) ? "." : directoryIdentifier.getDirectoryId(), new f(this));
                a(fTPClientE);
                long size = 0;
                int i2 = 0;
                for (FTPFile fTPFile : fTPFileArrListFiles) {
                    if (!fTPFile.isDirectory()) {
                        RemoteFileDescriptor remoteFileDescriptor = new RemoteFileDescriptor();
                        remoteFileDescriptor.setName(fTPFile.getName());
                        remoteFileDescriptor.setDirectory(directoryIdentifier);
                        remoteFileDescriptor.setSize(fTPFile.getSize());
                        remoteFileDescriptor.setLastModified(fTPFile.getTimestamp().getTimeInMillis());
                        arrayList.add(remoteFileDescriptor);
                        i2++;
                    }
                    size += fTPFile.getSize();
                }
                directoryFiles.setFiles(arrayList);
                dVar.a(size);
                dVar.a(i2);
                f();
                return directoryFiles;
            } catch (IOException e2) {
                throw new RemoteAccessException("Error Listing files: " + e2.getLocalizedMessage());
            }
        } catch (Throwable th) {
            f();
            throw th;
        }
    }

    private void c() throws RemoteAccessException {
        if (this.f2391a.f() == null || this.f2391a.f().b() == null || this.f2391a.f().b().isEmpty()) {
            return;
        }
        String strB = this.f2391a.f().b();
        try {
            if (C1007o.a(strB, this.f2392b)) {
            } else {
                throw new RemoteAccessException(this.f2391a.f().a());
            }
        } catch (V.g e2) {
            Logger.getLogger(e.class.getName()).log(Level.WARNING, "Failed to evaluate BrowseEnable condtion: " + strB, (Throwable) e2);
        }
    }

    private void d() throws RemoteAccessException {
        if (this.f2391a.g() == null || this.f2391a.g().b() == null || this.f2391a.g().b().isEmpty()) {
            return;
        }
        String strB = this.f2391a.g().b();
        try {
            if (C1007o.a(strB, this.f2392b)) {
            } else {
                throw new RemoteAccessException(this.f2391a.g().a());
            }
        } catch (V.g e2) {
            Logger.getLogger(e.class.getName()).log(Level.WARNING, "Failed to evaluate BrowseEnable condtion: " + strB, (Throwable) e2);
        }
    }

    private FTPClient e() throws RemoteAccessException {
        if (!this.f2390h.isConnected()) {
            try {
                this.f2390h.connect(this.f2394i, this.f2395j);
                a(this.f2390h);
                if (!FTPReply.isPositiveCompletion(this.f2390h.getReplyCode())) {
                    this.f2390h.disconnect();
                    throw new RemoteAccessException("Connection Refused");
                }
                try {
                    if (!this.f2390h.login(this.f2396d, this.f2397e) || !FTPReply.isPositiveCompletion(this.f2390h.getReplyCode())) {
                        a(this.f2390h);
                        f();
                        throw new RemoteAccessException("Invalid User and/or password");
                    }
                    a(this.f2390h);
                    if (this.f2391a.h()) {
                        this.f2390h.enterLocalPassiveMode();
                    }
                    try {
                        this.f2390h.setFileType(2);
                        a(this.f2390h);
                    } catch (IOException e2) {
                        f();
                        throw new RemoteAccessException("Unable to set Transfertype to binary: " + e2.getLocalizedMessage());
                    }
                } catch (IOException e3) {
                    f();
                    throw new RemoteAccessException("Login failed: " + e3.getLocalizedMessage());
                }
            } catch (IOException e4) {
                throw new RemoteAccessException(e4.getMessage());
            }
        }
        return this.f2390h;
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public File readRemoteFile(File file, RemoteFileDescriptor remoteFileDescriptor) throws RemoteAccessException {
        int i2;
        this.f2401g = false;
        d();
        File file2 = new File(file, remoteFileDescriptor.getName());
        AutoCloseable autoCloseable = null;
        FTPClient fTPClientE = e();
        try {
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                InputStream inputStreamRetrieveFileStream = fTPClientE.retrieveFileStream("./" + remoteFileDescriptor.getName());
                a(this.f2390h);
                byte[] bArr = new byte[4096];
                int size = (int) remoteFileDescriptor.getSize();
                int i3 = 0;
                a(remoteFileDescriptor);
                while (!this.f2401g && (i2 = inputStreamRetrieveFileStream.read(bArr)) != -1) {
                    bufferedOutputStream.write(bArr, 0, i2);
                    i3 += i2;
                    a(i3, size);
                }
                if (this.f2401g) {
                    a(fTPClientE);
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (0 == 0) {
                        file2.delete();
                    }
                    f();
                    return null;
                }
                a(fTPClientE);
                try {
                    bufferedOutputStream.close();
                } catch (IOException e3) {
                    C.c("Failed to close OutputStream for FTP.");
                }
                a(remoteFileDescriptor, file2);
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e4) {
                    }
                }
                if (1 == 0) {
                    file2.delete();
                }
                f();
                return file2;
            } catch (IOException e5) {
                e5.printStackTrace();
                throw new RemoteAccessException("Unable to read file: " + e5.getLocalizedMessage());
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    autoCloseable.close();
                } catch (IOException e6) {
                }
            }
            if (0 == 0) {
                file2.delete();
            }
            f();
            throw th;
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public boolean deleteFile(RemoteFileDescriptor remoteFileDescriptor) throws RemoteAccessException {
        d();
        try {
            FTPClient fTPClientE = e();
            this.f2393c.b();
            fTPClientE.deleteFile(remoteFileDescriptor.getName());
            this.f2393c.a();
            a(fTPClientE);
            if (FTPReply.isPositiveCompletion(fTPClientE.getReplyCode())) {
                return true;
            }
            String[] replyStrings = fTPClientE.getReplyStrings();
            if (replyStrings == null || replyStrings.length <= 0) {
                return false;
            }
            throw new RemoteAccessException("Delete failed: " + replyStrings[0]);
        } catch (IOException e2) {
            throw new RemoteAccessException("Delete failed: " + e2.getLocalizedMessage());
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public File getDownloadDirectory() {
        return this.f2398f;
    }

    public DirectoryInformation a(DirectoryIdentifier directoryIdentifier) {
        return new d();
    }

    public void a() {
        f();
        if (this.f2393c != null) {
            this.f2393c.f2404b = false;
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void addRefreshNeededListener(RefreshNeededListener refreshNeededListener) {
        this.f2400l.add(refreshNeededListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void removeRefreshNeededListener(RefreshNeededListener refreshNeededListener) {
        this.f2400l.remove(refreshNeededListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void addFileDownloadProgressListener(FileDownloadProgressListener fileDownloadProgressListener) {
        this.f2399k.add(fileDownloadProgressListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void removeFileDownloadProgressListener(FileDownloadProgressListener fileDownloadProgressListener) {
        this.f2399k.remove(fileDownloadProgressListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void cancelReadFile() {
        this.f2401g = true;
    }

    private void f() {
        try {
            if (this.f2390h != null && this.f2390h.isConnected()) {
                this.f2390h.logout();
                C.c("Logged out FTP Session.");
                this.f2390h.disconnect();
                C.c("Disconnected FTP Session.");
                Thread.sleep(10L);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void a(RemoteFileDescriptor remoteFileDescriptor) {
        Iterator it = this.f2399k.iterator();
        while (it.hasNext()) {
            ((FileDownloadProgressListener) it.next()).fileDownloadStarted(remoteFileDescriptor);
        }
    }

    private void a(long j2, long j3) {
        Iterator it = this.f2399k.iterator();
        while (it.hasNext()) {
            ((FileDownloadProgressListener) it.next()).fileDownloadProgressUpdate(j2, j3);
        }
    }

    private void a(RemoteFileDescriptor remoteFileDescriptor, File file) {
        Iterator it = this.f2399k.iterator();
        while (it.hasNext()) {
            ((FileDownloadProgressListener) it.next()).fileDownloadCompleted(remoteFileDescriptor, file);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        Iterator it = this.f2400l.iterator();
        while (it.hasNext()) {
            ((RefreshNeededListener) it.next()).refreshView();
        }
    }
}
