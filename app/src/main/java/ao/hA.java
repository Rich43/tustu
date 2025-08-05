package ao;

import bH.InterfaceC0993a;

/* loaded from: TunerStudioMS.jar:ao/hA.class */
final class hA implements InterfaceC0993a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f6000a;

    hA(String str) {
        this.f6000a = str;
    }

    @Override // bH.InterfaceC0993a
    public boolean a(String str) {
        return this.f6000a == null || (str != null && str.toLowerCase().contains(this.f6000a.toLowerCase()));
    }
}
