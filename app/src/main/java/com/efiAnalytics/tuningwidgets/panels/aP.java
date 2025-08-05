package com.efiAnalytics.tuningwidgets.panels;

import G.C0072be;
import G.C0113cs;
import G.InterfaceC0109co;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.C1705w;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.fE;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aP.class */
public class aP extends JPanel implements InterfaceC0109co, InterfaceC1565bc, TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    G.R f10365a;

    /* renamed from: b, reason: collision with root package name */
    G.aM f10366b;

    /* renamed from: c, reason: collision with root package name */
    G.aM f10367c;

    /* renamed from: d, reason: collision with root package name */
    fE f10368d;

    /* renamed from: e, reason: collision with root package name */
    double f10369e = Double.NaN;

    /* renamed from: f, reason: collision with root package name */
    C1705w f10370f;

    /* renamed from: g, reason: collision with root package name */
    JLabel f10371g;

    /* renamed from: h, reason: collision with root package name */
    String f10372h;

    /* renamed from: i, reason: collision with root package name */
    String f10373i;

    /* renamed from: j, reason: collision with root package name */
    C1701s f10374j;

    public aP(G.R r2, C1701s c1701s, String str) throws IllegalArgumentException {
        String strV;
        this.f10365a = null;
        this.f10366b = null;
        this.f10367c = null;
        this.f10368d = null;
        this.f10370f = null;
        this.f10371g = null;
        this.f10372h = null;
        this.f10373i = null;
        this.f10374j = null;
        this.f10365a = r2;
        this.f10374j = c1701s;
        setLayout(new BorderLayout());
        this.f10370f = new C1705w();
        this.f10370f.setName(c1701s.z());
        C0072be c0072be = null;
        if (r2 != null && str != null && !str.equals("")) {
            c0072be = (C0072be) r2.e().c(str);
            this.f10366b = r2.c(c0072be.b());
            this.f10367c = r2.c(c0072be.a());
            this.f10372h = c0072be.f();
            this.f10373i = c0072be.d();
        }
        c1701s.addTableModelListener(this);
        this.f10370f.h().a(C1677fh.a(c1701s));
        if (this.f10366b != null) {
            this.f10370f.b(this.f10366b.u());
        }
        this.f10370f.a(c1701s);
        this.f10370f.c(eJ.a());
        this.f10370f.a(false);
        setBorder(BorderFactory.createTitledBorder(""));
        add(BorderLayout.CENTER, this.f10370f);
        this.f10371g = new JLabel();
        this.f10371g.setHorizontalAlignment(0);
        add("South", this.f10371g);
        this.f10368d = new fE();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, this.f10368d);
        add("West", jPanel);
        if (c1701s.w() != null) {
            this.f10371g.setText(this.f10373i);
        }
        if (c1701s.v() != null) {
            if (this.f10366b != null) {
                strV = this.f10372h;
                if (this.f10366b.o() != null && !this.f10366b.o().equals("")) {
                    strV = strV + " " + this.f10366b.o();
                }
            } else {
                strV = c1701s.v() != null ? c1701s.v() : "";
            }
            this.f10368d.setText(strV);
        }
        if (r2 != null) {
            try {
                C0113cs.a().a(r2.c(), c0072be.f(), this);
                C0113cs.a().a(r2.c(), c0072be.d(), this);
            } catch (V.a e2) {
                bH.C.a("Unable to subscribe x or y axis for hightlights.", e2, this);
            }
        }
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f10374j.removeTableModelListener(this);
        C0113cs.a().a(this);
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) throws NumberFormatException {
        if (str.equals(this.f10372h)) {
            this.f10369e = d2;
        } else {
            if (!str.equals(this.f10373i) || this.f10369e == Double.NaN) {
                return;
            }
            this.f10370f.h().a(this.f10369e + "", d2 + "");
        }
    }
}
