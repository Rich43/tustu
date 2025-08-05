package com.efiAnalytics.dialogs;

import com.efiAnalytics.ui.Cdo;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/dialogs/f.class */
class f implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f9905a;

    f(e eVar) {
        this.f9905a = eVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((Cdo) focusEvent.getSource()).selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
