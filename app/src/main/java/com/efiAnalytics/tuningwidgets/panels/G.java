package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.C1603cn;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.bV;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import r.C1807j;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/G.class */
public class G extends aL {

    /* renamed from: a, reason: collision with root package name */
    JButton f10252a = new JButton(C1818g.b("Browse for inc"));

    /* renamed from: b, reason: collision with root package name */
    C1603cn f10253b = new C1603cn();

    /* renamed from: c, reason: collision with root package name */
    static String f10254c = " table(adcValue , \"fileName\") ";

    public G(String str) {
        if (str != null && !str.isEmpty()) {
            setBorder(BorderFactory.createTitledBorder(str));
        }
        setLayout(new GridLayout(0, 1));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(this.f10252a);
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout());
        this.f10253b.setHorizontalAlignment(0);
        jPanel2.add(this.f10253b);
        add(jPanel2);
        this.f10252a.addActionListener(new H(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() throws IllegalArgumentException {
        this.f10253b.setText(bV.b(this, C1818g.b("Select Custom inc File"), new String[]{"inc"}, "", C1807j.u()));
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.aL
    public String a() {
        return "//--    Generated from user inc file \n//--    Useing user File:\n//-- " + this.f10253b.getText();
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.aL
    public double[] a(int i2) throws V.a {
        if (!b(i2)) {
            throw new V.a(C1818g.b("Can not generate table, Invalid file."));
        }
        double[] dArr = new double[i2];
        File file = new File(this.f10253b.a());
        try {
            bH.E eB = bH.E.b(file.getParentFile().getAbsolutePath(), file.getName());
            for (int i3 = 0; i3 < i2; i3++) {
                if (eB.b() != i2) {
                    dArr[i3] = eB.a((5.0f * i3) / 1024.0f);
                } else {
                    dArr[i3] = eB.a(i3);
                }
            }
            return dArr;
        } catch (IOException e2) {
            throw new V.a("Unable to load file " + file.getName() + "\nMake sure this is a valid " + i2 + " point inc file.");
        }
    }

    private boolean b(int i2) throws V.a {
        File file = new File(this.f10253b.a());
        try {
            bH.E.c();
            bH.E.b(file.getParentFile().getAbsolutePath(), file.getName());
            return true;
        } catch (IOException e2) {
            throw new V.a("Unable to load file " + file.getName() + "\nMake sure this is a valid " + i2 + " point inc file.");
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        this.f10252a.setEnabled(z2);
        this.f10253b.setEnabled(z2);
        super.setEnabled(z2);
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.aL
    public void a(InterfaceC1662et interfaceC1662et) {
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.aL
    public void b(InterfaceC1662et interfaceC1662et) {
    }

    public File b() {
        if (this.f10253b.a() == null || this.f10253b.a().isEmpty()) {
            return null;
        }
        return new File(this.f10253b.a());
    }

    public void a(File file) throws IllegalArgumentException {
        this.f10253b.setText(file.getAbsolutePath());
    }
}
