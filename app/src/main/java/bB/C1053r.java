package bb;

import com.efiAnalytics.ui.eJ;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.apache.commons.net.nntp.NNTPReply;

/* renamed from: bb.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/r.class */
class C1053r extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1051p f7810a;

    C1053r(C1051p c1051p) {
        this.f7810a = c1051p;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize.width < eJ.a(NNTPReply.AUTHENTICATION_REQUIRED)) {
            preferredSize.width = eJ.a(NNTPReply.AUTHENTICATION_REQUIRED);
        }
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        if (minimumSize.width < eJ.a(NNTPReply.AUTHENTICATION_REQUIRED)) {
            minimumSize.width = eJ.a(NNTPReply.AUTHENTICATION_REQUIRED);
        }
        return minimumSize;
    }
}
