package aP;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

/* loaded from: TunerStudioMS.jar:aP/aS.class */
public class aS extends JDialog implements bQ.d {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f2840a;

    /* renamed from: b, reason: collision with root package name */
    JTextPane f2841b;

    /* renamed from: c, reason: collision with root package name */
    JTextPane f2842c;

    /* renamed from: d, reason: collision with root package name */
    JLabel f2843d;

    /* renamed from: e, reason: collision with root package name */
    JLabel f2844e;

    /* renamed from: f, reason: collision with root package name */
    JLabel f2845f;

    public aS(Window window) throws IllegalArgumentException {
        super(window, "DAQ Report");
        this.f2840a = new JTextPane();
        this.f2841b = new JTextPane();
        this.f2842c = new JTextPane();
        this.f2843d = new JLabel("", 2);
        this.f2844e = new JLabel("", 2);
        this.f2845f = new JLabel("", 2);
        super.setDefaultCloseOperation(2);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0, 5, 5));
        jPanel.add(new JLabel("Max ODT's:", 4));
        jPanel.add(this.f2843d);
        jPanel.add(new JLabel("Max ODT Entries's:", 4));
        jPanel.add(this.f2844e);
        jPanel.add(new JLabel("Max Entry Size:", 4));
        jPanel.add(this.f2845f);
        add("North", jPanel);
        Font font = new Font("Monospaced", 0, com.efiAnalytics.ui.eJ.a(12));
        this.f2840a.setFont(font);
        this.f2841b.setFont(font);
        this.f2842c.setFont(font);
        JTabbedPane jTabbedPane = new JTabbedPane(3);
        jTabbedPane.add("ODTs", new JScrollPane(this.f2842c));
        jTabbedPane.add("Channels", new JScrollPane(this.f2841b));
        jTabbedPane.add("Buffer Ranges", new JScrollPane(this.f2840a));
        add(BorderLayout.CENTER, jTabbedPane);
        b();
        bQ.c.a().a(this);
        setSize(1024, 800);
        setVisible(true);
        com.efiAnalytics.ui.bV.a(window, (Component) this);
    }

    @Override // java.awt.Window
    public void dispose() {
        bQ.c.a().b(this);
        super.dispose();
    }

    private void b() throws IllegalArgumentException {
        this.f2843d.setText(bQ.c.a().f() + "");
        this.f2844e.setText(bQ.c.a().g() + "");
        this.f2845f.setText(bQ.c.a().e() + "");
        if (bQ.c.a().c() != null) {
            this.f2841b.setText(bQ.c.b(bQ.c.a().c()));
        }
        if (bQ.c.a().b() != null) {
            this.f2840a.setText(bQ.c.a(bQ.c.a().b()));
        }
        if (bQ.c.a().d() != null) {
            this.f2842c.setText(bQ.c.c(bQ.c.a().d()));
        }
    }

    @Override // bQ.d
    public void a() throws IllegalArgumentException {
        b();
    }
}
