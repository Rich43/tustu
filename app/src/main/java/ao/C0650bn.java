package ao;

import d.C1710b;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: ao.bn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bn.class */
class C0650bn implements HyperlinkListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0649bm f5422a;

    C0650bn(C0649bm c0649bm) {
        this.f5422a = c0649bm;
    }

    @Override // javax.swing.event.HyperlinkListener
    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        if (!hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) || System.currentTimeMillis() - 0 <= 2000) {
            return;
        }
        System.currentTimeMillis();
        if (hyperlinkEvent.getDescription().indexOf(CallSiteDescriptor.TOKEN_DELIMITER) == -1 || hyperlinkEvent.getDescription().startsWith("http:") || hyperlinkEvent.getDescription().startsWith("https:") || hyperlinkEvent.getDescription().startsWith("file:")) {
            if (hyperlinkEvent.getURL().getProtocol().startsWith("http")) {
                com.efiAnalytics.ui.aN.a(hyperlinkEvent.getURL().toString());
                return;
            }
            try {
                this.f5422a.f5419a.setPage(hyperlinkEvent.getURL());
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
            if (strSubstring2.indexOf("?") != -1) {
                strSubstring3 = strSubstring2.indexOf("?") != -1 ? description.substring(description.indexOf("?") + 1) : "";
                strSubstring2 = strSubstring2.substring(0, strSubstring2.indexOf("?"));
            }
            if (strSubstring.equals("appAction")) {
                d.g.a().a(strSubstring2, C1710b.a(strSubstring3));
            } else {
                com.efiAnalytics.ui.bV.d("Unsure how to handle link: " + description, this.f5422a.f5419a);
            }
        } catch (Exception e3) {
            com.efiAnalytics.ui.bV.d("Unsure how to handle this link:\n" + hyperlinkEvent.getDescription() + ", Message:\n" + e3.getMessage(), this.f5422a.f5419a);
        }
    }
}
