package aR;

import G.C0130m;
import G.G;
import G.R;
import G.T;
import com.efiAnalytics.ui.cY;
import d.InterfaceC1711c;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import s.C1818g;

/* renamed from: aR.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aR/b.class */
public class C0472b implements cY, InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    d.k f3853a = new d.k();

    @Override // d.InterfaceC1711c
    public String a() {
        return "sendControllerCommand";
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return "Send Controller Command";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Settings Dialogs";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return T.a().c() != null && T.a().c().O().b();
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e, IOException {
        C0130m c0130mA;
        String property = properties.getProperty("ecuConfig");
        R rC = (property == null || property.isEmpty()) ? T.a().c() : T.a().c(property);
        if (rC == null) {
            bH.C.a("Failed to send Controller command as the EcuConfiguration name was not found. Name: " + property);
            throw new d.e("EcuConfiguration not found with the provided name.");
        }
        String property2 = properties.getProperty("commandName");
        if (property2 == null || property2.isEmpty()) {
            throw new d.e("Missing required parameter commandName");
        }
        List listB = C0130m.b(rC.O(), property2);
        if (listB.size() > 1) {
            c0130mA = C0130m.a(rC.O(), listB);
        } else {
            if (listB.size() != 1) {
                throw new d.e("Command " + property2 + " not found in current configuration.");
            }
            c0130mA = (C0130m) listB.get(0);
        }
        rC.C().b(c0130mA);
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty("ecuConfig");
        if (((property == null || property.isEmpty()) ? T.a().c() : T.a().c(property)) == null) {
            bH.C.a("Failed to send Controller command as the EcuConfiguration name was not found. Name: " + property);
            throw new d.e("EcuConfiguration not found with the provided name.");
        }
        String property2 = properties.getProperty("commandName");
        if (property2 == null || property2.isEmpty()) {
            throw new d.e("Missing required parameter commandName");
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        R rC = T.a().c();
        this.f3853a.clear();
        List listD = rC.O().d();
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(listD);
        Collections.sort(arrayList);
        d.i iVar = new d.i("commandName", "");
        iVar.a(0);
        iVar.a(arrayList);
        iVar.c("These are Controller Commands defined and implemented by the firmware developers. Each command will trigger the ECU to perform a specific function. Use CAUTION!!! Some commands are intended to for use in a specific order or sequence. Insure you understand what the command does and how it works.");
        this.f3853a.add(iVar);
        return this.f3853a;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // com.efiAnalytics.ui.cY
    public List a(ActionListener actionListener) {
        ArrayList arrayList = new ArrayList();
        String strC = T.a().c().c();
        d dVar = new d(this, strC, actionListener);
        JMenu jMenu = new JMenu(C1818g.b("Send Controller Command"));
        for (String str : T.a().d()) {
            String str2 = str.equals(strC) ? "" : strC + " - ";
            R rC = T.a().c(str);
            if (rC.O().b()) {
                Iterator it = rC.O().c().iterator();
                while (it.hasNext()) {
                    C0473c c0473c = new C0473c(this, str2 + ((G) it.next()).b(), str);
                    jMenu.add((JMenuItem) c0473c);
                    c0473c.addActionListener(dVar);
                }
            }
        }
        arrayList.add(jMenu);
        return arrayList;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }
}
