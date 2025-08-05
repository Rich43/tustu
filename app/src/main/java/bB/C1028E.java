package bb;

import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* renamed from: bb.E, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/E.class */
public class C1028E extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f7716a = new JTextPane();

    /* renamed from: b, reason: collision with root package name */
    JLabel f7717b = new JLabel("Select");

    /* renamed from: c, reason: collision with root package name */
    JLabel f7718c = new JLabel("", 0);

    /* renamed from: d, reason: collision with root package name */
    JLabel f7719d = new JLabel();

    /* renamed from: e, reason: collision with root package name */
    JTextField f7720e = new JTextField("", 30);

    /* renamed from: f, reason: collision with root package name */
    JCheckBox f7721f = new JCheckBox(C1818g.b("Other / Browse"));

    /* renamed from: k, reason: collision with root package name */
    private ae.k f7722k = null;

    /* renamed from: g, reason: collision with root package name */
    JPanel f7723g = new JPanel();

    /* renamed from: h, reason: collision with root package name */
    JList f7724h = new JList();

    /* renamed from: i, reason: collision with root package name */
    DefaultListModel f7725i = new DefaultListModel();

    /* renamed from: l, reason: collision with root package name */
    private ae.m f7726l = null;

    /* renamed from: j, reason: collision with root package name */
    ae.q f7727j = null;

    public C1028E() {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createTitledBorder(C1818g.b("Detected Hardware")));
        jPanel2.setLayout(new GridLayout(1, 1));
        jPanel2.add(this.f7718c);
        jPanel.add(jPanel2);
        jPanel.add(new JLabel(" "));
        jPanel.add(new JLabel(C1818g.b("Select your new firmware below:")));
        JPanel jPanel3 = new JPanel();
        jPanel3.setBorder(BorderFactory.createTitledBorder(C1818g.b("Recommended Firmware")));
        jPanel3.setLayout(new GridLayout(1, 1));
        jPanel3.add(this.f7717b);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(1, 1));
        jPanel4.setBorder(BorderFactory.createTitledBorder(C1818g.b("Available Firmwares")));
        JList jList = new JList(new String[]{"Firmware download Service Unavailable, Browse to local file"});
        jList.setVisibleRowCount(5);
        jList.setPreferredSize(eJ.a(200, 100));
        jList.setEnabled(false);
        jList.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f7719d.setPreferredSize(eJ.a(200, 60));
        jPanel4.add(this.f7719d);
        jPanel.add(jPanel4);
        jPanel.add(new JLabel(" "));
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add("North", new JLabel(C1818g.b("Browse to your downloaded Firmware package.")));
        if (0 != 0) {
            JButton jButton = new JButton(C1818g.b("Browse"));
            jButton.addActionListener(new C1029F(this));
            jPanel5.add("West", jButton);
        } else {
            jPanel5.add("West", this.f7721f);
            this.f7721f.addActionListener(new C1030G(this));
            this.f7720e.setEnabled(this.f7721f.isSelected());
        }
        jPanel5.add(BorderLayout.CENTER, this.f7720e);
        this.f7720e.setEditable(false);
        jPanel.add(jPanel5);
        this.f7723g.setBorder(BorderFactory.createTitledBorder("Firmware Files."));
        this.f7723g.setLayout(new BorderLayout());
        this.f7723g.add(this.f7724h, BorderLayout.CENTER);
        this.f7724h.setModel(this.f7725i);
        this.f7724h.setBorder(BorderFactory.createBevelBorder(1));
        this.f7724h.addListSelectionListener(new C1031H(this));
        jPanel.add(this.f7723g);
        this.f7723g.setVisible(false);
        jPanel.add(this.f7723g);
        add(BorderLayout.CENTER, jPanel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        String strB = bV.b(this, C1818g.b("Select Firmware File"), C1798a.a().a(C1798a.f13314V, "zip;mot;s19").split(";"), "", C1798a.a().c(C1798a.f13297E, C1807j.E()));
        if (strB == null || strB.equals("")) {
            this.f7721f.setSelected(false);
            return;
        }
        File file = new File(strB);
        JDialog jDialog = (JDialog) bV.b(this);
        jDialog.getGlassPane().setCursor(Cursor.getPredefinedCursor(3));
        jDialog.getGlassPane().setVisible(true);
        bH.C.c("Set WAIT Cursor");
        new C1032I(this, file, jDialog).start();
    }

    public ae.k a() {
        return this.f7722k;
    }

    public void a(ae.k kVar) {
        this.f7722k = null;
        this.f7725i.clear();
        List<File> listD = kVar.d();
        List listA = ae.r.a().a(kVar, this.f7726l);
        for (File file : listD) {
            this.f7725i.addElement(new C1034K(this, kVar, file, listA.size() > 0 && ((ae.q) listA.get(0)).b(this.f7726l, file)));
        }
        this.f7722k = kVar;
        if (listD.size() <= 1) {
            this.f7723g.setVisible(false);
            this.f7724h.setSelectedIndex(0);
            return;
        }
        this.f7723g.setVisible(true);
        boolean z2 = false;
        if (kVar.g() != null) {
            if (listA.size() > 0) {
                int i2 = 0;
                while (true) {
                    if (i2 >= listD.size()) {
                        break;
                    }
                    if (((ae.q) listA.get(0)).b(this.f7726l, (File) listD.get(i2))) {
                        z2 = true;
                        this.f7724h.setSelectedIndex(i2);
                        break;
                    }
                    i2++;
                }
            } else {
                int i3 = 0;
                while (true) {
                    if (i3 >= listD.size()) {
                        break;
                    }
                    if (((File) listD.get(i3)).equals(kVar.g())) {
                        z2 = true;
                        this.f7724h.setSelectedIndex(i3);
                        break;
                    }
                    i3++;
                }
            }
        } else if (listA.size() == 1) {
            int i4 = 0;
            while (true) {
                if (i4 >= listD.size()) {
                    break;
                }
                if (((ae.q) listA.get(0)).a(this.f7726l, (File) listD.get(i4))) {
                    ((ae.q) listA.get(0)).a((File) listD.get(i4));
                    this.f7724h.setSelectedIndex(i4);
                    z2 = true;
                    break;
                }
                i4++;
            }
            this.f7727j = (ae.q) listA.get(0);
        }
        if (this.f7727j == null || z2) {
            return;
        }
        for (int i5 = 0; i5 < this.f7725i.getSize(); i5++) {
            C1034K c1034k = (C1034K) this.f7725i.elementAt(i5);
            c1034k.f7737a = this.f7727j.b(this.f7726l, c1034k.a());
            if (c1034k.f7737a && !z2) {
                this.f7724h.setSelectedIndex(i5);
                z2 = true;
            }
        }
    }

    public void a(ae.m mVar) throws IllegalArgumentException {
        this.f7726l = mVar;
        List listA = ae.r.a().a(mVar);
        this.f7718c.setText("<html>" + C1818g.b("Found") + ": <b>" + mVar.a() + "</b>");
        if (listA.isEmpty()) {
            this.f7719d.setText("<html>" + C1818g.b("Loading firmware to this device is not supported by this application.") + ":<br>" + mVar.a());
            this.f7721f.setEnabled(false);
        } else {
            this.f7719d.setText("<html>" + C1818g.b("Download firmware compatible with the detected hardware") + ":<br><b>" + mVar.a() + "</b><br>" + C1818g.b("Then check the box below and browse to the download location."));
            this.f7721f.setEnabled(true);
        }
    }
}
