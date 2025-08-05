package aP;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/gB.class */
class gB extends JPanel implements bA.b {

    /* renamed from: a, reason: collision with root package name */
    JComboBox f3400a = new JComboBox();

    /* renamed from: b, reason: collision with root package name */
    ArrayList f3401b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0308dx f3402c;

    public gB(C0308dx c0308dx) {
        this.f3402c = c0308dx;
        setLayout(new BorderLayout());
        add("North", new JLabel(C1818g.b("Device")));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("North", this.f3400a);
        add(BorderLayout.CENTER, jPanel);
        this.f3400a.addActionListener(new gC(this, c0308dx));
    }

    public void a(String str) {
        this.f3400a.addItem(str);
    }

    @Override // bA.b
    public void a() {
        this.f3400a.removeAllItems();
    }

    @Override // bA.b
    public List b() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.f3400a.getItemCount(); i2++) {
            arrayList.add((String) this.f3400a.getItemAt(i2));
        }
        return arrayList;
    }

    @Override // bA.b
    public void b(String str) {
        this.f3400a.setSelectedItem(str);
    }

    public void a(bA.a aVar) {
        this.f3401b.add(aVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        Iterator it = this.f3401b.iterator();
        while (it.hasNext()) {
            ((bA.a) it.next()).a(str);
        }
    }

    public void c(String str) {
        this.f3400a.removeItem(str);
    }
}
