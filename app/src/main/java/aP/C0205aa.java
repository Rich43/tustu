package aP;

import java.awt.Cursor;
import javax.swing.JComboBox;

/* renamed from: aP.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/aa.class */
public class C0205aa extends JComboBox {

    /* renamed from: a, reason: collision with root package name */
    G.R f2880a;

    /* renamed from: c, reason: collision with root package name */
    private String f2879c = "Loading..";

    /* renamed from: b, reason: collision with root package name */
    z.i f2881b = new z.i();

    public C0205aa(G.R r2) {
        this.f2880a = null;
        this.f2880a = r2;
        setEditable(true);
        addItem(this.f2879c);
        new C0206ab(this).start();
    }

    public void a() {
        setCursor(Cursor.getPredefinedCursor(3));
        String[] strArrA = this.f2881b.a();
        removeAllItems();
        for (String str : strArrA) {
            addItem(str);
        }
        if (this.f2880a != null && aE.a.A() != null) {
            setSelectedItem(aE.a.A().n(this.f2880a.c()));
        } else if (com.efiAnalytics.ui.bV.d()) {
            setSelectedItem("COM1");
        } else {
            setSelectedItem("/dev/ttyS0");
        }
        setCursor(Cursor.getDefaultCursor());
    }
}
