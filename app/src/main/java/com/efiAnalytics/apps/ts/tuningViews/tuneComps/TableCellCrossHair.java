package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import G.C0072be;
import G.C0088bu;
import G.C0113cs;
import G.R;
import G.T;
import G.bL;
import bH.W;
import bt.C1287O;
import bt.bO;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1421t;
import com.efiAnalytics.apps.ts.tuningViews.C1435h;
import com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.aR;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dD;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/TableCellCrossHair.class */
public class TableCellCrossHair extends TuneViewComponent implements InterfaceC1421t {

    /* renamed from: c, reason: collision with root package name */
    aR f9825c = new aR();

    /* renamed from: d, reason: collision with root package name */
    JButton f9826d = new JButton(C1818g.b("Select Table To Track"));

    /* renamed from: e, reason: collision with root package name */
    JPanel f9827e = new JPanel();

    /* renamed from: f, reason: collision with root package name */
    JPanel f9828f = new JPanel();

    /* renamed from: g, reason: collision with root package name */
    JLabel f9829g = new JLabel("");

    /* renamed from: r, reason: collision with root package name */
    private String f9830r = null;

    /* renamed from: h, reason: collision with root package name */
    R f9831h = null;

    /* renamed from: i, reason: collision with root package name */
    C1701s f9832i = null;

    /* renamed from: s, reason: collision with root package name */
    private int f9833s = 1;

    /* renamed from: j, reason: collision with root package name */
    i f9834j = new i(this);

    /* renamed from: k, reason: collision with root package name */
    String f9835k = null;

    /* renamed from: l, reason: collision with root package name */
    String f9836l = null;

    /* renamed from: m, reason: collision with root package name */
    dD f9837m = new dD(this.f9825c);

    /* renamed from: n, reason: collision with root package name */
    int f9838n = -1;

    /* renamed from: o, reason: collision with root package name */
    int f9839o = -1;

    /* renamed from: p, reason: collision with root package name */
    int f9840p = -1;

    /* renamed from: q, reason: collision with root package name */
    boolean f9841q = true;

    public TableCellCrossHair() {
        setLayout(new C1435h(this));
        super.setDoubleBuffered(true);
        this.f9827e.setLayout(new FlowLayout(2));
        this.f9827e.add(this.f9829g);
        this.f9827e.add(this.f9826d);
        this.f9826d.addActionListener(new g(this));
        super.addEditComponent(this.f9826d);
        this.f9826d.setPreferredSize(eJ.a(150, 16));
        this.f9828f.setLayout(new BorderLayout());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSelectPopup(int i2, int i3) {
        C1287O c1287o = new C1287O(getEcuConfiguration(), this.f9833s);
        c1287o.a(new h(this));
        this.f9827e.add(c1287o);
        c1287o.show(this, i2, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSelectedTable(String str) throws V.a, IllegalArgumentException {
        R ecuConfiguration = getEcuConfiguration();
        C0088bu c0088buC = ecuConfiguration.e().c(str);
        C0113cs.a().a(this.f9834j);
        if (c0088buC == null) {
            close();
            this.f9832i = null;
            this.f9825c.a();
            this.f9829g.setText("");
            return;
        }
        this.f9830r = str;
        try {
            this.f9832i = bO.a().a(ecuConfiguration, str);
            if (this.f9832i == null) {
                throw new V.a("Unknown Table: " + str);
            }
            this.f9829g.setText(bL.c(ecuConfiguration, str));
            C0072be c0072be = (C0072be) ecuConfiguration.e().c(str);
            this.f9835k = c0072be.d();
            this.f9836l = c0072be.f();
            C0113cs.a().a(ecuConfiguration.c(), this.f9835k, this.f9834j);
            C0113cs.a().a(ecuConfiguration.c(), this.f9836l, this.f9834j);
            this.f9840p = ecuConfiguration.c(c0072be.c()).u();
            setClean(false);
            double dDoubleValue = this.f9832i.getValueAt(0, 0).doubleValue();
            this.f9825c.a(W.b(dDoubleValue, this.f9840p));
            if (this.f9832i.A() == Double.MAX_VALUE) {
                this.f9832i.C();
            }
            this.f9825c.a(C1677fh.a(dDoubleValue, this.f9832i.A(), this.f9832i.B()));
            this.f9825c.repaint();
        } catch (Exception e2) {
            Logger.getLogger(TableCellCrossHair.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.a("Unable to load table: " + str);
        }
    }

    public void setSelectedTableName(String str) {
        this.f9830r = str;
    }

    public String getSelectedTableName() {
        return this.f9830r;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void initializeComponents() throws IllegalArgumentException {
        add(BorderLayout.CENTER, this.f9828f);
        this.f9828f.add(BorderLayout.CENTER, this.f9825c);
        if (this.f9830r == null || this.f9830r.isEmpty()) {
            return;
        }
        try {
            setSelectedTable(this.f9830r);
            setClean(true);
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this);
        }
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        C0113cs.a().a(this.f9834j);
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public boolean isDirty() {
        return !this.f9841q;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void setClean(boolean z2) {
        this.f9841q = z2;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void enableEditMode(boolean z2) {
        super.enableEditMode(z2);
        this.f9827e.setLayout(new FlowLayout(2));
        if (z2) {
            this.f9828f.add("North", this.f9827e);
            this.f9841q = false;
        } else {
            this.f9828f.remove(this.f9827e);
        }
        doLayout();
        super.validate();
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void setEcuConfigurationName(String str) throws IllegalArgumentException {
        String ecuConfigurationName = getEcuConfigurationName();
        super.setEcuConfigurationName(str);
        if (str == null || str.isEmpty()) {
            this.f9831h = T.a().c();
        } else {
            this.f9831h = T.a().c(str);
        }
        super.setEcuConfigurationName(str);
        if (ecuConfigurationName.equals(str)) {
            return;
        }
        try {
            setSelectedTable(this.f9830r);
        } catch (V.a e2) {
            Logger.getLogger(TableCellCrossHair.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public R getEcuConfiguration() {
        if (this.f9831h == null) {
            String ecuConfigurationName = getEcuConfigurationName();
            if (ecuConfigurationName == null || ecuConfigurationName.isEmpty()) {
                this.f9831h = T.a().c();
            } else {
                this.f9831h = T.a().c(ecuConfigurationName);
            }
        }
        return this.f9831h;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setXValues(double d2) throws NumberFormatException {
        double d3;
        double d4;
        double d5;
        double dB = C1677fh.b(this.f9832i.b(), d2);
        if (dB >= this.f9832i.b().length - 2) {
            double d6 = (Double.parseDouble(this.f9832i.b()[this.f9832i.b().length - 1]) - Double.parseDouble(this.f9832i.b()[this.f9832i.b().length - 2])) / 2.0d;
            d4 = Double.parseDouble(this.f9832i.b()[this.f9832i.b().length - 1]) + d6;
            d5 = Double.parseDouble(this.f9832i.b()[this.f9832i.b().length - 1]) - d6;
            this.f9839o = Math.round((float) dB);
            d3 = Double.parseDouble(this.f9832i.b()[this.f9839o]);
        } else if (dB <= 1.0d) {
            double d7 = (Double.parseDouble(this.f9832i.b()[1]) - Double.parseDouble(this.f9832i.b()[0])) / 2.0d;
            d4 = Double.parseDouble(this.f9832i.b()[0]) + d7;
            d5 = Double.parseDouble(this.f9832i.b()[0]) - d7;
            this.f9839o = Math.round((float) dB);
            d3 = Double.parseDouble(this.f9832i.b()[this.f9839o]);
        } else if (dB == Math.floor(dB)) {
            this.f9839o = (int) Math.floor(dB);
            d3 = Double.parseDouble(this.f9832i.b()[this.f9839o]);
            double d8 = Double.parseDouble(this.f9832i.b()[((int) Math.floor(dB)) - 1]) / 2.0d;
            d4 = d3 + (Double.parseDouble(this.f9832i.b()[((int) Math.floor(dB)) + 1]) / 2.0d);
            d5 = d3 - d8;
        } else {
            int iFloor = dB - Math.floor(dB) > 0.5d ? (int) (Math.floor(dB) + 1.0d) : (int) Math.floor(dB);
            this.f9839o = (int) Math.floor(iFloor);
            d3 = Double.parseDouble(this.f9832i.b()[this.f9839o]);
            d4 = d3 + ((Double.parseDouble(this.f9832i.b()[((int) Math.floor(iFloor)) + 1]) - d3) / 2.0d);
            d5 = d3 - ((d3 - Double.parseDouble(this.f9832i.b()[((int) Math.floor(iFloor)) - 1])) / 2.0d);
        }
        this.f9825c.b(d4);
        this.f9825c.a(d5);
        this.f9825c.g(d3);
        this.f9825c.e(d2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setYValues(double d2) throws NumberFormatException {
        double d3;
        double d4;
        double d5;
        double dA = C1677fh.a(this.f9832i.a(), d2);
        if (dA >= this.f9832i.a().length - 2) {
            double d6 = (Double.parseDouble(this.f9832i.a()[this.f9832i.a().length - 2]) - Double.parseDouble(this.f9832i.a()[this.f9832i.a().length - 1])) / 2.0d;
            d4 = Double.parseDouble(this.f9832i.a()[this.f9832i.a().length - 1]) + d6;
            d5 = Double.parseDouble(this.f9832i.a()[this.f9832i.a().length - 1]) - d6;
            this.f9838n = Math.round((float) dA);
            d3 = Double.parseDouble(this.f9832i.a()[this.f9838n]);
        } else if (dA <= 1.0d) {
            double d7 = (Double.parseDouble(this.f9832i.a()[0]) - Double.parseDouble(this.f9832i.a()[1])) / 2.0d;
            d4 = Double.parseDouble(this.f9832i.a()[0]) + d7;
            d5 = Double.parseDouble(this.f9832i.a()[0]) - d7;
            this.f9838n = Math.round((float) dA);
            d3 = Double.parseDouble(this.f9832i.a()[this.f9838n]);
        } else if (dA == Math.floor(dA)) {
            this.f9838n = (int) Math.floor(dA);
            d3 = Double.parseDouble(this.f9832i.a()[this.f9838n]);
            double d8 = Double.parseDouble(this.f9832i.a()[((int) Math.floor(dA)) + 1]) / 2.0d;
            d4 = d3 + (Double.parseDouble(this.f9832i.a()[((int) Math.floor(dA)) - 1]) / 2.0d);
            d5 = d3 - d8;
        } else {
            int iFloor = dA - Math.floor(dA) > 0.5d ? (int) (Math.floor(dA) + 1.0d) : (int) Math.floor(dA);
            this.f9838n = (int) Math.floor(iFloor);
            d3 = Double.parseDouble(this.f9832i.a()[this.f9838n]);
            double d9 = (d3 - Double.parseDouble(this.f9832i.a()[((int) Math.floor(iFloor)) + 1])) / 2.0d;
            d4 = d3 + ((Double.parseDouble(this.f9832i.a()[((int) Math.floor(iFloor)) - 1]) - d3) / 2.0d);
            d5 = d3 - d9;
        }
        this.f9825c.d(d4);
        this.f9825c.c(d5);
        this.f9825c.h(d3);
        this.f9825c.f(d2);
    }
}
