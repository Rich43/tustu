package bb;

import bH.aa;
import com.efiAnalytics.apps.ts.dashboard.HtmlDisplay;
import com.efiAnalytics.ui.fS;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:bb/u.class */
public class u extends JPanel implements fS {

    /* renamed from: a, reason: collision with root package name */
    aa f7819a;

    /* renamed from: b, reason: collision with root package name */
    private static String f7820b = "This Utility will install or update the firmware on your " + C1798a.f13272f + ". Please step through this wizard reading instructions carefully.";

    /* renamed from: c, reason: collision with root package name */
    private static String f7821c = "Click the Next Button to continue.";

    public u(aa aaVar) {
        this.f7819a = aaVar;
        setLayout(new BorderLayout());
        HtmlDisplay htmlDisplay = new HtmlDisplay();
        add(BorderLayout.CENTER, htmlDisplay);
        htmlDisplay.setText("<html>" + a(f7820b) + "<br><br><br>" + a(f7821c) + "</html>");
    }

    private String a(String str) {
        return this.f7819a != null ? this.f7819a.a(str) : str;
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return true;
    }
}
