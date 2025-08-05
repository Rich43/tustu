package ao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/* renamed from: ao.bj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bj.class */
public class C0646bj extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    JTextArea f5413a;

    public C0646bj(Window window) {
        super(window);
        this.f5413a = new JTextArea();
        add(new JScrollPane(this.f5413a), BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new C0647bk(this));
        jPanel.add(jButton);
        add(jPanel, "South");
        this.f5413a.setFont(new Font("Monospaced", 0, com.efiAnalytics.ui.eJ.a(12)));
    }

    public void a(String str) {
        this.f5413a.setText(str);
        this.f5413a.setCaretPosition(0);
    }
}
