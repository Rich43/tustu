package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import G.InterfaceC0109co;
import bH.W;
import com.efiAnalytics.ui.C1677fh;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/i.class */
class i implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    int f9861a = -1;

    /* renamed from: b, reason: collision with root package name */
    Double f9862b = Double.valueOf(Double.NaN);

    /* renamed from: c, reason: collision with root package name */
    double f9863c = Double.NaN;

    /* renamed from: d, reason: collision with root package name */
    double f9864d = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ TableCellCrossHair f9865e;

    i(TableCellCrossHair tableCellCrossHair) {
        this.f9865e = tableCellCrossHair;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) throws NumberFormatException {
        if (str.equals(this.f9865e.f9835k)) {
            if (Double.isNaN(this.f9863c)) {
                this.f9863c = d2;
            } else {
                this.f9863c = ((this.f9863c * 3.0d) + d2) / 4.0d;
            }
            this.f9865e.setXValues(this.f9863c);
            this.f9861a = 1;
            return;
        }
        if (str.equals(this.f9865e.f9836l)) {
            if (Double.isNaN(this.f9864d)) {
                this.f9864d = d2;
            } else {
                this.f9864d = ((this.f9864d * 3.0d) + d2) / 4.0d;
            }
            this.f9865e.setYValues(this.f9864d);
            if (this.f9865e.f9839o >= 0 && this.f9865e.f9838n >= 0) {
                Double valueAt = this.f9865e.f9832i.getValueAt(this.f9865e.f9838n, this.f9865e.f9839o);
                if (valueAt.doubleValue() != this.f9862b.doubleValue()) {
                    this.f9865e.f9825c.a(W.b(valueAt.doubleValue(), this.f9865e.f9840p));
                    this.f9862b = valueAt;
                    this.f9865e.f9825c.a(C1677fh.a(valueAt.doubleValue(), this.f9865e.f9832i.A(), this.f9865e.f9832i.B()));
                }
            }
            if (this.f9861a == 1) {
                this.f9865e.f9837m.a();
            }
            this.f9861a = 2;
        }
    }
}
