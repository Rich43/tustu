package bD;

import com.efiAnalytics.remotefileaccess.DirectoryFiles;
import com.efiAnalytics.remotefileaccess.DirectoryInformation;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bD/p.class */
class p extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0963i f6684a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    p(C0963i c0963i) {
        super("Read Dir");
        this.f6684a = c0963i;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            try {
                C0963i.a(this.f6684a.f6671c);
                DirectoryFiles filesIn = this.f6684a.f6668h.getFilesIn(null);
                filesIn.getFiles();
                DirectoryInformation directoryInformation = filesIn.getDirectoryInformation();
                this.f6684a.f6671c.a(directoryInformation.getFileCount());
                this.f6684a.f6671c.b(directoryInformation.getUsedBytes());
                this.f6684a.f6671c.a(directoryInformation.getTotalBytes());
                this.f6684a.f6669a = null;
                C0963i.b(this.f6684a.f6671c);
            } catch (RemoteAccessException e2) {
                Logger.getLogger(r.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                this.f6684a.f6669a = null;
                C0963i.b(this.f6684a.f6671c);
            }
        } catch (Throwable th) {
            this.f6684a.f6669a = null;
            C0963i.b(this.f6684a.f6671c);
            throw th;
        }
    }
}
