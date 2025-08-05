package com.efiAnalytics.tunerStudio.panels;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ag.class */
class ag implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10062a;

    ag(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10062a = triggerLoggerPanel;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10062a.f9984h.doLayout();
    }
}
