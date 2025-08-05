package J;

import G.C0113cs;
import G.InterfaceC0110cp;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:J/j.class */
public class j extends h implements InterfaceC0110cp, Serializable {

    /* renamed from: b, reason: collision with root package name */
    public static String f1471b = "ReSyncedCommsCount";

    /* renamed from: c, reason: collision with root package name */
    public static String f1472c = "wrongCounterCount";

    /* renamed from: d, reason: collision with root package name */
    protected String[] f1473d = {"CTO_PacketsSentCount", "CTO_PacketsReceivedCount", "CTO_PacketTimeout", "CTO_RetryFailedCount", "CTO_RetrySuccessCount", "DTO_PacketsReceivedCount", "wrongCounterCount", "lostPacketCount", "failedPacketCrcCount", "syncCount", "resyncDataStreamCount", "uploadPacketCount", "downloadPacketCount", "burnPageOkCount", "burnPageFailedCount", "controllerBusyCount", "CRC_Cal_Block_Match", "CRC_Cal_Block_No_Match"};

    /* renamed from: e, reason: collision with root package name */
    int f1474e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f1475f = 0;

    /* renamed from: g, reason: collision with root package name */
    int f1476g = 0;

    /* renamed from: h, reason: collision with root package name */
    int f1477h = 0;

    /* renamed from: i, reason: collision with root package name */
    int f1478i = 0;

    /* renamed from: j, reason: collision with root package name */
    int f1479j = 0;

    /* renamed from: k, reason: collision with root package name */
    int f1480k = 0;

    /* renamed from: l, reason: collision with root package name */
    int f1481l = 0;

    /* renamed from: m, reason: collision with root package name */
    int f1482m = 0;

    /* renamed from: n, reason: collision with root package name */
    int f1483n = 0;

    /* renamed from: o, reason: collision with root package name */
    int f1484o = 0;

    /* renamed from: p, reason: collision with root package name */
    int f1485p = 0;

    /* renamed from: q, reason: collision with root package name */
    int f1486q = 0;

    /* renamed from: r, reason: collision with root package name */
    int f1487r = 0;

    /* renamed from: s, reason: collision with root package name */
    int f1488s = 0;

    /* renamed from: t, reason: collision with root package name */
    int f1489t = 0;

    /* renamed from: u, reason: collision with root package name */
    int f1490u = 0;

    /* renamed from: v, reason: collision with root package name */
    int f1491v = 0;

    /* renamed from: w, reason: collision with root package name */
    int f1492w = 0;

    /* renamed from: x, reason: collision with root package name */
    int f1493x = 0;

    /* renamed from: y, reason: collision with root package name */
    int f1494y = 0;

    @Override // G.InterfaceC0110cp
    public String[] s() {
        return this.f1473d;
    }

    public void e() {
        this.f1474e++;
        C0113cs.a().a("CTO_PacketsSentCount", this.f1474e);
    }

    public void f() {
        this.f1475f++;
        x();
    }

    public void g() {
        this.f1476g++;
        x();
    }

    public void h() {
        this.f1477h++;
        x();
    }

    public void i() {
        this.f1478i++;
        x();
    }

    public void j() {
        this.f1479j++;
        C0113cs.a().a("DTO_PacketsSentCount", this.f1479j);
    }

    public void k() {
        this.f1480k++;
        C0113cs.a().a("DTO_PacketsReceivedCount", this.f1480k);
    }

    public void l() {
        this.f1481l++;
        C0113cs.a().a("wrongCounterCount", this.f1481l);
    }

    public void a(int i2) {
        this.f1483n += i2;
        x();
    }

    public void m() {
        this.f1484o++;
        x();
    }

    public void n() {
        this.f1485p++;
        x();
    }

    public void o() {
        this.f1486q++;
        x();
    }

    public void p() {
        this.f1487r++;
        C0113cs.a().a("uploadPacketCount", this.f1487r);
    }

    public void q() {
        this.f1488s++;
        C0113cs.a().a("downloadPacketCount", this.f1488s);
    }

    public void r() {
        this.f1489t++;
        x();
    }

    public void t() {
        this.f1490u++;
        x();
    }

    public void u() {
        this.f1491v++;
        x();
    }

    public void v() {
        this.f1493x++;
        x();
    }

    public void w() {
        this.f1494y++;
        x();
    }

    @Override // J.h
    public void a() {
        this.f1474e = 0;
        this.f1475f = 0;
        this.f1476g = 0;
        this.f1477h = 0;
        this.f1478i = 0;
        this.f1479j = 0;
        this.f1480k = 0;
        this.f1481l = 0;
        this.f1482m = 0;
        this.f1483n = 0;
        this.f1484o = 0;
        this.f1485p = 0;
        this.f1486q = 0;
        this.f1487r = 0;
        this.f1488s = 0;
        this.f1489t = 0;
        this.f1490u = 0;
        this.f1491v = 0;
        this.f1492w = 0;
        this.f1493x = 0;
        this.f1494y = 0;
    }

    @Override // G.InterfaceC0110cp
    public String c() {
        return C0113cs.f1154a;
    }

    public void x() {
        C0113cs.a().a("CTO_PacketsSentCount", this.f1474e);
        C0113cs.a().a("CTO_PacketsReceivedCount", this.f1475f);
        C0113cs.a().a("CTO_PacketTimeout", this.f1476g);
        C0113cs.a().a("CTO_RetryFailedCount", this.f1477h);
        C0113cs.a().a("CTO_RetrySuccessCount", this.f1478i);
        C0113cs.a().a("DTO_PacketsSentCount", this.f1479j);
        C0113cs.a().a("DTO_PacketsReceivedCount", this.f1480k);
        C0113cs.a().a("wrongCounterCount", this.f1481l);
        C0113cs.a().a("missingFrameCount", this.f1482m);
        C0113cs.a().a("lostPacketCount", this.f1483n);
        C0113cs.a().a("failedPacketCrcCount", this.f1484o);
        C0113cs.a().a("syncCount", this.f1485p);
        C0113cs.a().a("resyncDataStreamCount", this.f1486q);
        C0113cs.a().a(f1471b, this.f1492w);
        C0113cs.a().a("uploadPacketCount", this.f1487r);
        C0113cs.a().a("downloadPacketCount", this.f1488s);
        C0113cs.a().a("burnPageOkCount", this.f1489t);
        C0113cs.a().a("burnPageFailedCount", this.f1490u);
        C0113cs.a().a("controllerBusyCount", this.f1491v);
        C0113cs.a().a("CRC_Cal_Block_Match", this.f1493x);
        C0113cs.a().a("CRC_Cal_Block_No_Match", this.f1494y);
    }
}
