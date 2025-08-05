package j;

import W.C0184j;
import bH.C;
import h.InterfaceC1736a;
import h.g;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: j.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:j/a.class */
public class C1750a implements InterfaceC1736a {
    @Override // h.InterfaceC1736a
    public void a(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((C0184j) it.next()).a());
        }
        if (arrayList2.contains("RPM_ENGINE")) {
            C.c("Setting Field mapping to: FieldMaps/BigStuffGen4.properties");
            g.a();
            g.b("FieldMaps/BigStuffGen4.properties");
            return;
        }
        if (1 != 0) {
            C.c("Setting Field mapping to: FieldMaps/BigStuffGen3.properties");
            g.a();
            g.b("FieldMaps/BigStuffGen3.properties");
            return;
        }
        if (arrayList2.contains("MAP")) {
            C.c("Setting Field mapping to: FieldMaps/BigStuffGen3.properties");
            g.a();
            g.b("FieldMaps/BigStuffGen3.properties");
        } else if (arrayList2.contains("Scaled Load")) {
            C.c("Setting Field mapping to: FieldMaps/BigStuffAlt.properties");
            g.a();
            g.b("FieldMaps/BigStuffAlt.properties");
        } else if (arrayList2.contains("Scaled_Load")) {
            C.c("Setting Field mapping to: FieldMaps/BigStuff.properties");
            g.a();
            g.b("FieldMaps/BigStuff.properties");
        } else {
            C.c("Setting Field mapping to: FieldMaps/BigStuffReplay.properties");
            g.a();
            g.b("FieldMaps/BigStuffReplay.properties");
        }
    }
}
