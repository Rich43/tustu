package aP;

import bq.C1219a;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JMenuItem;
import r.C1811n;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/gJ.class */
class gJ extends C1219a {

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0308dx f3418c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    gJ(C0308dx c0308dx) {
        super(C1818g.b("Recent Vehicle Projects"));
        this.f3418c = c0308dx;
    }

    @Override // x.C1891a, javax.swing.JMenu
    public void setPopupMenuVisible(boolean z2) {
        if (z2) {
            j();
        } else {
            removeAll();
        }
        super.setPopupMenuVisible(z2);
    }

    private void j() {
        ArrayList arrayListA = new C1811n().a();
        if (arrayListA.size() > 0) {
            Iterator it = arrayListA.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String strSubstring = str.substring(str.lastIndexOf(File.separator) + 1);
                bA.e eVar = new bA.e();
                eVar.setText(strSubstring);
                eVar.setActionCommand(str);
                eVar.addActionListener(new gK(this));
                add((JMenuItem) eVar);
            }
        }
    }
}
