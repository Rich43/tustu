package com.efiAnalytics.ui;

import java.util.EventObject;
import javax.swing.DefaultCellEditor;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aB.class */
class aB extends DefaultCellEditor {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10718a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    aB(BinTableView binTableView) {
        super(new Cdo());
        this.f10718a = binTableView;
        Cdo cdo = (Cdo) getComponent();
        cdo.setFont(binTableView.getFont());
        cdo.setHorizontalAlignment(0);
        cdo.addKeyListener(new aC(this, binTableView));
    }

    @Override // javax.swing.DefaultCellEditor, javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public boolean shouldSelectCell(EventObject eventObject) {
        boolean zShouldSelectCell = super.shouldSelectCell(eventObject);
        if (zShouldSelectCell) {
            a();
        }
        return zShouldSelectCell;
    }

    public void a() {
        Cdo cdo = (Cdo) getComponent();
        cdo.setText(bH.W.b(cdo.getText(), this.f10718a.f10624a));
        cdo.selectAll();
    }
}
