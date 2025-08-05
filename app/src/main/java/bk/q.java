package bk;

import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bk/q.class */
public class q extends JLabel implements InterfaceC1565bc {

    /* renamed from: d, reason: collision with root package name */
    private String f8229d = "SD Logging Active";

    /* renamed from: e, reason: collision with root package name */
    private String f8230e = "SD Logging Idle";

    /* renamed from: a, reason: collision with root package name */
    r f8231a = null;

    /* renamed from: b, reason: collision with root package name */
    boolean f8232b = true;

    /* renamed from: c, reason: collision with root package name */
    boolean f8233c = false;

    public q() throws IllegalArgumentException {
        setText(C1818g.b(this.f8230e));
        setHorizontalAlignment(0);
        setBackground(Color.GREEN);
        setBorder(BorderFactory.createEtchedBorder(1, Color.lightGray, Color.WHITE));
    }

    public void a(boolean z2) throws IllegalArgumentException {
        if (z2 != this.f8233c) {
            if (z2) {
                setText(C1818g.b(this.f8229d));
                a();
            } else {
                setText(C1818g.b(this.f8230e));
                b();
            }
            this.f8233c = z2;
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        if (this.f8231a == null || !this.f8231a.f8234a) {
            return;
        }
        this.f8231a.f8234a = false;
    }

    private void a() {
        b();
        if (!this.f8232b) {
            setOpaque(true);
        } else {
            this.f8231a = new r(this);
            this.f8231a.start();
        }
    }

    private void b() {
        if (this.f8231a != null && this.f8231a.f8234a) {
            this.f8231a.f8234a = false;
        }
        if (this.f8232b) {
            return;
        }
        setOpaque(false);
    }

    public void a(String str) {
        this.f8229d = str;
    }

    @Override // javax.swing.JLabel
    public void setText(String str) throws IllegalArgumentException {
        super.setText(C1818g.b(str));
    }

    public void b(String str) {
        this.f8230e = str;
    }

    public void b(boolean z2) {
        this.f8232b = z2;
        if (this.f8231a != null) {
            this.f8231a.f8234a = false;
        }
    }
}
