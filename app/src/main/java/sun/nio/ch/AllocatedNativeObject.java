package sun.nio.ch;

/* loaded from: rt.jar:sun/nio/ch/AllocatedNativeObject.class */
class AllocatedNativeObject extends NativeObject {
    AllocatedNativeObject(int i2, boolean z2) {
        super(i2, z2);
    }

    synchronized void free() {
        if (this.allocationAddress != 0) {
            unsafe.freeMemory(this.allocationAddress);
            this.allocationAddress = 0L;
        }
    }
}
