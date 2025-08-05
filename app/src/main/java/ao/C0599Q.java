package ao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* renamed from: ao.Q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/Q.class */
final class C0599Q implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Component f5107a;

    C0599Q(Component component) {
        this.f5107a = component;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String name = ((JMenuItem) actionEvent.getSource()).getName();
        if (com.efiAnalytics.ui.bV.a("Are you sure you want to delete the field group: " + name, this.f5107a, true)) {
            h.i.d("GRAPH_FIELD_GROUP_NAME_" + name);
        }
    }
}
