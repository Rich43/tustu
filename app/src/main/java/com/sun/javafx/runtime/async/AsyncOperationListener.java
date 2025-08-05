package com.sun.javafx.runtime.async;

/* loaded from: jfxrt.jar:com/sun/javafx/runtime/async/AsyncOperationListener.class */
public interface AsyncOperationListener<V> {
    void onProgress(int i2, int i3);

    void onCompletion(V v2);

    void onCancel();

    void onException(Exception exc);
}
