package bq;

import bA.f;
import bH.W;
import c.InterfaceC1386e;
import com.efiAnalytics.ui.InterfaceC1581bs;
import com.efiAnalytics.ui.InterfaceC1598ci;
import com.efiAnalytics.ui.InterfaceC1633dr;
import com.efiAnalytics.ui.eJ;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuListener;
import r.C1798a;
import r.C1806i;
import s.InterfaceC1817f;

/* renamed from: bq.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bq/b.class */
public class C1220b extends JButton implements f, InterfaceC1581bs, InterfaceC1817f {

    /* renamed from: c, reason: collision with root package name */
    private boolean f8324c;

    /* renamed from: d, reason: collision with root package name */
    private InterfaceC1598ci f8325d;

    /* renamed from: e, reason: collision with root package name */
    private String f8326e;

    /* renamed from: f, reason: collision with root package name */
    private InterfaceC1386e f8327f;

    /* renamed from: g, reason: collision with root package name */
    private InterfaceC1386e f8328g;

    /* renamed from: h, reason: collision with root package name */
    private JPopupMenu f8329h;

    /* renamed from: i, reason: collision with root package name */
    private int f8330i;

    /* renamed from: a, reason: collision with root package name */
    String f8331a;

    /* renamed from: b, reason: collision with root package name */
    List f8332b;

    public C1220b(String str, boolean z2) {
        super(str);
        this.f8324c = true;
        this.f8325d = null;
        this.f8326e = null;
        this.f8327f = null;
        this.f8328g = null;
        this.f8329h = new JPopupMenu();
        this.f8330i = 0;
        this.f8331a = "";
        this.f8332b = new ArrayList();
        this.f8331a = str;
        a(z2);
        addActionListener(new C1221c(this));
        setHorizontalTextPosition(4);
        setMargin(new Insets(0, 0, 0, 0));
        setFocusable(false);
        super.addActionListener(new C1222d(this));
    }

    @Override // bA.f
    public void a(InterfaceC1598ci interfaceC1598ci) {
        this.f8325d = interfaceC1598ci;
    }

    public void a(boolean z2) {
        this.f8324c = z2;
    }

    @Override // bA.f
    public void c(String str) {
        setIcon(new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource(str)), this, 24)));
    }

    public int b() {
        return this.f8329h.getComponentCount();
    }

    public Component a(int i2) {
        return this.f8329h.getComponent(i2);
    }

    public void b(boolean z2) {
        if (z2) {
            boolean zC = C1798a.a().c(C1798a.f13378bh, C1798a.f13379bi);
            int i2 = 0;
            for (int i3 = 0; i3 < b(); i3++) {
                if (a(i3) instanceof InterfaceC1581bs) {
                    InterfaceC1581bs interfaceC1581bs = (InterfaceC1581bs) a(i3);
                    if (zC) {
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
                } else if (a(i3) instanceof JPopupMenu.Separator) {
                    ((JPopupMenu.Separator) a(i3)).setVisible(i2 != 0);
                    i2 = 0;
                }
                if (a(i3) instanceof bA.c) {
                    bA.c cVar = (bA.c) a(i3);
                    if (cVar.b() != null) {
                        cVar.setState(cVar.b().a());
                    }
                }
            }
        }
        if (g() == 1) {
            this.f8329h.show(this, getWidth(), getHeight() / 2);
        } else {
            this.f8329h.show(this, 0, getHeight());
        }
        this.f8329h.requestFocus();
    }

    public void a(PopupMenuListener popupMenuListener) {
        this.f8329h.addPopupMenuListener(popupMenuListener);
    }

    @Override // bA.f
    public InterfaceC1598ci c() {
        return this.f8325d;
    }

    @Override // bA.f
    public String d() {
        return this.f8326e;
    }

    @Override // bA.f
    public void b(String str) {
        this.f8326e = str;
    }

    private boolean j() {
        if (this.f8325d == null || this.f8326e == null || this.f8326e.equals("")) {
            return true;
        }
        return this.f8325d.a(this.f8326e);
    }

    @Override // javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        if (!(z2 && j()) && isEnabled()) {
            super.setEnabled(false);
        } else if (z2 != isEnabled()) {
            super.setEnabled(z2);
        }
    }

    @Override // bA.f, s.InterfaceC1817f
    public Component getComponent() {
        return this;
    }

    @Override // bA.f
    public void addSeparator() {
        this.f8329h.addSeparator();
    }

    @Override // java.awt.Container, bA.f
    public Component add(Component component) {
        this.f8329h.add(component);
        return component;
    }

    public int g() {
        return this.f8330i;
    }

    public void b(int i2) {
        this.f8330i = i2;
    }

    @Override // bA.f
    public void f() {
        Component[] components = this.f8329h.getComponents();
        for (int length = components.length - 1; length >= 0 && !(components[length] instanceof JMenuItem) && !(components[length] instanceof JMenu); length--) {
            remove(components[length]);
        }
    }

    @Override // javax.swing.AbstractButton
    public void setText(String str) {
        super.setText(str);
        this.f8331a = str;
        k();
    }

    private void k() {
        if (getWidth() >= getPreferredSize().width && (this.f8331a.length() <= 12 || getWidth() > r0.width * 1.5d)) {
            if (super.getText().startsWith("<html>")) {
                super.setText(this.f8331a);
                k();
                return;
            }
            return;
        }
        int mnemonic = super.getMnemonic();
        char c2 = (char) (mnemonic > 96 ? mnemonic - 32 : mnemonic);
        if (this.f8331a.contains(" ")) {
            char c3 = (char) (c2 + ' ');
            int iIndexOf = this.f8331a.indexOf(c2);
            String[] strArrSplit = ((iIndexOf < 0 || iIndexOf >= this.f8331a.indexOf(c3)) ? W.c(this.f8331a, "" + c3, "<u>" + c3 + "</u>") : W.c(this.f8331a, "" + c2, "<u>" + c2 + "</u>")).split(" ");
            String str = "<html><center>";
            int i2 = 0;
            while (i2 < strArrSplit.length) {
                str = ((strArrSplit.length <= 1 || i2 != 0 || strArrSplit[i2].length() > 2) && (strArrSplit.length <= 2 || i2 != 0 || strArrSplit[i2].length() > 5)) ? (strArrSplit.length <= i2 + 1 || strArrSplit[i2 + 1].length() > 2) ? i2 < strArrSplit.length - 1 ? (str + strArrSplit[i2]) + "<br>" : str + strArrSplit[i2] : str + " " + strArrSplit[i2] : str + strArrSplit[i2] + " ";
                i2++;
            }
            super.setText(str + "</center></html>");
        }
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        k();
    }

    @Override // s.InterfaceC1817f
    public void a(String str) {
        super.setText(str);
    }

    @Override // s.InterfaceC1817f
    public String a() {
        return this.f8331a;
    }

    @Override // bA.f
    public void a(InterfaceC1633dr interfaceC1633dr) {
        this.f8332b.add(interfaceC1633dr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        Iterator it = this.f8332b.iterator();
        while (it.hasNext()) {
            ((InterfaceC1633dr) it.next()).a();
        }
    }

    @Override // bA.f
    public boolean h() {
        return !C1806i.a().a("HF;'[PG-05");
    }

    @Override // bA.f, com.efiAnalytics.ui.InterfaceC1581bs
    public InterfaceC1386e i() {
        return this.f8327f;
    }

    @Override // bA.f
    public void b(InterfaceC1386e interfaceC1386e) {
        this.f8327f = interfaceC1386e;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public InterfaceC1386e e() {
        return this.f8328g;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public void a(InterfaceC1386e interfaceC1386e) {
        this.f8328g = interfaceC1386e;
    }
}
