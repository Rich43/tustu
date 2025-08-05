package aP;

import com.efiAnalytics.ui.BinTableView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gc.class */
class C0368gc implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3479a;

    C0368gc(C0308dx c0308dx) {
        this.f3479a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        C1798a.a().b(C1798a.cg, Boolean.toString(jCheckBoxMenuItem.getState()));
        BinTableView.i(jCheckBoxMenuItem.getState());
    }
}
