package aR;

import G.C0113cs;
import G.R;
import G.T;
import W.C0171aa;
import W.aj;
import aP.C0338f;
import aP.C0404hl;
import aP.cZ;
import bH.H;
import bH.W;
import d.InterfaceC1711c;
import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aR/g.class */
public class g implements InterfaceC1711c, d.l {

    /* renamed from: a, reason: collision with root package name */
    public static String f3862a = "ecuConfigName";

    /* renamed from: b, reason: collision with root package name */
    public static String f3863b = "Tune_File_Path";

    /* renamed from: c, reason: collision with root package name */
    public static String f3864c = "OutputChannel_Name";

    /* renamed from: d, reason: collision with root package name */
    public static String f3865d = "loadCalFile";

    /* renamed from: e, reason: collision with root package name */
    d.k f3866e = null;

    @Override // d.InterfaceC1711c
    public String a() {
        return f3865d;
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return "Load Tune or Partial Tune";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Other Actions";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e, HeadlessException {
        String property = properties.getProperty(f3862a);
        String property2 = properties.getProperty(f3863b, null);
        if (property2 == null) {
            throw new d.e(f3863b + " is required");
        }
        R rC = (property == null || property.isEmpty()) ? T.a().c() : T.a().c(property);
        if (rC != null) {
            C0338f.a().a(cZ.a().c(), rC, property2);
        } else {
            if (property != null) {
                throw new d.e("No working configuration and no config name requested");
            }
            throw new d.e("Configuration Name not found: " + property);
        }
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f3862a);
        String property2 = properties.getProperty(f3863b, null);
        if (property2 == null) {
            throw new d.e(f3863b + " is required");
        }
        if (((property == null || property.isEmpty()) ? T.a().c() : T.a().c(property)) == null) {
            if (property != null) {
                throw new d.e("No working configuration and no config name requested");
            }
            throw new d.e("Configuration Name not found: " + property);
        }
        if (!new File(property2).exists()) {
            throw new d.e("Tune File not found.");
        }
        String property3 = properties.getProperty(f3864c);
        if (property3 == null || property3.isEmpty()) {
            return;
        }
        if (property3.contains(" ") || W.f(property3)) {
            throw new d.e("Output Channel Name cannot contain special characters!");
        }
        if (H.a(property3.substring(0, 1))) {
            throw new d.e("Output Channel Name cannot start with a number!");
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        if (this.f3866e == null) {
            this.f3866e = new d.k();
            d.i iVar = new d.i(f3862a, "");
            iVar.a("Target Controller");
            iVar.a(0);
            ArrayList arrayList = new ArrayList();
            String[] strArrD = T.a().d();
            arrayList.add("");
            for (String str : strArrD) {
                arrayList.add(str);
            }
            iVar.a(arrayList);
            iVar.c("Select the Controller your want the tune file loaded to. If blank will always load toe the Primary Configuration.");
            this.f3866e.add(iVar);
            d.i iVar2 = new d.i(f3863b, "");
            iVar2.a("Path to tune file");
            iVar2.a(7);
            iVar2.c("Set the full Path to the file you would like loaded when this action is triggered.");
            this.f3866e.add(iVar2);
            d.i iVar3 = new d.i(f3864c, "");
            iVar3.a(1);
            iVar3.c("Optional: Set an OutputChannel name to represent the state of match between your tune file and the current values. If set, there will be an OutputChannel: AppEvents.[YourOutputChannelName]. This channel will be 1.0 if the current settings match those in the partial tune file, 0 if any settings do not match.");
            this.f3866e.add(iVar3);
        }
        return this.f3866e;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }

    @Override // d.l
    public void c(Properties properties) {
        String property = properties.getProperty(f3862a);
        String property2 = properties.getProperty(f3863b);
        String property3 = properties.getProperty(f3864c);
        if (property3 == null || property3.isEmpty()) {
            return;
        }
        File file = new File(property2);
        if (!file.exists()) {
            bH.C.b("Failed to inialize Channel " + property3 + ", Tune File not found: " + property2);
            return;
        }
        if (property == null || property.isEmpty()) {
            if (T.a().c().c() == null) {
                bH.C.a("Failed to inialize Channel " + property3 + ", Controller not found: " + property);
                return;
            }
            property = T.a().c().c();
        }
        R rC = T.a().c(property);
        if (rC == null && T.a().c() != null) {
            rC = T.a().c();
        }
        if (rC == null) {
            C0404hl.a().a("Failed to inialize tune match monitoring " + property3 + ", Controller not found: " + property);
            return;
        }
        R rA = rC.a();
        C0171aa c0171aa = new C0171aa();
        c0171aa.a(true);
        try {
            c0171aa.a(rA, property2);
            C0113cs.a().d(property3);
            if (file.getName().toLowerCase().endsWith(C1798a.cw)) {
                T.a.a().a(property, rA, null, property3);
                bH.C.d("Started full cal monitor on channel: " + property3);
            } else {
                T.a.a().a(property, rA, c0171aa.a(), property3);
                bH.C.d("Started cal monitor on channel: " + property3);
            }
        } catch (V.g e2) {
            String str = "Failed to inialize tune match monitoring! file: " + property2 + ", error: " + ((Object) e2);
            bH.C.d(str);
            C0404hl.a().a(str);
        } catch (aj e3) {
            bH.C.a("Password error opening tune file: " + property2);
        }
    }
}
