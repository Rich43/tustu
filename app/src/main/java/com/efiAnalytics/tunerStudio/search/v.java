package com.efiAnalytics.tunerStudio.search;

import com.efiAnalytics.ui.eV;
import java.awt.Component;
import javax.swing.JTable;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/v.class */
class v extends eV {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f10228a;

    v(q qVar) {
        this.f10228a = qVar;
    }

    @Override // com.efiAnalytics.ui.eV, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        Component tableCellRendererComponent = super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
        a(i2, i3);
        return tableCellRendererComponent;
    }

    @Override // com.efiAnalytics.ui.eV, javax.swing.table.TableCellEditor
    public Component getTableCellEditorComponent(JTable jTable, Object obj, boolean z2, int i2, int i3) {
        Component tableCellEditorComponent = super.getTableCellEditorComponent(jTable, obj, z2, i2, i3);
        a(i2, i3);
        return tableCellEditorComponent;
    }

    public String a(int i2, int i3) {
        String str = "";
        if (i2 >= this.f10228a.f10209b.size() || i3 != 2) {
            b(q.f10221p);
            a(q.f10221p);
        } else {
            f fVar = (f) this.f10228a.f10209b.get(i2);
            boolean zEquals = fVar.b().equals(bT.o.f7604a);
            if (fVar.c().equals(PdfOps.D_TOKEN)) {
                str = q.f10215j;
                a(false);
            } else if (!zEquals && (fVar.c().equals("I") || fVar.c().equals("O"))) {
                str = q.f10216k;
                a(true);
            } else if (this.f10228a.h(fVar)) {
                str = q.f10218m;
                a(true);
            } else if (System.currentTimeMillis() - fVar.e() > q.f10208a) {
                str = q.f10221p;
                a(true);
            } else if (zEquals || fVar.c().equals("A")) {
                str = q.f10217l;
                a(!fVar.f().h());
            } else if (fVar.c().equals("U")) {
                str = q.f10219n;
                a(false);
            } else {
                bH.C.a("Unknown Device Status: " + fVar.c());
                str = q.f10220o;
                a(false);
            }
            if (fVar.f().h()) {
                str = "Busy";
                a(false);
            } else {
                a(true);
            }
            b(str);
            a(str);
        }
        return str;
    }
}
