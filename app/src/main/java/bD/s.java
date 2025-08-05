package bD;

import com.efiAnalytics.remotefileaccess.RemoteFileAccess;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bD/s.class */
class s implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ RemoteFileAccess f6697a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ r f6698b;

    s(r rVar, RemoteFileAccess remoteFileAccess) {
        this.f6698b = rVar;
        this.f6697a = remoteFileAccess;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6698b.f6696i = true;
        this.f6697a.cancelReadFile();
        this.f6698b.f6693g.a();
    }
}
