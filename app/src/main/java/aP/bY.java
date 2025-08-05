package aP;

import G.C0053am;
import G.C0088bu;
import G.C0113cs;
import ao.C0804hg;
import bH.C1015w;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import java.awt.Component;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aP/bY.class */
public class bY implements G.S, KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    private static bY f3020a = null;

    /* renamed from: b, reason: collision with root package name */
    private ArrayList f3021b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private ArrayList f3022c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private boolean f3023d = false;

    /* renamed from: e, reason: collision with root package name */
    private boolean f3024e = false;

    /* renamed from: f, reason: collision with root package name */
    private String f3025f = "";

    private bY() {
    }

    public void a(KeyEventDispatcher keyEventDispatcher) {
        this.f3021b.add(keyEventDispatcher);
    }

    public void b(KeyEventDispatcher keyEventDispatcher) {
        this.f3021b.remove(keyEventDispatcher);
    }

    private boolean a(KeyEvent keyEvent) {
        Iterator it = this.f3021b.iterator();
        while (it.hasNext()) {
            if (((KeyEventDispatcher) it.next()).dispatchKeyEvent(keyEvent)) {
                return true;
            }
        }
        return false;
    }

    public static bY a() {
        if (f3020a == null) {
            f3020a = new bY();
        }
        return f3020a;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) throws HeadlessException, NumberFormatException {
        String strA;
        if (a(keyEvent)) {
            return true;
        }
        if (keyEvent.getID() == 401) {
            if (keyEvent.getKeyCode() == 16) {
                this.f3023d = true;
            }
            if (keyEvent.getKeyCode() == 128) {
                this.f3023d = true;
            }
            if (keyEvent.getKeyCode() == 32) {
                C0338f.a().p();
            }
            this.f3025f += keyEvent.getKeyChar();
            a(this.f3025f);
            if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 76 && !ac.r.a() && aE.a.A() != null) {
                C0338f.a().f((Window) cZ.a().c());
                return true;
            }
            if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 75 && ac.r.a()) {
                C0338f.a().g((Window) cZ.a().c());
                return true;
            }
            if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 65 && C0113cs.a().g("dataLogTime") > 0.0d && C0113cs.a().g("dataLoggingActive") == 0.0d) {
                C0338f.a().e();
            }
            if ((keyEvent.getSource() instanceof Component) && ((com.efiAnalytics.ui.bV.b((Component) keyEvent.getSource()) instanceof C0293dh) || (keyEvent.getSource() instanceof C1425x))) {
                if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 39) {
                    bT bTVarH = cZ.a().h();
                    bH.C.c(keyEvent.paramString());
                    bH.C.c("" + keyEvent.getSource());
                    bTVarH.a(true);
                    return true;
                }
                if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 37) {
                    cZ.a().h().b(true);
                    return true;
                }
            }
            if (keyEvent.getKeyCode() == 27) {
                cZ.a().b().u();
            }
            if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 117) {
                C0338f.a().h((Window) cZ.a().c());
            }
            if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 115 && aE.a.A() != null) {
                try {
                    aE.a.A().b();
                    bH.C.d("project.properties saved.");
                } catch (V.a e2) {
                    Logger.getLogger(bY.class.getName()).log(Level.WARNING, "Failed to save project.properties", (Throwable) e2);
                }
            }
            if (keyEvent.getKeyCode() == 116) {
                cZ.a().b().y();
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 121) {
                C0338f.a().z();
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 123) {
                C0338f.a().v();
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 68) {
                G.J.e(!G.J.I());
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 84) {
                new bH.X();
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 70) {
                new C1015w().a(cZ.a().c());
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 67) {
                C0338f.a().e((Frame) cZ.a().c());
            }
            if (aE.a.A() != null && keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 119) {
                C0338f.a().e((Frame) cZ.a().c());
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 80 && G.T.a().c() != null && G.T.a().c().C().D() != null) {
                C0338f.a().C();
            }
            KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (keyEvent.getModifiers() == 11 && keyEvent.getKeyCode() == 122 && (strA = C1798a.a().a(C1798a.cE, (String) null)) != null && (strA.equals("p_tobin@yahoo.com") || strA.equals("philip.tobin@yahoo.com"))) {
                bJ.a(cZ.a().c());
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 122) {
                C0338f.a().D();
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 69) {
                C0338f.a().d();
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 82) {
                C0338f.a().k((Window) cZ.a().c());
            }
            if (keyEvent.getModifiers() == 3 && keyEvent.getKeyCode() == 81) {
                C0338f.a().a((Window) cZ.a().c());
            }
            if (keyEvent.getKeyCode() == 112) {
                C0338f.a().u();
                return true;
            }
        } else if (keyEvent.getID() == 402) {
            this.f3025f = "";
            if (keyEvent.getKeyCode() == 16) {
                this.f3023d = false;
            }
            if (keyEvent.getKeyCode() == 128) {
                this.f3023d = false;
            }
        }
        if (!C1806i.a().a(" OKFDS09IFDSOK") || C0804hg.a().r() == null) {
            return false;
        }
        return ao.bK.a().dispatchKeyEvent(keyEvent);
    }

    private void a(List list) {
        this.f3022c.addAll(list);
    }

    private void b() {
        this.f3022c.clear();
    }

    private void a(String str) {
        Iterator it = this.f3022c.iterator();
        while (it.hasNext()) {
            C0053am c0053am = (C0053am) it.next();
            if (str.equals(c0053am.b()) && c0053am.c().equals("showPanel")) {
                String strA = c0053am.a(0);
                C0088bu c0088buC = c0053am.a().e().c(strA);
                if (c0088buC == null && strA.startsWith("std_")) {
                    C0338f.a().a(strA, "0", cZ.a().c());
                } else if (c0088buC != null) {
                    C0338f.a().a(c0053am.a(), c0088buC, cZ.a().c());
                } else {
                    com.efiAnalytics.ui.bV.d("Attempt to open invalid dialog name", cZ.a().c());
                }
            }
        }
    }

    @Override // G.S
    public void a(G.R r2) {
    }

    @Override // G.S
    public void b(G.R r2) {
        b();
    }

    @Override // G.S
    public void c(G.R r2) {
        if (r2.e().a() != null) {
            a(r2.e().a());
        }
    }
}
