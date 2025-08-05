package aP;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/* renamed from: aP.io, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/io.class */
class C0433io extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    Component f3740a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0431im f3741b;

    public C0433io(C0431im c0431im, Component component) {
        this.f3741b = c0431im;
        this.f3740a = null;
        this.f3740a = component;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        this.f3740a.setCursor(Cursor.getPredefinedCursor(12));
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        this.f3740a.setCursor(Cursor.getDefaultCursor());
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        String strB = this.f3741b.f3736a[this.f3741b.f3738c].f3744b;
        if (strB.startsWith("[localfile]")) {
            strB = bH.W.b(strB, "[localfile]", "file:///" + new File(".").getAbsolutePath());
        }
        com.efiAnalytics.ui.aN.a(strB);
    }
}
