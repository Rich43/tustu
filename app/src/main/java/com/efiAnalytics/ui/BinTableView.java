package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/BinTableView.class */
public class BinTableView extends JTable implements ClipboardOwner, HierarchyListener {

    /* renamed from: D, reason: collision with root package name */
    private int f10623D;

    /* renamed from: a, reason: collision with root package name */
    int f10624a;

    /* renamed from: b, reason: collision with root package name */
    int f10625b;

    /* renamed from: c, reason: collision with root package name */
    Font f10626c;

    /* renamed from: d, reason: collision with root package name */
    Font f10627d;

    /* renamed from: E, reason: collision with root package name */
    private boolean f10628E;

    /* renamed from: F, reason: collision with root package name */
    private aD f10629F;

    /* renamed from: G, reason: collision with root package name */
    private aB f10630G;

    /* renamed from: H, reason: collision with root package name */
    private int f10634H;

    /* renamed from: I, reason: collision with root package name */
    private double f10635I;

    /* renamed from: J, reason: collision with root package name */
    private double f10636J;

    /* renamed from: h, reason: collision with root package name */
    boolean f10637h;

    /* renamed from: i, reason: collision with root package name */
    double f10638i;

    /* renamed from: j, reason: collision with root package name */
    double f10639j;

    /* renamed from: k, reason: collision with root package name */
    Clipboard f10640k;

    /* renamed from: l, reason: collision with root package name */
    boolean f10641l;

    /* renamed from: K, reason: collision with root package name */
    private boolean f10642K;

    /* renamed from: m, reason: collision with root package name */
    aM[] f10643m;

    /* renamed from: L, reason: collision with root package name */
    private int f10644L;

    /* renamed from: M, reason: collision with root package name */
    private InterfaceC1662et f10645M;

    /* renamed from: N, reason: collision with root package name */
    private ArrayList f10647N;

    /* renamed from: O, reason: collision with root package name */
    private bH.aa f10648O;

    /* renamed from: P, reason: collision with root package name */
    private int f10649P;

    /* renamed from: Q, reason: collision with root package name */
    private double f10650Q;

    /* renamed from: R, reason: collision with root package name */
    private double f10651R;

    /* renamed from: S, reason: collision with root package name */
    private float f10652S;

    /* renamed from: o, reason: collision with root package name */
    Image f10653o;

    /* renamed from: p, reason: collision with root package name */
    boolean f10654p;

    /* renamed from: T, reason: collision with root package name */
    private Map f10655T;

    /* renamed from: W, reason: collision with root package name */
    private float f10658W;

    /* renamed from: X, reason: collision with root package name */
    private boolean f10659X;

    /* renamed from: Y, reason: collision with root package name */
    private boolean f10660Y;

    /* renamed from: Z, reason: collision with root package name */
    private boolean f10661Z;

    /* renamed from: aa, reason: collision with root package name */
    private boolean f10662aa;

    /* renamed from: ab, reason: collision with root package name */
    private boolean f10663ab;

    /* renamed from: q, reason: collision with root package name */
    ArrayList f10664q;

    /* renamed from: r, reason: collision with root package name */
    boolean f10665r;

    /* renamed from: ac, reason: collision with root package name */
    private fz f10666ac;

    /* renamed from: s, reason: collision with root package name */
    Stroke f10667s;

    /* renamed from: t, reason: collision with root package name */
    Stroke f10668t;

    /* renamed from: u, reason: collision with root package name */
    Color f10669u;

    /* renamed from: ad, reason: collision with root package name */
    private double f10670ad;

    /* renamed from: v, reason: collision with root package name */
    aK f10671v;

    /* renamed from: ae, reason: collision with root package name */
    private boolean f10672ae;

    /* renamed from: af, reason: collision with root package name */
    private InterfaceC1548am f10673af;

    /* renamed from: w, reason: collision with root package name */
    Cdo f10674w;

    /* renamed from: x, reason: collision with root package name */
    Cdo f10675x;

    /* renamed from: y, reason: collision with root package name */
    JPanel f10676y;

    /* renamed from: z, reason: collision with root package name */
    String f10677z;

    /* renamed from: A, reason: collision with root package name */
    long f10678A;

    /* renamed from: B, reason: collision with root package name */
    boolean f10679B;

    /* renamed from: C, reason: collision with root package name */
    boolean f10680C;

    /* renamed from: e, reason: collision with root package name */
    public static int f10631e = 0;

    /* renamed from: f, reason: collision with root package name */
    public static int f10632f = 1;

    /* renamed from: g, reason: collision with root package name */
    public static int f10633g = 2;

    /* renamed from: n, reason: collision with root package name */
    public static String f10646n = "tableInitialDir";

    /* renamed from: U, reason: collision with root package name */
    private static boolean f10656U = false;

    /* renamed from: V, reason: collision with root package name */
    private static boolean f10657V = true;

    public BinTableView(C1701s c1701s) {
        this();
        setModel(c1701s);
    }

    public BinTableView() {
        this.f10623D = 11;
        this.f10624a = 0;
        this.f10625b = 0;
        this.f10626c = new Font("Arial Unicode MS", 0, Q());
        this.f10627d = new Font("Arial Unicode MS", 1, Q());
        this.f10628E = false;
        this.f10629F = null;
        this.f10630G = null;
        this.f10634H = f10632f;
        this.f10635I = Double.NaN;
        this.f10636J = Double.NaN;
        this.f10637h = true;
        this.f10638i = -1.0d;
        this.f10639j = -1.0d;
        this.f10640k = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.f10641l = false;
        this.f10642K = true;
        this.f10643m = null;
        this.f10644L = 40;
        this.f10645M = null;
        this.f10647N = new ArrayList();
        this.f10648O = null;
        this.f10649P = 5;
        this.f10650Q = 0.05d;
        this.f10651R = Double.NaN;
        this.f10652S = Float.NaN;
        this.f10653o = null;
        this.f10654p = true;
        this.f10655T = new HashMap();
        this.f10658W = 0.7f;
        this.f10659X = true;
        this.f10660Y = true;
        this.f10661Z = false;
        this.f10662aa = false;
        this.f10663ab = false;
        this.f10664q = new ArrayList();
        this.f10665r = false;
        this.f10666ac = null;
        this.f10667s = new BasicStroke(2.0f);
        this.f10668t = new BasicStroke(1.0f);
        this.f10669u = new Color(0, 0, 255, 220);
        this.f10670ad = 10.0d;
        this.f10671v = new aK(this);
        this.f10672ae = false;
        this.f10673af = new aJ(this);
        this.f10674w = null;
        this.f10675x = null;
        this.f10676y = null;
        this.f10677z = "";
        this.f10678A = 0L;
        this.f10679B = false;
        this.f10680C = false;
        ListSelectionModel defaultListSelectionModel = new DefaultListSelectionModel();
        defaultListSelectionModel.setSelectionMode(2);
        setInputVerifier(new aI(this));
        setSelectionModel(defaultListSelectionModel);
        setFont(this.f10626c);
        setColumnSelectionAllowed(true);
        aL aLVar = new aL(this);
        addMouseListener(aLVar);
        addMouseWheelListener(aLVar);
        UIManager.put("ToolTip.background", new Color(240, 240, 240));
        ToolTipManager.sharedInstance().setDismissDelay(15000);
        if (f10656U) {
            return;
        }
        this.f10644L = 5;
    }

    public void a() throws NumberFormatException {
        String strD;
        if (I() && (strD = d("History Trace Length")) != null && !strD.equals("")) {
            c((int) Math.round(Double.parseDouble(strD)));
        }
        String strD2 = d("Smooth Cells - Key: s");
        if (strD2 != null && !strD2.equals("")) {
            a(Float.parseFloat(strD2));
        }
        String strD3 = d("Set increment amount");
        if (strD3 != null && !strD3.equals("")) {
            b(Float.parseFloat(strD3));
        }
        String strD4 = d("Set number of increments (CTRL pressed)");
        if (strD4 != null && !strD4.equals("")) {
            d((int) Float.parseFloat(strD4));
        }
        String strD5 = d("Set percent increment size (SHIFT pressed)");
        if (strD5 != null && !strD5.equals("")) {
            d(Double.parseDouble(strD5));
        }
        am();
        ah();
    }

    private String c(String str) {
        if (this.f10648O == null) {
            return str;
        }
        String str2 = (String) this.f10655T.get(str);
        if (str2 != null) {
            return str2;
        }
        String strA = this.f10648O.a(str);
        this.f10655T.put(str, strA);
        return strA;
    }

    @Override // javax.swing.JTable
    public boolean isCellEditable(int i2, int i3) {
        return getSelectedRows().length <= 1 && getSelectedColumns().length <= 1 && g();
    }

    public void a(int i2) {
        this.f10624a = i2;
    }

    public int b() {
        return this.f10624a;
    }

    public void b(int i2) {
        this.f10625b = i2;
    }

    public int c() {
        return this.f10625b;
    }

    public boolean d() {
        return this.f10641l;
    }

    public void e() {
        this.f10641l = false;
        if (getModel() instanceof C1701s) {
            ((C1701s) getModel()).q();
        }
    }

    @Override // java.awt.datatransfer.ClipboardOwner
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

    public void f() {
        String str = "";
        int[] selectedColumns = getSelectedColumns();
        int[] selectedRows = getSelectedRows();
        for (int i2 = selectedRows[0]; i2 < getRowCount() && i2 <= selectedRows[selectedRows.length - 1]; i2++) {
            for (int i3 = selectedColumns[0]; i3 < getColumnCount() && i3 <= selectedColumns[selectedColumns.length - 1]; i3++) {
                Double valueAt = ((C1701s) getModel()).getValueAt(i2, i3);
                str = valueAt != null ? str + bH.W.c(valueAt.doubleValue(), this.f10624a) + "\t" : str + "\t";
            }
            str = str + "\n";
        }
        this.f10640k.setContents(new StringSelection(str), this);
    }

    protected boolean g() {
        return T();
    }

    public void h() {
        if (g()) {
            D();
            C1701s c1701s = (C1701s) getModel();
            String strAd = ad();
            if (strAd == null) {
                return;
            }
            ak.aD aDVar = new ak.aD(strAd, "\n");
            for (int selectedRow = getSelectedRow(); selectedRow < c1701s.getRowCount() && aDVar.a(); selectedRow++) {
                ak.aD aDVar2 = new ak.aD(aDVar.b(), "\t");
                for (int selectedColumn = getSelectedColumn(); selectedColumn < c1701s.getColumnCount() && aDVar2.a(); selectedColumn++) {
                    String strB = aDVar2.b();
                    try {
                        if (bH.H.a(strB)) {
                            c1701s.setValueAt(new Double(strB), selectedRow, selectedColumn);
                        }
                    } catch (Exception e2) {
                        System.out.println("bad Double " + strB);
                        return;
                    }
                }
            }
        }
    }

    private void Z() {
        if (g()) {
            D();
            C1701s c1701s = (C1701s) getModel();
            String strAd = ad();
            if (strAd == null) {
                return;
            }
            ak.aD aDVar = new ak.aD(strAd, "\n");
            for (int selectedRow = getSelectedRow(); selectedRow < c1701s.getRowCount() && aDVar.a(); selectedRow++) {
                ak.aD aDVar2 = new ak.aD(aDVar.b(), "\t");
                for (int selectedColumn = getSelectedColumn(); selectedColumn < c1701s.getColumnCount() && aDVar2.a(); selectedColumn++) {
                    String strB = aDVar2.b();
                    try {
                        if (bH.H.a(strB)) {
                            Double valueAt = c1701s.getValueAt(selectedRow, selectedColumn);
                            double dDoubleValue = Double.valueOf(strB).doubleValue() / 100.0d;
                            if (dDoubleValue > -1.0d && dDoubleValue < 1.0d) {
                                dDoubleValue += 1.0d;
                            }
                            c1701s.setValueAt(Double.valueOf(valueAt.doubleValue() * dDoubleValue), selectedRow, selectedColumn);
                        }
                    } catch (Exception e2) {
                        bV.d("Bad Double " + strB, this);
                        System.out.println("bad Double " + strB);
                        return;
                    }
                }
            }
        }
    }

    private void aa() {
        if (g()) {
            D();
            C1701s c1701s = (C1701s) getModel();
            String strAd = ad();
            if (strAd == null) {
                return;
            }
            ak.aD aDVar = new ak.aD(strAd, "\n");
            for (int selectedRow = getSelectedRow(); selectedRow < c1701s.getRowCount() && aDVar.a(); selectedRow++) {
                ak.aD aDVar2 = new ak.aD(aDVar.b(), "\t");
                for (int selectedColumn = getSelectedColumn(); selectedColumn < c1701s.getColumnCount() && aDVar2.a(); selectedColumn++) {
                    String strB = aDVar2.b();
                    try {
                        if (bH.H.a(strB)) {
                            c1701s.setValueAt(Double.valueOf(c1701s.getValueAt(selectedRow, selectedColumn).doubleValue() * Double.valueOf(strB).doubleValue()), selectedRow, selectedColumn);
                        }
                    } catch (Exception e2) {
                        bV.d("Bad Double " + strB, this);
                        System.out.println("bad Double " + strB);
                        return;
                    }
                }
            }
        }
    }

    private void ab() {
        if (g()) {
            D();
            C1701s c1701s = (C1701s) getModel();
            String strAd = ad();
            if (strAd == null) {
                return;
            }
            ak.aD aDVar = new ak.aD(strAd, "\n");
            for (int selectedRow = getSelectedRow(); selectedRow < c1701s.getRowCount() && aDVar.a(); selectedRow++) {
                ak.aD aDVar2 = new ak.aD(aDVar.b(), "\t");
                for (int selectedColumn = getSelectedColumn(); selectedColumn < c1701s.getColumnCount() && aDVar2.a(); selectedColumn++) {
                    String strB = aDVar2.b();
                    try {
                        if (bH.H.a(strB)) {
                            c1701s.setValueAt(Double.valueOf(c1701s.getValueAt(selectedRow, selectedColumn).doubleValue() + Double.valueOf(strB).doubleValue()), selectedRow, selectedColumn);
                        }
                    } catch (Exception e2) {
                        bV.d("Bad Double " + strB, this);
                        System.out.println("bad Double " + strB);
                        return;
                    }
                }
            }
        }
    }

    private void ac() {
        if (g()) {
            D();
            C1701s c1701s = (C1701s) getModel();
            String strAd = ad();
            if (strAd == null) {
                return;
            }
            ak.aD aDVar = new ak.aD(strAd, "\n");
            for (int selectedRow = getSelectedRow(); selectedRow < c1701s.getRowCount() && aDVar.a(); selectedRow++) {
                ak.aD aDVar2 = new ak.aD(aDVar.b(), "\t");
                for (int selectedColumn = getSelectedColumn(); selectedColumn < c1701s.getColumnCount() && aDVar2.a(); selectedColumn++) {
                    String strB = aDVar2.b();
                    try {
                        if (bH.H.a(strB)) {
                            c1701s.setValueAt(Double.valueOf(c1701s.getValueAt(selectedRow, selectedColumn).doubleValue() - Double.valueOf(strB).doubleValue()), selectedRow, selectedColumn);
                        }
                    } catch (Exception e2) {
                        bV.d("Bad Double " + strB, this);
                        System.out.println("bad Double " + strB);
                        return;
                    }
                }
            }
        }
    }

    private String ad() {
        try {
            String string = this.f10640k.getData(DataFlavor.stringFlavor).toString();
            if (string == null || string.trim().isEmpty()) {
                bV.d("Clipboard data Empty", this);
                return null;
            }
            if (string.contains("\t")) {
                try {
                    Double.parseDouble(string.substring(0, string.indexOf("\t")));
                } catch (Exception e2) {
                    bV.d("Clipboard table data not numeric", this);
                    return null;
                }
            } else {
                try {
                    Double.parseDouble(string);
                } catch (Exception e3) {
                    bV.d("Clipboard data not numeric", this);
                    return null;
                }
            }
            return string;
        } catch (UnsupportedFlavorException | IOException e4) {
            bV.d("Clipboard data not a valid table copy", this);
            bH.C.a("Clipboard data not a valid table copy", e4);
            return null;
        }
    }

    @Override // javax.swing.JTable
    public TableCellEditor getCellEditor(int i2, int i3) {
        return super.getCellEditor(i2, i3);
    }

    @Override // javax.swing.JTable
    protected TableColumnModel createDefaultColumnModel() {
        TableColumnModel tableColumnModelCreateDefaultColumnModel = super.createDefaultColumnModel();
        tableColumnModelCreateDefaultColumnModel.addColumnModelListener(this);
        return tableColumnModelCreateDefaultColumnModel;
    }

    public int i() {
        return getWidth() / getColumnModel().getColumnCount();
    }

    protected int j() {
        return getFont().getSize() * 3;
    }

    @Override // javax.swing.JTable, javax.swing.event.TableColumnModelListener
    public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
        TableColumn column = ((TableColumnModel) tableColumnModelEvent.getSource()).getColumn(tableColumnModelEvent.getToIndex());
        column.setMaxWidth(j());
        column.setPreferredWidth(j());
        super.columnAdded(tableColumnModelEvent);
    }

    @Override // javax.swing.JTable
    public TableCellRenderer getCellRenderer(int i2, int i3) {
        if (this.f10661Z) {
            return R();
        }
        C1701s c1701s = (C1701s) getModel();
        C1562b[][] c1562bArrD = c1701s.D();
        if (!getFont().equals(this.f10626c)) {
            setFont(this.f10626c);
        }
        if (U() == f10632f && getModel().getValueAt(i2, i3) != null) {
            Double valueAt = c1701s.getValueAt(i2, i3);
            Color colorA = C1677fh.a(valueAt.doubleValue(), Double.isNaN(this.f10636J) ? c1701s.A() : this.f10636J, Double.isNaN(this.f10635I) ? c1701s.B() : this.f10635I);
            R().setBackground(colorA);
            R().setForeground(Color.black);
            if (c1562bArrD != null && R() != null) {
                String str = "";
                if (c1562bArrD[(c1562bArrD.length - i2) - 1][i3].m()) {
                    str = "<br>" + c("Cell is Locked from Analysis");
                    R().setBackground(Color.LIGHT_GRAY);
                }
                StringBuilder sb = new StringBuilder();
                if (this.f10660Y && c1562bArrD[(c1562bArrD.length - i2) - 1][i3].h() > 1.8d) {
                    sb.append("<html>").append(c("Begining Value")).append(": ").append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("<br> ").append(c("Target AFR")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].h())).append(str).append("</html>");
                } else if (this.f10660Y) {
                    sb.append("<html>").append(c("Begining Value")).append(": ").append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("<br> ").append(c("Lambda AFR")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.c(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].h(), 3)).append(str).append("</html>");
                } else {
                    sb.append("<html>").append(c("Begining Value")).append(": ").append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("</html>");
                }
                R().setToolTipText(sb.toString());
            } else if (c1701s.b(i2, i3) || R() == null) {
                R().setToolTipText(null);
            } else {
                try {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("<html>").append(c("Begining Value")).append(": ").append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("</html>");
                    R().setToolTipText(sb2.toString());
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println("Error getting clean value for table tooltip");
                }
            }
            boolean z2 = false;
            if (this.f10639j - i3 > -1.0d && this.f10639j - i3 < 1.0d && this.f10638i - i2 > -1.0d && this.f10638i - i2 < 1.0d) {
                double dAbs = this.f10639j - ((double) i3) >= 0.0d ? Math.abs((this.f10639j - i3) - 1.0d) : Math.abs((1.0d + this.f10639j) - i3);
                double dAbs2 = this.f10638i - ((double) i2) >= 0.0d ? Math.abs((this.f10638i - i2) - 1.0d) : Math.abs((1.0d + this.f10638i) - i2);
                double d2 = (dAbs == 0.0d || dAbs2 == 0.0d) ? 0.005d : dAbs * dAbs2;
                if (C1677fh.a()) {
                    colorA = new Color((int) (colorA.getRed() * (1.0d - d2)), (int) (colorA.getGreen() * (1.0d - d2)), (int) (255 + ((255 - 255) * d2)));
                } else {
                    int red = colorA.getRed() > 128 ? colorA.getRed() - 128 : 0;
                    int green = colorA.getGreen() > 128 ? colorA.getGreen() - 128 : 0;
                    int blue = colorA.getBlue() > 128 ? colorA.getBlue() - 128 : 0;
                    colorA = new Color((int) ((red < 128 ? red + 128 : 255) + ((255 - r29) * d2)), (int) ((green < 128 ? green + 128 : 255) + ((255 - r30) * d2)), (int) ((blue + 32 < 256 ? blue + 32 : 255) * (1.0d - d2)));
                }
                R().setBackground(colorA);
                if (C1677fh.a() && d2 >= 0.5d) {
                    R().setForeground(Color.WHITE);
                    z2 = true;
                }
                if (c1562bArrD != null) {
                    String str2 = c1562bArrD[(c1562bArrD.length - i2) - 1][i3].m() ? "<br>" + c("Cell is Locked from Analysis") : "";
                    if (this.f10660Y) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("<html>").append(c("Begining Value")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("<br> ").append(c("Target AFR")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].h())).append("<br> ").append(DecimalFormat.getPercentInstance().format(d2)).append(str2).append("</html>");
                        R().setToolTipText(sb3.toString());
                    } else {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("<html>").append(c("Begining Value")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("<br> ").append(DecimalFormat.getPercentInstance().format(d2)).append(str2).append("</html>");
                        R().setToolTipText(sb4.toString());
                    }
                } else if (c1701s.b(i2, i3)) {
                    R().setToolTipText(DecimalFormat.getPercentInstance().format(d2));
                } else {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("<html>").append(c("Begining Value")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(DecimalFormat.getPercentInstance().format(d2)).append("</html>");
                    R().setToolTipText(sb5.toString());
                }
            }
            if (!z2 && !c1701s.b(i2, i3)) {
                double dDoubleValue = c1701s.getValueAt(i2, i3).doubleValue() - c1701s.c(i2, i3).doubleValue();
                if (dDoubleValue >= 0.0d || Math.abs(dDoubleValue) <= P() * 0.5d) {
                    if (dDoubleValue > P() * 0.5d) {
                        R().setForeground(new Color(0, 0, 220));
                    }
                } else if (colorA.getRed() < 255 || colorA.getGreen() > 115) {
                    R().setForeground(new Color(225, 0, 0));
                } else {
                    R().setForeground(new Color(115, 0, 0));
                }
            } else if (!z2 && c1562bArrD != null && valueAt.doubleValue() != c1562bArrD[(c1562bArrD.length - i2) - 1][i3].a()) {
                double dDoubleValue2 = valueAt.doubleValue() - c1562bArrD[(c1562bArrD.length - i2) - 1][i3].a();
                if (dDoubleValue2 >= 0.0d || Math.abs(dDoubleValue2) <= P() * 0.5d) {
                    if (dDoubleValue2 > P() * 0.5d) {
                        R().setForeground(new Color(0, 0, 220));
                    }
                } else if (colorA.getRed() < 255 || colorA.getGreen() > 115) {
                    R().setForeground(new Color(225, 0, 0));
                } else {
                    R().setForeground(new Color(115, 0, 0));
                }
            }
        } else if (U() != f10633g || c1562bArrD == null || c1562bArrD[(c1562bArrD.length - i2) - 1][i3] == null) {
            R().setBackground(Color.white);
            R().setForeground(Color.black);
        } else {
            R().setBackground(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].k() == 0.0d ? Color.white : V().a(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].k()));
            R().setForeground(Color.black);
            if (c1562bArrD != null && R() != null) {
                String str3 = "";
                if (c1562bArrD[(c1562bArrD.length - i2) - 1][i3].m()) {
                    str3 = "<br>" + c("Cell is Locked from Analysis");
                    R().setBackground(Color.LIGHT_GRAY);
                }
                StringBuilder sb6 = new StringBuilder();
                if ((!this.f10660Y || c1562bArrD[(c1562bArrD.length - i2) - 1][i3].h() <= 1.8d) && this.f10660Y) {
                    sb6.append("<html>").append(c("Begining Value")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("<br> ").append(c("Lambda AFR")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.c(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].h(), 3)).append(str3).append("</html>");
                } else {
                    sb6.append("<html>").append(c("Begining Value")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("</html>");
                }
                R().setToolTipText(sb6.toString());
            } else if (c1701s.b(i2, i3) || R() == null) {
                R().setToolTipText(null);
            } else {
                try {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("<html>").append(c("Begining Value")).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append(" :").append("</html>");
                    R().setToolTipText(sb7.toString());
                } catch (Exception e3) {
                    e3.printStackTrace();
                    System.out.println("Error getting clean value for table tooltip");
                }
            }
            if (this.f10639j - i3 > -1.0d && this.f10639j - i3 < 1.0d && this.f10638i - i2 > -1.0d && this.f10638i - i2 < 1.0d) {
                double dAbs3 = this.f10639j - ((double) i3) >= 0.0d ? Math.abs((this.f10639j - i3) - 1.0d) : Math.abs((1.0d + this.f10639j) - i3);
                double dAbs4 = this.f10638i - ((double) i2) >= 0.0d ? Math.abs((this.f10638i - i2) - 1.0d) : Math.abs((1.0d + this.f10638i) - i2);
                double d3 = (dAbs3 == 0.0d || dAbs4 == 0.0d) ? 0.005d : dAbs3 * dAbs4;
                if (c1562bArrD != null) {
                    String str4 = c1562bArrD[(c1562bArrD.length - i2) - 1][i3].m() ? "<br>" + c("Cell is Locked from Analysis") : "";
                    if (this.f10660Y) {
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("<html>").append(c("Begining Value")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("<br> ").append(c("Target AFR")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].h())).append("<br> ").append(DecimalFormat.getPercentInstance().format(d3)).append(str4).append("</html>");
                        R().setToolTipText(sb8.toString());
                    } else {
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("<html>").append(c("Begining Value")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(c("Hit Count")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].j()).append("<br> ").append(c("Hit Weighting")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].l()).append("<br> ").append(DecimalFormat.getPercentInstance().format(d3)).append(str4).append("</html>");
                        R().setToolTipText(sb9.toString());
                    }
                } else if (c1701s.b(i2, i3)) {
                    R().setToolTipText(DecimalFormat.getPercentInstance().format(d3));
                } else {
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("<html>").append(c("Begining Value")).append(CallSiteDescriptor.TOKEN_DELIMITER).append(bH.W.b(c1701s.c(i2, i3).doubleValue())).append("<br> ").append(DecimalFormat.getPercentInstance().format(d3)).append("</html>");
                    R().setToolTipText(sb10.toString());
                }
            }
        }
        aD aDVarR = R();
        R();
        aDVarR.setHorizontalAlignment(0);
        return R();
    }

    @Override // javax.swing.JTable
    protected JTableHeader createDefaultTableHeader() {
        JTableHeader jTableHeaderCreateDefaultTableHeader = super.createDefaultTableHeader();
        jTableHeaderCreateDefaultTableHeader.setResizingAllowed(false);
        jTableHeaderCreateDefaultTableHeader.setAlignmentX(0.5f);
        aG aGVar = new aG(this, this);
        aGVar.setFont(new Font("Arial Unicode MS", 0, eJ.a(12)));
        jTableHeaderCreateDefaultTableHeader.setDefaultRenderer(aGVar);
        jTableHeaderCreateDefaultTableHeader.setForeground(Color.BLACK);
        return jTableHeaderCreateDefaultTableHeader;
    }

    private Image ae() {
        if (this.f10653o == null || this.f10653o.getWidth(null) != getWidth() || this.f10653o.getHeight(null) != getHeight()) {
            this.f10653o = createImage(getWidth(), getHeight());
        }
        return this.f10653o;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f10654p) {
            Image imageAe = ae();
            try {
                a(imageAe.getGraphics());
            } catch (Exception e2) {
            }
            graphics.drawImage(imageAe, 0, 0, null);
        } else {
            a(graphics);
        }
        if (isEnabled()) {
            return;
        }
        graphics.setColor(new Color(64, 64, 64, 100));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    public void a(Graphics graphics) {
        try {
            super.paint(graphics);
            if (!I() || this.f10643m == null) {
                return;
            }
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(this.f10669u);
            graphics2D.setStroke(this.f10667s);
            int i2 = i() - 6;
            int rowHeight = getRowHeight() - 4;
            graphics.drawOval(this.f10643m[0].f10748a - (i2 / 2), this.f10643m[0].f10749b - (rowHeight / 2), i2, rowHeight);
            graphics2D.setStroke(this.f10668t);
            graphics.fillOval(this.f10643m[0].f10748a - 2, this.f10643m[0].f10749b - 2, 4, 4);
            for (int i3 = 0; i3 < this.f10643m.length - 1; i3++) {
                if (this.f10643m[i3 + 1] != null) {
                    graphics.fillOval(this.f10643m[i3 + 1].f10748a - 2, this.f10643m[i3 + 1].f10749b - 2, 4, 4);
                    graphics.drawLine(this.f10643m[i3].f10748a, this.f10643m[i3].f10749b, this.f10643m[i3 + 1].f10748a, this.f10643m[i3 + 1].f10749b);
                } else {
                    bH.C.c("Skipped history " + i3);
                }
            }
        } catch (Exception e2) {
            bH.C.c("Bintableview paint error.");
        }
    }

    public void a(String[] strArr, String[] strArr2) {
        if (!I()) {
            this.f10643m = null;
            return;
        }
        String[] strArrA = ((C1701s) getModel()).a();
        String[] strArrB = ((C1701s) getModel()).b();
        boolean zH = ((C1701s) getModel()).H();
        this.f10643m = new aM[strArr.length];
        Rectangle cellRect = getCellRect(0, 0, true);
        for (int i2 = 0; i2 < strArr.length; i2++) {
            try {
                double dA = a(strArrB, Double.parseDouble(strArr[i2]));
                double dB = zH ? b(strArrA, Double.parseDouble(strArr2[i2])) : a(strArrA, Double.parseDouble(strArr2[i2]));
                this.f10643m[i2] = new aM(this, ((int) (cellRect.width * dA)) + (cellRect.width / 2), ((int) (cellRect.height * dB)) + (cellRect.height / 2), dA, dB);
            } catch (Exception e2) {
            }
        }
        if (!this.f10662aa || this.f10665r) {
            return;
        }
        a((float) this.f10638i, (float) this.f10639j);
    }

    public void a(String str, String str2) throws NumberFormatException {
        if (isEnabled()) {
            String[] strArrA = ((C1701s) getModel()).a();
            String[] strArrB = ((C1701s) getModel()).b();
            if (strArrB == null || strArrB.length == 0 || strArrB[0] == null || strArrA == null || strArrA.length == 0 || strArrA[0] == null) {
                return;
            }
            double d2 = Double.parseDouble(str);
            double d3 = Double.parseDouble(str2);
            boolean zH = ((C1701s) getModel()).H();
            this.f10638i = zH ? C1677fh.a(strArrA, d2) : C1677fh.b(strArrA, d2);
            this.f10639j = C1677fh.b(strArrB, d3);
            if (I()) {
                Rectangle cellRect = getCellRect(0, 0, true);
                double dA = a(strArrB, d3);
                double dA2 = zH ? C1677fh.a(strArrA, d2) : C1677fh.b(strArrA, d2);
                int iRound = (int) Math.round((cellRect.width * dA) + (cellRect.width / 2.0f));
                int i2 = ((int) (cellRect.height * dA2)) + (cellRect.height / 2);
                if (this.f10643m == null || this.f10643m.length < J() + 1) {
                    this.f10643m = new aM[J() + 1];
                    for (int i3 = 0; i3 < this.f10643m.length; i3++) {
                        this.f10643m[i3] = new aM(this, iRound, i2, d3, d2);
                    }
                }
                for (int length = this.f10643m.length - 2; length >= 0; length--) {
                    this.f10643m[length + 1] = this.f10643m[length];
                }
                this.f10643m[0] = new aM(this, iRound, i2, d3, d2);
            }
            if (this.f10637h) {
                this.f10671v.a();
                ar();
            }
            if (!this.f10662aa || this.f10665r) {
                return;
            }
            a((float) this.f10638i, (float) this.f10639j);
        }
    }

    public void a(float[] fArr, float[] fArr2) {
        if (!isEnabled() || fArr.length < 1 || fArr2.length < 1) {
            return;
        }
        String[] strArrA = ((C1701s) getModel()).a();
        String[] strArrB = ((C1701s) getModel()).b();
        if (strArrB == null || strArrB.length == 0 || strArrB[0] == null || strArrA == null || strArrA.length == 0 || strArrA[0] == null) {
            return;
        }
        double d2 = fArr[0];
        double d3 = fArr2[0];
        boolean zH = ((C1701s) getModel()).H();
        this.f10638i = zH ? C1677fh.a(strArrA, d2) : C1677fh.b(strArrA, d2);
        this.f10639j = C1677fh.b(strArrB, d3);
        if (this.f10662aa && !this.f10665r) {
            a((float) this.f10638i, (float) this.f10639j);
        }
        if (I()) {
            int iMin = Math.min(J() + 1, Math.min(fArr.length, fArr2.length));
            this.f10643m = new aM[iMin];
            Rectangle cellRect = getCellRect(0, 0, true);
            for (int i2 = 0; i2 < iMin; i2++) {
                this.f10643m[i2] = new aM(this, (int) Math.round((cellRect.width * a(strArrB, fArr2[i2])) + (cellRect.width / 2.0f)), ((int) (cellRect.height * (zH ? C1677fh.a(strArrA, fArr[i2]) : C1677fh.b(strArrA, fArr[i2])))) + (cellRect.height / 2), fArr2[i2], fArr[i2]);
            }
        }
        if (this.f10637h) {
            this.f10671v.a();
            ar();
        }
    }

    public void k() {
        if (isEnabled() && this.f10637h && isShowing() && this.f10643m != null) {
            String[] strArrA = ((C1701s) getModel()).a();
            String[] strArrB = ((C1701s) getModel()).b();
            Rectangle cellRect = getCellRect(0, 0, true);
            for (int i2 = 0; i2 < this.f10643m.length; i2++) {
                try {
                    double dA = a(strArrB, this.f10643m[i2].f10750c);
                    double dB = b(strArrA, this.f10643m[i2].f10751d);
                    int i3 = ((int) (cellRect.width * dA)) + (cellRect.width / 2);
                    int i4 = ((int) (cellRect.height * dB)) + (cellRect.height / 2);
                    this.f10643m[i2].f10748a = i3;
                    this.f10643m[i2].f10749b = i4;
                } catch (Exception e2) {
                    return;
                }
            }
            this.f10671v.a();
        }
    }

    public void l() {
        this.f10643m = null;
        repaint();
    }

    public void m() {
        this.f10671v.a();
    }

    public void n() {
        if (this.f10679B) {
            p();
        } else if (this.f10680C) {
            r();
        } else {
            a(aq());
        }
    }

    public void o() {
        if (this.f10679B) {
            q();
        } else if (this.f10680C) {
            s();
        } else {
            a(-aq());
        }
    }

    public void p() {
        a(aq() * L());
    }

    public void q() {
        a((-aq()) * L());
    }

    public void r() {
        c(1.0d + this.f10650Q);
    }

    public void s() {
        c(1.0d - this.f10650Q);
    }

    public void t() {
        if (f10656U) {
            if (g() || getSelectedColumn() < 0 || getSelectedRow() < 0) {
                D();
                C1701s c1701s = (C1701s) getModel();
                double dDoubleValue = c1701s.getValueAt(getSelectedRow(), getSelectedColumn()).doubleValue();
                for (int maxSelectionIndex = getSelectionModel().getMaxSelectionIndex(); maxSelectionIndex >= 0; maxSelectionIndex--) {
                    for (int selectedColumn = getSelectedColumn(); selectedColumn < c1701s.getColumnCount(); selectedColumn++) {
                        try {
                            c1701s.setValueAt(new Double(dDoubleValue), maxSelectionIndex, selectedColumn);
                        } catch (Exception e2) {
                            System.out.println("bad Double " + dDoubleValue);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void u() {
        if (g()) {
            D();
            this.f10637h = false;
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + c("Multiply Selected Cells by: ex. 1.2 = raise by 20%") + "}", true, c("Scale Cells"), true, (Component) this);
            this.f10637h = true;
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            c(Double.parseDouble(strA));
        }
    }

    public void v() {
        if (g()) {
            D();
            this.f10637h = false;
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + c("Increase Selected Cells by") + ":}", true, c("Add To Cells"), true, (Component) this);
            this.f10637h = true;
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            a(Double.parseDouble(strA));
        }
    }

    public void w() {
        if (f10656U && g()) {
            D();
            int[] selectedColumns = getSelectedColumns();
            int[] selectedRows = getSelectedRows();
            if (selectedColumns.length == 0 || selectedRows.length == 0) {
                return;
            }
            C1677fh.a((C1701s) getModel(), selectedColumns, selectedRows, M());
            repaint();
        }
    }

    public void x() {
        if (g()) {
            D();
            C1701s c1701s = new C1701s();
            c1701s.a(2, 2);
            int[] selectedColumns = getSelectedColumns();
            int[] selectedRows = getSelectedRows();
            if (selectedColumns.length == 0 || selectedRows.length == 0) {
                return;
            }
            C1701s c1701s2 = (C1701s) getModel();
            c1701s.setValueAt(c1701s2.getValueAt(selectedRows[0], selectedColumns[0]), 0, 0);
            c1701s.setValueAt(c1701s2.getValueAt(selectedRows[0], selectedColumns[selectedColumns.length - 1]), 0, 1);
            c1701s.setValueAt(c1701s2.getValueAt(selectedRows[selectedRows.length - 1], selectedColumns[0]), 1, 0);
            c1701s.setValueAt(c1701s2.getValueAt(selectedRows[selectedRows.length - 1], selectedColumns[selectedColumns.length - 1]), 1, 1);
            c1701s.c(new String[]{c1701s2.b()[selectedColumns[0]], c1701s2.b()[selectedColumns[selectedColumns.length - 1]]});
            c1701s.e(new String[]{c1701s2.a()[selectedRows[0]], c1701s2.a()[selectedRows[selectedRows.length - 1]]});
            for (int i2 = selectedColumns[0]; i2 <= selectedColumns[selectedColumns.length - 1]; i2++) {
                for (int i3 = selectedRows[0]; i3 <= selectedRows[selectedRows.length - 1]; i3++) {
                    c1701s2.setValueAt(new Double(C1677fh.a(c1701s, Double.parseDouble(c1701s2.b()[i2]), Double.parseDouble(c1701s2.a()[i3]))), i3, i2);
                }
            }
            repaint();
        }
    }

    public void y() {
        if (g()) {
            D();
            int[] selectedColumns = getSelectedColumns();
            int[] selectedRows = getSelectedRows();
            if (selectedColumns.length == 0 || selectedRows.length == 0) {
                return;
            }
            C1701s c1701s = (C1701s) getModel();
            for (int i2 = selectedRows[0]; i2 <= selectedRows[selectedRows.length - 1]; i2++) {
                double dDoubleValue = c1701s.getValueAt(i2, selectedColumns[0]).doubleValue();
                double dDoubleValue2 = c1701s.getValueAt(i2, selectedColumns[selectedColumns.length - 1]).doubleValue();
                C1701s c1701s2 = new C1701s();
                c1701s2.a(1, 2);
                c1701s2.setValueAt(Double.valueOf(dDoubleValue), 0, 0);
                c1701s2.setValueAt(Double.valueOf(dDoubleValue2), 0, 1);
                String[] strArrB = c1701s.b();
                c1701s2.c(new String[]{strArrB[selectedColumns[0]], strArrB[selectedColumns[selectedColumns.length - 1]]});
                c1701s2.d(new String[]{"1"});
                for (int i3 = selectedColumns[0]; i3 <= selectedColumns[selectedColumns.length - 1]; i3++) {
                    c1701s.setValueAt(new Double(C1677fh.a(c1701s2, Double.parseDouble(c1701s.b()[i3]), 0.0d)), i2, i3);
                }
            }
            repaint();
        }
    }

    public void z() {
        if (g()) {
            D();
            int[] selectedColumns = getSelectedColumns();
            int[] selectedRows = getSelectedRows();
            if (selectedColumns.length == 0 || selectedRows.length == 0) {
                return;
            }
            C1701s c1701s = (C1701s) getModel();
            for (int i2 = selectedColumns[0]; i2 <= selectedColumns[selectedColumns.length - 1]; i2++) {
                double dDoubleValue = c1701s.getValueAt(selectedRows[0], i2).doubleValue();
                double dDoubleValue2 = c1701s.getValueAt(selectedRows[selectedRows.length - 1], i2).doubleValue();
                C1701s c1701s2 = new C1701s();
                c1701s2.a(2, 1);
                c1701s2.setValueAt(Double.valueOf(dDoubleValue), 0, 0);
                c1701s2.setValueAt(Double.valueOf(dDoubleValue2), 1, 0);
                String[] strArrA = c1701s.a();
                c1701s2.d(new String[]{strArrA[selectedRows[selectedRows.length - 1]], strArrA[selectedRows[0]]});
                c1701s2.c(new String[]{"1"});
                for (int i3 = selectedRows[0]; i3 <= selectedRows[selectedRows.length - 1]; i3++) {
                    c1701s.setValueAt(new Double(C1677fh.a(c1701s2, 0.0d, Double.parseDouble(c1701s.a()[i3]))), i3, i2);
                }
            }
            repaint();
        }
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        if (getWidth() < 10 || getHeight() < 10) {
            l();
        }
        super.setBounds(i2, i3, i4, i5);
        SwingUtilities.invokeLater(new RunnableC1549an(this));
    }

    public void A() {
        C1562b[][] c1562bArrD = ((C1701s) getModel()).D();
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            for (int i3 = 0; i3 < getRowCount(); i3++) {
                if (isCellSelected(i3, i2) && c1562bArrD != null) {
                    C1701s c1701s = (C1701s) getModel();
                    Double dC = c1701s.c(i3, i2);
                    c1562bArrD[(c1562bArrD.length - 1) - i3][i2].b(c1562bArrD[(c1562bArrD.length - 1) - i3][i2].a());
                    c1701s.setValueAt(dC, i3, i2);
                }
            }
        }
    }

    public void B() {
        if (g()) {
            this.f10637h = false;
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + c("Decrease Selected Cells by") + ":}", true, c("Subtract From Cells"), true, (Component) this);
            this.f10637h = true;
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            a(-Double.parseDouble(strA));
        }
    }

    public void a(double d2) {
        C1701s c1701s;
        Double valueAt;
        if (g()) {
            D();
            for (int i2 = 0; i2 < getRowCount(); i2++) {
                for (int i3 = 0; i3 < getColumnCount(); i3++) {
                    if (isCellSelected(i2, i3) && (valueAt = (c1701s = (C1701s) getModel()).getValueAt(i2, i3)) != null && !valueAt.isNaN()) {
                        c1701s.setValueAt(new Double(a(valueAt.doubleValue(), d2)), i2, i3);
                    }
                }
            }
            O();
            repaint();
        }
    }

    public void C() {
        if (g()) {
            D();
            for (int i2 = 0; i2 < getRowCount(); i2++) {
                for (int i3 = 0; i3 < getColumnCount(); i3++) {
                    if (isCellSelected(i2, i3)) {
                        C1701s c1701s = (C1701s) getModel();
                        Double valueAt = c1701s.getValueAt(i2, i3);
                        Double dC = c1701s.c(i2, i3);
                        if (valueAt != null && !valueAt.isNaN()) {
                            c1701s.setValueAt(dC, i2, i3);
                        }
                    }
                }
            }
            repaint();
        }
    }

    private double a(double d2, double d3) {
        return Math.round((float) (((long) ((d2 + d3) * 1000000.0d)) / 100.0d)) / 10000.0d;
    }

    public void b(double d2) {
        if (g()) {
            D();
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                for (int i3 = 0; i3 < getRowCount(); i3++) {
                    if (isCellSelected(i3, i2)) {
                        C1701s c1701s = (C1701s) getModel();
                        c1701s.c(true);
                        if (c1701s.getValueAt(i3, i2) != null) {
                            c1701s.setValueAt(new Double(d2), i3, i2);
                        }
                    }
                }
            }
            repaint();
        }
    }

    public void c(double d2) {
        if (g()) {
            D();
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                for (int i3 = 0; i3 < getRowCount(); i3++) {
                    if (isCellSelected(i3, i2)) {
                        C1701s c1701s = (C1701s) getModel();
                        c1701s.setValueAt(Double.valueOf(c1701s.getValueAt(i3, i2).doubleValue() * d2), i3, i2);
                    }
                }
            }
        }
    }

    public void D() {
        C1701s c1701s = (C1701s) getModel();
        if (!c1701s.p()) {
            bV.d("This is a Read Only table. \nIf this table is edited, the new values will be used for analysis, \nbut will not be saved with Tune File.", this);
            c1701s.a(true);
        }
        this.f10641l = true;
    }

    private boolean af() {
        return getSelectedRowCount() > 1 || getSelectedColumnCount() > 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:147:0x02e9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 955
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.efiAnalytics.ui.BinTableView.a(java.lang.String):void");
    }

    public void E() {
        b("");
    }

    public void b(String str) {
        String strA = bV.a(this, "Export Table", new String[]{"table", "vex"}, str + "_" + bH.W.a() + ".table", d(f10646n));
        if (strA == null || strA.equals("")) {
            return;
        }
        String lowerCase = strA.toLowerCase();
        if (lowerCase.endsWith(".vex")) {
            try {
                new fF().a((C1701s) getModel(), strA, this.f10624a);
                return;
            } catch (V.a e2) {
                bV.d("Error Saving VEX file:\n" + e2.getMessage(), this);
                e2.printStackTrace();
                return;
            }
        }
        if (lowerCase.indexOf(".table") == -1) {
            strA = strA + ".table";
        }
        try {
            new eY().a(strA, (C1701s) getModel());
        } catch (V.a e3) {
            bV.d("Error Saving table file:\n" + e3.getMessage(), this);
            e3.printStackTrace();
        }
    }

    public void F() {
        String strA = bV.a(VectorFormat.DEFAULT_PREFIX + c("Number of records for History Trace (the blue line)") + "}", true, c("History Length"), true, (Component) this);
        if (strA == null || strA.equals("")) {
            return;
        }
        int iRound = (int) Math.round(Double.parseDouble(strA));
        c(iRound);
        this.f10643m = null;
        b("History Trace Length", "" + iRound);
    }

    public void G() {
        String strB;
        if (!g() || (strB = bV.b(this, c("Import Table"), new String[]{"table", "vex"}, "*.vex" + File.pathSeparator + "*.table", d(f10646n))) == null || strB.equals("")) {
            return;
        }
        if (strB.indexOf(File.separator) != -1) {
            b(f10646n, strB.substring(0, strB.lastIndexOf(File.separator)));
        }
        if (!strB.toLowerCase().endsWith(".vex")) {
            try {
                new eY().b(strB, (C1701s) getModel());
                return;
            } catch (Exception e2) {
                bV.d("Error Loading table file:\n" + e2.getMessage(), this);
                e2.printStackTrace();
                return;
            }
        }
        try {
            new fF().a((C1701s) getModel(), strB);
        } catch (V.a e3) {
            bV.d(c("Error Importing table!") + "\n" + e3.getMessage(), this);
        } catch (Exception e4) {
            bH.C.a("Unexpected problem importing table!", e4, this);
        }
    }

    public void H() {
        if (g()) {
            this.f10637h = false;
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + c("Set Selected Cells to") + ":}", true, c("Set Cell Values"), true, (Component) this);
            this.f10637h = true;
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            b(Double.parseDouble(strA));
            O();
        }
    }

    public void a(int i2, int i3) {
        int i4 = i2 / i();
        int rowHeight = i3 / getRowHeight();
        if (isCellSelected(rowHeight, i4)) {
            return;
        }
        changeSelection(rowHeight, i4, false, false);
    }

    public void b(int i2, int i3) {
        int i4 = i2 / i();
        int rowHeight = i3 / getRowHeight();
        JPopupMenu jPopupMenu = new JPopupMenu();
        add(jPopupMenu);
        C1554as c1554as = new C1554as(this);
        if (((C1701s) getModel()).D() != null) {
            jPopupMenu.add(c("Back to original value")).addActionListener(c1554as);
            jPopupMenu.addSeparator();
        } else {
            jPopupMenu.add(c("Revert to starting value")).addActionListener(c1554as);
        }
        if (g()) {
            jPopupMenu.add(c("Set to - Key: =")).addActionListener(c1554as);
            jPopupMenu.add(c("Increment - Key: > or ,")).addActionListener(c1554as);
            jPopupMenu.add(c("Decrement - Key: < or .")).addActionListener(c1554as);
            jPopupMenu.add(c("Increase by - Key: +")).addActionListener(c1554as);
            jPopupMenu.add(c("Decrease by - Key: -")).addActionListener(c1554as);
            jPopupMenu.add(c("Scale by - Key: *")).addActionListener(c1554as);
            jPopupMenu.add(c("Interpolate - Key: /")).addActionListener(c1554as);
            if (S()) {
                jPopupMenu.add(c("Interpolate Horizontal - Key: H")).addActionListener(c1554as);
                jPopupMenu.add(c("Interpolate Vertical - Key: V")).addActionListener(c1554as);
                jPopupMenu.add(c("Smooth Cells - Key: s")).addActionListener(c1554as);
                jPopupMenu.add(c("Fill Up and Right - Key: f")).addActionListener(c1554as);
                jPopupMenu.add(c("Set increment amount")).addActionListener(c1554as);
                jPopupMenu.add(c("Set number of increments (CTRL pressed)")).addActionListener(c1554as);
                jPopupMenu.add(c("Set percent increment size (SHIFT pressed)")).addActionListener(c1554as);
                jPopupMenu.add(c("Cell Color By Value")).addActionListener(c1554as);
            } else {
                jPopupMenu.add(c("Interpolate Horizontal - Key: H")).setEnabled(f10656U);
                jPopupMenu.add(c("Interpolate Vertical - Key: V")).setEnabled(f10656U);
                jPopupMenu.add(c("Smooth Cells - Key: s")).setEnabled(f10656U);
                jPopupMenu.add(c("Fill Up and Right - Key: f")).setEnabled(f10656U);
            }
            jPopupMenu.addSeparator();
        }
        jPopupMenu.add(c("Copy CTRL-C")).addActionListener(c1554as);
        if (g()) {
            jPopupMenu.add(c("Paste CTRL-V")).addActionListener(c1554as);
            if (S()) {
                JMenu jMenu = new JMenu(c("Paste Special"));
                jPopupMenu.add((JMenuItem) jMenu);
                jMenu.add(c("Multiply By Copied Values - Percent")).addActionListener(c1554as);
                jMenu.add(c("Multiply By Copied Values - Raw")).addActionListener(c1554as);
                jMenu.add(c("Add Copied Table Values")).addActionListener(c1554as);
                jMenu.add(c("Subtract Copied Table Values")).addActionListener(c1554as);
            }
        }
        if (this.f10673af != null && this.f10673af.a()) {
            jPopupMenu.addSeparator();
            C1578bp c1578bp = new C1578bp(c("Adjust Table Size & Shape"));
            a(c1578bp);
            jPopupMenu.add((JMenuItem) c1578bp);
        }
        jPopupMenu.addSeparator();
        jPopupMenu.add(c("Export Table")).addActionListener(c1554as);
        if (g()) {
            jPopupMenu.add(c("Import Table")).addActionListener(c1554as);
        }
        if (this.f10663ab) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(c("Follow Mode"), this.f10662aa);
            jCheckBoxMenuItem.addActionListener(new C1555at(this));
            jPopupMenu.add((JMenuItem) jCheckBoxMenuItem);
        }
        if (I()) {
            if (S()) {
                jPopupMenu.add(c("History Trace Length") + " (" + this.f10644L + ")").addActionListener(c1554as);
            } else {
                jPopupMenu.add(c("History Trace Length") + " (" + this.f10644L + ")").setEnabled(false);
                c(5);
            }
        }
        if (aj()) {
            jPopupMenu.add(c("Lock Selected Cells")).addActionListener(c1554as);
        }
        if (ak()) {
            jPopupMenu.add(c("Unlock Selected Cells")).addActionListener(c1554as);
        }
        jPopupMenu.show(this, i2, i3);
    }

    protected InterfaceC1579bq a(InterfaceC1579bq interfaceC1579bq) {
        C1556au c1556au = new C1556au(this);
        if (getSelectedColumns().length == 1) {
            if (getColumnCount() < this.f10673af.d() && (getColumnCount() + 1) * getRowCount() <= this.f10673af.f()) {
                interfaceC1579bq.add("Insert Column Before Selected").addActionListener(c1556au);
                interfaceC1579bq.add("Insert Column After Selected").addActionListener(c1556au);
            }
            if (getColumnCount() > this.f10673af.e()) {
                interfaceC1579bq.add("Delete Selected Column").addActionListener(c1556au);
            }
        }
        if (getSelectedRows().length == 1) {
            if (getRowCount() > this.f10673af.c()) {
                interfaceC1579bq.add("Delete Selected Row").addActionListener(c1556au);
            }
            if (getRowCount() < this.f10673af.b() && getColumnCount() * (getRowCount() + 1) <= this.f10673af.f()) {
                interfaceC1579bq.add("Insert Row Above Selected").addActionListener(c1556au);
                interfaceC1579bq.add("Insert Row Below Selected").addActionListener(c1556au);
            }
        }
        interfaceC1579bq.add("Resize Table").addActionListener(c1556au);
        return interfaceC1579bq;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ag() {
        double d2 = Double.NaN;
        double d3 = Double.NaN;
        try {
            d2 = Double.parseDouble(this.f10674w.getText());
        } catch (NumberFormatException e2) {
            if (this.f10674w.getText().trim().isEmpty()) {
                this.f10674w.a("Auto");
                this.f10674w.selectAll();
            }
        }
        try {
            d3 = Double.parseDouble(this.f10675x.getText());
        } catch (NumberFormatException e3) {
            if (this.f10675x.getText().trim().isEmpty()) {
                this.f10675x.a("Auto");
                this.f10675x.selectAll();
            }
        }
        h(d2);
        g(d3);
        ((C1701s) getModel()).C();
        repaint();
    }

    private void ah() {
        double d2 = Double.NaN;
        double d3 = Double.NaN;
        try {
            d2 = Double.parseDouble(d("cellColorMin"));
        } catch (Exception e2) {
        }
        try {
            d3 = Double.parseDouble(d("cellColorMax"));
        } catch (Exception e3) {
        }
        h(d2);
        g(d3);
        ((C1701s) getModel()).C();
        repaint();
    }

    private void ai() {
        if (this.f10676y == null) {
            this.f10674w = new Cdo("Auto", 1);
            this.f10675x = new Cdo("Auto", 1);
            double d2 = Double.NaN;
            double d3 = Double.NaN;
            try {
                d2 = Double.parseDouble(d("cellColorMin"));
            } catch (Exception e2) {
            }
            if (!Double.isNaN(d2)) {
                this.f10674w.a(d2);
            }
            try {
                d3 = Double.parseDouble(d("cellColorMax"));
            } catch (Exception e3) {
            }
            if (!Double.isNaN(d3)) {
                this.f10675x.a(d3);
            }
            C1557av c1557av = new C1557av(this);
            C1558aw c1558aw = new C1558aw(this);
            this.f10674w.addKeyListener(c1558aw);
            this.f10675x.addKeyListener(c1558aw);
            this.f10676y = new JPanel();
            this.f10676y.setBorder(BorderFactory.createTitledBorder(c("Fixed color Min/Max")));
            this.f10676y.setLayout(new GridLayout(1, 4, 6, 6));
            this.f10676y.add(new JLabel(c("Color Min"), 0));
            this.f10676y.add(this.f10674w);
            this.f10676y.add(new JLabel(c("Color Min"), 0));
            this.f10676y.add(this.f10675x);
            this.f10674w.addFocusListener(c1557av);
            this.f10675x.addFocusListener(c1557av);
        }
        bV.a(this.f10676y, this, c("Color Limits"), new C1559ax(this));
    }

    public double a(String[] strArr, double d2) {
        return C1677fh.b(strArr, d2);
    }

    @Override // javax.swing.JTable
    public void changeSelection(int i2, int i3, boolean z2, boolean z3) {
        super.changeSelection(i2, i3, z2, z3);
        this.f10677z = "";
        O();
    }

    public double b(String[] strArr, double d2) throws NumberFormatException {
        double d3 = 10.0d;
        try {
            d3 = Double.parseDouble(strArr[strArr.length - 1]);
        } catch (Exception e2) {
            System.out.println("axisValues=" + ((Object) strArr));
            System.out.println("Exception in getYaxisPosition, axisValues[axisValues.length-1]=" + strArr[strArr.length - 1] + ", axisValues.length=" + strArr.length);
        }
        double d4 = 0.0d;
        int length = strArr.length - 1;
        while (true) {
            if (length < 0) {
                break;
            }
            double d5 = Double.parseDouble(strArr[length]);
            if (d5 == d2) {
                d4 = length;
                break;
            }
            if (d5 > d2) {
                d4 = length == strArr.length - 1 ? length : length + ((d5 - d2) / (d5 - d3));
            } else {
                d3 = d5;
                length--;
            }
        }
        return d4;
    }

    public boolean I() {
        return this.f10642K;
    }

    public void a(boolean z2) {
        this.f10642K = z2;
    }

    public int J() {
        return this.f10644L;
    }

    public void c(int i2) {
        this.f10644L = i2;
        this.f10643m = null;
    }

    public void b(boolean z2) {
        C1701s c1701s = (C1701s) getModel();
        if (c1701s.f11740j == null || c1701s.f11740j.length == 0 || c1701s.f11740j[0].length == 0 || c1701s.f11740j[0][0] == null) {
            bH.C.c("Request to unlock cells, but there are no AnanlysisCells available");
            return;
        }
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            for (int i3 = 0; i3 < getRowCount(); i3++) {
                if (isCellSelected(i3, i2)) {
                    c1701s.D()[(c1701s.f11740j.length - i3) - 1][i2].a(z2);
                }
            }
        }
        repaint();
        al();
    }

    private boolean aj() {
        if (!g()) {
            return false;
        }
        C1701s c1701s = (C1701s) getModel();
        if (c1701s.f11740j == null || c1701s.f11740j.length == 0 || c1701s.f11740j[0].length == 0 || c1701s.f11740j[0][0] == null) {
            return false;
        }
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            for (int i3 = 0; i3 < getRowCount(); i3++) {
                if (isCellSelected(i3, i2) && !c1701s.D()[(c1701s.f11740j.length - i3) - 1][i2].m()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean ak() {
        C1701s c1701s = (C1701s) getModel();
        if (c1701s.f11740j == null || c1701s.f11740j.length == 0 || c1701s.f11740j[0].length == 0 || c1701s.f11740j[0][0] == null) {
            return false;
        }
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            for (int i3 = 0; i3 < getRowCount(); i3++) {
                if (isCellSelected(i3, i2) && c1701s.D()[(c1701s.D().length - i3) - 1][i2].m()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean c(int i2, int i3) {
        C1701s c1701s = (C1701s) getModel();
        if (c1701s.f11740j == null || c1701s.f11740j.length < i2 || c1701s.f11740j[0].length < i3 || c1701s.f11740j[0][0] == null) {
            return false;
        }
        return c1701s.D()[(c1701s.D().length - i2) - 1][i3].m();
    }

    private void al() {
        C1701s c1701s = (C1701s) getModel();
        if (c1701s.f11740j == null || c1701s.f11740j.length == 0 || c1701s.f11740j[0].length == 0 || c1701s.f11740j[0][0] == null || this.f10645M == null) {
            return;
        }
        String str = "";
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            for (int i3 = 0; i3 < getRowCount(); i3++) {
                if (c1701s.D()[(c1701s.f11740j.length - i3) - 1][i2].m()) {
                    str = str + "[" + i3 + CallSiteDescriptor.TOKEN_DELIMITER + i2 + "]";
                }
            }
        }
        b("Lock Selected Cells", str);
    }

    private void am() throws NumberFormatException {
        String strD;
        C1701s c1701s = (C1701s) getModel();
        if (c1701s.f11740j == null || c1701s.f11740j.length == 0 || c1701s.f11740j[0].length == 0 || c1701s.f11740j[0][0] == null || this.f10645M == null || (strD = d("Lock Selected Cells")) == null || strD.equals("")) {
            return;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(strD, "[");
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            String strSubstring = strNextToken.substring(0, strNextToken.length() - 1);
            int i2 = Integer.parseInt(strSubstring.substring(0, strSubstring.indexOf(CallSiteDescriptor.TOKEN_DELIMITER)));
            int i3 = Integer.parseInt(strSubstring.substring(strSubstring.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1, strSubstring.length()));
            if (i2 < c1701s.f11740j.length && i3 < c1701s.f11740j[i2].length) {
                c1701s.D()[(c1701s.f11740j.length - i2) - 1][i3].a(true);
            }
        }
    }

    public bH.aa K() {
        return this.f10648O;
    }

    public void a(bH.aa aaVar) {
        this.f10648O = aaVar;
    }

    public int L() {
        return this.f10649P;
    }

    public float M() {
        return this.f10658W;
    }

    public void a(float f2) {
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f2 > 1.0d) {
            f2 = 1.0f;
        }
        if (this.f10658W != f2) {
            b("Smooth Cells - Key: s", Float.toString(f2));
        }
        this.f10658W = f2;
    }

    private void an() {
        String strA = bV.a((Component) this, true, c("Preferred Cell Increment Size"), aq() + "");
        if (strA == null || strA.equals("")) {
            return;
        }
        try {
            float f2 = Float.parseFloat(strA);
            if (f2 > 0.0f) {
                b(f2);
            } else {
                bV.d(c("Increment size should be greater than zero."), this);
                an();
            }
        } catch (Exception e2) {
            bV.d(c("Invalid Increment size") + " " + aq(), this);
        }
    }

    private void ao() {
        String strA = bV.a((Component) this, true, "<html>" + c("Preferred Multi-Increment Count.") + "<br>" + c("With CTRL key down, will increment this many times"), L() + "");
        if (strA == null || strA.equals("")) {
            return;
        }
        try {
            int i2 = (int) Float.parseFloat(strA);
            if (i2 <= 0) {
                bV.d(c("Increment size should be greater than zero."), this);
                ao();
            } else if (i2 < 20) {
                d(i2);
            } else {
                bV.d(c("20 Seems a bit excessive, don't you think?"), this);
                ao();
            }
        } catch (Exception e2) {
            bV.d(c("Invalid Increment count") + " " + this.f10649P, this);
        }
    }

    private void ap() {
        String strA = bV.a((Component) this, true, "<html>" + c("Preferred Percent-Increment Amount.") + "<br>" + c("With SHIFT key down, increments by this percentage"), bH.W.c(X() * 100.0d, 0) + FXMLLoader.RESOURCE_KEY_PREFIX);
        if (strA == null || strA.equals("")) {
            return;
        }
        try {
            double d2 = Double.parseDouble(bH.W.b(strA, FXMLLoader.RESOURCE_KEY_PREFIX, "")) / 100.0d;
            if (d2 <= 0.0d) {
                bV.d(c("Increment size should be greater than zero."), this);
                ap();
            } else if (d2 < 0.5d) {
                d(d2);
            } else {
                bV.d(c("50 percent Seems a bit excessive, don't you think?"), this);
                ap();
            }
        } catch (Exception e2) {
            bV.d(c("Invalid Increment percent") + " " + this.f10650Q, this);
        }
    }

    public void b(float f2) {
        this.f10652S = f2;
        b("Set increment amount", f2 + "");
    }

    public void d(int i2) {
        this.f10649P = i2;
        b("Set number of increments (CTRL pressed)", i2 + "");
    }

    public void d(double d2) {
        i(d2);
        b("Set percent increment size (SHIFT pressed)", d2 + "");
    }

    private float aq() {
        return !Float.isNaN(this.f10652S) ? this.f10652S : (float) P();
    }

    public boolean N() {
        return this.f10628E;
    }

    public void c(boolean z2) {
        this.f10628E = z2;
    }

    public void e(double d2) {
        this.f10670ad = d2;
        if (this.f10671v != null) {
            this.f10671v.f10744b = Math.round(1000.0d / d2);
        }
    }

    protected void a(InterfaceC1548am interfaceC1548am) {
        this.f10673af = interfaceC1548am;
    }

    public void d(boolean z2) {
        boolean z3 = this.f10662aa ^ z2;
        this.f10662aa = z2;
        if (z3) {
            j(z2);
        }
    }

    public void a(aA aAVar) {
        this.f10664q.add(aAVar);
    }

    private void j(boolean z2) {
        Iterator it = this.f10664q.iterator();
        while (it.hasNext()) {
            ((aA) it.next()).a(z2);
        }
    }

    public void e(boolean z2) {
        this.f10663ab = z2;
    }

    @Override // javax.swing.JTable, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        addHierarchyListener(this);
    }

    @Override // javax.swing.JTable, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        removeHierarchyListener(this);
        super.removeNotify();
    }

    @Override // java.awt.event.HierarchyListener
    public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
        O();
    }

    public void O() {
        C1701s c1701s = (C1701s) getModel();
        if (c1701s.K()) {
            c1701s.c(false);
        }
    }

    private void ar() {
        Iterator it = this.f10647N.iterator();
        while (it.hasNext()) {
            ((InterfaceC1649eg) it.next()).a((int) Math.round(this.f10639j), (int) Math.round(this.f10638i), ((C1701s) getModel()).a(this.f10638i, this.f10639j));
        }
    }

    public void f(double d2) {
        this.f10651R = d2;
    }

    public double P() {
        return Double.isNaN(this.f10651R) ? Math.pow(10.0d, 1.0d - this.f10624a) / 10.0d : this.f10651R;
    }

    @Override // javax.swing.JTable
    public TableCellEditor getDefaultEditor(Class cls) {
        if (this.f10630G == null) {
            this.f10630G = new aB(this);
            this.f10630G.getComponent().setFont(getFont());
            this.f10630G.getComponent().addFocusListener(new C1560ay(this));
        }
        return this.f10630G;
    }

    @Override // javax.swing.JTable
    public void removeEditor() {
        int editingColumn = super.getEditingColumn();
        int editingRow = super.getEditingRow();
        super.removeEditor();
        if (!this.f10672ae || editingColumn < 0 || editingRow < 0) {
            return;
        }
        SwingUtilities.invokeLater(new RunnableC1561az(this, editingRow, editingColumn));
        this.f10672ae = false;
    }

    @Override // javax.swing.JTable
    public boolean editCellAt(int i2, int i3, EventObject eventObject) {
        if ((eventObject instanceof KeyEvent) && ((KeyEvent) eventObject).getKeyCode() == 32) {
            return false;
        }
        boolean zEditCellAt = super.editCellAt(i2, i3, eventObject);
        if (zEditCellAt) {
            this.f10630G.a();
        }
        return zEditCellAt;
    }

    public int Q() {
        return this.f10623D;
    }

    public void e(int i2) {
        this.f10623D = i2;
        if (this.f10628E) {
            this.f10626c = new Font(this.f10626c.getFontName(), 1, i2);
        } else {
            this.f10626c = new Font(this.f10626c.getFontName(), 0, i2);
        }
        this.f10627d = new Font(this.f10626c.getFontName(), 1, i2);
        setFont(this.f10626c);
        this.f10629F = null;
        this.f10630G = null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0377  */
    @Override // javax.swing.JTable, javax.swing.JComponent
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean processKeyBinding(javax.swing.KeyStroke r7, java.awt.event.KeyEvent r8, int r9, boolean r10) {
        /*
            Method dump skipped, instructions count: 1096
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.efiAnalytics.ui.BinTableView.processKeyBinding(javax.swing.KeyStroke, java.awt.event.KeyEvent, int, boolean):boolean");
    }

    private boolean as() {
        int[] selectedColumns = getSelectedColumns();
        int[] selectedRows = getSelectedRows();
        if (selectedColumns.length * selectedRows.length <= 1) {
            return false;
        }
        if (selectedColumns[selectedColumns.length - 1] >= getColumnCount() - 1) {
            return true;
        }
        changeSelection(selectedRows[0], selectedColumns[0] + 1, false, false);
        changeSelection(selectedRows[selectedRows.length - 1], selectedColumns[selectedColumns.length - 1] + 1, false, true);
        return true;
    }

    private int[] a(float f2, int i2, int i3) {
        if (i2 % 2 == 0) {
            f2 += 0.5f;
        }
        int[] iArr = new int[i2];
        int iRound = Math.round(f2);
        int length = iArr.length / 2;
        while (iRound - length < 0) {
            f2 += 1.0f;
            iRound = Math.round(f2);
        }
        while (((iRound - length) + iArr.length) - 1 > i3) {
            f2 -= 1.0f;
            iRound = Math.round(f2);
        }
        iArr[length] = Math.round(f2);
        for (int i4 = 1; i4 <= iArr.length - length; i4++) {
            int i5 = length - i4;
            int i6 = iArr[length] - i4;
            if (i5 >= 0 && i6 >= 0) {
                iArr[i5] = i6;
            }
            int i7 = length + i4;
            int i8 = iArr[length] + i4;
            if (i7 < iArr.length && i8 <= i3) {
                iArr[i7] = i8;
            }
        }
        return iArr;
    }

    private void a(float f2, float f3) {
        int[] selectedColumns = getSelectedColumns();
        int[] selectedRows = getSelectedRows();
        if (selectedColumns.length * selectedRows.length > 1) {
            int[] iArrA = a(f3, selectedColumns.length, getModel().getColumnCount() - 1);
            int[] iArrA2 = a(f2, selectedRows.length, getModel().getRowCount() - 1);
            if (iArrA[iArrA.length - 1] <= getColumnCount() - 1) {
                if (selectedColumns[0] == iArrA[0] && selectedRows[0] == iArrA2[0]) {
                    return;
                }
                changeSelection(iArrA2[0], iArrA[0], false, false);
                changeSelection(iArrA2[iArrA2.length - 1], iArrA[iArrA.length - 1], false, true);
                return;
            }
            return;
        }
        if (selectedColumns.length * selectedRows.length == 1) {
            int iRound = Math.round(f3);
            int iRound2 = Math.round(f2);
            if (selectedColumns[0] == iRound && selectedRows[0] == iRound2) {
                return;
            }
            selectedColumns[0] = iRound;
            selectedRows[0] = iRound2;
            changeSelection(selectedRows[0], selectedColumns[0], false, false);
        }
    }

    private boolean at() {
        return getSelectedColumns().length * getSelectedRows().length > 1;
    }

    private boolean au() {
        int[] selectedColumns = getSelectedColumns();
        int[] selectedRows = getSelectedRows();
        if (selectedColumns.length * selectedRows.length <= 1) {
            return false;
        }
        if (selectedColumns[0] <= 0) {
            return true;
        }
        changeSelection(selectedRows[0], selectedColumns[0] - 1, false, false);
        changeSelection(selectedRows[selectedRows.length - 1], selectedColumns[selectedColumns.length - 1] - 1, false, true);
        return true;
    }

    private boolean av() {
        int[] selectedColumns = getSelectedColumns();
        int[] selectedRows = getSelectedRows();
        if (selectedColumns.length * selectedRows.length <= 1) {
            return false;
        }
        if (selectedRows[selectedRows.length - 1] >= getRowCount() - 1) {
            return true;
        }
        changeSelection(selectedRows[0] + 1, selectedColumns[0], false, false);
        changeSelection(selectedRows[selectedRows.length - 1] + 1, selectedColumns[selectedColumns.length - 1], false, true);
        return true;
    }

    private boolean aw() {
        int[] selectedColumns = getSelectedColumns();
        int[] selectedRows = getSelectedRows();
        if (selectedColumns.length * selectedRows.length <= 1) {
            return false;
        }
        if (selectedRows[0] <= 0) {
            return true;
        }
        changeSelection(selectedRows[0] - 1, selectedColumns[0], false, false);
        changeSelection(selectedRows[selectedRows.length - 1] - 1, selectedColumns[selectedColumns.length - 1], false, true);
        return true;
    }

    private boolean a(char c2) {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(c2) != -1;
    }

    protected aD R() {
        if (this.f10629F == null) {
            this.f10629F = new aD(this, this);
        }
        return this.f10629F;
    }

    private String d(String str) {
        return this.f10645M == null ? "" : this.f10645M.a(c(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str, String str2) {
        if (this.f10645M != null) {
            this.f10645M.a(c(str), str2);
        }
    }

    public void a(InterfaceC1662et interfaceC1662et) throws NumberFormatException {
        this.f10645M = interfaceC1662et;
        a();
    }

    public static boolean S() {
        return f10656U;
    }

    public static void f(boolean z2) {
        f10656U = z2;
    }

    public boolean T() {
        return this.f10659X;
    }

    public void g(boolean z2) {
        this.f10659X = z2;
    }

    public void h(boolean z2) {
        this.f10660Y = z2;
    }

    public int U() {
        return this.f10634H;
    }

    public void f(int i2) {
        this.f10634H = i2;
    }

    public fz V() {
        if (this.f10666ac == null) {
            this.f10666ac = fz.a(Color.WHITE);
        }
        return this.f10666ac;
    }

    public static boolean W() {
        return f10657V;
    }

    public static void i(boolean z2) {
        f10657V = z2;
    }

    public void g(double d2) {
        this.f10635I = d2;
    }

    public void h(double d2) {
        this.f10636J = d2;
    }

    public double X() {
        return this.f10650Q;
    }

    public void i(double d2) {
        this.f10650Q = d2;
    }
}
