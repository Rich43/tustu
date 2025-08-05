package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;

/* renamed from: am.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:am/a.class */
public abstract class AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    private final long f4947b;

    /* renamed from: c, reason: collision with root package name */
    private String f4948c;

    /* renamed from: d, reason: collision with root package name */
    private long f4949d;

    /* renamed from: e, reason: collision with root package name */
    private long f4950e;

    /* renamed from: a, reason: collision with root package name */
    protected final SeekableByteChannel f4951a;

    protected AbstractC0573a(SeekableByteChannel seekableByteChannel, long j2) {
        this.f4951a = seekableByteChannel;
        this.f4947b = j2;
    }

    public long a() {
        return this.f4947b;
    }

    public String b() {
        return this.f4948c;
    }

    protected void a(String str) {
        this.f4948c = str;
    }

    public long c() {
        return this.f4949d;
    }

    protected void a(long j2) {
        this.f4949d = j2;
    }

    public long d() {
        return this.f4950e;
    }

    protected void b(long j2) {
        this.f4950e = j2;
    }

    public String toString() {
        return "BLOCK [pos=" + this.f4947b + ", id=" + this.f4948c + ", length=" + this.f4949d + ", linkCount=" + this.f4950e + "]";
    }

    public int hashCode() {
        return (31 * 1) + ((int) (this.f4947b ^ (this.f4947b >>> 32)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.f4947b == ((AbstractC0573a) obj).f4947b;
    }

    protected static String a(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        return AbstractC0570d.a(byteBufferAllocate, 4);
    }
}
