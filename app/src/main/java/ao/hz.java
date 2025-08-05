package ao;

import W.C0184j;
import W.C0188n;
import bH.InterfaceC0993a;
import g.C1733k;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

/* loaded from: TunerStudioMS.jar:ao/hz.class */
public class hz {
    public static String[] a(hF hFVar) {
        String[] strArr = new String[hFVar.d()];
        int i2 = 0;
        Iterator itC = hFVar.c();
        while (itC.hasNext()) {
            strArr[i2] = (String) itC.next();
            i2++;
        }
        return C1733k.b(strArr);
    }

    public static boolean a(hF hFVar, bJ bJVar, String str) {
        return a(a(hFVar), bJVar, new hA(str));
    }

    public static boolean a(String[] strArr, bJ bJVar, InterfaceC0993a interfaceC0993a) {
        boolean z2 = false;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (interfaceC0993a == null || interfaceC0993a.a(strArr[i2])) {
                bJVar.c(strArr[i2]);
                z2 = true;
            }
        }
        return z2;
    }

    public static void a(JTextComponent jTextComponent, String str) {
        Action hCVar;
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR != null) {
            TreeSet treeSet = new TreeSet();
            Iterator it = c0188nR.iterator();
            while (it.hasNext()) {
                treeSet.add(((C0184j) it.next()).a());
            }
            hCVar = new hB(treeSet.toArray());
        } else {
            hCVar = new hC();
        }
        jTextComponent.getInputMap().put(KeyStroke.getKeyStroke(str), str);
        jTextComponent.getActionMap().put(str, hCVar);
    }
}
