package aI;

import G.C0130m;
import G.C0132o;
import G.R;
import G.df;
import bH.C;
import com.efiAnalytics.remotefileaccess.DirectoryFiles;
import com.efiAnalytics.remotefileaccess.DirectoryIdentifier;
import com.efiAnalytics.remotefileaccess.FileDownloadProgressListener;
import com.efiAnalytics.remotefileaccess.RefreshNeededListener;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.remotefileaccess.RemoteFileAccess;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/l.class */
public class l implements q, r, s, RemoteFileAccess {

    /* renamed from: a, reason: collision with root package name */
    R f2464a;

    /* renamed from: b, reason: collision with root package name */
    j f2465b;

    /* renamed from: c, reason: collision with root package name */
    boolean f2466c = true;

    /* renamed from: d, reason: collision with root package name */
    boolean f2467d = false;

    /* renamed from: e, reason: collision with root package name */
    boolean f2468e = true;

    /* renamed from: m, reason: collision with root package name */
    private List f2469m = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    m f2470f = null;

    /* renamed from: n, reason: collision with root package name */
    private List f2471n = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    o f2472g = null;

    /* renamed from: h, reason: collision with root package name */
    e f2473h = null;

    /* renamed from: i, reason: collision with root package name */
    b f2474i = null;

    /* renamed from: j, reason: collision with root package name */
    long f2475j = 0;

    /* renamed from: k, reason: collision with root package name */
    int f2476k = 0;

    /* renamed from: l, reason: collision with root package name */
    File f2477l;

    public l(R r2, File file) {
        this.f2464a = null;
        this.f2465b = null;
        this.f2477l = null;
        this.f2464a = r2;
        this.f2477l = file;
        this.f2465b = new j(r2);
        f();
    }

    private void f() {
        this.f2470f = new m(this);
        this.f2470f.start();
        this.f2472g = o.d(this.f2464a);
        this.f2472g.a(this);
        this.f2473h = new e(this.f2464a, this);
        this.f2473h.a(this.f2464a.O().aq());
        this.f2474i = b.d(this.f2464a);
        t.d(this.f2464a).a(this);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public DirectoryFiles getFilesIn(DirectoryIdentifier directoryIdentifier) throws RemoteAccessException {
        u uVarA = a(directoryIdentifier);
        DirectoryFiles directoryFiles = new DirectoryFiles();
        directoryFiles.setDirectoryInformation(uVarA);
        this.f2465b.a();
        if (!t.d(this.f2464a).a()) {
            directoryFiles.setFiles(new ArrayList());
            return directoryFiles;
        }
        List listA = this.f2465b.a(uVarA.getFileCount());
        long size = 0;
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            size += ((RemoteFileDescriptor) it.next()).getSize();
        }
        uVarA.a(size);
        directoryFiles.setFiles(listA);
        uVarA.a(listA.size());
        return directoryFiles;
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public synchronized File readRemoteFile(File file, RemoteFileDescriptor remoteFileDescriptor) throws RemoteAccessException {
        File fileA = null;
        if (!this.f2464a.R()) {
            RemoteAccessException remoteAccessException = new RemoteAccessException(a("Please power on and connect to your MS3 before downloading file(s)."));
            remoteAccessException.setTerminalToBatch(true);
            throw remoteAccessException;
        }
        if (this.f2474i.a()) {
            RemoteAccessException remoteAccessException2 = new RemoteAccessException(a("Please turn off the engine before downloading file(s)."));
            remoteAccessException2.setTerminalToBatch(true);
            throw remoteAccessException2;
        }
        g();
        if (this.f2472g.d()) {
            RemoteAccessException remoteAccessException3 = new RemoteAccessException(a("SD Card is Actively Logging. Please Stop Logging to Download Files."));
            remoteAccessException3.setTerminalToBatch(true);
            throw remoteAccessException3;
        }
        if (!this.f2472g.c()) {
            RemoteAccessException remoteAccessException4 = new RemoteAccessException(a("SD Card is Not Ready for File Download."));
            remoteAccessException4.setTerminalToBatch(true);
            throw remoteAccessException4;
        }
        this.f2470f.b();
        try {
            try {
                this.f2472g.a(3000);
                try {
                    if (this.f2464a.O().T()) {
                        df.a(this.f2464a);
                    }
                } catch (V.g e2) {
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                fileA = this.f2473h.a(file, remoteFileDescriptor);
                this.f2475j = System.currentTimeMillis();
                try {
                    df.b(this.f2464a);
                } catch (V.g e3) {
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
                this.f2472g.b();
                a(remoteFileDescriptor, fileA);
                return fileA;
            } catch (RemoteAccessException e4) {
                throw e4;
            } catch (IOException e5) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                throw new RemoteAccessException("Error Writing File to Local File System. Error:\n" + e5.getMessage());
            }
        } catch (Throwable th) {
            try {
                df.b(this.f2464a);
            } catch (V.g e6) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
            }
            this.f2472g.b();
            a(remoteFileDescriptor, fileA);
            throw th;
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void cancelReadFile() {
        if (this.f2473h != null) {
            this.f2472g.a();
            this.f2470f.b();
            this.f2473h.a();
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public synchronized boolean deleteFile(RemoteFileDescriptor remoteFileDescriptor) throws RemoteAccessException {
        g();
        if (this.f2472g.d()) {
            throw new RemoteAccessException(a("SD Card is Actively Logging. Please Stop Logging to Delete Files."));
        }
        if (!this.f2472g.c()) {
            throw new RemoteAccessException(a("SD Card is Not Ready for Delete Operations."));
        }
        char[] cArr = new char[4];
        String name = remoteFileDescriptor.getName();
        if (name.indexOf(".") < 5) {
            throw new RemoteAccessException(a("File Name not of proper format for delete call.\nFormat: LOG####.MS3"));
        }
        String strSubstring = name.substring(name.indexOf(".") - 4, name.indexOf("."));
        cArr[0] = strSubstring.charAt(0);
        cArr[1] = strSubstring.charAt(1);
        cArr[2] = strSubstring.charAt(2);
        cArr[3] = strSubstring.charAt(3);
        this.f2470f.b();
        C.c("cancelled Refresh: " + System.currentTimeMillis());
        C0132o c0132oA = this.f2472g.a(d.a(this.f2464a.O(), cArr, 0), true, 1500);
        if (c0132oA.a() == 3) {
            throw new RemoteAccessException("Delete of " + remoteFileDescriptor.getName() + " failed." + (c0132oA.c() != null ? "\nMessage:" + c0132oA.c() : ""));
        }
        C.c("SD Deleted " + remoteFileDescriptor.getName());
        C.c("requested Refresh: " + System.currentTimeMillis());
        this.f2470f.a();
        return true;
    }

    private synchronized void g() throws RemoteAccessException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (!this.f2472g.c()) {
            if (!this.f2464a.R()) {
                RemoteAccessException remoteAccessException = new RemoteAccessException(a("The Controller has gone offline, can not complete SD action."));
                remoteAccessException.setTerminalToBatch(true);
                throw remoteAccessException;
            }
            if (!this.f2468e) {
                RemoteAccessException remoteAccessException2 = new RemoteAccessException(a("No SD Card in the MS3."));
                remoteAccessException2.setTerminalToBatch(true);
                throw remoteAccessException2;
            }
            if (System.currentTimeMillis() - jCurrentTimeMillis > 10000) {
                RemoteAccessException remoteAccessException3 = new RemoteAccessException(a("Timeout waiting for SD Card to become Ready.\nWaited 10 seconds, expected complete."));
                remoteAccessException3.setTerminalToBatch(true);
                throw remoteAccessException3;
            }
            try {
                wait(10L);
            } catch (InterruptedException e2) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a() {
    }

    public u a(DirectoryIdentifier directoryIdentifier) throws RemoteAccessException {
        if (!this.f2464a.R()) {
            throw new RemoteAccessException(a("MS3 not connected or powered off."));
        }
        if (!this.f2468e) {
            throw new RemoteAccessException(a("SD Card Card Not In."));
        }
        if (this.f2472g.d()) {
            throw new RemoteAccessException(a("SD Card is Actively Logging. Click Reset and Wait to Stop Logging."));
        }
        g();
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(d.a(this.f2464a.O()));
            arrayList.add(d.b(this.f2464a.O()));
            C0130m c0130mA = C0130m.a(this.f2464a.O(), arrayList);
            c0130mA.v("SD Status Command ");
            C0132o c0132oA = this.f2472g.a(c0130mA, false, 1500);
            if (c0132oA.a() == 3 || c0132oA.a() == 2) {
                throw new RemoteAccessException(a("Failed to get MS3 SD Card Extended Status.") + "\nMessage:" + c0132oA.c());
            }
            u uVar = new u();
            uVar.a(c0132oA.e());
            return uVar;
        } catch (x e2) {
            throw new RemoteAccessException("Unknown SD Command:\n" + e2.getMessage());
        } catch (RemoteAccessException e3) {
            throw e3;
        } catch (Exception e4) {
            if (e4.getMessage() == null || e4.getMessage().length() == 0) {
                throw new RemoteAccessException(a("Failed to get MS3 SD Card Extended Status."));
            }
            throw new RemoteAccessException(a("Failed to get MS3 SD Card Extended Status.") + "\nMessage:\n" + e4.getMessage());
        }
    }

    private String a(String str) {
        try {
            return C.a.a().a("", str);
        } catch (Exception e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return str;
        }
    }

    public void b() {
        this.f2472g.b(this);
        t.d(this.f2464a).b(this);
        if (this.f2470f != null) {
            this.f2470f.f2479b = false;
        }
        this.f2473h.a();
    }

    public void c() {
        h();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        Iterator it = this.f2469m.iterator();
        while (it.hasNext()) {
            ((RefreshNeededListener) it.next()).refreshView();
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void addRefreshNeededListener(RefreshNeededListener refreshNeededListener) {
        this.f2469m.add(refreshNeededListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void removeRefreshNeededListener(RefreshNeededListener refreshNeededListener) {
        this.f2469m.remove(refreshNeededListener);
    }

    @Override // aI.q
    public void a(boolean z2) {
        if (z2 && !this.f2466c) {
            a();
            if (!this.f2467d) {
                this.f2470f.a();
            }
        } else if (!z2) {
            this.f2470f.b();
        }
        this.f2466c = z2;
    }

    @Override // aI.s
    public void d() {
        this.f2468e = true;
    }

    @Override // aI.s
    public void e() {
        this.f2468e = false;
        c();
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public File getDownloadDirectory() {
        return this.f2477l;
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void addFileDownloadProgressListener(FileDownloadProgressListener fileDownloadProgressListener) {
        this.f2471n.add(fileDownloadProgressListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void removeFileDownloadProgressListener(FileDownloadProgressListener fileDownloadProgressListener) {
        this.f2471n.remove(fileDownloadProgressListener);
    }

    private void b(RemoteFileDescriptor remoteFileDescriptor) {
        Iterator it = this.f2471n.iterator();
        while (it.hasNext()) {
            ((FileDownloadProgressListener) it.next()).fileDownloadStarted(remoteFileDescriptor);
        }
    }

    private void a(RemoteFileDescriptor remoteFileDescriptor, File file) {
        Iterator it = this.f2471n.iterator();
        while (it.hasNext()) {
            ((FileDownloadProgressListener) it.next()).fileDownloadCompleted(remoteFileDescriptor, file);
        }
    }

    private void b(long j2, long j3) {
        Iterator it = this.f2471n.iterator();
        while (it.hasNext()) {
            ((FileDownloadProgressListener) it.next()).fileDownloadProgressUpdate(j2, j3);
        }
    }

    @Override // aI.r
    public void a(RemoteFileDescriptor remoteFileDescriptor) {
        b(remoteFileDescriptor);
    }

    @Override // aI.r
    public void a(long j2, long j3) {
        b(j2, j3);
    }

    @Override // aI.q
    public void b(boolean z2) {
        if (!z2 && this.f2467d && this.f2466c) {
            this.f2470f.a();
        } else if (z2) {
            this.f2470f.b();
        }
        this.f2467d = z2;
    }
}
