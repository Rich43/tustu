package com.efiAnalytics.tuningwidgets.portEditor;

/* renamed from: com.efiAnalytics.tuningwidgets.portEditor.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/c.class */
class RunnableC1531c implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10554a;

    RunnableC1531c(OutputPortEditor outputPortEditor) {
        this.f10554a = outputPortEditor;
    }

    @Override // java.lang.Runnable
    public void run() {
        o oVar = (o) this.f10554a.f10538v.a();
        if (oVar != null) {
            this.f10554a.a(oVar.a());
        }
    }
}
