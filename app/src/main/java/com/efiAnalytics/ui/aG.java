package com.efiAnalytics.ui;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aG.class */
class aG extends DefaultTableCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    Color f10734a = Color.lightGray;

    /* renamed from: b, reason: collision with root package name */
    Color f10735b = Color.lightGray;

    /* renamed from: c, reason: collision with root package name */
    Color f10736c = new Color(225, 225, JPEG.APP8);

    /* renamed from: d, reason: collision with root package name */
    JTable f10737d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ BinTableView f10738e;

    public aG(BinTableView binTableView, JTable jTable) {
        this.f10738e = binTableView;
        this.f10737d = jTable;
        setHorizontalAlignment(0);
        setForeground(Color.BLACK);
        jTable.getColumnModel().getSelectionModel().addListSelectionListener(new aH(this, binTableView));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(this.f10734a);
        graphics.draw3DRect(0, 0, getWidth() - 1, getHeight() - 1, true);
        if (this.f10737d.isEnabled()) {
            return;
        }
        graphics.setColor(new Color(64, 64, 64, 80));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        Component tableCellRendererComponent = super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
        Color color = this.f10734a;
        if (a(this.f10738e.getSelectedColumns(), i3)) {
            color = this.f10736c;
        }
        tableCellRendererComponent.setBackground(color);
        return tableCellRendererComponent;
    }

    private boolean a(int[] iArr, int i2) {
        for (int i3 : iArr) {
            if (i3 == i2) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.swing.JLabel
    public String getText() {
        String text = super.getText();
        return text != null ? bH.W.b(text, this.f10738e.c()) : text;
    }
}
