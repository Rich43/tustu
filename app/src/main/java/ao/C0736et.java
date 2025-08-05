package ao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: ao.et, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/et.class */
public class C0736et extends JDialog implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    boolean f5662a;

    /* renamed from: b, reason: collision with root package name */
    JButton f5663b;

    /* renamed from: c, reason: collision with root package name */
    JButton f5664c;

    public C0736et(Frame frame, String str, boolean z2) throws HeadlessException {
        super(frame, "Message", true);
        this.f5662a = false;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        add(BorderLayout.CENTER, jPanel);
        StringTokenizer stringTokenizer = new StringTokenizer(str, "\n");
        while (stringTokenizer.hasMoreElements()) {
            JLabel jLabel = new JLabel(stringTokenizer.nextToken(), 0);
            if (jLabel.getText().startsWith("!")) {
                jLabel.setBackground(Color.red);
            }
            jPanel.add(jLabel);
        }
        add(new JLabel(""), "West");
        add(new JLabel(""), "East");
        add(new JLabel(""), "North");
        a(z2);
        pack();
        Dimension size = frame.getSize();
        Dimension size2 = getSize();
        Point location = frame.getLocation();
        if (location.getX() <= 0.0d || location.getY() <= 0.0d) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((int) (location.getX() + ((screenSize.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((screenSize.getHeight() - size2.getHeight()) / 2.0d)));
        } else {
            setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
        }
        setVisible(true);
    }

    protected void a(boolean z2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        a(jPanel);
        if (z2) {
            b(jPanel);
        }
        add("South", jPanel);
    }

    protected void a(JPanel jPanel) {
        JButton jButton = new JButton("Yes");
        this.f5663b = jButton;
        jPanel.add(jButton);
        this.f5663b.addActionListener(this);
    }

    protected void b(JPanel jPanel) {
        JButton jButton = new JButton("No");
        this.f5664c = jButton;
        jPanel.add(jButton);
        this.f5664c.addActionListener(this);
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.f5663b) {
            this.f5662a = true;
            setVisible(false);
        } else if (actionEvent.getSource() == this.f5664c) {
            setVisible(false);
        }
    }
}
