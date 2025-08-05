package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:ao/hL.class */
public class hL extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hI f6041a;

    public hL(hI hIVar) {
        this.f6041a = hIVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f6041a.a(mouseEvent.getX(), mouseEvent.getY());
            mouseEvent.consume();
        }
    }
}
