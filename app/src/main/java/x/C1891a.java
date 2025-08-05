package x;

import bA.c;
import bA.e;
import bA.f;
import c.InterfaceC1386e;
import com.efiAnalytics.ui.InterfaceC1581bs;
import com.efiAnalytics.ui.InterfaceC1598ci;
import com.efiAnalytics.ui.InterfaceC1633dr;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import r.C1798a;

/* renamed from: x.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:x/a.class */
public class C1891a extends JMenu implements f, InterfaceC1581bs {

    /* renamed from: c, reason: collision with root package name */
    private boolean f14023c;

    /* renamed from: d, reason: collision with root package name */
    private InterfaceC1598ci f14024d;

    /* renamed from: e, reason: collision with root package name */
    private String f14025e;

    /* renamed from: f, reason: collision with root package name */
    private InterfaceC1386e f14026f;

    /* renamed from: g, reason: collision with root package name */
    private InterfaceC1386e f14027g;

    /* renamed from: a, reason: collision with root package name */
    List f14028a;

    /* renamed from: b, reason: collision with root package name */
    String f14029b;

    /* renamed from: h, reason: collision with root package name */
    private boolean f14030h;

    public C1891a() {
        this("");
    }

    public C1891a(String str, boolean z2) {
        super(str);
        this.f14023c = true;
        this.f14024d = null;
        this.f14025e = null;
        this.f14026f = null;
        this.f14027g = null;
        this.f14028a = new ArrayList();
        this.f14029b = "";
        this.f14030h = false;
        a(z2);
        super.addMouseListener(new C1892b(this));
    }

    public C1891a(String str) {
        this(str, true);
    }

    @Override // bA.f
    public void a(InterfaceC1598ci interfaceC1598ci) {
        this.f14024d = interfaceC1598ci;
    }

    public void a(boolean z2) {
        this.f14023c = z2;
    }

    public boolean b() {
        return C1798a.a().c(C1798a.f13378bh, C1798a.f13379bi);
    }

    @Override // javax.swing.JMenu
    public void setPopupMenuVisible(boolean z2) {
        if (z2) {
            boolean zB = b();
            int i2 = 0;
            for (int i3 = 0; i3 < getItemCount(); i3++) {
                if (getItem(i3) instanceof InterfaceC1581bs) {
                    InterfaceC1581bs interfaceC1581bs = (InterfaceC1581bs) getItem(i3);
                    if (zB) {
                        if (interfaceC1581bs.i() != null) {
                            boolean z3 = interfaceC1581bs.i() == null || interfaceC1581bs.i().a();
                            interfaceC1581bs.setVisible(z3);
                            if (z3) {
                                i2++;
                            }
                        } else {
                            interfaceC1581bs.setVisible(true);
                            i2++;
                        }
                        if (interfaceC1581bs.e() != null) {
                            interfaceC1581bs.setEnabled(interfaceC1581bs.e().a());
                        }
                    } else {
                        boolean z4 = (interfaceC1581bs.i() == null || interfaceC1581bs.i().a()) && (interfaceC1581bs.e() == null || interfaceC1581bs.e().a());
                        interfaceC1581bs.setVisible(z4);
                        if (interfaceC1581bs.e() != null) {
                            interfaceC1581bs.setEnabled(interfaceC1581bs.e().a());
                        }
                        if (z4) {
                            i2++;
                        }
                    }
                }
                if (getItem(i3) instanceof c) {
                    c cVar = (c) getItem(i3);
                    if (cVar.b() != null) {
                        cVar.setState(cVar.b().a());
                    }
                }
            }
        }
        super.setPopupMenuVisible(z2);
    }

    @Override // bA.f
    public InterfaceC1598ci c() {
        return this.f14024d;
    }

    @Override // bA.f
    public String d() {
        return this.f14025e;
    }

    @Override // bA.f
    public void b(String str) {
        this.f14025e = str;
    }

    @Override // bA.f
    public void c(String str) {
    }

    public void a(e eVar) {
        super.remove((JMenuItem) eVar);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public InterfaceC1386e e() {
        return this.f14026f;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public void a(InterfaceC1386e interfaceC1386e) {
        this.f14026f = interfaceC1386e;
    }

    @Override // bA.f
    public void f() {
        Component[] components = getComponents();
        for (int length = components.length - 1; length >= 0 && !(components[length] instanceof JMenuItem) && !(components[length] instanceof JMenu); length--) {
            remove(length);
        }
    }

    @Override // bA.f
    public void a(InterfaceC1633dr interfaceC1633dr) {
        this.f14028a.add(interfaceC1633dr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        Iterator it = this.f14028a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1633dr) it.next()).a();
        }
    }

    public String a() {
        return this.f14029b;
    }

    @Override // javax.swing.AbstractButton
    public void setText(String str) {
        super.setText(str);
        this.f14029b = str;
        if (g()) {
            k();
        }
    }

    private void k() {
        Dimension dimension;
        String str;
        try {
            dimension = getPreferredSize();
        } catch (Exception e2) {
            dimension = new Dimension(1, 1);
        }
        if (getWidth() >= dimension.width && (this.f14029b.length() <= 12 || getWidth() > dimension.width * 1.5d)) {
            if (super.getText().startsWith("<html>")) {
                super.setText(this.f14029b);
                k();
                return;
            }
            return;
        }
        if (this.f14029b.contains(" ")) {
            String[] strArrSplit = this.f14029b.split(" ");
            String str2 = "<html><center>";
            for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                if ((strArrSplit.length > 1 && i2 == 0 && strArrSplit[i2].length() <= 2) || (strArrSplit.length > 2 && i2 == 0 && strArrSplit[i2].length() <= 5)) {
                    str = str2 + strArrSplit[i2] + " ";
                } else if (strArrSplit.length > i2 + 1 && strArrSplit[i2 + 1].length() <= 2) {
                    str = str2 + " " + strArrSplit[i2];
                } else if (i2 < strArrSplit.length - 1) {
                    str = (str2 + strArrSplit[i2]) + "<br>";
                } else {
                    str = str2 + strArrSplit[i2];
                }
                str2 = str;
            }
            super.setText(str2 + "</center></html>");
        }
    }

    public boolean g() {
        return this.f14030h;
    }

    public void b(boolean z2) {
        this.f14030h = z2;
        k();
    }

    @Override // bA.f
    public boolean h() {
        return true;
    }

    @Override // bA.f, com.efiAnalytics.ui.InterfaceC1581bs
    public InterfaceC1386e i() {
        return this.f14027g;
    }

    @Override // bA.f
    public void b(InterfaceC1386e interfaceC1386e) {
        this.f14027g = interfaceC1386e;
    }
}
