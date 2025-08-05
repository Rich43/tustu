package com.intel.bluetooth;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/ThreadLocalWrapper.class */
class ThreadLocalWrapper {
    static boolean java11 = false;
    private Object threadLocal;
    private Object java11Object;

    ThreadLocalWrapper() {
        if (java11) {
            return;
        }
        try {
            this.threadLocal = new ThreadLocal();
        } catch (Throwable th) {
            java11 = true;
        }
    }

    public Object get() {
        if (java11) {
            return this.java11Object;
        }
        return ((ThreadLocal) this.threadLocal).get();
    }

    public void set(Object value) {
        if (java11) {
            this.java11Object = value;
        } else {
            ((ThreadLocal) this.threadLocal).set(value);
        }
    }
}
