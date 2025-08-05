package aY;

import com.efiAnalytics.ui.eJ;
import java.awt.Component;
import java.awt.Toolkit;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/* loaded from: TunerStudioMS.jar:aY/C.class */
class C extends DefaultListCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    Icon f4031a = null;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ s f4032b;

    C(s sVar) {
        this.f4032b = sVar;
    }

    @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
    public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
        Component listCellRendererComponent = super.getListCellRendererComponent(jList, obj, i2, z2, z3);
        if ((listCellRendererComponent instanceof JLabel) && (obj instanceof B)) {
            JLabel jLabel = (JLabel) listCellRendererComponent;
            jLabel.setIcon(getIcon());
        }
        return listCellRendererComponent;
    }

    @Override // javax.swing.JLabel
    public Icon getIcon() {
        if (this.f4031a == null) {
            this.f4031a = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("restoreIcon32.jpg")).getScaledInstance(eJ.a(18), eJ.a(18), 4));
        }
        return this.f4031a;
    }
}
