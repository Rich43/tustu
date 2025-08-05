package bL;

import com.efiAnalytics.ui.C1677fh;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* loaded from: TunerStudioMS.jar:bL/r.class */
class r implements TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f7198a;

    r(q qVar) {
        this.f7198a = qVar;
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        int iCeil = (int) Math.ceil(C1677fh.c(this.f7198a.f7187i));
        if (iCeil > this.f7198a.f7188a.a() && this.f7198a.f7188a.a(0L) != null) {
            this.f7198a.f7188a = new i(this.f7198a.f7188a.a(0L), iCeil + 1);
        }
        if (iCeil > this.f7198a.f7189b.a()) {
            double d2 = C1677fh.d(this.f7198a.f7186h);
            this.f7198a.f7189b = new g(iCeil + 1, d2);
        }
    }
}
