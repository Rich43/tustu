package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/* loaded from: TunerStudioMS.jar:ao/bL.class */
public class bL extends MouseAdapter implements MouseMotionListener, MouseWheelListener {

    /* renamed from: c, reason: collision with root package name */
    C0804hg f5323c;

    /* renamed from: a, reason: collision with root package name */
    long f5321a = System.currentTimeMillis();

    /* renamed from: b, reason: collision with root package name */
    int f5322b = 300;

    /* renamed from: d, reason: collision with root package name */
    boolean f5324d = false;

    /* renamed from: e, reason: collision with root package name */
    int f5325e = -1;

    /* renamed from: f, reason: collision with root package name */
    int f5326f = 2;

    /* renamed from: g, reason: collision with root package name */
    boolean f5327g = false;

    public bL(C0804hg c0804hg) {
        this.f5323c = null;
        this.f5323c = c0804hg;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof C0625ap) {
            C0625ap c0625ap = (C0625ap) mouseEvent.getSource();
            c0625ap.requestFocus();
            if (mouseEvent.getButton() != 3 || C0804hg.a().r() == null) {
                return;
            }
            c0625ap.a(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if ((mouseEvent.getSource() instanceof C0625ap) && C0804hg.a().r() != null) {
            C0625ap c0625ap = (C0625ap) mouseEvent.getSource();
            c0625ap.requestFocus();
            if (mouseEvent.getButton() == 1 && (c0625ap.m() || mouseEvent.isShiftDown())) {
                this.f5325e = mouseEvent.getX();
            } else if (mouseEvent.getButton() == 1 && mouseEvent.getY() > c0625ap.getHeight() - C0625ap.f5260B && !c0625ap.m()) {
                this.f5325e = mouseEvent.getX();
                if (mouseEvent.getClickCount() == 1 || c0625ap.m()) {
                    a(mouseEvent.getX(), c0625ap);
                } else if (mouseEvent.getClickCount() == 2) {
                    c0625ap.q();
                }
                this.f5324d = true;
                c0625ap.c(this.f5325e, mouseEvent.getX());
            } else if (mouseEvent.getButton() != 1 && mouseEvent.getButton() == 3) {
                c0625ap.a(mouseEvent.getX(), mouseEvent.getY());
            }
        }
        if (mouseEvent.getButton() == 2) {
            this.f5327g = true;
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        C0625ap c0625apC = C0645bi.a().c();
        if (this.f5324d && (mouseEvent.getSource() instanceof C0625ap)) {
            this.f5325e = -1;
        } else if ((mouseEvent.getSource() instanceof C0625ap) && mouseEvent.getButton() == 1) {
            C0625ap c0625ap = (C0625ap) mouseEvent.getSource();
            if (mouseEvent.getClickCount() == 1 || c0625ap.m() || mouseEvent.isShiftDown()) {
                aP aPVarE = c0625apC.e(mouseEvent.getX(), mouseEvent.getY());
                if (aPVarE != null) {
                    c0625apC.a(aPVarE);
                } else {
                    a(mouseEvent.getX(), c0625ap);
                }
            } else if (mouseEvent.getClickCount() == 2) {
                c0625ap.q();
            }
        }
        this.f5324d = false;
        if (mouseEvent.getButton() == 2) {
            this.f5327g = false;
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof C0625ap) {
            C0625ap c0625ap = (C0625ap) mouseEvent.getSource();
            if (this.f5324d) {
                if (mouseEvent.getX() > this.f5325e + this.f5326f) {
                    c0625ap.c(this.f5325e, mouseEvent.getX());
                    return;
                }
                return;
            }
            if (mouseEvent.getX() >= c0625ap.getWidth() || mouseEvent.getX() < 0) {
                if (System.currentTimeMillis() - this.f5321a > this.f5322b) {
                    if (mouseEvent.getX() > c0625ap.getWidth()) {
                        this.f5323c.w();
                    }
                    if (mouseEvent.getX() < 0) {
                        this.f5323c.z();
                    }
                    this.f5321a = System.currentTimeMillis();
                    return;
                }
                return;
            }
            if (!c0625ap.m()) {
                a(mouseEvent.getX(), c0625ap);
            } else {
                if (this.f5325e <= 0 || Math.abs(this.f5325e - mouseEvent.getX()) <= this.f5326f) {
                    return;
                }
                this.f5324d = true;
            }
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof C0625ap) {
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        double preciseWheelRotation = mouseWheelEvent.getPreciseWheelRotation();
        if (h.i.a(h.i.f12286G, h.i.f12289J).equals(h.i.f12288I) ^ ((mouseWheelEvent.getModifiers() == 2) | this.f5327g)) {
            if (preciseWheelRotation < 0.0d) {
                this.f5323c.y();
                return;
            } else {
                this.f5323c.x();
                return;
            }
        }
        if (preciseWheelRotation < 0.0d) {
            this.f5323c.u();
        } else {
            this.f5323c.v();
        }
    }

    protected void a(int i2, C0625ap c0625ap) {
        if (c0625ap.u()) {
            return;
        }
        this.f5323c.c(c0625ap.e(i2));
        this.f5323c.f();
    }
}
