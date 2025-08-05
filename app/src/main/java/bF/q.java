package bF;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/* loaded from: TunerStudioMS.jar:bF/q.class */
class q implements ListSelectionListener, TableColumnModelListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0973d f6878a;

    q(C0973d c0973d) {
        this.f6878a = c0973d;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        this.f6878a.g();
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
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f6878a.g();
    }
}
