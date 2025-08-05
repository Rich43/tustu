package br;

import G.C0126i;
import com.efiAnalytics.ui.C1701s;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* renamed from: br.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/q.class */
class C1253q implements TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    String f8506a;

    /* renamed from: b, reason: collision with root package name */
    C1701s f8507b;

    /* renamed from: c, reason: collision with root package name */
    C1701s f8508c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C1250n f8509d;

    public C1253q(C1250n c1250n, String str, C1701s c1701s, C1701s c1701s2) {
        this.f8509d = c1250n;
        this.f8506a = null;
        this.f8507b = null;
        this.f8508c = null;
        this.f8506a = str;
        this.f8507b = c1701s;
        this.f8508c = c1701s2;
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        if (tableModelEvent.getFirstRow() == -1 || tableModelEvent.getColumn() == -1) {
            return;
        }
        try {
            this.f8507b.a((Object) Double.valueOf(bH.F.g(C0126i.c(bH.W.b(this.f8506a, "egoADC", ((int) Math.round((this.f8508c.e(tableModelEvent.getFirstRow(), tableModelEvent.getColumn()).doubleValue() * 255.0d) / 5.0d)) + ""), this.f8509d.f8483d))), tableModelEvent.getFirstRow(), tableModelEvent.getColumn());
            this.f8509d.f8482c.b();
        } catch (Exception e2) {
            bH.C.c("Error updating VE Analyze target AFR table");
            e2.printStackTrace();
        }
    }
}
