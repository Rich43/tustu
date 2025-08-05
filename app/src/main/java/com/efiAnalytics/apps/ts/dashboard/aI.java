package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.bV;
import d.C1710b;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aI.class */
class aI implements HyperlinkListener {

    /* renamed from: a, reason: collision with root package name */
    long f9444a = 0;

    /* renamed from: b, reason: collision with root package name */
    int f9445b = 2000;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ HtmlDisplay f9446c;

    aI(HtmlDisplay htmlDisplay) {
        this.f9446c = htmlDisplay;
    }

    @Override // javax.swing.event.HyperlinkListener
    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        if (!hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) || System.currentTimeMillis() - this.f9444a <= this.f9445b) {
            return;
        }
        this.f9444a = System.currentTimeMillis();
        if (hyperlinkEvent.getDescription().indexOf(CallSiteDescriptor.TOKEN_DELIMITER) == -1 || hyperlinkEvent.getDescription().startsWith("http:") || hyperlinkEvent.getDescription().startsWith("https:") || hyperlinkEvent.getDescription().startsWith("file:")) {
            if ((this.f9446c.f9353d && hyperlinkEvent.getURL().getProtocol().startsWith("http")) || (this.f9446c.f9354f && (hyperlinkEvent.getDescription().startsWith("http:") || hyperlinkEvent.getDescription().startsWith("https:")))) {
                com.efiAnalytics.ui.aN.a(hyperlinkEvent.getURL().toString());
                return;
            }
            try {
                this.f9446c.f9350a.setPage(hyperlinkEvent.getURL());
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
                bV.d("Unsure how to handle link: " + description, this.f9446c.f9350a);
            }
        } catch (Exception e3) {
            bV.d("Unsure how to handle this link:\n" + hyperlinkEvent.getDescription() + ", Message:\n" + e3.getMessage(), this.f9446c.f9350a);
        }
    }
}
