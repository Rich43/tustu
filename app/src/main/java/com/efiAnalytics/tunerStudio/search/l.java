package com.efiAnalytics.tunerStudio.search;

import G.R;
import G.T;
import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/l.class */
public class l extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    Font f10195a = new Font("Helv", 0, eJ.a(11));

    /* renamed from: b, reason: collision with root package name */
    JTextField f10196b = new JTextField("", 15);

    /* renamed from: c, reason: collision with root package name */
    h f10197c = new h();

    /* renamed from: d, reason: collision with root package name */
    String f10198d = "";

    /* renamed from: e, reason: collision with root package name */
    p f10199e = null;

    public l() {
        setOpaque(false);
        this.f10196b.setFont(this.f10195a);
        this.f10196b.setFocusCycleRoot(false);
        this.f10196b.setFocusTraversalKeysEnabled(false);
        this.f10196b.setFocusTraversalPolicyProvider(false);
        setLayout(new FlowLayout(2));
        add(this.f10196b);
        e();
        this.f10196b.addFocusListener(new m(this));
        this.f10196b.addKeyListener(new n(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public p b() {
        if (this.f10199e == null || !this.f10199e.isAlive()) {
            this.f10199e = new p(this);
            this.f10199e.start();
        }
        return this.f10199e;
    }

    List a() {
        this.f10198d = this.f10196b.getText();
        ArrayList arrayList = new ArrayList();
        if (this.f10198d.length() > 0) {
            for (String str : T.a().d()) {
                R rC = T.a().c(str);
                if (rC != null) {
                    arrayList.addAll(g.a(rC, this.f10196b.getText()));
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        List listA = a();
        if (listA.isEmpty()) {
            this.f10197c.setVisible(false);
            this.f10198d = "";
            return;
        }
        this.f10197c.a();
        this.f10197c.a(listA, "Tune Settings");
        Point locationOnScreen = this.f10196b.getLocationOnScreen();
        int width = locationOnScreen.f12370x + this.f10196b.getWidth();
        this.f10197c.b();
        Dimension size = this.f10197c.getSize();
        this.f10197c.setBounds(width - size.width, locationOnScreen.f12371y + this.f10196b.getHeight(), size.width, size.height);
        if (this.f10197c.isVisible()) {
            return;
        }
        SwingUtilities.invokeLater(new o(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        if (this.f10197c.isVisible()) {
            this.f10197c.setVisible(false);
            this.f10198d = "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        this.f10196b.setText(C1818g.b("Search"));
        this.f10196b.setForeground(Color.gray);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        this.f10196b.setText("");
        Color color = UIManager.getColor("TextField.foreground");
        if (color != null) {
            this.f10196b.setForeground(color);
        } else {
            this.f10196b.setForeground(Color.black);
        }
    }
}
