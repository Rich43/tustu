package com.efiAnalytics.tunerStudio.panels;

import javax.swing.table.DefaultTableModel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/al.class */
class al extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10072a;

    al(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10072a = triggerLoggerPanel;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return false;
    }
}
