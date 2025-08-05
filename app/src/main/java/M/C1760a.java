package m;

import ak.V;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* renamed from: m.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:m/a.class */
public class C1760a implements V {

    /* renamed from: a, reason: collision with root package name */
    final c f12906a = new c(this);

    /* renamed from: b, reason: collision with root package name */
    List f12907b = null;

    @Override // ak.V
    public List a(List list) {
        try {
            SwingUtilities.invokeAndWait(new b(this, list));
        } catch (InterruptedException e2) {
            Logger.getLogger(C1760a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (InvocationTargetException e3) {
            Logger.getLogger(C1760a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        List arrayList = this.f12907b != null ? this.f12907b : new ArrayList();
        this.f12907b = null;
        return arrayList;
    }
}
