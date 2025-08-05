package aq;

import W.C0184j;
import W.C0188n;
import ao.C0645bi;
import ao.C0804hg;
import bH.R;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/* renamed from: aq.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aq/a.class */
public class C0833a extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1662et f6199a;

    /* renamed from: b, reason: collision with root package name */
    public static String f6200b = "logViewerHiddenFieldList";

    /* renamed from: g, reason: collision with root package name */
    List f6205g;

    /* renamed from: h, reason: collision with root package name */
    List f6206h;

    /* renamed from: c, reason: collision with root package name */
    DefaultListModel f6201c = new DefaultListModel();

    /* renamed from: d, reason: collision with root package name */
    JList f6202d = new JList(this.f6201c);

    /* renamed from: e, reason: collision with root package name */
    DefaultListModel f6203e = new DefaultListModel();

    /* renamed from: f, reason: collision with root package name */
    JList f6204f = new JList(this.f6203e);

    /* renamed from: i, reason: collision with root package name */
    JLabel f6207i = new JLabel(" ");

    public C0833a(InterfaceC1662et interfaceC1662et) {
        this.f6205g = null;
        this.f6206h = null;
        this.f6199a = interfaceC1662et;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(eJ.a(10), eJ.a(10)));
        String strA = interfaceC1662et.a(f6200b);
        this.f6206h = a(strA == null ? "" : strA);
        this.f6205g = f();
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        JScrollPane jScrollPane = new JScrollPane(this.f6202d);
        jPanel2.setBorder(BorderFactory.createTitledBorder("Available Fields"));
        jScrollPane.setPreferredSize(eJ.a(150, 180));
        jPanel2.add(BorderLayout.CENTER, jScrollPane);
        jPanel2.add("West", new JLabel(""));
        jPanel.add("West", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        JScrollPane jScrollPane2 = new JScrollPane(this.f6204f);
        jPanel3.setBorder(BorderFactory.createTitledBorder("Displayed Fields"));
        jScrollPane2.setPreferredSize(eJ.a(150, 180));
        jPanel3.add(BorderLayout.CENTER, jScrollPane2);
        jPanel3.add("East", new JLabel(""));
        jPanel.add("East", jPanel3);
        JPanel jPanel4 = new JPanel();
        Dimension dimensionA = eJ.a(60, 25);
        jPanel4.setLayout(new GridLayout(0, 1, eJ.a(4), eJ.a(4)));
        jPanel4.add(new JLabel(" "));
        JButton jButton = new JButton(">>");
        jButton.setPreferredSize(dimensionA);
        jButton.setMnemonic(65);
        jButton.setToolTipText("Add All");
        jButton.addActionListener(new b(this));
        jPanel4.add(jButton);
        JButton jButton2 = new JButton(">");
        jButton2.setPreferredSize(dimensionA);
        jButton2.setMnemonic(160);
        jButton2.setToolTipText("Add selected fields");
        jButton2.addActionListener(new c(this));
        jPanel4.add(jButton2);
        JButton jButton3 = new JButton("<");
        jButton3.setPreferredSize(dimensionA);
        jButton3.setMnemonic(153);
        jButton3.setToolTipText("Remove selected fields");
        jButton3.addActionListener(new d(this));
        jPanel4.add(jButton3);
        JButton jButton4 = new JButton("<<");
        jButton4.setPreferredSize(dimensionA);
        jButton4.setToolTipText("Remove All");
        jButton4.setMnemonic(82);
        jButton4.addActionListener(new e(this));
        jPanel4.add(jButton4);
        jPanel.add(BorderLayout.CENTER, jPanel4);
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new GridLayout(0, 1));
        add("South", jPanel5);
        a();
    }

    private void a() {
        R.b(this.f6205g);
        for (String str : this.f6205g) {
            if (this.f6206h.contains(str)) {
                this.f6201c.addElement(str);
            } else {
                this.f6203e.addElement(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        List<String> selectedValuesList = this.f6202d.getSelectedValuesList();
        List selectedValuesList2 = this.f6204f.getSelectedValuesList();
        for (String str : selectedValuesList) {
            if (!selectedValuesList2.contains(str)) {
                this.f6203e.addElement(str);
            }
            this.f6201c.removeElement(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        List selectedValuesList = this.f6202d.getSelectedValuesList();
        for (String str : this.f6204f.getSelectedValuesList()) {
            if (!selectedValuesList.contains(str)) {
                this.f6201c.addElement(str);
            }
            this.f6203e.removeElement(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        List selectedValuesList = this.f6204f.getSelectedValuesList();
        for (String str : this.f6205g) {
            if (!selectedValuesList.contains(str)) {
                this.f6203e.addElement(str);
            }
        }
        this.f6201c.removeAllElements();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        List selectedValuesList = this.f6202d.getSelectedValuesList();
        for (String str : this.f6205g) {
            if (!selectedValuesList.contains(str)) {
                this.f6201c.addElement(str);
            }
        }
        this.f6203e.removeAllElements();
    }

    private List f() {
        ArrayList arrayList = new ArrayList();
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR != null) {
            Iterator it = c0188nR.iterator();
            while (it.hasNext()) {
                arrayList.add(((C0184j) it.next()).a());
            }
        }
        return arrayList;
    }

    public static List a(String str) {
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(stringTokenizer.nextToken().trim());
        }
        return arrayList;
    }

    private String a(List list) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < list.size(); i2++) {
            sb.append((String) list.get(i2));
            if (i2 < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g() {
        for (int i2 = 0; i2 < this.f6201c.getSize(); i2++) {
            String str = (String) this.f6201c.get(i2);
            if (!this.f6206h.contains(str)) {
                this.f6206h.add(str);
            }
        }
        for (int i3 = 0; i3 < this.f6203e.getSize(); i3++) {
            String str2 = (String) this.f6203e.get(i3);
            if (this.f6206h.contains(str2)) {
                this.f6206h.remove(str2);
            }
        }
        this.f6199a.a(f6200b, a(this.f6206h));
        C0645bi.a().d().c(new C0188n());
        C0645bi.a().d().c(C0804hg.a().r());
        C0645bi.a().e().t();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean h() {
        return true;
    }

    public void a(Frame frame) {
        JDialog jDialog = new JDialog(frame, "Select Displayed Fields");
        jDialog.setLayout(new BorderLayout());
        jDialog.add(BorderLayout.CENTER, this);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        JButton jButton = new JButton("Ok");
        jButton.addActionListener(new f(this, jDialog));
        jPanel.add(jButton);
        JButton jButton2 = new JButton("Cancel");
        jButton2.addActionListener(new g(this, jDialog));
        jPanel.add(jButton2);
        jDialog.add("South", jPanel);
        jDialog.pack();
        bV.a((Window) frame, (Component) jDialog);
        jDialog.setVisible(true);
    }
}
