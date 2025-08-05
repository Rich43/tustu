package bb;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:bb/t.class */
class t extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1051p f7817a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ s f7818b;

    t(s sVar, C1051p c1051p) {
        this.f7818b = sVar;
        this.f7817a = c1051p;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        this.f7818b.a();
    }
}
