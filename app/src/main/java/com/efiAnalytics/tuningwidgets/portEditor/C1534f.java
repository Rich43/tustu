package com.efiAnalytics.tuningwidgets.portEditor;

import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.MenuContainer;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.tuningwidgets.portEditor.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/f.class */
class C1534f extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10557a;

    C1534f(OutputPortEditor outputPortEditor) {
        this.f10557a = outputPortEditor;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            MenuContainer component = getComponent(i2);
            if (component instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) component).close();
            }
        }
    }
}
