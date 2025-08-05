package aL;

import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

/* loaded from: TunerStudioMS.jar:aL/e.class */
class e {

    /* renamed from: a, reason: collision with root package name */
    Mixer.Info f2610a;

    /* renamed from: b, reason: collision with root package name */
    Mixer f2611b;

    /* renamed from: c, reason: collision with root package name */
    Line.Info f2612c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ a f2613d;

    e(a aVar, Mixer.Info info, Mixer mixer, Line.Info info2) {
        this.f2613d = aVar;
        this.f2610a = info;
        this.f2611b = mixer;
        this.f2612c = info2;
    }

    public String toString() {
        return this.f2610a.getName();
    }
}
