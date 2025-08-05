package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.AbstractC1600ck;
import com.sun.xml.internal.ws.encoding.MtomCodec;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileFilter;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import jdk.internal.dynalink.CallSiteDescriptor;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/X.class */
public class X extends AbstractC1600ck {

    /* renamed from: d, reason: collision with root package name */
    private String f10316d;

    /* renamed from: a, reason: collision with root package name */
    JTextPane f10315a = new JTextPane();

    /* renamed from: b, reason: collision with root package name */
    JCheckBox f10317b = new JCheckBox(C1818g.b("Include Data Logs"), true);

    /* renamed from: c, reason: collision with root package name */
    JCheckBox f10318c = new JCheckBox(C1818g.b("Include Restore Points"), true);

    public X() {
        this.f10316d = "";
        setBorder(BorderFactory.createTitledBorder(MtomCodec.XOP_LOCALNAME));
        this.f10316d = C1818g.b("Optional Items to include \nin Project Archive") + CallSiteDescriptor.TOKEN_DELIMITER;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        jPanel.add(this.f10317b);
        jPanel.add(this.f10318c);
        setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("North", jPanel);
        add(BorderLayout.CENTER, jPanel2);
        add("North", this.f10315a);
        this.f10315a.setText(this.f10316d);
        this.f10315a.setBackground(getBackground());
        this.f10315a.setBorder(BorderFactory.createEtchedBorder());
    }

    public boolean a() {
        return this.f10317b.isSelected();
    }

    public boolean b() {
        return this.f10318c.isSelected();
    }

    @Override // com.efiAnalytics.ui.AbstractC1600ck
    public void a(File file) {
    }

    @Override // com.efiAnalytics.ui.AbstractC1600ck
    public void b(File file) {
        bH.C.c("Directory Selected: " + file.getAbsolutePath());
    }

    public FileFilter c() {
        return (a() || b()) ? !a() ? new Z(this) : !b() ? new C1484aa(this) : new C1485ab(this) : new Y(this);
    }
}
