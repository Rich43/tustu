package o;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.ui.cY;
import d.C1710b;
import d.InterfaceC1711c;
import d.i;
import d.j;
import d.k;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.TransferHandler;

/* renamed from: o.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:o/a.class */
public class C1765a extends JMenu {

    /* renamed from: b, reason: collision with root package name */
    ArrayList f12947b;

    /* renamed from: a, reason: collision with root package name */
    List f12946a = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    InterfaceC1711c f12948c = new C1771g();

    public C1765a(String str, ArrayList arrayList) {
        super.setText(str);
        this.f12947b = arrayList;
        a();
    }

    private void a() {
        if (1 != 0) {
            C1769e c1769e = new C1769e(this, this.f12948c, null);
            c1769e.addActionListener(new C1768d(this, this.f12948c));
            add((JMenuItem) c1769e);
            addSeparator();
        }
        List<InterfaceC1711c> listA = d.g.a().a(new C1766b(this));
        Collections.sort(listA, new C1767c(this));
        for (InterfaceC1711c interfaceC1711c : listA) {
            if (interfaceC1711c instanceof cY) {
                if (interfaceC1711c.d()) {
                    Iterator it = ((cY) interfaceC1711c).a(new C1768d(this, interfaceC1711c)).iterator();
                    while (it.hasNext()) {
                        add((JMenuItem) it.next());
                    }
                }
            } else if (interfaceC1711c.f()) {
                C1769e c1769e2 = new C1769e(this, interfaceC1711c, interfaceC1711c.e());
                c1769e2.addActionListener(new C1768d(this, interfaceC1711c));
                add((JMenuItem) c1769e2);
            } else {
                C1769e c1769e3 = new C1769e(this, interfaceC1711c, null);
                c1769e3.addActionListener(new C1768d(this, interfaceC1711c));
                add((JMenuItem) c1769e3);
            }
        }
    }

    public String a(InterfaceC1711c interfaceC1711c) {
        String parameterValue;
        StringBuilder sb = new StringBuilder();
        sb.append(interfaceC1711c.a());
        k kVarE = interfaceC1711c.e();
        if (kVarE != null && !kVarE.isEmpty()) {
            sb.append("?");
            Iterator it = kVarE.iterator();
            while (it.hasNext()) {
                i iVar = (i) it.next();
                if (iVar.d() == null) {
                    Iterator it2 = this.f12947b.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            TransferHandler.HasGetTransferHandler hasGetTransferHandler = (AbstractC1420s) it2.next();
                            if ((hasGetTransferHandler instanceof j) && (parameterValue = ((j) hasGetTransferHandler).getParameterValue(iVar.c())) != null && !parameterValue.isEmpty()) {
                                iVar.b(parameterValue);
                                break;
                            }
                        }
                    }
                }
            }
            sb.append(C1710b.a(kVarE.a()));
        }
        return sb.toString();
    }

    public void a(InterfaceC1770f interfaceC1770f) {
        if (this.f12946a.contains(interfaceC1770f)) {
            return;
        }
        this.f12946a.add(interfaceC1770f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        Iterator it = this.f12946a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1770f) it.next()).a(str);
        }
    }
}
