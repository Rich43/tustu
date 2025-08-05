package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.Cdo;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/au.class */
class au extends Cdo {

    /* renamed from: a, reason: collision with root package name */
    boolean f10424a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1497an f10425b;

    au(C1497an c1497an) {
        this.f10425b = c1497an;
    }

    public void a() {
        if (this.f10424a) {
            return;
        }
        String text = getText();
        try {
            if (text.trim().length() > 0) {
                setText("" + ((int) Math.round(Double.parseDouble(text) * 16.387d)));
            }
        } catch (NumberFormatException e2) {
        }
        this.f10424a = true;
    }

    public void b() {
        if (this.f10424a) {
            String text = getText();
            try {
                if (text.trim().length() > 0) {
                    setText(bH.W.b(Double.parseDouble(text) / 16.387d, 1));
                }
            } catch (NumberFormatException e2) {
            }
            this.f10424a = false;
        }
    }
}
