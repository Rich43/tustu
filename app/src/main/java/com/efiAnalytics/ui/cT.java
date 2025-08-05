package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cT.class */
class cT extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    boolean f11161a;

    /* renamed from: b, reason: collision with root package name */
    JButton f11162b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ cS f11163c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    cT(cS cSVar, Frame frame) throws HeadlessException {
        super(frame, true);
        this.f11163c = cSVar;
        this.f11161a = false;
        this.f11162b = null;
        setTitle("Register");
        setDefaultCloseOperation(0);
        getContentPane().setLayout(new BorderLayout());
        String str = bH.M.f7011a[((int) (Math.random() * 100.0d)) % bH.M.f7011a.length] + "\nSupport " + cSVar.f7066a.a() + " , Register now!";
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        jPanel.add(new JLabel(" "));
        StringTokenizer stringTokenizer = new StringTokenizer(str, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            JLabel jLabel = new JLabel(stringTokenizer.nextToken());
            jLabel.setHorizontalAlignment(0);
            jPanel.add(jLabel);
        }
        jPanel.add(new JLabel(" "));
        getContentPane().add(BorderLayout.CENTER, jPanel);
        getContentPane().add("South", b());
        pack();
        a(frame);
        setResizable(false);
    }

    public void a() {
        this.f11162b.setEnabled(false);
        new cX(this).start();
        setVisible(true);
    }

    private void a(Frame frame) throws HeadlessException {
        Dimension size = frame.getSize();
        Dimension size2 = getSize();
        Point location = frame.getLocation();
        if (location.getX() > 0.0d && location.getY() > 0.0d) {
            setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((int) (location.getX() + ((screenSize.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((screenSize.getHeight() - size2.getHeight()) / 2.0d)));
        }
    }

    JPanel b() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (((int) (Math.random() * 10.0d)) % 2 == 0) {
            jPanel.add(f());
            jPanel.add(d());
        } else {
            jPanel.add(d());
            jPanel.add(f());
        }
        jPanel.add(c());
        return jPanel;
    }

    private JButton c() {
        JButton jButton = new JButton("Register");
        jButton.addActionListener(new cU(this));
        return jButton;
    }

    private JButton d() {
        JButton jButton = new JButton("Enter Registration Key");
        jButton.addActionListener(new cV(this));
        return jButton;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String e() {
        return this.f11163c.f11160f <= 0 ? "Register Later" : "Register Later (" + this.f11163c.f11160f + ")";
    }

    private JButton f() {
        this.f11162b = new JButton(e());
        this.f11162b.addActionListener(new cW(this));
        return this.f11162b;
    }
}
