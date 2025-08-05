package n;

import com.efiAnalytics.ui.aO;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import r.C1806i;
import s.C1818g;

/* renamed from: n.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:n/b.class */
public class C1762b extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f12929a;

    /* renamed from: b, reason: collision with root package name */
    JButton f12930b;

    /* renamed from: c, reason: collision with root package name */
    JButton f12931c;

    /* renamed from: d, reason: collision with root package name */
    JButton f12932d;

    /* renamed from: e, reason: collision with root package name */
    JButton f12933e;

    /* renamed from: f, reason: collision with root package name */
    ImageIcon f12934f;

    /* renamed from: g, reason: collision with root package name */
    ImageIcon f12935g;

    public C1762b() {
        this(24);
    }

    public C1762b(int i2) {
        this.f12929a = new ArrayList();
        this.f12930b = null;
        this.f12931c = null;
        this.f12932d = null;
        this.f12933e = null;
        this.f12934f = null;
        this.f12935g = null;
        setLayout(new GridLayout(eJ.a(1), eJ.a(3), eJ.a(2), eJ.a(2)));
        try {
            this.f12934f = new ImageIcon(cO.a().a(cO.f11129S, this, i2));
            this.f12935g = new ImageIcon(cO.a().a(cO.f11132V, this, i2));
        } catch (V.a e2) {
            Logger.getLogger(C1762b.class.getName()).log(Level.INFO, "Unable to load burn button image.", (Throwable) e2);
        }
        Image imageA = null;
        Border borderCreateEmptyBorder = BorderFactory.createEmptyBorder(eJ.a(3), eJ.a(4), eJ.a(2), eJ.a(4));
        try {
            imageA = cO.a().a(cO.f11137aa, this, i2);
        } catch (V.a e3) {
            Logger.getLogger(C1762b.class.getName()).log(Level.WARNING, "Failed to get Undo Image", (Throwable) e3);
        }
        this.f12930b = new JButton(null, new ImageIcon(imageA));
        this.f12930b.setMnemonic(90);
        this.f12930b.setToolTipText(C1818g.b("Undo Changes (Alt+Z)"));
        this.f12930b.addActionListener(new C1763c(this));
        this.f12930b.setEnabled(false);
        this.f12930b.setBorder(borderCreateEmptyBorder);
        this.f12930b.setToolTipText(C1818g.b("Undo last change made to configuration"));
        add(this.f12930b);
        try {
            imageA = cO.a().a(cO.f11138ab, this, i2);
        } catch (V.a e4) {
            Logger.getLogger(C1762b.class.getName()).log(Level.WARNING, "Failed to get Redo Image", (Throwable) e4);
        }
        this.f12931c = new JButton(null, new ImageIcon(imageA));
        this.f12931c.setMnemonic(89);
        this.f12931c.setToolTipText(C1818g.b("Redo Changes (Alt+Y)"));
        this.f12931c.addActionListener(new C1764d(this));
        this.f12931c.setEnabled(false);
        this.f12931c.setBorder(borderCreateEmptyBorder);
        this.f12931c.setToolTipText(C1818g.b("Redo last Undo made to configuration"));
        add(this.f12931c);
        this.f12933e = new JButton(C1818g.b("Burn"));
        this.f12933e.setMnemonic(66);
        this.f12933e.addActionListener(new e(this));
        if (!C1806i.a().a("OIJFDSFDSAPOFS")) {
            add(this.f12933e);
        }
        this.f12933e.setBorder(borderCreateEmptyBorder);
        this.f12932d = new JButton(C1818g.b("Close"));
        this.f12932d.setMnemonic(67);
        this.f12932d.addActionListener(new f(this));
        this.f12932d.setBorder(borderCreateEmptyBorder);
        add(this.f12932d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        Iterator it = this.f12929a.iterator();
        while (it.hasNext()) {
            ((aO) it.next()).d();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        Iterator it = this.f12929a.iterator();
        while (it.hasNext()) {
            ((aO) it.next()).e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        Iterator it = this.f12929a.iterator();
        while (it.hasNext()) {
            ((aO) it.next()).f();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        Iterator it = this.f12929a.iterator();
        while (it.hasNext()) {
            ((aO) it.next()).i();
        }
    }

    public void a(aO aOVar) {
        this.f12929a.add(aOVar);
    }

    public void a(boolean z2) {
        this.f12930b.setEnabled(z2);
    }

    public void b(boolean z2) {
        this.f12931c.setEnabled(z2);
    }

    public void c(boolean z2) {
        if (z2 && this.f12934f != null) {
            this.f12933e.setIcon(this.f12934f);
        } else if (z2 || this.f12935g == null) {
            this.f12933e.setEnabled(z2);
        } else {
            this.f12933e.setIcon(this.f12935g);
        }
    }

    public void d(boolean z2) {
        if (z2) {
            add(this.f12932d);
        } else {
            remove(this.f12932d);
        }
    }
}
