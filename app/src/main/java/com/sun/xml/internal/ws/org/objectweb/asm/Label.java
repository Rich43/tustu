package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/Label.class */
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

    void put(MethodWriter owner, ByteVector out, int source, boolean wideOffset) {
        if ((this.status & 2) == 0) {
            if (wideOffset) {
                addReference((-1) - source, out.length);
                out.putInt(-1);
                return;
            } else {
                addReference(source, out.length);
                out.putShort(-1);
                return;
            }
        }
        if (wideOffset) {
            out.putInt(this.position - source);
        } else {
            out.putShort(this.position - source);
        }
    }

    private void addReference(int sourcePosition, int referencePosition) {
        if (this.srcAndRefPositions == null) {
            this.srcAndRefPositions = new int[6];
        }
        if (this.referenceCount >= this.srcAndRefPositions.length) {
            int[] a2 = new int[this.srcAndRefPositions.length + 6];
            System.arraycopy(this.srcAndRefPositions, 0, a2, 0, this.srcAndRefPositions.length);
            this.srcAndRefPositions = a2;
        }
        int[] iArr = this.srcAndRefPositions;
        int i2 = this.referenceCount;
        this.referenceCount = i2 + 1;
        iArr[i2] = sourcePosition;
        int[] iArr2 = this.srcAndRefPositions;
        int i3 = this.referenceCount;
        this.referenceCount = i3 + 1;
        iArr2[i3] = referencePosition;
    }

    boolean resolve(MethodWriter owner, int position, byte[] data) {
        boolean needUpdate = false;
        this.status |= 2;
        this.position = position;
        int i2 = 0;
        while (i2 < this.referenceCount) {
            int i3 = i2;
            int i4 = i2 + 1;
            int source = this.srcAndRefPositions[i3];
            i2 = i4 + 1;
            int reference = this.srcAndRefPositions[i4];
            if (source >= 0) {
                int offset = position - source;
                if (offset < -32768 || offset > 32767) {
                    int opcode = data[reference - 1] & 255;
                    if (opcode <= 168) {
                        data[reference - 1] = (byte) (opcode + 49);
                    } else {
                        data[reference - 1] = (byte) (opcode + 20);
                    }
                    needUpdate = true;
                }
                data[reference] = (byte) (offset >>> 8);
                data[reference + 1] = (byte) offset;
            } else {
                int offset2 = position + source + 1;
                int reference2 = reference + 1;
                data[reference] = (byte) (offset2 >>> 24);
                int reference3 = reference2 + 1;
                data[reference2] = (byte) (offset2 >>> 16);
                data[reference3] = (byte) (offset2 >>> 8);
                data[reference3 + 1] = (byte) offset2;
            }
        }
        return needUpdate;
    }

    Label getFirst() {
        return this.frame == null ? this : this.frame.owner;
    }

    boolean inSubroutine(long id) {
        return ((this.status & 1024) == 0 || (this.srcAndRefPositions[(int) (id >>> 32)] & ((int) id)) == 0) ? false : true;
    }

    boolean inSameSubroutine(Label block) {
        for (int i2 = 0; i2 < this.srcAndRefPositions.length; i2++) {
            if ((this.srcAndRefPositions[i2] & block.srcAndRefPositions[i2]) != 0) {
                return true;
            }
        }
        return false;
    }

    void addToSubroutine(long id, int nbSubroutines) {
        if ((this.status & 1024) == 0) {
            this.status |= 1024;
            this.srcAndRefPositions = new int[((nbSubroutines - 1) / 32) + 1];
        }
        int[] iArr = this.srcAndRefPositions;
        int i2 = (int) (id >>> 32);
        iArr[i2] = iArr[i2] | ((int) id);
    }

    void visitSubroutine(Label JSR2, long id, int nbSubroutines) {
        if (JSR2 != null) {
            if ((this.status & 1024) != 0) {
                return;
            }
            this.status |= 1024;
            if ((this.status & 256) != 0 && !inSameSubroutine(JSR2)) {
                Edge e2 = new Edge();
                e2.info = this.inputStackTop;
                e2.successor = JSR2.successors.successor;
                e2.next = this.successors;
                this.successors = e2;
            }
        } else if (inSubroutine(id)) {
            return;
        } else {
            addToSubroutine(id, nbSubroutines);
        }
        Edge edge = this.successors;
        while (true) {
            Edge e3 = edge;
            if (e3 != null) {
                if ((this.status & 128) == 0 || e3 != this.successors.next) {
                    e3.successor.visitSubroutine(JSR2, id, nbSubroutines);
                }
                edge = e3.next;
            } else {
                return;
            }
        }
    }

    public String toString() {
        return "L" + System.identityHashCode(this);
    }
}
