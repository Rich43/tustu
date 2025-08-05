package aP;

import com.efiAnalytics.ui.InterfaceC1598ci;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JMenuBar;

/* loaded from: TunerStudioMS.jar:aP/gL.class */
class gL extends JMenuBar implements bA.d {

    /* renamed from: a, reason: collision with root package name */
    HashMap f3420a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0308dx f3421b;

    gL(C0308dx c0308dx) {
        this.f3421b = c0308dx;
    }

    @Override // bA.d
    public void a(Component component) {
        remove(component);
    }

    @Override // bA.d
    public bA.f a(String str, int i2) {
        return (bA.f) super.getMenu(i2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (z2 && (components[i2] instanceof bA.f)) {
                bA.f fVar = (bA.f) components[i2];
                InterfaceC1598ci interfaceC1598ciC = fVar.c();
                try {
                    fVar.setEnabled(interfaceC1598ciC == null || interfaceC1598ciC.a(fVar.d()));
                } catch (Exception e2) {
                    bH.C.c("Error trying to call setEnabled on Root Menu");
                }
                try {
                    fVar.setVisible(fVar.i() == null || fVar.i().a());
                } catch (Exception e3) {
                }
            } else if (components[i2] != 0) {
                try {
                    components[i2].setEnabled(z2);
                } catch (Exception e4) {
                    bH.C.c("Error trying to call setEnabled, i=" + i2);
                    e4.printStackTrace();
                }
            }
        }
    }

    @Override // bA.d
    public bA.b a() {
        return new gM(this);
    }

    @Override // bA.d
    public int a(String str) {
        return super.getMenuCount();
    }

    @Override // bA.d
    public int b() {
        int size = 0;
        Iterator it = this.f3420a.values().iterator();
        while (it.hasNext()) {
            size += ((ArrayList) it.next()).size();
        }
        return size;
    }

    @Override // bA.d
    public Component a(String str, Component component, int i2) {
        if (this.f3420a.size() > 1) {
        }
        c(str).add(component);
        return super.add(component, i2);
    }

    private ArrayList c(String str) {
        ArrayList arrayList = (ArrayList) this.f3420a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f3420a.put(str, arrayList);
        }
        return arrayList;
    }

    @Override // bA.d
    public void b(String str) {
        this.f3420a.remove(str);
    }
}
