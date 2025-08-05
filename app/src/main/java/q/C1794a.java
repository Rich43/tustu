package q;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.UIManager;

/* renamed from: q.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:q/a.class */
public class C1794a implements i {
    @Override // q.i
    public void a(Component component) throws IllegalArgumentException {
        JLabel jLabel = (JLabel) component;
        jLabel.setText("");
        jLabel.setHorizontalAlignment(2);
        jLabel.setEnabled(true);
        jLabel.setForeground(UIManager.getColor("Label.foreground"));
        jLabel.setOpaque(false);
    }

    @Override // q.i
    public void b(Component component) {
    }
}
