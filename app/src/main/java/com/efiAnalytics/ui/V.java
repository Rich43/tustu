package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/V.class */
class V implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int[] f10707a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ T f10708b;

    V(T t2, int[] iArr) {
        this.f10708b = t2;
        this.f10707a = iArr;
    }

    /* JADX WARN: Type inference failed for: r0v25, types: [javax.swing.table.TableModel] */
    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        int length = this.f10707a.length - 1;
        double d2 = Double.parseDouble(this.f10708b.f10704c.getModel().getValueAt(this.f10708b.f10703b ? this.f10707a[0] : 0, this.f10708b.f10703b ? 0 : this.f10707a[0]).toString());
        double d3 = (Double.parseDouble(this.f10708b.f10704c.getModel().getValueAt(this.f10708b.f10703b ? this.f10707a[length] : 0, this.f10708b.f10703b ? 0 : this.f10707a[length]).toString()) - d2) / length;
        for (int i2 = 1; i2 < length; i2++) {
            ?? model = this.f10708b.f10704c.getModel();
            double d4 = d2 + d3;
            d2 = model;
            model.setValueAt(Double.valueOf(d4), this.f10708b.f10703b ? this.f10707a[i2] : 0, this.f10708b.f10703b ? 0 : this.f10707a[i2]);
        }
    }
}
