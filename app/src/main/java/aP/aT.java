package aP;

import com.efiAnalytics.apps.ts.dashboard.C1425x;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: TunerStudioMS.jar:aP/aT.class */
public class aT extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    static int f2846a = -1;

    /* renamed from: b, reason: collision with root package name */
    static int f2847b = 150;

    /* renamed from: h, reason: collision with root package name */
    bT f2853h;

    /* renamed from: c, reason: collision with root package name */
    int f2848c = FTPReply.FILE_ACTION_PENDING;

    /* renamed from: d, reason: collision with root package name */
    int f2849d = f2846a;

    /* renamed from: e, reason: collision with root package name */
    long f2850e = f2846a;

    /* renamed from: f, reason: collision with root package name */
    int f2851f = f2846a;

    /* renamed from: g, reason: collision with root package name */
    long f2852g = f2846a;

    /* renamed from: i, reason: collision with root package name */
    boolean f2854i = false;

    public aT(bT bTVar) {
        this.f2853h = bTVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() > 0) {
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        C1425x c1425xE = this.f2853h.e();
        if (c1425xE == null || !c1425xE.L()) {
            a(mouseEvent);
        }
    }

    public void a(MouseEvent mouseEvent) {
        long jCurrentTimeMillis = System.currentTimeMillis() - this.f2852g;
        if (this.f2849d == f2846a || jCurrentTimeMillis <= f2847b) {
            return;
        }
        long x2 = ((mouseEvent.getX() - this.f2851f) * 1000) / jCurrentTimeMillis;
        if (Math.abs(x2) > this.f2848c) {
            if (x2 < 0) {
                this.f2853h.a(true);
            } else {
                this.f2853h.b(true);
            }
            a();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        this.f2849d = mouseEvent.getX();
        this.f2851f = mouseEvent.getX();
        this.f2850e = System.currentTimeMillis();
        this.f2852g = System.currentTimeMillis();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        a();
    }

    public void a() {
        this.f2849d = f2846a;
        this.f2850e = f2846a;
        this.f2851f = f2846a;
        this.f2852g = f2846a;
    }
}
