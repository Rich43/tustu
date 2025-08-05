package bF;

import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.C1700r;
import com.efiAnalytics.ui.C1705w;
import com.efiAnalytics.ui.InterfaceC1648ef;
import com.efiAnalytics.ui.InterfaceC1657eo;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import s.C1818g;

/* renamed from: bF.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bF/d.class */
public class C0973d extends JPanel implements TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    D f6846a;

    /* renamed from: b, reason: collision with root package name */
    C0983n f6847b;

    /* renamed from: c, reason: collision with root package name */
    JTableHeader f6848c;

    /* renamed from: d, reason: collision with root package name */
    JPanel f6849d;

    /* renamed from: e, reason: collision with root package name */
    int f6850e;

    /* renamed from: f, reason: collision with root package name */
    JPanel f6851f;

    /* renamed from: p, reason: collision with root package name */
    private boolean f6852p;

    /* renamed from: q, reason: collision with root package name */
    private int f6853q;

    /* renamed from: g, reason: collision with root package name */
    JToolBar f6854g;

    /* renamed from: h, reason: collision with root package name */
    u f6855h;

    /* renamed from: i, reason: collision with root package name */
    JPanel f6856i;

    /* renamed from: j, reason: collision with root package name */
    JPanel f6857j;

    /* renamed from: k, reason: collision with root package name */
    JLabel f6858k;

    /* renamed from: r, reason: collision with root package name */
    private final ArrayList f6859r;

    /* renamed from: l, reason: collision with root package name */
    List f6860l;

    /* renamed from: m, reason: collision with root package name */
    s f6861m;

    /* renamed from: n, reason: collision with root package name */
    int f6862n;

    /* renamed from: o, reason: collision with root package name */
    int f6863o;

    public C0973d() {
        this(null);
    }

    public C0973d(y yVar) {
        this.f6846a = null;
        this.f6847b = null;
        this.f6848c = null;
        this.f6849d = null;
        this.f6850e = -1;
        this.f6851f = null;
        this.f6852p = true;
        this.f6853q = -1;
        this.f6855h = new u(this);
        this.f6856i = new JPanel();
        this.f6858k = new JLabel(" ");
        this.f6859r = new ArrayList();
        this.f6860l = new ArrayList();
        this.f6861m = null;
        this.f6862n = -1;
        this.f6863o = -1;
        setLayout(new BorderLayout());
        this.f6857j = new JPanel();
        add(BorderLayout.CENTER, this.f6857j);
        this.f6857j.setLayout(new BorderLayout(1, 1));
        this.f6849d = new JPanel();
        this.f6849d.setLayout(new BorderLayout());
        this.f6846a = new D(yVar);
        this.f6846a.a(C1818g.d());
        this.f6850e = this.f6846a.w() + eJ.a(3);
        this.f6846a.setDoubleBuffered(true);
        this.f6846a.setRowHeight(this.f6850e);
        this.f6849d.add(BorderLayout.CENTER, this.f6846a);
        this.f6848c = this.f6846a.createDefaultTableHeader();
        this.f6848c.setFont(new Font(this.f6846a.getFont().getFamily(), 0, this.f6846a.w() - 2));
        this.f6848c.setReorderingAllowed(false);
        this.f6848c.setBackground(UIManager.getColor("Label.background"));
        this.f6848c.setForeground(UIManager.getColor("Label.foreground"));
        this.f6848c.addMouseListener(new C0974e(this));
        this.f6846a.setTableHeader(this.f6848c);
        this.f6848c.setMinimumSize(new Dimension(10, this.f6850e));
        this.f6848c.setPreferredSize(new Dimension(10, this.f6850e));
        if (yVar.f()) {
            this.f6849d.add("North", this.f6848c);
        } else {
            this.f6849d.add("South", this.f6848c);
        }
        this.f6848c.setFocusable(false);
        this.f6857j.add(BorderLayout.CENTER, this.f6849d);
        if (yVar.f()) {
            JPanel jPanel = this.f6856i;
            JToolBar jToolBarF = f();
            this.f6854g = jToolBarF;
            jPanel.add(jToolBarF);
            this.f6856i.setLayout(this.f6855h);
            this.f6857j.add("East", this.f6856i);
        } else {
            JPanel jPanel2 = this.f6857j;
            JToolBar jToolBarF2 = f();
            this.f6854g = jToolBarF2;
            jPanel2.add("North", jToolBarF2);
        }
        String[] strArr = {" "};
        String[][] strArrD = this.f6846a.h().d();
        boolean z2 = false;
        for (int i2 = 0; i2 < strArrD.length && !z2; i2++) {
            for (int i3 = 0; i3 < strArrD[0].length && !z2; i3++) {
                if (strArrD[i2][i3] != null && strArrD[i2][i3].length() > 0) {
                    z2 = true;
                }
            }
        }
        if (z2) {
            this.f6847b = new C0983n(this, this.f6846a.h().d(), strArr);
            this.f6847b.setFont(this.f6846a.getFont());
            this.f6858k.setFont(this.f6846a.getFont());
            this.f6847b.setBackground(UIManager.getColor("Label.background"));
            this.f6847b.setForeground(UIManager.getColor("Label.foreground"));
            this.f6847b.setEnabled(false);
            this.f6847b.addMouseListener(new C0975f(this));
            this.f6847b.setPreferredSize(new Dimension(this.f6846a.g(), 0));
            this.f6847b.setRowHeight(this.f6850e);
            this.f6847b.setSelectionMode(0);
            this.f6847b.setDefaultRenderer(String.class, new C0985p(this));
            this.f6847b.setFocusable(false);
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new BorderLayout());
            jPanel3.add(BorderLayout.CENTER, this.f6847b);
            if (yVar.f()) {
                jPanel3.add("North", this.f6858k);
            } else {
                jPanel3.add("South", this.f6858k);
            }
            this.f6857j.add("West", jPanel3);
        }
        q qVar = new q(this);
        this.f6846a.getSelectionModel().addListSelectionListener(qVar);
        this.f6846a.getColumnModel().addColumnModelListener(qVar);
    }

    private JToolBar f() {
        int iA = eJ.a(20);
        this.f6854g = new JToolBar("Edit curve values");
        this.f6854g.setFloatable(false);
        this.f6854g.setLayout(new BorderLayout());
        this.f6851f = new JPanel();
        if (this.f6846a.h().f()) {
            this.f6851f.setLayout(new GridLayout(0, 1, eJ.a(3), eJ.a(3)));
            this.f6854g.add("North", this.f6851f);
            this.f6851f.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, eJ.a(5)));
        } else {
            this.f6851f.setLayout(new GridLayout(1, 0, eJ.a(3), eJ.a(3)));
            this.f6854g.add("East", this.f6851f);
        }
        JButton jButton = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(C1705w.class.getResource("resources/equal_sign_16.png")), this)));
        jButton.setFocusable(false);
        jButton.setToolTipText("Set to - Key: =");
        jButton.addActionListener(new C0976g(this));
        jButton.setPreferredSize(new Dimension(iA, iA));
        this.f6851f.add(jButton);
        JButton jButton2 = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(C1705w.class.getResource("resources/up_16.png")), this)));
        jButton2.setFocusable(false);
        jButton2.setToolTipText("Increment - Key: > or ,");
        jButton2.addActionListener(new C0977h(this));
        jButton2.setPreferredSize(new Dimension(iA, iA));
        this.f6851f.add(jButton2);
        this.f6859r.add(jButton2);
        JButton jButton3 = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(C1705w.class.getResource("resources/down_16.png")), this)));
        jButton3.setFocusable(false);
        jButton3.setToolTipText("Decrement - Key: < or .");
        jButton3.addActionListener(new C0978i(this));
        jButton3.setPreferredSize(new Dimension(iA, iA));
        this.f6851f.add(jButton3);
        this.f6859r.add(jButton3);
        JButton jButton4 = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(C1705w.class.getResource("resources/minus_sign_16.png")), this)));
        jButton4.setFocusable(false);
        jButton4.setToolTipText("Decrease by - Key: -");
        jButton4.addActionListener(new C0979j(this));
        jButton4.setPreferredSize(new Dimension(iA, iA));
        this.f6851f.add(jButton4);
        this.f6859r.add(jButton4);
        JButton jButton5 = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(C1705w.class.getResource("resources/plus_sign_16.png")), this)));
        jButton5.setFocusable(false);
        jButton5.setToolTipText("Increase by - Key: +");
        jButton5.addActionListener(new C0980k(this));
        jButton5.setPreferredSize(new Dimension(iA, iA));
        this.f6851f.add(jButton5);
        this.f6859r.add(jButton5);
        JButton jButton6 = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(C1705w.class.getResource("resources/times_sign_16.png")), this)));
        jButton6.setFocusable(false);
        jButton6.setToolTipText("Scale by - Key: *");
        jButton6.addActionListener(new C0981l(this));
        jButton6.setPreferredSize(new Dimension(iA, iA));
        this.f6851f.add(jButton6);
        this.f6859r.add(jButton6);
        r rVar = new r(this, null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(C1705w.class.getResource("resources/interpolate.png")), this)));
        rVar.setFocusable(false);
        rVar.setToolTipText("Interpolate - Key: /");
        rVar.addActionListener(new C0982m(this));
        rVar.setPreferredSize(new Dimension(iA, iA));
        this.f6851f.add(rVar);
        this.f6859r.add(rVar);
        return this.f6854g;
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f6846a.a(interfaceC1662et);
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        if (this.f6861m == null) {
            this.f6861m = new s(this);
            this.f6861m.start();
        }
        this.f6861m.a();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        try {
            super.paint(graphics);
        } catch (Exception e2) {
        }
    }

    public y a() {
        return this.f6846a.h();
    }

    private void h() {
    }

    public void b() {
        JTableHeader tableHeader = this.f6846a.getTableHeader();
        TableColumnModel columnModel = tableHeader.getColumnModel();
        for (int i2 = 0; i2 < columnModel.getColumnCount(); i2++) {
            columnModel.getColumn(i2).setHeaderValue(a().getColumnName(i2));
        }
        if (this.f6847b != null) {
            String[][] strArrD = this.f6846a.h().d();
            for (int i3 = 0; i3 < strArrD.length; i3++) {
                this.f6847b.getModel().setValueAt(strArrD[i3][0], i3, 0);
            }
        }
        tableHeader.repaint();
    }

    public void a(int i2) {
        this.f6855h.a(i2);
        this.f6856i.doLayout();
    }

    public void b(int i2) {
        a(false);
        int rowCount = (3 + i2) * (this.f6846a.getRowCount() + 1);
        int i3 = ((int) (i() * i2)) * (this.f6846a.getColumnCount() + 1);
        setMinimumSize(new Dimension(i3, rowCount));
        setPreferredSize(new Dimension(i3, rowCount));
        this.f6853q = i2;
        invalidate();
        doLayout();
    }

    public int c() {
        return this.f6853q;
    }

    private double i() {
        if (j() > 3 || this.f6846a.b() != 0) {
            return (j() > 4 || this.f6846a.b() != 0) ? 3.35d : 2.71d;
        }
        return 2.65d;
    }

    private int j() {
        double d2 = 0.0d;
        TableModel model = this.f6846a.getModel();
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();
        for (int i2 = 0; i2 < rowCount; i2++) {
            for (int i3 = 0; i3 < columnCount; i3++) {
                double dDoubleValue = ((Double) model.getValueAt(i2, i3)).doubleValue();
                if (Math.abs(dDoubleValue) > d2) {
                    d2 = dDoubleValue;
                }
            }
        }
        return ((int) Math.log10(d2)) + 1 + this.f6846a.b();
    }

    public Dimension c(int i2) {
        int i3 = (!d() || this.f6846a.h().f()) ? 0 : this.f6854g.getPreferredSize().height;
        return new Dimension((((int) (i() * i2)) * this.f6846a.getColumnCount()) + (this.f6847b != null ? this.f6847b.getPreferredSize().width : 0) + ((d() && this.f6846a.h().f()) ? this.f6854g.getPreferredSize().width : 0), ((eJ.a(3) + i2) * (this.f6846a.getRowCount() + 1)) + eJ.a(2) + i3);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        int i2 = (!d() || this.f6846a.h().f()) ? 0 : this.f6854g.getPreferredSize().height;
        int i3 = (d() && this.f6846a.h().f()) ? this.f6854g.getPreferredSize().width : 0;
        if (this.f6853q > 0 && !this.f6852p) {
            return c(this.f6853q);
        }
        int iW = ((3 + this.f6846a.w()) * (this.f6846a.getRowCount() + 1)) + 2 + i2;
        int i4 = (((int) (i() * getFont().getSize())) * (this.f6846a.getColumnCount() + 1)) + i3 + eJ.a(5);
        int i5 = i3 > i4 ? i3 : i4;
        if (this.f6846a.h().f()) {
            iW = Math.max(this.f6854g.getPreferredSize().height, iW);
        }
        return new Dimension(i5, iW);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public void a(boolean z2) {
        this.f6852p = z2;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        if (this.f6846a == null || i4 <= 0 || i5 <= 0) {
            super.setBounds(i2, i3, i4, i5);
            return;
        }
        if (Math.abs(i5 - this.f6862n) > 3 || this.f6863o != i4) {
            this.f6862n = i5;
            int i6 = (this.f6846a.h().f() || !this.f6854g.isVisible()) ? 0 : this.f6854g.getPreferredSize().height;
            int i7 = (this.f6846a.h().f() && this.f6854g.isVisible()) ? this.f6854g.getPreferredSize().width : 0;
            int i8 = this.f6847b != null ? this.f6847b.getPreferredSize().width : 0;
            if (this.f6847b != null) {
                this.f6847b.setRowHeight(this.f6850e);
                this.f6847b.setFont(this.f6846a.getFont());
                this.f6858k.setFont(this.f6846a.getFont());
                String[][] strArrD = this.f6846a.h().d();
                int i9 = 0;
                FontMetrics fontMetrics = getFontMetrics(this.f6847b.getFont());
                for (String[] strArr : strArrD) {
                    int iStringWidth = fontMetrics.stringWidth(strArr[0]);
                    if (iStringWidth > i9) {
                        i9 = iStringWidth;
                    }
                }
                i8 = i9 + 15;
                this.f6847b.getColumnModel().getColumn(0).setMaxWidth(i8);
                this.f6847b.getColumnModel().getColumn(0).setMinWidth(i9);
                this.f6847b.setMinimumSize(new Dimension(i8, 10));
                this.f6847b.setPreferredSize(new Dimension(i8, 10));
            }
            TableColumnModel columnModel = this.f6846a.getColumnModel();
            int rowCount = (((i5 - i6) - 2) / (this.f6846a.getModel().getRowCount() + 1)) - 4;
            int i10 = rowCount < 1 ? 1 : rowCount;
            int i11 = (i4 - i8) - i7;
            int iRound = (int) Math.round((i11 / this.f6846a.getModel().getColumnCount()) / i());
            int i12 = iRound < 1 ? 1 : iRound;
            int i13 = i12 < i10 ? i12 : i10;
            int iA = eJ.a(11);
            if (i13 < iA && this.f6846a.h().f()) {
                i13 = iA;
            }
            this.f6846a.b(i13);
            int iRound2 = Math.round(i11 / this.f6846a.getModel().getColumnCount());
            for (int i14 = 0; i14 < columnModel.getColumnCount(); i14++) {
                columnModel.getColumn(i14).setMaxWidth(iRound2);
            }
            this.f6850e = (((i5 - getInsets().top) - getInsets().bottom) - i6) / (this.f6846a.getModel().getRowCount() + 1);
            if (this.f6850e > 0) {
                this.f6846a.setRowHeight(this.f6850e);
                int rowCount2 = (((i5 - i6) - (this.f6850e * this.f6846a.getModel().getRowCount())) - getInsets().top) - getInsets().bottom;
                this.f6848c.setMinimumSize(new Dimension(10, rowCount2));
                this.f6848c.setPreferredSize(new Dimension(10, rowCount2));
            }
        }
        super.setBounds(i2, i3, i4, i5);
        h();
    }

    @Override // java.awt.Component
    public void setSize(Dimension dimension) {
        super.setSize(dimension.width, dimension.height);
    }

    public void a(double d2) {
        this.f6846a.a(d2);
    }

    public void a(InterfaceC1648ef[] interfaceC1648efArr) {
        int iB;
        int iB2;
        if (b(interfaceC1648efArr)) {
            return;
        }
        if (interfaceC1648efArr.length == 0) {
            this.f6846a.getSelectionModel().clearSelection();
            bH.C.c("Clearing selection. Vertical: " + this.f6846a.h().f());
        }
        int iMin = Integer.MAX_VALUE;
        int iMin2 = Integer.MAX_VALUE;
        int iMax = Integer.MIN_VALUE;
        int iMax2 = Integer.MIN_VALUE;
        for (InterfaceC1648ef interfaceC1648ef : interfaceC1648efArr) {
            int iA = this.f6846a.h().g() ? interfaceC1648ef.a() + 1 : interfaceC1648ef.a();
            if (this.f6846a.h().f()) {
                iB2 = iA;
                iB = interfaceC1648ef.b();
            } else {
                iB = iA;
                iB2 = interfaceC1648ef.b();
            }
            iMin = Math.min(iB, iMin);
            iMax = Math.max(iB, iMax);
            iMin2 = Math.min(iB2, iMin2);
            iMax2 = Math.max(iB2, iMax2);
        }
        if (interfaceC1648efArr.length > 0) {
            this.f6846a.changeSelection(iMin, iMin2, false, false);
        }
        if (interfaceC1648efArr.length > 1) {
            this.f6846a.changeSelection(iMax, iMax2, false, true);
        }
    }

    private boolean b(InterfaceC1648ef[] interfaceC1648efArr) {
        List listK = k();
        if (listK.size() != interfaceC1648efArr.length) {
            return false;
        }
        for (int i2 = 0; i2 < interfaceC1648efArr.length; i2++) {
            if (interfaceC1648efArr[i2].a() != ((InterfaceC1648ef) listK.get(i2)).a() || interfaceC1648efArr[i2].b() != ((InterfaceC1648ef) listK.get(i2)).b()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List k() {
        int[] selectedRows = this.f6846a.getSelectedRows();
        int[] selectedColumns = this.f6846a.getSelectedColumns();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < selectedRows.length; i2++) {
            for (int i3 = 0; i3 < selectedColumns.length; i3++) {
                C1700r c1700r = this.f6846a.h().f() ? new C1700r(selectedColumns[i3], selectedRows[i2]) : new C1700r(selectedRows[i2], selectedColumns[i3]);
                if ((!this.f6846a.h().g() || c1700r.a() != 0) && (!this.f6846a.h().h() || c1700r.a() != this.f6846a.h().a() - 1)) {
                    if (this.f6846a.h().g()) {
                        c1700r.a(c1700r.a() - 1);
                        arrayList.add(c1700r);
                    } else {
                        arrayList.add(c1700r);
                    }
                }
            }
        }
        return arrayList;
    }

    public void a(InterfaceC1657eo interfaceC1657eo) {
        if (this.f6860l.contains(interfaceC1657eo)) {
            return;
        }
        this.f6860l.add(interfaceC1657eo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(List list) {
        InterfaceC1648ef[] interfaceC1648efArr = (InterfaceC1648ef[]) list.toArray(new InterfaceC1648ef[list.size()]);
        Iterator it = this.f6860l.iterator();
        while (it.hasNext()) {
            ((InterfaceC1657eo) it.next()).a(interfaceC1648efArr);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.f6846a.setEnabled(z2);
        C1685fp.a((Container) this.f6854g, z2);
    }

    public void a(Color color) {
        this.f6846a.a(color);
    }

    public boolean d() {
        return this.f6846a.y();
    }

    public void b(boolean z2) {
        this.f6846a.a(z2);
        ListSelectionModel selectionModel = this.f6846a.getSelectionModel();
        if (z2) {
            selectionModel.setSelectionMode(2);
        } else if (a().f()) {
            selectionModel.setSelectionMode(0);
        } else {
            selectionModel.setSelectionMode(1);
        }
        Iterator it = this.f6859r.iterator();
        while (it.hasNext()) {
            Component component = (Component) it.next();
            if (component instanceof r) {
                ((r) component).setEnabled(isEnabled());
            }
        }
        if (!z2) {
            this.f6857j.remove(this.f6854g);
            this.f6854g.setVisible(false);
            return;
        }
        this.f6854g.setVisible(true);
        if (a().f()) {
            this.f6857j.add("East", this.f6856i);
        } else {
            this.f6857j.add("North", this.f6854g);
        }
    }

    public D e() {
        return this.f6846a;
    }
}
