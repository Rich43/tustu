package ai;

import com.efiAnalytics.ui.aN;
import com.efiAnalytics.ui.bV;
import d.C1710b;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: ai.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ai/g.class */
class C0517g implements HyperlinkListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0516f f4521a;

    C0517g(C0516f c0516f) {
        this.f4521a = c0516f;
    }

    @Override // javax.swing.event.HyperlinkListener
    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        if (!hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) || System.currentTimeMillis() - 0 <= 2000) {
            return;
        }
        System.currentTimeMillis();
        if (!hyperlinkEvent.getDescription().contains(CallSiteDescriptor.TOKEN_DELIMITER) || hyperlinkEvent.getDescription().startsWith("http:") || hyperlinkEvent.getDescription().startsWith("https:") || hyperlinkEvent.getDescription().startsWith("file:")) {
            if (hyperlinkEvent.getURL().getProtocol().startsWith("http")) {
                aN.a(hyperlinkEvent.getURL().toString());
                return;
            }
            try {
                this.f4521a.f4515a.setPage(hyperlinkEvent.getURL());
                return;
            } catch (Exception e2) {
                System.out.println("Tried to load web help, but something went wrong.");
                e2.printStackTrace();
                return;
            }
        }
        String description = hyperlinkEvent.getDescription();
        try {
            String strSubstring = description.substring(0, description.indexOf(CallSiteDescriptor.TOKEN_DELIMITER));
            String strSubstring2 = description.substring(description.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1);
            String strSubstring3 = "";
            if (strSubstring2.contains("?")) {
                strSubstring3 = strSubstring2.contains("?") ? description.substring(description.indexOf("?") + 1) : "";
                strSubstring2 = strSubstring2.substring(0, strSubstring2.indexOf("?"));
            }
            if (strSubstring.equalsIgnoreCase("appAction")) {
                d.g.a().a(strSubstring2, C1710b.a(strSubstring3));
            } else {
                bV.d("Unsure how to handle link: " + description, this.f4521a.f4515a);
            }
        } catch (Exception e3) {
            bV.d("Unsure how to handle this link:\n" + hyperlinkEvent.getDescription() + ", Message:\n" + e3.getMessage(), this.f4521a.f4515a);
        }
    }
}
