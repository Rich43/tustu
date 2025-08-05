package ao;

import com.efiAnalytics.ui.C1578bp;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dT.class */
class dT extends C1578bp {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5540a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    dT(bP bPVar) {
        super("Recent Viewed Log Files");
        this.f5540a = bPVar;
    }

    @Override // javax.swing.JMenu
    public void setPopupMenuVisible(boolean z2) {
        if (z2) {
            a();
        } else {
            removeAll();
        }
        super.setPopupMenuVisible(z2);
    }

    private void a() {
        ArrayList arrayListA = this.f5540a.f5376u.a();
        if (arrayListA.size() > 0) {
            Iterator it = arrayListA.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String strSubstring = str.substring(str.lastIndexOf(File.separator) + 1);
                bA.e eVar = new bA.e();
                eVar.setText(strSubstring);
                eVar.setActionCommand(str);
                eVar.addActionListener(new dU(this));
                add((JMenuItem) eVar);
            }
        }
    }
}
