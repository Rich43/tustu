package aO;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:aO/m.class */
class m implements MouseWheelListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2705a;

    m(k kVar) {
        this.f2705a = kVar;
    }

    @Override // java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if (mouseWheelEvent.getWheelRotation() > 0) {
            if (mouseWheelEvent.getSource() instanceof JComponent) {
                this.f2705a.b(mouseWheelEvent.getX() / ((JComponent) mouseWheelEvent.getSource()).getWidth());
                return;
            }
            return;
        }
        if (mouseWheelEvent.getSource() instanceof JComponent) {
            this.f2705a.a(mouseWheelEvent.getX() / ((JComponent) mouseWheelEvent.getSource()).getWidth());
        }
    }
}
