package bt;

import G.C0072be;
import G.C0079bl;
import G.C0088bu;
import bH.C1007o;
import com.efiAnalytics.ui.InterfaceC1579bq;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import s.C1818g;

/* renamed from: bt.N, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/N.class */
public class C1286N implements ActionListener {

    /* renamed from: c, reason: collision with root package name */
    private int f8680c = -1;

    /* renamed from: a, reason: collision with root package name */
    List f8681a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    C1364w f8682b = new C1364w();

    public InterfaceC1579bq a(InterfaceC1579bq interfaceC1579bq, G.R r2, int i2) {
        this.f8680c = i2;
        HashMap map = new HashMap();
        Iterator itB = r2.e().b();
        while (itB.hasNext()) {
            G.aA aAVar = (G.aA) itB.next();
            try {
                if (!aAVar.c() && C1007o.a(aAVar.i(), r2)) {
                    String strB = C1818g.b(aAVar.e());
                    JMenu jMenu = (JMenu) map.get(strB);
                    if (jMenu == null) {
                        jMenu = new JMenu(strB);
                        jMenu.setEnabled(C1007o.a(aAVar.aH(), r2));
                        map.put(strB, jMenu);
                    }
                    if (aAVar.b()) {
                        a(r2, aAVar.a(), jMenu);
                        if (jMenu.getMenuComponentCount() > 0) {
                            interfaceC1579bq.add(jMenu);
                        }
                    }
                }
            } catch (V.g e2) {
            }
        }
        return interfaceC1579bq;
    }

    private void a(G.R r2, Iterator it, JMenu jMenu) {
        G.aA aAVar = null;
        while (it.hasNext()) {
            G.aA aAVar2 = (G.aA) it.next();
            try {
                if (!aAVar2.c() && C1007o.a(aAVar2.i(), r2)) {
                    C0088bu c0088buC = r2.e().c(aAVar2.d());
                    if (a(c0088buC)) {
                        String strB = C1818g.b(aAVar2.e());
                        if (aAVar2.b()) {
                            JMenu jMenu2 = new JMenu(strB);
                            a(r2, aAVar2.a(), jMenu2);
                            jMenu.add((JMenuItem) jMenu2);
                        } else {
                            boolean zA = true;
                            try {
                                zA = C1007o.a(aAVar2.aH(), r2);
                            } catch (Exception e2) {
                            }
                            if (zA) {
                                JMenuItem jMenuItem = new JMenuItem(strB);
                                jMenuItem.addActionListener(this);
                                jMenuItem.setActionCommand(aAVar2.d());
                                jMenuItem.setEnabled(C1007o.a(aAVar2.aH(), r2));
                                jMenuItem.setIcon(this.f8682b.a(aAVar2));
                                jMenu.add(jMenuItem);
                                aAVar = aAVar2;
                            }
                        }
                    } else if (c0088buC != null && a(r2, c0088buC)) {
                        JMenu jMenu3 = new JMenu(C1818g.b(aAVar2.e()));
                        jMenu3.setIcon(this.f8682b.a(aAVar2));
                        a(r2, c0088buC, jMenu3);
                        jMenu.add((JMenuItem) jMenu3);
                    } else if (c0088buC == null && a(r2, aAVar2)) {
                        JMenu jMenu4 = new JMenu(C1818g.b(aAVar2.e()));
                        a(r2, aAVar2.a(), jMenu4);
                        jMenu.add((JMenuItem) jMenu4);
                    }
                } else if (aAVar2.c() && aAVar != null && !aAVar.c() && it.hasNext()) {
                    jMenu.addSeparator();
                    aAVar = aAVar2;
                }
            } catch (V.g e3) {
            }
        }
    }

    private void a(G.R r2, C0088bu c0088bu, JMenu jMenu) {
        Iterator itK = c0088bu.K();
        while (itK.hasNext()) {
            C0088bu c0088bu2 = (C0088bu) itK.next();
            if (a(c0088bu2)) {
                try {
                    if (C1007o.a(G.bL.a(r2, c0088bu2.aJ()), r2)) {
                        JMenuItem jMenuItem = new JMenuItem(G.bL.c(r2, c0088bu2.aJ()));
                        jMenuItem.addActionListener(this);
                        jMenuItem.setActionCommand(c0088bu2.aJ());
                        jMenuItem.setIcon(this.f8682b.a(c0088bu2, null, null));
                        jMenu.add(jMenuItem);
                    }
                } catch (V.g e2) {
                    bH.C.a("Fail to set enable condition: " + e2.getLocalizedMessage());
                    Logger.getLogger(C1287O.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            } else if (a(r2, c0088bu2)) {
                a(r2, c0088bu2, jMenu);
            }
        }
    }

    private boolean a(C0088bu c0088bu) {
        int i2 = c0088bu instanceof C0072be ? 1 : c0088bu instanceof C0079bl ? 2 : ((c0088bu instanceof C0088bu) && c0088bu.R() == 3 && c0088bu.S()) ? 1 : ((c0088bu instanceof C0088bu) && (c0088bu.R() == 3 || c0088bu.R() == 4) && c0088bu.T()) ? 2 : -1;
        return (i2 & this.f8680c) == i2;
    }

    private boolean a(G.R r2, G.aA aAVar) {
        C0088bu c0088buC = r2.e().c(aAVar.d());
        if (c0088buC != null && (a(c0088buC) || a(r2, c0088buC))) {
            return true;
        }
        if (!aAVar.b()) {
            return false;
        }
        Iterator itA = aAVar.a();
        while (itA.hasNext()) {
            C0088bu c0088buC2 = r2.e().c(((G.aA) itA.next()).d());
            if (c0088buC2 != null && (a(c0088buC2) || a(r2, c0088buC2))) {
                return true;
            }
        }
        return false;
    }

    private boolean a(G.R r2, C0088bu c0088bu) {
        if (!c0088bu.L()) {
            return false;
        }
        Iterator itK = c0088bu.K();
        while (itK.hasNext()) {
            C0088bu c0088bu2 = (C0088bu) itK.next();
            if (a(c0088bu2)) {
                try {
                    return C1007o.a(G.bL.a(r2, c0088bu2.aJ()), r2);
                } catch (V.g e2) {
                    return true;
                }
            }
            if (c0088bu2.L() && a(r2, c0088bu2)) {
                try {
                    if (C1007o.a(G.bL.a(r2, c0088bu2.aJ()), r2)) {
                        return true;
                    }
                } catch (V.g e3) {
                    bH.C.a(e3.getMessage());
                    e3.printStackTrace();
                }
            }
        }
        return false;
    }

    public void a(ActionListener actionListener) {
        if (this.f8681a.contains(actionListener)) {
            return;
        }
        this.f8681a.add(actionListener);
    }

    public void b(ActionListener actionListener) {
        this.f8681a.remove(actionListener);
    }

    private void a(ActionEvent actionEvent) {
        Iterator it = this.f8681a.iterator();
        while (it.hasNext()) {
            ((ActionListener) it.next()).actionPerformed(actionEvent);
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        a(actionEvent);
    }
}
