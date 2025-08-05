package j;

import W.C0184j;
import bH.C;
import h.InterfaceC1736a;
import h.g;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: j.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:j/c.class */
public class C1752c implements InterfaceC1736a {
    @Override // h.InterfaceC1736a
    public void a(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((C0184j) it.next()).a());
        }
        if (arrayList2.contains("AFR 10Bit") || arrayList2.contains("Batt. Volt.") || arrayList2.contains("TPS 8Bit")) {
            C.c("Setting Field mapping to: FieldMaps/Buell.properties");
            g.a();
            g.b("FieldMaps/Buell.properties");
            return;
        }
        if (arrayList2.contains("PW In1")) {
            C.c("Setting Field mapping to: FieldMaps/PWMonster.properties");
            g.a();
            g.b("FieldMaps/PWMonster.properties");
            return;
        }
        if (arrayList2.contains("Fuel: TPS AE add fuel ms") && arrayList2.contains("Fuel: Last inj pulse width")) {
            C.c("Setting Field mapping to: FieldMaps/rusEFI.properties");
            g.a();
            g.b("FieldMaps/rusEFI.properties");
            return;
        }
        if (arrayList2.contains("Fuel: Total cor") && arrayList2.contains("SPK: Spark Advance")) {
            C.c("Setting Field mapping to: FieldMaps/MS3_1.3.properties");
            g.a();
            g.b("FieldMaps/MS3_1.3.properties");
            return;
        }
        if (arrayList2.contains("EGO cor1")) {
            C.c("Setting Field mapping to: FieldMaps/MS3_1.2.properties");
            g.a();
            g.b("FieldMaps/MS3_1.2.properties");
            return;
        }
        if (arrayList2.contains("egocor Gego1")) {
            C.c("Setting Field mapping to: FieldMaps/MS3Seq1.0.properties");
            g.a();
            g.b("FieldMaps/MS3Seq1.0.properties");
            return;
        }
        if (arrayList2.contains("egocor_Gego1") && arrayList2.contains("Seq PW1")) {
            C.c("Setting Field mapping to: FieldMaps/MS3Seq.properties");
            g.a();
            g.b("FieldMaps/MS3Seq.properties");
        } else if (arrayList2.contains("AFR1")) {
            C.c("Setting Field mapping to: FieldMaps/MS2BaseCode.properties");
            g.a();
            g.b("FieldMaps/MS2BaseCode.properties");
        } else if (arrayList2.contains("egocor_Gego1")) {
            C.c("Setting Field mapping to: FieldMaps/MS3NonSeq.properties");
            g.a();
            g.b("FieldMaps/MS3NonSeq.properties");
        } else {
            C.c("Setting Field mapping to: FieldMaps/MegaSquirt.properties");
            g.a();
            g.b("FieldMaps/MegaSquirt.properties");
        }
    }
}
