package com.efiAnalytics.ui.calculators.calculator;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.OptionalDouble;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/calculators/calculator/Display.class */
class Display extends JPanel {

    /* renamed from: b, reason: collision with root package name */
    private JLabel f11197b;

    /* renamed from: c, reason: collision with root package name */
    private JLabel f11198c;

    /* renamed from: d, reason: collision with root package name */
    private final StringBuilder f11199d;

    /* renamed from: a, reason: collision with root package name */
    String f11200a = "";

    Display() throws IllegalArgumentException {
        f();
        this.f11199d = new StringBuilder(32);
    }

    private void f() throws IllegalArgumentException {
        setLayout(new BorderLayout(1, 1));
        this.f11197b = new JLabel("0");
        this.f11197b.setFont(new Font("Consolas", 0, eJ.a(30)));
        this.f11197b.setFocusable(false);
        this.f11197b.setBorder(BorderFactory.createEmptyBorder(eJ.a(2), eJ.a(2), eJ.a(2), eJ.a(2)));
        this.f11197b.setHorizontalAlignment(4);
        this.f11197b.setMaximumSize(this.f11197b.getPreferredSize());
        add(BorderLayout.CENTER, this.f11197b);
        this.f11198c = new JLabel("0");
        this.f11198c.setFont(new Font("Consolas", 0, eJ.a(12)));
        this.f11198c.setFocusable(false);
        this.f11198c.setBorder(BorderFactory.createEmptyBorder(eJ.a(2), eJ.a(2), eJ.a(2), eJ.a(2)));
        this.f11198c.setHorizontalAlignment(4);
        this.f11198c.setMaximumSize(this.f11198c.getPreferredSize());
        this.f11198c.setMinimumSize(this.f11198c.getPreferredSize());
        this.f11198c.setText(" ");
        add("North", this.f11198c);
    }

    private boolean b(double d2) {
        return ((double) Math.round(d2)) != d2;
    }

    void a(double d2) throws IllegalArgumentException {
        String string = b(d2) ? Double.toString(d2) : Long.toString((long) d2);
        if (string.length() > 16) {
            string = string.substring(0, 16);
        }
        this.f11197b.setText(string);
    }

    OptionalDouble a() throws IllegalArgumentException {
        if (!g()) {
            return OptionalDouble.empty();
        }
        double d2 = Double.parseDouble(this.f11199d.toString());
        d();
        return OptionalDouble.of(d2);
    }

    void a(String str) throws IllegalArgumentException {
        if (this.f11200a.equals("=")) {
            this.f11198c.setText(" ");
        }
        this.f11198c.setText(this.f11198c.getText().equals(" ") ? this.f11197b.getText() + " " + str : this.f11198c.getText() + " " + this.f11197b.getText() + " " + str);
        this.f11200a = str;
    }

    void b() throws IllegalArgumentException {
        this.f11198c.setText(" ");
        this.f11200a = "";
    }

    void c() throws IllegalArgumentException {
        if (this.f11200a.equals("=")) {
            b();
        }
    }

    void b(String str) throws IllegalArgumentException {
        this.f11199d.append(str);
        this.f11197b.setText(this.f11199d.toString());
        c();
    }

    private boolean g() {
        return this.f11199d.length() > 0;
    }

    void d() throws IllegalArgumentException {
        this.f11199d.delete(0, this.f11199d.length());
        this.f11197b.setText("0");
    }

    void e() {
        if (this.f11199d.indexOf(".") == -1) {
            this.f11199d.append('.');
        }
    }
}
