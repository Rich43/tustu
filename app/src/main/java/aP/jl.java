package aP;

import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:aP/jl.class */
class jl extends DefaultCellEditor {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iX f3797a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    jl(iX iXVar) {
        super(new JTextField());
        this.f3797a = iXVar;
        JTextField jTextField = (JTextField) getComponent();
        jTextField.setFont(iXVar.getFont());
        jTextField.setHorizontalAlignment(0);
        jTextField.addKeyListener(new jm(this, iXVar));
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
        ((JTextField) getComponent()).selectAll();
    }
}
