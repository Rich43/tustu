package aP;

/* loaded from: TunerStudioMS.jar:aP/bQ.class */
class bQ implements W.aq {

    /* renamed from: a, reason: collision with root package name */
    long f2999a = -1;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bO f3000b;

    bQ(bO bOVar) {
        this.f3000b = bOVar;
    }

    @Override // W.aq
    public void a(double d2) {
        if (this.f2999a == -1) {
            this.f2999a = System.currentTimeMillis();
        }
        this.f3000b.f2987a.a(d2);
    }

    @Override // W.aq
    public void a() {
        StringBuilder sb = new StringBuilder();
        bO bOVar = this.f3000b;
        bOVar.f2995i = sb.append(bOVar.f2995i).append("Transformed ").append(((bR) this.f3000b.f2994h.get(this.f3000b.f2996j)).c()).append(" data log:\nProcessed ").append(this.f3000b.f2989c.x()).append(" records in ").append((System.currentTimeMillis() - this.f2999a) / 1000.0d).append(" s.\nOutput File:\n").append(((bR) this.f3000b.f2994h.get(this.f3000b.f2996j)).b().getAbsolutePath()).append("\n\n").toString();
    }
}
