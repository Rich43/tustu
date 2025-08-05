package bt;

import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.eY;
import java.io.File;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:bt/bP.class */
class bP implements TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    C1701s f8933a;

    /* renamed from: b, reason: collision with root package name */
    String f8934b;

    /* renamed from: c, reason: collision with root package name */
    String f8935c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ bO f8936d;

    bP(bO bOVar, C1701s c1701s, String str, String str2) {
        this.f8936d = bOVar;
        this.f8933a = null;
        this.f8934b = null;
        this.f8935c = null;
        this.f8933a = c1701s;
        this.f8935c = str;
        this.f8934b = str2;
    }

    protected void a() {
        eY eYVar = new eY();
        File fileA = C1807j.a(this.f8935c, this.f8934b);
        try {
            eYVar.a(fileA.getAbsolutePath(), this.f8933a);
        } catch (V.a e2) {
            bH.C.a("Unable to save Lambda delay table to: " + fileA.getAbsolutePath() + "\nMessage: " + e2.getMessage());
        }
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        a();
    }
}
