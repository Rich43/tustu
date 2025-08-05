package aY;

import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:aY/o.class */
public class o extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    private static int f4071a = 2;

    private o(Frame frame) {
        super(frame, "MegaLogViewer Not Found", true);
        setLayout(new BorderLayout());
        add("North", new JLabel(" "));
        add("East", new JLabel(" "));
        add("West", new JLabel(" "));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 3, 3));
        jPanel.add(new JLabel("The location MegaLogViewer is not known.", 0));
        jPanel.add(new JLabel("Please select from the following choices:", 0));
        jPanel.add(new JLabel("", 0));
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        JButton jButton = new JButton("Browse");
        jButton.addActionListener(new p(this));
        jPanel2.add(jButton);
        JButton jButton2 = new JButton("Download MegaLogViewer");
        jButton2.addActionListener(new q(this));
        jPanel2.add(jButton2);
        JButton jButton3 = new JButton("Cancel");
        jButton3.addActionListener(new r(this));
        jPanel2.add(jButton3);
        add("South", jPanel2);
        pack();
        bV.a((Window) frame, (Component) this);
    }

    public static int a(Frame frame) {
        new o(frame).setVisible(true);
        return f4071a;
    }
}
