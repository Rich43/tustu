package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableLongValue;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/LongConstant.class */
public final class LongConstant implements ObservableLongValue {
    private final long value;

    private LongConstant(long value) {
        this.value = value;
    }

    public static LongConstant valueOf(long value) {
        return new LongConstant(value);
    }

    @Override // javafx.beans.value.ObservableLongValue
    public long get() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public Number getValue2() {
        return Long.valueOf(this.value);
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super Number> observer) {
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super Number> observer) {
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public int intValue() {
        return (int) this.value;
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
