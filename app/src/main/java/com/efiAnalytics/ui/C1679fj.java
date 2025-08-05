package com.efiAnalytics.ui;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import javax.swing.JLabel;

/* renamed from: com.efiAnalytics.ui.fj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fj.class */
public class C1679fj extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    Timer f11673a = new Timer();

    /* renamed from: b, reason: collision with root package name */
    C1680fk f11674b = null;

    /* renamed from: c, reason: collision with root package name */
    long f11675c = 0;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11676d = true;

    public C1679fj() {
        setHorizontalAlignment(0);
    }

    public void a(Date date) {
        if (this.f11676d && this.f11674b == null) {
            this.f11674b = new C1680fk(this);
            this.f11673a.scheduleAtFixedRate(this.f11674b, 0L, 1000L);
        }
        this.f11675c = date.getTime() - System.currentTimeMillis();
        b();
    }

    public void a() {
        if (this.f11674b != null) {
            this.f11674b.cancel();
            this.f11674b = null;
        }
        setText("");
    }

    public void b() {
        long jCurrentTimeMillis = System.currentTimeMillis() + this.f11675c;
        DateFormat dateInstance = DateFormat.getDateInstance(2);
        DateFormat timeInstance = DateFormat.getTimeInstance(2);
        Date date = new Date(jCurrentTimeMillis);
        setText(dateInstance.format(date) + " - " + timeInstance.format(date));
    }

    public void a(boolean z2) {
        this.f11676d = z2;
        if (z2 || this.f11674b == null) {
            return;
        }
        this.f11674b.cancel();
        this.f11674b = null;
    }
}
