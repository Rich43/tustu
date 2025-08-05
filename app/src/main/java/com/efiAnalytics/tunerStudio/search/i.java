package com.efiAnalytics.tunerStudio.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/i.class */
class i extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JPanel f10192a = new JPanel();

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ h f10193b;

    i(h hVar, A a2, String str) throws IllegalArgumentException {
        this.f10193b = hVar;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setMinimumSize(new Dimension(50, 30));
        jPanel.setPreferredSize(new Dimension(35, 20));
        add("West", jPanel);
        JLabel jLabel = new JLabel(str + " - " + a2.a());
        jLabel.setFont(new Font("SansSerif", 1, 13));
        jLabel.setForeground(Color.DARK_GRAY);
        add("North", jLabel);
        this.f10192a.setLayout(new GridLayout(0, 1));
        Iterator it = a2.iterator();
        while (it.hasNext()) {
            B b2 = (B) it.next();
            C c2 = new C();
            c2.a(b2);
            this.f10192a.add(c2);
        }
        add(BorderLayout.CENTER, this.f10192a);
    }

    public List a() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.f10192a.getComponentCount(); i2++) {
            arrayList.add((C) this.f10192a.getComponent(i2));
        }
        return arrayList;
    }
}
