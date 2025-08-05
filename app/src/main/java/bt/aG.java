package bt;

import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:bt/aG.class */
class aG extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f8752a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1303al f8753b;

    aG(C1303al c1303al) {
        this.f8753b = c1303al;
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        com.efiAnalytics.ui.bV.a(this, z2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.awt.Container, bA.f
    public Component add(Component component) {
        if (component instanceof InterfaceC1565bc) {
            this.f8752a.add((InterfaceC1565bc) component);
        }
        return super.add(component);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        super.paintChildren(graphics);
    }

    public void a() {
        Iterator it = this.f8752a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1565bc) it.next()).close();
        }
    }
}
