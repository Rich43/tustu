package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.Component;
import java.awt.Toolkit;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/n.class */
class n extends DefaultListCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    Icon f10577a = null;

    /* renamed from: b, reason: collision with root package name */
    Icon f10578b = null;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10579c;

    n(OutputPortEditor outputPortEditor) {
        this.f10579c = outputPortEditor;
    }

    @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
    public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
        Component listCellRendererComponent = super.getListCellRendererComponent(jList, obj, i2, z2, z3);
        if ((listCellRendererComponent instanceof JLabel) && (obj instanceof o)) {
            JLabel jLabel = (JLabel) listCellRendererComponent;
            if (((o) obj).b()) {
                jLabel.setIcon(a());
            } else {
                jLabel.setIcon(b());
            }
        }
        return listCellRendererComponent;
    }

    Icon a() {
        if (this.f10577a == null) {
            this.f10577a = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("greenBulb.png")));
        }
        return this.f10577a;
    }

    Icon b() {
        if (this.f10578b == null) {
            this.f10578b = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("redBulb.png")));
        }
        return this.f10578b;
    }
}
