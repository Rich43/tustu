package bp;

import G.C0048ah;
import G.C0083bp;
import G.R;
import G.aM;
import G.aN;
import G.aR;
import V.g;
import bH.C;
import bt.C1345d;
import bt.aT;
import bt.bG;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.renderers.BasicReadoutGaugePainter;
import com.efiAnalytics.ui.C1605cp;
import com.efiAnalytics.ui.aO;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.slf4j.Marker;
import s.C1818g;
import sun.util.locale.LanguageTag;

/* renamed from: bp.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bp/a.class */
public class C1217a extends C1345d implements aN, aO {

    /* renamed from: a, reason: collision with root package name */
    aT f8319a;

    /* renamed from: b, reason: collision with root package name */
    aM f8320b;

    /* renamed from: c, reason: collision with root package name */
    R f8321c;

    public C1217a(R r2) {
        this.f8319a = null;
        this.f8320b = null;
        this.f8321c = null;
        this.f8321c = r2;
        this.f8320b = r2.c("triggerOffset");
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        setLayout(new BorderLayout());
        C0048ah c0048ahA = new C1388aa().a(r2, "advance");
        c0048ahA.c(C1818g.b("Advance"));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        bG bGVar = new bG();
        bGVar.c().setBackColor(Color.BLACK);
        bGVar.c().setFontColor(Color.WHITE);
        bGVar.c().setFontSizeAdjustment(-4);
        bGVar.b(c0048ahA.aJ());
        bGVar.a(new BasicReadoutGaugePainter());
        jPanel.add(bGVar);
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1));
        jPanel2.add(new JLabel(C1818g.b("Match above value with timing light reading."), 0));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        C0083bp c0083bp = new C0083bp();
        c0083bp.v("offsetField");
        c0083bp.e(C1818g.b("Ignition Offset angle"));
        c0083bp.a("triggerOffset");
        this.f8319a = new aT(r2, c0083bp);
        this.f8319a.setFont(new Font("Helv", 1, 20));
        try {
            aR.a().a(r2.c(), c0083bp.b(), this.f8319a);
        } catch (V.a e2) {
            e2.printStackTrace();
            C.a("Error subscribing to ParameterValue Changes. Parameter:" + c0083bp.b(), e2, this);
        }
        jPanel3.add(this.f8319a);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(1, 0, 3, 3));
        C1605cp c1605cp = new C1605cp(LanguageTag.SEP);
        c1605cp.setFont(new Font("Helv", 1, 30));
        c1605cp.b(new Dimension(40, 40));
        c1605cp.setBackground(Color.RED);
        c1605cp.a(new C1218b(this));
        jPanel4.add(c1605cp);
        C1605cp c1605cp2 = new C1605cp(Marker.ANY_NON_NULL_MARKER);
        c1605cp2.setFont(new Font("Helv", 1, 30));
        c1605cp2.setBackground(Color.GREEN);
        c1605cp2.a(new c(this));
        jPanel4.add(c1605cp2);
        jPanel3.add(jPanel4);
        jPanel2.add(jPanel3);
        jPanel2.add(new JLabel(C1818g.b("Press Up/Down arrows to adjust. Hold Shift to adjust by 5x"), 0));
        add("South", jPanel2);
        this.f8319a.requestFocus();
    }

    public void a() {
        try {
            this.f8320b.a(this.f8321c.h(), this.f8320b.j(this.f8321c.h()) + (5.0d * this.f8320b.A()));
        } catch (Exception e2) {
            bV.d("Unable to increment offset.\nThe configuration does not support this operation.", this);
            e2.printStackTrace();
        }
    }

    public void b() {
        try {
            this.f8320b.a(this.f8321c.h(), this.f8320b.j(this.f8321c.h()) - (5.0d * this.f8320b.A()));
        } catch (Exception e2) {
            bV.d("Unable to increment offset.\nThe configuration does not support this operation.", this);
            e2.printStackTrace();
        }
    }

    @Override // com.efiAnalytics.ui.aO
    public void e() {
        try {
            this.f8321c.h().c();
        } catch (g e2) {
            C.a("Failed to undo data change.");
            e2.printStackTrace();
        }
        this.f8319a.requestFocus();
    }

    @Override // com.efiAnalytics.ui.aO
    public void d() {
        try {
            this.f8321c.h().d();
        } catch (g e2) {
            C.a("Failed to redo data change.");
            e2.printStackTrace();
        }
        this.f8319a.requestFocus();
    }

    @Override // com.efiAnalytics.ui.aO
    public void f() {
        this.f8321c.I();
        this.f8319a.requestFocus();
    }

    @Override // com.efiAnalytics.ui.aO
    public void i() {
        f();
        this.f8319a.close();
        aR.a().a(this);
        l();
    }

    @Override // G.aN
    public void a(String str, String str2) {
    }
}
