package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JDialog;

/* renamed from: com.efiAnalytics.ui.dk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dk.class */
public class C1627dk extends JDialog implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    public boolean f11425a;

    /* renamed from: b, reason: collision with root package name */
    JButton f11426b;

    /* renamed from: c, reason: collision with root package name */
    JButton f11427c;

    public C1627dk(Frame frame, String str, boolean z2) throws HeadlessException {
        super(frame, "Message", true);
        this.f11425a = false;
        setLayout(new BorderLayout());
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(0, 1));
        add(BorderLayout.CENTER, panel);
        StringTokenizer stringTokenizer = new StringTokenizer(str, "\n");
        while (stringTokenizer.hasMoreElements()) {
            panel.add(new Label(stringTokenizer.nextToken()));
        }
        add(new Label(""), "West");
        add(new Label(""), "East");
        add(new Label(""), "North");
        a(z2);
        pack();
        Dimension size = frame != null ? frame.getSize() : Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size2 = getSize();
        Point location = frame != null ? frame.getLocation() : new Point(0, 0);
        if (location.getX() <= 0.0d || location.getY() <= 0.0d) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((int) (location.getX() + ((screenSize.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((screenSize.getHeight() - size2.getHeight()) / 2.0d)));
        } else {
            setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
        }
        setVisible(true);
    }

    protected void a(boolean z2) {
        Panel panel = new Panel();
        panel.setLayout(new FlowLayout());
        a(panel, z2);
        if (z2) {
            a(panel);
        }
        add("South", panel);
    }

    protected void a(Panel panel, boolean z2) {
        if (z2) {
            JButton jButton = new JButton("Yes");
            this.f11426b = jButton;
            panel.add(jButton);
        } else {
            JButton jButton2 = new JButton("Ok");
            this.f11426b = jButton2;
            panel.add(jButton2);
        }
        this.f11426b.addActionListener(this);
    }

    protected void a(Panel panel) {
        JButton jButton = new JButton("No");
        this.f11427c = jButton;
        panel.add(jButton);
        this.f11427c.addActionListener(this);
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.f11426b) {
            this.f11425a = true;
            setVisible(false);
        } else if (actionEvent.getSource() == this.f11427c) {
            setVisible(false);
        }
    }

    public static void a(String str, Component component) {
        C1627dk c1627dk = new C1627dk(a(component), str, false);
        c1627dk.requestFocus();
        c1627dk.dispose();
    }

    public static Frame a(Component component) {
        while (component != null && !(component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }
}
