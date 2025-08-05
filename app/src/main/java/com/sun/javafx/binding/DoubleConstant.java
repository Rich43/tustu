package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/DoubleConstant.class */
public final class DoubleConstant implements ObservableDoubleValue {
    private final double value;

    private DoubleConstant(double value) {
        this.value = value;
    }

    public static DoubleConstant valueOf(double value) {
        return new DoubleConstant(value);
    }

    @Override // javafx.beans.value.ObservableDoubleValue
    public double get() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public Number getValue2() {
        return Double.valueOf(this.value);
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
        return (int) this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public long longValue() {
        return (long) this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public float floatValue() {
        return (float) this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public double doubleValue() {
        return this.value;
    }
}
