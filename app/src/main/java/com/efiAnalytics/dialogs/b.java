package com.efiAnalytics.dialogs;

import bH.I;
import bH.aa;
import bu.C1368a;
import com.efiAnalytics.ui.InterfaceC1535a;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/dialogs/b.class */
public class b extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    e f9888a;

    /* renamed from: b, reason: collision with root package name */
    aa f9889b;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f9890c;

    public b(Window window, String str, aa aaVar) {
        super(window, str, Dialog.ModalityType.MODELESS);
        this.f9889b = null;
        this.f9890c = new ArrayList();
        this.f9888a = new e(str, aaVar);
        this.f9889b = aaVar;
        setLayout(new BorderLayout());
        add(this.f9888a, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(a("Apply"));
        jButton.addActionListener(new c(this));
        JButton jButton2 = new JButton(a("Cancel"));
        jButton2.addActionListener(new d(this));
        if (I.a()) {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        } else {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        }
        add(jPanel, "South");
    }

    public void a(C1368a c1368a) throws IllegalArgumentException {
        this.f9888a.a(c1368a);
    }

    public C1368a a() {
        return this.f9888a.b();
    }

    public String b() {
        return this.f9888a.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        Iterator it = this.f9890c.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1535a) it.next()).a()) {
                return;
            }
        }
        dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        Iterator it = this.f9890c.iterator();
        while (it.hasNext()) {
            ((InterfaceC1535a) it.next()).b();
        }
        dispose();
    }

    public void a(InterfaceC1535a interfaceC1535a) {
        this.f9890c.add(interfaceC1535a);
    }

    private String a(String str) {
        if (this.f9889b != null) {
            str = this.f9889b.a(str);
        }
        return str;
    }
}
