package bD;

import bH.W;
import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/* renamed from: bD.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/f.class */
class C0960f extends DefaultTableCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    Date f6662a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0957c f6663b;

    public C0960f(C0957c c0957c) {
        this.f6663b = c0957c;
        this.f6662a = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Types.CLOB, 1, 1);
        this.f6662a = calendar.getTime();
        setFont(new Font(getFont().getName(), 0, eJ.a(12)));
    }

    @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        if (obj instanceof Icon) {
            setIcon((Icon) obj);
            setText("");
        } else if (obj instanceof Date) {
            setIcon((Icon) null);
            setText(((Date) obj).toString());
        } else if (obj != null && i3 == F.f6618e) {
            setIcon((Icon) null);
            setText(W.a(((Long) obj).longValue()));
        } else if (obj != null) {
            setText(obj.toString());
            setIcon((Icon) null);
        }
        if (z2) {
            super.setBackground(this.f6663b.getSelectionBackground());
            super.setForeground(this.f6663b.getSelectionForeground());
        } else {
            super.setBackground(Color.white);
            super.setForeground(Color.BLACK);
        }
        return this;
    }
}
