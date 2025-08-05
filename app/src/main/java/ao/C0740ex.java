package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* renamed from: ao.ex, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ex.class */
class C0740ex implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5693a;

    C0740ex(C0737eu c0737eu) {
        this.f5693a = c0737eu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JComboBox jComboBox = (JComboBox) actionEvent.getSource();
        this.f5693a.f5675k = jComboBox.getSelectedItem().toString();
        h.i.c(h.i.f12309ad, this.f5693a.f5675k);
    }
}
