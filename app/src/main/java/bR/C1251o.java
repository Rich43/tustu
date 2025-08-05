package br;

import com.efiAnalytics.ui.C1701s;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* renamed from: br.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/o.class */
class C1251o implements TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    C1701s f8502a;

    /* renamed from: b, reason: collision with root package name */
    C1701s f8503b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1250n f8504c;

    public C1251o(C1250n c1250n, C1701s c1701s, C1701s c1701s2) {
        this.f8504c = c1250n;
        this.f8502a = null;
        this.f8503b = null;
        this.f8502a = c1701s;
        this.f8503b = c1701s2;
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        if (this.f8502a.D() != null) {
            this.f8504c.f8482c.b();
        }
    }
}
