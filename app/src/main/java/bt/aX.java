package bt;

import s.C1818g;

/* loaded from: TunerStudioMS.jar:bt/aX.class */
class aX implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aT f8800a;

    aX(aT aTVar) {
        this.f8800a = aTVar;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        String strO;
        String strL = this.f8800a.f8777a.l();
        if (strL != null) {
            strL = C1818g.b(strL);
        }
        if (strL != null && strL.length() == 0) {
            strL = " ";
        }
        G.aM aMVarC = this.f8800a.f8778b.c(this.f8800a.f8777a.b());
        if (aMVarC != null && (strO = aMVarC.o()) != null && !strO.equals("") && this.f8800a.f8785i.getText() != null && !this.f8800a.f8785i.getText().endsWith(strO)) {
            strL = strL + "(" + C1818g.b(strO) + ")";
        }
        this.f8800a.f8785i.setText(strL);
    }
}
