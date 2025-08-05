package com.efiAnalytics.tunerStudio.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/h.class */
public class h extends JWindow {

    /* renamed from: a, reason: collision with root package name */
    JPanel f10188a = new JPanel();

    /* renamed from: b, reason: collision with root package name */
    int f10189b = 240;

    /* renamed from: c, reason: collision with root package name */
    int f10190c = 400;

    /* renamed from: d, reason: collision with root package name */
    List f10191d = new ArrayList();

    public h() {
        this.f10188a.setLayout(new BoxLayout(this.f10188a, 1));
        JScrollPane jScrollPane = new JScrollPane(this.f10188a);
        jScrollPane.setMaximumSize(new Dimension(this.f10189b, this.f10190c));
        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        jScrollPane.setHorizontalScrollBarPolicy(31);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, jScrollPane);
    }

    public void a(List list, String str) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            this.f10188a.add(new i(this, (A) it.next(), str));
        }
        b();
    }

    public void a() {
        this.f10188a.removeAll();
        this.f10191d.clear();
    }

    protected void b() {
        if (this.f10188a.getComponentCount() > 0) {
            Dimension preferredSize = super.getPreferredSize();
            if (preferredSize.height > this.f10190c) {
                preferredSize.height = this.f10190c;
            }
            preferredSize.width += 40;
            super.setSize(preferredSize);
        }
    }

    private List g() {
        if (this.f10191d.isEmpty()) {
            for (int i2 = 0; i2 < this.f10188a.getComponentCount(); i2++) {
                if (this.f10188a.getComponent(i2) instanceof i) {
                    this.f10191d.addAll(((i) this.f10188a.getComponent(i2)).a());
                }
            }
        }
        return this.f10191d;
    }

    public boolean a(int i2) {
        List listG = g();
        int i3 = 0;
        while (i3 < listG.size()) {
            ((C) listG.get(i3)).a(i3 == i2);
            if (i3 == i2) {
                ((C) listG.get(i3)).scrollRectToVisible(((C) listG.get(i3)).getBounds());
            }
            i3++;
        }
        return i2 >= 0 && i2 < listG.size();
    }

    public boolean c() {
        return a(e() + 1);
    }

    public boolean d() {
        return a(e() - 1);
    }

    public int e() {
        List listG = g();
        for (int i2 = 0; i2 < listG.size(); i2++) {
            if (((C) listG.get(i2)).b()) {
                return i2;
            }
        }
        return -1;
    }

    public void f() {
        List listG = g();
        for (int i2 = 0; i2 < listG.size(); i2++) {
            if (((C) listG.get(i2)).b()) {
                ((C) listG.get(i2)).a();
                return;
            }
        }
    }
}
