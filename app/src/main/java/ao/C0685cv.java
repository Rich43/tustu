package ao;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.cv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cv.class */
class C0685cv implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5505a;

    C0685cv(bP bPVar) {
        this.f5505a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        this.f5505a.v();
    }
}
