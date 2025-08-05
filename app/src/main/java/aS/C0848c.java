package as;

import com.efiAnalytics.remotefileaccess.FileDownloadProgressListener;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import java.io.File;
import javax.swing.SwingUtilities;

/* renamed from: as.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/c.class */
class C0848c implements FileDownloadProgressListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0847b f6246a;

    C0848c(C0847b c0847b) {
        this.f6246a = c0847b;
    }

    @Override // com.efiAnalytics.remotefileaccess.FileDownloadProgressListener
    public void fileDownloadStarted(RemoteFileDescriptor remoteFileDescriptor) {
    }

    @Override // com.efiAnalytics.remotefileaccess.FileDownloadProgressListener
    public void fileDownloadProgressUpdate(long j2, long j3) {
    }

    @Override // com.efiAnalytics.remotefileaccess.FileDownloadProgressListener
    public void fileDownloadCompleted(RemoteFileDescriptor remoteFileDescriptor, File file) {
        SwingUtilities.invokeLater(new RunnableC0849d(this, file, new String[]{file.getAbsolutePath()}));
    }
}
