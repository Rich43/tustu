package com.efiAnalytics.ui;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/* renamed from: com.efiAnalytics.ui.ai, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ai.class */
class C1544ai extends DefaultTableCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    Color f10833a = Color.lightGray;

    /* renamed from: b, reason: collision with root package name */
    Color f10834b = new Color(225, 225, JPEG.APP8);

    /* renamed from: d, reason: collision with root package name */
    private int f10835d = -1;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1705w f10836c;

    public C1544ai(C1705w c1705w) {
        this.f10836c = c1705w;
        setBackground(this.f10833a);
        setForeground(Color.BLACK);
        setHorizontalAlignment(0);
        c1705w.f11762a.getSelectionModel().addListSelectionListener(new C1545aj(this, c1705w));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(this.f10833a);
        graphics.draw3DRect(0, 0, getWidth() - 1, getHeight() - 1, true);
        if (this.f10836c.f11762a.isEnabled()) {
            return;
        }
        graphics.setColor(new Color(64, 64, 64, 80));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override // javax.swing.JLabel
    public String getText() {
        String text = super.getText();
        return text != null ? bH.W.a(text, a()) : text;
    }

    @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        Component tableCellRendererComponent = super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
        Color color = this.f10833a;
        if (a(this.f10836c.f11762a.getSelectedRows(), i2)) {
            color = this.f10834b;
        }
        tableCellRendererComponent.setBackground(color);
        if ((tableCellRendererComponent instanceof JComponent) && this.f10836c.f11791E.size() > i2) {
            ((JComponent) tableCellRendererComponent).setToolTipText((String) this.f10836c.f11791E.get(i2));
        }
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

    public int a() {
        return this.f10835d >= 0 ? this.f10835d : this.f10836c.q();
    }

    public void a(int i2) {
        this.f10835d = i2;
    }
}
