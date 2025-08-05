package bi;

import G.C0130m;
import G.C0132o;
import G.R;
import G.da;
import aP.C0338f;
import bH.C;
import bt.C1345d;
import com.efiAnalytics.ui.bV;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/* renamed from: bi.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bi/g.class */
public class C1172g extends C1345d {

    /* renamed from: a, reason: collision with root package name */
    int[] f8159a = {253, 250, 251, 165, 151};

    /* renamed from: b, reason: collision with root package name */
    int[] f8160b = {253, 250, 252, 90, 77};

    /* renamed from: c, reason: collision with root package name */
    int[] f8161c = {253, 250, 253, 195, 183};

    /* renamed from: d, reason: collision with root package name */
    int[] f8162d = {253, 250, 254, 60, 49};

    /* renamed from: e, reason: collision with root package name */
    R f8163e;

    public C1172g(R r2) {
        this.f8163e = r2;
        setLayout(new FlowLayout(1));
        JButton jButton = new JButton("Bootstrap");
        jButton.addActionListener(new C1173h(this));
        add(jButton);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a() {
        if (!this.f8163e.R()) {
            bV.d("Not Connected to ECU!", this);
            return false;
        }
        this.f8163e.C().g(true);
        try {
            Thread.sleep(20L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C1172g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        da daVar = new da();
        C0132o c0132oA = daVar.a(this.f8163e, a(this.f8159a), 60);
        if (c0132oA.a() == 3) {
            C.a("Bootstrap Failed on Write 1! :" + c0132oA.c());
            return false;
        }
        C0132o c0132oA2 = daVar.a(this.f8163e, a(this.f8160b), 60);
        if (c0132oA2.a() == 3) {
            C.a("Bootstrap Failed on Write 2! :" + c0132oA2.c());
            return false;
        }
        C0132o c0132oA3 = daVar.a(this.f8163e, a(this.f8161c), 60);
        if (c0132oA3.a() == 3) {
            C.a("Bootstrap Failed on Write 3! :" + c0132oA3.c());
            return false;
        }
        C0130m c0130mA = a(this.f8162d);
        c0130mA.b(0);
        c0130mA.a(true);
        C0132o c0132oA4 = daVar.a(this.f8163e, c0130mA, 60);
        if (c0132oA4.a() == 3) {
            C.a("Bootstrap Failed on Write 4! :" + c0132oA4.c());
            return false;
        }
        C.d("Bootstrap Command Successfull. Exiting.");
        return C0338f.a().s();
    }

    private C0130m a(int[] iArr) {
        C0130m c0130mA = C0130m.a(this.f8163e.O(), iArr);
        c0130mA.b(3);
        c0130mA.a(true);
        c0130mA.i(7);
        c0130mA.a(30);
        c0130mA.v("Bootstrap command");
        return c0130mA;
    }
}
