package bF;

import javax.swing.DefaultListSelectionModel;

/* renamed from: bF.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bF/o.class */
class C0984o extends DefaultListSelectionModel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0973d f6875a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0983n f6876b;

    C0984o(C0983n c0983n, C0973d c0973d) {
        this.f6876b = c0983n;
        this.f6875a = c0973d;
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
