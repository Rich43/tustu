package bt;

import bH.C1007o;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.MenuContainer;
import javax.swing.Action;

/* renamed from: bt.bi, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bi.class */
class RunnableC1327bi implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1324bf f9018a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1324bf f9019b;

    RunnableC1327bi(C1324bf c1324bf, C1324bf c1324bf2) {
        this.f9019b = c1324bf;
        this.f9018a = c1324bf2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public void run() {
        try {
            boolean zA = this.f9019b.a_() != null ? C1007o.a(this.f9019b.a_(), this.f9019b.f9010o) : true;
            this.f9019b.setEnabled(zA);
            if (!zA) {
                for (int i2 = 0; i2 < this.f9019b.getComponentCount(); i2++) {
                    this.f9019b.a(this.f9019b.getComponent(i2), zA);
                }
            } else if (this.f9019b.f9009n != null) {
                for (int i3 = 0; i3 < this.f9019b.getComponentCount(); i3++) {
                    Component component = this.f9019b.getComponent(i3);
                    if (component instanceof InterfaceC1349h) {
                        ((InterfaceC1349h) component).a();
                    } else {
                        this.f9019b.a(component, zA);
                    }
                }
            }
            if (this.f9019b.getLayout() instanceof CardLayout) {
                CardLayout cardLayout = (CardLayout) this.f9019b.getLayout();
                boolean z2 = false;
                int i4 = 0;
                while (true) {
                    if (i4 >= this.f9019b.getComponentCount()) {
                        break;
                    }
                    if (this.f9019b.getComponent(i4) instanceof C1324bf) {
                        C1324bf c1324bf = (C1324bf) this.f9019b.getComponent(i4);
                        if (C1007o.a(c1324bf.a_(), this.f9019b.f9010o) && c1324bf.a_() != null) {
                            cardLayout.show(this.f9018a, c1324bf.getName());
                            c1324bf.a();
                            z2 = true;
                            break;
                        }
                    }
                    i4++;
                }
                if (!z2) {
                    cardLayout.show(this.f9018a, Action.DEFAULT);
                }
            }
            for (int i5 = 0; i5 < this.f9019b.getComponentCount(); i5++) {
                Component component2 = this.f9019b.getComponent(i5);
                if (component2 instanceof InterfaceC1349h) {
                    ((InterfaceC1349h) component2).a();
                } else if (component2 instanceof C1324bf) {
                    ((C1324bf) component2).a();
                } else if (component2 instanceof C1303al) {
                    ((C1303al) component2).m();
                } else if (component2 instanceof C1300ai) {
                    ((C1300ai) component2).a();
                } else {
                    this.f9019b.a(component2, zA);
                }
            }
            for (int i6 = 0; i6 < this.f9019b.getComponentCount(); i6++) {
                MenuContainer component3 = this.f9019b.getComponent(i6);
                if (component3 instanceof bY) {
                    ((bY) component3).b();
                }
            }
        } catch (V.g e2) {
            bH.C.a("Failed to evaluate active condition for panel: " + ((Object) this.f9019b.f9009n));
        }
    }
}
