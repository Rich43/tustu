package com.efiAnalytics.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/* renamed from: com.efiAnalytics.ui.ad, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ad.class */
class C1539ad implements Transferable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ StringBuilder f10824a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1538ac f10825b;

    C1539ad(C1538ac c1538ac, StringBuilder sb) {
        this.f10825b = c1538ac;
        this.f10824a = sb;
    }

    @Override // java.awt.datatransfer.Transferable
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.stringFlavor};
    }

    @Override // java.awt.datatransfer.Transferable
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return DataFlavor.stringFlavor.equals(dataFlavor);
    }

    @Override // java.awt.datatransfer.Transferable
    public Object getTransferData(DataFlavor dataFlavor) {
        if (DataFlavor.stringFlavor.equals(dataFlavor)) {
            return this.f10824a.toString();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
