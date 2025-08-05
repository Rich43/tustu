package com.sun.javafx.runtime.async;

/* loaded from: jfxrt.jar:com/sun/javafx/runtime/async/AsyncOperation.class */
public interface AsyncOperation {
    void start();

    void cancel();

    boolean isCancelled();

    boolean isDone();
}
