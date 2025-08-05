package q;

import bt.C1366y;
import com.efiAnalytics.ui.C1685fp;
import java.awt.Color;
import java.awt.Component;
import javax.swing.UIManager;

/* renamed from: q.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:q/b.class */
public class C1795b implements i {
    @Override // q.i
    public void a(Component component) {
        C1366y c1366y = (C1366y) component;
        c1366y.removeAllItems();
        c1366y.b();
        C1685fp.a((Component) c1366y, true);
    }

    @Override // q.i
    public void b(Component component) {
        C1366y c1366y = (C1366y) component;
        c1366y.removeAllItems();
        c1366y.b();
        C1685fp.a((Component) c1366y, true);
        Color color = UIManager.getColor("ComboBox.background");
        if (color != null) {
            c1366y.setBackground(color);
        }
        Color color2 = UIManager.getColor("ComboBox.foreground");
        if (color2 != null) {
            c1366y.setForeground(color2);
        }
    }
}
