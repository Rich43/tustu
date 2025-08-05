package q;

import bt.bI;
import java.awt.Color;
import java.awt.Component;
import javax.swing.UIManager;

/* loaded from: TunerStudioMS.jar:q/e.class */
public class e implements i {
    @Override // q.i
    public void a(Component component) {
        bI bIVar = (bI) component;
        bIVar.setColumns(20);
        bIVar.setText("");
        bIVar.setEnabled(true);
        bIVar.setEditable(true);
        bIVar.setBackground(Color.white);
    }

    @Override // q.i
    public void b(Component component) {
        bI bIVar = (bI) component;
        bIVar.setColumns(20);
        bIVar.setText("");
        bIVar.setEnabled(true);
        bIVar.setEditable(true);
        Color color = UIManager.getColor("TextField.background");
        if (color != null) {
            bIVar.setBackground(color);
        } else {
            bIVar.setBackground(Color.white);
        }
        Color color2 = UIManager.getColor("TextField.foreground");
        if (color2 != null) {
            bIVar.setForeground(color2);
        } else {
            bIVar.setForeground(Color.BLACK);
        }
    }
}
