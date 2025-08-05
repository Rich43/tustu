package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/IntegerConstant.class */
public final class IntegerConstant implements ObservableIntegerValue {
    private final int value;

    private IntegerConstant(int value) {
        this.value = value;
    }

    public static IntegerConstant valueOf(int value) {
        return new IntegerConstant(value);
    }

    @Override // javafx.beans.value.ObservableIntegerValue
    public int get() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public Number getValue2() {
        return Integer.valueOf(this.value);
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super Number> listener) {
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super Number> listener) {
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public int intValue() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public long longValue() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public float floatValue() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public double doubleValue() {
        return this.value;
    }
}
