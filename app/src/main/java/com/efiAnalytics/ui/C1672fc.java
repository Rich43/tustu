package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/* renamed from: com.efiAnalytics.ui.fc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fc.class */
public class C1672fc extends JWindow {

    /* renamed from: a, reason: collision with root package name */
    int f11659a = -1;

    /* renamed from: b, reason: collision with root package name */
    int f11660b = -1;

    /* renamed from: c, reason: collision with root package name */
    Color f11661c = Color.YELLOW;

    /* renamed from: d, reason: collision with root package name */
    int f11662d = 4;

    /* renamed from: e, reason: collision with root package name */
    int f11663e = 3;

    /* renamed from: f, reason: collision with root package name */
    ArrayList f11664f = new ArrayList();

    public C1672fc() {
        setLayout(new GridLayout(this.f11663e, this.f11662d));
        C1675ff c1675ff = new C1675ff(this);
        for (int i2 = 0; i2 < this.f11663e; i2++) {
            for (int i3 = 0; i3 < this.f11662d; i3++) {
                Component c1676fg = new C1676fg(this, i2, i3);
                add(c1676fg);
                c1676fg.addMouseListener(c1675ff);
                c1676fg.addMouseMotionListener(c1675ff);
            }
        }
        addWindowFocusListener(new C1673fd(this));
        setFocusable(true);
        SwingUtilities.invokeLater(new RunnableC1674fe(this));
    }

    public boolean a(int i2, int i3) {
        return this.f11659a >= i2 && this.f11660b >= i3;
    }

    public void a(InterfaceC1671fb interfaceC1671fb) {
        this.f11664f.add(interfaceC1671fb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i2, int i3) {
        Iterator it = this.f11664f.iterator();
        while (it.hasNext()) {
            ((InterfaceC1671fb) it.next()).a(i2, i3);
        }
    }
}
