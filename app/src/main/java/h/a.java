package H;

import G.InterfaceC0049ai;
import G.InterfaceC0109co;
import G.InterfaceC0139v;
import G.aB;
import bH.C;

/* loaded from: TunerStudioMS.jar:H/a.class */
public class a implements InterfaceC0049ai, InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    private static long f1334a = 0;

    public static long a() {
        return f1334a;
    }

    @Override // G.InterfaceC0049ai
    public boolean a(InterfaceC0139v interfaceC0139v) {
        if (interfaceC0139v.a(new byte[]{-1, -4, 20, -4, 20}, 10, 1).length != 1) {
            C.d("Could not get value for Vbatt");
            return false;
        }
        double d2 = r0[0] * 0.109d;
        C.d("Vbatt=" + d2);
        boolean z2 = d2 > 4.0d;
        if (!z2) {
            aB.a().a("BigStuff3 found, Key On to connect");
            f1334a = System.currentTimeMillis();
        }
        return z2;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
    }
}
