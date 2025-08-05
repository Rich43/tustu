package aP;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerListModel;
import javax.swing.table.TableModel;
import n.C1762b;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.net.nntp.NNTPReply;
import r.C1798a;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: TunerStudioMS.jar:aP/iX.class */
public class iX extends com.efiAnalytics.ui.dF implements com.efiAnalytics.ui.aO, FocusListener {

    /* renamed from: a, reason: collision with root package name */
    int f3694a;

    /* renamed from: b, reason: collision with root package name */
    int f3695b;

    /* renamed from: c, reason: collision with root package name */
    G.Y f3696c;

    /* renamed from: d, reason: collision with root package name */
    jk f3697d;

    /* renamed from: e, reason: collision with root package name */
    Vector f3698e;

    /* renamed from: f, reason: collision with root package name */
    Vector f3699f;

    /* renamed from: g, reason: collision with root package name */
    C0450je f3700g;

    /* renamed from: h, reason: collision with root package name */
    jk f3701h;

    /* renamed from: i, reason: collision with root package name */
    JTable f3702i;

    /* renamed from: j, reason: collision with root package name */
    JScrollPane f3703j;

    /* renamed from: k, reason: collision with root package name */
    int f3704k;

    /* renamed from: l, reason: collision with root package name */
    Color f3705l;

    /* renamed from: p, reason: collision with root package name */
    private boolean f3706p;

    /* renamed from: m, reason: collision with root package name */
    C0449jd f3707m;

    /* renamed from: n, reason: collision with root package name */
    String[] f3708n;

    public iX(Frame frame, G.Y y2) {
        super(frame, "Controller RAM Editor");
        this.f3694a = 1;
        this.f3695b = 16;
        this.f3696c = null;
        this.f3697d = new jk(this);
        this.f3698e = new Vector();
        this.f3699f = new Vector();
        this.f3700g = new C0450je(this, this.f3697d);
        this.f3701h = new jk(this);
        this.f3702i = new JTable(this.f3701h);
        this.f3704k = -1;
        this.f3705l = new Color(242, 242, 255);
        this.f3706p = false;
        this.f3707m = new C0449jd(this);
        this.f3708n = new String[]{"0x00", "0x01", "0x02", "0x03", "0x04", "0x05", "0x06", "0x07", "0x08", "0x09", "0x0A", "0x0B", "0x0C", "0x0D", "0x0E", "0x0F"};
        this.f3696c = y2;
        super.setDefaultCloseOperation(2);
        a(C1798a.a().c(C1798a.f13365aU, this.f3694a));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        Integer[] numArr = new Integer[y2.e()];
        for (int i2 = 0; i2 < numArr.length; i2++) {
            numArr[(numArr.length - i2) - 1] = new Integer(i2);
        }
        JSpinner jSpinner = new JSpinner(new SpinnerListModel(numArr));
        jSpinner.setValue(numArr[numArr.length - 1]);
        jSpinner.addChangeListener(new iY(this));
        jPanel.add(new JLabel("Page:"));
        jSpinner.setPreferredSize(com.efiAnalytics.ui.eJ.a(40, 20));
        jPanel.add(jSpinner);
        JPanel jPanel2 = new JPanel();
        ButtonGroup buttonGroup = new ButtonGroup();
        jPanel2.setLayout(new GridLayout(1, 0));
        JRadioButton jRadioButton = new JRadioButton("Bin", this.f3694a == 3);
        jRadioButton.addActionListener(new iZ(this));
        jPanel2.add(jRadioButton);
        buttonGroup.add(jRadioButton);
        JRadioButton jRadioButton2 = new JRadioButton("Dec", this.f3694a == 2);
        jRadioButton2.addActionListener(new C0446ja(this));
        jPanel2.add(jRadioButton2);
        buttonGroup.add(jRadioButton2);
        JRadioButton jRadioButton3 = new JRadioButton("Hex", this.f3694a == 1);
        jRadioButton3.addActionListener(new C0447jb(this));
        jPanel2.add(jRadioButton3);
        buttonGroup.add(jRadioButton3);
        jPanel.add(jPanel2);
        C1762b c1762b = new C1762b();
        c1762b.a(this);
        jPanel.add(c1762b);
        add("South", jPanel);
        for (String str : this.f3708n) {
            this.f3698e.add(str);
        }
        this.f3697d.setColumnIdentifiers(this.f3698e);
        this.f3697d.addTableModelListener(new jn(this));
        this.f3703j = new JScrollPane(this.f3700g);
        this.f3703j.setRowHeaderView(this.f3702i);
        this.f3703j.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, this.f3702i.getTableHeader());
        Vector vector = new Vector();
        vector.add(" ");
        this.f3701h.setColumnIdentifiers(vector);
        this.f3702i.getColumnModel().getColumn(0).setPreferredWidth(com.efiAnalytics.ui.eJ.a(50));
        this.f3702i.setPreferredScrollableViewportSize(this.f3702i.getPreferredSize());
        add(BorderLayout.CENTER, this.f3703j);
        b(0);
        setSize(720, NNTPReply.AUTHENTICATION_REQUIRED);
        com.efiAnalytics.ui.bV.a((Window) frame, (Component) this);
        y2.a(this.f3707m);
        this.f3700g.addMouseListener(new jo(this));
    }

    public void a(int i2) {
        if (i2 < 1 || i2 > 3) {
            return;
        }
        this.f3694a = i2;
        if (isVisible()) {
            b(this.f3704k);
        }
        C1798a.a().b(C1798a.f13365aU, this.f3694a + "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int b(String str) {
        return Integer.parseInt(bH.W.b(bH.W.b(str, "0x", ""), "0b", ""), this.f3694a == 1 ? 16 : this.f3694a == 3 ? 2 : 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(int i2, int i3) {
        if (i2 > 255) {
            i2 = 255;
        } else if (i2 < 0) {
            i2 = 0;
        }
        switch (i3) {
            case 1:
                return "0x" + bH.W.a(Integer.toHexString(i2).toUpperCase(), '0', 2);
            case 2:
                return Integer.toString(i2);
            case 3:
                return "0b" + bH.W.a(Integer.toBinaryString(i2).toUpperCase(), '0', 8);
            default:
                return "0x" + bH.W.a(Integer.toHexString(i2).toUpperCase(), '0', 2);
        }
    }

    public void b(int i2) {
        if (this.f3696c == null || i2 < 0 || i2 >= this.f3696c.e()) {
            com.efiAnalytics.ui.bV.d("Invalid Page: " + i2, this);
            return;
        }
        this.f3704k = i2;
        this.f3699f.clear();
        Vector vector = new Vector();
        int[] iArrB = this.f3696c.b(i2);
        int i3 = 0;
        while (i3 < iArrB.length) {
            Vector vector2 = new Vector();
            this.f3699f.add(vector2);
            Vector vector3 = new Vector();
            vector3.add(Constants.INDENT + bH.W.a(Integer.toHexString(i3).toUpperCase(), '0', 4));
            vector.add(vector3);
            for (int i4 = 0; i3 < iArrB.length && i4 < this.f3695b; i4++) {
                int i5 = i3;
                i3++;
                vector2.add(b(iArrB[i5], this.f3694a));
            }
        }
        Vector vector4 = new Vector();
        vector4.add(" ");
        this.f3701h.setDataVector(vector, vector4);
        this.f3697d.setDataVector(this.f3699f, this.f3698e);
    }

    public void c(int i2) {
        for (int i3 = 0; i3 < this.f3697d.getRowCount(); i3++) {
            for (int i4 = 0; i4 < this.f3697d.getColumnCount(); i4++) {
                if (this.f3700g.isCellSelected(i3, i4)) {
                    TableModel model = this.f3700g.getModel();
                    try {
                        if (((Vector) this.f3699f.get(i3)).get(i4) != null) {
                            model.setValueAt(b(i2 + b((String) ((Vector) this.f3699f.get(i3)).get(i4)), this.f3694a), i3, i4);
                        }
                    } catch (NumberFormatException e2) {
                        bH.C.c("Unable to parse: " + ((String) ((Vector) this.f3699f.get(i3)).get(i4)));
                    }
                }
            }
        }
        this.f3700g.repaint();
    }

    public void a(String str) {
        if (str != null && str.equals("Increment - Key: > or ,")) {
            a();
            return;
        }
        if (str != null && str.equals("Decrement - Key: < or .")) {
            g();
            return;
        }
        if (str != null && str.equals("Increase by - Key: +")) {
            j();
            return;
        }
        if (str != null && str.equals("Decrease by - Key: -")) {
            c();
            return;
        }
        if (str != null && str.equals("Scale by - Key: *")) {
            h();
        } else {
            if (str == null || !str.equals("Set to - Key: =")) {
                return;
            }
            b();
        }
    }

    public void a() {
        c(1);
    }

    public void b() {
        String strA = com.efiAnalytics.ui.bV.a(VectorFormat.DEFAULT_PREFIX + c("Set Selected Cells to") + ":}", true, c("Set Cell Values"), true, (Component) this);
        this.f3700g.requestFocus();
        if (strA == null || strA.equals("")) {
            return;
        }
        d(Integer.parseInt(strA));
    }

    public void d(int i2) {
        if (i2 < 0 || i2 > 255) {
            com.efiAnalytics.ui.bV.d("Invalid Value: " + i2, this);
            return;
        }
        for (int i3 = 0; i3 < this.f3700g.getColumnCount(); i3++) {
            for (int i4 = 0; i4 < this.f3700g.getRowCount(); i4++) {
                if (this.f3700g.isCellSelected(i4, i3)) {
                    try {
                        this.f3700g.getModel().setValueAt(b(i2, this.f3694a), i4, i3);
                    } catch (NumberFormatException e2) {
                        bH.C.c("Unable to parse: " + ((String) ((Vector) this.f3699f.get(i4)).get(i3)));
                    }
                }
            }
        }
        repaint();
    }

    public void c() {
        String strA = com.efiAnalytics.ui.bV.a(VectorFormat.DEFAULT_PREFIX + c("Decrease Selected Cells by") + ":}", true, c("Subtract From Cells"), true, (Component) this);
        this.f3700g.requestFocus();
        if (strA == null || strA.equals("")) {
            return;
        }
        c(-Integer.parseInt(strA));
    }

    public void g() {
        c(-1);
    }

    public void h() {
        String strA = com.efiAnalytics.ui.bV.a(VectorFormat.DEFAULT_PREFIX + c("Multiply Selected Cells by: ex. 1.2 = raise by 20%") + "}", true, c("Scale Cells"), true, (Component) this);
        this.f3700g.requestFocus();
        if (strA == null || strA.equals("")) {
            return;
        }
        e(Integer.parseInt(strA));
    }

    public void j() {
        String strA = com.efiAnalytics.ui.bV.a(VectorFormat.DEFAULT_PREFIX + c("Increase Selected Cells by") + ":}", true, c("Add To Cells"), true, (Component) this);
        this.f3700g.requestFocus();
        if (strA == null || strA.equals("")) {
            return;
        }
        c(Integer.parseInt(strA));
    }

    private String c(String str) {
        return C1818g.b(str);
    }

    public void e(int i2) {
        for (int i3 = 0; i3 < this.f3700g.getColumnCount(); i3++) {
            for (int i4 = 0; i4 < this.f3700g.getRowCount(); i4++) {
                if (this.f3700g.isCellSelected(i4, i3)) {
                    try {
                        this.f3700g.getModel().setValueAt(b(i2 * b((String) ((Vector) this.f3699f.get(i4)).get(i3)), this.f3694a), i4, i3);
                    } catch (NumberFormatException e2) {
                        bH.C.c("Unable to parse: " + ((String) ((Vector) this.f3699f.get(i4)).get(i3)));
                    }
                }
            }
        }
    }

    public void a(int i2, int i3) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        this.f3700g.add(jPopupMenu);
        C0448jc c0448jc = new C0448jc(this);
        jPopupMenu.add("Set to - Key: =").addActionListener(c0448jc);
        jPopupMenu.add("Increment - Key: > or ,").addActionListener(c0448jc);
        jPopupMenu.add("Decrement - Key: < or .").addActionListener(c0448jc);
        jPopupMenu.add("Increase by - Key: +").addActionListener(c0448jc);
        jPopupMenu.add("Decrease by - Key: -").addActionListener(c0448jc);
        jPopupMenu.add("Scale by - Key: *").addActionListener(c0448jc);
        jPopupMenu.show(this.f3700g, i2, i3);
    }

    @Override // com.efiAnalytics.ui.aO
    public void e() {
    }

    @Override // com.efiAnalytics.ui.aO
    public void d() {
    }

    @Override // com.efiAnalytics.ui.aO
    public void f() {
        G.T.a().c().I();
    }

    @Override // com.efiAnalytics.ui.aO
    public void i() {
        dispose();
    }

    @Override // java.awt.Window
    public void dispose() {
        this.f3696c.b(this.f3707m);
        super.dispose();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        bH.C.c(focusEvent.toString());
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
    }
}
