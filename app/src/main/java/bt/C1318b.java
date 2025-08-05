package bt;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: bt.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/b.class */
class C1318b extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1291a f8901a;

    C1318b(C1291a c1291a) {
        this.f8901a = c1291a;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (this.f8901a.isEnabled()) {
            this.f8901a.a();
        }
    }
}
