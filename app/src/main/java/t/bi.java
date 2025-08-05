package t;

import G.C0113cs;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent;
import com.efiAnalytics.ui.C1685fp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.UIManager;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/bi.class */
public class bi extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JComboBox f13839a;

    /* renamed from: b, reason: collision with root package name */
    JComboBox f13840b;

    /* renamed from: c, reason: collision with root package name */
    boolean f13841c;

    /* renamed from: d, reason: collision with root package name */
    private JPanel f13842d;

    public bi(Window window, C1836ai c1836ai) {
        super(window, C1818g.b("Select Output Channel"));
        this.f13839a = new JComboBox();
        this.f13840b = new JComboBox();
        this.f13841c = false;
        this.f13842d = new JPanel();
        a(c1836ai);
        this.f13842d.setLayout(new BorderLayout());
        add(this.f13842d, BorderLayout.CENTER);
        String ecuConfigurationName = c().c() ? c().b().getEcuConfigurationName() : "";
        G.T tA = G.T.a();
        G.R rA = a(ecuConfigurationName);
        rA = rA == null ? tA.c() : rA;
        String[] strArrD = tA.d();
        this.f13839a.setEditable(false);
        this.f13839a.addItem(new bm(this, C1818g.b(Action.DEFAULT), ""));
        for (String str : strArrD) {
            this.f13839a.addItem(str);
        }
        this.f13839a.addItem(new bm(this, C0113cs.f1154a, C0113cs.f1154a));
        this.f13839a.setSelectedItem(((AbstractC1420s) c().a().get(0)).getEcuConfigurationName());
        this.f13839a.addActionListener(new bj(this));
        this.f13840b.setEditable(false);
        if (ecuConfigurationName.equals(C0113cs.f1154a)) {
            d();
        } else {
            a(rA);
        }
        this.f13840b.addActionListener(new bk(this));
        JButton jButton = new JButton(C1818g.b("Close"));
        jButton.addActionListener(new bl(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 1));
        jPanel2.setBorder(BorderFactory.createTitledBorder(C1818g.b(FXMLLoader.CONTROLLER_SUFFIX)));
        jPanel2.add(this.f13839a);
        jPanel.add(jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 1));
        jPanel3.setBorder(BorderFactory.createTitledBorder(C1818g.b("Output Channel")));
        jPanel3.add(this.f13840b);
        jPanel.add(jPanel3);
        this.f13842d.add(BorderLayout.CENTER, jPanel);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(2));
        jPanel4.add(jButton);
        add("South", jPanel4);
        pack();
    }

    public void a(String str, String str2) {
        String strA;
        Object selectedItem = this.f13839a.getSelectedItem();
        String strA2 = this.f13839a.getSelectedItem() != null ? selectedItem instanceof bm ? ((bm) selectedItem).a() : selectedItem.toString() : null;
        for (int i2 = 0; i2 < this.f13839a.getItemCount(); i2++) {
            if ((this.f13839a.getItemAt(i2) instanceof bm) && (strA = ((bm) this.f13839a.getItemAt(i2)).a()) != null && strA.equals(str)) {
                this.f13839a.setSelectedIndex(i2);
            }
        }
        this.f13839a.setSelectedItem(str);
        if (str.equals(C0113cs.f1154a)) {
            d();
        } else {
            a(a(str));
        }
        if (str2 != null) {
            if (this.f13840b.getSelectedItem() == null || !this.f13840b.getSelectedItem().equals(str2)) {
                this.f13840b.setSelectedItem(str2);
            }
        }
    }

    public void e(ArrayList arrayList) {
        if (arrayList.isEmpty() || !(arrayList.get(0) instanceof SingleChannelDashComponent)) {
            C1685fp.a((Container) this.f13842d, false);
            return;
        }
        C1685fp.a((Container) this.f13842d, true);
        SingleChannelDashComponent singleChannelDashComponent = (SingleChannelDashComponent) arrayList.get(0);
        try {
            a(singleChannelDashComponent.getEcuConfigurationName(), singleChannelDashComponent.getOutputChannel());
        } catch (Exception e2) {
            a("", "");
        }
        String ecuConfigurationName = singleChannelDashComponent.getEcuConfigurationName();
        String outputChannel = singleChannelDashComponent.getOutputChannel();
        Color color = UIManager.getColor("Label.foreground");
        this.f13839a.setForeground(color);
        this.f13840b.setForeground(color);
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if ((abstractC1420s instanceof SingleChannelDashComponent) && !((SingleChannelDashComponent) abstractC1420s).getEcuConfigurationName().equals(ecuConfigurationName)) {
                this.f13839a.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            AbstractC1420s abstractC1420s2 = (AbstractC1420s) it2.next();
            if (abstractC1420s2 instanceof SingleChannelDashComponent) {
                SingleChannelDashComponent singleChannelDashComponent2 = (SingleChannelDashComponent) abstractC1420s2;
                if (singleChannelDashComponent2.getOutputChannel() != null && !singleChannelDashComponent2.getOutputChannel().equals(outputChannel)) {
                    this.f13840b.setForeground(Color.GRAY);
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        a(bH.W.a(C0113cs.a().b()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(G.R r2) {
        a(bH.W.a(r2.s()));
    }

    private void a(String[] strArr) {
        ActionListener[] actionListeners = this.f13840b.getActionListeners();
        for (ActionListener actionListener : actionListeners) {
            this.f13840b.removeActionListener(actionListener);
        }
        this.f13840b.removeAllItems();
        this.f13840b.addItem("");
        for (String str : strArr) {
            this.f13840b.addItem(str);
        }
        this.f13840b.setSelectedItem("");
        for (ActionListener actionListener2 : actionListeners) {
            this.f13840b.addActionListener(actionListener2);
        }
        if (c().c()) {
            this.f13840b.setSelectedItem(c().b().getOutputChannel());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public G.R a(String str) {
        return (str == null || str.equals("")) ? aE.a.A().E() : G.T.a().c(str);
    }

    public JPanel a() {
        return this.f13842d;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        this.f13841c = true;
        e(arrayList);
        this.f13841c = false;
    }
}
