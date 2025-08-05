package aY;

import G.R;
import aP.C0205aa;
import ac.C0491c;
import com.efiAnalytics.ui.C1603cn;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jdk.internal.dynalink.CallSiteDescriptor;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;
import z.C1897a;

/* loaded from: TunerStudioMS.jar:aY/f.class */
public class f extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    R f4048a;

    /* renamed from: b, reason: collision with root package name */
    C1603cn f4049b;

    /* renamed from: c, reason: collision with root package name */
    JLabel f4050c;

    /* renamed from: d, reason: collision with root package name */
    JLabel f4051d;

    /* renamed from: e, reason: collision with root package name */
    JLabel f4052e;

    /* renamed from: f, reason: collision with root package name */
    String f4053f;

    /* renamed from: g, reason: collision with root package name */
    JLabel f4054g;

    /* renamed from: h, reason: collision with root package name */
    JComboBox f4055h;

    /* renamed from: i, reason: collision with root package name */
    C0205aa f4056i;

    /* renamed from: j, reason: collision with root package name */
    z.i f4057j;

    /* renamed from: k, reason: collision with root package name */
    JButton f4058k;

    /* renamed from: l, reason: collision with root package name */
    C1897a f4059l;

    /* renamed from: m, reason: collision with root package name */
    C0491c f4060m;

    /* renamed from: n, reason: collision with root package name */
    boolean f4061n;

    public f(Frame frame, R r2, File file, boolean z2) throws IllegalArgumentException {
        super(frame, C1818g.b("Burst Mode Logger"), true);
        this.f4048a = null;
        this.f4049b = new C1603cn();
        this.f4050c = new JLabel();
        this.f4051d = new JLabel();
        this.f4052e = new JLabel();
        this.f4053f = C1818g.b("Idle: Click 'Start Logging' to begin processing");
        this.f4054g = new JLabel(this.f4053f, 0);
        this.f4055h = new JComboBox();
        this.f4056i = null;
        this.f4057j = new z.i();
        this.f4058k = new JButton(C1818g.b("Start Logging"));
        this.f4059l = null;
        this.f4060m = null;
        this.f4061n = false;
        if (z2) {
            setTitle(C1818g.b("Palm Extract Utility"));
        }
        this.f4061n = z2;
        this.f4048a = r2;
        this.f4049b.setPreferredSize(new Dimension(100, 20));
        this.f4049b.setMinimumSize(new Dimension(100, 20));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(getTitle()));
        jPanel.setLayout(new BorderLayout());
        add("North", new JLabel(" "));
        add("South", new JLabel(" "));
        add("East", new JLabel(Constants.INDENT));
        add("West", new JLabel(Constants.INDENT));
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1, 3, 3));
        if (z2) {
            jPanel2.add(new JLabel(C1818g.b("Palm Logger Extract Utility"), 0));
        } else {
            jPanel2.add(new JLabel(C1818g.b("Burst Mode Logger"), 0));
        }
        jPanel2.add(this.f4049b);
        try {
            this.f4049b.a(file.getCanonicalPath());
        } catch (IOException e2) {
            bH.C.a(C1818g.b("Failed to get canonical path for burst file"));
            e2.printStackTrace();
        }
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 0, 5, 5));
        jPanel3.add(new JLabel(C1818g.b(RuntimeModeler.PORT) + CallSiteDescriptor.TOKEN_DELIMITER, 4));
        this.f4056i = new C0205aa(r2);
        this.f4056i.setPreferredSize(new Dimension(80, 24));
        jPanel3.add(a(this.f4056i, "North"));
        this.f4056i.setSelectedItem(r2.O().s());
        jPanel3.add(new JLabel(C1818g.b("Baud") + CallSiteDescriptor.TOKEN_DELIMITER, 4));
        for (String str : this.f4057j.c()) {
            this.f4055h.addItem(str);
        }
        if (z2) {
            this.f4055h.setSelectedItem("115200");
        } else {
            this.f4055h.setSelectedItem(Integer.valueOf(r2.O().r()));
        }
        this.f4055h.setPreferredSize(new Dimension(80, 24));
        jPanel3.add(a(this.f4055h, "North"));
        jPanel3.add(new JLabel(""));
        if (z2) {
            jPanel2.add(jPanel3);
        }
        jPanel2.add(new JLabel(" "));
        jPanel2.add(this.f4054g);
        jPanel2.add(new JLabel(" "));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(1, 0, 3, 3));
        jPanel4.add(new JLabel(C1818g.b("Records") + CallSiteDescriptor.TOKEN_DELIMITER, 4));
        jPanel4.add(this.f4051d);
        jPanel4.add(new JLabel(C1818g.b("Log Time") + CallSiteDescriptor.TOKEN_DELIMITER, 4));
        jPanel4.add(this.f4050c);
        jPanel4.add(new JLabel(C1818g.b("Rate") + CallSiteDescriptor.TOKEN_DELIMITER, 4));
        jPanel4.add(this.f4052e);
        jPanel2.add(jPanel4);
        jPanel.add("North", jPanel2);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(2));
        jPanel5.add(this.f4058k);
        this.f4058k.setMnemonic(83);
        this.f4058k.addActionListener(new g(this));
        JButton jButton = new JButton(C1818g.b("Close"));
        jButton.setMnemonic(67);
        jButton.addActionListener(new h(this));
        jPanel5.add(jButton);
        jPanel.add("South", jPanel5);
        pack();
    }

    public void a() {
        if (this.f4059l != null && this.f4059l.c()) {
            c();
            try {
                Thread.sleep(300L);
            } catch (InterruptedException e2) {
                Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() throws IllegalArgumentException {
        if (this.f4059l != null && this.f4059l.c()) {
            c();
            return;
        }
        try {
            b();
        } catch (V.a e2) {
            bH.C.a("Unable to start burst capture.", e2, this);
        }
    }

    public void b() throws IllegalArgumentException {
        if (this.f4059l != null) {
            this.f4059l.b();
        }
        this.f4059l = new C1897a(this.f4048a);
        this.f4059l.a(this.f4061n);
        if (!this.f4061n) {
        }
        this.f4060m = new aZ.b();
        this.f4059l.a(this.f4060m);
        this.f4059l.a(new i(this));
        this.f4054g.setText(C1818g.b("Polling device") + "...");
        this.f4055h.setEnabled(false);
        this.f4056i.setEnabled(false);
        this.f4060m.a(this.f4048a.c(), this.f4049b.a());
        this.f4059l.b(this.f4056i.getSelectedItem().toString());
        this.f4059l.a(this.f4055h.getSelectedItem().toString());
        this.f4059l.a();
        this.f4058k.setText(C1818g.b("Stop Logging"));
    }

    public void c() {
        if (this.f4059l != null) {
            this.f4059l.b();
            if (this.f4060m != null) {
                this.f4059l.b(this.f4060m);
                try {
                    this.f4060m.l();
                } catch (V.a e2) {
                    bH.C.a("Could not stop data log.", e2, this);
                }
            }
        }
        this.f4058k.setText(C1818g.b("Start Logging"));
        this.f4054g.setText(this.f4053f);
        this.f4055h.setEnabled(true);
        this.f4056i.setEnabled(true);
    }

    private JPanel a(Component component, String str) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(str, component);
        return jPanel;
    }
}
