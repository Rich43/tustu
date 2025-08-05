package aS;

import aP.cZ;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aS/c.class */
class c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ b f3905a;

    c(b bVar) {
        this.f3905a = bVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (this.f3905a.f3904g.f3897g) {
            JPanel jPanel = new JPanel();
            jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Settings Error!")));
            jPanel.setLayout(new BorderLayout());
            JLabel jLabel = new JLabel(this.f3905a.a());
            jLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            jPanel.add(BorderLayout.CENTER, jLabel);
            bV.a(jPanel, cZ.a().c(), C1818g.b("Error") + "!", new d(this));
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e2) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        String str = C1818g.b("Settings Configuration Error!") + "\n" + C1818g.b(C1798a.f13268b + " is online") + "\n" + C1818g.b("Correct Settings and Power Cycle Controller.");
        if (this.f3905a.f3901d != null && this.f3905a.f3901d.length() > 0) {
            str = str + "\n \nReported Setting Error:       \n" + this.f3905a.f3901d;
        }
        this.f3905a.f3903f = cZ.a().b();
        this.f3905a.f3903f.l(str + "\n\nYou must correct this error, then power cycle your controller.");
    }
}
