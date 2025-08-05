package q;

import bt.C1273A;
import java.awt.Color;
import java.awt.Component;
import javax.swing.UIManager;

/* renamed from: q.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:q/c.class */
public class C1796c implements i {
    @Override // q.i
    public void a(Component component) {
        C1273A c1273a = (C1273A) component;
        c1273a.setText("");
        c1273a.setEnabled(true);
        c1273a.setEditable(true);
        c1273a.setBackground(Color.white);
        c1273a.a(false);
    }

    @Override // q.i
    public void b(Component component) {
        C1273A c1273a = (C1273A) component;
        c1273a.setText("");
        c1273a.setEnabled(true);
        c1273a.setEditable(true);
        Color color = UIManager.getColor("TextField.background");
        if (color != null) {
            c1273a.setBackground(color);
        } else {
            c1273a.setBackground(Color.white);
        }
        Color color2 = UIManager.getColor("TextField.foreground");
        if (color2 != null) {
            c1273a.setForeground(color2);
        } else {
            c1273a.setForeground(Color.BLACK);
        }
        c1273a.a(false);
    }
}
