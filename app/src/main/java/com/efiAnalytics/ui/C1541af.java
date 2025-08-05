package com.efiAnalytics.ui;

import javax.swing.DefaultListSelectionModel;

/* renamed from: com.efiAnalytics.ui.af, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/af.class */
class C1541af extends DefaultListSelectionModel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10828a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1540ae f10829b;

    C1541af(C1540ae c1540ae, C1705w c1705w) {
        this.f10829b = c1540ae;
        this.f10828a = c1705w;
    }

    @Override // javax.swing.DefaultListSelectionModel, javax.swing.ListSelectionModel
    public int getMinSelectionIndex() {
        return 1100000;
    }

    @Override // javax.swing.DefaultListSelectionModel, javax.swing.ListSelectionModel
    public int getMaxSelectionIndex() {
        return -1;
    }

    @Override // javax.swing.DefaultListSelectionModel, javax.swing.ListSelectionModel
    public boolean isSelectedIndex(int i2) {
        return false;
    }
}
