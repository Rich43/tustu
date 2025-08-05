package ao;

import W.C0184j;
import com.efiAnalytics.ui.C1580br;
import h.C1737b;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/* renamed from: ao.af, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/af.class */
public class C0615af extends JButton {

    /* renamed from: b, reason: collision with root package name */
    private String f5171b = null;

    /* renamed from: a, reason: collision with root package name */
    int f5172a = 14;

    /* renamed from: c, reason: collision with root package name */
    private static Image f5173c = null;

    /* renamed from: d, reason: collision with root package name */
    private static Image f5174d = null;

    public C0615af() {
        f5173c = b();
        f5174d = c();
        if (f5173c != null) {
            super.setIcon(new ImageIcon(b()));
        } else {
            super.setText("...");
        }
        if (f5174d != null) {
            super.setDisabledIcon(new ImageIcon(c()));
        }
        super.addActionListener(new C0616ag(this));
        Dimension dimensionA = com.efiAnalytics.ui.eJ.a(((this.f5172a * 2) / 3) + 8, this.f5172a + 6);
        super.setPreferredSize(dimensionA);
        super.setMinimumSize(dimensionA);
        setEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        if (C1737b.a().a("searchLogFiles")) {
            C0184j c0184jA = C0804hg.a().r().a(this.f5171b);
            C1580br c1580br = new C1580br();
            C0601S.a(c1580br, c0184jA, this);
            add(c1580br);
            c1580br.show(this, 0, getHeight());
        }
    }

    void a(String str) {
        this.f5171b = str;
        setEnabled((str == null || str.trim().isEmpty()) ? false : true);
    }

    private Image b() {
        if (f5173c == null) {
            try {
                f5173c = com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11109y, this, this.f5172a);
            } catch (V.a e2) {
                e2.printStackTrace();
            }
        }
        return f5173c;
    }

    private Image c() {
        if (f5174d == null) {
            f5174d = GrayFilter.createDisabledImage(b());
        }
        return f5174d;
    }
}
