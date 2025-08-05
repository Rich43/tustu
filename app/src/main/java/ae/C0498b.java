package ae;

import java.io.File;

/* renamed from: ae.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ae/b.class */
public class C0498b implements InterfaceC0501e {
    @Override // ae.InterfaceC0501e
    public String a(File file) {
        return file.getName().equals("microsquirt.s19") ? "MicroSquirt (Cased)" : file.getName().equals("microsquirt-module.s19") ? "MicroSquirt Module" : file.getName().equals("mspnp2.s19") ? "DIY - Plug-N-Play" : file.getName().equals("megasquirt2.s19") ? "MegaSquirt 2" : file.getName().equals("msns-extra.s19") ? "MegaSquirt 1 - MS1Extra" : file.getName().equals("megasquirt.s19") ? "MegaSquirt 1 - B&G" : file.getName().startsWith("Monitor_") ? "MegaSquirt II - B&G" : file.getName().equalsIgnoreCase("ms3pro.s19") ? o.f4396l : file.getName().equalsIgnoreCase("ms3pro-ult.s19") ? o.f4398n : file.getName().equalsIgnoreCase("ms3pro-evo.s19") ? o.f4397m : file.getName().equalsIgnoreCase("ms3.s19") ? o.f4395k : "";
    }
}
