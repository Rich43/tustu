package aY;

import G.R;
import G.T;
import W.ag;
import aP.C0338f;
import aP.cZ;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aY/w.class */
class w implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ s f4084a;

    w(s sVar) {
        this.f4084a = sVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        R rC = T.a().c(this.f4084a.f4080f.a());
        ag agVarA = this.f4084a.f4076b.a();
        if (rC == null || agVarA == null) {
            bV.d(C1818g.b("You must select a Restore Point and a Controller to restore."), this.f4084a.f4076b);
        }
        JFrame jFrameC = cZ.a().c();
        if (bV.a(C1818g.b("Load Restore Point to replace current settings?") + "\n\n" + C1818g.b("All current settings will be over written with the settings in the selected Restore Point.") + "\n\n" + C1818g.b("Selected Restore Point") + ": " + agVarA.d(C1798a.cw), (Component) jFrameC, true)) {
            C0338f.a().d(rC, "Before loading Restore Point " + agVarA.d(C1798a.cw) + " to " + rC.c());
            C0338f.a().a(jFrameC, rC, agVarA.a().getAbsolutePath());
        }
    }
}
