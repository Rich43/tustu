package com.sun.javafx.jmx;

/* loaded from: jfxrt.jar:com/sun/javafx/jmx/MXNodeAlgorithmContext.class */
public class MXNodeAlgorithmContext {
    private int counter;

    public MXNodeAlgorithmContext() {
        this(0);
    }

    public MXNodeAlgorithmContext(int initCounterValue) {
        this.counter = initCounterValue;
    }

    public int getNextInt() {
        int i2 = this.counter + 1;
        this.counter = i2;
        return i2;
    }
}
