package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.Cdo;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/av.class */
class av extends Cdo {

    /* renamed from: a, reason: collision with root package name */
    boolean f10426a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1497an f10427b;

    av(C1497an c1497an) {
        this.f10427b = c1497an;
    }

    public void a() {
        if (this.f10426a) {
            return;
        }
        String text = getText();
        try {
            if (text.trim().length() > 0) {
                setText("" + ((int) Math.round(Double.parseDouble(text) * 10.5d)));
            }
        } catch (NumberFormatException e2) {
        }
        this.f10426a = true;
    }

    public void b() {
        if (this.f10426a) {
            String text = getText();
            try {
                if (text.trim().length() > 0) {
                    setText(bH.W.b(Double.parseDouble(text) / 10.5d, 1));
                }
            } catch (NumberFormatException e2) {
            }
            this.f10426a = false;
        }
    }
}
