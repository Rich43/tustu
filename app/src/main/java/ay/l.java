package aY;

import G.C0126i;
import G.R;
import G.aI;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:aY/l.class */
public class l extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    JTextField f4066a;

    /* renamed from: b, reason: collision with root package name */
    JTextField f4067b;

    /* renamed from: c, reason: collision with root package name */
    R f4068c;

    public l(Window window, R r2) {
        super(window, "Expression Evaluator");
        this.f4066a = new JTextField("", 40);
        this.f4067b = new JTextField("", 40);
        this.f4068c = null;
        this.f4068c = r2;
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder("Expression Evaluator"));
        jPanel.setLayout(new BorderLayout(5, 5));
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1));
        jPanel2.add(new JLabel("Expression:"));
        jPanel2.add(this.f4066a);
        jPanel2.add(new JLabel("Result:"));
        jPanel2.add(this.f4067b);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        JButton jButton = new JButton("Evaluate Expression");
        jButton.addActionListener(new m(this));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add("West", jButton);
        JButton jButton2 = new JButton("Close");
        jButton2.addActionListener(new n(this));
        jPanel3.add("East", jButton2);
        jPanel.add("South", jPanel3);
        pack();
    }

    protected void a() {
        try {
            this.f4067b.setText("" + C0126i.a((aI) this.f4068c, " " + this.f4066a.getText() + " "));
        } catch (V.g e2) {
            bV.d("Error:" + e2.getMessage(), this);
        }
    }
}
