package bt;

import com.efiAnalytics.ui.InterfaceC1579bq;
import java.awt.event.ActionListener;
import javax.swing.JMenu;

/* renamed from: bt.M, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/M.class */
public class C1285M extends JMenu implements InterfaceC1579bq {

    /* renamed from: a, reason: collision with root package name */
    C1286N f8679a = new C1286N();

    public C1285M(G.R r2, int i2) {
        this.f8679a.a(this, r2, i2);
    }

    @Override // javax.swing.AbstractButton
    public void addActionListener(ActionListener actionListener) {
        this.f8679a.a(actionListener);
    }

    @Override // javax.swing.AbstractButton
    public void removeActionListener(ActionListener actionListener) {
        this.f8679a.b(actionListener);
    }
}
