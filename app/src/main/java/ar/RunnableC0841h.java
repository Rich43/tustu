package ar;

/* renamed from: ar.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ar/h.class */
class RunnableC0841h implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0840g f6237a;

    RunnableC0841h(C0840g c0840g) {
        this.f6237a = c0840g;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f6237a.f6235d && this.f6237a.getSelectedIndex() == this.f6237a.getTabCount() - 1) {
            this.f6237a.b();
            return;
        }
        if (C0839f.a().g() == null || !C0839f.a().g().equals(this.f6237a.getTitleAt(this.f6237a.getSelectedIndex()))) {
            if (C0839f.a().a(this.f6237a.getTitleAt(this.f6237a.getSelectedIndex()))) {
                this.f6237a.f6232a = this.f6237a.getSelectedIndex();
            } else if (this.f6237a.f6232a >= 0) {
                this.f6237a.setSelectedIndex(this.f6237a.f6232a);
            }
        }
    }
}
