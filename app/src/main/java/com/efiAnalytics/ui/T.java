package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/T.class */
class T extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10702a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ boolean f10703b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ S f10704c;

    T(S s2, C1705w c1705w, boolean z2) {
        this.f10704c = s2;
        this.f10702a = c1705w;
        this.f10703b = z2;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            int[] selectedRows = this.f10703b ? this.f10704c.getSelectedRows() : this.f10704c.getSelectedColumns();
            if (selectedRows.length == 0) {
                int[] iArr = new int[1];
                iArr[0] = this.f10703b ? this.f10704c.rowAtPoint(mouseEvent.getPoint()) : this.f10704c.columnAtPoint(mouseEvent.getPoint());
                selectedRows = iArr;
            }
            int[] iArr2 = selectedRows;
            JPopupMenu jPopupMenu = new JPopupMenu();
            jPopupMenu.add("Revert to starting value").addActionListener(new U(this, iArr2));
            if (iArr2.length > 2) {
                jPopupMenu.add("Interpolate selected cell values").addActionListener(new V(this, iArr2));
            }
            this.f10704c.add(jPopupMenu);
            jPopupMenu.show(this.f10704c, mouseEvent.getX(), mouseEvent.getY());
            this.f10704c.f10700d = true;
        }
    }
}
