package com.efiAnalytics.ui;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/* renamed from: com.efiAnalytics.ui.bj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bj.class */
class C1572bj implements HyperlinkListener {

    /* renamed from: a, reason: collision with root package name */
    long f11004a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1571bi f11005b;

    C1572bj(C1571bi c1571bi) {
        this.f11005b = c1571bi;
    }

    @Override // javax.swing.event.HyperlinkListener
    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        if (!hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) || System.currentTimeMillis() - this.f11004a <= 2000) {
            return;
        }
        this.f11004a = System.currentTimeMillis();
        if (hyperlinkEvent.getURL().getProtocol().startsWith("http") || hyperlinkEvent.getDescription().startsWith("http:") || hyperlinkEvent.getDescription().startsWith("https:")) {
            aN.a(hyperlinkEvent.getURL().toString());
        }
    }
}
