package bD;

import bH.W;
import com.efiAnalytics.remotefileaccess.FileDownloadProgressListener;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bD.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/b.class */
public class C0956b implements FileDownloadProgressListener {

    /* renamed from: a, reason: collision with root package name */
    List f6650a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f6651b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    boolean f6652c = false;

    /* renamed from: d, reason: collision with root package name */
    double f6653d = 0.0d;

    /* renamed from: e, reason: collision with root package name */
    long f6654e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f6655f = 0;

    /* renamed from: g, reason: collision with root package name */
    RemoteFileDescriptor f6656g = null;

    /* renamed from: h, reason: collision with root package name */
    long f6657h = 0;

    /* renamed from: i, reason: collision with root package name */
    InterfaceC0955a f6658i;

    public C0956b(InterfaceC0955a interfaceC0955a) {
        this.f6658i = interfaceC0955a;
    }

    public void a(RemoteFileDescriptor remoteFileDescriptor) {
        if (this.f6650a.contains(remoteFileDescriptor)) {
            bH.C.c("Tried to add a file again: " + remoteFileDescriptor.getName());
        } else {
            this.f6650a.add(remoteFileDescriptor);
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.FileDownloadProgressListener
    public void fileDownloadStarted(RemoteFileDescriptor remoteFileDescriptor) {
        if (!this.f6652c) {
            this.f6658i.a();
            this.f6655f = this.f6650a.size();
            Iterator it = this.f6650a.iterator();
            while (it.hasNext()) {
                this.f6653d += ((RemoteFileDescriptor) it.next()).getSize();
            }
            this.f6652c = true;
            String str = "Beginning download of " + remoteFileDescriptor.getName();
            if (this.f6655f > 1) {
                str = str + ", File 1 of " + this.f6655f;
            }
            this.f6658i.b().a(str);
            this.f6658i.b().a(-1.0d);
            this.f6657h = System.currentTimeMillis();
        }
        this.f6656g = remoteFileDescriptor;
    }

    @Override // com.efiAnalytics.remotefileaccess.FileDownloadProgressListener
    public void fileDownloadProgressUpdate(long j2, long j3) {
        long j4 = j2 + this.f6654e;
        this.f6658i.b().a(j4 / this.f6653d);
        if (this.f6656g != null) {
            double dCurrentTimeMillis = (System.currentTimeMillis() - this.f6657h) / 1000.0d;
            this.f6658i.b().a(this.f6650a.size() + this.f6651b.size() == 1 ? "Downloading " + this.f6656g.getName() + " \t" + W.a(j4) + " of " + W.a((long) this.f6653d) + " \t" + W.a((long) (j4 / dCurrentTimeMillis)) + "/sec" : "Downloading file " + (this.f6651b.size() + 1) + " of " + (this.f6650a.size() + this.f6651b.size()) + " (" + this.f6656g.getName() + ") \t" + W.a(j4) + " of " + W.a((long) this.f6653d) + " \t" + W.a((long) (j4 / dCurrentTimeMillis)) + "/sec");
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.FileDownloadProgressListener
    public void fileDownloadCompleted(RemoteFileDescriptor remoteFileDescriptor, File file) {
        this.f6650a.remove(remoteFileDescriptor);
        if (remoteFileDescriptor == null || file == null) {
            return;
        }
        if (remoteFileDescriptor.getSize() > file.length()) {
            this.f6653d -= remoteFileDescriptor.getSize() - file.length();
        }
        this.f6654e += file.length();
        this.f6658i.b().a(this.f6654e / this.f6653d);
        if (file != null && file.length() > 0) {
            this.f6651b.add(file);
        }
        try {
            if (this.f6650a.isEmpty()) {
                try {
                    if (this.f6658i.c() != null) {
                        this.f6658i.c().a(this.f6651b);
                    }
                    a();
                } catch (Exception e2) {
                    Logger.getLogger(C0956b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    a();
                }
            }
        } catch (Throwable th) {
            a();
            throw th;
        }
    }

    public void a() {
        this.f6658i.d();
        this.f6652c = false;
        this.f6655f = 0;
        this.f6654e = 0L;
        this.f6653d = 0.0d;
        this.f6656g = null;
        this.f6651b = new ArrayList();
    }
}
