package bt;

import G.C0048ah;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac;
import com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter;
import com.efiAnalytics.ui.dD;
import com.efiAnalytics.ui.eJ;
import java.awt.Dimension;

/* loaded from: TunerStudioMS.jar:bt/bG.class */
public class bG extends C1324bf implements InterfaceC1390ac {

    /* renamed from: b, reason: collision with root package name */
    private Gauge f8908b = null;

    /* renamed from: a, reason: collision with root package name */
    dD f8909a = new dD(this);

    public bG() {
        setLayout(new bH(this));
        a(C1388aa.b());
        this.f8908b.setSweepAngle(270);
        this.f8908b.setSweepBeginDegree(315);
        this.f8908b.setFaceAngle(360);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac
    public void a(AbstractC1420s abstractC1420s) {
        c().repaint();
    }

    public Gauge c() {
        return this.f8908b;
    }

    public void a(Gauge gauge) {
        this.f8908b = gauge;
        if (getComponentCount() > 0) {
            removeAll();
        }
        add(gauge);
    }

    public void b(String str) {
        a(G.T.a().c(), str);
    }

    public void a(G.R r2, String str) {
        C0048ah c0048ahK = r2.k(str);
        if (c0048ahK == null) {
            return;
        }
        try {
            this.f8908b.setTitle(c0048ahK.k().a());
            this.f8908b.setUnits(c0048ahK.j().a());
        } catch (V.g e2) {
            bH.C.a(e2);
        }
        this.f8908b.setMin(c0048ahK.b());
        this.f8908b.setMax(c0048ahK.e());
        this.f8908b.setLowWarning(c0048ahK.f());
        this.f8908b.setLowCritical(c0048ahK.o());
        this.f8908b.setHighWarning(c0048ahK.g());
        this.f8908b.setHighCritical(c0048ahK.h());
        this.f8908b.setOutputChannel(c0048ahK.i());
        this.f8908b.setValueDigits(Integer.valueOf(c0048ahK.l()));
        this.f8908b.setLabelDigits(c0048ahK.n());
        this.f8908b.setEcuConfigurationName(r2.c());
        try {
            this.f8908b.subscribeToOutput();
        } catch (V.a e3) {
            com.efiAnalytics.ui.bV.d(e3.getMessage(), this);
        }
    }

    public void a(GaugePainter gaugePainter) {
        this.f8908b.setGaugePainter(gaugePainter);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(eJ.a(160), eJ.a(160));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(eJ.a(75), eJ.a(75));
    }
}
