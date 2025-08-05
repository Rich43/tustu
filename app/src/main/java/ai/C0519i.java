package ai;

import com.efiAnalytics.ui.aN;
import com.efiAnalytics.ui.bV;
import java.io.FileNotFoundException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/* renamed from: ai.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ai/i.class */
class C0519i implements HyperlinkListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0516f f4523a;

    C0519i(C0516f c0516f) {
        this.f4523a = c0516f;
    }

    @Override // javax.swing.event.HyperlinkListener
    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        if (hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            if (hyperlinkEvent.getDescription().startsWith("app")) {
                this.f4523a.a(C0511a.a().a(hyperlinkEvent.getDescription().substring(4)).a());
            } else {
                if (hyperlinkEvent.getURL().getProtocol().startsWith("http")) {
                    aN.a(hyperlinkEvent.getURL().toString());
                    return;
                }
                try {
                    this.f4523a.f4515a.setPage(hyperlinkEvent.getURL());
                } catch (FileNotFoundException e2) {
                    System.out.println("Tried to load web help, File Not Found.");
                    bV.d("File Not Found:\n" + e2.getMessage(), this.f4523a.f4515a);
                    e2.printStackTrace();
                } catch (Exception e3) {
                    System.out.println("Tried to load web help, but something went wrong.");
                    e3.printStackTrace();
                }
            }
        }
    }
}
