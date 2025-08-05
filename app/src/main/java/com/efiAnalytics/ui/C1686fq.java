package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* renamed from: com.efiAnalytics.ui.fq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fq.class */
public class C1686fq extends JDialog implements ActionListener {

    /* renamed from: f, reason: collision with root package name */
    private boolean f11691f;

    /* renamed from: a, reason: collision with root package name */
    boolean f11692a;

    /* renamed from: b, reason: collision with root package name */
    JTextField f11693b;

    /* renamed from: c, reason: collision with root package name */
    JButton f11694c;

    /* renamed from: d, reason: collision with root package name */
    JButton f11695d;

    /* renamed from: e, reason: collision with root package name */
    Window f11696e;

    /* renamed from: g, reason: collision with root package name */
    private bH.aa f11697g;

    public C1686fq(Window window, boolean z2, String str, String str2, bH.aa aaVar) {
        super(window, "User Input", Dialog.ModalityType.TOOLKIT_MODAL);
        this.f11691f = false;
        this.f11692a = false;
        this.f11693b = null;
        this.f11696e = null;
        this.f11697g = null;
        this.f11691f = z2;
        this.f11696e = window;
        this.f11697g = aaVar;
        setTitle(a("Standard Input"));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        boolean z3 = (str != null && str.contains("\n")) || str.contains("<br>");
        if (z3) {
            jPanel.setLayout(new BorderLayout());
            str = bH.W.b(str, "\n", "<br>");
            if (!str.startsWith("<html>")) {
                str = "<html>" + str;
            }
        } else {
            jPanel.setLayout(new GridLayout(1, 2));
        }
        add(BorderLayout.CENTER, jPanel);
        str = str == null ? "Value:" : str;
        if (z3) {
            jPanel.add(BorderLayout.CENTER, new JLabel(str));
        } else {
            jPanel.add(new JLabel(str));
        }
        this.f11693b = new JTextField();
        this.f11693b.addFocusListener(new C1687fr(this));
        this.f11693b.addActionListener(this);
        if (str2 != null) {
            this.f11693b.setText(str2);
        }
        if (z3) {
            jPanel.add("South", this.f11693b);
        } else {
            jPanel.add(this.f11693b);
        }
        add(new JLabel(" "), "West");
        add(new JLabel(" "), "East");
        add(new JLabel(" "), "North");
        a(true);
        pack();
        Dimension size = window.getSize();
        Dimension size2 = getSize();
        Point location = window.getLocation();
        setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
        this.f11693b.selectAll();
    }

    public String a() {
        return !this.f11692a ? "" : this.f11693b.getText();
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
        JButton jButton = new JButton(a("OK"));
        this.f11694c = jButton;
        jPanel.add(jButton);
        this.f11694c.addActionListener(this);
    }

    protected void b(JPanel jPanel) {
        JButton jButton = new JButton(a("Cancel"));
        this.f11695d = jButton;
        jPanel.add(jButton);
        this.f11695d.addActionListener(this);
    }

    private void b() {
        if (c()) {
            this.f11692a = true;
            setVisible(false);
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof JTextField) {
            b();
        }
        if (actionEvent.getSource() == this.f11694c) {
            b();
        }
        if (actionEvent.getSource() == this.f11695d) {
            this.f11692a = false;
            setVisible(false);
        }
    }

    private boolean c() {
        if (!this.f11691f) {
            if (!this.f11693b.equals("")) {
                return true;
            }
            C1627dk.a("You must enter a value.", this.f11696e);
            return true;
        }
        try {
            if (this.f11693b.getText().equals("")) {
                C1627dk.a("Values Must Be Numeric", this.f11696e);
                return false;
            }
            Double.parseDouble(this.f11693b.getText());
            return true;
        } catch (Exception e2) {
            C1627dk.a("Values Must Be Numeric", this.f11696e);
            return false;
        }
    }

    private String a(String str) {
        return this.f11697g != null ? this.f11697g.a(str) : str;
    }
}
