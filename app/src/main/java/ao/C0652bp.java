package ao;

import com.efiAnalytics.ui.C1699q;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

/* renamed from: ao.bp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bp.class */
public class C0652bp extends JPanel {

    /* renamed from: b, reason: collision with root package name */
    fX f5425b;

    /* renamed from: d, reason: collision with root package name */
    public static String f5427d = "Hits";

    /* renamed from: e, reason: collision with root package name */
    public static String f5428e = "Total Weight";

    /* renamed from: f, reason: collision with root package name */
    public static String f5429f = "Z Field";

    /* renamed from: a, reason: collision with root package name */
    C1699q f5424a = new C1699q();

    /* renamed from: c, reason: collision with root package name */
    JPanel f5426c = new JPanel();

    /* renamed from: g, reason: collision with root package name */
    String f5430g = f5427d;

    public C0652bp(fX fXVar) {
        this.f5425b = fXVar;
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f5424a);
        this.f5424a.b(0.95d);
        add("West", this.f5426c);
        this.f5426c.setBackground(Color.BLACK);
        this.f5426c.setOpaque(true);
    }

    public String a() {
        return this.f5430g;
    }

    public void a(String str) {
        this.f5430g = str;
    }

    public void b() {
        this.f5424a.a();
        this.f5424a.d(0.0d);
        this.f5424a.c(0.0d);
        gG gGVarB = this.f5425b.b();
        this.f5424a.d(gGVarB.w());
        if (this.f5430g.equals(f5427d)) {
            this.f5424a.e("Hits");
            String[] strArrB = gGVarB.b();
            for (int i2 = 0; i2 < strArrB.length; i2++) {
                this.f5424a.a(gGVarB.D()[0][i2].j(), i2);
            }
        } else if (this.f5430g.equals(f5428e)) {
            this.f5424a.e("Total Weight");
            String[] strArrB2 = gGVarB.b();
            for (int i3 = 0; i3 < strArrB2.length; i3++) {
                this.f5424a.a(gGVarB.D()[0][i3].k(), i3);
            }
        } else {
            this.f5424a.e(this.f5425b.f5728d.getSelectedItem() + "");
            String[] strArrB3 = gGVarB.b();
            for (int i4 = 0; i4 < strArrB3.length; i4++) {
                this.f5424a.a(gGVarB.D()[0][i4].i().doubleValue(), i4);
            }
        }
        this.f5426c.setPreferredSize(new Dimension(this.f5425b.f5736l.h().i() / 3, 10));
        doLayout();
        this.f5424a.repaint();
    }

    public void c() {
        this.f5424a.b();
    }

    public void b(String str) {
        this.f5424a.a(str);
    }

    public void c(String str) {
        this.f5424a.d(str);
    }
}
