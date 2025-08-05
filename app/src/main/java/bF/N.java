package bF;

import bH.W;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/* loaded from: TunerStudioMS.jar:bF/N.class */
class N extends DefaultTableCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    D f6824a;

    /* renamed from: b, reason: collision with root package name */
    boolean f6825b = false;

    /* renamed from: c, reason: collision with root package name */
    int f6826c = 3;

    /* renamed from: d, reason: collision with root package name */
    BasicStroke f6827d = new BasicStroke(this.f6826c);

    /* renamed from: e, reason: collision with root package name */
    Color f6828e = Color.DARK_GRAY;

    /* renamed from: f, reason: collision with root package name */
    Image f6829f = null;

    /* renamed from: i, reason: collision with root package name */
    private int f6830i = 0;

    /* renamed from: g, reason: collision with root package name */
    boolean f6831g = false;

    /* renamed from: h, reason: collision with root package name */
    final /* synthetic */ D f6832h;

    public N(D d2, D d3) {
        this.f6832h = d2;
        this.f6824a = null;
        this.f6824a = d3;
    }

    @Override // javax.swing.JLabel
    public String getText() {
        if (this.f6824a == null) {
            return "";
        }
        String text = super.getText();
        return text != null ? W.c(Double.valueOf(text).doubleValue(), ((y) this.f6832h.getModel()).a(this.f6830i, this.f6830i).c()) : "";
    }

    @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        return super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (!this.f6831g) {
            a(graphics);
            return;
        }
        Image imageA = a();
        a(imageA.getGraphics());
        graphics.drawImage(imageA, 0, 0, null);
    }

    public void a(Graphics graphics) {
        super.paint(graphics);
        if (this.f6825b) {
            graphics.setColor(this.f6828e);
            ((Graphics2D) graphics).setStroke(this.f6827d);
            graphics.drawRect(this.f6826c / 2, this.f6826c / 2, getWidth() - this.f6826c, getHeight() - this.f6826c);
        }
    }

    private Image a() {
        if (this.f6829f == null || this.f6829f.getWidth(null) != getWidth() || this.f6829f.getHeight(null) != getHeight()) {
            this.f6829f = createImage(getWidth(), getHeight());
        }
        return this.f6829f;
    }

    public void a(int i2) {
        this.f6830i = i2;
    }
}
