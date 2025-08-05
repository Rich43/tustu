package com.efiAnalytics.ui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* renamed from: com.efiAnalytics.ui.ed, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ed.class */
class C1646ed implements TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1645ec f11577a;

    C1646ed(C1645ec c1645ec) {
        this.f11577a = c1645ec;
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        int column = tableModelEvent.getColumn();
        for (int firstRow = tableModelEvent.getFirstRow(); firstRow <= tableModelEvent.getLastRow(); firstRow++) {
            if (column >= 0 && firstRow >= 0) {
                this.f11577a.setValueAt(this.f11577a.f11573b.getValueAt(firstRow, column), firstRow, column);
            }
        }
    }
}
