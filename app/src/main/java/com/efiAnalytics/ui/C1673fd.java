package com.efiAnalytics.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

/* renamed from: com.efiAnalytics.ui.fd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fd.class */
class C1673fd implements WindowFocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1672fc f11665a;

    C1673fd(C1672fc c1672fc) {
        this.f11665a = c1672fc;
    }

    @Override // java.awt.event.WindowFocusListener
    public void windowGainedFocus(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowFocusListener
    public void windowLostFocus(WindowEvent windowEvent) {
        this.f11665a.dispose();
    }
}
