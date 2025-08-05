package ao;

import com.efiAnalytics.ui.BinTableView;
import com.efiAnalytics.ui.C1621de;
import java.awt.Component;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.TextComponent;
import java.awt.Window;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:ao/bK.class */
public class bK implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    C0804hg f5317a;

    /* renamed from: b, reason: collision with root package name */
    int f5318b = -1;

    /* renamed from: c, reason: collision with root package name */
    int f5319c = 0;

    /* renamed from: d, reason: collision with root package name */
    private static bK f5320d = null;

    private bK(C0804hg c0804hg) {
        this.f5317a = null;
        this.f5317a = c0804hg;
    }

    public static bK a() {
        if (f5320d == null) {
            f5320d = new bK(C0804hg.a());
        }
        return f5320d;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.isConsumed() || (keyEvent.getSource() instanceof JComboBox)) {
            return false;
        }
        if (keyEvent.getID() != 401) {
            if (keyEvent.getID() != 402) {
                return false;
            }
            switch (keyEvent.getKeyCode()) {
                case 16:
                    this.f5318b = -1;
                    this.f5319c = 0;
                    break;
            }
            return (keyEvent.getSource() == null || (keyEvent.getSource() instanceof JTextArea) || (keyEvent.getSource() instanceof JTextField) || (keyEvent.getSource() instanceof TextComponent)) ? false : false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getSource() instanceof Component) {
            Window windowB = com.efiAnalytics.ui.bV.b((Component) keyEvent.getSource());
            Frame frameA = com.efiAnalytics.ui.bV.a((Component) keyEvent.getSource());
            if (frameA != null && windowB != null && !frameA.equals(windowB)) {
                return false;
            }
        }
        if (keyEvent.getSource() != null && ((keyEvent.getSource() instanceof JTextArea) || (keyEvent.getSource() instanceof JTextField) || (keyEvent.getSource() instanceof JMenu) || (keyEvent.getSource() instanceof JMenuItem) || (keyEvent.getSource() instanceof JRootPane) || (keyEvent.getSource() instanceof TextComponent))) {
            return false;
        }
        if (keyEvent.isControlDown() && (keyCode == 48 || keyCode == 96)) {
            C0645bi.a().c().t();
            return true;
        }
        if (keyEvent.isControlDown() && keyCode == 117) {
            C0636b.a().a((Window) C0645bi.a().b());
            return true;
        }
        if (keyEvent.isControlDown() && keyCode == 127) {
            InterfaceC0642bf interfaceC0642bfI = C0645bi.a().i();
            if (interfaceC0642bfI == null) {
                return true;
            }
            interfaceC0642bfI.b();
            return true;
        }
        if (!keyEvent.isControlDown()) {
            if (keyEvent.getSource() != null && !(keyEvent.getSource() instanceof C1621de)) {
                switch (keyCode) {
                    case 16:
                        this.f5318b = this.f5317a.p();
                        break;
                    case 33:
                        this.f5317a.z();
                        break;
                    case 34:
                        this.f5317a.w();
                        break;
                    case 35:
                        this.f5317a.o();
                        break;
                    case 36:
                        this.f5317a.n();
                        break;
                    case 113:
                        this.f5317a.C();
                        break;
                    case 114:
                        this.f5317a.b();
                        break;
                    case 127:
                        C0645bi.a().c().c();
                        break;
                }
            }
        } else {
            switch (keyCode) {
                case 32:
                    this.f5317a.k();
                    break;
                case 66:
                    if (this.f5318b != -1) {
                        if (this.f5319c > 0) {
                            C0625ap c0625apC = C0645bi.a().c();
                            int i2 = this.f5318b;
                            int i3 = this.f5318b;
                            int i4 = this.f5319c - 1;
                            this.f5319c = i4;
                            c0625apC.d(i2, i3 + i4);
                            break;
                        } else {
                            this.f5317a.m();
                            this.f5318b--;
                            break;
                        }
                    } else {
                        this.f5317a.m();
                        break;
                    }
                case 68:
                    this.f5317a.h();
                    break;
                case 78:
                    if (this.f5318b != -1) {
                        C0625ap c0625apC2 = C0645bi.a().c();
                        int i5 = this.f5318b;
                        int i6 = this.f5318b;
                        int i7 = this.f5319c + 1;
                        this.f5319c = i7;
                        c0625apC2.d(i5, i6 + i7);
                        break;
                    } else {
                        this.f5317a.l();
                        break;
                    }
                case 80:
                    this.f5317a.e();
                    break;
                case 83:
                    this.f5317a.j();
                    break;
                case 85:
                    this.f5317a.g();
                    break;
            }
        }
        if (keyEvent.getSource() == null || (keyEvent.getSource() instanceof BinTableView) || (keyEvent.getSource() instanceof com.efiAnalytics.ui.eM) || (keyEvent.getSource() instanceof C1621de)) {
            return false;
        }
        switch (keyCode) {
            case 37:
                if (this.f5318b != -1) {
                    if (this.f5319c > 0) {
                        C0625ap c0625apC3 = C0645bi.a().c();
                        int i8 = this.f5318b;
                        int i9 = this.f5318b;
                        int i10 = this.f5319c - 1;
                        this.f5319c = i10;
                        c0625apC3.d(i8, i9 + i10);
                        break;
                    } else {
                        this.f5317a.m();
                        this.f5318b--;
                        break;
                    }
                } else {
                    this.f5317a.m();
                    break;
                }
            case 38:
                this.f5317a.u();
                break;
            case 39:
                if (this.f5318b != -1) {
                    C0625ap c0625apC4 = C0645bi.a().c();
                    int i11 = this.f5318b;
                    int i12 = this.f5318b;
                    int i13 = this.f5319c + 1;
                    this.f5319c = i13;
                    c0625apC4.d(i11, i12 + i13);
                    break;
                } else {
                    this.f5317a.l();
                    break;
                }
            case 40:
                this.f5317a.v();
                break;
        }
        return false;
    }

    public void b() {
        this.f5318b = -1;
    }
}
