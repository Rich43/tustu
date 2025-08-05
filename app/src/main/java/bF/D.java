package bF;

import G.C0094c;
import bH.W;
import bH.aa;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: TunerStudioMS.jar:bF/D.class */
public class D extends JTable implements ClipboardOwner, HierarchyListener {

    /* renamed from: t, reason: collision with root package name */
    private int f6780t;

    /* renamed from: u, reason: collision with root package name */
    private int f6781u;

    /* renamed from: a, reason: collision with root package name */
    int f6782a;

    /* renamed from: b, reason: collision with root package name */
    Font f6783b;

    /* renamed from: c, reason: collision with root package name */
    Font f6784c;

    /* renamed from: v, reason: collision with root package name */
    private boolean f6785v;

    /* renamed from: w, reason: collision with root package name */
    private N f6786w;

    /* renamed from: x, reason: collision with root package name */
    private L f6787x;

    /* renamed from: d, reason: collision with root package name */
    boolean f6788d;

    /* renamed from: e, reason: collision with root package name */
    boolean f6789e;

    /* renamed from: f, reason: collision with root package name */
    double f6790f;

    /* renamed from: g, reason: collision with root package name */
    Clipboard f6791g;

    /* renamed from: h, reason: collision with root package name */
    boolean f6792h;

    /* renamed from: y, reason: collision with root package name */
    private InterfaceC1662et f6793y;

    /* renamed from: i, reason: collision with root package name */
    public static String f6794i = "table1DInitialDir";

    /* renamed from: z, reason: collision with root package name */
    private aa f6795z;

    /* renamed from: A, reason: collision with root package name */
    private int f6796A;

    /* renamed from: B, reason: collision with root package name */
    private float f6797B;

    /* renamed from: j, reason: collision with root package name */
    Image f6798j;

    /* renamed from: k, reason: collision with root package name */
    boolean f6799k;

    /* renamed from: C, reason: collision with root package name */
    private Map f6800C;

    /* renamed from: D, reason: collision with root package name */
    private boolean f6801D;

    /* renamed from: l, reason: collision with root package name */
    List f6802l;

    /* renamed from: m, reason: collision with root package name */
    Stroke f6803m;

    /* renamed from: n, reason: collision with root package name */
    Color f6804n;

    /* renamed from: o, reason: collision with root package name */
    Color f6805o;

    /* renamed from: E, reason: collision with root package name */
    private boolean f6806E;

    /* renamed from: p, reason: collision with root package name */
    long f6807p;

    /* renamed from: q, reason: collision with root package name */
    double f6808q;

    /* renamed from: r, reason: collision with root package name */
    String f6809r;

    /* renamed from: s, reason: collision with root package name */
    long f6810s;

    public D() {
        this(null);
    }

    public D(y yVar) {
        super(yVar);
        this.f6780t = (int) (eJ.a() * 1.2d);
        this.f6781u = w() * 3;
        this.f6782a = 0;
        this.f6783b = new Font("Arial Unicode MS", 0, w());
        this.f6784c = new Font("Arial Unicode MS", 1, w());
        this.f6785v = true;
        this.f6786w = null;
        this.f6787x = null;
        this.f6788d = true;
        this.f6789e = true;
        this.f6790f = -1.0d;
        this.f6791g = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.f6792h = false;
        this.f6793y = null;
        this.f6795z = null;
        this.f6796A = 5;
        this.f6797B = Float.NaN;
        this.f6798j = null;
        this.f6799k = true;
        this.f6800C = new HashMap();
        this.f6801D = false;
        this.f6802l = new ArrayList();
        this.f6803m = new BasicStroke(2.0f);
        this.f6804n = new Color(0, 0, 255, 180);
        this.f6805o = new Color(0, 0, 0, 85);
        this.f6806E = false;
        this.f6807p = -1L;
        this.f6808q = 0.0d;
        this.f6809r = "";
        this.f6810s = 0L;
        DefaultListSelectionModel defaultListSelectionModel = new DefaultListSelectionModel();
        defaultListSelectionModel.setSelectionMode(2);
        setInputVerifier(new O(this));
        setSelectionModel(defaultListSelectionModel);
        setFont(this.f6783b);
        setColumnSelectionAllowed(true);
        addMouseListener(new P(this));
        UIManager.put("ToolTip.background", new Color(240, 240, 240));
        ToolTipManager.sharedInstance().setDismissDelay(15000);
        addKeyListener(new E(this));
    }

    public void a() {
        String strC = c("Set increment amount");
        if (strC == null || strC.equals("")) {
            return;
        }
        a(Float.parseFloat(strC));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(KeyEvent keyEvent) {
        if (h().f()) {
            int selectedColumn = getSelectedColumn();
            int selectedRow = getSelectedRow();
            int rowCount = getRowCount();
            int columnCount = getColumnCount();
            if (keyEvent.getKeyCode() == 9 && keyEvent.isShiftDown()) {
                TableCellEditor cellEditor = getCellEditor();
                if (cellEditor != null) {
                    if (bH.H.a(cellEditor.getCellEditorValue().toString())) {
                        cellEditor.stopCellEditing();
                    } else {
                        cellEditor.cancelCellEditing();
                    }
                }
                if (selectedRow > 0) {
                    changeSelection(selectedRow - 1, selectedColumn, false, false);
                } else if (selectedColumn > 0) {
                    changeSelection(rowCount - 1, selectedColumn - 1, false, false);
                } else {
                    changeSelection(rowCount - 1, columnCount - 1, false, false);
                }
                keyEvent.consume();
                return;
            }
            if (keyEvent.getKeyCode() == 10 || keyEvent.getKeyCode() == 9) {
                TableCellEditor cellEditor2 = getCellEditor();
                if (cellEditor2 != null) {
                    if (bH.H.a(cellEditor2.getCellEditorValue().toString())) {
                        cellEditor2.stopCellEditing();
                    } else {
                        cellEditor2.cancelCellEditing();
                    }
                }
                if (selectedRow < rowCount - 1) {
                    changeSelection(selectedRow + 1, selectedColumn, false, false);
                } else if (selectedColumn < columnCount - 1) {
                    changeSelection(0, selectedColumn + 1, false, false);
                } else {
                    changeSelection(0, 0, false, false);
                }
                keyEvent.consume();
            }
        }
    }

    private String b(String str) {
        if (this.f6795z == null) {
            return str;
        }
        String str2 = (String) this.f6800C.get(str);
        if (str2 != null) {
            return str2;
        }
        String strA = this.f6795z.a(str);
        this.f6800C.put(str, strA);
        return strA;
    }

    @Override // javax.swing.JTable
    public boolean isCellEditable(int i2, int i3) {
        return getSelectedRows().length <= 1 && getSelectedColumns().length <= 1 && d();
    }

    public int b() {
        int iC = 0;
        int columnCount = h().f() ? h().getColumnCount() : h().getRowCount();
        for (int i2 = 0; i2 < columnCount; i2++) {
            C cA = h().a(i2, i2);
            if (cA.c() > iC) {
                iC = cA.c();
            }
        }
        return iC;
    }

    @Override // javax.swing.JTable
    protected TableModel createDefaultDataModel() {
        y yVar = new y();
        C0972c c0972c = new C0972c(8);
        c0972c.a(new C0094c("Row 1"));
        for (int i2 = 0; i2 < c0972c.a(); i2++) {
            c0972c.a(i2, Double.valueOf(50.0d));
        }
        yVar.a(c0972c);
        return yVar;
    }

    @Override // java.awt.datatransfer.ClipboardOwner
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

    public void c() {
        String str = "";
        int[] selectedColumns = getSelectedColumns();
        int[] selectedRows = getSelectedRows();
        for (int i2 = selectedRows[0]; i2 < getRowCount() && i2 <= selectedRows[selectedRows.length - 1]; i2++) {
            for (int i3 = selectedColumns[0]; i3 < getColumnCount() && i3 <= selectedColumns[selectedColumns.length - 1]; i3++) {
                y yVar = (y) getModel();
                str = str + W.c(((Double) yVar.getValueAt(i2, i3)).doubleValue(), yVar.a(i2, i3).c()) + "\t";
            }
            str = str + "\n";
        }
        this.f6791g.setContents(new StringSelection(str), this);
    }

    protected boolean d() {
        return true;
    }

    public void e() {
        if (d()) {
            s();
            y yVar = (y) getModel();
            try {
                StringTokenizer stringTokenizer = new StringTokenizer(this.f6791g.getData(DataFlavor.stringFlavor).toString(), "\n");
                for (int selectedRow = getSelectedRow(); selectedRow < yVar.getRowCount() && stringTokenizer.hasMoreTokens(); selectedRow++) {
                    StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), "\t");
                    for (int selectedColumn = getSelectedColumn(); selectedColumn < yVar.getColumnCount() && stringTokenizer2.hasMoreTokens(); selectedColumn++) {
                        String strNextToken = stringTokenizer2.nextToken();
                        try {
                            yVar.setValueAt(new Double(strNextToken), selectedRow, selectedColumn);
                        } catch (Exception e2) {
                            System.out.println("bad Double " + strNextToken);
                            return;
                        }
                    }
                }
            } catch (Exception e3) {
                System.out.println("Clipboard data not valid");
            }
        }
    }

    @Override // javax.swing.JTable
    public TableCellEditor getCellEditor(int i2, int i3) {
        L l2 = (L) super.getCellEditor(i2, i3);
        if (((y) getModel()).f()) {
            l2.a(i3);
        } else {
            l2.a(i2);
        }
        return l2;
    }

    @Override // javax.swing.JTable
    protected TableColumnModel createDefaultColumnModel() {
        TableColumnModel tableColumnModelCreateDefaultColumnModel = super.createDefaultColumnModel();
        tableColumnModelCreateDefaultColumnModel.addColumnModelListener(this);
        return tableColumnModelCreateDefaultColumnModel;
    }

    public int f() {
        return getWidth() / getColumnModel().getColumnCount();
    }

    protected int g() {
        return getFont() != null ? getFont().getSize() * 3 : this.f6780t * 3;
    }

    @Override // javax.swing.JTable, javax.swing.event.TableColumnModelListener
    public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
        TableColumn column = ((TableColumnModel) tableColumnModelEvent.getSource()).getColumn(tableColumnModelEvent.getToIndex());
        column.setMaxWidth(g());
        column.setPreferredWidth(g());
        super.columnAdded(tableColumnModelEvent);
    }

    @Override // javax.swing.JTable
    public TableCellRenderer getCellRenderer(int i2, int i3) {
        y yVar = (y) getModel();
        if (!getFont().equals(this.f6783b)) {
            setFont(this.f6783b);
        }
        if (yVar.f()) {
            x().a(i3);
        } else {
            x().a(i2);
        }
        if (!isEnabled()) {
            x().setBackground(Color.LIGHT_GRAY);
            x().setForeground(Color.GRAY);
            return x();
        }
        Color colorA = h().f() ? a(i3) : a(i2);
        if (colorA != null) {
            x().setBackground(colorA);
            x().setForeground(Color.black);
        } else if (!this.f6788d || getModel().getValueAt(i2, i3) == null) {
            x().setBackground(Color.white);
            x().setForeground(Color.black);
        } else {
            x().setBackground(C1677fh.a(((Double) yVar.getValueAt(i2, i3)).doubleValue(), yVar.a(i2, i3).d(), yVar.a(i2, i3).e()));
            x().setForeground(Color.black);
        }
        if (getModel().getValueAt(i2, i3) != null && !yVar.b(i2, i3)) {
            double dDoubleValue = ((Double) yVar.getValueAt(i2, i3)).doubleValue() - Double.valueOf(yVar.c(i2, i3)).doubleValue();
            if (dDoubleValue < 0.0d && Math.abs(dDoubleValue) > c(i2, i3) * 0.5d) {
                x().setForeground(new Color(115, 0, 0));
            } else if (dDoubleValue > c(i2, i3) * 0.5d) {
                x().setForeground(new Color(30, 30, 255));
            }
            if (yVar.b(i2, i3) || x() == null) {
                x().setToolTipText("");
            } else {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>").append(b("Begining Value")).append(": ").append(W.c(yVar.c(i2, i3), b())).append("</html>");
                    x().setToolTipText(sb.toString());
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println("Error getting clean value for table tooltip");
                }
            }
        }
        N nX = x();
        x();
        nX.setHorizontalAlignment(0);
        return x();
    }

    @Override // javax.swing.JTable
    protected JTableHeader createDefaultTableHeader() {
        JTableHeader jTableHeaderCreateDefaultTableHeader = super.createDefaultTableHeader();
        jTableHeaderCreateDefaultTableHeader.setResizingAllowed(false);
        jTableHeaderCreateDefaultTableHeader.setAlignmentX(0.5f);
        v vVar = new v(this);
        vVar.setFont(new Font("Arial Unicode MS", 0, eJ.a(12)));
        jTableHeaderCreateDefaultTableHeader.setDefaultRenderer(vVar);
        vVar.setBackground(UIManager.getColor("Label.background"));
        jTableHeaderCreateDefaultTableHeader.setBackground(UIManager.getColor("Label.background"));
        vVar.a(false);
        return jTableHeaderCreateDefaultTableHeader;
    }

    private Image z() {
        if (this.f6798j == null || this.f6798j.getWidth(null) != getWidth() || this.f6798j.getHeight(null) != getHeight()) {
            this.f6798j = createImage(getWidth(), getHeight());
        }
        return this.f6798j;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f6807p > 0) {
            this.f6808q = ((this.f6808q * 4.0d) + (1000.0d / (System.currentTimeMillis() - this.f6807p))) / 5.0d;
        }
        this.f6807p = System.currentTimeMillis();
        if (!this.f6799k) {
            a(graphics);
            return;
        }
        Image imageZ = z();
        a(imageZ.getGraphics());
        graphics.drawImage(imageZ, 0, 0, null);
    }

    public void a(Graphics graphics) {
        try {
            super.paint(graphics);
            if (this.f6790f >= 0.0d) {
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(this.f6805o);
                graphics2D.setStroke(this.f6803m);
                if (h().f()) {
                    int iF = (f() * r().getColumnCount()) - (3 * 2);
                    int rowHeight = getRowHeight() - (2 * 2);
                    int rowHeight2 = (int) (getRowHeight() * r().getRowCount() * this.f6790f);
                    graphics.fillRoundRect(2, 2 + rowHeight2, iF, rowHeight, f(), getRowHeight());
                    graphics.setColor(this.f6804n);
                    graphics.drawRoundRect(2, 2 + rowHeight2, iF, rowHeight, f(), getRowHeight());
                    return;
                }
                int iF2 = f() - (2 * 2);
                int rowHeight3 = (getRowHeight() * r().getRowCount()) - (3 * 2);
                int iF3 = (int) (f() * r().getColumnCount() * this.f6790f);
                graphics.fillRoundRect(2 + iF3, 2, iF2, rowHeight3, f(), getRowHeight());
                graphics.setColor(this.f6804n);
                graphics.drawRoundRect(2 + iF3, 2, iF2, rowHeight3, f(), getRowHeight());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            bH.C.c("Table1DView paint error.");
        }
    }

    public void a(double d2) {
        if (isEnabled() && this.f6789e) {
            this.f6790f = C1677fh.b(((y) getModel()).e(), d2) / r0.length;
        }
    }

    public y h() {
        return (y) getModel();
    }

    public void i() {
        if (d()) {
            s();
            for (int i2 = 0; i2 < getRowCount(); i2++) {
                for (int i3 = 0; i3 < getColumnCount(); i3++) {
                    if (isCellSelected(i2, i3)) {
                        y yVar = (y) getModel();
                        Double d2 = (Double) yVar.getValueAt(i2, i3);
                        if (d2 != null && !d2.isNaN()) {
                            yVar.setValueAt(Double.valueOf(d2.doubleValue() + d(i2, i3)), i2, i3);
                        }
                    }
                }
            }
            repaint();
        }
    }

    public void j() {
        if (d()) {
            s();
            for (int i2 = 0; i2 < getRowCount(); i2++) {
                for (int i3 = 0; i3 < getColumnCount(); i3++) {
                    if (isCellSelected(i2, i3)) {
                        y yVar = (y) getModel();
                        Double d2 = (Double) yVar.getValueAt(i2, i3);
                        if (d2 != null && !d2.isNaN()) {
                            yVar.setValueAt(Double.valueOf(d2.doubleValue() - d(i2, i3)), i2, i3);
                        }
                    }
                }
            }
            repaint();
        }
    }

    public void k() {
        int[] selectedRows = getSelectedRows();
        int[] selectedColumns = getSelectedColumns();
        if (selectedRows == null || selectedRows.length <= 0) {
            return;
        }
        b(d(selectedRows[0], selectedColumns[0]) * u());
    }

    public void l() {
        if (this.f6801D) {
            if (d() || getSelectedColumn() < 0 || getSelectedRow() < 0) {
                s();
                y yVar = (y) getModel();
                double dDoubleValue = ((Double) yVar.getValueAt(getSelectedRow(), getSelectedColumn())).doubleValue();
                for (int maxSelectionIndex = getSelectionModel().getMaxSelectionIndex(); maxSelectionIndex >= 0; maxSelectionIndex--) {
                    for (int selectedColumn = getSelectedColumn(); selectedColumn < yVar.getColumnCount(); selectedColumn++) {
                        try {
                            yVar.setValueAt(new Double(dDoubleValue), maxSelectionIndex, selectedColumn);
                        } catch (Exception e2) {
                            System.out.println("bad Double " + dDoubleValue);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void m() {
        int[] selectedRows = getSelectedRows();
        int[] selectedColumns = getSelectedColumns();
        if (selectedRows == null || selectedRows.length <= 0) {
            return;
        }
        b((-d(selectedRows[0], selectedColumns[0])) * u());
    }

    public void n() {
        if (d()) {
            s();
            this.f6789e = false;
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + b("Multiply Selected Cells by: ex. 1.2 = raise by 20%") + "}", true, b("Scale Cells"), true, (Component) this);
            this.f6789e = true;
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            d(Double.parseDouble(strA));
        }
    }

    public void o() {
        if (d()) {
            s();
            this.f6789e = false;
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + b("Increase Selected Cells by") + ":}", true, b("Add To Cells"), true, (Component) this);
            this.f6789e = true;
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            b(Double.parseDouble(strA));
        }
    }

    public void p() {
        if (d() && y()) {
            s();
            int[] selectedColumns = getSelectedColumns();
            int[] selectedRows = getSelectedRows();
            if (selectedColumns.length == 0 || selectedRows.length == 0) {
                return;
            }
            if (r().f()) {
                if (selectedRows.length > 1) {
                    for (int i2 = 0; i2 < selectedColumns.length; i2++) {
                        double dDoubleValue = ((Double) h().getValueAt(selectedRows[0], selectedColumns[i2])).doubleValue();
                        double dDoubleValue2 = ((Double) h().getValueAt(selectedRows[selectedRows.length - 1], selectedColumns[i2])).doubleValue();
                        for (int i3 = 0; i3 < selectedRows.length; i3++) {
                            getModel().setValueAt(Double.valueOf(dDoubleValue + ((i3 * (dDoubleValue2 - dDoubleValue)) / (selectedRows.length - 1))), selectedRows[i3], selectedColumns[i2]);
                        }
                    }
                }
            } else if (selectedColumns.length > 1) {
                for (int i4 = 0; i4 < selectedRows.length; i4++) {
                    C cA = r().a(selectedRows[i4], selectedColumns[0]);
                    double dDoubleValue3 = cA.a(selectedColumns[0]).doubleValue();
                    double dDoubleValue4 = cA.a(selectedColumns[selectedColumns.length - 1]).doubleValue();
                    for (int i5 = 0; i5 < selectedColumns.length; i5++) {
                        getModel().setValueAt(Double.valueOf(dDoubleValue3 + ((i5 * (dDoubleValue4 - dDoubleValue3)) / (selectedColumns.length - 1))), selectedRows[i4], selectedColumns[i5]);
                    }
                }
            }
            repaint();
        }
    }

    @Override // javax.swing.JTable
    public void removeEditor() {
        int editingColumn = super.getEditingColumn();
        int editingRow = super.getEditingRow();
        super.removeEditor();
        if (!this.f6806E || editingColumn < 0 || editingRow < 0) {
            return;
        }
        SwingUtilities.invokeLater(new F(this, editingRow, editingColumn));
        this.f6806E = false;
    }

    public void q() {
        if (d()) {
            this.f6789e = false;
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + b("Decrease Selected Cells by") + ":}", true, b("Subtract From Cells"), true, (Component) this);
            this.f6789e = true;
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            b(-Double.parseDouble(strA));
        }
    }

    public void b(double d2) {
        if (d()) {
            s();
            for (int i2 = 0; i2 < getRowCount(); i2++) {
                for (int i3 = 0; i3 < getColumnCount(); i3++) {
                    if (isCellSelected(i2, i3)) {
                        y yVar = (y) getModel();
                        Double d3 = (Double) yVar.getValueAt(i2, i3);
                        if (d3 != null && !d3.isNaN()) {
                            yVar.setValueAt(new Double(a(d3.doubleValue(), d2)), i2, i3);
                        }
                    }
                }
            }
            repaint();
        }
    }

    private double a(double d2, double d3) {
        return Math.round(((long) ((d2 + d3) * 1000000.0d)) / 100.0d) / 10000.0d;
    }

    public void c(double d2) {
        if (d()) {
            s();
            ((y) getModel()).e(true);
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                for (int i3 = 0; i3 < getRowCount(); i3++) {
                    if (isCellSelected(i3, i2)) {
                        y yVar = (y) getModel();
                        if (((Double) yVar.getValueAt(i3, i2)) != null) {
                            yVar.setValueAt(Double.valueOf(d2), i3, i2);
                        }
                    }
                }
            }
            repaint();
        }
    }

    public void d(double d2) {
        if (d()) {
            s();
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                for (int i3 = 0; i3 < getRowCount(); i3++) {
                    if (isCellSelected(i3, i2)) {
                        y yVar = (y) getModel();
                        yVar.setValueAt(new Double(((Double) yVar.getValueAt(i3, i2)).doubleValue() * d2), i3, i2);
                    }
                }
            }
        }
    }

    public y r() {
        return (y) getModel();
    }

    public void s() {
        this.f6792h = true;
    }

    private boolean A() {
        return getSelectedRowCount() > 1 || getSelectedColumnCount() > 1;
    }

    public void a(String str) {
        if (str != null && str.equals(b("Increment - Key: > or ,"))) {
            i();
            repaint();
            return;
        }
        if (str != null && str.equals(b("Decrement - Key: < or ."))) {
            j();
            repaint();
            return;
        }
        if (str != null && str.equals(b("Increase by - Key: +"))) {
            o();
            repaint();
            return;
        }
        if (str != null && str.equals(b("Decrease by - Key: -"))) {
            q();
            repaint();
            return;
        }
        if (str != null && str.equals(b("Scale by - Key: *"))) {
            n();
            repaint();
            return;
        }
        if (str != null && str.equals(b("Set to - Key: ="))) {
            t();
            return;
        }
        if (str != null && str.equals(b("Copy CTRL-C"))) {
            c();
            return;
        }
        if (str != null && str.equals(b("Paste CTRL-V"))) {
            e();
            repaint();
        } else if (str != null && str.equals(b("Interpolate - Key: /"))) {
            p();
        } else {
            if (str == null || !str.startsWith(b("Set increment amount"))) {
                return;
            }
            B();
        }
    }

    public void t() {
        if (d()) {
            this.f6789e = false;
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + b("Set Selected Cells to") + ":}", true, b("Set Cell Values"), true, (Component) this);
            this.f6789e = true;
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            c(Double.parseDouble(strA));
            ((y) getModel()).e(false);
        }
    }

    public void a(int i2, int i3) {
        int iF = i2 / f();
        int rowHeight = i3 / getRowHeight();
        if (isCellSelected(rowHeight, iF)) {
            return;
        }
        changeSelection(rowHeight, iF, false, false);
    }

    public void b(int i2, int i3) {
        int iF = i2 / f();
        int rowHeight = i3 / getRowHeight();
        JPopupMenu jPopupMenu = new JPopupMenu();
        add(jPopupMenu);
        G g2 = new G(this);
        if (d() && y()) {
            jPopupMenu.add(b("Set to - Key: =")).addActionListener(g2);
            jPopupMenu.add(b("Increment - Key: > or ,")).addActionListener(g2);
            jPopupMenu.add(b("Decrement - Key: < or .")).addActionListener(g2);
            jPopupMenu.add(b("Increase by - Key: +")).addActionListener(g2);
            jPopupMenu.add(b("Decrease by - Key: -")).addActionListener(g2);
            jPopupMenu.add(b("Scale by - Key: *")).addActionListener(g2);
            jPopupMenu.add(b("Set increment amount")).addActionListener(g2);
            jPopupMenu.add(b("Interpolate - Key: /")).addActionListener(g2);
            jPopupMenu.addSeparator();
        }
        jPopupMenu.add(b("Copy CTRL-C")).addActionListener(g2);
        if (d()) {
            jPopupMenu.add(b("Paste CTRL-V")).addActionListener(g2);
        }
        jPopupMenu.addSeparator();
        jPopupMenu.show(this, i2, i3);
    }

    @Override // javax.swing.JTable
    public void changeSelection(int i2, int i3, boolean z2, boolean z3) {
        super.changeSelection(i2, i3, z2, z3);
        this.f6809r = "";
        v();
    }

    public void a(aa aaVar) {
        this.f6795z = aaVar;
    }

    public int u() {
        return this.f6796A;
    }

    private void B() {
        String strA = bV.a((Component) this, true, b("Preferred Cell Increment Size"), d(0, 0) + "");
        if (strA == null || strA.equals("")) {
            return;
        }
        try {
            float f2 = Float.parseFloat(strA);
            if (f2 <= 0.0f) {
                bV.d(b("Increment size should be greater than zero."), this);
            } else {
                a(f2);
            }
        } catch (Exception e2) {
            bV.d(b("Invalid Increment size") + " " + d(0, 0), this);
        }
    }

    public void a(float f2) {
        this.f6797B = f2;
        a("Set increment amount", f2 + "");
    }

    private float d(int i2, int i3) {
        return !Float.isNaN(this.f6797B) ? this.f6797B : (float) c(i2, i3);
    }

    @Override // java.awt.Component
    public void repaint() {
        super.repaint();
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
        v();
    }

    public void v() {
        y yVar = (y) getModel();
        if (yVar.j()) {
            yVar.e(false);
        }
    }

    public double c(int i2, int i3) {
        double d2 = h().f() ? h().a(i2, i3).d(i2) : h().a(i2, i3).d(i3);
        return Double.isNaN(d2) ? Math.pow(10.0d, 1.0d - ((y) getModel()).a(i2, i3).c()) / 10.0d : d2;
    }

    @Override // javax.swing.JTable
    public TableCellEditor getDefaultEditor(Class cls) {
        if (this.f6787x == null) {
            this.f6787x = new L(this);
            this.f6787x.getComponent().setFont(getFont());
            this.f6787x.getComponent().addFocusListener(new H(this));
        }
        return this.f6787x;
    }

    @Override // javax.swing.JTable
    public boolean editCellAt(int i2, int i3, EventObject eventObject) {
        if ((eventObject instanceof KeyEvent) && ((KeyEvent) eventObject).getKeyCode() == 32) {
            return false;
        }
        boolean zEditCellAt = super.editCellAt(i2, i3, eventObject);
        if (zEditCellAt) {
            this.f6787x.a();
        }
        return zEditCellAt;
    }

    public Color a(int i2) {
        if (i2 < this.f6802l.size()) {
            return (Color) this.f6802l.get(i2);
        }
        return null;
    }

    public void a(Color color) {
        this.f6802l.add(color);
    }

    public int w() {
        return this.f6780t;
    }

    public void b(int i2) {
        this.f6780t = i2;
        if (this.f6785v) {
            this.f6783b = new Font(this.f6783b.getFontName(), 1, i2);
        } else {
            this.f6783b = new Font(this.f6783b.getFontName(), 0, i2);
        }
        this.f6784c = new Font(this.f6783b.getFontName(), 1, i2);
        this.f6786w = null;
        this.f6787x = null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:110:0x02d5  */
    @Override // javax.swing.JTable, javax.swing.JComponent
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean processKeyBinding(javax.swing.KeyStroke r7, java.awt.event.KeyEvent r8, int r9, boolean r10) {
        /*
            Method dump skipped, instructions count: 843
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: bF.D.processKeyBinding(javax.swing.KeyStroke, java.awt.event.KeyEvent, int, boolean):boolean");
    }

    private boolean a(char c2) {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(c2) != -1;
    }

    protected N x() {
        if (this.f6786w == null) {
            this.f6786w = new N(this, this);
        }
        return this.f6786w;
    }

    private String c(String str) {
        return this.f6793y == null ? "" : this.f6793y.a(str);
    }

    private void a(String str, String str2) {
        if (this.f6793y != null) {
            this.f6793y.a(str, str2);
        }
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f6793y = interfaceC1662et;
        a();
    }

    public boolean y() {
        return this.f6801D;
    }

    public void a(boolean z2) {
        this.f6801D = z2;
    }
}
