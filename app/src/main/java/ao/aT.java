package ao;

/* loaded from: TunerStudioMS.jar:ao/aT.class */
class aT implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f5160a;

    aT(aQ aQVar) {
        this.f5160a = aQVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f5160a.f5152p.setDividerLocation(1.0d);
        this.f5160a.t();
    }
}
