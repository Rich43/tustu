package aP;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.ix, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ix.class */
class C0442ix implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0440iv f3762a;

    C0442ix(C0440iv c0440iv) {
        this.f3762a = c0440iv;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3762a.setCursor(Cursor.getPredefinedCursor(3));
        new C0443iy(this).start();
    }
}
