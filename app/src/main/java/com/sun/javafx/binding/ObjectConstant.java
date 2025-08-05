package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/ObjectConstant.class */
public class ObjectConstant<T> implements ObservableObjectValue<T> {
    private final T value;

    private ObjectConstant(T value) {
        this.value = value;
    }

    public static <T> ObjectConstant<T> valueOf(T value) {
        return new ObjectConstant<>(value);
    }

    @Override // javafx.beans.value.ObservableObjectValue
    public T get() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableValue
    public T getValue() {
        return this.value;
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super T> observer) {
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super T> observer) {
    }
}
