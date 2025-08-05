package bq;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bq.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bq/c.class */
class C1221c implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1220b f8333a;

    C1221c(C1220b c1220b) {
        this.f8333a = c1220b;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8333a.b(!this.f8333a.f8329h.isVisible());
    }
}
