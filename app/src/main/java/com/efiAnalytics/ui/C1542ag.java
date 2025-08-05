package com.efiAnalytics.ui;

import javax.swing.Icon;
import javax.swing.JButton;

/* renamed from: com.efiAnalytics.ui.ag, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ag.class */
class C1542ag extends JButton {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10830a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1542ag(C1705w c1705w, String str, Icon icon) {
        super(str, icon);
        this.f10830a = c1705w;
        super.setEnabled(false);
    }

    @Override // javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(false);
    }
}
