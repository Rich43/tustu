package ao;

import g.C1733k;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

/* renamed from: ao.P, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/P.class */
final class C0598P implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ List f5104a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f5105b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ Component f5106c;

    C0598P(List list, String str, Component component) {
        this.f5104a = list;
        this.f5105b = str;
        this.f5106c = component;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strSubstring = "";
        Iterator it = this.f5104a.iterator();
        while (it.hasNext()) {
            strSubstring = strSubstring + ((String) it.next()) + ", ";
        }
        if (strSubstring.endsWith(", ")) {
            strSubstring = strSubstring.substring(0, strSubstring.length() - 2);
        }
        String strA = C1733k.a("{Save the fields in '" + this.f5105b + "' As}", false, "Selected Fields: " + strSubstring, true, this.f5106c);
        if (strA == null || strA.isEmpty()) {
            return;
        }
        C0597O.a(strA, this.f5104a);
    }
}
