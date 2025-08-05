package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/a.class */
class a implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BurnButtonTv f9849a;

    a(BurnButtonTv burnButtonTv) {
        this.f9849a = burnButtonTv;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9849a.burn();
    }
}
