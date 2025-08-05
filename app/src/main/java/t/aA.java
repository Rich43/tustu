package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.C1606cq;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.UIManager;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aA.class */
public class aA extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JComboBox f13715a;

    /* renamed from: b, reason: collision with root package name */
    JPanel f13716b;

    /* renamed from: c, reason: collision with root package name */
    String f13717c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f13718d;

    public aA(Window window, C1836ai c1836ai) {
        super(window, "Select Font Family");
        this.f13715a = new JComboBox();
        this.f13716b = new JPanel();
        this.f13717c = C1818g.b(Action.DEFAULT);
        this.f13718d = false;
        a(c1836ai);
        setLayout(new BorderLayout());
        add(this.f13716b, BorderLayout.CENTER);
        this.f13716b.setLayout(new BorderLayout());
        this.f13716b.setBorder(BorderFactory.createTitledBorder("Font Family"));
        this.f13715a.setEditable(false);
        this.f13715a.addItem(this.f13717c);
        for (String str : C1606cq.a().c()) {
            this.f13715a.addItem(str);
        }
        this.f13715a.setSelectedItem(((AbstractC1420s) c().a().get(0)).getFontFamily());
        this.f13715a.addActionListener(new aB(this));
        this.f13716b.add(BorderLayout.CENTER, this.f13715a);
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new aC(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        jPanel.add(jButton);
        add("South", jPanel);
        pack();
    }

    public void a(String str) {
        if (str == null || str.equals("")) {
            this.f13715a.setSelectedItem(this.f13717c);
        } else {
            this.f13715a.setSelectedItem(str);
        }
    }

    public void e(ArrayList arrayList) {
        if (arrayList.size() <= 0) {
            this.f13715a.setEnabled(false);
            return;
        }
        this.f13715a.setEnabled(true);
        AbstractC1420s abstractC1420s = (AbstractC1420s) arrayList.get(0);
        a(abstractC1420s.getFontFamily());
        this.f13715a.setForeground(UIManager.getColor("Label.foreground"));
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (!abstractC1420s.getFontFamily().equals(((AbstractC1420s) it.next()).getFontFamily())) {
                this.f13715a.setForeground(Color.GRAY);
                return;
            }
        }
    }

    public JPanel a() {
        return this.f13716b;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        this.f13718d = true;
        e(arrayList);
        this.f13718d = false;
    }
}
