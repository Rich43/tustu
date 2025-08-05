package sun.misc;

/* loaded from: rt.jar:sun/misc/CRC16.class */
public class CRC16 {
    public int value = 0;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5, types: [int] */
    public void update(byte b2) {
        byte b3 = b2;
        for (int i2 = 7; i2 >= 0; i2--) {
            b3 <<= 1;
            int i3 = (b3 >>> 8) & 1;
            if ((this.value & 32768) != 0) {
                this.value = ((this.value << 1) + i3) ^ 4129;
            } else {
                this.value = (this.value << 1) + i3;
            }
        }
        this.value &= 65535;
    }

    public void reset() {
        this.value = 0;
    }
}
