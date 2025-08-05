package u;

import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:u/e.class */
class e extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f13989a;

    e(d dVar) {
        this.f13989a = dVar;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        for (Object obj : getComponents()) {
            if (obj instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) obj).close();
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() throws HeadlessException {
        Dimension preferredSize = super.getPreferredSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (preferredSize.width > (screenSize.width * 17) / 20) {
            preferredSize.width = (screenSize.width * 17) / 20;
        }
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMaximumSize() {
        return super.getMaximumSize();
    }
}
