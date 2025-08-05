package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.db, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/db.class */
class C0692db implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5549a;

    C0692db(bP bPVar) {
        this.f5549a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c("fieldSelectionStyle", "selectFromDash");
        } else {
            h.i.c("fieldSelectionStyle", "standardSelection");
        }
        com.efiAnalytics.ui.bV.d("The Changes will take effect after restarting.", C0645bi.a().b());
    }
}
