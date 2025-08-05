package bV;

import G.C0130m;
import G.C0132o;
import G.R;
import G.da;
import bH.C;
import bH.C0995c;
import bN.l;
import bN.t;
import bN.u;
import bT.o;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bV/d.class */
public class d implements h {

    /* renamed from: b, reason: collision with root package name */
    private int f7632b = 0;

    /* renamed from: a, reason: collision with root package name */
    byte[] f7633a = null;

    /* renamed from: c, reason: collision with root package name */
    private int f7634c = -1;

    /* renamed from: d, reason: collision with root package name */
    private int f7635d = -1;

    /* renamed from: e, reason: collision with root package name */
    private boolean f7636e = true;

    /* renamed from: f, reason: collision with root package name */
    private boolean f7637f = false;

    @Override // bT.a
    public int a() {
        return 164;
    }

    @Override // bT.a
    public void a(o oVar, t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:USER_CMD_WRITE_RAW_NEXT Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:USER_CMD_WRITE_RAW_NEXT valid packet data not found.");
        }
        if (bArrC.length < 1) {
            throw new bT.h("PID:USER_CMD_WRITE_RAW_NEXT invalid packet data length, must have atleast 1 byte");
        }
        oVar.d();
        R rF = oVar.f();
        try {
            l lVarB = u.a().b();
            int length = this.f7633a.length - this.f7632b;
            if (bArrC.length > this.f7632b) {
                lVarB.a(254);
                lVarB.b("WriteRaw too many bytes, truncated ".getBytes());
                System.arraycopy(bArrC, 0, this.f7633a, length, this.f7632b);
                this.f7632b = 0;
                C.b("PID:USER_CMD_WRITE_RAW_NEXT received extra bytes, truncated??");
            } else if (bArrC.length <= this.f7632b) {
                System.arraycopy(bArrC, 0, this.f7633a, length, bArrC.length);
                this.f7632b -= bArrC.length;
            }
            if (this.f7632b == 0) {
                C0132o c0132oA = a(rF, this.f7633a);
                if (c0132oA.a() == 3) {
                    lVarB.a(254);
                    String str = "WriteRaw Failed: " + c0132oA.c();
                    lVarB.b(str.getBytes());
                    C.c(str);
                } else {
                    lVarB.a(255);
                    if (c0132oA.e() != null) {
                        lVarB.b(C0995c.a(c0132oA.e()));
                    }
                }
                oVar.a(lVarB);
            }
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public C0132o a(R r2, byte[] bArr) {
        C0130m c0130mA = C0130m.a(r2.O(), C0995c.b(bArr));
        da daVar = new da();
        c0130mA.c(this.f7636e);
        c0130mA.b(this.f7635d);
        c0130mA.a(this.f7634c);
        return daVar.a(r2, c0130mA, 3000);
    }

    void a(int i2) {
        this.f7632b = i2;
    }

    int b() {
        return this.f7632b;
    }

    void a(byte[] bArr) {
        this.f7633a = bArr;
    }

    public void b(int i2) {
        this.f7634c = i2;
    }

    public void c(int i2) {
        this.f7635d = i2;
    }

    public void a(boolean z2) {
        this.f7636e = z2;
    }
}
