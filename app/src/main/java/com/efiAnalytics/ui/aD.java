package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aD.class */
class aD extends DefaultTableCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    BinTableView f10721a;

    /* renamed from: b, reason: collision with root package name */
    boolean f10722b = false;

    /* renamed from: c, reason: collision with root package name */
    int f10723c = 3;

    /* renamed from: d, reason: collision with root package name */
    BasicStroke f10724d = new BasicStroke(this.f10723c);

    /* renamed from: e, reason: collision with root package name */
    Color f10725e = Color.DARK_GRAY;

    /* renamed from: f, reason: collision with root package name */
    Image f10726f = null;

    /* renamed from: g, reason: collision with root package name */
    boolean f10727g = false;

    /* renamed from: h, reason: collision with root package name */
    Border f10728h = BorderFactory.createEmptyBorder(0, 0, 0, 0);

    /* renamed from: i, reason: collision with root package name */
    final /* synthetic */ BinTableView f10729i;

    public aD(BinTableView binTableView, BinTableView binTableView2) {
        this.f10729i = binTableView;
        this.f10721a = null;
        this.f10721a = binTableView2;
        UIDefaults uIDefaults = new UIDefaults();
        uIDefaults.put("TextArea.borderPainter", new aE(this, binTableView));
        UIManager.put("Table.cellNoFocusBorder", new Insets(0, 0, 0, 0));
        UIManager.put("Table.focusCellHighlightBorder", new Insets(0, 0, 0, 0));
        putClientProperty("Nimbus.Overrides", uIDefaults);
        putClientProperty("Nimbus.Overrides.InheritDefaults", false);
        addFocusListener(new aF(this, binTableView));
    }

    @Override // javax.swing.JLabel
    public String getText() {
        String text;
        return (this.f10721a == null || this.f10729i.f10661Z || (text = super.getText()) == null || text.isEmpty()) ? "" : bH.W.c(Double.valueOf(text).doubleValue(), this.f10729i.f10624a);
    }

    @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        this.f10722b = this.f10729i.c(i2, i3);
        return super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (!this.f10727g) {
            a(graphics);
            return;
        }
        Image imageA = a();
        try {
            a(imageA.getGraphics());
        } catch (Exception e2) {
        }
        graphics.drawImage(imageA, 0, 0, null);
    }

    public void a(Graphics graphics) {
        super.paint(graphics);
        if (this.f10722b) {
            graphics.setColor(this.f10725e);
            ((Graphics2D) graphics).setStroke(this.f10724d);
            graphics.drawRect(this.f10723c / 2, this.f10723c / 2, getWidth() - this.f10723c, getHeight() - this.f10723c);
        }
    }

    private Image a() {
        if (this.f10726f == null || this.f10726f.getWidth(null) != getWidth() || this.f10726f.getHeight(null) != getHeight()) {
            this.f10726f = createImage(getWidth(), getHeight());
        }
        return this.f10726f;
    }

    @Override // javax.swing.JComponent
    public Border getBorder() {
        return this.f10728h;
    }
}
