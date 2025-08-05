package bF;

import bH.W;
import com.efiAnalytics.ui.Cdo;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;

/* loaded from: TunerStudioMS.jar:bF/L.class */
class L extends DefaultCellEditor {

    /* renamed from: b, reason: collision with root package name */
    private int f6820b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f6821a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    L(D d2) {
        super(new Cdo());
        this.f6821a = d2;
        this.f6820b = 0;
        Cdo cdo = (Cdo) getComponent();
        cdo.setFont(d2.getFont());
        cdo.setHorizontalAlignment(0);
        cdo.addKeyListener(new M(this, d2));
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
        cdo.setText(W.b(cdo.getText(), ((y) this.f6821a.getModel()).a(this.f6820b, this.f6820b).c()));
        cdo.selectAll();
    }

    public void a(int i2) {
        this.f6820b = i2;
    }
}
