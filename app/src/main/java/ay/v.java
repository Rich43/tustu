package aY;

import G.R;
import G.T;
import W.ag;
import aP.C0338f;
import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aY/v.class */
class v implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ s f4083a;

    v(s sVar) {
        this.f4083a = sVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        R rC = T.a().c(this.f4083a.f4080f.a());
        ag agVarA = this.f4083a.f4076b.a();
        if (rC == null || agVarA == null) {
            bV.d(C1818g.b("You must select a Restore Point and a Controller to restore."), this.f4083a.f4076b);
        }
        C0338f.a().b(rC, agVarA.a().getAbsolutePath());
    }
}
