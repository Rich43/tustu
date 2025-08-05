package com.efiAnalytics.tunerStudio.panels;

import W.aB;
import java.io.File;
import s.C1818g;

/* renamed from: com.efiAnalytics.tunerStudio.panels.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/s.class */
class C1470s extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aB f10141a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ File f10142b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ String f10143c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C1466o f10144d;

    C1470s(C1466o c1466o, aB aBVar, File file, String str) {
        this.f10144d = c1466o;
        this.f10141a = aBVar;
        this.f10142b = file;
        this.f10143c = str;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IllegalArgumentException {
        try {
            aB aBVar = this.f10141a;
            File file = this.f10142b;
            TriggerLoggerPanel triggerLoggerPanel = this.f10144d.f10137k;
            aBVar.a(file, TriggerLoggerPanel.f10025S + 1);
            this.f10144d.f10135i.a(this.f10143c);
            this.f10144d.f10136j.setSelected(false);
            this.f10144d.f10137k.f10000x.a(true);
        } catch (V.a e2) {
            bH.C.a(C1818g.b("Unable to load Ignition Log File."), e2, this);
        }
    }
}
