package bt;

import com.efiAnalytics.ui.Cdo;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;

/* renamed from: bt.A, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/A.class */
public class C1273A extends Cdo {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f8652a;

    /* renamed from: b, reason: collision with root package name */
    private int f8653b;

    public C1273A() {
        this("");
    }

    public C1273A(String str) {
        super(str);
        this.f8652a = new ArrayList();
        this.f8653b = 60;
        addFocusListener(new C1274B(this));
        addKeyListener(new C1275C(this));
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    @Override // javax.swing.JTextField, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize.width < this.f8653b) {
            preferredSize.setSize(this.f8653b, preferredSize.height);
        }
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        if (minimumSize.width < this.f8653b) {
            minimumSize.setSize(this.f8653b, minimumSize.height);
        }
        return minimumSize;
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        if (z2) {
            setBorder(BorderFactory.createLoweredBevelBorder());
        } else {
            setBorder(BorderFactory.createEmptyBorder());
        }
    }

    public double a() {
        return f() ? Integer.parseInt(getText(), 16) : Double.parseDouble(getText());
    }

    public void a(bX bXVar) {
        this.f8652a.add(bXVar);
    }

    public void b(bX bXVar) {
        this.f8652a.remove(bXVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        Iterator it = this.f8652a.iterator();
        while (it.hasNext()) {
            ((bX) it.next()).b(getText());
        }
    }

    public void a(int i2) {
        this.f8653b = i2;
    }
}
