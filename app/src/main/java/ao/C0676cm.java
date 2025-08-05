package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.cm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cm.class */
class C0676cm implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5496a;

    C0676cm(bP bPVar) {
        this.f5496a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        at.c.a().c(actionEvent.getActionCommand());
    }
}
