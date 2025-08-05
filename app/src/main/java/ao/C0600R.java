package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JMenuItem;

/* renamed from: ao.R, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/R.class */
final class C0600R implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ List f5108a;

    C0600R(List list) {
        this.f5108a = list;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C0597O.a(this.f5108a, ((JMenuItem) actionEvent.getSource()).getName());
    }
}
