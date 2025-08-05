package com.efiAnalytics.tunerStudio.panels;

import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.ui.C1564bb;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/F.class */
public class F extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    C1425x f9939a;

    public F(J.h hVar) {
        this.f9939a = null;
        this.f9939a = new C1425x();
        this.f9939a.f(true);
        setBorder(BorderFactory.createTitledBorder("Protocol Stats"));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f9939a);
        setPreferredSize(eJ.a(800, 320));
        setMinimumSize(eJ.a(800, 320));
        this.f9939a.a(new C1388aa().a(hVar));
        this.f9939a.setEnabled(false);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f9939a.removeAll();
    }

    public JDialog a(Window window, String str, InterfaceC1565bc interfaceC1565bc) {
        JDialog jDialog = new JDialog(window, str);
        jDialog.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new G(this));
        if (interfaceC1565bc != null) {
            jButton.addActionListener(new C1564bb(interfaceC1565bc));
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        jPanel.add(jButton);
        jDialog.add("South", jPanel);
        jDialog.pack();
        bV.a(window, (Component) jDialog);
        jDialog.setVisible(true);
        return jDialog;
    }
}
