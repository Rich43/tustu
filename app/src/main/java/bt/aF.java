package bt;

import com.efiAnalytics.ui.eJ;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:bt/aF.class */
class aF implements LayoutManager {

    /* renamed from: d, reason: collision with root package name */
    private JComponent f8748d;

    /* renamed from: a, reason: collision with root package name */
    JButton f8749a;

    /* renamed from: b, reason: collision with root package name */
    JButton f8750b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1303al f8751c;

    aF(C1303al c1303al, JComponent jComponent, JButton jButton, JButton jButton2) {
        this.f8751c = c1303al;
        this.f8748d = null;
        this.f8749a = null;
        this.f8750b = null;
        this.f8748d = jComponent;
        this.f8749a = jButton;
        this.f8750b = jButton2;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return container.getPreferredSize();
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return container.getMinimumSize();
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        int iA = eJ.a(6);
        if (this.f8749a != null) {
            Dimension preferredSize = this.f8749a.getPreferredSize();
            this.f8749a.setSize(preferredSize);
            this.f8749a.setLocation((container.getWidth() - eJ.a(5)) - preferredSize.width, eJ.a(3));
        }
        if (this.f8750b != null) {
            this.f8750b.setSize(this.f8750b.getPreferredSize());
            this.f8750b.setLocation(eJ.a(5), eJ.a(3));
            iA += this.f8750b.getWidth();
        }
        if (this.f8751c.f8865t != null) {
            this.f8751c.f8865t.setSize(this.f8751c.f8865t.getPreferredSize());
            this.f8751c.f8865t.setLocation(iA, eJ.a(3));
        }
        if (this.f8748d != null) {
            if (this.f8751c.f8876E) {
                Dimension preferredSize2 = this.f8748d.getPreferredSize();
                this.f8748d.setBounds(0, container.getHeight() - preferredSize2.height, container.getWidth(), preferredSize2.height);
                return;
            }
            Dimension preferredSize3 = this.f8748d.getPreferredSize();
            int y2 = this.f8749a.getY() + this.f8749a.getHeight();
            int height = preferredSize3.height < container.getHeight() - y2 ? preferredSize3.height : container.getHeight() - y2;
            int width = preferredSize3.width < container.getWidth() ? preferredSize3.width : container.getWidth();
            this.f8748d.setBounds((container.getWidth() - width) - eJ.a(5), eJ.a(20), width, height);
        }
    }
}
