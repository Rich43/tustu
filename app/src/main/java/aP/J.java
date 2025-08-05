package aP;

import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/* loaded from: TunerStudioMS.jar:aP/J.class */
public class J extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextArea f2757a;

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1565bc f2758b;

    public J(ActionListener actionListener) {
        this.f2757a = new JTextArea();
        this.f2758b = null;
        setLayout(new BorderLayout());
        this.f2757a.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f2757a.setEditable(false);
        add(BorderLayout.CENTER, new JScrollPane(this.f2757a));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (actionListener != null) {
            JButton jButton = new JButton("What do Warnings Mean?");
            jButton.addActionListener(actionListener);
            jPanel.add(jButton);
        }
        JButton jButton2 = new JButton("Close");
        jButton2.addActionListener(new K(this));
        jPanel.add(jButton2);
        add("South", jPanel);
    }

    public J(String str) {
        this((ActionListener) null);
        a(str);
    }

    public void a(String str) {
        this.f2757a.setText(str);
    }

    public void a() {
        if (this.f2758b != null) {
            this.f2758b.close();
        }
    }

    public void a(InterfaceC1565bc interfaceC1565bc) {
        this.f2758b = interfaceC1565bc;
    }

    public void a(Window window) {
        a(window, "Review Audit");
    }

    public JDialog a(Window window, String str) {
        com.efiAnalytics.ui.dF dFVar = new com.efiAnalytics.ui.dF(window, str);
        dFVar.add(this);
        dFVar.setSize(com.efiAnalytics.ui.eJ.a(680), com.efiAnalytics.ui.eJ.a(400));
        a((InterfaceC1565bc) dFVar);
        com.efiAnalytics.ui.bV.a(window, (Component) dFVar);
        dFVar.setVisible(true);
        return dFVar;
    }
}
