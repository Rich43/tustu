package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.ui.bb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bb.class */
public class C1564bb implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1565bc f10985a;

    public C1564bb(InterfaceC1565bc interfaceC1565bc) {
        this.f10985a = null;
        this.f10985a = interfaceC1565bc;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10985a.close();
    }
}
