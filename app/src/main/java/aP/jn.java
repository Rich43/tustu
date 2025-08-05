package aP;

import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* loaded from: TunerStudioMS.jar:aP/jn.class */
class jn implements TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iX f3800a;

    jn(iX iXVar) {
        this.f3800a = iXVar;
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        if (tableModelEvent.getType() == 0) {
            int column = tableModelEvent.getColumn();
            int firstRow = tableModelEvent.getFirstRow();
            if (firstRow < 0 || column < 0) {
                return;
            }
            try {
                this.f3800a.f3696c.a(this.f3800a.f3704k, (firstRow * this.f3800a.f3695b) + column, new int[]{this.f3800a.b((String) ((Vector) this.f3800a.f3699f.get(firstRow)).get(column))}, true, true);
            } catch (Exception e2) {
                bH.C.a("Bad number value: " + e2.getMessage());
            }
        }
    }
}
