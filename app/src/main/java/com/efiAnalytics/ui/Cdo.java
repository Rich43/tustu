package com.efiAnalytics.ui;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

/* renamed from: com.efiAnalytics.ui.do, reason: invalid class name */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/do.class */
public class Cdo extends JTextField {

    /* renamed from: c, reason: collision with root package name */
    double f11438c;

    /* renamed from: a, reason: collision with root package name */
    private int f11439a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f11440b;

    /* renamed from: d, reason: collision with root package name */
    C1631dp f11441d;

    public Cdo() {
        this("");
    }

    public Cdo(String str) {
        this(str, 5);
    }

    public Cdo(String str, int i2) {
        super(str, i2);
        this.f11438c = Double.NaN;
        this.f11439a = -1;
        this.f11440b = false;
        this.f11441d = null;
        a();
        setBorder(BorderFactory.createBevelBorder(1));
    }

    private void a() {
        this.f11441d = new C1631dp(this);
        ((AbstractDocument) getDocument()).setDocumentFilter(this.f11441d);
    }

    public void a(double d2) {
        if (f()) {
            setText(Integer.toHexString((int) d2));
        } else if (this.f11439a >= 0) {
            setText(bH.W.b(d2, this.f11439a));
        } else {
            setText(bH.W.a(d2));
        }
    }

    public void d() {
        this.f11438c = Double.NaN;
    }

    public double e() {
        if (Double.isNaN(this.f11438c)) {
            try {
                if (f()) {
                    this.f11438c = Integer.parseInt(getText(), 16);
                } else {
                    this.f11438c = Double.parseDouble(getText());
                }
            } catch (NumberFormatException e2) {
                return Double.NaN;
            }
        }
        return this.f11438c;
    }

    @Override // javax.swing.text.JTextComponent
    public void setText(String str) {
        String strI = bH.W.i(str);
        if (f()) {
            if (strI.startsWith("0x")) {
                strI = strI.substring(2);
            }
            if (this.f11439a > 0) {
                strI = bH.W.a(strI, '0', this.f11439a);
            }
            strI = "0x" + strI;
        } else if (this.f11439a >= 0 && bH.H.a(strI)) {
            strI = bH.W.a(strI, this.f11439a);
        }
        super.setText(strI);
    }

    public void a(String str) {
        this.f11441d.a(false);
        super.setText(str);
        this.f11441d.a(true);
    }

    public void b(int i2) {
        double dE = e();
        this.f11439a = i2;
        a(dE);
    }

    public boolean f() {
        return this.f11440b;
    }

    public void a(boolean z2) {
        this.f11440b = z2;
    }
}
