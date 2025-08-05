package al;

import am.AbstractC0573a;
import am.C0575c;
import am.C0577e;
import am.g;
import bH.C;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/* renamed from: al.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:al/f.class */
public class C0572f {

    /* renamed from: a, reason: collision with root package name */
    C0577e f4936a;

    /* renamed from: b, reason: collision with root package name */
    C0575c f4937b;

    /* renamed from: g, reason: collision with root package name */
    SeekableByteChannel f4943g;

    /* renamed from: i, reason: collision with root package name */
    int f4945i;

    /* renamed from: j, reason: collision with root package name */
    byte[] f4946j;

    /* renamed from: c, reason: collision with root package name */
    g f4938c = null;

    /* renamed from: d, reason: collision with root package name */
    am.f f4939d = null;

    /* renamed from: k, reason: collision with root package name */
    private long[] f4940k = null;

    /* renamed from: e, reason: collision with root package name */
    int f4941e = 0;

    /* renamed from: f, reason: collision with root package name */
    ByteBuffer f4942f = null;

    /* renamed from: h, reason: collision with root package name */
    long f4944h = -1;

    public C0572f(SeekableByteChannel seekableByteChannel, C0577e c0577e, C0575c c0575c) {
        this.f4943g = seekableByteChannel;
        this.f4936a = c0577e;
        this.f4937b = c0575c;
        this.f4945i = (int) c0575c.f();
        this.f4946j = new byte[this.f4945i];
    }

    public byte[] a() {
        if (this.f4942f == null) {
            this.f4942f = b();
        }
        if (this.f4942f.remaining() < this.f4945i) {
            int iRemaining = this.f4942f.remaining();
            this.f4942f.get(this.f4946j, 0, iRemaining);
            this.f4942f = b();
            if (this.f4942f != null) {
                this.f4942f.get(this.f4946j, iRemaining, this.f4945i - iRemaining);
            } else {
                C.b("Partial Data Block with no next block?");
            }
        }
        if (this.f4942f == null || this.f4942f.remaining() < this.f4946j.length) {
            return null;
        }
        this.f4942f.get(this.f4946j);
        return this.f4946j;
    }

    private ByteBuffer b() throws IOException {
        if (this.f4939d == null) {
            AbstractC0573a abstractC0573aG = this.f4936a.g();
            if (abstractC0573aG instanceof am.f) {
                this.f4939d = (am.f) abstractC0573aG;
                this.f4940k = this.f4939d.e();
                SeekableByteChannel seekableByteChannel = this.f4943g;
                long[] jArr = this.f4940k;
                int i2 = this.f4941e;
                this.f4941e = i2 + 1;
                this.f4938c = g.b(seekableByteChannel, jArr[i2]);
            } else if (abstractC0573aG instanceof g) {
                this.f4938c = (g) abstractC0573aG;
            }
        } else {
            if (this.f4939d == null || this.f4941e >= this.f4940k.length) {
                return null;
            }
            SeekableByteChannel seekableByteChannel2 = this.f4943g;
            long[] jArr2 = this.f4940k;
            int i3 = this.f4941e;
            this.f4941e = i3 + 1;
            this.f4938c = g.b(seekableByteChannel2, jArr2[i3]);
        }
        if (this.f4938c != null) {
            this.f4942f = this.f4938c.a(this.f4943g);
            this.f4942f.position(0);
        }
        return this.f4942f;
    }
}
