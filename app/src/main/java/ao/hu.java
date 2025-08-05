package ao;

import java.io.File;

/* loaded from: TunerStudioMS.jar:ao/hu.class */
class hu extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f6138a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ hn f6139b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ hn f6140c;

    hu(hn hnVar, String str, hn hnVar2) {
        this.f6140c = hnVar;
        this.f6138a = str;
        this.f6139b = hnVar2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            hF hFVarA = new hG().a(this.f6138a);
            this.f6140c.f6117f = this.f6138a;
            this.f6140c.f6128q = this.f6138a.substring(this.f6138a.lastIndexOf(".") + 1);
            this.f6140c.a(hFVarA);
            if (this.f6140c.f6113v != null) {
                W.C.a().e(this.f6140c.f6113v);
            }
            this.f6140c.f6113v = new File(this.f6140c.f6117f);
            W.C.a().a(this.f6140c.f6113v, this.f6139b);
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d("Unable to open file:\n" + this.f6138a + "\n \nError Message:\n" + e2.getMessage(), this.f6139b);
        }
    }
}
