package bk;

import G.R;
import bt.C1345d;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.ui.C1679fj;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bk.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bk/a.class */
public class C1176a extends C1345d implements I.n, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    aI.p f8172a;

    /* renamed from: b, reason: collision with root package name */
    JButton f8173b;

    /* renamed from: c, reason: collision with root package name */
    C1679fj f8174c;

    /* renamed from: d, reason: collision with root package name */
    C1679fj f8175d;

    /* renamed from: e, reason: collision with root package name */
    I.o f8176e;

    /* renamed from: f, reason: collision with root package name */
    R f8177f;

    /* renamed from: g, reason: collision with root package name */
    int f8178g;

    /* renamed from: h, reason: collision with root package name */
    C1178c f8179h;

    public C1176a(R r2) {
        this(r2, false);
    }

    public C1176a(R r2, boolean z2) throws V.a {
        this.f8172a = null;
        this.f8173b = null;
        this.f8174c = new C1679fj();
        this.f8175d = null;
        this.f8176e = null;
        this.f8178g = 60000;
        this.f8179h = null;
        this.f8177f = r2;
        this.f8172a = new aI.p(r2);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(C1818g.b("MS3 Real-time Clock")));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 5, 5));
        if (!z2) {
            jPanel.add(new JLabel("Local PC Time:"));
            this.f8175d = new C1679fj();
            this.f8175d.a(new Date());
            jPanel.add(this.f8175d);
            jPanel.add(new JLabel("Current MS3 Time:"));
        }
        jPanel.add(this.f8174c);
        if (this.f8178g <= 1000) {
            this.f8174c.a(false);
        }
        add(BorderLayout.CENTER, jPanel);
        this.f8173b = new JButton("Set MS3 Time to Now");
        this.f8173b.addActionListener(new C1177b(this));
        add("South", this.f8173b);
        b();
        this.f8176e = new I.o(r2);
        this.f8176e.a(this);
        this.f8176e.a("seconds", I.o.f1393b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        try {
            this.f8172a.a(new Date());
            b();
        } catch (RemoteAccessException e2) {
            bV.d(e2.getMessage(), this);
        }
    }

    private synchronized void b() {
        if (this.f8179h != null) {
            notify();
        } else {
            this.f8179h = new C1178c(this);
            this.f8179h.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void c() {
        try {
            this.f8174c.a(this.f8172a.g());
        } catch (RemoteAccessException e2) {
            Logger.getLogger(C1176a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            this.f8174c.a();
        }
        try {
            wait(this.f8178g);
        } catch (InterruptedException e3) {
            Logger.getLogger(C1176a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8176e.a();
        this.f8176e.b(this);
        if (this.f8179h != null) {
            this.f8179h.a();
        }
        if (this.f8175d != null) {
            this.f8175d.a();
        }
        if (this.f8174c != null) {
            this.f8174c.a();
        }
    }

    @Override // I.n
    public void a(String str) {
        b();
    }
}
