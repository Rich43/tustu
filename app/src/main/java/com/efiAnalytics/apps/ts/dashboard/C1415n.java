package com.efiAnalytics.apps.ts.dashboard;

import bt.bG;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/n.class */
class C1415n extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    File f9509a;

    /* renamed from: b, reason: collision with root package name */
    int f9510b;

    /* renamed from: c, reason: collision with root package name */
    JLabel f9511c;

    /* renamed from: d, reason: collision with root package name */
    bG f9512d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C1408g f9513e;

    public C1415n(C1408g c1408g, File file) {
        this.f9513e = c1408g;
        this.f9510b = eJ.a(120);
        this.f9511c = new JLabel("", 0);
        this.f9512d = new bG();
        this.f9512d.setPreferredSize(new Dimension(this.f9510b, this.f9510b));
        this.f9512d.setMinimumSize(new Dimension(this.f9510b, this.f9510b));
        this.f9511c.setPreferredSize(new Dimension(this.f9510b, getFont().getSize()));
        this.f9511c.setMinimumSize(new Dimension(this.f9510b, getFont().getSize()));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f9512d);
        add("South", this.f9511c);
        a(file);
    }

    public C1415n(C1408g c1408g, int i2) {
        this.f9513e = c1408g;
        this.f9510b = eJ.a(120);
        this.f9511c = new JLabel("", 0);
        this.f9512d = new bG();
        this.f9510b = i2;
        this.f9512d.setPreferredSize(new Dimension(i2, i2));
        this.f9512d.setMinimumSize(new Dimension(i2, i2));
        this.f9511c.setPreferredSize(new Dimension(i2, getFont().getSize()));
        this.f9511c.setMinimumSize(new Dimension(i2, getFont().getSize()));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f9512d);
        add("South", this.f9511c);
    }

    public void a(File file) {
        this.f9509a = file;
        if (file == null || !file.exists()) {
            this.f9511c.setText("");
            repaint();
            return;
        }
        try {
            this.f9512d.a(C1388aa.a(file));
            this.f9512d.validate();
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this);
        }
        this.f9511c.setText(file.getName());
        repaint();
    }
}
