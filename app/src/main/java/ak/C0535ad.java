package ak;

import java.util.ArrayList;
import java.util.List;

/* renamed from: ak.ad, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/ad.class */
class C0535ad extends C0541aj implements Comparable {

    /* renamed from: a, reason: collision with root package name */
    int f4690a;

    /* renamed from: b, reason: collision with root package name */
    int f4691b;

    /* renamed from: c, reason: collision with root package name */
    int f4692c;

    /* renamed from: d, reason: collision with root package name */
    int f4693d;

    /* renamed from: e, reason: collision with root package name */
    int f4694e;

    /* renamed from: f, reason: collision with root package name */
    int f4695f;

    /* renamed from: g, reason: collision with root package name */
    int f4696g;

    /* renamed from: h, reason: collision with root package name */
    List f4697h;

    /* renamed from: i, reason: collision with root package name */
    al f4698i;

    /* renamed from: j, reason: collision with root package name */
    int f4699j;

    /* renamed from: k, reason: collision with root package name */
    int f4700k;

    /* renamed from: l, reason: collision with root package name */
    double f4701l;

    /* renamed from: m, reason: collision with root package name */
    final /* synthetic */ C0534ac f4702m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0535ad(C0534ac c0534ac, int i2) {
        super(c0534ac.f4689f.f4683o, i2);
        this.f4702m = c0534ac;
        this.f4700k = 0;
        this.f4701l = 0.0d;
        if (this.f4691b != 0 && this.f4694e > 0) {
            this.f4697h = new ArrayList(this.f4694e);
            int i3 = this.f4691b;
            for (int i4 = 0; i4 < this.f4694e; i4++) {
                C0536ae c0536ae = new C0536ae(this, i3);
                c0536ae.f4718p = i4;
                if (i4 == 0 && this.f4696g > 0) {
                    c0536ae.f4719q = c0536ae.a(0);
                    c0536ae.f4720r = c0536ae.a(this.f4696g - 1);
                }
                this.f4697h.add(c0536ae);
                i3 = c0536ae.f4703a;
            }
        }
        if (this.f4692c != 0) {
            this.f4698i = new al(c0534ac.f4689f.f4683o, this.f4692c);
        }
    }

    @Override // ak.C0541aj
    public String toString() {
        StringBuilder sb = new StringBuilder();
        C0536ae c0536aeB = b();
        sb.append("ChannelGroupBlock{").append(",\n\t recordId=").append(this.f4693d).append(",\n\t commentBlock=").append((Object) this.f4698i).append(",\n\t nChannels=").append(this.f4694e).append(",\n\t nRecords=").append(this.f4696g).append(",\n\t sizeOfRecord=").append(this.f4695f).append(",\n\t samplingRate=").append(c0536aeB.f4711i).append(",\n\t firstTimeValue=").append(c0536aeB.f4719q).append(",\n\t lastTimeValue=").append(c0536aeB.f4720r).append(",\n\t logRecordIdx=").append(this.f4699j).append(",\n\t channelBlocks=");
        for (int i2 = 0; i2 < this.f4697h.size(); i2++) {
            sb.append("\n\t  [").append(i2).append("]=").append(this.f4697h.get(i2));
        }
        sb.append("\n}");
        return sb.toString();
    }

    @Override // java.lang.Comparable
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(C0535ad c0535ad) {
        return ((C0536ae) this.f4697h.get(0)).f4711i < ((C0536ae) c0535ad.f4697h.get(0)).f4711i ? -1 : 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x00c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a() {
        /*
            Method dump skipped, instructions count: 225
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.C0535ad.a():void");
    }

    C0536ae b() {
        C0536ae c0536ae = (C0536ae) this.f4697h.get(0);
        if (c0536ae.f4706d) {
            return c0536ae;
        }
        return null;
    }
}
