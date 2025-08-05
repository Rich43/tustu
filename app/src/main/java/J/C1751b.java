package j;

import W.C0184j;
import bH.C;
import h.InterfaceC1736a;
import h.g;
import h.i;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: j.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:j/b.class */
public class C1751b implements InterfaceC1736a {
    @Override // h.InterfaceC1736a
    public void a(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((C0184j) it.next()).a());
        }
        if (i.a("fieldMapping", "Auto").equals("Auto")) {
            if (arrayList2.contains("Front AFF") || arrayList2.contains("Idle Set") || arrayList2.contains("Advance F")) {
                C.c("Setting Field mapping to: FieldMaps/PowerVision.properties");
                g.a();
                g.b("FieldMaps/PowerVision.properties");
                return;
            }
            if (arrayList2.contains("Calculated Desired Cylinder Cut-Off Step") || arrayList2.contains("Engine Speed(10ms)")) {
                C.c("Setting Field mapping to: FieldMaps/DynoSpectrum.properties");
                g.a();
                g.b("FieldMaps/DynoSpectrum.properties");
                return;
            }
            if (arrayList2.contains("PC5 RPM") || arrayList2.contains("WB2 AFR")) {
                C.c("Setting Field mapping to: FieldMaps/PowerCommanderB.properties");
                g.a();
                g.b("FieldMaps/PowerCommanderB.properties");
                return;
            }
            if (arrayList2.contains("PC5 RPM") || arrayList2.contains("PC5 TPS")) {
                C.c("Setting Field mapping to: FieldMaps/PowerCommander.properties");
                g.a();
                g.b("FieldMaps/PowerCommander.properties");
                return;
            }
            if (arrayList2.contains("Manifold Pres") && arrayList2.contains("Engine Temp") && arrayList2.contains("Throttle Pos")) {
                C.c("Setting Field mapping to: FieldMaps/Motec.properties");
                g.a();
                g.b("FieldMaps/Motec.properties");
                return;
            }
            if (arrayList2.contains("Inlet Manifold Pressure") && arrayList2.contains("Throttle Position")) {
                C.c("Setting Field mapping to: FieldMaps/MotecB.properties");
                g.a();
                g.b("FieldMaps/MotecB.properties");
                return;
            }
            if (arrayList2.contains("File Time")) {
                C.c("Setting Field mapping to: FieldMaps/Vi-PEC.properties");
                g.a();
                g.b("FieldMaps/Vi-PEC.properties");
                return;
            }
            if (arrayList2.contains("Engine Temp") || (arrayList2.contains("Injector BPW Front") && arrayList2.contains("Engine Speed"))) {
                C.c("Setting Field mapping to: FieldMaps/Harley.properties");
                g.a();
                g.b("FieldMaps/Harley.properties");
                return;
            }
            if (arrayList2.contains("O2 Integrator Front") && arrayList2.contains("Injector PW Front") && arrayList2.contains("Vehicle Speed")) {
                C.c("Setting Field mapping to: FieldMaps/SERT_Harley.properties.properties");
                g.a();
                g.b("FieldMaps/SERT_Harley.properties.properties");
                return;
            }
            if (arrayList2.contains("Interleaved BLM") || arrayList2.contains("Elapsed Time") || arrayList2.contains("Diag Code")) {
                C.c("Setting Field mapping to: FieldMaps/TCFI.properties");
                g.a();
                g.b("FieldMaps/TCFI.properties");
                return;
            }
            if (arrayList2.contains("Device Time")) {
                C.c("Setting Field mapping to: FieldMaps/torque.properties");
                g.a();
                g.b("FieldMaps/torque.properties");
                return;
            }
            if (arrayList2.contains("Front Adv") || arrayList2.contains("Idle RPM") || arrayList2.contains("Front AFR Sensor")) {
                C.c("Setting Field mapping to: FieldMaps/TwinScan.properties");
                g.a();
                g.b("FieldMaps/TwinScan.properties");
                return;
            }
            if (arrayList2.contains("OPEN LOOP FA") || arrayList2.contains("1/1 Short Term ADAP") || arrayList2.contains("Actual Spark Cyl 1")) {
                C.c("Setting Field mapping to: FieldMaps/Diablo.properties");
                g.a();
                g.b("FieldMaps/Diablo.properties");
            } else if (!arrayList2.contains("Acc enrichment") && !arrayList2.contains("Power on count")) {
                C.c("Checking MS Field mapping types");
                new C1752c().a(arrayList);
            } else {
                C.c("Setting Field mapping to: FieldMaps/MaxxEcu.properties");
                g.a();
                g.b("FieldMaps/MaxxEcu.properties");
            }
        }
    }
}
