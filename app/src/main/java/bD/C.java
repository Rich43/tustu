package bD;

import com.efiAnalytics.remotefileaccess.DirectoryFiles;
import com.efiAnalytics.remotefileaccess.DirectoryInformation;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;

/* loaded from: TunerStudioMS.jar:bD/C.class */
class C extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ r f6611a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C(r rVar) {
        super("Read Dir Thread");
        this.f6611a = rVar;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            try {
                this.f6611a.f6687a.c();
                this.f6611a.f6687a.a("Reading Files");
                DirectoryFiles filesIn = this.f6611a.f6686j.getFilesIn(null);
                this.f6611a.f6687a.a(filesIn.getFiles());
                DirectoryInformation directoryInformation = filesIn.getDirectoryInformation();
                this.f6611a.f6691e.a(directoryInformation.getFileCount());
                this.f6611a.f6691e.b(directoryInformation.getUsedBytes());
                this.f6611a.f6691e.a(directoryInformation.getTotalBytes());
                this.f6611a.f6687a.d();
                this.f6611a.f6688b = null;
            } catch (RemoteAccessException e2) {
                bH.C.c("Dir Listing interrupted: " + e2.getMessage());
                this.f6611a.f6687a.a(e2.getMessage());
                this.f6611a.f6688b = null;
            }
        } catch (Throwable th) {
            this.f6611a.f6688b = null;
            throw th;
        }
    }
}
