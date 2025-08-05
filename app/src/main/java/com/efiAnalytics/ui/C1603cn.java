package com.efiAnalytics.ui;

import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import javax.swing.JLabel;

/* renamed from: com.efiAnalytics.ui.cn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cn.class */
public class C1603cn extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    String f11268a = "";

    /* renamed from: b, reason: collision with root package name */
    double f11269b;

    public C1603cn() {
        this.f11269b = 0.0d;
        setFont(new Font("SansSerif", 0, eJ.a(11)));
        this.f11269b = getFontMetrics(getFont()).stringWidth("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ") / 52;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) throws IllegalArgumentException {
        String strSubstring = this.f11268a;
        int width = (int) (getWidth() / this.f11269b);
        if (strSubstring != null && strSubstring.length() > width) {
            int i2 = width - 19;
            if (i2 > 0) {
                strSubstring = strSubstring.substring(0, 10) + " .... " + strSubstring.substring(strSubstring.length() - i2, strSubstring.length());
            } else if (strSubstring.lastIndexOf(File.separator) != -1) {
                strSubstring = strSubstring.substring(strSubstring.lastIndexOf(File.separator));
            }
        }
        if (strSubstring != null && !getText().equals(strSubstring)) {
            super.setText(strSubstring);
        }
        super.paint(graphics);
    }

    public void a(String str) throws IllegalArgumentException {
        this.f11268a = str;
        super.setText(this.f11268a);
    }

    @Override // javax.swing.JLabel
    public void setText(String str) throws IllegalArgumentException {
        this.f11268a = str;
        super.setText(this.f11268a);
    }

    public String a() {
        return this.f11268a;
    }
}
