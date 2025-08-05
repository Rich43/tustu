package bt;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:bt/aM.class */
class aM extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    boolean f8763a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1303al f8764b;

    aM(C1303al c1303al) {
        this.f8764b = c1303al;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) throws NumberFormatException {
        if (this.f8764b.isEnabled()) {
            if (C1806i.a().a("joijt;i609tr0932")) {
                this.f8763a = (keyEvent.getModifiers() & 64) == 64 || (keyEvent.getModifiers() & 1) == 1;
                this.f8764b.f8861p.g(this.f8763a || ((keyEvent.getModifiers() & 128) == 128 || (keyEvent.getModifiers() & 2) == 2));
            }
            if (keyEvent.getModifiers() == 3) {
                switch (keyEvent.getKeyCode()) {
                    case 37:
                        this.f8764b.f8861p.d();
                        this.f8764b.f8861p.d();
                        this.f8764b.f8861p.d();
                        this.f8764b.f8861p.d();
                        this.f8764b.f8861p.d();
                        keyEvent.consume();
                        break;
                    case 38:
                    case 46:
                    case 160:
                        this.f8764b.f8861p.e(this.f8764b.f8861p.K());
                        keyEvent.consume();
                        break;
                    case 39:
                        this.f8764b.f8861p.c();
                        this.f8764b.f8861p.c();
                        this.f8764b.f8861p.c();
                        this.f8764b.f8861p.c();
                        this.f8764b.f8861p.c();
                        keyEvent.consume();
                        break;
                    case 40:
                    case 44:
                    case 153:
                        this.f8764b.f8861p.f(this.f8764b.f8861p.K());
                        keyEvent.consume();
                        break;
                }
            }
            if (keyEvent.getModifiers() == 2) {
                switch (keyEvent.getKeyCode()) {
                    case 37:
                        this.f8764b.f8861p.d();
                        keyEvent.consume();
                        break;
                    case 38:
                    case 46:
                    case 160:
                        this.f8764b.f8861p.e(1);
                        keyEvent.consume();
                        break;
                    case 39:
                        this.f8764b.f8861p.c();
                        keyEvent.consume();
                        break;
                    case 40:
                    case 44:
                    case 153:
                        this.f8764b.f8861p.f(1);
                        keyEvent.consume();
                        break;
                    case 66:
                        this.f8764b.x();
                        keyEvent.consume();
                        break;
                    case 70:
                        this.f8764b.c(!this.f8764b.f8870y);
                        keyEvent.consume();
                        break;
                }
            }
            switch (keyEvent.getKeyCode()) {
                case 37:
                    this.f8764b.f8861p.c(!this.f8763a);
                    this.f8764b.f8861p.repaint();
                    keyEvent.consume();
                    break;
                case 38:
                case 46:
                case 160:
                    this.f8764b.f8861p.e(1);
                    keyEvent.consume();
                    break;
                case 39:
                    this.f8764b.f8861p.b(!this.f8763a);
                    this.f8764b.f8861p.repaint();
                    keyEvent.consume();
                    break;
                case 40:
                case 44:
                case 153:
                    this.f8764b.f8861p.f(1);
                    keyEvent.consume();
                    break;
                case 61:
                    this.f8764b.u();
                    keyEvent.consume();
                    break;
                case 65:
                    this.f8764b.f();
                    keyEvent.consume();
                    break;
            }
        }
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        this.f8763a = (keyEvent.getModifiers() & 64) == 64 || (keyEvent.getModifiers() & 1) == 1;
        this.f8764b.f8861p.g(this.f8763a || ((keyEvent.getModifiers() & 128) == 128 || (keyEvent.getModifiers() & 2) == 2));
    }
}
