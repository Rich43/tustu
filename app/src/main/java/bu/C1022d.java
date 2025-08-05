package bU;

/* renamed from: bU.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bU/d.class */
public class C1022d implements bT.a {
    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DISCONNEC Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        oVar.a(-1);
    }

    @Override // bT.a
    public int a() {
        return 254;
    }
}
