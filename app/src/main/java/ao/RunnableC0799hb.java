package ao;

/* renamed from: ao.hb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/hb.class */
class RunnableC0799hb implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gS f6044a;

    RunnableC0799hb(gS gSVar) {
        this.f6044a = gSVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6044a.getParent().doLayout();
        this.f6044a.getParent().validate();
    }
}
