package q;

import bH.C;
import bt.aZ;
import java.awt.Color;
import java.awt.Component;

/* renamed from: q.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:q/d.class */
public class C1797d implements i {
    @Override // q.i
    public void a(Component component) {
        aZ aZVar = (aZ) component;
        aZVar.setText("");
        aZVar.setEnabled(true);
        aZVar.setForeground(Color.BLACK);
        aZVar.setOpaque(false);
    }

    @Override // q.i
    public void b(Component component) {
        aZ aZVar = (aZ) component;
        aZVar.setEnabled(true);
        if (aZVar.getText() == null || aZVar.getText().length() <= 0) {
            return;
        }
        C.c("Check out: " + aZVar.getText());
    }
}
