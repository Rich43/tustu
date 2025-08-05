package ao;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/* renamed from: ao.gj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gj.class */
class C0780gj implements TableColumnModelListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5979a;

    C0780gj(fX fXVar) {
        this.f5979a = fXVar;
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnRemoved(TableColumnModelEvent tableColumnModelEvent) {
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnMarginChanged(ChangeEvent changeEvent) {
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnSelectionChanged(ListSelectionEvent listSelectionEvent) {
        this.f5979a.f5773O.a();
    }
}
