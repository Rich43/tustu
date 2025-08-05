package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.ad, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ad.class */
final class C0613ad implements ActionListener {
    C0613ad() {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C0645bi.a().c().b(actionEvent.getActionCommand());
        C0645bi.a().c().i();
        C0645bi.a().c().repaint();
    }
}
