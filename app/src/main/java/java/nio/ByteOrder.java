package java.nio;

/* loaded from: rt.jar:java/nio/ByteOrder.class */
public final class ByteOrder {
    private String name;
    public static final ByteOrder BIG_ENDIAN = new ByteOrder("BIG_ENDIAN");
    public static final ByteOrder LITTLE_ENDIAN = new ByteOrder("LITTLE_ENDIAN");

    private ByteOrder(String str) {
        this.name = str;
    }

    public static ByteOrder nativeOrder() {
        return Bits.byteOrder();
    }

    public String toString() {
        return this.name;
    }
}
