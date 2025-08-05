package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/Label.class */
public class Label {
    static final int DEBUG = 1;
    static final int RESOLVED = 2;
    static final int RESIZED = 4;
    static final int PUSHED = 8;
    static final int TARGET = 16;
    static final int STORE = 32;
    static final int REACHABLE = 64;
    static final int JSR = 128;
    static final int RET = 256;
    static final int SUBROUTINE = 512;
    static final int VISITED = 1024;
    static final int VISITED2 = 2048;
    public Object info;
    int status;
    int line;
    int position;
    private int referenceCount;
    private int[] srcAndRefPositions;
    int inputStackTop;
    int outputStackMax;
    Frame frame;
    Label successor;
    Edge successors;
    Label next;

    public int getOffset() {
        if ((this.status & 2) == 0) {
            throw new IllegalStateException("Label offset position has not been resolved yet");
        }
        return this.position;
    }

    void put(MethodWriter methodWriter, ByteVector byteVector, int i2, boolean z2) {
        if ((this.status & 2) == 0) {
            if (z2) {
                addReference((-1) - i2, byteVector.length);
                byteVector.putInt(-1);
                return;
            } else {
                addReference(i2, byteVector.length);
                byteVector.putShort(-1);
                return;
            }
        }
        if (z2) {
            byteVector.putInt(this.position - i2);
        } else {
            byteVector.putShort(this.position - i2);
        }
    }

    private void addReference(int i2, int i3) {
        if (this.srcAndRefPositions == null) {
            this.srcAndRefPositions = new int[6];
        }
        if (this.referenceCount >= this.srcAndRefPositions.length) {
            int[] iArr = new int[this.srcAndRefPositions.length + 6];
            System.arraycopy(this.srcAndRefPositions, 0, iArr, 0, this.srcAndRefPositions.length);
            this.srcAndRefPositions = iArr;
        }
        int[] iArr2 = this.srcAndRefPositions;
        int i4 = this.referenceCount;
        this.referenceCount = i4 + 1;
        iArr2[i4] = i2;
        int[] iArr3 = this.srcAndRefPositions;
        int i5 = this.referenceCount;
        this.referenceCount = i5 + 1;
        iArr3[i5] = i3;
    }

    boolean resolve(MethodWriter methodWriter, int i2, byte[] bArr) {
        boolean z2 = false;
        this.status |= 2;
        this.position = i2;
        int i3 = 0;
        while (i3 < this.referenceCount) {
            int i4 = i3;
            int i5 = i3 + 1;
            int i6 = this.srcAndRefPositions[i4];
            i3 = i5 + 1;
            int i7 = this.srcAndRefPositions[i5];
            if (i6 >= 0) {
                int i8 = i2 - i6;
                if (i8 < -32768 || i8 > 32767) {
                    int i9 = bArr[i7 - 1] & 255;
                    if (i9 <= 168) {
                        bArr[i7 - 1] = (byte) (i9 + 49);
                    } else {
                        bArr[i7 - 1] = (byte) (i9 + 20);
                    }
                    z2 = true;
                }
                bArr[i7] = (byte) (i8 >>> 8);
                bArr[i7 + 1] = (byte) i8;
            } else {
                int i10 = i2 + i6 + 1;
                int i11 = i7 + 1;
                bArr[i7] = (byte) (i10 >>> 24);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i10 >>> 16);
                bArr[i12] = (byte) (i10 >>> 8);
                bArr[i12 + 1] = (byte) i10;
            }
        }
        return z2;
    }

    Label getFirst() {
        return this.frame == null ? this : this.frame.owner;
    }

    boolean inSubroutine(long j2) {
        return ((this.status & 1024) == 0 || (this.srcAndRefPositions[(int) (j2 >>> 32)] & ((int) j2)) == 0) ? false : true;
    }

    boolean inSameSubroutine(Label label) {
        if ((this.status & 1024) == 0 || (label.status & 1024) == 0) {
            return false;
        }
        for (int i2 = 0; i2 < this.srcAndRefPositions.length; i2++) {
            if ((this.srcAndRefPositions[i2] & label.srcAndRefPositions[i2]) != 0) {
                return true;
            }
        }
        return false;
    }

    void addToSubroutine(long j2, int i2) {
        if ((this.status & 1024) == 0) {
            this.status |= 1024;
            this.srcAndRefPositions = new int[(i2 / 32) + 1];
        }
        int[] iArr = this.srcAndRefPositions;
        int i3 = (int) (j2 >>> 32);
        iArr[i3] = iArr[i3] | ((int) j2);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void visitSubroutine(jdk.internal.org.objectweb.asm.Label r6, long r7, int r9) {
        /*
            r5 = this;
            r0 = r5
            r10 = r0
        L3:
            r0 = r10
            if (r0 == 0) goto Le3
            r0 = r10
            r11 = r0
            r0 = r11
            jdk.internal.org.objectweb.asm.Label r0 = r0.next
            r10 = r0
            r0 = r11
            r1 = 0
            r0.next = r1
            r0 = r6
            if (r0 == 0) goto L81
            r0 = r11
            int r0 = r0.status
            r1 = 2048(0x800, float:2.87E-42)
            r0 = r0 & r1
            if (r0 == 0) goto L2c
            goto L3
        L2c:
            r0 = r11
            r1 = r0
            int r1 = r1.status
            r2 = 2048(0x800, float:2.87E-42)
            r1 = r1 | r2
            r0.status = r1
            r0 = r11
            int r0 = r0.status
            r1 = 256(0x100, float:3.59E-43)
            r0 = r0 & r1
            if (r0 == 0) goto L95
            r0 = r11
            r1 = r6
            boolean r0 = r0.inSameSubroutine(r1)
            if (r0 != 0) goto L95
            jdk.internal.org.objectweb.asm.Edge r0 = new jdk.internal.org.objectweb.asm.Edge
            r1 = r0
            r1.<init>()
            r12 = r0
            r0 = r12
            r1 = r11
            int r1 = r1.inputStackTop
            r0.info = r1
            r0 = r12
            r1 = r6
            jdk.internal.org.objectweb.asm.Edge r1 = r1.successors
            jdk.internal.org.objectweb.asm.Label r1 = r1.successor
            r0.successor = r1
            r0 = r12
            r1 = r11
            jdk.internal.org.objectweb.asm.Edge r1 = r1.successors
            r0.next = r1
            r0 = r11
            r1 = r12
            r0.successors = r1
            goto L95
        L81:
            r0 = r11
            r1 = r7
            boolean r0 = r0.inSubroutine(r1)
            if (r0 == 0) goto L8d
            goto L3
        L8d:
            r0 = r11
            r1 = r7
            r2 = r9
            r0.addToSubroutine(r1, r2)
        L95:
            r0 = r11
            jdk.internal.org.objectweb.asm.Edge r0 = r0.successors
            r12 = r0
        L9c:
            r0 = r12
            if (r0 == 0) goto Le0
            r0 = r11
            int r0 = r0.status
            r1 = 128(0x80, float:1.8E-43)
            r0 = r0 & r1
            if (r0 == 0) goto Lba
            r0 = r12
            r1 = r11
            jdk.internal.org.objectweb.asm.Edge r1 = r1.successors
            jdk.internal.org.objectweb.asm.Edge r1 = r1.next
            if (r0 == r1) goto Ld6
        Lba:
            r0 = r12
            jdk.internal.org.objectweb.asm.Label r0 = r0.successor
            jdk.internal.org.objectweb.asm.Label r0 = r0.next
            if (r0 != 0) goto Ld6
            r0 = r12
            jdk.internal.org.objectweb.asm.Label r0 = r0.successor
            r1 = r10
            r0.next = r1
            r0 = r12
            jdk.internal.org.objectweb.asm.Label r0 = r0.successor
            r10 = r0
        Ld6:
            r0 = r12
            jdk.internal.org.objectweb.asm.Edge r0 = r0.next
            r12 = r0
            goto L9c
        Le0:
            goto L3
        Le3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.org.objectweb.asm.Label.visitSubroutine(jdk.internal.org.objectweb.asm.Label, long, int):void");
    }

    public String toString() {
        return "L" + System.identityHashCode(this);
    }
}
