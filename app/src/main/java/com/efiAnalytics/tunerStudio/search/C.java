package com.efiAnalytics.tunerStudio.search;

import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/C.class */
public class C extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JLabel f10157a = new JLabel();

    /* renamed from: b, reason: collision with root package name */
    B f10158b = null;

    /* renamed from: c, reason: collision with root package name */
    Font f10159c = new Font("SansSerif", 0, eJ.a(11));

    /* renamed from: d, reason: collision with root package name */
    private boolean f10160d = false;

    public C() {
        setLayout(new GridLayout(1, 1));
        add(this.f10157a);
        this.f10157a.setFont(this.f10159c);
        setBackground(Color.WHITE);
        this.f10157a.setForeground(Color.BLACK);
        addMouseListener(new D(this));
    }

    public void a(B b2) throws IllegalArgumentException {
        String strA = a(b2.a(), b2.b());
        if (b2.d() != null && !b2.d().isEmpty()) {
            strA = strA + " - (" + b2.d() + ")";
        }
        this.f10157a.setText("<html>" + strA + "</html>");
        this.f10158b = b2;
    }

    private String a(String str, String str2) {
        int iIndexOf = str.toLowerCase().indexOf(str2.toLowerCase());
        while (true) {
            int i2 = iIndexOf;
            if (i2 < 0) {
                return str;
            }
            String str3 = "<font color=\"red\"><b>" + str.substring(i2, i2 + str2.length()) + "</b></font>";
            str = str.substring(0, i2) + str3 + str.substring(i2 + str2.length());
            iIndexOf = str.toLowerCase().indexOf(str2.toLowerCase(), i2 + str3.length());
        }
    }

    void a(boolean z2) {
        this.f10160d = z2;
        if (z2) {
            this.f10157a.setForeground(Color.WHITE);
            setBackground(Color.BLUE.darker());
        } else {
            this.f10157a.setForeground(Color.BLACK);
            setBackground(Color.WHITE);
        }
    }

    public void a() {
        this.f10158b.c().a(this.f10158b);
    }

    public boolean b() {
        return this.f10160d;
    }
}
