package com.efiAnalytics.tuningwidgets.portEditor;

import G.R;
import G.T;
import G.aM;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/q.class */
public class q extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JComboBox f10585a;

    /* renamed from: b, reason: collision with root package name */
    R f10586b;

    /* renamed from: d, reason: collision with root package name */
    aM f10588d;

    /* renamed from: c, reason: collision with root package name */
    aE.a f10587c = aE.a.A();

    /* renamed from: f, reason: collision with root package name */
    private int f10589f = -1;

    /* renamed from: g, reason: collision with root package name */
    private int f10590g = -1;

    /* renamed from: e, reason: collision with root package name */
    ArrayList f10591e = new ArrayList();

    public q(R r2, String str) {
        this.f10585a = null;
        this.f10586b = null;
        this.f10588d = null;
        this.f10586b = r2;
        if (str != null && !str.equals("")) {
            this.f10588d = r2.c(str);
        }
        this.f10585a = new JComboBox();
        setLayout(new GridLayout());
        if (str != null && r2.c(str) != null && C1806i.a().a("HF-0FD-0HHFJG")) {
            add(this.f10585a);
            this.f10585a.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        }
        this.f10585a.addActionListener(new r(this));
        if (this.f10588d == null) {
            bH.C.d("No [Constant] for outputCanId defined in [PortEditor], Controller Selector disabled.");
            this.f10585a.addItem(r2.c());
            return;
        }
        for (String str2 : T.a().d()) {
            this.f10585a.addItem(str2);
        }
    }

    private double[][] a(double[][] dArr) {
        for (int i2 = 0; i2 < dArr.length; i2++) {
            for (int i3 = 0; i3 < dArr[0].length; i3++) {
                if (dArr[i2][i3] < 0.0d) {
                    dArr[i2][i3] = 0.0d;
                }
            }
        }
        return dArr;
    }

    protected void a(String str) {
        if (this.f10588d == null || str == null || str.length() <= 0) {
            return;
        }
        try {
            double[][] dArrA = a(this.f10588d.i(this.f10586b.h()));
            R rC = T.a().c(str);
            if (rC == null) {
                bH.C.c("onSelect no EcuConfig found for " + str + " using a CAN ID of 0");
                dArrA[b()][a()] = 0.0d;
            } else if (b() >= 0 && a() >= 0) {
                dArrA[b()][a()] = rC.O().x();
            }
            this.f10588d.a(this.f10586b.h(), dArrA);
            this.f10585a.setSelectedItem(str);
            a(rC);
        } catch (V.g e2) {
            bH.C.a("Error updating:" + this.f10588d.aJ() + " CAN ID to that of " + str);
            Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (Exception e3) {
            bH.C.a("Error updating:" + this.f10588d.aJ() + " CAN ID to that of " + str);
            Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    private void d() {
        if (this.f10588d != null) {
            try {
                int i2 = (int) this.f10588d.i(this.f10586b.h())[b()][a()];
                for (String str : T.a().d()) {
                    R rC = T.a().c(str);
                    if (rC.O().x() == i2) {
                        a(rC.c());
                        return;
                    }
                }
            } catch (V.g e2) {
                e2.printStackTrace();
            }
        }
    }

    private void a(R r2) {
        Iterator it = this.f10591e.iterator();
        while (it.hasNext()) {
            ((F) it.next()).a(r2);
        }
    }

    public void a(F f2) {
        this.f10591e.add(f2);
    }

    public int a() {
        return this.f10589f;
    }

    public void a(int i2) {
        this.f10589f = i2;
        d();
    }

    public int b() {
        return this.f10590g;
    }

    public void b(int i2) {
        this.f10590g = i2;
    }

    public R c() {
        if (this.f10588d != null && b() >= 0 && a() >= 0) {
            try {
                int i2 = (int) this.f10588d.i(this.f10586b.h())[b()][a()];
                for (String str : T.a().d()) {
                    R rC = T.a().c(str);
                    if (rC.O().x() == i2) {
                        return T.a().c(rC.c());
                    }
                }
            } catch (V.g e2) {
                e2.printStackTrace();
            }
        }
        return T.a().c((String) this.f10585a.getSelectedItem());
    }
}
