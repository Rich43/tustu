package d;

import e.C1717e;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:d/g.class */
public class g {

    /* renamed from: a, reason: collision with root package name */
    private static g f12096a = null;

    /* renamed from: b, reason: collision with root package name */
    private HashMap f12097b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private ArrayList f12098c = new ArrayList();

    private g() {
        C1717e c1717e = new C1717e();
        this.f12097b.put(c1717e.a(), c1717e);
    }

    public static g a() {
        if (f12096a == null) {
            f12096a = new g();
        }
        return f12096a;
    }

    public void a(String str, InterfaceC1711c interfaceC1711c) {
        this.f12097b.put(str, interfaceC1711c);
    }

    public void a(String str) {
        this.f12097b.remove(str);
    }

    public List a(InterfaceC1709a interfaceC1709a) {
        ArrayList arrayList = new ArrayList();
        for (InterfaceC1711c interfaceC1711c : this.f12097b.values()) {
            if (interfaceC1709a == null || interfaceC1709a.a(interfaceC1711c)) {
                arrayList.add(interfaceC1711c);
            }
        }
        Iterator it = this.f12098c.iterator();
        while (it.hasNext()) {
            arrayList.addAll(((f) it.next()).a(interfaceC1709a));
        }
        return arrayList;
    }

    public void a(f fVar) {
        this.f12098c.add(fVar);
    }

    public void a(String str, Properties properties) throws e {
        InterfaceC1711c interfaceC1711cB = b(str);
        if (interfaceC1711cB == null) {
            throw new e("No Action found for action name: " + str);
        }
        try {
            interfaceC1711cB.a(properties);
        } catch (Error e2) {
            e2.printStackTrace();
            a(str);
            throw new e("An Error occurred action name: " + str + ", Action removed.\n" + e2.getMessage());
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new e("An Exception occurred action name: " + str + "\n" + e3.getMessage());
        }
    }

    public InterfaceC1711c b(String str) {
        InterfaceC1711c interfaceC1711cA = (InterfaceC1711c) this.f12097b.get(str);
        if (interfaceC1711cA == null) {
            Iterator it = this.f12098c.iterator();
            while (it.hasNext()) {
                interfaceC1711cA = ((f) it.next()).a(str);
                if (interfaceC1711cA != null) {
                    return interfaceC1711cA;
                }
            }
        }
        return interfaceC1711cA;
    }

    public void c(String str) throws e {
        String strSubstring = str.substring(str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1);
        String strSubstring2 = "";
        if (strSubstring.contains("?")) {
            strSubstring2 = strSubstring.contains("?") ? str.substring(str.indexOf("?") + 1) : "";
            strSubstring = strSubstring.substring(0, strSubstring.indexOf("?"));
        }
        a(strSubstring, C1710b.a(strSubstring2));
    }
}
