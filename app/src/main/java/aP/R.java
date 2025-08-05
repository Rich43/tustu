package aP;

import bq.C1220b;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/* loaded from: TunerStudioMS.jar:aP/R.class */
public class R extends MouseAdapter implements PopupMenuListener {

    /* renamed from: a, reason: collision with root package name */
    boolean f2772a = false;

    public void a(C1220b c1220b) {
        c1220b.addMouseListener(this);
        c1220b.a(this);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        C1220b c1220b = (C1220b) mouseEvent.getSource();
        if (c1220b.isEnabled() && this.f2772a) {
            c1220b.b(true);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override // javax.swing.event.PopupMenuListener
    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
        this.f2772a = true;
    }

    @Override // javax.swing.event.PopupMenuListener
    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
        this.f2772a = false;
    }

    @Override // javax.swing.event.PopupMenuListener
    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
        this.f2772a = false;
    }
}
