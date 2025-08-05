package as;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/* loaded from: TunerStudioMS.jar:as/n.class */
class n implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0855j f6265a;

    n(C0855j c0855j) {
        this.f6265a = c0855j;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strA = bV.a(h.i.f(h.i.f12326as, h.i.e("lastFileDir", h.h.d())), this.f6265a.f6259b);
        if (strA == null || strA.isEmpty()) {
            bV.d(this.f6265a.a("A valid Directory is required if downloading to a specific directory."), this.f6265a.f6258a);
            return;
        }
        File file = new File(strA);
        if (!file.exists() || !file.isDirectory()) {
            bV.d(file.getAbsolutePath() + "\n" + this.f6265a.a("is not a valid Directory."), this.f6265a.f6258a);
        } else if (!h.h.a(strA)) {
            bV.d(this.f6265a.a("Do not have write access to directory") + "\n" + file.getAbsolutePath(), this.f6265a.f6258a);
        } else {
            this.f6265a.f6258a.setText(strA);
            h.i.f(h.i.f12326as, strA);
        }
    }
}
