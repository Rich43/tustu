package ao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/* renamed from: ao.cl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cl.class */
class C0675cl implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JMenu f5494a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f5495b;

    C0675cl(bP bPVar, JMenu jMenu) {
        this.f5495b = bPVar;
        this.f5494a = jMenu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem jMenuItem = (JMenuItem) actionEvent.getSource();
        if (com.efiAnalytics.ui.bV.a("Are you sure you want to delete the Settings Profile: " + jMenuItem.getText(), (Component) jMenuItem, true) && at.c.a().b(jMenuItem.getText())) {
            this.f5494a.remove(jMenuItem);
        }
    }
}
