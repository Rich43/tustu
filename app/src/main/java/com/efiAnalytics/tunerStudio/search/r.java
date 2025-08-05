package com.efiAnalytics.tunerStudio.search;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/r.class */
class r extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f10224a;

    r(q qVar) {
        this.f10224a = qVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3 || mouseEvent.isPopupTrigger()) {
            int iRowAtPoint = this.f10224a.rowAtPoint(mouseEvent.getPoint());
            if (iRowAtPoint < 0 || iRowAtPoint >= this.f10224a.getRowCount()) {
                this.f10224a.clearSelection();
            } else {
                this.f10224a.setRowSelectionInterval(iRowAtPoint, iRowAtPoint);
            }
            if (this.f10224a.getSelectedRow() >= 0 && (mouseEvent.getComponent() instanceof JTable)) {
                this.f10224a.j().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
            }
        }
    }
}
