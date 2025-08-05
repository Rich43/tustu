package aY;

import W.ag;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/* loaded from: TunerStudioMS.jar:aY/A.class */
class A extends JList {

    /* renamed from: a, reason: collision with root package name */
    DefaultListModel f4027a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ s f4028b;

    A(s sVar) {
        this.f4028b = sVar;
        this.f4027a = null;
        this.f4027a = new DefaultListModel();
        super.setCellRenderer(new C(sVar));
        super.setModel(this.f4027a);
        setSelectionMode(0);
        super.setVisibleRowCount(12);
    }

    public void a(ag agVar) {
        this.f4027a.add(0, new B(this.f4028b, agVar));
    }

    public ag a() {
        B b2 = (B) super.getSelectedValue();
        if (b2 != null) {
            return b2.f4029a;
        }
        return null;
    }
}
