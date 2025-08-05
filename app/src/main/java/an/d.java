package aN;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:aN/d.class */
class d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    JTextField f2636a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ a f2637b;

    d(a aVar, JTextField jTextField) {
        this.f2637b = aVar;
        this.f2636a = jTextField;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strB = bV.b(this.f2636a, "Load File for Difference", null, null, this.f2637b.b("lastDir", null));
        if (strB != null && strB.length() > 2) {
            this.f2636a.setText(strB);
            this.f2637b.a("lastDir", new File(strB).getParentFile().getAbsolutePath());
        }
        this.f2637b.b();
    }
}
