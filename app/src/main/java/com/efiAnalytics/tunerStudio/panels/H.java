package com.efiAnalytics.tunerStudio.panels;

import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.ui.C1564bb;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/H.class */
public class H extends JPanel implements InterfaceC1565bc {

    /* renamed from: c, reason: collision with root package name */
    private G.R f9941c = null;

    /* renamed from: a, reason: collision with root package name */
    C1425x f9942a;

    /* renamed from: b, reason: collision with root package name */
    C1425x f9943b;

    public H(G.R r2) {
        this.f9942a = null;
        this.f9943b = null;
        this.f9942a = new C1425x(r2);
        this.f9943b = new C1425x(r2);
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Real-Time Display")));
        setLayout(new BorderLayout());
        n.n nVar = new n.n();
        nVar.setTabPlacement(3);
        add(BorderLayout.CENTER, nVar);
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(800, 600));
        try {
            this.f9943b.a(new C1388aa().b(r2));
            nVar.addTab(C1818g.b("Summary"), this.f9943b);
            this.f9943b.setEnabled(false);
        } catch (V.a e2) {
            bH.C.a("Could not get Real-time display summary.");
        }
        this.f9942a.a(new C1388aa().a(r2));
        nVar.addTab(C1818g.b("All OutputChannels"), this.f9942a);
        this.f9942a.setEnabled(false);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f9942a.removeAll();
        this.f9943b.removeAll();
    }

    public JDialog a(Frame frame, String str, InterfaceC1565bc interfaceC1565bc) {
        JDialog jDialog = new JDialog(frame, str);
        jDialog.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new I(this));
        if (interfaceC1565bc != null) {
            jButton.addActionListener(new C1564bb(interfaceC1565bc));
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        jPanel.add(jButton);
        jDialog.add("South", jPanel);
        jDialog.pack();
        bV.a((Window) frame, (Component) jDialog);
        jDialog.setVisible(true);
        return jDialog;
    }
}
