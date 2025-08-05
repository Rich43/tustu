package bD;

import bH.C1011s;
import bH.W;
import bH.aa;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/* loaded from: TunerStudioMS.jar:bD/I.class */
public class I extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    aa f6632a;

    /* renamed from: b, reason: collision with root package name */
    JPanel f6633b;

    /* renamed from: c, reason: collision with root package name */
    JButton f6634c;

    /* renamed from: d, reason: collision with root package name */
    JButton f6635d;

    /* renamed from: e, reason: collision with root package name */
    JCheckBox f6636e;

    /* renamed from: f, reason: collision with root package name */
    boolean f6637f;

    /* renamed from: g, reason: collision with root package name */
    List f6638g;

    public I(Window window, String str, String str2) {
        super(window, str, Dialog.ModalityType.DOCUMENT_MODAL);
        this.f6632a = bV.a();
        this.f6633b = new JPanel();
        this.f6637f = false;
        this.f6638g = null;
        setLayout(new BorderLayout());
        add("North", new JLabel(str2, 0));
        this.f6633b.setLayout(new GridLayout(0, 1));
        add(BorderLayout.CENTER, new JScrollPane(this.f6633b));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        this.f6635d = new JButton(a("Cancel"));
        this.f6634c = new JButton(a("Ok"));
        if (bH.I.a()) {
            jPanel.add(this.f6635d);
        }
        jPanel.add(this.f6634c);
        if (!bH.I.a()) {
            jPanel.add(this.f6635d);
        }
        this.f6635d.addActionListener(new J(this));
        this.f6634c.addActionListener(new K(this));
        add("South", jPanel);
        this.f6636e = new JCheckBox(a("Apply Name to all, followed by number.") + " " + a("Example: ZMax --> ZMax001, ZMax002, etc"));
        this.f6636e.setSelected(true);
        this.f6636e.addActionListener(new L(this));
    }

    public File[] a(File[] fileArr) {
        this.f6633b.removeAll();
        this.f6633b.add(this.f6636e);
        this.f6638g = new ArrayList();
        for (File file : fileArr) {
            N n2 = new N(this, file);
            this.f6638g.add(n2);
            this.f6633b.add(n2);
        }
        a(!this.f6636e.isSelected());
        ((N) this.f6638g.get(0)).f6644b.addKeyListener(new M(this));
        pack();
        int height = getHeight();
        if (height > eJ.a(500)) {
            height = eJ.a(500);
        }
        setSize(getWidth() + eJ.a(40), height);
        bV.a(super.getOwner(), (Component) this);
        ((N) this.f6638g.get(0)).f6644b.requestFocus();
        setVisible(true);
        if (this.f6637f) {
            return fileArr;
        }
        File[] fileArrA = a();
        for (int i2 = 0; i2 < fileArr.length; i2++) {
            File file2 = fileArr[i2];
            File file3 = fileArrA[i2];
            if (file2.exists() && !file2.renameTo(file3)) {
                bH.C.b("Failed to rename Log File: " + file2.getName() + " --> " + file3.getName());
                fileArrA[i2] = fileArr[i2];
            }
        }
        return fileArrA;
    }

    private File[] a() {
        File[] fileArr = new File[this.f6638g.size()];
        for (int i2 = 0; i2 < this.f6638g.size(); i2++) {
            fileArr[i2] = ((N) this.f6638g.get(i2)).a();
        }
        return fileArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        String strSubstring;
        if (this.f6638g != null) {
            String strB = ((N) this.f6638g.get(0)).b();
            if (strB.contains(".")) {
                strSubstring = strB.substring(strB.lastIndexOf(".") + 1);
                strB = strB.substring(0, strB.lastIndexOf("."));
            } else {
                strSubstring = "";
            }
            for (int i2 = 1; i2 < this.f6638g.size(); i2++) {
                ((N) this.f6638g.get(i2)).a(!strSubstring.isEmpty() ? strB + "_" + W.a("" + i2, '0', 3) + "." + strSubstring : strB + "_" + W.a("" + i2, '0', 3));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z2) {
        Component[] components = this.f6633b.getComponents();
        for (int i2 = 2; i2 < components.length; i2++) {
            C1685fp.a(components[i2], z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (e()) {
            this.f6637f = false;
            dispose();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        this.f6637f = true;
        dispose();
    }

    private boolean e() {
        ArrayList arrayList = new ArrayList();
        for (File file : a()) {
            if (!C1011s.a(file.getName())) {
                arrayList.add(file);
            }
        }
        if (arrayList.isEmpty()) {
            return true;
        }
        String str = a("The following file(s) names contain characters not allowed byt the file system") + ":\n";
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            str = str + ((File) it.next()).getName() + "\n";
        }
        bV.d(str, this);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        return this.f6632a != null ? this.f6632a.a(str) : str;
    }
}
