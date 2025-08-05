package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;

/* renamed from: com.efiAnalytics.ui.al, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/al.class */
class C1547al extends C1544ai {

    /* renamed from: e, reason: collision with root package name */
    private int f10842e;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C1705w f10843d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1547al(C1705w c1705w) {
        super(c1705w);
        this.f10843d = c1705w;
        this.f10842e = 2;
    }

    @Override // com.efiAnalytics.ui.C1544ai, javax.swing.JLabel
    public String getText() {
        String text = super.getText();
        return text != null ? bH.W.a(text, a()) : text;
    }

    @Override // com.efiAnalytics.ui.C1544ai, javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        Component tableCellRendererComponent = super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
        Color color = this.f10833a;
        if (a(this.f10843d.f11762a.getSelectedRows(), i2)) {
            color = this.f10834b;
        }
        tableCellRendererComponent.setBackground(color);
        if ((tableCellRendererComponent instanceof JComponent) && this.f10843d.f11791E.size() > i2) {
            ((JComponent) tableCellRendererComponent).setToolTipText((String) this.f10843d.f11791E.get(i2));
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

    @Override // com.efiAnalytics.ui.C1544ai
    public int a() {
        return this.f10842e;
    }

    @Override // com.efiAnalytics.ui.C1544ai
    public void a(int i2) {
        this.f10842e = i2;
    }
}
