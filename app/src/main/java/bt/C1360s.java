package bt;

import G.C0048ah;
import G.C0084bq;
import G.C0085br;
import G.C0113cs;
import G.InterfaceC0109co;
import com.efiAnalytics.ui.C1628dl;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bt.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/s.class */
public class C1360s extends C1628dl implements InterfaceC0109co, InterfaceC1565bc {

    /* renamed from: c, reason: collision with root package name */
    private HashMap f9117c;

    /* renamed from: a, reason: collision with root package name */
    C1362u f9118a;

    /* renamed from: d, reason: collision with root package name */
    private boolean f9119d;

    public C1360s() {
        this.f9117c = new HashMap();
        this.f9118a = null;
        this.f9119d = false;
        this.f9118a = new C1362u(this);
        addMouseListener(new C1363v(this));
    }

    public C1360s(G.R r2, C0084bq c0084bq) {
        this.f9117c = new HashMap();
        this.f9118a = null;
        this.f9119d = false;
        if (c0084bq != null && c0084bq.b() > 0) {
            C0085br[] c0085brArrA = c0084bq.a();
            for (int i2 = 0; i2 < c0085brArrA.length; i2++) {
                try {
                    a(r2.c(), c0085brArrA[i2].a(), c0085brArrA[i2].e(), c0085brArrA[i2].d() == null ? null : new Color(c0085brArrA[i2].d().a()), c0085brArrA[i2].b(), c0085brArrA[i2].c(), c0085brArrA[i2].f(), c0085brArrA[i2].g());
                } catch (V.a e2) {
                    bH.C.a("Can not add line to Graph.", e2, this);
                }
            }
        }
        addMouseListener(new C1363v(this));
    }

    public void a(String str, String str2, String str3, Color color, double d2, double d3, boolean z2, boolean z3) throws V.a {
        G.R rC = G.T.a().c(str);
        if (rC == null) {
            throw new V.a("Failed to add OutputChannel " + str2 + ",\n" + str + " not currently loaded.");
        }
        G.aH aHVarG = rC.g(str2);
        if (aHVarG == null) {
            throw new V.a("Failed to add OutputChannel " + str2 + ",\n" + ((Object) aHVarG) + " not found in " + str);
        }
        C0048ah c0048ahA = a(str2, rC);
        C1361t c1361t = new C1361t(this);
        c1361t.a(str2);
        c1361t.b(str2);
        if (color == null) {
            color = a(this.f9117c.size());
        }
        c1361t.a(color);
        if (str3 == null || str3.equals("")) {
            str3 = aHVarG.e();
        }
        if (d2 != d3) {
            c1361t.b(d2);
            c1361t.c(d3);
            c1361t.d(aHVarG.d());
            c1361t.a(z3);
            c1361t.b(z2);
            c1361t.c(str3);
            if (c0048ahA != null) {
                c1361t.d(c0048ahA.l());
            }
        } else if (c0048ahA != null) {
            c1361t.b(c0048ahA.a());
            c1361t.c(c0048ahA.d());
            c1361t.d(c0048ahA.l());
            c1361t.a(z3);
            c1361t.b(z2);
            if (c0048ahA.j() == null || c0048ahA.j().equals("")) {
                c1361t.c(str3);
            } else {
                try {
                    c1361t.c(c0048ahA.j().a());
                } catch (V.g e2) {
                    Logger.getLogger(C1360s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    c1361t.c("");
                }
            }
        } else {
            c1361t.b(aHVarG.n());
            c1361t.c(aHVarG.m());
            c1361t.d(aHVarG.d());
            c1361t.c(str3);
            c1361t.a(true);
            c1361t.b(true);
        }
        this.f9117c.put(str2, c1361t);
        a(c1361t);
        C0113cs.a().a(str, str2, this);
    }

    public void a() {
        C0113cs.a().a(this);
    }

    private Color a(int i2) {
        switch (i2 % 4) {
            case 0:
                return Color.CYAN;
            case 1:
                return Color.RED;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.GREEN;
            default:
                return Color.MAGENTA;
        }
    }

    private C0048ah a(String str, G.R r2) {
        Iterator itB = r2.B();
        while (itB.hasNext()) {
            C0048ah c0048ah = (C0048ah) itB.next();
            if (c0048ah.i().equals(str)) {
                return c0048ah;
            }
        }
        return null;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        C1361t c1361t = (C1361t) this.f9117c.get(str);
        if (c1361t == null) {
            bH.C.c("LiveGraph::Registered as OuputChannelListener, but I don't have the channel " + str);
        } else {
            if (c()) {
                return;
            }
            c1361t.a(d2);
            b();
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        a();
    }

    public void b() {
        if (this.f9118a == null) {
            this.f9118a = new C1362u(this);
        }
        this.f9118a.a();
    }

    @Override // com.efiAnalytics.ui.C1628dl, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(0, 0);
    }

    public boolean c() {
        return this.f9119d;
    }

    public void a(boolean z2) {
        this.f9119d = z2;
    }
}
