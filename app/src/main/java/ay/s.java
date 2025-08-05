package aY;

import W.C0200z;
import W.ag;
import aP.aP;
import com.efiAnalytics.tuningwidgets.panels.U;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aY/s.class */
public class s extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aE.a f4075a;

    /* renamed from: b, reason: collision with root package name */
    A f4076b = new A(this);

    /* renamed from: c, reason: collision with root package name */
    U f4077c = new U();

    /* renamed from: d, reason: collision with root package name */
    JButton f4078d;

    /* renamed from: e, reason: collision with root package name */
    JButton f4079e;

    /* renamed from: f, reason: collision with root package name */
    aP f4080f;

    public s(aE.a aVar) throws IllegalArgumentException {
        this.f4075a = null;
        this.f4078d = null;
        this.f4079e = null;
        this.f4080f = null;
        this.f4075a = aVar;
        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel();
        jLabel.setText("<html><p>" + C1818g.b("Restore Points are automatically saved on key events set in preferences.") + " " + C1818g.b("Each Restore Point contains all Controller Settings for that point in time.") + " " + C1818g.b("Below you can retrieve those settings or view changes made since that Restore Point.") + "</p></html>");
        jLabel.setPreferredSize(new Dimension(eJ.a(300), eJ.a(80)));
        jLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("restore-icon-02.png"))));
        jLabel.setIconTextGap(eJ.a(15));
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Automatic Saved Restore Points")));
        add("North", jLabel);
        JScrollPane jScrollPane = new JScrollPane(this.f4076b);
        add("West", jScrollPane);
        jScrollPane.setBorder(BorderFactory.createTitledBorder(C1818g.b("Restore Points")));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f4080f = new aP();
        this.f4080f.setBorder(BorderFactory.createTitledBorder(C1818g.b("Controller to Restore")));
        this.f4080f.a(new t(this));
        jPanel.add("North", this.f4080f);
        this.f4077c.setBorder(BorderFactory.createTitledBorder(C1818g.b("Selected Restore Point")));
        jPanel.add(BorderLayout.CENTER, this.f4077c);
        add(BorderLayout.CENTER, jPanel);
        a();
        this.f4076b.addListSelectionListener(new u(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0));
        this.f4079e = new JButton(C1818g.b("Compare to Current"));
        this.f4079e.addActionListener(new v(this));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(1));
        jPanel3.add(this.f4079e);
        this.f4079e.setEnabled(false);
        jPanel2.add(jPanel3);
        this.f4078d = new JButton(C1818g.b("Load Restore Point"));
        this.f4078d.addActionListener(new w(this));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(1));
        jPanel4.add(this.f4078d);
        this.f4078d.setEnabled(false);
        jPanel2.add(jPanel4);
        this.f4076b.addListSelectionListener(new x(this));
        add("South", jPanel2);
    }

    public void a(Component component) {
        bV.a(this, component, C1818g.b("Tune Restore Points"), (InterfaceC1565bc) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f4076b.f4027a.clear();
        ArrayList arrayListA = C0200z.a(C1807j.b(this.f4075a), C1798a.cw, new y(this));
        Collections.sort(arrayListA, new z(this));
        Iterator it = arrayListA.iterator();
        while (it.hasNext()) {
            this.f4076b.a((ag) it.next());
        }
        this.f4076b.setBorder(BorderFactory.createLoweredBevelBorder());
    }
}
