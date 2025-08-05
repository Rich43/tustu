package bl;

import com.efiAnalytics.ui.aN;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/* renamed from: bl.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/k.class */
class C1189k implements HyperlinkListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1186h f8254a;

    C1189k(C1186h c1186h) {
        this.f8254a = c1186h;
    }

    @Override // javax.swing.event.HyperlinkListener
    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        if (hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            aN.a(hyperlinkEvent.getURL().toString());
        }
    }
}
