package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import G.R;
import G.T;
import com.efiAnalytics.apps.ts.tuningViews.C1435h;
import com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent;
import com.efiAnalytics.ui.cO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import n.C1762b;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/BurnButtonTv.class */
public class BurnButtonTv extends TuneViewComponent {

    /* renamed from: f, reason: collision with root package name */
    ImageIcon f9815f;

    /* renamed from: g, reason: collision with root package name */
    ImageIcon f9816g;

    /* renamed from: k, reason: collision with root package name */
    private final List f9811k = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    JButton f9812c = new JButton();

    /* renamed from: d, reason: collision with root package name */
    d f9813d = new d(this);

    /* renamed from: e, reason: collision with root package name */
    e f9814e = null;

    /* renamed from: h, reason: collision with root package name */
    boolean f9817h = false;

    /* renamed from: i, reason: collision with root package name */
    boolean f9818i = false;

    /* renamed from: j, reason: collision with root package name */
    Runnable f9819j = new c(this);

    public BurnButtonTv() {
        this.f9815f = null;
        this.f9816g = null;
        setLayout(new C1435h(this));
        try {
            this.f9815f = new ImageIcon(cO.a().a(cO.f11130T, this, 24));
            this.f9816g = new ImageIcon(cO.a().a(cO.f11133W, this, 24));
        } catch (V.a e2) {
            Logger.getLogger(C1762b.class.getName()).log(Level.INFO, "Unable to load burn button image.", (Throwable) e2);
        }
        this.f9812c.setText(C1818g.b("Burn"));
        add(this.f9812c);
        this.f9812c.addActionListener(new a(this));
        super.addMouseListener(new b(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void burn() {
        for (String str : T.a().d()) {
            T.a().c(str).I();
        }
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void initializeComponents() {
        enableBurn(false);
        for (String str : T.a().d()) {
            R rC = T.a().c(str);
            this.f9811k.add(rC);
            rC.C().a(this.f9813d);
            if (rC.C().C()) {
                enableBurn(true);
            }
        }
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        Iterator it = this.f9811k.iterator();
        while (it.hasNext()) {
            ((R) it.next()).C().b(this.f9813d);
        }
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public boolean isDirty() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void setClean(boolean z2) {
    }

    public void enableBurn(boolean z2) {
        if (z2 && this.f9815f != null) {
            this.f9812c.setIcon(this.f9815f);
        } else if (z2 || this.f9816g == null) {
            this.f9812c.setEnabled(z2);
        } else {
            this.f9812c.setIcon(this.f9816g);
        }
        this.f9817h = z2;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent, java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        if (getWidth() != i4 || getHeight() != i5) {
            if (this.f9814e == null || !this.f9814e.f9853a) {
                this.f9814e = new e(this);
                this.f9814e.start();
            } else {
                this.f9814e.a();
            }
        }
        super.setBounds(i2, i3, i4, i5);
    }
}
